package nro.models.boss.cell;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class XenBoHung1 extends FutureBoss {

    public XenBoHung1() {
        super(BossFactory.XEN_BO_HUNG_1, BossData.XEN_BO_HUNG_1);
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
    public void rewards(Player pl) {
        if (pl != null) {
            try {
                if (Util.isTrue(15, 100)) {
                    ItemMap itemMap = null;
                    int x = this.location.x;
                    int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
                    itemMap = new ItemMap(pl.zone, 16, 1, x, y, pl.id);
                    Service.getInstance().dropItemMap(zone, itemMap);
                }
               
                TaskService.gI().checkDoneTaskKillBoss(pl, this);
            } catch (Exception e) {
                // TODO: handle exception
            }
            generalRewards(pl, (byte) 12, (byte) 25);
        }

    }

    @Override
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl != null) {
                this.playerSkill.skillSelect = this.getSkillAttack();
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    }
                    SkillService.gI().useSkill(this, pl, null);
                    checkPlayerDie(pl);
                } else {
                    goToPlayer(pl, false);
                }
            }
        } catch (Exception ex) {
            Log.error(XenBoHung1.class, ex);
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
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"|-1|Tất cả nhào vô", "|-1|Mình ta cũng đủ để hủy diệt các ngươi"};
        this.textTalkAfter = new String[]{};
    }

    @Override
    public void leaveMap() {
        CreatBossLastDie(BossFactory.XEN_BO_HUNG_2, this.location.x);
        this.setJustRestToFuture();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
