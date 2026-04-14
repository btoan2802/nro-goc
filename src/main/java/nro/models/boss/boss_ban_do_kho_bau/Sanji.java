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
public class Sanji extends BossBanDoKhoBau {

    public Sanji(BanDoKhoBau banDoKhoBau) {
        super(BossFactory.SANJI, BossData.SANJI, banDoKhoBau);
    }

    @Override
    public void initTalk() {
        this.textTalkMidle = new String[]{
            "|-1|Đừng có khơi mào một trận chiến nếu ngươi không kết thúc được nó!",
            "|-1|Là đàn ông, chúng ta phải sẵn sàng tha thứ cho lời nói dối của phụ nữ!",
            "|-1|Dù có chết, tôi cũng không đánh phụ nữ!",
            "|-1|Con dao là linh hồn của người đầu bếp, không phải là thứ để các ngươi tự do múa máy như thế!",
            "|-1|Miễn là còn điều gì đó cần được bảo vệ, tôi sẽ vẫn tiếp tục chiến đấu!"
        };
    }

    @Override
    public void joinMap() {
        try {
            this.zone = this.banDoKhoBau.getMapById(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
            ChangeMapService.gI().changeMap(this, this.zone, 115, 456);
        } catch (Exception e) {

        }
    }

    @Override
    public void leaveMap() {
        if (!Manager.is_reload_boss) {
            for (BossBanDoKhoBau boss : this.banDoKhoBau.bosses) {
                if (boss.id == BossFactory.ZORO && !boss.isBossDie()) {
                    boss.changeToAttack();
                    break;
                }
            }
        }
        super.leaveMap();
    }

}
