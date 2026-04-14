package nro.services;

import nro.consts.ConstItem;
import nro.models.item.DataShopReward;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.war.BlackBallWar;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.npc.specialnpc.MagicTree;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.utils.Log;
import nro.utils.TimeUtil;
import nro.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import nro.sendEff.SendEffect;

public class InventoryService {

    private static InventoryService i;

    public static InventoryService gI() {
        if (i == null) {
            i = new InventoryService();
        }
        return i;
    }

    public List<Item> copyItemsBag(Player player) {
        return copyList(player.inventory.itemsBag);
    }

    private List<Item> copyList(List<Item> items) {
        List<Item> list = new ArrayList<>();
        for (Item item : items) {
            list.add(ItemService.gI().copyItem(item));
        }
        return list;
    }

    public boolean existItemBag(Player player, int tempId) {
        return existItemInList(player.inventory.itemsBag, tempId);
    }

    private boolean existItemInList(List<Item> list, int tempId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isNotNullItem() && list.get(i).template.id == tempId) {
                return true;
            }
        }
        return false;
    }

    public boolean addItemBag(Player player, Item item, int maxQuantity) {
        return InventoryServiceNew.gI().addItemBag(player, item);
    }

    public boolean addItemBag(Player player, Item item) {
        return InventoryServiceNew.gI().addItemBag(player, item);
    }

    public boolean addItemBox(Player player, Item item, int maxQuantity) {
        return InventoryServiceNew.gI().addItemList(player.inventory.itemsBox, item);
    }

    public boolean addItemBox_ct_pet(Player player, Item item, int maxStack) {
        for (Item boxItem : player.inventory.itemsBox_ct_pet) {
            if (boxItem.isNotNullItem() && boxItem.template.id == item.template.id && boxItem.quantity < maxStack) {
                int addQuantity = Math.min(item.quantity, maxStack - boxItem.quantity);
                boxItem.quantity += addQuantity;
                item.quantity -= addQuantity;
                return true;
            }
        }

        for (int i = 0; i < player.inventory.itemsBox_ct_pet.size(); i++) {
            if (!player.inventory.itemsBox_ct_pet.get(i).isNotNullItem()) {
                Item newItem = ItemService.gI().copyItem(item);
                newItem.quantity = Math.min(item.quantity, maxStack);
                player.inventory.itemsBox_ct_pet.set(i, newItem);
                item.quantity -= newItem.quantity;
                return true;
            }
        }

        return false;
    }

    public boolean addItemBody(Player player, Item item, int maxQuantity) {
        return addItemList(player.inventory.itemsBody, item, maxQuantity);
    }

    public boolean hasOptionTemplateId(Item item, int optionTemplateId) {
        for (ItemOption option : item.itemOptions) {
            if (option.optionTemplate.id == optionTemplateId) {
                return true;
            }
        }
        return false;
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

    public boolean addItemNotUpToUpQuantity(List<Item> items, Item item) {
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isNotNullItem()) {
                items.set(i, item);
                return true;
            }
        }
        return false;
    }

    public Item findItemBag(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBag, tempId);
    }

    public boolean addItemList(List<Item> items, Item item, int maxQuantity) {
        return InventoryServiceNew.gI().addItemList(items, item);
    }

    private boolean isItemIncremental(Item item) { // item cộng dồn số lượng
        switch (item.template.type) {
            case 8: // vật phẩm nhiệm vụ
            case 12: // ngọc rồng
            case 30: // sao pha lê
            case 6: // đậu thần
            case 29: // item time, đồ ăn
            case 25: // rađa dò ngọc namếc
            case 27: // đồ tạp
            case 33: // mảnh rada
            case 14: // đá nâng cấp
            case 50: // vé đổi đồ hủy diệt
            case 75:
                // case 31: // item 31 coongj doonf
                // if (iItem_up_to(item.template.id)) {
                // return false;
                // }
                return true;
            default:
                return false;
        }
    }

    public boolean iItem_up_to(int id) {
        switch (id) {
            case 1994:
            case 1995:
            case 1996:
                return true;
        }
        return false;
    }

    private byte isItemIncrementalOption(Item item) { // trả về id option template
        int temp = item.template.id;
        byte opp = -1;
        switch (temp) {
            case 521:
                opp = 1;
                break;
            default:
                break;

        }
        return opp;
    }

    public void CheckAndRestShopTime(Player player) {
        if (TimeUtil.isYesterday(player.inventory.time_buy_shop_today)) {
            // Rest lại phần thưởng mỗi ngày và nhận thưởng điểm danh
            // Log.warning("Nhan qua diem danh thanh cong");
            // cửa hàng miễn phí
            player.inventory.time_buy_shop_today = System.currentTimeMillis();
            if (player.inventory.free_turn_buy_shop == 0) {
                player.inventory.free_turn_buy_shop += 1;
            }
            // reset nhận quà phút
            player.inventory.timeOnline = 0;
            for (DataShopReward shopDay : player.inventory.dShopTimes) {
                shopDay.isBuy = false;
                shopDay.tookAttendance = false;
            }
            // reset quà nhiệm vụ hằng ngày
            player.inventory.sideTaskToDay = 0;
            for (DataShopReward shopTask : player.inventory.dShopTasks) {
                shopTask.isBuy = false;
                shopTask.tookAttendance = false;
            }
            // cửa hàng điểm danh
            for (DataShopReward shopDay : player.inventory.dShopDays) {
                if (!shopDay.tookAttendance) {
                    shopDay.tookAttendance = true;
                    break;
                }
            }

        }
    }

    public void addItemGiftCodeToPlayer(Player p, HashMap<Integer, Integer> Items) {

        Set<Integer> keySet = Items.keySet();
        String textGift = "Bạn vừa nhận được:\b";
        for (Integer key : keySet) {
            int idItem = key;
            int quantity = Items.get(key);
            if (idItem == -1) {
                p.inventory.gold = Math.min(p.inventory.gold + (long) quantity, 1L);
                textGift += quantity + " vàng\b";
            } else if (idItem == -2) {
                p.inventory.gem = Math.min(p.inventory.gem + quantity, 1);
                textGift += quantity + " ngọc\b";
            } else if (idItem == -3) {

            } else {
                Item itemGiftTemplate = ItemService.gI().createNewItem((short) idItem);
                if (itemGiftTemplate != null) {
                    Item itemGift = ItemService.gI().createNewItem((short) idItem);
                    ItemService.gI().OptionAllItem(itemGift, 0);
                    if (itemGift.template.type <= 5 || itemGift.template.type == 29 || itemGift.template.type == 27
                            || itemGift.template.type == 14 || itemGift.template.type == 12) {
                        itemGift.itemOptions.add(new ItemOption(30, 1));
                    }

                    itemGift.quantity = quantity;
                    addItemBag(p, itemGift, 1);
                    textGift += "x" + quantity + " " + itemGift.template.name + "\b";
                }
            }
        }
        sendItemBags(p);
        Service.getInstance().sendThongBaoFromAdmin(p, textGift);
    }

    public void throwItem(Player player, int where, int index) {
        Item itemThrow = null;
        if (where == 0) {
            if (index >= 0 && index <= player.inventory.itemsBody.size()) {
                itemThrow = player.inventory.itemsBody.get(index);
                if (itemThrow.isNotNullItem()) {
                    removeItemBody(player, index);
                    sendItemBody(player);
                }
            }
        } else if (where == 1) {
            if (index >= 0 && index <= player.inventory.itemsBag.size()) {
                itemThrow = player.inventory.itemsBag.get(index);
                if (itemThrow.isNotNullItem()) {
                    if (itemThrow.template.id == 457 || itemThrow.template.id == 1429) {
                        Service.getInstance().sendThongBao(player, "Không thể vứt vật phẩm");
                    } else {
                        removeItemBag(player, index);
                        sortItemBag(player);
                        sendItemBags(player);
                    }
                }
            }
        }
        if (!itemThrow.isNotNullItem()) {
            return;
        } else {
            Service.getInstance().point(player);
        }

        // ItemMap itemMap = new ItemMap(player.map, itemThrow.template.id,
        // itemThrow.quantity, player.location.x, player.location.y, player.id);
        // itemMap.options = itemThrow.itemOptions;
        // Service.getInstance().dropItemMap(player.map, itemMap);
        Service.getInstance().Send_Caitrang(player);
    }

    public void arrangeItems(List<Item> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (!list.get(i).isNotNullItem()) {
                int indexSwap = -1;
                for (int j = i + 1; j < list.size(); j++) {
                    if (list.get(j).isNotNullItem()) {
                        indexSwap = j;
                        break;
                    }
                }
                if (indexSwap != -1) {
                    Item sItem = ItemService.gI().createItemNull();
                    list.set(i, list.get(indexSwap));
                    list.set(indexSwap, sItem);
                } else {
                    break;
                }
            }
        }
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

    private Item putItemBox_pet_ct(Player player, Item item) {
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
        byte type = item.getType();
        if (item.isNotNullItem()) {
            if (type >= 0 && type <= 5 || type == 32 || type == 11 || type == 23 || type == 39 || type == 24 || type == 99 || type == 73 || type == 74
                    || type == 98) {
                if (item.template.gender == player.gender || item.template.gender == 3) {
                    if (item.getId() == ConstItem.QUAN_DI_BIEN) {
                        List<Item> itemsBody = player.inventory.itemsBody;
                        if (itemsBody.get(0).isNotNullItem() && itemsBody.get(5).isNotNullItem()) {
                            Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                                    "Vui lòng cởi áo để có thể sử dụng!");
                            return sItem;
                        }
                    }
                    boolean forPet = false;
                    long powerRequire = item.template.strRequire;
                    for (ItemOption io : item.itemOptions) {
                        if (io.optionTemplate.id == 21) {
                            powerRequire = io.param * 1000000000L;
                            break;
                        } else if (io.optionTemplate.id == 208) {
                            forPet = true;
                        }
                    }
                    if ((item.template.id == 548 || item.template.id == 547 || item.template.id == 1262)
                            && !player.isPet) {
                        forPet = true;
                    }
                    if (forPet && !player.isPet) {
                        Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                                "Trang bị chỉ dành cho đệ tử!");
                    } else if (player.isPet && type > 6 && type != 32 && type != 11 && type != 39) {
                        Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                                "Trang bị không phù hợp với đệ tử!");
                    } else if (player.isPet && type == 11 && !forPet) {
                        Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                                "Đeo lưng phải có chỉ số cho đệ tử !!");
                    } else if (powerRequire <= player.nPoint.power) {
                        byte index = 0;
                        switch (type) {
                            case 11:
                                index = 7;
                                break;
                            case 23:
                            case 24:
                                index = 8;
                                break;
                            case 32:
                                index = 6;
                                break;
                            case 98:
                                index = 9;
                                break;
                            case 99:
                                index = 10;
                                break;
                            case 74:
                                index = 11;
                                break;
                            case 73:
                                index = 12;
                                break;
                            case 39:
                                if (!player.isPet) {
                                    index = 13; // Nếu không phải pet, index = 13
                                } else {
                                    index = 8;  // Nếu là pet, index = 8
                                }
                                break;
                            default:
                                index = type;
                        }

                        sItem = player.inventory.itemsBody.get(index);
                        player.inventory.itemsBody.set(index, item);
                    } else {
                        Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                                "Sức mạnh không đủ yêu cầu!");
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                            "Trang bị không phù hợp với hành tinh!");
                }
            } else {
                Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player,
                        "Trang bị không phù hợp!");
            }
        }
        return sItem;
    }

    // ==========================================================================
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
            SendEffect.getInstance().removeTitle(player);
            Service.getInstance().point(player);
            Service.getInstance().sendFlagBag(player);
        }
    }

    public void itemBodyToBag(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBody.size()) {
            return;
        }
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBody.set(index, putItemBag(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player);
            SendEffect.getInstance().removeTitle(player);
            Service.getInstance().point(player);
            Service.getInstance().sendFlagBag(player);
        }
    }

    public void itemBagToPetBody(Player player, int index) {
        if (player.pet != null && player.pet.nPoint.power >= 1500000) {
            if (index < 0 || index >= player.inventory.itemsBag.size()) {
                return;
            }
            Item item = player.inventory.itemsBag.get(index);
            if (item.isNotNullItem()) {
                Item itemSwap = putItemBody(player.pet, item);
                player.inventory.itemsBag.set(index, itemSwap);
                sendItemBags(player);
                sendItemBody(player);
                Service.getInstance().Send_Caitrang(player.pet);
                Service.getInstance().sendFlagBagPet(player.pet);
                Service.getInstance().Send_Caitrang(player);
                if (!itemSwap.equals(item)) {
                    Service.getInstance().point(player);
                    Service.getInstance().showInfoPet(player);
                }
            }
        } else {
            Service.getInstance().sendThongBaoOK(player, "Đệ tử phải đạt 1tr5 sức mạnh mới có thể mặc");
        }
    }

    public void itemPetBodyToBag(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBody.size()) {
            return;
        }
        Item item = player.pet.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.pet.inventory.itemsBody.set(index, putItemBag(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player.pet);
            Service.getInstance().sendFlagBagPet(player.pet);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
            Service.getInstance().showInfoPet(player);
        }
    }

    // --------------------------------------------------------------------------
    public void itemBoxToBodyOrBag(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBox.size()) {
            return;
        }
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
                            Service.getInstance().point(player);
                        }
                    }
                }
            }
            if (!done) {
                if (addItemBag(player, item, 99)) {
                    if (item.quantity == 0) {
                        Item sItem = ItemService.gI().createItemNull();
                        player.inventory.itemsBox.set(index, sItem);
                    }
                    sendItemBags(player);
                }
            }
            sendItemBox(player);
        }
    }

    // --------------------------------------------------------------------------
    public void itemBoxToBodyOrBag_ct_pet(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBox_ct_pet.size()) {
            return;
        }
        Item item = player.inventory.itemsBox_ct_pet.get(index);
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
                            player.inventory.itemsBox_ct_pet.set(index, itemBody);
                            done = true;

                            sendItemBody(player);
                            Service.getInstance().Send_Caitrang(player);
                            Service.getInstance().point(player);
                        }
                    }
                }
            }
            if (!done) {
                if (addItemBag(player, item, 99)) {
                    if (item.quantity == 0) {
                        Item sItem = ItemService.gI().createItemNull();
                        player.inventory.itemsBox_ct_pet.set(index, sItem);
                    }
                    sendItemBags(player);
                }
            }
            sendItemBox_pet_ct(player);
        }
    }

    public void itemBagToBox(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBag.size()) {
            return;
        }
        Item item = player.inventory.itemsBag.get(index);
        if (item.isNotNullItem()) {
            if (addItemBox(player, item, 99)) {
                if (item.quantity == 0) {
                    Item sItem = ItemService.gI().createItemNull();
                    player.inventory.itemsBag.set(index, sItem);
                }
                arrangeItems(player.inventory.itemsBag);
                sendItemBags(player);
                sendItemBox(player);
//                System.err.println("đưa item vô ok");
            }
        }
    }

    public void itemBodyToBox(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBody.size()) {
            return;
        }
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBody.set(index, putItemBox(player, item));
            arrangeItems(player.inventory.itemsBag);
            sendItemBody(player);
            sendItemBox(player);
            Service.getInstance().Send_Caitrang(player);
            sendItemBody(player);
            Service.getInstance().point(player);
        }
    }

    public void itemBagToBox_ct_pet(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBag.size()) {
            Service.getInstance().sendThongBao(player, "Vị trí không hợp lệ!");
            return;
        }
        Item item = player.inventory.itemsBag.get(index);
        if (!item.isNotNullItem()) {
            Service.getInstance().sendThongBao(player, "Vật phẩm không hợp lệ!");
            return;
        }
        if (item.template.type != 5 && item.template.type != 23 && item.template.type != 24 && item.template.type != 98) {
            Service.getInstance().sendThongBao(player, "Chỉ chứa pet và cải trang ván bay , hạn sử dụng là vĩnh viễn!!");
            return;
        }
        // Kiểm tra nếu vật phẩm có option id 249 (mua bằng ngọc xanh)
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 249) {
                Service.getInstance().sendThongBao(player, "Vật phẩm mua bằng ngọc xanh không thể thêm vào rương!!");
                return;
            }
        }
        if (addItemBox_ct_pet(player, item, 99)) {
            if (item.quantity == 0) {
                Item nullItem = ItemService.gI().createItemNull();
                player.inventory.itemsBag.set(index, nullItem);
            }
            arrangeItems(player.inventory.itemsBag);
            sendItemBags(player);
            sendItemBox_pet_ct(player);
//            System.err.println("Trend itemsBox_ct_pet thành công");
        } else {
            Service.getInstance().sendThongBao(player, "Rương sưu tầm đã đầy hoặc không thể thêm vật phẩm!");
        }
    }

    public void itemBodyToBox_ct_pet(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBody.size()) {
            return;
        }
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            if (item.template.type != 5 && item.template.type != 23 && item.template.type != 24 && item.template.type != 98) {
                Service.getInstance().sendThongBao(player, "Chỉ chứa pet và cải trang ván bay , hạn sử dụng là vĩnh viễn!!");
                return;
            }
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 249) {
                    Service.getInstance().sendThongBao(player, "Vật phẩm mua bằng ngọc xanh không thể thêm vào rương!");
                    return;
                }
            }
            player.inventory.itemsBody.set(index, putItemBox_pet_ct(player, item));
            arrangeItems(player.inventory.itemsBag);
            sendItemBody(player);
            sendItemBox_pet_ct(player);
            Service.getInstance().Send_Caitrang(player);
            sendItemBody(player);
            Service.getInstance().point(player);
