package nro.jdbc.daos;

import nro.models.item.Item;
import nro.models.item.ItemLuckyRound;
import nro.models.item.ItemOption;
import nro.models.item.ItemOptionLuckyRound;
import nro.models.shop.ItemShop;
import nro.models.shop.Shop;
import nro.models.shop.TabShop;
import nro.server.Manager;
import nro.server.SettingGame;
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

/**
 *
 * @Stole By Arriety
 *
 */
public class RatioDAO {

    private static RatioDAO i;

    public static RatioDAO gI() {
        if (i == null) {
            i = new RatioDAO();
        }
        return i;
    }

    public void Load(Connection con) {
        // load lucky new
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM setting WHERE type = 1");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String loai = rs.getString("loai");
                short code = rs.getShort("code");
                int id = rs.getInt("id");

                // Cập nhật thuộc tính dựa trên giá trị của 'loai'
                switch (loai) {
                    case "pha_le_hoa":
                        SettingGame.RATIO_PHA_LE_HOA = code;
                        break;
                    case "nang_cap":
                        SettingGame.RATIO_NANG_CAP = code;
                        break;
                    case "roi_item_rac":
                        SettingGame.RATIO_RAC = code;
                        break;
                    case "roi_dtl_cold":
                        SettingGame.RATIO_DTL_COLD = code;
                        break;
                    case "roi_do_tl_boss":
                        SettingGame.RATIO_GANG_TAY = code;
                        break;
                    default:

                        break;
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
