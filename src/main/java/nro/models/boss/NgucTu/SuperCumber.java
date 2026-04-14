package nro.models.boss.NgucTu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import nro.models.boss.*;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Client;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 * @author 💖 Nothing 💖
 */
public class SuperCumber extends FutureBoss {

    public SuperCumber() {
        super(BossFactory.CUMBER2, BossData.CUMBER2);
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
                if (Util.isTrue(25, 70)) {
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
    if (roll < 40) {
        this.dropItemReward(16, (int) plKill.id);
        return;
    }
    if (roll < 50) {
        int itemId = itemLessRare[Util.nextInt(itemLessRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }
    if (roll < 52) {
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
        textTalkAfter = new String[]{"|-1|Ta đã giấu hết ngọc rồng rồi, các ngươi tìm vô ích hahaha"};
    }

    @Override
    public void leaveMap() {
        BossFactory.createBossAffterLeaveMap(BossFactory.CUMBER, true);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
