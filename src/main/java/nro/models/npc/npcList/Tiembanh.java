/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//sktrungthu
package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.Event.EventService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.Input;
import nro.services.func.ShopService;
import nro.utils.Util;

/**
 *
 * @author kenit
 */
public class Tiembanh extends Npc {

//    private final String[] textChat = new String[]{"Bắt gioiiiiii....", "H\nNh!"};
    public Tiembanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                if (this.mapId == 5 || this.mapId == 0 || mapId == 7 || mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì",
                            "Đổi bánh",
                            "Đổi bánh\nSự kiện",
                            "Cửa hàng\n sự kiện",
                            "Từ chối");
                }
            }
        }
    }

//    @Override
//    public void update() {
//        //   System.out.println("chạy chạy");
//        if (Util.canDoWithTime(this.lastTimeChat, (long) this.timeChat)) {
//            this.lastTimeChat = System.currentTimeMillis();
//            this.npcChat(this.textChat[Util.nextInt(0, this.textChat.length - 1)]);
//        }
//    }
    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5 || this.mapId == 0 || mapId == 7 || mapId == 14) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0: {
                            this.createOtherMenu(player, ConstNpc.OPEN_SKTT_01,
                                    "Các cư dân thu thập đủ nguyên liệu và đến chỗ ta để làm bánh trung thu với công thức sau:\n"
                                    + "Bánh gạo nướng: 15 Gạo tẻ + 10 Lạp xưởng + 01 Trứng\n"
                                    + "Bánh đậu xanh: 30 Nếp dẻo + 20 Đậu xanh + 02 Bánh Socola\n"
                                    + "Sử dụng Bánh gạo nướng và Bánh đậu xanh giúp tăng chỉ số hoặc dùng để đổi Bánh thượng hạng",
                                    "Đổi Bánh\ngạo nướng",
                                    "Đổi Bánh\nđậu xanh",
                                    "Từ chối");
                        }
                        break;
                        case 1: {
                            this.createOtherMenu(player, ConstNpc.OPEN_SKTT_02,
                                    "Các cư dân thu thập đủ nguyên liệu và đến chỗ ta để làm bánh trung thu thượng hạng với công thức sau:\n"
                                    + "Bánh thượng hạng 01: 01 Bánh gạo nướng + 01 bánh đậu xanh\n"
                                    + "*** Phần thưởng: Bánh thượng hạng 01"
                                    + "Bánh thượng hạng 02: 02 Bánh gạo nướng + 02 bánh đậu xanh\n"
                                    + "*** Phần thưởng: Bánh thượng hạng 02",
                                    "Bánh thượng\n hạng 01",
                                    "Bánh thượng\n hạng 02",
                                    "Từ chối");
                        }
                        break;
                        case 2:
                            ShopService.gI().openShopSpecial(player, this,
                                    ConstNpc.SHOP_TIEM_BANH, 0, -1);
                            break;

                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_SKTT_01) {
                    switch (select) {
                        case 0: // shop
                            EventService.gI().EventTrungThu2024_1(player, 1);
                            break;
                        case 1:
                            EventService.gI().EventTrungThu2024_2(player, 1);
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_SKTT_02) {
                    switch (select) {
                        case 0: // shop
                            EventService.gI().EventTrungThu2024_3(player, 1);
                            break;
                        case 1:
                            EventService.gI().EventTrungThu2024_4(player, 1);
                            break;
                    }
                }
            }
        }
    }
}
