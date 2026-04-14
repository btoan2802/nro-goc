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
public class Super_Broly_Green extends FutureBoss {

    public Super_Broly_Green() {
        super(BossFactory.BROLY_LEGEND_NEW, BossData.SUPER_BROLY_NEW);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {

        if (!generalRewards(pl, (byte) 12, (byte) 35)) {
            baseRewards(pl, 10, 20, (byte) 3);
        }
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
        this.textTalkBefore = new String[]{
            "|-1|Các ngươi chết chắt rồi!",
            "|-2|Hắn là.... Siêu Xayda huyền thoại!"
        };
        this.textTalkMidle = new String[]{"|-1|Các ngươi làm ta nỗi giận rồi đấy",
            "|-1|Gaaaaaa",
            "|-2|Không..thể..nào!!",
            "|-2|Tên này điên thật rồi!!",
            "|-1|Không thể tha thứ được",
            "|-1|Đây là sức mạnh vô hạn",
            "|-1|Ngươi đâu rồi... Gôku!!!",
            "|-1|Được thôi, nếu muốn chết đến vậy, ta rất vui lòng!!"
        };
        this.textTalkAfter = new String[]{"|-1|Khôngggggggg!!"};

    }

    @Override
    public void leaveMap() {
        Boss fide1 = BossManager.gI().getBossById(BossFactory.FIDE_NEW);
        Boss fide2 = BossManager.gI().getBossById(BossFactory.FIDE_GOLD_NEW);
        if ((fide1 == null && fide2 == null)
                || (fide2 != null && fide2.isBossDie())) {
            BossFactory.createBossAffterLeaveMap(BossFactory.FIDE_NEW, true);
        }

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
