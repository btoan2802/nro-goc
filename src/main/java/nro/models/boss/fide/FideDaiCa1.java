package nro.models.boss.fide;

import java.util.Calendar;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.TimeUtil;
import nro.utils.Util;

public class FideDaiCa1 extends FutureBoss {

    public FideDaiCa1() {
        super(BossFactory.FIDE_DAI_CA_1, BossData.FIDE_DAI_CA_1);
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
            final Calendar rightNow = Calendar.getInstance();
            int hour = rightNow.get(11);
            if (TimeUtil.timeBoss(hour)) {// time boss
                if (plAtt != null && plAtt.playerTask.taskMain.id != 21) {
                    if (plAtt.playerTask.taskMain.index != 0) {
                        if (damage >= 0) {
                            damage = 0;
                            Service.getInstance().sendThongBao(plAtt,
                                    "Bây giờ là giờ nhiệm vụ, không phải nhiệm vụ hiện tại của bạn, boss miễn nhiễm sát thương");
                        }
                    }
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
        this.textTalkBefore = new String[]{"|-1|Khá lắm con trai, dám phá tan giấc mộng của ta...",
            "|-1|Ta không thấy đám Ginyu đâu,... các ngươi đã giết chúng rồi à?",
            "|-1|Tuy không biết các ngươi dùng quỷ kế gì, nhưng ta rất ấn tượng đấy!",
            "|-1|Không thể tha thứ, ta không thể tha cho lũ sâu bọ các ngươi được!!"
        };

        this.textTalkMidle = new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
            "|-1|Toàn bọn tốt thí",
            "|-2|Không..thể..nào!!",
            "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!",
            "|-1|Chúng mày nghĩ kiến lại thắng nổi khủng long sao?",
            "|-1|Hô hô hô",
            "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
        };
        this.textTalkAfter = new String[]{"|-1|Biến hình, hây aaaa..."};
    }

    @Override
    public void leaveMap() {
        CreatBossLastDie(BossFactory.FIDE_DAI_CA_2, this.location.x);
        super.leaveMap();
        this.setJustRestToFuture();
        BossManager.gI().removeBoss(this);
    }

}
