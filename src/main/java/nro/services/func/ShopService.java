package nro.services.func;

import nro.consts.ConstAchive;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.CaiTrang;
import nro.models.item.DataShopReward;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.models.shop.ItemShop;
import nro.models.shop.Shop;
import nro.models.shop.TabShop;
import nro.server.Manager;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Log;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Phong Vũ
 * @copyright Phong Vũ
 */
public class ShopService {

    private static final int COST_GOLD_BAR = 50000000;
    private static final int COST_LOCK_GOLD_BAR = 30000000;

    private static final byte NORMAL_SHOP = 0;
    private static final byte SPEC_SHOP = 3;

    private static ShopService i;

    public static ShopService gI() {
        if (i == null) {
            i = new ShopService();
        }
        return i;
    }

    // Lấy ra itemshop khi mua
    private ItemShop getItemShop(int shopId, int tempId) {
        ItemShop itemShop = null;
        Shop shop = null;
        switch (shopId) {
            case ConstNpc.SHOP_BUNMA_QK_0:
                shop = getShop(ConstNpc.BUNMA, 0, -1);
                break;
            case ConstNpc.SHOP_DENDE_0:
                shop = getShop(ConstNpc.DENDE, 0, -1);
                break;
            case ConstNpc.SHOP_APPULE_0:
                shop = getShop(ConstNpc.APPULE, 0, -1);
                break;
            case ConstNpc.SHOP_CUI_NGUC_TU:
                shop = getShop(ConstNpc.CUI, 0, -1);
                break;
            case ConstNpc.SHOP_URON_0:
                shop = getShop(ConstNpc.URON, 0, -1);
                break;
            case ConstNpc.SHOP_SANTA_0:
                shop = getShop(ConstNpc.SANTA, 0, -1);
                break;
            case ConstNpc.SHOP_SANTA_1:
                shop = getShop(ConstNpc.SANTA, 1, -1);
                break;
            case ConstNpc.SHOP_SANTA_THOIVANG:
                shop = getShop(ConstNpc.SANTA, 3, -1);
                break;
            case ConstNpc.SHOP_SANTA_THOI_VANG_2:
                shop = getShop(ConstNpc.SANTA, 4, -1);
                break;
            case ConstNpc.SHOP_SANTA_CAI_TRANG:
                shop = getShop(ConstNpc.SANTA, 5, -1);
                break;
            case ConstNpc.SHOP_SANTA_SKILL:
                shop = getShop(ConstNpc.SANTA, 6, -1);
                break;
            case ConstNpc.SHOP_SANTA_LUCKY:
                shop = getShop(ConstNpc.SANTA, 7, -1);
                break;
            case ConstNpc.SHOP_SANTA_SU_KIEN:
                shop = getShop(ConstNpc.SANTA, 8, -1);
                break;
            case ConstNpc.SHOP_SANTA_THOI_VANG_3:
                shop = getShop(ConstNpc.SANTA, 13, -1);
                break;
            case ConstNpc.SHOP_EVENT_2:
                shop = getShop(ConstNpc.CAY_HOA_HONG, 1, -1);
                break;
            case ConstNpc.SHOP_SANTA_LINH_THU:
                shop = getShop(ConstNpc.SANTA, 9, -1);
                break;
            case ConstNpc.SHOP_SANTA_PET:
                shop = getShop(ConstNpc.SANTA, 10, -1);
                break;
            case ConstNpc.SHOP_SANTA_DE:
                shop = getShop(ConstNpc.SANTA, 15, -1);
                break;
            case ConstNpc.SHOP_SANTA_VAN_BAY:
                shop = getShop(ConstNpc.SANTA, 11, -1);
                break;
            case ConstNpc.SHOP_SANTA_VPDL:
                shop = getShop(ConstNpc.SANTA, 12, -1);
                break;
            case ConstNpc.SHOP_BUNMA_THOI_VANG:
                shop = getShop(ConstNpc.BUNMA_TL, 1, -1);
                break;
            case ConstNpc.SHOP_FREE_DAY:
                shop = getShop(ConstNpc.QUY_LAO_KAME, 3, -1);
                break;
            case ConstNpc.SHOP_DIEM_DANH:
                shop = getShop(ConstNpc.JACO, 2, -1);
                break;
            case ConstNpc.SHOP_ONLINE:
                shop = getShop(ConstNpc.QUY_LAO_KAME, 4, -1);
                break;
            case ConstNpc.SHOP_MOC_NAP:
                shop = getShop(ConstNpc.JACO, 5, -1);
                break;
            case ConstNpc.SHOP_DIEM_NAP:
                shop = getShop(ConstNpc.LY_TIEU_NUONG1, 1, -1);
                break;
            case ConstNpc.SHOP_POWER:
                shop = getShop(ConstNpc.QUY_LAO_KAME, 6, -1);
                break;
            case ConstNpc.SHOP_SIDE_TASK_DAY:
                shop = getShop(ConstNpc.BO_MONG, 1, -1);
                break;
            case ConstNpc.SHOP_BA_HAT_MIT_0:
                shop = getShop(ConstNpc.BA_HAT_MIT, 0, -1);
                break;
            case ConstNpc.SHOP_BA_HAT_MIT_1:
                shop = getShop(ConstNpc.BA_HAT_MIT, 1, -1);
                break;
            case ConstNpc.SHOP_BA_HAT_MIT_2:
                shop = getShop(ConstNpc.BA_HAT_MIT, 2, -1);
                break;
            case ConstNpc.SHOP_BA_HAT_MIT_3:
                shop = getShop(ConstNpc.BA_HAT_MIT, 3, -1);
                break;
            case ConstNpc.SHOP_BUNMA_TL_0:
                shop = getShop(ConstNpc.BUNMA_TL, 0, -1);
                break;
            case ConstNpc.SHOP_BUNMA_TL_2:
                shop = getShop(ConstNpc.BUNMA_TL, 2, -1);
                break;
            case ConstNpc.SHOP_BONG_TOI:
                shop = getShop(ConstNpc.THUONG_DE_NEW, 0, -1);
                break;
            case ConstNpc.SHOP_BILL_HUY_DIET_0:
                shop = getShop(ConstNpc.BILL, 0, -1);
                break;
            case ConstNpc.SHOP_THAN_VU_TRU:
                shop = getShop(ConstNpc.FU, 0, -1);
                break;
            case ConstNpc.SHOP_THAN_VU_TRU1:
                shop = getShop(ConstNpc.FU, 1, -1);
                break;
            case ConstNpc.SHOP_WHIS_THIEN_SU:
                shop = getShop(ConstNpc.WHIS, 0, -1);
                break;
            case ConstNpc.SHOP_HONG_NGOC:
                shop = getShop(ConstNpc.QUY_LAO_KAME, 0, -1);
                break;
            case ConstNpc.SHOP_LY_TIEU_NUONG:
                shop = getShop(ConstNpc.LY_TIEU_NUONG, 0, -1);
                break;
            case ConstNpc.SHOP_POTAGE:
                shop = getShop(ConstNpc.POTAGE, 0, -1);
                break;
            case ConstNpc.SHOP_EVENT:
                shop = getShop(ConstNpc.CAY_HOA_HONG, 0, -1);
                break;
            case ConstNpc.SHOP_DOC_NHAN:
                shop = getShop(ConstNpc.DOC_NHAN, 0, -1);
                break;
            case ConstNpc.SHOP_SU_KIEN_TET:
                shop = getShop(ConstNpc.QUY_LAO_KAME, 1, -1);
                break;
            case ConstNpc.SHOP_TORIBOT:
                shop = getShop(ConstNpc.TORIBOT, 0, -1);
                break;
            case ConstNpc.SHOP_TORIBOT1:
                shop = getShop(ConstNpc.TORIBOT, 1, -1);
                break;
            case ConstNpc.SANTA_ROSE_GAME:
                shop = getShop(ConstNpc.SANTA, 2, -1);
                break;
            case ConstNpc.SHOP_THUONG_DE_76:
                shop = getShop(ConstNpc.THUONG_DE_76, 0, -1);
                break;
            case ConstNpc.SHOP_THO_NGOC_THOI_VANG:
                shop = getShop(ConstNpc.THO_NGOC, 0, -1);
                break;
            case ConstNpc.SHOP_BANG_HOI:
                shop = getShop(ConstNpc.TRONG_TAI_BANG, 0, -1);
                break;
            case ConstNpc.SHOP_HANG_NGA:
                shop = getShop(ConstNpc.HANG_NGA, 0, -1);
                break;
            case ConstNpc.SHOP_TIEM_BANH:
                shop = getShop(ConstNpc.TRUNG_THU, 0, -1);
                break;
            case ConstNpc.SHOP_CHU_CUOI:
                shop = getShop(ConstNpc.CHU_CUOI, 0, -1);
                break;
            case ConstNpc.SHOP_BO_MONG:
                shop = getShop(ConstNpc.BO_MONG, 0, -1);
                break;    
        }
        if (shop != null) {
            for (TabShop tab : shop.tabShops) {
                for (ItemShop is : tab.itemShops) {
                    if (is.temp.id == tempId) {
                        itemShop = is;
                        break;
                    }
                }
                if (itemShop != null) {
                    break;
                }
            }
        }
        return itemShop;
    }