//            System.err.println("body to box_pet_ct");
        }
    }

    // --------------------------------------------------------------------------
    public void subQuantityItemsBag(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBag, item, quantity);
    }

    public void subQuantityItemsBag(Player player, short itemId, int quantity) {
        for (Item item : player.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && item.template.id == itemId)
                .collect(Collectors.toList())) {
            final int quantityItem = item.quantity;
            if (quantity > 0) {
                subQuantityItem(player.inventory.itemsBag, item, Math.min(quantity, quantityItem));
                quantity -= Math.min(quantity, quantityItem);
            }
            return;
        }
    }

    public void subQuantityItemsBagById(Player player, int itemId, int quantity) {
    try {
        for (Item item : player.inventory.itemsBag) {
            if (item != null && item.isNotNullItem() && item.template.id == itemId) {
                if (item.quantity > quantity) {
                    item.quantity -= quantity;
                    quantity = 0;
                } else {
                    quantity -= item.quantity;
                    item.quantity = 0;
                }
                if (quantity <= 0) break;
            }
        }
        InventoryService.gI().sendItemBags(player);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public boolean subQuantityItemsBagGoldBar(Player player, short itemId, int quantity) {
        try {
            List<Item> itemsToRemove = player.inventory.itemsBag.stream()
                    .filter(item -> item.isNotNullItem() && item.template.id == itemId)
                    .collect(Collectors.toList());

            for (Item item : itemsToRemove) {
                int quantityItem = item.quantity;
                if (quantity > 0) {
                    int quantityToRemove = Math.min(quantity, quantityItem);
                    subQuantityItem(player.inventory.itemsBag, item, quantityToRemove);
                    quantity -= quantityToRemove;
                }
            }

            return true;
        } catch (NullPointerException | IndexOutOfBoundsException | ConcurrentModificationException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void subQuantityItemsBody(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBody, item, quantity);
    }

    public void subQuantityItem(List<Item> items, Item item, int quantity) {
        if (item != null) {
            for (Item it : items) {
                // if (it.isNotNullItem() && it.template.id == item.template.id) {
                if (item.equals(it)) {
                    it.quantity -= quantity;
                    if (it.quantity <= 0) {
                        removeItem(items, item);
                    }
                    break;
                }
            }
        }
    }

    public void subQuantityItemsBox(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBox, item, quantity);
    }

    public void sortItemBag(Player player) {
        sortItem(player.inventory.itemsBag);
    }

    public void sortItem(List<Item> items) {
        int index = 0;
        for (Item item : items) {
            if (item.isNotNullItem()) {
                items.set(index, item);
                index++;
            }
        }
        for (int i = index; i < items.size(); i++) {
            Item item = ItemService.gI().createItemNull();
            items.set(i, item);
        }
    }

    // --------------------------------------------------------------------------
    public void removeItem(List<Item> items, int index) {
        Item item = ItemService.gI().createItemNull();
        items.set(index, item);
    }

    public void removeItem(List<Item> items, Item item) {
        Item it = ItemService.gI().createItemNull();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                items.set(i, it);
                break;
            }
        }
    }

    public void removeItemBag(Player player, int index) {
        removeItem(player.inventory.itemsBag, index);
    }

    public void removeItemBag(Player player, Item item) {
        removeItem(player.inventory.itemsBag, item);
    }

    public void removeItemBody(Player player, int index) {
        removeItem(player.inventory.itemsBody, index);
    }

    public void removeItemPetBody(Player player, int index) {
        removeItemBody(player.pet, index);
    }

    public void removeItemBox(Player player, int index) {
        removeItem(player.inventory.itemsBox, index);
    }

    public Item findItem(List<Item> list, int tempId) {
        for (Item item : list) {
            if (item.isNotNullItem() && item.template.id == tempId) {
                return item;
            }
        }
        return null;
    }

    /// item sự kiện
    public Item findVeTangNgoc(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2023)) {
                return item;
            }
        }
        return null;
    }

    public Item findBuaBaoVeNangCap(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 987) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuKeo(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2013) && item.quantity >= 10) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuBanh(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2014) && item.quantity >= 10) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuBingo(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2015) && item.quantity >= 10) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuGiokeo(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2016) && item.quantity >= 3) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuVe(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2018) && item.quantity >= 3) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuHopmaquy(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2017) && item.quantity >= 3) {
                return item;
            }
        }
        return null;
    }

    public Item finditemBongHoa(Player player, int soluong) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 589) && item.quantity >= soluong) {
                return item;
            }
        }
        return null;
    }

    public Item finditemVo(Player player, int soluong) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 649) && item.quantity >= soluong) {
                return item;
            }
        }
        return null;
    } 
    
    public boolean finditemWoodChest(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        for (Item item : player.inventory.itemsBox) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        return true;
    }

    public Item finditemKeoGiangSinh(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 2026) {
                return item;
            }
        }
        return null;
    }

    public Item findItem(Player player, int id, int quantity) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == id) && item.quantity >= quantity) {
                return item;
            }
        }
        return null;
    }

    public Item findItemBagByTemp(Player player, int tempId) {
        return findItem(player.inventory.itemsBag, tempId);
    }

    public List<Item> getListItem(Player player, int... items) {
        return player.inventory.itemsBag.stream().filter(i -> in(i, items)).collect(Collectors.toList());
    }

    private boolean in(Item item, int... items) {
        return IntStream.of(items).anyMatch(id -> (item.isNotNullItem() && item.template.id == id));
    }

    public Item findMealChangeDestroyClothes(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id >= 663 && item.template.id <= 667)
                    && item.quantity >= 99) {
                return item;
            }
        }
        return null;
    }

    public Item findItemThanLinh(Player player, byte type) {
        if (player.inventory.itemsBody != null) {
            for (Item item : player.inventory.itemsBody) {
                if (item.isNotNullItem() && item.template.type == type && (item.template.id >= 555 && item.template.id <= 567)) {
                    return item;
                }
            }
        }
        return null;
    }

    public Item findTicketChangeDestroyClothes(Player player, byte typeClothe) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 50 && item.template.id == (2001 + typeClothe)
                    && item.quantity >= 99) {
                return item;
            }
        }
        return null;
    }

    public Item findGodClothesByType(Player player, int type) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == type
                    && (item.template.id >= 555 && item.template.id <= 567)) {
                return item;
            }
        }
        return null;
    }

    // Thiên sứ
    public Item findMealChangeAngleClothes(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 1084
                    && item.quantity >= 99) {
                return item;
            }
        }
        return null;
    }

    // --------------------------------------------------------------------------
    public void sendItemBags(Player player) {
        arrangeItems(player.inventory.itemsBag);
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
                List<ItemOption> itemOptions = item.getDisplayOptions();
                msg.writer().writeByte(itemOptions.size()); // options
                for (ItemOption o : itemOptions) {
                    msg.writer().writeByte(o.optionTemplate.id);
                    msg.writer().writeShort(o.param);
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
                    List<ItemOption> itemOptions = item.getDisplayOptions();
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

        if (player.isPl()) {
            if (player.inventory.itemsBody.get(11).isNotNullItem()) {
                if (!player.isChanthientu) {
                    player.isChanthientu = true;
                    SendEffect.getInstance().sendChanThienTu(player, (short) player.inventory.itemsBody.get(11).template.id);
                }
            } else {
                if (player.isChanthientu) {
                    player.isChanthientu = false;
                    SendEffect.getInstance().sendChanThienTu(player, (short) 0);
                }
            }
        }
        if (player.isPl()) {
            if (player.inventory.itemsBody.get(12).isNotNullItem()) {
                if (!player.isDanhhieu) {
                    player.isDanhhieu = true;
                    SendEffect.getInstance().sendDanhhieu(player, (short) player.inventory.itemsBody.get(12).template.id);
                }
            } else {
                if (player.isDanhhieu) {
                    player.isDanhhieu = false;
                    SendEffect.getInstance().sendDanhhieu(player, (short) 0);
                }
            }
        }
        Service.getInstance().Send_Caitrang(player);
    }

    public void openBox(Player player) {
        Message msg;
        try {
            player.activeBoxType = 1; // Đánh dấu mở rương itemsBox
            msg = new Message(-35);
            msg.writer().writeByte(1); // Mở giao diện
            //  msg.writer().writeByte(0); // Loại rương: 0 = itemsBox
            player.sendMessage(msg);
            msg.cleanup();

            sendItemBox(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendItemBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0); // Gửi danh sách vật phẩm
            //      msg.writer().writeByte(0); // Loại rương: 0 = itemsBox
            msg.writer().writeByte(player.inventory.itemsBox.size());
            for (Item it : player.inventory.itemsBox) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    List<ItemOption> itemOptions = it.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption io : itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openBox_pet_ct(Player player) {
        Message msg;
        try {
            player.activeBoxType = 2; // Đánh dấu mở rương itemsBox_ct_pet
            msg = new Message(-35);
            msg.writer().writeByte(1); // Mở giao diện
            //   msg.writer().writeByte(1); // Loại rương: 1 = itemsBox_ct_pet
            player.sendMessage(msg);
            msg.cleanup();

            sendItemBox_pet_ct(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendItemBox_pet_ct(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0); // Gửi danh sách vật phẩm
            //  msg.writer().writeByte(1); // Loại rương: 1 = itemsBox_ct_pet
            msg.writer().writeByte(player.inventory.itemsBox_ct_pet.size());
            for (Item it : player.inventory.itemsBox_ct_pet) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    List<ItemOption> itemOptions = it.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption io : itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            if (Util.canDoWithTime(player.lastTimeEatPea, 9000)) {
                player.lastTimeEatPea = System.currentTimeMillis();
                int hpKiHoiPhuc = 0;
                int lvPea = 0;
                if (pea.template.id <= 595) {
                    lvPea = Integer.parseInt(pea.template.name.substring(13));
                    for (ItemOption io : pea.itemOptions) {
                        if (io.optionTemplate.id == 2) {
                            hpKiHoiPhuc = io.param * 1000;
                            break;
                        }
                        if (io.optionTemplate.id == 48) {
                            hpKiHoiPhuc = io.param;
                            break;
                        }
                    }
                } else {
                    hpKiHoiPhuc = 2_000_000_000;
                    lvPea = 10;
                }

                player.nPoint.setHp(player.nPoint.addInt(player.nPoint.hp, hpKiHoiPhuc));
                player.nPoint.setMp(player.nPoint.addInt(player.nPoint.mp, hpKiHoiPhuc));
                PlayerService.gI().sendInfoHpMp(player);
                Service.getInstance().sendInfoPlayerEatPea(player);
                if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                    int statima = 100 * lvPea;
                    player.pet.nPoint.stamina += statima;
                    if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                        player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                    }
                    player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
                    player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
                    Service.getInstance().sendInfoPlayerEatPea(player.pet);
                    Service.getInstance().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
                }
                subQuantityItemsBag(player, pea, 1);
                sendItemBags(player);
            }
        }
    }

    public int addPeaHarvest(Player player, byte level, int quantity) {
        Item pea = ItemService.gI().createNewItem(MagicTree.PEA_TEMP[level - 1], quantity);
        pea.itemOptions.add(new ItemOption(level - 1 > 1 ? 2 : 48, MagicTree.PEA_PARAM[level - 1]));
        addItemBag(player, pea, 99999);
        if (pea.quantity > 0) {
            addItemBox(player, pea, 99999);
        }
        if (pea.quantity < quantity) {
            Service.getInstance().sendThongBao(player,
                    "Bạn vừa thu hoạch được " + (quantity - pea.quantity) + " hạt " + pea.template.name);
        }
        return pea.quantity;
    }

    public Item getPeaBox(Player player) {
        for (Item item : player.inventory.itemsBox) {
            if (item.isNotNullItem() && item.template.type == 6) {
                return item;
            }
        }
        return null;
    }

    private byte getNumPeaBag(Player player) {
        return getNumPea(player.inventory.itemsBag);
    }

    private byte getNumPeaBox(Player player) {
        return getNumPea(player.inventory.itemsBox);
    }

    private byte getNumPea(List<Item> items) {
        byte num = 0;
        for (Item item : items) {
            if (item.isNotNullItem() && item.template.type == 6) {
                num += item.quantity;
            }
        }
        return num;
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

    public String itemsBagToString(Player player) {
        JSONArray dataBag = new JSONArray();
        for (Item item : player.inventory.itemsBag) {
            JSONObject dataItem = new JSONObject();
            if (item.isNotNullItem()) {
                JSONArray options = new JSONArray();
                dataItem.put("temp_id", item.template.id);
                dataItem.put("quantity", item.quantity);
                for (ItemOption io : item.itemOptions) {
                    JSONArray option = new JSONArray();
                    option.add(io.optionTemplate.id);
                    option.add(io.param);
                    options.add(option);
                }
                dataItem.put("option", options);
            } else {
                JSONArray options = new JSONArray();
                dataItem.put("temp_id", -1);
                dataItem.put("quantity", 0);
                dataItem.put("create_time", 0);
                dataItem.put("option", options);
            }
            dataBag.add(dataItem);
        }
        String itemsBag = dataBag.toJSONString();
        return itemsBag;
    }

    public Item findItemBagByIndex(Player player, int index) {
        if (player.inventory.itemsBag.get(index).isNotNullItem()) {
            return player.inventory.itemsBag.get(index);
        }
        return null;
    }

    public int getQuantity(Player player, int itemID) {
        Item item = findItem(player.inventory.itemsBag, itemID);
        if (item == null) {
            return -1;
        }
        return item.quantity;
    }

    public int getQuantityItemBagById(Player player, int itemId) {
    int quantity = 0;
    for (Item item : player.inventory.itemsBag) {
        if (item != null && item.isNotNullItem() && item.template.id == itemId) {
            quantity += item.quantity;
        }
    }
    return quantity;
}

}
