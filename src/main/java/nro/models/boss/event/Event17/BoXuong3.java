package nro.models.boss.event.Event17;

import java.util.Random;

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
public class BoXuong3 extends Boss {

    public BoXuong3() {
        super(BossFactory.BO_XUONG_3, BossData.BO_XUONG_3);
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
    public void rewards(Player plKill) {
        int[] itemDos = new int[]{702,703,704,705,706,707,708};
        int randomDo = new Random().nextInt(itemDos.length);
        if (Util.isTrue(40, 100)) {
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            if (Util.isTrue(100, 100)) {
                this.dropItemReward(19, (int) plKill.id);
            }
        }
    }
    

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }
@Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, 1, piercing, isMobAttack);
    }
    @Override
    public void initTalk() {

        this.textTalkMidle = new String[]{
            "|-1|Cho kẹo hay bị ghẹo??",};
        this.textTalkAfter = new String[]{"|-1|Ta sẽ sớm quay lại"};
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }

}