    private Shop getShop(int npcId, int order, int gender) {
        for (Shop shop : Manager.SHOPS) {
            if (shop.npcId == npcId && shop.shopOrder == order) {
                if (gender != -1) {

                    return new Shop(shop, gender);
                } else {
                    return shop;
                }
            }
        }
        return null;
    }

    private Shop getShopHuyDiet(Player player, Shop s) {
        Shop shop = new Shop(s);
        for (TabShop tabShop : shop.tabShops) {
            for (ItemShop item : tabShop.itemShops) {
                item.iconSpec = 15012 + item.temp.type;
                item.costSpec = 99;
            }
        }
        return shop;
    }

    private Shop getShopBua(Player player, Shop s) {
        Shop shop = new Shop(s);
        for (TabShop tabShop : shop.tabShops) {
            for (ItemShop item : tabShop.itemShops) {

                long min = 0;
                switch (item.temp.id) {
                    case 213:
                        long timeTriTue = player.charms.tdTriTue;
                        long current = System.currentTimeMillis();
                        min = (timeTriTue - current) / 60000;

                        break;
                    case 214:
                        min = (player.charms.tdManhMe - System.currentTimeMillis()) / 60000;
                        break;
                    case 215:
                        min = (player.charms.tdDaTrau - System.currentTimeMillis()) / 60000;
                        break;
                    case 216:
                        min = (player.charms.tdOaiHung - System.currentTimeMillis()) / 60000;
                        break;
                    case 217:
                        min = (player.charms.tdBatTu - System.currentTimeMillis()) / 60000;
                        break;
                    case 218:
                        min = (player.charms.tdDeoDai - System.currentTimeMillis()) / 60000;
                        break;
                    case 219:
                        min = (player.charms.tdThuHut - System.currentTimeMillis()) / 60000;
                        break;
                    case 522:
                        min = (player.charms.tdDeTu - System.currentTimeMillis()) / 60000;
                        break;
                    case 671:
                        min = (player.charms.tdTriTue3 - System.currentTimeMillis()) / 60000;
                        break;
                    case 672:
                        min = (player.charms.tdTriTue4 - System.currentTimeMillis()) / 60000;
                        break;
                    case 2025:
                        min = (player.charms.tdDeTuMabu - System.currentTimeMillis()) / 60000;
                        break;
                    case 2076:
                        min = (player.charms.tdDeTuMabu2 - System.currentTimeMillis()) / 60000;
                        break;
                    case 1387:
                        min = (player.charms.tdDeTuMabu3 - System.currentTimeMillis()) / 60000;
                        break;

                }
                if (min > 0) {
                    item.options.clear();
                    if (min >= 1440) {
                        item.options.add(new ItemOption(63, (int) min / 1440));
                    } else if (min >= 60) {
                        item.options.add(new ItemOption(64, (int) min / 60));
                    } else {
                        item.options.add(new ItemOption(65, (int) min));
                    }
                }
            }
        }
        return shop;
    }

    // shop đồ hủy diệt
    public void openShopBillHuyDiet(Player player, int shopId, int order) {
        Shop shop = getShopHuyDiet(player, getShop(ConstNpc.BILL, order, -1));
        openShopType3(player, shop, shopId);
    }

    public void openShopWhisThienSu(Player player, int shopId, int order) {
        Shop shop = getShop(ConstNpc.WHIS, order, -1);
        openShopType3(player, shop, shopId);
    }

    // shop bùa
    public void openShopBua(Player player, int shopId, int order) {
        // player.iDMark.setShopId(shopId);
        Shop shop = getShopBua(player, getShop(ConstNpc.BA_HAT_MIT, order, -1));
        openShopType0(player, shop, shopId);
    }

    // shop normal
    public void openShopNormal(Player player, Npc npc, int shopId, int order, int gender) {
        Shop shop = getShop(npc.tempId, order, gender);
        openShopType0(player, shop, shopId);
    }

    public void openShopSpecial(Player player, Npc npc, int shopId, int order, int gender) {
        Shop shop = getShop(npc.tempId, order, gender);
        openShopType3(player, shop, shopId);
    }

