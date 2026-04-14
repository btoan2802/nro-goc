package nro.services;

import nro.jdbc.DBService;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.utils.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 💖 Arriety 💖
 * @copyright 💖 Aurora 💖
 *
 */
public class GiftService {

    private static GiftService i;

    private GiftService() {
    }

    public static GiftService gI() {
        if (i == null) {
            i = new GiftService();
        }
        return i;
    }

    public void use(Player player, String code) {
        int lent = code.length();
        if (code.equals("") || lent < 5 || lent > 30) {
            Service.getInstance().sendThongBaoOK(player, "Mã quà tặng có chiều dài từ 5 đến 30 ký tự.");
            return;
        }
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher m1 = p.matcher(code);
        if (!m1.find()) {
            Service.getInstance().sendThongBaoOK(player, "Mã quà tặng chỉ gồm chữ và số.");
            return;
        }
        code = code.toLowerCase();

        try (PreparedStatement stmt = DBService.gI().getConnectionForGame().prepareStatement(
                "SELECT * FROM `gift_codes` WHERE `code` like ? AND (expires_at IS NULL OR expires_at > now()) LIMIT 1;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            stmt.setString(1, code);
            try (ResultSet res = stmt.executeQuery()) {
                if (!res.first()) {
                    Service.getInstance().sendThongBaoOK(player, "Mã quà tặng không tồn tại hoặc đã hết hạn.");
                    return;
                }

                int id = res.getInt("id");
                byte status = res.getByte("status");
                byte type = res.getByte("type");
                boolean active = res.getBoolean("active");

                if (type == 0 && status == 1) {
                    Service.getInstance().sendThongBaoOK(player, "Mã quà tặng đã được sử dụng");
                    return;
                } else if (type == 1 && isUsedGiftCode((int) player.id, id)) {
                    Service.getInstance().sendThongBaoOK(player, "Mỗi người chỉ được sử dụng 1 lần.");
                    return;
                } else if (active && !player.getSession().actived) {
                    Service.getInstance().sendThongBao(player, "Cần kích hoạt tài khoản để nhận mã quà tặng này");
                    return;
                }

                int gold = res.getInt("gold");
                int gem = res.getInt("gem");
                int ruby = res.getInt("ruby");

                JSONArray arrItem = new JSONArray(res.getString("items"));
                int size = arrItem.length();

                if (size > InventoryService.gI().getCountEmptyBag(player)) {
                    Service.getInstance().sendThongBaoOK(player, "Bạn không đủ chỗ trống trong hành trang.");
                    return;
                }

                StringBuilder sb = new StringBuilder("Chúc mừng, bạn đã được tặng:\n");
                for (int i = 0; i < size; i++) {
                    JSONObject itemObj = arrItem.getJSONObject(i);
                    if (itemObj.has("id") && itemObj.has("quantity") && itemObj.has("options")) {
                        int itemID = itemObj.getInt("id");
                        int quantity = itemObj.getInt("quantity");
                        JSONArray options = itemObj.getJSONArray("options");

                        Item item = ItemService.gI().createNewItem((short) itemID, quantity);
                        for (int j = 0; j < options.length(); j++) {
                            JSONObject obj = options.getJSONObject(j);
                            if (obj.has("id") && obj.has("param")) {
                                int optionID = obj.getInt("id");
                                int param = obj.getInt("param");
                                item.itemOptions.add(new ItemOption(optionID, param));
                            }
                        }
                        item.createTime = System.currentTimeMillis();
                        InventoryService.gI().addItemBag(player, item, 0);
                        sb.append(String.format("- x%s %s\n", Util.numberToMoney(quantity), item.template.name));
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Dữ liệu item không hợp lệ.");
                        return;
                    }
                }

                if (gold > 0) {
                    player.inventory.addGold(gold);
                    sb.append(String.format("- %s vàng\n", Util.numberToMoney(gold)));
                }

                if (gem > 0) {
                    player.inventory.gem += gem;
                    sb.append(String.format("- %s ngọc xanh\n", Util.numberToMoney(gem)));
                }

                if (ruby > 0) {
                    player.inventory.ruby += ruby;
                    sb.append(String.format("- %s hồng ngọc\n", Util.numberToMoney(ruby)));
                }

                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                NpcService.gI().createTutorial(player, -1, sb.toString());

//                addUsedGiftCode((int) player.id, id, code);
if (type == 1) {
    addUsedGiftCode((int) player.id, id, code);
}

                if (type == 0) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    res.updateByte("status", (byte) 1);
                    res.updateTimestamp("updated_at", timestamp);
                    res.updateRow();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isUsedGiftCode(int playerID, int giftCodeId) {
        try (PreparedStatement stmt = DBService.gI().getConnectionForGame().prepareStatement(
                "SELECT * FROM `gift_code_histories` WHERE `gift_code_id` = ? AND `player_id` = ? LIMIT 1;",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setInt(1, giftCodeId);
            stmt.setInt(2, playerID);
            try (ResultSet res = stmt.executeQuery()) {
                return res.first();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addUsedGiftCode(int playerID, int giftCodeId, String code) {
        try (PreparedStatement stmt = DBService.gI().getConnectionForGame().prepareStatement(
                "INSERT INTO `gift_code_histories`(`player_id`, `gift_code_id`, `code`, `created_at`) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, playerID);
            stmt.setInt(2, giftCodeId);
            stmt.setString(3, code);
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
