package nro.manager;

import lombok.Getter;
import nro.jdbc.DBService;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.services.ItemService;
import nro.utils.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nro.models.player.Pet;
import nro.models.task.TaskMain;
import nro.services.TaskService;

/**
 * @Stole By Arriety
 */
public class TopManager {

    @Getter
    private List<Player> list = new ArrayList<>();
    @Getter
    private List<Player> listTopboss = new ArrayList<>();
    @Getter
    private List<Player> listTopGapthu = new ArrayList<>();
    @Getter
    private List<Player> listTopRuongbau = new ArrayList<>();

    @Getter
    private List<Player> listTopBauCua = new ArrayList<>();
    @Getter
    private List<Player> listTopEvent = new ArrayList<>();
    @Getter
    private List<Player> listTopNV = new ArrayList<>();
    @Getter
    private List<Player> listPointEvent = new ArrayList<>();
//    @Getter
//    private List<Player> listTopThoiVang = new ArrayList<>();

    private static final TopManager INSTANCE = new TopManager();

    public static TopManager getInstance() {
        return INSTANCE;
    }

    public void loadTopNV() {
        listTopNV.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY "
                    + "CAST(SUBSTRING_INDEX(data_task, ',', -2) AS UNSIGNED) DESC,"
                    + "CAST(SUBSTRING_INDEX(data_task, ',', -1) AS UNSIGNED) DESC,"
                    + "CAST(SUBSTRING_INDEX(data_task, ',', 1) AS UNSIGNED) DESC "
                    + " LIMIT 20;");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;

                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                dataArray = (JSONArray) jv.parse(rs.getString("data_task"));

                TaskMain taskMain = TaskService.gI().getTaskMainById(player,
                        Byte.parseByte(dataArray.get(1).toString()));
                taskMain.subTasks.get(Integer.parseInt(dataArray.get(2).toString())).count = Short
                        .parseShort(dataArray.get(0).toString());
                taskMain.index = Byte.parseByte(dataArray.get(2).toString());

                player.playerTask.taskMain = taskMain;

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId,
                                Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv
                                .parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();

                listTopNV.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void load() {
        list.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT p.* FROM player p JOIN account a ON a.id = p.account_id WHERE a.is_admin = 0 AND (a.ban IS NULL OR a.ban = 0) ORDER BY (COALESCE(p.power,0) + COALESCE(p.pet_power,0)) DESC LIMIT 20;");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;

                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                // Sửa lỗi cho player.nPoint.power
                dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                String powerStr = (dataArray.size() > 11 && dataArray.get(11) != null) ? dataArray.get(11).toString() : "0";
                player.nPoint.power = Long.parseLong(powerStr);
                dataArray.clear();

                // Sửa lỗi cho pet.nPoint.power
                if (rs.getString("pet_info") != "{}") {
                    Pet pet = new Pet(player);
                    dataObject = (JSONObject) jv.parse(rs.getString("pet_point"));
                    Object powerObj = dataObject.get("power");
                    String petPowerStr = (powerObj != null) ? String.valueOf(powerObj) : "0";
                    pet.nPoint.power = Long.parseLong(petPowerStr);
                    player.pet = pet;
                }

                if (player.pet != null) {
                    player.nPoint.tiemNang = player.nPoint.power + player.pet.nPoint.power;
                } else {
                    player.nPoint.tiemNang = player.nPoint.power;
                }

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId,
                                Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv
                                .parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();
                list.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void loadTopboss() {
        listTopboss.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY player.boss_point DESC LIMIT 20");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;
                Player player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.bosspoint = rs.getInt("boss_point");
                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId,
                                Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv
                                .parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();
                listTopboss.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void loadtopEvent() {
        listTopEvent.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY player.event_point DESC LIMIT 10");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;

                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                player.event.setEventPoint(rs.getInt("event_point"));
                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId,
                                Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv
                                .parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();
                listTopEvent.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void loadTopGapthu() {
        listTopGapthu.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY player.topGapthu DESC LIMIT 20");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;

                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                player.GapthuPoint = rs.getInt("topGapthu");
                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId,
                                Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv
                                .parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();
                listTopGapthu.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
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
        }
    }

    public void loadTopRuongBau() {
        listTopRuongbau.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY player.RuongBauPoint DESC LIMIT 20");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;

                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                player.RuongbauPoint = rs.getInt("RuongBauPoint");
                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId,
                                Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv
                                .parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();
                listTopRuongbau.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
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
        }
    }

    public void loadTopBaucuA() {
        listTopBauCua.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY player.BauCuaPoint DESC LIMIT 20");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;

                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                player.PauCuaPoint = rs.getInt("BauCuaPoint");

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId,
                                Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv
                                .parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();
                listTopBauCua.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
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
        }
    }

    public void loadTopSK() {
        listPointEvent.clear();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("SELECT * FROM player ORDER BY topevent LIMIT 20;");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;
                JSONObject dataObject = null;

                Player player = new Player();

                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");

                player.pointSK = rs.getInt("topevent");

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId,
                                Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv
                                .parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();
                dataObject.clear();
                listPointEvent.add(player);
            }
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

//    public void loadTopThoiVang() {
//        listTopThoiVang.clear();
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try ( Connection con = DBService.gI().getConnectionForGetPlayer();) {
//            ps = con.prepareStatement("SELECT * FROM player ORDER BY `share` LIMIT 10;");
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                JSONValue jv = new JSONValue();
//                JSONArray dataArray = null;
//                JSONObject dataObject = null;
//
//                Player player = new Player();
//
//                player.id = rs.getInt("id");
//                player.name = rs.getString("name");
//                player.head = rs.getShort("head");
//                player.gender = rs.getByte("gender");
//
//                player.pointThoiVang = rs.getInt("share");
//
//                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
//                for (int i = 0; i < dataArray.size(); i++) {
//                    Item item = null;
//                    dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
//                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
//                    if (tempId != -1) {
//                        item = ItemService.gI().createNewItem(tempId,
//                                Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
//                        JSONArray options = (JSONArray) jv
//                                .parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
//                        for (int j = 0; j < options.size(); j++) {
//                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
//                            item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
//                                    Integer.parseInt(String.valueOf(opt.get(1)))));
//                        }
//                        item.createTime = Long.parseLong(String.valueOf(dataObject.get("create_time")));
//                        if (ItemService.gI().isOutOfDateTime(item)) {
//                            item = ItemService.gI().createItemNull();
//                        }
//                    } else {
//                        item = ItemService.gI().createItemNull();
//                    }
//                    player.inventory.itemsBody.add(item);
//                }
//                dataArray.clear();
//                dataObject.clear();
//                listTopThoiVang.add(player);
//            }
//        } catch (Exception e) {
//            Log.error(PlayerDAO.class, e);
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (ps != null) {
//                    ps.close();
//                }
//            } catch (SQLException ex) {
//            }
//        }
//    }
}
