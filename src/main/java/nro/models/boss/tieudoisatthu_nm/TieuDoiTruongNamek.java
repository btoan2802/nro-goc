package nro.models.boss.tieudoisatthu_nm;

import nro.consts.ConstOption;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author @copyright
 *
 */
public class TieuDoiTruongNamek extends FutureBoss {

    public TieuDoiTruongNamek() {
        super(BossFactory.TIEU_DOI_TRUONG_NAMEK, BossData.TIEU_DOI_TRUONG_NAMEK);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
public void rewards(Player plKill) {
    // Tỉ lệ rơi item 1429: 1/10 = 10%
    if (Util.isTrue(1, 10)) {
        // Drop item 1429 số lượng 50
        Service.getInstance().dropItemMap(
            this.zone,
            Util.ratiItem(zone, 1429, 30, this.location.x, this.location.y, plKill.id)
        );
    } else {
        // Nếu không rơi item 1429 thì rơi generalRewards
        generalRewards(plKill, (byte) 12, (byte) 25);
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
        this.textTalkMidle = new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
            "|-1|Chán", "|-1|Đại ca Fide có nhầm không nhỉ",
            "|-1|Một mình tao chấp hết tụi bây",
            "|-1|HAHAHAHA", "|-1|Chỉ là bọn con nít"
        };
        this.textTalkAfter = new String[]{"|-1|Cay quá!", "|-1|Ta sẽ sớm quay lại!"
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
            short listBossTogether[] = {BossFactory.SO4_NAMEK, BossFactory.SO3_NAMEK, BossFactory.SO2_NAMEK,
                BossFactory.SO1_NAMEK};
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            CreatBossTogether(zone, listBossTogether, x);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }
    }

}
