package nro.models.boss.FideGold;

import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.ServerNotify;
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
public class BroLy_Green extends FutureBoss {

    public BroLy_Green() {
        super(BossFactory.BROLY_NEW, BossData.BROLY_NEW);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        if (!generalRewards(pl, (byte) 12, (byte) 25)) {
            baseRewards(pl, 10, 20, (byte) 3);
        }

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
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"|-1|Nộp mạng đi",
            "|-1|Gôku đã phá hủy hành tinh Xayda của bọn ta",
            "|-1|Hành tinh này sẽ trở về cát bụi",
            "|-1|Các ngươi đã khiến ta phải sống cực khổ ngoài vũ trụ",
            "|-1|Mục đích duy nhất của ta là trả thù"
        };
        this.textTalkAfter = new String[]{"|-1|Biến hình siêu Xayda huyền thoại!!!"};

    }

    @Override
    public void leaveMap() {
        CreatBossLastDie(BossFactory.BROLY_LEGEND_NEW, expoff);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void joinMap() {

    }
}
