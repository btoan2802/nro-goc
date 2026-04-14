package nro.services;

import nro.attr.Attribute;
import nro.consts.ConstAttribute;
import nro.consts.ConstEvent;
import nro.consts.ConstItem;
import nro.consts.ConstMob;
import nro.event.Event;
import nro.models.item.ItemLuckyRound;
import nro.models.item.ItemOptionLuckyRound;
import nro.models.item.ItemReward;
import nro.models.mob.MobReward;
import nro.models.npc.Npc;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.mob.Mob;
import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.server.ServerNotify;
import nro.server.SettingGame;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import nro.lib.RandomCollection;

import nro.services.func.CombineServiceNew;
import nro.services.func.ShopService;

public class RewardService {

    private Player player;
    int quan = 0;

    // id option set kich hoat (tên set, hiệu ứng set, tỉ lệ, type tỉ lệ)
    private static final int[][][] ACTIVATION_SET = {
            { { 129, 141, 1, 1000 }, { 127, 139, 1, 1000 }, { 128, 140, 1, 1000 } }, // songoku - thien xin hang - kirin
            { { 131, 143, 1, 1000 }, { 132, 144, 1, 1000 }, { 130, 142, 1, 1000 } }, // oc tieu - pikkoro daimao -
            // picolo
            { { 135, 138, 1, 1000 }, { 133, 136, 1, 1000 }, { 134, 137, 1, 1000 } } // kakarot - cadic - nappa
    };

    private static final int[][] ACTIVATION_SET_70PT = {
            { 227, 230, 1, 1000 }, { 229, 232, 1, 1000 }, { 228, 231, 1, 1000 }
    };
    private static RewardService i;

    private RewardService() {

    }

    public static RewardService gI() {
        if (i == null) {
            i = new RewardService();
        }
        return i;
    }

    private MobReward getMobReward(Mob mob) {
        for (MobReward mobReward : Manager.MOB_REWARDS) {
            if (mobReward.tempId == mob.tempId) {
                return mobReward;
            }
        }
        return null;
    }

    public byte generateTypeTrangBi() {
        if (Util.isTrue(30, 100 * (SettingGame.RATIO_GANG_TAY + 1))) {
            byte listType[] = { 2 };
            return Util.randomItem(listType);
        } else {
            byte listType[] = { 0, 1, 3 };
            return Util.randomItem(listType);
        }
    }

    // trả về list item quái die
    public List<ItemMap> getRewardItems(Player player, Mob mob, int x, int yEnd) {
        int mapid = player.zone.map.mapId;
        int mobLevel = mob.level;
        List<ItemMap> list = new ArrayList<>();
        MobReward mobReward = getMobReward(mob);

        if (MapService.gI().isMapthientu(player.zone.map)) {
            if (Util.isTrue(2, 200)) {
                ItemMap itemMap = new ItemMap(mob.zone, 1517, 1, x, yEnd, player.id);
                // itemMap.options.add(new ItemOption(74, 0));
                list.add(itemMap);
            }
            if (Util.isTrue(2, 200)) {
                ItemMap itemMap = new ItemMap(mob.zone, 1518, 1, x, yEnd, player.id);
                // itemMap.options.add(new ItemOption(74, 0));
                list.add(itemMap);
            }
        }
        //====================================VPSK NOEL=======================================================
        boolean hasOption253 = false;

        if (player.inventory != null && player.inventory.itemsBody != null)
            for (Item item : player.inventory.itemsBody) {
                if (item == null || item.itemOptions == null) continue;
                for (ItemOption op : item.itemOptions)
                    if (op != null && op.optionTemplate != null && op.optionTemplate.id == 253) { hasOption253 = true; break; }
                if (hasOption253) break;
            }

        if (player.zone != null && player.zone.map != null && hasOption253) {

            if (player.zone.map.mapId == 199 ) {
                if (Util.isTrue(15, 100)) list.add(new ItemMap(mob.zone, 1167, 1, x, yEnd, player.id));
                if (Util.isTrue(15, 100)) list.add(new ItemMap(mob.zone, 1170, 1, x, yEnd, player.id));
                if (Util.isTrue(10, 100)) list.add(new ItemMap(mob.zone, 1299, 1, x, yEnd, player.id));
            }

            if (MapService.gI().isMapCold(player.zone.map)) {
                if (Util.isTrue(10, 100)) list.add(new ItemMap(mob.zone, 1168, 1, x, yEnd, player.id));
                if (Util.isTrue(10, 100)) list.add(new ItemMap(mob.zone, 1169, 1, x, yEnd, player.id));
            }
        }
        if (mapid >= 1 && mapid <= 199) {
            if (mapid == 7 || mapid == 14) {
            } else if (mapid >= 105 && mapid <= 110) {
                if (Util.isTrue(18, 100)) { 
                    ItemMap itemMap = new ItemMap(mob.zone, 1429, 1, x, yEnd, player.id);
                    list.add(itemMap);
                }
            } else {
                if (Util.isTrue(9, 100)) {
                    ItemMap itemMap = new ItemMap(mob.zone, 1429, 1, x, yEnd, player.id);
                    list.add(itemMap);
                }
            }
        }
        if (MapService.gI().isMaptv(player.zone.map)) {
            if (Util.isTrue(3, 500)) {
                ItemMap itemMap = new ItemMap(mob.zone, 2011, 1, x, yEnd, player.id);
                itemMap.options.add(new ItemOption(30, 0));
                list.add(itemMap);
            }
        }
        final Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(11);
        if (mapid == 0 ||mapid == 7 || mapid == 14) {
            if (TimeUtil.timeBoss(hour)) {
            if(player.cFlag >= 1 && player.cFlag <= 8){
                if (Util.isTrue(20, 100)) { 
                    ItemMap itemMap = new ItemMap(mob.zone, 590, 1, x, yEnd, player.id);
                    list.add(itemMap);
                }
            }
            }
            }
        if (MapService.gI().isMapSKtt(player.zone.map)) {
            if (Util.isTrue(5, 100)) {
                ItemMap itemMap = new ItemMap(mob.zone, 1327, 1, x, yEnd, player.id);
                // Service.getInstance().sendThongBao(player, "Bạn nhận được 1 hồng ngọc");
                list.add(itemMap);
            }
        }
        if (MapService.gI().isMapSKtt(player.zone.map)) {
            if (Util.isTrue(10, 100)) {
                ItemMap itemMap = new ItemMap(mob.zone, 1328, 1, x, yEnd, player.id);
                // Service.getInstance().sendThongBao(player, "Bạn nhận được 1 hồng ngọc");
                list.add(itemMap);
            }
        }
        if (MapService.gI().isMapSKtt(player.zone.map)) {
            if (Util.isTrue(15, 100)) {
                ItemMap itemMap = new ItemMap(mob.zone, 1329, 1, x, yEnd, player.id);
                // Service.getInstance().sendThongBao(player, "Bạn nhận được 1 hồng ngọc");
                list.add(itemMap);
            }
        }
        if (MapService.gI().isMapSKtt(player.zone.map)) {
            if (Util.isTrue(20, 100)) {
                ItemMap itemMap = new ItemMap(mob.zone, 1330, 1, x, yEnd, player.id);
                // Service.getInstance().sendThongBao(player, "Bạn nhận được 1 hồng ngọc");
                list.add(itemMap);
            }
        }

        if (MapService.gI().isMapPk(player.zone.map)) {
            if (Util.isTrue(100, 100)) {
                ItemMap itemMap = new ItemMap(mob.zone, 861, 1, x, yEnd, player.id);
                Service.getInstance().sendThongBao(player, "Bạn nhận được 1 hồng ngọc");
                list.add(itemMap);
            }
        }
        if (mob.effectSkill != null && mob.effectSkill.isSocola) {
            ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, 516, 1,
                    mob.location.x, mob.location.y,
                    player.id);
            list.add(itemMap);
            mob.effectSkill.removeSocola();
        }
        if (player.nPoint.tlRoiEvent_id == 1 && player.zone.map.mapId == 6) {
            if (Util.isTrue(60, 100)) {
                short vpsk[] = { 1496, 610, 1497 };
                ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, vpsk[Util.nextInt(vpsk.length)], 1,
                        mob.location.x, mob.location.y,
                        player.id);
                list.add(itemMap);
            }
        }
        if (Util.isTrue(10, 100 * (SettingGame.RATIO_RAC + 1))) {
            // rác
            short rac[] = SettingGame.ItemRac;
            ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, rac[Util.nextInt(rac.length)], 1,
                    mob.location.x, mob.location.y,
                    player.id);
            list.add(itemMap);
        }
        if (player.nPoint.isDoPhaLe && Util.isTrue(3, 100)) {
            short rac[] = { 441, 442, 443, 444, 445, 446, 447 };

            ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, rac[Util.nextInt(rac.length)], 1,
                    mob.location.x, mob.location.y,
                    player.id);
            list.add(itemMap);
        }
        if (mobLevel >= 2) {// rơi đồ cơ bản
            int MaxRatioRoi = mobLevel * mobLevel * 70;
            if (Util.isTrue(1, MaxRatioRoi)) {
                int levelItem = 0;
                if (mobLevel <= 10) {
                    levelItem = mob.level - 2; // tối đa rada cấp 9
                } else if (mobLevel <= 13) {
                    levelItem = 9; // tối đâ cấp 10
                } else {
                    levelItem = Util.nextInt(10, 11); // tối đâ cấp lưỡng long
                }

                if (levelItem > 11) {
                    levelItem = 11;
                }
                byte tileType = generateTypeTrangBi();
                int idItem = ConstItem.doSKHVip[tileType][player.gender][levelItem];

                ItemMap itemMap = ItemService.gI().RaitiDoSpl_0_3s(mob.zone,
                        idItem, 1, mob.location.x, mob.location.y, player.id, levelItem);
                if (MapService.gI().isMapCold(mapid) && Util.isTrue(5, 100)) {
                    itemMap.options.add(new ItemOption(Util.nextInt(34, 36), 0));
                }
                list.add(itemMap);
            }
        }

        if (MapService.gI().isMapCereal(mapid)) {
            if (player.itemTime != null) {
                if (player.itemTime.isUseGroup_5_1 && Util.isTrue(5, 100)) {
                    // up da phap su
                    short rac[] = { 1308};
                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, rac[Util.nextInt(rac.length)], 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap);
                }
            }
        }

        else if (MapService.gI().isMapTuongLai(mapid)) {
            if (player.itemTime != null) {
                if (player.itemTime.isUseMayDo && Util.isTrue(5, 100)) {
                    // up cskb
                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, 380, 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap);
                }

                if (Util.isTrue(2, 100)) {
                    // up mảnh bông tai
                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) Util.nextInt(933, 934), 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap);
                }
                // d nro.utils.Logger.warning("zone " + mapid);
            }

        } else if (MapService.gI().isMapCold(player.zone.map.mapId)) {
            if (!player.isBoss) {
                if (Util.isTrue(1, 45000 * (SettingGame.RATIO_DTL_COLD + 1))) {
                    ItemMap item = null;
                    // ### rơi đồ thần linh hủy diệt
                    short idItem = 0;
                    byte tileType = generateTypeTrangBi();
                    if (Util.isTrue(30, 100)) {
                        // tỉ lệ rớt đồ thần linh
                        idItem = ConstItem.doSKHVip[tileType][player.gender][12];
                        item = ItemService.gI().RaitiDoSpl(mob.zone,
                                idItem, 1, mob.location.x, mob.location.y, player.id);
                    } else {
                        // tỉ lệ rớt đồ 6 7s
                        idItem = ConstItem.doSKHVip[tileType][player.gender][11];
                        item = ItemService.gI().RaitiDoSpl2(mob.zone,
                                idItem, 1, mob.location.x, mob.location.y, player.id);
                    }

                    ServerNotify.gI()
                            .notify(player.name + " vừa nhặt được " + item.itemTemplate.name + " tại "
                                    + player.zone.map.mapName + " khu " + player.zone.zoneId);

                    list.add(item);
                }

                if (player.setClothes.godClothes == 5 && Util.isTrue(1, 280)) {
                    ItemMap itemMap = new ItemMap(mob.zone, (short) Util.nextInt(663, 667), 1, x, yEnd, player.id);
                    list.add(itemMap);
                }
                // if (Util.isTrue(20, 100)) list.add(new ItemMap(mob.zone, 1168, 1, x, yEnd, player.id));
                // if (Util.isTrue(20, 100)) list.add(new ItemMap(mob.zone, 1169, 1, x, yEnd, player.id));
            
            }
        } else if (MapService.gI().isMapHtnt(mapid)) {

            if (Util.isTrue(1, 200)) {
                // up mvbt3
                ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) Util.nextInt(1601, 1602), 1,
                        mob.location.x, mob.location.y,
                        player.id);
                list.add(itemMap);
            }
            if (Util.isTrue(1, 200)) {
                // up mvbt3
                ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) 1229, 1,
                        mob.location.x, mob.location.y,
                        player.id);
                list.add(itemMap);
            }
        } else if (MapService.gI().isMapHacTinh(mapid)) {
            if (Util.isTrue(1, 100)) {
                // up đá hắc tinh
                ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) 1361, 1,
                        mob.location.x, mob.location.y,
                        player.id);
                list.add(itemMap);
            }

        } else if (MapService.gI().isMapNguHanhSon(mapid)) {
            if (Util.isTrue(5, 100)) {
                switch (mapid) {
                    case 122: {
                        ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) 539, 1,
                                mob.location.x, mob.location.y,
                                player.id);
                        list.add(itemMap);
                        break;
                    }
                    case 123: {
                        ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) 538, 1,
                                mob.location.x, mob.location.y,
                                player.id);
                        list.add(itemMap);
                        break;
                    }
                    case 124: {
                        ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) 537, 1,
                                mob.location.x, mob.location.y,
                                player.id);
                        list.add(itemMap);
                        break;
                    }
                    default:
                        break;
                }
                if (Util.isTrue(1, 100)) {
                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) 540, 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap);
                }
            }
        } else if (MapService.gI().isMapContrung(mapid)) {
            if (player.itemTime != null) {
                if (player.itemTime.isUseGroup_3_1 && Util.isTrue(7, 100)) {
                    // up đá hắc tinh

                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) Util.nextInt(1247, 1249),
                            1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap);
                }
            }

        } else if (MapService.gI().isMapBanDoKhoBau_new(mapid)) {
            if (player.itemTime != null) {
                if (Util.isTrue(1, 100)) {
                    // up đá hắc tinh
                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) Util.nextInt(220, 224),
                            1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap);
                }
            }
        } 
