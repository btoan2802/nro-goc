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

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class MapDauDinh extends FutureBoss {

    public MapDauDinh() {
        super(BossFactory.MAP_DAU_DINH, BossData.MAP_DAU_DINH);
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
    protected boolean useSpecialSkill() {
        return false;
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
        this.textTalkBefore = new String[]{"|-1|Tao là Mập Đầu Đinh",
            "|-2|Em chào đại ca!",
            "|-1|Ai thèm làm đại ca của mày?"
        };
        this.textTalkMidle = new String[]{"|-1|Chết hết đi cho tao",
            "|-1|Tao sẽ giết hết bọn mày",
            "|-1|Hahaha",
            "|-1|Tao chỉ cần 10 phút để giết hết bọn mày",
            "|-1|Được rồi tao sẽ thổi bay hết bọn mày",
            "|-1|Muốn đùa thì thêm tí muối đi!",
            "|-2|Thằng này,tao nhịn mày lâu lắm rồi ấy nhá",
            "|-2|Coi thường nhau quá đấy",};
        this.textTalkAfter = new String[]{"|-1|Ôi bạn ơi ....ơi!!!"};
    }

}
