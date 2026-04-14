package nro.models.boss.mabu_planet;

import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.consts.ConstItem;
import nro.consts.ConstOption;
import nro.models.map.ItemMap;
import nro.services.RewardService;
import nro.services.Service;
import nro.server.ServerNotify;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class Mabu_6 extends FutureBoss {

    public Mabu_6() {
        super(BossFactory.MABU_6, BossData.MABU_6_NEW);
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
                if (Util.isTrue(20, 100)) {
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
                } else {

                    int x = pl.location.x + 16;
                    if (x < 0 || x >= this.zone.map.mapWidth) {
                        return;
                    }
                    int y = pl.zone.map.yPhysicInTop(x, pl.location.y - 24);

                    short itemId = 765;
                    itemMap1 = new ItemMap(this.zone, itemId, 1, x, y,
                            pl.id);
                    itemMap1.options.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(40, 50)));
                    itemMap1.options.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(40, 50)));
                    itemMap1.options.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(40, 50)));
                    itemMap1.options.add(new ItemOption(ConstOption.HSD_NGAY, 1));
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
        BossFactory.createBossAffterLeaveMap(BossFactory.DRABUBRA_NEW, true);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