//        else if (mapid == 155) { // hành tinh ngục tù
//            if (player.itemTime != null) {
//                if (player.itemTime.isUseGroup_5_1 && Util.isTrue(5, 100)) {
//                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) Util.nextInt(220, 224),
//                            1,
//                            mob.location.x, mob.location.y,
//                            player.id);
//                    list.add(itemMap);
//                }
//                if (player.itemTime.isUseGroup_5_2 && Util.isTrue(5, 100)) {
//                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) 1610,
//                            1,
//                            mob.location.x, mob.location.y,
//                            player.id);
//                    list.add(itemMap);
//                }
//                if (player.setClothes.huydietClothers == 5 && Util.isTrue(5, 100)) {
//                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) 1609,
//                            1,
//                            mob.location.x, mob.location.y,
//                            player.id);
//                    list.add(itemMap);
//                }
//            }
//        } else if (mapid == 177) {
//
//        } 
        else if (mapid == 179) {
            if (Util.isTrue(5, 100)) {
                ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, (short) Util.nextInt(220, 224), 1,
                        mob.location.x, mob.location.y,
                        player.id);
                list.add(itemMap);
            }
        } else if (MapService.gI().isVegetable(player.zone.map.mapId)) {
            if (!player.isBoss) {// base drop
                if (player.itemTime.isUseGroup_3_2 && Util.isTrue(1, 120)) {
                    // up cskb caaps 2
                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, 1363, 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap);
                }
                if (player.setClothes.huydietClothers == 5 && Util.isTrue(1,300)) {
                    short itemList[] = { 1066, 1067, 1068, 1069, 1070 };
                    ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone, Util.randomItem(itemList), 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap);
                } 
                else if (Util.isTrue(1, 45000 * (SettingGame.RATIO_DTL_COLD + 1))) {
                    ItemMap item = null;
                    // ### rơi đồ thần linh hủy diệt
                    short idItem = 0;
                    item = ItemService.gI().RaitiDoSpl(mob.zone,
                            idItem, 1, mob.location.x, mob.location.y, player.id);

                    ServerNotify.gI()
                            .notify(player.name + " vừa nhặt được " + item.itemTemplate.name + " tại "
                                    + player.zone.map.mapName + " khu " + player.zone.zoneId);

                    list.add(item);
                }
            }
        }

        switch (Manager.EVENT_SEVER) {
            case 4: {// sự kiện tết
                if (Util.isTrue(8, 100)) {
                    int rac[] = { 2030, 2029, 2037, 2038 };

                    ItemMap itemMap1 = ItemService.gI().CreateAllItemMap(mob.zone,
                            rac[Util.nextInt(rac.length)], 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap1);
                }
                if (MapService.gI().isMapFuture(mapid)) {
                    if (Util.isTrue(1, 1000)) {
                        ItemMap itemMap2 = new ItemMap(mob.zone,
                                Util.nextInt(ConstItem.LA_DONG_2023, ConstItem.LA_CHUOI), 1, x, yEnd, player.id);
                        itemMap2.options.add(new ItemOption(74, 0));
                        list.add(itemMap2);
                    }
                } else if (MapService.gI().isMapCold(player.zone.map)) {

                    if (Util.isTrue(1, 5000)) {
                        ItemMap itemMap3 = new ItemMap(mob.zone,
                                Util.nextInt(ConstItem.GIA_VI_TONG_HOP, ConstItem.PHU_GIA_TAO_MAU), 1, x,
                                yEnd, player.id);
                        itemMap3.options.add(new ItemOption(74, 0));
                        list.add(itemMap3);
                    }

                }

            }
                break;
            case 13: {
                int ratio = 10;
                if (player.isPet) {
                    ratio = 20;
                }
                if (Util.isTrue(ratio, 100)) {

                    ItemMap itemMap1 = ItemService.gI().CreateAllItemMap(mob.zone,
                            (short) 610, 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap1);
                }
            }

                break;
            case ConstEvent.SU_KIEN_HE_2024: {

                // if (Util.isTrue(1, 100)) {
                // ItemMap itemMap1 = ItemService.gI().CreateAllItemMap(mob.zone,
                // (short) 610, 1,
                // mob.location.x, mob.location.y,
                // player.id);
                // list.add(itemMap1);
                // }
            }
                break;
            case ConstEvent.SU_KIEN_TRUNG_THU_2024: {
                // 2 hành tinh
                int ratio = 1;
                if (player.nPoint.tlRoiEvent_id == Manager.EVENT_SEVER) {
                    ratio = 2;
                }
                if (player.itemTime != null && player.itemTime.isUseGroup_5_5 && Util.isTrue(1, 100)) {

                    ItemMap itemMap1 = ItemService.gI().CreateAllItemMap(mob.zone,
                            (short) 579, 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap1);
                }
                if (mapid <= 48 && Util.isTrue(5 * ratio, 100)) {

                    ItemMap itemMap1 = ItemService.gI().CreateAllItemMap(mob.zone,
                            (short) 1328, 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap1);
                }
                if (MapService.gI().isMapFide(mapid) && Util.isTrue(5 * ratio, 100)) {

                    ItemMap itemMap1 = ItemService.gI().CreateAllItemMap(mob.zone,
                            (short) 1329, 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap1);
                }
                if (MapService.gI().isMapFuture(mapid) && Util.isTrue(5 * ratio, 100)) {

                    ItemMap itemMap1 = ItemService.gI().CreateAllItemMap(mob.zone,
                            (short) 1330, 1,
                            mob.location.x, mob.location.y,
                            player.id);
                    list.add(itemMap1);
                }
            }
                break;
            case 17:
                // if (player.nPoint.tlRoiEvent_id == Manager.EVENT_SEVER && Util.isTrue(10,
                // 100)) {
                // // rác
                // short rac[] = {2013, 2014};
                // ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone,
                // Util.randomItem(rac), 1,
                // mob.location.x, mob.location.y,
                // player.id);
                // list.add(itemMap);
                // }

                // if (player.nPoint.tlRoiEvent_id == Manager.EVENT_SEVER && Util.isTrue(10,
                // 100)) {
                // short rac[] = { 707, 708 };
                // ItemMap itemMap = ItemService.gI().CreateAllItemMap(mob.zone,
                // Util.randomItem(rac), 1,
                // mob.location.x, mob.location.y,
                // player.id);
                // list.add(itemMap);
                // }
                break;
        }

        if (mobReward != null) {
            int itemSize = mobReward.itemRewards.size();
            int goldSize = mobReward.goldRewards.size();
            int cskbSize = mobReward.capsuleKyBi.size();
            int foodSize = mobReward.foods.size();
            int biKiepSize = mobReward.biKieps.size();
            if (itemSize > 0) {
                ItemReward ir = mobReward.itemRewards.get(Util.nextInt(0, itemSize - 1));
                boolean inMap = false;
                if (ir.mapId[0] == -1) {
                    inMap = true;
                } else {
                    for (int i = 0; i < ir.mapId.length; i++) {
                        if (mob.zone.map.mapId == ir.mapId[i]) {
                            inMap = true;
                            break;
                        }
                    }
                }

                if (mob.tempId == ConstMob.HIRUDEGARN) {
                    if (quan < 1 && Util.isTrue(50, 100)) {
                        // RandomCollection<Integer> rd = new RandomCollection<>();
                        // rd.add(1, 1066);
                        // rd.add(40, 861);
                        // rd.add(30, 15);
                        // rd.add(20, 16);
                        // rd.add(9, 2022);
                        // for (int i = 0; i < 1; i++) {
                        // int itemID = rd.next();
                        quan = 1;
                        ItemMap itemMap = new ItemMap(mob.zone, 1477, 1, x + Util.nextInt(-50, 50),
                                yEnd, player.id);
                        list.add(itemMap);
                        // }
                    }
                    for (int i = 0; i < 10; i++) {
                        ItemReward gr = mobReward.goldRewards.get(Util.nextInt(0, goldSize - 1));
                        if (Util.isTrue(gr.ratio, gr.typeRatio)) {
                            ItemMap itemMap = new ItemMap(mob.zone, gr.tempId, 1, x + Util.nextInt(-50,
                                    50), yEnd,
                                    player.id);
                            initQuantityGold(itemMap);
                            list.add(itemMap);
                        }
                    }
                }
                // skh 3 map dau
//                if (player.isPl()) {
//                    int mapId = player.zone.map.mapId;
//                    if (!player.isBoss && (mapId == 1 || mapId == 2 || mapId == 3 || mapId == 16 || mapId == 17
//                            || mapId == 18 || mapId == 8 || mapId == 9 || mapId == 11)) {
//                        if (Util.isTrue(5, 100)) {
//
//                            int[][] itemKH = { { 0, 6, 21, 27, 12 }, { 1, 7, 22, 28, 12 }, { 2, 8, 23, 29, 12 } }; // td,
//                            int skhId = ItemService.gI().randomSKHId(player.gender);
//                            ItemMap it = ItemService.gI().itemMapSKH(mob.zone,
//                                    itemKH[player.gender][Util.nextInt(0, 4)], 1, mob.location.x, mob.location.y,
//                                    player.id, skhId);
//                            list.add(it);
//                        }
//                    }
//
//                }

                // roi hoa hong
                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_20_11 || Manager.EVENT_SEVER == ConstEvent.SU_KIEN_8_3) {
                    if (Util.isTrue(1, 200)) {
                        try {
                            ItemMap itemMap = new ItemMap(mob.zone, 589, 1, x, yEnd, player.id);
                            long e = TimeUtil.getTime("30-11-2022", "dd-MM-yyyy");
                            if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_8_3) {
                                e = TimeUtil.getTime("1-4-2023", "dd-MM-yyyy");
                            }
                            itemMap.options.add(new ItemOption(196, (int) (e / 1000)));
                            list.add(itemMap);
                        } catch (Exception e) {
                            Logger.getLogger(RewardService.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                }
                // sk ngu hanh son

                if (Event.isEvent()) {
                    Event.getInstance().dropItem(player, mob, list, x, yEnd);
                }
                if (mapid == 153) {// map bang
                    if (player.clan != null && player.zone != null) {
                        int numMenber = player.zone.getPlayersSameClan(player.clan.id).size();
                        if (numMenber >= 2) {
                            if (Util.isTrue(100, 100)) {
                                player.clanMember.memberPoint++;
                                Service.getInstance().sendThongBao(player, "Bạn nhận được capsule bang hội");
                            }
                        }
                    }
                }
            }
        }
        return list;

    }

    private void initQuantityGold(ItemMap item) {
        switch (item.itemTemplate.id) {
            case 76:
                item.quantity = Util.nextInt(1000, 5000);
                break;
            case 188:
                item.quantity = Util.nextInt(5000, 10000);
                break;
            case 189:
                item.quantity = Util.nextInt(10000, 20000);
                break;
            case 190:
                item.quantity = Util.nextInt(20000, 30000);
                break;
        }
        Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.VANG);
        if (at != null && !at.isExpired()) {
            item.quantity += item.quantity * at.getValue() / 100;
        }
    }

    public void NhanDeTu(Player player) {
        if (player.pet == null) {
            PetService.gI().createNormalPet(player, Util.nextInt(0, 2), null);
        }
    }

    public void RewardBoss(ItemMap item) {
        initBaseOptionClothesMap(item);
        int star = 0;
        int ratio = Util.nextInt(0, 100);

        if (ratio < 55) { // 50% tỉ lệ ra đến 3 sao
            star = Util.nextInt(1, 3);
        } else if (ratio < 65) {// 30% tỉ lệ 4 đến 5 sao
            star = Util.nextInt(3, 4);
        }
        // else if (ratio < 95) { // 10% tỉ lệ 6 sao
        // star = Util.nextInt(0, 3);
        // } else if (ratio >= 98 && ratio <= 100) {
        // star = 6;
        // }
        if (star > 0) {
            item.options.add(new ItemOption(107, star));
        }
        // if (!item.isDTL() && !item.isDHD()) {
        // item.options.add(new ItemOption(236, 1));
        // }
    }

    public void initBaseOptionClothesMap(ItemMap item) {
        SetClothes(item.itemTemplate.id, item.itemTemplate.type, item.options);
        if (item.itemTemplate.id >= 555 && item.itemTemplate.id <= 567) {
//            item.options.add(new ItemOption(86, 1));
            item.options.add(new ItemOption(254, 1));
        }
    }

    public void initBaseOptionClothes(Item item) {
        SetClothes(item.template.id, item.template.type, item.itemOptions);
    }

    // chỉ số cơ bản: hp, ki, hồi phục, sđ, crit
    private static void SetClothes(int tempId, int type, List<ItemOption> list) {
        int[][] option_param = { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 } };
        switch (type) {
            case 0: // áo
                option_param[0][0] = 47; // giáp
                switch (tempId) {
                    case 0:
                        option_param[0][1] = 2;
                        break;
                    case 33:
                        option_param[0][1] = 4;
                        break;
                    case 3:
                        option_param[0][1] = 8;
                        break;
                    case 34:
                        option_param[0][1] = 16;
                        break;
                    case 136:
                        option_param[0][1] = 24;
                        break;
                    case 137:
                        option_param[0][1] = 40;
                        break;
                    case 138:
                        option_param[0][1] = 60;
                        break;
                    case 139:
                        option_param[0][1] = 90;
                        break;
                    case 230:
                        option_param[0][1] = 200;
                        break;
                    case 231:
                        option_param[0][1] = 250;
                        break;
                    case 232:
                        option_param[0][1] = 300;
                        break;
                    case 233:
                        option_param[0][1] = 400;
                        break;
                    case 1:
                        option_param[0][1] = 2;
                        break;
                    case 41:
                        option_param[0][1] = 4;
                        break;
                    case 4:
                        option_param[0][1] = 8;
                        break;
                    case 42:
                        option_param[0][1] = 16;
                        break;
                    case 152:
                        option_param[0][1] = 24;
                        break;
                    case 153:
                        option_param[0][1] = 40;
                        break;
                    case 154:
                        option_param[0][1] = 60;
                        break;
                    case 155:
                        option_param[0][1] = 90;
                        break;
                    case 234:
                        option_param[0][1] = 200;
                        break;
                    case 235:
                        option_param[0][1] = 250;
                        break;
                    case 236:
                        option_param[0][1] = 300;
                        break;
                    case 237:
                        option_param[0][1] = 400;
                        break;
                    case 2:
                        option_param[0][1] = 3;
                        break;
                    case 49:
                        option_param[0][1] = 5;
                        break;
                    case 5:
                        option_param[0][1] = 10;
                        break;
                    case 50:
                        option_param[0][1] = 20;
                        break;
                    case 168:
                        option_param[0][1] = 30;
                        break;
                    case 169:
                        option_param[0][1] = 50;
                        break;
                    case 170:
                        option_param[0][1] = 70;
                        break;
                    case 171:
                        option_param[0][1] = 100;
                        break;
                    case 238:
                        option_param[0][1] = 230;
                        break;
                    case 239:
                        option_param[0][1] = 280;
                        break;
                    case 240:
                        option_param[0][1] = 330;
                        break;
                    case 241:
                        option_param[0][1] = 450;
                        break;
                    case 555: // áo thần trái đất
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 700;
                        option_param[2][1] = 15;
                        break;
                    case 557: // áo thần namếc
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 600;
                        option_param[2][1] = 15;
                        break;
                    case 559: // áo thần xayda
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 800;
                        option_param[2][1] = 15;
                        break;
                    case 650: // áo huỷ diệt trái đất
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // Không thể giao dịch

                        option_param[0][1] = 1100;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                    case 652: // áo huỷ diệt namếc
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // Không thể giao dịch

                        option_param[0][1] = 1000;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                    case 654: // áo huỷ diệt xayda
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // Không thể giao dịch

                        option_param[0][1] = 1300;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                }
                break;
            case 1: // quần
                option_param[0][0] = 6; // hp
                option_param[1][0] = 27; // hp hồi/30s
                switch (tempId) {
                    case 6:
                        option_param[0][1] = 30;
                        break;
                    case 35:
                        option_param[0][1] = 150;
                        option_param[1][1] = 12;
                        break;
                    case 9:
                        option_param[0][1] = 300;
                        option_param[1][1] = 40;
                        break;
                    case 36:
                        option_param[0][1] = 600;
                        option_param[1][1] = 120;
                        break;
                    case 140:
                        option_param[0][1] = 1400;
                        option_param[1][1] = 280;
                        break;
                    case 141:
                        option_param[0][1] = 3000;
                        option_param[1][1] = 600;
                        break;
                    case 142:
                        option_param[0][1] = 6000;
                        option_param[1][1] = 1200;
                        break;
                    case 143:
                        option_param[0][1] = 10000;
                        option_param[1][1] = 2000;
                        break;
                    case 242:
                        option_param[0][1] = 14000;
                        option_param[1][1] = 2500;
                        break;
                    case 243:
                        option_param[0][1] = 18000;
                        option_param[1][1] = 3000;
                        break;
                    case 244:
                        option_param[0][1] = 22000;
                        option_param[1][1] = 3500;
                        break;
                    case 245:
                        option_param[0][1] = 26000;
                        option_param[1][1] = 4000;
                        break;
                    case 7:
                        option_param[0][1] = 20;
                        break;
                    case 43:
                        option_param[0][1] = 25;
                        option_param[1][1] = 10;
                        break;
                    case 10:
                        option_param[0][1] = 120;
                        option_param[1][1] = 28;
                        break;
                    case 44:
                        option_param[0][1] = 250;
                        option_param[1][1] = 100;
                        break;
                    case 156:
                        option_param[0][1] = 600;
                        option_param[1][1] = 240;
                        break;
                    case 157:
                        option_param[0][1] = 1200;
                        option_param[1][1] = 480;
                        break;
                    case 158:
                        option_param[0][1] = 2400;
                        option_param[1][1] = 960;
                        break;
                    case 159:
                        option_param[0][1] = 4800;
                        option_param[1][1] = 1800;
                        break;
                    case 246:
                        option_param[0][1] = 13000;
                        option_param[1][1] = 2200;
                        break;
                    case 247:
                        option_param[0][1] = 17000;
                        option_param[1][1] = 2700;
                        break;
                    case 248:
                        option_param[0][1] = 21000;
                        option_param[1][1] = 3200;
                        break;
                    case 249:
                        option_param[0][1] = 25000;
                        option_param[1][1] = 3700;
                        break;
                    case 8:
                        option_param[0][1] = 20;
                        break;
                    case 51:
                        option_param[0][1] = 20;
                        option_param[1][1] = 8;
                        break;
                    case 11:
                        option_param[0][1] = 100;
                        option_param[1][1] = 20;
                        break;
                    case 52:
                        option_param[0][1] = 200;
                        option_param[1][1] = 80;
                        break;
                    case 172:
                        option_param[0][1] = 500;
                        option_param[1][1] = 200;
                        break;
                    case 173:
                        option_param[0][1] = 1000;
                        option_param[1][1] = 400;
                        break;
                    case 174:
                        option_param[0][1] = 2000;
                        option_param[1][1] = 800;
                        break;
                    case 175:
                        option_param[0][1] = 4000;
                        option_param[1][1] = 1600;
                        break;
                    case 250:
                        option_param[0][1] = 12000;
                        option_param[1][1] = 2100;
                        break;
                    case 251:
                        option_param[0][1] = 16000;
                        option_param[1][1] = 2600;
                        break;
                    case 252:
                        option_param[0][1] = 20000;
                        option_param[1][1] = 3100;
                        break;
                    case 253:
                        option_param[0][1] = 24000;
                        option_param[1][1] = 3600;
                        break;
                    case 556: // quần thần trái đất
                        option_param[0][0] = 22; // hp
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 65;
                        option_param[1][1] = 10000;
                        option_param[2][1] = 15;
                        break;
                    case 558: // quần thần namếc
                        option_param[0][0] = 22; // hp
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 55;
                        option_param[1][1] = 10000;
                        option_param[2][1] = 15;
                        break;
                    case 560: // quần thần xayda
                        option_param[0][0] = 22; // hp
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 60;
                        option_param[1][1] = 10000;
                        option_param[2][1] = 15;
                        break;
                    case 651: // quần huỷ diệt trái đất
                        option_param[0][0] = 22; // hp
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // không thể gd

                        option_param[0][1] = 100;
                        option_param[1][1] = 25000;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                    case 653: // quần huỷ diệt namếc
                        option_param[0][0] = 22; // hp
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // không thể gd

                        option_param[0][1] = 90;
                        option_param[1][1] = 22000;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                    case 655: // quần huỷ diệt xayda
                        option_param[0][0] = 22; // hp
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // không thể gd

                        option_param[0][1] = 80;
                        option_param[1][1] = 24000;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                }
                break;
            case 2: // găng
                option_param[0][0] = 0; // sđ
                switch (tempId) {
                    case 21:
                        option_param[0][1] = 4;
                        break;
                    case 24:
                        option_param[0][1] = 7;
                        break;
                    case 37:
                        option_param[0][1] = 14;
                        break;
                    case 38:
                        option_param[0][1] = 28;
                        break;
                    case 144:
                        option_param[0][1] = 55;
                        break;
                    case 145:
                        option_param[0][1] = 110;
                        break;
                    case 146:
                        option_param[0][1] = 220;
                        break;
                    case 147:
                        option_param[0][1] = 530;
                        break;
                    case 254:
                        option_param[0][1] = 680;
                        break;
                    case 255:
                        option_param[0][1] = 1000;
                        break;
                    case 256:
                        option_param[0][1] = 1500;
                        break;
                    case 257:
                        option_param[0][1] = 2200;
                        break;
                    case 22:
                        option_param[0][1] = 3;
                        break;
                    case 46:
                        option_param[0][1] = 6;
                        break;
                    case 25:
                        option_param[0][1] = 12;
                        break;
                    case 45:
                        option_param[0][1] = 24;
                        break;
                    case 160:
                        option_param[0][1] = 50;
                        break;
                    case 161:
                        option_param[0][1] = 100;
                        break;
                    case 162:
                        option_param[0][1] = 200;
                        break;
                    case 163:
                        option_param[0][1] = 500;
                        break;
                    case 258:
                        option_param[0][1] = 630;
                        break;
                    case 259:
                        option_param[0][1] = 950;
                        break;
                    case 260:
                        option_param[0][1] = 1450;
                        break;
                    case 261:
                        option_param[0][1] = 2150;
                        break;
                    case 23:
                        option_param[0][1] = 5;
                        break;
                    case 53:
                        option_param[0][1] = 8;
                        break;
                    case 26:
                        option_param[0][1] = 16;
                        break;
                    case 54:
                        option_param[0][1] = 32;
                        break;
                    case 176:
                        option_param[0][1] = 60;
                        break;
                    case 177:
                        option_param[0][1] = 120;
                        break;
                    case 178:
                        option_param[0][1] = 240;
                        break;
                    case 179:
                        option_param[0][1] = 560;
                        break;
                    case 262:
                        option_param[0][1] = 700;
                        break;
                    case 263:
                        option_param[0][1] = 1050;
                        break;
                    case 264:
                        option_param[0][1] = 1550;
                        break;
                    case 265:
                        option_param[0][1] = 2250;
                        break;
                    case 562: // găng thần trái đất
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 3200;
                        option_param[2][1] = 17;
                        break;
                    case 564: // găng thần namếc
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 3000;
                        option_param[2][1] = 17;
                        break;
                    case 566: // găng thần xayda
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 3500;
                        option_param[2][1] = 17;
                        break;
                    case 657: // găng huỷ diệt trái đất
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // không thể gd

                        option_param[0][1] = 7000;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                    case 659: // găng huỷ diệt namếc
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // không thể gd

                        option_param[0][1] = 6500;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                    case 661: // găng huỷ diệt xayda
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // không thể gd

                        option_param[0][1] = 7500;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                }
                break;
            case 3: // giày
                option_param[0][0] = 7; // ki
                option_param[1][0] = 28; // ki hồi /30s
                switch (tempId) {
                    case 27:
                        option_param[0][1] = 10;
                        break;
                    case 30:
                        option_param[0][1] = 25;
                        option_param[1][1] = 5;
                        break;
                    case 39:
                        option_param[0][1] = 120;
                        option_param[1][1] = 24;
                        break;
                    case 40:
                        option_param[0][1] = 250;
                        option_param[1][1] = 50;
                        break;
                    case 148:
                        option_param[0][1] = 500;
                        option_param[1][1] = 100;
                        break;
                    case 149:
                        option_param[0][1] = 1200;
                        option_param[1][1] = 240;
                        break;
                    case 150:
                        option_param[0][1] = 2400;
                        option_param[1][1] = 480;
                        break;
                    case 151:
                        option_param[0][1] = 5000;
                        option_param[1][1] = 1000;
                        break;
                    case 266:
                        option_param[0][1] = 9000;
                        option_param[1][1] = 1500;
                        break;
                    case 267:
                        option_param[0][1] = 14000;
                        option_param[1][1] = 2000;
                        break;
                    case 268:
                        option_param[0][1] = 19000;
                        option_param[1][1] = 2500;
                        break;
                    case 269:
                        option_param[0][1] = 24000;
                        option_param[1][1] = 3000;
                        break;
                    case 28:
                        option_param[0][1] = 15;
                        break;
                    case 47:
                        option_param[0][1] = 30;
                        option_param[1][1] = 6;
                        break;
                    case 31:
                        option_param[0][1] = 150;
                        option_param[1][1] = 30;
                        break;
                    case 48:
                        option_param[0][1] = 300;
                        option_param[1][1] = 60;
                        break;
                    case 164:
                        option_param[0][1] = 600;
                        option_param[1][1] = 120;
                        break;
                    case 165:
                        option_param[0][1] = 1500;
                        option_param[1][1] = 300;
                        break;
                    case 166:
                        option_param[0][1] = 3000;
                        option_param[1][1] = 600;
                        break;
                    case 167:
                        option_param[0][1] = 6000;
                        option_param[1][1] = 1200;
                        break;
                    case 270:
                        option_param[0][1] = 10000;
                        option_param[1][1] = 1700;
                        break;
                    case 271:
                        option_param[0][1] = 15000;
                        option_param[1][1] = 2200;
                        break;
                    case 272:
                        option_param[0][1] = 20000;
                        option_param[1][1] = 2700;
                        break;
                    case 273:
                        option_param[0][1] = 25000;
                        option_param[1][1] = 3200;
                        break;
                    case 29:
                        option_param[0][1] = 10;
                        break;
                    case 55:
                        option_param[0][1] = 20;
                        option_param[1][1] = 4;
                        break;
                    case 32:
                        option_param[0][1] = 100;
                        option_param[1][1] = 20;
                        break;
                    case 56:
                        option_param[0][1] = 200;
                        option_param[1][1] = 40;
                        break;
                    case 180:
                        option_param[0][1] = 400;
                        option_param[1][1] = 80;
                        break;
                    case 181:
                        option_param[0][1] = 1000;
                        option_param[1][1] = 200;
                        break;
                    case 182:
                        option_param[0][1] = 2000;
                        option_param[1][1] = 400;
                        break;
                    case 183:
                        option_param[0][1] = 4000;
                        option_param[1][1] = 800;
                        break;
                    case 274:
                        option_param[0][1] = 8000;
                        option_param[1][1] = 1300;
                        break;
                    case 275:
                        option_param[0][1] = 13000;
                        option_param[1][1] = 1800;
                        break;
                    case 276:
                        option_param[0][1] = 18000;
                        option_param[1][1] = 2300;
                        break;
                    case 277:
                        option_param[0][1] = 23000;
                        option_param[1][1] = 2800;
                        break;
                    case 563: // giày thần trái đất
                        option_param[0][0] = 23;
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 40;
                        option_param[1][1] = 10000;
                        option_param[2][1] = 14;
                        break;
                    case 565: // giày thần namếc
                        option_param[0][0] = 23;
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 60;
                        option_param[1][1] = 10000;
                        option_param[2][1] = 14;
                        break;
                    case 567: // giày thần xayda
                        option_param[0][0] = 23;
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 50;
                        option_param[1][1] = 10000;
                        option_param[2][1] = 14;
                        break;
                    case 658: // giày huỷ diệt trái đất
                        option_param[0][0] = 23;
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // không thể gd

                        option_param[0][1] = 80;
                        option_param[1][1] = 22000;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                    case 660: // giày huỷ diệt namếc
                        option_param[0][0] = 23;
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // không thể gd

                        option_param[0][1] = 90;
                        option_param[1][1] = 25000;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                    case 662: // giày huỷ diệt xayda
                        option_param[0][0] = 23;
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // không thể gd

                        option_param[0][1] = 85;
                        option_param[1][1] = 20000;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1;
                        break;
                }
                break;
            case 4: // rada
                option_param[0][0] = 14; // crit
                switch (tempId) {
                    case 12:
                        option_param[0][1] = 1;
                        break;
                    case 57:
                        option_param[0][1] = 2;
                        break;
                    case 58:
                        option_param[0][1] = 3;
                        break;
                    case 59:
                        option_param[0][1] = 4;
                        break;
                    case 184:
                        option_param[0][1] = 5;
                        break;
                    case 185:
                        option_param[0][1] = 6;
                        break;
                    case 186:
                        option_param[0][1] = 7;
                        break;
                    case 187:
                        option_param[0][1] = 8;
                        break;
                    case 278:
                        option_param[0][1] = 9;
                        break;
                    case 279:
                        option_param[0][1] = 10;
                        break;
                    case 280:
                        option_param[0][1] = 11;
                        break;
                    case 281:
                        option_param[0][1] = 12;
                        break;
                    case 561: // nhẫn thần linh
                        option_param[2][0] = 21; // yêu cầu sức mạnh

                        option_param[0][1] = 16;
                        option_param[2][1] = 18;

                        break;
                    case 656: // nhẫn huỷ diệt
                        option_param[2][0] = 21; // yêu cầu sức mạnh
                        option_param[3][0] = 30; // không thể gd

                        option_param[0][1] = 18;
                        option_param[2][1] = 45;
                        option_param[3][1] = 1; // không thể gd
                        break;
                }
                break;
        }

        for (int i = 0; i < option_param.length; i++) {
            if (option_param[i][0] != -1 && option_param[i][1] != -1) {
                if (option_param[i][0] == 21) {
                    list.add(new ItemOption(option_param[i][0],
                            (option_param[i][1])));
                } else {

                    list.add(new ItemOption(option_param[i][0],
                            (option_param[i][1] + Util.nextInt(-(option_param[i][1] * 10 / 100),
                                    option_param[i][1] * 10 / 100))));
                }

            }
        }

    }

    private void initBaseOptionSaoPhaLe(ItemMap item) {
        int optionId = -1;
        switch (item.itemTemplate.id) {
            case 441: // hút máu
                optionId = 95;
                break;
            case 442: // hút ki
                optionId = 96;
                break;
            case 443: // phản sát thương
                optionId = 97;
                break;
            case 444:
                break;
            case 445:
                break;
            case 446: // vàng
                optionId = 100;
                break;
            case 447: // tnsm
                optionId = 101;
                break;
        }
        item.options.add(new ItemOption(optionId, 5));
    }

    public void initBaseOptionSaoPhaLe(Item item) {
        int optionId = -1;
        int param = 5;
        switch (item.template.id) {
            case 441: // hút máu
                optionId = 95;
                break;
            case 442: // hút ki
                optionId = 96;
                break;
            case 443: // phản sát thương
                optionId = 97;
                break;
            case 444:
                param = 3;
                optionId = 98;
                break;
            case 445:
                param = 3;
                optionId = 99;
                break;
            case 446: // vàng
                optionId = 100;
                break;
            case 447: // tnsm
                optionId = 101;
                break;
        }
        if (optionId != -1) {
            item.itemOptions.add(new ItemOption(optionId, param));
        }
    }

    // sao pha lê
    public void initStarOption(ItemMap item, RatioStar[] ratioStars) {
        RatioStar ratioStar = ratioStars[Util.nextInt(0, ratioStars.length - 1)];
        if (Util.isTrue(ratioStar.ratio, ratioStar.typeRatio)) {
            item.options.add(new ItemOption(107, ratioStar.numStar));
        }
    }

    public void initStarOption(Item item, RatioStar[] ratioStars) {
        RatioStar ratioStar = ratioStars[Util.nextInt(0, ratioStars.length - 1)];
        if (Util.isTrue(ratioStar.ratio, ratioStar.typeRatio)) {
            item.itemOptions.add(new ItemOption(107, ratioStar.numStar));
        }
    }

    // vật phẩm sự kiện
