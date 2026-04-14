package nro.models.boss.boss_ban_do_kho_bau;

import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.phoban.BanDoKhoBau;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class Luffy extends BossBanDoKhoBau {

    public Luffy(BanDoKhoBau banDoKhoBau) {
        super(BossFactory.LUFFY, BossData.LUFFY, banDoKhoBau);
    }

    @Override
    public void initTalk() {
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
    }

    @Override
    public void idle() {
    }

    @Override
    public void joinMap() {
        try {
            this.zone = this.banDoKhoBau.getMapById(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
            ChangeMapService.gI().changeMap(this, this.zone, 165, 456);
        } catch (Exception e) {

        }
    }

}
