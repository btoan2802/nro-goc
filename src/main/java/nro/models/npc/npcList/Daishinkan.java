/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.Input;
import nro.services.func.ShopService;
import nro.utils.Util;

/**
 *
 * @author kenit
 */
public class Daishinkan extends Npc {

    private final String[] textChat = new String[]{"Tranh tài cao thủ....", "Tại đây\nGhi danh anh hùng!"};

    public Daishinkan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, cậu muốn tôi giúp gì?",
                            "Xem top\nsức mạnh",
                            "Xem top\n nhiệm vụ",
                            "Xem top\nNạp",
                            "Top\n Tiêu",
                            "Top\n Kill boss","Nhận\n Quà");
                }
            }
        }
    }

    @Override
    public void update() {
        //   System.out.println("chạy chạy");
        if (Util.canDoWithTime(this.lastTimeChat, (long) this.timeChat)) {
            this.lastTimeChat = System.currentTimeMillis();
            this.npcChat(this.textChat[Util.nextInt(0, this.textChat.length - 1)]);
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
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
                        case 3:
                            Service.getInstance().showTopSaiTv(player);
                            break;
                        case 4:
                            Service.getInstance().showTopBossp(player);
                            break;
                        case 5:
                            ShopService.gI().openBoxItemReward(player);
                            break;
                    }
                }
            }
        }
    }
}
