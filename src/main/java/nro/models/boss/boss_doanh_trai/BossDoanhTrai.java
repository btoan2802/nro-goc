package nro.models.boss.boss_doanh_trai;

import nro.consts.ConstEvent;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.map.ItemMap;
import nro.models.map.phoban.DoanhTrai;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public abstract class BossDoanhTrai extends Boss {

    private int highestDame; // dame lớn nhất trong clan
    private int highestHp; // hp lớn nhất trong clan

    private int xHpForDame = 50; // dame gốc = highesHp / xHpForDame;
    private int xDameForHp = 50; // hp gốc = xDameForHp * highestDame;

    protected DoanhTrai doanhTrai;

    public BossDoanhTrai(short id, BossData data, DoanhTrai doanhTrai) {
        super(id, data);
        this.xHpForDame = data.dame;

        int[] arrHp = data.hp[Util.nextInt(0, data.hp.length - 1)];
        this.xDameForHp = arrHp[Util.nextInt(0, arrHp.length - 1)];
        this.doanhTrai = doanhTrai;

        this.spawn(doanhTrai.clan);
    }

    private void spawn(Clan clan) {
        switch (this.typeDame) {
            case DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN:
                for (ClanMember cm : clan.getMembers()) {
                    for (Player pl : clan.membersInGame) {
                        if (pl.id == cm.id && pl.nPoint.hpMax >= highestHp) {
                            this.highestHp = pl.nPoint.hpMax;
                        }
                    }
                }
                long maxDameBoss = this.highestHp / this.xHpForDame;
                if (maxDameBoss > 100000000) {
                    maxDameBoss = 100000000;
                }
                this.nPoint.dameg = (int) maxDameBoss;
                break;
        }
        switch (this.typeHp) {
            case HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN:
                for (ClanMember cm : clan.getMembers()) {
                    for (Player pl : clan.membersInGame) {
                        if (pl.id == cm.id && pl.nPoint.dame >= highestDame) {
                            this.highestDame = pl.nPoint.dame;
                        }
                    }
                }
                long maxHpBox = this.highestDame * this.xDameForHp;
                if (maxHpBox > 2000000000) {
                    maxHpBox = 2000000000;
                }
                this.nPoint.hpg = (int) maxHpBox;
                this.nPoint.calPoint();
                this.nPoint.setFullHpMp();
                break;
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {
        if (pl.isDie()) {
            Service.getInstance().chat(this, "Chừa chưa ranh con, nên nhớ ta là " + this.name);
        }
    }

    @Override
    public void initTalk() {

    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        int x = this.location.x;
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
        if (Util.isTrue(1, 2)) {
            int[] set1 = {17, 18, 19};
            itemMap = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 1, x, y, pl.id);
        } else {
            switch (Manager.EVENT_SEVER) {
                case ConstEvent.SU_KIEN_TRUNG_THU_2024: {// sự kiện trung thu 2024
                    itemMap = new ItemMap(this.zone, (short) Util.nextInt(1331, 1332), 1, x, y, pl.id);
                }
                break;
            }

        }

        if (itemMap != null) {
            Service.getInstance().dropItemMap(zone, itemMap);
        }
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    protected void notifyPlayeKill(Player player) {
    }
}
