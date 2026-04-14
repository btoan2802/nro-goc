package nro.services.giftcode;

import nro.jdbc.DBService;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.InventoryServiceNew;
import nro.services.Service;
import nro.utils.Log;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class GiftCodeNew {

    private static GiftCodeNew instance;
    private static final ReentrantLock lock = new ReentrantLock();
    private BlockingQueue<PlayerCodeRequest> requestQueue = new ArrayBlockingQueue<>(100);

    public static GiftCodeNew gI() {
        if (instance == null) {
            instance = new GiftCodeNew();
        }
        return instance;
    }

    public void giftCode(Player player, String code) throws Exception {
        code = code.replaceAll("\\s", "").toLowerCase();
        if (!isValidGiftCode(code)) {
            Service.getInstance().sendThongBao(player, "Gift-Code không hợp lệ");
            return;
        }
        if (!Util.canDoWithTime(player.lastTimeDelay, 1000)) {
            Service.getInstance().sendThongBao(player, "Thao tác quá nhanh");
            return;
        }
        player.lastTimeDelay = System.currentTimeMillis();
        Service.getInstance().sendThongBao(player, "Đang kiểm tra Gift-Code");
        // Tạo yêu cầu và thêm vào hàng đợi
        try {
            Thread.sleep(500);
            if (player != null) {
                PlayerCodeRequest request = new PlayerCodeRequest(player, code);
                addToQueue(request);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private boolean isValidGiftCode(String code) {
        return code.matches("^[a-z0-9]+$");
    }

    private void addToQueue(PlayerCodeRequest request) {
        requestQueue.add(request); // Thêm yêu cầu vào hàng đợi
    }

    public void processRequests() throws Exception {
        while (true) {
            if (!requestQueue.isEmpty()) {
                PlayerCodeRequest request = requestQueue.poll(); // Lấy yêu cầu từ hàng đợi
                if (request != null) {
                    processRequest(request); // Xử lý yêu cầu
                }
            } else {
                Thread.sleep(300); // Nghỉ 1 giây trước khi kiểm tra lại hàng đợi
            }
        }
    }

    private void processRequest(PlayerCodeRequest request) throws Exception {
        String code = request.getCode();
        Player player = request.getPlayer();

        boolean locked = lock.tryLock(); // Thử khóa
        if (!locked) {
            // Xử lý khi không thể khóa
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (player != null) {
                con = DBService.gI().getConnectionForCode();
                ps = con.prepareStatement(
                        "SELECT * FROM giftcode WHERE `Luot` >= 1 AND `Code` = ? FOR UPDATE",
                        ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setString(1, code);
                rs = ps.executeQuery();
                if (rs != null && rs.first()) {
                    handleGiftCode(player, code, rs);
                } else {
                    Service.getInstance().sendThongBaoOK(player,
                            "Code " + code + " không tồn tại hoặc hết lượt sử dụng");
                }
            }
        } catch (SQLException | InterruptedException e) {
            handleException(player, "Lỗi không xác định, hãy thử lại", e);
        } finally {
            closeResources(rs, ps, con);
            lock.unlock(); // Mở khóa

            // Sau khi xử lý xong, xóa yêu cầu ra khỏi hàng đợi
            requestQueue.remove(request);
        }
    }

    private void handleGiftCode(Player player, String code, ResultSet rs) throws Exception {
        if (code.equals("vievip")) {
            if (!player.getSession().actived) {
                Service.getInstance().sendThongBaoOK(player, "Code chỉ dành cho mở thành viên !");
                return;
            }
        }
        if (code.equals("tanthu") || code.equals("vievip")) {
            player.lock = true;
        }
        Connection con = null;
        PreparedStatement updatePs = null;

        try {
            con = DBService.gI().getConnectionForCode(); // Obtain connection separately
            int luot = rs.getInt("Luot");
            int type = rs.getInt("type");
            HashMap<Integer, HashMap<Integer, Integer>> itemMap = parseItemMap(rs.getString("Item"));
            ArrayList<ItemOption> parseOptionItems = parseOptionItems(rs.getString("Option"));

            // Check if the gift code history is empty for the player
            if (!isEmptyHistoryCode((int) player.id, code, player)) {
                return; // Exit if history is not empty
            }

            // Check if player's inventory has enough space for items
            if (InventoryService.gI().getCountEmptyBag(player) < itemMap.size()) {
                Service.getInstance().sendThongBaoOK(player, "Hành trang đầy, cần " + itemMap.size() + " ô trống");
                return; // Exit if inventory is full
            }

            boolean isUsed;
            if (type == 1) {
                isUsed = isUseGiftCode((int) player.id, code, player);
            } else {
                isUsed = false;
            }
            if (!isUsed) {
                int setLuong = luot - 1;
                updatePs = con.prepareStatement(
                        "UPDATE `giftcode` SET `Luot` = ? WHERE `Code` = ? LIMIT 1");
                updatePs.setInt(1, setLuong);
                updatePs.setString(2, code);
                updatePs.executeUpdate(); // Execute update query

                // Add items from the gift code to player's inventory
                InventoryServiceNew.gI().addItemGiftCodeToPlayer(player, itemMap,parseOptionItems);
            }
        } catch (SQLException e) {
            handleException(player, "Lỗi khi cập nhật số lượt sử dụng", e);
        } finally {
            // Close PreparedStatement and Connection
            closeResources(null, updatePs, con);
        }
    }

    private HashMap<Integer, HashMap<Integer, Integer>> parseItemMap(String itemJson) {
        HashMap<Integer, HashMap<Integer, Integer>> itemMap = new HashMap<>();

        JSONArray jar = (JSONArray) JSONValue.parse(itemJson);
        if (jar != null) {
            for (Object obj : jar) {
                JSONObject jsonObj = (JSONObject) obj;
                int id = Integer.parseInt(jsonObj.get("id").toString());
                int soluong = Integer.parseInt(jsonObj.get("soluong").toString());
                int hsd = Integer.parseInt(jsonObj.get("hsd").toString());

                HashMap<Integer, Integer> itemMap_hsd = new HashMap<>();
                itemMap_hsd.put(soluong, hsd);
                itemMap.put(id, itemMap_hsd);
            }
        }
        return itemMap;
    }

    private ArrayList<ItemOption> parseOptionItems(String optionJson) {
        ArrayList<ItemOption> optionItem = new ArrayList<>();

        JSONArray option = (JSONArray) JSONValue.parse(optionJson);
        if (option != null) {
            for (Object obj : option) {
                JSONObject jsonObj = (JSONObject) obj;
                int id = Integer.parseInt(jsonObj.get("id").toString());
                int param = Integer.parseInt(jsonObj.get("param").toString());
                optionItem.add(new ItemOption(id, param));
            }
        }
        return optionItem;
    }

    private boolean isUseGiftCode(int idPlayer, String code, Player player) throws Exception {
        Connection connection = null;
        PreparedStatement psSelect = null;

        try {
            connection = DBService.gI().getConnectionForHisCode();
            psSelect = connection.prepareStatement(
                    "SELECT * FROM giftcode_his WHERE `player_id` = ? AND `code` = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            psSelect.setInt(1, idPlayer);
            psSelect.setString(2, code);

            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs != null && rs.first()) {
                    Service.getInstance().sendThongBaoOK(player,
                            "Bạn đã nhập Gift-Code này vào: " + rs.getTimestamp("time"));
                    return true;
                } else {
                    insertGiftCodeHistory(idPlayer, code);
                    return false;
                }
            }
        } catch (SQLException e) {
            handleException(player, "Lỗi không xác định, hãy thử lại", e);
            return true;
        } finally {
            closeResources(null, psSelect, connection);
        }
    }

    private void insertGiftCodeHistory(int idPlayer, String code) throws Exception {
        Connection connection = null;
        PreparedStatement psInsert = null;

        try {
            connection = DBService.gI().getConnectionForHisCode();
            psInsert = connection.prepareStatement(
                    "INSERT INTO `giftcode_his` (`player_id`, `code`, `time`) VALUES (?, ?, ?)");
            psInsert.setInt(1, idPlayer);
            psInsert.setString(2, code);
            psInsert.setString(3, Util.toDateString(Date.from(Instant.now())));
            psInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception here
        } finally {
            closeResources(null, psInsert, connection);
        }
    }

    private boolean isEmptyHistoryCode(int idPlayer, String code, Player player) throws Exception {
        Connection connection = null;
        PreparedStatement psSelect = null;

        try {
            connection = DBService.gI().getConnectionForHisCode();
            psSelect = connection.prepareStatement(
                    "SELECT * FROM giftcode_his WHERE `player_id` = ? AND `code` = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            psSelect.setInt(1, idPlayer);
            psSelect.setString(2, code);

            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs != null && rs.first()) {
                    Service.getInstance().sendThongBaoOK(player,
                            "Bạn đã nhập Gift-Code này vào: " + rs.getTimestamp("time"));
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            handleException(player, "Lỗi không xác định, hãy thử lại", e);
            return false;
        } finally {
            closeResources(null, psSelect, connection);
        }
    }

    private void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleException(Player player, String message, Exception e) {
        e.printStackTrace();
        if (player != null) {
            Service.getInstance().sendThongBaoOK(player, message);
        }

    }

    private static class PlayerCodeRequest {

        private final Player player;
        private final String code;

        public PlayerCodeRequest(Player player, String code) {
            this.player = player;
            this.code = code;
        }

        public Player getPlayer() {
            return player;
        }

        public String getCode() {
            return code;
        }
    }
}
