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
public class SuperBlackNew extends FutureBoss {

    public SuperBlackNew() {
        super(BossFactory.BLACK_GOKU_NEW, BossData.BLACK_NEW);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
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
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    public void NonPk() {
        this.changeToIdle();
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"|-1|Ta chính là người mang thân thể của Songoku",
            "|-1|Sức mạnh của ta là không có giới hạn",
            "|-1|Ta sẽ thống trị vũ trụ",
            "|-1|Để ta nói cho nghe,người Sayan sau khi hồi phục sức mạnh sẽ tăng lên rất nhiều",
            "|-2|Tại sao ngươi lại lấy thân thể của songoku chứ?"
        };
        this.textTalkAfter = new String[]{"|-1|Không xong rồi, hợp thể thôi",
            "|-2|Ngươi nói gì chứ?"};

    }

    @Override
    public void joinMap() {
    }

    @Override
    public void leaveMap() {
        ChangeToAttackTogether(BossFactory.ZAMASU_NEW);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
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
    public void doneChatS() {

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
            }
            return dame;
        }
    }

}
