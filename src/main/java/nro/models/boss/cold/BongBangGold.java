package nro.models.boss.cold;

import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

public class BongBangGold extends FutureBoss {

    public BongBangGold() {
        super(BossFactory.BONG_BANG_GOLD, BossData.BONG_BANG_GOLD);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        int[] tempIds1 = new int[]{20, 19, 18, 17, 16, 19, 20, 18, 16};
        for (int i = 0; i < Util.nextInt(1, 5); i++) {
            short tempId = (short) tempIds1[Util.nextInt(0, tempIds1.length - 1)];
            ItemMap itemMap = new ItemMap(this.zone, tempId, 1,
                    pl.location.x + i * 18, this.zone.map.yPhysicInTop(pl.location.x + i * 18, pl.location.y - 24),
                    pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }

    }

    @Override
    public void idle() {

    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        int dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                rewards(plAtt);
                notifyPlayeKill(plAtt);
                die();
            }
            return dame;
        }
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[]{"|-1|Ta chính là đệ nhất vũ trụ cao thủ"};
        textTalkAfter = new String[]{"|-1|Ác quỷ biến hình aaa..."};
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
    }

}
