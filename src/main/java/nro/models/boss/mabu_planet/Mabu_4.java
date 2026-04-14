package nro.models.boss.mabu_planet;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.consts.ConstItem;
import nro.models.map.ItemMap;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class Mabu_4 extends FutureBoss {

    public Mabu_4() {
        super(BossFactory.MABU_4, BossData.MABU_4_NEW);
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
                if (Util.isTrue(15, 100)) {
                    int level = 13;
                    int x = pl.location.x + 16;
                    if (x < 0 || x >= this.zone.map.mapWidth) {
                        return;
                    }
                    int y = pl.zone.map.yPhysicInTop(x, pl.location.y - 24);

                    byte typeTrangBi = 1;

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
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
            "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
            "|-1|Các ngươi không nhúc nhích được sao?",
            "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
        };
        this.textTalkAfter = new String[]{"|-1|Cay quá!",
            "|-1|Ta mà lại thua được sao?",
            "|-1|Hãy trả thù cho ta!"};
    }

    @Override
    public void leaveMap() {
        CreatBossLastDie(BossFactory.MABU_5, expoff);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
