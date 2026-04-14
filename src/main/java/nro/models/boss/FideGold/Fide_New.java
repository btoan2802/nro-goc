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
public class Fide_New extends FutureBoss {

    public Fide_New() {
        super(BossFactory.FIDE_NEW, BossData.FIDE_DAI_CA_3_NEW);
    }

    @Override
    public void rewards(Player pl) {
        try {
            if (!generalRewards(pl, (byte) 12, (byte) 25)) {
                baseRewards(pl, 10, 20, (byte) 3);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        BossPointEven(pl);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{
            "|-1|Ta cảm thấy nguồn sức mạnh kinh khủng đang chạy trong người ta rồi",
            "|-2|Hắn ta là...bán thần!"
        };
        this.textTalkMidle = new String[]{"|-1|Tấn công bọn chúng đi Broly",
            "|-1|Gaaaaaa",
            "|-2|Sức mạnh kinh khủng quá!!",
            "|-2|Chúng ta thua mất!!",
            "|-1|Hô hô hô",
            "|-1|Chết đi Gôku",
            "|-1|Các ngươi yếu quá đấy!",
            "|-1|Chỉ có thế thôi sao"
        };
        this.textTalkAfter = new String[]{"|-1|Biến hình!"};

    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void idle() {

    }

    @Override
    public void leaveMap() {
        CreatBossLastDie(BossFactory.FIDE_GOLD_NEW, expoff);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(2, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            short listBossTogether[] = {BossFactory.BROLY_NEW};
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            CreatBossTogether(zone, listBossTogether, x);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }

    }

    @Override
    public void doneChatS() {
        this.changeToAttack();
        BossManager.gI().getBossById(BossFactory.BROLY_NEW).changeToAttack();
    }
}
