package nro.models.boss.mabu_planet;

import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.consts.ConstItem;
import nro.models.map.ItemMap;
import nro.services.RewardService;
import nro.services.Service;
import nro.server.ServerNotify;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class VegataMajin extends FutureBoss {

    public VegataMajin() {
        super(BossFactory.MAJIN_VEGETA_NEW, BossData.MAJIN_VEGETA_NEW);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        if (pl != null) {
            try {
                TaskService.gI().checkDoneTaskKillBoss(pl, this);
                ItemMap itemMap1 = null;
                if (Util.isTrue(555, 100)) {
                    int level = 10;
                    int x = pl.location.x + 16;
                    if (x < 0 || x >= this.zone.map.mapWidth) {
                        return;
                    }
                    int y = pl.zone.map.yPhysicInTop(x, pl.location.y - 24);

                    byte typeTrangBi = RewardService.gI().generateTypeTrangBi();

                    short itemId = ConstItem.doSKHVip[typeTrangBi][pl.gender][level];
                    itemMap1 = new ItemMap(this.zone, itemId, 1, x, y,
                            pl.id);
                    RewardService.gI().RewardBoss(itemMap1);
                    Service.getInstance().dropItemMap(zone, itemMap1);
                }

            } catch (Exception e) {

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
        this.textTalkMidle = new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
            "|-1|Chán", "|-1|Đại ca Babyđây có nhầm không nhỉ",
            "|-1|Một mình tao chấp hết tụi bây",
            "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
        };
        this.textTalkAfter = new String[]{"|-1|Cay quá!"
        };
    }

    @Override
    public void leaveMap() {
        CreatBossLastDie(BossFactory.MABU_1, expoff);
        super.leaveMap();
        BossManager.gI().removeBoss(this);

    }

}
