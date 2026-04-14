package nro.models.boss.event.Event15;

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
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class XenConEvent extends FutureBoss {

    public XenConEvent() {
        super(BossFactory.XEN_CON_EVENT, BossData.XEN_CON_EVENT);
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
    public void rewards(Player pl) {
        if (pl != null) {
            if (!generalRewards(pl, (byte) 9, (byte) 10)) {
                baseRewards(pl, 10, 20, (byte) 5);
            }
        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {

        damage = Util.nextInt(1000, 5000) * 1000;
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"|-1|Bọn mày dám cả gan đến đây",
            "|-1|Ta sẽ sớm tiến hóa thôi haha!",
            "|-2|Chỉ là một con bọ!",
            "|-1|Mày làm tao phấn khích rồi đấy hahaha.."
        };
        this.textTalkMidle = new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
            "|-1|Ha ha ha! Mắt mày mù à? Nhìn máy đo chỉ số đi!!",
            "|-1|Định chạy trốn hả, hử",
            "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
            "|-1|Hahaha mày đây rồi",};
        this.textTalkAfter = new String[]{"|-1|Ta sẽ sớm lột xác hahahaa"};
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }

}
