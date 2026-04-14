/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.NpcService;
import nro.services.func.CombineServiceNew;

/**
 *
 * @author KENIT
 */
public class Hatmitht extends Npc {

    public Hatmitht(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                "Ngươi muốn nâng cấp chân mệnh hả\n"
                + "Ta sẽ giúp ngươi điều đó?\n"
                + "|7|Hãy đọc kỹ hướng dẫn sử dụng nhé!!!",
                "Nâng cấp\nChân Mệnh",
                "Hướng\nDẫn",
                "Từ chối");
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_THIEN_TU);
                        break;
                    case 1:
                        NpcService.gI().createTutorial(player, this.avartar,
                                ConstNpc.HUONG_DAN_TT);
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                switch (player.combineNew.typeCombine) {
                    case CombineServiceNew.NANG_CAP_THIEN_TU:
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                        break;

                }
            }
        }
    }
}