//    private void initEventOption(ItemMap item) {
//        switch (item.itemTemplate.id) {
//            case 2013:
//                item.options.add(new ItemOption(74, 0));
//                break;
//            case 2014:
//                item.options.add(new ItemOption(74, 0));
//                break;
//            case 2015:
//                item.options.add(new ItemOption(74, 0));
//                break;
//        }
//    }

    // hạn sử dụng
    private void initExpiryDateOption(ItemMap item) {

    }

    // vật phẩm không thể giao dịch
    private void initNotTradeOption(ItemMap item) {
        switch (item.itemTemplate.id) {
            case 2009:
                item.options.add(new ItemOption(30, 0));
                break;

        }
    }

    // vật phẩm ký gửi
    private void initDepositOption(ItemMap item) {

    }
    // set kích hoạt 70pt

    public void initActivationOption70Pt(int gender, int type, List<ItemOption> list) {
        if (type <= 4) {
            int[] idOption = ACTIVATION_SET_70PT[gender];
            list.add(new ItemOption(idOption[0], 1)); // tên set
            list.add(new ItemOption(idOption[1], 1)); // hiệu ứng set
            list.add(new ItemOption(30, 7)); // không thể giao dịch
        }
    }

    // set kích hoạt
    public void initActivationOption(int gender, int type, List<ItemOption> list) {
        if (type <= 4) {
            int[] idOption = ACTIVATION_SET[gender][Util.nextInt(0, 2)];
            list.add(new ItemOption(idOption[0], 1)); // tên set
            list.add(new ItemOption(idOption[1], 1)); // hiệu ứng set
            list.add(new ItemOption(30, 7)); // không thể giao dịch
        }
    }

    public void initActivationOptionRandomFist(int gender, int type, List<ItemOption> list, int idsetrandom) {
        if (type <= 4) {
            int[] idOption = ACTIVATION_SET[gender][idsetrandom];
            list.add(new ItemOption(idOption[0], 1)); // tên set
            list.add(new ItemOption(idOption[1], 1)); // hiệu ứng set
            list.add(new ItemOption(30, 7)); // không thể giao dịch
        }
    }

    public boolean isItemCanhaveActivation(ItemMap item) {
        switch (item.itemTemplate.id) {
            case 0:
            case 1:
            case 2:
            case 6:
            case 7:
            case 8:
            case 12:
            case 21:
            case 22:
            case 23:
            case 27:
            case 28:
            case 29:
                return true;
            default:
                return false;
        }
    }

    private byte getMaxStarOfItemReward(ItemMap itemMap) {
        switch (itemMap.itemTemplate.id) {
            case 232:
            case 233:
            case 244:
            case 245:
            case 256:
            case 257:
            case 268:
            case 269:
            case 280:
            case 281:
            case 236:
            case 237:
            case 248:
            case 249:
            case 260:
            case 261:
            case 272:
            case 273:
            case 240:
            case 241:
            case 252:
            case 253:
            case 264:
            case 265:
            case 276:
            case 277:
                // đồ thần
            case 555:
            case 556:
            case 562:
            case 563:
            case 557:
            case 558:
            case 564:
            case 565:
            case 559:
            case 560:
            case 566:
            case 567:
            case 561:
                return 7;
            default:
                return 3;
        }
    }

    // --------------------------------------------------------------------------
    // Item reward lucky round
    public List<Item> getListItemLuckyRound(Player player, int num) {
        List<Item> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ItemLuckyRound item = Manager.LUCKY_ROUND_REWARDS.next();
            if (item != null && (item.temp.gender == player.gender || item.temp.gender > 2)) {
                Item it = ItemService.gI().createNewItem(item.temp.id);
                for (ItemOptionLuckyRound io : item.itemOptions) {
                    int param = 0;
                    if (io.param2 != -1) {
                        param = Util.nextInt(io.param1, io.param2);
                    } else {
                        param = io.param1;
                    }
                    it.itemOptions.add(new ItemOption(io.itemOption.optionTemplate.id, param));
                }
                list.add(it);
            } else {
                Item it = ItemService.gI().createNewItem((short) 189, Util.nextInt(5000, 500000) * 1000);
                list.add(it);
            }
        }
        return list;
    }

    public List<Item> getListItemMayGapThuThuong(Player player, int num) {
        List<Item> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ItemLuckyRound item = Manager.MAY_GAP_THU_THUONG.next();
            if (Util.isTrue(50, 100) && item != null
                    && (item.temp.gender == player.gender || item.temp.gender > 2)) {
                Item it = ItemService.gI().createNewItem(item.temp.id);
                for (ItemOptionLuckyRound io : item.itemOptions) {
                    int param = 0;
                    if (io.param2 != -1) {
                        param = Util.nextInt(io.param1, io.param2);
                    } else {
                        param = io.param1;
                    }
                    it.itemOptions.add(new ItemOption(io.itemOption.optionTemplate.id, param));
                }
                list.add(it);
            } else {
                Item it = ItemService.gI().createNewItem((short) 189, Util.nextInt(5, 50) * 1000);
                list.add(it);
            }
        }
        return list;
    }

    public List<Item> getListItemMayGapThuCaoCap(Player player, int num) {
        List<Item> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ItemLuckyRound item = Manager.MAY_GAP_THU_CAO_CAP.next();
            if (item != null && Util.isTrue(70, 100)) {
                Item it = ItemService.gI().createNewItem(item.temp.id);
                for (ItemOptionLuckyRound io : item.itemOptions) {
                    int param = 0;
                    if (io.param2 != -1) {
                        param = Util.nextInt(io.param1, io.param2);
                    } else {
                        param = io.param1;
                    }
                    it.itemOptions.add(new ItemOption(io.itemOption.optionTemplate.id, param));
                }
                list.add(it);
            } else {
                Item it = ItemService.gI().createNewItem((short) 189, Util.nextInt(5, 50) * 1000);
                list.add(it);
            }
        }
        return list;
    }

    public List<Item> getListItemMayGapThuVip(Player player, int num) {
        List<Item> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ItemLuckyRound item = Manager.MAY_GAP_THU_VIP.next();
            if (item != null && Util.isTrue(90, 100)) {
                Item it = ItemService.gI().createNewItem(item.temp.id);
                for (ItemOptionLuckyRound io : item.itemOptions) {
                    int param = 0;
                    if (io.param2 != -1) {
                        param = Util.nextInt(io.param1, io.param2);
                    } else {
                        param = io.param1;
                    }
                    it.itemOptions.add(new ItemOption(io.itemOption.optionTemplate.id, param));
                }
                list.add(it);
            } else {
                Item it = ItemService.gI().createNewItem((short) 189, Util.nextInt(5, 50) * 1000);
                list.add(it);
            }
        }
        return list;
    }

    public static class RatioStar {

        public byte numStar;
        public int ratio;
        public int typeRatio;

        public RatioStar(byte numStar, int ratio, int typeRatio) {
            this.numStar = numStar;
            this.ratio = ratio;
            this.typeRatio = typeRatio;
        }
    }

    public void rewardFirstTimeLoginPerDay(Player player) {
        if (Util.compareDay(Date.from(Instant.now()), player.firstTimeLogin)) {
            Item item = ItemService.gI().createNewItem((short) 649);
            item.quantity = 1;
            item.itemOptions.add(new ItemOption(74, 0));
            item.itemOptions.add(new ItemOption(30, 0));
            InventoryService.gI().addItemBag(player, item, 0);
            Service.getInstance().sendThongBao(player,
                    "Quà đăng nhập hàng ngày: \nBạn nhận được " + item.template.name + " số lượng : " + item.quantity);
            player.firstTimeLogin = Date.from(Instant.now());
        }
    }

    public void OpenHopThanlinh(Player player, int itemUseiD) {
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            Item itemused = InventoryService.gI().findItemBagByTemp(player, itemUseiD);

            int[][] items = { { 555, 556, 562, 563, 561 }, { 557, 558, 564, 565, 561 }, { 559, 560, 566, 567, 561 } };
            Item aotl = ItemService.gI().createNewItem((short) items[player.gender][0]);
            Item wTl = ItemService.gI().createNewItem((short) items[player.gender][1]);
            Item gTl = ItemService.gI().createNewItem((short) items[player.gender][2]);
            Item jayTl = ItemService.gI().createNewItem((short) items[player.gender][3]);
            Item RdTl = ItemService.gI().createNewItem((short) items[player.gender][4]);

            RewardService.gI().initBaseOptionClothes(aotl);
            RewardService.gI().initBaseOptionClothes(wTl);
            RewardService.gI().initBaseOptionClothes(gTl);
            RewardService.gI().initBaseOptionClothes(jayTl);
            RewardService.gI().initBaseOptionClothes(RdTl);

            aotl.itemOptions.add(new ItemOption(30, 1));
            wTl.itemOptions.add(new ItemOption(30, 1));
            gTl.itemOptions.add(new ItemOption(30, 1));
            jayTl.itemOptions.add(new ItemOption(30, 1));
            RdTl.itemOptions.add(new ItemOption(30, 1));

            InventoryService.gI().addItemBag(player, aotl, 1);
            InventoryService.gI().addItemBag(player, wTl, 1);
            InventoryService.gI().addItemBag(player, gTl, 1);
            InventoryService.gI().addItemBag(player, jayTl, 1);
            InventoryService.gI().addItemBag(player, RdTl, 1);

            InventoryService.gI().subQuantityItemsBag(player, itemused, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được Set thần linh");
        } else {
            Service.getInstance().sendThongBao(player, "Yêu cầu có 5 ô trống hành trang");
        }
    }

    public Item Random_CS_ThanLinh(int itemId, int gender) {
        Item it = ItemService.gI().createNewItem((short) itemId, 1);
        List<Integer> aotl = Arrays.asList(555, 557, 559);
        List<Integer> quantl = Arrays.asList(556, 558, 560);
        List<Integer> gangtl = Arrays.asList(562, 564, 566);
        List<Integer> giaytl = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (aotl.contains(itemId)) {
            it.itemOptions.add(new ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1001) + 1800))); // áo
            // từ
            // 1800-2800
            // giáp
        }
        if (quantl.contains(itemId)) {
            it.itemOptions.add(new ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(16) + 85))); // hp
            // 85-100k
        }
        if (gangtl.contains(itemId)) {
            it.itemOptions.add(new ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(150) + 8500))); // 8500-10000
        }
        if (giaytl.contains(itemId)) {
            it.itemOptions.add(new ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(11) + 80))); // ki
            // 80-90k
        }
        if (ntl == itemId) {
            it.itemOptions.add(new ItemOption(14, new Random().nextInt(3) + 17)); // chí mạng 17-19%
        }
        it.itemOptions.add(new ItemOption(21, 80));// yêu cầu sm 80 tỉ
        it.itemOptions.add(new ItemOption(30, 1));// ko the gd
        return it;
    }

    public void HoptraHoaCuc(Player pl, int ItemuseId) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 1) {
            Item itemUsed = InventoryService.gI().findItemBagByTemp(pl, ItemuseId);
            short icon0 = itemUsed.template.iconID;
            short[] listVpRandom = { 1314, 1315, 1316 };
            Item caiTrangRandom = ItemService.gI().createNewItem(listVpRandom[pl.gender]);
            caiTrangRandom.itemOptions.add(new ItemOption(50, Util.nextInt(30, 40)));
            caiTrangRandom.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
            caiTrangRandom.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
            switch (pl.gender) {
                case 0:
                    caiTrangRandom.itemOptions.add(new ItemOption(213, Util.nextInt(5, 15)));
                    break;
                case 1:
                    caiTrangRandom.itemOptions.add(new ItemOption(214, Util.nextInt(5, 15)));
                    break;
                case 2:
                    caiTrangRandom.itemOptions.add(new ItemOption(215, Util.nextInt(5, 15)));
                    break;
                default:
                    break;
            }
            if (Util.isTrue(80, 100)) {
                caiTrangRandom.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));
            }
            short icon1 = caiTrangRandom.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon0, icon1);
            InventoryService.gI().subQuantityItemsBag(pl, itemUsed, 1);
            InventoryService.gI().addItemBag(pl, caiTrangRandom, 1);
        } else {
            Service.getInstance().sendThongBao(pl, "Ít nhất có 1 chỗ trống trong hành trang");
        }
    }

    public void MayGapThu_Thuong(Player player, int count, Npc npc) {
        if (count < 1 || count > 501 || player == null || npc == null) {
            return;
        }
        int COST_GAP_THU = 500000000;
        Service.getInstance().sendThongBao(player,
                "Tiến hành gắp " + count + " lần");

        for (int i = 0; i < count; i++) {
            nro.utils.Logger.warning("Chay " + i + 1);
            if (player.inventory.gold < COST_GAP_THU) {
                npc.createOtherMenu(player, 12345,
                        "|7|HẾT TIỀN!\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1),
                        "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                break;
            }
            if (1 + player.inventory.itemsBoxCrackBall.size() > 110) {
                npc.createOtherMenu(player, 12345,
                        "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"
                                + "|2|TỔNG LƯỢT GẮP : " + (i + 1) + " LƯỢT"
                                + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                        "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                break;
            }
            player.inventory.gold -= COST_GAP_THU;
            Service.getInstance().sendMoney(player);
            Item gapx10 = ItemService.gI()
                    .createNewItem((short) Util.nextInt(1143, 1154));
            int luot = count - i - 1;
            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                if (Util.isTrue(10, 100)) {
                    InventoryService.gI().addItemBag(player, gapx10, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : "
                                    + luot + " LƯỢT\n"
                                    + "|2|Đã gắp được : "
                                    + gapx10.template.name
                                    + "\n|7|TỔNG ĐIỂM : \nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ");
                } else {
                    Service.getInstance().sendThongBao(player,
                            "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : "
                                    + luot + " LƯỢT\n"
                                    + "|2|Gắp hụt rồi!");
                }
            } else {
                if (Util.isTrue(10, 100)) {
                    player.inventory.itemsBoxCrackBall.add(gapx10);
                    Service.getInstance().sendThongBao(player,
                            "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "
                                    + luot + " LƯỢT\n"
                                    + "|2|Đã gắp được : "
                                    + gapx10.template.name
                                    + "\n|7|TỔNG ĐIỂM : ");
                } else {
                    Service.getInstance().sendThongBao(player,
                            "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "
                                    + luot + " LƯỢT\n"
                                    + "|2|Gắp hụt rồi!"
                                    + "\n|7|TỔNG ĐIỂM : ");
                }
            }
        }
    }

    public void openVongQuayLTN_THUONG(Player player, int count, Npc npc) {
        if (count < 1 || count > 200) {// chống call số âm
            return;
        }
        if (ItemService.gI().getQuantityItemOnBag(player, (short) 457) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn không có đủ Xu");
            return;
        } else {
            try {
                for (int i = 0; i < count; i++) {
                    Service.getInstance().sendThongBao(player,
                            "Gắp thú thường đang gắp lần " + (1 + i));
                    if (InventoryService.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall) > 1
                            || InventoryService.gI().getCountEmptyBag(player) > 1) {// check rương
                        if (ItemService.gI().SubThoiVang(player, (short) 1)) {
                            List<Item> list = RewardService.gI().getListItemMayGapThuThuong(player, 1);
                            if (list.get(0) != null) {
                                Item itemLucky = list.get(0);
                                CombineServiceNew.gI().sendEffectOpenItem(player, itemLucky.template.iconID,
                                        itemLucky.template.iconID);

                                Service.getInstance().sendMoney(player);
                                Service.getInstance().sendThongBao(player,
                                        "Bạn vừa nhận được: " + list.get(0).template.name);
                                player.GapthuPoint += 1;
                                if (InventoryService.gI().getCountEmptyBag(player) > 0) { // có rương
                                    InventoryService.gI().addItemBag(player, itemLucky, 1);
                                } else {// nếu kh troogns rương, add rương phụ
                                    for (Item item : list) {
                                        InventoryService.gI()
                                                .addItemNotUpToUpQuantity(player.inventory.itemsBoxCrackBall, item);
                                    }
                                }

                                Service.getInstance().sendMoney(player);

                                InventoryService.gI().sendItemBags(player);
                            }

                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn không có đủ Xu");

                            break;
                        }

                    } else {
                        Service.getInstance().sendThongBao(player, "Rương phụ và hành trang đã đầy");
                        break;
                    }
                    ShopService.gI().openBoxItemLuckyRound(player);
                    Thread.sleep(300);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void openVongQuayLTN_CAO_CAP(Player player, int count, Npc npc) {
        if (count < 1 || count > 200) {// chống call số âm
            return;
        }
        if (ItemService.gI().getQuantityItemOnBag(player, (short) 457) < 2) {
            Service.getInstance().sendThongBao(player, "Bạn không có đủ xu");
            return;
        } else {
            try {
                for (int i = 0; i < count; i++) {
                    Service.getInstance().sendThongBao(player,
                            "Gắp thú thường đang gắp lần " + (1 + i));
                    if (InventoryService.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall) > 1
                            || InventoryService.gI().getCountEmptyBag(player) > 1) {// check rương
                        if (ItemService.gI().SubThoiVang(player, (short) 2)) {
                            List<Item> list = RewardService.gI().getListItemMayGapThuCaoCap(player, 1);
                            if (list.get(0) != null) {
                                Item itemLucky = list.get(0);
                                CombineServiceNew.gI().sendEffectOpenItem(player, itemLucky.template.iconID,
                                        itemLucky.template.iconID);

                                Service.getInstance().sendMoney(player);
                                Service.getInstance().sendThongBao(player,
                                        "Bạn vừa nhận được: " + list.get(0).template.name);
                                player.GapthuPoint += 2;
                                if (InventoryService.gI().getCountEmptyBag(player) > 0) { // có rương
                                    InventoryService.gI().addItemBag(player, itemLucky, 1);
                                } else {// nếu kh troogns rương, add rương phụ
                                    for (Item item : list) {
                                        InventoryService.gI()
                                                .addItemNotUpToUpQuantity(player.inventory.itemsBoxCrackBall, item);
                                    }
                                }

                                Service.getInstance().sendMoney(player);

                                InventoryService.gI().sendItemBags(player);
                            }

                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn không có đủ xu");

                            break;
                        }

                    } else {
                        Service.getInstance().sendThongBao(player, "Rương phụ và hành trang đã đầy");
                        break;
                    }
                    ShopService.gI().openBoxItemLuckyRound(player);
                    Thread.sleep(300);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void openVongQuayLTN_VIP(Player player, int count, Npc npc) {
        if (count < 1 || count > 200) {// chống call số âm
            return;
        }
        if (ItemService.gI().getQuantityItemOnBag(player, (short) 457) < 5) {
            Service.getInstance().sendThongBao(player, "Bạn không có đủ xu");
            return;
        } else {
            try {
                for (int i = 0; i < count; i++) {
                    Service.getInstance().sendThongBao(player,
                            "Gắp thú thường đang gắp lần " + (1 + i));
                    if (InventoryService.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall) > 1
                            || InventoryService.gI().getCountEmptyBag(player) > 1) {// check rương
                        if (ItemService.gI().SubThoiVang(player, (short) 5)) {
                            List<Item> list = RewardService.gI().getListItemMayGapThuVip(player, 1);
                            player.GapthuPoint += 5;
                            if (list.get(0) != null) {
                                Item itemLucky = list.get(0);
                                CombineServiceNew.gI().sendEffectOpenItem(player, itemLucky.template.iconID,
                                        itemLucky.template.iconID);

                                Service.getInstance().sendMoney(player);
                                Service.getInstance().sendThongBao(player,
                                        "Bạn vừa nhận được: " + list.get(0).template.name);

                                if (InventoryService.gI().getCountEmptyBag(player) > 0) { // có rương
                                    InventoryService.gI().addItemBag(player, itemLucky, 1);
                                } else {// nếu kh troogns rương, add rương phụ
                                    for (Item item : list) {
                                        InventoryService.gI()
                                                .addItemNotUpToUpQuantity(player.inventory.itemsBoxCrackBall, item);
                                    }
                                }

                                Service.getInstance().sendMoney(player);

                                InventoryService.gI().sendItemBags(player);
                            }

                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn không có đủ xu");

                            break;
                        }

                    } else {
                        Service.getInstance().sendThongBao(player, "Rương phụ và hành trang đã đầy");
                        break;
                    }
                    ShopService.gI().openBoxItemLuckyRound(player);
                    Thread.sleep(300);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void baseRewards(Player player, Mob mob) {// Rác
        if (player != null) {
            ItemMap itemMap = null;
            int count = Util.nextInt(30, 40);
            for (int i = 0; i < count; i++) {

                int x = player.location.x + (14 * i);
                if (x < 0 || x > player.zone.map.mapWidth) {
                    return;
                }

                int y = player.zone.map.yPhysicInTop(player.location.x, player.location.y);
                itemMap = ItemService.gI().BaseRewar(player.zone, player, x, y, (byte) 4);
                if (itemMap != null) {
                    Service.getInstance().dropItemMap(mob.zone, itemMap);
                }
            }

        }
    }

    public void DropItemSaoBojack(ItemMap itMap) {
        RewardService.gI().initBaseOptionClothesMap(itMap);
        RewardService.gI().initStarOption(itMap, new RewardService.RatioStar[] {
                new RatioStar((byte) 1, 1, 2),
                new RatioStar((byte) 2, 1, 3),
                new RatioStar((byte) 3, 1, 4),
                new RatioStar((byte) 4, 1, 5), });
    }
}
