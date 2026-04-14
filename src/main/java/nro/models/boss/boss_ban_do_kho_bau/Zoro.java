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
public class Zoro extends BossBanDoKhoBau {

    public Zoro(BanDoKhoBau banDoKhoBau) {
        super(BossFactory.ZORO, BossData.ZORO, banDoKhoBau);
    }

    @Override
    public void initTalk() {
        this.textTalkMidle = new String[]{
            "T|-1|ôi là một thợ săn hải tặc",
            "|-1|Nếu ngươi chết, ta sẽ giết ngươi!",
            "|-1|Tốt thôi! Tôi thà làm hải tặc còn hơn chết ở đây!",
            "|-1|Chỉ những người đã chịu đựng lâu, mới có thể nhìn thấy ánh sáng trong bóng tối",
            "|-1|Ngươi muốn giết ta? Ngươi còn không có thể giết ta chán nản!",
            "|-1|Nếu tôi chết ở đây, thì tôi là một người đàn ông chỉ có thể đi xa đến mức này",
            "|-1|Tôi làm mọi thứ theo cách riêng của tôi! Vì vậy, đừng có nói với tôi về nó!"
        };
    }

    @Override
    public void idle() {
    }

    @Override
    public void joinMap() {
        try {
            this.zone = this.banDoKhoBau.getMapById(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
            ChangeMapService.gI().changeMap(this, this.zone, 240, 456);
        } catch (Exception e) {

        }
    }

    @Override
    public void leaveMap() {
        if (!Manager.is_reload_boss) {
            for (BossBanDoKhoBau boss : this.banDoKhoBau.bosses) {
                if (boss.id == BossFactory.LUFFY) {
                    boss.changeToAttack();
                    break;
                }
            }
        }
        super.leaveMap();
    }

}
