package nro.models.boss.DaiHaiTrinh;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.func.ChangeMapService;
import nro.utils.Util;
import nro.consts.ConstOption;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.services.ItemService;
import nro.services.Service;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class ZoroNew extends FutureBoss {

    public ZoroNew() {
        super(BossFactory.ZORO_NEW, BossData.ZORO_NEW);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        try {
            int x = this.location.x + 14;
            if (x < 0 || x >= this.zone.map.mapWidth) {
                return;
            }
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

            itemMap = new ItemMap(this.zone, 619, 1, x, y, pl.id);

            if (itemMap != null) {
                ItemService.gI().SysOptionItemMap(itemMap, 100);
                Service.getInstance().dropItemMap(zone, itemMap);
            }
            ItemMap itemMap2 = new ItemMap(this.zone, 457, Util.nextInt(1, 5), x, y, pl.id);
            if (itemMap2 != null) {
                Service.getInstance().dropItemMap(zone, itemMap);
            }
            if (!generalRewards(pl, (byte) 12, (byte) 25)) {
                baseRewards(pl, 10, 16, (byte) 5);
            }
        } catch (Exception e) {
            // TODO: handle exception
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
        this.textTalkMidle = new String[]{
            "|-1|Tôi là một thợ săn hải tặc",
            "|-1|Nếu ngươi chết, ta sẽ giết ngươi!",
            "|-1|Tốt thôi! Tôi thà làm hải tặc còn hơn chết ở đây!",
            "|-1|Chỉ những người đã chịu đựng lâu, mới có thể nhìn thấy ánh sáng trong bóng tối",
            "|-1|Ngươi muốn giết ta? Ngươi còn không có thể giết ta chán nản!",
            "|-1|Nếu tôi chết ở đây, thì tôi là một người đàn ông chỉ có thể đi xa đến mức này",
            "|-1|Tôi làm mọi thứ theo cách riêng của tôi! Vì vậy, đừng có nói với tôi về nó!"
        };
    }

    @Override
    public void leaveMap() {
        Boss SuperCheck = BossManager.gI().getBossById(BossFactory.SANJI_NEW);
        if (SuperCheck == null || (SuperCheck != null && SuperCheck.isDie())) {
            ChangeToAttackTogether(BossFactory.LUFFY_NEW);
        }
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
