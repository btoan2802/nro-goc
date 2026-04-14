package nro.models.boss.robotsatthu;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.PlayerService;
import nro.services.TaskService;
import nro.utils.Log;
import nro.utils.Logger;

public class Android_14 extends Boss {

    public boolean callApk13;

    public Android_14() {
        super(BossFactory.ANDROID_14, BossData.ANDROID_14);
    }

    @Override
    public void rewards(Player pl) {
        generalRewards(pl, (byte) 13, (byte) 25);
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
     

    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void leaveMap() {
        ChangeToAttackTogether(BossFactory.ANDROID_13);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    public void callApk13() {
        try {
            this.changeToIdle();
            this.recoverHP();
            this.callApk13 = true;
            Boss apk15 = BossManager.gI().getBossById(BossFactory.ANDROID_15);
            if (apk15 != null) {
                ((Android_15) apk15).callApk13 = true;
                ((Android_15) apk15).NonPk();
                ((Android_15) apk15).recoverHP();
            }
            short listBoss[] = {BossFactory.ANDROID_13};
            CreatBossTogether(zone, listBoss, this.location.x);
        } catch (Exception e) {
            Log.error("Lỗi call apk từ apk 14");
        }

    }

    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void joinMap() {

    }

    @Override
    public void initTalk() {
        // this.textTalkBefore = new String[] { "|-1|Các ngươi tìm đến cái chết à" };
        this.textTalkMidle = new String[]{"|-1|Ê cố lên nhóc",
            "|-1|Chán", "|-1|Hãy cho ta biết Gôku đang ở đâu"};
    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
