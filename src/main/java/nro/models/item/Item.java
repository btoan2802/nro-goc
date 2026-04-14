package nro.models.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import nro.consts.ConstItem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import nro.jdbc.DBService;
import nro.jdbc.daos.AccountDAO;
import nro.jdbc.daos.ShopDAO;
import nro.services.ItemService;
import nro.utils.Log;
import nro.utils.Util;
import org.json.JSONArray;
import org.json.JSONObject;

public class Item {

    private static final ItemOption OPTION_NULL = new ItemOption(73, 0);

    public ItemTemplate template;

    public String info;

    public String content;

    public int quantity;

    public List<ItemOption> itemOptions;

    public long createTime;

    public boolean isNotNullItem() {
        return this.template != null;
    }

    public Item() {
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    public String getInfo() {
        String strInfo = "";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString();
        }
        return strInfo;
    }

    public String getInfoItem() {
        String strInfo = "|1|" + template.name + "\n|0|";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString() + "\n";
        }
        strInfo += "|2|" + template.description;
        return strInfo;
    }

    public List<ItemOption> getDisplayOptions() {
        List<ItemOption> list = new ArrayList<>();
        if (itemOptions.isEmpty()) {
            list.add(OPTION_NULL);
        } else {
            for (ItemOption o : itemOptions) {
                list.add(o.format());
            }
        }
        return list;
    }

    public String getContent() {
        return "Yêu cầu sức mạnh " + this.template.strRequire + " trở lên";
    }

    public boolean canConsign() {
        if (template.id == ConstItem.THOI_VANG) {
            return false;
        }
        for (ItemOption o : itemOptions) {
            int optionId = o.optionTemplate.id;
            if (optionId == 86) {
                return true;
            }
        }
        return false;
    }

    public void dispose() {
        this.template = null;
        this.info = null;
        this.content = null;
        if (this.itemOptions != null) {
            for (ItemOption io : this.itemOptions) {
                io.dispose();
            }
            this.itemOptions.clear();
        }
        this.itemOptions = null;
    }

    // head-avatar2
    public static class ArrHead2Frames {

        public List<Integer> frames = new ArrayList<>();

    }

    public short getId() {
        return template.id;
    }

    public byte getType() {
        return template.type;
    }

    public String getName() {
        return template.name;
    }

    public String typeName() {
        switch (this.template.type) {
            case 0:
                return "Áo";
            case 1:
                return "Quần";
            case 2:
                return "Găng";
            case 3:
                return "Giày";
            case 4:
                return "Rada";
            default:
                return "";
        }
    }

    public boolean isTinhThachNangCap() {
        return this.template.id >= 1484 && this.template.id <= 1488;
    }

    public boolean isLTHUNANGCAP() {
        return this.template.id == 1283 || this.template.id == 2053 || this.template.id == 2054
                || this.template.id == 1288 || this.template.id == 1278;
    }

    public boolean isDHD() {
        if (this.template.id >= 650 && this.template.id <= 662) {
            return true;
        }
        return false;
    }

    public boolean isDTL() {
        if (this.template.id >= 555 && this.template.id <= 567) {
            return true;
        }
        return false;
    }

    public boolean isMTS() {
        return this.template.id >= 1066 && this.template.id <= 1070;
    }

    public boolean isCT() {
        return this.template.id >= 1071 && this.template.id <= 1073;
    }

    public boolean isDMM() {
        return this.template.id >= 1080 && this.template.id <= 1084;
    }

    public boolean isDANANGCAP() {
        return this.template.id >= 1074 && this.template.id <= 1078;
    }

    public boolean isThucAn() {
        if (this.template.id >= 663 && this.template.id <= 667) {
            return true;
        }
        return false;
    }

    public boolean isTrangBiHacHoa() {
        return this.template.type == 5 || this.template.type == 98 || this.template.type == 11;
    }

    public boolean haveOption(int idOption) {
        if (this != null && this.isNotNullItem()) {
            return this.itemOptions.stream().anyMatch(op -> op != null && op.optionTemplate.id == idOption);
        }
        return false;
    }

    public int getIndexOption(int idOption) {
        if (this != null && this.isNotNullItem()) {
            for (int i = 0; i < this.itemOptions.size(); i++) {
                if (this.itemOptions.get(i).optionTemplate.id == idOption) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean isSKH() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id >= 127 && itemOption.optionTemplate.id <= 135) {
                return true;
            }
        }
        return false;
    }

    public boolean isCanSKH() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id == 207) {
                return true;
            }
        }
        return false;
    }

    public boolean isCheTao() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id == 236) {
                return true;
            }
        }
        return false;
    }

    public Item GetOptionItem(int id) {
        Item it = ItemService.gI().createNewItem((short) id);
        try (Connection con = DBService.gI().getConnectionForGame();) {
            PreparedStatement ps = con.prepareStatement("select * from item_template where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String json = rs.getString("option_reward");
                JSONArray jsonArray = new JSONArray(json);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int optionId = jsonObject.getInt("id");
                        int param1 = jsonObject.getInt("min");
                        int param2 = jsonObject.getInt("max");
                        ItemOption itemOption = new ItemOption(optionId, Util.nextInt(param1, param2));
                        it.itemOptions.add(itemOption);
                    }
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
        return it;
    }
}
