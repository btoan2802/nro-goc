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
import nro.services.func.ChangeMapService;
import nro.utils.TimeUtil;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class Kuku extends FutureBoss {

    public Kuku() {
        super(BossFactory.KUKU, BossData.KUKU);
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
        this.textTalkBefore = new String[]{"|-1|Hế lô em,anh đứng đây từ chiều",
            "|-1|Mày hiểu thế là sao chứ? Cuối cùng tao đã có thể giết mày!",
            "|-2|Tao lại sợ mày quá cơ,cho bố cái địa chỉ!",
            "|-1|Mày làm tao phấn khích rồi đấy hahaha.."
        };
        this.textTalkMidle = new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
            "|-1|Ha ha ha! Mắt mày mù à? Nhìn máy đo chỉ số đi!!",
            "|-1|Định chạy trốn hả, hử",
            "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
            "|-1|Hahaha mày đây rồi",
            "|-1|Tao đã có lệnh từ đại ca Fide rồi"
        };
        this.textTalkAfter = new String[]{"|-2|Đẹp trai nó phải thế"};
    }

//    @Override
//    public void leaveMap() {
//        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
//        super.leaveMap();
//    }

}
