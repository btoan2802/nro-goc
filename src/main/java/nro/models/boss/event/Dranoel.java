package nro.models.boss.event;

import nro.consts.ConstRatio;
import nro.models.boss.*;
import nro.models.boss.cell.SieuBoHung;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class Dranoel extends Boss {

    public Dranoel() {
        super(BossFactory.DRANOEL, BossData.DRANOEL);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
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
            Log.error(SieuBoHung.class, ex);
        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        int dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            // damage = this.nPoint.hpMax / 20;
            damage = this.nPoint.subDameInjureWithDeff(damage);
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
//                rewards(plAtt);
                notifyPlayeKill(plAtt);
                die();
            }
            return dame;
        }
    }

       @Override
public void rewards(Player plKill) {
    TaskService.gI().checkDoneTaskKillBoss(plKill, this);

    int x = this.location.x;
    int y = this.location.y;

    // ===== HIẾM NHẤT =====
    int[] itemRare = {555, 556, 557, 558, 559,
        560, 563, 565, 567};

    // ===== ĐỠ HIẾM HƠN =====
    int[] itemLessRare = {
        925,926, 927, 928, 929, 930, 931
    };

    int roll = Util.nextInt(100); // 0 - 99

    // 10% ITEM THƯỜNG (ID 16) - LÊN ĐẦU
    if (roll < 15) {
        this.dropItemReward(Util.nextInt(925, 931), (int) plKill.id);
        return;
    }

    // 5% ĐỠ HIẾM
    if (roll < 20) {
        int itemId = itemLessRare[Util.nextInt(itemLessRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }

    // 2% HIẾM NHẤT
    if (roll < 22) {
        int itemId = itemRare[Util.nextInt(itemRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }

    // ===== CÒN LẠI =====
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
        this.textTalkBefore = new String[]{"|-2|Nguy to, hắn ta hợp thể rồi!!!"};
        this.textTalkMidle = new String[]{"|-1|Ta chính là thế giới",
            "|-1|Ta chính là công lí",
            "|-1|Lũ các ngươi làm ta thấy đau rồi ấy haha"
        };
        this.textTalkAfter = new String[]{"|-1|Không thể nào",
            "|-1|Ta chính là vị thần của thế giới này!!!!"};

    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }
}
