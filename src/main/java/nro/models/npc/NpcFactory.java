package nro.models.npc;

import static java.awt.SystemColor.text;
import nro.attr.Attribute;
import nro.attr.AttributeManager;
import nro.consts.*;
import nro.dialog.ConfirmDialog;
import nro.dialog.MenuDialog;
import nro.event.Event;
import nro.jdbc.daos.PlayerDAO;
import nro.lib.RandomCollection;
import nro.models.boss.Boss;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.event.EscortedBoss;
import nro.models.boss.event.Qilin;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.consignment.ConsignmentShop;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.ItemTemplate;
import nro.models.map.ItemMap;
import nro.models.map.Map;
import nro.models.map.SantaCity;
import nro.models.map.Zone;
import nro.models.map.challenge.MartialCongressService;
import nro.models.map.dhvt.DaiHoiManager;
import nro.models.map.dungeon.SnakeRoad;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.map.mabu.MabuWar;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.DoanhTrai;
import nro.models.map.war.BlackBallWar;
import nro.models.map.war.NamekBallWar;
import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.noti.NotiManager;
import nro.server.Maintenance;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.server.SettingGame;
import nro.server.io.Message;
import nro.services.*;
import nro.services.Event.EventService;
import nro.services.func.*;
import nro.services.func.lr.LuckyRoundGold;
import nro.services.giftcode.RequestService;
import nro.utils.Log;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;

import nro.models.npc.npcList.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import nro.data.DataGame;
import nro.manager.TopManager;
import nro.manager.TournamentsManager;
import nro.models.boss.BossData;
import nro.models.boss.TRAIN_BOSS.ThanMeoKarin;
import nro.server.Client;

import static nro.server.Manager.*;
import static nro.services.func.SummonDragon.*;

public class NpcFactory {

    private static ConSoMayManService cs = ConSoMayManService.gI();

