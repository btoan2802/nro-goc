package nro.models.boss.Khidot;

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
public class Khidot1 extends Boss {

    public Khidot1() {
        super(BossFactory.KHI_DOT_1, BossData.KHI_DOT_1);
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
        if (Util.isTrue(100, 100)) {
            this.dropItemReward(1045, (int) pl.id, 1);
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
    
    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, 1, piercing, isMobAttack);
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }

}
