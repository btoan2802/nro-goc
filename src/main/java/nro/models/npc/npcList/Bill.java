package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;

public class Bill extends Npc {

    public Bill(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 48) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, ""
                        + "Ta sẽ phù phép cho đồ thần linh của ngươi thành trang bị hủy diệt, nhưng hãy mang cho ta thêm x99 thức ăn nữa!", 
                        "Nâng cấp\n đồ hủy diệt", 
                        "Đóng");
            } else if (this.mapId == 154) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Whis và ta sẽ đưa người về trái đất?!",
                        "Về\n Trái đất",
                        "Đóng");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 48:
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.DOI_DO_THAN_LINH_THANH_HUY_DIET);
                                    break;
                                case 1:
                                    // ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_BILL_HUY_DIET_0, 0, -1);
                                    break;
                            }
                            break;
                        case ConstNpc.MENU_START_COMBINE:
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.DOI_DO_THAN_LINH_THANH_HUY_DIET:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                            break;
                    }
                    break;

                case 154:
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 432);
                                    break;
                            }
                            break;
                    }
                    break;
            }
        }
    }
}