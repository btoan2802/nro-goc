package nro.jdbc.daos;

import nro.models.item.ItemLuckyRound;
import nro.models.item.ItemOption;
import nro.models.item.ItemOptionLuckyRound;
import nro.models.shop.ItemShop;
import nro.models.shop.Shop;
import nro.models.shop.TabShop;
import nro.server.Manager;
import nro.services.ItemService;
import nro.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShopDAO {

    public static List<Shop> getShops(Connection con) {
        List<Shop> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select * from shop order by npc_id asc, shop_order asc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Shop shop = new Shop();
                shop.id = rs.getInt(1);
                shop.npcId = rs.getByte(2);
                shop.shopOrder = rs.getByte(3);
                loadShopTab(con, shop);
                list.add(shop);
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            Log.error(ShopDAO.class, e);
        }
        return list;
    }

    private static void loadShopTab(Connection con, Shop shop) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from tab_shop where shop_id = ? order by id");
            ps.setInt(1, shop.id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TabShop tab = new TabShop();
                tab.shop = shop;
                tab.id = rs.getInt(1);
                tab.name = rs.getString(3).replaceAll("<>", "\n");
                loadItemShop(con, tab);
                shop.tabShops.add(tab);
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            Log.error(ShopDAO.class, e);
        }
    }

    private static void loadItemShop(Connection con, TabShop tabShop) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from item_shop where is_sell = 1 and tab_id = ? "
                    + "order by create_time desc");
            ps.setInt(1, tabShop.id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ItemShop itemShop = new ItemShop();
                itemShop.tabShop = tabShop;
                itemShop.id = rs.getInt(1);
                itemShop.temp = ItemService.gI().getTemplate(rs.getShort(3));
                itemShop.gold = rs.getInt(4);
                itemShop.gem = rs.getInt(5);
                itemShop.isNew = rs.getBoolean(6);
                itemShop.itemExchange = rs.getInt("item_exchange");
                if (itemShop.itemExchange != -1) {
                    itemShop.iconSpec = ItemService.gI().getTemplate(itemShop.itemExchange).iconID;
                    itemShop.costSpec = rs.getInt("quantity_exchange");
                }

                /* LAST OPTION */
                String jsonLast = rs.getString("first_option");
                JSONArray jsonLastArray = new JSONArray(jsonLast);
                for (int i = 0; i < jsonLastArray.length(); i++) {
                    JSONObject jsonObject = jsonLastArray.getJSONObject(i);
                    int optionId = jsonObject.getInt("id");
                    int optionParam = jsonObject.getInt("param");
                    itemShop.options.add(new ItemOption(optionId, optionParam));
                }
                /* NEW */
 /* NEW */
                String json = rs.getString("item_option");
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int optionId = jsonObject.getInt("id");
                    int optionParam = jsonObject.getInt("param");
                    itemShop.options.add(new ItemOption(optionId, optionParam));
                }

                /* NEW */
 /* Item Random */
                String jsonRandom = rs.getString("random_option");
                JSONArray jsonRandomArray = new JSONArray(jsonRandom);
                for (int i = 0; i < jsonRandomArray.length(); i++) {
                    JSONObject jsonObject = jsonRandomArray.getJSONObject(i);
                    int optionId = jsonObject.getInt("id");
                    int optionParam = jsonObject.getInt("max");
                    itemShop.options.add(new ItemOption(optionId, optionParam));
                }
                if (jsonRandomArray != null && jsonRandomArray.length() > 0) {
                    AddItemLucky(jsonRandomArray, rs.getShort(3));
                }
                /* NEW */
 /* optionSub */
                String jsonSub = rs.getString("sub_option");
                JSONArray jsonSubArray = new JSONArray(jsonSub);
                for (int i = 0; i < jsonSubArray.length(); i++) {
                    JSONObject jsonObject = jsonSubArray.getJSONObject(i);
                    int optionId = jsonObject.getInt("id");
                    int optionParam = jsonObject.getInt("param");
                    itemShop.options.add(new ItemOption(optionId, optionParam));
                }
                // loadItemShopOption(con, itemShop);
                tabShop.itemShops.add(itemShop);
            }
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            Log.error(ShopDAO.class, e);
        }
    }

    public static void AddItemLucky(JSONArray jsonArray, short idTemplate) {
        ItemLuckyRound item = new ItemLuckyRound();
        item.temp = ItemService.gI().getTemplate(idTemplate);
        item.ratio = 1;
        item.typeRatio = 1;
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int optionId = jsonObject.getInt("id");
                int param1 = jsonObject.getInt("min");
                int param2 = jsonObject.getInt("max");
                ItemOptionLuckyRound io = new ItemOptionLuckyRound(); // khởi tạo option lucky
                ItemOption itemOption = new ItemOption(optionId, 0); // lấy data option
                io.itemOption = itemOption;
                io.param1 = param1;
                if (param2 != -1) {
                    io.param2 = param2;
                }
                item.itemOptions.add(io);
            }
            Manager.LUCKY_SHOP.add(item);
        }

    }

}
