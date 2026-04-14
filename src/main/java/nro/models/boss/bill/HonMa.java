package nro.models.boss.bill;

import nro.consts.ConstItem;
import nro.consts.ConstOption;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.ItemService;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @author 💖 Phong vũ 💖
 * @copyright 💖 Thưởng 💖
 *
 */
public class HonMa extends FutureBoss {

    public HonMa() {
        super(BossFactory.HON_MA, BossData.HON_MA);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {

        if (!generalRewards(pl, (byte) 11, (byte) 35)) {
            try {
                ItemMap itemMap = null;
                int x = this.location.x;
                if (x < 0 || x >= this.zone.map.mapWidth) {
                    return;
                }
                short listItem[] = {17, 18, 19, 20, 1066, 1067, 1068, 1069, 1070};
                int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
                itemMap = new ItemMap(pl.zone, Util.randomItem(listItem), 1, x, y, pl.id);
                Service.getInstance().dropItemMap(zone, itemMap);
            } catch (Exception e) {
                // TODO: handle exception
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
    public void initTalk() {
        textTalkMidle = new String[]{"|-1|Bí ngô"};
        textTalkAfter = new String[]{"|-1|Để xem, hahahaha", "|-1|Bí ngô"};
    }

    @Override
    public void leaveMap() {
        this.setJustRestToFuture();
        super.leaveMap();
    }

}
