/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.Service;
import nro.services.TaskService;
import nro.models.item.Item;
import nro.services.ItemService;

/**
 *
 * @author kenit
 */
public class Ruongsuutam extends Npc {

    public Ruongsuutam(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                if (this.mapId == 102 || this.mapId == 84) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Vàng bạc châu báu gì cứ yên tâm giao cho tôi",
                            "Mở rương\n(" + (player.inventory.itemsBox_ct_pet.size()
                            - InventoryService.gI().getCountEmptyListItem(
                                    player.inventory.itemsBox_ct_pet))
                            + " món)", "Nâng cấp\nrương", "Từ chối");
                }
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 102 || this.mapId == 84) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0: // Mở rương
                            InventoryService.gI().sendItemBox_pet_ct(player);
                            InventoryService.gI().openBox_pet_ct(player);
                            break;
                        case 1: // Nâng cấp rương
                            int currentSize = player.inventory.itemsBox_ct_pet.size();
                            if (currentSize >= 80) {
                                Service.getInstance().sendThongBaoFromAdmin(player, "Rương sưu tầm của bạn đã đạt tối đa (80 ô)");
                                return;
                            }
                            long upgradeCost = 500 + 100 * currentSize; // 500 + 100 * currentSize
                            this.createOtherMenu(player, ConstNpc.MENU_RUONG_SUU_TAM,
                                    "Bạn có chắc muốn mở thêm ô " + (currentSize + 1) + " với giá " + upgradeCost + " TVK ?",
                                    "Đồng ý", "Từ chối");
                            break;
                        case 2: // Từ chối
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_RUONG_SUU_TAM) {
                    switch (select) {
                        case 0: // Đồng ý nâng cấp
                            int currentSize = player.inventory.itemsBox_ct_pet.size();
                            if (currentSize >= 80) {
                                Service.getInstance().sendThongBaoFromAdmin(player, "Rương sưu tầm của bạn đã đạt tối đa (80 ô)");
                                return;
                            }
                            int upgradeCost = 2000 * currentSize;
                             Item tv = InventoryService.gI().findItemBag(player, 1429);
                            if (tv.quantity < upgradeCost) {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ " + upgradeCost + " TVK để mở ô mới!");
                                return;
                            }
                            player.inventory.itemsBox_ct_pet.add(ItemService.gI().createItemNull());
                            InventoryService.gI().subQuantityItemsBag(player, tv, upgradeCost);
                            Service.getInstance().sendMoney(player);
                            Service.getInstance().sendThongBao(player,
                                    "Rương sưu tầm của bạn đã được mở rộng thêm 1 ô (tổng: " + (currentSize + 1) + " ô)");
                            break;
                        case 1: // Từ chối
                            break;
                    }
                }
            }
        }
    }
}
