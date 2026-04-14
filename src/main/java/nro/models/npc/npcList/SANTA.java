package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.models.boss.BossManager;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.Event.EventService;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;

public class SANTA extends Npc {

    public SANTA(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?\n",
                        "Cửa hàng", "Vật phẩm", "Shop\n Phụ Kiện", "Shop\nĐệ Tử","Check Boss");

            } else if (this.mapId == 0) {
                createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Hãy tiêu diệt thỏ đại ca, sau đó mang carot đến cho ta nhé!!s\n",
                        "Đổi\nBill Bí ngô", "Đổi \nCải trang\n thỏ đại ca",
                        "Đổi\nhộp quà\ntrung thu");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0: // shop
                            this.openShopWithGender(player, ConstNpc.SHOP_SANTA_0, 0);
                            break;
                        case 1:
                            ShopService.gI().openShopSpecial(player, this,
                                    ConstNpc.SHOP_SANTA_THOI_VANG_2, 4, -1);
                            break;
//                        case 2: // cải trang
//                            ShopService.gI().openShopSpecial(player, this,
//                                    ConstNpc.SHOP_SANTA_THOI_VANG_3, 3, -1);
//                            break;
                       case 3: 
                           ShopService.gI().openShopSpecial(player, this,
                                 ConstNpc.SHOP_SANTA_DE, 15, -1);

                         break;
                        case 2: // cải trang
                            ShopService.gI().openShopSpecial(player, this,
                                    ConstNpc.SHOP_SANTA_LUCKY, 7, -1);
                            break;
                                 case 4://check boss
                           BossManager.gI().showListBoss1(player);
                           break;
//                                  case 6: // cải trang
//                            ShopService.gI().openShopSpecial(player, this,
//                                    ConstNpc.SHOP_SANTA_CAI_TRANG, 5, -1);
//                            break;
//                                  case 7: // cải trang
//                            ShopService.gI().openShopSpecial(player, this,
//                                    ConstNpc.SHOP_SANTA_VAN_BAY, 11, -1);
//                            break;

                    }
                }
            } else if (this.mapId == 0) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0: // shop
                            EventService.gI().EventTrungThu2024_Doi_Carrot(player,
                                    (byte) 1, 1);
                            break;
                        case 1:
                            EventService.gI().EventTrungThu2024_Doi_Carrot(player,
                                    (byte) 2, 1);
                            break;
                        case 2: // cải trang
                            EventService.gI().EventTrungThu2024_Doi_Carrot(player,
                                    (byte) 3, 1);
                            break;

                    }
                }
            }
        }
    }
};
