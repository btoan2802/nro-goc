package nro.models.boss.Doraemon;

import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.func.ChangeMapService;
import nro.utils.Util;
import nro.consts.ConstOption;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.services.ItemService;
import nro.services.Service;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class Suneo extends FutureBoss {

    public Suneo() {
        super(BossFactory.SUNEO, BossData.SUNEO);
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
        this.textTalkMidle = new String[]{
            "|-1|A ha ha"
        };
    }

    @Override
    public void leaveMap() {
        ChangeToAttackTogether(BossFactory.CHAIEN);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
