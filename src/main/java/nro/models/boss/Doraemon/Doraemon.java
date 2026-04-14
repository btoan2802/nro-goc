package nro.models.boss.Doraemon;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author @copyright
 *
 */
public class Doraemon extends FutureBoss {

    public Doraemon() {
        super(BossFactory.DORAEMON, BossData.DORAEMON);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {

        if (!generalRewards(pl, (byte) 11, (byte) 25)) {
            baseRewards(pl, 10, 16, (byte) 5);
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
        this.textTalkBefore = new String[]{"|-1|Chóng chóng tre nè Nobita",
            "|-1|Ơ, đây là đâu thế"
        };
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        this.changeToIdle();
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            short listBossTogether[] = {BossFactory.NOBITA, BossFactory.SIZUKA,
                BossFactory.CHAIEN, BossFactory.SUNEO};
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            CreatBossTogether(zone, listBossTogether, x);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }
    }

}
