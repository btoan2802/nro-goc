package nro.models.boss.KamiOren;

import nro.consts.ConstItem;
import nro.models.boss.*;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.ServerNotify;
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
public class Kamioren extends FutureBoss {

    public Kamioren() {
        super(BossFactory.KAMIOREN, BossData.KAMIOREN);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie()) {
            return 0;
        } else {
            if (plAtt != null) {
                if (Util.isTrue(10, 100)) {
                    damage = 1;
                    Service.getInstance().chat(this, "Xí hụt..");
                }
            }
            int dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                rewards(plAtt);
            }
            return dame;
        }
    }
    
    @Override
    public void rewards(Player pl) {
        // TaskService.gI().checkDoneTaskKillBoss(pl, this);
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        if (Util.isTrue(18, 100)) {
            int[] set1 = {562, 564, 566, 561};
            itemMap = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 1,
                    x, y, pl.id);
            RewardService.gI().initBaseOptionClothesMap(itemMap);
        } else if (Util.isTrue(1, 15)) {
            int[] set2 = {555, 556, 563, 557, 558, 565, 559, 567, 560};
            itemMap = new ItemMap(this.zone, set2[Util.nextInt(0, set2.length - 1)], 1,
                    x, y, pl.id);
            RewardService.gI().initBaseOptionClothesMap(itemMap);
        } else if (Util.isTrue(1, 5)) {
            itemMap = new ItemMap(this.zone, 16, 1, x, y, pl.id);
        }
        if (Manager.EVENT_SEVER == 4 && itemMap == null) {
            itemMap = new ItemMap(this.zone,
                    ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0,
                            ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)], 1,
                    x, y, pl.id);
            itemMap.options.add(new ItemOption(74, 0));
        }
        if (itemMap != null) {
            Service.getInstance().dropItemMap(zone, itemMap);
        }
        generalRewards(pl, (byte) 13, (byte) 25);
        baseRewards(pl, 10, 20, (byte) 3);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"|-2|Nguy to, hắn ta hợp thể rồi!!!"};
        this.textTalkMidle = new String[]{"|-1|Các ngươi chịu thua đi",
            "|-1|Đây là bản hợp thể mạnh nhất vũ trụ",
            "|-1|Kamioren bọn ta là 1 thể",
            "|-1|Hạ màn thôi",
            "|-1|Các ngươi chịu chết đi",
            "|-2|Quái vật gì đây?",
            "|-1|Lũ các ngươi làm ta thấy đau rồi ấy haha"
        };
        this.textTalkAfter = new String[]{"|-1|Không thể nào",
            "|-1|Ta chính là bản hợp thể mạnh nhất mà!!!!"};

    }

    @Override
    public void leaveMap() {
        Boss SuperCheck = BossManager.gI().getBossById(BossFactory.OREN);
        Boss SuperCheck2 = BossManager.gI().getBossById(BossFactory.KAMI);
        if (SuperCheck != null) {
            SuperCheck.leaveMap();
            BossManager.gI().removeBoss(SuperCheck);
        }
        if (SuperCheck2 != null) {
            SuperCheck2.leaveMap();
            BossManager.gI().removeBoss(SuperCheck2);
        }
        BossFactory.createBossAffterLeaveMap(BossFactory.OREN, true);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
