package nro.services;

import java.sql.Connection;
import nro.models.item.ItemOptionTemplate;
import nro.models.item.ItemTemplate;
import nro.attr.Attribute;
import nro.consts.ConstAttribute;
import nro.consts.ConstEvent;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.consts.ConstOption;
import nro.consts.ConstPlayer;
import nro.consts.ConstTask;
import nro.jdbc.daos.PlayerDAO;
import nro.models.clan.ClanMember;
import nro.models.consignment.ConsignmentItem;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.map.phoban.DoanhTrai;
import nro.models.npc.Npc;
import nro.models.player.Inventory;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.shop.ItemShop;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.server.SettingGame;
import nro.server.io.Message;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;
import nro.services.func.SummonDragon;
import nro.services.func.TransactionService;
import nro.services.func.lr.LuckyRoundGold;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import nro.jdbc.DBService;
import nro.models.item.ItemTime;
import nro.utils.Log;

public class NpcMethod {

    private static NpcMethod i;

    public static NpcMethod gI() {
        if (i == null) {
            i = new NpcMethod();
        }
        return i;
    }

    public void NhanThoiVang(Player player) {
        if (player.getSession().VND > 0) {
            if (InventoryService.gI().getCountEmptyBag(player) <= 1) {
                Service.getInstance().sendThongBao(player,
                        "Hành trang đã đầy, cần 2 ô trống");
                return;
            }
            int vang_nhan = player.getSession().VND;
            Logger.errorSaveHistGoldBar(player, vang_nhan, (byte) 0, "Nhận TV");
            Item tv = ItemService.gI().createNewItem((short) 457,
                    vang_nhan);
            InventoryService.gI().addItemBag(player, tv,
                    vang_nhan);
            player.event.addMocNap(vang_nhan);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Con vừa nhận được "
                    + vang_nhan + " Xu");
            PlayerDAO.subGoldBar(player.getSession().userId,
                    vang_nhan);

            player.getSession().VND -= player.getSession().VND;
            if (Manager.EVENT_SEVER == 4) {
                int soLuongThiepChucTet = vang_nhan / 10;
                if (soLuongThiepChucTet > 0) {
                    Item thiep_chuc_tet = ItemService.gI().createNewItem(
                            (short) ConstItem.THIEP_CHUC_TET,
                            soLuongThiepChucTet);
                    InventoryService.gI().addItemBag(player, thiep_chuc_tet, 999);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBaoOK(player,
                            "Con vừa được ta lì xì "
                            + soLuongThiepChucTet + " thiệp chúc tết");
                }

            } else if (SettingGame.Item_Tang_Them > 0) {
                int soLuongThiepChucTet = vang_nhan / 10;
                if (soLuongThiepChucTet > 0) {
                    Item thiep_chuc_tet = ItemService.gI().createNewItem(
                            (short) SettingGame.Item_Tang_Them,
                            soLuongThiepChucTet);
                    InventoryService.gI().addItemBag(player, thiep_chuc_tet, 999);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBaoOK(player,
                            "Con vừa được nhận thêm "
                            + soLuongThiepChucTet
                            + thiep_chuc_tet.template.name);
                }
            }

        } else {
            Service.getInstance().sendThongBao(player,
                    "Con không có vàng, hãy truy cập trang chủ của "
                    + SettingGame.NAME_GAME + " để mua nhé!");
        }
    }

    public void NhanNgocXanh(Player player, Npc npc) {
        if (player.inventory.gem < 10_000_000) {
            player.inventory.gem = 10_000_000;
            Service.getInstance().sendMoney(player);
            Service.getInstance().sendThongBao(player,
                    "Bạn vừa nhận được 10tr ngọc xanh");
        } else {
            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Quá giới hạn rồi",
                    "Đóng");
        }
    }
    
    public void NhanHongNgoc(Player player, Npc npc) {
    if (player.inventory.ruby < 100_000) {  
        player.inventory.ruby = 100_000;
        Service.getInstance().sendMoney(player);  
        Service.getInstance().sendThongBao(player,
                "Bạn vừa nhận được 100K Hồng Ngọc");
    } else {
        npc.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Quá giới hạn rồi",
                "Đóng");
        }
    }


    public void NhanQUADiemDanh(Player player, Npc npc) { //sktrungthu
        if (!player.getSession().actived) {
            Service.getInstance().sendThongBao(player,
                    "Tính năng chỉ dành cho thành viên!!");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player,
                    "Hành trang đã đầy, cần 1 ô trống");
            return;
        }
        if (player.inventory.free_turn_buy_shop >= 1) {
            player.inventory.free_turn_buy_shop -= 1;
            Item tv = ItemService.gI().createNewItem((short) 1340,
                    1);
            InventoryService.gI().addItemBag(player, tv,
                    1);

            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player,
                    "Bạn đã hết lượt nhận miễn phí, hãy chờ đến ngày mai");
            return;
        }

        Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được 1 hộp quà trung thu");
    }

    public void NhanDeTu(Player player, Npc npc) {
        if (player.pet == null) {
            PetService.gI().createNormalPet(player, player.gender, null);
            Service.getInstance().sendThongBaoFromAdmin(player, "Bạn nhận đệ tử !");
        } else {
            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Con đã có đệ tử rồi mà !", "Đóng");
        }
    }

    public void ShareFanpage(Player player) {
        int check_share_fanpage = PlayerDAO.checkShareFanpage(player);
        if (check_share_fanpage == 0) {
            Service.getInstance().sendThongBaoOK(player,
                    "Để nhận quà chia sẽ, bạn hãy vào Fanpage của " + SettingGame.NAME_GAME
                    + ", sau đó ấn like trang, tiếp theo là like và chia sẽ bài viết sự kiện và để lại tên nhân vật\n"
                    + " Mỗi nhân vật chỉ được nhận 1 lần duy nhất");

        } else if (check_share_fanpage == 99) {
            Service.getInstance().sendThongBao(player,
                    "Bạn đã nhận thưởng rồi");
        } else if (check_share_fanpage == 1) {
            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                Service.getInstance().sendThongBao(player,
                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới phát quà cho con được");
                return;
            }
            if (PlayerDAO.updateShareFanpage(player, 99)) {
                player.inventory.activeTitle_1 = 1;
                Item ruong = ItemService.gI().createNewItem((short) 1013, 3);
                ruong.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                InventoryService.gI().addItemBag(player, ruong, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được " + ruong.template.name + " và danh hiệu QD");
                // Service.getInstance().rsDanhHieu(player);
            } else {
                Service.getInstance().sendThongBao(player,
                        "Có lỗi xảy ra, hãy liên hệ admin");
            }
        } else {
            Service.getInstance().sendThongBao(player,
                    "Có lỗi xảy ra, hãy liên hệ admin");
        }
    }

    public void ItemShopWeb(Player player) {
        String getDataShopDraw = PlayerDAO.getDataShopWeb(player);
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        List<Item> list_item_bag_web = new ArrayList<>();

        // data bag
        try {
            dataArray = (JSONArray) jv.parse(getDataShopDraw);
            int sizeBag = dataArray.size();
            if (getDataShopDraw.equals("[]")) {
                Service.getInstance().sendThongBaoOK(player,
                        "Bạn không có vật phẩm nào nhận từ trang chủ");

            } else if (sizeBag == 0) {
                Service.getInstance().sendThongBao(player,
                        "Bạn không có vật phẩm nào");
            } else {
                if (InventoryService.gI().getCountEmptyBag(player) <= sizeBag) {
                    Service.getInstance().sendThongBao(player,
                            "Hành trang không đủ ô trống, cần " + sizeBag
                            + " ô trống trong hành trang ");
                    return;
                }
                String item_data = "";
                if (PlayerDAO.clearDataShopWeb(player)) {
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId,
                                    Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = System.currentTimeMillis();
                            list_item_bag_web.add(item);
                        }
                    }
                    for (Item item : list_item_bag_web) {
                        item_data += item.template.name + "\n";
                        InventoryService.gI().addItemBag(player, item, 999);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBaoOK(player,
                            "Bạn vừa nhận được " + item_data + " từ trang chủ");

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Có lỗi xảy ra, hãy liên hệ admin");
                }

            }
        } catch (Exception e) {
            Logger.logException(NpcMethod.class, e, "Err nhan shop web");
            // TODO: handle exception
        }

    }

    public void LatThe(Player player, Npc npc, int select) {
        if (player.iDMark.isBaseMenu()) {
            switch (select) {
                case 0:
                    ShopService.gI().openShopSpecial(player, npc,
                            ConstNpc.SHOP_LY_TIEU_NUONG, 0, -1);
                    break;
                case 1: {
                    if (player.playerTask.sideTask.template != null) {
                        String npcSay = "Nhiệm vụ hiện tại: "
                                + player.playerTask.sideTask.getName() + " ("
                                + player.playerTask.sideTask.getLevel() + ")"
                                + "\nHiện tại đã hoàn thành: "
                                + player.playerTask.sideTask.count + "/"
                                + player.playerTask.sideTask.maxCount + " ("
                                + player.playerTask.sideTask.getPercentProcess()
                                + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                + player.playerTask.sideTask.leftTask + "/"
                                + ConstTask.MAX_SIDE_TASK;
                        npc.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ", "đóng");
                    } else {
                        npc.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                + "sức cậu có thể làm được cái nào?\n"
                                + "|2| Mỗi nhiệm vụ cấp Siêu khó được hoàn thành, cậu sẽ được 1 mốc nhiệm vụ\n(reset sau 12h đêm)\n"
                                + "|5| Nhiệm vụ hoàn thành trong hôm nay: " + player.inventory.sideTaskToDay,
                                "Dễ\n[1 thẻ]", "Bình thường\n[2 thẻ]", "Khó\n[3 thẻ]", "Siêu khó\n[4 thẻ]");
                    }
                }
                break;
            }
        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
            switch (select) {
                case 0:
                case 1:
                case 2:
                case 3:
                    TaskService.gI().changeSideTask(player, (byte) select);
                    break;
                case 4:
                    npc.openShop(player, ConstNpc.SHOP_SIDE_TASK_DAY, 1);
                    break;
            }
        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
            switch (select) {
                case 0:
                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                        TaskService.gI().paySideTask(player);
                    } else {
                        Service.getInstance().sendThongBaoOK(player,
                                "Cần 1 ô trống trong hành trang để nhận thưởng");
                    }
                    break;
                case 1:
                    TaskService.gI().removeSideTask(player);
                    break;
            }
        }
    }

    public void NhanQuaThanhVien(Player player, Npc npc) {
        // if (!player.isAdmin()) {
        // Service.getInstance().sendThongBao(player,
        // "Tính năng đang được cập nhật");
        // return;
        // }
        if (player.getSession().actived) {
            int check_qua_thanh_vien = PlayerDAO.checkQuaThanhVien(player);
            if (check_qua_thanh_vien == 0) {
                Service.getInstance().sendThongBaoOK(player,
                        "Con không có quà thành viên, hãy vào trang chủ của " + SettingGame.NAME_GAME
                        + " , nạp 10k, sau đó mở thành viên để nhận phần thưởng");

            } else if (check_qua_thanh_vien == 99) {
                Service.getInstance().sendThongBao(player,
                        "Con đã nhận thưởng rồi");
            } else if (check_qua_thanh_vien == 1) {
                if (InventoryService.gI().getCountEmptyBag(player) < 5) {
                    npc.npcChat(player,
                            "Con phải có ít nhất 5 ô trống trong hành trang ta mới phát quà cho con được");
                    return;
                }
                long gold_gift = 5_000_000_000l;
                if (player.inventory.getGold() + gold_gift > Inventory.LIMIT_GOLD) {
                    npc.npcChat(player,
                            "Vàng chứa trong hành trang đã đạt giới hạn tối đa");
                    return;
                }
                if (PlayerDAO.updateQuaThanhVien(player, 99)) {
                    player.inventory.gold += gold_gift;
                    Item item_1 = ItemService.gI().createNewItem((short) 457, 20); // Thỏi vàng
                    Item item_2 = ItemService.gI().createNewItem((short) 1367, 1); // Cải trang Goku God
                    Item item_3 = ItemService.gI().createNewItem((short) 1124, 1); // Pet khí gas
                    Item item_4 = ItemService.gI().createNewItem((short) 1290, 1); // Đeo Lưng
                    Item item_5 = ItemService.gI().createNewItem((short) (1446 + (5 * player.gender)), 1); // Sách biến
                    // hình lv1

                    // item_1.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    // 2
                    item_2.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 23));
                    item_2.itemOptions.add(new ItemOption(ConstOption.HP_PT, 23));
                    item_2.itemOptions.add(new ItemOption(ConstOption.KI_PT, 23));
                    item_2.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, 39));
                    item_2.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    // 3
                    item_3.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 5));
                    item_3.itemOptions.add(new ItemOption(ConstOption.HP_PT, 5));
                    item_3.itemOptions.add(new ItemOption(ConstOption.KI_PT, 5));
                    item_3.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    // 4
                    item_4.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 5));
                    item_4.itemOptions.add(new ItemOption(ConstOption.HP_PT, 5));
                    item_4.itemOptions.add(new ItemOption(ConstOption.KI_PT, 5));
                    item_4.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));

                    // 5
                    item_5.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));

                    InventoryService.gI().addItemBag(player, item_1, 1);
                    InventoryService.gI().addItemBag(player, item_2, 1);
                    InventoryService.gI().addItemBag(player, item_3, 1);
                    InventoryService.gI().addItemBag(player, item_4, 1);
                    InventoryService.gI().addItemBag(player, item_5, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được:\n "
                            + "5 Tỷ vàng\n"
                            + "x5 " + item_1.template.name + "\n"
                            + "x5 " + item_2.template.name + "\n"
                            + "x10 " + item_3.template.name + "\n"
                            + "x10 " + item_4.template.name + "\n"
                            + "x10 " + item_5.template.name);
                    npc.npcChat(player,
                            "Nhận quà thành viên thành công, cảm ơn con đã đồng hành cùng "
                            + SettingGame.NAME_GAME);
                } else {
                    Service.getInstance().sendThongBao(player,
                            "Có lỗi xảy ra, hãy liên hệ admin");
                }
            } else {
                Service.getInstance().sendThongBao(player,
                        "Có lỗi xảy ra, hãy liên hệ admin");
            }

        } else {
            Service.getInstance().sendThongBaoOK(player,
                    "Con chưa mở thành viên, hãy mở thành viên để nhận phần thưởng!!\n"
            // + "Phần thưởng bao gồm:\n"
            // + "5 tỷ vàng\n"
            // + "5 Vé lật thẻ\n"
            // + "3 thuốc x3 sức mạnh\n"
            // + "Combo x5 item x2"
            );
        }
    }

    public void DoiCanhHacTinh(Player player, Npc npc) {
        Item da_hac_tinh = InventoryService.gI().findItemBagByTemp(player,
                (short) 1361);
        if (da_hac_tinh != null && da_hac_tinh.quantity >= 99) {
            if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                npc.npcChat(player,
                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới phát quà cho con được");
                return;
            }
            if (ItemService.gI().SubThoiVang(player, 50)) {
                InventoryService.gI().subQuantityItemsBag(player, da_hac_tinh, 99);

                Item item_1 = ItemService.gI().createNewItem((short) 1274); // cánh hắc tinh
                ItemService.gI().OptionAllItem(item_1, 95);
                InventoryService.gI().addItemBag(player, item_1, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được " + item_1.template.name);
            }
        } else {
            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Ngươi không đủ đá hắc tinh, hãy thu thập cho ta x99 đá hắc tinh bằng cách hạ gục quái ở đây",
                    "đóng");
        }

    }

    public void QuaDHVT(Player player, Npc npc) {

        if (player.receivedTopDhVT) {
            if (InventoryService.gI().getCountEmptyBag(player) < 2) {
                npc.npcChat(player,
                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới phát quà cho con được");
                return;
            }
            player.receivedTopDhVT = false;

            Item item_1 = ItemService.gI().createNewItem((short) 989);
            Item item_2 = ItemService.gI().createNewItem((short) 16, 3);
            item_1.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 35));
            item_1.itemOptions.add(new ItemOption(ConstOption.HP_PT, 35));
            item_1.itemOptions.add(new ItemOption(ConstOption.KI_PT, 35));
            item_1.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 25));
            item_1.itemOptions.add(new ItemOption(241, 15));
            item_1.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            item_1.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 1));
            InventoryService.gI().addItemBag(player, item_1, 1);
            InventoryService.gI().addItemBag(player, item_2, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn vừa nhận được " + item_1.template.name + ", " + item_2.template.name
                    + ", cố gắng phát huy ở trận đấu tiếp theo nhé");
        } else {
            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Ngươi không nằm trong top 1, hãy đến gặp NPC Ghi danh ở đại hội võ thuật để tham gia giải đấu\n"
                    + "|1|Cải trang top đại hội võ thuật có hạn sử dụng ngày và chỉ số khủng đấy",
                    "Đóng");
        }

    }

    public void menuNangCapDeTu(Player player, Npc npc) {
        String getNameDeTu = "";
        for (String[] petString : PetService.nameDetu) {
            getNameDeTu += petString[0] + "\n";
        }
        if (player.pet == null) {
            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Ngươi chưa có đệ tử, hãy cố gắng thu nhận đồ đệ nhé",
                    "Đóng");
            return;
        }
        if (player.pet.LevelZeno >= PetService.maxLevelPet) {
            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Đệ tử ngươi đã đạt cấp độ tối đa",
                    "Đóng");
            return;
        }
        if (PetService.gI().isDeTuNangCap(player.pet)) {
            String npcSaid = "Cấp độ đệ tử hiện tại: ";
            int levelDetu = player.pet.LevelZeno;
            String namePeString = PetService.gI().getNameDeTu(player.pet);
            npcSaid += namePeString + "\n";
            npcSaid += "|5|Sau khi nâng cấp : " + PetService.gI().getNameDeTuNewLevel(player.pet) + "\n";
            // int getNguyenLieu = (levelDetu + 1) * (levelDetu + 1) * 20;
            int getNguyenLieu = 80;
            int getNRO = 10;// trứng
            int getGold = 10;

            int getRatio = 0;
            switch (levelDetu) {
                case 0:
                    getRatio = 80;
                    getNguyenLieu = 10;
                    break;
                case 1:
                    getRatio = 60;
                    getNguyenLieu = 20;
                    break;
                case 2:
                    getRatio = 40;
                    getNguyenLieu = 40;
                    break;
                case 3:
                    getRatio = 50;
                    getNguyenLieu = 30;
                    break;
                case 4:
                    getRatio = 50;
                    getNguyenLieu = 30;
                    break;
                case 5:
                    getRatio = 50;
                    getNguyenLieu = 30;
                    break;
                case 6:
                    getRatio = 50;
                    getNguyenLieu = 30;
                    break;
            }
            npcSaid += "|2|Nguyên liệu cần x" + getNRO + " trứng đệ tử + x" + getNguyenLieu
                    + " đá ngũ sắc"
                    + "\n";
            npcSaid += "Tỉ lệ thành công: " + getRatio + "%";
            npc.createOtherMenu(player, ConstNpc.MENU_NANG_DE_TU,
                    npcSaid,
                    "Nâng cấp\nđệ tử", "Hướng Dẫn", "Đóng");
        } else {
            // npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
            // "Yêu cầu có một trong các đệ từ: \n" + getNameDeTu,
            // "Đóng");
            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Yêu cầu có đệ tử Fide \n",
                    "Đóng");

            return;
        }

    }

    public void startCombine(Player player, int select) {
        switch (player.combineNew.typeCombine) {
            case CombineServiceNew.PHA_LE_HOA_LINH_THU:
                switch (select) {
                    case 0:
                        CombineServiceNew.gI().phaLeHoaLinhThu(player, 1);
                        break;
                    case 1:
                        CombineServiceNew.gI().phaLeHoaLinhThu(player, 10);
                        break;
                    case 2:
                        CombineServiceNew.gI().phaLeHoaLinhThu(player, 50);
                        break;
                    case 3:
                        CombineServiceNew.gI().phaLeHoaLinhThu(player, 100);
                        break;
                    case 4:
                        CombineServiceNew.gI().phaLeHoaLinhThu(player, 200);
                        break;
                    case 5:
                        CombineServiceNew.gI().phaLeHoaLinhThu(player, 1000);
                        break;
                    default:
                        break;
                }
                break;
            case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
            case CombineServiceNew.PHA_LE_HOA_TRANG_BI_X10:
                switch (select) {
                    case 0:
                        CombineServiceNew.gI().phaLeHoaTrangBiX10(player, 1);
                        break;
                    case 1:
                        CombineServiceNew.gI().phaLeHoaTrangBiX10(player, 10);
                        //System.err.println("open 10");
                        break;
                    case 2:
                        CombineServiceNew.gI().phaLeHoaTrangBiX10(player, 50);
                        break;
                    case 3:
                        CombineServiceNew.gI().phaLeHoaTrangBiX10(player, 100);
                        break;
                    case 4:
                        CombineServiceNew.gI().phaLeHoaTrangBiX10(player, 200);
                        break;
                    case 5:
                        CombineServiceNew.gI().phaLeHoaTrangBiX10(player, 1000);
                        break;
                    default:
                        break;
                }
                break;
            case CombineServiceNew.THANG_HOA_NGOC_BOI:
            case CombineServiceNew.THANG_HOA_NGOC_BOI_DE_TU:
                switch (select) {
                    case 0:
                        CombineServiceNew.gI().ThanghoaNgocBoi(player);
                        // System.err.println("ok nang cap ngoc boi 1");
                        break;
                    case 1:
                        CombineServiceNew.gI().ThanghoaNgocBoidetu(player);
                        // System.err.println("ok nang cap ngoc boi 2");
                        break;
                    default:
                        break;
                }
                break;
            case CombineServiceNew.EP_SAO_TRANG_BI:
            case CombineServiceNew.EP_PHA_LE_LINH_THU:
            case CombineServiceNew.DOI_DO_THAN_LINH_THANH_HUY_DIET:
            case CombineServiceNew.DAP_SET_KICH_HOAT:
            case CombineServiceNew.THANG_CAP_NGOC_BOI:
            case CombineServiceNew.TRAO_DOI_XU_HADES:
            case CombineServiceNew.NANG_CAP_CHI_SO_BONG_TAI:
            case CombineServiceNew.DAP_SET_KICH_HOAT_CAO_CAP:
            case CombineServiceNew.GIA_HAN_CAI_TRANG:
            case CombineServiceNew.AN_TRANG_BI:
            case CombineServiceNew.BONG_TOI_TRANG_BI:
            case CombineServiceNew.DLETE_BONG_TOI_TRANG_BI:
            case CombineServiceNew.CHE_BIEN_TRA_HOA_CUC:
            case CombineServiceNew.GHEP_RUONG_GOD:
            case CombineServiceNew.NANG_CAP_SKH_THUONG:
            case CombineServiceNew.NANG_CAP_SKH_VIP:
            case CombineServiceNew.NANG_CAP_ZENO:
            case CombineServiceNew.CHE_TAO_BO_KEO_KINH_DI:
            case CombineServiceNew.CHE_TAO_GIO_KEO_KINH_DI:
            case CombineServiceNew.NANG_CAP_VAT_PHAM:
            case CombineServiceNew.NANG_CAP_BONG_TAI:
            case CombineServiceNew.NANG_CAP_BONG_TAI_3:
            case CombineServiceNew.NANG_CAP_CHI_SO_BONG_TAI_3:
            case CombineServiceNew.LAM_PHEP_NHAP_DA:
            case CombineServiceNew.NHAP_NGOC_RONG:
            case CombineServiceNew.EP_SAO_ZENO:
            case CombineServiceNew.GHEP_CAI_TRANG_2:
            case CombineServiceNew.NANG_CAP_SKH_THUONG_GOLD_BAR:
            case CombineServiceNew.PHAN_TACH_HUY_DIET_LAY_MANH:
            case CombineServiceNew.DOI_DO_THIEN_SU:
                if (select == 0) {
                    CombineServiceNew.gI().startCombine(player);
                } else if (select == 1) {
                    switch (player.combineNew.typeCombine) {
                        case CombineServiceNew.NHAP_NGOC_RONG:
                            CombineServiceNew.gI().startCombine_2(player);
                            break;
                    }

                }
                break;

        }
    }

    public void SetNangCapDeTu(Player player, Npc npc) {
        String getNameDeTu = "";
        for (String[] petString : PetService.nameDetu) {
            getNameDeTu += petString[0] + "\n";
        }
        if (player.pet == null) {

            Service.getInstance().sendThongBaoOK(player,
                    "Yêu cầu có đệ từ nở từ trứng \n");
            // Service.getInstance().sendThongBaoOK(player,
            // "Yêu cầu có một trong các đệ từ \n" + getNameDeTu);
            return;
        }
        if (PetService.gI().isDeTuNangCap(player.pet)) {
            int levelDetu = player.pet.LevelZeno;

            if (levelDetu >= PetService.maxLevelPet) {
                Service.getInstance().sendThongBao(player,
                        "Đệ tử của ngươi đã đạt cấp độ tối đa");
                return;
            }
            int getNguyenLieu = 80;
            int getNRO = 10;// trứng
            int getGold = 10;

            int getRatio = 0;
            switch (levelDetu) {
                case 0:
                    getRatio = 80;
                    getNguyenLieu = 10;
                    break;
                case 1:
                    getRatio = 60;
                    getNguyenLieu = 20;
                    break;
                case 2:
                    getRatio = 40;
                    getNguyenLieu = 40;
                    break;
                case 3:
                    getRatio = 50;
                    getNguyenLieu = 30;
                    break;
                case 4:
                    getRatio = 50;
                    getNguyenLieu = 30;
                    break;
                case 5:
                    getRatio = 50;
                    getNguyenLieu = 30;
                    break;
                case 6:
                    getRatio = 50;
                    getNguyenLieu = 30;
                    break;
            }
            Item da_ngu_sac = InventoryService.gI().findItemBag(player, (short) 674);
            Item trung_de_tu = InventoryService.gI().findItemBag(player, (short) 1477);

            if (da_ngu_sac == null || da_ngu_sac.quantity < getNguyenLieu) {
                Service.getInstance().sendThongBao(player,
                        "Còn thiếu đá ngũ sắc");
                return;
            }
            if (trung_de_tu == null || trung_de_tu.quantity < getNRO) {
                Service.getInstance().sendThongBao(player,
                        "Còn thiếu trứng đệ tử");
                return;
            }

            // if (ItemService.gI().SubThoiVang(player, getGold)) {
            InventoryService.gI().subQuantityItemsBag(player, trung_de_tu, getNRO);
            InventoryService.gI().subQuantityItemsBag(player, da_ngu_sac, getNguyenLieu);

            if (Util.isTrue(getRatio, 120)) {
                player.pet.LevelZeno += 1;
                // Tên đệ tử nameDetu[Loại đệ từ][Cấp độ]
                // 0 = Mabu, 1 = black goku,
                // 2 = Xên, 3 = fide
                String namePeString = PetService.gI().getNameDeTu(player.pet);
                player.pet.baseName = "" + namePeString;
                // player.pet.name = "$[ Cấp : " + (player.pet.LevelZeno + 1) + " ] " +
                // player.pet.baseName; // tên
                player.pet.name = "$" + player.pet.baseName; // hiển
                ChangeMapService.gI().changeMapInYard(player, player.zone.map.mapId, -1,
                        player.location.x); // thị
                if (player.pet.isMabu) {

                    Service.getInstance().sendThongBao(player,
                            "Đệ tử của bạn được nâng cấp thành " + namePeString);

                } else if (player.pet.isBU) {

                    Service.getInstance().sendThongBao(player,
                            "Đệ tử của bạn được nâng cấp thành " + namePeString);

                } else if (player.pet.isCell) {

                    Service.getInstance().sendThongBao(player,
                            "Đệ tử của bạn được nâng cấp thành " + namePeString);

                } else if (player.pet.isCell) {

                    Service.getInstance().sendThongBao(player,
                            "Đệ tử của bạn được nâng cấp thành " + namePeString);

                } else if (player.pet.isGoku) {

                    Service.getInstance().sendThongBao(player,
                            "Đệ tử của bạn được nâng cấp thành " + namePeString);

                }

            } else {
                npc.npcChat(player,
                        "Nâng cấp thất bại, chúc con may mắn lần sau");
                Service.getInstance().sendThongBao(player,
                        "Nâng cấp thất bại");
            }
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
        } else {
            Service.getInstance().sendThongBaoOK(player,
                    "Yêu cầu có đệ từ nở từ trứng\n");
            return;
        }

    }

    public void DoanhTrai(Player player, Npc npc, int select) {

        if (npc.mapId == 27) {
            switch (player.iDMark.getIndexMenu()) {
                case ConstNpc.MENU_KHONG_CHO_VAO_DT:
                    if (select == 1) {
                        NpcService.gI().createTutorial(player, npc.avartar,
                                ConstNpc.HUONG_DAN_DOANH_TRAI);
                    }
                    break;
                case ConstNpc.MENU_CHO_VAO_DT:
                    switch (select) {
                        case 0:
                            DoanhTraiService.gI().openDoanhTrai(player);
                            break;
                        case 2:
                            NpcService.gI().createTutorial(player, npc.avartar,
                                    ConstNpc.HUONG_DAN_DOANH_TRAI);
                            break;
                    }
                    break;
                case ConstNpc.MENU_VAO_DT:
                    switch (select) {
                        case 0:
                            ChangeMapService.gI().changeMap(player, 53, 0, 35, 432);
                            break;
                        case 2:
                            NpcService.gI().createTutorial(player, npc.avartar,
                                    ConstNpc.HUONG_DAN_DOANH_TRAI);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void Nhan_Top_Suc_Manh(Player player, Npc npc) {
        int check_top = player.inventory.top_suc_manh;
        if (check_top == 0) {
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn không nằm trong top");
            return;

        } else if (check_top == 99) {
            Service.getInstance().sendThongBao(player,
                    "Con đã nhận thưởng rồi, hãy đợi sự kiện tiếp theo nhé");
            return;
        } else if (check_top >= 1 && check_top <= 51) {
            if (InventoryService.gI().getCountEmptyBag(player) <= 12) {
                npc.npcChat(player,
                        "Con phải có ít nhất 12 ô trống trong hành trang ta mới phát quà cho con được");
                return;
            }
            player.inventory.top_suc_manh = 99;
            Item vat_pham_1_top = ItemService.gI().createNewItem((short) 989, 1);// cải trang
            Item vat_pham_2_top = ItemService.gI().createNewItem((short) 982, 1);// VPDL
            String info_vang = " ";
            if (check_top == 1) {
                // info_vang = ADD_THOI_VANG_BO_NRO(player, 5000, 10);
                // CHỈ SỐ CẢI TRANG
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 28));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 28));
                vat_pham_1_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 2) {
                // info_vang = ADD_THOI_VANG_BO_NRO(player, 3000, 7);
                // CHỈ SỐ CẢI TRANG
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 13));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 27));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 27));
                vat_pham_1_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 3) {
                // info_vang = ADD_THOI_VANG_BO_NRO(player, 1000, 5);
                // CHỈ SỐ CẢI TRANG
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 12));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 26));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 26));
                vat_pham_1_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 4) {
                // info_vang = ADD_THOI_VANG_BO_NRO(player, 500, 5);
                // CHỈ SỐ CẢI TRANG
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 11));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 25));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 25));
                vat_pham_1_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 5) {
                // info_vang = ADD_THOI_VANG_BO_NRO(player, 200, 3);
                // CHỈ SỐ CẢI TRANG
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_1_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh

                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 6) {
                // info_vang = ADD_THOI_VANG_BO_NRO(player, 200, 3);
                // CHỈ SỐ CẢI TRANG
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_1_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh

                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 7) {
                // info_vang = ADD_THOI_VANG_BO_NRO(player, 200, 3);
                // CHỈ SỐ CẢI TRANG
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_1_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh

                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 8) {
                // info_vang = ADD_THOI_VANG_BO_NRO(player, 200, 3);
                // CHỈ SỐ CẢI TRANG
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_1_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh

                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 9) {
                // info_vang = ADD_THOI_VANG_BO_NRO(player, 200, 3);
                // CHỈ SỐ CẢI TRANG
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_1_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh

                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 10) {
                // info_vang = ADD_THOI_VANG_BO_NRO(player, 200, 3);
                // CHỈ SỐ CẢI TRANG
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 24));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_1_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh

                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 6));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(234, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top > 10 && check_top <= 50) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 100, 1);
            } else {
                Service.getInstance().sendThongBao(player,
                        "Có lỗi xảy ra, hãy liên hệ admin");
                return;
            }
            String info_item = " ";
            if (check_top >= 1 && check_top <= 10) {
                if (vat_pham_1_top != null) {
                    InventoryService.gI().addItemBag(player, vat_pham_1_top, 1);

                    info_item += vat_pham_1_top.template.name + "\n";

                }
                if (vat_pham_2_top != null) {
                    InventoryService.gI().addItemBag(player, vat_pham_2_top, 1);
                    info_item += vat_pham_2_top.template.name + "\n";
                }
            }
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player,
                    "Bạn vừa nhận được " + info_vang + info_item);
            npc.npcChat(player,
                    "Nhận thưởng top " + check_top
                    + " thành công, con rất nổ lực, nhưng vẫn còn nhiều sự kiện phía trước, hãy trở nên mạnh hơn nữa nhé");

        } else {
            Service.getInstance().sendThongBao(player,
                    "Có lỗi xảy ra, hãy liên hệ admin");
            return;
        }
    }

    public void Nhan_Top_Suc_Manh_De_Tu(Player player, Npc npc) {

        int check_top = player.inventory.top_suc_manh_de_tu;
        if (check_top == 0) {
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn không nằm trong top");
            return;

        } else if (check_top == 99) {
            Service.getInstance().sendThongBao(player,
                    "Con đã nhận thưởng rồi, hãy đợi sự kiện tiếp theo nhé");
            return;
        } else if (check_top >= 1 && check_top <= 5) {
            if (InventoryService.gI().getCountEmptyBag(player) <= 12) {
                npc.npcChat(player,
                        "Con phải có ít nhất 12 ô trống trong hành trang ta mới phát quà cho con được");
                return;
            }
            player.inventory.top_suc_manh_de_tu = 99;
            short idCt = (short) 876;// td
            if (player.gender == 1) {// nm
                idCt = (short) 877;
            } else if (player.gender == 2) {// xd
                idCt = (short) 875;
            }
            Item vat_pham_1_top = ItemService.gI().createNewItem((short) idCt, 1);// Cải trang
            Item vat_pham_2_top = ItemService.gI().createNewItem((short) 1110, 1);// Kiếm z
            String info_vang = " ";
            if (check_top == 1) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 3000, 20);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 20));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 32));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 32));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 20));
                vat_pham_1_top.itemOptions.add(new ItemOption(243, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 12));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 12));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(243, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 2) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 2000, 12);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 19));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 14));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 30));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 30));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 19));
                vat_pham_1_top.itemOptions.add(new ItemOption(243, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 11));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 11));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(243, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 3) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 1000, 8);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 18));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 14));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 29));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 29));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 18));
                vat_pham_1_top.itemOptions.add(new ItemOption(243, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 10));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 10));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(243, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 4) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 500, 5);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 17));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 13));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 28));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 28));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 17));
                vat_pham_1_top.itemOptions.add(new ItemOption(243, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(243, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 5) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 500, 5);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 17));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 13));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 28));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 28));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 17));
                vat_pham_1_top.itemOptions.add(new ItemOption(243, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(243, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else {
                Service.getInstance().sendThongBao(player,
                        "Có lỗi xảy ra, hãy liên hệ admin");
                return;
            }
            String info_item = " ";
            if (check_top >= 1 && check_top <= 5) {
                if (vat_pham_1_top != null) {
                    InventoryService.gI().addItemBag(player, vat_pham_1_top, 1);

                    info_item += vat_pham_1_top.template.name + "\n";

                }
                if (vat_pham_2_top != null) {
                    InventoryService.gI().addItemBag(player, vat_pham_2_top, 1);
                    info_item += vat_pham_2_top.template.name + "\n";
                }
            }
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player,
                    "Bạn vừa nhận được " + info_vang + info_item);
            npc.npcChat(player,
                    "Nhận thưởng top " + check_top
                    + " thành công, con rất nổ lực, nhưng vẫn còn nhiều sự kiện phía trước, hãy trở nên mạnh hơn nữa nhé");

        } else {
            Service.getInstance().sendThongBao(player,
                    "Có lỗi xảy ra, hãy liên hệ admin");
            return;
        }
    }

    public void Nhan_Top_Nhiem_Vu(Player player, Npc npc) {
        int check_top = player.inventory.top_nhiem_vu;
        if (check_top == 0) {
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn không nằm trong top");
            return;

        } else if (check_top == 99) {
            Service.getInstance().sendThongBao(player,
                    "Con đã nhận thưởng rồi, hãy đợi sự kiện tiếp theo nhé");
            return;
        } else if (check_top >= 1 && check_top <= 50) {
            if (InventoryService.gI().getCountEmptyBag(player) <= 12) {
                npc.npcChat(player,
                        "Con phải có ít nhất 12 ô trống trong hành trang ta mới phát quà cho con được");
                return;
            }
            player.inventory.top_nhiem_vu = 99;
            String infor_top = "";
            if (check_top == 1) {
                Service.getInstance().sendThongBao(player,
                        "Bạn đã nhận quà rồi");
                // infor_top = ADD_THOI_VANG_BO_NRO(player, 1000, 20);
            } else if (check_top == 2) {
                Service.getInstance().sendThongBao(player,
                        "Bạn đã nhận quà rồi");
                // infor_top = ADD_THOI_VANG_BO_NRO(player, 300, 10);
            } else if (check_top == 3) {
                // infor_top = ADD_THOI_VANG_BO_NRO(player, 300, 10);
                Service.getInstance().sendThongBao(player,
                        "Bạn đã nhận quà rồi");
            } else if (check_top == 4) {
                // infor_top = ADD_THOI_VANG_BO_NRO(player, 300, 10);
                Service.getInstance().sendThongBao(player,
                        "Bạn đã nhận quà rồi");
            } else if (check_top == 5) {
                // infor_top = ADD_THOI_VANG_BO_NRO(player, 300, 10);
                Service.getInstance().sendThongBao(player,
                        "Bạn đã nhận quà rồi");
            } else if (check_top > 5 && check_top <= 50) {
                infor_top = ADD_THOI_VANG_BO_NRO(player, 300, 10);
                // Service.getInstance().sendThongBao(player,
                // "Bạn đã nhận quà rồi");
            } else {
                Service.getInstance().sendThongBao(player,
                        "Có lỗi xảy ra, hãy liên hệ admin");
                return;
            }
            if (infor_top != "") {
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được " + infor_top);
            }

            InventoryService.gI().sendItemBags(player);

            Service.getInstance().sendThongBaoOK(player,
                    "Nhận thưởng top " + check_top
                    + " thành công, con rất nổ lực, nhưng vẫn còn nhiều sự kiện phía trước, hãy trở nên mạnh hơn nữa nhé");
        } else {
            Service.getInstance().sendThongBao(player,
                    "Có lỗi xảy ra, hãy liên hệ admin");
            return;
        }
    }

    public void Nhan_Top_Nap(Player player, Npc npc) {
        int check_top = player.inventory.top_nap;
        if (check_top == 0) {
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn không nằm trong top");
            return;

        } else if (check_top == 99) {
            Service.getInstance().sendThongBao(player,
                    "Con đã nhận thưởng rồi, hãy đợi sự kiện tiếp theo nhé");
            return;
        } else if (check_top >= 1 && check_top <= 5) {
            if (InventoryService.gI().getCountEmptyBag(player) <= 12) {
                npc.npcChat(player,
                        "Con phải có ít nhất 12 ô trống trong hành trang ta mới phát quà cho con được");
                return;
            }
            player.inventory.top_nap = 99;
            Item vat_pham_1_top = ItemService.gI().createNewItem((short) 1273, 1);// Linh thú
            Item vat_pham_2_top = ItemService.gI().createNewItem((short) 1271, 1);// PEt
            String info_vang = " ";
            if (check_top == 1) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 2000, 10);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 10));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 10));
                vat_pham_1_top.itemOptions.add(new ItemOption(237, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 10));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 10));
                vat_pham_2_top.itemOptions.add(new ItemOption(237, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 2) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 1500, 7);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 9));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 9));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 9));
                vat_pham_1_top.itemOptions.add(new ItemOption(237, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(237, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 3) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 1000, 5);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 8));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 8));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 8));
                vat_pham_1_top.itemOptions.add(new ItemOption(237, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(237, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 4) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 500, 5);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 7));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 7));
                vat_pham_1_top.itemOptions.add(new ItemOption(237, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(237, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else if (check_top == 5) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 200, 3);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 7));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 7));

                vat_pham_1_top.itemOptions.add(new ItemOption(237, check_top));// top sức mạnh

                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 7));

                vat_pham_2_top.itemOptions.add(new ItemOption(237, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            } else {
                Service.getInstance().sendThongBao(player,
                        "Có lỗi xảy ra, hãy liên hệ admin");
                return;
            }
            String info_item = " ";
            if (check_top >= 1 && check_top <= 5) {
                if (vat_pham_1_top != null) {
                    InventoryService.gI().addItemBag(player, vat_pham_1_top, 1);

                    info_item += vat_pham_1_top.template.name + "\n";

                }
                if (vat_pham_2_top != null) {
                    InventoryService.gI().addItemBag(player, vat_pham_2_top, 1);
                    info_item += vat_pham_2_top.template.name + "\n";
                }
            }
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player,
                    "Bạn vừa nhận được " + info_vang + info_item);
            npc.npcChat(player,
                    "Nhận thưởng top " + check_top
                    + " thành công, con rất nổ lực, nhưng vẫn còn nhiều sự kiện phía trước, hãy trở nên mạnh hơn nữa nhé");

        } else {
            Service.getInstance().sendThongBao(player,
                    "Có lỗi xảy ra, hãy liên hệ admin");
            return;
        }
    }

    public void Nhan_Top_Suc_Manh_Tuan(Player player, Npc npc) {
        int check_top = player.inventory.top_suc_manh_tuan;
        if (check_top == 0) {
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn không nằm trong top");
            return;

        } else if (check_top == 99) {
            Service.getInstance().sendThongBao(player,
                    "Con đã nhận thưởng rồi, hãy đợi sự kiện tiếp theo nhé");
            return;
        } else if (check_top >= 1 && check_top <= 10) {
            if (InventoryService.gI().getCountEmptyBag(player) <= 12) {
                npc.npcChat(player,
                        "Con phải có ít nhất 12 ô trống trong hành trang ta mới phát quà cho con được");
                return;
            }
            player.inventory.top_suc_manh_tuan = 99;
            Item vat_pham_1_top = ItemService.gI().createNewItem((short) 904, 1);// Cải trang
            Item vat_pham_2_top = ItemService.gI().createNewItem((short) 865, 1);// Kiếm z
            Item vat_pham_3_top = ItemService.gI().createNewItem((short) 1406, 1);
            String info_vang = " ";
            if (check_top == 1) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 3000, 20);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 28));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 28));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 10));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 10));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));

                // CHỈ SỐ RƯƠNG KÍCH HOẠT
                vat_pham_3_top.itemOptions.add(new ItemOption(ConstOption.CAP, 12));
            } else if (check_top == 2) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 2000, 12);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 14));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 14));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 27));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 27));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 9));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                // CHỈ SỐ RƯƠNG KÍCH HOẠT
                vat_pham_3_top.itemOptions.add(new ItemOption(ConstOption.CAP, 9));
            } else if (check_top == 3) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 1000, 8);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 13));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 14));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 26));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 26));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 8));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                // CHỈ SỐ RƯƠNG KÍCH HOẠT
                vat_pham_3_top.itemOptions.add(new ItemOption(ConstOption.CAP, 6));
            } else if (check_top == 4) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 500, 5);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 13));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 13));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 25));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 25));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                // CHỈ SỐ RƯƠNG KÍCH HOẠT
                vat_pham_3_top.itemOptions.add(new ItemOption(ConstOption.CAP, 0));
            } else if (check_top == 5) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 500, 5);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 13));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 13));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 25));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 25));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                // CHỈ SỐ RƯƠNG KÍCH HOẠT
                vat_pham_3_top.itemOptions.add(new ItemOption(ConstOption.CAP, 0));
            } else if (check_top >= 6 && check_top <= 10) {
                info_vang = ADD_THOI_VANG_BO_NRO(player, 200, 3);
                // CHỈ SỐ CẢI TRANG

                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 13));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 13));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 25));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 25));
                vat_pham_1_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_1_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh

                // CHỈ SỐ VPDL
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HP_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KI_PT, 7));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));
                vat_pham_2_top.itemOptions.add(new ItemOption(240, check_top));// top sức mạnh
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));
                vat_pham_2_top.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));

            } else {
                Service.getInstance().sendThongBao(player,
                        "Có lỗi xảy ra, hãy liên hệ admin");
                return;
            }
            String info_item = " ";
            if (check_top >= 1 && check_top <= 10) {
                if (vat_pham_1_top != null) {
                    InventoryService.gI().addItemBag(player, vat_pham_1_top, 1);

                    info_item += vat_pham_1_top.template.name + "\n";

                }
                if (vat_pham_2_top != null) {
                    InventoryService.gI().addItemBag(player, vat_pham_2_top, 1);
                    info_item += vat_pham_2_top.template.name + "\n";
                }
                if (vat_pham_3_top != null) {
                    InventoryService.gI().addItemBag(player, vat_pham_3_top, 1);
                    info_item += vat_pham_3_top.template.name + "\n";
                }
            }
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player,
                    "Bạn vừa nhận được " + info_vang + info_item);
            npc.npcChat(player,
                    "Nhận thưởng top " + check_top
                    + " thành công, con rất nổ lực, nhưng vẫn còn nhiều sự kiện phía trước, hãy trở nên mạnh hơn nữa nhé");

        } else {
            Service.getInstance().sendThongBao(player,
                    "Có lỗi xảy ra, hãy liên hệ admin");
            return;
        }
    }

    private String ADD_THOI_VANG_BO_NRO(Player player, int value_thoi_vang, int value_bo_nro) {

        Item ThoiVang = ItemService.gI().createNewItem((short) 457, value_thoi_vang);
        InventoryService.gI().addItemBag(player, ThoiVang, 9999);
        for (int i = 0; i < 7; i++) {
            Item Bo_Nr = ItemService.gI().createNewItem((short) (14 + i), value_bo_nro);
            InventoryService.gI().addItemBag(player, Bo_Nr, 9999);
        }
        InventoryService.gI().sendItemBags(player);
        String info = "\n" + value_thoi_vang + " Xu và " + value_bo_nro + " bộ ngọc rồng\n";
        return info;

    }

    public void chonDoHuyDiet(Player player, short idRuong, int select) {
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
            return;
        }

        Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, idRuong);
        if (item == null) {
            return;
        }
        if (item.quantity < 1) {
            return;
        }
        short[] ao = {650, 652, 654};
        short[] quan = {651, 653, 655};
        short[] gang = {657, 659, 661};
        short[] giay = {658, 660, 662};
        short[] rada = {656};
        short[] itemsToOpen = {};

        int gender = player.gender;
        switch (select) {
            case 0:
                itemsToOpen = ao;
                break;
            case 1:
                itemsToOpen = quan;
                break;
            case 2:
                itemsToOpen = gang;
                break;
            case 3:
                itemsToOpen = giay;
                break;
            case 4:
                itemsToOpen = rada;
                gender = 0;
                break;
        }
        InventoryServiceNew.gI().subQuantityItemsBag(player,
                item, 1);
        Item item2 = ItemService.gI().createNewItem(itemsToOpen[gender]);
        ItemService.gI().OptionAllItem(item2, 0);
        if (item2 != null) {
            InventoryServiceNew.gI().addItemBag(player, item2);

            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item2.template.name);
        }

        InventoryServiceNew.gI().sendItemBags(player);

    }

    public void ruong5Sao(Player player, short idRuong, int select) {
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
            return;
        }
        Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, idRuong);
        if (item == null) {
            return;
        }
        if (item.quantity < 1) {
            return;
        }
        InventoryServiceNew.gI().subQuantityItemsBag(player,
                item, 1);
        short idItem = ConstItem.doSKHVip[select][player.gender][10];
        Item item2 = ItemService.gI().createNewItem(idItem);

        if (item2 != null) {
            ItemService.gI().OptionAllItem(item2, 0);
            item.itemOptions.add(new ItemOption(107, 5));
            InventoryServiceNew.gI().addItemBag(player, item2);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item2.template.name);
        }

        InventoryServiceNew.gI().sendItemBags(player);

    }

    public void ruong6Sao(Player player, short idRuong, int select) {
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
            return;
        }
        Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, idRuong);
        if (item == null) {
            return;
        }
        if (item.quantity < 1) {
            return;
        }
        InventoryServiceNew.gI().subQuantityItemsBag(player,
                item, 1);
        short idItem = ConstItem.doSKHVip[select][player.gender][11];
        Item item2 = ItemService.gI().createNewItem(idItem);

        if (item2 != null) {
            ItemService.gI().OptionAllItem(item2, 0);
            item.itemOptions.add(new ItemOption(107, 6));
            InventoryServiceNew.gI().addItemBag(player, item2);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item2.template.name);
        }

        InventoryServiceNew.gI().sendItemBags(player);

    }

    public void ruongThanLinh(Player player, short idRuong, int select) {
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
            return;
        }
        Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, idRuong);
        if (item == null) {
            return;
        }
        if (item.quantity < 1) {
            return;
        }
        InventoryServiceNew.gI().subQuantityItemsBag(player,
                item, 1);
        short idItem = ConstItem.doSKHVip[select][player.gender][12];
        Item item2 = ItemService.gI().createNewItem(idItem);

        if (item2 != null) {
            ItemService.gI().OptionAllItem(item2, 0);
            // item.itemOptions.add(new ItemOption(107, 6));
            InventoryServiceNew.gI().addItemBag(player, item2);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item2.template.name);
        }

        InventoryServiceNew.gI().sendItemBags(player);

    }

    public void RuongThanLinhGender(Player player, int select) {
        // rương thần linh chọn hành tinh buff bẩn để bán

        int emtyBag = 5;
        Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1426);
        if (item == null || item.quantity < 1) {
            return;
        } else {

            if (InventoryServiceNew.gI().getCountEmptyBag(player) > emtyBag) {
                // xóa rương
                InventoryServiceNew.gI().subQuantityItemsBag(player,
                        item, 1);

                for (int i = 0; i < 5; i++) {
                    int star = 0;
                    int ratio = Util.nextInt(0, 100);

                    if (ratio < 55) { // 50% tỉ lệ ra đến 3 sao
                        star = Util.nextInt(1, 3);
                    } else if (ratio < 75) {// 30% tỉ lệ 4 đến 5 sao
                        star = Util.nextInt(3, 4);
                    } else if (ratio < 90) { // 10% tỉ lệ 6 sao
                        star = Util.nextInt(0, 3);
                    } else if (ratio >= 90 && ratio <= 100) {
                        star = 4;
                    }

                    short itemId = ConstItem.doSKHVip[i][select][12];
                    Item itemSKH = ItemService.gI().createNewItem((short) itemId);
                    RewardService.gI().initBaseOptionClothes(itemSKH);

                    if (itemSKH != null) {

                        itemSKH.itemOptions.add(new ItemOption(ConstOption.KY_GUI_VANG, 1));
                        if (star > 0) {
                            itemSKH.itemOptions.add(new ItemOption(107, star));
                        }
                        InventoryServiceNew.gI().addItemBag(player, itemSKH);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + itemSKH.template.name);
                    }
                }
            } else {
                Service.getInstance().sendThongBao(player, "Hành trang cần " + emtyBag + " đủ ô trống");
            }
        }
    }
    
    public void RuongThanLinhGender1(Player player, int select) {
    int emtyBag = 5;
    Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1460);
    if (item == null || item.quantity < 1) {
        return;
    }
    if (InventoryServiceNew.gI().getCountEmptyBag(player) > emtyBag) {
        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        for (int i = 0; i < 5; i++) {
            short itemId = ConstItem.doSKHVip[i][select][12];
            Item itemSKH = ItemService.gI().createNewItem(itemId);
            RewardService.gI().initBaseOptionClothes(itemSKH);
            if (itemSKH != null) {
                itemSKH.itemOptions.add(new ItemOption(ConstOption.KY_GUI_VANG, 1));
                itemSKH.itemOptions.add(new ItemOption(30, 1)); 
                InventoryServiceNew.gI().addItemBag(player, itemSKH);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + itemSKH.template.name);
            }
        }
    } else {
        Service.getInstance().sendThongBao(player, "Hành trang cần " + emtyBag + " đủ ô trống");
    }
}

    public void changePetPlanet(Player player, int select) {
    // select: 0 Trái đất, 1 Namếc, 2 Xayda
    if (player == null) return;

    // Validate lựa chọn
    if (select < 0 || select > 2) {
        return;
    }

    // Check có đệ
    if (player.pet == null) {
        Service.getInstance().sendThongBao(player, "Bạn chưa có đệ tử");
        return;
    }

    // Tìm item đổi hành tinh (1460)
    Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1347);
    if (item == null || item.quantity < 1) {
        return;
    }

    // Chặn nếu đệ đang mặc đồ
    for (int i = 0; i < player.pet.inventory.itemsBody.size(); i++) {
        Item it = player.pet.inventory.itemsBody.get(i);
        if (it != null && it.isNotNullItem()) {
            Service.getInstance().sendThongBao(player, "Hãy tháo hết trang bị trên người đệ tử");
            return;
        }
    }
    // Nếu đã cùng hành tinh thì thôi (tuỳ bạn, có thể bỏ)
    if (player.pet.gender == select) {
        Service.getInstance().sendThongBao(player, "Đệ tử đã ở hành tinh này rồi");
        return;
    }

    // ===== ĐỔI HÀNH TINH ĐỆ =====
    player.pet.gender = (byte) select;


    // Gửi lại info đệ (tuỳ server bạn có hàm nào phù hợp)
    ChangeMapService.gI().changeMapInYard(player, player.zone.map.mapId, -1,
                        player.location.x);

    // Trừ item
    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
    InventoryServiceNew.gI().sendItemBags(player);

    Service.getInstance().sendThongBao(player, "Đổi hành tinh đệ tử thành công");
}

