package nro.services;

import nro.consts.ConstItem;
import nro.consts.ConstOption;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.war.BlackBallWar;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.npc.specialnpc.MagicTree;
import nro.models.player.Inventory;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.server.io.Message;

import nro.utils.Logger;
import nro.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import nro.models.npc.specialnpc.KaminEgg;

public class InventoryServiceNew {

    private static InventoryServiceNew I;

    public static InventoryServiceNew gI() {
        if (InventoryServiceNew.I == null) {
            InventoryServiceNew.I = new InventoryServiceNew();
        }
        return InventoryServiceNew.I;
    }

    private void __________________Tìm_kiếm_item_____________________________() {
        // **********************************************************************
    }

    //
    // public Item findItem(List<Item> list, int tempId) throws Exception {
    // for (Item item : list) {
    // if (item.isNotNullItem() && item.template.id == tempId) {
    // return item;
    // }
    // }
    // throw new Exception("Không tìm thấy item " + tempId);
    // }
    public Item findItem(List<Item> list, int tempId) {
        try {
            for (Item item : list) {
                if (item.isNotNullItem() && item.template.id == tempId) {
                    return item;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void addItemGiftCodeToPlayer(Player p, HashMap<Integer, HashMap<Integer, Integer>> Items, ArrayList<ItemOption> optionList) {
        int[] randomItemIds = {1559, 1560, 1561, 1562, 1563, 1564, 1565, 1566, 1567};
        String textGift = "Bạn vừa nhận được:\b";
        int quantityGoldBar = 0;
        Set<Integer> keySet = Items.keySet();
        for (Integer key : keySet) {
            int idItem = key;
            int quantity = 0;
            int hsd = 0;
            // OPTION
            HashMap<Integer, Integer> ItemOption = Items.get(key);
            Set<Integer> keySetOption = ItemOption.keySet();
            for (Integer keyO : keySetOption) {
                quantity = keyO;
                hsd = ItemOption.get(keyO);
            }
            switch (idItem) {
                case -1:
                    p.inventory.gold = Math.min(p.inventory.gold + (long) quantity, 1L);
                    textGift += quantity + " vàng\b";
                    break;
                case -2:
                    p.inventory.gem = Math.min(p.inventory.gem + quantity, 1);
                    textGift += quantity + " ngọc\b";
                    break;
                case -3:
                    break;
                default:
                    Item itemGiftTemplate = ItemService.gI().createNewItem((short) idItem);
                    if (itemGiftTemplate != null) {
                        Item itemGift = ItemService.gI().createNewItem((short) idItem);

                        ItemService.gI().OptionAllItem(itemGift, hsd);
                         if (optionList != null && !optionList.isEmpty()) {
                        itemGift.itemOptions.addAll(optionList);
                    }
                        if (itemGift.isDTL()) {
                            itemGift.itemOptions.add(new ItemOption(ConstOption.KY_GUI_VANG, 1));
                        }
                        if (itemGift.template.type == 5 || itemGift.template.type == 29
                                || itemGift.template.type == 27 || itemGift.template.type == 12
                                || itemGift.template.type == 14) {
                            if (itemGift.getId() == 457) {
                                if (p.lock == true || itemGift.quantity == 20 || itemGift.quantity == 150
                                        || itemGift.quantity == 10) {
                                    itemGift.itemOptions.add(new ItemOption(30, 1));
                                    p.lock = false;
                                }
                            } else if (itemGift.template.id != 1362 && itemGift.getId() != 457) {
                                itemGift.itemOptions.add(new ItemOption(30, 1));
                            }
                        }
                        itemGift.quantity = quantity;
                        if (itemGift.template.id == 457) {
                            quantityGoldBar += quantity;
                        }
                        addItemBag(p, itemGift);
                        textGift += "x" + quantity + " " + itemGift.template.name + "\b";
                    }
                    break;
            }
        }
//        Random rand = new Random();
//        int selectedId = randomItemIds[rand.nextInt(randomItemIds.length)];
//        Item itemGift = ItemService.gI().createNewItem((short) selectedId, 1);
//
//        if (itemGift != null) {
//            switch (selectedId) {
//                case 1559:
//                    itemGift.itemOptions.add(new ItemOption(7, Util.nextInt(150, 2200)));
//                    itemGift.itemOptions.add(new ItemOption(103, 1));
//                    itemGift.itemOptions.add(new ItemOption(248, 0));
//                    itemGift.itemOptions.add(new ItemOption(30, 0));
//                    break;
//                case 1560:
//                    itemGift.itemOptions.add(new ItemOption(6, Util.nextInt(150, 3000)));
//                    itemGift.itemOptions.add(new ItemOption(50, 1));
//                    itemGift.itemOptions.add(new ItemOption(248, 0));
//                    itemGift.itemOptions.add(new ItemOption(30, 0));
//                    break;
//                case 1561:
//                    itemGift.itemOptions.add(new ItemOption(0, Util.nextInt(100, 300)));
//                    itemGift.itemOptions.add(new ItemOption(103, 1));
//                    itemGift.itemOptions.add(new ItemOption(248, 0));
//                    itemGift.itemOptions.add(new ItemOption(30, 0));
//                    break;
//                case 1562:
//                    itemGift.itemOptions.add(new ItemOption(7, Util.nextInt(170, 2500)));
//                    itemGift.itemOptions.add(new ItemOption(77, 1));
//                    itemGift.itemOptions.add(new ItemOption(248, 0));
//                    itemGift.itemOptions.add(new ItemOption(30, 0));
//                    break;
//                case 1563:
//                    itemGift.itemOptions.add(new ItemOption(0, Util.nextInt(100, 350)));
//                    itemGift.itemOptions.add(new ItemOption(77, 1));
//                    itemGift.itemOptions.add(new ItemOption(248, 0));
//                    itemGift.itemOptions.add(new ItemOption(30, 0));
//                    break;
//                case 1564:
//                    itemGift.itemOptions.add(new ItemOption(6, Util.nextInt(140, 3100)));
//                    itemGift.itemOptions.add(new ItemOption(77, 1));
//                    itemGift.itemOptions.add(new ItemOption(248, 0));
//                    itemGift.itemOptions.add(new ItemOption(30, 0));
//                    break;
//                case 1565:
//                    itemGift.itemOptions.add(new ItemOption(0, Util.nextInt(100, 400)));
//                    itemGift.itemOptions.add(new ItemOption(50, 1));
//                    itemGift.itemOptions.add(new ItemOption(248, 0));
//                    itemGift.itemOptions.add(new ItemOption(30, 0));
//                    break;
//                case 1566:
//                    itemGift.itemOptions.add(new ItemOption(0, Util.nextInt(140, 450)));
//                    itemGift.itemOptions.add(new ItemOption(77, 1));
//                    itemGift.itemOptions.add(new ItemOption(248, 0));
//                    itemGift.itemOptions.add(new ItemOption(30, 0));
//                    break;
//                case 1567:
//                    itemGift.itemOptions.add(new ItemOption(7, Util.nextInt(140, 2700)));
//                    itemGift.itemOptions.add(new ItemOption(77, 1));
//                    itemGift.itemOptions.add(new ItemOption(248, 0));
//                    itemGift.itemOptions.add(new ItemOption(30, 0));
//                    break;
//            }
//            InventoryService.gI().addItemBag(p, itemGift, 999);
//            Service.getInstance().sendThongBao(p, "Chúc mừng bạn vừa nhận được " + itemGift.template.name + " từ gift code!");
//        }
        InventoryService.gI().sendItemBags(p);
        if (!textGift.equals("Chúc mừng bạn vừa nhận được:")) {
            Service.getInstance().sendThongBao(p, textGift);
        }

        // Ghi log nếu có vàng
        if (quantityGoldBar > 0) {
            Logger.errorSaveHistGoldBar(p, quantityGoldBar, (byte) 3, "Code");
        }
    }

    public Item findItemBody(Player player, int tempId) throws Exception {
        return this.findItem(player.inventory.itemsBody, tempId);
    }

    public Item findItemBag(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBag, tempId);
    }

    // public Item findItemBag(Player player, int tempId) throws Exception {
    // return this.findItem(player.inventory.itemsBag, tempId);
    // }
    public Item findItemBox(Player player, int tempId) throws Exception {
        return this.findItem(player.inventory.itemsBox, tempId);
    }

    public boolean isExistItem(List<Item> list, int tempId) {
        try {
            this.findItem(list, tempId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExistItemBody(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBody, tempId);
    }

    // public boolean isExistItemBag(Player player, int tempId) {
    // return ItemService.gI().isExistItemBag(player, (short) tempId);
    // }
    public boolean isExistItemBox(Player player, int tempId) {
        return this.isExistItem(player.inventory.itemsBox, tempId);
    }

    public boolean hasOptionTemplateId(Item item, int optionTemplateId) {
        for (ItemOption option : item.itemOptions) {
            if (option.optionTemplate.id == optionTemplateId) {
                return true;
            }
        }
        return false;
    }

    private void __________________Sao_chép_danh_sách_item__________________() {
        // **********************************************************************
    }

    public List<Item> copyList(List<Item> items) {
        List<Item> list = new ArrayList<>();
        for (Item item : items) {
            list.add(ItemService.gI().copyItem(item));
        }
        return list;
    }

    public List<Item> copyItemsBody(Player player) {
        return copyList(player.inventory.itemsBody);
    }

    public List<Item> copyItemsBag(Player player) {
        return copyList(player.inventory.itemsBag);
    }

    public List<Item> copyItemsBox(Player player) {
        return copyList(player.inventory.itemsBox);
    }

    private void __________________Vứt_bỏ_item______________________________() {
        // **********************************************************************
    }

    public void throwItem(Player player, int where, int index) {
        Item itemThrow = null;
        if (where == 0) {
            itemThrow = player.inventory.itemsBody.get(index);
            removeItemBody(player, index);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player);
            // Service.getInstance().rsDanhHieu(player);
        } else if (where == 1) {
            itemThrow = player.inventory.itemsBag.get(index);
            if (itemThrow.template.id != 457) {
                removeItemBag(player, index);
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Không thể vứt bỏ "
                        + itemThrow.template.name);
            }
        }
        if (itemThrow == null) {
            return;
        }

    }

    private void __________________Xoá_bỏ_item______________________________() {
        // **********************************************************************
    }

    public void removeItem(List<Item> items, int index) {
        Item item = ItemService.gI().createItemNull();
        items.set(index, item);
    }

    public void removeItem(List<Item> items, Item item) {
        if (item == null) {
            return;
        }
        Item it = ItemService.gI().createItemNull();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                items.set(i, it);
                item.dispose();
                break;
            }
        }
    }

    public void removeItemBag(Player player, int index) {
        this.removeItem(player.inventory.itemsBag, index);
    }

    public void removeItemBag(Player player, Item item) {
        this.removeItem(player.inventory.itemsBag, item);
    }

    public void removeItemBody(Player player, int index) {
        this.removeItem(player.inventory.itemsBody, index);
    }

    public void removeItemPetBody(Player player, int index) {
        this.removeItemBody(player.pet, index);
    }

    public void removeItemBox(Player player, int index) {
        this.removeItem(player.inventory.itemsBox, index);
    }

    private void __________________Giảm_số_lượng_item_______________________() {
        // **********************************************************************
    }

    public void subQuantityItemsBag(Player player, short itemId, int quantity) {
        for (Item item : player.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && item.template.id == itemId)
                .collect(Collectors.toList())) {
            final int quantityItem = item.quantity;
            if (quantity > 0) {
                subQuantityItem(player.inventory.itemsBag, item, Math.min(quantity,
                        quantityItem));
                quantity -= Math.min(quantity, quantityItem);
            }
            return;
        }
    }