    private static boolean nhanVang = true;
    private static boolean nhanDeTu = true;

    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        Npc npc = null;
        try {
            switch (tempId) {

                case ConstNpc.MAY_GAP_THU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (this.mapId == 5) {
                                this.createOtherMenu(player, 1234,
                                        "|2|MÁY GẮP THÚ \nGẮP THƯỜNG: 1 THỎI VÀNG/LƯỢT\nGẮP CAO CẤP: 2 THỎI VÀNG/LƯỢT\nGẮP VIP : 5 THỎI VÀNG/LƯỢT\n"
                                                + "CHỌN CÁC TÙY CHỌN BÊN DƯỚI ĐỂ XEM THÊM THÔNG TIN CHI TIẾT\n|7|MỌI ITEM SẼ ĐƯỢC ĐẨY VÀO RƯƠNG PHỤ NẾU HÀNH TRANG ĐẦY!",
                                        "Gắp Thường", "Gắp Cao Cấp", "Gắp VIP", "Xem Top", "Rương Đồ");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                NpcMenu.gI().confirm_may_gap_thu(this, player, select);
                            }
                        }
                    };
                    break;
                case ConstNpc.TO_SU_KAIO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Chào con!", "Đóng");

                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // NpcMenu.gI().confirm_may_gap_thu(this, player, select);
                            }
                        }
                    };
                    break;
                    case ConstNpc.LY_TIEU_NUONG1:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            if (!canOpenNpc(player)) return;

                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ta nhận đổi Thỏi vàng và bán gói VIP\n"
                                  + "VIP hiện tại của con: VIP " + player.vip1
                                  + "\nSố dư: " + player.getSession().VND + " VND"
                                    + "\nĐiểm nạp tích lũy: " + player.getSession().count_card + " Điểm",
                                    "Đổi Thỏi\nvàng Khóa", "Mua\n Gói Vip","Shop\n Tích Lũy","Mua Casule Tết\n (20K)");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (!canOpenNpc(player)) return;

                            int indexMenu = player.iDMark.getIndexMenu();

                            // ===== MENU CƠ BẢN =====
                            if (player.iDMark.isBaseMenu()) {
                                switch (select) {
                                    case 0:
                                        Input.gI().createFormDoiThoiVang(player);
                                        break;
                                      
                                    case 1:
                                        this.createOtherMenu(player, 3000,
                                                "|7|GÓI VIP\n\n"
                                                        // + "|0|VIP 1: 20.000 VND\n"
                                              + "|0|VIP 1: 50.000 VND\n"
                                              + "|1|VIP 2: 100.000 VND\n"
                                              + "|2|VIP 3: 200.000 VND\n"
                                                        + "|2|VIP 4: 500.000 VND\n\n"
                                              + "|5|VIP hiện tại: VIP " + player.vip1
                                              + "\nSố dư: " + player.getSession().VND + " VND",
                                                // "Mua VIP 1",
                                                "Mua VIP 1",
                                                "Mua VIP 2",
                                                "Mua VIP 3",
                                                "Mua VIP 4",
                                                "Đóng");
                                        break;
                                    case 2:
                                        ShopService.gI().openShopSpecial(player, this,
                                                    ConstNpc.SHOP_DIEM_NAP, 1, -1);
                                        break;
                                    case 3:
                                        this.createOtherMenu(player, 51222,
                                                "Mua Capsule Tết có tỉ lệ ra đồ ngon!!"
                                              + "\nSố dư: " + player.getSession().VND + " VND",
                                                "Mua X1",
                                                "Mua X5",
                                                "Đóng");
                                        break;
                                }
                            } else if (indexMenu == 51222) {
                                switch (select) {
                                    case 0:
                                        if (player.getSession().VND < 20_000) {
                                            Service.getInstance().sendThongBaoOK(player, "Không đủ 20K VND!");
                                            return;
                                        }

                                        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                                            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
                                            return;
                                        }
                                        
                                        Item tv = ItemService.gI().createNewItem((short) 1187, 1);
                                        tv.itemOptions.add(new ItemOption(30, 0));
                                        InventoryService.gI().addItemBag(player, tv, 1);
                                        InventoryService.gI().sendItemBags(player);
                                        player.getSession().VND -= 20_000;
                                        PlayerDAO.updateAccountVND(player.getSession().userId,player.getSession().VND);
                                        Service.getInstance().sendThongBaoOK(player,
                                                "Bạn nhận được X1 Capsule Tết Dương Lịch 2026!");
                                        break;
                                    case 1:
                                        if (player.getSession().VND < 100_000) {
                                            Service.getInstance().sendThongBaoOK(player, "Không đủ 20K VND!");
                                            return;
                                        }

                                        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                                            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
                                            return;
                                        }
                                        
                                        Item tv1 = ItemService.gI().createNewItem((short) 1187, 5);
                                        tv1.itemOptions.add(new ItemOption(30, 0));
                                        InventoryService.gI().addItemBag(player, tv1, 1);
                                        InventoryService.gI().sendItemBags(player);
                                        player.getSession().VND -= 100_000;
                                        PlayerDAO.updateAccountVND(player.getSession().userId,player.getSession().VND);
                                        Service.getInstance().sendThongBaoOK(player,
                                                "Bạn nhận được X5 Capsule Tết Dương Lịch 2026!");
                                        break;
                                }
                            }
                            // ===== MENU VIP =====
                            else if (indexMenu == 3000) {
                                switch (select) {
                                    // case 0: createConfirmVIP(player, 1); break;
                                    case 0: createConfirmVIP(player, 1); break;
                                    case 1: createConfirmVIP(player,2); break;
                                    case 2: createConfirmVIP(player,3); break;
                                     case 3: createConfirmVIP(player, 4); break;
                                }
                            } 
                            // ===== MENU XÁC NHẬN =====
                            else if (indexMenu >= 4001 && indexMenu <= 4004) {
                                int vipLevel = indexMenu - 4000;
                                if (select == 0) { // bấm Xác nhận
                                    buyVIP(player, vipLevel);
                                }
                            }
                        }
                        private void createConfirmVIP(Player player, int vipLevel) {
                            String msg = "";
                            switch (vipLevel) {
                                // case 1:
                                //     msg = "|7|XÁC NHẬN VIP 1\n\nGiá: 20.000 VND\nQuà tặng:\n- 10 Rương Bình EXP ngẫu nhiên\n";
                                //     break;
                                case 1:
                                    msg = "|7|XÁC NHẬN VIP 1\n\nGiá: 50.000 VND\nQuà tặng:\n- 100K Thỏi Vàng Khóa\n- 3 Hộp Quà Giáng Sinh\n- 5 Bình EXP(3 loại)";
                                    break;
                                case 2:
                                    msg = "|7|XÁC NHẬN VIP 2\n\nGiá: 100.000 VND\nQuà tặng:\n- 200k Thỏi Vàng Khóa\n- 8 Hộp Quà Giáng Sinh\n- 2 Hộp Quà Chú Lùn\n- 1 Bộ NR Băng\n- 10 Bình EXP(3 loại)";
                                    break;
                                case 3:
                                    msg = "|7|XÁC NHẬN VIP 3\n\nGiá: 200.000 VND\nQuà tặng:\n- 400k Thỏi Vàng Khóa\n- Danh Hiệu 10% Chỉ Số(7 ngày)\n- 20 Hộp Quà Giáng Sinh\n- 5 Hộp Quà Chú Lùn\n- 3 Bộ NR Băng\n- 20 Thức Ăn\n- 20 Bình EXP(3 loại)";
                                    break;
                                case 4:
                                    msg = "|7|XÁC NHẬN VIP 4\n\nGiá: 500.000 VND\nQuà tặng:\n- 1.000k Thỏi Vàng Khóa\n- 5 Bộ NR Băng\n- 15 Đá Ngũ Sắc\n- 1 Rương Thần Linh(Có thể giao dịch)\n- 99 Sushi(Thức ăn)\n- 1 Bộ NR\n- 30 Bình EXP(3 loại)";
                                    break;
                            }
                            this.createOtherMenu(player, 4000 + vipLevel, msg, "Xác nhận", "Đóng");
                        }
                        private void buyVIP(Player player, int vipLevel) {
                            int price = 0;
                            switch (vipLevel) {
                                // case 1: price = 20000; break;
                                case 1: price = 50000; break;
                                case 2: price = 100000; break;
                                case 3: price = 200000; break;
                                case 4: price = 500000; break;
                            }

                            // Kiểm tra VIP trước
                            if (vipLevel > player.vip1 + 1) {
    Service.getInstance().sendThongBaoOK(player, "Phải mua VIP trước đó!");
    return;
}

                            if (player.vip1 >= vipLevel) {
                                Service.getInstance().sendThongBaoOK(player, "Con đã mua VIP " + vipLevel + " rồi!");
                                return;
                            }

                            // Kiểm tra tiền
                            if (player.getSession().VND < price) {
                                Service.getInstance().sendThongBaoOK(player, "Không đủ " + price + " VND!");
                                return;
                            }

                            if (InventoryService.gI().getCountEmptyBag(player) < 8) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 7 ô trống trong hành trang");
            return;
        }
                            try {
                                // Trừ tiền và cập nhật DB
                                player.getSession().VND -= price;
                                PlayerDAO.updateAccountVND(
                        player.getSession().userId,player.getSession().VND );
                                player.vip1 = vipLevel;
                                PlayerDAO.updateVip(player);

                                // Add quà VIP
                                if (vipLevel == 1) {
                                    addItem(player, (short)1429, 100000, new ItemOption(30, 0));
                                    addItem(player, (short)648, 3, new ItemOption(30, 0));
                                    addItem(player, (short)1973, 5, new ItemOption(30, 0));
                                } else if (vipLevel == 2) {
                                    addItem(player, (short)1429, 200000, new ItemOption(30, 0));
                                    addItem(player, (short)1171, 2, new ItemOption(30, 0));
                                     addItem(player, (short)648, 8, new ItemOption(30, 0));
                                    addItem(player, (short)1973, 10, new ItemOption(30, 0));
                                    addItem(player, (short)1974, 1, new ItemOption(30, 0));
                                } else if (vipLevel == 3) {
                                    addItem(player, (short)1429, 400000, new ItemOption(30, 0));
                                    addItem(player, (short)648, 20, new ItemOption(30, 0));
                                    addItem(player, (short)1171, 5, new ItemOption(30, 0));
                                   addItem(player, (short)1973, 20, new ItemOption(30, 0));
                                    addItem(player, (short)1974, 3, new ItemOption(30, 0));
                                     addItem(player, (short)667, 20, new ItemOption(30, 0));
                                     addItem(player, (short)1533, 1, new ItemOption(50, 10),new ItemOption(77, 10),new ItemOption(103, 10),new ItemOption(93, 7));
                                } else if (vipLevel == 4) {
                                    addItem(player, (short)1429, 1000000, new ItemOption(30, 0));
                                    addItem(player, (short)648, 50, new ItemOption(30, 0));
                                    addItem(player, (short)674, 15, new ItemOption(30, 0));
                                   addItem(player, (short)1973, 30, new ItemOption(30, 0));
                                    addItem(player, (short)1974, 5);
                                     addItem(player, (short)667, 99, new ItemOption(30, 0));
                                     addItem(player, (short)1460, 1);
                                     addItem(player, (short)1971, 1);
                                }

                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBaoOK(player, "Chúc mừng con đã mua VIP " + vipLevel + "!");

                            } catch(Exception e) {
                                Service.getInstance().sendThongBaoOK(player, "Mua VIP thất bại, thử lại sau!");
                                e.printStackTrace();
                            }
                        }


                        private void addItem(Player player, short id, int quantity, ItemOption... options) {
                            Item item = ItemService.gI().createNewItem(id, quantity);
                            if (options != null) {
                                for (ItemOption op : options) {
                                    if (op != null) {
                                        item.itemOptions.add(op);
                                    }
                                }
                            }
                            InventoryService.gI().addItemBag(player, item);
                        }

                        
                    };
                break;
                case ConstNpc.MR_POPO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {

                            createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ngươi tìm ta có việc gì",
                                    "Đóng");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // if (player.pet == null) {
                                // Service.getInstance().sendThongBao(player,
                                // "Yêu cầu có đệ tử Black Gôku");
                                // return;
                                // }
                                // if (!player.pet.isMabu) {
                                // Service.getInstance().sendThongBao(player,
                                // "Yêu cầu có đệ tử Black Gôku");
                                // return;
                                // }
                                // if (player.inventory.gold < 2000000000l) {
                                // Service.getInstance().sendThongBao(player,
                                // "Cần 2 tỷ vàng để thực hiện");
                                // return;
                                // }
                                // Item phongAn = InventoryService.gI().findItemBag(player, (short) 1390);
                                // if (phongAn != null && phongAn.quantity >= 99) {
                                // player.inventory.gold -= 2000000000l;
                                // InventoryService.gI().subQuantityItemsBag(player, phongAn, 99);
                                // player.pet.isMabu = false;
                                // player.pet.isBlackGoku = true;
                                // player.pet.name = "$" + "Black Gôku Rose";

                                // InventoryService.gI().sendItemBags(player);
                                // Service.getInstance().sendMoney(player);
                                // ChangeMapService.gI().changeMapInYard(player, player.zone.zoneId, -1,
                                // Util.nextInt(300, 500));
                                // Service.getInstance().sendThongBao(player,
                                // "Đệ tử của bạn được nâng cấp thành Black Gôku Rose, chỉ số hợp thể tăng thêm
                                // 5%");
                                // Service.getInstance().chatJustForMe(player, player.pet,
                                // "Nguồn năng lượng Rose thật mạnh mẽ, cảm ơn sư phụ");
                                // } else {
                                // Service.getInstance().sendThongBao(player,
                                // "Không đủ bình phong ấn");
                                // return;
                                // }
                                // NpcMenu.gI().confirm_may_gap_thu(this, player, select);
                            }
                        }
                    };
                    break;
                case ConstNpc.THIEN_SU_VEGETA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (mapId == 5) {
                                Item vp1 = InventoryService.gI().findItemBagByTemp(pl, 1170);
                                Item vp2 = InventoryService.gI().findItemBagByTemp(pl, 1165);
                                Item vp3 = InventoryService.gI().findItemBagByTemp(pl, 1167);
                                Item vp4 = InventoryService.gI().findItemBagByTemp(pl, 1168);
                                Item vp5 = InventoryService.gI().findItemBagByTemp(pl, 1169);
                                Item vp6 = InventoryService.gI().findItemBagByTemp(pl, 1299);
                                Item vp7 = InventoryService.gI().findItemBagByTemp(pl, 1166);
                                int q1 = vp1 != null ? vp1.quantity : 0;
                                int q2 = vp2 != null ? vp2.quantity : 0;
                                int q3 = vp3 != null ? vp3.quantity : 0;
                                int q4 = vp4 != null ? vp4.quantity : 0;
                                int q5 = vp5 != null ? vp5.quantity : 0;
                                int q6 = vp6 != null ? vp6.quantity : 0;
                                int q7 = vp7 != null ? vp7.quantity : 0;
                                 createOtherMenu(pl, ConstNpc.BASE_MENU,
                        "Chưa có Người yêu à..."
                        + "\n Cùng đón Noel cùng với sever nhé <3"
                        + "\n\nHộp quà giáng sinh: x99 Gói quà, x20 Chuông đồng, x99 Bánh quy + 3.000 TVK"
                         + "\nTúi chú lùn(VPDL): x99 Kẹo đường, x99 Kẹo người tuyết, x50 Dây buộc + 5.000 TVK"
                         + "\nHộp quà băng giá: x10 cá Tuyết"
                        + "\n\n|2| Đang có: " + q1 + " Gói quà, " + q2 + " Chuông đồng, " + q3 + " Bánh quy, "+ q4 + " Kẹo đường, "+ q5 + " Kẹo người tuyết, "+ q6 + " Dây buộc, "+ q7 + " Cá Tuyết ",
                        "Làm\nHộp Quà","Tặng Kẹo\n Chú Lùn","Đổi\n Cá Tuyết","Đến\n Hang Tuyết","Đóng");
                                 }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                NpcMenu.gI().confim_Vegeta2(this, player, select);
                            }
                        }
                    };
                    break;

                // case ConstNpc.CAY_WISH:
                // npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                // @Override
                // public void openBaseMenu(Player player) {
                // Item lden = InventoryService.gI().findItemBagByTemp(player, 1264);
                // if (player.lastTimeWish == 0 && player.isWish) {
                // this.createOtherMenu(player, 12,
                // "|7|Cây ước nguyện đã ở đây được vài trăm năm và nắm quyền thực hiện ước
                // nguyện từ Lồng đèn ước nguyện\n"
                // + "|2|Cây sẽ thực hiện điều ước nếu ngươi đưa cho Cây Lồng đèn ước nguyện và
                // còn phụ thuộc vào may mắn của nhà ngươi\n"
                // + "|7|Thời gian thực hiện điều ước còn lại : "
                // + Util.msToTime(player.lastTimeWish)
                // + "\n|6|(Đưa cho cây ước nguyện lồng đèn và sau 24h cây sẽ cho ngươi 1 phần
                // quà)",
                // "Nhận\nĐiều ước", "Đóng");
                // return;
                // }
                // if (player.lastTimeWish > 0 && player.isWish) {
                // this.createOtherMenu(player, 13,
                // "|7|Cây ước nguyện đã ở đây được vài trăm năm và nắm quyền thực hiện ước
                // nguyện từ Lồng đèn ước nguyện\n"
                // + "|2|Cây sẽ thực hiện điều ước nếu ngươi đưa cho Cây Lồng đèn ước nguyện và
                // còn phụ thuộc vào may mắn của nhà ngươi\n"
                // + "|7|Thời gian thực hiện điều ước còn lại : "
                // + Util.msToTime(player.lastTimeWish)
                // + "\n|6|(Đưa cho cây ước nguyện lồng đèn và sau 24h cây sẽ cho ngươi 1 phần
                // quà)",
                // "Ước nguyện\n nhanh", "Đóng");
                // return;
                // }
                // if (lden != null && lden.quantity > 0 && player.lastTimeWish == 0) {
                // this.createOtherMenu(player, ConstNpc.BASE_MENU,
                // "|7|Cây ước nguyện đã ở đây được vài trăm năm và nắm quyền thực hiện ước
                // nguyện từ Lồng đèn ước nguyện\n"
                // + "|2|Cây sẽ thực hiện điều ước nếu ngươi đưa cho Cây Lồng đèn ước nguyện và
                // còn phụ thuộc vào may mắn của nhà ngươi\n"
                // + (player.lastTimeWish > 0
                // ? " |7|Thời gian còn thực hiện điều ước còn lại : "
                // + Util.msToTime(player.lastTimeWish)
                // : "|7|Đang không thực hiện điều ước")
                // + "\n|6|(Đưa cho cây ước nguyện lồng đèn và sau 24h cây sẽ cho ngươi 1 phần
                // quà)",
                // "Trao\nLồng đèn", "Đóng");
                // } else {
                // this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                // "|7|Cây ước nguyện đã ở đây được vài trăm năm và nắm quyền thực hiện ước
                // nguyện từ Lồng đèn ước nguyện\n"
                // + "|2|Cây sẽ thực hiện điều ước nếu ngươi đưa cho Cây Lồng đèn ước nguyện và
                // còn phụ thuộc vào may mắn của nhà ngươi\n"
                // + (player.lastTimeWish > 0
                // ? "|7|Thời gian còn thực hiện điều ước còn lại : "
                // + Util.msToTime(player.lastTimeWish)
                // : "|7|Đang không thực hiện điều ước")
                // + "\n|6|(Đưa cho cây ước nguyện lồng đèn và sau 24h cây sẽ cho ngươi 1 phần
                // quà)",
                // "Đóng");
                // }
                // }
                // @Override
                // public void confirmMenu(Player player, int select) {
                // if (canOpenNpc(player)) {
                // NpcMenu.gI().confirm_cay_uoc_nguyen(this, player, select);
                // }
                // }
                // };
                // break;
                case ConstNpc.NGO_KHONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chu mi nga",
                                    "Đóng");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // switch (select) {
                                // case 0:// bát giới
                                // EventService.gI().NguHanhSon_2(player, 1, (short) 548);
                                // break;
                                // case 1:// ngộ không
                                // EventService.gI().NguHanhSon_2(player, 1, (short) 547);
                                // break;
                                // case 2: // satang
                                // EventService.gI().NguHanhSon_2(player, 1, (short) 1262);
                                // break;
                                // }
                            }
                        }
                    };
                    break;
                case ConstNpc.DUONG_TANG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (this.mapId == MapName.LANG_ARU) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Nam mô"
                                        + " Thí chủ có muốn đế ngũ hành sơn không??",
                                        "Từ chối");
                            } else if (mapId == MapName.NGU_HANH_SON) {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Bản ngã của ta đã bị sao chép và nhân bản ra một hình hài mới\n"
                                                + " Xin hãy cẩn trọng với hắn!",
                                        "Về làng\nAru", "Đóng");
                            } else if (this.mapId == MapName.NGU_HANH_SON_3) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Nam mô"
                                        + " Thí chủ có muốn ta giúp gì không??",
                                        "Từ chối");
                            }

                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // NpcMenu.gI().confirm_duong_tang(this, player, select);
                            }
                        }
                    };
                    break;
                case ConstNpc.NGU_DAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            long timeBua_HP = 0;
                            long timeBua_KI = 0;
                            long timeBua_SD = 0;
                            long timeBua_TNSM = 0;
                            if (player.charms.tdPhuHP > 0) {
                                timeBua_HP = (player.charms.tdPhuHP - System.currentTimeMillis())
                                        / 1000;
                                if (timeBua_HP < 0) {
                                    timeBua_HP = 0;
                                }
                            }
                            if (player.charms.tdPhuKI > 0) {
                                timeBua_KI = (player.charms.tdPhuKI - System.currentTimeMillis())
                                        / 1000;
                                if (timeBua_KI < 0) {
                                    timeBua_KI = 0;
                                }
                            }
                            if (player.charms.tdPhuSD > 0) {
                                timeBua_SD = (player.charms.tdPhuSD - System.currentTimeMillis())
                                        / 1000;
                                if (timeBua_SD < 0) {
                                    timeBua_SD = 0;
                                }
                            }
                            if (player.charms.tdPhuTNSM > 0) {
                                timeBua_TNSM = (player.charms.tdPhuTNSM - System.currentTimeMillis())
                                        / 1000;
                                if (timeBua_TNSM < 0) {
                                    timeBua_TNSM = 0;
                                }
                            }

                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Ta có thể ban cho cậu năng lực của biển cả, giá chỉ 1 tỷ vàng mỗi lần phù, cậu sẽ mạnh hơn rất nhiều\n"
                                            + "|2| Phù HP còn " + TimeUtil.getTimeFromSecondToString((int) timeBua_HP)
                                            + "\nPhù KI còn " + TimeUtil.getTimeFromSecondToString((int) timeBua_KI)
                                            + "\nPhù SD còn " + TimeUtil.getTimeFromSecondToString((int) timeBua_SD)
                                            + "\nPhù TNSM còn "
                                            + TimeUtil.getTimeFromSecondToString((int) timeBua_TNSM),
                                    "Phù \n10% HP", "Phù \n10% KI", "Phù \n10% SD", "Phù \n100% TNSM", "Từ chối");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (select) {
                                    case 0:
                                    case 1:
                                    case 2:
                                    case 3:
                                        if (player.inventory.gold >= 1_000_000_000l) {
                                            player.inventory.gold -= 1_000_000_000l;
                                            player.charms.addTimeCharms(3000 + select, 10);
                                            Service.getInstance().point(player);
                                            Service.getInstance().sendMoney(player);
                                            Service.getInstance().sendThongBao(player,
                                                    "Phù phép thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Ngươi không đủ vàng, cần 1 tỷ vàng để phù");
                                        }
                                        break;
                                }
                            }

                        }
                    };
                    break;
                case ConstNpc.DOC_NHAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (MapService.gI().isMapBanDoKhoBau_new(mapId)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho cậu??\n"
                                            + "|2|Bản đồ kho báu mới được sửa đổ như sau:"
                                            + "Tất cả người chơi điều có thể vào khu\n"
                                            + "Dùng để up đá nâng cấp và các vật phẩm\n"
                                            + "Bpss ở đây rơi nhiều rương báu và đồ xịn\n"
                                            + "TNSM từ quái ở đây ít lại để cân bằng game",
                                            "Cửa hàng", "Về\nĐảo Kame", "Đóng");
                                } else {
                                   if (this.mapId == 57) {
                if (player.zone.isCheckKilledAll(57) && !player.clan.doanhTrai.isHaveDoneDoanhTrai) {
                    player.clan.doanhTrai.isHaveDoneDoanhTrai = true;
                    player.clan.doanhTrai.lastTimeDoneDoanhTrai = System.currentTimeMillis();
                    player.clan.doanhTrai.DropNgocRong();
                    Service.getInstance().sendThongBao(player, "Trại Độc Nhãn đã bị tiêu diệt, bạn có 5 phút để tìm kiếm viên ngọc rồng 4 sao trước khi phi thuyền đến đón");
                    NpcService.gI().createTutorial(player, avartar, "Ta chịu thua, nhưng các ngươi đừng có mong lấy được ngọc của ta\b"
                            + "ta đã giấu ngọc 4 sao và 1 đống 7 sao trong doanh trại này\b"
                            + "Các ngươi chỉ có 5 phút đi tìm, đố các ngươi tìm ra hahaha");
                } else {
                    NpcService.gI().createTutorial(player, avartar, "hãy tiêu diệt hết quái");
                }
            }
                                }

                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (MapService.gI().isMapBanDoKhoBau_new(mapId)) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            ShopService.gI().openShopSpecial(player, this,
                                                    ConstNpc.SHOP_DOC_NHAN, 0, -1);
                                        } else if (select == 1) {
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 604);
                                        }
                                    }
                                } else {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            // ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 604);
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BARDOCK:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Ngươi cần ta có việc gì??",
                                        "Xem top\nsức mạnh",
                                        "Xem top\n nhiệm vụ",
                                        "Xem top\nNạp",
                                        "Top\n Tiêu Sài",
                                        "Top\n Ngư dân",
                                        "BOSS");
                            } else if (this.mapId == 173) {
                                long timeBua = (player.charms.tdDeTuMabu - System.currentTimeMillis());
                                if (timeBua < 0) {
                                    timeBua = 0;
                                }
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Ta sẽ giúp ngươi x10 tiềm năng sức mạnh khi đánh quái ở đây trong 10 phút với chi phí là 5 tỷ vàng, có cộng dồn thời gian nếu ngươi mua nhiều"
                                                + "\nngươi đang có "
                                                + TimeUtil.getTimeFromSecondToString((int) (timeBua / 1000))
                                                + " phù x10 tiềm năng sức mạnh"
                                                + "\n|5|(có tác dụng cho đệ tử)",
                                        "Về\nĐảo kame", "Phù x10", "Từ chối");
                            } else if (this.mapId == 163) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Ta có thể giúp gì cho cậu?",
                                        "Về\nHành tinh\nBill", "Từ chối");
                            }

                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                NpcMenu.gI().confirm_bardock(this, player, select);
                            }
                        }
                    };
                    break;
                case ConstNpc.BERRY:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (mapId == 163) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Hãy giúp thôi thu thập giỏ thức ăn",
                                            "OK");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Hãy cứu dân làng chúng tôi",
                                            "OK");
                                }

                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // NpcMenu.gI().confirm_Tapion(this, player, select);
                            }
                        }
                    };
                    break;
                case ConstNpc.TAPION:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 199) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ác quỷ truyền thuyết Hirudegarn\nđã thoát khỏi phong ấn ngàn năm\nHãy giúp tôi chế ngự nó",
                                            "OK", "Từ chối");
                                }
                                if (this.mapId == 126) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Tôi sẽ đưa bạn về", "OK",
                                            "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                NpcMenu.gI().confirm_Tapion(this, player, select);
                            }
                        }
                    };
                    break;
                case ConstNpc.VADOS:
                    npc = new Vados(mapId, status, cx, cy, tempId, avartar);
                    break;
                // case ConstNpc.NPC_DAU_GIA:
                // npc = new Daugia(mapId, status, cx, cy, tempId, avartar);
                // break;
                case ConstNpc.CAY_HOA_HONG:
                    npc = new CayHoaHong(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.QUY_LAO_KAME:
                    npc = new QuyLaoKame(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.CHU_CUOI: // sktrungthu
                    npc = new Chucuoi(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.HANG_NGA: // sktrungthu
                    npc = new Hangnga(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.TRUNG_THU: // sktrungthu
                    npc = new Tiembanh(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.LOAD_TOP_NPC:
                    npc = new Daishinkan(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.THO_NGOC:
                    npc = new Thongoc(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.THUONG_NHAN:
                    npc = new Thuongnhan(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.RUONG_PET_CAITRANG:
                    npc = new Ruongsuutam(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.GHI_DANH:
                    npc = new GhiDanh(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.HAT_MIT_HT:
                    if (mapId == 185) {
                        npc = new Hatmitht(mapId, status, cx, cy, tempId, avartar);
                    }
                    break;
                case ConstNpc.TRUONG_LAO_GURU:
                case ConstNpc.VUA_VEGETA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                EscortedBoss escortedBoss = player.getEscortedBoss();
                                if (escortedBoss != null && escortedBoss instanceof Qilin) {
                                    this.createOtherMenu(player, ConstNpc.ESCORT_QILIN_MENU,
                                             "Ah con đã tìm thấy Ông Già Noel\nTa sẽ thưởng cho con 1 Vớ Giáng Sinh.",
                                            "Đồng ý", "Từ chối");
                                } else {
                                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                        super.openBaseMenu(player);
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.getIndexMenu() == ConstNpc.ESCORT_QILIN_MENU) {
                                    switch (select) {
                                        case 0: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            EscortedBoss escortedBoss = player.getEscortedBoss();
                                            if (escortedBoss != null) {
                                                escortedBoss.stopEscorting();
                                                Item item = ItemService.gI()
                                                        .createNewItem((short) 1166);
                                                item.quantity = 1;
                                                item.itemOptions.add(new ItemOption(74, 0));
                        item.itemOptions.add(new ItemOption(93, 30));
                                                InventoryService.gI().addItemBag(player, item, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + item.template.name);
                                            }
                                        }
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.LY_TIEU_NUONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Chào mừng đến với Lý Tiểu Nương!\n"
                                                + "Kết quả giải trước: " + cs.srtDataKetQua()
                                                + "\nNgười thắng giải trước: " + cs.getNameListTop()
                                                + "\nTổng giải thưởng: " + cs.topUpGem(player.id)
                                                + "\n<" + cs.countdownTime + "> giây"
                                                + "\nCác số bạn đã chọn: " + cs.strNumber(player.id),
                                        "Cập nhật", "Chọn số\n5 ngọc xanh", "Chọn\nAi Đây", "Đóng");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player) && this.mapId == 5) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            createOtherMenu(player, ConstNpc.BASE_MENU,
                                                    "Kết quả giải trước: " + cs.srtDataKetQua()
                                                            + "\nNgười thắng giải trước: " + cs.getNameListTop()
                                                            + "\nTổng giải thưởng: " + cs.topUpGem(player.id)
                                                            + "\n<" + cs.countdownTime + "> giây"
                                                            + "\n Các số bạn đã chọn: " + cs.strNumber(player.id),
                                                    "Cập nhật",
                                                    "1 số\n5 ngọc xanh");
                                            break;
                                        case 1:
                                            Input.gI().createFromChonSoMayMan(player);
                                            break;
                                        case 2:
                                            if (player.getSession().actived) {
                                                player.iDMark.setIndexMenu(ConstNpc.MENU_BAUCUA);
                                                NpcMenu.gI().confirm_Ly_Tieu_Nuong_Bau_Cua(this, player, 0);
                                            } else {
                                                if (player.nPoint.power >= 50_000_000_000L) {
                                                    player.iDMark.setIndexMenu(ConstNpc.MENU_BAUCUA);
                                                    NpcMenu.gI().confirm_Ly_Tieu_Nuong_Bau_Cua(this, player, 0);
                                                } else {
                                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                                            "Bạn cần kích hoạt thành viên hoặc có sức mạnh trên 50 tỷ để chơi Mini Game!");
                                                }
                                            }
                                            break;
                                        case 3:
                                            // ShopService.gI().openShopSpecial(player, this,
                                            // ConstNpc.SHOP_SIDE_TASK_DAY, 1, -1);
                                            break;
                                    }
                                } else {
                                    NpcMenu.gI().confirm_Ly_Tieu_Nuong_Bau_Cua(this, player, select);
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    npc = new Npc_Homes(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.THUONG_DE_NEW:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Ta có thể giúp gì cho cậu",
                                        // "Nâng cấp\n đồ hủy diệt",
                                        // "Nâng cấp\n đồ kích hoạt",
                                        // "Nâng cấp\n đồ kích hoạt Vip",
                                        "Nâng cấp\n đệ tử",
                                        "Đóng");

                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        // case 0:
                                        //// CombineServiceNew.gI().openTabCombine(player,
                                        // // CombineServiceNew.DOI_DO_THAN_LINH_THANH_HUY_DIET);
                                        // break;
                                        // case 1:
                                        // CombineServiceNew.gI().openTabCombine(player,
                                        // CombineServiceNew.NANG_CAP_SKH_THUONG);
                                        // break;
                                        // case 0:
                                        // CombineServiceNew.gI().openTabCombine(player,
                                        // CombineServiceNew.NANG_CAP_SKH_VIP);
                                        // break;
                                        case 0:
                                            NpcMethod.gI().menuNangCapDeTu(player, this);
                                            break;
                                    }

                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                    NpcMethod.gI().startCombine(player, select);
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DE_TU) {
                                    switch (select) {
                                        case 0:
                                            NpcMethod.gI().SetNangCapDeTu(player, this);
                                            break;
                                        case 1:
                                            Service.getInstance().sendThongBaoOK(player,
                                                    "Ngươi có thể kiếm đệ bằng cách nhặt trứng từ săn Super Broly \nSau khi trứng nở ngươi sẽ nhận được ngẫu nhiên đệ tử Goku, Black Goku,Fide,..."
                                                            + "\n Nâng cấp đệ tử giúp đệ tăng chỉ số SD HP KI khi hợp thể:\n"
                                                            + " - Cấp 0 10%\n"
                                                            + " - Cấp 1 16%\n"
                                                            + " - Cấp 2 22%\n"
                                                            + " - Cấp 3 28%\n"
                                                            + " - Cấp 4 35%\n");
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.BUNMA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    if (player.gender == 0) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Ở đây tôi chỉ bán đồ cho Hành tinh trái đất thôi !", "Đóng");
                                    }

                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:// Shop
                                            this.openShop(player, ConstNpc.SHOP_BUNMA_QK_0, 0);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DENDE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    if (player.isHoldNamecBall) {
                                        this.createOtherMenu(player, ConstNpc.ORTHER_MENU,
                                                "Ô,ngọc rồng Namek,anh thật may mắn,nếu tìm đủ 7 viên ngọc có thể triệu hồi Rồng Thần Namek,",
                                                "Gọi rồng", "Từ chối");
                                    } else {
                                        if (player.gender == 1) {
                                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                                        } else {
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Xin lỗi anh ! Em chỉ bán đồ cho tộc Namek !", "Đóng");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:// Shop
                                            this.openShop(player, ConstNpc.SHOP_DENDE_0, 0);
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.ORTHER_MENU) {
                                    NamekBallWar.gI().summonDragon(player, this);
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.APPULE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    if (player.gender == 2) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Hãy về hành tinh lạc hậu của ngươi đi, ở đây ta chỉ bán đồ cho Xayda thôi !",
                                                "Đóng");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:// Shop
                                            this.openShop(player, ConstNpc.SHOP_APPULE_0, 0);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DR_DRIEF:
                    npc = new DR_DRIEF(mapId, status, cx, cy, tempId, avartar) {
                    };
                    break;
                case ConstNpc.CARGO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Hãy lên đường cứu đứa bé nhà tôi\n"
                                                        + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                                    } else {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                                "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                            break;
                                        case 1:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                            break;
                                        case 2:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CUI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        private final int COST_FIND_BOSS = 500_000_000;

                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Đội quân Fide rất hùng mạnh\n"
                                                        + "Hãy cẩn thận trước khi đến hang ổ của bọn chúng");
                                    } else {
                                        if (this.mapId == 19) {
                                            int taskId = TaskService.gI().getIdTask(pl);
                                            switch (taskId) {
                                                case ConstTask.TASK_19_0:
                                                case ConstTask.TASK_19_1:
                                                case ConstTask.TASK_19_2:
                                                    int menu = ConstNpc.IGNORE_MENU;
                                                    String bossTaskName = "";
                                                    if (taskId == ConstTask.TASK_19_0) {
                                                        menu = ConstNpc.MENU_FIND_KUKU;
                                                        bossTaskName = "Kuku";
                                                    } else if (taskId == ConstTask.TASK_19_1) {
                                                        menu = ConstNpc.MENU_FIND_MAP_DAU_DINH;
                                                        bossTaskName = "Mập\nđầu đinh";
                                                    } else if (taskId == ConstTask.TASK_19_2) {
                                                        menu = ConstNpc.MENU_FIND_RAMBO;
                                                        bossTaskName = "Rambo";
                                                    }
                                                    this.createOtherMenu(pl, menu,
                                                            "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                            "Đến chỗ\n" + bossTaskName + "\n("
                                                                    + Util.numberToMoney(COST_FIND_BOSS)
                                                                    + " vàng)",
                                                            "Đến Cold", "Đến\nNappa", "Hành tinh\nNgục tù", "Từ chối");
                                                    break;
                                                default:
                                                    this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                            "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                            "Đến Cold", "Đến\nNappa", "Từ chối");

                                                    break;
                                            }
                                        } else if (this.mapId == 68) {
                                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                    "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                                        } else if (this.mapId == 155) {
                                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                    "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                                        } else {
                                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                    "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                                            + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                                    "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 26) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 19) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_27_0) {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Hãy hoàn thành nhiệm vụ nhặt 50 Capsule Kì Bí");
                                                }
                                                break;
                                            case 1:
                                                if (TaskService.gI().getIdTask(player) > ConstTask.TASK_17_1) {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Hãy hoàn thành nhiệm vụ trước");
                                                }
                                                break;
                                            case 2:
                                                // if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_26_0) {
                                                // ChangeMapService.gI().changeMapBySpaceShip(player, 155, -1, 120);
                                                // } else {
                                                // Service.getInstance().sendThongBao(player,
                                                // "Hãy hoàn thành nhiệm vụ tiêu diệt xên trước");
                                                // }
                                                break;
                                        }
                                    } else {

                                        switch (player.iDMark.getIndexMenu()) {
                                            case ConstNpc.MENU_FIND_KUKU:
                                            case ConstNpc.MENU_FIND_MAP_DAU_DINH:
                                            case ConstNpc.MENU_FIND_RAMBO: {
                                                switch (select) {
                                                    case 0:
                                                        int taskId = TaskService.gI().getIdTask(player);
                                                        String bossTaskName = "";
                                                        short bossID = -1;
                                                        if (taskId == ConstTask.TASK_19_0) {
                                                            bossID = BossFactory.KUKU;
                                                            bossTaskName = "Kuku";
                                                        } else if (taskId == ConstTask.TASK_19_1) {
                                                            bossID = BossFactory.MAP_DAU_DINH;
                                                            bossTaskName = "Mập\nđầu đinh";
                                                        } else if (taskId == ConstTask.TASK_19_2) {
                                                            bossID = BossFactory.RAMBO;
                                                            bossTaskName = "Rambo";
                                                        }
                                                        Boss boss = BossManager.gI().getBossById(bossID);
                                                        if (boss != null && !boss.isBossDie()) {
                                                            if (player.inventory.gold >= COST_FIND_BOSS) {
                                                                player.inventory.gold -= COST_FIND_BOSS;
                                                                ChangeMapService.gI().changeMap(player, boss.zone,
                                                                        boss.location.x, boss.location.y);
                                                                Service.getInstance().sendMoney(player);
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Không đủ vàng, còn thiếu "
                                                                                + Util.numberToMoney(
                                                                                        COST_FIND_BOSS
                                                                                                - player.inventory.gold)
                                                                                + " vàng");
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    bossTaskName + " chưa xuất hiện");
                                                        }
                                                        break;
                                                    case 1:
                                                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_26_0) {
                                                            ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1,
                                                                    295);
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy hoàn thành nhiệm vụ tiêu diệt xên trước");
                                                        }
                                                        break;
                                                    case 2:
                                                        if (TaskService.gI().getIdTask(player) > ConstTask.TASK_17_1) {
                                                            ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1,
                                                                    90);
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy hoàn thành nhiệm vụ trước");
                                                        }
                                                        break;
                                                    case 3:
                                                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_26_0) {
                                                            ChangeMapService.gI().changeMapBySpaceShip(player, 155, -1,
                                                                    120);
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy hoàn thành nhiệm vụ tiêu diệt xên trước");
                                                        }
                                                        break;
                                                }
                                            }
                                                break;
                                        }

                                    }
                                } else if (this.mapId == 68 || this.mapId == 155) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                                break;
                                            case 1:
                                                // if (this.mapId == 155) { // nếu ở hành tinh ngục tù thì có shop
                                                // this.openShop(player, ConstNpc.SHOP_CUI_NGUC_TU, 0);
                                                // }
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.THUONG_DE_76:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (mapId == 0 || mapId == 7 || mapId == 14 || mapId == 5) {
                                    createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ta có thể giúp gì cho ngươi",
                                            "Đến\nhành tinh\nCereal",
                                            "Từ chối");
                                } else if (mapId == 197) {
                                    long timeBua = (player.charms.tdDeTuMabu - System.currentTimeMillis());
                                    if (timeBua < 0) {
                                        timeBua = 0;
                                    }

                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ta sẽ giúp đệ của ngươi tăng x2 tiềm năng sức mạnh khi đánh quái ở đây trong 10 phút với chi phí là 2 tỷ vàng, có cộng dồn thời gian nếu ngươi mua nhiều"
                                                    + "\nngươi đang có "
                                                    + TimeUtil.getTimeFromSecondToString((int) (timeBua / 1000))
                                                    + " phù 20% tiềm năng sức mạnh"
                                                    + "\n|5|(có tác dụng cho đệ tử)",
                                            "Cửa hàng", "Phù 20%", "Về\nĐảo kame", "Từ chối");

                                } else if (mapId == 177) {
                                    createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Dùng máy dò mảnh vỡ bông tai cấp 2 mua ở Bulma tương lai, sau khi sử dụng máy dò, hạ quái tại đây sẽ rơi mảnh vỡ bông tai",
                                            "Về\nđảo Kame",
                                            "từ chối");
                                } else {
                                    createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ta có thể giúp gì cho con??",
                                            "Về\nđảo Kame",
                                            "từ chối");
                                }

                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14 || this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0: // Khu vực luyện tập
                                                if (TaskService.gI().getIdTask(player) <= ConstTask.TASK_21_0) {

                                                    Service.getInstance().sendThongBao(player,
                                                            "Hãy hoàn thành nhiệm vụ TDST");
                                                    return;
                                                }
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 197, -1, 176);
                                                break;
                                            case 11:// thung lũng xanh
                                                   // ChangeMapService.gI().changeMapBySpaceShip(player, 177, -1, 350);
                                                break;
                                            case 21:
                                                NpcMethod.gI().menuNangCapDeTu(player, this);
                                                break;

                                            case 3:

                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DE_TU) {
                                        switch (select) {
                                            case 0:
                                                NpcMethod.gI().SetNangCapDeTu(player, this);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 197) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:// cửa hàng
                                                ShopService.gI().openShopSpecial(player, this,
                                                        ConstNpc.SHOP_THUONG_DE_76, 0, -1);
                                                break;
                                            case 1:
                                                if (player.inventory.gold >= 2000000000l) {
                                                    player.inventory.gold -= 2000000000l;
                                                    player.charms.addTimeCharms(2025, 11);
                                                    Service.getInstance().sendMoney(player);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Phù sức mạnh thành công, ngươi được tăng 20% tiềm năng sức mạnh trong 10 phút khi đánh quái tại đây");
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Ngươi không đủ vàng, cần 2 tỷ vàng để phù");
                                                }
                                                break;
                                            case 2:// về đảo kame
                                                ChangeMapService.gI().changeMapInYard(player, 5, -1, 143);
                                                break;
                                            case 3:

                                                break;
                                        }
                                    }
                                } else if (this.mapId == 177) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:// về đảo kame
                                                ChangeMapService.gI().changeMapInYard(player, 5, -1, 143);
                                                break;
                                        }
                                    }
                                } else {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 176);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.SANTA:
                    npc = new SANTA(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.TRONG_TAI_BANG:
                    npc = new TrongtaiB(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.URON:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                this.openShopWithGender(pl, ConstNpc.SHOP_URON_0, 0);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {

                            }
                        }
                    };
                    break;
                case ConstNpc.EVENT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì?",
                                        "Cửa hàng",
                                        "Đóng");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ShopService.gI().openShopSpecial(player, this,
                                                    ConstNpc.SHOP_EVENT, 0, -1);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BA_HAT_MIT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5 || this.mapId == 20 || this.mapId == 13) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì?",
                                            "Ép sao\ntrang bị", "Pha lê\nhóa\ntrang bị","Chuyển Hóa\nSao Pha Lê", "SKH\nThần linh",
                                            "Pháp sư\n trang bị", "Ấn\n Trang bị","Tẩy\nẤn");
                                } else if (this.mapId == 121) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì?",
                                            "Về đảo\nrùa");
                                } else if (this.mapId == 167) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Đã khuya rồi sao con còn ở chốn này ?",
                                            "Chế tạo\n Bó kẹo\nKinh dị", "Chế tạo\n Giỏ kẹo\n Kinh dị", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì?",
                                            "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm", "Nhập\nNgọc Rồng",
                                            "Chức năng\nPorata");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5 || this.mapId == 20 || this.mapId == 13) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.EP_SAO_TRANG_BI);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                                break;
                                            case 2:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.CHUYEN_SPL);
                                                break;
                                            case 33:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.TAY_SPL);
                                                break;    
                                            case 3:
                                                this.createOtherMenu(player, ConstNpc.MENU_SET_KH,
                                                        "Chọn nâng cấp của con đi"
                                                                + " ...",
                                                        "Nâng cấp\nSkh vip",
                                                        "Từ Chối");
                                                break;
                                            case 4:
                                                this.createOtherMenu(player, 10,
                                                        "Nâng cấp trang bị pháp sư giúp tăng ngẫu nhiên các chỉ số\n"
                                                                + "Chỉ pháp sư hóa được phụ kiện ",
                                                        "Nâng cấp\nPháp sư", "Xóa\ndòng");
                                                break;
                                            case 5:
                                                this.createOtherMenu(player, 111,
                                                        "Muốn khảm ấn trang bị? đủ 5 món cộng chỉ số đó nha\n"
                                                                + "Tinh ấn: +15% HP\n"
                                                         + "Nguyệt ấn: +15% SD\n"
                                                         + "Nhật ấn: +15% KI",
                                                        "Khảm\n ấn");
                                                break;
                                            case 6:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.TAY_AN_TRANG_BI);
                                                break; 
                                        }
                                    } else if (player.iDMark.getIndexMenu() == 10) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.BONG_TOI_TRANG_BI);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.DLETE_BONG_TOI_TRANG_BI);
                                                break;
                                            case 2:
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Ngọc pháp sư giúp nâng cấp Trang bị thường lên Trang bị ánh sáng\n "
                                                                + "Các trang bị có thể nâng cấp ánh sáng bao gồm cải trang, vật phẩm đeo lưng, "
                                                                + "giáp luyện tập, pet, linh thú,...\n"
                                                                + "Chỉ số mỗi lần nâng cấp sẽ xuât hiện 1 trong 3 loại\n"
                                                                + "Tối đa là 8 cấp, mỗi cấp sẽ cộng thêm chỉ số theo cấp\n Cấp càng cao, chỉ số càng mạnh"
                                                                + "\nNếu không ưng ý, hãy mang cho ta đá tẩy ánh sáng có thể xóa các dòng chỉ số khi cần\nTa chúc ngươi may mắn !",
                                                        "Đóng");
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == 111) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.AN_TRANG_BI);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.TAY_AN_TRANG_BI);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == 2353) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_ZENO);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.EP_SAO_ZENO);
                                                break;
                                        }
                                    }  else if (player.iDMark.getIndexMenu() == 11) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.CHE_BIEN_TRA_HOA_CUC);
                                                break;
                                            case 1:
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "|0|Nhân ngày 20 tháng 11 và để cảm ơn thầy cô \nhãy tranh thủ gia công bằng tay các vật phẩm"
                                                                + "\n Biếu tặng những phần quà để tỏ lòng tôn kính \n Ta ở đây sẽ giúp ngươi các công việc đó"
                                                                + "\n Hãy mang đến cho ta các vật phẩm ngươi có thể thu thập qua các map (trái đất , namec , xayda ) sẽ nhận được Lá trà tươi"
                                                                + "\n Khu vực map Nappa sẽ nhận được Nia tre và con có thể đến tương lai thu thập que tre nhưng sớm quay trở lại nhé"
                                                                + "\n Cuối cùng con cần thêm hoa cúc sau vườn santa để có thể làm thành vật phẩm thượng hạng !!"
                                                                + "\n|5|Chúc con nhiều may mắn !!!",
                                                        "Đóng");
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_SET_KH) {
                                        switch (select) {
                                            case 120:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_SKH_THUONG_GOLD_BAR);
                                                break;
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_SKH_VIP);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHUYEN_HOA_TRANG_BI) {
                                        switch (select) {
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHUYEN_SPL) {
                                            if (select == 0) {
                                                CombineServiceNew.gI().startCombine(player);
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TAY_SPL) {
                                            if (select == 0) {
                                                CombineServiceNew.gI().startCombine(player);
                                            }
                                        }else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TAY_AN) {
                                            if (select == 0) {
                                                CombineServiceNew.gI().startCombine(player);
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        NpcMethod.gI().startCombine(player, select);
                                        switch (player.combineNew.typeCombine) {
                                            case CombineServiceNew.EP_SAO_TRANG_BI:
                                                switch (select) {
                                                    case 0 -> {
                                                        if (player.combineNew.typeCombine == CombineServiceNew.EP_SAO_TRANG_BI) {
                                                            player.combineNew.epSao = 1;
                                                        }
                                                    }
                                                    case 1 -> {
                                                        if (player.combineNew.typeCombine == CombineServiceNew.EP_SAO_TRANG_BI) {
                                                            player.combineNew.epSao = 2;
                                                        }
                                                    }

                                                }
                                                case CombineServiceNew.TAY_AN_TRANG_BI:
                                                    if (select == 0) {
                                                    CombineServiceNew.gI().startCombine(player);
                                                }
                                                break;
                                        }
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                } else if (this.mapId == 167) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.CHE_TAO_BO_KEO_KINH_DI);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.CHE_TAO_GIO_KEO_KINH_DI);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        switch (player.combineNew.typeCombine) {
                                            case CombineServiceNew.CHE_TAO_BO_KEO_KINH_DI:
                                            case CombineServiceNew.CHE_TAO_GIO_KEO_KINH_DI:
                                                if (select == 0) {
                                                    CombineServiceNew.gI().startCombine(player);
                                                }
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 112) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44
                                        || this.mapId == 84) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0: // shop bùa
                                                createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                                        "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                                + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                                        "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng",
                                                        "Đóng");
                                                break;
                                            case 1:
                                                // CombineService.gI().openTabCombine(player,
                                                // CombineService.NANG_CAP_TRANG_BI);
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_VAT_PHAM);
                                                break;
                                            case 2:
                                                // CombineService.gI().openTabCombine(player,
                                                // CombineService.NHAP_NGOC_RONG);
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NHAP_NGOC_RONG);
                                                break;
                                            case 3: // shop bùa
                                                createOtherMenu(player, ConstNpc.MENU_GET_BONG_TAI,
                                                        "|0|Chức năng bông tai ở hết đây!!\n"
                                                                + "Con muốn nâng cấp gì cứ trọn\n"
                                                                + "|7|-Chỉ số full Cấp 2 10%\n"
                                                                + "|7|-Chỉ số full Cấp 3 15%\n"
                                                                ,
                                                        "Nâng Cấp\nBông tai", "Nâng Cấp\nChỉ số\nBông tai",
                                                        "Nâng Cấp\nPorata 3", "Nâng Cấp\nChỉ số\nPorata 3"
                                                        );
                                                break;
                                            case 4:
                                                // this.createOtherMenu(player, 10,
                                                // "Nâng cấp trang bị ánh sáng giúp tăng ngẫu nhiên các chỉ số HP, KI,"
                                                // +
                                                // "SD, ngoài ra\n ngươi cũng có thể xóa chỉ số nếu không ưng ý"
                                                // +
                                                // ".\nNgươi muốn ra giúp gì?",
                                                // "Nâng\ncấp", "Tẩy\ndòng", "Hướng dẫn");
                                                // this.createOtherMenu(player, 10,
                                                // "Nâng cấp trang bị pháp sư giúp tăng ngẫu nhiên các chỉ số "
                                                // + "HP, KI, SD, ngoài ra\n ngươi cũng có thể xóa dòng "
                                                // + " bóng tối khi có Ngọc pháp sư.\nNgươi muốn ra giúp"
                                                // + " gì?\n (Vật phẩm cải trang cần Đá bóng ma ,"
                                                // + "Vật phẩm đeo lưng cần Đá bóng thuật)",
                                                // "Nâng cấp\nPháp sư", "Xóa\ndòng", "Hướng dẫn");
                                                break;

                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_GET_BONG_TAI) {
                                        switch (select) {
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_BONG_TAI);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_CHI_SO_BONG_TAI);
                                                break;
                                            case 2:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_BONG_TAI_3);
                                                break;
                                            case 3:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_CHI_SO_BONG_TAI_3);
                                                break;
                                            case 4:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_BONG_TAI_4);
                                                break;
                                            case 5:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.NANG_CAP_CHI_SO_BONG_TAI_4);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                                        switch (select) {
                                            case 0:
                                                ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_0, 0);
                                                break;
                                            case 1:
                                                ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_1, 1);
                                                break;
                                            case 2:
                                                ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_2, 2);
                                                break;
                                            case 3:
                                                // ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_3, 3);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        switch (player.combineNew.typeCombine) {
                                            case CombineServiceNew.CHUYEN_SPL:
                                            case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                            case CombineServiceNew.NANG_CAP_CHI_SO_BONG_TAI:
                                            case CombineServiceNew.NANG_CAP_BONG_TAI:
                                            case CombineServiceNew.NANG_CAP_CHI_SO_BONG_TAI_3:
                                            case CombineServiceNew.NANG_CAP_BONG_TAI_3:
                                            case CombineServiceNew.NANG_CAP_CHI_SO_BONG_TAI_4:
                                            case CombineServiceNew.NANG_CAP_BONG_TAI_4:
                                            case CombineServiceNew.LAM_PHEP_NHAP_DA:
                                            case CombineServiceNew.NHAP_NGOC_RONG:
                                            case CombineServiceNew.NANG_CAP_BONG_TOI:
                                            case CombineServiceNew.THANH_TAY_BONG_TOI:
                                                if (select == 0) {
                                                    CombineServiceNew.gI().startCombine(player);
                                                }
                                                if (select == 1) {
                                                    CombineServiceNew.gI().startCombine_2(player);
                                                }
                                                break;
                                                case CombineServiceNew.AN_TRANG_BI:
                                                case CombineServiceNew.TAY_AN_TRANG_BI:
                                                    if (select == 0) {
                                                        CombineServiceNew.gI().startCombine(player);
                                                    }
                                                    break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RUONG_DO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                InventoryService.gI().sendItemBox(player);
                                InventoryService.gI().openBox(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {

                            }
                        }
                    };
                    break;
                // case ConstNpc.RUONG_PET_CAITRANG:
                // npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                //
                // @Override
                // public void openBaseMenu(Player player) {
                // if (canOpenNpc(player)) {
                // InventoryService.gI().sendItemBox_pet_ct(player);
                // InventoryService.gI().openBox_pet_ct(player);
                // System.err.println("oki1");
                // }
                // }
                //
                // @Override
                // public void confirmMenu(Player player, int select) {
                // if (canOpenNpc(player)) {
                //
                // }
                // }
                // };
                // break;
                case ConstNpc.DAU_THAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                player.magicTree.openMenuTree();
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                TaskService.gI().checkDoneTaskConfirmMenuNpc(player, this, (byte) select);
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                                        if (select == 0) {
                                            player.magicTree.harvestPea();
                                        } else if (select == 1) {
                                            if (player.magicTree.level == 10) {
                                                player.magicTree.fastRespawnPea();
                                            } else {
                                                player.magicTree.showConfirmUpgradeMagicTree();
                                            }
                                        } else if (select == 2) {
                                            player.magicTree.fastRespawnPea();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                                        if (select == 0) {
                                            player.magicTree.harvestPea();
                                        } else if (select == 1) {
                                            player.magicTree.showConfirmUpgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                                        if (select == 0) {
                                            player.magicTree.upgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_UPGRADE:
                                        if (select == 0) {
                                            player.magicTree.fastUpgradeMagicTree();
                                        } else if (select == 1) {
                                            player.magicTree.showConfirmUnuppgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                                        if (select == 0) {
                                            player.magicTree.unupgradeMagicTree();
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CALICK:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        private final byte COUNT_CHANGE = 50;
                        private int count;

                        private void changeMap() {
                            if (this.mapId != 102) {
                                count++;
                                if (this.count >= COUNT_CHANGE) {
                                    count = 0;
                                    this.map.npcs.remove(this);
                                    Map map = MapService.gI().getMapForCalich();
                                    this.mapId = map.mapId;
                                    this.cx = Util.nextInt(100, map.mapWidth - 100);
                                    this.cy = map.yPhysicInTop(this.cx, 0);
                                    this.map = map;
                                    this.map.npcs.add(this);
                                }
                            }
                        }

                        @Override
                        public void openBaseMenu(Player player) {
                            player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                            if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_6) {
                                Service.getInstance().hideWaitDialog(player);
                                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                return;
                            }
                            if (this.mapId != player.zone.map.mapId) {
                                Service.getInstance().sendThongBao(player, "Calích đã rời khỏi map!");
                                Service.getInstance().hideWaitDialog(player);
                                return;
                            }

                            if (this.mapId == 102) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào chú, cháu có thể giúp gì?",
                                        "Kể\nChuyện", "Quay về\nQuá khứ");
                            } else {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào chú, cháu có thể giúp gì?",
                                        "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 102) {
                                if (player.iDMark.isBaseMenu()) {
                                    if (select == 0) {
                                        // kể chuyện
                                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                                    } else if (select == 1) {
                                        // về quá khứ
                                        ChangeMapService.gI().goToQuaKhu(player);
                                    }
                                }
                            } else if (player.iDMark.isBaseMenu()) {
                                if (select == 0) {
                                    // kể chuyện
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                                } else if (select == 1) {
                                    // đến tương lai
                                    // changeMap();
                                    if (TaskService.gI().getIdTask(player) > ConstTask.TASK_20_6) {
                                        ChangeMapService.gI().goToTuongLai(player);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.JACO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.MENU_NHAN_THUONG_QL,
                                        "Nhận thưởng tự động...", "Điểm danh\nHàng ngày",
                                         "Quà\n Nạp Đầu","Từ chối");
                            }
                        }

//                        @Override
//                        public void confirmMenu(Player player, int select) {
//                            if (canOpenNpc(player)) {
//                                // if (this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
//                                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NHAN_THUONG_QL) {
//                                        switch (select) {
//                                            case 0:
//                                                if (player.getSession().actived) {
//                                                    this.openShop(player, ConstNpc.SHOP_DIEM_DANH, 2);
//                                                } else {
//                                                    Service.getInstance().sendThongBaoFromAdmin(player,
//                                                            "Bạn cần nạp đầu"
//                                                                    + "để mở Shop Điểm Danh,"
//                                                                    + " nhằm tránh lạm phát!");
//                                                }
//                                                break;
////                                            case 1:
////                                                this.openShop(player, ConstNpc.SHOP_MOC_NAP, 5);
////                                                break;
//                                            case 1:
//                                                 this.createOtherMenu(player, 1254,
//                                            "Bạn đã nạp: "+player.getSession().tongNap+" VND\n\n"
//                                                         + "|7|Phần thưởng nạp đầu gồm: \n"
//                                                         + "|0|- 80K Thỏi Vàng Khóa\n"
//                                                         + "- X10 ngọc rồng 3 sao\n"
//                                                         + "- X1 rương thần linh tự chọn",
//                                            "Nhận Quà", "Đóng");
//                                                break;
//                                        }
//                                    } else if (player.iDMark.getIndexMenu() == 1254) {
//                                        switch (select) {
//                                            case 0:
//                                                if (player.getSession().tongNap >= 50000) {
//
//                                                    if (player.napDau == 0) {
//
//                                                        // Tạo item
//                                                        Item tv = ItemService.gI().createNewItem((short) 1429, 80_000);
//                                                        Item nr3s = ItemService.gI().createNewItem((short) 16, 10);
//                                                        Item ruongtl = ItemService.gI().createNewItem((short) 1997, 1);
//
//                                                        InventoryService.gI().addItemBag(player, tv, 80000);
//                                                        InventoryService.gI().addItemBag(player, nr3s, 10);
//                                                        InventoryService.gI().addItemBag(player, ruongtl, 1);
//                                                        InventoryService.gI().sendItemBags(player);
//
//                                                        // cập nhật flag
//                                                        player.napDau = 1;
//                                                        PlayerDAO.updateNapDau(player);
//
//                                                        Service.getInstance().sendThongBaoOK(player,
//                                                                "Bạn đã nhận quà nạp đầu thành công!");
//
//                                                    } else {
//                                                        Service.getInstance().sendThongBaoOK(player,
//                                                                "Bạn đã nhận quà nạp đầu trước đó rồi!");
//                                                    }
//
//                                                } else {
//                                                    Service.getInstance().sendThongBaoOK(player,
//                                                            "Bạn chưa nạp đủ 50K!");
//                                                }
//                                                break;
//
//                                            
//
//                                        }
//                                    }
//                                // }
//
//                            }
//                        }
//                    };
                    @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // if (this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NHAN_THUONG_QL) {
                                        switch (select) {
                                            case 0:
                                                if (player.getSession().tongNap >= 50000) {
                                                    this.openShop(player, ConstNpc.SHOP_DIEM_DANH, 2);
                                                } else {
                                                    Service.getInstance().sendThongBaoFromAdmin(player,
                                                            "Bạn cần mở gói nạp đầu "
                                                                    + "để mở Shop Điểm Danh,"
                                                                    + " nhằm tránh lạm phát!");
                                                }
                                                break;
                                            case 1:
                                                 this.createOtherMenu(player, 1254,
                                            "Bạn đã nạp: "+player.getSession().tongNap+" VND\n\n"
                                                         + "|7|Phần thưởng nạp đầu gồm: \n"
                                                         + "|0|- 80K Thỏi Vàng Khóa\n"
                                                         + "- X10 ngọc rồng 3 sao\n"
                                                         + "- X1 rương thần linh tự chọn\n"
                                                        + "- X5 bình x2x3x4 tnsm\n"
                                                    + "- Mở Giao Dịch Ngọc Rồng Hero\n\n"
                                                         + "|2| Gói nạp đầu tối thiểu 50k",
                                            "Nhận Quà", "Đóng");
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == 1254) {
                                        switch (select) {
                                            case 0:
                                                if (player.getSession().tongNap >= 50000) {
                                                    if (!player.getSession().actived) {
                                                        player.getSession().actived = true;
                                                        PlayerDAO.ActivedPlayer(player.getSession().userId);
                                                    }
                                                    if (player.napDau == 0) {
if (InventoryService.gI().getCountEmptyBag(player) < 7) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 7 ô trống trong hành trang");
            return;
        }
                                                        // Tạo item
                                                         PlayerDAO.ActivedPlayer(player.getSession().userId);
                                                        player.getSession().actived = true;
                                                        Item tv = ItemService.gI().createNewItem((short) 1429, 80_000);
                                                        tv.itemOptions.add(new ItemOption(30, 0));
                                                        Item nr3s = ItemService.gI().createNewItem((short) 16, 10);
                                                        nr3s.itemOptions.add(new ItemOption(30, 0));
                                                        Item ruongtl = ItemService.gI().createNewItem((short) 1997, 1);
                                                        ruongtl.itemOptions.add(new ItemOption(30, 0));
                                                        Item x2 = ItemService.gI().createNewItem((short) 1994, 5);
                                                        x2.itemOptions.add(new ItemOption(30, 0));
                                                        Item x3 = ItemService.gI().createNewItem((short) 1995, 5);
                                                        x3.itemOptions.add(new ItemOption(30, 0));
                                                        Item x4 = ItemService.gI().createNewItem((short) 1996, 5);
                                                        x4.itemOptions.add(new ItemOption(30, 0));
                                                        
                                                        InventoryService.gI().addItemBag(player, tv, 80000);
                                                        InventoryService.gI().addItemBag(player, nr3s, 10);
                                                        InventoryService.gI().addItemBag(player, ruongtl, 1);
                                                        InventoryService.gI().addItemBag(player, x2, 5);
                                                        InventoryService.gI().addItemBag(player, x3, 5);
                                                        InventoryService.gI().addItemBag(player, x4, 5);
                                                        InventoryService.gI().sendItemBags(player);
                                                        // cập nhật flag
                                                        player.napDau = 1;
                                                        PlayerDAO.updateNapDau(player);

                                                        Service.getInstance().sendThongBaoOK(player,
                                                                "Bạn đã nhận quà nạp đầu thành công!");

                                                    } else {
                                                        Service.getInstance().sendThongBaoOK(player,
                                                                "Bạn đã nhận quà nạp đầu trước đó rồi!");
                                                    }

                                                } else {
                                                    Service.getInstance().sendThongBaoOK(player,
                                                            "Bạn chưa nạp đủ 50K!");
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                        };
                    break;
                case ConstNpc.POTAGE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 4) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ngươi có muốn đến hắc tinh? Nơi đó là nơi ở của những con quái vật Hatchiyack trong truyền thuyết!"
                                                    + "\nĐảo côn trùng nơi chưa được khai phá, chứa nhiều vật phẩm quý và sức mạnh khủng"
                                                    + "\n|5|Hãy cẩn thận",
                                            "Đến\nHắc tinh", "Đảo\ncôn trùng", "Cửa hàng\nPotage", "Từ chối");
                                } else if (MapService.gI().isMapContrung(mapId)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Đây là nơi ở của Xên bọ hung, hãy cẩn thận, hắn sẽ xuất hiện mỗi 10 phút sau khi bị hạ gục\n",
                                            "Về\nRừng Xương", "Cửa hàng\nPotage", "Triệu hồi\nbọ hiếm", "Đóng");
                                } else if (MapService.gI().isMapHacTinh(mapId)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Đánh quái tại nơi đây sẽ rơi ra đá hắc tinh\n"
                                                    + "|2| Ngươi có muốn dùng x99 đá hắc tinh và 50 xu để đổi đôi cánh hắc tinh?\n|7|(có tỉ lệ vĩnh viễn)",
                                            "Đổi\n Cánh", "Về\nRừng Xương", "Cửa hàng\nPotage", "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 4) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 139, -1, 117);
                                                break;
                                            case 1:
                                                if (Manager.EVENT_SEVER == 15) {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 168, -1, 1185);
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                            "Tính năng sẽ được cập nhật trong sự kiện sắp tới",
                                                            "đóng");
                                                }

                                                break;
                                            case 2:
                                                ShopService.gI().openShopSpecial(player, this,
                                                        ConstNpc.SHOP_POTAGE, 0, -1);
                                                break;

                                        }
                                    }
                                } else if ((MapService.gI().isMapContrung(mapId))) {

                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 4, -1, 117);
                                                break;
                                            case 1:
                                                ShopService.gI().openShopSpecial(player, this,
                                                        ConstNpc.SHOP_POTAGE, 0, -1);
                                                break;
                                            case 2:
                                                this.createOtherMenu(player, 1121,
                                                        "Ta sẽ giúp ngươi bắt giữ bọ hiếm về cho Quy Lão\n"
                                                                + "|5|Dùng 3 loại bọ (mỗi loại 20 con) và 2 que đốt để để đổi lấy bọ trong truyền thuyết, ngươi sẽ nhận ngẫu nhiên 1 trong 2 con bọ trong truyền thuyết đấy!",
                                                        "Đổi\n1 lần", "Đổi\n5 lần", "Đổi\n20 lần",
                                                        "Đóng");

                                                break;

                                        }
                                    } else if (player.iDMark.getIndexMenu() == 1121) {
                                        switch (select) {
                                            case 0:
                                                EventService.gI().doi_bo_hung_Random(player, 1);
                                                break;
                                            case 1:
                                                EventService.gI().doi_bo_hung_Random(player, 5);
                                                break;
                                            case 2:
                                                EventService.gI().doi_bo_hung_Random(player, 20);
                                                break;

                                        }
                                    }

                                } else if (MapService.gI().isMapHacTinh(mapId)) {

                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                NpcMethod.gI().DoiCanhHacTinh(player, this);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 4, -1, 117);
                                                break;
                                            case 2:
                                                ShopService.gI().openShopSpecial(player, this,
                                                        ConstNpc.SHOP_POTAGE, 0, -1);
                                                break;

                                        }
                                    }

                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.THAN_MEO_KARIN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (mapId == ConstMap.THAP_KARIN) {
                                    if (player.zone instanceof ZSnakeRoad) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Hãy cầm lấy hai hạt đậu cuối cùng ở đây\nCố giữ mình nhé "
                                                        + player.name,
                                                "Cảm ơn\nsư phụ");
                                    }
                                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                        String message;
                                        if (player.istrain) {
                                            message = "Muốn chiến thắng Tàu Pảy Pảy phải đánh bại được ta";
                                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message,
                                                    "Hủy đăng ký tập tự động", "Tập luyện với\nThần Mèo",
                                                    "Thách đấu với\nThần Mèo");
                                        } else {
                                            message = "Muốn chiến thắng Tàu Pảy Pảy phải đánh bại được ta";
                                            this.createOtherMenu(player, ConstNpc.BASE_MENU, message,
                                                    "Đăng ký tập tự động", "Tập luyện với\nThần Mèo",
                                                    "Thách đấu với\nThần Mèo");
                                        }
                                        // this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp
                                        // gì nào?", getMenuSuKien(EVENT_SEVER));
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (mapId == ConstMap.THAP_KARIN) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (!player.istrain) {
                                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE,
                                                            "Đăng ký để mỗi khi Offline quá 30 phút, con sẽ được tự động luyện tập với tốc độ "
                                                                    + player.nPoint.getexp() + " sức mạnh mỗi phút",
                                                            "Hướng dẫn thêm", "Đồng ý 1 ngọc mỗi lần", "Không đồng ý");
                                                    break;
                                                } else {
                                                    player.istrain = false;
                                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                            "Con đã hủy thành công đăng ký tập tự động", "Đóng");
                                                }
                                                break;
                                            case 1:
                                                if (player.playerTask.taskMain.id == 5
                                                        && player.playerTask.taskMain.index == 5) {
                                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_THAN_MEO,
                                                            "Con có chắc muốn tập luyện?\nTập luyện với mèo thần Karin?",
                                                            "Đồng ý luyện tập", "Không đồng ý");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_THAN_MEO,
                                                            "Con có chắc muốn tập luyện?\nTập luyện với mèo thần Karin?",
                                                            "Đồng ý luyện tập", "Không đồng ý");
                                                }
                                                break;
                                            case 2:
                                                this.createOtherMenu(player, ConstNpc.MENU_CHALLENGE_THAN_MEO,
                                                        "Con có chắc muốn thách đấu?\nThách đấu với mèo thần Karin?",
                                                        "Đồng ý thách đấu", "Không đồng ý");
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                                        switch (select) {
                                            case 0:
                                                Service.getInstance().sendPopUpMultiLine(player, tempId, this.avartar,
                                                        ConstNpc.INFOR_TRAIN_OFFLINE);
                                                break;
                                            case 1:
                                                player.istrain = true;
                                                NpcService.gI().createTutorial(player, this.avartar,
                                                        "Từ giờ, quá 30 phút Offline con sẽ tự động luyện tập");
                                                break;
                                            case 3:
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_THAN_MEO) {
                                        switch (select) {
                                            case 0:
                                                // player.setfight((byte) 0, (byte) 1);
                                                // player.zone.load_Me_To_Another(player);
                                                // player.zone.loadAnotherToMe(player);
                                                // player.zone.mapInfo(player);
                                                // DataGame.updateMap(player.getSession());
                                                // try {
                                                // new ThanMeoKarin(BossFactory.THAN_MEO, BossData.THAN_MEO,
                                                // player.zone, player);
                                                // } catch (Exception e) {
                                                // e.printStackTrace();
                                                // }
                                                Service.getInstance().sendThongBao(player,
                                                        "Không thể thách đấu trong lúc này");
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHALLENGE_THAN_MEO) {
                                        switch (select) {
                                            case 0:
                                                // player.setfight((byte) 1, (byte) 1);
                                                // player.zone.load_Me_To_Another(player);
                                                // player.zone.loadAnotherToMe(player);
                                                // player.zone.mapInfo(player);
                                                // DataGame.updateMap(player.getSession());
                                                // try {
                                                // new ThanMeoKarin(BossFactory.THAN_MEO, BossData.THAN_MEO,
                                                // player.zone, player);
                                                // } catch (Exception e) {
                                                // e.printStackTrace();
                                                // }
                                                Service.getInstance().sendThongBao(player,
                                                        "Không thể thách đấu trong lúc này");
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.THUONG_DE:
                    npc = new Npc_ThuongDe(mapId, status, cx, cy, tempId, avartar);
                    break;

                case ConstNpc.THAN_VU_TRU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 48) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con muốn làm gì nào",
                                            "Di chuyển");
                                } else if (mapId == 184) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "HÃY RỜI KHỎI ĐÂY MAU",
                                            "Di chuyển");

                                } else if (mapId == 0) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho con??",
                                            "Cửa hàng", "Ghép\ncải trang", "đóng");

                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 48) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                                        "Con muốn đi đâu?", "Về\nthần điện",
                                                        "Thánh địa\nKaio", EventService.getMenuSuKien(EVENT_SEVER),
                                                        "Từ chối");
                                                break;
                                            case 1:
                                                switch (EVENT_SEVER) {
                                                    case 2:
                                                        Attribute at = ServerManager.gI().getAttributeManager()
                                                                .find(ConstAttribute.HP);
                                                        String text = "Sự kiện 20/11 chính thức tại Ngọc Rồng "
                                                                + Manager.SERVER_NAME + "\n "
                                                                + "Số điểm hiện tại của bạn là : "
                                                                + player.event.getEventPoint()
                                                                + "\nTổng số hoa đã tặng trên toàn máy chủ "
                                                                + EVENT_COUNT_THAN_VU_TRU % 999 + "/999";
                                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                                                                at != null && !at.isExpired() ? text
                                                                        + "\nToàn bộ máy chủ được tăng 20% HP,thời gian còn lại "
                                                                        + at.getTime() / 60 + " phút."
                                                                        : text + "\nKhi tặng đủ 999 bông hoa toàn bộ máy chủ được tăng 20% HP trong 60 phút\n",
                                                                "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông",
                                                                "Đổi\nHộp quà");
                                                        break;
                                                }
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                                break;

                                            // case 2:
                                            // con đường rắn độc
                                            // Service.getInstance().sendThongBao(player, "Comming Soon.");
                                            // if (player.clan != null) {
                                            // Calendar calendar = Calendar.getInstance();
                                            // int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                            // if (!(dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.WEDNESDAY
                                            // || dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.SUNDAY)) {
                                            // Service.getInstance().sendThongBao(player, "Chỉ mở vào thứ 2, 4, 6,
                                            // CN hàng tuần!");
                                            // return;
                                            // }
                                            // if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                            // Service.getInstance().sendThongBao(player, "Phải tham gia bang hội ít
                                            // nhất 2 ngày mới có thể tham gia!");
                                            // return;
                                            // }
                                            // if (player.clan.snakeRoad == null) {
                                            // this.createOtherMenu(player, ConstNpc.MENU_CHON_CAP_DO, "Hãy mau trở
                                            // về bằng con đường rắn độc\nbọn Xayda đã đến Trái Đất", "Chọn\ncấp
                                            // độ", "Từ chối");
                                            // } else {
                                            // if (player.clan.snakeRoad.isClosed()) {
                                            // Service.getInstance().sendThongBao(player, "Bang hội đã hết lượt tham
                                            // gia!");
                                            // } else {
                                            // this.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_CDRD, "Con có
                                            // chắc chắn muốn đến con đường rắn độc cấp độ " +
                                            // player.clan.snakeRoad.getLevel() + "?", "Đồng ý", "Từ chối");
                                            // }
                                            // }
                                            // } else {
                                            // Service.getInstance().sendThongBao(player, "Chỉ dành cho những người
                                            // trong bang hội!");
                                            // }
                                            // break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHON_CAP_DO) {
                                        switch (select) {
                                            case 0:
                                                // Input.gI().createFormChooseLevelCDRD(player);
                                                break;

                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_CDRD) {
                                        switch (select) {
                                            case 0:
                                                if (player.clan != null) {
                                                    synchronized (player.clan) {
                                                        if (player.clan.snakeRoad == null) {
                                                            int level = Byte.parseByte(
                                                                    String.valueOf(PLAYERID_OBJECT.get(player.id)));
                                                            SnakeRoad road = new SnakeRoad(level);
                                                            ServerManager.gI().getDungeonManager().addDungeon(road);
                                                            road.join(player);
                                                            player.clan.snakeRoad = road;
                                                        } else {
                                                            player.clan.snakeRoad.join(player);
                                                        }
                                                    }
                                                }
                                                break;

                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_SUKIEN) {
                                        EventService.gI().openMenuSuKien(player, this, tempId, select);
                                    }
                                } else if (mapId == 184) {
                                    if (select == 0) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, -1);
                                    }
                                } else if (this.mapId == 0) { // thành phố vegeta
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ShopService.gI().openShopSpecial(player, this,
                                                        ConstNpc.SHOP_THAN_VU_TRU, 0, -1);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.GHEP_CAI_TRANG_2);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        NpcMethod.gI().startCombine(player, select);
                                    }
                                }
                            }
                        }

                    };
                    break;
                case ConstNpc.FU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 185) {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Cậu có bao nhiêu nguyên liệu rồi\n"
                                                    + "|7|Công thức\n"
                                                    + "|3|Hạn Sử Dụng: X50 Ma Qoái + 20 Tinh Thể + 5000 Thỏi Vàng\n"
                                                    + "|1|Vĩnh Viễn:X99 Ma Qoái + 20 Tinh thể + 15000 Thỏi Vàng\n",
                                            "Thiên Tử\n HSD 5 Ngày\n(-5K TVK)",
                                            "Thiên Tử\nVĩnh Viễn\n(-15K TVK)",
                                            " Về nhà");
                                } else if (mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Thế hệ mới sẽ giết con người các ngươi?",
                                            "Cửa hàng", "Ghép\ncải trang", "Tới map\n Thiên tử", "đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 15) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                break;
                                            case 1:
                                                break;
                                            case 2:
                                                break;
                                        }
                                    }
                                } else if (mapId == 185) {
//                                    if (select == 0) {
//                                        ShopService.gI().openShopSpecial(player, this,
//                                                ConstNpc.SHOP_THAN_VU_TRU1, 1, -1);
//                                    }
                                    if (select == 0) {
                                        Item dauxanh1 = InventoryService.gI().findItem(player, 1517, 50); // dau xanh
                                        Item ga1 = InventoryService.gI().findItem(player, 1518, 20); // ga
                                        Item tv = InventoryService.gI().findItem(player, 1429, 5000); // ga
                                        if (dauxanh1 != null && ga1 != null) {
                                            if (tv != null) {
                                                InventoryService.gI().subQuantityItemsBag(player, dauxanh1, 50);
                                                InventoryService.gI().subQuantityItemsBag(player, ga1, 20);
                                                InventoryService.gI().subQuantityItemsBag(player, tv, 5000);
                                                Item banhChung = ItemService.gI().createNewItem((short) 1508);
                                                banhChung.itemOptions.add(new ItemOption(50, 3));
                                                banhChung.itemOptions.add(new ItemOption(77, 3));
                                                banhChung.itemOptions.add(new ItemOption(103, 3));
                                                banhChung.itemOptions.add(new ItemOption(93, 5));
                                                banhChung.itemOptions.add(new ItemOption(30, 0));
                                                Service.getInstance().sendMoney(player);
                                                InventoryService.gI().addItemBag(player, banhChung, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận thành công vòng chân thiên tử 5 ngày");
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ thỏi vàng khóa");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                        }

                                    }
                                    if (select == 1) {
                                        Item maqoai = InventoryService.gI().findItem(player, 1517, 99); // dau xanh
                                        Item tinhthe = InventoryService.gI().findItem(player, 1518, 20); // ga
                                        Item tv = InventoryService.gI().findItem(player, 1429, 15000); // ga
                                        if (maqoai != null && tinhthe != null) {
                                            if (tv != null) { // Kiểm tra nếu đủ ruby
                                                InventoryService.gI().subQuantityItemsBag(player, maqoai, 99);
                                                InventoryService.gI().subQuantityItemsBag(player, tinhthe, 20);
                                                InventoryService.gI().subQuantityItemsBag(player, tv, 15000);
                                                Item Thientuvv = ItemService.gI().createNewItem((short) 1508);
                                                Thientuvv.itemOptions.add(new ItemOption(50, 3));
                                                Thientuvv.itemOptions.add(new ItemOption(77, 3));
                                                Thientuvv.itemOptions.add(new ItemOption(103, 3));
                                                Thientuvv.itemOptions.add(new ItemOption(30, 0));
                                                InventoryService.gI().addItemBag(player, Thientuvv, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận thành công vòng chân thiên tử Vĩnh viễn");
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ thỏi vàng khóa");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                        }

                                    }
                                    if (select == 33) {
                                        Item maqoai1 = InventoryService.gI().findItem(player, 457, 3);
                                        if (maqoai1 != null) {
                                            if (player.inventory.ruby >= 199) { // Kiểm tra nếu đủ ruby
                                                InventoryService.gI().subQuantityItemsBag(player, maqoai1, 1);
                                                Item Thientuvv1 = ItemService.gI().createNewItem((short) 2044);
                                                Thientuvv1.itemOptions.add(new ItemOption(30, 0));
                                                InventoryService.gI().addItemBag(player, Thientuvv1, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                player.inventory.ruby -= 199; // Trừ ruby sau khi kiểm tra
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Nhận máy dò thành công");
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ ruby");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                        }

                                    }
                                    if (select == 2) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                    }
                                } else if (this.mapId == 5) { // thành phố vegeta
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ShopService.gI().openShopSpecial(player, this,
                                                        ConstNpc.SHOP_THAN_VU_TRU, 0, -1);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player,
                                                        CombineServiceNew.GHEP_CAI_TRANG_2);
                                                break;
                                            case 2:
                                                // CombineServiceNew.gI().openTabCombine(player,
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 185, -1, -1);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        NpcMethod.gI().startCombine(player, select);
                                    }
                                }
                            }
                        }

                    };
                    break;
                case ConstNpc.KIBIT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Đến\nKaio", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.OSIN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Đến\nKaio", "Hành Tinh\nBill", "Từ chối");
                                } else if (this.mapId == 52) {
                                    if (MabuWar.gI().isTimeMabuWar()) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bây giờ tôi sẽ bí mật...\n"
                                                + " đuổi theo 2 tên đồ tể... \n"
                                                + "Quý vị nào muốn đi theo thì xin mời !", "Đến\n Cổng\nphi thuyền",
                                                "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hiện chưa phải lúc... \n"
                                                + "Quý vị vui lòng quay lại sau!", "OK");
                                    }
                                    // this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta có thể giúp gì cho
                                    // ngươi ?",
                                    // "Từ chối");

                                } else if (this.mapId == 154) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Về thánh địa", "Hành tinh\n Ngục tù", "Từ chối");
                                } else if (this.mapId == 155) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Quay về", "Từ chối");
                                } else if (MapService.gI().isMapMabuWar(this.mapId)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Đừng vội xem thường Babyđây,ngay đến cha hắn là thần ma đạo sĩ\n"
                                                    + "Bibiđây khi còn sống cũng phải sợ hắn đấy",
                                            "Giải trừ\nphép thuật\n50Tr Vàng",
                                            player.zone.map.mapId != 120 ? "Xuống\nTầng Dưới" : "Lên\nTầng Trên",
                                            "Về nhà");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 52) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (MabuWar.gI().isTimeMabuWar()) {
                                                    ChangeMapService.gI().changeMap(player, 114, -1, 354, 240);
                                                }
                                                break;
                                            case 1:
                                                // ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 50) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                                break;
                                            case 1:
                                                if (player.nPoint.power >= 40_000_000_000L) {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 154, -1, 354);
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Hãy luyện tập dến sức mạnh 40 tỉ để có thể đến hành tinh Bill");
                                                }
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 154) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                                break;
                                            case 1:
                                                if (!Manager.gI().getGameConfig().isOpenPrisonPlanet()) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Lối vào hành tinh ngục tù chưa mở");
                                                    return;
                                                }
                                                if (player.nPoint.power < 60_000_000_000L) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Yêu cầu tối thiếu 60 tỷ sức mạnh");
                                                    return;
                                                }
                                                ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                                Service.getInstance().sendThongBao(player,
                                                        "Không thể vào map khi đang đua top !");
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 155) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            ChangeMapService.gI().changeMapBySpaceShip(player,
                                                    154, -1, 822);
                                        }
                                    }
                                } else if (MapService.gI().isMapMabuWar(this.mapId)) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
