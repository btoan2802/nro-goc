package nro.models.boss.SuperZamasu;

import java.util.Random;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
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
public class ZamasuNew extends FutureBoss {

    public ZamasuNew() {
        super(BossFactory.ZAMASU_NEW, BossData.ZAMASU_NEW);
    }

    @Override
public void rewards(Player plKill) {
    TaskService.gI().checkDoneTaskKillBoss(plKill, this);

    int x = this.location.x;
    int y = this.location.y;
    int[] itemRare = {561, 562, 564, 566};
    int[] itemLessRare = {
        555, 556, 557, 558, 559,
        560, 563, 565, 567
    };

    int roll = Util.nextInt(100); 
    if (roll < 15) {
        this.dropItemReward(16, (int) plKill.id);
        return;
    }
    if (roll < 20) {
        int itemId = itemLessRare[Util.nextInt(itemLessRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }
    if (roll < 22) {
        int itemId = itemRare[Util.nextInt(itemRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }
    generalRewards(plKill, (byte) 12, (byte) 25);
}

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"|-1|Kia là một con người sao?",
            "|-3|Ủa tên kia là ai vậy?",
            "|-2|Lẽ nào đúng như chúng ta đã nghĩ",
            "|-1|Lũ con người không đủ tư cách để nói chuyện với ta",
            "|-2|Zamas! Tại sao chứ !",
            "|-1|Ta sẽ cho người biết sức mạnh của một vị thần là như thế nào !"
        };
        this.textTalkMidle = new String[]{"|-1|Ta là kaioshin của vũ trụ thứ 10 ",
            "|-1|Tên của ta là Zamas, ta sẽ thay đổi thế giới này",
            "|-1|Lũ con người các ngươi là những thứ ta cần loại bỏ đầu tiên",
            "|-2|Tại sao các ngươi lại nhắm tới con người bọn ta chứ?",
            "|-1|Bởi vì ta muốn thực hiện kế hoạch đưa con người về số 0 !",
            "|-1|Lần này ta không nương tay đâu!",
            "|-2|Ngươi thực sự rất mạnh. Nhưng chưa đủ thực lực đâu!!",
            "|-1|Cái gì!? Đó là điều ngu ngốc nhất ta từng nghe! Mau biến đi",
            "|-1|Hắn thực sự rất mạnh, đúng là cuộc chiến hay",
            "|-3|Không lí nào ta lại run sợ bọn con người sao"
        };
        this.textTalkAfter = new String[]{"|-1|Chỉ còn một cách duy nhất mà thôi",
            "|-1|Bông tai Porata!"};

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
        Boss SuperCheck = BossManager.gI().getBossById(BossFactory.BLACK_GOKU_NEW);
        if (SuperCheck == null || SuperCheck.isBossDie()) {
            fusionEffect();
            CreatBossLastDie(BossFactory.SUPER_ZAMASU_NEW, expoff);
        }

        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    private void removeTogether() {
        Boss black = BossManager.gI().getBossById(BossFactory.BLACK_GOKU_NEW);
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
            short listBossTogether[] = {BossFactory.BLACK_GOKU_NEW};
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            CreatBossTogether(zone, listBossTogether, x);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }

    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        int dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                notifyPlayeKill(plAtt);
                die();
                // StopDame();
            }
            return dame;
        }
    }

    public void NonPk() {
        this.changeToIdle();
    }

    @Override
    public void doneChatS() {
        this.changeToAttack();
        BossManager.gI().getBossById(BossFactory.BLACK_GOKU_NEW).changeToAttack();
    }

}
