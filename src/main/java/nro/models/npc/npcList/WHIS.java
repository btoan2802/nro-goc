package nro.models.npc.npcList;

import java.sql.Connection;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.jdbc.DBService;
import nro.models.item.Item;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.NpcMethod;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;

public class WHIS extends Npc {

    public WHIS(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 48) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn gì nào",
                        "Đóng");
            } else if (mapId == 0) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn nâng cấp trang bị ?",
                        "Thần linh\n hóa \n Hủy diệt", "Hủy diệt\nhóa\nKích hoạt", "Học\nTuyệt Kỹ","Đóng");
            } else if (mapId == 154 || mapId == 5) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Phân tách trang bị thần linh",
                        "Chuyển hóa\nNgũ Sắc",
                        "Đổi đồ Thiên Sứ",
                        "Cửa hàng",
//                        "Học\nTuyệt Kỹ",
                        "Đóng");
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
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (select == 0) {
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.DOI_DO_THAN_LINH_THANH_HUY_DIET);
                            }
                            break;
                        case ConstNpc.MENU_START_COMBINE: {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.DOI_DO_THAN_LINH_THANH_HUY_DIET:
                                    // case CombineServiceNew.NANG_CAP_SKH_VIP:
                                    // Log.success("ID COMBINE " + player.combineNew.typeCombine);
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;

                            }
                        }
                        break;
                    }
                    break;
                case 154,5: {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.PHAN_TACH_HUY_DIET_LAY_MANH);
                                break;
                            case 1:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.DOI_DO_THIEN_SU);
                                break;
                            case 2:
                                ShopService.gI().openShopWhisThienSu(player,
                                        ConstNpc.SHOP_WHIS_THIEN_SU, 0);
                                break;
                            case 34:
                                NpcMethod.gI().learnSkill9(player);
                            break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        NpcMethod.gI().startCombine(player, select);
                    }
                }

                break;
            }
        }
    }

    void DoiTrangBiHuyDiet(Player player, int type) {
        if (InventoryService.gI().getQuantity(player, 457) >= 5) {
            Item vp = ItemService.gI().createNewItem((short) ConstItem.doSKHVip[type][player.gender][13]);
            RewardService.gI().initBaseOptionClothes(vp);
            InventoryService.gI().removeItemBody(player, type);
            InventoryService.gI().subQuantityItemsBag(player, (short) 457, 5);
            InventoryService.gI().addItemBag(player, vp);
            InventoryService.gI().sendItemBags(player);
            InventoryService.gI().sendItemBody(player);
            Service.getInstance().sendThongBao(player, "ĐỔi thành công " + vp.getName());
            player.setClothes.setup();
        } else {
            Service.getInstance().sendThongBao(player, "Bạn không có đủ 5 Xu !");
        }
    }
};
