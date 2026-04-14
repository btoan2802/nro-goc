package nro.models.map;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nro.consts.ConstIgnoreName;
import nro.models.mob.Mob;
import nro.models.mob.MobFactory;
import nro.server.ServerNotify;
import nro.services.MapService;
import nro.utils.Logger;
import nro.utils.Util;

public class BigMobManager {

    private static BigMobManager i;

    public static BigMobManager getInstance() {
        if (i == null) {
            i = new BigMobManager();
        }
        return i;
    }

    public void createBigMob() {

        int idMap = 167; // cảng hải tặc
        int idMob = 72; // robot bảo vệ
        int hpMob = 5500000;
        int xP = 550;
        int yP = 336;
        String nameMob = "Rôbốt bảo vệ";
        if (Util.isTrue(50, 100)) {
            idMap = 165;
            idMob = 71;
            hpMob = 7500000;
            xP = 740;
            yP = 576;
            nameMob = "Vua Bạch Tuộc";
        }
        Map map1 = MapService.gI().getMapById(idMap);
        if (map1 != null) {
            for (Zone zoneX : map1.zones) {
                for (Mob modX : zoneX.mobs) {
                    if (modX.tempId == idMob) {
                        // ServerNotify.gI().notify(nameMob + " đang có mặt tại " +
                        // modX.zone.map.mapName + " khu "
                        // + modX.zone.zoneId);
                        // Logger.warning("Mob " + nameMob + " id:" + modX.tempId + " đã tồn tại, đã
                        // thông báo lại");
                        return;
                    }
                }
            }
            Zone zone = map1.zones.get(Util.nextInt(map1.zones.size()));
            if (zone != null) {
                try {
                    Mob mob = new Mob();
                    mob.tempId = idMob;
                    mob.level = 21;
                    mob.point.setHpFull(hpMob);
                    mob.location.x = xP;
                    mob.location.y = yP;
                    mob.point.setHP(mob.point.getHpFull());
                    mob.pDame = 1;
                    mob.pTiemNang = 21;
                    mob.setTiemNang();
                    mob.status = 5;

                    Mob mobZone = MobFactory.newMob(mob);
                    mobZone.zone = zone;
                    zone.addMob(mobZone);
                    ServerNotify.gI().notify(nameMob + " xuất hiện tại " + mobZone.zone.map.mapName + " khu "
                            + mobZone.zone.zoneId);
                    // Logger.warning(nameMob + " xuất hiện tại " + mobZone.zone.map.mapName + " khu
                    // "
                    // + mobZone.zone.zoneId);

                } catch (Exception e) {
                    Logger.logException(MapService.class, e, "Lỗi tạo big mob");
                }
            }

        }

    }

}
