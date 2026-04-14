package nro.models.boss.fide;

import java.util.Calendar;
import nro.models.boss.*;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.TimeUtil;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class FideDaiCa2 extends FutureBoss {

    public FideDaiCa2() {
        super(BossFactory.FIDE_DAI_CA_2, BossData.FIDE_DAI_CA_2);
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

        this.textTalkBefore = new String[]{"|-1|Hê hê, cẩn thận đi",
            "|-1|Nếu đã biến thành thế này thì ta sẽ không nhùn nhặn như trước đâu"
        };
        this.textTalkMidle = new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
            "|-1|Ôi, xin lỗi nhé. Sức mạnh lớn quá nên ta cũng chẳng điều khiển nổi nữa!",
            "|-1|Hahaha! Ấn tượng đấy! Tên nào cũng lủi rất nhanh!", "|-2|A...Tốc độ nhanh quá!",
            "|-1|Hình như... mày không phải là một thằng nhóc bình thường!",
            "|-1|Mấy đòn vừa rồi, nói thật là cũng đau đấy!",
            "|-1|Nhưng tiếc rằng đối thủ của mày lại là Fide này...",
            "|-2|Chết tiệt.. chúng ta đã đánh giá quá thấp sức mạnh của hắn!!", "|-2|Đồ..Đồ quái vật..!",
            "|-2|Tốc độ kinh hoàng quá! Ai mà né nổi chứ!",};
        this.textTalkAfter = new String[]{"|-1|Ác quỷ biến hình, hêy aaa......."};
    }

    @Override
    public void leaveMap() {
        CreatBossLastDie(BossFactory.FIDE_DAI_CA_3, expoff);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void joinMap() {
        if (this.zone != null) {
            ChangeMapService.gI().changeMap(this, zone, this.location.x, this.location.y);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }
    }
}
