package nro.models.boss.tieudoisatthu_nm;

import nro.consts.ConstOption;
import nro.models.boss.*;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class So1Namek extends FutureBoss {

    public So1Namek() {
        super(BossFactory.SO1_NAMEK, BossData.SO1_NAMEK);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
public void rewards(Player plKill) {
    // Tỉ lệ rơi item 1429: 1/10 = 10%
    if (Util.isTrue(1, 10)) {
        // Drop item 1429 số lượng 50
        Service.getInstance().dropItemMap(
            this.zone,
            Util.ratiItem(zone, 1429, 30, this.location.x, this.location.y, plKill.id)
        );
    } else {
        // Nếu không rơi item 1429 thì rơi generalRewards
        generalRewards(plKill, (byte) 12, (byte) 25);
    }
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
        this.textTalkMidle = new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
            "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
            "|-1|Một mình tao chấp hết tụi bây",
            "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
        };
        this.textTalkAfter = new String[]{"|-1|Cay quá!",
            "|-1|Ta mà lại thua được sao?",
            "|-1|Hãy trả thù cho ta!"
        };
    }

    @Override
    public void leaveMap() {
        try {
            Boss together = BossManager.gI().getBossById(BossFactory.SO2_NAMEK);
            if (together == null || together.isBossDie()) {
                ChangeToAttackTogether(BossFactory.TIEU_DOI_TRUONG_NAMEK);

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