//    public void learnSkill9(Player pl) {
//        Message msg;
//        try {
//            Item bikipskill = InventoryService.gI().findItemBag(pl, 1229);
//            if (bikipskill == null || bikipskill.quantity < 9999) {
//                Service.getInstance().sendThongBao(pl,
//                        "Hãy thu thập đủ x9999 bí kíp tuyệt kỹ bằng cách đánh quái ở hành tinh ngục tù!");
//                return;
//            }
//            byte idSkill = -1;
//            if (pl.gender == 0) {
//                idSkill = Skill.SUPER_KAME;
//            } else if (pl.gender == 1) {
//                idSkill = Skill.MAFUBA;
//            } else if (pl.gender == 2) {
//                idSkill = Skill.SUPER_ANTOMIC;
//            }
//            Skill curSkill = SkillUtil.getSkillbyId(pl, idSkill);
//            if (curSkill.point == 7) {
//                Service.getInstance().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
//            } else {
//                if (curSkill.point == 0) {
//                    curSkill = SkillUtil.createSkill(idSkill, 1);
//                    SkillUtil.setSkill(pl, curSkill);
//                    msg = Service.getInstance().messageSubCommand((byte) 23);
//                    msg.writer().writeShort(curSkill.skillId);
//                    pl.sendMessage(msg);
//                    msg.cleanup();
//                    Service.getInstance().sendThongBao(pl, "Bạn vừa học được kỹ năng " + curSkill.template.name);
//                } else {
//                    curSkill = SkillUtil.createSkill(idSkill,
//                            curSkill.point + 1);
//                    SkillUtil.setSkill(pl, curSkill);
//                    msg = Service.getInstance().messageSubCommand((byte) 62);
//                    msg.writer().writeShort(curSkill.skillId);
//                    pl.sendMessage(msg);
//                    msg.cleanup();
//                    Service.getInstance().sendThongBao(pl, "Bạn vừa nâng cấp thành công kỹ năng "
//                            + curSkill.template.name + " lên cấp " + curSkill.point);
//                }
//                InventoryService.gI().subQuantityItemsBag(pl, bikipskill, 9999);
//                InventoryService.gI().sendItemBags(pl);
//            }
//
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
//    }

    public void learnSkill9(Player pl) {
    Message msg = null;
    try {
        Item bikipskill = InventoryService.gI().findItemBag(pl, 1229);
        if (bikipskill == null || bikipskill.quantity < 9999) {
            Service.getInstance().sendThongBao(pl,
                    "Hãy thu thập đủ x9999 bí kíp tuyệt kỹ bằng cách đánh quái ở hành tinh ngục tù!");
            return;
        }

        byte idSkill;
        if (pl.gender == 0) {
            idSkill = Skill.SUPER_KAME;
        } else if (pl.gender == 1) {
            idSkill = Skill.MAFUBA;
        } else {
            idSkill = Skill.SUPER_ANTOMIC;
        }

        Skill curSkill = SkillUtil.getSkillbyId(pl, idSkill);

        if (curSkill.point >= 7) {
            Service.getInstance().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
            return;
        }

        int newPoint = (curSkill.point == 0) ? 1 : (curSkill.point + 1);
        Skill newSkill = SkillUtil.createSkill(idSkill, newPoint);
        SkillUtil.setSkill(pl, newSkill);

        // gửi packet cập nhật client giống hệt case 3
        msg = Service.getInstance().messageSubCommand((curSkill.point == 0) ? (byte) 23 : (byte) 62);
        msg.writer().writeShort(newSkill.skillId);
        pl.sendMessage(msg);
        msg.cleanup();
        msg = null;

        // trừ item và cập nhật túi
        InventoryService.gI().subQuantityItemsBag(pl, bikipskill, 9999);
        InventoryService.gI().sendItemBags(pl);

        // thông báo
        if (curSkill.point == 0) {
            Service.getInstance().sendThongBao(pl, "Bạn vừa học được kỹ năng " + newSkill.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn vừa nâng cấp thành công kỹ năng "
                    + newSkill.template.name + " lên cấp " + newSkill.point);
        }

    } catch (Exception e) {
        Log.error(NpcMethod.class, e); // hoặc class bạn đang đặt hàm
    } finally {
        if (msg != null) {
            try { msg.cleanup(); } catch (Exception ignored) {}
        }
    }
}


    public void RuongThanLinhGenderAndType(Player player, int idItem, int select) {
        // rương thần linh chọn hành tinh buff bẩn để bán

        int emtyBag = 1;
        int gender = player.gender;// trừ id ruiwng nhỏ nhất
        if (gender < 0 || gender > 4) {
            Service.getInstance().sendThongBao(player, "Có lỗi xảy ra");
            return;
        }
        Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, idItem);
        if (item == null || item.quantity < 1) {
            return;
        } else {

            if (InventoryServiceNew.gI().getCountEmptyBag(player) >= emtyBag) {
                // xóa rương
                InventoryServiceNew.gI().subQuantityItemsBag(player,
                        item, 1);
                int star = 0;
                int ratio = Util.nextInt(0, 100);

                if (ratio < 55) { // 50% tỉ lệ ra đến 3 sao
                    star = Util.nextInt(1, 3);
                } else if (ratio < 75) {// 30% tỉ lệ 4 đến 5 sao
                    star = Util.nextInt(3, 4);
                } else if (ratio < 90) { // 10% tỉ lệ 6 sao
                    star = Util.nextInt(0, 3);
                } else if (ratio >= 90 && ratio <= 100) {
                    star = 4;
                }

                short itemId = ConstItem.doSKHVip[select][gender][12];
                Item itemSKH = ItemService.gI().createNewItem((short) itemId);
                RewardService.gI().initBaseOptionClothes(itemSKH);

                if (itemSKH != null) {

                    itemSKH.itemOptions.add(new ItemOption(ConstOption.KY_GUI_VANG, 1));
                    itemSKH.itemOptions.add(new ItemOption(30, 1));
                    if (star > 0) {
                        itemSKH.itemOptions.add(new ItemOption(107, star));
                    }
                    InventoryServiceNew.gI().addItemBag(player, itemSKH);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + itemSKH.template.name);

                }
            } else {
                Service.getInstance().sendThongBao(player, "Hành trang cần " + emtyBag + " đủ ô trống");
            }

        }

    }

    public void OpenSKHKaio(Player player, int select) {
        // rương skh
        int level = 0;
        int emtyBag = 5;
        Item item = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1406);
        if (item == null || item.quantity < 1) {
            return;
        } else {
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == ConstOption.CAP) {
                    level = io.param;
                    break;
                }
            }
            if (level == 0) {
                emtyBag = 1;
            }
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > emtyBag) {
                // xóa rương
                InventoryServiceNew.gI().subQuantityItemsBag(player,
                        item, 1);
                // tạo item
                if (level == 0) {

                    short itemId = ConstItem.doSKHVip[Util.nextInt(0, 4)][player.gender][0];
                    Item itemSKH = ItemService.gI().createNewItem((short) itemId);
                    int skhId = 127 + (player.gender * 3) + select;
                    RewardService.gI().initBaseOptionClothes(itemSKH);
                    ItemService.gI().AddOptionSKH(itemSKH, skhId);
                    if (itemSKH != null) {
                        InventoryServiceNew.gI().addItemBag(player, itemSKH);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + itemSKH.template.name);
                    }

                } else {
                    for (int i = 0; i < 5; i++) {
                        short itemId = ConstItem.doSKHVip[i][player.gender][level - 1];
                        Item itemSKH = ItemService.gI().createNewItem((short) itemId);
                        int skhId = 127 + (player.gender * 3) + select;
                        RewardService.gI().initBaseOptionClothes(itemSKH);
                        ItemService.gI().AddOptionSKH(itemSKH, skhId);
                        if (itemSKH != null) {
                            InventoryServiceNew.gI().addItemBag(player, itemSKH);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + itemSKH.template.name);
                        }

                    }
                }

            } else {
                Service.getInstance().sendThongBao(player, "Hành trang cần " + emtyBag + " đủ ô trống");
            }
        }
    }

    public void show_list_top(Player player, int select) {
        switch (select) {
            case 0:
                Service.getInstance().showTopPower(player);
                break;
            case 1:
                Service.getInstance().showtopEvent(player);
                break;
        }

    }

    public void WhisRongXuong(Player player, int select) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        TransactionService.gI().cancelTrade(player);
        for (int i = 0; i < 7; i++) {
            Item nrx = InventoryService.gI().findItemBag(player, 702 + i);
            if (nrx == null || nrx.quantity < 1) {
                Item it = ItemService.gI().createNewItem((short) (702 + i));
                Service.getInstance().sendThongBao(player, "Bạn còn thiếu " + it.template.name);
                InventoryService.gI().subQuantityItemsBag(player, nrx, 1);
                return;
            }
        }
        switch (select) {
            case 0: {// capsule halloween
                subNroXuong(player);
                Item item = ItemService.gI().createNewItem((short) 818);
                InventoryService.gI().addItemBag(player, item, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item.template.name);

            }
            break;
            case 1: {// hộp quà halloween
                subNroXuong(player);
                Item item = ItemService.gI().createNewItem((short) 2012);
                InventoryService.gI().addItemBag(player, item, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item.template.name);

            }
            break;
            case 2: {// chiêu 2 đệ tử
                if (player.pet != null) {
                    if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                        subNroXuong(player);
                        player.pet.openSkill2();
                        Service.getInstance().sendThongBao(player,
                                "Thay chiêu đệ tử thành công");
                        if (player.pet.playerSkill.skills.get(2).skillId != -1) {
                            player.pet.openSkill3();
                        }
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                    return;
                }
            }
            break;
            case 3: {// hộp quà halloween

                if (InventoryServiceNew.gI().getCountEmptyBag(player) < 3) {
                    Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 3 ô trống trong hành trang");
                    return;
                }
                subNroXuong(player);
                Item item1 = ItemService.gI().createNewItem((short) 1994);
                Item item2 = ItemService.gI().createNewItem((short) 1995);
                Item item3 = ItemService.gI().createNewItem((short) 1996);
                InventoryService.gI().addItemBag(player, item1, 1);
                InventoryService.gI().addItemBag(player, item2, 1);
                InventoryService.gI().addItemBag(player, item3, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được Combo thuốc x2 3 4 sức mạnh");

            }
            break;
        }
    }

    private void subNroXuong(Player player) {
        for (int i = 0; i < 7; i++) {
            Item nrx = InventoryService.gI().findItemBag(player, 702 + i);
            if (nrx != null) {
                InventoryService.gI().subQuantityItemsBag(player, nrx, 1);
            }
        }
    }

    public boolean SummonDragonWhis_1_1(Player playerSummonShenron, int select) {
        switch (select) {
            case 0: // Giàu có\n+1 Ti\nVàng
                if (playerSummonShenron.inventory.gold < playerSummonShenron.inventory.LIMIT_GOLD) {
                    playerSummonShenron.inventory.gold += 1000000000L;
                } else {
                    playerSummonShenron.inventory.gold = playerSummonShenron.inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(playerSummonShenron);
                break;
            case 1: // Đẹp trai\nnhất vũ trụ
                if (InventoryService.gI().getCountEmptyBag(playerSummonShenron) > 0) {
                    byte gender = playerSummonShenron.gender;
                    Item avtVip = ItemService.gI().createNewItem((short) (gender == ConstPlayer.TRAI_DAT ? 227
                            : gender == ConstPlayer.NAMEC ? 228 : 229));
                    avtVip.itemOptions.add(new ItemOption(97, Util.nextInt(10, 50)));
                    avtVip.itemOptions.add(new ItemOption(77, Util.nextInt(5, 15)));
                    InventoryService.gI().addItemBag(playerSummonShenron, avtVip, 1);
                    InventoryService.gI().sendItemBags(playerSummonShenron);
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron, "Hành trang đã đầy");
                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
                break;
            case 2: // thay chiêu 2-3 đệ tử
                if (playerSummonShenron.pet != null) {
                    if (playerSummonShenron.pet.playerSkill.skills.get(1).skillId != -1) {
                        playerSummonShenron.pet.openSkill2();
                        if (playerSummonShenron.pet.playerSkill.skills.get(2).skillId != -1) {
                            playerSummonShenron.pet.openSkill3();

                        }
                    } else {
                        Service.getInstance().sendThongBao(playerSummonShenron,
                                "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                        SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                        return false;
                    }
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron, "Ngươi làm gì có đệ tử?");
                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
                break;
            case 3: // chí mạng +2%
                if (playerSummonShenron.nPoint.critg < 9) {
                    playerSummonShenron.nPoint.critg += 2;
                    Service.getInstance().point(playerSummonShenron);
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron,
                            "Điều ước này đã quá sức với ta, ta sẽ cho ngươi chọn lại");
                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
                break;
            case 4: // Thay\nChiêu 4\nĐệ tử
                if (playerSummonShenron.pet != null) {
                    if (playerSummonShenron.pet.playerSkill.skills.get(3).skillId != -1) {
                        playerSummonShenron.pet.openSkill4();
                        // if (playerSummonShenron.pet.playerSkill.skills.get(4).skillId != -1) {
                        // playerSummonShenron.pet.openSkill5();
                        // }
                    } else {

                        Service.getInstance().sendThongBao(playerSummonShenron,
                                "Ít nhất đệ tử ngươi phải có chiêu 4 chứ!");
                        SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                        return false;
                    }
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron, "Ngươi làm gì có đệ tử?");
                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
                break;
            case 5:
                break;
        }
        return true;
    }

    public boolean SummonDragonWhis_1_2(Player playerSummonShenron, int select) {
        switch (select) {
            case 0: // Găng tay\nđang mang\nlên 1 cấp"
            {
                Item ao = playerSummonShenron.inventory.itemsBody.get(2);
                if (ao.isNotNullItem()) {
                    int level = 0;
                    for (ItemOption io : ao.itemOptions) {
                        if (io.optionTemplate.id == 72) {
                            level = io.param;
                            if (level < 7) {
                                io.param++;
                            }
                            break;
                        }
                    }
                    if (level < 7) {
                        if (level == 0) {
                            ao.itemOptions.add(new ItemOption(72, 1));
                            ao.itemOptions.add(new ItemOption(205, 1));
                        }
                        for (ItemOption io : ao.itemOptions) {
                            if (io.optionTemplate.id == 0) {
                                io.param += (io.param * 10 / 100);
                                break;
                            }
                        }
                        InventoryServiceNew.gI().sendItemBody(playerSummonShenron);
                    } else {
                        Service.getInstance().sendThongBao(playerSummonShenron,
                                "Găng tay của ngươi đã đạt cấp tối đa");
                        SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                        return false;
                    }
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron,
                            "Ngươi hiện tại có đeo găng đâu");

                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
            }
            break;
            case 1: {
                if (playerSummonShenron.pet == null || playerSummonShenron == null) {
                    Service.getInstance().sendThongBao(playerSummonShenron,
                            "Ngươi hiện tại chưa có đệ tử");

                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
                Item ao = playerSummonShenron.pet.inventory.itemsBody.get(2);
                if (ao.isNotNullItem()) {
                    int level = 0;
                    for (ItemOption io : ao.itemOptions) {
                        if (io.optionTemplate.id == 72) {
                            level = io.param;
                            if (level < 7) {
                                io.param++;
                            }
                            break;
                        }
                    }
                    if (level < 7) {
                        if (level == 0) {
                            ao.itemOptions.add(new ItemOption(72, 1));
                            ao.itemOptions.add(new ItemOption(205, 1));
                        }
                        for (ItemOption io : ao.itemOptions) {
                            if (io.optionTemplate.id == 0) {
                                io.param += (io.param * 10 / 100);
                                break;
                            }
                        }
                        InventoryServiceNew.gI().sendItemBody(playerSummonShenron);
                        Service.getInstance().Send_Caitrang(playerSummonShenron.pet);
                    } else {
                        Service.getInstance().sendThongBao(playerSummonShenron,
                                "Găng tay của đệ tử đã đạt cấp tối đa");
                        SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                        return false;
                    }
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron,
                            "Đệ tử hiện tại có đeo găng đâu");

                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
            }
            break;
            case 2: // +200 tr smtn
                // if (playerSummonShenron.nPoint.power < 80000000000l) {
                Service.getInstance().sendThongBao(playerSummonShenron, "Điều ước không thể thực hiện vào hiện tại !");
                SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                return false;
//                   SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
//                Service.getInstance().addSMTN(playerSummonShenron, (byte) 2, 200000000,
//                        false);
            // } else {
            // Service.getInstance().sendThongBao(playerSummonShenron,
            // "Xin lỗi, điều ước này không thể thực hiện trong thời gian đua top");
            // SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
            // return false;
            // }
            // case 3: {
            // if (InventoryService.gI().getCountEmptyBag(playerSummonShenron) > 1) {
            // Item itemVip = ItemService.gI().createNewItem((short) 1433, 1);
            // InventoryService.gI().addItemBag(playerSummonShenron, itemVip, 1);
            // InventoryService.gI().sendItemBags(playerSummonShenron);
            // } else {
            // Service.getInstance().sendThongBao(playerSummonShenron, "Hành trang đã đầy");
            // SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
            // return false;
            // }
            // }
            // break;
            // case 4: { // +500\nphiếu\nđiểm thưởng
            // if (InventoryService.gI().getCountEmptyBag(playerSummonShenron) > 1) {
            // Item itemVip = ItemService.gI().createNewItem((short) 1362, 500);
            // InventoryService.gI().addItemBag(playerSummonShenron, itemVip, 1);
            // InventoryService.gI().sendItemBags(playerSummonShenron);
            // } else {
            // Service.getInstance().sendThongBao(playerSummonShenron, "Hành trang đã đầy");
            // SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
            // return false;
            // }
            // }
            // break;
            // hết
        }
        return true;
    }

    public boolean SummonDragonWhis_2_1(Player playerSummonShenron, int select) {
        switch (select) {
            case 0: // +150 ngọc
                playerSummonShenron.inventory.gem += 2000;
                PlayerService.gI().sendInfoHpMpMoney(playerSummonShenron);
                break;
            case 1: // +20 tr smtn
                if (playerSummonShenron.nPoint.power < 1) {
                    Service.getInstance().addSMTN(playerSummonShenron, (byte) 2, 20000000, false);
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron,
                            "Xin lỗi, điều ước này khó quá, ta không thể thực hiện.");
                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }

                break;
            case 2: // 2 tr vàng
                playerSummonShenron.inventory.addGold(200000000);
                PlayerService.gI().sendInfoHpMpMoney(playerSummonShenron);
                break;
        }
        return true;
    }

    public boolean SummonDragonWhis_3_1(Player playerSummonShenron, int select) {
        switch (select) {
            case 0: // +15 ngọc
                playerSummonShenron.inventory.gem += 200;
                PlayerService.gI().sendInfoHpMpMoney(playerSummonShenron);
                break;
            case 1: // +2 tr smtn
                Service.getInstance().addSMTN(playerSummonShenron, (byte) 2, 2000000, false);

                break;
            case 2: // 200k vàng
                playerSummonShenron.inventory.addGold(20000000);
                PlayerService.gI().sendInfoHpMpMoney(playerSummonShenron);
                break;
        }
        return true;
    }

    public boolean SummonDragonBlack_1(Player playerSummonShenron, int select) {
        switch (select) {
            case 0:
                if (InventoryService.gI().getCountEmptyBag(playerSummonShenron) > 0) {
                    Item ctGohan = ItemService.gI().createNewItem((short) 989);
                    ctGohan.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                    ctGohan.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                    ctGohan.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                    ctGohan.itemOptions.add(new ItemOption(5, Util.nextInt(20, 30)));
                    ctGohan.itemOptions.add(new ItemOption(47, Util.nextInt(5, 15)));

                    ctGohan.itemOptions.add(new ItemOption(93, 60));
                    InventoryService.gI().addItemBag(playerSummonShenron, ctGohan, 0);
                    InventoryService.gI().sendItemBags(playerSummonShenron);
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron, "Hành trang đã đầy");
                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
                break;
            case 1:
                if (InventoryService.gI().getCountEmptyBag(playerSummonShenron) > 0) {
                    Item ctBiden = ItemService.gI().createNewItem((short) 990);
                    ctBiden.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                    ctBiden.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                    ctBiden.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                    ctBiden.itemOptions.add(new ItemOption(5, Util.nextInt(20, 30)));
                    ctBiden.itemOptions.add(new ItemOption(47, Util.nextInt(5, 15)));

                    ctBiden.itemOptions.add(new ItemOption(93, 60));
                    InventoryService.gI().addItemBag(playerSummonShenron, ctBiden, 0);
                    InventoryService.gI().sendItemBags(playerSummonShenron);
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron, "Hành trang đã đầy");
                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
                break;
            case 2:
                if (InventoryService.gI().getCountEmptyBag(playerSummonShenron) > 0) {
                    Item ctConuong = ItemService.gI().createNewItem((short) 991);
                    ctConuong.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                    ctConuong.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                    ctConuong.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                    ctConuong.itemOptions.add(new ItemOption(5, Util.nextInt(20, 30)));
                    ctConuong.itemOptions.add(new ItemOption(47, Util.nextInt(5, 15)));

                    ctConuong.itemOptions.add(new ItemOption(93, 60));
                    InventoryService.gI().addItemBag(playerSummonShenron, ctConuong, 0);
                    InventoryService.gI().sendItemBags(playerSummonShenron);
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron, "Hành trang đã đầy");
                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
                break;
            case 3:
                if (InventoryService.gI().getCountEmptyBag(playerSummonShenron) > 0) {
                    Item PetThoOm = ItemService.gI().createNewItem((short) 1039);
                    PetThoOm.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
                    PetThoOm.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
                    PetThoOm.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
                    PetThoOm.itemOptions.add(new ItemOption(93, 60));
                    InventoryService.gI().addItemBag(playerSummonShenron, PetThoOm, 0);
                    InventoryService.gI().sendItemBags(playerSummonShenron);
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron, "Hành trang đã đầy");
                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
                break;
            case 4:
                if (InventoryService.gI().getCountEmptyBag(playerSummonShenron) > 0) {
                    Item PetThoMap = ItemService.gI().createNewItem((short) 1040);
                    PetThoMap.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
                    PetThoMap.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
                    PetThoMap.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
                    PetThoMap.itemOptions.add(new ItemOption(93, 60));
                    InventoryService.gI().addItemBag(playerSummonShenron, PetThoMap, 0);
                    InventoryService.gI().sendItemBags(playerSummonShenron);
                } else {
                    Service.getInstance().sendThongBao(playerSummonShenron, "Hành trang đã đầy");
                    SummonDragon.gI().reOpenShenronWishes(playerSummonShenron);
                    return false;
                }
                break;
        }
        return true;
    }

    public boolean SummonDragonICE_1(Player playerSummonShenron, int select) {
        if (playerSummonShenron == null) {
            System.err.println("Error: playerSummonShenron is null in SummonDragonICE_1");
            return false; // or handle appropriately
        }
        if (playerSummonShenron.itemTime == null) {
            //   playerSummonShenron.itemTime = new ItemTime(); // Ensure ItemTime has a no-arg constructor
            System.out.println("Warning: itemTime was null for player " + playerSummonShenron.name + ", initialized new ItemTime");
        }
        switch (select) {
            case 0: {//10% sd
                playerSummonShenron.itemTime.lastTimeUseGroup_7_1 = System.currentTimeMillis();
                playerSummonShenron.itemTime.isUseGroup_7_1 = true;
                Service.getInstance().point(playerSummonShenron);
                ItemTimeService.gI().sendAllItemTime(playerSummonShenron);
                InventoryService.gI().sendItemBags(playerSummonShenron);
            }
            break;
            case 1: {// 15% hp ki
                playerSummonShenron.itemTime.lastTimeUseGroup_7_2 = System.currentTimeMillis();
                playerSummonShenron.itemTime.isUseGroup_7_2 = true;
                Service.getInstance().point(playerSummonShenron);
                ItemTimeService.gI().sendAllItemTime(playerSummonShenron);
                InventoryService.gI().sendItemBags(playerSummonShenron);
            }
            break;
            case 2://100% tnsm sư
                playerSummonShenron.itemTime.lastTimeUseGroup_7_4 = System.currentTimeMillis();
                playerSummonShenron.itemTime.isUseGroup_7_4 = true;
                Service.getInstance().point(playerSummonShenron);
                ItemTimeService.gI().sendAllItemTime(playerSummonShenron);
                InventoryService.gI().sendItemBags(playerSummonShenron);
                break;
            case 3: {///100%  tnsm đệ
                playerSummonShenron.itemTime.lastTimeUseGroup_7_5 = System.currentTimeMillis();
                playerSummonShenron.itemTime.isUseGroup_7_5 = true;
                Service.getInstance().point(playerSummonShenron);
                ItemTimeService.gI().sendAllItemTime(playerSummonShenron);
                InventoryService.gI().sendItemBags(playerSummonShenron);

                break;
            }
        }
        return true;
    }
}
