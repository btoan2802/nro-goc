package nro.services.func;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.consts.ConstOption;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.map.Zone;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.server.Client;
import nro.server.io.Message;
import nro.services.*;
import nro.services.func.lr.LuckyRoundGold;
import nro.services.*;

import java.util.HashMap;
import java.util.Map;
import nro.models.auction.AuctionService;
import nro.models.item.ItemOption;
import nro.server.SettingGame;
import nro.services.giftcode.GiftCodeNew;
import nro.utils.Util;

public class Input {

    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<Integer, Object>();

    public static final int NHAP_PASS_ADMIN = 9999;
    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE_VIP = 501;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int CHOOSE_LEVEL_BDKB = 5066;
    public static final int CHOOSE_LEVEL_CDRD = 7700;
    public static final int CHOOSE_VONG_QUAY_THUONG_DE = 7781;
    public static final int TANG_NGOC_HONG = 505;
    public static final int ADD_ITEM = 506;
    public static final int SELL_GOLD = 517;
    public static final int SELL_GOLD1 = 523;
    public static final int OPEN_CSKB = 518;
    public static final int ITEM_BUFF = 519;
    public static final int GIFT_CODE = 520;
    public static final int DAU_GIA = 521;
    public static final int KEN_ITEM = 522;
    public static final int DOI_THOI_VANG = 6001;
    public static final int DOI_THOI_VANG1 = 6011;
    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;
    // Phong vu
    public static final int MAM_NGU_QUA = 5505;
    public static final int NAU_BANH_TET = 5506;
    public static final int NAU_BANH_CHUNG = 5507;

    public static final int CHON_SO_MAY_MAN = 1906;

    public static final int SEND_ITEM_OP = 555;
    private static Input intance;

