package nro.models.map.war;

import nro.models.map.Map;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.TimeUtil;
import nro.utils.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nro.services.PlayerService;
import nro.utils.TimeUtil;
import nro.utils.Util;
import java.util.ArrayList;
import java.util.List;
import nro.models.map.Zone;
import nro.services.PlayerService;
import nro.utils.TimeUtil;
import nro.utils.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nro.services.PlayerService;
import nro.utils.TimeUtil;
import nro.utils.Util;
import java.util.ArrayList;
import java.util.List;
import nro.services.PlayerService;

/**
 *
 * Bản Quyền được CODER bởi KENIT lẤY THÌ LẤY ĐỔI DÒNG NÀY LÀ CON CHÓ LỊCH SỰ
 * TẠO LÊN ...
 */


public class DaichienWar {

    public static final byte HOUR_OPEN_1 = 0;
    public static final byte MIN_OPEN_1 = 0;
    public static final byte SECOND_OPEN_1 = 0;
    public static final byte HOUR_CLOSE_1 = 24;
    public static final byte MIN_CLOSE_1 = 5;
    public static final byte SECOND_CLOSE_1 = 0;

    // time2 (12:00 - 13:00)
    public static final byte HOUR_OPEN_2 = 20;
    public static final byte MIN_OPEN_2 = 10;
    public static final byte SECOND_OPEN_2 = 0;
    public static final byte HOUR_CLOSE_2 = 20;
    public static final byte MIN_CLOSE_2 = 15;
    public static final byte SECOND_CLOSE_2 = 0;

    private static final int TIME_WAR = 300000;
    private static final int MAX_REVIVE_COUNT = 5;
    private static final long REVIVE_DELAY = 5000;

    private static DaichienWar i;


    public static List<long[]> TIME_WINDOWS = new ArrayList<>();
    private int day = -1;

    private DaichienWar() {
        this.maps = new ArrayList<>();
    }

    public static DaichienWar gI() {
        if (i == null) {
            i = new DaichienWar();
        }
        i.setTime();
        return i;
    }

    public void setTime() {
        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
            i.day = TimeUtil.getCurrDay();
            TIME_WINDOWS.clear(); 
            try {
                // First time slot: 20:00 - 21:00
                long open1 = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_1 + ":" + MIN_OPEN_1 + ":" + SECOND_OPEN_1, "dd/MM/yyyy HH:mm:ss");
                long close1 = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_1 + ":" + MIN_CLOSE_1 + ":" + SECOND_CLOSE_1, "dd/MM/yyyy HH:mm:ss");
                TIME_WINDOWS.add(new long[]{open1, close1});

                // Second time slot: 12:00 - 13:00
                long open2 = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_2 + ":" + MIN_OPEN_2 + ":" + SECOND_OPEN_2, "dd/MM/yyyy HH:mm:ss");
                long close2 = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_2 + ":" + MIN_CLOSE_2 + ":" + SECOND_CLOSE_2, "dd/MM/yyyy HH:mm:ss");
                TIME_WINDOWS.add(new long[]{open2, close2});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isEventOpen() {
        long now = System.currentTimeMillis();
        for (long[] window : TIME_WINDOWS) {
            if (now >= window[0] && now <= window[1]) {
                return true;
            }
        }
        return false;
    }

    private List<Map> maps;

    public void addMap(Map map) {
        this.maps.add(map);
    }

    public void update(Player player) {
        try {
            long now = System.currentTimeMillis();
            if (TIME_WAR <= 0) {
                Service.getInstance().sendThongBaoFromAdmin(player, "Lỗi cấu hình thời gian, vui lòng báo Admin!");
                return;
            }

            if (player != null && player.zone != null && player.zone.map != null) {
                if (player.zone.map.mapId == 188) {
                    if (!player.isInMap188) {
                        player.lastTimeInMap188 = now;
                        player.isInMap188 = true;
                        player.reviveCount = 0;
                        player.lastDeathTime = 0;
                    } else {
                        if (player.isDie() && player.reviveCount < MAX_REVIVE_COUNT) {
                            Service.getInstance().sendThongBao(player, "Bạn sẽ được hồi sinh sau 5 giây!");
                            if (player.lastDeathTime == 0) {
                                player.lastDeathTime = now;
                            } else if (Util.canDoWithTime(player.lastDeathTime, REVIVE_DELAY)) {
                                PlayerService.gI().hoiSinh(player);
                                player.reviveCount++;
                                player.lastDeathTime = 0;
                                ///getzonerandom to hoisinh
                                Zone newZone = getDifferentZone(player);
                                if (newZone != null) {
                                    ChangeMapService.gI().changeMap(player, newZone, player.location.x, 312);
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không tìm thấy khu vực mới để chuyển!");
                                }
                                Service.getInstance().sendThongBao(player, "Hồi sinh thành công, bạn còn " + (MAX_REVIVE_COUNT - player.reviveCount) + " lượt hồi sinh.");
                            }
                        } else if (player.isDie() && player.reviveCount >= MAX_REVIVE_COUNT) {
                            Service.getInstance().sendThongBao(player, "Bạn đã hết lượt hồi sinh!");
                            kickOutOfMap(player);
                        }
                        // Existing timer logic
                        if (!player.isDie() && Util.canDoWithTime(player.lastTimeInMap188, TIME_WAR)) {
                            kickOutOfMap(player);
                        } else if (!player.isDie() && Util.canDoWithTime(player.lastTimeNotifyTimeLeft, 60000)) {
                            long timeLeft = TIME_WAR - (now - player.lastTimeInMap188);
                            Service.getInstance().sendThongBao(player, "Thời gian còn lại " + TimeUtil.getTimeLeft(timeLeft));
                            player.lastTimeNotifyTimeLeft = now;
                        }
                    }
                } else {
                    if (player.isInMap188) {
                        player.isInMap188 = false;
                        player.lastTimeInMap188 = 0;
                        player.lastTimeNotifyTimeLeft = 0;
                        player.reviveCount = 0;
                        player.lastDeathTime = 0;
                    }
                }
            }
        } catch (NullPointerException ex) {
            Service.getInstance().sendThongBaoFromAdmin(player, "Đã xảy ra lỗi, vui lòng báo Admin!");
        }
    }

    private Zone getDifferentZone(Player player) { //random zone
        if (player.zone != null && player.zone.map != null && player.zone.map.zones != null) {
            List<Zone> availableZones = new ArrayList<>();
            for (Zone zone : player.zone.map.zones) {
                if (zone != player.zone) {
                    availableZones.add(zone);
                }
            }
            if (!availableZones.isEmpty()) {
                return availableZones.get(new Random().nextInt(availableZones.size()));
            }
        }
        return null;
    }

    private void kickOutOfMap(Player player) {
        if (player.cFlag == 8) {
            Service.getInstance().changeFlag(player, Util.nextInt(1, 7));
        }
        Service.getInstance().sendThongBao(player, "Đã hết thời gian, Trọng tài sẽ đưa bạn về Đảo Guru");
        ChangeMapService.gI().changeMapBySpaceShip(player, 13, -1, 360);
    }

    public void joinMapDaichien(Player player) {
        Service.getInstance().changeFlag(player, Util.nextInt(1, 7));
    }
}
