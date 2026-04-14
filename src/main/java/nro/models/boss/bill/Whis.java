package nro.models.boss.bill;

import java.util.Random;
import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

public class Whis extends FutureBoss {

    public Whis() {
        super(BossFactory.WHIS, BossData.WHIS);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie()) {
            return 0;
        } else {
            if (plAtt != null) {
                if (Util.isTrue(10, 100)) {
                    damage = 1;
                    Service.getInstance().chat(this, "Xí hụt..");
                }
            }
            int dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
            }
            return dame;
        }
    }
    
    @Override
    public void joinMap() {
        super.joinMap();
        int x = this.location.x;
        short listBossTogether[] = {BossFactory.BILL};
        CreatBossTogether(zone, listBossTogether, x);

    }

   @Override
public void rewards(Player plKill) {
    TaskService.gI().checkDoneTaskKillBoss(plKill, this);

    int x = this.location.x;
    int y = this.location.y;

    int[] itemRare = {561, 562, 564, 566};
    int[] itemLessRare = {
        555, 556, 557, 558, 559,
        560, 563, 565, 567
    };

    int roll = Util.nextInt(100);

    // 20% - item 16
    if (roll < 20) {
        this.dropItemReward(16, (int) plKill.id);
        return;
    }

    // 10% - item ít hiếm
    if (roll < 30) {
        int itemId = itemLessRare[Util.nextInt(itemLessRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }

    // 5% - item hiếm
    if (roll < 35) {
        int itemId = itemRare[Util.nextInt(itemRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }

    // 65% - RANDOM 1 TRONG 2 (50 / 50)
    if (Util.nextInt(2) == 0) {
        generalRewards(plKill, (byte) 12, (byte) 25);
    } else {
        baseRewards(plKill, 1, 5, (byte) 3);
    }
}

    @Override
    public void idle() {

    }

    @Override
    public void leaveMap() {
        ChangeToAttackTogether(BossFactory.BILL);
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
