package nro.services;

import nro.consts.Cmd;
import nro.consts.ConstNpc;
import nro.consts.ConstOption;
import nro.consts.ConstPlayer;
import nro.data.DataGame;
import nro.jdbc.daos.AccountDAO;
import nro.manager.TopManager;
import nro.models.Part;
import nro.models.PartManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.map.dungeon.zones.ZDungeon;
import nro.models.mob.Mob;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.power.Caption;
import nro.power.CaptionManager;
import nro.server.Client;
import nro.server.Manager;
import nro.server.ServerNotify;
import nro.server.SettingGame;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.func.Input;
import nro.utils.*;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import nro.jdbc.DBService;
import nro.jdbc.daos.ShopDAO;
import nro.manager.TopPlayerManager;
import nro.models.auction.AuctionService;
import nro.models.boss.Boss;
import nro.models.boss.BossManager;
import static nro.models.boss.BossManager.BOSSES_IN_GAME;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.item.FlagBag;
import nro.models.player.Referee101;
import nro.models.player.Referee202;
import nro.models.shop.ItemShop;
import nro.models.shop.Shop;
import nro.models.task.TaskMain;
import nro.resources.Resources;
import nro.sendEff.SendEffect;
import static nro.server.Manager.SHOPS;
import nro.services.func.Chonaiday;
import nro.services.func.ChangeMapService;

public class Service {

