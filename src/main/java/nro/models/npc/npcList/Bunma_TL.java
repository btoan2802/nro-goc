package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.NpcMethod;
import nro.services.TaskService;
import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;

public class Bunma_TL extends Npc {

    public Bunma_TL(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 187:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ồ! Một cậu bé dễ thương\n"
                            + "Cậu muốn mua một số vật phẩm mới không nào ?",
                            "Cửa hàng", "Đóng");
                    break;
                case 102:
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?",
                                "Cửa hàng", "Đóng");
                    }
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (mapId) {
                    case 187:
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.BASE_MENU:
                                switch (select) {
                                    case 0:
                                        ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_BUNMA_TL_2, 2,
                                                -1);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case ConstNpc.MENU_START_COMBINE:
                                NpcMethod.gI().startCombine(player, select);
                                break;
                            default:
                                break;
                        }

                        break;
                    case 102:
                        if (select == 0) {
                            ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                    player.gender);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
};