//                                                // if (player.inventory.getGold() >= 50000000) {
//                                                // Service.getInstance().changeFlag(player, 9);
//                                                // player.inventory.subGold(50000000);
//
//                                                // } else {
//                                                // Service.getInstance().sendThongBao(player, "Không đủ vàng");
//                                                // }
//                                                Service.getInstance().sendThongBao(player,
//                                                        "Không cần giải trừ phép thuật");
                                                break;
                                            case 1:
//                                                Service.getInstance().sendThongBao(player,
//                                                        "Boss chỉ xuất hiện ở tầng này, không cần xuống tầng dưới");
                                                 if (player.zone.map.mapId == 120) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player,
                                            player.gender + 21, -1, 250);
                                }
                                if (player.cFlag == 9) {
                                    if (player.getPowerPoint() >= 2) {
                                        if (!(player.zone.map.mapId == 119)) {
                                            int idMapNextFloor = player.zone.map.mapId == 115
                                                    ? player.zone.map.mapId + 2
                                                    : player.zone.map.mapId + 1;
                                            ChangeMapService.gI().changeMap(player, idMapNextFloor, -1,
                                                    354, 240);
                                        } else {
                                            Zone zone = MabuWar.gI().getMapLastFloor(120);
                                            if (zone != null) {
                                                ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                            }
                                        }
                                        player.resetPowerPoint();
                                        player.sendMenuGotoNextFloorMabuWar = false;
                                        Service.getInstance().sendPowerInfo(player, "%",
                                                player.getPowerPoint());
                                        if (Util.isTrue(1, 30)) {
                                            player.inventory.ruby += 1;
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            Service.getInstance().sendThongBao(player,
                                                    "Bạn nhận được 1 Hồng Ngọc");
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Bạn đen vô cùng luôn nên không nhận được gì cả");
                                        }
                                    } else {
                                        this.npcChat(player,
                                                "Ngươi cần có đủ điểm để xuống tầng tiếp theo");
                                    }
                                } else {
                                    this.npcChat(player,
                                            "Ngươi đang theo phe Babiđây,Hãy qua bên đó mà thể hiện");
                                }
                                                break;
                                            case 2:

                                                ChangeMapService.gI().changeMapBySpaceShip(player,
                                                        player.gender + 21, -1, 250);

                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BABIDAY:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (MapService.gI().isMapMabuWar(this.mapId)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Đừng vội xem thường Babyđây,ngay đến cha hắn là thần ma đạo sĩ\n"
                                                    + "Bibiđây khi còn sống cũng phải sợ hắn đấy",
                                            "Yểm bùa\n50Tr Vàng",
                                            player.zone.map.mapId != 120 ? "Xuống\nTầng Dưới" : "Lên\nTầng trên",
                                            "Về nhà");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (MapService.gI().isMapMabuWar(this.mapId)) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                // if (player.inventory.getGold() >= 50000000) {
                                                // Service.getInstance().changeFlag(player, 10);
                                                // player.inventory.subGold(50000000);
                                                // } else {
                                                // Service.getInstance().sendThongBao(player, "Không đủ vàng");
                                                // }
                                                Service.getInstance().sendThongBao(player,
                                                        "Không cần giải trừ phép thuật");
                                                break;
                                            case 1:
                                                Service.getInstance().sendThongBao(player,
                                                        "Qua Osin mà đi!");
                                                // if (player.zone.map.mapId == 120) {
                                                // ChangeMapService.gI().changeMapBySpaceShip(player,
                                                // 114, -1, 250);
                                                // } else if (player.zone.map.mapId == 114) {
                                                // ChangeMapService.gI().changeMapBySpaceShip(player,
                                                // 120, -1, 250);
                                                // }
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player,
                                                        player.gender + 21, -1, 250);
                                                break;
                                            // if (player.zone.map.mapId == 120) {
                                            // ChangeMapService.gI().changeMapBySpaceShip(player,
                                            // player.gender + 21, -1, 250);
                                            // }
                                            // if (player.cFlag == 10) {
                                            // // if (player.getPowerPoint() >= 20) {
                                            // if (!(player.zone.map.mapId == 119)) {
                                            // int idMapNextFloor = player.zone.map.mapId == 115
                                            // ? player.zone.map.mapId + 2
                                            // : player.zone.map.mapId + 1;
                                            // ChangeMapService.gI().changeMap(player, idMapNextFloor, -1,
                                            // 354, 240);
                                            // } else {
                                            // Zone zone = MabuWar.gI().getMapLastFloor(120);
                                            // if (zone != null) {
                                            // ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                            // } else {
                                            // Service.getInstance().sendThongBao(player,
                                            // "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                            // ChangeMapService.gI().changeMapBySpaceShip(player,
                                            // player.gender + 21, -1, 250);
                                            // }
                                            // }
                                            // // player.resetPowerPoint();
                                            // player.sendMenuGotoNextFloorMabuWar = false;
                                            // Service.getInstance().sendPowerInfo(player, "TL",
                                            // player.getPowerPoint());
                                            // if (Util.isTrue(1, 30)) {
                                            // player.inventory.ruby += 1;
                                            // PlayerService.gI().sendInfoHpMpMoney(player);
                                            // Service.getInstance().sendThongBao(player,
                                            // "Bạn nhận được 1 Hồng Ngọc");
                                            // } else {
                                            // Service.getInstance().sendThongBao(player,
                                            // "Bạn đen vô cùng luôn nên không nhận được gì cả");
                                            // }
                                            // } else {
                                            // this.npcChat(player,
                                            // "Ngươi cần có đủ điểm để xuống tầng tiếp theo");
                                            // }
                                            // break;
                                            // } else {
                                            // this.npcChat(player,
                                            // "Ngươi đang theo phe Ôsin,Hãy qua bên đó mà thể hiện");
                                            // }
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.LINH_CANH:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                NpcMenu.gI().MenuDoanhTrai(player, this);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                NpcMethod.gI().DoanhTrai(player, this, select);
                            }
                        }
                    };
                    break;
                case ConstNpc.QUA_TRUNG:
                    npc = new QUA_TRUNG(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.QUOC_VUONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?", "Bản thân", "Đệ tử",
                                    "Từ chối");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                                this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                                        "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                                                + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                                        "Nâng\ngiới hạn\nsức mạnh",
                                                        "Nâng ngay\n"
                                                                + Util.numberToMoney(
                                                                        OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER)
                                                                + " vàng",
                                                        "Đóng");
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Sức mạnh của con đã đạt tới giới hạn", "Đóng");
                                            }
                                            break;
                                        case 1:
                                            if (player.pet != null) {
                                                if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                                    + Util.numberToMoney(
                                                                            player.pet.nPoint.getPowerNextLimit()),
                                                            "Nâng ngay\n" + Util.numberToMoney(
                                                                    OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER)
                                                                    + " vàng",
                                                            "Đóng");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                            "Sức mạnh của đệ con đã đạt tới giới hạn", "Đóng");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                            }
                                            // giới hạn đệ tử
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                                    switch (select) {
                                        case 0:
                                            OpenPowerService.gI().openPowerBasic(player);
                                            break;
                                        case 1:
                                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                                if (OpenPowerService.gI().openPowerSpeed(player)) {
                                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                                    Service.getInstance().sendMoney(player);
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn không đủ vàng để mở, còn thiếu " + Util.numberToMoney(
                                                                (OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                                        - player.inventory.gold))
                                                                + " vàng");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                                    if (select == 0) {
                                        if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                            if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                                player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                                Service.getInstance().sendMoney(player);
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Bạn không đủ vàng để mở, còn thiếu " + Util
                                                            .numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                                    - player.inventory.gold))
                                                            + " vàng");
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BUNMA_TL:
                    npc = new Bunma_TL(mapId, status, cx, cy, tempId, avartar) {
                    };
                    break;
                case ConstNpc.RONG_OMEGA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                // this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hiện tại không thể thực
                                // hiện !", "Đóng");
                                BlackBallWar.gI().setTime();
                                if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                                    try {
                                        long now = System.currentTimeMillis();
                                        if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW,
                                                    "Đường đến với ngọc rồng sao đen đã mở, "
                                                            + "ngươi có muốn tham gia không?",
                                                    "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                        } else {
                                            String[] optionRewards = new String[7];
                                            int index = 0;
                                            for (int i = 0; i < 7; i++) {
                                                if (player.rewardBlackBall.timeOutOfDateReward[i] > System
                                                        .currentTimeMillis()) {
                                                    optionRewards[index] = "Nhận thưởng\n" + (i + 1) + " sao";
                                                    index++;
                                                }
                                            }
                                            if (index != 0) {
                                                String[] options = new String[index + 1];
                                                for (int i = 0; i < index; i++) {
                                                    options[i] = optionRewards[i];
                                                }
                                                options[options.length - 1] = "Từ chối";
                                                this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW,
                                                        "Ngươi có một vài phần thưởng ngọc " + "rồng sao đen đây!",
                                                        options);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                                        "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                            }
                                        }
                                    } catch (Exception ex) {
                                        Log.error("Lỗi mở menu rồng Omega");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.MENU_REWARD_BDW:
                                        player.rewardBlackBall.getRewardSelect((byte) select);
                                        break;
                                    case ConstNpc.MENU_OPEN_BDW:
                                        if (select == 0) {
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "|7| Phần thường ngọc rồng sao đen hỗ trợ cho ngươi có các chỉ số :"
                                                            + "\n 1 sao: Tăng 10% sức đánh"
                                                            + "\n 2 sao: Tăng 15% hp "
                                                            + "\n 3 sao: Tăng 15% ki "
                                                            + "\n  4 sao: Tăng 35% TNSM"
                                                            + "\n  5 sao: Combo 10 đá nâng cấp "
                                                            + "\n 6 sao: Combo 10 sao pha lê "
                                                            + "\n  7 sao: Tặng 5% Sức đánh chí mạng cho toàn bang"
                                                            + "\n Chúc ngươi và các thành viên thành công trong việc giữ được Ngọc rồng sao đen ",
                                                    "Đóng");
                                        } else if (select == 1) {
                                            player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                            ChangeMapService.gI().openChangeMapTab(player);
                                        }
                                        break;
                                    case ConstNpc.MENU_NOT_OPEN_BDW:
                                        if (select == 0) {
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "|7| Phần thường ngọc rồng sao đen hỗ trợ cho ngươi có các chỉ số :"
                                                            + "\n 1 sao: Tăng 10% sức đánh"
                                                            + "\n 2 sao: Tăng 15% hp "
                                                            + "\n 3 sao: Tăng 15% ki "
                                                            + "\n  4 sao: Tăng 35% TNSM"
                                                            + "\n  5 sao: Combo 10 đá nâng cấp "
                                                            + "\n 6 sao: Combo 10 sao pha lê "
                                                            + "\n  7 sao: Tặng 5% Sức đánh chí mạng cho toàn bang"
                                                            + "\n Chúc ngươi và các thành viên thành công trong việc giữ được Ngọc rồng sao đen ",
                                                    "Đóng");
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (player.isHoldBlackBall) {
                                    this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?",
                                            "Phù hộ", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME,
                                            "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                                    if (select == 0) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                                "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                                "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                                "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                                "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                                "Từ chối");
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                                    if (select == 0) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                                    switch (select) {
                                        case 0:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                            break;
                                        case 1:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                            break;
                                        case 2:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                            break;
                                        case 3:
                                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.NPC_64:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (mapId == 0) {
                                    String data_top = Service.getInstance().get_top_player(player);
                                    String say_top = "";
                                    if (data_top != "") {
                                        say_top = "|2|Chúc mừng, con đã đạt được\n" + data_top
                                                + "Ta có các phần thưởng top dành cho con đây";
                                    }

                                    createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn xem thông tin gì?\n"
                                            + say_top,
                                            "Nhận quà\nTop",
                                            "Đóng");
                                } else if (mapId == 48) {
                                    // createOtherMenu(player, ConstNpc.BASE_MENU,
                                    // "Ta sẽ giúp con nâng cấp trang bị hủy diệt thành trang bị kích hoạt",
                                    // "Nâng cấp\nđệ tử", "Đóng");
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con muốn làm gì nào ?!", "Đóng");
                                } else {
                                    createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn xem thông tin gì?",
                                            "Đóng");
                                }

                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {

                                if (mapId == 0) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 99:
                                                // Service.getInstance().showTopPower(player);
                                                break;
                                            // case 0:
                                            // String getTxt = ChangeMapService.gI().getStringHoTroNv();
                                            // this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            // getTxt,
                                            // "đóng");
                                            // // Service.getInstance().showtopEvent(player);
                                            // break;
                                            case 0:
                                                String data_top = Service.getInstance().get_top_player(player);
                                                String say_top = "Nhận thưởng đua top sức mạnh\n";
                                                if (data_top != "") {
                                                    say_top = "|2|Chúc mừng, con đã đạt được\n" + data_top
                                                            + "Ta có các phần thưởng top dành cho con đây";
                                                }

                                                this.createOtherMenu(player, ConstNpc.MENU_NHAN_TOP,
                                                        say_top,
                                                        "Top \nsức mạnh", "smdt", "nv", "nap", "Top \nsự kiện",
                                                        "đóng");
                                                break;
                                            case 2:

                                                // NpcMethod.gI().QuaDHVT(player, this);
                                                // Service.getInstance().showTopRuongBau(player);
                                                break;
                                            case 3:
                                                // String getTxt = ChangeMapService.gI().getStringHoTroNv();
                                                // this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                // getTxt,
                                                // "đóng");
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NHAN_TOP) {
                                        switch (select) {
                                            case 0:
                                                NpcMethod.gI().Nhan_Top_Suc_Manh(player, this);
                                                // NpcMethod.gI().Nhan_Top_Suc_Manh(player, this);
                                                break;
                                            case 1:
                                                NpcMethod.gI().Nhan_Top_Suc_Manh_De_Tu(player, this);
                                                break;
                                            case 2:
                                                NpcMethod.gI().Nhan_Top_Nhiem_Vu(player, this);
                                                break;
                                            case 3:
                                                NpcMethod.gI().Nhan_Top_Nap(player, this);
                                                break;
                                            case 4:
                                                NpcMethod.gI().Nhan_Top_Suc_Manh_Tuan(player, this);
                                                break;

                                        }
                                    }
                                } else if (mapId == 48) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {

                                            case 0:
                                                NpcMethod.gI().menuNangCapDeTu(player, this);
                                                // CombineServiceNew.gI().openTabCombine(player,
                                                // CombineServiceNew.NANG_CAP_SKH_THUONG);
                                                break;
                                            case 1:
                                                // CombineServiceNew.gI().openTabCombine(player,
                                                // CombineServiceNew.NANG_CAP_SKH_VIP);
                                                break;
                                            case 2:
                                                // NpcMethod.gI().menuNangCapDeTu(player, this);
                                                break;
                                        }

                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        NpcMethod.gI().startCombine(player, select);
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DE_TU) {
                                        switch (select) {
                                            case 0:
                                                NpcMethod.gI().SetNangCapDeTu(player, this);
                                                break;
                                        }
                                    }
                                } else {
                                    if (select == 0) {
                                        // Service.getInstance().showTopPower(player);
                                    }
                                }

                            }
                        }
                    };
                    break;
                case ConstNpc.BILL:
                    npc = new Bill(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.WHIS:
                    npc = new WHIS(mapId, status, cx, cy, tempId, avartar);
                    break;
                case ConstNpc.TORIBOT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ngươi muốn gì?",
                                            "Nâng cấp\nphụ kiện",
                                            "Cửa Hàng\n Xu", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn gì nào",
                                            "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {

                                switch (this.mapId) {
                                    case 5:
                                        switch (player.iDMark.getIndexMenu()) {
                                            case ConstNpc.BASE_MENU:
                                                if (select == 10) {
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.CHE_TAO_TRANG_BI);
                                                } else if (select == 0) {
                                                    this.createOtherMenu(player,
                                                            ConstNpc.MENU_NANG_CAP_VA_THANH_TAY_PHU_KIEN,
                                                            "Dùng đá ngũ sắc để nâng cấp sẽ giúp cải trang và phụ kiện của ngươi trở nên mạnh mẽ hơn",
                                                            "Nâng cấp\nphụ kiện", "Thanh tẩy\nphụ kiện", "Hướng dẫn",
                                                            "Đóng");
                                                } else if (select == 11) {
                                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                            "Dùng 3 loại trang bị có chỉ số {Có thể chế tạo} và 1 bùa phù thủy, trang bị thu thập từ việc săn boss"
                                                                    + " (bao gồm cải trang, pet, vật phẩm đeo lưng,..) sẽ có tỉ lệ xuất hiện chỉ số này, vật phẩm nhận được sau khi chế tạo ngẫu nhiên gồm\n"
                                                                    + "|5| Trang bị (có cả thần linh, hủy diệt), cải trang, phụ kiện, item, ngọc rồng \n"
                                                                    + " thêm đá may mắn sẽ giúp ngươi tăng thêm tỉ lệ ra vật phẩm hiếm và tỉ lệ vĩnh viễn đấy!",
                                                            "Đóng");

                                                } else if (select == 1) {
                                                   ShopService.gI().openShopSpecial(player, this,
                                    ConstNpc.TORIBOT, 1, -1);
                                                }
                                                break;
                                            case ConstNpc.MENU_NANG_CAP_VA_THANH_TAY_PHU_KIEN: {
                                                if (select == 0) {
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.NANG_CAP_PHU_KIEN);
                                                } else if (select == 1) {
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.THANH_TAY_PHU_KIEN);
                                                } else if (select == 2) {
                                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                            "Ngươi có thể nâng cấp cải trang và giáp luyện tập bằng đá ngũ sắc\n"
                                                                    + "Sau khi nâng cấp lần đầu, trang bị sẽ nhận ngẫu nhiên chỉ số: \n"
                                                                    + "Sức đánh, hp, ki, %hp, %ki, xuyên giáp, tnsm, sức đánh chí mạng.v.v."
                                                                    + "Các cấp sau, trang bị sẽ tăng chỉ số đó thêm 5% mỗi cấp, "
                                                                    + "nếu không ưng ý, ngươi có thể thanh tẩy chỉ số ngũ sắc "
                                                                    + " bằng đá suy vong và cấp ngũ sắc sẽ về cấp 0",
                                                            "đóng");
                                                }

                                            }
                                                break;
                                            case ConstNpc.MENU_START_COMBINE: {

                                                switch (player.combineNew.typeCombine) {
                                                    case CombineServiceNew.CHE_TAO_TRANG_BI:
                                                    case CombineServiceNew.NANG_CAP_PHU_KIEN:
                                                    case CombineServiceNew.THANH_TAY_PHU_KIEN:
                                                        
                                                        if (select == 0) {
                                                            CombineServiceNew.gI().startCombine(player);
                                                        } else if (select == 1) {
                                                            CombineServiceNew.gI().startCombine_2(player);
                                                        }
                                                        break;

                                                }
                                            }
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                    
                case ConstNpc.BO_MONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Vàng ngọc nhận quà miễn phí liên hệ ta nhé!\n",
                                        "Đổi\n Mật khẩu", "Nhiệm vụ\nHàng ngày", "Shop Thẻ\nNV", "Đóng");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player) && this.mapId == 47 || this.mapId == 84) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            Input.gI().createFormChangePassword(player);
                                            break;
                                        case 1:
                                            NpcMethod.gI().LatThe(player, this, select);
                                            break;
                                        
                                        case 2:
                                            ShopService.gI().openShopSpecial(player, this,ConstNpc.SHOP_BO_MONG, 0, -1);
                                            break;
                                        case 4:
                                            break;    
                                    }
                                } else {
                                    NpcMethod.gI().LatThe(player, this, select);
                                }
                            }
                        }
                    };
                    break;
                    
                case ConstNpc.GOKU_SSJ:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 80) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart",
                                            "Từ chối");
                                } else if (this.mapId == 131) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        if (this.mapId == 80) {
                                            if (select == 0) {
                                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_21_0) {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 940);
                                                } else {
                                                    this.npcChat(player,
                                                            "Xin lỗi, tôi chưa thể đưa cậu tới nơi đó lúc này...giúp tôi tiêu diệt Fide đại ca trước");
                                                }

                                            }
                                        } else if (this.mapId == 131) {
                                            if (select == 0) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.GOKU_SSJ_:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                                    Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
                                    int soLuong = 0;
                                    if (biKiep != null) {
                                        soLuong = biKiep.quantity;
                                    }
                                    if (soLuong >= 10000) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + soLuong
                                                + " bí kiếp.\n"
                                                + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart",
                                                "Học dịch\nchuyển", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + soLuong
                                                + " bí kiếp.\n"
                                                + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart",
                                                "Đóng");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                                    Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
                                    int soLuong = 0;
                                    if (biKiep != null) {
                                        soLuong = biKiep.quantity;
                                    }
                                    if (soLuong >= 10000 && InventoryService.gI().getCountEmptyBag(player) > 0) {
                                        Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                        yardart.itemOptions.add(new ItemOption(47, 400));
                                        yardart.itemOptions.add(new ItemOption(108, 10));
                                        InventoryService.gI().addItemBag(player, yardart, 0);
                                        InventoryService.gI().subQuantityItemsBag(player, biKiep, 10000);
                                        InventoryService.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player,
                                                "Bạn vừa nhận được trang phục tộc Yardart");
                                    }
                                }
                            }
                        }
                    };
                    break;
                // case ConstNpc.GHI_DANH:
                // npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                // @Override
                // public void openBaseMenu(Player player) {
                // if (canOpenNpc(player)) {
                // this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "|2|Xin chào ! Bạn đang
                // làm gì ở đây vậy ?", "Đóng");
                //// String[] menuselect = new String[]{};
                //// if (this.map.mapId == 52) {
                //// if (DaiHoiManager.gI().openDHVT
                //// && (System.currentTimeMillis() <= DaiHoiManager.gI().tOpenDHVT)) {
                //// String nameDH = DaiHoiManager.gI().nameRoundDHVT();
                //// this.createOtherMenu(player, ConstNpc.MENU_DHVT,
                //// "Hiện đang có giải đấu " + nameDH
                //// + " bạn có muốn đăng ký không? \nSố người đã đăng ký :"
                //// + DaiHoiManager.gI().lstIDPlayers.size(),
                //// new String[]{
                //// "Giải\n" + nameDH + "\n(" + DaiHoiManager.gI().costRoundDHVT()
                //// + ")",
                //// "Từ chối", "Đại Hội\nVõ Thuật\nLần thứ\n23",
                //// "Giải siêu hạng"});
                //// } else {
                //// this.createOtherMenu(player, ConstNpc.BASE_MENU,
                //// "Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau",
                //// new String[]{"Thông tin\bChi tiết", "OK",
                //// "Đại Hội\nVõ Thuật\nLần thứ\n23", "Giải siêu hạng"});
                //// }
                //// } else if (this.mapId == 129) {
                //// int goldchallenge = player.goldChallenge;
                //// if (player.levelWoodChest == 0) {
                //// menuselect = new String[]{
                //// "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng",
                //// "Về\nĐại Hội\nVõ Thuật"};
                //// } else {
                //// menuselect = new String[]{
                //// "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng",
                //// "Nhận thưởng\nRương cấp\n" + player.levelWoodChest,
                //// "Về\nĐại Hội\nVõ Thuật"};
                //// }
                //// this.createOtherMenu(player, ConstNpc.BASE_MENU,
                //// "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày
                // lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào",
                //// menuselect, "Từ chối");
                ////
                //// } else {
                //// super.openBaseMenu(player);
                //// }
                //
                // }
                // }
                //
                // @Override
                // public void confirmMenu(Player player, int select) {
                // if (canOpenNpc(player)) {
                // if (this.map.mapId == 52) {
                // if (player.iDMark.isBaseMenu()) {
                // switch (select) {
                // case 0:
                // Service.getInstance().sendThongBaoFromAdmin(player,
                // "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,13,18h\bGiải Siêu cấp 1:
                // 9,14,19h\bGiải Siêu cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại
                // hạng: 12,17,22,23h\nGiải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2
                // ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8
                // ngọc\bGiải Ngoại hạng: 10.000 vàng\bVô địch: 5 viên đá nâng cấp\nVui lòng đến
                // đúng giờ để đăng ký thi đấu");
                // break;
                // case 1:
                // Service.getInstance().sendThongBaoFromAdmin(player,
                // "Nhớ Đến Đúng Giờ nhé");
                // break;
                // case 2:
                //// ChangeMapService.gI().changeMapNonSpaceship(player, 129,
                //// player.location.x, 360);
                // Service.getInstance().sendThongBao(player,
                // "Tính năng đang cập nhật, vui lòng quay lại sau");
                // break;
                // case 3:
                // Service.getInstance().sendThongBao(player,
                // "Tính năng đang cập nhật, vui lòng quay lại sau");
                // // ChangeMapService.gI().changeMapNonSpaceship(player, 113,
                // // player.location.x, 360);
                // break;
                // }
                // } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DHVT) {
                // switch (select) {
                // case 0:
                // // if (DaiHoiService.gI().canRegisDHVT(player.nPoint.power)) {
                // if (DaiHoiManager.gI().lstIDPlayers.size() < 256) {
                // if (DaiHoiManager.gI().typeDHVT == (byte) 5
                // && player.inventory.gold >= 10000) {
                // if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                // Service.getInstance().sendThongBao(player,
                // "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                // } else {
                // player.inventory.gold -= 10000;
                // Service.getInstance().sendMoney(player);
                // Service.getInstance().sendThongBao(player,
                // "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                // DaiHoiManager.gI().lstIDPlayers.add(player.id);
                // }
                // } else if (DaiHoiManager.gI().typeDHVT > (byte) 0
                // && DaiHoiManager.gI().typeDHVT < (byte) 5
                // && player.inventory.gem >= (int) (2
                // * DaiHoiManager.gI().typeDHVT)) {
                // if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                // Service.getInstance().sendThongBao(player,
                // "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                // } else {
                // player.inventory.gem -= (int) (2
                // * DaiHoiManager.gI().typeDHVT);
                // Service.getInstance().sendMoney(player);
                // Service.getInstance().sendThongBao(player,
                // "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                // DaiHoiManager.gI().lstIDPlayers.add(player.id);
                // }
                // } else {
                // Service.getInstance().sendThongBao(player,
                // "Không đủ vàng ngọc để đăng ký thi đấu");
                // }
                // } else {
                // Service.getInstance().sendThongBao(player,
                // "Hiện tại đã đạt tới số lượng người đăng ký tối đa, xin hãy chờ đến giải
                // sau");
                // }
                //
                // // } else {
                // // Service.getInstance().sendThongBao(player, "Bạn không đủ điều kiện
                // // tham gia giải này, hãy quay lại vào giải phù hợp");
                // // }
                // break;
                // case 1:
                // break;
                // case 2:
                //// ChangeMapService.gI().changeMapNonSpaceship(player, 129,
                //// player.location.x, 360);
                // Service.getInstance().sendThongBao(player,
                // "Tính năng đang cập nhật, vui lòng quay lại sau");
                // break;
                // case 3:
                // Service.getInstance().sendThongBao(player,
                // "Tính năng đang cập nhật, vui lòng quay lại sau");
                // break;
                // }
                // }
                // } else if (this.mapId == 129) {
                // int goldchallenge = player.goldChallenge;
                // if (player.levelWoodChest == 0) {
                // switch (select) {
                // case 0:
                // if (InventoryService.gI().finditemWoodChest(player)) {
                // if (player.inventory.gold >= goldchallenge) {
                // MartialCongressService.gI().startChallenge(player);
                // player.inventory.gold -= (goldchallenge);
                // PlayerService.gI().sendInfoHpMpMoney(player);
                // player.goldChallenge += 250_000_000;
                // } else {
                // Service.getInstance().sendThongBao(player,
                // "Không đủ vàng, còn thiếu "
                // + Util.numberToMoney(
                // goldchallenge - player.inventory.gold)
                // + " vàng");
                // }
                // } else {
                // Service.getInstance().sendThongBao(player,
                // "Hãy mở rương báu vật trước");
                // }
                // break;
                // case 1:
                // ChangeMapService.gI().changeMapNonSpaceship(player, 52,
                // player.location.x, 336);
                // break;
                // }
                // } else {
                // switch (select) {
                // case 0:
                // if (InventoryService.gI().finditemWoodChest(player)) {
                // if (player.inventory.gold >= goldchallenge) {
                // MartialCongressService.gI().startChallenge(player);
                // player.inventory.gold -= (goldchallenge);
                // PlayerService.gI().sendInfoHpMpMoney(player);
                // player.goldChallenge += 250_000_000;
                // } else {
                // Service.getInstance().sendThongBao(player,
                // "Không đủ vàng, còn thiếu "
                // + Util.numberToMoney(
                // goldchallenge - player.inventory.gold)
                // + " vàng");
                // }
                // } else {
                // Service.getInstance().sendThongBao(player,
                // "Hãy mở rương báu vật trước");
                // }
                // break;
                // case 1:
                // if (!player.receivedWoodChest) {
                // if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                // Item it = ItemService.gI().createNewItem((short) 570);
                // it.itemOptions
                // .add(new ItemOption(72, player.levelWoodChest));
                // it.itemOptions.add(new ItemOption(30, 0));
                // it.createTime = System.currentTimeMillis();
                // InventoryService.gI().addItemBag(player, it);
                // InventoryService.gI().sendItemBags(player);
                //
                // player.receivedWoodChest = true;
                // player.levelWoodChest = 0;
                // Service.getInstance().sendThongBao(player,
                // "Bạn nhận được rương gỗ");
                // } else {
                // this.npcChat(player, "Hành trang đã đầy");
                // }
                // } else {
                // Service.getInstance().sendThongBao(player,
                // "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                // }
                // break;
                // case 2:
                // ChangeMapService.gI().changeMapNonSpaceship(player, 52,
                // player.location.x, 336);
                // break;
                // }
                // }
                // }
                // }
                // }
                // };
                // break;
                case ConstNpc.NOI_BANH:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Xin chào " + player.name + "\nTôi là nồi nấu bánh\nTôi có thể giúp gì cho bạn",
                                        "Làm\nBánh Tét", "Làm\nBánh Chưng", "Nấu bánh", "Đổi Hộp\nQuà Tết",
                                        "Hướng dẫn\nLàm bánh");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0: {
                                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                    Item bachi = InventoryService.gI().findItemBagByTemp(player,
                                                            ConstItem.THIT_BA_CHI);
                                                    Item gaonep = InventoryService.gI().findItemBagByTemp(player,
                                                            ConstItem.GAO_NEP);
                                                    Item Do_xanh = InventoryService.gI().findItemBagByTemp(player,
                                                            ConstItem.DO_XANH);
                                                    Item laChuoi = InventoryService.gI().findItemBag(player,
                                                            ConstItem.LA_CHUOI);
                                                    if (bachi != null && bachi.quantity >= 99
                                                            && gaonep != null && gaonep.quantity >= 99
                                                            && Do_xanh != null && Do_xanh.quantity >= 99
                                                            && laChuoi != null && laChuoi.quantity >= 20) {

                                                        InventoryService.gI().subQuantityItemsBag(player, bachi, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, gaonep, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, Do_xanh, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, laChuoi, 20);
                                                        Item banhtet = ItemService.gI()
                                                                .createNewItem((short) ConstItem.BANH_TET_2023);
                                                        banhtet.itemOptions.add(new ItemOption(74, 0));
                                                        InventoryService.gI().addItemBag(player, banhtet, 0);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bạn nhận được Bánh Tét");
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ nguyên để nấu bánh Tét");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Hành trang cần 1 ô trống");
                                                }
                                            }
                                                break;
                                            case 1: {
                                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                    Item ThitHeo = InventoryService.gI().findItemBagByTemp(player,
                                                            ConstItem.THIT_HEO_2023);
                                                    Item gaonep = InventoryService.gI().findItemBagByTemp(player,
                                                            ConstItem.GAO_NEP);
                                                    Item Do_xanh = InventoryService.gI().findItemBagByTemp(player,
                                                            ConstItem.DO_XANH);
                                                    Item Ladong = InventoryService.gI().findItemBagByTemp(player,
                                                            ConstItem.LA_DONG_2023);
                                                    if (ThitHeo != null && ThitHeo.quantity >= 99
                                                            && gaonep != null && gaonep.quantity >= 99
                                                            && Do_xanh != null && Do_xanh.quantity >= 99
                                                            && Ladong != null && Ladong.quantity >= 20) {
                                                        InventoryService.gI().subQuantityItemsBag(player, ThitHeo, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, gaonep, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, Do_xanh, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, Ladong, 20);

                                                        Item banhChung = ItemService.gI()
                                                                .createNewItem((short) 2035);
                                                        banhChung.itemOptions.add(new ItemOption(74, 0));
                                                        InventoryService.gI().addItemBag(player, banhChung, 0);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bạn nhận được Bánh Chưng");

                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ nguyên liệu để nấu bánh !");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Hành trang cần 1 ô trống");
                                                }
                                            }
                                                break;
                                            case 2: {
                                                this.createOtherMenu(player, ConstNpc.MENU_NAU_BANH_NHANH,
                                                        "|2|Tôi có thể giúp bạn nấu bánh trong vòng 5 phút, để không phải chờ, "
                                                                + "tôi sẽ dùng lửa siêu thần, nấu bánh nhanh với chi phí là 500 triệu vàng mỗi bánh",
                                                        getMenuLamBanh(player, 0), getMenuLamBanh(player, 1),
                                                        "Nấu bánh\ntét\nnhanh", "Nấu bánh\nchưng\nnhanh",
                                                        "đóng");
                                            }
                                                break;
                                            case 3:
                                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                    Item tetCaKe = InventoryService.gI().findItemBag(player, 2033);
                                                    Item ChungCake = InventoryService.gI().findItemBagByTemp(player,
                                                            2036);
                                                    if (tetCaKe != null && tetCaKe.quantity >= 5
                                                            && ChungCake != null && ChungCake.quantity >= 5) {
                                                        Item hopQua = ItemService.gI()
                                                                .createNewItem((short) ConstItem.HOP_QUA_TET_2023, 1);
                                                        hopQua.itemOptions
                                                                .add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                                                        InventoryService.gI().subQuantityItemsBag(player, tetCaKe, 5);
                                                        InventoryService.gI().subQuantityItemsBag(player, ChungCake, 5);
                                                        InventoryService.gI().addItemBag(player, hopQua, 0);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bạn nhận được Hộp quà tết");
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ nguyên liệu để đổi");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
                                                }
                                                break;
                                            case 4:
                                                Service.getInstance().sendThongBaoOK(player,
                                                        "Nguyên liệu:\n"
                                                                + "Bánh tét: x99 thịt ba chỉ + x99 đậu xanh + x99 gạo nếp + x20 lá chuối\n"
                                                                + "Bánh chưng: x99 thịt ba chỉ + x99 đậu xanh + x99 gạo nếp + x20 lá dong\n"
                                                                + "Nấu chín bánh: x1 bánh + x1 gia vị tổng hợp + x1 phụ gia tạo màu\n"
                                                                + "Đổi quà Tết: x5 bánh tét + x5 bánh chưng\n");
                                                break;
                                        }
                                        break;
                                    case ConstNpc.MENU_NAU_BANH_NHANH:
                                        switch (select) {

                                            case 0:
                                                if (!player.event.isCookingTetCake()) {
                                                    Item banhTet2 = InventoryService.gI().findItem(player,
                                                            ConstItem.BANH_TET_2023, 1);
                                                    Item phuGiaTaoMau2 = InventoryService.gI().findItem(player,
                                                            ConstItem.PHU_GIA_TAO_MAU, 1);
                                                    Item giaVi2 = InventoryService.gI().findItem(player,
                                                            ConstItem.GIA_VI_TONG_HOP, 1);
                                                    if (banhTet2 != null && phuGiaTaoMau2 != null && giaVi2 != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, banhTet2, 1);
                                                        InventoryService.gI().subQuantityItemsBag(player, phuGiaTaoMau2,
                                                                1);
                                                        InventoryService.gI().subQuantityItemsBag(player, giaVi2, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                        player.event.setTimeCookTetCake(300);
                                                        player.event.setCookingTetCake(true);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bắt đầu nấu bánh,thời gian nấu bánh là 5 phút");
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ nguyên liệu");
                                                    }
                                                } else if (player.event.isCookingTetCake()
                                                        && player.event.getTimeCookTetCake() == 0) {
                                                    Item cake = ItemService.gI()
                                                            .createNewItem((short) ConstItem.BANH_TET_CHIN, 1);
                                                    cake.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                                                    cake.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));
                                                    InventoryService.gI().addItemBag(player, cake, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.event.setCookingTetCake(false);
                                                    player.event.addEventPoint(1);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Bánh Tét (đã chính) và 1 điểm sự kiện");
                                                }
                                                break;
                                            case 1:
                                                if (!player.event.isCookingChungCake()) {
                                                    Item banhChung3 = InventoryService.gI().findItem(player,
                                                            ConstItem.BANH_CHUNG_2023, 1);
                                                    Item phuGiaTaoMau3 = InventoryService.gI().findItem(player,
                                                            ConstItem.PHU_GIA_TAO_MAU, 1);
                                                    Item giaVi3 = InventoryService.gI().findItem(player,
                                                            ConstItem.GIA_VI_TONG_HOP, 1);
                                                    if (banhChung3 != null && phuGiaTaoMau3 != null && giaVi3 != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, banhChung3,
                                                                1);
                                                        InventoryService.gI().subQuantityItemsBag(player, phuGiaTaoMau3,
                                                                1);
                                                        InventoryService.gI().subQuantityItemsBag(player, giaVi3, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                        player.event.setTimeCookChungCake(300);
                                                        player.event.setCookingChungCake(true);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bắt đầu nấu bánh,thời gian nấu bánh là 5 phút");
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ nguyên liệu");
                                                    }
                                                } else if (player.event.isCookingChungCake()
                                                        && player.event.getTimeCookChungCake() == 0) {
                                                    Item cake = ItemService.gI()
                                                            .createNewItem((short) ConstItem.BANH_CHUNG_CHIN, 1);
                                                    cake.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                                                    cake.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));
                                                    InventoryService.gI().addItemBag(player, cake, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.event.setCookingChungCake(false);
                                                    player.event.addEventPoint(1);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Bánh Chưng (đã chín) và 1 điểm sự kiện");
                                                }
                                                break;
                                            case 2:
                                                Input.gI().createFormNauBanhTet(player);
                                                break;
                                            case 3:
                                                Input.gI().createFormNauBanhChung(player);
                                                break;
                                        }
                                        break;

                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CUA_HANG_KY_GUI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Của hàng chúng tôi chuyên bán hàng hiệu,hàng độc,nếu bạn không chê thì mại đzô",
                                        "Hướng dẫn\n");
                                // this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                // "Hiện tại chưa hoạt động !",
                                // "OK");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                Service.getInstance().sendThongBaoOK(player,
                                                        "Bạn có thể ký gửi vật phẩm của mình tại đây với giá 1 xu mỗi lượt ký."
                                                                + " Sau khi bán thành công, bạn sẽ nhận lại 95% số xu bán được."
                                                                + " Vật phẩm sẽ hoàn trả sau 2 ngày ký gửi");
                                                break;
                                            case 1:
                                                // if (!Manager.gI().getGameConfig().isOpenSuperMarket()) {
                                                // Service.getInstance().sendThongBao(player, "Chức năng kí gửi chưa
                                                // mở,vui lòng quay lại sau");
                                                // return;
                                                // }
                                                if (player.nPoint.power < 20000000000L) {
                                                    Service.getInstance().sendThongBaoOK(player,
                                                            "Yêu cầu trên 20 tỷ");
                                                    return;
                                                }
                                                ConsignmentShop.getInstance().show(player);
                                                // KyGuiService.gI().openShopKyGui(player);
                                                break;
                                            case 2:
                                                ConsignmentShop.getInstance().showExpiringItems(player);
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                default:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                // player.gender);
                            }
                        }
                    };
            }
        } catch (Exception e) {
            Log.error(NpcFactory.class, e, "Lỗi load npc");
        }
        return npc;
    }

    // girlkun75-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1
                                && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2
                                && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    case ConstNpc.BLACK_SHENRON:
                        if (player.iDMark.getIndexMenu() == ConstNpc.BLACK_SHENRON
                                && select == BLACK_SHENRON_WISHES.length) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.BLACK_SHENRON, BLACK_SHENRON_SAY,
                                    BLACK_SHENRON_WISHES);
                            break;
                        }
                    case ConstNpc.ICE_SHENRON:
                        if (player.iDMark.getIndexMenu() == ConstNpc.ICE_SHENRON
                                && select == ICE_SHENRON_WISHES.length) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.ICE_SHENRON, ICE_SHENRON_SAY,
                                    ICE_SHENRON_WISHES);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.MENU_CHON_CAI_TRANG:
                        UseItem.gI().AcceptChonCaitrang1493(player, select);
                        break;
                    case ConstNpc.MENU_CHON_PET_NHI:
                        UseItem.gI().AcceptChonPetgnhi(player, select);
                        break;
                    case ConstNpc.MENU_CHON_PET_NHI_THUONG:
                        UseItem.gI().AcceptChonPetgnhiThuongh(player, select);
                        break;
                    case ConstNpc.MENU_ACP_MO_TRUNG_BU:
                        if (select == 0) {
                            if (InventoryService.gI().existItemBag(player, 1477)) {
                                InventoryService.gI().subQuantityItemsBag(player, (short) 1477, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().creatTrungTrungPet(player, (byte) 0);
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Đã xảy ra lỗi trong quá trình xử lý, vui lòng liên hệ lại ADMIN !");
                            }
                        }
                        break;
                    case ConstNpc.MENU_ACP_MO_UUB:
                        if (select == 0) {
                            if (InventoryService.gI().existItemBag(player, 1552)) {
                                InventoryService.gI().subQuantityItemsBag(player, (short) 1552, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().createGoku1Pet(player, (byte) 0);
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Đã xảy ra lỗi trong quá trình xử lý, vui lòng liên hệ lại ADMIN !");
                            }
                        }
                        break;
                    case ConstNpc.CONFIRM_DIALOG:
                        ConfirmDialog confirmDialog = player.getConfirmDialog();
                        if (confirmDialog != null) {
                            if (confirmDialog instanceof MenuDialog menu) {
                                menu.getRunable().setIndexSelected(select);
                                menu.run();
                                return;
                            }
                            if (select == 0) {
                                confirmDialog.run();
                            } else {
                                confirmDialog.cancel();
                            }
                            player.setConfirmDialog(null);
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:

                        break;
                    case ConstNpc.RUONG_GO:
                        int size = player.textRuongGo.size();
                        if (size > 0) {
                            String menuselect = "OK [" + (size - 1) + "]";
                            if (size == 1) {
                                menuselect = "OK";
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.RUONG_GO, -1,
                                    player.textRuongGo.get(size - 1), menuselect);
                            player.textRuongGo.remove(size - 1);
                        }
                        break;
                    case ConstNpc.MENU_MABU_WAR:
                        if (select == 0) {
                            if (player.zone.finishMabuWar) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                            } else if (player.zone.map.mapId == 119) {
                                Zone zone = MabuWar.gI().getMapLastFloor(120);
                                if (zone != null) {
                                    ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                }
                            } else {
                                int idMapNextFloor = player.zone.map.mapId == 115 ? player.zone.map.mapId + 2
                                        : player.zone.map.mapId + 1;
                                ChangeMapService.gI().changeMap(player, idMapNextFloor, -1, 354, 240);
                            }
                            player.resetPowerPoint();
                            player.sendMenuGotoNextFloorMabuWar = false;
                            Service.getInstance().sendPowerInfo(player, "TL", player.getPowerPoint());
                            if (Util.isTrue(1, 30)) {
                                player.inventory.ruby += 1;
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 1 Hồng Ngọc");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Bạn đen vô cùng luôn nên không nhận được gì cả");
                            }
                        }
                        break;
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP:
                        // PVP_old.gI().sendInvitePVP(player, (byte) select);
                        PVPServcice.gI().sendInvitePVP(player, (byte) select);
                        break;
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPServcice.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.SUMMON_BLACK_SHENRON:
                        if (select == 0) {
                            SummonDragon.gI().summonBlackShenron(player);
                        }
                        break;
                    case ConstNpc.SUMMON_ICE_SHENRON:
                        if (select == 0) {
                            SummonDragon.gI().summonIceShenron(player);
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.MENU_CHAT_GLOBAL_VIP:
                        if (select == 0) {
                            ChatGlobalService.gI().chatVip(player, player.chatVip);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (!player.isAdmin()) {
                            return;
                        }
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.getInstance().sendThongBao(player,
                                    "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;
                    case ConstNpc.BUFF_PET:
                        if (!player.isAdmin()) {
                            return;
                        }
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.getInstance().sendThongBao(player, "Phát đệ tử cho "
                                        + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.MENU_ADMIN:
                        if (!player.isAdmin()) {
                            return;
                        }
                        switch (select) {
                            case 0:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i, 10);
                                    InventoryService.gI().addItemBag(player, item, 0);
                                }
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được bộ 1 sao");
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.getInstance().sendThongBao(player,
                                            "Tạo pet thành công");
                                } else {
                                    if (player.pet.isMabu) {
                                        PetService.gI().changeNormalPet(player);
                                    } else {
                                        PetService.gI().changeMabuPet(player);
                                    }
                                    Service.getInstance().sendThongBao(player,
                                            "Đã đổi pet thành Mabu");
                                }
                                break;
                            case 2:
                                // PlayerService.gI().baoTri();
                                Maintenance.gI().start(180);
                                Service.getInstance().sendThongBao(player,
                                        "Bảo trì sau 180 giây");
                                break;
                            case 3:
                                Maintenance.gI().start(5);
                                Service.getInstance().sendThongBao(player,
                                        "Bảo trì sau 5 giây");
                                break;
                            case 4:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 5:
                                NotiManager.getInstance().load();
                                NotiManager.getInstance().sendAlert(player);
                                NotiManager.getInstance().sendNoti(player);
                                Service.getInstance().chat(player, "Cập nhật thông báo thành công");
                                break;
                            case 6:
                                this.createOtherMenu(player, ConstNpc.CALL_BOSS_DIALOG, "Call Boss Menu",
                                        "Cumber", "BulMa Hồng", "Bulma Đen", "Bulma Đỏ",
                                        "Black Goku", "Chill", "Whis", "Cooler",
                                        "Xên Bọ Hung", "Kuku", "Mập đầu đinh", "Rambo",
                                        " Tiểu đội trưởng", "Fide đại ca", "Android 20",
                                        " King Kong", "Xên Bọ Hung 1", "Xên Max",
                                        "Santa Claus", "Zamasu Max", "Super Broly", "Uub");
                                break;
                        }
                        break;
                    case ConstNpc.MENU_ADMIN_2:
                        if (!player.isAdmin()) {
                            return;
                        }
                        switch (select) {
                            case 0: {// x99 hộp thần linh
                                Item item = ItemService.gI().createNewItem((short) 1426, 99);
                                InventoryService.gI().addItemBag(player, item, 0);

                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item.template.name);
                            }

                                break;
                            case 1: {// x99 hộp hủy diệt
                                Item item = ItemService.gI().createNewItem((short) 1384, 99);
                                InventoryService.gI().addItemBag(player, item, 0);

                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item.template.name);
                            }

                                break;
                            case 2: {// tăng 50 tỷ sm
                                if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                                    Service.getInstance().sendThongBao(player, "Vui lòng mở giới hạn sức mạnh");
                                    return;
                                }

                                if (player.nPoint.power + 10_000_000_000l < 500_000_000_000l) {
                                    Service.getInstance().addSMTN(player, (byte) 0, 50_000_000_000l, false);
                                    Service.getInstance().addSMTN(player, (byte) 1, 50_000_000_000l, false);
                                    Service.getInstance().sendThongBao(player, "Buff sức mạnh thành công");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Sức mạnh đã tối đa");
                                }
                                Service.getInstance().point(player);
                            }

                                break;
                            case 3: {// cộng full skill

                                Message msg;
                                try {
                                    for (Skill skill : player.playerSkill.skills) {
                                        if (skill.point == 0) {
                                            skill = SkillUtil.createSkill(
                                                    skill.template.id,
                                                    7);
                                            SkillUtil.setSkill(player, skill);

                                            msg = Service.getInstance().messageSubCommand((byte) 23);
                                            msg.writer().writeShort(skill.skillId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } else {
                                            skill = SkillUtil.createSkill(
                                                    skill.template.id,
                                                    7);
                                            // System.out.println(curSkill.template.name + " - " +
                                            // curSkill.point);
                                            SkillUtil.setSkill(player, skill);
                                            msg = Service.getInstance().messageSubCommand((byte) 62);
                                            msg.writer().writeShort(skill.skillId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        }
                                    }
                                    Service.getInstance().sendThongBao(player,
                                            "Đã cộng full skill");
                                } catch (Exception e) {
                                    Log.error(UseItem.class, e);
                                }

                            }
                                break;
                            case 4: {// cộng chỉ số gốc
                                if (player.nPoint.hpg <= 1_000_000_000) {
                                    player.nPoint.hpg += 10_000_000;
                                }
                                if (player.nPoint.mpg <= 1_000_000_000) {
                                    player.nPoint.mpg += 10_000_000;
                                }
                                if (player.nPoint.dameg <= 100_000_000) {
                                    player.nPoint.dameg += 5_000_000;
                                }
                                Service.getInstance().sendThongBao(player, "Cộng chỉ số thành công");
                                Service.getInstance().point(player);
                            }
                                break;
                            case 5: {// Nhận dệ tử
                                RewardService.gI().NhanDeTu(player);
                            }
                                break;

                        }
                        break;
                    case ConstNpc.MENU_ADMIN_3:
                        if (!player.isAdmin()) {
                            return;
                        }
                        switch (select) {
                            case 0: {// reset boss chưa hoàn thiện
                                Manager.gI().ReloadShop();
                            }
                                break;
                            case 1: {
                                Manager.gI().ReloadRatio();
                                Service.getInstance().sendThongBao(player, "Cập nhật shop thành công");
                            }
                                break;
                            case 2: {
                                Manager.gI().ReloadBoss();
                                BossFactory.initBoss();
                                Service.getInstance().sendThongBao(player,
                                        "Reset boss thành công, tất cả boss đã được tái tạo lại");
                            }
                                break;
                            case 3: {
                                Input.gI().createFormBuffItem(player);
                            }
                                break;
                        }
                        break;
                    case ConstNpc.MENU_ADMIN_TEST:
                        if (!player.isAdmin()) {
                            return;
                        }
                        switch (select) {
                            case 0: {// NHẬN ITEM SHOP
                                try {
                                    RequestService.gI().RegisterCMD(player, RequestService.GIVE_ITEM_SHOP_WEB);
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                                break;

                        }
                        break;
                    case ConstNpc.CALL_BOSS_DIALOG:
                        if (!player.isAdmin()) {
                            return;
                        }
                        switch (select) {
                            case 0:
                                BossFactory.createBoss(BossFactory.CUMBER);
                                break;
                            case 1:
                                BossFactory.createBoss(BossFactory.BULMA);
                                break;
                            case 2:
                                BossFactory.createBoss(BossFactory.POCTHO);
                                break;
                            case 3:
                                BossFactory.createBoss(BossFactory.CHICHITHO);
                                break;
                            case 4:
                                BossFactory.createBoss(BossFactory.BLACKGOKU);
                                break;
                            case 5:
                                BossFactory.createBoss(BossFactory.CHILL);
                                break;
                            case 6:
                                BossFactory.createBoss(BossFactory.WHIS);
                                break;
                            case 7:
                                BossFactory.createBoss(BossFactory.COOLER);
                                break;
                            case 8:
                                BossFactory.createBoss(BossFactory.XEN_BO_HUNG);
                                break;
                            case 9:
                                BossFactory.createBoss(BossFactory.KUKU);
                                break;
                            case 10:
                                BossFactory.createBoss(BossFactory.MAP_DAU_DINH);
                                break;
                            case 11:
                                BossFactory.createBoss(BossFactory.RAMBO);
                                break;
                            case 12:
                                BossFactory.createBoss(BossFactory.TIEU_DOI_TRUONG);
                                break;
                            case 13:
                                BossFactory.createBoss(BossFactory.FIDE_DAI_CA_1);
                                break;
                            case 14:
                                BossFactory.createBoss(BossFactory.ANDROID_20);
                                break;
                            case 15:
                                BossFactory.createBoss(BossFactory.KINGKONG);
                                break;
                            case 16:
                                BossFactory.createBoss(BossFactory.XEN_BO_HUNG_1);
                                break;
                            case 17:
                                BossFactory.createBoss(BossFactory.XEN_MAX);
                                break;
                            case 18:
                                BossFactory.createBoss(BossFactory.SANTA_CLAUS);
                                break;
                            case 19:
                                BossFactory.createBoss(BossFactory.S_ZMAS);
                                break;
                            case 20:
                                BossFactory.createBoss(BossFactory.SUPER_BROLY);
                                break;
                            case 21:
                                BossFactory.createBoss(BossFactory.UUB);
                                break;
                        }
                        break;

                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            Service.getInstance().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        if (!player.isAdmin()) {
                            return;
                        }
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x,
                                                p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x,
                                                player.location.y);
                                    }
                                    break;
                                case 2:
                                    if (p != null) {
                                        Input.gI().createFormChangeName(player, p);
                                    }
                                    break;
                                case 3:
                                    if (p != null) {
                                        String[] selects = new String[] { "Đồng ý", "Hủy" };
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                                "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    }
                                    break;
                            }
                        }
                        break;
                    case 1353:
                        NpcMethod.gI().chonDoHuyDiet(player, (short) player.iDMark.getIndexMenu(), select);
                        break;
                    case 1388:// 5sao
                        NpcMethod.gI().ruong5Sao(player, (short) player.iDMark.getIndexMenu(), select);
                        break;
                    case 1389: // 6 sao
                        NpcMethod.gI().ruong6Sao(player, (short) player.iDMark.getIndexMenu(), select);
                        break;
                    case 1393:// thần linh
                        NpcMethod.gI().ruongThanLinh(player, (short) player.iDMark.getIndexMenu(), select);
                        break;
                    case 1406:
                        NpcMethod.gI().OpenSKHKaio(player, select);
                        break;
                    case 1426:
                        NpcMethod.gI().RuongThanLinhGender(player, select);
                        break;
                    case 1460:
                        NpcMethod.gI().RuongThanLinhGender1(player, select);
                        break;  
                    case 1347:
    NpcMethod.gI().changePetPlanet(player, select);
    break;

                    case 1033:
                    case 1034:
                    case 1035:
                        NpcMethod.gI().RuongThanLinhGenderAndType(player, player.iDMark.getIndexMenu(), select);
                        break;
                    case 702:
                    case 703:
                    case 704:
                    case 705:
                    case 706:
                    case 707:
                    case 708:
                        NpcMethod.gI().WhisRongXuong(player, select);
                        break;

                }
            }
        };
    }

    public static String getMenuLamBanh(Player player, int type) {
        switch (type) {
            case 0:// bánh tét
                if (player.event.isCookingTetCake()) {
                    int timeCookTetCake = player.event.getTimeCookTetCake();
                    if (timeCookTetCake == 0) {
                        return "Lấy bánh";
                    } else if (timeCookTetCake > 0) {
                        return "Đang nấu\nBánh Tét\nCòn " + TimeUtil.secToTime(timeCookTetCake);
                    }
                } else {
                    return "Nấu\nBánh Tét";
                }
                break;
            case 1:// bánh chưng
                if (player.event.isCookingChungCake()) {
                    int timeCookChungCake = player.event.getTimeCookChungCake();
                    if (timeCookChungCake == 0) {
                        return "Lấy bánh";
                    } else if (timeCookChungCake > 0) {
                        return "Đang nấu\nBánh Chưng\nCòn " + TimeUtil.secToTime(timeCookChungCake);
                    }
                } else {
                    return "Nấu\nBánh Chưng";
                }
                break;
        }
        return "";
    }

}
