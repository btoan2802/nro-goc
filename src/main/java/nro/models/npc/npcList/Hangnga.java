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
import nro.services.NpcMethod;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.Input;
import nro.services.func.ShopService;
import nro.utils.Util;

/**
 *
 * @author kenit
 */
public class Hangnga extends Npc {

//    private final String[] textChat = new String[]{"Bắt gioiiiiii....", "H\nNh!"};

    public Hangnga(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                if (this.mapId == 191) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi tìm ta có việc gì\n"
                            + " Mỗi ngày phát miền phí 1 hộp trung thu!"
                            + " Nhanh tay nào..",
                            "Cửa hàng\n sự kiện",
                             "Nhận quà\n Trung thu",
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
            if (this.mapId == 191 ) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            ShopService.gI().openShopSpecial(player, this,
                                    ConstNpc.SHOP_HANG_NGA, 0, -1);
                            break;
                        case 1:
                        NpcMethod.gI().NhanQUADiemDanh(player, this);
                        break;

                    }
                }
            }
        }
    }
}
