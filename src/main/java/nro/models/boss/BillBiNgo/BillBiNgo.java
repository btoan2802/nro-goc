package nro.models.boss.BillBiNgo;

import nro.consts.ConstItem;
import nro.consts.ConstOption;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
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
public class BillBiNgo extends FutureBoss {

    public BillBiNgo() {
        super(BossFactory.BILL_BI_NGO, BossData.BILL_BI_NGO);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        ItemMap itemMap2 = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        itemMap = new ItemMap(this.zone, 739, 1, x, y, pl.id);
        if (Util.isTrue(1, 10)) {
            itemMap2 = new ItemMap(this.zone, 1394, 1, x, y, pl.id);
        }

        itemMap.options.add(new ItemOption(ConstOption.SUC_DANH_PT, 28));
        itemMap.options.add(new ItemOption(ConstOption.HP_PT, 30));
        itemMap.options.add(new ItemOption(ConstOption.KI_PT, 30));
        itemMap.options.add(new ItemOption(ConstOption.GIAP_PT, 20));
        itemMap.options.add(new ItemOption(ConstOption.CHI_MANG, 20));

        if (Util.isTrue(80, 100)) {
            itemMap.options.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(2, 5)));
        }

        if (itemMap != null) {
            Service.getInstance().dropItemMap(zone, itemMap);
        }
        if (itemMap2 != null) {
            Service.getInstance().dropItemMap(zone, itemMap2);
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
        textTalkMidle = new String[]{"|-1|Bí ngô"};
        textTalkAfter = new String[]{"|-1|Để xem, hahahaha", "|-1|Bí ngô"};
    }

    @Override
    public void leaveMap() {
        this.setJustRestToFuture();
        super.leaveMap();
    }

}