    private Input() {

    }

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        try {
            Player pl = null;
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {
                case CHON_SO_MAY_MAN:
                    int gemDat = Integer.parseInt(text[0]);
                    ConSoMayManService.gI().addPlayerData(player, gemDat);
                    break;
                case SEND_ITEM_OP:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int idOptionBuff = Integer.parseInt(text[2]);
                        int slOptionBuff = Integer.parseInt(text[3]);
                        int slItemBuff = Integer.parseInt(text[4]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            switch (idItemBuff) {
                                case -1:
                                    pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) slItemBuff, Inventory.LIMIT_GOLD);
                                    txtBuff += slItemBuff + " vàng\b";
                                    Service.getInstance().sendMoney(player);
                                    break;
                                case -2:
                                    pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + slItemBuff, 2000000000);
                                    txtBuff += slItemBuff + " ngọc\b";
                                    Service.getInstance().sendMoney(player);
                                    break;
                                case -3:
                                    pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + slItemBuff, 2000000000);
                                    txtBuff += slItemBuff + " ngọc khóa\b";
                                    Service.getInstance().sendMoney(player);
                                    break;
                                default:
                                    Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                    itemBuffTemplate.itemOptions.add(new ItemOption(idOptionBuff, slOptionBuff));
                                    itemBuffTemplate.quantity = slItemBuff;
                                    txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\b";
                                    InventoryService.gI().addItemBag(pBuffItem, itemBuffTemplate, slItemBuff);
                                    InventoryService.gI().sendItemBags(pBuffItem);
                                    break;
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(player, 24, txtBuff);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    break;
                   case DOI_THOI_VANG1: 
                            try {
                                TransactionService.gI().cancelTrade(player);
                                int vnd = player.getSession().VND;
                                if (vnd <= 0) {
                                    Service.getInstance().sendThongBao(player, "Bạn không có VND để đổi!");
                                    return;
                                }
                                int soLuong = Math.abs(Integer.parseInt(text[0]));
                                if (soLuong < 1) {
                                    Service.getInstance().sendThongBao(player, "Số lượng phải lớn hơn 0!");
                                    return;
                                }
                                if (soLuong > 10_000_000) {
                                    Service.getInstance().sendThongBao(player, "Số lượng tối đa là 10.000.000");
                                    return;
                                }
                                if (soLuong > vnd) {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ VND, hiện có: " + vnd);
                                    return;
                                }
                                if (InventoryService.gI().getCountEmptyBag(player) <= 0) {
                                    Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
                                    return;
                                }
                                PlayerDAO.subVND(player.getSession().userId, soLuong);
                                player.getSession().VND -= soLuong;
                                Item tv = ItemService.gI().createNewItem((short) 457, soLuong);
                                InventoryService.gI().addItemBag(player, tv);
                                InventoryService.gI().sendItemBags(player);

                                Service.getInstance().sendThongBao(player,
                                    "Bạn đổi thành công " + soLuong + " VND ra " + soLuong + " TV ");
                            } catch (Exception e) {
                                e.printStackTrace();
                                Service.getInstance().sendThongBao(player, "Bạn nhập sai số lượng");
                            }
                            break;
                        case DOI_THOI_VANG:
                    try {
                        TransactionService.gI().cancelTrade(player);
                        Item tv = InventoryService.gI().findItemBagByTemp(player, 457);
                        if (tv == null) {
                            Service.getInstance().sendThongBao(player, "Bạn không có Thỏi vàng thường");
                            return;
                        }
                        if (!tv.isNotNullItem()) {
                            Service.getInstance().sendThongBao(player, "Bạn đã hết Vàng");
                            return;
                        }
                        int sl = Math.abs(Integer.parseInt(text[0]));
                        if (sl < 1 || sl > 10000000) {
                            Service.getInstance().sendThongBao(player, "Số lượng hợp lệ từ 1 đến 10.000.000");
                            return;
                        }
                        if (tv.quantity < sl) {
                            Service.getInstance().sendThongBao(player, "Bạn không đủ số lượng Thỏi vàng thường");
                            return;
                        }
                        if (InventoryService.gI().getCountEmptyBag(player) <= 0) {
                            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
                            return;
                        }
                        InventoryService.gI().subQuantityItemsBag(player, tv, sl);
                        Item tvKhoa = ItemService.gI().createNewItem((short) 1429, sl);
                        tvKhoa.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                        InventoryService.gI().addItemBag(player, tvKhoa);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBaoOK(player, 
                            "Đã đổi " + sl + " Thỏi vàng thường → " + sl + " Thỏi vàng khóa!");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Bạn nhập sai số lượng");
                    }
                    break;
                case SELL_GOLD:
                    try {
                    TransactionService.gI().cancelTrade(player);
                    Item tv = InventoryService.gI().findItemBagByTemp(player, 457);
                    if (tv == null) {
                        return;
                    }
                    if (!tv.isNotNullItem()) {
                        Service.getInstance().sendThongBao(player, "Bạn đã hết Vàng");
                        return;
                    }
                    int sl = Math.abs(Integer.parseInt(text[0]));
                    if (sl < 1 || sl > 100000) {
                        Service.getInstance().sendThongBao(player,
                                "Bạn chỉ có thể bán từ 1 Vàng trở lên, và tối đa 100000 thỏi");
                        return;
                    }
                    if (tv.quantity < sl) {
                        Service.getInstance().sendThongBao(player, "Bạn không đủ số lượng Vàng");
                        return;
                    }
                    int count = 0;
                    Service.getInstance().sendThongBaoOK(player,
                            "Đang bán vàng, số lượng lớn có thể mất thời gian");
                    for (int i = 0; i < sl; i++) {
                        if (player.inventory.gold + 50000000 >= Inventory.LIMIT_GOLD) {
                            Service.getInstance().sendThongBao(player,
                                    "Đã vượt quá giới hạn vàng, tự động tắt");
                            break;
                        }
                        TransactionService.gI().cancelTrade(player);
                        player.inventory.addGold(50000000);
                        count++;
                    }
                    if (!tv.haveOption(30)) {
                        player.pointThoiVang += count;
                    }
                    InventoryService.gI().subQuantityItemsBag(player, tv, count);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBaoOK(player, "Bạn vừa bán " + count + " Vàng nhận được "
                            + Util.powerToString((long) 50000000 * count) + " vàng");
                } catch (Exception e) {
                    e.printStackTrace();
                    Service.getInstance().sendThongBao(player, "Bạn nhập sai số lượng");
                }
                break;
                case SELL_GOLD1:
                    try {
                    TransactionService.gI().cancelTrade(player);
                    Item tv = InventoryService.gI().findItemBagByTemp(player, 1429);
                    if (tv == null) {
                        return;
                    }
                    if (!tv.isNotNullItem()) {
                        Service.getInstance().sendThongBao(player, "Bạn đã hết Vàng Khóa");
                        return;
                    }
                    int sl = Math.abs(Integer.parseInt(text[0]));
                    if (sl < 1 || sl > 100000) {
                        Service.getInstance().sendThongBao(player,
                                "Bạn chỉ có thể bán từ 1 Vàng trở lên, và tối đa 100000 thỏi");
                        return;
                    }
                    if (tv.quantity < sl) {
                        Service.getInstance().sendThongBao(player, "Bạn không đủ số lượng Vàng");
                        return;
                    }
                    int count = 0;
                    Service.getInstance().sendThongBaoOK(player,
                            "Đang bán vàng, số lượng lớn có thể mất thời gian");
                    for (int i = 0; i < sl; i++) {
                        if (player.inventory.gold + 50000000 >= Inventory.LIMIT_GOLD) {
                            Service.getInstance().sendThongBao(player,
                                    "Đã vượt quá giới hạn vàng, tự động tắt");
                            break;
                        }
                        TransactionService.gI().cancelTrade(player);
                        player.inventory.addGold(50000000);
                        count++;
                    }
//                    if (!tv.haveOption(30)) {
                        player.pointThoiVang += count;
//                    }
                    InventoryService.gI().subQuantityItemsBag(player, tv, count);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBaoOK(player, "Bạn vừa bán " + count + " Vàng nhận được "
                            + Util.powerToString((long) 50000000 * count) + " vàng");
                } catch (Exception e) {
                    e.printStackTrace();
                    Service.getInstance().sendThongBao(player, "Bạn nhập sai số lượng");
                }
                break;
                case CHANGE_PASSWORD:
                    Service.getInstance().changePassword(player, text[0], text[1], text[2]);
                    break;
                case GIFT_CODE_VIP:
                    TransactionService.gI().cancelTrade(player);
//                    GiftService.gI().use(player, text[0]);
                    String code = text[0].toLowerCase();
                    if (checkString(code)) {
                        GiftCodeNew.gI().giftCode(player, code);
                    } else {
                        Service.getInstance().sendThongBao(player, "Mã code gồm 5 đến 20 ký tự viết thường và số");
                    }
                    break;
                case GIFT_CODE:
                    GiftService.gI().use(player, text[0]);
                    break;
                case FIND_PLAYER:
                    pl = Client.gI().getPlayer(text[0]);
                    if (pl != null) {
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, -1, "Ngài muốn..?",
                                new String[]{"Đi tới\n" + pl.name, "Gọi " + pl.name + "\ntới đây", "Đổi tên", "Ban"},
                                pl);
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case CHANGE_NAME:
                    Player plChanged = (Player) PLAYER_ID_OBJECT.get((int) player.id);
                    if (plChanged != null) {
                        if (PlayerDAO.isExistName(text[0])) {
                            Service.getInstance().sendThongBao(player, "Tên nhân vật đã tồn tại");
                        } else if (Util.haveSpecialCharacter(text[0]) || text[0].length() < 5
                                || text[0].length() > 9) {
                            Service.getInstance().sendThongBao(player,
                                    "Tên nhân vật không được phép có ký tự đặc biệt và độ dài từ 5-9 ký tự");
                        } else {
                            Item the = InventoryService.gI().findItemBagByTemp(player, 1256);
                            plChanged.name = text[0];
                            InventoryService.gI().subQuantityItemsBag(player, the, 1);
                            InventoryService.gI().sendItemBags(player);
                            PlayerDAO.saveName(plChanged);
                            Service.getInstance().player(plChanged);
                            Service.getInstance().Send_Caitrang(plChanged);
                            Service.getInstance().sendFlagBag(plChanged);
                            Zone zone = plChanged.zone;
                            ChangeMapService.gI().changeMap(plChanged, zone, plChanged.location.x,
                                    plChanged.location.y);
                            Service.getInstance().sendThongBao(plChanged,
                                    "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            Service.getInstance().sendThongBao(player, "Đổi tên người chơi thành công");
                        }
                    }
                    break;
                case CHOOSE_LEVEL_BDKB: {
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                }

                // BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) );
                break;
                case CHOOSE_VONG_QUAY_THUONG_DE: {
                    TransactionService.gI().cancelTrade(player);
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 100) {
                        LuckyRoundService.gI().openCrackBallUI(player, (byte) 0);// 0 là open vàng
                        Thread.sleep(1000);
                        LuckyRoundGold.gI().payAndGetStarted1(player, (byte) level);
                    } else {
                        Service.getInstance().sendThongBao(player, "Quay ít nhất 1 lưượt, tối đa 100 lượt");
                    }
                }
                break;
                case CHOOSE_LEVEL_CDRD: {
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.THAN_VU_TRU, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_CDRD,
                                    "Con có chắc chắn muốn đến con đường rắn độc cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                }
                // BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) );
                break;
                case TANG_NGOC_HONG:
                    pl = Client.gI().getPlayer(text[0]);
                    int numruby = Integer.parseInt((text[1]));
                    if (pl != null) {
                        if (numruby > 0 && player.inventory.ruby >= numruby) {
                            Item item = InventoryService.gI().findVeTangNgoc(player);
                            player.inventory.subRuby(numruby);
                            PlayerService.gI().sendInfoHpMpMoney(player);
                            pl.inventory.ruby += numruby;
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().sendThongBao(player, "Tặng Hồng ngọc thành công");
                            Service.getInstance().sendThongBao(pl,
                                    "Bạn được " + player.name + " tặng " + numruby + " Hồng ngọc");
                            InventoryService.gI().subQuantityItemsBag(player, item, 1);
                            InventoryService.gI().sendItemBags(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc để tặng");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case ADD_ITEM: {
                    if (!player.isAdmin()) {
                        return;
                    }
                    short id = Short.parseShort((text[0]));
                    int quantity = Integer.parseInt(text[1]);
                    Item item = ItemService.gI().createNewItem(id);
                    if (item.template.type < 7) {
                        for (int i = 0; i < quantity; i++) {
                            item = ItemService.gI().createNewItem(id);
                            RewardService.gI().initBaseOptionClothes(item);
                            InventoryService.gI().addItemBag(player, item, 0);
                        }
                    } else {
                        item.quantity = quantity;
                        InventoryService.gI().addItemBag(player, item, 0);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn nhận được " + item.template.name + " Số lượng: " + quantity);
                }

                break;
                case ITEM_BUFF: {
                    short id = 0;
                    int option_id = 0;
                    int param = 0;
                    try {
                        id = Short.parseShort((text[0]));
                        option_id = Integer.parseInt(text[1]);
                        param = Integer.parseInt(text[2]);
                    } catch (Exception e) {
                        id = 14;
                        option_id = 0;
                        param = 0;
                        // TODO: handle exception
                    }

                    if (option_id < 0 || option_id > 254) {
                        Service.getInstance().sendThongBao(player,
                                "Option không tồn tại");
                        return;
                    }
                    if (param < 0 || param > 1000000) {
                        Service.getInstance().sendThongBao(player,
                                "Chỉ số phải nhỏ hơn 1 triệu và lớn hơn hoặc bằng 0");
                        return;
                    }
                    Item item = ItemService.gI().createNewItem(id);
                    if (item != null) {
                        RewardService.gI().initBaseOptionClothes(item);
                        item.itemOptions.add(new ItemOption(option_id, param));
                        InventoryService.gI().addItemBag(player, item, 0);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn nhận được " + item.template.name);
                    }

                }

                break;
                case MAM_NGU_QUA:
                    TransactionService.gI().cancelTrade(player);
                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                        int quantity = Math.abs(Integer.parseInt(text[0]));
                        if (quantity > 0 && quantity < 999) {
                            if (player.inventory.gold > 200_000_000L * quantity) {
                                int ID_ITEM_NHAN = 1340; // mâm ngũ quả
                                int id_cau = 1620; // cầu
                                int sl_doi = 1;

                                Item cau = InventoryService.gI().findItemBagByTemp(player, id_cau);

                                if (cau != null && cau.quantity >= sl_doi * quantity) {

                                    InventoryService.gI().subQuantityItemsBag(player, cau, sl_doi * quantity);

                                    player.inventory.gold -= 200_000_000L * quantity;
                                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                                    Service.getInstance().sendMoney(player);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn vừa nhận được " + quantity + " Hộp quà trung thu");
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Thiếu vật phẩm để đổi được " + quantity + " Hộp quà trung thu");
                                }
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Mỗi Hộp quà trung thu cần 200 triệu vàng");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
                    }
                    break;

                case DAU_GIA:
                    AuctionService.gI().putPlayerPrice(player, Integer.parseInt(text[0]));
                    break;
                case KEN_ITEM:
                    //    Service.getInstance().buffItem(player, text[0], text[1], text[2], text[3]);
                    break;
                case NAU_BANH_TET:
                    try {
                    TransactionService.gI().cancelTrade(player);

                    int quantity = Math.abs(Integer.parseInt(text[0]));
                    if (quantity < 1 || quantity > 999) {
                        Service.getInstance().sendThongBao(player,
                                "Tối đa 999 lần nấu");
                        return;
                    }
                    if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                        Service.getInstance().sendThongBao(player, "Cần ít nhất 1 chỗ trống");
                        return;
                    }
                    if (quantity < 1) {
                        Service.getInstance().sendThongBao(player, "Có lỗi xảy ra");
                        return;
                    }
                    long COST = 500000000l * quantity;
                    if (player.inventory.gold < COST) {
                        Service.getInstance().sendThongBao(player,
                                "Bạn không đủ vàng, cần 500 triệu vàng khi nấu 1 bánh");
                        return;
                    }
                    // Nấu

                    short ID_ITEM_NHAN = ConstItem.BANH_TET_CHIN; // Bánh tét chín
                    short id_banh_tet = ConstItem.BANH_TET_2023; // bánh tét
                    short id_phu_gia_tao_mau = ConstItem.PHU_GIA_TAO_MAU; // phụ gia
                    short id_gia_vi_tong_hop = ConstItem.GIA_VI_TONG_HOP; // gia vị
                    int sl = 1 * quantity;

                    if (ItemService.gI().getQuantityItemOnBag(player, id_banh_tet) < sl) {
                        Service.getInstance().sendThongBao(player, "Thiếu bánh tét (chưa nấu)");
                        return;
                    }
                    if (ItemService.gI().getQuantityItemOnBag(player, id_phu_gia_tao_mau) < sl) {
                        Service.getInstance().sendThongBao(player, "Thiếu phụ gia tạo màu");
                        return;
                    }
                    if (ItemService.gI().getQuantityItemOnBag(player, id_gia_vi_tong_hop) < sl) {
                        Service.getInstance().sendThongBao(player, "Thiếu gia vị tổng hợp");
                        return;
                    }
                    InventoryService.gI().subQuantityItemsBag(player, id_banh_tet, sl);
                    InventoryService.gI().subQuantityItemsBag(player, id_phu_gia_tao_mau, sl);
                    InventoryService.gI().subQuantityItemsBag(player, id_gia_vi_tong_hop, sl);

                    player.inventory.gold -= COST;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN);
                    itemNhan.quantity = quantity;

                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));
                    // player.event.addEventPoint(quantity);
                    InventoryService.gI().addItemBag(player, itemNhan, 999);

                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nấu thành công " + quantity + " bánh tét và nhận được "
                            + Util.powerToString((long) quantity) + " điểm sự kiện");
                } catch (Exception e) {
                    e.printStackTrace();
                    Service.getInstance().sendThongBao(player, "Bạn nhập sai số lượng");
                }
                break;
                case NAU_BANH_CHUNG:
                    try {
                    TransactionService.gI().cancelTrade(player);

                    int quantity = Math.abs(Integer.parseInt(text[0]));
                    if (quantity < 1 || quantity > 999) {
                        Service.getInstance().sendThongBao(player,
                                "Tối đa 999 lần nấu");
                        return;
                    }
                    if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                        Service.getInstance().sendThongBao(player, "Cần ít nhất 1 chỗ trống");
                        return;
                    }
                    if (quantity < 1) {
                        Service.getInstance().sendThongBao(player, "Có lỗi xảy ra");
                        return;
                    }
                    long COST = 500000000l * quantity;
                    if (player.inventory.gold < COST) {
                        Service.getInstance().sendThongBao(player,
                                "Bạn không đủ vàng, cần 500 triệu vàng khi nấu 1 bánh");
                        return;
                    }
                    // Nấu

