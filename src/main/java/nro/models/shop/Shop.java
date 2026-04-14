package nro.models.shop;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class Shop {

    public int id;

    public byte npcId;

    public byte shopOrder;

    public List<TabShop> tabShops;

    public Shop() {
        this.tabShops = new ArrayList<>();
    }

    public Shop(Shop shop, int gender) {
        this.tabShops = new ArrayList<>();
        this.id = shop.id;
        this.npcId = shop.npcId;
        this.shopOrder = shop.shopOrder;
        for (TabShop tabShop : shop.tabShops) {
            this.tabShops.add(new TabShop(tabShop, gender));
        }
    }

    public ItemShop getItemShop(int temp) {
        for (TabShop tab : this.tabShops) {
            for (ItemShop is : tab.itemShops) {
                if (is.temp.id == temp) {
                    return is;
                }
            }
        }
        return null;
    }

    public Shop(Shop shop) {
        this.id = shop.id;
        this.npcId = shop.npcId;
        this.shopOrder = shop.shopOrder;
        this.tabShops = new ArrayList<>();
        for (TabShop tabShop : shop.tabShops) {
            this.tabShops.add(new TabShop(tabShop));
        }
    }

}
