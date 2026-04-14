package nro.models.boss.cell;

import java.util.Random;
import nro.consts.ConstItem;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
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

/**
 * @author outcast c-cute hột me 😳
 */
public class XenMax extends FutureBoss {

    public XenMax() {
        super(BossFactory.XEN_MAX, BossData.XEN_MAX);
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
            Log.error(XenMax.class, ex);
        }
    }

    @Override
    public void idle() {
    }

@Override
public void rewards(Player plKill) {
    TaskService.gI().checkDoneTaskKillBoss(plKill, this);

    int x = this.location.x;
    int y = this.location.y;

    // ===== HIẾM NHẤT =====
    int[] itemRare = {561, 562, 564, 566};

    // ===== ĐỠ HIẾM HƠN =====
    int[] itemLessRare = {
        555, 556, 557, 558, 559,
        560, 563, 565, 567
    };

    int roll = Util.nextInt(100); // 0 - 99

    // 10% ITEM THƯỜNG (ID 16) - LÊN ĐẦU
    if (roll < 20) {
        this.dropItemReward(16, (int) plKill.id);
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
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"|-1|Kame Kame Haaaaa!!",
            "|-1|Mi khá đấy nhưng so với ta chỉ là hạng tôm tép",
            "|-1|Tất cả nhào vô hết đi", "|-1|Cứ chưởng tiếp đi. haha",
            "|-1|Các ngươi yếu thế này sao hạ được ta đây. haha",
            "|-1|Khi công pháo!!", "|-1|Cho mi biết sự lợi hại của ta"};
        this.textTalkAfter = new String[]{"|-1|Các ngươi được lắm",
            "|-1|Hãy đợi đấy thời gian tới ta sẽ quay lại.."};
    }
}
