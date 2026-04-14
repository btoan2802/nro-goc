package nro.models.boss.broly;

import java.util.Random;
import nro.consts.ConstItem;
import nro.consts.ConstRatio;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class SuperZamax extends Broly {

    public SuperZamax() {
        super(BossFactory.S_ZMAS, BossData.SUPER_ZAMAS);
        this.nPoint.defg = (short) (this.nPoint.hpg / 1000000);
        if (this.nPoint.defg < 0) {
            this.nPoint.defg = (short) -this.nPoint.defg;
        }
    }

    public SuperZamax(byte id, BossData data) {
        super(id, data);
        this.nPoint.defg = (short) (this.nPoint.hpg / 10000000);
        if (this.nPoint.defg < 0) {
            this.nPoint.defg = (short) -this.nPoint.defg;
        }
    }

    @Override
    public void attack() {
        try {
            if (!charge()) {
                Player pl = getPlayerAttack();
                if (pl != null) {
                    this.playerSkill.skillSelect = this.getSkillAttack();
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                    false);
                        }
                        this.effectCharger();
                        try {
                            SkillService.gI().useSkill(this, pl, null);
                        } catch (Exception e) {
                            Log.error(SuperZamax.class, e);
                        }
                    } else {
                        goToPlayer(pl, false);
                    }
                    if (Util.isTrue(5, ConstRatio.PER100)) {
                        this.changeIdle();
                    }
                }
            }
        } catch (Exception ex) {
            Log.error(SuperZamax.class, ex);
        }
    }

    @Override
    public Player getPlayerAttack() {
        if (countChangePlayerAttack < targetCountChangePlayerAttack
                && plAttack != null && plAttack.zone != null && plAttack.zone.equals(this.zone)
                && !plAttack.effectSkin.isVoHinh) {
            if (!plAttack.isDie()) {
                this.countChangePlayerAttack++;
                return plAttack;
            } else {
                plAttack = null;
            }
        } else {
            this.targetCountChangePlayerAttack = Util.nextInt(10, 20);
            this.countChangePlayerAttack = 0;
            plAttack = this.zone.getRandomPlayerInMap();
        }
        return plAttack;
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
    }

    @Override
    public void die() {
        this.secondTimeRestToNextTimeAppear = 900; // 15p
        super.die();
    }

//    @Override
//    public void rewards(Player pl) {
//        ItemMap itemMap = null;
//        int x = this.location.x;
//        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
//        if (Util.isTrue(1, 50)) {
//            int[] set1 = {562, 564, 566, 561};
//            itemMap = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 1, x, y, pl.id);
//            RewardService.gI().initBaseOptionClothesMap(itemMap);
//        } else if (Util.isTrue(15, 15)) {
//            int[] set2 = {555, 556, 563, 557, 558, 565, 559, 567, 560};
//            itemMap = new ItemMap(this.zone, set2[Util.nextInt(0, set2.length - 1)], 1, x, y, pl.id);
//            RewardService.gI().initBaseOptionClothesMap(itemMap);
//        } else if (Util.isTrue(1, 5)) {
//            itemMap = new ItemMap(this.zone, 16, 1, x, y, pl.id);
//        }
//        if (Manager.EVENT_SEVER == 3) {
//            if (pl.nPoint.wearingNoelHat && Util.isTrue(1, 30)) {
//                itemMap = new ItemMap(this.zone, 926, 1, x, y, pl.id);
//                itemMap.options.add(new ItemOption(93, 1));
//            }
//        }
//        if (Manager.EVENT_SEVER == 4 && itemMap == null) {
//            itemMap = new ItemMap(this.zone,
//                    ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0, ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)], 1,
//                    x, y, pl.id);
//            itemMap.options.add(new ItemOption(74, 0));
//        }
//        if (itemMap != null) {
//            Service.getInstance().dropItemMap(zone, itemMap);
//        }
//
//    }
    @Override
    public void rewards(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
        int[] itemDos = new int[]{
            555, 557, 559,
            562, 564, 566,
            563, 565, 567};
        int randomDo = new Random().nextInt(itemDos.length);
        if (Util.isTrue(10, 100)) {
            if (Util.isTrue(1, 5)) {
                Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            if (Util.isTrue(10, 100)) {
                this.dropItemReward(16, (int) plKill.id);
            }
        }
        generalRewards(plKill, (byte) 12, (byte) 25);
    }
    
    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

}
