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
import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;

/**
 *
 * @author KENIT
 */
public class Thongoc extends Npc {

    public Thongoc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                "Ta là thợ ngọc đến từ hành tinh mới\n"
                + "Ta có hỗ trợ liên quan tới vật phẩm mới  Ngọc Bội\n"
                + "Ngươi muốn ta giúp gì?",
                "Cửa hàng\nThợ ngọc",
                "Thăng hoa\nNgọc bội",
                "Thăng cấp\nNgọc bội");
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        ShopService.gI().openShopSpecial(player, this,
                                ConstNpc.SHOP_THO_NGOC_THOI_VANG, 0, -1);
                        break;
                    case 1:
                        CombineServiceNew.gI().openTabCombine(player,
                                CombineServiceNew.THANG_HOA_NGOC_BOI);
                        break;
                    case 2:
                        CombineServiceNew.gI().openTabCombine(player,
                                CombineServiceNew.THANG_CAP_NGOC_BOI);
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
