package nro.models.boss.chill;

import java.util.Random;
import nro.consts.ConstItem;
import nro.models.boss.*;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class Chill2 extends FutureBoss {

    public Chill2() {
        super(BossFactory.CHILL2, BossData.CHILL2);
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
    
//    @Override
//    public void rewards(Player pl) {
//        // TaskService.gI().checkDoneTaskKillBoss(pl, this);
//        ItemMap itemMap = null;
//        int x = this.location.x;
//        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
//
//        if (Util.isTrue(18, 100)) {
//            int[] set1 = {562, 564, 566, 561};
//            itemMap = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 1,
//                    x, y, pl.id);
//            RewardService.gI().initBaseOptionClothesMap(itemMap);
//        } else if (Util.isTrue(1, 15)) {
//            int[] set2 = {555, 556, 563, 557, 558, 565, 559, 567, 560};
//            itemMap = new ItemMap(this.zone, set2[Util.nextInt(0, set2.length - 1)], 1,
//                    x, y, pl.id);
//            RewardService.gI().initBaseOptionClothesMap(itemMap);
//        } else if (Util.isTrue(1, 5)) {
//            itemMap = new ItemMap(this.zone, 16, 1, x, y, pl.id);
//        }
//        if (Manager.EVENT_SEVER == 4 && itemMap == null) {
//            itemMap = new ItemMap(this.zone,
//                    ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0,
//                            ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)], 1,
//                    x, y, pl.id);
//            itemMap.options.add(new ItemOption(74, 0));
//        }
//        if (itemMap != null) {
//            Service.getInstance().dropItemMap(zone, itemMap);
//        }
//        generalRewards(pl, (byte) 13, (byte) 25);
//        baseRewards(pl, 10, 20, (byte) 3);
//    }
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
    if (roll < 15) {
        this.dropItemReward(16, (int) plKill.id);
        return;
    }
    if (roll < 20) {
        int itemId = itemLessRare[Util.nextInt(itemLessRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }
    if (roll < 22) {
        int itemId = itemRare[Util.nextInt(itemRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }
    generalRewards(plKill, (byte) 12, (byte) 25);
}

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {

        textTalkAfter = new String[]{"|-1|Ta đã giấu hết ngọc rồng rồi, các ngươi tìm vô ích hahaha"};
    }

    @Override
    public void leaveMap() {
        BossFactory.createBossAffterLeaveMap(BossFactory.CHILL, true);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
