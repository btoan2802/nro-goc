package nro.models.boss;

import nro.utils.Log;
import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstPlayer;
import nro.models.boss.testdame.Maydosucmanh;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.server.io.Message;
import nro.services.MapService;
import nro.utils.Logger;

public class BossManager {

    public static final List<Boss> BOSSES_IN_GAME;
    private static BossManager intance;

    static {
        BOSSES_IN_GAME = new ArrayList<>();
    }

    public static List<Boss> getBosses() {
        return BOSSES_IN_GAME;
    }

    public void updateAllBoss() {
        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
            try {
                Boss boss = BOSSES_IN_GAME.get(i);
                if (boss != null) {
                    boss.update();
                }
            } catch (Exception e) {
                Log.error(BossManager.class, e);
            }
        }

    }

    private BossManager() {

    }

    public static BossManager gI() {
        if (intance == null) {
            intance = new BossManager();
        }
        return intance;
    }

    public Boss getBossById(short bossId) {
        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
            if (BOSSES_IN_GAME.get(i).id == bossId) {
                return BOSSES_IN_GAME.get(i);
            }
        }
        return null;
    }

    public void setJustRest(short bossId) {
        Boss boss = getBossById(bossId);
        if (boss != null) {
            boss.setJustRest();
        }
    }

    public void addBoss(Boss boss) {
        boolean have = false;
        for (Boss b : BOSSES_IN_GAME) {
            if (boss.equals(b)) {
                have = true;
                break;
            }
        }
        if (!have) {
            BOSSES_IN_GAME.add(boss);
        }
    }

//    public void showListBoss(Player player) {
//        Message msg;
//        try {
//            int count = 0;
//            List<Boss> bossDisplay = new ArrayList<>();
//            for (int i = 0; i < BOSSES_IN_GAME.size(); i++) {
//                Boss boss = BossManager.BOSSES_IN_GAME.get(i);
//                if (MapService.gI().isMapMabuWar(boss.data.mapJoin[0])
//                        || MapService.gI().isMapBlackBallWar(boss.data.mapJoin[0])
//                        || MapService.gI().isMapBanDoKhoBau(boss.data.mapJoin[0])
//                        || MapService.gI().isMapDoanhTrai(boss.data.mapJoin[0])) {
//                    continue;
//                }
//                bossDisplay.add(boss);
//                count += 1;
//                if (i > 120) {
//                    break;
//                }
//            }
//            msg = new Message(-96);
//            msg.writer().writeByte(0);
//            msg.writer().writeUTF("Boss");
//            msg.writer().writeByte((int) count);
//            for (int i = 0; i < count; i++) {
//                Boss boss = bossDisplay.get(i);
//                msg.writer().writeInt(i);
//                msg.writer().writeInt((int) boss.id);
//                msg.writer().writeShort(boss.data.outfit[0]);
//                if (player.getSession().version > 214) {
//                    msg.writer().writeShort(-1);
//                }
//
//                msg.writer().writeShort(boss.data.outfit[1]);
//                msg.writer().writeShort(boss.data.outfit[2]);
//                msg.writer().writeUTF(boss.data.name);
//                if (boss.zone != null) {
//                    msg.writer().writeUTF("Live");
//                    msg.writer().writeUTF(
//                            boss.zone.map.mapName + "(" + boss.zone.map.mapId + ")");
//                } else {
//                    msg.writer().writeUTF("Die");
//                    msg.writer().writeUTF("Die");
//                }
//            }
//            player.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void showListBoss(Player player) {
        Message msg;
        try {
            // Lọc ra những boss còn sống và không phải isYar hoặc Maydosucmanh
            long aliveBossCount = BOSSES_IN_GAME.stream()
                    .filter(boss -> boss != null && boss.zone != null)
                    .filter(boss -> !BossFactory.isYar((byte) boss.id))
                    .filter(boss -> !(boss instanceof Maydosucmanh)) // Loại bỏ Maydosucmanh
                    .count();

            int count = (int) (aliveBossCount > Byte.MAX_VALUE ? Byte.MAX_VALUE : aliveBossCount);
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss (SL: " + count + ")");

            msg.writer().writeByte(count);
            for (int i = 0; i < BOSSES_IN_GAME.size(); i++) {
                Boss boss = BossManager.BOSSES_IN_GAME.get(i);

                // Kiểm tra nếu boss không phải là Yar, không phải Maydosucmanh và còn sống
                if (boss == null || boss.zone == null || BossFactory.isYar((byte) boss.id) || boss instanceof Maydosucmanh) {
                    continue;
                }

                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.getBody());
                msg.writer().writeShort(boss.getLeg());
                msg.writer().writeUTF(boss.name);
                msg.writer().writeUTF("Sống");
                msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListBoss1(Player player) {
        Message msg;
        try {
            // Lọc ra những boss còn sống và không phải isYar hoặc Maydosucmanh
            long aliveBossCount = BOSSES_IN_GAME.stream()
                    .filter(boss -> boss != null && boss.zone != null)
                    .filter(boss -> !BossFactory.isYar((byte) boss.id))
                    .filter(boss -> !(boss instanceof Maydosucmanh)) // Loại bỏ Maydosucmanh
                    .count();

            int count = (int) (aliveBossCount > Byte.MAX_VALUE ? Byte.MAX_VALUE : aliveBossCount);
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss (SL: " + count + ")");

            msg.writer().writeByte(count);
            for (int i = 0; i < BOSSES_IN_GAME.size(); i++) {
                Boss boss = BossManager.BOSSES_IN_GAME.get(i);

                // Kiểm tra nếu boss không phải là Yar, không phải Maydosucmanh và còn sống
                if (boss == null || boss.zone == null || BossFactory.isYar((byte) boss.id) || boss instanceof Maydosucmanh) {
                    continue;
                }

                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.getBody());
                msg.writer().writeShort(boss.getLeg());
                msg.writer().writeUTF(boss.name);
                msg.writer().writeUTF(" Còn Sống");
                msg.writer().writeUTF("Map: " + boss.zone.map.mapName);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void removeBoss(Boss boss) {
        BOSSES_IN_GAME.remove(boss);
        boss.dispose();
    }

    public void SendTbaoBosss() {
        // Tạo bản sao để tránh lỗi khi danh sách gốc bị chỉnh sửa ở thread khác
        List<Boss> snapshot = new ArrayList<>(BOSSES_IN_GAME);
    
        for (Boss boss : snapshot) {
            if (boss != null && boss.zone != null
                    && !MapService.gI().isMapDoanhTrai(boss.zone.map.mapId)
                    && !MapService.gI().isMapBanDoKhoBau(boss.zone.map.mapId)
                    && !boss.notNotify
                    && boss.typePk != ConstPlayer.NON_PK) {
                String notificationMessage = "Boss " + boss.name + " vừa xuất hiện tại " + boss.zone.map.mapName;
                ServerNotify.gI().notify(notificationMessage);
            }
        }
    }
    

}