                    short ID_ITEM_NHAN = ConstItem.BANH_CHUNG_CHIN; // Bánh tét chín
                    short id_banh_tet = ConstItem.BANH_CHUNG_2023; // bánh tét
                    short id_phu_gia_tao_mau = ConstItem.PHU_GIA_TAO_MAU; // phụ gia
                    short id_gia_vi_tong_hop = ConstItem.GIA_VI_TONG_HOP; // gia vị
                    int sl = 1 * quantity;

                    if (ItemService.gI().getQuantityItemOnBag(player, id_banh_tet) < sl) {
                        Service.getInstance().sendThongBao(player, "Thiếu bánh chưng (chưa nấu)");
                        return;
                    }
                    if (ItemService.gI().getQuantityItemOnBag(player, id_phu_gia_tao_mau) < sl) {
                        Service.getInstance().sendThongBao(player, "Thiếu phụ gia tạo màu");
                        return;
                    }
                    if (ItemService.gI().getQuantityItemOnBag(player, id_gia_vi_tong_hop) < sl) {
                        Service.getInstance().sendThongBao(player, "Thiếu gia vị tổng hợp");
                        return;
                    }
                    InventoryService.gI().subQuantityItemsBag(player, id_banh_tet, sl);
                    InventoryService.gI().subQuantityItemsBag(player, id_phu_gia_tao_mau, sl);
                    InventoryService.gI().subQuantityItemsBag(player, id_gia_vi_tong_hop, sl);

