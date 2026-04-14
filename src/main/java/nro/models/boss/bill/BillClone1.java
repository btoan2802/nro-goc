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

public class BillClone1 extends Boss {

    public BillClone1() {
        super(BossFactory.BILL_CLONE1, BossData.BILL);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
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
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {

    }

    @Override
    public void leaveMap() {
        BossFactory.createBossAffterLeaveMap(BossFactory.WHIS_CLONE1, false);
        this.setJustRestToFuture();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
