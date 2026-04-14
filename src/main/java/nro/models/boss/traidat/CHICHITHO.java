package nro.models.boss.traidat;

import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 * @author Phong Vũ
 * @copyright Phong Vũ
 */
public class CHICHITHO extends Boss {

    public CHICHITHO() {
        super(BossFactory.CHICHITHO, BossData.CHICHITHO);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        // Cải trang thỏ
        int[] tempIds1 = new int[]{1042};
        int[] tempIds2 = new int[]{17};
        int[] tempIds3 = new int[]{555, 556, 563, 557, 558, 565, 559, 567, 560};
        // Nhan, gang than 1/30
        int[] tempIds4 = new int[]{562, 564, 566, 561};
        int tempId = -1;
        if (Util.isTrue(1, 100)) {
            tempId = tempIds1[Util.nextInt(0, tempIds1.length - 1)];
        } else if (Util.isTrue(1, 50)) {
            tempId = tempIds4[Util.nextInt(0, tempIds4.length - 1)];
        } else if (Util.isTrue(1, 15)) {
            tempId = tempIds3[Util.nextInt(0, tempIds3.length - 1)];
        } else {
            tempId = tempIds2[Util.nextInt(0, tempIds2.length - 1)];
        }
        if (tempId != -1) {
            ItemMap itemMap = new ItemMap(this.zone, tempId, 1,
                    pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);
            if (tempId >= 2027 && tempId <= 2038) {
                itemMap.options.add(new ItemOption(74, 0));
            } else if (tempId == 1042) {
                itemMap.options.add(new ItemOption(77, Util.nextInt(22, 26)));
                itemMap.options.add(new ItemOption(103, Util.nextInt(22, 26)));
                itemMap.options.add(new ItemOption(50, Util.nextInt(22, 26)));
                itemMap.options.add(new ItemOption(117, 12));
                itemMap.options.add(new ItemOption(93, Util.nextInt(1, 3)));
            }
            RewardService.gI().initBaseOptionClothesMap(itemMap);
            Service.getInstance().dropItemMap(this.zone, itemMap);
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
            "|-1|Chán", "|-1|Ta có nhầm không nhỉ"};

    }

    @Override
    public void leaveMap() {
        BossFactory.createBossAffterLeaveMap(BossFactory.CHICHITHO, true);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
