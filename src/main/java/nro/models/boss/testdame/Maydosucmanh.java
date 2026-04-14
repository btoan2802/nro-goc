package nro.models.boss.testdame;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.Logger;
import nro.utils.Util;

public class Maydosucmanh extends Boss {

    public Maydosucmanh(Zone zone, int x, int y) {
        super(Util.randomBossId(), BossData.TEST_DAME);
        this.zone = zone;
        this.location.x = x;
        this.location.y = y;
        this.notNotify = true;
    }

    private long lastTimeHoiPhuc;

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (damage >= this.nPoint.hp) {
            this.recoverHP();
            return 0;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    private void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
        Service.getInstance().chat(this, "Tốt lắm");
    }

    @Override
    public void idle() {

    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(lastTimeHoiPhuc, 120000)) {
            // tự hồi phục mỗi 2p
            lastTimeHoiPhuc = System.currentTimeMillis();
            recoverHP();
        }
    }

    @Override
    public void joinMap() {
        if (this.zone != null) {
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP,
                    this.location.x);
            int y = zone.map.yPhysicInTop(this.location.x, 100);
            this.location.y = y;

        }
    }

    public void initTalk() {
        this.textTalkMidle = new String[]{"|-1|Kiểm tra sức đánh nào",
            "|-1|Hahaa", "|-1|Mại dzô"};
    }

    @Override
    public void rewards(Player pl) {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

}
