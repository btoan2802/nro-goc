package nro.models.boss.mabu_planet;

import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;

import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.models.map.ItemMap;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class Yacon extends FutureBoss {

    public Yacon() {
        super(BossFactory.YACON_NEW, BossData.YACON_NEW);
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
        CreatBossLastDie(BossFactory.MAJIN_VEGETA_NEW, expoff);
        super.leaveMap();
        BossManager.gI().removeBoss(this);

    }

    @Override
    public void doneChatS() {

    }

    @Override
    public void joinMap() {
        if (this.zone != null) {
            if (this.zone == null) {
                this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
            }
            if (this.zone != null) {
                short listBossTogether[] = {BossFactory.DRABUBRA_NEW, BossFactory.BUIBUI_NEW};
                int x = Util.nextInt(100, zone.map.mapWidth - 100);
                CreatBossTogether(zone, listBossTogether, x);
                ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
                ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
            }
        }
    }
}
