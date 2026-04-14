package nro.models.boss.traidat;

import java.util.Random;
import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

public class POCTHO extends Boss {

    public POCTHO() {
        super(BossFactory.POCTHO, BossData.POCTHO);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player plKill) {
        int[] itemDos = new int[]{2016,1302};
        int randomDo = new Random().nextInt(itemDos.length);
        if (Util.isTrue(20, 100)) {
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            if (Util.isTrue(10, 100)) {
                this.dropItemReward(17, (int) plKill.id);
            }
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, 1, piercing, isMobAttack);
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkMidle = new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
            "|-1|Chán", "|-1|Ta có nhầm không nhỉ"};

    }
}
