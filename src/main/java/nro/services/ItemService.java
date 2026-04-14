package nro.services;

import nro.models.item.ItemOptionTemplate;
import nro.models.item.ItemTemplate;
import nro.consts.ConstOption;
import nro.models.consignment.ConsignmentItem;
import nro.models.item.Item;
import nro.models.item.ItemLuckyRound;
import nro.models.item.ItemOption;
import nro.models.item.ItemOptionLuckyRound;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.shop.ItemShop;
import nro.server.Manager;
import nro.utils.Log;
import nro.utils.Logger;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemService {

    private final short THOI_VANG_ID = 457;
    private final short HONG_NGOC_ID = 861;
    public static final int TDST_NM_BOSS = 1;
    public static final int BOJACK_BOSS = 2;
    public static final int FUTURE_BOSS = 3;
    public static final int BIG_MOB_1 = 4;
    public static final int RW_EVENT = 5;
    public static final int COLD = 6;
    private static final short LIST_ITEM_TDST[] = {17, 17, 17};
    private static final short LIST_ITEM_BOJACK[] = {17, 17, 17};
    private static final short LIST_ITEM_FUTURE[] = {17, 17, 17};
    private static final short LIST_ITEM_BIG_MOB_1[] = {17, 17, 17};
    private static final short LIST_ITEM_EVENT[] = {17, 17, 17};
    private static final short LIST_COLD[] = {17, 17, 17};
    private static final short LIST_HUY_DIET[] = {17, 17, 17};
    private static final short LIST_DAI_HAI_TRINH[] = {457};
    private static final Map<Integer, short[]> phuKienMap = new HashMap<>();
    private static final Map<Integer, short[]> phukienlinhtinh = new HashMap<>();

    // Khởi tạo danh sách phụ kiện cho từng type
    static {
        // Cải trang
        phuKienMap.put(5,
                new short[]{386, 387, 388, 389, 390, 391, 392, 393, 394, 421, 422, 463, 642, 643, 675, 676, 677, 678,
                    679, 680, 681, 730, 731, 732, 739, 827, 860, 883, 898, 906, 937, 948, 952, 953, 957, 958, 959,
                    985, 989, 990, 991, 2067});
        // Vật phẩm đeo lưng
        phuKienMap.put(11,
                new short[]{467, 468, 469, 470, 471, 740, 741, 745, 800, 801,
                    802, 803, 804, 805, 814, 815, 816, 817, 822, 823, 852, 865,
                    954, 955, 966, 982, 983, 999, 1021,
                    1022, 1023, 1028, 1030, 1031, 1047,
                    1100, 1108, 1109, 1110, 1111, 1128, 1129, 1130,
                    1137, 1138, 1139, 1140, 1142, 1149, 1158, 1159, 1160, 1161,
                    1162, 1163, 1164, 1185, 1186, 1197, 1206, 1258, 1276, 1289,
                    1290, 1349, 1350, 1415});
        // Ván bay
        phuKienMap.put(23,
                new short[]{733, 734, 735, 743, 744, 746, 795, 849, 897, 920, 1092, 1131, 1172, 1252, 1253, 1267,
                    1268, 1269, 1270, 1322,
                    1413, 1414, 532, 1292, 1293, 1294});
        // Pet đi theo
        phuKienMap.put(98,
                new short[]{892, 893, 916, 917, 918, 919, 936, 942, 943, 944, 967, 1039, 1040, 1046, 1188, 1202, 1203,
                    1207,
                    1243, 1244, 1259, 1265, 1271, 1336});
        // item linh tinh
        phukienlinhtinh.put(27,
                new short[]{381, 382, 383, 384, 933, 934, 935, 1150, 1152, 1153, 1151, 1517, 1518});
        // Pet sau lưng
        phuKienMap.put(99, new short[]{1272, 1273, 1278, 1279, 1280, 1281, 1282, 1283, 1284, 1285, 1286, 1287, 1992,
            1993, 2055, 2056, 2059, 2060});
    }

    public static ItemService i;

    public static ItemService gI() {
        if (i == null) {
            i = new ItemService();
        }
        return i;
    }

    public Item createItemNull() {
        Item item = new Item();
        return item;
    }

    public Item createItemFromItemShop(ItemShop itemShop) {
        Item item = new Item();
        item.template = itemShop.temp;
        item.quantity = 1;
        item.content = item.getContent();
        item.info = item.getInfo();
        for (ItemOption io : itemShop.options) {
            if (io.optionTemplate.id == 31) {// số lương
                item.quantity = io.param;
                continue;
            }
            if (io.optionTemplate.id == 199) {// chỉ số hiển thị
                continue;
            }
            if (io.optionTemplate.id == 200) {// random vĩnh viễn
                if (Util.isTrue(io.param, 100)) {
                    item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 3)));
                }

                continue;
            }
            item.itemOptions.add(new ItemOption(io));
        }
        return item;
    }

    public Item createItemFromItemLucky(int id_item) {
        Item it = null;
        ItemLuckyRound item = null;
        for (ItemLuckyRound items : Manager.LUCKY_SHOP) {
            if (items.temp.id == id_item) {
                item = items;
                break;
            }
        }
        if (item != null) {
            it = ItemService.gI().createNewItem(item.temp.id);
            for (ItemOptionLuckyRound io : item.itemOptions) {
                int param = 0;
                if (io.param2 != -1) {
                    param = Util.nextInt(io.param1, io.param2);
                } else {
                    param = io.param1;
                }
                if (io.itemOption.optionTemplate.id == 199) {// chỉ số hiển thị
                    continue;
                }
                if (io.itemOption.optionTemplate.id == 200) {// random vĩnh viễn
                    if (Util.isTrue(io.param2, 100)) {
                        it.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(3, 7)));
                    }
                    continue;
                }
                it.itemOptions.add(new ItemOption(io.itemOption.optionTemplate.id, param));
            }
        }
        return it;
    }

    public Item copyItem(Item item) {
        if (item == null) {
            return null;
        }
        Item it = new Item();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        return it;
    }

    public ConsignmentItem convertToConsignmentItem(Item item) {
        ConsignmentItem it = new ConsignmentItem();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        it.setPriceGold(-1);
        it.setPriceGem(-1);
        it.setIdOrder(-1);
        return it;
    }

    public Item createNewItem(short tempId) {
        return createNewItem(tempId, 1);
    }

    public Item createNewItem(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public ConsignmentItem createNewConsignmentItem(short tempId, int quantity) {
        ConsignmentItem item = new ConsignmentItem();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public static ItemMap RaitiDoSpl(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        RewardService.gI().initBaseOptionClothesMap(it);

        if (Util.isTrue(90, 100)) {
            // tỉ lệ ra spl 0-4 sao
            it.options.add(new ItemOption(107, Util.nextInt(4)));
        } else {
            // tỉ lệ ra spl 5 - 6 sao
            if (Util.isTrue(95, 100)) {
                it.options.add(new ItemOption(107, 5));
            } else {
                it.options.add(new ItemOption(107, 6));
            }

        }

        return it;
    }

    public static ItemMap RaitiDoSpl2(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        RewardService.gI().initBaseOptionClothesMap(it);

        if (Util.isTrue(95, 100)) {
            it.options.add(new ItemOption(107, 6));
        } else {
            it.options.add(new ItemOption(107, 7));
        }

        return it;
    }

    public static ItemMap RaitiDoSpl_0_3s(Zone zone, int tempId, int quantity, int x, int y, long playerId,
            int level) {
        int rationSPL = (level + 1) * 4;
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        RewardService.gI().initBaseOptionClothesMap(it);
        if (Util.isTrue(rationSPL, 100)) {
            // tỉ lệ ra spl 1-3 sao
            it.options.add(new ItemOption(107, Util.nextInt(1, 3)));
        }

        return it;
    }

    public Item createItemFromItemMap(ItemMap itemMap) {
        Item item = createNewItem(itemMap.itemTemplate.id, itemMap.quantity);
        item.itemOptions = itemMap.options;
        return item;
    }

    public ItemOptionTemplate getItemOptionTemplate(int id) {
        return Manager.ITEM_OPTION_TEMPLATES.get(id);
    }

    public ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }

    public boolean isItemActivation(Item item) {
        return false;
    }

    public int getPercentTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                    return 10;
                case 530:
                case 535:
                    return 20;
                case 531:
                case 536:
                    return 30;

                case 533:
                    return 40;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean isTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                case 530:
                case 535:
                case 531:
                case 536:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public int getQuantityItemOnBag(Player player, short itemId) {
        List<Integer> quantityItems = player.inventory.itemsBag.stream()
                .filter(it -> it.isNotNullItem() && it.template.id == itemId)
                .map(item -> item.quantity).collect(Collectors.toList());
        if (!quantityItems.isEmpty()) {
            return quantityItems.stream().reduce(0, Integer::sum);
        }
        return 0;
    }

    public boolean SubThoiVang(Player player, int cost) { // trừ thỏi vàng

        if (cost < 0) {
            Service.getInstance().sendThongBao(player, "Dữ liệu không hợp lệ, vui lòng liên hệ admin");
            return false;
        }
        int quantityThoiVang = getQuantityItemOnBag(player, (short) THOI_VANG_ID);
        if (quantityThoiVang <= 0) {
            Service.getInstance().sendThongBao(player, "Bạn không đủ " + cost + " thỏi vàng");
            return false;
        }
        if (quantityThoiVang < cost) {
            Service.getInstance().sendThongBao(player, "Bạn không đủ thỏi vàng");
            return false;
        }
        if (quantityThoiVang - cost < 0) {
            Service.getInstance().sendThongBao(player, "Có lỗi xảy ra, vui lòng liên hệ admin");
            return false;
        }

        player.inventory.subGoldBar(cost);
        if (InventoryService.gI().subQuantityItemsBagGoldBar(player, (short) THOI_VANG_ID, cost)) {
            // Log.warning("Sub thỏi vàng thành công");
            return true;
        }
        return false;
    }

    public boolean SubHongngoc(Player player, int cost) { // trừ hồng ngcoj

        if (cost < 0) {
            Service.getInstance().sendThongBao(player, "Dữ liệu không hợp lệ, vui lòng liên hệ admin");
            return false;
        }
        int quantityHongngoc = getQuantityItemOnBag(player, (short) HONG_NGOC_ID);
        if (quantityHongngoc <= 0) {
            Service.getInstance().sendThongBao(player, "Bạn không đủ " + cost + " hồng ngọc");
            return false;
        }
        if (quantityHongngoc < cost) {
            Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc");
            return false;
        }
        if (quantityHongngoc - cost < 0) {
            Service.getInstance().sendThongBao(player, "Có lỗi xảy ra, vui lòng liên hệ admin");
            return false;
        }

        player.inventory.subRuby(cost);
        if (InventoryService.gI().subQuantityItemsBagGoldBar(player, (short) HONG_NGOC_ID, cost)) {
            Log.warning("Sub thỏi vàng thành công");
            return true;
        }
        return false;
    }

    public boolean isOutOfDateTime(Item item) {
        long now = System.currentTimeMillis();
        if (item != null) {
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 93) {
                    int dayPass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                    if (dayPass != 0) {
                        io.param -= dayPass;
                        if (io.param <= 0) {
                            return true;
                        } else {
                            item.createTime = System.currentTimeMillis();
                        }
                    }
                }
            }
        }
        return false;
    }
     public int randomSKHId1(byte gender) {

    
        if (gender == 3) {
            gender = 2;
        }

        int[][] options = {{129, 128, 127}, {131, 130, 132}, {135, 133, 134}};
        int skhv1 = 20;//ngon nhất
        int skhv2 = 30;// tạm
        int skhc = 50;// cùi

        int skhId = 0;
        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            skhId = 0;
        } else if (rd <= skhv1 + skhv2) {
            skhId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            skhId = 2;
        }
        return options[gender][skhId];
    }
     
    public int randomSKHId(byte gender) {

    
        if (gender == 3) {
            gender = 2;
        }

        int[][] options = {{129, 128, 127}, {131, 130, 132}, {135, 133, 134}};
        int skhv1 = 12;//ngon nhất
        int skhv2 = 38;// tạm
        int skhc = 50;// cùi

        int skhId = 0;
        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            skhId = 0;
        } else if (rd <= skhv1 + skhv2) {
            skhId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            skhId = 2;
        }
        return options[gender][skhId];
    }

    public void AddOptionSKH(ItemMap item, int skhId) {
        AddOptionSKHAll(item.options, skhId);
    }

    public void AddOptionSKH(Item item, int skhId) {
        AddOptionSKHAll(item.itemOptions, skhId);
    }

    private void AddOptionSKHAll(List<ItemOption> item, int skhId) {
        item.add(new ItemOption(skhId, 1));
        item.add(new ItemOption(optionIdSKH(skhId), 1));
        item.add(new ItemOption(30, 1));
    }

    public int optionIdSKH(int skhId) {
        switch (skhId) {
            case 127: // Set TDHS
                return 139;
            case 128: // Set QCKK
                return 140;
            case 129: // Set Kamejoko
                return 141;
            case 130: // Set KI
                return 142;
            case 131: // Set LH
                return 143;
            case 132: // Set trứng
                return 144;
            case 133: // Set Galick
                return 136;
            case 134: // Set khi
                return 137;
            case 135: // Set HP
                return 138;
            case 196:
                return 193;
            case 197:
                return 194;
            case 198:
                return 195;
        }
        return 0;
    }

    public boolean isItemNoLimitQuantity(int id) {// item k giới hạn số lượng
        if (id >= 1066 && id <= 1070) {// mảnh trang bị thiên sứ
            return true;
        }
        return false;
    }

    public ItemMap CreateAllItemMap(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        AddOptionItemMap(it);
        return it;
    }

    public ItemMap itemMapSKH(Zone zone, int tempId, int quantity, int x, int y, long playerId, int skhid) {
        ItemMap item = createItemMapSetKichHoat(zone, tempId, quantity, x, y, playerId);
        if (item != null) {
            // item.options.addAll(ItemService.gI().getListOptionItemShop((short) tempId));
            item.options.add(new ItemOption(skhid, 1));
            item.options.add(new ItemOption(optionIdSKH(skhid), 1));
            item.options.add(new ItemOption(30, 1));
        }
        return item;
    }

    public ItemMap createItemMapSetKichHoat(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap item = new ItemMap(zone, tempId, quantity, x, y, playerId);
        item.quantity = quantity;
        item.options = createItemNull().itemOptions;
        item.itemTemplate = getTemplate(tempId);

        return item;
    }

    public ItemMap SysOptionItemMap(ItemMap it, int tiLeHanSuDung) {
        Item item = ItemService.gI().createNewItem(it.itemTemplate.id, 1);
        OptionAllItem(item, tiLeHanSuDung);
        if (item.itemOptions != null) {
            for (ItemOption io : item.itemOptions) {
                it.options.add(io);
            }
        }
        return it;

    }

    public ItemMap BaseRewar(Zone zone, Player player, int x, int y, byte type) {
        ItemMap itemMap = null;
        short[] set1 = {190};
        switch (type) {
            case TDST_NM_BOSS:
                set1 = LIST_ITEM_TDST;
                break;
            case BOJACK_BOSS:
                set1 = LIST_ITEM_BOJACK;
                break;
            case FUTURE_BOSS:
                set1 = LIST_ITEM_FUTURE;
                break;
            case BIG_MOB_1:
                set1 = LIST_ITEM_BIG_MOB_1;
                break;
            case RW_EVENT:
                set1 = LIST_ITEM_EVENT;
                break;
            case COLD:
                set1 = LIST_COLD;
                break;
            case 7:
                set1 = LIST_HUY_DIET;
                break;
            case 8:
                set1 = LIST_HUY_DIET;
                break;

        }
        short idItem = (short) set1[Util.nextInt(0, set1.length - 1)];
        itemMap = new ItemMap(zone, idItem, (idItem == 190) ? 30000 : 1, x, y,
                player.id);
        if (itemMap != null) {
            AddOptionItemMap(itemMap);
        }
        return itemMap;
    }

    public ItemMap AddOptionItemMap(ItemMap it) {
        switch (it.itemTemplate.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                trangBi_ItemMap(it);
                break;
            case 5:
                Ct_ItemMap(it);
                break;
            case 14:
                Dnc_ItemMap(it);
                break;
            case 30:
                Spl_ItemMap(it);
                break;
            case 27:
                _27_ItemMap(it);
                break;
            case 11:
                VPDL_ItemMap(it);
                break;
        }

        return it;
    }

    private ItemMap trangBi_ItemMap(ItemMap it) {

        RewardService.gI().initBaseOptionClothesMap(it);

        if (Util.isTrue(90, 100)) {
            // tỉ lệ ra spl 0-4 sao
            it.options.add(new ItemOption(107, Util.nextInt(4)));
        } else {
            // tỉ lệ ra spl 5 - 6 sao
            if (Util.isTrue(95, 100)) {
                it.options.add(new ItemOption(107, 5));
            } else {
                it.options.add(new ItemOption(107, 6));
            }

        }
        return it;
    }

    private ItemMap VPDL_ItemMap(ItemMap it) {
        // switch (it.itemTemplate.id) {
        // case 1114:
        // it.options.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI,
        // Util.nextInt(3, 12))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 12)));
        // // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(5, 12)));
        // // hut ki
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(5, 18))); //
        // dame chi mang

        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.VPDL_LUOI_HAI:
        // it.options.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI,
        // Util.nextInt(3, 8))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 12)));
        // // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(5, 12)));
        // // hut ki
        // it.options.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG,
        // Util.nextInt(5, 12))); // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // }
        return it;
    }

    private ItemMap Ct_ItemMap(ItemMap it) {
        // switch (it.itemTemplate.id) {
        // case ConstCT.CT_BUJIN:
        // it.options.add(new ItemOption(50, Util.nextInt(5, 7))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 7))); //
        // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(5, 7))); //
        // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(10, 15)));
        // // tnsm

        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_KOGU:
        // it.options.add(new ItemOption(50, Util.nextInt(7, 9))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(7, 9))); //
        // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(7, 9))); //
        // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(12, 17)));
        // // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_ZANGYA:
        // it.options.add(new ItemOption(50, Util.nextInt(9, 11))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(9, 11)));
        // // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(9, 11)));
        // // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(14, 19)));
        // // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_BIDO:
        // it.options.add(new ItemOption(50, Util.nextInt(11, 13))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(11, 13)));
        // // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(11, 13)));
        // // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(16, 21)));
        // // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_BOJACK:
        // it.options.add(new ItemOption(50, Util.nextInt(13, 15))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(13, 15)));
        // // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(13, 15)));
        // // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(18, 23)));
        // // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_SUPER_BOJACK:
        // it.options.add(new ItemOption(50, Util.nextInt(20))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(20))); //
        // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(20))); //
        // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(35))); //
        // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_SO_4:
        // it.options.add(new ItemOption(50, Util.nextInt(9, 15))); // sd
        // it.options.add(new ItemOption(77, Util.nextInt(9, 15))); // hp
        // it.options.add(new ItemOption(103, Util.nextInt(9, 15))); // ki
        // it.options.add(new ItemOption(94, Util.nextInt(9, 15))); // giáp
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 2))); //
        // giáp
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_SO_3:
        // it.options.add(new ItemOption(50, Util.nextInt(10, 17))); // sd
        // it.options.add(new ItemOption(77, Util.nextInt(10, 17))); // hp
        // it.options.add(new ItemOption(103, Util.nextInt(10, 17))); // ki
        // it.options.add(new ItemOption(94, Util.nextInt(10, 17))); // giáp
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 4))); //
        // cm
        // it.options.add(new ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_SO_2:
        // it.options.add(new ItemOption(50, Util.nextInt(12, 18))); // sd
        // it.options.add(new ItemOption(77, Util.nextInt(12, 18))); // hp
        // it.options.add(new ItemOption(103, Util.nextInt(12, 18))); // ki
        // it.options.add(new ItemOption(94, Util.nextInt(12, 18))); // giáp
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(2, 5))); //
        // cm
        // it.options.add(new ItemOption(93, Util.nextInt(2, 5))); // hsd
        // break;
        // case ConstCT.CT_SO_1:
        // it.options.add(new ItemOption(50, Util.nextInt(12, 20))); // sd
        // it.options.add(new ItemOption(77, Util.nextInt(12, 20))); // hp
        // it.options.add(new ItemOption(103, Util.nextInt(12, 20))); // ki
        // it.options.add(new ItemOption(94, Util.nextInt(12, 20))); // giáp
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(3, 7))); //
        // cm
        // it.options.add(new ItemOption(93, Util.nextInt(3, 5))); // hsd
        // break;
        // case ConstCT.CT_SO_TDT:
        // it.options.add(new ItemOption(50, Util.nextInt(12, 23))); // sd
        // it.options.add(new ItemOption(77, Util.nextInt(12, 23))); // hp
        // it.options.add(new ItemOption(103, Util.nextInt(12, 23))); // ki
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 12)));
        // // giáp
        // it.options.add(new ItemOption(94, Util.nextInt(12, 23))); // cm
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(5, 12))); //
        // giáp
        // it.options.add(new ItemOption(93, Util.nextInt(3, 7))); // hsd
        // break;
        // }
        return it;
    }

    private ItemMap Dnc_ItemMap(ItemMap it) {
        int idParma;
        switch (it.itemTemplate.id) {
            case 220: // rada
                idParma = 71;
                break;
            case 221: // giay
                idParma = 70;
                break;
            case 222: // quan
                idParma = 69;
                break;
            case 223: // ao
                idParma = 68;
                break;
            case 224: // gang
                idParma = 67;
                break;
            case 1329: // đá nâng cấp
                idParma = 224;
                break;
            default:
                idParma = 0;
                break;
        }
        if (idParma > 0) {
            it.options.add(new ItemOption(idParma, 1));
        }

        return it;
    }

    private ItemMap Spl_ItemMap(ItemMap it) {

        int idParma;
        switch (it.itemTemplate.id) {
            case 441:
                idParma = 95;
                break;
            case 442:
                idParma = 96;
                break;
            case 443:
                idParma = 97;
                break;
            case 444:
                idParma = 98;
                break;
            case 445:
                idParma = 99;
                break;
            case 446:
                idParma = 10;
                break;
            case 447:
                idParma = 101;
                break;
            default:
                idParma = 0;
        }
        if (idParma > 0) {
            it.options.add(new ItemOption(idParma, 5));
        }

        return it;
    }

    private ItemMap _27_ItemMap(ItemMap it) {
        switch (it.itemTemplate.id) {
            case 568:
            case 590:
                it.options.add(new ItemOption(86, 1));
                break;
            case 1353:
                it.options.add(new ItemOption(30, 1));
                break;
            case 570:
                it.options.add(new ItemOption(72, Util.nextInt(8, 11)));
                break;
            case 381:
                it.options.add(new ItemOption(94, Util.nextInt(8, 11)));
                break;
            case 1484:
                it.options.add(new ItemOption(94, 5));
                break;
            case 1485:
                it.options.add(new ItemOption(77, 3));
                break;
            case 1486:
                it.options.add(new ItemOption(103, 3));
                break;
            case 1487:
                it.options.add(new ItemOption(5, 2));
                break;
            case 1488:
                it.options.add(new ItemOption(50, 3));
                break;
        }
        return it;
    }

    public Item OptionAllItem(Item item, int TiLeHanSuDung) {

        switch (item.template.type) {
            case 0: // áo
            case 1: // quần
            case 2: // găng
            case 3: // giày
            case 4: // rada
                RewardService.gI().initBaseOptionClothes(item);
                // if (TiLeHanSuDung > 0) {
                // item.itemOptions.add(new ItemOption(107, TiLeHanSuDung));
                // }
                break;
            case 5: // cải trang
                _5_Item(item, TiLeHanSuDung);
                break;
            case 11: // đeo lưng
                _11_Item(item, TiLeHanSuDung);
                break;
            case 14: // đá nâng cấp
                ChiSoDnc(item);
                break;
            case 23: // ván bay
            case 24:
                _23_24_Item(item, TiLeHanSuDung);
                break;
            case 27:
            case 31:
                _27_Item(item, TiLeHanSuDung);
                break;
            case 29: // item thời gian
                _29_Item(item, TiLeHanSuDung);
                break;
            case 30: // sao pha lê
                ChiSoSpl(item);
                break;
            case 72: // linh thú
                // ChiSoLinhThu(item);
                break;
            case 77: // chân tử
                // ChiSoChanTu(item);
                break;
            case 98: // mini pet đi theo
                _98_Item(item, TiLeHanSuDung);
                break;
            case 99: // pet đeo lưng
                _99_Item(item, TiLeHanSuDung);
                break;
        }
        return item;
    }

    private Item _5_Item(Item item, int TiLeHanSuDung) {// cải trang

        switch (item.template.id) {
            case 1351: {
                item.itemOptions.add(new ItemOption(50, 18));
                item.itemOptions.add(new ItemOption(77, 18));
                item.itemOptions.add(new ItemOption(103, 18));
                item.itemOptions.add(new ItemOption(101, 15));
            }
            break;
            case 1456: {
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 22));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, 22));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, 15));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_KI, 15));
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 5));
            }
            break;
            case 1367: {
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 30));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 20));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, 20));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, 30));
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 5));
            }
            break;
            case 448: {
                item.itemOptions.add(new ItemOption(ConstOption.HUT_HP_QUAI_PT, 50));
            }
            break;
            case 450: {
                item.itemOptions.add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH, 1));

            }
            break;
            case 451: {
                item.itemOptions.add(new ItemOption(ConstOption.PT_VANG_TU_QUAI, 200));
            }
            break;
            case 618: // CẢI TRANG HẢI TẶC
            case 619:
            case 620:
            case 621:
            case 622:
            case 623:
            case 624:
            case 625:
            case 626: {
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 6));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 12));
                item.itemOptions.add(new ItemOption(ConstOption.TANG_CHI_SO_HAI_TAC, 1));
                item.itemOptions.add(new ItemOption(238, 1));
            }
            break;
            case 544:
            case 545:
            case 546: { // Cải trang Tôn Ngộ Không SP
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 30, TiLeHanSuDung)));
                item.itemOptions.add(
                        new ItemOption(ConstOption.TNSM_CHO_DE_TU_KHI_SU_PHU_MAT_CT,
                                Util.nextInt(500, 1000, TiLeHanSuDung)));
            }
            break;
            case 547: { // Cải trang Tôn Ngộ Không
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(30, 40, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(30, 40, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(30, 40, TiLeHanSuDung)));
                item.itemOptions.add(
                        new ItemOption(ConstOption.TNSM_DANH_QUAI_TANG_20_PT, Util.nextInt(500, 1000, TiLeHanSuDung)));
                item.itemOptions.add(
                        new ItemOption(ConstOption.KI, Util.nextInt(1000, 5000, TiLeHanSuDung)));

            }
            break;
            case 548: { // Cải trang Bát Giới
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(30, 40, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(30, 40, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(30, 40, TiLeHanSuDung)));
                item.itemOptions.add(
                        new ItemOption(ConstOption.TNSM_DANH_QUAI_TANG_20_PT, Util.nextInt(500, 1000, TiLeHanSuDung)));
                item.itemOptions.add(
                        new ItemOption(ConstOption.HP, Util.nextInt(1000, 5000, TiLeHanSuDung)));

            }
            break;
            case 1262: { // Cải trang sa tăng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(30, 40, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(30, 40, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(30, 40, TiLeHanSuDung)));
                item.itemOptions.add(
                        new ItemOption(ConstOption.TNSM_DANH_QUAI_TANG_20_PT, Util.nextInt(500, 1000, TiLeHanSuDung)));
                item.itemOptions.add(
                        new ItemOption(ConstOption.TAN_CONG, Util.nextInt(500, 1000, TiLeHanSuDung)));

            }
            break;
            case 1037: // Gohan bãi biễn
            case 1038: { // Gohan kính đen
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(28, 33, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(28, 33, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(28, 33, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KHANG_TDHS, 1));

            }
            break;
            case 388:// Mũ noel xanh
            case 391:// Mũ noel xanh
            case 394: { // Mũ noel xanh
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 35, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(20, 70, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(20, 70, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(20, 100, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(10, 35, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH, 1));

            }
            break;
            case 387: // Mũ noel đỏ
            case 390: // Mũ noel đỏ
            case 393: { // Mũ noel đỏ
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 30, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(20, 55, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(20, 55, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(20, 70, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(10, 25, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH, 1));

            }
            break;
            case 386: // Mũ noel xám
            case 389: // Mũ noel xám
            case 392: { // Mũ noel xám
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH, 1));

            }
            break;

            case 629: { // cải trang fide vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                ;
                item.itemOptions
                        .add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 15));

            }
            break;
            case 1368:
            case 2067: { // cải trang bư ốm
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(10, 35, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(10, 35, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.BIEN_SOCOLA, 1));

            }
            break;
            case 989: { // cải trang Gô han siêu nhân
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(10, 45, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HAP_THU_ROI_BOC_PHA, Util.nextInt(3, 15, TiLeHanSuDung)));

            }
            break;
            case 990: { // cải trang Videl siêu nhân
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(10, 30, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(10, 45, TiLeHanSuDung)));

            }
            break;
            case 991: { // cải trang Pen siêu nhân
                item.itemOptions
                        .add(new ItemOption(ConstOption.TAN_CONG_KHI_DANH_QUAI, Util.nextInt(15, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 36, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 30, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(10, 50, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(10, 45, TiLeHanSuDung)));

            }
            break;
            case 985: { // cải trang chill
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 32, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.CHINH_XAC_PT, Util.nextInt(10, 30, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.NE_DON_PT, Util.nextInt(10, 30, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(10, 38, TiLeHanSuDung)));
            }
            break;
            case 957: // cải trang võ sĩ
            case 958: // cải trang võ sĩ
            case 959: { // cải trang võ sĩ
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 32, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(10, 48, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(10, 48, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(10, 28, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HAP_THU_ROI_BOC_PHA, 1));

            }
            break;
            case 948: { // cải trang hổ
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CAN_CHIEN, Util.nextInt(10, 28, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CHUONG_PT, Util.nextInt(18, 38, TiLeHanSuDung)));

            }
            break;
            case 952: { // cải trang hổ
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(10, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(18, 38, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(18, 38, TiLeHanSuDung)));
            }
            break;
            case 953: { // cải trang hổ
                item.itemOptions
                        .add(new ItemOption(ConstOption.TAN_CONG_KHI_DANH_QUAI, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(15, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.PT_VANG_TU_QUAI, Util.nextInt(50, 100, TiLeHanSuDung)));

            }
            break;
            case 906: { // cải trang Mysty mask
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.PHAN_SAT_THUONG, Util.nextInt(10, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(18, 38, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(18, 38, TiLeHanSuDung)));
            }
            break;
            case 860: { // cải trang Mị nương
                item.itemOptions.add(
                        new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 20));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(241, Util.nextInt(10, 20, TiLeHanSuDung)));

            }
            break;
            case 827: { // cải trang Noel
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(28, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(18, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH, 1));
            }
            break;
            case 742: { // cải trang caufila dơi
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(28, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(28, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(28, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(246, 1));

            }
            break;
            case 739: { // cải trang bill bí ngô
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(28, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(28, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(28, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.BIEN_XUNG_QUANG_THANH_BI_NGO, 1));

            }
            break;
            case 730: { // cải trang sois
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(18, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_LUA, 20));

            }
            break;
            case 731: { // cải trang sois
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HA_DOC_DOI_THU, 1));
            }
            break;
            case 732: { // cải trang sois
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HAP_THU_ROI_BOC_PHA, 1));

            }
            break;
            case 675:
            case 676:
            case 677:
            case 678:
            case 679:
            case 680:
            case 681: { // cải trang nữ
                item.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 5 + (item.template.id - 675) * 2));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 20 + (item.template.id - 675) * 2));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, 20 + (item.template.id - 675) * 2));
                item.itemOptions
                        .add(new ItemOption(ConstOption.PT_TOC_DO_CHAY, 20));
                item.itemOptions
                        .add(new ItemOption(ConstOption.PHAN_SAT_THUONG, Util.nextInt(10, 18, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.CUTE_HOI_KI, Util.nextInt(3, 10, TiLeHanSuDung)));

            }
            break;
            case 630: { // cải trang sơn tinh thủy tinh
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, 30));
            }
            break;
            case 421:
            case 422: { // cải trang sơn tinh thủy tinh
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, 30));
            }
            break;
            case 1342: { // cải trang goku super gold
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(18, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 32, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(241, Util.nextInt(2, 10, TiLeHanSuDung)));
            }
            break;
            case 1317: { // cải trang thầy giáo cadic
                item.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 18, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(8, 28, TiLeHanSuDung)));

            }
            break;
            case 1263: { // cải trang Bulma S
                // item.itemOptions.add(new
                // ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 15));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(30, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(30, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(30, 35, TiLeHanSuDung)));
                // item.itemOptions
                // .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(8, 28,
                // TiLeHanSuDung)));

            }
            break;
            case 1260: { // cải trang nguyệt
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(18, 28, TiLeHanSuDung)));

            }
            break;
            case 1261: { // cải trang nhật
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(18, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(18, 28, TiLeHanSuDung)));
            }
            break;
            case 898: { // cải trang zamasu
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(30, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(30, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(30, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP, Util.nextInt(15, 25, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(5, 8, TiLeHanSuDung)));
                // item.itemOptions.add(new ItemOption(235, 1));
            }
            break;
            case 941: { // cải trang hổ
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(15, 25, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(15, 25, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(15, 25, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP, Util.nextInt(4, 15, TiLeHanSuDung)));

            }
            break;
            case 1234: { // cải trang póc biển
                item.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 18));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(25, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(25, 35, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CAN_CHIEN, Util.nextInt(10, 30, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CHUONG_PT, Util.nextInt(10, 30, TiLeHanSuDung)));

            }
            break;
            case 1235: { // cải trang pic biển
                item.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 18));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(15, 25, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(15, 25, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(15, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(5, 8, TiLeHanSuDung)));

            }
            break;
            case 1236: { // cải trang kingkong biển
                item.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 18));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(25, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(25, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(15, 55, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(15, 55, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
            }
            break;
            case 937: {// mabu noel
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(15, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(15, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(15, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH, 1));

            }
            break;
            // case 421: {// sơn tinh
            // item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(15,
            // 30, TiLeHanSuDung)));
            // item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(15, 30,
            // TiLeHanSuDung)));
            // item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(15, 30,
            // TiLeHanSuDung)));
            // item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(50,
            // 200, TiLeHanSuDung)));
            // }
            // break;
            // case 422: {// thủy tinh
            // item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(15,
            // 30, TiLeHanSuDung)));
            // item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(15, 30,
            // TiLeHanSuDung)));
            // item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(15, 30,
            // TiLeHanSuDung)));
            // item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S,
            // Util.nextInt(10, 70, TiLeHanSuDung)));
            // item.itemOptions.add(new ItemOption(ConstOption.KI_PT_MOI_30_S,
            // Util.nextInt(10, 70, TiLeHanSuDung)));
            // }
            // break;
            case 642: { // cải trang ma trơi
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 25, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 25, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(15, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(15, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KHONG_BI_HOA_XUONG, 1));
            }
            break;
            case 904: { //
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(20, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 32, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.PHAN_SAT_THUONG, Util.nextInt(15, 25, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.VO_HIEU_HOA_CHUONG, Util.nextInt(50, 70, TiLeHanSuDung)));
            }
            break;

            case 643: { // cải trang ma trơi
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 25, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(20, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(20, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(15, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.NE_DON_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KHONG_BI_HOA_XUONG, 1));
            }
            break;
            case 463: { // cải trang thỏ đại ca
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(15, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KHANG_TDHS, 1));
            }
            break;
            case 464: { // cải trang thỏ bunma
                item.itemOptions.add(
                        new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 18));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(10, 30, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(10, 30, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(15, 255, TiLeHanSuDung)));

            }
            break;
            case 1370: { // cải trang bông băng gold
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(10, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(10, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP, Util.nextInt(10, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH, 1));
                // item.itemOptions.add(new ItemOption(235, 1));
            }
            break;
            case 2041: { // cải trang kingkong biển
                item.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 18));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(25, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(25, 28, TiLeHanSuDung)));

                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
            }
            break;
            case 883: { // cải black gogan
                item.itemOptions.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI, 18));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(25, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(25, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(5, 55, TiLeHanSuDung)));
            }
            break;
            case 884: { // cải trang Hit
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(1, 50, TiLeHanSuDung)));

            }
            break;
            case 2040: { // cải trang Cải trang Black Goku
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 28, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(10, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(10, 35, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(15, 45, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(15, 45, TiLeHanSuDung)));
            }
            break;
            case 1036: { // cải trang Cải trang Black Goku
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(1, 20, TiLeHanSuDung)));
                //item.itemOptions.add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(15, 45, TiLeHanSuDung)));
            }
            break;

        }
        if (TiLeHanSuDung > 0) {
            if (TiLeHanSuDung < 30) { // Nếu nhỏ hơn 30 thì sẽ lấy số ngày
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, TiLeHanSuDung));
            } else if (Util.isTrue(TiLeHanSuDung, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(5, 7)));
            }
        }

        return item;
    }

    private Item _29_Item(Item item, int TiLeHanSuDung) {
        switch (item.template.id) {

            case 465: { // Bánh Trung Thu 1 trứng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, 10));
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));
            }

            break;
            case 467: { // Bánh Trung Thu 2 trứng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 15));
                item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, 15));
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));
            }

            break;
            case 472: { // Bánh Trung Thu Đặc Biệt
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 20));
                item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, 20));
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));
            }

            break;
            case 473: { // Hộp bánh Trung Thu
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 25));
                item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, 25));
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 30));
            }

            break;

        }
        return item;
    }

    private Item _98_Item(Item item, int TiLeHanSuDung) {// Pet đi theo
        switch (item.template.id) {
            case 1118: { // Pet chó Địa ngục
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(7, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(7, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(7, 12, TiLeHanSuDung)));
            }
            break;
            case 892: { // Thỏ xám
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
            }
            break;
            case 893: { // Thỏ trắng
                item.itemOptions.add(new ItemOption(101, 5));
            }
            break;
            case 916: { // Lính bảo vệ tam giác
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 4, TiLeHanSuDung)));
            }
            break;
            case 917: { // Lính bảo vệ vuông
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(5, 20, TiLeHanSuDung)));
            }

            break;
            case 918: { // Lính bảo vệ tròn
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 919: { // Búp bê
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 936: { // Tuần lộc nhí
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 942: { // Hổ mặp vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 943: { // Hổ mặp trắng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 944: { // Hổ mặp xanh
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 967: { // Sao la
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHIEU_CUOI, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 1039: { // Pet Thỏ ốm
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HOI_PHUC_KI_KHI_BI_DANH, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 1040: { // Pet Thỏ mập
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CHUONG_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CAN_CHIEN, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 1046: { // Pet Khỉ Bong Bóng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(1, 20, TiLeHanSuDung)));
            }
            break;
            case 1188: { // Pet mèo đen đuôi vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(1, 20, TiLeHanSuDung)));
            }
            break;
            case 1202: { // Pet mèo trắng đuôi vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.CUTE_HOI_KI, Util.nextInt(1, 3, TiLeHanSuDung)));
            }
            break;
            case 1203: { // Pet mèo trắng đuôi vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.CUTE_HOI_KI, Util.nextInt(1, 5, TiLeHanSuDung)));
            }
            break;
            case 1207: { // Pet Minion
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.NE_DON_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;

            case 1224: { // Pet Voi Chín Ngà
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(5, 15, TiLeHanSuDung)));
            }
            break;
            case 1225: { // Pet Gà Chín Cựa
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHIEU_CUOI, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 1226: { // Pet Ngựa Chín Hồng mao
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(10, 30, TiLeHanSuDung)));
            }
            break;
            case 1243: { // Pet bọ cánh cứng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 17, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HUT_HP_KI_XUNG_QUANH, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 1244: { // Pet ngài đêm
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 17, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 17, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HOI_PHUC_KI_KHI_BI_DANH, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 1259: { // Pet Shiba vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.DE_TU_SD_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 1265: { // Heo Bướm
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GIAP, Util.nextInt(10, 200, TiLeHanSuDung)));
            }
            break;
            case 1271: { // Mèo Bí Ngô
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(7, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(7, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(7, 10, TiLeHanSuDung)));

            }
            break;
            case 1336: { // Mini Noel
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;

        }
        if (TiLeHanSuDung > 0) {
            if (TiLeHanSuDung < 30) { // Nếu nhỏ hơn 30 thì sẽ lấy số ngày
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, TiLeHanSuDung));
            } else if (Util.isTrue(TiLeHanSuDung, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(5, 7)));
            }
        }
        return item;
    }

    private Item _99_Item(Item item, int TiLeHanSuDung) {// Linh thú
        switch (item.template.id) {
            case 1288: { // Kỳ lân hóa thần
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
            }
            break;
            case 1295: { // Sứ giả Kaio
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.NE_DON_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
            }
            break;
            case 1296: { // Thần thú Kaio
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 12, TiLeHanSuDung)));
            }
            break;
            case 1272: { // Linh thú mèo Halloween
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(5, 13, TiLeHanSuDung)));
            }
            break;
            case 1273: { // Linh thú Thần Chết bí ngô
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(7, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(7, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(7, 10, TiLeHanSuDung)));
                // item.itemOptions.add(new ItemOption(ConstOption.KI_PT_MOI_30_S,
                // Util.nextInt(5, 13, TiLeHanSuDung)));
            }
            break;
            case 1278: { // Ngân long
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP, Util.nextInt(50, 200, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
            }
            break;
            case 1279: { // Mộc long
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(5, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(5, 20, TiLeHanSuDung)));
            }
            break;
            case 1280: { // Ám long
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                if (Util.isTrue(50, 100)) {
                    item.itemOptions
                            .add(new ItemOption(ConstOption.XUYEN_GIAP_CAN_CHIEN, Util.nextInt(5, 15, TiLeHanSuDung)));
                } else {
                    item.itemOptions
                            .add(new ItemOption(ConstOption.XUYEN_GIAP_CHUONG_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                }
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(5, 12, TiLeHanSuDung)));
            }
            break;
            case 1281: { // Hỏa long
                item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG, Util.nextInt(500, 1000, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
            }
            break;
            case 1282: { // Tiểu tiên thần thoại
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(5, 15, TiLeHanSuDung)));
            }
            break;
            case 1283: { // Tiểu tiên thần thoại
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(5, 25, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.NE_DON_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
            }
            break;
            case 1284: { // Rồng con
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.DE_TU_SD_PT, Util.nextInt(5, 20, TiLeHanSuDung)));

            }
            break;
            case 1285: { // Kỳ lân diệt thần
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHIEU_CUOI, Util.nextInt(5, 15, TiLeHanSuDung)));
            }
            break;
            case 1286: { // Hộ vệ địa ngục
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(5, 20, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(5, 20, TiLeHanSuDung)));
            }
            break;
            case 1287: { // Băng long
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(
                        new ItemOption(ConstOption.PHUC_HOI_HP_KI_CHO_DONG_DOI, Util.nextInt(5, 10, TiLeHanSuDung)));

            }
            break;
            case 1420: { // Bươm bướm
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAM_THOI_GIAN_MU, Util.nextInt(5, 35, TiLeHanSuDung)));
            }
            break;
            case 1421: { // Chuồn chuồn
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(15, 45, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.NE_DON_PT, Util.nextInt(10, 15, TiLeHanSuDung)));
            }

            break;
            case 1422: { // Ve sầu
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.PHAN_SAT_THUONG, Util.nextInt(5, 25, TiLeHanSuDung)));
            }
            break;
            case 1992: { // Quạ thượng thần
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 1993: { // Tuần lộc
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KI_PT_MOI_30_S, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 2055: { // Hỏa thần
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 2056: { // Băng thần
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 2059: { // Tử Điểu
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TAN_CONG, Util.nextInt(100, 500, TiLeHanSuDung)));
            }
            break;
            case 2060: { // Phượng Hoàng Lửa
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 7, TiLeHanSuDung)));
            }
            break;

        }
        if (TiLeHanSuDung > 0) {
            if (TiLeHanSuDung < 30) { // Nếu nhỏ hơn 30 thì sẽ lấy số ngày
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, TiLeHanSuDung));
            } else if (Util.isTrue(TiLeHanSuDung, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(5, 7)));
            }
        }
        return item;
    }

    private Item _11_Item(Item item, int TiLeHanSuDung) {// Đeo lưng
        switch (item.template.id) {
            case 1333: // Lồng đèn cá chép
            {
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 6));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 3));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, 3));
                item.itemOptions.add(new ItemOption(ConstOption.VAT_PHAM_SU_KIEN, 0));
            }
            break;
            case 1334: // Lồng đèn ông Sao
            {
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 3));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 6));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, 3));
                item.itemOptions.add(new ItemOption(ConstOption.VAT_PHAM_SU_KIEN, 0));
            }
            break;
            case 1335: // Rồng lân phun lửa
            {
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 3));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 3));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, 6));
                item.itemOptions.add(new ItemOption(ConstOption.VAT_PHAM_SU_KIEN, 0));
            }
            break;
            case 1119: // Thỏ gõ búa
            case 1120: // Trống ếch
            {
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(7, 13)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(7, 13)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(7, 13)));
                item.itemOptions.add(new ItemOption(ConstOption.VAT_PHAM_SU_KIEN, 16));
            }
            break;
            case 1324: { // Thố thần Lasuna
                item.itemOptions.add(new ItemOption(201, Util.nextInt(50, 100)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(50, 100)));
            }
            break;
            case 1325: { // Thố thần Kamatsu
                item.itemOptions.add(new ItemOption(202, 100));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(50, 100)));
            }
            break;
            case 1326: { // Thố thần Hinatu
                item.itemOptions.add(new ItemOption(203, 100));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(50, 100)));
            }
            break;
            case 1274: { // cánh hắc tinh
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10)));
                if (Util.isTrue(1, 2)) {
                    item.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(5, 10)));
                } else {
                    item.itemOptions.add(new ItemOption(241, Util.nextInt(5, 10)));
                }
            }
            break;
            case 1289: { // Angle Six Wing
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(15, 35)));

            }
            break;
            case 1290: { // Angle Techno Wing
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 15)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 15)));
                item.itemOptions.add(new ItemOption(ConstOption.NE_DON_PT, Util.nextInt(5, 18)));
            }
            break;
            case 1349: { // Dù băng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.PHAN_SAT_THUONG, Util.nextInt(5, 10)));
            }
            break;
            case 1350: { // Ván trượt tuyết
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(5, 10)));
            }
            break;
            case 1408: { // Bong bóng hoa hồng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 18)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 10)));
            }
            break;
            case 1409: { // Bong bóng thiên sứ
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 18)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 18)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(5, 10)));
            }
            break;
            case 1415: { // Đao rồng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10)));
                item.itemOptions.add(new ItemOption(ConstOption.XUYEN_GIAP_CAN_CHIEN, Util.nextInt(5, 10)));

            }
            break;
            case 1276: { // Kitty Cute
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12)));
                item.itemOptions.add(new ItemOption(ConstOption.CUTE_HOI_KI, Util.nextInt(1, 8)));

            }
            break;
            case 800: { // Lồng đèn Cô Vy
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 10));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, 10));
            }
            break;
            case 801: { // Lồng đèn Con tàu
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 12));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 12));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, 12));
            }
            break;
            case 802: { // Lồng đèn Con gà
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 14));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 14));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 803: { // Lồng đèn Con bướm
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 16));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 16));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, 16));
            }
            break;
            case 804: { // Lồng đèn Đôrêmon
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 16));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, 16));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, 16));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, 15));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, 16));
            }
            break;
            case 814: { // Ma trơi
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 7, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 815: // Hồn ma Goku
            case 816: // Hồn ma Ca đíc
            case 817: { // Hồn ma Pôcôlô
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.PHAN_SAT_THUONG, Util.nextInt(10, 20, TiLeHanSuDung)));
            }
            break;
            case 822: { // Cây thông
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
            }
            break;
            case 823: { // Túi quà
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
            }
            break;

            case 1185: // cành mai
            case 1186: { // cành đào
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
            }
            break;
            case 1197: { // Bóng Vịt Vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
            }
            break;
            case 1142: { // bong bóng heo
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(5, 15, TiLeHanSuDung)));

            }
            break;
            case 1206: { // Trái tim Valentine
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.PHAN_SAT_THUONG, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(5, 10, TiLeHanSuDung)));

            }
            break;
            case 1230: {// gậy thiên sứ
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                // item.itemOptions.add(new ItemOption(241, Util.nextInt(1, 15,
                // TiLeHanSuDung)));

            }
            break;
            case 1231: { // gậy quy lão
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(5, 10, TiLeHanSuDung)));

            }
            break;
            case 1258: { // Kiếm Z Red
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CAN_CHIEN, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CHUONG_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(5, 10, TiLeHanSuDung)));
            }
            break;
            case 1021: {// Búa Mjolnir
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
            }
            break;
            case 1022: { // Búa Stormbreaker
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 12, TiLeHanSuDung)));
            }
            break;
            case 1023: { // Quạt ba tiêu
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(5, 25, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.NE_DON_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
            }
            break;
            case 1024: { // Gậy như ý
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TAN_CONG, Util.nextInt(100, 300, TiLeHanSuDung)));
            }
            break;
            case 1025: { // Bồ cào
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CAN_CHIEN, Util.nextInt(5, 10, TiLeHanSuDung)));
            }
            break;
            case 1026: { // Nguyệt nha sản
                item.itemOptions
                        .add(new ItemOption(ConstOption.TAN_CONG_KHI_DANH_QUAI, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 25, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
            }
            break;
            case 1027: { // Quyền trượng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 13, TiLeHanSuDung)));

            }
            break;
            case 1028: { // Dao răng cưa
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CAN_CHIEN, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.XUYEN_GIAP_CHUONG_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
            }
            break;
            case 1030: { // hoa sen
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HOI_HP_30_S, Util.nextInt(5, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HOI_KI_30_S, Util.nextInt(5, 15, TiLeHanSuDung)));
            }
            break;
            case 1031: { // hoa đăng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(5, 25, TiLeHanSuDung)));
            }
            break;
            case 467: // Lồng đèn Ông Sao
            case 468: // Lồng đèn Cá chép
            case 469: // Lồng đèn Kéo Quân
            case 470: // Lồng đèn Ông trăng
            case 471: { // Lồng đèn Hội An
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(3, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, 20));

            }
            break;
            case 1047: { // Lồng đèn lon
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(7, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(7, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(7, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP, Util.nextInt(100, 200, TiLeHanSuDung)));
            }
            break;
            case 1100: { // Chậu hoa ăn thịt
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(7, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(7, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(7, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG, Util.nextInt(100, 300, TiLeHanSuDung)));
            }
            break;
            case 1108: { // Đinh ba Satan
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 6, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(8, 15, TiLeHanSuDung)));
            }
            break;
            case 1109: { // Chổi phù thủy
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHIEU_CUOI, Util.nextInt(8, 15, TiLeHanSuDung)));
            }
            break;
            case 1110: { // Cánh thiên thần
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(8, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(8, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(8, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HUT_HP_QUAI_PT, Util.nextInt(3, 12, TiLeHanSuDung)));
            }
            break;
            case 1111: { // Cánh thiên thần 2
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHIEU_CUOI, Util.nextInt(3, 7, TiLeHanSuDung)));

            }
            break;

            case 1112: { // Cây nắp ấm
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 6, TiLeHanSuDung)));

            }
            break;

            case 1128: { // Cờ logo Quatar 1
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(10, 20, TiLeHanSuDung)));
            }
            break;
            case 1129: { // Cờ logo Quatar 2
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 5, TiLeHanSuDung)));
            }
            break;
            case 1130: { // Cờ giày vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.CHINH_XAC, Util.nextInt(3, 6, TiLeHanSuDung)));
            }
            break;
            case 1137: { // Cờ GOAL
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 11, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 11, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 11, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, Util.nextInt(3, 5, TiLeHanSuDung)));
            }
            break;
            case 1138: { // Cờ FIFA
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 6, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(20, 35, TiLeHanSuDung)));
            }
            break;
            case 1139: { // Quả Bóng Vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(10, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(10, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(20, 30, TiLeHanSuDung)));
            }
            break;
            case 1140: { // Mèo mun đột biến
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 13, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.PT_VANG_TU_QUAI, Util.nextInt(18, 100, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(18, 26, TiLeHanSuDung)));
            }
            break;
            case 1149: { // cánh thiên sứ hắc ám
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 5, TiLeHanSuDung)));
            }
            break;
            case 1158: // Chú lùn
            case 1159: // Chú lùn
            case 1160: // Chú lùn
            case 1161: // Chú lùn
            case 1162: // Chú lùn
            case 1163: // Chú lùn
            case 1164: { // Chú lùn
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 5, TiLeHanSuDung)));
            }
            break;
            case 994: { // Vỏ ốc
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(8, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(8, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(8, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(3, 15, TiLeHanSuDung)));
            }
            break;
            case 995: { // Cây kem
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(8, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(8, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(8, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 5, TiLeHanSuDung)));
            }
            break;
            case 996: { // Cá heo
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(8, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 997: { // Con diều
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(10, 25, TiLeHanSuDung)));
            }
            break;
            case 998: { // Diều rồng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(241, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 999: { // Mèo mun
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(5, 12, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(241, Util.nextInt(1, 10, TiLeHanSuDung)));
            }
            break;
            case 805: // vòng sáng thiên thần
            case 741: { // Cánh dơi Dracula
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 10, TiLeHanSuDung)));

            }
            break;
            case 740: { // lưỡi hái thần chết
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(3, 15, TiLeHanSuDung)));
            }
            break;
            case 745: { // Bông tuyết
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH, Util.nextInt(3, 15, TiLeHanSuDung)));
            }
            break;
            case 955: { // Bó Hoa Vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(1, 5, TiLeHanSuDung)));
            }
            break;
            case 954: { // Bó Hoa Hồng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(1, 5, TiLeHanSuDung)));
            }
            break;
            case 852: { // Cây trúc
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(10, 25, TiLeHanSuDung)));
            }
            break;
            case 865: { // kiếm z
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GAY_SAT_THUONG_BANG_PT_HP_DOI_THU,
                                Util.nextInt(1, 7, TiLeHanSuDung)));
            }
            break;
            case 966: { // trái bóng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(10, 25, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(10, 25, TiLeHanSuDung)));
            }
            break;
            case 982: { // Cúp vàng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(10, 25, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(10, 25, TiLeHanSuDung)));
            }
            break;
            case 983: { // Cờ cổ động
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(3, 15, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 8, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, Util.nextInt(10, 25, TiLeHanSuDung)));
                item.itemOptions
                        .add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(10, 25, TiLeHanSuDung)));
            }
            break;

        }
        if (TiLeHanSuDung > 0) {
            if (TiLeHanSuDung < 30) { // Nếu nhỏ hơn 30 thì sẽ lấy số ngày
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, TiLeHanSuDung));
            } else if (Util.isTrue(TiLeHanSuDung, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(5, 7)));
            }
        }
        return item;
    }

    private Item _23_24_Item(Item item, int TiLeHanSuDung) {// Ván bay
        switch (item.template.id) {
            case 532: { // Quỷ Chim
                // item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1,
                // 2, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));

            }
            break;
            case 733: { // Cân đẩu vân ngũ sắc
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));
            }
            break;
            case 734: { // Ngọc Thố
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, 5));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));
            }
            break;
            case 735: { // Lồng đèn cá chép
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, 5));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));
            }
            break;
            case 743: { // Chổi bay Phù Thủy
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, 5));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));
            }
            break;
            case 744: { // Cột nhà

                item.itemOptions.add(new ItemOption(ConstOption.NGAU_TANG_SD_QUAI_KHI_MAT_CT_TAU_77, 15));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));
            }
            break;
            case 746: { // Xe tuần lộc
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 25));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));
            }
            break;
            case 795: { // Ghế bay

                item.itemOptions.add(new ItemOption(ConstOption.NGAU_TANG_SD_KHI_MAT_CT_FIDE, 15));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));
            }
            break;
            case 849: { // Pháo Thăng Thiên
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 15));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));
            }
            break;
            case 897: { // Rùa bay
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, 10));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));
            }
            break;
            case 920: { // Gậy như ý
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, 10));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));
            }
            break;
            case 1092: { // Gậy Quy Lão
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 10));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));

            }
            break;
            case 1131: { // Quả bóng siêu việt
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 10));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));

            }
            break;
            case 1172: { // Xe heo tuần lộc
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 10));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));

            }
            break;
            case 1252: { // Ve Sầu Xên
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 10));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));

            }
            break;
            case 1253: { // Ve Sầu Xên tiến hóa
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 25));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));

            }
            break;
            case 1267: { // Đài sen
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 25));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));

            }
            break;
            case 1268: { // Đoá sen
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 25));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));

            }
            break;
            case 1269: { // Xe kéo bí ngô
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(7, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(7, 10, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(7, 10, TiLeHanSuDung)));
                // item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, 15));
                // item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                // item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));

            }
            break;
            case 1270: { // Mèo kéo xương
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));

            }
            break;
            case 1292: { // Rồng băng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 25));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_HP, 1));
            }
            break;
            case 1293: { // Đĩa bay
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 35));
                item.itemOptions.add(new ItemOption(ConstOption.NE_DON_PT, 15));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));

            }
            break;
            case 1294: { // Kỳ lân thượng cổ
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 5, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 25));
                item.itemOptions.add(new ItemOption(ConstOption.CHINH_XAC_PT, 15));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));
            }
            break;
            case 1322: { // Thú cưỡi mèo Kitty
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 25));
                item.itemOptions.add(new ItemOption(ConstOption.CHINH_XAC_PT, 15));
                item.itemOptions.add(new ItemOption(ConstOption.CUTE_HOI_KI, 2));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));
            }
            break;
            case 1413: { // Thú cưỡi Rồng thanh long
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 25));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));

            }
            break;
            case 1414: { // Cá chép rồng
                item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3, TiLeHanSuDung)));
                item.itemOptions.add(new ItemOption(ConstOption.TOC_DO_DI_CHUYEN, 25));
                item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 5));
                item.itemOptions.add(new ItemOption(ConstOption.BAY_VA_HOI_PHUC_KI, 1));

            }
            break;

        }
        if (TiLeHanSuDung > 0) {
            if (TiLeHanSuDung < 30) { // Nếu nhỏ hơn 30 thì sẽ lấy số ngày
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, TiLeHanSuDung));
            } else if (Util.isTrue(TiLeHanSuDung, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(5, 7)));
            }
        }
        return item;
    }

    private Item _27_Item(Item item, int TiLeHanSuDung) {
        switch (item.template.id) {
            case 1406:
                item.itemOptions.add(new ItemOption(72, TiLeHanSuDung));
                break;
            case 570:
                item.itemOptions.add(new ItemOption(72, Util.nextInt(8, 11)));
                break;
            case 381:
                item.itemOptions.add(new ItemOption(94, 5));
                break;
            case 1434:
                item.itemOptions.add(new ItemOption(72, TiLeHanSuDung));
                break;
            case 1484:
                item.itemOptions.add(new ItemOption(94, 5));
                break;
            case 1485:
                item.itemOptions.add(new ItemOption(77, 3));
                break;
            case 1486:
                item.itemOptions.add(new ItemOption(103, 3));
                break;
            case 1487:
                item.itemOptions.add(new ItemOption(5, 2));
                break;
            case 1488:
                item.itemOptions.add(new ItemOption(50, 3));
                break;
        }
        return item;
    }

    public Item ChiSoSpl(Item item) {

        short[] optionspl = {95, 96, 97, 98, 99, 10, 101};// hut hp, hut ki,pst, xg chuong, xg can chuyen, chinh xac,
        // tnsm
        int idParma;
        switch (item.template.id) {
            case 441:
                idParma = 0;
                break;
            case 442:
                idParma = 1;
                break;
            case 443:
                idParma = 2;
                break;
            case 444:
                idParma = 3;
                break;
            case 445:
                idParma = 4;
                break;
            case 446:
                idParma = 5;
                break;
            case 447:
                idParma = 6;
                break;
            default:
                idParma = 0;
        }
        item.itemOptions.add(new ItemOption(optionspl[idParma], 5));
        return item;
    }

    public Item ChiSoDnc(Item item) {

        int idParma;
        switch (item.template.id) {
            case 220: // rada
                idParma = 71;
                break;
            case 221: // giay
                idParma = 70;
                break;
            case 222: // quan
                idParma = 69;
                break;
            case 223: // ao
                idParma = 68;
                break;
            case 224: // gang
                idParma = 67;
                break;
            case 1329:// đá nâng cấp
                idParma = 224;
                break;
            default:
                idParma = 0;
                break;
        }
        if (idParma > 0) {
            item.itemOptions.add(new ItemOption(idParma, 1));
        }

        return item;

    }

    public static short getRandomPhuKien(int type, Player player) {
        if (type == -1) {
            // Nếu type == -1, chọn ngẫu nhiên một type từ danh sách listTypeRandom
            short[] listTypeRandom = {5, 11, 98, 23, 99};
            type = Util.randomItem(listTypeRandom);
        }
        short[] listItem = phuKienMap.get(type);

        if (listItem != null && listItem.length > 0) {
            short idItem = listItem[Util.nextInt(listItem.length)];
            if (type == 5) {
                Item item = ItemService.gI().createNewItem(idItem);
                if (item.template.gender != 3) {
                    for (int i = 0; i < 10; i++) {
                        idItem = Util.randomItem(listItem);
                        Item itemCheck = ItemService.gI().createNewItem(idItem);
                        byte genderSp = player.gender;
                        byte genderPet = 3;
                        if (player.pet != null) {
                            genderPet = player.pet.gender;
                        }
                        if (itemCheck.template.gender == 3 || itemCheck.template.gender == genderSp
                                || itemCheck.template.gender == genderPet) {
                            break;
                        }
                    }
                }
            }
            return idItem;
        }
        return -1;
    }

    public static short[] getAllPhuKienWhereType(int type, Player player) {
        if (type == -1) {
            // Nếu type == -1, chọn ngẫu nhiên một type từ danh sách listTypeRandom
            short[] listTypeRandom = {5, 11, 98, 23, 99};
            type = listTypeRandom[Util.nextInt(listTypeRandom.length)];
        }
        short[] listItem = phuKienMap.get(type);
        return listItem;
    }

    public static short[] getAllItemRandom() {
        int totalLength = 0;
        for (short[] array : phuKienMap.values()) {
            totalLength += array.length;
        }
        short[] result = new short[totalLength];
        int index = 0;
        for (short[] array : phuKienMap.values()) {
            System.arraycopy(array, 0, result, index, array.length);
            index += array.length;
        }

        return result;
    }

    public static short[] getAllItemOther() {
        int totalLength = 0;
        totalLength += phuKienMap.get(23).length;
        totalLength += phuKienMap.get(98).length;
        totalLength += phuKienMap.get(99).length;
        short[] result = new short[totalLength];
        int index = 0;
        System.arraycopy(phuKienMap.get(23), 0, result, index, phuKienMap.get(23).length);
        index += phuKienMap.get(23).length;
        System.arraycopy(phuKienMap.get(98), 0, result, index, phuKienMap.get(98).length);
        index += phuKienMap.get(98).length;
        System.arraycopy(phuKienMap.get(99), 0, result, index, phuKienMap.get(99).length);
        index += phuKienMap.get(99).length;
        return result;
    }

    public static short[] getAllItemfreeWhereType1(int type, Player player) {
        if (type == -1) {
            // Nếu type == -1, chọn ngẫu nhiên một type từ danh sách listTypeRandom
            short[] listTypeRandom = {27};
            type = listTypeRandom[Util.nextInt(listTypeRandom.length)];
        }
        short[] listItem = phukienlinhtinh.get(type);
        return listItem;
    }

    public static short[] getAllItemfreeOther1() {
        int totalLength = 0;
        totalLength += phukienlinhtinh.get(27).length;

        short[] result = new short[totalLength];
        int index = 0;
        System.arraycopy(phukienlinhtinh.get(27), 0, result, index, phukienlinhtinh.get(27).length);
        index += phukienlinhtinh.get(27).length;
        return result;
    }

   

}