    private static Service instance;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void sendMessAllPlayer(Message msg) {
        msg.transformData();
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        msg.transformData();
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        msg.transformData();
        if (zone != null) {
            List<Player> players = zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null) {
                        pl.sendMessage(msg);
                    }
                }
            }
            msg.cleanup();
        }
    }

    public void sendQuaySo(Player player, byte type, String number, String result, String finish) {
        Message msg = new Message(-126);
        try {
            msg.writer().writeByte(type);
            switch (type) {
                case 0:
                    msg.writer().writeUTF(number);// so nguoi chs da nhap
                    break;
                case 1:// bat dau xoayyy
                    msg.writer().writeByte(0);// deo de lam gi
                    msg.writer().writeUTF(result);// so dung
                    msg.writer().writeUTF(finish);// send thong bao
                    break;
            }
            player.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            msg.cleanup();
        }
    }

    public void sendMessAllPlayerInMap(Player player, Message msg) {
        msg.transformData();
        if (player.zone != null) {
            if (player.zone.map.isMapOffline) {
                if (player.isPet) {
                    ((Pet) player).master.sendMessage(msg);
                } else {
                    player.sendMessage(msg);
                }
            } else {
                List<Player> players = player.zone.getPlayers();
                synchronized (players) {
                    for (Player pl : players) {
                        if (pl != null) {
                            pl.sendMessage(msg);
                        }
                    }
                }
                msg.cleanup();
            }
        }
    }

    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player.zone != null) {
            List<Player> players = player.zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null && !pl.equals(player)) {
                        pl.sendMessage(msg);
                    }
                }
            }

            msg.cleanup();
        }
    }

    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);// Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(pl.nPoint.hp);
            msg.writer().writeByte(0);// Hiệu ứng Ăn Đậu
            msg.writer().writeInt(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(pl.nPoint.hp);
            msg.writer().writeByte(1);
            msg.writer().writeInt(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void loginDe(Session session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chat(Player player, String text) {
        if (player.getSession() != null && player.isAdmin()) {
            try {
                // 1. Lệnh Buff Item: "buff <id> <quantity>"
                if (text.startsWith("buff")) {
                    String[] parts = text.split(" ");
                    if (parts.length >= 3) {
                        short id = Short.parseShort(parts[1]);
                        int quantity = Integer.parseInt(parts[2]);
                        Item item = ItemService.gI().createNewItem(id, quantity);
                        if (item != null) {
                            InventoryService.gI().addItemBag(player, item, quantity);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Nhận: " + item.template.name);
                        }
                    }
                    return;
                }
                // Dùng phím tắt 'i' cho buff item nhanh
                if (text.startsWith("i ")) {
                    String[] parts = text.split(" ");
                    if (parts.length >= 3) {
                        short id = Short.parseShort(parts[1]);
                        int quantity = Integer.parseInt(parts[2]);
                        Item item = ItemService.gI().createNewItem(id, quantity);
                        if (item != null) {
                            InventoryService.gI().addItemBag(player, item, quantity);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Nhận: " + item.template.name);
                        }
                    }
                    return;
                }

                // 2. Lệnh Menu Admin tổng hợp: "admin"
                if (text.equals("admin")) {
                    String info = "|7|--ADMIN MENU--\n|4|Online: " + Client.gI().getPlayers().size();
                    NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, -1, info,
                            "Bảo trì", "Ngọc rồng", "Đệ tử", "Buff Item", "Đổi hành tinh", "Đóng");
                    return;
                }

                // 3. Lệnh Dịch chuyển Map: "m <mapId>"
                if (text.startsWith("m ")) {
                    int mapId = Integer.parseInt(text.substring(2));
                    ChangeMapService.gI().changeMapInYard(player, mapId, -1, 500);
                    return;
                }

                // 4. Lệnh Set chỉ số nhanh: hp, ki, sd, def, crit
                if (text.startsWith("hp ")) {
                    int hp = Integer.parseInt(text.replace("hp ", ""));
                    player.nPoint.hpg = hp;
                    player.nPoint.calPoint();
                    player.nPoint.hp = player.nPoint.hpMax;
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    Service.getInstance().sendThongBao(player, "Set HP gốc: " + hp);
                    return;
                }
                if (text.startsWith("ki ")) {
                    int ki = Integer.parseInt(text.replace("ki ", ""));
                    player.nPoint.mpg = ki;
                    player.nPoint.calPoint();
                    player.nPoint.mp = player.nPoint.mpMax;
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    Service.getInstance().sendThongBao(player, "Set KI gốc: " + ki);
                    return;
                }
                if (text.startsWith("sd ")) {
                    int sd = Integer.parseInt(text.replace("sd ", ""));
                    player.nPoint.dameg = sd;
                    player.nPoint.calPoint();
                    Service.getInstance().point(player);
                    Service.getInstance().sendThongBao(player, "Set Sức đánh gốc: " + sd);
                    return;
                }
                if (text.startsWith("def ")) {
                    int def = Integer.parseInt(text.replace("def ", ""));
                    player.nPoint.defg = def;
                    player.nPoint.calPoint();
                    Service.getInstance().point(player);
                    Service.getInstance().sendThongBao(player, "Set Giáp gốc: " + def);
                    return;
                }
                if (text.startsWith("crit ")) {
                    int crit = Integer.parseInt(text.replace("crit ", ""));
                    player.nPoint.critg = crit;
                    player.nPoint.calPoint();
                    Service.getInstance().point(player);
                    Service.getInstance().sendThongBao(player, "Set Chí mạng gốc: " + crit);
                    return;
                }

                // 5. Các lệnh Reload Data: loadshop, loadbt, loadvq
                if (text.equals("loadshop")) {
                    Manager.ReloadShop();
                    Service.getInstance().sendThongBao(player, "Reload Shop thành công!");
                    return;
                }
                if (text.equals("loadbt")) {
                    Manager.gI().ReloadBoss();
                    Service.getInstance().sendThongBao(player, "Reload Boss thành công!");
                    return;
                }
                if (text.equals("loadvq")) {
                    Manager.ReloadShop(); // Thường reload shop sẽ reload luôn lucky round
                    Service.getInstance().sendThongBao(player, "Reload Vòng quay thành công!");
                    return;
                }

                // 6. Lệnh thông báo thế giới: "chat <nội dung>"
                if (text.startsWith("chat ")) {
                    String msg = text.replace("chat ", "");
                    HeThongChatGlobal(msg);
                    return;
                }

                // Giữ lại một số lệnh admin cũ hữu ích
                if (text.equals("boss")) {
                    BossManager.gI().showListBoss(player);
                    return;
                }
                if (text.equals("hsk")) {
                    Service.getInstance().releaseCooldownSkill(player);
                    PlayerService.gI().hoiPhuc(player, player.nPoint.hpMax, player.nPoint.mpMax);
                    return;
                }
                if (text.startsWith("up ")) {
                    long power = Long.parseLong(text.replace("up ", ""));
                    addSMTN(player, (byte) 2, power, false);
                    return;
                }
                if (text.startsWith("upp ")) {
                    long power = Long.parseLong(text.replace("upp ", ""));
                    addSMTN(player.pet, (byte) 2, power, false);
                    return;
                }

            } catch (Exception e) {
                Service.getInstance().sendThongBao(player, "Lệnh sai cú pháp hoặc lỗi hệ thống!");
            }
        }

        // Lệnh cho người chơi thường
        if (text.equals("tt")) {
            infoall(player);
            return;
        }

        if (text.startsWith("ten con la ")) {
            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
            return;
        }

        if (player.pet != null) {
            String petText = text.toLowerCase();
            if (petText.equals("di theo") || petText.equals("follow")) {
                player.pet.changeStatus(Pet.FOLLOW);
                return;
            } else if (petText.equals("bao ve") || petText.equals("protect")) {
                player.pet.changeStatus(Pet.PROTECT);
                return;
            } else if (petText.equals("tan cong") || petText.equals("attack")) {
                player.pet.changeStatus(Pet.ATTACK);
                return;
            } else if (petText.equals("ve nha") || petText.equals("go home")) {
                player.pet.changeStatus(Pet.GOHOME);
                return;
            } else if (petText.equals("bien hinh")) {
                player.pet.transform();
                return;
            }
        }

        text = transformText(text);
        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        chatMap(player, text);
    }

    private String transformText(String text) {
        text = text.replaceAll("\\.com", "***")
                .replaceAll("\\.net", "***")
                .replaceAll("\\.xyz", "***")
                .replaceAll("\\.me", "***")
                .replaceAll("\\.pro", "***")
                .replaceAll("\\.mobi", "***")
                .replaceAll("\\.online", "***")
                .replaceAll("\\.info", "***")
                .replaceAll("\\.tk", "***")
                .replaceAll("\\.ml", "***")
                .replaceAll("\\.ga", "***")
                .replaceAll("\\.gq", "***")
                .replaceAll("\\.io", "***")
                .replaceAll("\\.club", "***")
                .replaceAll("chấm com", "***")
                .replaceAll("chấm vn", "***")
                .replaceAll("chấm me", "***")
                .replaceAll("cltx", "***")
                .replaceAll("cl", "***")
                .replaceAll("địt", "***")
                .replaceAll("đụ", "***")
                .replaceAll("cặc", "***")
                .replaceAll("lồn", "***")
                .replaceAll("cộng sản", "***")
                .replaceAll("cộngsản", "***")
                .replaceAll("game rác", "***")
                .replaceAll("cong san", "***")
                .replaceAll("congsan", "***")
                .replaceAll("cặc", "***");
        return text;
    }

    public void infoall(Player player) {
        String info = "|0|-- THÔNG TIN NHÂN VẬT --\n"
                + "|1|Họ tên: " + player.name + "\n"
                + "|1|Sức mạnh: " + Util.formatNumber(player.nPoint.power) + "\n"
                + "|2|Tiềm năng: " + Util.formatNumber(player.nPoint.tiemNang) + "\n"
                + "|7|HP: " + Util.formatNumber(player.nPoint.hp) + "/" + Util.formatNumber(player.nPoint.hpMax) + "\n"
                + "|4|KI: " + Util.formatNumber(player.nPoint.mp) + "/" + Util.formatNumber(player.nPoint.mpMax) + "\n"
                + "|2|Sức đánh: " + Util.formatNumber(player.nPoint.dame) + "\n"
                + "|2|Giáp: " + Util.formatNumber(player.nPoint.def) + "\n"
                + "|2|Chí mạng: " + player.nPoint.crit + "%";
        sendThongBaoOK(player, info);
    }

    public void HeThongChatGlobal(String text) {
        sendThongBaoAllPlayer(text);
    }

    public void chatMap(Player player, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void point(Player player) {
        player.nPoint.calPoint();
        Send_Info_NV(player);
        if (!player.isPet && !player.isBoss) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writer().writeInt(player.nPoint.hpg);
                msg.writer().writeInt(player.nPoint.mpg);
                msg.writer().writeInt(player.nPoint.dameg);
                msg.writer().writeInt(player.nPoint.hpMax);// hp full
                msg.writer().writeInt(player.nPoint.mpMax);// mp full
                msg.writer().writeInt(player.nPoint.hp);// hp
                msg.writer().writeInt(player.nPoint.mp);// mp
                msg.writer().writeByte(player.nPoint.speed);// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writer().writeInt(player.nPoint.dame);// dam base
                msg.writer().writeInt(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong(player.nPoint.tiemNang);
                msg.writer().writeShort(100);
                msg.writer().writeShort(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void player(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(pl.name);
            msg.writer().writeByte(0); // cPK
            msg.writer().writeByte(pl.typePk);
            msg.writer().writeLong(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            // --------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.skills;

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            // ---vang---luong--luongKhoa
            long gold = pl.inventory.getGoldDisplay();
            if (pl.isVersionAbove(214)) {
                msg.writer().writeLong(gold);
            } else {
                msg.writer().writeInt((int) gold);
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            // --------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            // --------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            // --------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            // -----------------
            DataGame.sendHeadAvatar(msg);
            // -----------------
            msg.writer().writeShort(514); // char info id - con chim thông báo
            msg.writer().writeShort(515); // char info id
            msg.writer().writeShort(537); // char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); // nhập thể
            // msg.writer().writeInt(1632811835); //deltatime
            msg.writer().writeInt(333); // deltatime
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); // is new member

            // if (pl.isAdmin()) {
            msg.writer().writeShort(pl.getAura()); // idauraeff
            msg.writer().writeByte(-1);
            // }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

    public void addTV(Player player) {
    // Tùy rule, ở đây ví dụ random từ 1 đến 5 thỏi
    int quantity = 3_000; // TỰ CHỌN SỐ LƯỢNG KHI GỌI

    addTV(player, quantity);
    Service.getInstance().sendThongBao(player, "Bạn nhận được " + quantity + " Thỏi vàng");
}
public void addTV(Player player, int quantity) {
    if (quantity <= 0) {
        return;
    }

    // Kiểm tra còn chỗ trống không
    if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
        Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
        return;
    }

    // Tạo item Thỏi Vàng (id 1429)
    Item thoivang = ItemService.gI().createNewItem((short) 1429);
    thoivang.quantity = quantity;

    // Nếu hệ thống của ông có cơ chế cộng dồn, có thể gộp với stack sẵn có:
    
        InventoryServiceNew.gI().addItemBag(player, thoivang);
        InventoryServiceNew.gI().sendItemBags(player);

    // Gửi thông báo (tùy thích)
     Service.getInstance().sendThongBao(player, "Bạn nhận được " + quantity + " Thỏi vàng");
}

    public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;

            param = master.nPoint.calSubTNSM(param);
            master.nPoint.powerUp(param);
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {

            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }

            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, param);

            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    // public void congTiemNang(Player pl, byte type, int tiemnang) {
    // Message msg;
    // try {
    // msg = new Message(-3);
    // msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
    // msg.writer().writeInt(tiemnang);// số tn cần cộng
    // if (!pl.isPet) {
    // pl.sendMessage(msg);
    // } else {
    // ((Pet) pl).master.nPoint.powerUp(tiemnang);
    // ((Pet) pl).master.nPoint.tiemNangUp(tiemnang);
    // ((Pet) pl).master.sendMessage(msg);
    // }
    // msg.cleanup();
    // switch (type) {
    // case 1:
    // pl.nPoint.tiemNangUp(tiemnang);
    // break;
    // case 2:
    // pl.nPoint.powerUp(tiemnang);
    // pl.nPoint.tiemNangUp(tiemnang);
    // break;
    // default:
    // pl.nPoint.powerUp(tiemnang);
    // break;
    // }
    // } catch (Exception e) {
    //
    // }
    // }
    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    public String getCurrStrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần cấp 11";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần cấp 3";
        } else if (sucmanh < 11100010000000L) {
            return "Thần Huỷ Diệt cấp 1";
        }
        return "Thần Huỷ Diệt cấp 2";
    }

    public void hsChar(Player pl, int hp, int mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }

            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(hp);
            msg.writer().writeInt(mp);
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else {
                ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            }

            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); // cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();

            // Send_Info_NV(pl);
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob);
                    break;
                }
            }
        }
    }

    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();

                msg.writer().writeShort(head);// set head
                msg.writer().writeShort(body);// setbody
                msg.writer().writeShort(leg);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendFlagBag(Player pl) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.getFlagBag());
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTextTime(Player pl, byte id, String name, short time) {
        Message msg;
        try {
            msg = new Message(Cmd.MESSAGE_TIME);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(name);
            msg.writer().writeShort(time);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendFlagBagPet(Pet pet) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt((int) pet.id);
            msg.writer().writeByte(pet.getFlagBag());
            sendMessAllPlayerInMap(pet, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendThongBaoOK(Session session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 1139, text);
    }

    public void sendBigMessAllPlayer(int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendThongBao(Player pl, String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            pl.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            long gold = pl.inventory.getGoldDisplay();
            if (pl.isVersionAbove(214)) {
                msg.writer().writeLong(gold);
            } else {
                msg.writer().writeInt((int) gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
            sendMessAllPlayerIgnoreMe(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public boolean isItemMoney(int type) {
        return type == 9 || type == 10 || type == 34;
    }

    public void useSkillNotFocus(Player pl, Message m) throws IOException {
        byte status = m.reader().readByte();
        if (status == 20) {
            byte SkillID = m.reader().readByte();
            short xPlayer = m.reader().readShort();
            short yPlayer = m.reader().readShort();
            byte dir = m.reader().readByte();
            short x = m.reader().readShort();
            short y = m.reader().readShort();
            SkillService.gI().useSKillNotFocus(pl, SkillID, xPlayer, yPlayer, dir, x, y);
        } else {
            SkillService.gI().useSkill(pl, null, null);
        }
    }

    public void sendThongBaoTopDame(Player plAtt, long dame) {
        if (plAtt != null && !plAtt.isBoss && !plAtt.isPet && dame >= 5000000l && dame > Manager.TOP_DAME) {
            Manager.TOP_DAME = dame;
            try {
                ServerNotify.gI().notify(plAtt.name + " vừa đánh chiêu "
                        + plAtt.playerSkill.skillSelect.template.name + " gây " + Util.formatNumber(dame)
                        + " sát thương");
            } catch (Exception e) {
                Log.warning("Loi thong bao top dame");
                // TODO: handle exception
            }
        }

    }

    public void chatGlobal(Player pl, String text) {
        if (pl.inventory.getGem() >= 5) {
            if (pl.isAdmin() || Util.canDoWithTime(pl.lastTimeChatGlobal, 180000)) {
                if (pl.isAdmin() || pl.nPoint.power > 2000000000) {
                    pl.inventory.subGem(5);
                    sendMoney(pl);
                    pl.lastTimeChatGlobal = System.currentTimeMillis();
                    Message msg;
                    try {
                        msg = new Message(92);
                        msg.writer().writeUTF(pl.name);
                        msg.writer().writeUTF("|5|" + text);
                        msg.writer().writeInt((int) pl.id);
                        msg.writer().writeShort(pl.getHead());
                        msg.writer().writeShort(pl.getBody());
                        msg.writer().writeShort(pl.getFlagBag()); // bag
                        msg.writer().writeShort(pl.getLeg());
                        msg.writer().writeByte(0);
                        sendMessAllPlayer(msg);
                        msg.cleanup();
                    } catch (Exception e) {
                    }
                } else {
                    sendThongBao(pl, "Sức mạnh phải ít nhất 2tỷ mới có thể chat thế giới");
                }
            } else {
                sendThongBao(pl, "Không thể chat thế giới lúc này, vui lòng đợi "
                        + TimeUtil.getTimeLeft(pl.lastTimeChatGlobal, 120));
            }
        } else {
            sendThongBao(pl, "Không đủ ngọc chat thế giới");
        }
    }

    private int tiLeXanhDo = 3;

    public int xanhToDo(int n) {
        return n * tiLeXanhDo;
    }

    public int doToXanh(int n) {
        return (int) n / tiLeXanhDo;
    }

    public static final int[] flagTempId = {363, 364, 365, 366, 367, 368, 369, 370, 371, 519, 520, 747};
    public static final int[] flagIconId = {2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331, 4386, 4385, 2325};

    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeShort(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changeFlag(Player pl, int index) {
        Message msg;
        try {
            pl.cFlag = (byte) index;
            msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
                msg.writer().writeByte(index);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();
            }
            pl.lastTimeChangeFlag = System.currentTimeMillis();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendFlagPlayerToMe(Player me, Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.cFlag);
            msg.writer().writeShort(flagIconId[pl.cFlag]);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chooseFlag(Player pl, int index) {
        if (Util.canDoWithTime(pl.lastTimeChangeFlag, 60000)) {
            if (!MapService.gI().isMapBlackBallWar(pl.zone.map.mapId)
                    && !MapService.gI().isMapBH(pl.zone.map.mapId)
                    && !MapService.gI().isMapMabuWar(pl.zone.map.mapId) && !pl.isHoldBlackBall) {
                changeFlag(pl, index);
            } else {
                sendThongBao(pl, "Không thể đổi cờ ở khu vực này");
            }
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi " + TimeUtil.getTimeLeft(pl.lastTimeChangeFlag, 60)
                    + " nữa!");
        }
    }

    public void attackPlayer(Player pl, int idPlAnPem) {
        SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null);
    }

    public void openZoneUI(Player pl) {
        if (pl.zone == null || pl.zone.map.isMapOffline) {
            sendThongBaoOK(pl, "Không thể đổi khu vực trong map này");
            return;
        }
        int mapid = pl.zone.map.mapId;
        if (!pl.isAdmin()) {
            if (mapid == 188) {
                if (pl.zone.getNumOfPlayers() > 1) {
                    sendThongBaoOK(pl, "Bạn phải hạ ngục hết chiến binh mới được đổi khu");
                    return;
                }
            } else if (MapService.gI().isMapDoanhTrai(mapid) || MapService.gI().isMapBanDoKhoBau(mapid)
                    || mapid == 120 || MapService.gI().isMapVS(mapid) || mapid == 126 || pl.zone instanceof ZDungeon) {
                sendThongBaoOK(pl, "Không thể đổi khu vực trong map này");
                return;
            }
        }
        Message msg;
        try {
            msg = new Message(29);
            msg.writer().writeByte(pl.zone.map.zones.size());
            for (Zone zone : pl.zone.map.zones) {
                msg.writer().writeByte(zone.zoneId);
                int numPlayers = zone.getNumOfPlayers();
                msg.writer().writeByte((numPlayers < 5 ? 0 : (numPlayers < 8 ? 1 : 2)));
                msg.writer().writeByte(numPlayers);
                msg.writer().writeByte(zone.maxPlayer);
                msg.writer().writeByte(0);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

//    public void buffItem(Player player, String playerName, String itemIds, String quantity, String optionsData) {
//        try {
//            if (Integer.parseInt(quantity) <= 0) {
//                getInstance().sendThongBao(player, "Số lượng không hợp lệ!");
//            }
//
//            Player plGame = null;
//            if (playerName.equals("-1")) {
//                plGame = player;
//            } else {
//                plGame = Client.gI().getPlayer(playerName);
//            }
//
//            if (plGame == null) {
//                getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc offline");
//                return;
//            }
//
//            List<Integer> listItemId = new ArrayList();
//            String[] spl_1 = itemIds.split("\\.");
//
//            for (String str : spl_1) {
//                String[] spl_2 = str.split("-");
//                if (spl_2.length == 2) {
//                    int from = Integer.parseInt(spl_2[0]);
//                    int to = Integer.parseInt(spl_2[1]);
//
//                    for (int i = from; i <= to; ++i) {
//                        listItemId.add(i);
//                    }
//                } else {
//                    listItemId.add(Integer.parseInt(str));
//                }
//            }
//
//            for (Integer id : listItemId) {
//                if (InventoryServiceNew.gI().getCountEmptyBag(plGame) == 0) {
//                    getInstance().sendThongBao(player, "Hành trang ngươi chơi " + playerName + " đã đầy");
//                    return;
//                }
//
//                Item item = ItemService.gI().createNewItem(id, Integer.parseInt(quantity));
//                if (!optionsData.equals("-1")) {
//                    String[] subOptions = optionsData.split("-");
//
//                    for (String so : subOptions) {
//                        String[] optionData = so.split("\\.");
//                        int optionId = Integer.parseInt(optionData[0]);
//                        int param = 1;
//                        if (optionData.length == 2) {
//                            param = Integer.parseInt(optionData[1]);
//                        }
//                        item.itemOptions.add(new ItemOption(optionId, param));
//                    }
//                }
//
//                InventoryServiceNew.gI().addItemBag(plGame, item);
//                InventoryServiceNew.gI().sendItemBags(plGame);
//                getInstance().sendThongBao(plGame, "Bạn vừa nhận được " + item.template.name);
//                String textLog = player.name + "\n" + itemIds + " - " + item.template.name + "(" + quantity + ")\n";
//
//                for (ItemOption io : item.itemOptions) {
//                    textLog = textLog + "** " + io.getOptionString() + "\n";
//                }
//            }
//
//            getInstance().sendThongBao(player, "Cung vật phẩm thành công!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            getInstance().sendThongBao(player, "Thông tin không hợp lệ");
//        }
//
//    }

    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                long leftTime = skill.lastTimeUseThisSkill - System.currentTimeMillis() + skill.coolDown;
                if (leftTime < 0) {
                    leftTime = 0;
                }
                leftTime = pl.nPoint.calLimit(leftTime);
                msg.writer().writeInt((int) leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                msg.writer().writeShort(skill.skillId);

                long timeLeft = skill.lastTimeUseThisSkill - System.currentTimeMillis() + skill.coolDown;
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                timeLeft = pl.nPoint.calLimit(timeLeft);
                msg.writer().writeInt((int) timeLeft);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(getClass(), e, "Loi send time skill");
        }
    }

    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt((int) item.playerId);//
            if (item.playerId == -2) {
                msg.writer().writeShort(item.range);
            }
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt((int) item.playerId);//
            if (item.playerId == -2) {
                msg.writer().writeShort(item.range);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    
    
    public void showInfoPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());

                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());

                        List<ItemOption> itemOptions = item.getDisplayOptions();
                        int countOption = itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeShort(iop.param);
                        }
                    }
                }

                msg.writer().writeInt(pl.pet.nPoint.hp); // hp
                msg.writer().writeInt(pl.pet.nPoint.hpMax); // hpfull
                msg.writer().writeInt(pl.pet.nPoint.mp); // mp
                msg.writer().writeInt(pl.pet.nPoint.mpMax); // mpfull
                msg.writer().writeInt(pl.pet.nPoint.dame); // damefull
                msg.writer().writeUTF(pl.pet.name); // name
                msg.writer().writeUTF(getCurrStrLevel(pl.pet)); // curr level
                msg.writer().writeLong(pl.pet.nPoint.power); // power
                msg.writer().writeLong(pl.pet.nPoint.tiemNang); // tiềm năng
                msg.writer().writeByte(pl.pet.getStatus()); // status
                msg.writer().writeShort(pl.pet.nPoint.stamina); // stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); // stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); // crit
                msg.writer().writeShort(pl.pet.nPoint.def); // def
                int sizeSkill = pl.pet.playerSkill.skills.size();
                msg.writer().writeByte(4); // counnt pet skill
                for (int i = 0; i < pl.pet.playerSkill.skills.size(); i++) {
                    if (pl.pet.playerSkill.skills.get(i).skillId != -1) {
                        msg.writer().writeShort(pl.pet.playerSkill.skills.get(i).skillId);
                    } else {
                        if (i == 1) {
                            msg.writer().writeShort(-1);
                            msg.writer().writeUTF("Cần đạt sức mạnh 150 triệu để mở");
                        } else if (i == 2) {
                            msg.writer().writeShort(-1);
                            msg.writer().writeUTF("Cần đạt sức mạnh 1 tỷ 5 để mở");
                        } else if (i == 3) {
                            msg.writer().writeShort(-1);
                            msg.writer().writeUTF("Cần đạt sức mạnh 20 tỷ để mở");
                        }
                    }
                }

                pl.sendMessage(msg);
                msg.cleanup();

            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void sendChiSoPetGoc(Player pl) {
        if (pl == null || pl.pet == null) {
            return;
        }

        try {
            Message msg = new Message(-109);
            msg.writer().writeInt(pl.pet.nPoint.hpg);
            msg.writer().writeInt(pl.pet.nPoint.mpg);
            msg.writer().writeInt(pl.pet.nPoint.dameg);
            msg.writer().writeShort(pl.pet.nPoint.defg);
            msg.writer().writeByte(pl.pet.nPoint.critg);

            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }
    // public void sendItemTime(Player pl, int itemId, int time) {
    // Message msg;
    // try {
    // msg = new Message(-106);
    // msg.writer().writeShort(itemId);
    // msg.writer().writeShort(time);
    // pl.sendMessage(msg);
    // } catch (Exception e) {
    // }
    // }
    // public void removeItemTime(Player pl, int itemTime) {
    // sendItemTime(pl, itemTime, 0);
    // }
    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(speed != -1 ? speed : pl.nPoint.speed);
            pl.sendMessage(msg);
            // Service.getInstance().sendMessAllPlayerInMap(pl.map, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.getInstance().getCurrStrLevel(pl));
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin()) {
                SubMenuService.gI().showMenuForAdmin(player);
            }
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void subMenuPlayer(Player player) {
        Message msg;
        try {
            msg = messageSubCommand((byte) 63);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("String 1");
            msg.writer().writeUTF("String 2");
            msg.writer().writeShort(550);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chatPrivate(Player plChat, Player plReceive, String text) {
        String s1 = text.length() > 100 ? text.substring(0, 100) : text;
        Message msg;
        try {
            msg = new Message(92);

            msg.writer().writeUTF(plChat.name);
            String s = s1;
            msg.writer().writeUTF("|5|" + s);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            if (plChat.getSession().version > 214) {
                msg.writer().writeShort(-1);
            }
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag());
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 6) {
                if (newPass.equals(rePass)) {
                    player.getSession().pp = newPass;
                    AccountDAO.updateAccount(player.getSession());
                    Service.getInstance().sendThongBao(player, "Đổi mật khẩu thành công!");
                } else {
                    Service.getInstance().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Mật khẩu ít nhất 6 ký tự!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    public void switchToCreateChar(Session session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendCaption(Session session, byte gender) {
        Message msg;
        try {
            List<Caption> captions = CaptionManager.getInstance().getCaptions();
            msg = new Message(-41);
            msg.writer().writeByte(captions.size());
            for (Caption caption : captions) {
                msg.writer().writeUTF(caption.getCaption(gender));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendWaitToLogin(Session session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendMessage(Session session, int cmd, String path) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile(path));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTopRank(Player pl) {
        Message msg;
        try {
            msg = new Message(Cmd.THELUC);
            msg.writer().writeInt(1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg = null;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendPowerInfo(Player pl, String info, short point) {
        Message m = null;
        try {
            m = new Message(-115);
            m.writer().writeUTF(info);
            m.writer().writeShort(point);
            m.writer().writeShort(20);
            m.writer().writeShort(10);
            m.writer().flush();
            if (pl != null && pl.getSession() != null) {
                pl.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void setMabuHold(Player pl, byte type) {
        Message m = null;
        try {
            m = new Message(52);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void sendPercentMabuEgg(Player player, byte percent) {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(percent);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPlayerInfo(Player player) {
        try {
            Message msg = messageSubCommand((byte) 7);
            msg.writer().writeInt((int) player.id);
            if (player.clan != null) {
                msg.writer().writeInt(player.clan.id);
            } else {
                msg.writer().writeInt(-1);
            }
            int level = CaptionManager.getInstance().getLevel(player);
            level = player.isInvisible ? 0 : level;
            msg.writer().writeByte(level);
            msg.writer().writeBoolean(player.isInvisible);
            msg.writer().writeByte(player.typePk);
            msg.writer().writeByte(player.gender);
            msg.writer().writeByte(player.gender);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeUTF(player.name);
            msg.writer().writeInt(player.nPoint.hp);
            msg.writer().writeInt(player.nPoint.hpMax);
            msg.writer().writeShort(player.getBody());
            msg.writer().writeShort(player.getLeg());
            msg.writer().writeByte(player.getFlagBag());
            msg.writer().writeByte(-1);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(0);

            // msg.writer().writeShort(0);
            // msg.writer().writeByte(0);
            // msg.writer().writeShort(0);
            sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCurrLevel(Player pl) {

    }

    public int getWidthHeightImgPetFollow(int id) {
        if (id == 15067) {
            return 65;
        }
        return 75;
    }

//    public void showTopThoiVang(Player player) {
//        List<Player> list = TopManager.getInstance().getListTopThoiVang();
//        Message msg = new Message(Cmd.TOP);
//        try {
//            msg.writer().writeByte(0);
//            msg.writer().writeUTF("Top Tiêu Sài : ");
//            msg.writer().writeByte(list.size());
//            for (int i = 0; i < list.size(); i++) {
//                Player pl = list.get(i);
//                msg.writer().writeInt(i + 1);
//                msg.writer().writeInt((int) pl.id);
//                msg.writer().writeShort(pl.getHead());
//                if (player.isVersionAbove(220)) {
//                    Part part = PartManager.getInstance().find(pl.getHead());
//                    msg.writer().writeShort(part.getIcon(0));
//                }
//                msg.writer().writeShort(pl.getBody());
//                msg.writer().writeShort(pl.getLeg());
//                msg.writer().writeUTF(pl.name);
//                msg.writer().writeUTF("Thỏi : " + pl.pointThoiVang);
//                msg.writer().writeUTF("Thỏi : " + pl.pointThoiVang);
//            }
//            player.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    public void showTopSktrungthu(Player player) {
        List<Player> players = TopPlayerManager.GetTopSktrungthu();
        if (players.isEmpty()) {
            this.sendThongBao(player, "Không có top nào cả");
            return;
        }
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top sự kiện");
            msg.writer().writeByte(players.size());
            for (int i = 0; i < players.size(); i++) {
                Player pl = players.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Client.gI().getPlayer(pl.id) != null ? "Online" : "");
                msg.writer().writeUTF("Tổng điểm : " + pl.RuongbauPoint);
            }
            player.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            msg.cleanup();
        }
    }
    
    
    public void showTopSK(Player player) {
        List<Player> list = TopManager.getInstance().getListTopEvent();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Sự kiện !");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Điểm : " + pl.pointSK);
                msg.writer().writeUTF("Điểm : " + pl.pointSK);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopVongQuay(Player player) {
        List<Player> list = TopManager.getInstance().getListTopGapthu();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top vòng quay");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Điểm : " + pl.GapthuPoint);
                msg.writer().writeUTF("Điểm : " + pl.GapthuPoint);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopPauCua(Player player) {
        List<Player> list = TopManager.getInstance().getListTopBauCua();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Thăm quan Muôn Thú ");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Util.numberToMoney(pl.PauCuaPoint) + " Lượt");
                msg.writer().writeUTF("Số lần thăm quan : " + Util.numberToMoney(pl.PauCuaPoint));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Showtopboss(Player player) {
        System.err.println("load1");
        List<Player> list = TopManager.getInstance().getListTopboss();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Điểm Săn Boss");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Util.numberToMoney(pl.bosspoint) + " Lượt");
                msg.writer().writeUTF("Số lần thăm quan : " + Util.numberToMoney(pl.bosspoint));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopNap(Player player) {
        List<Player> players = TopPlayerManager.GetTopNap();
        if (players.isEmpty()) {
            this.sendThongBao(player, "Không có top nào cả");
            return;
        }
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Nạp");
            msg.writer().writeByte(players.size());
            for (int i = 0; i < players.size(); i++) {
                Player pl = players.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Client.gI().getPlayer(pl.id) != null ? "Online" : "");
                msg.writer().writeUTF("Tổng Nạp: " + pl.tongNap);
            }
            player.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            msg.cleanup();
        }
    }

    public void showTopClanPoint(Player player) {
        List<Clan> clans = TopPlayerManager.GetTopClanPoint();
        if (clans.isEmpty()) {
            this.sendThongBao(player, "Không có top bang hội nào cả");
            return;
        }
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Bang Hội");
            msg.writer().writeByte(clans.size());
            for (int i = 0; i < clans.size(); i++) {
                Clan clan = clans.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt(clan.id);
                msg.writer().writeShort(clan.imgId);
                if (player.isVersionAbove(220)) {
                    FlagBag fb = FlagBagService.gI().getFlagBag(clan.imgId);
                    msg.writer().writeShort(fb != null ? fb.iconId : -1); // Use clan flag icon
                }
                msg.writer().writeShort(-1); // No body for clan
                msg.writer().writeShort(-1); // No leg for clan
                msg.writer().writeUTF(clan.name);
                msg.writer().writeUTF("Point: " + clan.clanPoint); // No online status for clan
                msg.writer().writeUTF("Phần thưởng: NỊT");
            }
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            this.sendThongBao(player, "Có lỗi khi tải top bang hội, vui lòng thử lại sau!");
        } finally {
            msg.cleanup();
        }
    }

    public void showTopBossp(Player player) {
        List<Player> players = TopPlayerManager.GetTopBOSS();
        if (players.isEmpty()) {
            this.sendThongBao(player, "Không có top nào cả");
            return;
        }
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Boss");
            msg.writer().writeByte(players.size());
            for (int i = 0; i < players.size(); i++) {
                Player pl = players.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Client.gI().getPlayer(pl.id) != null ? "Online" : "");
                msg.writer().writeUTF("Tổng điểm : " + pl.bosspoint);
            }
            player.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            msg.cleanup();
        }
    }

    public void showTopSaiTv(Player player) {
        List<Player> players = TopPlayerManager.GetTopSaitv();
        if (players.isEmpty()) {
            this.sendThongBao(player, "Không có top nào cả");
            return;
        }
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Sài\n Thỏi Vàng");
            msg.writer().writeByte(players.size());
            for (int i = 0; i < players.size(); i++) {
                Player pl = players.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Client.gI().getPlayer(pl.id) != null ? "Online" : "");
                msg.writer().writeUTF("Tổng TV tiêu : " + pl.pointThoiVang);
            }
            player.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            msg.cleanup();
        }
    }

    public void showtopEvent(Player player) {
        List<Player> list = TopManager.getInstance().getListTopEvent();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Giáng Sinh");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Util.numberToMoney(pl.event.getEventPoint()) + " điểm");
                msg.writer().writeUTF("Điểm : " + Util.numberToMoney(pl.event.getEventPoint()));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopPower(Player player) {
        List<Player> list = TopManager.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Sức Mạnh");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Tổng : " + Util.numberToMoney(pl.nPoint.tiemNang));
                String name = " Đệ tử: ";
                name += pl.pet != null ? ("" + Util.numberToMoney(pl.pet.nPoint.power)) : "0";
                name += "\nSư phụ: " + Util.numberToMoney(pl.nPoint.power);
                msg.writer().writeUTF(name);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopNVU(Player player) {
        List<Player> list = TopManager.getInstance().getListTopNV();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Sức Mạnh");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(Client.gI().getPlayer(pl.id) != null ? "Online" : "");

                TaskMain task = TaskService.gI().getTaskMainById(pl, pl.playerTask.taskMain.id);

                msg.writer().writeUTF(task.name + " : " + task.index);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopGapthu(Player player) {
        List<Player> list = TopManager.getInstance().getListTopGapthu();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top săn boss");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Điểm : " + pl.GapthuPoint);
                msg.writer().writeUTF("Điểm : " + pl.GapthuPoint);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopRuongBau(Player player) {
        List<Player> list = TopManager.getInstance().getListTopRuongbau();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Số lần mở rương báu :");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    Part part = PartManager.getInstance().find(pl.getHead());
                    msg.writer().writeShort(part.getIcon(0));
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF("Số lần mở rương báu : " + pl.RuongbauPoint);
                msg.writer().writeUTF("Số lần mở rương báu : " + pl.RuongbauPoint);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToRegisterScr(Session session) {
        try {
            Message message;
            try {
                message = new Message(42);
                message.writer().writeByte(0);
                session.sendMessage(message);
                message.cleanup();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    //========
    public void sendEffAllPlayer(Player pl, int idEff, int layer, int loop, int loopCount) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(idEff);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopCount);
            msg.writer().writeByte(0);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendEffPlayer(Player pl) {
        if (pl.isPl()) {
            Item danhhieu = pl.inventory.itemsBody.get(12);
            if (danhhieu.isNotNullItem()) {
                Service.getInstance().sendEffAllPlayer(pl, danhhieu.template.part, 1, -1, 1);
            }
        }
    }

    public void sendEffAllPlayerMapToMe(Player pl) {
        try {
            for (Player plM : pl.zone.getPlayers()) {
                if (plM.isPl() && plM.inventory.itemsBody.size() >= 12) {
                    Item danhhieu = plM.inventory.itemsBody.get(12);
                    if (danhhieu.isNotNullItem()) {
                        Service.getInstance().sendEffPlayer(plM, pl, danhhieu.template.part, 1, -1, 1);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void sendEffPlayer(Player pl, Player plReceive, int idEff, int layer, int loop, int loopCount) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(idEff);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopCount);
            msg.writer().writeByte(0);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendEffAllPlayer(Player pl, int idEff) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(idEff);
            msg.writer().writeByte(0);
            msg.writer().writeByte(50);
            msg.writer().writeShort(1);
            msg.writer().writeByte(-1);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //========
    public void addEffect(Player player, int idEff) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.pet.id);
            me.writer().writeShort(idEff);
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addEffect1(Player player, int idEff) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            me.writer().writeShort(idEff);
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitleRv(Player player, Player p2, short id) {
        if (id == 0) {
            return;
        }
        new Thread(() -> {
            Message me;
            try {
                Thread.sleep(1000);
                me = new Message(-128);
                me.writer().writeByte(0);
                me.writer().writeInt((int) player.id);
                // top nạp
                me.writer().writeShort(id);

                me.writer().writeByte(1);
                me.writer().writeByte(-1);
                me.writer().writeShort(50);
                me.writer().writeByte(-1);
                if (p2 != null) {
                    p2.sendMessage(me);
                }
                if (player != null) {
                    this.sendMessAllPlayerInMap(player, me);
                }

                me.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendFoot(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (id == 1185) {
                me.writer().writeShort(74);
            } else if (id == 1186) {
                me.writer().writeShort(75);
            } else if (id == 1187) {
                me.writer().writeShort(76);
            } else if (id == 1188) {
                me.writer().writeShort(77);
            } else if (id == 1189) {
                me.writer().writeShort(78);
            } else if (id == 1190) {
                me.writer().writeShort(79);
            } else if (id == 1191) {
                me.writer().writeShort(80);
            } else if (id == 1192) {
                me.writer().writeShort(81);
            } else if (id == 1193) {
                me.writer().writeShort(82);
            }
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            if (!player.isPet && !player.isBoss && !player.isMiniPet) {
                player.getSession().sendMessage(me);
            }
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void get_all_title(Player plReceive, Player plInfo) {
        if (!plReceive.isPet && !plReceive.isBoss && !plReceive.isMiniPet) {
            short data_id_1 = 0;
             
//             if (plInfo != null) {
//             sendTitleRv(plInfo, plReceive, (short) 81);
//             sendTitleRv(plInfo, plReceive, (short) 83);
//             } else {
//             sendTitleRv(plReceive, plInfo, (short) 81);
//             sendTitleRv(plReceive, plInfo, (short) 83);
//             }
            // pháo hoa
            if (plReceive.itemTime != null && plReceive.itemTime.isUseUsePhaoHoa) {
                if (plInfo != null) {
                    sendTitleRv(plInfo, plReceive, (short) 65);
                } else {
                    sendTitleRv(plReceive, plInfo, (short) 65);
                }
            }
             if (plReceive.getSession().actived) {
            // // thành viên
             if (plInfo != null) {
             sendTitleRv(plInfo, plReceive, (short) 90);
             } else {
             sendTitleRv(plReceive, plInfo, (short) 90);
             }

             }
            if (plReceive.inventory.activeTitle_1 == 1) {
                // Danh hiệu chichi
                if (plInfo != null) {
                    sendTitleRv(plInfo, plReceive, (short) 82);
                } else {
                    sendTitleRv(plReceive, plInfo, (short) 82);
                }

            }
        }
    }
    public String get_top_player(Player player) {
        String thong_bao = "";
        if (player.inventory.top_suc_manh > 0 && player.inventory.top_suc_manh <= 98) {
            thong_bao += "Top " + player.inventory.top_suc_manh + " sức mạnh\n";
        }
        if (player.inventory.top_suc_manh_de_tu > 0 && player.inventory.top_suc_manh_de_tu <= 98) {
            thong_bao += "Top " + player.inventory.top_suc_manh_de_tu + " sự kiện đại dương\n";
        }
        if (player.inventory.top_nhiem_vu > 0 && player.inventory.top_nhiem_vu <= 98) {
            thong_bao += "Top " + player.inventory.top_nhiem_vu + " nhiệm vụ\n";
        }
        if (player.inventory.top_nap > 0 && player.inventory.top_nap <= 98) {
            thong_bao += "Top " + player.inventory.top_nap + " nạp thẻ\n";
        }
        if (player.inventory.top_suc_manh_tuan > 0 && player.inventory.top_suc_manh_tuan <= 98) {
            thong_bao += "Top " + player.inventory.top_suc_manh_tuan + " sức mạnh trong tuần trước\n";
        }
        return thong_bao;
    }

    public boolean ThongBaoNhanTop(Player player) {
        String thong_bao = "";
        thong_bao = get_top_player(player);
        if (thong_bao != "") {
            Service.getInstance().sendThongBaoFromAdmin(player,
                    "Chúc mừng, bạn đã đạt được \n" + thong_bao + "|7|Hãy đến Quy lão để nhận thưởng");
            return true;
        }
        return false;
    }

    public void rsDanhHieu(Player player) {
        removeTitle(player);

        get_all_title(player, null);

        if (!player.isPet) {
             if (player.inventory.itemsBody.get(11).isNotNullItem()) {
             new Thread(() -> {
             try {
             Thread.sleep(1000);
             sendFoot(player, (short) player.inventory.itemsBody.get(11).template.id);
             } catch (Exception e) {
             }
             }).start();
             }
        }
    }
    public void excuteDB(String sql) {
        // PreparedStatement ps = null;
        // Connection con = DBService.gI().getConnectionForGetPlayer();

        // ps = con.prepareStatement("insert into account(username,password) values
        // (?,?)",
        // Statement.RETURN_GENERATED_KEYS);
        // ps.setString(1, user);
        // ps.setString(2, password);
        // ps.executeUpdate();
        // ResultSet rs = ps.getGeneratedKeys();
        // rs.next();
        // key = rs.getInt(1);
        // System.out.println("Tạo tài khoản thành công!");
        // try {
        // ps.close();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    public void regisAccount(Session session, Message _msg) {
        try {
            PreparedStatement ps = null;
            int key = -1;
            int sl = 0;
            String day = _msg.reader().readUTF();
            String month = _msg.reader().readUTF();
            String year = _msg.reader().readUTF();
            String address = _msg.reader().readUTF();
            String cmnd = _msg.reader().readUTF();
            String dayCmnd = _msg.reader().readUTF();
            String noiCapCmnd = _msg.reader().readUTF();
            String user = _msg.reader().readUTF();
            String pass = _msg.reader().readUTF();
            if (!(user.length() >= 4 && user.length() <= 18)) {
                sendThongBaoOK(session, "Tài khoản phải có độ dài 4-18 ký tự");
                return;
            }
            if (!(pass.length() >= 6 && pass.length() <= 18)) {
                sendThongBaoOK(session, "Mật khẩu phải có độ dài 6-18 ký tự");
                return;
            }
            try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
                ps = con.prepareStatement("SELECT COUNT(1) AS sl FROM account WHERE ip_address = ?");
                ps.setString(1, session.ipAddress);
                ResultSet rset = ps.executeQuery();
                rset.next();
                sl = rset.getInt("sl");
                if (sl > 5) {
                    sendThongBaoOK(session, "Số lượng account tối đa có thể đăng ký cho 1 Ip là 5");
                } else {
                    ps = con.prepareStatement("select * from account where username = ?");
                    ps.setString(1, user);
                    if (ps.executeQuery().next()) {
                        sendThongBaoOK(session, "Tạo thất bại do tài khoản đã tồn tại");
                    } else {
                        ps = con.prepareStatement("insert into account(username,password) values (?,?)", Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, user);
                        ps.setString(2, pass);
                        ps.executeUpdate();
                        ResultSet rs = ps.getGeneratedKeys();
                        rs.next();
                        key = rs.getInt(1);
                        sendThongBaoOK(session, "Tạo tài khoản thành công!");
                    }
                }
            } catch (Exception e) {
                Log.error(AccountDAO.class, e);
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            sendThongBaoOK(session, "Tạo tài khoản thất bại");
        }
    }


}
