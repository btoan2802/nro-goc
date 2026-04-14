package nro.services.giftcode;

import nro.jdbc.DBService;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.InventoryServiceNew;
import nro.services.Service;
import nro.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GiftCodengocboi {

    private static GiftCodengocboi instance;

    public static GiftCodengocboi gI() {
        if (instance == null) {
            instance = new GiftCodengocboi();
        }
        return instance;
    }

    private boolean isUseGiftCode(int idPlayer, String code, Player player) throws Exception {
        try (Connection connection = DBService.gI().getConnectionForHisCode(); PreparedStatement psSelect = connection.prepareStatement(
                "SELECT * FROM giftcode_his WHERE `player_id` = ? AND `code` = ?;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY); PreparedStatement psInsert = connection.prepareStatement(
                        "INSERT INTO `giftcode_his` (`player_id`, `code`, `time`) VALUES (?, ?, ?);")) {

            psSelect.setInt(1, idPlayer);
            psSelect.setString(2, code);

            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs != null && rs.first()) {
                    Service.getInstance().sendThongBaoOK(player,
                            "Bạn đã nhập Gift-Code này vào: " + rs.getTimestamp("time"));
                    return true;
                } else {
                    psInsert.setInt(1, idPlayer);
                    psInsert.setString(2, code);
                    psInsert.setString(3, Util.toDateString(Date.from(Instant.now())));

                    psInsert.executeUpdate();
                    return false;
                }
            }
        } catch (Exception errorLog) {
            errorLog.printStackTrace();
        }
        Service.getInstance().sendThongBaoOK(player,
                "Lỗi không xác định, hãy thử lại");
        return true;
    }

    private void getCode(Player player, String code) throws Exception {
        try (Connection con = DBService.gI().getConnectionForCode(); PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM giftcode WHERE `Luot` >= 1 AND `Code` = ?;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {

            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs != null && rs.first()) {

                    HashMap<Integer, HashMap<Integer, Integer>> itemMap = new HashMap<>();
                    ArrayList<ItemOption> optionItem = new ArrayList<>();

                    if (rs.getString("Code").equals(code) && rs.getInt("Luot") > 0) {
                        JSONArray jar = (JSONArray) JSONValue.parse(rs.getString("Item"));
                        if (jar != null) {

                            for (Object obj : jar) {
                                JSONObject jsonObj = (JSONObject) obj;
                                HashMap<Integer, Integer> itemMap_hsd = new HashMap<>();
                                int id = Integer.parseInt(jsonObj.get("id").toString());
                                int soluong = Integer.parseInt(jsonObj.get("soluong").toString());
                                int hsd = Integer.parseInt(jsonObj.get("hsd").toString());
                                itemMap_hsd.put(soluong, hsd);
                                itemMap.put(id, itemMap_hsd);

                            }
                        } else {
                            Service.getInstance().sendThongBaoOK(player,
                                    "Lỗi lấy item code");
                            return;
                        }
                        JSONArray option = (JSONArray) JSONValue.parse(rs.getString("Option"));
                        if (option != null) {
                            for (Object obj : option) {
                                JSONObject jsonobject = (JSONObject) obj;
                                optionItem.add(new ItemOption(Integer.parseInt(jsonobject.get("id").toString()), Integer.parseInt(jsonobject.get("param").toString())));
                            }
                        } else {
                            Service.getInstance().sendThongBaoOK(player,
                                    "Lỗi lấy chỉ số vật phẩm");
                            return;
                        }
                        if (InventoryService.gI().getCountEmptyBag(player) < itemMap.size()) {
                            Service.getInstance().sendThongBaoOK(player,
                                    "Hành trang đầy, cần " + itemMap.size() + " ô trống");
                            return;
                        }
                        boolean isUsed = GiftCodengocboi.gI().isUseGiftCode((int) player.id, code, player);
                        if (!isUsed) {
                            int luot = rs.getInt("Luot") - 1;
                            ps.executeUpdate("UPDATE `giftcode` SET `Luot` = '" + luot + "' WHERE `Code` = '"
                                    + code + "' LIMIT 1;");
                            // psInsert.setInt(1, (int) player.id);
                            // psInsert.setString(2, code);
                            // psInsert.setString(3, Util.toDateString(Date.from(Instant.now())));
                            // psInsert.executeUpdate();

                            // InventoryService.gI().addItemGiftCodeToPlayer(player, itemMap);
                            // InventoryServiceNew.gI().addItemGiftCodeToPlayer(player, itemMap,option);
                        }
                        return;
                    }
                    return;
                } else {
                    Service.getInstance().sendThongBaoOK(player,
                            "Code " + code + " không tồn tại hoặc hết lượt sử dụng");
                }
            } catch (Exception errorLog) {
                errorLog.printStackTrace();
            }
        } catch (Exception errorLog) {
            errorLog.printStackTrace();
        }
    }

    public void giftCode(Player player, String code) throws Exception {
        Service.getInstance().sendThongBao(player, "Đang kiểm tra Gift-Code");
        Thread.sleep(1000);
        getCode(player, code);
    }
}
