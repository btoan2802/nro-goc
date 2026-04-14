package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.Event.EventService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.services.func.Input;
import nro.services.func.ShopService;
//sktrungthu

public class Chucuoi extends Npc {

    public Chucuoi(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 189) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm boss thỏ đại ca đi\n Nhiều phần thưởng đang chờ đó!",
                        "Về\n Làng Aru");
            } else if (mapId == 0 || mapId == 7 || mapId == 14) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Sự kiện trung thu 2025\n"
                        + "Sự kiện đến hết 2025-10-10 23h59.\n"
                        + "Chi tiết xem tại  " + Manager.DOMAIN,
                        "Vào khu\nsự kiện",
                        "Đổi\n Cà rốt",
                        "Cửa hàng\nsự kiện",
                        "Tặng mâm\nngũ quả",
                        "Top sự kiện\n trung thu\n2025",
                        "Đóng");
            } else if (mapId == 190) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Ngươi cần giúp gì nào",
                        "Gieo\nhạt",
                        "Từ chối");
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
                case 0:
                case 7:
                case 14:
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 189, -1, 255);
                            }
                            if (select == 1) {
                                this.createOtherMenu(player, ConstNpc.OPEN_SKTT_03,
                                        "Các cư dân thu thập đủ nguyên liệu và đến chỗ ta để đổi các phần thưởng sau:\n"
                                        + "1.199 Cà rốt: Cải trang Goku Chú Cuội\n"
                                        + "2.99 Cà rốt: Cải trang Kirin Chú cuội\n"
                                        + "3.50 Cà rốt: Hộp quà Trung thu\n"
                                        + "4.40 Cà rốt: Mâm ngũ quả",
                                        "Tuỳ chọn 1",
                                        "Tuỳ chọn 2",
                                        "Tuỳ chọn 3",
                                        "Tuỳ chọn 4");
                            }
                            if (select == 2) {
                                ShopService.gI().openShopSpecial(player, this,
                                        ConstNpc.SHOP_CHU_CUOI, 0, -1);
                            }
                            if (select == 3) {
                                Input.gI().createFormDoiMamNguQua(player);
                            }
                            if (select == 4) {
                                Service.getInstance().showTopSktrungthu(player);
                            }
                            break;
                        case ConstNpc.OPEN_SKTT_03:
                            if (select == 0) {
                                EventService.gI().EventTrungThu2024_5(player, 1);
                            } else if (select == 1) {
                                EventService.gI().EventTrungThu2024_6(player, 1);
                            } else if (select == 2) {
                                EventService.gI().EventTrungThu2024_7(player, 1);
                            } else if (select == 3) {
                                EventService.gI().EventTrungThu2024_8(player, 1);
                            }
                            break;
                    }
                    break;
                case 190: {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Service.getInstance().sendThongBao(player, "Loading");
                                break;
                            case 1:
                                Service.getInstance().sendThongBao(player, "Loading");
                                break;
                            case 2:
                                Service.getInstance().sendThongBao(player, "Loading");
                                break;
                        }
                    }
                }

                break;
                case 189: {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 695);
                                break;
                        }
                    }
                }

                break;
            }
        }
    }
};
