package nro.jdbc.daos;

import com.google.gson.Gson;
import nro.consts.ConstMap;
import nro.jdbc.DBService;
import nro.manager.AchiveManager;
import nro.models.item.DataShopReward;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.ItemTime;
import nro.models.player.*;
import nro.models.skill.Skill;
import nro.models.task.Achivement;
import nro.models.task.AchivementTemplate;
import nro.server.Manager;
import nro.services.MapService;
import nro.utils.Log;
import nro.utils.Logger;
import nro.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;

public class PlayerDAO {

    public static boolean updateTimeLogout;

    public static void createNewPlayer(Connection con, int userId, String name, byte gender, int hair) {
        PreparedStatement ps = null;
        try {
            JSONArray dataInventory = new JSONArray();

            dataInventory.add(10000000000l);// vàng 10 tỷ
            dataInventory.add(500000); // ngọc xanh
            dataInventory.add(0);
            dataInventory.add(0);
            dataInventory.add(0);
            dataInventory.add(0);
            dataInventory.add(0);
            dataInventory.add(0);
            dataInventory.add(0);
            dataInventory.add(0);
            dataInventory.add(0);
            dataInventory.add(0);
            dataInventory.add(0);
            String inventory = dataInventory.toJSONString();

            JSONArray dataLocation = new JSONArray();
            dataLocation.add(100);
            dataLocation.add(384);
            dataLocation.add(39 + gender);
            String location = dataLocation.toJSONString();

            JSONArray dataFreeShop = new JSONArray();
            dataFreeShop.add(0);
            dataFreeShop.add(0);
            dataFreeShop.add(0);
            dataFreeShop.add(0);
            String freeShop = dataFreeShop.toJSONString();

            JSONArray dataPoint = new JSONArray();
            dataPoint.add(0);// nang dong
            dataPoint.add(gender == 1 ? 200 : 100);// mp
            dataPoint.add(gender == 1 ? 200 : 100);// mpg
            dataPoint.add(0);// critg
            dataPoint.add(0);// limitpower
            dataPoint.add(1000);// stamina
            dataPoint.add(gender == 0 ? 200 : 100);// hp
            dataPoint.add(0);// defg
            dataPoint.add(2000);// tn
            dataPoint.add(1000);// maxsta
            dataPoint.add(gender == 2 ? 15 : 10);// damg
            dataPoint.add(2000);// pow
            dataPoint.add(gender == 0 ? 200 : 100);// hpg
            String point = dataPoint.toJSONString();

            JSONArray dataMagicTree = new JSONArray();
            dataMagicTree.add(0);// isupgr
            dataMagicTree.add(new Date().getTime());
            dataMagicTree.add(1);// LV
            dataMagicTree.add(new Date().getTime());
            dataMagicTree.add(5);// curr_pea
            String magicTree = dataMagicTree.toJSONString();

            /**
             *
             * [
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {"temp_id":"-1","option":[],"create_time":"0""}, ... ]
             */
            int idAo = gender == 0 ? 0 : gender == 1 ? 1 : 2;
            int idQuan = gender == 0 ? 6 : gender == 1 ? 7 : 8;
            int def = gender == 2 ? 3 : 2;
            int hp = gender == 0 ? 30 : 20;

            JSONArray dataBody = new JSONArray();
            for (int i = 0; i < 9; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                if (i == 0) {
                    option.add(47);
                    option.add(def);
                    options.add(option);
                    item.put("temp_id", idAo);
                    item.put("create_time", System.currentTimeMillis());
                    item.put("quantity", 1);
                } else if (i == 1) {
                    option.add(6);
                    option.add(hp);
                    options.add(option);
                    item.put("temp_id", idQuan);
                    item.put("create_time", System.currentTimeMillis());
                    item.put("quantity", 1);
                } else {
                    item.put("temp_id", -1);
                    item.put("create_time", 0);
                    item.put("quantity", 1);
                }
                item.put("option", options);
                dataBody.add(item);
            }
            String itemsBody = dataBody.toJSONString();

            JSONArray dataBag = new JSONArray();
            for (int i = 0; i < 20; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                 if (i == 0) {
                    option.add(73);
                 option.add(1);
                 options.add(option);
                 item.put("temp_id", 194);
                 item.put("create_time", System.currentTimeMillis());
                 item.put("quantity", 1);
                 } else {
                item.put("temp_id", -1);
                item.put("create_time", 0);
                item.put("quantity", 1);
                 }
                item.put("option", options);
                dataBag.add(item);
            }
            String itemsBag = dataBag.toJSONString();

            JSONArray dataBox = new JSONArray();
            for (int i = 0; i < 20; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
                if (i == 0) {
                    item.put("temp_id", 12);
                    option.add(14);
                    option.add(1);
                    options.add(option);
                    item.put("create_time", System.currentTimeMillis());
                } else {
                    item.put("temp_id", -1);
                    item.put("create_time", 0);
                }
                item.put("option", options);
                item.put("quantity", 1);
                dataBox.add(item);
            }
            String itemsBox = dataBox.toJSONString();

            JSONArray dataBox_pet_ct = new JSONArray();
            for (int i = 0; i < 10; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                JSONArray option = new JSONArray();
//                if (i == 0) {
//                    item.put("temp_id", 12);
//                    option.add(14);
//                    option.add(1);
//                    options.add(option);
//                    item.put("create_time", System.currentTimeMillis());
//                } else {
                item.put("temp_id", -1);
                item.put("create_time", 0);
                //    }
                item.put("option", options);
                item.put("quantity", 1);
                dataBox_pet_ct.add(item);
            }
            String itemsBox_pet_ct = dataBox_pet_ct.toJSONString();

            JSONArray dataLuckyRound = new JSONArray();
            for (int i = 0; i < 110; i++) {
                JSONObject item = new JSONObject();
                JSONArray options = new JSONArray();
                item.put("temp_id", -1);
                item.put("option", options);
                item.put("create_time", 0);
                item.put("quantity", 1);
                dataLuckyRound.add(item);
            }
            String itemsBoxLuckyRound = dataLuckyRound.toJSONString();

            String friends = "[]";
            String enemies = "[]";

            JSONArray dataIntrinsic = new JSONArray();
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            dataIntrinsic.add(0);
            String intrinsic = dataIntrinsic.toJSONString();

            JSONArray dataItemTime = new JSONArray();
            // 69 item time
            for (int i = 0; i < 68; i++) {
                dataItemTime.add(0);
            }

            String itemTime = dataItemTime.toJSONString();

            JSONArray dataTask = new JSONArray();
            dataTask.add(0);
            dataTask.add(0);
            dataTask.add(0);
            String task = dataTask.toJSONString();

            JSONArray dataAchive = new JSONArray();
            for (AchivementTemplate a : AchiveManager.getInstance().getList()) {
                JSONObject jobj = new JSONObject();
                jobj.put("id", a.getId());
                jobj.put("count", 0);
                jobj.put("finish", 0);
                jobj.put("receive", 0);
                dataAchive.add(jobj);
            }
            String achive = dataAchive.toJSONString();

            String mabuEgg = "{}";

            JSONArray dataCharms = new JSONArray();
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            dataCharms.add(0);
            String charms = dataCharms.toJSONString();

            int[] skillsArr = gender == 0 ? new int[]{0, 1, 6, 9, 10, 20, 22, 19, 24}
                    : gender == 1 ? new int[]{2, 3, 7, 11, 12, 17, 18, 19, 26}
                    : new int[]{4, 5, 8, 13, 14, 21, 23, 19, 25};
            // [{"temp_id":"4","point":0,"last_time_use":0},]

            // for (int i = 0; i < skillsArr.length; i++) {
            // dataObject.put("temp_id", skillsArr[i]);
            // if (i == 0) {
            // dataObject.put("point", 1);
            // } else {
            // dataObject.put("point", 0);
            // }
            // dataObject.put("last_time_use", 0);
            // dataArray.add(dataObject.toJSONString());
            // dataObject.clear();
            // }
            // String skills = dataArray.toJSONString();
            // dataArray.clear();
            //
            JSONArray dataSkills = new JSONArray();
            for (int i = 0; i < skillsArr.length; i++) {
                JSONArray skill = new JSONArray();
                skill.add(skillsArr[i]);
                skill.add(0);
                if (i == 0 || i == skillsArr.length) {
                    skill.add(1);
                } else {
                    skill.add(0);
                }
                dataSkills.add(skill);
            }
            String skills = dataSkills.toJSONString();

            JSONArray dataSkillShortcut = new JSONArray();
            dataSkillShortcut.add(gender == 0 ? 0 : gender == 1 ? 2 : 4);
            for (int i = 0; i < 8; i++) {
                dataSkillShortcut.add(-1);
            }
            String skillsShortcut = dataSkillShortcut.toJSONString();

            String petInfo = "{}";
            String petPoint = "{}";
            String petBody = "[]";
            String petSkill = "[]";

            JSONArray dataBlackBall = new JSONArray();
            for (int i = 1; i <= 7; i++) {
                JSONArray arr = new JSONArray();
                arr.add(0);
                arr.add(0);
                dataBlackBall.add(arr);
            }
            String blackBall = dataBlackBall.toJSONString();
            String kaminEgg = "{}";

            JSONArray dataWishTree = new JSONArray();
            dataWishTree.add(0);
            dataWishTree.add(0);
            String WishTree = dataWishTree.toJSONString();

            JSONArray dataTopPoint = new JSONArray();

            dataTopPoint.add(0);
            dataTopPoint.add(0);
            dataTopPoint.add(0);
            dataTopPoint.add(0);
            dataTopPoint.add(0);

            String TopPoint = dataTopPoint.toJSONString();

            JSONArray dataThuongde = new JSONArray();

            dataThuongde.add(0);
            dataThuongde.add(0);
            dataThuongde.add(0);
            dataThuongde.add(0);
            dataThuongde.add(0);
            dataThuongde.add(0);

            String Thuongde = dataThuongde.toJSONString();

            // data nhận quà hằng ngày
            JSONArray dataRewardToday = new JSONArray();
            for (int i = 0; i < 30; i++) {
                JSONObject item = new JSONObject();
                item.put("isBuy", 0);
                item.put("tookAttendance", 0);
                item.put("day", i + 1);
                dataRewardToday.add(item);
            }
            String rewardToday = dataRewardToday.toJSONString();

            JSONArray cardReward = new JSONArray();
            int[] rewardValues = {50000, 100000, 150000, 300000, 500000, 800000, 1000000, 1200000};

            for (int i = 0; i < 8; i++) {
                JSONObject item = new JSONObject();
                item.put("isBuy", 0);
                item.put("tookAttendance", 0);
                item.put("card", rewardValues[i % rewardValues.length]);
                cardReward.add(item);
            }
            String cardRewardData = cardReward.toJSONString();

            ps = con.prepareStatement("insert into player"
                    + "(account_id, name, head, gender, have_tennis_space_ship, clan_id_sv" + Manager.SERVER + ", "
                    + "data_inventory, data_location, data_point, data_magic_tree, items_body, "
                    + "items_bag, items_box, items_box_pet_ct, items_box_lucky_round, friends, enemies, data_intrinsic, data_item_time,"
                    + "data_task, data_mabu_egg, data_charm, skills, skills_shortcut, pet_info, pet_point, pet_body, pet_skill,"
                    + "data_black_ball, thoi_vang, data_side_task, achivements, data_kamin, boss_point, Wish_tree, data_top, data_Moc_ThuongDe, vip_point, freeShop,"
                    + "dayReward, taskDayReward, cardReward) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            ps.setInt(1, userId);                  // account_id (int)
            ps.setString(2, name);                 // name (string)
            ps.setInt(3, hair);                    // head (int)
            ps.setByte(4, gender);                 // gender (byte)
            ps.setBoolean(5, false);               // have_tennis_space_ship (boolean)
            ps.setInt(6, -1);                      // clan_id_svN (int)
            ps.setString(7, inventory);            // data_inventory (string)
            ps.setString(8, location);             // data_location (string)
            ps.setString(9, point);                // data_point (string)
            ps.setString(10, magicTree);           // data_magic_tree (string)
            ps.setString(11, itemsBody);           // items_body (string)
            ps.setString(12, itemsBag);            // items_bag (string)
            ps.setString(13, itemsBox);            // items_box (string)
            ps.setString(14, itemsBox_pet_ct);            // items_box (string)
            ps.setString(15, itemsBoxLuckyRound);  // items_box_lucky_round (string)
            ps.setString(16, friends);             // friends (string)
            ps.setString(17, enemies);             // enemies (string)
            ps.setString(18, intrinsic);           // data_intrinsic (string)
            ps.setString(19, itemTime);            // data_item_time (string)
            ps.setString(20, task);                // data_task (string)
            ps.setString(21, mabuEgg);             // data_mabu_egg (string)
            ps.setString(22, charms);              // data_charm (string)
            ps.setString(23, skills);              // skills (string)
            ps.setString(24, skillsShortcut);      // skills_shortcut (string)
            ps.setString(25, petInfo);             // pet_info (string)
            ps.setString(26, petPoint);            // pet_point (string)
            ps.setString(27, petBody);             // pet_body (string)
            ps.setString(28, petSkill);            // pet_skill (string)
            ps.setString(29, blackBall);           // data_black_ball (string)
            ps.setInt(30, 10);                     // thoi_vang (int)
            ps.setString(31, "{}");                // data_side_task (string)
            ps.setString(32, achive);              // achivements (string)
            ps.setString(33, kaminEgg);            // data_kamin (string)
            ps.setString(34, "0");                 // boss_point (string)
            ps.setString(35, WishTree);            // Wish_tree (string)
            ps.setString(36, TopPoint);            // data_top (string)
            ps.setString(37, Thuongde);            // data_Moc_ThuongDe (string, assuming it’s a string column)
            ps.setInt(38, 0);                      // vip_point (int, assuming default value of 0)
            ps.setString(39, freeShop);            // freeShop (string)
            ps.setString(40, rewardToday);         // dayReward (string)
            ps.setString(41, rewardToday);         // taskDayReward (string)

// Lưu dữ liệu cardReward vào trường cardReward riêng biệt
            ps.setString(42, cardRewardData);      // cardReward (string)

            ps.executeUpdate();

            // Log.success("Tạo player mới thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(PlayerDAO.class, e, "Lỗi tạo player mới");
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void updatePlayer(Player player, Connection connection) {
        if (player.isDisposed() || player.isSaving()) {
            return;
        }
        player.setSaving(true);
        try {
            int n1s = 0;
            int n2s = 0;
            int n3s = 0;
            int tv = 0;
            if (player.loaded) {
                long st = System.currentTimeMillis();
                try {

                    JSONArray dataInventory = new JSONArray();
                    // data kim lượng
                    dataInventory.add(player.inventory.gold > Inventory.LIMIT_GOLD
                            ? Inventory.LIMIT_GOLD
                            : player.inventory.gold);
                    dataInventory.add(player.inventory.gem);
                    dataInventory.add(player.inventory.ruby);
                    dataInventory.add(player.inventory.goldLimit);
                    dataInventory.add(player.inventory.topSm);
                    dataInventory.add(player.inventory.topNv);
                    dataInventory.add(player.inventory.topNap);
                    dataInventory.add(player.inventory.event_point);
                    dataInventory.add(player.inventory.top_event);
                    dataInventory.add(player.inventory.activeTitle_1);
                    dataInventory.add(player.inventory.activeTitle_2);
                    dataInventory.add(player.inventory.activeTitle_3);
                    dataInventory.add(player.inventory.activeTitle_4);
                    String inventory = dataInventory.toJSONString();

                    int mapId = -1;
                    mapId = player.mapIdBeforeLogout;
                    int x = player.location.x;
                    int y = player.location.y;
                    int hp = player.nPoint.hp;
                    int mp = player.nPoint.mp;
                    if (player.isDie()) {
                        mapId = player.gender + 21;
                        x = 300;
                        y = 336;
                        hp = 1;
                        mp = 1;
                    } else {
                        if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                                || mapId == 126 || mapId == ConstMap.CON_DUONG_RAN_DOC
                                || mapId == ConstMap.CON_DUONG_RAN_DOC_142 || mapId == ConstMap.CON_DUONG_RAN_DOC_143
                                || mapId == ConstMap.HOANG_MAC) {
                            mapId = player.gender + 21;
                            x = 300;
                            y = 336;
                        }
                    }

                    // data vị trí
                    JSONArray dataLocation = new JSONArray();
                    dataLocation.add(x);
                    dataLocation.add(y);
                    dataLocation.add(mapId);
                    String location = dataLocation.toJSONString();
                    // data free shop
                    JSONArray dataFreeShop = new JSONArray();
                    dataFreeShop.add(player.inventory.free_turn_buy_shop);
                    dataFreeShop.add(player.inventory.time_buy_shop_today);
                    dataFreeShop.add(player.inventory.timeOnline);
                    dataFreeShop.add(player.inventory.sideTaskToDay);
                    String freeShop = dataFreeShop.toJSONString();
                    // data chỉ số
                    JSONArray dataPoint = new JSONArray();
                    dataPoint.add(0);
                    dataPoint.add(mp);
                    dataPoint.add(player.nPoint.mpg);
                    dataPoint.add(player.nPoint.critg);
                    dataPoint.add(player.nPoint.limitPower);
                    dataPoint.add(player.nPoint.stamina);
                    dataPoint.add(hp);
                    dataPoint.add(player.nPoint.defg);
                    dataPoint.add(player.nPoint.tiemNang);
                    dataPoint.add(player.nPoint.maxStamina);
                    dataPoint.add(player.nPoint.dameg);
                    dataPoint.add(player.nPoint.power);
                    dataPoint.add(player.nPoint.hpg);
                    String point = dataPoint.toJSONString();

                    // data đậu thần
                    JSONArray dataMagicTree = new JSONArray();
                    dataMagicTree.add(player.magicTree.isUpgrade ? 1 : 0);
                    dataMagicTree.add(player.magicTree.lastTimeUpgrade);
                    dataMagicTree.add(player.magicTree.level);
                    dataMagicTree.add(player.magicTree.lastTimeHarvest);
                    dataMagicTree.add(player.magicTree.currPeas);
                    String magicTree = dataMagicTree.toJSONString();

                    // data body
                    JSONArray dataBody = new JSONArray();
                    for (Item item : player.inventory.itemsBody) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);
                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBody.add(dataItem);
                    }
                    String itemsBody = dataBody.toJSONString();

                    // data bag
                    JSONArray dataBag = new JSONArray();
                    for (Item item : player.inventory.itemsBag) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            switch (item.template.id) {
                                case 14:
                                    n1s += item.quantity;
                                    break;
                                case 15:
                                    n2s += item.quantity;
                                    break;
                                case 16:
                                    n3s += item.quantity;
                                    break;
                                case 457:
                                    tv += item.quantity;
                                    break;
                            }
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);

                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBag.add(dataItem);
                    }
                    String itemsBag = dataBag.toJSONString();

                    // data box
                    JSONArray dataBox = new JSONArray();
                    for (Item item : player.inventory.itemsBox) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            switch (item.template.id) {
                                case 14:
                                    n1s += item.quantity;
                                    break;
                                case 15:
                                    n2s += item.quantity;
                                    break;
                                case 16:
                                    n3s += item.quantity;
                                    break;
                                case 457:
                                    tv += item.quantity;
                                    break;
                            }
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);

                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBox.add(dataItem);
                    }
                    String itemsBox = dataBox.toJSONString();

