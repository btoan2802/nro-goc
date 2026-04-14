package nro.models.boss.SuperZamasu;

import java.util.Random;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
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
public class SuperZamasuNew extends FutureBoss {

    public SuperZamasuNew() {
        super(BossFactory.SUPER_ZAMASU_NEW, BossData.SUPER_ZAMASU_NEW);
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
            }
            return dame;
        }
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

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"|-2|Nguy to, hắn ta hợp thể rồi!!!"};
        this.textTalkMidle = new String[]{"|-1|Ta chính là thế giới",
            "|-1|Ta chính là công lí",
            "|-1|Hãy chiêm ngưỡng vẻ đẹp của ta !Hỡi con người",
            "|-1|Sức mạnh to lớn nằm trong cơ thể bất tử",
            "|-1|Ta sẽ đem công lí tới toàn bộ vũ trụ này",
            "|-2|Ngươi cứ lải nhải hoài 2 chữ công lí vậy?",
            "|-1|Lũ các ngươi làm ta thấy đau rồi ấy haha"
        };
        this.textTalkAfter = new String[]{"|-1|Không thể nào",
            "|-1|Ta chính là vị thần của thế giới này!!!!"};

    }

    @Override
    public void leaveMap() {
        Boss SuperCheck = BossManager.gI().getBossById(BossFactory.ZAMASU_NEW);
        Boss SuperCheck2 = BossManager.gI().getBossById(BossFactory.BLACK_GOKU_NEW);
        if (SuperCheck != null) {
            SuperCheck.leaveMap();
            BossManager.gI().removeBoss(SuperCheck);
        }
        if (SuperCheck2 != null) {
            SuperCheck2.leaveMap();
            BossManager.gI().removeBoss(SuperCheck2);
        }
        Boss bl = BossManager.gI().getBossById(BossFactory.BLACKGOKU);
        if (bl != null) {
            bl.setJustRest();
        }
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
