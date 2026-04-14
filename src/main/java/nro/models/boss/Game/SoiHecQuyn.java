package nro.models.boss.Game;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class SoiHecQuyn extends Boss {

    public SoiHecQuyn() {
        super(BossFactory.HEC_QUYN_EVENT, BossData.HEC_QUYN_EVENT);

    }

    private boolean checkNhatXuong = false;
    private long lastTimeNhatXuong = 0;
    private long lastTimRestPawn;

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {

    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        int dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            if (damage > 10000) {
                damage = 10000;
            }
            if (checkNhatXuong) {
                return 10;
            }
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                // notifyPlayeKill(plAtt);
                die();
            }
            return dame;
        }
    }

    public void NhatXuong() {
        checkNhatXuong = true;
        lastTimeNhatXuong = System.currentTimeMillis();
    }

    public boolean checkNhatXuong() {
        return checkNhatXuong;
    }

    @Override
    public void attack() {
        if (isDie()) {
            super.leaveMap();

        }
        if (lastTimeNhatXuong > 0) {
            if (Util.canDoWithTime(lastTimeNhatXuong, 5000)) {
                lastTimeNhatXuong = 0;
                checkNhatXuong = false;
                super.leaveMap();
                setJustRest();
                changeStatus(DIE);
                // super.respawn();
                // setJustRestToFuture();
            }
        }
        if (Util.canDoWithTime(lastTimRestPawn, 180000)) {
            // Logger.warning("Soi hecquynh ount map");
            lastTimRestPawn = System.currentTimeMillis();
            super.leaveMap();
            setJustRest();
            changeStatus(DIE);
            // super.respawn();

        }

        try {
            Player pl = getPlayerAttack();
            if (pl != null) {
                if (!useSpecialSkill()) {
                    this.playerSkill.skillSelect = this.getSkillAttack();
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                    false);
                        }
                        SkillService.gI().useSkill(this, pl, null);
                        checkPlayerDie(pl);
                    } else {
                        goToPlayer(pl, false);
                    }
                }
            }
        } catch (Exception ex) {
            // Logger.warning(SoiHecQuynEvent.class, ex);
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
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"|-1|Khà khà", "|-1|He he", "|-1|Chết nè"};
        this.textTalkAfter = new String[]{};
    }

    @Override
    public void leaveMap() {
        // ChangeMapService.gI().spaceShipArrive(this, (byte) 2,
        // ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }

    @Override
    public void joinMap() {
        super.joinMap();
        this.lastTimRestPawn = System.currentTimeMillis();
        this.lastTimeNhatXuong = 0;
        this.checkNhatXuong = false;
    }
}
