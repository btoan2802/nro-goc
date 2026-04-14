package nro.models.boss.TRAIN_BOSS;

import nro.consts.ConstRatio;
import nro.data.DataGame;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.services.MapService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class ThanMeoKarin extends TrainBoss {

    Player pltarget;

    public ThanMeoKarin(short bossID, BossData bossData, Zone zone, Player pl) throws Exception {
        super(BossFactory.THAN_MEO, bossData.THAN_MEO, zone, pl);
        this.pltarget = pl;
    }

    @Override
    public void rewards(Player plKill) {
        if (plKill.ischallenge_type2) {
            plKill.typetrain++;
        }
        plKill.rsfight();
        this.chat("Hôm nay ta không được khỏe");
    }

    @Override
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl != null && !pl.isDie() && !pl.isMiniPet && !pl.isPet) {
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
    public Player getPlayerAttack() throws Exception {
        if (countChangePlayerAttack < targetCountChangePlayerAttack
                && plAttack != null && plAttack.zone != null
                && plAttack.zone.equals(this.zone)) {
            if (!plAttack.isDie() && !plAttack.effectSkin.isVoHinh && !plAttack.isMiniPet & !plAttack.isPet) {
                this.countChangePlayerAttack++;
                return plAttack;
            } else {
                plAttack = null;
            }
        } else {
            this.targetCountChangePlayerAttack = Util.nextInt(10, 20);
            this.countChangePlayerAttack = 0;
            plAttack = this.zone.getRandomPlayerInMap();
            if (plAttack != null && plAttack.effectSkin.isVoHinh) {
                plAttack = null;
            }
            if (pltarget.isDie() || pltarget.zone.map.mapId != 46) {
                this.leaveMap();
            }
        }
        return plAttack;
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            ChangeMapService.gI().changeMapInYard(this, this.zone, 409);
        }
    }

    @Override
    public void leaveMap() {
        if (pltarget.zone.map.mapId != 46 && pltarget != null) {
            pltarget.rsfight();
            Service.getInstance().sendThongBao(pltarget, "Bạn đã bị xử thua vì đã bỏ chạy");
            MapService.gI().exitMap(this);
            BossManager.gI().removeBoss(this);
            this.dispose();
        } else {
            pltarget.rsfight();
            MapService.gI().exitMap(this);
            BossManager.gI().removeBoss(this);
            this.dispose();
            this.plattack.zone.mapInfo(this.plattack);
            DataGame.updateMap(this.plattack.getSession());
        }
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void idle() {
    }

    @Override
    public void initTalk() {
        textTalkMidle = new String[]{"|-1|Ta chính là đệ nhất vũ trụ cao thủ"};
    }

}
