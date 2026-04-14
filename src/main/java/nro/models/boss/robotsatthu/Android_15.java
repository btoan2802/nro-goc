package nro.models.boss.robotsatthu;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.ServerNotify;
import nro.services.PlayerService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Logger;
import nro.utils.Util;

public class Android_15 extends Boss {

    public boolean callApk13;

    public Android_15() {
        super(BossFactory.ANDROID_15, BossData.ANDROID_15);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        generalRewards(pl, (byte) 13, (byte) 25);
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
   
    }

    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }

    private void CallApk() {
        try {
            Boss apk14 = BossManager.gI().getBossById(BossFactory.ANDROID_14);
            if (apk14 != null) {
                ((Android_14) apk14).callApk13();
            }
        } catch (Exception e) {
            Logger.error("Lỗi call apk");
        }
    }

    @Override
    public void leaveMap() {
        ChangeToAttackTogether(BossFactory.ANDROID_14);
        super.leaveMap();
        setJustRestToFuture();
        changeStatus(DIE);
    }

    public void NonPk() {
        this.changeToIdle();
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            short listBossTogether[] = {BossFactory.ANDROID_14, BossFactory.ANDROID_13};
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            CreatBossTogether(zone, listBossTogether, x);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }

    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
