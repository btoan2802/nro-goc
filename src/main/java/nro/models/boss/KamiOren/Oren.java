package nro.models.boss.KamiOren;

import nro.consts.ConstItem;
import nro.models.boss.*;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.ServerNotify;
import nro.server.io.Message;
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
public class Oren extends FutureBoss {

    public Oren() {
        super(BossFactory.OREN, BossData.OREN);
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
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"|-1|Kia là một con người sao?",
            "|-3|Ủa tên kia là ai vậy?",
            "|-2|Lẽ nào đúng như chúng ta đã nghĩ",
            "|-1|Ta sẽ sớm thâu tóm nơi đây",
            "|-1|Các ngươi chịu chết đi!"
        };
        this.textTalkMidle = new String[]{
            "|-1|Lũ con người các ngươi là những thứ ta cần loại bỏ đầu tiên",
            "|-1|Lần này ta không nương tay đâu!",
            "|-2|Ngươi thực sự rất mạnh. Nhưng chưa đủ thực lực đâu!!",
            "|-1|Hắn thực sự rất mạnh, đúng là cuộc chiến hay",};
        this.textTalkAfter = new String[]{"|-1|Chỉ còn một cách duy nhất mà thôi",
            "|-1|Hợp thể!"};

    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void idle() {

    }

    private void fusionEffect() {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte((byte) 6);
            msg.writer().writeInt((int) this.id);
            Service.getInstance().sendMessAllPlayerInMap(this, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    @Override
    public void leaveMap() {
        // removeTogether();
        Boss SuperCheck = BossManager.gI().getBossById(BossFactory.KAMI);
        if (SuperCheck == null || SuperCheck.isBossDie()) {
            fusionEffect();
            CreatBossLastDie(BossFactory.KAMIOREN, expoff);
        }

        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    private void removeTogether() {
        Boss black = BossManager.gI().getBossById(BossFactory.KAMI);
        if (black != null && !black.isDie()) {
            black.leaveMap();
            BossManager.gI().removeBoss(black);
        }

    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            short listBossTogether[] = {BossFactory.KAMI};
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            CreatBossTogether(zone, listBossTogether, x);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }

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

    public void NonPk() {
        this.changeToIdle();
    }

    // private void StopDame() {
    // Boss blackGK = BossManager.gI().getBossById(BossFactory.BLACK_GOKU_NEW);
    // if (blackGK != null && !blackGK.isDie()) {
    // ((SuperBlackNew) blackGK).NonPk();
    // }
    // }
    @Override
    public void doneChatS() {
        this.changeToAttack();
        Boss CheckBoss1 = BossManager.gI().getBossById(BossFactory.KAMI);
        if (CheckBoss1 != null && !CheckBoss1.isBossDie()) {
            CheckBoss1.changeToAttack();
        }
    }

}
