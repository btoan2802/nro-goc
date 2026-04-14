package nro.models.player;

import java.util.ArrayList;
import java.util.List;
import nro.models.item.DataShopReward;
import nro.models.item.Item;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class Inventory {

    public static final long LIMIT_GOLD = 10_000_000_000_000l;
    public static final byte BAG_LIMIT = 120;
    private Player player;

    public Item trainArmor;

    public List<Item> itemsBody;
    public List<Item> itemsBag;
    public List<Item> itemsBox;
    public List<Item> itemsBox_ct_pet;
    public List<Item> itemsReward = new ArrayList();

    public List<DataShopReward> dShopDays;
    public List<DataShopReward> dShopTimes;
    public List<DataShopReward> dShopNaps;
    public List<DataShopReward> dShopTasks;
    public List<DataShopReward> dShopPowers;
    public List<Item> itemsBoxCrackBall;

    // public List<Item> itemsReward;
    public long gold, goldLimit;
    public int gem;
    public int ruby;
    public int topSm;
    public int topNv;
    public int topNap;
    public int vip_point;
    public int event_point, top_event;
    private int goldBar;
    public int top_suc_manh;
    public int top_suc_manh_de_tu;
    public int top_nhiem_vu;
    public int top_nap;
    public int top_suc_manh_tuan;

    public byte free_turn_buy_shop;
    public long time_buy_shop_today;
    public short timeOnline;
    public short sideTaskToDay;

    // Danh hiệu đã nhận
    public byte activeTitle_1;
    public byte activeTitle_2;
    public byte activeTitle_3;
    public byte activeTitle_4;

    // public int goldBar;
    public Inventory(Player player) {
        this.player = player;
        itemsBody = new ArrayList<>();
        itemsBag = new ArrayList<>();
        itemsBox = new ArrayList<>();
        itemsBox_ct_pet = new ArrayList<>();
        itemsBoxCrackBall = new ArrayList<>();
        dShopDays = new ArrayList<>();
        dShopTimes = new ArrayList<>();
        dShopNaps = new ArrayList<>();
        dShopTasks = new ArrayList<>();
        dShopPowers = new ArrayList<>();
    }

    public int getGem() {
        return this.gem;
    }

    public long getGold() {
        return this.gold;
    }

    public long getGoldLimit() {
        return LIMIT_GOLD;
    }

    public long getGoldDisplay() {
        long amount = gold;
        if (amount > Integer.MAX_VALUE && !player.isVersionAbove(214)) {
            return Integer.MAX_VALUE;
        }
        return amount;
    }

    public long getRuby() {
        return this.ruby;
    }

    public void subGem(int num) {
        this.gem -= num;
    }

    public void subGold(long num) {
        this.gold -= num;
    }

    public void subRuby(int num) {
        this.ruby -= num;
    }

    public void addGold(long gold) {
        this.gold += gold;
        long goldLimit = getGoldLimit();
        if (this.gold > goldLimit) {
            this.gold = goldLimit;
        }
    }

    public int getGoldBar() {
        return this.goldBar;
    }

    public void setGoldBar(int value) {
        this.goldBar = value;
    }

    public void addGoldBar(int value) {
        int baseAdd = this.goldBar + value;
        if (baseAdd < 0) {
            return;
        }
        this.goldBar += value;
    }

    public void subGoldBar(int value) {
        int baseAdd = this.goldBar - value;
        if (value < 0 || baseAdd < 0) {
            return;
        }
        this.goldBar -= value;
    }

    public int getGemLimit() {
        return gem + 2000000000;
    }

    public void addGem(int gem) {
        this.gem += gem;
        int gemLimit = getGemLimit();
        if (this.gem > gemLimit) {
            this.gem = gemLimit;
        }
    }

    public void dispose() {
        this.player = null;
        if (this.trainArmor != null) {
            this.trainArmor.dispose();
        }
        this.trainArmor = null;
        if (this.itemsBody != null) {
            for (Item it : this.itemsBody) {
                it.dispose();
            }
            this.itemsBody.clear();
        }
        if (this.itemsBag != null) {
            for (Item it : this.itemsBag) {
                it.dispose();
            }
            this.itemsBag.clear();
        }
        if (this.itemsBox != null) {
            for (Item it : this.itemsBox) {
                it.dispose();
            }
            this.itemsBox.clear();
        }
        if (this.itemsBox_ct_pet != null) {
            for (Item it : this.itemsBox_ct_pet) {
                it.dispose();
            }
            this.itemsBox_ct_pet.clear();
        }
        if (this.itemsBoxCrackBall != null) {
            for (Item it : this.itemsBoxCrackBall) {
                it.dispose();
            }
            this.itemsBoxCrackBall.clear();
        }
        if (this.dShopDays != null) {
            this.dShopDays.clear();
        }
        if (this.dShopTimes != null) {
            this.dShopTimes.clear();
        }
        if (this.dShopNaps != null) {
            this.dShopNaps.clear();
        }
        if (this.dShopTasks != null) {
            this.dShopTasks.clear();
        }
        if (this.dShopPowers != null) {
            this.dShopPowers.clear();
        }
        this.itemsBody = null;
        this.itemsBag = null;
        this.itemsBox = null;
        this.itemsBox_ct_pet = null;
        this.itemsBoxCrackBall = null;
        this.dShopDays = null;
        this.dShopTimes = null;
        this.dShopNaps = null;
        this.dShopTasks = null;
        this.dShopPowers = null;
    }
}
