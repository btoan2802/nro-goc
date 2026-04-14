package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.services.Event.EventService;
import nro.services.func.ShopService;

public class CayHoaHong extends Npc {

    public CayHoaHong(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5 || this.mapId == 7 || this.mapId == 0 || this.mapId == 14) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Ta có thể giúp gì cho ngươi??\n"
                        + "|2|Bí ngô: x99 bánh quy halloween và x99 kẹo halloween\n"
                        + "|2|Giỏ kẹo bí ngô: x3 kẹo halloween\n"
                        + "|2|Hộp bánh kẹo: x3 bánh quy halloween và x3 kẹo halloween\n"
                        + "|2|Bí ngô Halloween: x99 hộp bánh kẹo và x99 giỏ bánh kẹo",
                        "Cửa hàng\nsự kiện\nhalloween",
                        "Bí ngô",
                        // "Đổi\n ngọc rồng\nbí ngô",
                        "Giỏ kẹo\nbí ngô",
                        "Hộp\nbánh kẹo",
                        "Bí ngô\nHalloween",
                        // "Đổi\ncải trang\nHalloween",
                        "Đóng");
            } else {
                super.openBaseMenu(player);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5 || this.mapId == 7 || this.mapId == 0 || this.mapId == 14) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                        ShopService.gI().openShopSpecial(player, this,
                        ConstNpc.SHOP_EVENT, 0, -1);
                            // handleFlowerPicking(player);
                            break;
                        case 1:
                            EventService.gI().Halloween_note_2_1(player, 1);
                            // EventService.gI().Halloween_1(player, 1);
                            break;
                        // case 2:
                        //     EventService.gI().Halloween_note_2_2(player, 1);
                        //     // EventService.gI().Halloween_2(player, 1);
                        //     break;
                        case 2:
                            EventService.gI().Halloween_1(player, 1);
                            break;
                       
                        case 3:
                            // EventService.gI().Halloween_note_2_2(player, 1);
                            EventService.gI().Halloween_2(player, 1);
                            break;
                        case 4:
                            EventService.gI().Halloween_3(player, 1);
                            break;
                        // case 5:
                        //     EventService.gI().Halloween_4(player, 1);
                        //     break;
                    }
                }
            }
        }
    }

    private void handleFlowerPicking(Player player) {
        Item keotia = InventoryService.gI().findItemBagByTemp(player, 1333);
        if (keotia != null) {
            InventoryService.gI().subQuantityItemsBag(player, keotia, 1);
            Item bohoa = ItemService.gI().createNewItem((short) 1340);
            bohoa.itemOptions.add(new ItemOption(30, 0));
            InventoryService.gI().addItemBag(player, bohoa, 99);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player,
                    "Bạn vừa nhặt được " + bohoa.template.name);
        } else {
            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "|0|Hãy mang dụng cụ kéo tỉa để có thể thu hoa bạn nhé!",
                    "Đóng");
        }
    }
}