                    // data box pet ct
                    JSONArray dataBox_pet_ct = new JSONArray();
                    for (Item item : player.inventory.itemsBox_ct_pet) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            JSONArray options = new JSONArray();
                            switch (item.template.id) {
                                case 14:
                                    n1s += item.quantity;
                                    break;
                                case 15:
                                    n2s += item.quantity;
                                    break;
                                case 16:
                                    n3s += item.quantity;
                                    break;
                                case 457:
                                    tv += item.quantity;
                                    break;
                            }
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);

                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataBox_pet_ct.add(dataItem);
                    }
                    String itemsBox_pet_ct = dataBox_pet_ct.toJSONString();

                    // data box crack ball
                    JSONArray dataCrackBall = new JSONArray();
                    for (Item item : player.inventory.itemsBoxCrackBall) {
                        JSONObject dataItem = new JSONObject();
                        if (item.isNotNullItem()) {
                            dataItem.put("temp_id", item.template.id);
                            dataItem.put("quantity", item.quantity);
                            dataItem.put("create_time", item.createTime);
                            JSONArray options = new JSONArray();
                            for (ItemOption io : item.itemOptions) {
                                JSONArray option = new JSONArray();
                                option.add(io.optionTemplate.id);
                                option.add(io.param);
                                options.add(option);
                            }
                            dataItem.put("option", options);
                        } else {
                            JSONArray options = new JSONArray();
                            dataItem.put("temp_id", -1);
                            dataItem.put("quantity", 0);
                            dataItem.put("create_time", 0);
                            dataItem.put("option", options);
                        }
                        dataCrackBall.add(dataItem);
                    }
                    String itemsBoxLuckyRound = dataCrackBall.toJSONString();

                    //data limit
                    JSONArray datavatDaichien = new JSONArray();
                    datavatDaichien.add(player.joindaichien);
                    datavatDaichien.add(player.joinfree);
                    String Datadaichien = datavatDaichien.toJSONString();

                    // data bạn bè
                    JSONArray dataFriends = new JSONArray();
                    for (Friend f : player.friends) {
                        JSONObject friend = new JSONObject();
                        friend.put("id", f.id);
                        friend.put("name", f.name);
                        friend.put("power", f.power);
                        friend.put("head", f.head);
                        friend.put("body", f.body);
                        friend.put("leg", f.leg);
                        friend.put("bag", f.bag);
                        dataFriends.add(friend);
                    }
                    String friend = dataFriends.toJSONString();

                    // data kẻ thù
                    JSONArray dataEnemies = new JSONArray();
                    for (Friend e : player.enemies) {
                        JSONObject enemy = new JSONObject();
                        enemy.put("id", e.id);
                        enemy.put("name", e.name);
                        enemy.put("power", e.power);
                        enemy.put("head", e.head);
                        enemy.put("body", e.body);
                        enemy.put("leg", e.leg);
                        enemy.put("bag", e.bag);
                        dataEnemies.add(enemy);
                    }
                    String enemy = dataEnemies.toJSONString();

                    // data nội tại
                    JSONArray dataIntrinsic = new JSONArray();
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.id);
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.param1);
                    dataIntrinsic.add(player.playerIntrinsic.countOpen);
                    dataIntrinsic.add(player.playerIntrinsic.intrinsic.param2);
                    String intrinsic = dataIntrinsic.toJSONString();

                    // data item time
                    JSONArray dataItemTime = new JSONArray();
                    dataItemTime.add(player.itemTime.isUseBoKhi
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseAnDanh
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh))
                            : 0);
                    dataItemTime
                            .add(player.itemTime.isOpenPower
                                    ? (ItemTime.TIME_OPEN_POWER
                                    - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower))
                                    : 0);
                    dataItemTime.add(player.itemTime.isUseCuongNo
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseMayDo
                            ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseBoHuyet
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet))
                            : 0);
                    dataItemTime.add(player.itemTime.iconMeal);
                    dataItemTime.add(player.itemTime.isEatMeal
                            ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGiapXen
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseBanhChung
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhChung))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseBanhTet
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTet))
                            : 0);

                    dataItemTime.add(player.itemTime.isUseBoKhi2
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGiapXen2
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseCuongNo2
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseBoHuyet2
                            ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseMdSkh
                            ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeMdSkh))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseBohoaHong
                            ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeBohoaHong))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseMaydoBongtoi
                            ? (ItemTime.TIME_MAY_DO
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseMaydoBongtoi))
                            : 0);
                    // x2 x3 x4
                    dataItemTime.add(player.itemTime.isUseX2TNSM
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseX2TNSM))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseX3TNSM
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseX3TNSM))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseX4TNSM
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseX4TNSM))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseThitSuon
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeuseThitSuon))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseThitThan
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeuseThitThan))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseDauVe
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseDauve))
                            : 0);
                    // group 1
                    dataItemTime.add(player.itemTime.isUseGroup_1_1
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_1_1))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_1_2
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_1_2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_1_3
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_1_3))
                            : 0);
                    // group 2
                    dataItemTime.add(player.itemTime.isUseGroup_2_1
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_2_1))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_2_2
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_2_2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_2_3
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_2_3))
                            : 0);
                    // group 3
                    dataItemTime.add(player.itemTime.isUseGroup_3_1
                            ? (ItemTime.TIME_MAY_DO
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_3_1))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_3_2
                            ? (ItemTime.TIME_MAY_DO
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_3_2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_3_3
                            ? (ItemTime.TIME_60_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_3_3))
                            : 0);
                    // group 4
                    dataItemTime.add(player.itemTime.isUseGroup_4_1
                            ? (ItemTime.TIME_90_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_4_1))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_4_2
                            ? (ItemTime.TIME_90_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_4_2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_4_3
                            ? (ItemTime.TIME_90_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_4_3))
                            : 0);
                    dataItemTime.add(player.lastTimexDameChuong);
                    // Bánh trung thu
                    dataItemTime.add(player.itemTime.isUseBanhTrungThu_1
                            ? (ItemTime.TIME_60_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu_1))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseBanhTrungThu_2
                            ? (ItemTime.TIME_90_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu_2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseBanhTrungThu_3
                            ? (ItemTime.TIME_120_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu_3))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseBanhTrungThu_4
                            ? (ItemTime.TIME_150_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu_4))
                            : 0);
                    // group 5 10 phút
                    dataItemTime.add(player.itemTime.isUseGroup_5_1
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_1))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_5_2
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_5_3
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_3))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_5_4
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_4))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_5_5
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_5))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_5_6
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_6))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_5_7
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_7))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_5_8
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_8))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_5_9
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_9))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_5_10
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_10))
                            : 0);
                    // Gorup 6 30 phút
                    dataItemTime.add(player.itemTime.isUseGroup_6_1
                            ? (ItemTime.TIME_MAY_DO
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_6_1))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_6_2
                            ? (ItemTime.TIME_MAY_DO
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_6_2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_6_3
                            ? (ItemTime.TIME_MAY_DO
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_6_3))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_6_4
                            ? (ItemTime.TIME_MAY_DO
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_6_4))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_6_5
                            ? (ItemTime.TIME_MAY_DO
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_6_5))
                            : 0);
                    // Gorup 7 60 phút
                    dataItemTime.add(player.itemTime.isUseGroup_7_1
                            ? (ItemTime.TIME_MAY_DO
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_7_1))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_7_2
                            ? (ItemTime.TIME_60_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_7_2))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_7_3
                            ? (ItemTime.TIME_60_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_7_3))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_7_4
                            ? (ItemTime.TIME_60_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_7_4))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseGroup_7_5
                            ? (ItemTime.TIME_60_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_7_5))
                            : 0);
                    dataItemTime.add(player.itemTime.isUseHoangHoa
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeUseHoangHoa))
                            : 0);
                    dataItemTime.add(player.itemTime.isHuyHieu
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeHuyHieu))
                            : 0);
                    dataItemTime.add(player.itemTime.isCaChua
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeCaChua))
                            : 0);
                    dataItemTime.add(player.itemTime.isCaRot
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeCaRot))
                            : 0);
                    dataItemTime.add(player.itemTime.isChuoi
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeChuoi))
                            : 0);
                    dataItemTime.add(player.itemTime.isKeoBayTay
                            ? (ItemTime.TIME_ITEM
                            - (System.currentTimeMillis() - player.itemTime.lastTimeKeoBanTay))
                            : 0);
                    dataItemTime.add(player.itemTime.isDaiHaiTrinh
                            ? (ItemTime.TIME_60_MIN
                            - (System.currentTimeMillis() - player.itemTime.lastTimeDaiHaiTrinh))
                            : 0);
                    String itemTime = dataItemTime.toJSONString();

                    // data nhiệm vụ
                    JSONArray dataTask = new JSONArray();
                    dataTask.add(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
                    dataTask.add(player.playerTask.taskMain.id);
                    dataTask.add(player.playerTask.taskMain.index);
                    String task = dataTask.toJSONString();

                    // data nhiệm vụ hàng ngày
                    JSONArray dataSideTask = new JSONArray();
                    dataSideTask.add(
                            player.playerTask.sideTask.template != null ? player.playerTask.sideTask.template.id : -1);
                    dataSideTask.add(player.playerTask.sideTask.level);
                    dataSideTask.add(player.playerTask.sideTask.count);
                    dataSideTask.add(player.playerTask.sideTask.maxCount);
                    dataSideTask.add(player.playerTask.sideTask.leftTask);
                    dataSideTask.add(player.playerTask.sideTask.receivedTime);

                    String sideTask = dataSideTask.toJSONString();

                    JSONArray dataAchive = new JSONArray();
                    for (Achivement a : player.playerTask.achivements) {
                        JSONObject jobj = new JSONObject();
                        jobj.put("id", a.getId());
                        jobj.put("count", a.getCount());
                        jobj.put("finish", a.isFinish() ? 1 : 0);
                        jobj.put("receive", a.isReceive() ? 1 : 0);
                        dataAchive.add(jobj);
                    }
                    String achive = dataAchive.toJSONString();

                    // data trứng bư
                    JSONObject dataMaBu = new JSONObject();
                    if (player.mabuEgg != null) {
                        dataMaBu.put("create_time", player.mabuEgg.lastTimeCreate);
                        dataMaBu.put("time_done", player.mabuEgg.timeDone);
                    }
                    String mabuEgg = dataMaBu.toJSONString();
                    JSONObject dataKamin = new JSONObject();
                    if (player.kaminEgg != null) {
                        dataKamin.put("create_time", player.kaminEgg.lastTimeCreate);
                        dataKamin.put("time_done", player.kaminEgg.timeDone);
                    }
                    String kaminEgg = dataKamin.toJSONString();
                    // data bùa
                    JSONArray dataCharms = new JSONArray();
                    dataCharms.add(player.charms.tdTriTue);
                    dataCharms.add(player.charms.tdManhMe);
                    dataCharms.add(player.charms.tdDaTrau);
                    dataCharms.add(player.charms.tdOaiHung);
                    dataCharms.add(player.charms.tdBatTu);
                    dataCharms.add(player.charms.tdDeoDai);
                    dataCharms.add(player.charms.tdThuHut);
                    dataCharms.add(player.charms.tdDeTu);
                    dataCharms.add(player.charms.tdTriTue3);
                    dataCharms.add(player.charms.tdTriTue4);
                    dataCharms.add(player.charms.tdDeTuMabu);
                    dataCharms.add(player.charms.tdDeTuMabu2);
                    dataCharms.add(player.charms.tdDeTuMabu3);
                    dataCharms.add(player.charms.tdPhuHP);
                    dataCharms.add(player.charms.tdPhuKI);
                    dataCharms.add(player.charms.tdPhuSD);
                    dataCharms.add(player.charms.tdPhuTNSM);
                    String charm = dataCharms.toJSONString();

                    // data skill
                    JSONArray dataSkills = new JSONArray();
                    for (Skill skill : player.playerSkill.skills) {
                        // if (skill.skillId != -1) {
                        JSONArray dataskill = new JSONArray();
                        dataskill.add(skill.template.id);
                        dataskill.add(skill.lastTimeUseThisSkill);
                        dataskill.add(skill.point);
                        // } else {
                        // dataObject.put("temp_id", -1);
                        // dataObject.put("point", 0);
                        // dataObject.put("last_time_use", 0);
                        // }
                        dataSkills.add(dataskill);
                    }
                    String skills = dataSkills.toJSONString();

                    JSONArray dataSkillShortcut = new JSONArray();
                    // data skill shortcut
                    for (int skillId : player.playerSkill.skillShortCut) {
                        dataSkillShortcut.add(skillId);
                    }
                    String skillShortcut = dataSkillShortcut.toJSONString();

                    JSONObject jPetInfo = new JSONObject();
                    JSONObject jPetPoint = new JSONObject();
                    JSONArray jPetBody = new JSONArray();
                    JSONArray jPetSkills = new JSONArray();
                    String petInfo = jPetInfo.toJSONString();
                    String petPoint = jPetPoint.toJSONString();
                    String petBody = jPetBody.toJSONString();
                    String petSkill = jPetSkills.toJSONString();

                    JSONArray dataChallenge = new JSONArray();
                    dataChallenge.add(player.goldChallenge);
                    dataChallenge.add(player.levelWoodChest);
                    dataChallenge.add(player.receivedWoodChest ? 1 : 0);
                    String challenge = dataChallenge.toJSONString();

                    int TopDhVt = player.receivedTopDhVT ? 1 : 0;

                    JSONArray dataWish = new JSONArray();
                    dataWish.add(player.lastTimeWish);
                    dataWish.add(player.isWish == true ? 1 : 0);
                    String Wishing = dataWish.toJSONString();

                    JSONArray dataSuKienTet = new JSONArray();
                    dataSuKienTet.add(player.event.getTimeCookTetCake());
                    dataSuKienTet.add(player.event.getTimeCookChungCake());
                    dataSuKienTet.add(player.event.isCookingTetCake() ? 1 : 0);
                    dataSuKienTet.add(player.event.isCookingChungCake() ? 1 : 0);
                    dataSuKienTet.add(player.event.isReceivedLuckyMoney() ? 1 : 0);
                    String skTet = dataSuKienTet.toJSONString();

                    JSONArray dataBuyLimit = new JSONArray();
                    for (int i = 0; i < player.buyLimit.length; i++) {
                        dataBuyLimit.add(player.buyLimit[i]);
                    }
                    String buyLimit = dataBuyLimit.toJSONString();

                    JSONArray dataRwLimit = new JSONArray();
                    for (int i = 0; i < player.getRewardLimit().length; i++) {
                        dataRwLimit.add(player.getRewardLimit()[i]);
                    }
                    String rwLimit = dataRwLimit.toJSONString();

                    // data pet
                    if (player.pet != null) {
                        jPetInfo.put("name", player.pet.baseName);
                        jPetInfo.put("gender", player.pet.gender);
                        jPetInfo.put("is_mabu", player.pet.isMabu ? 1 : 0);
                        jPetInfo.put("is_Wukong", player.pet.isBU ? 1 : 0);
                        jPetInfo.put("is_Heo", player.pet.isCell ? 1 : 0);
                        jPetInfo.put("is_Satang", player.pet.isFide ? 1 : 0);
                        jPetInfo.put("is_Goku", player.pet.isGoku ? 1 : 0);
                        // if (player.pet.isMabu) {
                        jPetInfo.put("L_vel_Zeno", player.pet.LevelZeno);
                        // }
                        jPetInfo.put("status", player.pet.status);
                        jPetInfo.put("type_fusion", player.fusion.typeFusion);
                        int timeLeftFusion = (int) (Fusion.TIME_FUSION
                                - (System.currentTimeMillis() - player.fusion.lastTimeFusion));
                        jPetInfo.put("left_fusion", timeLeftFusion < 0 ? 0 : timeLeftFusion);
                        petInfo = jPetInfo.toJSONString();

                        jPetPoint.put("power", player.pet.nPoint.power);
                        jPetPoint.put("tiem_nang", player.pet.nPoint.tiemNang);
                        jPetPoint.put("stamina", player.pet.nPoint.stamina);
                        jPetPoint.put("max_stamina", player.pet.nPoint.maxStamina);
                        jPetPoint.put("hpg", player.pet.nPoint.hpg);
                        jPetPoint.put("mpg", player.pet.nPoint.mpg);
                        jPetPoint.put("damg", player.pet.nPoint.dameg);
                        jPetPoint.put("defg", player.pet.nPoint.defg);
                        jPetPoint.put("critg", player.pet.nPoint.critg);
                        jPetPoint.put("limit_power", player.pet.nPoint.limitPower);
                        jPetPoint.put("hp", player.pet.nPoint.hp);
                        jPetPoint.put("mp", player.pet.nPoint.mp);
                        petPoint = jPetPoint.toJSONString();

                        for (Item item : player.pet.inventory.itemsBody) {
                            JSONObject dataItem = new JSONObject();
                            if (item.isNotNullItem()) {
                                dataItem.put("temp_id", item.template.id);
                                dataItem.put("quantity", item.quantity);
                                dataItem.put("create_time", item.createTime);
                                JSONArray options = new JSONArray();
                                for (ItemOption io : item.itemOptions) {
                                    JSONArray option = new JSONArray();
                                    option.add(io.optionTemplate.id);
                                    option.add(io.param);
                                    options.add(option);
                                }
                                dataItem.put("option", options);
                            } else {
                                JSONArray options = new JSONArray();
                                dataItem.put("temp_id", -1);
                                dataItem.put("quantity", 0);
                                dataItem.put("create_time", 0);
                                dataItem.put("option", options);
                            }
                            jPetBody.add(dataItem);
                        }
                        petBody = jPetBody.toJSONString();

                        for (Skill s : player.pet.playerSkill.skills) {
                            JSONArray pskill = new JSONArray();
                            if (s.skillId != -1) {
                                pskill.add(s.template.id);
                                pskill.add(s.point);
                            } else {
                                pskill.add(-1);
                                pskill.add(0);
                            }
                            jPetSkills.add(pskill);
                        }
                        petSkill = jPetSkills.toJSONString();
                    }

                    JSONArray dataBlackBall = new JSONArray();
                    // data thưởng ngọc rồng đen
                    for (int i = 1; i <= 7; i++) {
                        JSONArray data = new JSONArray();
                        data.add(player.rewardBlackBall.timeOutOfDateReward[i - 1]);
                        data.add(player.rewardBlackBall.lastTimeGetReward[i - 1]);
                        dataBlackBall.add(data);
                    }
                    String blackBall = dataBlackBall.toJSONString();

                    // data top
                    JSONArray dataTopPoint = new JSONArray();

                    dataTopPoint.add(player.inventory.top_suc_manh);
                    dataTopPoint.add(player.inventory.top_suc_manh_de_tu);
                    dataTopPoint.add(player.inventory.top_nhiem_vu);
                    dataTopPoint.add(player.inventory.top_nap);
                    dataTopPoint.add(player.inventory.top_suc_manh_tuan);

                    String TopPoint = dataTopPoint.toJSONString();

                    // data nhận quà hằng ngày
                    JSONArray dataRewardToday = new JSONArray();
                    for (DataShopReward item : player.inventory.dShopDays) {
                        JSONObject dataItem = new JSONObject();
                        dataItem.put("isBuy", item.isBuy ? 1 : 0);
                        dataItem.put("tookAttendance", item.tookAttendance ? 1 : 0);
                        dataItem.put("day", item.target);
                        dataRewardToday.add(dataItem);
                    }
                    String rewardToday = dataRewardToday.toJSONString();
                    // data nhận quà online
                    JSONArray dataRewardTime = new JSONArray();
                    for (DataShopReward item : player.inventory.dShopTimes) {
                        JSONObject dataItem = new JSONObject();
                        dataItem.put("isBuy", item.isBuy ? 1 : 0);
                        dataItem.put("tookAttendance", item.tookAttendance ? 1 : 0);
                        dataItem.put("timeGive", item.target);
                        dataRewardTime.add(dataItem);
                    }
                    String rewardTime = dataRewardTime.toJSONString();
                    // data nhận quà mốc nạp
                    JSONArray dataRewardCard = new JSONArray();
                    for (DataShopReward item : player.inventory.dShopNaps) {
                        JSONObject dataItem = new JSONObject();
                        dataItem.put("isBuy", item.isBuy ? 1 : 0);
                        dataItem.put("tookAttendance", item.tookAttendance ? 1 : 0);
                        dataItem.put("card", item.target);
                        dataRewardCard.add(dataItem);
                    }
                    String rewardCard = dataRewardCard.toJSONString();
                    // data nhận quà nhiệm vụ Lý Tiểu Nương
                    JSONArray dataRewardTaskDay = new JSONArray();
                    for (DataShopReward item : player.inventory.dShopTasks) {
                        JSONObject dataItem = new JSONObject();
                        dataItem.put("isBuy", item.isBuy ? 1 : 0);
                        dataItem.put("tookAttendance", item.tookAttendance ? 1 : 0);
                        dataItem.put("day", item.target);
                        dataRewardTaskDay.add(dataItem);
                    }
                    String rewardTaskDay = dataRewardTaskDay.toJSONString();
                    // data nhận quà mốc sức mạnh
                    JSONArray dataRewardPower = new JSONArray();
                    for (DataShopReward item : player.inventory.dShopPowers) {
                        JSONObject dataItem = new JSONObject();
                        dataItem.put("isBuy", item.isBuy ? 1 : 0);
                        dataItem.put("tookAttendance", item.tookAttendance ? 1 : 0);
                        dataItem.put("card", item.target);
                        dataRewardPower.add(dataItem);
                    }
                    JSONArray dataThuongDe = new JSONArray();
                    for (boolean td : player.mocThuongDe) {
                        dataThuongDe.add(td ? 1 : 0);
                    }
                    String dtThuongDe = dataThuongDe.toJSONString();

                    String rewardPower = dataRewardPower.toJSONString();
                    Gson gson = new Gson();
                    PreparedStatement ps = null;
                    try {
                        ps = connection.prepareStatement("update player set head = ?, have_tennis_space_ship = ?,"
                                + "clan_id_sv" + Manager.SERVER
                                + " = ?, data_inventory = ?, data_location = ?, data_point = ?, data_magic_tree = ?,"
                                + "items_body = ?, items_bag = ?, items_box = ?, items_box_pet_ct = ?, items_box_lucky_round = ?, friends = ?,"
                                + "enemies = ?, data_intrinsic = ?, data_item_time = ?, data_task = ?, data_mabu_egg = ?,"
                                + "pet_info = ?, pet_point = ?, pet_body = ?, pet_skill = ? , power = ?, pet_power = ?, "
                                + "data_black_ball = ?, data_side_task = ?, data_charm = ?, skills = ?, skills_shortcut = ?,"
                                + "thoi_vang = ?, 1sao = ?, 2sao = ?, 3sao = ?, collection_book = ?, event_point = ?, firstTimeLogin = ?,"
                                + " challenge = ?, sk_tet = ?, buy_limit = ?, moc_nap = ?,achivements = ? , reward_limit = ?,data_kamin = ?,boss_point =?, Wish_tree = ?,"
                                + "topGapthu = ?, RuongBauPoint = ? , BauCuaPoint = ?, data_top = ?, TopDhVt = ?, freeShop = ?, dayReward = ?, timeReward = ?, cardReward = ?,"
                                + " taskDayReward = ?, powerReward = ?, data_Moc_ThuongDe = ?,topevent = ?, sell_tv = ? , time_ban = ? , data_daichien = ?, napdau = ?, vip1 = ? where id = ?");

                        ps.setShort(1, player.head);
                        ps.setBoolean(2, player.haveTennisSpaceShip);
                        ps.setShort(3, (short) (player.clan != null ? player.clan.id : -1));
                        ps.setString(4, inventory);
                        ps.setString(5, location);
                        ps.setString(6, point);
                        ps.setString(7, magicTree);
                        ps.setString(8, itemsBody);
                        ps.setString(9, itemsBag);
                        ps.setString(10, itemsBox);
                        ps.setString(11, itemsBox_pet_ct);
                        ps.setString(12, itemsBoxLuckyRound);
                        ps.setString(13, friend);
                        ps.setString(14, enemy);
                        ps.setString(15, intrinsic);
                        ps.setString(16, itemTime);
                        ps.setString(17, task);
                        ps.setString(18, mabuEgg);
                        ps.setString(19, petInfo);
                        ps.setString(20, petPoint);
                        ps.setString(21, petBody);
                        ps.setString(22, petSkill);
                        ps.setLong(23, player.nPoint.power);
                        ps.setLong(24, player.pet != null ? player.pet.nPoint.power : 0);
                        ps.setString(25, blackBall);
                        ps.setString(26, sideTask);
                        ps.setString(27, charm);
                        ps.setString(28, skills);
                        ps.setString(29, skillShortcut);
                        ps.setInt(30, tv);
                        ps.setInt(31, n1s);
                        ps.setInt(32, n2s);
                        ps.setInt(33, n3s);
                        ps.setString(34, gson.toJson(player.getCollectionBook().getCards()));
                        ps.setInt(35, player.event.getEventPoint());
                        ps.setString(36, Util.toDateString(player.firstTimeLogin));
                        ps.setString(37, challenge);
                        ps.setString(38, skTet);
                        ps.setString(39, buyLimit);
                        ps.setInt(40, player.event.getMocNapDaNhan());
                        ps.setString(41, achive);
                        ps.setString(42, rwLimit);
                        ps.setString(43, kaminEgg);
                        ps.setInt(44, (int) player.bosspoint);
                        ps.setString(45, Wishing);
                        ps.setInt(46, (int) player.GapthuPoint);
                        ps.setInt(47, (int) player.RuongbauPoint);
                        ps.setInt(48, (int) player.PauCuaPoint);
                        ps.setString(49, TopPoint);
                        ps.setInt(50, (int) TopDhVt);
                        ps.setString(51, freeShop);
                        ps.setString(52, rewardToday);
                        ps.setString(53, rewardTime);
                        ps.setString(54, rewardCard);
                        ps.setString(55, rewardTaskDay);
                        ps.setString(56, rewardPower);
                        ps.setString(57, dtThuongDe);
                        ps.setInt(58, player.pointSK);
                        ps.setInt(59, player.pointThoiVang);
                        ps.setLong(60, player.lastTimeBan2);
                        ps.setString(61, Datadaichien);
                        ps.setInt(62, player.napDau);
                         ps.setInt(63, player.vip1);
//                         ps.setInt(64, player.diemtuanloc);
                        ps.setInt(64, (int) player.id);
                        ps.executeUpdate();
                        // ServerLogSavePlayer.gI().add(ps.toString());
                        // Log.success("Total time save player " + player.name + " thành công! " +
                        // (System.currentTimeMillis() - st));
                        if (updateTimeLogout) {
                            AccountDAO.updateAccoutLogout(player.getSession());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            ps.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.error(PlayerDAO.class, e, "Lỗi save player " + player.name);
                } finally {

                }
            }
        } finally {
            player.setSaving(false);
        }
    }

   public static void addDiemTuanLoc(Player player, long playerId, int add) {
    PreparedStatement ps = null;
    try (Connection con = DBService.gI().getConnectionForSaveData()) {

        ps = con.prepareStatement(
            "UPDATE player SET diemtuanloc = diemtuanloc + ? WHERE id = ?"
        );
        ps.setInt(1, add);
        ps.setLong(2, playerId);
        ps.executeUpdate();

    } catch (Exception e) {
        Log.error(PlayerDAO.class,
                e, "Lỗi update diemtuanloc " + player.name);
    } finally {
        try {
            if (ps != null) ps.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PlayerDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
public static void addDiemTetDuong(Player player, long playerId, int add) {
    PreparedStatement ps = null;
    try (Connection con = DBService.gI().getConnectionForSaveData()) {

        ps = con.prepareStatement(
            "UPDATE player SET tetduong = tetduong + ? WHERE id = ?"
        );
        ps.setInt(1, add);
        ps.setLong(2, playerId);
        ps.executeUpdate();

    } catch (Exception e) {
        Log.error(PlayerDAO.class,
                e, "Lỗi update tetduong " + player.name);
    } finally {
        try {
            if (ps != null) ps.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PlayerDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    
    public static void saveName(Player player) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update player set name = ? where id = ?");
            ps.setString(1, player.name);
            ps.setInt(2, (int) player.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isExistName(String name) {
        boolean exist = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGame();) {
            ps = con.prepareStatement("select * from player where name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return exist;
    }

    public static void subRuby(Player player, int userId, int ruby) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set ruby = ruby - ? where id = ?");
            ps.setInt(1, ruby);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(PlayerDAO.class, e, "Lỗi update ruby " + player.name);
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void addbosspoint(int id, int num) {

        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update player set boss_point = (boss_point + ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(PlayerDAO.class, e, "Lỗi update boss point ");
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void subGoldBar(int id, int num) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set thoi_vang = (thoi_vang - ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng ");
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static boolean subCountCardIfEnough(int id, int num) {
    PreparedStatement ps = null;
    try (Connection con = DBService.gI().getConnectionForSaveData()) {
        ps = con.prepareStatement(
            "UPDATE account SET count_card = count_card - ? WHERE id = ? AND count_card >= ?"
        );
        ps.setInt(1, num);
        ps.setInt(2, id);
        ps.setInt(3, num);
        return ps.executeUpdate() > 0; // 1 = trừ thành công, 0 = không đủ
    } catch (Exception e) {
        Log.error(PlayerDAO.class, e, "Lỗi update count_card (if enough)");
        return false;
    } finally {
        try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
    }
}


    
    public static void subVND(int id, int num) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set vnd = (vnd - ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng ");
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void ActivedPlayer(int plId) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("update account set active = true where id = ?");
            ps.setInt(1, plId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(PlayerDAO.class, e, "Lỗi update thành viên ! ");
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void addHistoryReceiveGoldBar(Player player, int goldBefore, int goldAfter,
            int goldBagBefore, int goldBagAfter, int goldBoxBefore, int goldBoxAfter) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            ps = con.prepareStatement("insert into history_receive_goldbar(player_id,player_name,gold_before_receive,"
                    + "gold_after_receive,gold_bag_before,gold_bag_after,gold_box_before,gold_box_after) values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, (int) player.id);
            ps.setString(2, player.name);
            ps.setInt(3, goldBefore);
            ps.setInt(4, goldAfter);
            ps.setInt(5, goldBagBefore);
            ps.setInt(6, goldBagAfter);
            ps.setInt(7, goldBoxBefore);
            ps.setInt(8, goldBoxAfter);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateItemReward(Player player) {
        String dataItemReward = "";
        for (Item item : player.getSession().itemsReward) {
            if (item.isNotNullItem()) {
                dataItemReward += "{" + item.template.id + ":" + item.quantity;
                if (!item.itemOptions.isEmpty()) {
                    dataItemReward += "|";
                    for (ItemOption io : item.itemOptions) {
                        dataItemReward += "[" + io.optionTemplate.id + ":" + io.param + "],";
                    }
                    dataItemReward = dataItemReward.substring(0, dataItemReward.length() - 1) + "};";
                }
            }
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = DBService.gI().getConnectionForGetPlayer();) {
            ps = con.prepareStatement("update account set reward = ? where id = ?");
            ps.setString(1, dataItemReward);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
        } catch (Exception e) {
            Log.error(PlayerDAO.class, e, "Lỗi update phần thưởng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public static void saveBag(Connection con, Player player) {
        if (player.loaded) {
            PreparedStatement ps = null;
            try {
                JSONArray dataBag = new JSONArray();
                for (Item item : player.inventory.itemsBag) {
                    JSONObject dataItem = new JSONObject();
                    if (item.isNotNullItem()) {
                        dataItem.put("temp_id", item.template.id);
                        dataItem.put("quantity", item.quantity);
                        dataItem.put("create_time", item.createTime);
                        JSONArray options = new JSONArray();
                        for (ItemOption io : item.itemOptions) {
                            JSONArray option = new JSONArray();
                            option.add(io.optionTemplate.id);
                            option.add(io.param);
                            options.add(option);
                        }
                        dataItem.put("option", options);
                    } else {
                        JSONArray options = new JSONArray();
                        dataItem.put("temp_id", -1);
                        dataItem.put("quantity", 0);
                        dataItem.put("create_time", 0);
                        dataItem.put("option", options);
                    }
                    dataBag.add(dataItem);
                }
                String itemsBag = dataBag.toJSONString();

                ps = con.prepareStatement("update player set items_bag = ? where id = ?");
                ps.setString(1, itemsBag);
                ps.setInt(2, (int) player.id);
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                Log.error(PlayerDAO.class, e, "Lỗi save bag player " + player.name);
            } finally {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static int checkTopSucManh(Player player) {
        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement("select topsucmanh from player  where name = ?");) {

            ps.setString(1, player.name);
            ResultSet rs = ps.executeQuery();
            int nhanqua = 0;
            while (rs.next()) {
                nhanqua = rs.getInt("topsucmanh");
            }
            rs.close();
            ps.close();
            con.close();
            return nhanqua;
        } catch (Exception e) {
            Logger.error(" error check toptsucmanh");
            return -1;
        }
    }

    public static boolean updateTopSucManh(Player player, int num) {
        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement("update player  set topsucmanh = ? where name = ?");) {

            ps.setInt(1, num);
            ps.setString(2, player.name);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            Logger.error(" error ");
            return false;
        }
    }

    public static int checkTopNV(Player player) {
        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement("select topsucmanh from player  where name = ?");) {

            ps.setString(1, player.name);
            ResultSet rs = ps.executeQuery();
            int nhanqua = 0;
            while (rs.next()) {
                nhanqua = rs.getInt("topsucmanh");
            }
            rs.close();
            ps.close();
            con.close();
            return nhanqua;
        } catch (Exception e) {
            Logger.error(" error check topsucmanh");
            return -1;
        }
    }

    public static boolean updateTopNV(Player player, int num) {
        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement("update player  set topsucmanh = ? where name = ?");) {
            ps.setInt(1, num);
            ps.setString(2, player.name);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            Logger.error(" error ");
            return false;
        }
    }

    public static int checkTopNapThe(Player player) {
        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement("select topnapthe from player  where name = ?");) {

            ps.setString(1, player.name);
            ResultSet rs = ps.executeQuery();
            int nhanqua = 0;
            while (rs.next()) {
                nhanqua = rs.getInt("topnapthe");
            }
            rs.close();
            ps.close();
            con.close();
            return nhanqua;
        } catch (Exception e) {
            Logger.error(" error check topnapthe");
            return -1;
        }
    }

    public static boolean updateTopNapThe(Player player, int num) {
        try (Connection con = DBService.gI().getConnection(); PreparedStatement ps = con.prepareStatement("update player  set topnapthe = ? where name = ?");) {
            ps.setInt(1, num);
            ps.setString(2, player.name);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            Logger.error(" error ");
            return false;
        }
    }

    public static int checkShareFanpage(Player player) {
        try (Connection con = DBService.gI().getConnectionForShareFanpage(); PreparedStatement ps = con.prepareStatement("select share_fanpage from player  where name = ?");) {

            ps.setString(1, player.name);
            ResultSet rs = ps.executeQuery();
            int nhanqua = 0;
            while (rs.next()) {
                nhanqua = rs.getInt("share_fanpage");
            }
            rs.close();
            ps.close();
            con.close();
            return nhanqua;
        } catch (Exception e) {
            Logger.error(" error check share_fanpage");
            return -1;
        }
    }

    public static void updateNapDau(Player player) {
    try (Connection con = DBService.gI().getConnectionForGame();
         PreparedStatement ps = con.prepareStatement(
             "UPDATE player SET napdau = ? WHERE name = ?")) {

        ps.setInt(1, player.napDau);     // giá trị 0 hoặc 1
        ps.setString(2, player.name);
        ps.executeUpdate();

    } catch (Exception e) {
        Log.error(PlayerDAO.class, e);
    }
}

    public static boolean updateShareFanpage(Player player, int num) {
        try (Connection con = DBService.gI().getConnectionForShareFanpage(); PreparedStatement ps = con.prepareStatement("update player  set share_fanpage = ? where name = ?");) {

            ps.setInt(1, num);
            ps.setString(2, player.name);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            Logger.error(" share_fanpage ");
            return false;
        }
    }

    public static String getDataShopWeb(Player player) {
        try (Connection con = DBService.gI().getConnectionForShareFanpage(); PreparedStatement ps = con.prepareStatement("select item_bag_web from player  where name = ?");) {

            ps.setString(1, player.name);
            ResultSet rs = ps.executeQuery();
            String data_bag_web = "";
            while (rs.next()) {
                data_bag_web = rs.getString("item_bag_web");
            }
            rs.close();
            ps.close();
            con.close();
            return data_bag_web;
        } catch (Exception e) {
            Logger.error(" error check item_bag_web");
            return "[]";
        }
    }

    public static boolean clearDataShopWeb(Player player) {
        try (Connection con = DBService.gI().getConnectionForShareFanpage(); PreparedStatement ps = con.prepareStatement("update player  set item_bag_web = ? where name = ?");) {

            ps.setString(1, "[]");
            ps.setString(2, player.name);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            Logger.error(" item_bag_web ");
            return false;
        }
    }

    public static int checkQuaThanhVien(Player player) {
        try (Connection con = DBService.gI().getConnectionForQuaThanhVien(); PreparedStatement ps = con.prepareStatement("select nhanqua from player  where name = ?");) {

            ps.setString(1, player.name);
            ResultSet rs = ps.executeQuery();
            int nhanqua = 0;
            while (rs.next()) {
                nhanqua = rs.getInt("nhanqua");
            }
            rs.close();
            ps.close();
            con.close();
            return nhanqua;
        } catch (Exception e) {
            Logger.error(" error check nhanqua");
            return -1;
        }
    }
    
    public static void updateVip(Player player) {
    try (Connection con = DBService.gI().getConnectionForGame();
         PreparedStatement ps = con.prepareStatement(
             "UPDATE player SET vip1 = ? WHERE name = ?")) {

        ps.setInt(1, player.vip1);   // 0,1,2,3
        ps.setString(2, player.name);
        ps.executeUpdate();

    } catch (Exception e) {
        Log.error(PlayerDAO.class, e);
    }
}
    public static void updateAccountVND(long accountId, int vnd) {
    try (Connection con = DBService.gI().getConnectionForSaveData();
         PreparedStatement ps = con.prepareStatement(
             "UPDATE account SET vnd = ? WHERE id = ?")) {

        ps.setInt(1, vnd);
        ps.setLong(2, accountId);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public static boolean updateQuaThanhVien(Player player, int num) {
        try (Connection con = DBService.gI().getConnectionForQuaThanhVien(); PreparedStatement ps = con.prepareStatement("update player  set nhanqua = ? where name = ?");) {

            ps.setInt(1, num);
            ps.setString(2, player.name);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            Logger.error(" nhanqua ");
            return false;
        }
    }
}