                    player.inventory.gold -= COST;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN);
                    itemNhan.quantity = quantity;

                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));
                    // player.event.addEventPoint(quantity);
                    InventoryService.gI().addItemBag(player, itemNhan, 999);

                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nấu thành công " + quantity + " bánh chưng và nhận được "
                            + Util.powerToString((long) quantity) + " điểm sự kiện");
                } catch (Exception e) {
                    e.printStackTrace();
                    Service.getInstance().sendThongBao(player, "Bạn nhập sai số lượng");
                }
                break;
                case OPEN_CSKB:
                    try {
                    TransactionService.gI().cancelTrade(player);

                    int quantity = Math.abs(Integer.parseInt(text[0]));
                    if (quantity < 1 || quantity > 999) {
                        Service.getInstance().sendThongBao(player,
                                "Tối đa từ 1 đến 999 lần mở");
                        return;
                    }
                    for (int i = 0; i < quantity; i++) {
                        if (!UseItem.gI().openCSKB_auto(player)) {
                            Service.getInstance().sendThongBao(player, "Mở capsule tự động đã hoàn tất");
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Service.getInstance().sendThongBao(player, "Bạn nhập sai số lượng");
                }
                break;
                case NHAP_PASS_ADMIN:
                    if (text[0].equals("a")) {
                        String CPU = Input.gI().getCpuLoad();
                        String str = "";
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, -1,
                                "|7|Manager " + SettingGame.NAME_GAME + " Server. \n"
                                + "|1|CPU: " + CPU + "%\n"
                                + "|1|Thread Quantity : " + Thread.activeCount() + "\n"
                                + "|2|Online: " + Client.gI().getPlayers().size() + "\n" + str,
                                "Ngọc rồng", "Đệ tử", "Bảo trì", "Bảo trì\n5s",
                                "Tìm kiếm\nngười chơi", "Cập Nhật\nThông Báo",
                                "Call Boss",
                                "Đóng");

                    } else {
                        Service.getInstance().sendThongBao(player, "Sai mật khẩu!");
                    }
                    break;
            }
        } catch (Exception e) {
        }
    }

    private boolean checkString(String str) {
        String regex = "^[a-z0-9]{5,20}$";
        return str.matches(regex);
    }

    public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
        pl.iDMark.setTypeInput(typeInput);
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createFormSenditem1(Player pl) {
        createForm(pl, SEND_ITEM_OP, "SEND Vật Phẩm Option",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID Trang Bị", NUMERIC),
                new SubInput("ID Option", NUMERIC),
                new SubInput("Param", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Đổi mật khẩu", new SubInput("Mật khẩu cũ",
                PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormGiftCode(Player pl) {
        createForm(pl, GIFT_CODE, "Mã quà tặng", new SubInput("Nhập mã quà tặng", ANY));
    }

    public void createFormPutPriceAuction(Player pl) {
        createForm(pl, DAU_GIA, " trả giá ", new SubInput("Nhập thông tin", NUMERIC));
    }

    public void createFormBuffItem(Player pl) {
        this.createForm((Player) pl, KEN_ITEM, "Cung vật phẩm Ngọc rồng KEDEV", new SubInput("Tên người chơi (-1: cho bản thân)", (byte) 1), new SubInput("Id vật phẩm", (byte) 0), new SubInput("Số lượng", (byte) 0), new SubInput("Chỉ số (định dạng: id1.param1-id2.param2 hoặc -1 nếu không có chỉ số)", (byte) 1));
    }

    public void createFormGiftCodeVip(Player pl) {
        createForm(pl, GIFT_CODE_VIP, "Mã quà tặng", new SubInput("Nhập mã quà tặng", ANY));
    }

    public void createFormFindPlayer(Player pl) {
        createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }

    public void createFormChangeName(Player pl, Player plChanged) {
        PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
        createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChooseLevelBDKB(Player pl) {
        createForm(pl, CHOOSE_LEVEL_BDKB, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormChooseLevelCDRD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_CDRD, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormVongQuayThuongDe(Player pl) {
        createForm(pl, CHOOSE_VONG_QUAY_THUONG_DE, "Chọn số lượt quay", new SubInput("Lượt quay (1-100)", NUMERIC));
    }

    public void createFromChonSoMayMan(Player player) {
        createForm(player, CHON_SO_MAY_MAN, "Hãy chọn 1 số từ 0 đến 99 giá 5 ngọc xanh", new SubInput("Số bạn chọn", NUMERIC));

    }

    public void createFormTangRuby(Player pl) {
        createForm(pl, TANG_NGOC_HONG, "Tặng ngọc", new SubInput("Tên nhân vật", ANY),
                new SubInput("Số Hồng Ngọc Muốn Tặng", NUMERIC));
    }

    public void createFormAddItem(Player pl) {
        createForm(pl, ADD_ITEM, "Add Item", new SubInput("ID VẬT PHẨM", NUMERIC),
                new SubInput("SỐ LƯỢNG", NUMERIC));
    }

    public void ceateFormBanThoiVang(Player pl) {
        createForm(pl, SELL_GOLD, "Bán Vàng", new SubInput("Số lượng (1 đến 100.000)", NUMERIC));
    }

    public void ceateFormBanThoiVang1(Player pl) {
        createForm(pl, SELL_GOLD1, "Bán Vàng", new SubInput("Số lượng (1 đến 100.000)", NUMERIC));
    }
    
    public void createFormDoiMamNguQua(Player pl) {
        createForm(pl, MAM_NGU_QUA, "Nhập số lượng Mâm ngũ quả", new SubInput("Số lượng", NUMERIC));
    }

    public void createFormNauBanhTet(Player pl) {
        createForm(pl, NAU_BANH_TET, "Nhập số lượng bánh tét", new SubInput("Số lượng", NUMERIC));
    }

    public void createFormNauBanhChung(Player pl) {
        createForm(pl, NAU_BANH_CHUNG, "Nhập số lượng bánh chưng", new SubInput("Số lượng", NUMERIC));
    }

    public void createFormOpenCSKB(Player pl) {
        createForm(pl, OPEN_CSKB, "Mở CSKB tự động", new SubInput("Nhập số lượng", NUMERIC));
    }

    public void createFormBuff(Player pl) {
        createForm(pl, ITEM_BUFF, "Vật phẩm",
                new SubInput("ID vật phẩm", NUMERIC),
                new SubInput("ID optionn", NUMERIC),
                new SubInput("Chỉ số", NUMERIC));
    }
    
    public void createFormDoiThoiVang(Player pl) {
    createForm(pl, DOI_THOI_VANG, "Đổi Thỏi Vàng sang Thỏi Vàng Khóa", 
        new SubInput("Số lượng (1 đến 10.000.000)", NUMERIC));
    }
    
    public void createFormDoiVND(Player pl) {
    int vnd = pl.getSession().VND;
    String content = "Đổi VND sang TV Thường (1 VND = 1 TV)\nSố dư hiện tại: " + vnd + " VND";
    createForm(pl, DOI_THOI_VANG1, content, new SubInput("Nhập số lượng VND muốn đổi:", NUMERIC));
    }

    public class SubInput {

        private String name;
        private byte typeInput;

        public SubInput(String name, byte typeInput) {
            this.name = name;
            this.typeInput = typeInput;
        }
    }

    public void createFormNhapPasswordAdmin(Player player) {
    player.iDMark.setTypeInput(Input.NHAP_PASS_ADMIN);

    createForm(player, Input.NHAP_PASS_ADMIN,
            "Nhập mật khẩu Admin để mở Menu:",
            new SubInput("Mật khẩu", PASSWORD));
}
    
    private String getCpuLoad() {
    OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    double load = os.getProcessCpuLoad();
    if (load < 0) load = 0;
    return String.format("%.1f", load * 100);
}
}
