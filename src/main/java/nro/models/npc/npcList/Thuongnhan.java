/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.NpcMethod;
import nro.services.NpcService;
import nro.services.Service;
import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;

/**
 *
 * @author KENIT
 */
public class Thuongnhan extends Npc {

    public Thuongnhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                "Ta có một vài vật phẩm quý hiếm đây ngươi có muốn xem qua không?",
                "Cửa hàng",
                "Trao đổi\nXu Vàng",
                "Từ Chối");
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        Service.getInstance().sendThongBao(player, "Đợi update!");
                        break;
                    case 1:
                        CombineServiceNew.gI().openTabCombine(player,
                                CombineServiceNew.TRAO_DOI_XU_HADES);
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                NpcMethod.gI().startCombine(player, select);
//            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
//                switch (player.combineNew.typeCombine) {
//                    case CombineServiceNew.THANG_HOA_NGOC_BOI:               
//                        if (select == 0) {
//                            CombineServiceNew.gI().startCombine(player);
//                        }
//                        break;

                //   }
            }
        }
    }
}
