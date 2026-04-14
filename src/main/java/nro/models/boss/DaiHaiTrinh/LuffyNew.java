package nro.models.boss.DaiHaiTrinh;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
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
 * @author @copyright
 *
 */
public class LuffyNew extends FutureBoss {

    public LuffyNew() {
        super(BossFactory.LUFFY_NEW, BossData.LUFFY_NEW);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        try {
            ItemMap itemMap = null;
            int x = this.location.x + 14;
            if (x < 0 || x >= this.zone.map.mapWidth) {
                return;
            }
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

            itemMap = new ItemMap(this.zone, 618, 1, x, y, pl.id);
            ItemMap itemMap2 = new ItemMap(this.zone, 457, Util.nextInt(1, 5), x, y, pl.id);
            if (itemMap2 != null) {
                Service.getInstance().dropItemMap(zone, itemMap);
            }
            if (itemMap != null) {
                ItemService.gI().SysOptionItemMap(itemMap, 100);
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
        this.textTalkBefore = new String[]{"|-1|Các ngươi muốn tìm 673.000 tỷ??",
            "|-1|Vậy thì hãy bước qua xác của đồng đội ta trước đã"
        };
        this.textTalkMidle = new String[]{
            "|-1|Gomu gomu no... pistal",
            "|-1|Gomu gomu no... ",
            "|-1|Gomu Gomu no Gatling",
            "|-1|Gomu Gomu no Bazooka",
            "|-1|Ta sẽ trở thành vua hải tặc",
            "|-1|Chỉ cần tay chân ta còn cử động được thì ta vô địch!",
            "|-1|Bạn bè của ta… dù ta chết… cũng đừng hòng cướp đi bất cứ người nào!!!",
            "|-1|Thế giới này chỉ cần có một vua hải tặc thôi!"
        };
        this.textTalkAfter = new String[]{"|-1|Ta sẽ trở thành vua hải tặc!"
        };
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        this.changeToIdle();
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            short listBossTogether[] = {BossFactory.ZORO_NEW, BossFactory.SANJI_NEW,
                BossFactory.CHOPPER_NEW, BossFactory.FRANKY_NEW, BossFactory.ROBIN_NEW,
                BossFactory.NAMI_NEW, BossFactory.BROOK_NEW, BossFactory.USOPP_NEW};
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            CreatBossTogether(zone, listBossTogether, x);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }
    }

}
