package nro.models.boss.TRAIN_BOSS;

import nro.consts.ConstRatio;
import nro.data.DataGame;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.MapService;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

public abstract class TrainBoss extends Boss {

    Player plattack;

    public TrainBoss(short bossID, BossData bossData, Zone zone, Player pl) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
        this.plattack = pl;
    }

    @Override
    public void reward(Player plKill) {
        // vật phẩm rơi khi diệt boss nhân bản
        if (plKill.ischallenge_type2) {
            plKill.typetrain++;
        }
        plKill.rsfight();
        this.chat("Hôm nay ta không được khỏe");
        this.plattack = plKill;

    }

    @Override
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl != null && !pl.isDie() && !pl.isMiniPet) {
                this.playerSkill.skillSelect = this.getSkillAttack();
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                    false);
                        } else {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 30)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                    false);
                        }
                    }
                    SkillService.gI().useSkill(this, pl, null);
                    checkPlayerDie(pl);
                } else {
                    goToPlayer(pl, false);
                }
            }
        } catch (Exception ex) {
            Log.error(Boss.class, ex);
        }
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
        }
    }

    @Override
    public void leaveMap() {
        // ChangeMapService.gI().spaceShipArrive(this, (byte) 2,
        // ChangeMapService.DEFAULT_SPACE_SHIP);
        MapService.gI().exitMap(this);
        BossManager.gI().removeBoss(this);
        this.dispose();
        this.plattack.zone.mapInfo(this.plattack);
        DataGame.updateMap(this.plattack.getSession());
    }

    @Override
    public void checkPlayerDie(Player player) {
        if (player.isDie()) {
            this.chat("Chừa nha con!!!");
        }
    }
}