    public void subQuantityItemsBag(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBag, item, quantity);
    }

    public void subQuantityItemsBody(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBody, item, quantity);
    }

    public void subQuantityItemsBox(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBox, item, quantity);
    }

    public void subQuantityItem(List<Item> items, Item item, int quantity) {
        if (item != null) {
            for (Item it : items) {
                if (item.equals(it)) {
                    it.quantity -= quantity;
                    if (it.quantity <= 0) {
                        this.removeItem(items, item);
                    }
                    break;
                }
            }
        }
    }

    private void __________________Sắp_xếp_danh_sách_item___________________() {
        // **********************************************************************
    }

    public void sortItems(List<Item> list) {
        int first = -1;
        int last = -1;
        Item tempFirst = null;
        Item tempLast = null;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isNotNullItem()) {
                first = i;
                tempFirst = list.get(i);
                break;
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).isNotNullItem()) {
                last = i;
                tempLast = list.get(i);
                break;
            }
        }
        if (first != -1 && last != -1 && first < last) {
            list.set(first, tempLast);
            list.set(last, tempFirst);
            sortItems(list);
        }
    }

    private void __________________Thao_tác_tháo_mặc_item___________________() {
        // **********************************************************************
    }

    private Item putItemBag(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
            if (!player.inventory.itemsBag.get(i).isNotNullItem()) {
                player.inventory.itemsBag.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBox(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBox.size(); i++) {
            if (!player.inventory.itemsBox.get(i).isNotNullItem()) {
                player.inventory.itemsBox.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBox_ct_pet(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBox_ct_pet.size(); i++) {
            if (!player.inventory.itemsBox_ct_pet.get(i).isNotNullItem()) {
                player.inventory.itemsBox_ct_pet.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBody(Player player, Item item) {
        Item sItem = item;
        if (!item.isNotNullItem()) {
            return sItem;
        }
        switch (item.template.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 32:
            case 23:
            case 24:
            case 11:
            case 72:
            case 39:
            case 27:
            case 77:
            case 78:
                break;
            default:
                Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                        "Trang bị không phù hợp!");
                return sItem;
        }
        if (item.template.gender < 3 && item.template.gender != player.gender) {
            Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                    "Trang bị không phù hợp!");
            return sItem;
        }
        long powerRequire = item.template.strRequire;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 21) {
                powerRequire = io.param * 1000000000L;
                break;
            }
        }
        if (player.nPoint.power < powerRequire) {
            Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                    "Sức mạnh không đủ yêu cầu!");
            return sItem;
        }
        int index = -1;
        switch (item.template.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                index = item.template.type;
                break;
            case 32:
                index = 6;
                break;
            case 23:
            case 24:
                index = 9;
                break;
            case 11:
                index = 8;
                break;
            case 39:
                if (!player.isPet) {
                    index = 13; // Nếu không phải pet, index = 13
                } else {
                    index = 8;  // Nếu là pet, index = 8
                }
                break;
            case 72:
                if (!player.isPet) {
                    index = 10;
                    break;
                } else {
                    break;
                }
            case 77:
                index = 11;
                break;
            case 78:
                index = 7;
                break;

        }
        if (player.inventory.itemsBody.size() < index) {
            Service.getInstance().sendThongBao(player.isPet ? ((Pet) player).master : player,
                    "Trang bị không phù hợp!");
            return sItem;
        }
        sItem = player.inventory.itemsBody.get(index);
        player.inventory.itemsBody.set(index, item);

        return sItem;
    }

    public void itemBagToBody(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBag.size()) {
            return;
        }
        Item item = player.inventory.itemsBag.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBag.set(index, putItemBody(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
            Service.getInstance().sendFlagBag(player);
        }

    }

    public void itemBodyToBag(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            if (index == 10) {
                // Service.getInstance().sendPetFollow(player, (short) 0);
            }
            // if (index == 7) {
            // if (player.newpet != null) {
            // ChangeMapService.gI().exitMap(player.newpet);
            // player.newpet.dispose();
            // player.newpet = null;
            // }
            // }
            player.inventory.itemsBody.set(index, putItemBag(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().player(player);
            player.zone.load_Me_To_Another(player);
            // player.zone.load_Another_To_Me(player);
            Service.getInstance().sendFlagBag(player);
            //  Service.getInstance().rsDanhHieu(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
            // Service.getInstance().point(player);
        }
    }

    public void itemBagToPetBody(Player player, int index) {
        if (player.pet != null && player.pet.nPoint.power >= 1500000) {
            Item item = player.inventory.itemsBag.get(index);
            if (item.isNotNullItem()) {
                Item itemSwap = putItemBody(player.pet, item);
                player.inventory.itemsBag.set(index, itemSwap);
                sendItemBags(player);
                sendItemBody(player);
                Service.getInstance().Send_Caitrang(player.pet);
                Service.getInstance().Send_Caitrang(player);
//                Service.getInstance().rsDanhHieu(player);
//                Service.getInstance().rsDanhHieu(player.pet);
                if (!itemSwap.equals(item)) {
                    Service.getInstance().point(player);
                    Service.getInstance().showInfoPet(player);
                }
            }
        } else {
            Service.getInstance().sendThongBaoOK(player,
                    "Đệ tử phải đạt 1tr5 sức mạnh mới có thể mặc");

        }
    }

    public void itemPetBodyToBag(Player player, int index) {
        // phát hiện lỗi 470
        Item item = player.pet.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.pet.inventory.itemsBody.set(index, putItemBag(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player.pet);
            Service.getInstance().Send_Caitrang(player);
            //   Service.getInstance().rsDanhHieu(player);
            Service.getInstance().point(player);
            Service.getInstance().showInfoPet(player);
        }
    }

    public void itemBoxToBodyOrBag(Player player, int index) {
        Item item = player.inventory.itemsBox.get(index);
        if (item.isNotNullItem()) {
            boolean done = false;
            if (item.template.type >= 0 && item.template.type <= 5 || item.template.type == 32) {
                Item itemBody = player.inventory.itemsBody.get(item.template.type == 32 ? 6 : item.template.type);
                if (!itemBody.isNotNullItem()) {
                    if (item.template.gender == player.gender || item.template.gender == 3) {
                        long powerRequire = item.template.strRequire;
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 21) {
                                powerRequire = io.param * 1000000000L;
                                break;
                            }
                        }
                        if (powerRequire <= player.nPoint.power) {
                            player.inventory.itemsBody.set(item.template.type == 32 ? 6 : item.template.type, item);
                            player.inventory.itemsBox.set(index, itemBody);
                            done = true;

                            sendItemBody(player);
                            Service.getInstance().Send_Caitrang(player);
                            //    Service.getInstance().rsDanhHieu(player);
                            Service.getInstance().point(player);
                        }
                    }
                }
            }
            if (!done) {
                if (addItemBag(player, item)) {
                    if (item.quantity == 0) {
                        Item sItem = ItemService.gI().createItemNull();
                        player.inventory.itemsBox.set(index, sItem);
                    }
                    sendItemBags(player);
                }
            }
            sendItemBox2(player);
        }
    }

    public void itemBagToBox(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        if (item != null) {
            if (addItemBox(player, item)) {
                if (item.quantity == 0) {
                    Item sItem = ItemService.gI().createItemNull();
                    player.inventory.itemsBag.set(index, sItem);
                }
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
                sendItemBox2(player);
            }
        }
    }

    public void itemBodyToBox_ct_pet(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBody.set(index, putItemBox(player, item));
            sortItems(player.inventory.itemsBag);
            sendItemBody(player);
            sendItemBox3(player);
            Service.getInstance().Send_Caitrang(player);
            //  Service.getInstance().rsDanhHieu(player);
            sendItemBody(player);
            Service.getInstance().point(player);
        }
    }

    public void itemBagToBox_ct_pet(Player player, int index) {
        Item item = player.inventory.itemsBag.get(index);
        if (item != null) {
            if (addItemBox_ct_pet(player, item)) {
                if (item.quantity == 0) {
                    Item sItem = ItemService.gI().createItemNull();
                    player.inventory.itemsBag.set(index, sItem);
                }
                sortItems(player.inventory.itemsBag);
                sendItemBags(player);
                sendItemBox3(player);
                System.err.println("đây đưa vo item ct");
            }
        }
    }

    public void itemBodyToBox(Player player, int index) {
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBody.set(index, putItemBox(player, item));
            sortItems(player.inventory.itemsBag);
            sendItemBody(player);
            sendItemBox2(player);
            Service.getInstance().Send_Caitrang(player);
            //  Service.getInstance().rsDanhHieu(player);
            sendItemBody(player);
            Service.getInstance().point(player);
        }
    }

    private void __________________Gửi_danh_sách_item_cho_người_chơi________() {
        // **********************************************************************
    }

    public void sendItemBags(Player player) {
        sortItems(player.inventory.itemsBag);
        Message msg;
        try {
            msg = new Message(-36);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBag.size());
            for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
                Item item = player.inventory.itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    continue;
                }
                msg.writer().writeShort(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeUTF(item.getInfo());
                msg.writer().writeUTF(item.getContent());
                msg.writer().writeByte(item.itemOptions.size()); // options
                for (int j = 0; j < item.itemOptions.size(); j++) {
                    msg.writer().writeByte(item.itemOptions.get(j).optionTemplate.id);
                    msg.writer().writeShort(item.itemOptions.get(j).param);
                }
            }

            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemBody(Player player) {
        Message msg;
        try {
            msg = new Message(-37);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeByte(player.inventory.itemsBody.size());
            for (Item item : player.inventory.itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        //   Service.getInstance().rsDanhHieu(player);
        Service.getInstance().Send_Caitrang(player);
    }

    public void sendItemBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBox.size());
            for (Item it : player.inventory.itemsBox) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    msg.writer().writeByte(it.itemOptions.size());
                    for (ItemOption io : it.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        this.openBox(player);
    }

    private void sendItemBox2(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBox.size());
            for (Item it : player.inventory.itemsBox) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    msg.writer().writeByte(it.itemOptions.size());
                    for (ItemOption io : it.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void sendItemBox3(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBox_ct_pet.size());
            for (Item it : player.inventory.itemsBox_ct_pet) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    msg.writer().writeByte(it.itemOptions.size());
                    for (ItemOption io : it.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void openBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemBox_ct_pet(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBox_ct_pet.size());
            for (Item it : player.inventory.itemsBox_ct_pet) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    msg.writer().writeByte(it.itemOptions.size());
                    for (ItemOption io : it.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        this.openBox_ct_pet(player);
    }

    public void openBox_ct_pet(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void __________________Thêm_vật_phẩm_vào_danh_sách______________() {
        // **********************************************************************
    }

    private boolean addItemSpecial(Player player, Item item) {
        // bùa
        if (item.template.type == 13) {
            int min = 0;
            try {
                // String tagShopBua = player.iDMark.getShopOpen().tagName;
                // if (tagShopBua.equals("BUA_1H") || tagShopBua.equals("BUA_DETU")) {
                // min = 60;
                // } else if (tagShopBua.equals("BUA_8H")) {
                // min = 60 * 8;
                // } else if (tagShopBua.equals("BUA_1M")) {
                // min = 60 * 24 * 30;
                // }
            } catch (Exception e) {
            }
            player.charms.addTimeCharms(item.template.id, min);

            return true;
        }

        switch (item.template.id) {

            case 453: // tàu tennis
                player.haveTennisSpaceShip = true;
                return true;
            case 74: // đùi gà nướng
                player.nPoint.setFullHpMp();
                PlayerService.gI().sendInfoHpMp(player);
                return true;
        }
        return false;
    }

    private Item converItem(Item item) {
        short idItem = item.template.id;
        Item itemNew = null;
        switch (idItem) {
            // case 1000:
            // case 1001:
            // case 1002: { // ngọc rồng 3 sao
            // itemNew = ItemService.gI().createNewItem((short) 16);

            // }
            // break;
            // case 1003:
            // case 1004:
            // case 1005:
            // case 1006:
            // case 1007:
            // case 1008:
            // case 1009:
            // case 1010:
            // case 1011:
            // case 1012: { // Vé lật thẻ
            // itemNew = ItemService.gI().createNewItem((short) 1401);
            // }
            // break;
            // case 1118:
            // case 1119:
            // case 1120:
            // case 1121:
            // case 1122:
            // case 1123:
            // case 1124: { // Vé điểm thưởng
            // itemNew = ItemService.gI().createNewItem((short) 1425);
            // }
            // break;
        }
        if (itemNew != null) {
            for (ItemOption io : item.itemOptions) {
                itemNew.itemOptions.add(io);
            }
            return itemNew;
        } else {
            return item;
        }

    }

    public boolean addItemBag(Player player, Item item) {
        try {

            item = converItem(item);
            // ngọc rồng đen
            if (ItemMapService.gI().isBlackBall(item.template.id)) {
                return BlackBallWar.gI().pickBlackBall(player, item);
            }   
            if (item.template.id == 453) {
                player.haveTennisSpaceShip = true;
                return true;
            }
            if (item.template.id == 74) {
                player.nPoint.setFullHpMp();
                PlayerService.gI().sendInfoHpMp(player);
                return true;
            }
            if (item.template.id == 516) {
                return true;
            }
            switch (item.template.type) {
                case 9:
                    if (player.inventory.gold + item.quantity <= player.inventory.getGoldLimit()) {
                        if (player.playerIntrinsic.intrinsic.id == 23) {
                            player.inventory.gold += player.nPoint.calPercent(item.quantity,
                                    player.playerIntrinsic.intrinsic.param1);
                        } else {
                            player.inventory.gold += item.quantity;
                        }
                        Service.getInstance().sendMoney(player);
                        return true;
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Vàng sau khi nhặt quá giới hạn cho phép");

                        return false;
                    }
                case 10:
                    player.inventory.gem += item.quantity;
                    Service.getInstance().sendMoney(player);
                    return true;
                case 34:
                    player.inventory.ruby += item.quantity;
                    Service.getInstance().sendMoney(player);
                    return true;
            }
            if (item.template.id == 457) {
                player.inventory.addGoldBar(item.quantity);
            }
            if (addItemSpecial(player, item)) {
                return true;
            }

            // gold, gem, ruby
            switch (item.template.type) {
                case 9:
                    if (player.inventory.gold + item.quantity <= 80) {
                        player.inventory.gold += item.quantity;
                        Service.getInstance().sendMoney(player);
                        return true;
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Vàng sau khi nhặt quá giới hạn cho phép");

                        return false;
                    }
                case 10:
                    player.inventory.gem += item.quantity;
                    Service.getInstance().sendMoney(player);
                    return true;
                case 34:
                    player.inventory.ruby += item.quantity;
                    Service.getInstance().sendMoney(player);
                    return true;
            }

            // mở rộng hành trang - rương đồ
            if (item.template.id == 517) {
                if (player.inventory.itemsBag.size() >= Inventory.BAG_LIMIT) {
                    Service.getInstance().sendThongBao(player,
                            "Hành trang của bạn đã đạt tối đa");
                    return false;
                } else {
                    player.inventory.itemsBag.add(ItemService.gI().createItemNull());
                    Service.getInstance().sendThongBao(player,
                            "Hành trang của bạn đã được mở rộng thêm 1 ô");

                    return true;

                }
            } else if (item.template.id == 518) {
                if (player.inventory.itemsBox.size() < 80) {
                    player.inventory.itemsBox.add(ItemService.gI().createItemNull());
                    Service.getInstance().sendThongBao(player,
                            "Rương đồ của bạn đã được mở rộng thêm 1 ô");

                    return true;
                } else {
                    Service.getInstance().sendThongBao(player, "Rương đồ của bạn đã đạt tối đa");
                    return false;
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
            Logger.logException(Player.class, e,
                    "Lỗi nhặt add item " + item.template.name + " của player - " + player.name);
        }
        return addItemList(player.inventory.itemsBag, item);

    }

    public byte getIndexBag(Player pl, Item it) {
        for (byte i = 0; i < pl.inventory.itemsBag.size(); ++i) {
            Item item = pl.inventory.itemsBag.get(i);
            if (item != null && it.equals(item)) {
                return i;
            }
        }
        return -1;
    }

    public boolean addItemBox(Player player, Item item) {
        return addItemList(player.inventory.itemsBox, item);
    }

    public boolean addItemBox_ct_pet(Player player, Item item) {
        return addItemList(player.inventory.itemsBox_ct_pet, item);
    }

    public boolean addItemList(List<Item> items, Item itemAdd) {
        try {
            // nếu item ko có option, add option rỗng vào
            if (itemAdd.itemOptions.isEmpty()) {
                itemAdd.itemOptions.add(new ItemOption(73, 0));
            }

            // item cộng thêm chỉ số param: tự động luyện tập
            int[] idParam = isItemIncrementalOption(itemAdd);
            if (idParam[0] != -1) {
                for (Item it : items) {
                    if (it.isNotNullItem() && it.template.id == itemAdd.template.id) {
                        for (ItemOption io : it.itemOptions) {
                            if (io.optionTemplate.id == idParam[0]) {
                                io.param += idParam[1];
                            }
                        }
                        return true;
                    }
                }
            }

            // item tăng số lượng
            if (itemAdd.template.isUpToUp) { // fix ở đây
                for (Item it : items) {
                    if (!it.isNotNullItem() || it.template.id != itemAdd.template.id) {
                        continue;
                    }
                    // 457-thỏi vàng; 590-bí kiếp
                    if (itemAdd.template.id == 457 || itemAdd.template.id == 590
                            || itemAdd.template.id == 610
                            || itemAdd.template.type == 14 || itemAdd.template.type == 12
                            || itemAdd.template.type == 35
                            || itemAdd.template.type == 27 || itemAdd.template.type == 30
                            || itemAdd.template.type == 31 || itemAdd.template.type == 29
                            || itemAdd.template.type == 6 || itemAdd.template.id == 361) {
                        if (itemAdd.template.type == 12 || itemAdd.template.type == 29
                                || itemAdd.template.type == 27) {
                            int count = 0;
                            for (ItemOption io : itemAdd.itemOptions) {
                                if (io.optionTemplate.id == 30) {
                                    count++;
                                    break;
                                }
                            }
                            for (ItemOption io : it.itemOptions) {
                                if (io.optionTemplate.id == 30) {
                                    count--;
                                    break;
                                }
                            }
                            if (count != 0) {
                                continue;
                            }
                        }
                        it.quantity += itemAdd.quantity;
                        itemAdd.quantity = 0;
                        return true;
                    }
                    if (itemAdd.template.id == 570
                            || itemAdd.template.id == 571
                            || itemAdd.template.id == 572) { // rương
                        // gỗ
                        int level_chest_1 = 0;
                        int level_chest_2 = 0;
                        for (ItemOption io : itemAdd.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level_chest_1 = io.param;
                                break;
                            }
                        }
                        for (ItemOption io : it.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level_chest_2 = io.param;
                                break;
                            }
                        }
                        if (level_chest_1 != level_chest_2) {
                            continue;
                        }
                    }

                    if (it.quantity < 99) {
                        int add = 99 - it.quantity;
                        if (itemAdd.quantity <= add) {
                            it.quantity += itemAdd.quantity;
                            itemAdd.quantity = 0;
                            return true;
                        } else {
                            it.quantity = 99;
                            itemAdd.quantity -= add;
                        }
                    }
                }
            }

            // add item vào ô mới
            if (itemAdd.quantity > 0) {
                for (int i = 0; i < items.size(); i++) {
                    if (!items.get(i).isNotNullItem()) {
                        items.set(i, ItemService.gI().copyItem(itemAdd));
                        itemAdd.quantity = 0;
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.getStackTrace();
            Logger.logException(InventoryServiceNew.class, e, "Lỗi nhặt add item ");
            return false;
        }
    }

    private void __________________Kiểm_tra_điều_kiện_vật_phẩm______________() {
        // **********************************************************************
    }

    /**
     * Kiểm tra vật phẩm có phải là vật phẩm tăng chỉ số option hay không
     *
     * @param item
     * @return id option tăng chỉ số - param
     */
    private int[] isItemIncrementalOption(Item item) {
        for (ItemOption io : item.itemOptions) {
            switch (io.optionTemplate.id) {
                case 1:
                    return new int[]{io.optionTemplate.id, io.param};
            }
        }
        return new int[]{-1, -1};
    }

    private void __________________Kiểm_tra_danh_sách_còn_chỗ_trống_________() {
        // **********************************************************************
    }

    public byte getCountEmptyBag(Player player) {
        return getCountEmptyListItem(player.inventory.itemsBag);
    }

    public byte getCountEmptyListItem(List<Item> list) {
        byte count = 0;
        for (Item item : list) {
            if (!item.isNotNullItem()) {
                count++;
            }
        }
        return count;
    }
}
