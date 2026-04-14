package nro.models.boss.nappa;

import java.util.Calendar;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.TimeUtil;
import nro.utils.Util;

public class Rambo extends FutureBoss {

    public Rambo() {
        super(BossFactory.RAMBO, BossData.RAMBO);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void joinMap() {
        super.joinMap();

    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        int dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            
            final Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(11);
            if (TimeUtil.timeBoss(hour)) {// time boss
                if (plAtt != null && plAtt.playerTask.taskMain.id != 19) {
                    if (plAtt.playerTask.taskMain.index != 0) {
                        if (damage >= 0) {
                            damage = 0;
                            Service.getInstance().sendThongBao(plAtt,
                                    "Bây giờ là giờ nhiệm vụ, không phải nhiệm vụ hiện tại của bạn, boss miễn nhiễm sát thương");
                        }
                    }
                }
            }
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                notifyPlayeKill(plAtt);
                die();
            }
            return dame;
        }
    }
    @Override
    public void rewards(Player pl) {
        generalRewards(pl, (byte) 13, (byte) 25);
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        BossPointEven(pl);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {

        this.textTalkBefore = new String[]{"|-2|Xin chào, lâu lắm không gặp!",
            "|-2|Tao đã kết liễu Mập đầu đinh rồi, lần này đến lượt mày...",
            "|-1|Mày đã giết Mập đầu đinh sao..!? Đừng.. đừng hòng lừa tao!",
            "|-2|Vậy sao mày không thử sức mạnh của tao luôn đi?"
        };
        this.textTalkMidle = new String[]{"|-1|Hahaha",
            "|-1|Ngạc nhiên thật, đúng là mày đã tiến bộ rất nhanh..",
            "|-1|Tao sẽ cho mày biết lý do tại sao tao lại không dùng đến năng lực thực sự..",
            "|-1|Đến tao còn không thắng nổi thì đừng mộng tưởng đối đầu với đại ca Fide!",
            "|-1|Ha ha ha! Ngươi tưởng chạy trốn được sao?",
            "|-2|Oái..!",
            "|-2|Đừng tưởng thế này là xong..! Tao sẽ còn mạnh hơn nữa!",};
        this.textTalkAfter = new String[]{"|-1|Ôi bạn ơi..."};
    }

}
