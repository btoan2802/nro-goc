package nro.models.boss.broly;

import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.player.Player;
import nro.services.RewardService;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class SuperBrolyRed extends SuperBroly {

    public SuperBrolyRed() {
        super(BossFactory.SUPER_BROLY_RED, BossData.SUPER_BROLY_RED);
    }

    @Override
    public void rewards(Player pl) {
        RewardService.gI().NhanDeTu(pl);
    }
}
