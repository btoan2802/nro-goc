package nro.models.boss.DaiHaiTrinh;

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
public class RobinNew extends FutureBoss {

    public RobinNew() {
        super(BossFactory.ROBIN_NEW, BossData.ROBIN_NEW);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        try {
            int x = this.location.x + 14;
            if (x < 0 || x >= this.zone.map.mapWidth) {
                return;
            }
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

            itemMap = new ItemMap(this.zone, 626, 1, x, y, pl.id);

            if (itemMap != null) {
                ItemService.gI().SysOptionItemMap(itemMap, 100);
                Service.getInstance().dropItemMap(zone, itemMap);
            }
            ItemMap itemMap2 = new ItemMap(this.zone, 457, Util.nextInt(1, 5), x, y, pl.id);
            if (itemMap2 != null) {
                Service.getInstance().dropItemMap(zone, itemMap);
            }
            if (!generalRewards(pl, (byte) 12, (byte) 25)) {
                baseRewards(pl, 10, 16, (byte) 5);
            }
        } catch (Exception e) {
            // TODO: handle exception
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
            "|-1|Á.."
        };
    }

    @Override
    public void leaveMap() {
        ChangeToAttackTogether(BossFactory.ZORO_NEW);

        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
