package nro.models.boss.boss_bdkb_new;

import nro.consts.ConstItem;
import nro.consts.ConstOption;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.ItemService;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @author 💖 Phong vũ 💖
 * @copyright 💖 Thưởng 💖
 *
 */
public class TrungUyXanhLoNew extends FutureBoss {

    public TrungUyXanhLoNew() {
        super(BossFactory.TRUNG_UY_XANH_LO_NEW, BossData.TRUNG_UY_XANH_LO_2);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        int x = this.location.x;
        if (x < 0 || x >= this.zone.map.mapWidth) {
            return;
        }
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        itemMap = new ItemMap(this.zone, 738, 1, x, y, pl.id);
        itemMap.options.add(new ItemOption(ConstOption.SUC_DANH_PT, 30));
        itemMap.options.add(new ItemOption(ConstOption.HP_PT, 30));
        itemMap.options.add(new ItemOption(ConstOption.KI_PT, 30));
        itemMap.options.add(new ItemOption(ConstOption.HP_PT_MOI_30_S, 20));
        itemMap.options.add(new ItemOption(ConstOption.KI_PT_MOI_30_S, 20));

        if (Util.isTrue(80, 100)) {
            itemMap.options.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(2, 5)));
        }

        if (itemMap != null) {
            Service.getInstance().dropItemMap(zone, itemMap);
        }
        if (!generalRewards(pl, (byte) 11, (byte) 15)) {
            baseRewards(pl, 2, 5, (byte) 4);
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
        textTalkMidle = new String[]{"|-1|Ta chính là đệ nhất vũ trụ cao thủ"};
        textTalkAfter = new String[]{"|-1|Để xem, hahahaha"};
    }

    @Override
    public void doneChatS() {
    }

    @Override
    public void joinMap() {
    }

    @Override
    public void leaveMap() {

        ChangeToAttackTogether(BossFactory.TRUNG_UY_THEP_NEW);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
