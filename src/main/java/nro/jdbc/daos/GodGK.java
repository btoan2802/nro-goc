package nro.jdbc.daos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nro.card.Card;
import nro.card.CollectionBook;
import nro.consts.ConstAchive;
import nro.consts.ConstMap;
import nro.consts.ConstPlayer;
import nro.jdbc.DBService;
import nro.manager.AchiveManager;
import nro.manager.PetFollowManager;
import nro.models.player.PetFollow;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.item.DataShopReward;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.ItemTime;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.npc.specialnpc.MagicTree;
import nro.models.player.*;
import nro.models.skill.Skill;
import nro.models.task.Achivement;
import nro.models.task.AchivementTemplate;
import nro.models.task.TaskMain;
import nro.server.Client;
import nro.server.Manager;
import nro.server.io.Session;
import nro.server.model.AntiLogin;
import nro.services.*;
import nro.utils.Log;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nro.models.npc.specialnpc.KaminEgg;
import nro.sendEff.SendEffect;

public class GodGK {

    public static boolean login(Session session, AntiLogin al) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = DBService.gI().getConnectionForLogin();
            String query = "select * from account where username = ? and password = ? limit 1";
            ps = conn.prepareStatement(query);
            ps.setString(1, session.uu);
            ps.setString(2, session.pp);
            rs = ps.executeQuery();
            if (rs.next()) {
                session.userId = rs.getInt("account.id");
                Session plInGame = Client.gI().getSession(session);
                if (plInGame != null) {
                    Service.getInstance().sendThongBaoOK(plInGame, "Máy chủ tắt hoặc mất sóng!");
                    Client.gI().kickSession(plInGame);
                    Service.getInstance().sendThongBaoOK(session, "Máy chủ tắt hoặc mất sóng!");
                    return false;
                }

                session.isAdmin = rs.getBoolean("is_admin");
                session.lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                session.actived = rs.getBoolean("active");
                session.goldBar = rs.getInt("account.thoi_vang");
                session.dataReward = rs.getString("reward");
                session.VND = rs.getInt("vnd");
                session.count_card = rs.getInt("count_card");
                if (rs.getTimestamp("last_time_login").getTime() > session.lastTimeLogout) {
                    Service.getInstance().sendThongBaoOK(session, "Vui lòng đăng nhập lại !!!!");
                    return false;
                }

                if (rs.getBoolean("ban")) {
                    Service.getInstance().sendThongBaoOK(session, "Tài khoản đã bị khóa do vi phạm điều khoản!");
                } else {
                    long lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                    int secondsPass = (int) ((System.currentTimeMillis() - lastTimeLogout) / 1000);
                    if (secondsPass < Manager.SECOND_WAIT_LOGIN && !session.isAdmin) {
                        Service.getInstance().sendThongBaoOK(session, "Vui lòng chờ "
                                + (Manager.SECOND_WAIT_LOGIN - secondsPass) + " giây để đăng nhập lại.");
                    }
                }
                al.reset();
                return true;
            } else {
                Service.getInstance().sendThongBaoOK(session, "Thông tin tài khoản hoặc mật khẩu không chính xác");
                al.wrong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                }
            }
        }
        return false;
    }

    public static Player loadPlayer(Session session) {

        try {
            Connection connection = DBService.gI().getConnectionForLogin();
            PreparedStatement ps = connection.prepareStatement("select * from player where account_id = ? limit 1");
            ps.setInt(1, session.userId);
            ResultSet rs = ps.executeQuery();
            try {
                //  Logger.warning("Login");
                if (rs.next()) {
                    int plHp = 200000000;
                    int plMp = 200000000;
                    JSONValue jv = new JSONValue();
                    JSONArray dataArray = null;
                    JSONObject dataObject = null;

                    Player player = new Player();               
                    // base info
                    player.id = rs.getInt("id");
                    player.name = rs.getString("name");
                    player.head = rs.getShort("head");
                    player.gender = rs.getByte("gender");
                    player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                    player.time_create = rs.getString("create_time");
                    if (TimeUtil.isDuaTopSmTuan(player.time_create)) {
                        player.isDuaTop = true;
                    }
                    int clanId = rs.getInt("clan_id_sv" + Manager.SERVER);
                    if (clanId != -1) {
                        try {
                            Clan clan = ClanService.gI().getClanById(clanId);
                            if (clan != null) {
                                for (ClanMember cm : clan.getMembers()) {
                                    if (cm.id == player.id) {
                                        clan.addMemberOnline(player);
                                        player.clan = clan;
                                        player.clanMember = cm;
                                        player.setBuff(clan.getBuff());
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.error(GodGK.class, e, "Loi load clan");
                        }

                    }
                    player.tetduong = rs.getInt("tetduong");
                    player.diemtuanloc = rs.getInt("diemtuanloc");
                    //napdau
                    player.napDau = rs.getInt("napdau");
                    //vip
                    // player.vip = rs.getInt("vip");
                    player.vip1 = rs.getInt("vip1");
                    //điểm sk
                    player.pointSK = rs.getInt("topevent");
                    //điểm thỏi vàng
                    player.pointThoiVang = rs.getInt("sell_tv");
                    // diem su kien
                    int evPoint = rs.getInt("event_point");
                    player.event.setEventPoint(evPoint);
                    // điểm vip nạp
                    int vip_point = rs.getInt("vip_point");
                    player.inventory.vip_point = vip_point;
                    // điểm gắp thú
                    player.GapthuPoint = rs.getInt("topGapthu");

                    // điểm rương báu
                    player.RuongbauPoint = rs.getInt("RuongBauPoint");
                    player.lastTimeBan2 = Long.parseLong(String.valueOf(rs.getString("time_ban")));
                    // điểm Bầu cua
                    player.PauCuaPoint = rs.getInt("BauCuaPoint");
                    // điểm boss
                    player.bosspoint = rs.getInt("boss_point");

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("sk_tet"));
                    int timeBanhTet = Integer.parseInt(dataArray.get(0).toString());
                    int timeBanhChung = Integer.parseInt(dataArray.get(1).toString());
                    boolean isNauBanhTet = Integer.parseInt(dataArray.get(2).toString()) == 1;
                    boolean isNauBanhChung = Integer.parseInt(dataArray.get(3).toString()) == 1;
                    boolean receivedLuckMoney = Integer.parseInt(dataArray.get(4).toString()) == 1;

                    player.event.setTimeCookTetCake(timeBanhTet);
                    player.event.setTimeCookChungCake(timeBanhChung);
                    player.event.setCookingTetCake(isNauBanhTet);
                    player.event.setCookingChungCake(isNauBanhChung);
                    player.event.setReceivedLuckyMoney(receivedLuckMoney);
                    dataArray.clear();

                    // data kim lượng
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                    player.inventory.gold = Long.parseLong(dataArray.get(0).toString());
                    player.inventory.gem = Integer.parseInt(dataArray.get(1).toString());
                    player.inventory.ruby = Integer.parseInt(dataArray.get(2).toString());
                    player.inventory.goldLimit = Long.parseLong(dataArray.get(3).toString());
                    player.inventory.topSm = Integer.parseInt(dataArray.get(4).toString());
                    player.inventory.topNv = Integer.parseInt(dataArray.get(5).toString());
                    player.inventory.topNap = Integer.parseInt(dataArray.get(6).toString());
                    player.inventory.event_point = Integer.parseInt(dataArray.get(7).toString());
                    player.inventory.top_event = Integer.parseInt(dataArray.get(8).toString());
                    player.inventory.activeTitle_1 = Byte.parseByte(dataArray.get(9).toString());
                    player.inventory.activeTitle_2 = Byte.parseByte(dataArray.get(10).toString());
                    player.inventory.activeTitle_3 = Byte.parseByte(dataArray.get(11).toString());
                    player.inventory.activeTitle_4 = Byte.parseByte(dataArray.get(12).toString());
                    dataArray.clear();
                    // data top
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("data_top"));
                    player.inventory.top_suc_manh = Integer.parseInt(dataArray.get(0).toString());
                    player.inventory.top_suc_manh_de_tu = Integer.parseInt(dataArray.get(1).toString());
                    player.inventory.top_nhiem_vu = Integer.parseInt(dataArray.get(2).toString());
                    player.inventory.top_nap = Integer.parseInt(dataArray.get(3).toString());
                    player.inventory.top_suc_manh_tuan = Integer.parseInt(dataArray.get(4).toString());
                    dataArray.clear();

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("data_Moc_ThuongDe"));
                    player.mocThuongDe = new boolean[dataArray.size()];
                    for (int i = 0; i < dataArray.size(); i++) {
                        player.mocThuongDe[i] = Integer.parseInt(dataArray.get(i).toString()) != 0;
                    }
                    dataArray.clear();
                    //
                    player.event.setDiemTichLuy(session.diemTichNap);
                    player.event.setMocNapDaNhan(rs.getInt("moc_nap"));
                    player.server = session.server;
                    // data tọa độ
                    try {
                        dataArray = (JSONArray) jv.parse(rs.getString("data_location"));
                        player.location.x = Integer.parseInt(dataArray.get(0).toString());
                        player.location.y = Integer.parseInt(dataArray.get(1).toString());
                        int mapId = Integer.parseInt(dataArray.get(2).toString());
                        if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                                || MapService.gI().isMapBanDoKhoBau(mapId) || mapId == 126
                                || mapId == ConstMap.CON_DUONG_RAN_DOC
                                || mapId == ConstMap.CON_DUONG_RAN_DOC_142 || mapId == ConstMap.CON_DUONG_RAN_DOC_143
                                || mapId == ConstMap.HOANG_MAC) {
                            mapId = player.gender + 21;
                            player.location.x = 300;
                            player.location.y = 336;
                        } else if (MapService.gI().isMapBH(mapId)) {
                            mapId = 13;
                            player.location.x = 262;
                            player.location.y = 264;
                        }
                        player.zone = MapService.gI().getMapCanJoin(player, mapId);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dataArray.clear();
                    // data free shop
                    try {
                        dataArray = (JSONArray) jv.parse(rs.getString("freeShop"));
                        player.inventory.free_turn_buy_shop = Byte.parseByte(dataArray.get(0).toString());
                        player.inventory.time_buy_shop_today = Long.parseLong(dataArray.get(1).toString());
                        player.inventory.timeOnline = Short.parseShort(dataArray.get(2).toString());
                        player.inventory.sideTaskToDay = Short.parseShort(dataArray.get(3).toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dataArray.clear();
                    // data chỉ số
                    dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
                    plMp = Integer.parseInt(dataArray.get(1).toString());
                    player.nPoint.mpg = Integer.parseInt(dataArray.get(2).toString());
                    player.nPoint.critg = Byte.parseByte(dataArray.get(3).toString());
                    player.nPoint.limitPower = Byte.parseByte(dataArray.get(4).toString());
                    player.nPoint.stamina = Short.parseShort(dataArray.get(5).toString());
                    plHp = Integer.parseInt(dataArray.get(6).toString());
                    player.nPoint.defg = Integer.parseInt(dataArray.get(7).toString());
                    player.nPoint.tiemNang = Long.parseLong(dataArray.get(8).toString());
                    player.nPoint.maxStamina = Short.parseShort(dataArray.get(9).toString());
                    player.nPoint.dameg = Integer.parseInt(dataArray.get(10).toString());
                    player.nPoint.power = Long.parseLong(dataArray.get(11).toString());
                    player.nPoint.hpg = Integer.parseInt(dataArray.get(12).toString());
                    dataArray.clear();

                    // data đậu thần
                    dataArray = (JSONArray) jv.parse(rs.getString("data_magic_tree"));
                    boolean isUpgrade = Byte.parseByte(dataArray.get(0).toString()) == 1;
                    long lastTimeUpgrade = Long.parseLong(dataArray.get(1).toString());
                    byte level = Byte.parseByte(dataArray.get(2).toString());
                    long lastTimeHarvest = Long.parseLong(dataArray.get(3).toString());
                    byte currPea = Byte.parseByte(dataArray.get(4).toString());
                    player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade,
                            lastTimeUpgrade);
                    dataArray.clear();

                    // data phần thưởng sao đen
                    dataArray = (JSONArray) jv.parse(rs.getString("data_black_ball"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray reward = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(reward.get(0).toString());
                        player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(reward.get(1).toString());
                        reward.clear();
                    }
                    dataArray.clear();

                    // data body
                    dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId,
                                    Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
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

                    // data bag
                    dataArray = (JSONArray) jv.parse(rs.getString("items_bag"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId,
                                    Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
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
                        player.inventory.itemsBag.add(item);
                    }
                    dataArray.clear();
                    dataObject.clear();

                    // data box
                    dataArray = (JSONArray) jv.parse(rs.getString("items_box"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId,
                                    Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
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
                        player.inventory.itemsBox.add(item);
                    }
                    dataArray.clear();
                    dataObject.clear();

                    // data pet ct
                    dataArray = (JSONArray) jv.parse(rs.getString("items_box_pet_ct"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId,
                                    Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
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
                        player.inventory.itemsBox_ct_pet.add(item);
                    }
                    dataArray.clear();
                    dataObject.clear();

                    // data box lucky round
                    dataArray = (JSONArray) jv.parse(rs.getString("items_box_lucky_round"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        dataObject = (JSONObject) dataArray.get(i);
                        short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId,
                                    Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                            JSONArray options = (JSONArray) dataObject.get("option");
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) options.get(j);
                                item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsBoxCrackBall.add(item);
                    }
                    dataArray.clear();
                    dataObject.clear();

                    // DATA SHOP DAY
                    dataArray = (JSONArray) jv.parse(rs.getString("dayReward"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        DataShopReward shopDay = new DataShopReward();
                        dataObject = (JSONObject) dataArray.get(i);
                        byte isBuy = Byte.parseByte(String.valueOf(dataObject.get("isBuy")));
                        byte tookAttendance = Byte.parseByte(String.valueOf(dataObject.get("tookAttendance")));
                        byte day = Byte.parseByte(String.valueOf(dataObject.get("day")));
                        // Thực hiện các thao tác với đối tượng data
                        shopDay.isBuy = (isBuy != 0);
                        shopDay.tookAttendance = (tookAttendance != 0);
                        shopDay.target = day; // Ví dụ: Gán giá trị byte cho trường day
                        player.inventory.dShopDays.add(shopDay);
                    }
                    dataArray.clear();
                    dataObject.clear();
                    // DATA SHOP TIME
                    dataArray = (JSONArray) jv.parse(rs.getString("timeReward"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        DataShopReward shopTime = new DataShopReward();
                        dataObject = (JSONObject) dataArray.get(i);
                        byte isBuy = Byte.parseByte(String.valueOf(dataObject.get("isBuy")));
                        byte tookAttendance = Byte.parseByte(String.valueOf(dataObject.get("tookAttendance")));
                        short timeGive = Short.parseShort(String.valueOf(dataObject.get("timeGive")));
                        // Thực hiện các thao tác với đối tượng data
                        shopTime.isBuy = (isBuy != 0);
                        shopTime.tookAttendance = (tookAttendance != 0);
                        shopTime.target = timeGive; // Ví dụ: Gán giá trị byte cho trường day
                        player.inventory.dShopTimes.add(shopTime);
                    }
                    dataArray.clear();
                    dataObject.clear();
                    // DATA MỐC NẠP
                    dataArray = (JSONArray) jv.parse(rs.getString("cardReward"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        DataShopReward shopNap = new DataShopReward();
                        dataObject = (JSONObject) dataArray.get(i);
                        byte isBuy = Byte.parseByte(String.valueOf(dataObject.get("isBuy")));
                        byte tookAttendance = Byte.parseByte(String.valueOf(dataObject.get("tookAttendance")));
                        int tongNap = Integer.parseInt(String.valueOf(dataObject.get("card")));
                        // Thực hiện các thao tác với đối tượng data
                        shopNap.isBuy = (isBuy != 0);
                        shopNap.tookAttendance = (tookAttendance != 0);
                        shopNap.target = tongNap; // Ví dụ: Gán giá trị byte cho trường day
                        player.inventory.dShopNaps.add(shopNap);
                    }
                    dataArray.clear();
                    dataObject.clear();
                    // DATA SHOP NHIỆM VỤ LÝ TIỂU NƯƠNG
                    dataArray = (JSONArray) jv.parse(rs.getString("taskDayReward"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        DataShopReward shopTask = new DataShopReward();
                        dataObject = (JSONObject) dataArray.get(i);
                        byte isBuy = Byte.parseByte(String.valueOf(dataObject.get("isBuy")));
                        byte tookAttendance = Byte.parseByte(String.valueOf(dataObject.get("tookAttendance")));
                        byte day = Byte.parseByte(String.valueOf(dataObject.get("day")));
                        // Thực hiện các thao tác với đối tượng data
                        shopTask.isBuy = (isBuy != 0);
                        shopTask.tookAttendance = (tookAttendance != 0);
                        shopTask.target = day; // Ví dụ: Gán giá trị byte cho trường day
                        player.inventory.dShopTasks.add(shopTask);
                    }
                    dataArray.clear();
                    dataObject.clear();
                    // DATA MỐC SỨC MẠNH
                    dataArray = (JSONArray) jv.parse(rs.getString("powerReward"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        DataShopReward shopPower = new DataShopReward();
                        dataObject = (JSONObject) dataArray.get(i);
                        byte isBuy = Byte.parseByte(String.valueOf(dataObject.get("isBuy")));
                        byte tookAttendance = Byte.parseByte(String.valueOf(dataObject.get("tookAttendance")));
                        int tongNap = Integer.parseInt(String.valueOf(dataObject.get("card")));
                        // Thực hiện các thao tác với đối tượng data
                        shopPower.isBuy = (isBuy != 0);
                        shopPower.tookAttendance = (tookAttendance != 0);
                        shopPower.target = tongNap; // Ví dụ: Gán giá trị byte cho trường day
                        player.inventory.dShopPowers.add(shopPower);
                    }
                    dataArray.clear();
                    dataObject.clear();
                    // data friends
                    dataArray = (JSONArray) jv.parse(rs.getString("friends"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        dataObject = (JSONObject) dataArray.get(i);
                        Friend friend = new Friend();
                        friend.id = Integer.parseInt(String.valueOf(dataObject.get("id")));
                        friend.name = String.valueOf(dataObject.get("name"));
                        friend.head = Short.parseShort(String.valueOf(dataObject.get("head")));
                        friend.body = Short.parseShort(String.valueOf(dataObject.get("body")));
                        friend.leg = Short.parseShort(String.valueOf(dataObject.get("leg")));
                        friend.bag = Byte.parseByte(String.valueOf(dataObject.get("bag")));
                        friend.power = Long.parseLong(String.valueOf(dataObject.get("power")));
                        player.friends.add(friend);
                        dataObject.clear();
                    }
                    dataArray.clear();

                    // data enemies
                    dataArray = (JSONArray) jv.parse(rs.getString("enemies"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        dataObject = (JSONObject) dataArray.get(i);
                        Enemy enemy = new Enemy();
                        enemy.id = Integer.parseInt(String.valueOf(dataObject.get("id")));
                        enemy.name = String.valueOf(dataObject.get("name"));
                        enemy.head = Short.parseShort(String.valueOf(dataObject.get("head")));
                        enemy.body = Short.parseShort(String.valueOf(dataObject.get("body")));
                        enemy.leg = Short.parseShort(String.valueOf(dataObject.get("leg")));
                        enemy.bag = Byte.parseByte(String.valueOf(dataObject.get("bag")));
                        enemy.power = Long.parseLong(String.valueOf(dataObject.get("power")));
                        player.enemies.add(enemy);
                        dataObject.clear();
                    }
                    dataArray.clear();

                    // data nội tại
                    dataArray = (JSONArray) jv.parse(rs.getString("data_intrinsic"));
                    byte intrinsicId = Byte.parseByte(dataArray.get(0).toString());
                    player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
                    player.playerIntrinsic.intrinsic.param1 = Short.parseShort(dataArray.get(1).toString());
                    player.playerIntrinsic.countOpen = Byte.parseByte(dataArray.get(2).toString());
                    player.playerIntrinsic.intrinsic.param2 = Short.parseShort(dataArray.get(3).toString());
                    dataArray.clear();

                    dataArray = (JSONArray) jv.parse(rs.getString("Wish_tree"));
                    player.lastTimeWish = Long.parseLong(dataArray.get(0).toString());
                    player.isWish = Integer.parseInt(dataArray.get(1).toString()) == 1 ? true : false;
                    dataArray.clear();

                    // data item time
                    dataArray = (JSONArray) jv.parse(rs.getString("data_item_time"));
                    int timeBoKhi = Integer.parseInt(dataArray.get(0).toString());
                    int timeAnDanh = Integer.parseInt(dataArray.get(1).toString());
                    int timeOpenPower = Integer.parseInt(dataArray.get(2).toString());
                    int timeCuongNo = Integer.parseInt(dataArray.get(3).toString());
                    int timeBoHuyet = Integer.parseInt(dataArray.get(5).toString());
                    int timeGiapXen = Integer.parseInt(dataArray.get(8).toString());
                    int timeMayDo = 0;
                    int timeMeal = 0;
                    int iconMeal = 0;
                    try {
                        timeMayDo = Integer.parseInt(dataArray.get(4).toString());
                        timeMeal = Integer.parseInt(dataArray.get(7).toString());
                        iconMeal = Integer.parseInt(dataArray.get(6).toString());
                    } catch (Exception e) {
                    }
                    int timeBanhChung1 = 0;
                    int timeBanhTet1 = 0;
                    int timeBoKhi2 = 0;
                    int timeGiapXen2 = 0;
                    int timeCuongNo2 = 0;
                    int timeBoHuyet2 = 0;
                    int timeeMdSkh = 0;
                    int timeBohoahong = 0;
                    int TImeMaydoBongtoi = 0;
                    int TImeX2TNSM = 0;
                    int TImeX3TNSM = 0;
                    int TImeX4TNSM = 0;
                    int TimeThitSuon = 0;
                    int TimethitThan = 0;
                    int TimeDauve = 0;
                    int TimeGroup_1_1 = 0;
                    int TimeGroup_1_2 = 0;
                    int TimeGroup_1_3 = 0;
                    int TimeGroup_2_1 = 0;
                    int TimeGroup_2_2 = 0;
                    int TimeGroup_2_3 = 0;
                    int TimeGroup_3_1 = 0;
                    int TimeGroup_3_2 = 0;
                    int TimeGroup_3_3 = 0;
                    int TimeGroup_4_1 = 0;
                    int TimeGroup_4_2 = 0;
                    int TimeGroup_4_3 = 0;
                    long LastTime_X_Dame = 0;
                    int TimeBanhTrungThu_1 = 0;
                    int TimeBanhTrungThu_2 = 0;
                    int TimeBanhTrungThu_3 = 0;
                    int TimeBanhTrungThu_4 = 0;
                    int TimeGroup_5_1 = 0;
                    int TimeGroup_5_2 = 0;
                    int TimeGroup_5_3 = 0;
                    int TimeGroup_5_4 = 0;
                    int TimeGroup_5_5 = 0;
                    int TimeGroup_5_6 = 0;
                    int TimeGroup_5_7 = 0;
                    int TimeGroup_5_8 = 0;
                    int TimeGroup_5_9 = 0;
                    int TimeGroup_5_10 = 0;
                    int TimeGroup_6_1 = 0;
                    int TimeGroup_6_2 = 0;
                    int TimeGroup_6_3 = 0;
                    int TimeGroup_6_4 = 0;
                    int TimeGroup_6_5 = 0;
                    int TimeGroup_7_1 = 0;
                    int TimeGroup_7_2 = 0;
                    int TimeGroup_7_3 = 0;
                    int TimeGroup_7_4 = 0;
                    int TimeGroup_7_5 = 0;
                    int timeHoangHoa = 0;
                    int timeHuyHieu = 0;
                    int timeCaChua = 0;
                    int timeCaRot = 0;
                    int timeChuoi = 0;
                    int timeKeoBanTay = 0;
                    int timeDaiHaiTrinh = 0;
                    if (dataArray.size() >= 15) {
                        timeBanhChung1 = Integer.parseInt(dataArray.get(9).toString());
                        timeBanhTet1 = Integer.parseInt(dataArray.get(10).toString());
                        timeBoKhi2 = Integer.parseInt(dataArray.get(11).toString());
                        timeGiapXen2 = Integer.parseInt(dataArray.get(12).toString());
                        timeCuongNo2 = Integer.parseInt(dataArray.get(13).toString());
                        timeBoHuyet2 = Integer.parseInt(dataArray.get(14).toString());
                        timeeMdSkh = Integer.parseInt(dataArray.get(15).toString());
                        timeBohoahong = Integer.parseInt(dataArray.get(16).toString());
                        TImeMaydoBongtoi = Integer.parseInt(dataArray.get(17).toString());
                        TImeX2TNSM = Integer.parseInt(dataArray.get(18).toString());
                        TImeX3TNSM = Integer.parseInt(dataArray.get(19).toString());
                        TImeX4TNSM = Integer.parseInt(dataArray.get(20).toString());
                        TimeThitSuon = Integer.parseInt(dataArray.get(21).toString());
                        TimethitThan = Integer.parseInt(dataArray.get(22).toString());
                        TimeDauve = Integer.parseInt(dataArray.get(23).toString());
                        TimeGroup_1_1 = Integer.parseInt(dataArray.get(24).toString());
                        TimeGroup_1_2 = Integer.parseInt(dataArray.get(25).toString());
                        TimeGroup_1_3 = Integer.parseInt(dataArray.get(26).toString());
                        TimeGroup_2_1 = Integer.parseInt(dataArray.get(27).toString());
                        TimeGroup_2_2 = Integer.parseInt(dataArray.get(28).toString());
                        TimeGroup_2_3 = Integer.parseInt(dataArray.get(29).toString());
                        TimeGroup_3_1 = Integer.parseInt(dataArray.get(30).toString());
                        TimeGroup_3_2 = Integer.parseInt(dataArray.get(31).toString());
                        TimeGroup_3_3 = Integer.parseInt(dataArray.get(32).toString());
                        TimeGroup_4_1 = Integer.parseInt(dataArray.get(33).toString());
                        TimeGroup_4_2 = Integer.parseInt(dataArray.get(34).toString());
                        TimeGroup_4_3 = Integer.parseInt(dataArray.get(35).toString());
                        TimeGroup_4_3 = Integer.parseInt(dataArray.get(35).toString());
                        LastTime_X_Dame = Long.parseLong(dataArray.get(36).toString());
                        TimeBanhTrungThu_1 = Integer.parseInt(dataArray.get(37).toString());
                        TimeBanhTrungThu_2 = Integer.parseInt(dataArray.get(38).toString());
                        TimeBanhTrungThu_3 = Integer.parseInt(dataArray.get(39).toString());
                        TimeBanhTrungThu_4 = Integer.parseInt(dataArray.get(40).toString());

                        TimeGroup_5_1 = Integer.parseInt(dataArray.get(41).toString());
                        TimeGroup_5_2 = Integer.parseInt(dataArray.get(42).toString());
                        TimeGroup_5_3 = Integer.parseInt(dataArray.get(43).toString());
                        TimeGroup_5_4 = Integer.parseInt(dataArray.get(44).toString());
                        TimeGroup_5_5 = Integer.parseInt(dataArray.get(45).toString());
                        TimeGroup_5_6 = Integer.parseInt(dataArray.get(46).toString());
                        TimeGroup_5_7 = Integer.parseInt(dataArray.get(47).toString());
                        TimeGroup_5_8 = Integer.parseInt(dataArray.get(48).toString());
                        TimeGroup_5_9 = Integer.parseInt(dataArray.get(49).toString());
                        TimeGroup_5_10 = Integer.parseInt(dataArray.get(50).toString());

                        TimeGroup_6_1 = Integer.parseInt(dataArray.get(51).toString());
                        TimeGroup_6_2 = Integer.parseInt(dataArray.get(52).toString());
                        TimeGroup_6_3 = Integer.parseInt(dataArray.get(53).toString());
                        TimeGroup_6_4 = Integer.parseInt(dataArray.get(54).toString());
                        TimeGroup_6_5 = Integer.parseInt(dataArray.get(55).toString());

                        TimeGroup_7_1 = Integer.parseInt(dataArray.get(56).toString());
                        TimeGroup_7_2 = Integer.parseInt(dataArray.get(57).toString());
                        TimeGroup_7_3 = Integer.parseInt(dataArray.get(58).toString());
                        TimeGroup_7_4 = Integer.parseInt(dataArray.get(59).toString());
                        TimeGroup_7_5 = Integer.parseInt(dataArray.get(60).toString());

                        try {
                            timeHoangHoa = Integer.parseInt(dataArray.get(61).toString());
                        } catch (Exception e) {
                            timeHoangHoa = 0;
                        }

                        try {
                            timeHuyHieu = Integer.parseInt(dataArray.get(62).toString());
                            timeCaChua = Integer.parseInt(dataArray.get(63).toString());
                            timeCaRot = Integer.parseInt(dataArray.get(64).toString());
                            timeChuoi = Integer.parseInt(dataArray.get(65).toString());
                        } catch (Exception e) {
                            timeHuyHieu = 0;
                            timeCaChua = 0;
                            timeCaRot = 0;
                            timeChuoi = 0;
                        }

                        try {
                            timeKeoBanTay = Integer.parseInt(dataArray.get(66).toString());
                        } catch (Exception e) {
                            timeKeoBanTay = 0;
                        }

                        try {
                            timeDaiHaiTrinh = Integer.parseInt(dataArray.get(67).toString());
                        } catch (Exception e) {
                            timeDaiHaiTrinh = 0;
                        }
                    }
                    player.itemTime.lastTimeBoHuyet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet);
                    player.itemTime.lastTimeBoKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi);
                    player.itemTime.lastTimeGiapXen = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen);
                    player.itemTime.lastTimeCuongNo = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo);
                    player.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet2);
                    player.itemTime.lastTimeBoKhi2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi2);
                    player.itemTime.lastTimeGiapXen2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen2);
                    player.itemTime.lastTimeCuongNo2 = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo2);
                    player.itemTime.lastTimeAnDanh = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh);
                    player.itemTime.lastTimeOpenPower = System.currentTimeMillis()
                            - (ItemTime.TIME_OPEN_POWER - timeOpenPower);
                    player.itemTime.lastTimeUseMayDo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDo);
                    player.itemTime.lastTimeEatMeal = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal);
                    player.itemTime.lastTimeBanhChung = System.currentTimeMillis()
                            - (ItemTime.TIME_EAT_MEAL - timeBanhChung1);
                    player.itemTime.lastTimeBanhTet = System.currentTimeMillis()
                            - (ItemTime.TIME_EAT_MEAL - timeBanhTet1);
                    player.itemTime.lastTimeMdSkh = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeeMdSkh);
                    player.itemTime.lastTimeBohoaHong = System.currentTimeMillis()
                            - (ItemTime.TIME_MAY_DO - timeBohoahong);
                    player.itemTime.lastTimeUseMaydoBongtoi = System.currentTimeMillis()
                            - (ItemTime.TIME_MAY_DO - TImeMaydoBongtoi);
                    // x2 x3 x4
                    player.itemTime.lastTimeUseX2TNSM = System.currentTimeMillis() - (ItemTime.TIME_ITEM - TImeX2TNSM);
                    player.itemTime.lastTimeUseX3TNSM = System.currentTimeMillis() - (ItemTime.TIME_ITEM - TImeX3TNSM);
                    player.itemTime.lastTimeUseX4TNSM = System.currentTimeMillis() - (ItemTime.TIME_ITEM - TImeX4TNSM);
                    //
                    // thịt sườn, thăn , đậu ve
                    player.itemTime.lastTimeuseThitSuon = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeThitSuon);
                    player.itemTime.lastTimeuseThitThan = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimethitThan);
                    player.itemTime.lastTimeUseDauve = System.currentTimeMillis() - (ItemTime.TIME_ITEM - TimeDauve);
                    // Group 1
                    player.itemTime.lastTimeUseGroup_1_1 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_1_1);
                    player.itemTime.lastTimeUseGroup_1_2 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_1_2);
                    player.itemTime.lastTimeUseGroup_1_3 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_1_3);
                    // Group 2
                    player.itemTime.lastTimeUseGroup_2_1 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_2_1);
                    player.itemTime.lastTimeUseGroup_2_2 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_2_2);
                    player.itemTime.lastTimeUseGroup_2_3 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_2_3);
                    // Group 3
                    player.itemTime.lastTimeUseGroup_3_1 = System.currentTimeMillis()
                            - (ItemTime.TIME_MAY_DO - TimeGroup_3_1);
                    player.itemTime.lastTimeUseGroup_3_2 = System.currentTimeMillis()
                            - (ItemTime.TIME_MAY_DO - TimeGroup_3_2);
                    player.itemTime.lastTimeUseGroup_3_3 = System.currentTimeMillis()
                            - (ItemTime.TIME_60_MIN - TimeGroup_3_3);
                    // Group 4
                    player.itemTime.lastTimeUseGroup_4_1 = System.currentTimeMillis()
                            - (ItemTime.TIME_90_MIN - TimeGroup_4_1);
                    player.itemTime.lastTimeUseGroup_4_2 = System.currentTimeMillis()
                            - (ItemTime.TIME_90_MIN - TimeGroup_4_2);
                    player.itemTime.lastTimeUseGroup_4_3 = System.currentTimeMillis()
                            - (ItemTime.TIME_90_MIN - TimeGroup_4_3);
                    // Hồi x4 chưởng
                    player.lastTimexDameChuong = LastTime_X_Dame;
                    // Bánh trung thu
                    player.itemTime.lastTimeBanhTrungThu_1 = System.currentTimeMillis()
                            - (ItemTime.TIME_60_MIN - TimeBanhTrungThu_1);
                    player.itemTime.lastTimeBanhTrungThu_2 = System.currentTimeMillis()
                            - (ItemTime.TIME_90_MIN - TimeBanhTrungThu_2);
                    player.itemTime.lastTimeBanhTrungThu_3 = System.currentTimeMillis()
                            - (ItemTime.TIME_120_MIN - TimeBanhTrungThu_3);
                    player.itemTime.lastTimeBanhTrungThu_4 = System.currentTimeMillis()
                            - (ItemTime.TIME_150_MIN - TimeBanhTrungThu_4);
                    // Group 5 10 phút
                    player.itemTime.lastTimeUseGroup_5_1 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_5_1);
                    player.itemTime.lastTimeUseGroup_5_2 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_5_2);
                    player.itemTime.lastTimeUseGroup_5_3 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_5_3);
                    player.itemTime.lastTimeUseGroup_5_4 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_5_4);
                    player.itemTime.lastTimeUseGroup_5_5 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_5_5);
                    player.itemTime.lastTimeUseGroup_5_6 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_5_6);
                    player.itemTime.lastTimeUseGroup_5_7 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_5_7);
                    player.itemTime.lastTimeUseGroup_5_8 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_5_8);
                    player.itemTime.lastTimeUseGroup_5_9 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_5_9);
                    player.itemTime.lastTimeUseGroup_5_10 = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - TimeGroup_5_10);
                    // Group 6 30 phút
                    player.itemTime.lastTimeUseGroup_6_1 = System.currentTimeMillis()
                            - (ItemTime.TIME_MAY_DO - TimeGroup_6_1);
                    player.itemTime.lastTimeUseGroup_6_2 = System.currentTimeMillis()
                            - (ItemTime.TIME_MAY_DO - TimeGroup_6_2);
                    player.itemTime.lastTimeUseGroup_6_3 = System.currentTimeMillis()
                            - (ItemTime.TIME_MAY_DO - TimeGroup_6_3);
                    player.itemTime.lastTimeUseGroup_6_4 = System.currentTimeMillis()
                            - (ItemTime.TIME_MAY_DO - TimeGroup_6_4);
                    player.itemTime.lastTimeUseGroup_6_5 = System.currentTimeMillis()
                            - (ItemTime.TIME_MAY_DO - TimeGroup_6_5);
                    // Group 7 60 phút
                    player.itemTime.lastTimeUseGroup_7_1 = System.currentTimeMillis()
                            - (ItemTime.TIME_MAY_DO - TimeGroup_7_1);
                    player.itemTime.lastTimeUseGroup_7_2 = System.currentTimeMillis()
                            - (ItemTime.TIME_60_MIN - TimeGroup_7_2);
                    player.itemTime.lastTimeUseGroup_7_3 = System.currentTimeMillis()
                            - (ItemTime.TIME_60_MIN - TimeGroup_7_3);
                    player.itemTime.lastTimeUseGroup_7_4 = System.currentTimeMillis()
                            - (ItemTime.TIME_60_MIN - TimeGroup_7_4);
                    player.itemTime.lastTimeUseGroup_7_5 = System.currentTimeMillis()
                            - (ItemTime.TIME_60_MIN - TimeGroup_7_5);

                    player.itemTime.lastTimeUseHoangHoa = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - timeHoangHoa);

                    player.itemTime.lastTimeHuyHieu = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - timeHuyHieu);
                    player.itemTime.lastTimeCaChua = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - timeCaChua);
                    player.itemTime.lastTimeCaRot = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - timeCaRot);
                    player.itemTime.lastTimeChuoi = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - timeChuoi);
                    player.itemTime.lastTimeKeoBanTay = System.currentTimeMillis()
                            - (ItemTime.TIME_ITEM - timeKeoBanTay);
                    player.itemTime.lastTimeDaiHaiTrinh = System.currentTimeMillis()
                            - (ItemTime.TIME_60_MIN - timeDaiHaiTrinh);

                    player.itemTime.iconMeal = iconMeal;
                    player.itemTime.isUseBoHuyet = timeBoHuyet != 0;
                    player.itemTime.isUseBoKhi = timeBoKhi != 0;
                    player.itemTime.isUseGiapXen = timeGiapXen != 0;
                    player.itemTime.isUseCuongNo = timeCuongNo != 0;
                    player.itemTime.isUseBoHuyet2 = timeBoHuyet2 != 0;
                    player.itemTime.isUseBoKhi2 = timeBoKhi2 != 0;
                    player.itemTime.isUseGiapXen2 = timeGiapXen2 != 0;
                    player.itemTime.isUseCuongNo2 = timeCuongNo2 != 0;
                    player.itemTime.isUseAnDanh = timeAnDanh != 0;
                    player.itemTime.isOpenPower = timeOpenPower != 0;
                    player.itemTime.isUseMayDo = timeMayDo != 0;
                    player.itemTime.isEatMeal = timeMeal != 0;
                    player.itemTime.isUseBanhChung = timeBanhChung1 != 0;
                    player.itemTime.isUseBanhTet = timeBanhTet1 != 0;
                    player.itemTime.isUseMdSkh = timeeMdSkh != 0;
                    player.itemTime.isUseBohoaHong = timeBohoahong != 0;
                    player.itemTime.isUseMaydoBongtoi = TImeMaydoBongtoi != 0;
                    // x2 x3 x4
                    player.itemTime.isUseX2TNSM = TImeX2TNSM != 0;
                    player.itemTime.isUseX3TNSM = TImeX3TNSM != 0;
                    player.itemTime.isUseX4TNSM = TImeX4TNSM != 0;
                    // thịt thăn , sườn , đậu ve
                    player.itemTime.isUseThitSuon = TimeThitSuon != 0;
                    player.itemTime.isUseThitThan = TimethitThan != 0;
                    player.itemTime.isUseDauVe = TimeDauve != 0;
                    // Group 1
                    player.itemTime.isUseGroup_1_1 = TimeGroup_1_1 != 0;
                    player.itemTime.isUseGroup_1_2 = TimeGroup_1_2 != 0;
                    player.itemTime.isUseGroup_1_3 = TimeGroup_1_3 != 0;
                    // Group 2
                    player.itemTime.isUseGroup_2_1 = TimeGroup_2_1 != 0;
                    player.itemTime.isUseGroup_2_2 = TimeGroup_2_2 != 0;
                    player.itemTime.isUseGroup_2_3 = TimeGroup_2_3 != 0;
                    // Group 3
                    player.itemTime.isUseGroup_3_1 = TimeGroup_3_1 != 0;
                    player.itemTime.isUseGroup_3_2 = TimeGroup_3_2 != 0;
                    player.itemTime.isUseGroup_3_3 = TimeGroup_3_3 != 0;
                    // Group 4
                    player.itemTime.isUseGroup_4_1 = TimeGroup_4_1 != 0;
                    player.itemTime.isUseGroup_4_2 = TimeGroup_4_2 != 0;
                    player.itemTime.isUseGroup_4_3 = TimeGroup_4_3 != 0;
                    // Bánh trung thu
                    player.itemTime.isUseBanhTrungThu_1 = TimeBanhTrungThu_1 != 0;
                    player.itemTime.isUseBanhTrungThu_2 = TimeBanhTrungThu_2 != 0;
                    player.itemTime.isUseBanhTrungThu_3 = TimeBanhTrungThu_3 != 0;
                    player.itemTime.isUseBanhTrungThu_4 = TimeBanhTrungThu_4 != 0;
                    // Group 5 10 phút
                    player.itemTime.isUseGroup_5_1 = TimeGroup_5_1 != 0;
                    player.itemTime.isUseGroup_5_2 = TimeGroup_5_2 != 0;
                    player.itemTime.isUseGroup_5_3 = TimeGroup_5_3 != 0;
                    player.itemTime.isUseGroup_5_4 = TimeGroup_5_4 != 0;
                    player.itemTime.isUseGroup_5_5 = TimeGroup_5_5 != 0;
                    player.itemTime.isUseGroup_5_6 = TimeGroup_5_6 != 0;
                    player.itemTime.isUseGroup_5_7 = TimeGroup_5_7 != 0;
                    player.itemTime.isUseGroup_5_8 = TimeGroup_5_8 != 0;
                    player.itemTime.isUseGroup_5_9 = TimeGroup_5_9 != 0;
                    player.itemTime.isUseGroup_5_10 = TimeGroup_5_10 != 0;
                    // Group 6 30 phút
                    player.itemTime.isUseGroup_6_1 = TimeGroup_6_1 != 0;
                    player.itemTime.isUseGroup_6_2 = TimeGroup_6_2 != 0;
                    player.itemTime.isUseGroup_6_3 = TimeGroup_6_3 != 0;
                    player.itemTime.isUseGroup_6_4 = TimeGroup_6_4 != 0;
                    player.itemTime.isUseGroup_6_5 = TimeGroup_6_5 != 0;
                    // Group 7 60 phút
                    player.itemTime.isUseGroup_7_1 = TimeGroup_7_1 != 0;
                    player.itemTime.isUseGroup_7_2 = TimeGroup_7_2 != 0;
                    player.itemTime.isUseGroup_7_3 = TimeGroup_7_3 != 0;
                    player.itemTime.isUseGroup_7_4 = TimeGroup_7_4 != 0;
                    player.itemTime.isUseGroup_7_5 = TimeGroup_7_5 != 0;

                    player.itemTime.isUseHoangHoa = timeHoangHoa != 0;
                    player.itemTime.isHuyHieu = timeHuyHieu != 0;
                    player.itemTime.isCaChua = timeCaChua != 0;
                    player.itemTime.isCaRot = timeCaRot != 0;
                    player.itemTime.isChuoi = timeChuoi != 0;
                    player.itemTime.isKeoBayTay = timeKeoBanTay != 0;
                    player.itemTime.isDaiHaiTrinh = timeDaiHaiTrinh != 0;

                    dataArray.clear();
                    // data nhiệm vụ
                    dataArray = (JSONArray) jv.parse(rs.getString("data_task"));
                    TaskMain taskMain = TaskService.gI().getTaskMainById(player,
                            Byte.parseByte(dataArray.get(1).toString()));
                    taskMain.subTasks.get(Integer.parseInt(dataArray.get(2).toString())).count = Short
                            .parseShort(dataArray.get(0).toString());
                    taskMain.index = Byte.parseByte(dataArray.get(2).toString());
                    player.playerTask.taskMain = taskMain;
                    dataArray.clear();

                    // data nhiệm vụ hàng ngày
                    try {
                        dataArray = (JSONArray) jv.parse(rs.getString("data_side_task"));
                        String format = "dd-MM-yyyy";
                        long receivedTime = Long.parseLong(String.valueOf(dataArray.get(5)));
                        Date date = new Date(receivedTime);
                        if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                            player.playerTask.sideTask.template = TaskService.gI()
                                    .getSideTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                            player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            player.playerTask.sideTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                            player.playerTask.sideTask.receivedTime = receivedTime;
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }

                    dataArray = (JSONArray) jv.parse(rs.getString("achivements"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        dataObject = (JSONObject) jv.parse(String.valueOf(dataArray.get(i)));
                        Achivement achivement = new Achivement();
                        achivement.setId(Integer.parseInt(dataObject.get("id").toString()));
                        achivement.setCount(Integer.parseInt(dataObject.get("count").toString()));
                        achivement.setFinish(Integer.parseInt(dataObject.get("finish").toString()) == 1);
                        achivement.setReceive(Integer.parseInt(dataObject.get("receive").toString()) == 1);

                        AchivementTemplate a = AchiveManager.getInstance().findByID(achivement.getId());

                        achivement.setDetail(a.getDetail());
                        achivement.setMaxCount(a.getMaxCount());
                        achivement.setMoney(a.getMoney());

                        player.playerTask.achivements.add(achivement);
                    }

                    List<AchivementTemplate> listAchivements = AchiveManager.getInstance().getList();
                    if (dataArray.size() < listAchivements.size()) { // add thêm nhiệm vụ khi có nhiệm vụ mới
                        for (int i = dataArray.size(); i < listAchivements.size(); i++) {
                            AchivementTemplate a = AchiveManager.getInstance().findByID(i);
                            Achivement achivement = new Achivement();
                            if (a != null) {
                                achivement.setId(a.getId());
                                achivement.setCount(0);
                                achivement.setFinish(false);
                                achivement.setReceive(false);
                                achivement.setName(a.getName());
                                achivement.setDetail(a.getDetail());
                                achivement.setMaxCount(a.getMaxCount());
                                achivement.setMoney(a.getMoney());
                                player.playerTask.achivements.add(achivement);
                            }
                        }
                    }
                    dataArray.clear();

                    // data trứng bư
                    dataObject = (JSONObject) jv.parse(rs.getString("data_mabu_egg"));
                    Object createTime = dataObject.get("create_time");
                    if (createTime != null) {
                        player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(createTime)),
                                Long.parseLong(String.valueOf(dataObject.get("time_done"))));
                    }
                    dataObject.clear();

                    dataObject = (JSONObject) jv.parse(rs.getString("data_kamin"));
                    Object createTimee = dataObject.get("create_time");
                    if (createTimee != null) {
                        player.kaminEgg = new KaminEgg(player, Long.parseLong(String.valueOf(createTimee)),
                                Long.parseLong(String.valueOf(dataObject.get("time_done"))));
                    }
                    dataObject.clear();

                    // data bùa
                    dataArray = (JSONArray) jv.parse(rs.getString("data_charm"));
                    player.charms.tdTriTue = Long.parseLong(dataArray.get(0).toString());
                    player.charms.tdManhMe = Long.parseLong(dataArray.get(1).toString());
                    player.charms.tdDaTrau = Long.parseLong(dataArray.get(2).toString());
                    player.charms.tdOaiHung = Long.parseLong(dataArray.get(3).toString());
                    player.charms.tdBatTu = Long.parseLong(dataArray.get(4).toString());
                    player.charms.tdDeoDai = Long.parseLong(dataArray.get(5).toString());
                    player.charms.tdThuHut = Long.parseLong(dataArray.get(6).toString());
                    player.charms.tdDeTu = Long.parseLong(dataArray.get(7).toString());
                    player.charms.tdTriTue3 = Long.parseLong(dataArray.get(8).toString());
                    player.charms.tdTriTue4 = Long.parseLong(dataArray.get(9).toString());
                    if (dataArray.size() >= 11) {
                        player.charms.tdDeTuMabu = Long.parseLong(dataArray.get(10).toString());
                        player.charms.tdDeTuMabu2 = Long.parseLong(dataArray.get(11).toString());
                        player.charms.tdDeTuMabu3 = Long.parseLong(dataArray.get(12).toString());
                        player.charms.tdPhuHP = Long.parseLong(dataArray.get(13).toString());
                        player.charms.tdPhuKI = Long.parseLong(dataArray.get(14).toString());
                        player.charms.tdPhuSD = Long.parseLong(dataArray.get(15).toString());
                        player.charms.tdPhuTNSM = Long.parseLong(dataArray.get(16).toString());
                    }
                    dataArray.clear();

                    // data skill
                    dataArray = (JSONArray) jv.parse(rs.getString("skills"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray skillTemp = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        int tempId = Integer.parseInt(skillTemp.get(0).toString());
                        byte point = Byte.parseByte(skillTemp.get(2).toString());
                        Skill skill = null;
                        if (point != 0) {
                            skill = SkillUtil.createSkill(tempId, point);
                        } else {
                            skill = SkillUtil.createSkillLevel0(tempId);
                        }
                        skill.lastTimeUseThisSkill = Long.parseLong(skillTemp.get(1).toString());
                        player.playerSkill.skills.add(skill);
                        skillTemp.clear();
                    }
                    dataArray.clear();
                    // data skill shortcut
                    dataArray = (JSONArray) jv.parse(rs.getString("skills_shortcut"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
                    }
                    for (int i : player.playerSkill.skillShortCut) {
                        if (player.playerSkill.getSkillbyId(i) != null
                                && player.playerSkill.getSkillbyId(i).damage > 0) {
                            player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                            break;
                        }
                    }
                    if (player.playerSkill.skillSelect == null) {
                        player.playerSkill.skillSelect = player.playerSkill
                                .getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                                        ? Skill.DRAGON
                                        : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
                    }
                    dataArray.clear();

                    Gson gson = new Gson();
                    List<Card> cards = gson.fromJson(rs.getString("collection_book"), new TypeToken<List<Card>>() {
                    }.getType());

                    CollectionBook book = new CollectionBook(player);
                    if (cards != null) {
                        book.setCards(cards);
                    } else {
                        book.setCards(new ArrayList<>());
                    }
                    book.init();
                    player.setCollectionBook(book);
                    List<Item> itemsBody = player.inventory.itemsBody;
                    while (itemsBody.size() < 14) {
                        itemsBody.add(ItemService.gI().createItemNull());
                    }
                    if (itemsBody.get(9).isNotNullItem()) {
                        MiniPet.callMiniPet(player, (player.inventory.itemsBody.get(9).template.id),
                                player.inventory.itemsBody.get(9).template.name);
                    }
                    if (itemsBody.get(10).isNotNullItem()) {
                        PetFollow pet = PetFollowManager.gI().findByID(itemsBody.get(10).getId());
                        player.setPetFollow(pet);
                    }
                    if (itemsBody.get(11).isNotNullItem()) {
                        SendEffect.getInstance().sendChanThienTu(player, (player.inventory.itemsBody.get(11).template.id));
                    }
                    if (itemsBody.get(12).isNotNullItem()) {
                        SendEffect.getInstance().sendDanhhieu(player, (player.inventory.itemsBody.get(12).template.id));
                    }
                    if (itemsBody.get(13).isNotNullItem() && itemsBody.get(13).template.type == 39) {
                        // No sendDanhhieu call, add alternative logic here if needed
                    }
                    player.firstTimeLogin = rs.getTimestamp("firstTimeLogin");

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("buy_limit"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        player.buyLimit[i] = Byte.parseByte(dataArray.get(i).toString());
                    }

                    dataArray = (JSONArray) JSONValue.parse(rs.getString("reward_limit"));

                    player.rewardLimit = new byte[dataArray.size()];
                    for (int i = 0; i < dataArray.size(); i++) {
                        player.rewardLimit[i] = Byte.parseByte(dataArray.get(i).toString());
                    }
                    // Quà đhvt
                    player.receivedTopDhVT = rs.getInt("TopDhVt") == 1;
                    // dhvt23
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("challenge"));
                    player.goldChallenge = Integer.parseInt(dataArray.get(0).toString());
                    player.levelWoodChest = Integer.parseInt(dataArray.get(1).toString());
                    player.receivedWoodChest = Integer.parseInt(dataArray.get(2).toString()) == 1;
                    dataArray.clear();

                    //data daichien
                    dataArray = (JSONArray) jv.parse(rs.getString("data_daichien"));
                    player.joindaichien = Integer.parseInt(dataArray.get(0).toString());
                    player.joinfree = Integer.parseInt(dataArray.get(1).toString());
                    dataArray.clear();

                    PlayerService.gI().dailyLogin(player);

                    // data pet
                    dataObject = (JSONObject) jv.parse(rs.getString("pet_info"));
                    if (!String.valueOf(dataObject).equals("{}")) {
                        Pet pet = new Pet(player);
                        pet.id = -player.id;
                        pet.gender = Byte.parseByte(String.valueOf(dataObject.get("gender")));
                        pet.isMabu = Byte.parseByte(String.valueOf(dataObject.get("is_mabu"))) == 1;
                        pet.isBU = Byte.parseByte(String.valueOf(dataObject.get("is_Wukong"))) == 1;
                        pet.isCell = Byte.parseByte(String.valueOf(dataObject.get("is_Heo"))) == 1;
                        pet.isFide = Byte.parseByte(String.valueOf(dataObject.get("is_Satang"))) == 1;
                        pet.isGoku = Byte.parseByte(String.valueOf(dataObject.get("is_Goku"))) == 1;

                        String namePet = String.valueOf(dataObject.get("name"));

                        // if (pet.isMabu) {
                        pet.LevelZeno = Integer.parseInt(String.valueOf(dataObject.get("L_vel_Zeno")));
                        pet.baseName = namePet; // tên lưu
                        // pet.name = "$[ Cấp : " + (pet.LevelZeno + 1) + " ] " + namePet; // tên hiển
                        // thị
                        pet.name = "$" + namePet; // tên hiển thị
                        // }
                        // }
                        player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataObject.get("type_fusion")));
                        player.fusion.lastTimeFusion = System.currentTimeMillis()
                                - (Fusion.TIME_FUSION
                                - Integer.parseInt(String.valueOf(dataObject.get("left_fusion"))));
                        pet.status = Byte.parseByte(String.valueOf(dataObject.get("status")));

                        // data chỉ số
                        dataObject = (JSONObject) jv.parse(rs.getString("pet_point"));
                        pet.nPoint.stamina = Short.parseShort(String.valueOf(dataObject.get("stamina")));
                        pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataObject.get("max_stamina")));
                        pet.nPoint.hpg = Integer.parseInt(String.valueOf(dataObject.get("hpg")));
                        pet.nPoint.mpg = Integer.parseInt(String.valueOf(dataObject.get("mpg")));
                        pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataObject.get("damg")));
                        pet.nPoint.defg = Integer.parseInt(String.valueOf(dataObject.get("defg")));
                        pet.nPoint.critg = Integer.parseInt(String.valueOf(dataObject.get("critg")));
                        pet.nPoint.power = Long.parseLong(String.valueOf(dataObject.get("power")));
                        pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataObject.get("tiem_nang")));
                        pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataObject.get("limit_power")));
                        int hp = Integer.parseInt(String.valueOf(dataObject.get("hp")));
                        int mp = Integer.parseInt(String.valueOf(dataObject.get("mp")));

                        // data body
                        dataArray = (JSONArray) jv.parse(rs.getString("pet_body"));
                        for (int i = 0; i < dataArray.size(); i++) {
                            dataObject = (JSONObject) dataArray.get(i);
                            Item item = null;
                            short tempId = Short.parseShort(String.valueOf(dataObject.get("temp_id")));
                            if (tempId != -1) {
                                item = ItemService.gI().createNewItem(tempId,
                                        Integer.parseInt(String.valueOf(dataObject.get("quantity"))));
                                JSONArray options = (JSONArray) dataObject.get("option");
                                for (int j = 0; j < options.size(); j++) {
                                    JSONArray opt = (JSONArray) options.get(j);
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
                            pet.inventory.itemsBody.add(item);
                        }

                        // data skills
                        dataArray = (JSONArray) jv.parse(rs.getString("pet_skill"));
                        for (int i = 0; i < dataArray.size(); i++) {
                            JSONArray skillTemp = (JSONArray) dataArray.get(i);
                            int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                            byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                            Skill skill = null;
                            if (point != 0) {
                                skill = SkillUtil.createSkill(tempId, point);
                            } else {
                                skill = SkillUtil.createSkillLevel0(tempId);
                            }
                            switch (skill.template.id) {
                                case Skill.KAMEJOKO:
                                case Skill.MASENKO:
                                case Skill.ANTOMIC:
                                    skill.coolDown = 1000;
                                    break;
                            }
                            switch (skill.template.id) {
                                case Skill.SOCOLA:
                                case Skill.HUYT_SAO:
                                case Skill.THOI_MIEN:
                                    skill.coolDown = 10000;
                                    break;
                            }
                            pet.playerSkill.skills.add(skill);
                        }
                        if (dataArray.size() < 5) {
                            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                        }
                        pet.nPoint.hp = hp;
                        pet.nPoint.mp = mp;
                        // pet.nPoint.calPoint();
                        player.pet = pet;
                    }
                    if (session.ruby > 0) {
                        player.inventory.ruby += session.ruby;
                        player.playerTask.achivements.get(ConstAchive.LAN_DAU_NAP_NGOC).count += session.ruby;
                        PlayerDAO.subRuby(player, session.userId, session.ruby);
                    }
                    player.nPoint.hp = plHp;
                    player.nPoint.mp = plMp;
                    session.player = player;
                    PreparedStatement ps2 = connection
                            .prepareStatement("update account set last_time_login = ?, ip_address = ? where id = ?");
                    ps2.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                    ps2.setString(2, session.ipAddress);
                    ps2.setInt(3, session.userId);
                    ps2.executeUpdate();
                    ps2.close();
                    return player;
                }
            } finally {
                rs.close();
                ps.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            session.dataLoadFailed = true;
        }
        return null;
    }

}
