package nro.models.boss.robotsatthu;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.TaskService;

public class Android19 extends Boss {

    public Android19() {
        super(BossFactory.ANDROID_19, BossData.ANDROID_19);
    }

    @Override
    public void joinMap() {

    }

    @Override
    public void leaveMap() {
        ChangeToAttackTogether(BossFactory.ANDROID_20);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        if (!generalRewards(pl, (byte) 10, (byte) 25)) {
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
        this.textTalkMidle = new String[]{"|-1|Oải rồi hả?",
            "|-1|Ê cố lên nhóc",
            "|-1|Chán",
            "|-1|Ngươi sẽ không bao giờ thắng được đâu!!",
            "|-2|Ngươi vừa hút được nhiều rồi đấy, nhưng giờ thì đừng hòng!!",};
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null) {
            switch (plAtt.playerSkill.skillSelect.template.id) {
                case Skill.KAMEJOKO:
                case Skill.MASENKO:
                case Skill.ANTOMIC:
                    int hpHoi = damage - (int) this.nPoint.calPercent(damage, 80);
                    PlayerService.gI().hoiPhuc(this, hpHoi, 0);
                    return 0;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

}
