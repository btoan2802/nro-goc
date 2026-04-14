package nro.models.boss.boss_ban_do_kho_bau;

import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.phoban.BanDoKhoBau;
import nro.server.Manager;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class Franky extends BossBanDoKhoBau {

    public Franky(BanDoKhoBau banDoKhoBau) {
        super(BossFactory.FRANKY, BossData.FRANKY, banDoKhoBau);
    }

    @Override
    public void idle() {
    }

    @Override
    public void joinMap() {
        try {
            this.zone = this.banDoKhoBau.getMapById(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
            ChangeMapService.gI().changeMap(this, this.zone, 1270, 552);
        } catch (Exception e) {

        }
    }

    @Override
    public void initTalk() {
        this.textTalkMidle = new String[]{
            "|-1|Superrrr....."
        };
    }

    @Override
    public void leaveMap() {
        if (!Manager.is_reload_boss) {
            for (BossBanDoKhoBau boss : this.banDoKhoBau.bosses) {
                if (boss.id == BossFactory.BROOK && !boss.isBossDie()) {
                    boss.changeToAttack();
                    break;
                }
            }
        }
        super.leaveMap();
    }

}