    private void openShopType0(Player player, Shop shop, int shopId) {
        player.iDMark.setShopId(shopId);
        if (shop != null) {
            Message msg;
            try {
                msg = new Message(-44);
                msg.writer().writeByte(NORMAL_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {
                        msg.writer().writeShort(itemShop.temp.id);
                        msg.writer().writeInt(itemShop.gold);
                        msg.writer().writeInt(itemShop.gem);
                        msg.writer().writeByte(itemShop.options.size());
                        for (ItemOption option : itemShop.options) {
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        CaiTrang caiTrang = Manager.gI().getCaiTrangByItemId(itemShop.temp.id);
                        msg.writer().writeByte(caiTrang != null ? 1 : 0);
                        if (caiTrang != null) {
                            msg.writer().writeShort(caiTrang.getID()[0]);
                            msg.writer().writeShort(caiTrang.getID()[1]);
                            msg.writer().writeShort(caiTrang.getID()[2]);
                            msg.writer().writeShort(caiTrang.getID()[3]);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(ShopService.class, e);
            }
        }
    }

    private void openShopType3(Player player, Shop shop, int shopId) {
        player.iDMark.setShopId(shopId);
        if (shop != null) {
            Message msg;
            try {
                msg = new Message(-44);
                msg.writer().writeByte(SPEC_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    // System.out.println(tab.name);
                    for (ItemShop itemShop : tab.itemShops) {
                        msg.writer().writeShort(itemShop.temp.id);
                        msg.writer().writeShort(itemShop.iconSpec);
                        msg.writer().writeInt(itemShop.costSpec);
                        msg.writer().writeByte(itemShop.options.size());
                        for (ItemOption option : itemShop.options) {
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        CaiTrang caiTrang = Manager.gI().getCaiTrangByItemId(itemShop.temp.id);
                        msg.writer().writeByte(caiTrang != null ? 1 : 0);
                        if (caiTrang != null) {
                            msg.writer().writeShort(caiTrang.getID()[0]);
                            msg.writer().writeShort(caiTrang.getID()[1]);
                            msg.writer().writeShort(caiTrang.getID()[2]);
                            msg.writer().writeShort(caiTrang.getID()[3]);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
                // System.out.println("sent");
            } catch (Exception e) {
                Log.error(ShopService.class, e);
            }
        }
    }

    //điểm nạp
    public boolean subPointNap(Player pl, int cost) {
    if (pl == null || pl.getSession() == null) return false;

    boolean ok = PlayerDAO.subCountCardIfEnough(pl.getSession().userId, cost);
    if (!ok) {
        return false; // không đủ hoặc lỗi DB
    }

    // sync RAM
    pl.getSession().count_card -= cost;
    return true;
}

    
    private void buyItemShopNormal(Player player, ItemShop is) {
        if (is != null) {

            if (is.temp.id == 517 && player.inventory.itemsBag.size() >= Inventory.BAG_LIMIT) {
                Service.getInstance().sendThongBao(player, "Hành trang đã đạt tới số lượng tối đa");
                Service.getInstance().sendMoney(player);
                return;
            }
            if (is.temp.id == 518 && player.inventory.itemsBox.size() >= 40) {
                Service.getInstance().sendThongBao(player, "rương đồ đã đạt tới số lượng tối đa");
                Service.getInstance().sendMoney(player);
                return;
            }
            // if (is.temp.id == 988 && player.inventory.goldLimit >= 50000000000L) {
            // Service.getInstance().sendThongBao(player, "Giới hạn vàng của bạn đã đạt tối
            // đa");
            // Service.getInstance().sendMoney(player);
            // return;
            // }
            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                int gold = is.gold;
                int gem = is.gem;
                int itemExchange = is.itemExchange;
                if (gold != 0) {
                    if (player.inventory.gold >= gold) {
                        player.inventory.gold -= gold;
                    } else {
                        Service.getInstance().sendThongBao(player, "Bạn không đủ vàng, còn thiếu "
                                + (Util.numberToMoney(gold - player.inventory.gold) + " vàng"));
                        Service.getInstance().sendMoney(player);
                        return;
                    }
                }
                if (gem != 0) {
                    if (player.inventory.getGem() >= gem) {
                        player.inventory.subGem(gem);
                    } else {
                        Service.getInstance().sendThongBao(player, "Bạn không đủ ngọc, còn thiếu "
                                + (gem - player.inventory.getGem()) + " ngọc");
                        Service.getInstance().sendMoney(player);
                        return;
                    }
                }
                if (itemExchange >= 0) {
                    Item itm = InventoryService.gI().findItemBagByTemp(player, itemExchange);

                    // if (isLimitItem(itemShopID)) {
                    // if (player.buyLimit[itemShopID - 1074] < getBuyLimit(itemShopID)) {
                    // player.buyLimit[itemShopID - 1074]++;
                    // } else {
                    // Service.getInstance().sendThongBao(player, "Số lượt mua trong ngày đã đạt
                    // giới hạn");
                    // return;
                    // }
                    // }
                    if (player.iDMark.getShopId() == ConstNpc.SHOP_DIEM_NAP) {
                        if (subPointNap(player, is.costSpec)) {
                            InventoryService.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is), 99);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn nhận được " + is.temp.name);
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn không đủ điểm Nạp");
                        }
                        return;
                    }
                    if (itemExchange == 861 && player.inventory.getRuby() >= is.costSpec) {
                        player.inventory.subRuby(is.costSpec);
                    } else if (player.iDMark.getShopId() == ConstNpc.SHOP_BILL_HUY_DIET_0) {
                        if (player.setClothes.godClothes == 5) {
                            Item vp = ItemService.gI().createNewItem((short) is.temp.id);
                            Item thanlinh = InventoryService.gI().findItemThanLinh(player, vp.getType());
                            if (thanlinh == null) {
                                Service.getInstance().sendThongBao(player, "Không tìm thấy đồ thần linh đang mặc");
                                return;
                            }
                            Item meal = InventoryService.gI().findMealChangeDestroyClothes(player);

                            Item item = InventoryService.gI().findItem(player.inventory.itemsBody, thanlinh.template.id);
                            if (item == null) {
                                Service.getInstance().sendThongBao(player, "Yêu cầu phải phải có " + is.temp.name + " trong túi đồ");
                                return;
                            }

                            if (meal != null) {
                                if (player.inventory.gold >= is.costSpec) {

                                    player.inventory.subGold(is.costSpec);

                                    InventoryService.gI().removeItemBody(player, vp.getType());

                                    InventoryService.gI().sendItemBody(player);

                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ vật phẩm để trao đổi.");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, meal, 99);
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Yêu cầu có 99 thức ăn !");
                                return;
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Phải mặc đủ 5 trang bị thần linh !");
                            return;
                        }

                    } else if ((itemExchange == 189 || itemExchange == 190)
                            && player.inventory.getGold() >= is.costSpec) {
                        player.inventory.subGold(is.costSpec);
                    } else if (itm != null && itm.isNotNullItem() && itm.quantity >= is.costSpec) {
                        if (itm.getId() == 457) {
                            if (itm.haveOption(30)) {
                                Service.getInstance().sendThongBao(player, "Thỏi vàng khóa không thể sử dụng để mua vật phẩm tại Cửa hàng !");
                                return;
                            }
                            player.pointThoiVang += is.costSpec;
                        }
                        InventoryService.gI().subQuantityItemsBag(player, itm, is.costSpec);
                    } else {
                        Service.getInstance().sendThongBao(player, "Bạn không đủ vật phẩm để trao đổi.");
                        return;
                    }
                }
                if (gold == 0 && gem == 0 && itemExchange < 0) {
                    if (player.iDMark.getShopId() == ConstNpc.SHOP_FREE_DAY) {
                        // của hàng miễn phí hằng ngày
                        if (player.inventory.free_turn_buy_shop >= 1) {
                            player.inventory.free_turn_buy_shop -= 1;
                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Bạn đã hết lượt mua miễn phí, hãy chờ đến ngày mai");
                            Service.getInstance().sendMoney(player);
                            return;
                        }
                    } else if (player.iDMark.getShopId() == ConstNpc.SHOP_DIEM_DANH) {
                        // Kiểm tra xem shop free hằng ngày hay shop điểm danh
                        Item item = ItemService.gI().createItemFromItemShop(is);

                        int dayBuy = -1;
                        for (ItemOption io : item.itemOptions) {
                            int optId = io.optionTemplate.id;
                            switch (optId) {
                                case 224: // có chỉ số hằng ngày
                                    dayBuy = io.param;
                                    break;
                            }
                        }
                        if (dayBuy != -1) {
                            boolean checkBuySusses = false;
                            for (DataShopReward shopDay : player.inventory.dShopDays) {
                                if (shopDay.target == dayBuy) {
                                    if (shopDay.isBuy) {// nếu đã mua
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận vật phẩm này rồi.");
                                        return;
                                    }
                                    if (!shopDay.tookAttendance) { // nếu chưa điểm danh
                                        Service.getInstance().sendThongBao(player,
                                                "Bạn cần điểm danh đủ " + dayBuy + " ngày để nhận vật phẩm");
                                        return;
                                    }
                                    // set mua
                                    shopDay.isBuy = true;
                                    checkBuySusses = true;
                                    break;
                                }
                            }
                            if (!checkBuySusses) {
                                Service.getInstance().sendThongBao(player,
                                        "Vật phẩm điểm danh không hợp lệ");
                                return;
                            }
                            // kiểm tra ngày đăng nhập đã đến chưa

                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Vật phẩm điểm danh không hợp lệ");
                            return;
                        }
                    } else if (player.iDMark.getShopId() == ConstNpc.SHOP_ONLINE) {
                        Item item = ItemService.gI().createItemFromItemShop(is);

                        int minuteOption = -1;
                        for (ItemOption io : item.itemOptions) {
                            int optId = io.optionTemplate.id;
                            switch (optId) {
                                case 226: // có chỉ số phút online
                                    minuteOption = io.param;
                                    break;
                            }
                        }
                        if (minuteOption != -1) {
                            boolean checkBuySusses = false;
                            for (DataShopReward shopTime : player.inventory.dShopTimes) {
                                if (shopTime.target == minuteOption) {
                                    if (shopTime.isBuy) {// nếu đã mua
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận vật phẩm này rồi.");
                                        return;
                                    }
                                    // if (!shopTime.tookAttendance) { // nếu chưa online đủ
                                    // Service.getInstance().sendThongBao(player,
                                    // "Bạn cần điểm danh đủ " + minuteOption + " ngày để nhận vật phẩm");
                                    // return;
                                    // }
                                    if (player.inventory.timeOnline < shopTime.target) { // nếu chưa online đủ
                                        Service.getInstance().sendThongBao(player,
                                                "Bạn cần online đủ " + minuteOption + " phút để nhận vật phẩm");
                                        return;
                                    }
                                    // set mua
                                    shopTime.isBuy = true;
                                    checkBuySusses = true;
                                    break;
                                }
                            }
                            if (!checkBuySusses) {
                                Service.getInstance().sendThongBao(player,
                                        "Vật phẩm không hợp lệ");
                                return;
                            }
                            // kiểm tra ngày đăng nhập đã đến chưa

                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Vật phẩm không hợp lệ");
                            return;
                        }
                    } else if (player.iDMark.getShopId() == ConstNpc.SHOP_MOC_NAP) {
                        // Kiểm tra xem shop free hằng ngày hay shop điểm danh
                        Item item = ItemService.gI().createItemFromItemShop(is);

                        int mocNapItem = -1;
                        for (ItemOption io : item.itemOptions) {
                            int optId = io.optionTemplate.id;
                            switch (optId) {
                                case 227: // mốc nạp
                                    mocNapItem = io.param * 1000;
                                    break;
                            }
                        }
                        if (mocNapItem != -1) {
                            boolean checkBuySusses = false;
                            for (DataShopReward shopNap : player.inventory.dShopNaps) {
                                if (shopNap.target == mocNapItem) {
                                    if (shopNap.isBuy) {// nếu đã mua
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận vật phẩm này rồi.");
                                        return;
                                    }
                                    if (player.event.getMocNapDaNhan() < shopNap.target) { // nếu chưa online đủ
                                        Service.getInstance().sendThongBao(player,
                                                "Mốc nạp hiện tại của bạn chưa đủ để nhận thưởng, mốc nạp cần đạt "
                                                + Util.numberToMoney(mocNapItem) + " để nhận vật phẩm");
                                        return;
                                    }
                                    // set mua
                                    shopNap.isBuy = true;
                                    checkBuySusses = true;
                                    break;
                                }
                            }
                            if (!checkBuySusses) {
                                Service.getInstance().sendThongBao(player,
                                        "Chưa đủ điều kiện vui lòng inbox admin");
                                return;
                            }
                            // kiểm tra ngày đăng nhập đã đến chưa

                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Chưa đủ điều kiện vui lòng inbox admin");
                            return;
                        }

                    } else if (player.iDMark.getShopId() == ConstNpc.SHOP_SIDE_TASK_DAY) {
                        // Kiểm tra mốc nhiệm vụ
                        Item item = ItemService.gI().createItemFromItemShop(is);

                        int dayBuy = -1;
                        for (ItemOption io : item.itemOptions) {
                            int optId = io.optionTemplate.id;
                            switch (optId) {
                                case 229: // có chỉ số nhiệm vụ
                                    dayBuy = io.param;
                                    break;
                            }
                        }
                        if (dayBuy != -1) {
                            boolean checkBuySusses = false;
                            for (DataShopReward shopSideTask : player.inventory.dShopTasks) {
                                if (shopSideTask.target == dayBuy) {
                                    if (shopSideTask.isBuy) {// nếu đã mua
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận vật phẩm này rồi.");
                                        return;
                                    }
                                    if (player.inventory.sideTaskToDay < shopSideTask.target) { // nếu chưa đủ nhiệm vụ
                                        Service.getInstance().sendThongBao(player,
                                                "Bạn cần hoàn thành " + dayBuy
                                                + " nhiệm vụ Siêu khó trong hôm nay để nhận vật phẩm");
                                        return;
                                    }
                                    if (!shopSideTask.tookAttendance) { // nếu chưa hoàn thành
                                        Service.getInstance().sendThongBao(player,
                                                "Bạn cần hoàn thành " + dayBuy
                                                + " nhiệm vụ Siêu khó trong hôm nay để nhận vật phẩm");
                                        return;
                                    }
                                    // set mua
                                    shopSideTask.isBuy = true;
                                    checkBuySusses = true;
                                    break;
                                }
                            }
                            if (!checkBuySusses) {
                                Service.getInstance().sendThongBao(player,
                                        "Vật phẩm nhận không hợp lệ");
                                return;
                            }
                            // kiểm tra ngày đăng nhập đã đến chưa

                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Vật phẩm nhận không hợp lệ");
                            return;
                        }
                    } else if (player.iDMark.getShopId() == ConstNpc.SHOP_POWER) {
                        Item item = ItemService.gI().createItemFromItemShop(is);

                        int minuteOption = -1;
                        for (ItemOption io : item.itemOptions) {
                            int optId = io.optionTemplate.id;
                            switch (optId) {
                                case 230: // có chỉ số mốc sức mạnh
                                    minuteOption = io.param;
                                    break;
                            }
                        }
                        if (minuteOption != -1) {
                            boolean checkBuySusses = false;
                            for (DataShopReward shopTime : player.inventory.dShopPowers) {
                                if (shopTime.target == minuteOption) {
                                    if (shopTime.isBuy) {// nếu đã mua
                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận vật phẩm này rồi.");
                                        return;
                                    }
                                    // if (!shopTime.tookAttendance) { // nếu chưa online đủ
                                    // Service.getInstance().sendThongBao(player,
                                    // "Bạn cần điểm danh đủ " + minuteOption + " ngày để nhận vật phẩm");
                                    // return;
                                    // }
                                    long powerTager = (long) shopTime.target * 1000000000l;
                                    if (player.nPoint.power < powerTager) { // nếu chưa online đủ
                                        Service.getInstance().sendThongBao(player,
                                                "Bạn cần đạt " + minuteOption + " tỷ sức mạnh để nhận vật phẩm");
                                        return;
                                    }
                                    // set mua
                                    shopTime.isBuy = true;
                                    checkBuySusses = true;
                                    break;
                                }
                            }
                            if (!checkBuySusses) {
                                Service.getInstance().sendThongBao(player,
                                        "Vật phẩm không hợp lệ");
                                return;
                            }
                            // kiểm tra ngày đăng nhập đã đến chưa

                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Vật phẩm không hợp lệ");
                            return;
                        }
                    }
                }
                switch (player.iDMark.getShopId()) {
                    case ConstNpc.SHOP_SANTA_1:
                        player.head = is.temp.part;
                        Service.getInstance().Send_Caitrang(player);
                        Service.getInstance().sendThongBao(player, "Đổi kiểu tóc thành công");
                        break;
                    case ConstNpc.SHOP_BA_HAT_MIT_0:
                        player.charms.addTimeCharms(is.temp.id, 60);
                        openShopBua(player, player.iDMark.getShopId(), 0);
                        break;
                    case ConstNpc.SHOP_BA_HAT_MIT_1:
                        player.charms.addTimeCharms(is.temp.id, 60 * 8);
                        openShopBua(player, player.iDMark.getShopId(), 1);
                        break;
                    case ConstNpc.SHOP_BA_HAT_MIT_2:
                        player.charms.addTimeCharms(is.temp.id, 60 * 24 * 30);
                        openShopBua(player, player.iDMark.getShopId(), 2);
                        break;
                    case ConstNpc.SHOP_BA_HAT_MIT_3:
                        player.charms.addTimeCharms(is.temp.id, 60);
                        openShopBua(player, player.iDMark.getShopId(), 3);
                        break;
                    case ConstNpc.SHOP_SANTA_LUCKY: {

                        Item item_get = ItemService.gI().createItemFromItemShop(is);
                        Item item = ItemService.gI().createItemFromItemLucky(item_get.template.id);
                        if (item != null) {
                            InventoryService.gI().addItemBag(player, item, 99);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Đổi thành công " + is.temp.name);
                        } else {

                            Service.getInstance().sendThongBao(player,
                                    "Có lỗi xảy ra, vui lòng báo admin");
                        }
                    }
                    break;
                    case ConstNpc.SHOP_BILL_HUY_DIET_0: {

                        Item item = ItemService.gI().createItemFromItemShop(is);
                        int param = 0;
                        if (Util.isTrue(2, 10)) {
                            param = Util.nextInt(10, 15);
                        } else if (Util.isTrue(3, 10)) {
                            param = Util.nextInt(0, 10);
                        }
                        for (ItemOption io : item.itemOptions) {
                            int optId = io.optionTemplate.id;
                            switch (optId) {
                                case 47: // giáp
                                case 6: // hp
                                case 26: // hp/30s
                                case 22: // hp k
                                case 0: // sức đánh
                                case 7: // ki
                                case 28: // ki/30s
                                case 23: // ki k
                                case 14: // crit
                                    io.param += ((long) io.param * param / 100);
                                    break;
                            }
                        }

                        // InventoryService.gI().subQuantityItemsBag(player, ticket, 99);
                        InventoryService.gI().addItemBag(player, item, 99);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Đổi thành công " + is.temp.name);

                    }

                    break;
                    case ConstNpc.SHOP_WHIS_THIEN_SU: {
                        int param = 0;
                        if (Util.isTrue(2, 10)) {
                            param = Util.nextInt(10, 15);
                        } else if (Util.isTrue(3, 10)) {
                            param = Util.nextInt(0, 10);
                        }
                        Item item = ItemService.gI().createItemFromItemShop(is);
                        for (ItemOption io : item.itemOptions) {
                            int optId = io.optionTemplate.id;
                            switch (optId) {
                                case 47: // giáp
                                case 6: // hp
                                case 26: // hp/30s
                                case 22: // hp k
                                case 0: // sức đánh
                                case 7: // ki
                                case 28: // ki/30s
                                case 23: // ki k
                                case 14: // crit
                                    io.param += ((long) io.param * param / 100);
                                    break;
                            }
                        }
                        // item.itemOptions.add(new ItemOption(41, 1));
                        InventoryService.gI().addItemBag(player, item, 99);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Đổi thành công " + is.temp.name);
                    }

                    break;
                    case ConstNpc.SHOP_SU_KIEN_TET: {
                        int pointExchange = 0;
                        int evPoint = player.event.getEventPoint();
                        for (ItemOption io : is.options) {
                            if (io.optionTemplate.id == 200) {
                                pointExchange = io.param;
                            }
                        }
                        if (pointExchange > 0) {
                            if (evPoint >= pointExchange) {
                                InventoryService.gI().addItemBag(player,
                                        ItemService.gI().createItemFromItemShop(is),
                                        99);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + is.temp.name);
                                player.event.subEventPoint(pointExchange);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ điểm sự kiện");
                            }
                        }
                    }

                    break;
                    case ConstNpc.SHOP_ONLINE:
                    case ConstNpc.SHOP_DIEM_DANH:
                    case ConstNpc.SHOP_MOC_NAP:
                    case ConstNpc.SHOP_SIDE_TASK_DAY:
                    case ConstNpc.SHOP_POWER: {
                        Item item = ItemService.gI().createItemFromItemShop(is);
                        List<ItemOption> filteredOptions = new ArrayList<>();
                        for (ItemOption io : item.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 224: // shop điểm danh
                                case 226: // shop online
                                case 227: // mốc nạp
                                case 228: // mốc nạp
                                case 229:// nhiệm vụ hằng ngày
                                case 230: // mốc sức mạnh
                                    continue;
                            }
                            filteredOptions.add(io);
                        }

                        item.itemOptions = filteredOptions;
                        InventoryService.gI().addItemBag(player, item, 99);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Nhận thưởng thành công " + is.temp.name);

                    }
                    break;
                    default:
                        InventoryService.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is), 99);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Mua thành công " + is.temp.name);
                        break;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
            }
            Service.getInstance().sendMoney(player);
        }

    }

    private boolean isLimitItem(int id) {
        return id >= ConstItem.DA_NANG_CAP_CAP_1 && id <= ConstItem.CONG_THUC_VIP_1086;
    }

    private int getBuyLimit(int id) {
        switch (id) {
            case ConstItem.DA_NANG_CAP_CAP_1:
            case ConstItem.DA_NANG_CAP_CAP_2:
            case ConstItem.DA_MAY_MAN_CAP_1:
            case ConstItem.DA_MAY_MAN_CAP_2:
            case ConstItem.CONG_THUC_VIP:
            case ConstItem.CONG_THUC_VIP_1085:
            case ConstItem.CONG_THUC_VIP_1086:
                return 10;
            case ConstItem.DA_NANG_CAP_CAP_3:
            case ConstItem.DA_MAY_MAN_CAP_3:
                return 5;
            case ConstItem.DA_NANG_CAP_CAP_4:
            case ConstItem.DA_MAY_MAN_CAP_4:
                return 2;
            case ConstItem.DA_NANG_CAP_CAP_5:
            case ConstItem.DA_MAY_MAN_CAP_5:
                return 1;
        }
        return -1;
    }

    // item reward lucky round---------------------------------------------------
    public void openListItemLuckyRound(Player player) {
        player.iDMark.setShopId(ConstNpc.MENU_ITEM_LUCKY_ROUND);
        InventoryService.gI().arrangeItems(Manager.LIST_ITEM_LUCKY_REWARD);
        Message msg;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(4);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Phần\nthưởng");
            int n = Manager.LIST_ITEM_LUCKY_REWARD.size()
                    - InventoryService.gI().getCountEmptyListItem(Manager.LIST_ITEM_LUCKY_REWARD);
            msg.writer().writeByte(n);
            for (int i = 0; i < n; i++) {
                Item item = Manager.LIST_ITEM_LUCKY_REWARD.get(i);
                msg.writer().writeShort(item.template.id);
                msg.writer().writeUTF("\n|2|Vật phẩm vòng quay");
                msg.writer().writeByte(item.itemOptions.size() + 1);
                boolean isSoLuong = false;
                for (ItemOption io : item.itemOptions) {
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                    if (io.optionTemplate.id == 31) {
                        isSoLuong = true;
                    }
                }
                // số lượng
                if (isSoLuong) {// nếu có rồi thì set trống
                    msg.writer().writeByte(73);
                    msg.writer().writeShort(item.quantity);
                } else {
                    msg.writer().writeByte(31);
                    msg.writer().writeShort(item.quantity);
                }

                //
                msg.writer().writeByte(1);
                if (item.template.type == 5) {
                    CaiTrang ct = Manager.gI().getCaiTrangByItemId(item.template.id);
                    msg.writer().writeByte(1);
                    msg.writer().writeShort(ct.getID()[0]);
                    msg.writer().writeShort(ct.getID()[1]);
                    msg.writer().writeShort(ct.getID()[2]);
                    msg.writer().writeShort(ct.getID()[3]);
                } else {
                    msg.writer().writeByte(0);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // item shop chế tạo---------------------------------------------------
    public void openListItemCheTao(Player player, int type) {
        player.iDMark.setShopId(ConstNpc.MENU_ITEM_LUCKY_ROUND);
        Message msg;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(NORMAL_SHOP);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Chế tạo");
            short[] itemList = null;
            if (type == 99) {
                itemList = ItemService.getAllItemOther();
            } else {
                itemList = ItemService.getAllPhuKienWhereType(type, player);
            }
            int n = itemList.length;

            msg.writer().writeByte(n);

            for (short idItem : itemList) {
                Item itemShop = ItemService.gI().createNewItem(idItem);
                ItemService.gI().OptionAllItem(itemShop, 0);
                msg.writer().writeShort(itemShop.template.id);
                msg.writer().writeInt(0);
                msg.writer().writeInt(0);
                msg.writer().writeByte(itemShop.itemOptions.size());
                for (ItemOption option : itemShop.itemOptions) {
                    msg.writer().writeByte(option.optionTemplate.id);
                    msg.writer().writeShort(option.param);
                }
                msg.writer().writeByte(1);
                CaiTrang caiTrang = Manager.gI().getCaiTrangByItemId(itemShop.template.id);
                msg.writer().writeByte(caiTrang != null ? 1 : 0);
                if (caiTrang != null) {
                    msg.writer().writeShort(caiTrang.getID()[0]);
                    msg.writer().writeShort(caiTrang.getID()[1]);
                    msg.writer().writeShort(caiTrang.getID()[2]);
                    msg.writer().writeShort(caiTrang.getID()[3]);
                }
            }

            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(ShopService.class, e);
        }

    }

    public void openListItemCheTaoOLD(Player player, int type) {
        player.iDMark.setShopId(ConstNpc.MENU_ITEM_LUCKY_ROUND);

        Message msg;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(4);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Phần\nthưởng");
            short[] itemList = null;
            if (type == 99) {
                itemList = ItemService.getAllItemOther();
            } else {
                itemList = ItemService.getAllPhuKienWhereType(type, player);
            }
            int n = itemList.length;
            msg.writer().writeByte(n);
            for (short idItem : itemList) {
                Item item = ItemService.gI().createNewItem(idItem);
                ItemService.gI().OptionAllItem(item, 0);
                msg.writer().writeShort(item.template.id);
                msg.writer().writeUTF("\n|2|Vật phẩm chế tạo");
                msg.writer().writeByte(item.itemOptions.size() + 1);
                for (ItemOption io : item.itemOptions) {
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                }
                // số lượng
                msg.writer().writeByte(31);
                msg.writer().writeShort(item.quantity);
                //
                msg.writer().writeByte(1);
                if (item.template.type == 5) {
                    CaiTrang ct = Manager.gI().getCaiTrangByItemId(item.template.id);
                    if (ct != null) {
                        msg.writer().writeByte(1);
                        msg.writer().writeShort(ct.getID()[0]);
                        msg.writer().writeShort(ct.getID()[1]);
                        msg.writer().writeShort(ct.getID()[2]);
                        msg.writer().writeShort(ct.getID()[3]);
                    } else {
                        msg.writer().writeByte(0);
                    }
                } else {
                    msg.writer().writeByte(0);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // item shop get free---------------------------------------------------
    public void openListItemFreetest(Player player, int type) {
        player.iDMark.setShopId(ConstNpc.MENU_ITEM_GET_FREE);
        Message msg;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(NORMAL_SHOP);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Rương\nFree");
            short[] itemList = null;
            if (type == 27) {
                itemList = ItemService.getAllItemfreeOther1();
            } else {
                itemList = ItemService.getAllItemfreeWhereType1(type, player);
            }
            int n = itemList.length;

            msg.writer().writeByte(n);

            for (short idItem : itemList) {
                Item itemShop = ItemService.gI().createNewItem(idItem);
                ItemService.gI().OptionAllItem(itemShop, 0);
                msg.writer().writeShort(itemShop.template.id);
                msg.writer().writeInt(0);
                msg.writer().writeInt(0);
                msg.writer().writeByte(itemShop.itemOptions.size());
                for (ItemOption option : itemShop.itemOptions) {
                    msg.writer().writeByte(option.optionTemplate.id);
                    msg.writer().writeShort(option.param);
                }
                msg.writer().writeByte(1);
                CaiTrang caiTrang = Manager.gI().getCaiTrangByItemId(itemShop.template.id);
                msg.writer().writeByte(caiTrang != null ? 1 : 0);
                if (caiTrang != null) {
                    msg.writer().writeShort(caiTrang.getID()[0]);
                    msg.writer().writeShort(caiTrang.getID()[1]);
                    msg.writer().writeShort(caiTrang.getID()[2]);
                    msg.writer().writeShort(caiTrang.getID()[3]);
                }
            }

            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(ShopService.class, e);
        }

    }

    // item reward lucky round---------------------------------------------------
    public void openBoxItemLuckyRound(Player player) {
        player.iDMark.setShopId(ConstNpc.SIDE_BOX_LUCKY_ROUND);
        InventoryService.gI().arrangeItems(player.inventory.itemsBoxCrackBall);
        Message msg;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(4);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Phần\nthưởng");
            int n = player.inventory.itemsBoxCrackBall.size()
                    - InventoryService.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall);
            msg.writer().writeByte(n);
            for (int i = 0; i < n; i++) {
                Item item = player.inventory.itemsBoxCrackBall.get(i);
                msg.writer().writeShort(item.template.id);
                msg.writer().writeUTF("\n|7|LUCKY BALL");
                boolean isSoLuong = false;
                msg.writer().writeByte(item.itemOptions.size() + 1);
                for (ItemOption io : item.itemOptions) {
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                    if (io.optionTemplate.id == 31) {
                        isSoLuong = true;
                    }
                }
                // số lượng
                // số lượng
                if (isSoLuong) {// nếu có rồi thì set trống
                    msg.writer().writeByte(73);
                    msg.writer().writeShort(item.quantity);
                } else {
                    msg.writer().writeByte(31);
                    msg.writer().writeShort(item.quantity);
                }
                //
                msg.writer().writeByte(1);
                if (item.template.type == 5) {
                    CaiTrang ct = Manager.gI().getCaiTrangByItemId(item.template.id);
                    msg.writer().writeByte(1);
                    msg.writer().writeShort(ct.getID()[0]);
                    msg.writer().writeShort(ct.getID()[1]);
                    msg.writer().writeShort(ct.getID()[2]);
                    msg.writer().writeShort(ct.getID()[3]);

                } else {
                    msg.writer().writeByte(0);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getItemSideBoxLuckyRound(Player player, byte type, int index) {
        if (index < 0 || index >= player.inventory.itemsBoxCrackBall.size()) {
            return;
        }
        Item item = player.inventory.itemsBoxCrackBall.get(index);
        switch (type) {
            case 0: // nhận
                if (item.isNotNullItem()) {
                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 31) {// số lương
                                item.quantity = io.param;
                                item.itemOptions.remove(io);
                                break;
                            }
                        }
                        InventoryService.gI().addItemBag(player, item, 0);
                        Service.getInstance().sendThongBao(player,
                                "Bạn nhận được " + (item.template.id == 189
                                        ? Util.numberToMoney(item.quantity) + " vàng"
                                        : item.template.name));
                        InventoryService.gI().sendItemBags(player);
                        InventoryService.gI().removeItem(player.inventory.itemsBoxCrackBall, index);
                        openBoxItemLuckyRound(player);
                    } else {
                        Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                }
                break;
            case 1: // xóa
                InventoryService.gI().subQuantityItem(player.inventory.itemsBoxCrackBall, item, item.quantity);
                openBoxItemLuckyRound(player);
                Service.getInstance().sendThongBao(player, "Xóa vật phẩm thành công");
                break;
            case 2: // nhận hết
                for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                    item = player.inventory.itemsBoxCrackBall.get(i);
                    if (item.isNotNullItem()) {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            for (ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id == 31) {// số lương
                                    item.quantity = io.param;
                                    item.itemOptions.remove(io);
                                    break;
                                }
                            }
                            if (InventoryService.gI().addItemBag(player, item, 0)) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                                Service.getInstance().sendThongBao(player,
                                        "Bạn nhận được " + (item.template.id == 189
                                                ? Util.numberToMoney(item.quantity) + " vàng"
                                                : item.template.name));
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy");
                            break;
                        }
                    } else {
                        break;
                    }
                }
                InventoryService.gI().sendItemBags(player);
                openBoxItemLuckyRound(player);
                break;
        }
    }
    // item reward---------------------------------------------------------------

    public void openBoxItemReward(Player player) {
        if (player.getSession().itemsReward == null) {
            player.getSession().initItemsReward();
        }
        player.iDMark.setShopId(ConstNpc.SIDE_BOX_ITEM_REWARD);
        Message msg;
        try {
            msg = new Message(-44);
            msg.writer().writeByte(4);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Phần\nthưởng");
            msg.writer().writeByte(player.getSession().itemsReward.size());
            for (Item item : player.getSession().itemsReward) {
                msg.writer().writeShort(item.template.id);
                msg.writer().writeUTF("\n|7|DRAGON LUCKY");
                List<ItemOption> itemOptions = item.getDisplayOptions();
                msg.writer().writeByte(itemOptions.size() + 1);
                for (ItemOption io : itemOptions) {
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                }
                // số lượng
                msg.writer().writeByte(31);
                msg.writer().writeShort(item.quantity);
                //
                msg.writer().writeByte(1);
                CaiTrang ct = Manager.gI().getCaiTrangByItemId(item.template.id);
                msg.writer().writeByte(ct != null ? 1 : 0);
                if (ct != null) {
                    msg.writer().writeShort(ct.getID()[0]);
                    msg.writer().writeShort(ct.getID()[1]);
                    msg.writer().writeShort(ct.getID()[2]);
                    msg.writer().writeShort(ct.getID()[3]);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void getItemSideBoxReward(Player player, byte type, int index) {
        if (index < 0 || index >= player.getSession().itemsReward.size()) {
            return;
        }
        Item item = player.getSession().itemsReward.get(index);
        switch (type) {
            case 0: // nhận
                if (item.isNotNullItem()) {
                    if (InventoryService.gI().getCountEmptyBag(player) != 0) {
                        InventoryService.gI().addItemBag(player, item, 0);
                        Service.getInstance().sendThongBao(player,
                                "Bạn nhận được " + (item.template.id == 189
                                        ? Util.numberToMoney(item.quantity) + " vàng"
                                        : item.template.name));
                        InventoryService.gI().sendItemBags(player);
                        player.getSession().itemsReward.remove(index);
                        openBoxItemReward(player);
                    } else {
                        Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                }
                break;
            case 1: // xóa
                player.getSession().itemsReward.remove(index);
                openBoxItemReward(player);
                Service.getInstance().sendThongBao(player, "Xóa vật phẩm thành công");
                break;
            case 2: // nhận hết
                for (int i = player.getSession().itemsReward.size() - 1; i >= 0; i--) {
                    item = player.getSession().itemsReward.get(i);
                    if (item.isNotNullItem()) {
                        if (InventoryService.gI().addItemBag(player, item, 0)) {
                            player.getSession().itemsReward.remove(i);
                            Service.getInstance().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng"
                                            : item.template.name));
                        }
                    } else {
                        break;
                    }
                }
                InventoryService.gI().sendItemBags(player);
                openBoxItemReward(player);
                break;
        }
        PlayerDAO.updateItemReward(player);

    }

    // --------------------------------------------------------------------------
    public void buyItem(Player player, byte type, int tempId) {
        switch (player.iDMark.getShopId()) {
            case ConstNpc.SIDE_BOX_LUCKY_ROUND:
                getItemSideBoxLuckyRound(player, type, tempId);
                break;
            case ConstNpc.SIDE_BOX_ITEM_REWARD:
                getItemSideBoxReward(player, type, tempId);
                break;
            case ConstNpc.MENU_ITEM_GET_FREE:
                Item item1 = ItemService.gI().createNewItem((short) tempId);
                ItemService.gI().OptionAllItem(item1, 0);
                InventoryService.gI().addItemBag(player, item1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa bú nhận được " + item1.template.name);
                break;
            case ConstNpc.MENU_ITEM_LUCKY_ROUND:
                if (player.isAdmin()) {
                    Item item = ItemService.gI().createNewItem((short) tempId);
                    ItemService.gI().OptionAllItem(item, 0);
                    InventoryService.gI().addItemBag(player, item);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + item.template.name);
                } else {
                    Service.getInstance().sendThongBaoOK(player,
                            "Đây chỉ là các phần thưởng khi bạn tham gia nâng cấp , không nhận được!");
                }

                break;
            default:
                buyItemShopNormal(player, getItemShop(player.iDMark.getShopId(), tempId));
                break;
        }
    }


    public void showConfirmSellItem(Player pl, int where, int index) {
        Item item = null;
        TransactionService.gI().cancelTrade(pl);
        if (where == 0) {
            if (index < 0 || index >= pl.inventory.itemsBody.size()) {
                return;
            }
            item = pl.inventory.itemsBody.get(index);
        } else {
            if (index < 0 || index >= pl.inventory.itemsBag.size()) {
                return;
            }
            item = pl.inventory.itemsBag.get(index);
        }
        if (item.isNotNullItem()) {
            long goldReceive = 0;
            if (item.template.id == 457) {
                UseItem.gI().closeTab(pl);
                UseItem.gI().closeTab(pl);
                Input.gI().ceateFormBanThoiVang(pl);
                return;
            } 
            // else if (item.template.id == 2011) {
            //     goldReceive = COST_LOCK_GOLD_BAR;
            // } 
            else if (item.template.id == 1429) { // thỏi vàng khóa
                goldReceive = COST_LOCK_GOLD_BAR;
            } else {
                goldReceive = item.quantity;
            }
            Message msg = new Message(7);
            try {
                msg.writer().writeByte(where);
                msg.writer().writeShort(index);
                msg.writer()
                        .writeUTF("Bạn có muốn bán\n x"
                                + (item.template.id == 457
                                || item.template.id == 1429 ? 1 : item.quantity)
                                + " "
                                + item.template.name
                                + "\nvới giá là " + Util.numberToMoney(goldReceive) + " vàng?");
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    public void sellItem(Player pl, int where, int index) {
        Item item = null;
        if (where == 0) {
            if (index < 0 || index >= pl.inventory.itemsBody.size()) {
                return;
            }
            item = pl.inventory.itemsBody.get(index);
        } else {
            if (index < 0 || index >= pl.inventory.itemsBag.size()) {
                return;
            }
            item = pl.inventory.itemsBag.get(index);
        }
        if (item != null && item.isNotNullItem()) {
            long goldReceive = 0;
            if (item.template.id == 457) {
                goldReceive = COST_GOLD_BAR;
            } 
            // else if (item.template.id == 2011) {
            //     goldReceive = COST_LOCK_GOLD_BAR;
            // } 
            else if (item.template.id == 1429) {
                goldReceive = COST_LOCK_GOLD_BAR; // 500tr
            } else {
                goldReceive = item.quantity;
            }
            if (pl.inventory.gold + goldReceive <= pl.inventory.getGoldLimit()) {
                if (where == 0) {
                    InventoryService.gI().subQuantityItemsBody(pl, item, item.quantity);
                    InventoryService.gI().sendItemBody(pl);
                    Service.getInstance().Send_Caitrang(pl);
                } else {
                    if (item.template.id == 457 || item.template.id == 1429) {
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        InventoryService.gI().subQuantityItemsBag(pl, item, item.quantity);
                    }
                    InventoryService.gI().sendItemBags(pl);
                }
                pl.inventory.addGold(goldReceive);
                pl.playerTask.achivements.get(ConstAchive.TRUM_NHAT_VE_CHAI).count++;
                PlayerService.gI().sendInfoHpMpMoney(pl);
                Service.getInstance().sendThongBao(pl, "Đã bán " + item.template.name
                        + " thu được " + Util.numberToMoney(goldReceive) + " vàng");
            } else {
                Service.getInstance().sendThongBao(pl, "Vàng sau khi bán vượt quá giới hạn");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        }
    }
}
