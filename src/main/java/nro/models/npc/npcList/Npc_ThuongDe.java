package nro.models.npc.npcList;

import java.util.List;
import nro.attr.Attribute;
import nro.consts.ConstAttribute;
import nro.consts.ConstMap;
import nro.consts.ConstNpc;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.dungeon.SnakeRoad;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.server.Manager;
import static nro.server.Manager.EVENT_COUNT_THUONG_DE;
import static nro.server.Manager.EVENT_SEVER;
import nro.server.ServerManager;
import nro.services.InventoryService;
import nro.services.Service;
import nro.services.Event.EventService;
import nro.services.ItemService;
import nro.services.NpcService;
import nro.services.func.ChangeMapService;
import nro.services.func.Input;
import nro.services.func.LuckyRoundService;
import nro.services.func.ShopService;
import nro.services.func.lr.LuckyRoundGold;
import nro.utils.Util;

public class Npc_ThuongDe extends Npc {

    public Npc_ThuongDe(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (player.zone instanceof ZSnakeRoad) {
            if (mapId == ConstMap.CON_DUONG_RAN_DOC) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy lắm lấy tay ta mau",
                        "Về thần điện");
            }
        } else {
            if (canOpenNpc(player)) {
                switch (mapId) {
                    case 5:
                    case 7:
                    case 14:
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con muốn làm gì nào",
                                "Vòng quay", "Rương phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                - InventoryService.gI().getCountEmptyListItem(
                                        player.inventory.itemsBoxCrackBall))
                                + " món)", "Xóa hết\n trong rương", "Top");
                        break;
                    case 45:
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con muốn làm gì nào",
                                "Đến Kaio");
                        break;
                    default:
                        break;
                }
            }
        }

    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (mapId == 5 || mapId == 7 || mapId == 14) {

                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            this.createOtherMenu(player, 1, "Con muốn thực hiện quay sao ?"
                                    + "\n|7|Hiện tại ta chỉ lấy của con 5b vàng /lượt thôi nhé !",
                                    "1\nLần", "100\nLần", "Đóng");
                            break;
                        case 1:
                            ShopService.gI().openBoxItemLuckyRound(player);
                            break;
                        case 2:
                            NpcService.gI().createMenuConMeo(player,
                                    ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                    "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                    + "sẽ không thể khôi phục!",
                                    "Đồng ý", "Hủy bỏ");
                            break;
                       case 3:
                           Service.getInstance().showTopVongQuay(player);
                           break;
                       case 4:
                           this.createOtherMenu(player, 2, "|2|Chào con!\n"
                                   + "|6|Con đã thực hiện được " + player.GapthuPoint + " lượt rồi đấy\n"
                                   + "Con có may mắn nhận được gì không?\n"
                                   + "Nếu may mắn chưa đến với con\nthì ta có thể cho con những phần quà hỗ trợ nữa đấy !",
                                   "100 Điểm", "200 Điểm", "500 Điểm", "1000 Điểm",
                                   "2500 Điểm", "5000 Điểm", "Đóng");
                           break;
                        default:
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == 1) {
                    switch (select) {
                        case 0:
                            LuckyRoundGold.gI().payAndGetStarted(player, (byte) 1);
                            break;
                        case 1:
                            LuckyRoundGold.gI().payAndGetStarted(player, (byte) 100);
                            break;
                        default:
                            break;
                    }
                    Service.getInstance().sendThongBao(player, "Quay hoàn tất vui lòng check rương phụ !");
                } else if (player.iDMark.getIndexMenu() == 2) {
                    switch (select) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            NhanQuaMoc(player, select);
                            break;
                        default:
                            break;
                    }
                }

            } else if (this.mapId == 45) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 10:
                            if (player.getSession().actived) {
                                // Service.getInstance().sendThongBao(player,
                                // "Tính năng đang cập nhật");
                                this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                        "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                        "Rương phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                        - InventoryService.gI().getCountEmptyListItem(
                                                player.inventory.itemsBoxCrackBall))
                                        + " món)",
                                        "Xóa hết\ntrong rương", "Quay\nnhiều lần", "Top\nvòng quay",
                                        "Đóng");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Bạn Cần Mở Thành Viên Để Sử Dụng Chức Năng Này");
                            }

                            break;
                        case 0:
                            ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                            break;
                        case 1:
                            switch (EVENT_SEVER) {
                                case 2:
                                    Attribute at = ServerManager.gI().getAttributeManager()
                                            .find(ConstAttribute.KI);
                                    String text = "Sự kiện 20/11 chính thức tại Ngọc Rồng "
                                            + Manager.SERVER_NAME + "\n + "
                                            + "Số điểm hiện tại của bạn là : "
                                            + player.event.getEventPoint()
                                            + "\nTổng số hoa đã tặng trên toàn máy chủ "
                                            + EVENT_COUNT_THUONG_DE % 999 + "/999";
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                                            at != null && !at.isExpired() ? text
                                            + "\nToàn bộ máy chủ được tăng 20% KI,thời gian còn lại "
                                            + at.getTime() / 60 + " phút."
                                            : text + "\nKhi tặng đủ 999 bông hoa toàn bộ máy chủ được tăng 20% Ki trong 60 phút\n",
                                            "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông",
                                            "Đổi\nHộp quà");
                                    break;
                            }
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                    switch (select) {
                        case 0:
                            LuckyRoundService.gI().openCrackBallUI(player,
                                    LuckyRoundService.USING_GOLD);
                            break;
                        case 1:
                            ShopService.gI().openBoxItemLuckyRound(player);
                            break;
                        case 2:
                            NpcService.gI().createMenuConMeo(player,
                                    ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                    "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                    + "sẽ không thể khôi phục!",
                                    "Đồng ý", "Hủy bỏ");
                            break;
                        case 3:
                            Input.gI().createFormVongQuayThuongDe(player);
                            break;
                        case 4:
                            Service.getInstance().showTopVongQuay(player);
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_SUKIEN) {
                    EventService.gI().openMenuSuKien(player, this, tempId, select);
                }
            } else if (player.zone instanceof ZSnakeRoad) {
                if (mapId == ConstMap.CON_DUONG_RAN_DOC) {
                    ZSnakeRoad zroad = (ZSnakeRoad) player.zone;
                    if (zroad.isKilledAll()) {
                        SnakeRoad road = (SnakeRoad) zroad.getDungeon();
                        ZSnakeRoad egr = (ZSnakeRoad) road.find(ConstMap.THAN_DIEN);
                        egr.enter(player, 360, 408);
                        Service.getInstance().sendThongBao(player, "Hãy xuống gặp thần mèo Karin");
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Hãy tiêu diệt hết quái vật ở đây!");
                    }
                }
            }
        }

    }

    int[] moc = {100, 200, 500, 1000, 2500, 5000};

    void NhanQuaMoc(Player player, int select) {
        if (InventoryService.gI().getCountEmptyBag(player) > 7) {
            int point = moc[select];
            if (!player.mocThuongDe[select]) {
                if (player.GapthuPoint >= point) {
                    player.mocThuongDe[select] = true;
                    short listVp[] = null;
                    int slVp = 10;
                    int slVp1 = 2500;
                    switch (select) {
                        case 0:
                            listVp = new short[]{1150, 1151, 1152, 1153, 1550};
                            break;
                        case 1:
                            listVp = new short[]{1150, 1151, 1152, 1153};
                            break;
                        case 2:
                            listVp = new short[]{1415, 1150, 1151, 1152, 1153};
                            break;
                        case 3:
                            listVp = new short[]{1408, 1150, 1151, 1152, 1153};
                            break;
                        case 4:
                            listVp = new short[]{1456,1150, 1151, 1152, 1153};
                            slVp = 10;
                            break;
                        case 5:
                            listVp = new short[]{1493, 1150, 1151, 1152, 1153, 861, 1429, 1549};
                            slVp = 20;
                            break;
                    }
                    if (listVp != null) {
                        for (short id : listVp) {
                            Item vp = ItemService.gI().createNewItem(id);
                            switch (vp.getId()) {
//                                case 1455: // IRON 
//                                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(25, 38)));
//                                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(25, 38)));
//                                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(25, 38)));
//                                    vp.itemOptions.add(new ItemOption(101, Util.nextInt(25, 38)));
//                                    break;
                                case 1456: // ZENO SAMA
                                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(25, 30)));
                                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(25, 30)));
                                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(25, 30)));
                                    vp.itemOptions.add(new ItemOption(5, Util.nextInt(5, 15)));
                                    break;
                                case 1415:
                                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(10, 1)));
                                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(10, 15)));
                                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(10, 15)));
                                    vp.itemOptions.add(new ItemOption(5, Util.nextInt(5, 10)));
                                    break;
                                case 1408:
                                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(10, 1)));
                                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(10, 15)));
                                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(10, 15)));
                                    vp.itemOptions.add(new ItemOption(5, Util.nextInt(5, 10)));
                                    break;
                                case 1542:
                                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(10, 1)));
                                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(10, 15)));
                                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(10, 15)));
                                    vp.itemOptions.add(new ItemOption(5, Util.nextInt(5, 10)));
                                    break;
                                case 1150:
                                case 1151:
                                case 1152:
                                case 1153:
                                case 1550:
                                    vp.quantity = slVp;
                                    break;
                                case 457:
                                case 861:
                                    vp.quantity = slVp1;
                                    break;

                            }
                            InventoryService.gI().addItemBag(player, vp);
                        }
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Bạn vừa nhận được mốc " + point + " điểm, vui lòng kiểm tra hành trang !");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Con còn thiếu " + (point - player.GapthuPoint) + " điểm nữa để nhận mốc này !");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Con đã nhận mốc " + point + " điểm rồi mà !!!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hãy để trống 8 ô hành trang trước khi nhận nhé !");
        }
    }
}
