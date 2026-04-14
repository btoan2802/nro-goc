package nro.models.boss.event;

import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.player.Player;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Log;
import nro.utils.Util;

public abstract class EscortedBoss extends Boss {

    protected Player escort;

    /**
     * Khoảng cách tối đa cho phép để tiếp tục dắt boss.
     * Tùy map và gameplay bạn có thể chỉnh 200–600.
     */
    private static final int MAX_ESCORT_DISTANCE = 300;

    /**
     * Khoảng cách "theo sát" - nếu xa hơn thì boss sẽ cố chạy theo.
     */
    private static final int FOLLOW_DISTANCE = 24;

    public EscortedBoss(short id, BossData data) {
        super(id, data);
    }

    @Override
    public void attack() {
        move();
    }

   public void move() {
        if (Util.isTrue(99, ConstRatio.PER100)) {
            if (escort == null) {
                try {
                    Player pl = getPlayerAttack();
                    if (pl != null) {
                        if (!useSpecialSkill()) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                        }
                    }
                } catch (Exception ex) {
                    Log.error(EscortedBoss.class, ex);
                }
            } else {
                int d = Util.getDistance(this, escort);
                if (d > 24) {
                    int x = 0;
                    if (location.x < escort.location.x) {
                        x = location.x + Util.nextInt(12, 36);
                    } else {
                        x = location.x - Util.nextInt(12, 36);
                    }
                    if (x < 35) {
                        x = 35;
                    } else if (x > this.zone.map.mapWidth - 35) {
                        x = this.zone.map.mapWidth - 35;
                    }
                    int y = escort.location.y;
                    if (location.y > 50) {
                        y = this.zone.map.yPhysicInTop(x, y - 50);
                    }
                    goToXY(x, y, false);
                }
            }
        }
    }


    public void joinMapEscort() {
        if (!MapService.gI().isMapVS(escort.zone.map.mapId)) {
            this.location.x = escort.location.x + Util.nextInt(-10, 10);
            this.location.y = escort.location.y;
            MapService.gI().goToMap(this, escort.zone);
            escort.zone.load_Me_To_Another(this);
        } else {
            stopEscorting();
        }
    }

    public void setEscort(Player escort) {
        this.escort = escort;
        escort.setEscortedBoss(this);
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
    }

    public void stopEscorting() {
        changeStatus(LEAVE_MAP);
        this.escort.setEscortedBoss(null);
        this.escort = null;
    }

}
