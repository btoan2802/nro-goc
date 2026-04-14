/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import nro.jdbc.DBService;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import static nro.server.Manager.SERVER;
import nro.services.ItemService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Arriety
 */
public class TopPlayerManager {

    public static List<Player> GetTopNap() {
        List<Player> players = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.data_point, p.items_body, p.head, p.gender, a.tongnap FROM player p JOIN account a ON p.account_id = a.id ORDER BY a.tongnap DESC LIMIT 10;";

        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            JSONValue jv = new JSONValue();

            while (rs.next()) {
                Player player = new Player();
                player.id = rs.getLong("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.tongNap = rs.getInt("tongnap");

                JSONArray dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                player.nPoint.power = Long.parseLong(dataArray.get(11).toString());

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONObject dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));

                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
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

                players.add(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }
    
    public static List<Player> GetTopSktrungthu() {
        List<Player> players = new ArrayList<>();
        String query = "SELECT id, name, data_point, items_body, head, gender, RuongBauPoint FROM player ORDER BY RuongBauPoint DESC LIMIT 10;";

        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            JSONValue jv = new JSONValue();

            while (rs.next()) {
                Player player = new Player();
                player.id = rs.getLong("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.RuongbauPoint = rs.getInt("RuongBauPoint");

                JSONArray dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                player.nPoint.power = Long.parseLong(dataArray.get(11).toString());

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONObject dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));

                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
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

                players.add(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    public static List<Clan> GetTopClanPoint() {
        List<Clan> clans = new ArrayList<>();
        String query = "SELECT id, name, img_id, clan_point FROM clan_sv" + SERVER + " ORDER BY clan_point DESC LIMIT 10;";

        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Clan clan = new Clan();
                clan.id = rs.getInt("id");
                clan.name = rs.getString("name");
                clan.imgId = rs.getByte("img_id");
                clan.clanPoint = rs.getInt("clan_point");
                clans.add(clan);
            }
        } catch (Exception e) {
            System.err.println("Error loading TopClanPoint: " + e.getMessage());
            e.printStackTrace();
        }
        return clans;
    }

    public static List<Player> GetTopBOSS() {
        List<Player> players = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.data_point, p.items_body, p.head, p.gender, p.boss_point FROM player p JOIN account a ON a.id = p.account_id WHERE a.is_admin = 0 AND (a.ban IS NULL OR a.ban = 0) ORDER BY COALESCE(p.boss_point,0) DESC LIMIT 10;";

        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            JSONValue jv = new JSONValue();

            while (rs.next()) {
                Player player = new Player();
                player.id = rs.getLong("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.bosspoint = rs.getInt("boss_point");

                JSONArray dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                player.nPoint.power = Long.parseLong(dataArray.get(11).toString());

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONObject dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));

                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
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

                players.add(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    public static List<Player> GetTopSaitv() {
        List<Player> players = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.data_point, p.items_body, p.head, p.gender, p.sell_tv FROM player p JOIN account a ON a.id = p.account_id WHERE a.is_admin = 0 AND (a.ban IS NULL OR a.ban = 0) ORDER BY p.sell_tv DESC LIMIT 10;";

        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            JSONValue jv = new JSONValue();

            while (rs.next()) {
                Player player = new Player();
                player.id = rs.getLong("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.pointThoiVang = rs.getInt("sell_tv");

                JSONArray dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                player.nPoint.power = Long.parseLong(dataArray.get(11).toString());

                dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONObject dataObject = (JSONObject) jv.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));

                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataObject.get("option")).replaceAll("\"", ""));
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

                players.add(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

}
