package nro.jdbc.daos;

import nro.models.item.Item;
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
import nro.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @Stole By Arriety
 *
 */
public class LuckyDAO {

    private static LuckyDAO i;

    public static LuckyDAO gI() {
        if (i == null) {
            i = new LuckyDAO();
        }
        return i;
    }

    public void LoadItemLucky(Connection con) {
        // load lucky new
        Manager.LUCKY_ROUND_REWARDS.clear();
        Manager.LIST_ITEM_LUCKY_REWARD.clear();
        try {
            PreparedStatement ps = con.prepareStatement("select * from item_lucky where is_sell = 1 and tab_id = 1");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemLuckyRound item = new ItemLuckyRound();
                Item itemShow = ItemService.gI().createNewItem((short) rs.getInt(3));
                item.temp = ItemService.gI().getTemplate(rs.getInt(3));
                item.ratio = rs.getInt(4);
                item.typeRatio = rs.getInt(5);
                String json = rs.getString(8);
                int ratio_vv = rs.getInt(9);
                JSONArray jsonArray = new JSONArray(json);
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

                        itemShow.itemOptions.add(new ItemOption(optionId, param2));
                        item.itemOptions.add(io);

                    }
                }
                if (ratio_vv == -1) {

                    ItemOptionLuckyRound io = new ItemOptionLuckyRound(); // khởi tạo option lucky
                    ItemOption itemOption = new ItemOption(93, 0); // lấy data option
                    io.itemOption = itemOption;
                    io.param1 = 1;
                    io.param2 = 3;
                    item.itemOptions.add(io);
                }
                // thêm item vào vòng quay
                Manager.LUCKY_ROUND_REWARDS.add(item.ratio, item);
                Manager.LIST_ITEM_LUCKY_REWARD.add(itemShow);
                // tỉ lệ vĩnh viễn (3-10 chỉnh trên data là hợp lý)
                // nếu item có hsd thì thêm vào vòng quay
                if (ratio_vv > 0) {
                    // khởi tạo otiop lucky
                    ItemOptionLuckyRound io = new ItemOptionLuckyRound();
                    ItemOption itemOption = new ItemOption(93, 0);
                    io.itemOption = itemOption;
                    // tỉ lệ hsd từ 1 đến 3
                    io.param1 = 1;
                    io.param2 = 3;
                    item.itemOptions.add(io);
                    item.ratio = ratio_vv;
                    // kết thúc khởi tạo otion lucky
                    Manager.LUCKY_ROUND_REWARDS.add(item.ratio, item);

                }
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

}
