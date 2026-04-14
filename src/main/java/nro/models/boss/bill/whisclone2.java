package nro.models.boss.bill;

import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

public class whisclone2 extends Boss {

    public whisclone2() {
        super(BossFactory.WHIS_CLONE2, BossData.WHIS);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void joinMap() {
        super.joinMap();
        BossFactory.createBoss(BossFactory.BILL_CLONE2).zone = this.zone;
    }

    @Override
    public void rewards(Player pl) {

        if (!generalRewards(pl, (byte) 12, (byte) 25)) {
            baseRewards(pl, 10, 20, (byte) 3);
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void leaveMap() {
        ChangeToAttackTogether(BossFactory.BILL_CLONE2);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {

    }

}
