package nro.services;

import nro.models.item.ItemOptionTemplate;
import nro.models.item.ItemTemplate;
import nro.attr.Attribute;
import nro.consts.ConstAttribute;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.consts.ConstOption;
import nro.consts.ConstPlayer;
import nro.consts.ConstTask;
import nro.consts.MapName;
import nro.jdbc.daos.PlayerDAO;
import nro.lib.RandomCollection;
import nro.models.clan.ClanMember;
import nro.models.consignment.ConsignmentItem;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.SantaCity;
import nro.models.map.Zone;
import nro.models.map.phoban.DoanhTrai;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.models.shop.ItemShop;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.server.SettingGame;
import nro.services.Event.EventService;
import nro.services.func.Chonaiday;
import nro.services.func.ChangeMapService;
import nro.services.func.ShopService;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NpcMenu {

    private static NpcMenu i;

    public static NpcMenu gI() {
        if (i == null) {
            i = new NpcMenu();
        }
        return i;
    }

    public void MenuDoanhTrai(Player player, Npc npc) {

        if (player.clan == null) {
            npc.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                    "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
        } else {

            if (!player.isAdmin() && player.clan.getMembers().size() < 5) {
                npc.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                        "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
            } else {
                ClanMember clanMember = player.clan.getClanMember((int) player.id);
                int days = (int) (((System.currentTimeMillis() / 1000)
                        - clanMember.joinTime)
                        / 60
                        / 60 / 24);
                if (!player.isAdmin() && days < 2) {
                    NpcService.gI().createTutorial(player, npc.avartar,
                            "Chỉ những thành viên gia nhập bang hội tối thiểu 2 ngày mới có thể tham gia");
                    return;
                }
                if (!player.clan.haveGoneDoanhTrai && player.clan.timeOpenDoanhTrai != 0) {
                    npc.createOtherMenu(player, ConstNpc.MENU_VAO_DT,
                            "Bang hội của ngươi đang đánh trại độc nhãn\n"
                            + "Thời gian còn lại là "
                            + TimeUtil.getTimeAgo(TimeUtil.getSecondLeft(
                                    player.clan.timeOpenDoanhTrai,
                                    DoanhTrai.TIME_DOANH_TRAI / 1000))
                            + ". Ngươi có muốn tham gia không?",
                            "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                } else {
                    List<Player> plSameClans = new ArrayList<>();
                    List<Player> playersMap = player.zone.getPlayers();
                    synchronized (playersMap) {
                        for (Player pl : playersMap) {
                            if (!pl.equals(player) && pl.clan != null
                                    && pl.clan.id == player.clan.id && pl.location.x >= 1285
                                    && pl.location.x <= 1645) {
                                plSameClans.add(pl);
                            }

                        }
                    }
                    if (plSameClans.size() >= 2 || player.isAdmin()) {
                        if (!player.isAdmin() && player.clanMember
                                .getNumDateFromJoinTimeToToday() < DoanhTrai.DATE_WAIT_FROM_JOIN_CLAN) {
                            npc.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                    "Bang hội chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                    "OK", "Hướng\ndẫn\nthêm");
                        } else if (player.clan.haveGoneDoanhTrai) {
                            npc.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                    "Bang hội của ngươi đã đi trại lúc "
                                    + Util.formatTime(player.clan.timeOpenDoanhTrai)
                                    + " hôm nay. Người mở\n" + "("
                                    + player.clan.playerOpenDoanhTrai.name
                                    + "). Hẹn ngươi quay lại vào ngày mai",
                                    "OK", "Hướng\ndẫn\nthêm");

                        } else {
                            npc.createOtherMenu(player, ConstNpc.MENU_CHO_VAO_DT,
                                    "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                                    + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                                    "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                        }
                    } else {
                        npc.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                "Ngươi phải có ít nhất 2 đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                + "Hahaha.",
                                "OK", "Hướng\ndẫn\nthêm");
                    }
                }
            }

        }
    }

    
    public void confim_Vegeta2(Npc npc, Player player, int select) {
            if (npc.mapId == 5) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0 ->  {
                            Item vp1 = null;
                            Item vp2 = null;
                            Item vp3 = null;
                            Item vp4 = null;
                            try {
                                vp1 = InventoryService.gI().findItemBagByTemp(player, 1170);
                                vp2 = InventoryService.gI().findItemBagByTemp(player, 1165);
                                vp3 = InventoryService.gI().findItemBagByTemp(player, 1167);
                                vp4 = InventoryService.gI().findItemBagByTemp(player, 1429);
                            } catch (Exception e) {
                            }
                            if (vp1 == null || vp1.quantity < 99) {
                                Service.getInstance().sendThongBaoOK(player, "Bạn không đủ Gói quà");
                            } else if (vp2 == null || vp2.quantity < 20) {
                                Service.getInstance().sendThongBaoOK(player, "Bạn không đủ Chuông đồng");
                            } else if (vp3 == null || vp3.quantity < 99) {
                                Service.getInstance().sendThongBaoOK(player, "Bạn không đủ Bánh quy");
                            } else if (vp4 == null || vp4.quantity < 3000) {
                                Service.getInstance().sendThongBaoOK(player, "Bạn không đủ Thỏi Vàng Khóa");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, vp1, 99);
                                InventoryService.gI().subQuantityItemsBag(player, vp2, 20);
                                InventoryService.gI().subQuantityItemsBag(player, vp3, 99);
                                InventoryService.gI().subQuantityItemsBag(player, vp4, 3000);
                                Service.getInstance().sendMoney(player);
                                Item hopquanoel = ItemService.gI().createNewItem((short) 648);
                                InventoryService.gI().addItemBag(player, hopquanoel, 1);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + hopquanoel.template.name);
                            }
                        }
                        case 1 ->  {
                            Item vp1 = null;
                            Item vp2 = null;
                            Item vp3 = null;
                            Item vp4 = null;
                            try {
                                vp1 = InventoryService.gI().findItemBagByTemp(player, 1168);
                                vp2 = InventoryService.gI().findItemBagByTemp(player, 1169);
                                vp3 = InventoryService.gI().findItemBagByTemp(player, 1299);
                                vp4 = InventoryService.gI().findItemBagByTemp(player, 1429);
                            } catch (Exception e) {
                            }
                            if (vp1 == null || vp1.quantity < 99) {
                                Service.getInstance().sendThongBaoOK(player, "Bạn không đủ Kẹo đường");
                            } else if (vp2 == null || vp2.quantity < 99) {
                                Service.getInstance().sendThongBaoOK(player, "Bạn không đủ Kẹo người tuyết");
                            } else if (vp3 == null || vp3.quantity < 50) {
                                Service.getInstance().sendThongBaoOK(player, "Bạn không đủ Dây Buộc");
                            } else if (vp4 == null || vp4.quantity < 5000) {
                                Service.getInstance().sendThongBaoOK(player, "Bạn không đủ Thỏi vàng khóa");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, vp1, 99);
                                InventoryService.gI().subQuantityItemsBag(player, vp2, 99);
                                InventoryService.gI().subQuantityItemsBag(player, vp3, 50);
                                InventoryService.gI().subQuantityItemsBag(player, vp4, 5000);
                                Service.getInstance().sendMoney(player);
                                Item hopquanoel = ItemService.gI().createNewItem((short) 1171);
                                InventoryService.gI().addItemBag(player, hopquanoel, 1);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + hopquanoel.template.name);
                            }
                        }
                        case 3 -> {
                            ChangeMapService.gI().changeMapInYard(player, 199, -1,
                                 Util.nextInt(300, 500));
                        }  
                        case 2 ->  {
                            Item vp1 = null;
                            try {
                                vp1 = InventoryService.gI().findItemBagByTemp(player, 1166);
                            } catch (Exception e) {
                            }
                            if (vp1 == null || vp1.quantity < 10) {
                                Service.getInstance().sendThongBaoOK(player, "Bạn không đủ X10 Cá Tuyết");
                            } else {
                                InventoryService.gI().subQuantityItemsBag(player, vp1, 10);
                                Service.getInstance().sendMoney(player);
                                Item hopquanoel = ItemService.gI().createNewItem((short) 1972);
                                InventoryService.gI().addItemBag(player, hopquanoel, 1);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + hopquanoel.template.name);
                            }
                        }
                        case 4 -> {
//                            Service.getInstance().showtopEvent(player);
                        }                               
                    }
                }
            } 
    }

    public void confirm_may_gap_thu(Npc npc, Player player, int select) {
        if (npc.mapId == 5) {

            if (player.iDMark.getIndexMenu() == 1234) {
                switch (select) {
                    case 0:
                        npc.createOtherMenu(player, 12345,
                                "GẮP THƯỜNG : 1-5% CHỈ SỐ\n|3|GẮP X1 : GẮP THỦ CÔNG\nGẮP X10 : AUTO X10 LẦN GẮP\nGẮP X100 : AUTO X100 LẦN GẮP\n"
                                + "|7|LƯU Ý : MỌI CHỈ SỐ ĐỀU RANDOM KHÔNG CÓ OPTION NHẤT ĐỊNH",
                                "Gắp x1", "Gắp x10", "Gắp x100", "Rương Đồ");
                        break;
                    case 1:
                        npc.createOtherMenu(player, 12346,
                                "GẮP CAO CẤP : 5-8% CHỈ SỐ\n|3|GẮP X1 : GẮP THỦ CÔNG\nGẮP X10 : AUTO X10 LẦN GẮP\nGẮP X100 : AUTO X100 LẦN GẮP\n"
                                + "|7|LƯU Ý : MỌI CHỈ SỐ ĐỀU RANDOM KHÔNG CÓ OPTION NHẤT ĐỊNH",
                                "Gắp x1", "Gắp x10", "Gắp x100", "Rương Đồ");
                        break;
                    case 2:
                        npc.createOtherMenu(player, 12347,
                                "GẮP VIP : 8-12% CHỈ SỐ\n|3|GẮP X1 : GẮP THỦ CÔNG\nGẮP X10 : AUTO X10 LẦN GẮP\nGẮP X100 : AUTO X100 LẦN GẮP\n"
                                + "|7|LƯU Ý : MỌI CHỈ SỐ ĐỀU RANDOM KHÔNG CÓ OPTION NHẤT ĐỊNH",
                                "Gắp x1", "Gắp x10", "Gắp x100", "Rương Đồ");
                        break;
                    case 3:
                        // Service.getInstance().sendThongBaoFromAdmin(player,
                        // "Số điểm đã gắp của bạn : " + player.GapthuPoint);
                        Service.getInstance().showTopGapthu(player);
                        break;
                    case 4:
                        npc.createOtherMenu(player, ConstNpc.RUONG_DO,
                                "|1|Rương phụ chứa vật phẩm mà ngươi kiếm được từ vận may",
                                "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                - InventoryServiceNew.gI().getCountEmptyListItem(
                                        player.inventory.itemsBoxCrackBall))
                                + "/110)",
                                "Xóa Hết\nRương Phụ", "Đóng");
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 12345) { // thường
                switch (select) {
                    case 0:

                        RewardService.gI().openVongQuayLTN_THUONG(player, 1, npc);

                        break;
                    case 1:

                        RewardService.gI().openVongQuayLTN_THUONG(player, 10, npc);

                        break;
                    case 2:
                        RewardService.gI().openVongQuayLTN_THUONG(player, 100, npc);

                        break;
                    case 3:
                        npc.createOtherMenu(player, ConstNpc.RUONG_DO,
                                "|1|Rương phụ chứa vật phẩm mà ngươi kiếm được từ vận may",
                                "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                - InventoryServiceNew.gI().getCountEmptyListItem(
                                        player.inventory.itemsBoxCrackBall))
                                + "/110)",
                                "Xóa Hết\nRương Phụ", "Đóng");
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 12346) { // cao cấp
                switch (select) {
                    case 0:
                        RewardService.gI().openVongQuayLTN_CAO_CAP(player, 1, npc);
                        break;
                    case 1:
                        RewardService.gI().openVongQuayLTN_CAO_CAP(player, 10, npc);
                        break;
                    case 2:
                        RewardService.gI().openVongQuayLTN_CAO_CAP(player, 100, npc);
                        break;
                    case 3:
                        npc.createOtherMenu(player, ConstNpc.RUONG_DO,
                                "|1|Tình yêu như một dây đàn\n"
                                + "Tình vừa được thì đàn đứt dây\n"
                                + "Đứt dây này anh thay dây khác\n"
                                + "Mất em rồi anh biết thay ai?",
                                "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                - InventoryServiceNew.gI().getCountEmptyListItem(
                                        player.inventory.itemsBoxCrackBall))
                                + "/110)",
                                "Xóa Hết\nRương Phụ", "Đóng");
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 12347) {
                switch (select) {
                    case 0:
                        RewardService.gI().openVongQuayLTN_VIP(player, 1, npc);
                        break;
                    case 1:
                        RewardService.gI().openVongQuayLTN_VIP(player, 10, npc);
                        break;
                    case 2:
                        RewardService.gI().openVongQuayLTN_VIP(player, 100, npc);
                        break;
                    case 3:
                        npc.createOtherMenu(player, ConstNpc.RUONG_DO,
                                "|4|Tình yêu như một dây đàn\n"
                                + "Tình vừa được thì đàn đứt dây\n"
                                + "Đứt dây này anh thay dây khác\n"
                                + "Mất em rồi anh biết thay ai?",
                                "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                - InventoryServiceNew.gI().getCountEmptyListItem(
                                        player.inventory.itemsBoxCrackBall))
                                + "/110)",
                                "Xóa Hết\nRương Phụ", "Đóng");
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.RUONG_DO) {
                switch (select) {
                    case 0:
                        ShopService.gI().openBoxItemLuckyRound(player);
                        break;
                    case 1:
                        NpcService.gI().createMenuConMeo(player,
                                ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, npc.avartar,
                                "|3|Bạn chắc muốn xóa hết vật phẩm trong rương phụ?\n"
                                + "|7|Sau khi xóa sẽ không thể khôi phục!",
                                "Đồng ý", "Hủy bỏ");
                        break;
                }
            }
        }
    }

    public void confim_Vegeta(Npc npc, Player player, int select) {
        if (npc.mapId == 5) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0: // nghĩa địa
                        ChangeMapService.gI().changeMapInYard(player, 167, -1, 143);
                        // Service.getInstance().sendThongBao(player,
                        // "Không thể vào map khi đang đua top !");
                        break;
                    case 1:// thung lũng xanh
                        ChangeMapService.gI().changeMapInYard(player, 168, -1, 143);
                        // Service.getInstance().sendThongBao(player,
                        // "Không thể vào map khi đang đua top !");
                        break;
                    case 2:// không gian máu
                        ChangeMapService.gI().changeMapInYard(player, 171, -1, 143);
                        break;
                    case 3: // hầm mỏ
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        if (hour % 2 == 0) {
                            if (player.nPoint.power >= 10_000_000_000L) {
                                ChangeMapService.gI().changeMapInYard(player, 172, -1, -1);
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Sức mạnh hiện tại của bạn không đủ , hãy rèn luyện thêm trước khi vào map !");
                            }
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Khu hầm mỏ đang được phục hồi ,trở lại vào khung giờ chẵn !"
                                    + "\n Hiện tại ta không cho ngươi qua được !",
                                    "Đóng");
                        }
                        break;
                    case 4:
                        break;
                }
            }
        } else if (npc.mapId == 167 || npc.mapId == 170) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        ChangeMapService.gI().changeMapInYard(player, 5, -1, 143);
                        break;
                    case 1:

                        break;
                }
            }
        } else if (npc.mapId == 168) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        short cost = 1;
                        if (ItemService.gI().SubThoiVang(player, cost)) {
                            player.charms.addTimeCharms(2025, 61);
                            Service.getInstance().sendMoney(player);
                            Service.getInstance().sendThongBao(player,
                                    "Thành công, đệ tử của ngươi được tăng thêm 1h x2 tnsm khi đánh quái tại đây, đệ tử sẽ tự dùng đậu khi hết thể lực");
                        }
                        break;
                    case 1:
                        short cost2 = 5;
                        if (ItemService.gI().SubThoiVang(player, cost2)) {
                            player.charms.addTimeCharms(2076, 61);
                            Service.getInstance().sendMoney(player);
                            Service.getInstance().sendThongBao(player,
                                    "Thành công, đệ tử của ngươi được tăng thêm 1h x3 tnsm khi đánh quái tại đây, đệ tử sẽ tự dùng đậu khi hết thể lực");
                        }
                        break;
                    case 2:
                        ChangeMapService.gI().changeMapInYard(player, 5, -1, 143);
                        break;
                }
            }
        } else if (npc.mapId == 171) {
            switch (select) {
                case 0:
                    ChangeMapService.gI().changeMap(player, 5, -1, -1, -1);
                    break;
            }
        } else if (npc.mapId == 172) {
            if (select == 0) {
                ChangeMapService.gI().changeMap(player, 5, -1, -1, -1);
            }
        }
    }

    public void confirm_cay_uoc_nguyen(Npc npc, Player player, int select) {
        if (player.iDMark.isBaseMenu()) {
            switch (select) {
                case 0: {
                    Item lden = InventoryService.gI().findItemBagByTemp(player, 1264);
                    if (lden != null && lden.quantity > 0) {
                        if (player.lastTimeWish > 0) {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Thời gian ước nguyện hiên tại còn : "
                                    + Util.msToTime(player.lastTimeWish)
                                    + " vui lòng đợi hết thời gian ước nguyện",
                                    "Đóng");
                        }
                        InventoryService.gI().subQuantityItemsBag(player, lden, 1);
                        player.lastTimeWish = System.currentTimeMillis()
                                + (1000 * 60 * 60 * 24 * 1);
                        player.isWish = true;
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Đã trao thành công Lồng Đèn Ước Nguyện , vui lòng đợi 24h sau");
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Số lượng lồng đèn không đủ để ước");
                    }

                }
                break;
            }
        }
        if (player.iDMark.getIndexMenu() == 12) {
            switch (select) {
                case 0:
                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                        // hành trang
                        player.isWish = false;
                        int listIt[] = {16, 17, 342, 343, 344, 345, 1271};
                        int idItem = listIt[Util.nextInt(listIt.length)];
                        Item meoBiNgo = ItemService.gI().createNewItem((short) idItem);
                        if (idItem == 1271) {
                            meoBiNgo.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                            meoBiNgo.itemOptions.add(new ItemOption(77, Util.nextInt(5, 15)));
                            meoBiNgo.itemOptions.add(new ItemOption(103, Util.nextInt(5, 15)));
                            if (Util.isTrue(90, 100)) {
                                meoBiNgo.itemOptions
                                        .add(new ItemOption(93, Util.nextInt(1, 3)));
                            }
                        }

                        InventoryService.gI().addItemBag(player, meoBiNgo, 1);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn vư nhần được " + meoBiNgo.template.name);
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Cần 1 ô trống trong hành trang");

                    }
                    break;
            }
        }
        if (player.iDMark.getIndexMenu() == 13) {
            switch (select) {
                case 0:
                    if (player.inventory.gold >= 1_000_000_000) {
                        player.inventory.gold -= 1_000_000_000;
                        player.lastTimeWish = 0;
                        Service.getInstance().sendThongBao(player,
                                "Ước nguyện thành công , vui lòng kiểm tra cây ước nguyện");
                        Service.getInstance().sendMoney(player);
                    } else {
                        npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Yêu cầu tối thiểu có đủ 1 tỷ vàng", "Đóng");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void confirm_bardock(Npc npc, Player player, int select) {
        if (npc.mapId == 0 || npc.mapId == 7 || npc.mapId == 14) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        Service.getInstance().showTopPower(player);
                        break;
                    case 1:
                        Service.getInstance().showTopNVU(player);
                        break;
                    case 2:
                        Service.getInstance().showTopNap(player);
                        break;
//                    case 3:
//                        Service.getInstance().showTopThoiVang(player);
//                        break;
                    case 4:
                        Service.getInstance().showTopPauCua(player);
                        break;
                    case 5:
                        Service.getInstance().showTopBossp(player);
                        System.err.println("zo");
                        break;
                }
            }
        } else if (npc.mapId == 173) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        ChangeMapService.gI().changeMapInYard(player, 5, -1, 250);
                        break;
                    case 1:
                        // if (TaskService.gI().getIdTask(player) < ConstTask.TASK_26_0) {
                        // Service.getInstance().sendThongBao(player,
                        // "Yêu cầu hoàn thành nhiệm vụ xên hoàn thiện ở thị trấn Ginder");
                        // return;
                        // }
                        if (player.inventory.gold >= 5000000000l) {
                            player.inventory.gold -= 5000000000l;
                            player.charms.addTimeCharms(2025, 11);
                            Service.getInstance().sendMoney(player);
                            Service.getInstance().sendThongBao(player,
                                    "Phù sức mạnh thành công, ngươi được tăng x10 tiềm năng sức mạnh trong 10 phút khi đánh quái tại đây");
                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Ngươi không đủ vàng, cần 5 tỷ vàng để phù");
                        }

                        break;
                }
            }
        } else if (npc.mapId == 163) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        ChangeMapService.gI().changeMapBySpaceShip(player, 154, -1, 800);
                        break;
                    case 1:
                        break;
                }
            }
        }
    }

    public void confirm_duong_tang(Npc npc, Player player, int select) {
        if (npc.mapId == MapName.LANG_ARU) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        // if (TaskService.gI().getIdTask(player) < ConstTask.TASK_26_0) {

                        // Service.getInstance().sendThongBao(player,
                        // "Hãy hoàn thành nhiệm vụ tiêu diệt xên trước");
                        // return;
                        // }
                        // Item ve_vao = InventoryService.gI().findItemBag(player,
                        // ConstItem.QUA_HONG_DAO);
                        // if (ve_vao != null && ve_vao.quantity >= 1) {
                        // if (!Manager.gI().getGameConfig().isOpenPrisonPlanet()) {
                        // Service.getInstance().sendThongBao(player,
                        // "Lối vào ngũ hành sơn chưa mở");
                        // return;
                        // }
                        // InventoryService.gI().subQuantityItemsBag(player, ve_vao, 1);
                        // InventoryService.gI().sendItemBags(player);
                        ChangeMapService.gI().changeMapBySpaceShip(player, 124, -1, 100);
                        // } else {
                        // Service.getInstance().sendThongBao(player,
                        // "Cần 1 quả hồng đào để vào map");
                        // }

                        break;
                }
            }
        }
        if (npc.mapId == MapName.NGU_HANH_SON) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0: // về làng aru
                        Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, 0, 0);
                        if (zone != null) {
                            player.location.x = 600;
                            player.location.y = 432;
                            MapService.gI().goToMap(player, zone);
                            Service.getInstance().clearMap(player);
                            zone.mapInfo(player);
                            player.zone.loadAnotherToMe(player);
                            player.zone.load_Me_To_Another(player);
                        }
                        break;
                    case 1:
                        EventService.gI().NguHanhSon_2(player, 1);
                        // if (player.inventory.gold >= 5000000000l) {
                        // player.inventory.gold -= 5000000000l;
                        // player.charms.addTimeCharms(2025, 61);
                        // Service.getInstance().sendThongBao(player,
                        // "Phù sức mạnh thành công, con được tăng x10 tnsm trong 10 phút");
                        // } else {
                        // Service.getInstance().sendThongBao(player,
                        // "Con không đủ vàng, cần 5 tỷ vàng để thực hiện");
                        // }

                        break;
                }
            }
        } else if (npc.mapId == MapName.NGU_HANH_SON_3) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0: // về làng aru
                        // EventService.gI().NguHanhSon_1(player, 1);
                        break;
                    case 1:

                        // Service.getInstance().sendThongBao(player,
                        // "Đợi ta xíu, tính năng đang cập nhật");
                        break;
                }
            }
        }
        // if (npc.mapId == MapName.NGU_HANH_SON) {
        // if (player.iDMark.isBaseMenu()) {
        // switch (select) {
        // case 0:
        // // Đổi đào
        // Item item = InventoryService.gI().findItemBagByTemp(player,
        // ConstItem.QUA_HONG_DAO);
        // if (item == null || item.quantity < 10) {
        // npc.npcChat(player,
        // "Cần 10 quả đào xanh để đổi lấy đào chín từ bần tăng.");
        // return;
        // }
        // if (InventoryService.gI().getCountEmptyBag(player) == 0) {
        // npc.npcChat(player, "Túi đầy rồi kìa.");
        // return;
        // }
        // Item newItem = ItemService.gI()
        // .createNewItem((short) ConstItem.QUA_HONG_DAO_CHIN, 1);
        // InventoryService.gI().subQuantityItemsBag(player, item, 10);
        // InventoryService.gI().addItemBag(player, newItem, 0);
        // InventoryService.gI().sendItemBags(player);
        // npc.npcChat(player,
        // "Ta đã đổi cho thí chủ rồi đó, hãy mang cho đệ tử ta đi nào.");
        // break;

        // case 1:
        // // giải phong ấn
        // if (InventoryService.gI().getCountEmptyBag(player) == 0) {
        // npc.npcChat(player, "Túi đầy rồi kìa.");
        // return;
        // }
        // int[] itemsNeed = { ConstItem.CHU_GIAI, ConstItem.CHU_KHAI,
        // ConstItem.CHU_PHONG, ConstItem.CHU_AN };
        // List<Item> items = InventoryService.gI().getListItem(player, itemsNeed)
        // .stream().filter(i -> i.quantity >= 10)
        // .collect(Collectors.toList());
        // boolean[] flags = new boolean[4];
        // for (Item i : items) {
        // switch ((int) i.template.id) {
        // case ConstItem.CHU_GIAI:
        // flags[0] = true;
        // break;
        // case ConstItem.CHU_KHAI:
        // flags[1] = true;
        // break;
        // case ConstItem.CHU_PHONG:
        // flags[2] = true;
        // break;
        // case ConstItem.CHU_AN:
        // flags[3] = true;
        // break;
        // }
        // }
        // for (int i = 0; i < flags.length; i++) {
        // if (!flags[i]) {
        // ItemTemplate template = ItemService.gI()
        // .getTemplate(itemsNeed[i]);
        // npc.npcChat("Thí chủ còn thiếu " + template.name);
        // return;
        // }
        // }
        // for (Item i : items) {
        // InventoryService.gI().subQuantityItemsBag(player, i, 10);
        // }
        // RandomCollection<Integer> rc = new RandomCollection<>();
        // rc.add(10, ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU);
        // rc.add(10, ConstItem.CAI_TRANG_BAT_GIOI_DE_TU);
        // rc.add(50, ConstItem.GAY_NHU_Y);
        // switch (player.gender) {
        // case ConstPlayer.TRAI_DAT:
        // rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG);
        // break;
        // case ConstPlayer.NAMEC:
        // rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG_545);
        // break;
        // case ConstPlayer.XAYDA:
        // rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG_546);
        // break;
        // }
        // int itemID = rc.next();
        // Item nItem = ItemService.gI().createNewItem((short) itemID);
        // boolean all = itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU
        // || itemID == ConstItem.CAI_TRANG_BAT_GIOI_DE_TU
        // || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG
        // || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_545
        // || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_546;
        // if (all) {
        // nItem.itemOptions.add(new ItemOption(50, Util.nextInt(20, 35)));
        // nItem.itemOptions.add(new ItemOption(77, Util.nextInt(20, 35)));
        // nItem.itemOptions.add(new ItemOption(103, Util.nextInt(20, 35)));
        // nItem.itemOptions.add(new ItemOption(94, Util.nextInt(5, 10)));
        // nItem.itemOptions.add(new ItemOption(100, Util.nextInt(10, 20)));
        // nItem.itemOptions.add(new ItemOption(101, Util.nextInt(10, 20)));
        // }
        // if (itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG
        // || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_545
        // || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_546) {
        // nItem.itemOptions.add(new ItemOption(80, Util.nextInt(5, 15)));
        // nItem.itemOptions.add(new ItemOption(81, Util.nextInt(5, 15)));
        // nItem.itemOptions.add(new ItemOption(106, 0));
        // } else if (itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU
        // || itemID == ConstItem.CAI_TRANG_BAT_GIOI_DE_TU) {
        // nItem.itemOptions.add(new ItemOption(197, 0));
        // }
        // if (all) {
        // if (Util.isTrue(499, 500)) {
        // nItem.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
        // }
        // } else if (itemID == ConstItem.GAY_NHU_Y) {
        // RandomCollection<Integer> rc2 = new RandomCollection<>();
        // rc2.add(60, 30);
        // rc2.add(30, 90);
        // rc2.add(10, 365);
        // nItem.itemOptions.add(new ItemOption(93, rc2.next()));
        // }
        // InventoryService.gI().addItemBag(player, nItem, 0);
        // InventoryService.gI().sendItemBags(player);
        // npc.npcChat(player.zone,
        // "A mi phò phò, đa tạ thí chủ tương trợ, xin hãy nhận món quà mọn này, bần
        // tăng sẽ niệm chú giải thoát cho Ngộ Không");
        // break;
        // }
        // }
        // }
    }

    public void confirm_Tapion(Npc npc, Player player, int select) {
        {
            if (npc.mapId == 19) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            SantaCity santaCity = (SantaCity) MapService.gI().getMapById(126);
                            if (santaCity != null) {
                                if (!santaCity.isOpened() || santaCity.isClosed()) {
                                    Service.getInstance().sendThongBao(player,
                                            "Hẹn gặp bạn lúc 22h mỗi ngày");
                                    return;
                                }
                                santaCity.enter(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Có lỗi xảy ra!");
                            }
                            break;
                    }
                }
            }
            if (npc.mapId == 126) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            SantaCity santaCity = (SantaCity) MapService.gI().getMapById(126);
                            if (santaCity != null) {
                                santaCity.leave(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Có lỗi xảy ra!");
                            }
                            break;
                    }
                }
            }
        }
    }

    public void confirm_Ly_Tieu_Nuong_Bau_Cua(Npc npc, Player player, int select) {
        if (npc.mapId == 5) {
            if (player.iDMark.isBaseMenu()) {

            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_BAUCUA) {
                switch (select) {
                    case 0:
                        String time = ((Chonaiday.gI().lastTimeEnd
                                - System.currentTimeMillis()) / 1000) + " giây";

                        if (((Chonaiday.gI().lastTimeEnd - System.currentTimeMillis())
                                / 1000) > 5) {
                            npc.createOtherMenu(player, ConstNpc.MENU_BAUCUA,
                                    "|2| Trò chơi chọn chiến binh thi đấu đang được diễn ra"
                                    + "\n Mua vé và dự đoán đúng, kết quả bạn sẽ nhận được thưởng lớn\nHãy tham gia ngay"
                                    + "\n\n|7| Kết quả kỳ trước -> "
                                    + Chonaiday.gI().getNameBauCua(Chonaiday.gI().x)
                                    + " : "
                                    + Chonaiday.gI().getNameBauCua(Chonaiday.gI().y)
                                    + " : "
                                    + Chonaiday.gI().getNameBauCua(Chonaiday.gI().z)
                                    + "\n\n|6|Tổng Bầu :"
                                    + Chonaiday.gI().TotalGoldBau
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldBau + ")"
                                    + "\nTổng Cua : " + Chonaiday.gI().TotalGoldCua
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldCua + ")"
                                    + "\nTổng Tôm : " + Chonaiday.gI().TotalGoldTom
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldTom + ")"
                                    + "\nTổng Cá : " + Chonaiday.gI().TotalGoldCA
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldCa + ")"
                                    + "\nTổng Hươu : " + Chonaiday.gI().TotalHuou
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldHuou + ")"
                                    + "\nTổng Gà : " + Chonaiday.gI().TotalGa
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldGa + ")"
                                    + "\n\nThời gian còn lại : " + time,
                                    "Update", "Bầu", "Cua", "Tôm", "Cá", "Hươu", "Gà",
                                    "Hướng dẫn", "Đóng");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.MENU_BAUCUA,
                                    "|2| Trò chơi chọn chiến binh thi đấu đang được diễn ra"
                                    + "\n Mua vé và dự đoán đúng, kết quả bạn sẽ nhận được thưởng lớn\nHãy tham gia ngay"
                                    + "\n\n|7| Kết quả kỳ trước -> "
                                    + Chonaiday.gI().getNameBauCua(Chonaiday.gI().x)
                                    + " : "
                                    + Chonaiday.gI().getNameBauCua(Chonaiday.gI().y)
                                    + " : "
                                    + Chonaiday.gI().getNameBauCua(Chonaiday.gI().z)
                                    + "\n\n|6|Tổng Bầu :"
                                    + Chonaiday.gI().TotalGoldBau
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldBau + ")"
                                    + "\nTổng Cua : " + Chonaiday.gI().TotalGoldCua
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldCua + ")"
                                    + "\nTổng Tôm : " + Chonaiday.gI().TotalGoldTom
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldTom + ")"
                                    + "\nTổng Cá : " + Chonaiday.gI().TotalGoldCA
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldCa + ")"
                                    + "\nTổng Hươu : " + Chonaiday.gI().TotalHuou
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldHuou + ")"
                                    + "\nTổng Gà : " + Chonaiday.gI().TotalGa
                                    + "\t\t( Tổng thỏi vàng :  " + player.GoldGa + ")"
                                    + "\n\nThời gian còn lại : " + time,
                                    "Update", "Bầu", "Cua", "Tôm", "Cá", "Hươu", "Gà",
                                    "Hướng dẫn", "Đóng");
                        }
                        break;
                    case 1:
                        npc.createOtherMenu(player, ConstNpc.MENU_BAU,
                                "|6| -> Lựa chọn Bầu <-\n|7|Lựa chọn loại vé mà con mong muống \n Mức vé càng cao tym đập càng hồi hộp",
                                "1 Thỏi", "10 Thỏi", "100 Thỏi ", "Đóng");
                        break;
                    case 2:
                        npc.createOtherMenu(player, ConstNpc.MENU_CUA,
                                "|6| -> Lựa chọn Cua <-\n|7|Lựa chọn loại vé mà con mong muống \n Mức vé càng cao tym đập càng hồi hộp",
                                "1 Thỏi", "10 Thỏi", "100 Thỏi ", "Đóng");
                        break;
                    case 3:
                        npc.createOtherMenu(player, ConstNpc.MENU_TOM,
                                "|6| -> Lựa chọn Tôm <-\n|7|Lựa chọn loại vé mà con mong muống \n Mức vé càng cao tym đập càng hồi hộp",
                                "1 Thỏi", "10 Thỏi", "100 Thỏi ", "Đóng");
                        break;
                    case 4:
                        npc.createOtherMenu(player, ConstNpc.MENU_CA,
                                "|6| -> Lựa chọn Cá <-\n|7|Lựa chọn loại vé mà con mong muống \n Mức vé càng cao tym đập càng hồi hộp",
                                "1 Thỏi", "10 Thỏi", "100 Thỏi ", "Đóng");
                        break;
                    case 5:
                        npc.createOtherMenu(player, ConstNpc.MENU_HUOU,
                                "|6| -> Lựa chọn Hươu <-\n|7|Lựa chọn loại vé mà con mong muống \n Mức vé càng cao tym đập càng hồi hộp",
                                "1 Thỏi", "10 Thỏi", "100 Thỏi ", "Đóng");
                        break;
                    case 6:
                        npc.createOtherMenu(player, ConstNpc.MENU_GA,
                                "|6| -> Lựa chọn Gà <-\n|7|Lựa chọn loại vé mà con mong muống \n Mức vé càng cao tym đập càng hồi hộp",
                                "1 Thỏi", "10 Thỏi", "100 Thỏi ", "Đóng");
                        break;
                    case 7:
                        Service.getInstance().sendThongBaoOK(player,
                                "Mỗi 60S, NPC Lý Tiểu Nương Sẽ Đổ Chọn 3 chiến binh\n"
                                + "Mỗi Chiến binh sẽ ngẫu nhiên 1 trong 6 Chiến binh trên trên(Có thể giống nhau)\n"
                                + "Với mỗi Chiến binh ra ngay chiến binh bạn chọn, bạn sẽ nhận được thỏi vàng tương đương (+ số vàng đã đặt)\n"
                                + "Ví dụ: nếu bạn đặt Xayda 10 thỏi vàng, và có 2 thẻ là Xayda thì bạn sẽ nhận được 30 thỏi vàng\n");
                        break;
                }

            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_BAU) {
                switch (select) {
                    case 0: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 1;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldBau += sl;
                            Chonaiday.gI().addPlayerBau(player);
                            Chonaiday.gI().TotalGoldBau += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " Trái Bầu");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 1: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 10;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldBau += sl;
                            Chonaiday.gI().addPlayerBau(player);
                            Chonaiday.gI().TotalGoldBau += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " Trái Bầu");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 2: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 100;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldBau += sl;
                            Chonaiday.gI().addPlayerBau(player);
                            Chonaiday.gI().TotalGoldBau += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " Trái Bầu");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                }

            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CUA) {
                switch (select) {
                    case 0: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 1;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldCua += sl;
                            Chonaiday.gI().addPlayerCua(player);
                            Chonaiday.gI().TotalGoldCua += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Cua");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 1: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 10;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldCua += sl;
                            Chonaiday.gI().addPlayerCua(player);
                            Chonaiday.gI().TotalGoldCua += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Cua");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 2: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 100;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldCua += sl;
                            Chonaiday.gI().addPlayerCua(player);
                            Chonaiday.gI().TotalGoldCua += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Cua");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                }

            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TOM) {
                switch (select) {
                    case 0: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 1;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldTom += sl;
                            Chonaiday.gI().addPlayerTom(player);
                            Chonaiday.gI().TotalGoldTom += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Tôm");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 1: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 10;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldTom += sl;
                            Chonaiday.gI().addPlayerTom(player);
                            Chonaiday.gI().TotalGoldTom += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Tôm");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 2: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 100;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldTom += sl;
                            Chonaiday.gI().addPlayerTom(player);
                            Chonaiday.gI().TotalGoldTom += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Tôm");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                }

            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CA) {
                switch (select) {
                    case 0: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 1;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldCa += sl;
                            Chonaiday.gI().addPlayerCa(player);
                            Chonaiday.gI().TotalGoldCA += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Cá");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 1: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 10;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldCa += sl;
                            Chonaiday.gI().addPlayerCa(player);
                            Chonaiday.gI().TotalGoldCA += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Cá");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 2: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 100;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldCa += sl;
                            Chonaiday.gI().addPlayerCa(player);
                            Chonaiday.gI().TotalGoldCA += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Cá");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_HUOU) {
                switch (select) {
                    case 0: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 1;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldHuou += sl;
                            Chonaiday.gI().addplayerHuou(player);
                            Chonaiday.gI().TotalHuou += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Hươu");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 1: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 10;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldHuou += sl;
                            Chonaiday.gI().addplayerHuou(player);
                            Chonaiday.gI().TotalHuou += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Hươu");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 2: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 100;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldHuou += sl;
                            Chonaiday.gI().addplayerHuou(player);
                            Chonaiday.gI().TotalHuou += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Hươu");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_GA) {
                switch (select) {
                    case 0: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 1;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldGa += sl;
                            Chonaiday.gI().addPlayerGa(player);
                            Chonaiday.gI().TotalGa += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Gà");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 1: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 10;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldGa += sl;
                            Chonaiday.gI().addPlayerGa(player);
                            Chonaiday.gI().TotalGa += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Gà");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                    case 2: {
                        Item tv = InventoryServiceNew.gI().findItemBag(player, 457);
                        int sl = 100;
                        if (tv != null && tv.quantity >= sl) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv, sl);
                            InventoryServiceNew.gI().sendItemBags(player);
                            player.GoldGa += sl;
                            Chonaiday.gI().addPlayerGa(player);
                            Chonaiday.gI().TotalGa += sl;
                            player.PauCuaPoint += sl;
                            Service.getInstance().sendThongBao(player,
                                    "Đã mua thành công " + sl + " con Gà");
                        } else {
                            npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "|7|Không đủ số lượng đơn vị tiền tệ để THAM QUAN!",
                                    "Đóng");
                        }
                        break;
                    }
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_UPDATE) {
                switch (select) {
                    case 0:
                        String time = ((Chonaiday.gI().lastTimeEnd
                                - System.currentTimeMillis()) / 1000) + " giây";
                        npc.createOtherMenu(player, ConstNpc.MENU_UPDATE,
                                "|2| Trò chơi tham quan muôn thú đang được diễn ra"
                                + "\n Mua vé và dự đoán đúng, kết quả bạn sẽ nhận được thưởng lớn\nHãy tham gia ngay"
                                + "\n\n|7| Kết quả kỳ trước -> "
                                + Chonaiday.gI().getNameBauCua(Chonaiday.gI().x)
                                + " : "
                                + Chonaiday.gI().getNameBauCua(Chonaiday.gI().y)
                                + " : "
                                + Chonaiday.gI().getNameBauCua(Chonaiday.gI().z)
                                + "\n\n|6|Tổng Bầu :" + Chonaiday.gI().TotalGoldBau
                                + "\t\t( Tổng thỏi vàng :  " + player.GoldBau + ")"
                                + "\nTổng Cua : " + Chonaiday.gI().TotalGoldCua
                                + "\t\t( Tổng thỏi vàng :  " + player.GoldCua + ")"
                                + "\nTổng Tôm : " + Chonaiday.gI().TotalGoldTom
                                + "\t\t( Tổng thỏi vàng :  " + player.GoldTom + ")"
                                + "\nTổng Cá : " + Chonaiday.gI().TotalGoldCA
                                + "\t\t( Tổng thỏi vàng :  " + player.GoldCa + ")"
                                + "\nTổng Hươu : " + Chonaiday.gI().TotalHuou
                                + "\t\t( Tổng thỏi vàng :  " + player.GoldHuou + ")"
                                + "\nTổng Gà : " + Chonaiday.gI().TotalGa
                                + "\t\t( Tổng thỏi vàng :  " + player.GoldGa + ")"
                                + "\n\nThời gian còn lại : " + time,
                                "Update", "Hướng dẫn", "Đóng");
                        break;
                    case 1:
                        break;
                }
            }
        }
    }

}
