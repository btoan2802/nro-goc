package nro.models.mob;

import nro.consts.ConstMap;
import nro.consts.ConstMob;

import java.util.ArrayList;
import java.util.List;
import nro.models.map.Zone;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.player.Location;
import nro.models.player.Player;
import nro.power.CaptionManager;
import nro.services.Service;
import nro.utils.Log;
import nro.utils.Logger;
import nro.utils.Util;
import nro.services.MapService;
import nro.services.MobService;
import nro.services.TaskService;

public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;

    public long lastTimeDie;
    public int sieuquai = 0;

    public boolean actived;

    private List<Long> targetID;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.setHP(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
        this.status = 5;
        this.targetID = new ArrayList<>();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public int getSys() {
        return 0;
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    public byte status;

    // private List<Player> playerAttack = new LinkedList<>();
    protected long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.getHP() <= 0;
    }

    private long getRandomTargetID() {
        if (targetID.isEmpty()) {
            return -1;
        }
        return targetID.get(Util.nextInt(targetID.size()));
    }

    public boolean mobDontSavePlayer() {
        switch (this.id) {
            case ConstMob.HIRUDEGARN:
            case ConstMob.CO_MAY_HUY_DIET:
            case ConstMob.VUA_BACH_TUOC:
            case ConstMob.GAU_TUONG_CUOP:
            case ConstMob.ROBOT_BAO_VE:
                return true;
        }
        return false;
    }

    private void addTargetID(Player player) {
        if (targetID.contains(player.id) || mobDontSavePlayer()) {
            return;
        }
        if (targetID.size() >= 10) {
            targetID.remove(0);
        }
        targetID.add(player.id);
    }

    public void removeTargetID(Player player) {
        if (targetID.contains(player.id)) {
            targetID.remove(player.id);
        }
    }

    private void ClearTarget() {
        targetID.clear();
    }

    public synchronized void injured(Player plAtt, int damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (plAtt != null) {
                addTargetID(plAtt);
                plAtt.AddMobEnermy(this);
            }
            this.addPlayerAttack(plAtt);

            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp - 1;
                }
                if ((isMobHit10HP(this)) && damage > 10) {
                    if (plAtt.zone != null && (plAtt.zone.map.mapId < 164 || plAtt.zone.map.mapId > 166)) {
                        damage = 10;
                    }
                }
            }
            if (damage < 0) {
                damage = 1;
            }
            this.point.hp -= damage;
            if (this.isDie()) {
                MobService.gI().sendMobDieAffterAttacked(this, plAtt, damage);
                MobService.gI().dropItemTask(plAtt, this);
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                setDie(plAtt);
            } else {
                MobService.gI().sendMobStillAliveAffterAttacked(this, damage,
                        plAtt != null ? plAtt.nPoint.isCrit : false);
            }
            if (plAtt != null) {
                long tnsm_add = plAtt.nPoint.calLimit(getTiemNangForPlayer(plAtt, damage));
                Service.getInstance().addSMTN(plAtt, (byte) 2, tnsm_add, true);
            }
        }
    }

    public boolean isMobHit10HP(Mob m) {
        return m.tempId == 0 || m.tempId == 86 || m.tempId == 87 || m.tempId == 88;
    }

    public long getTiemNangForPlayer(Player pl, long dame) {
        // int levelPlayer = CaptionManager.getInstance().getLevel(pl);
        // int n = levelPlayer - this.level;
        // long pDameHit = dame * 100 / point.getHpFull();
        // long tiemNang = pDameHit * maxTiemNang / 100;
        int levelPlayer = CaptionManager.getInstance().getLevel(pl);
        int n = levelPlayer - this.level;
        long pDameHit = dame;
        long tiemNang = 0;
        if (MapService.gI().isMapDoanhTrai(pl.zone.map.mapId)) {
            tiemNang = (pDameHit * 110 * (this.pTiemNang + Util.nextInt(-2, 2)) / 150000);
        } else {
            tiemNang = (pDameHit / 11);
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                long sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                long add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        tiemNang = pl.nPoint.calSucManhTiemNang(tiemNang);
        return tiemNang;
    }

    public void update() {
        if (this.isDie()) {
            ClearTarget();
            if (!(zone instanceof ZSnakeRoad)) {
                if ((zone.map.type == ConstMap.MAP_NORMAL
                        || zone.map.type == ConstMap.MAP_OFFLINE
                        || zone.map.type == ConstMap.MAP_BLACK_BALL_WAR) && !is_not_hoi_sinh(tempId)
                        && Util.canDoWithTime(lastTimeDie, 2500)) {
                    MobService.gI().hoiSinhMob(this);
                } else if (this.zone.map.type == ConstMap.MAP_DOANH_TRAI && Util.canDoWithTime(lastTimeDie, 10000)) {
                    MobService.gI().hoiSinhMobDoanhTrai(this);
                }
            }
            return;
        }
        if (point.hp > point.maxHp) {
            point.hp = point.maxHp;
        }
        if (zone != null) {
            effectSkill.update();
            if (!zone.getPlayers().isEmpty() && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
                attackPlayer();
            }
        }
    }

    private boolean is_not_hoi_sinh(int id) {
        switch (id) {
            case ConstMob.HIRUDEGARN:
            case ConstMob.VUA_BACH_TUOC:
            case ConstMob.ROBOT_BAO_VE:
                return true;

        }
        return false;
    }

    public boolean isDa(int TempId) {
        if (TempId == 87 || TempId == 88 || TempId == 86) {
            return true;
        } else {
            return false;
        }
    }

    public void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && !isDa(tempId)) {
            Player pl = getPlayerCanAttack();
            if (pl != null) {
                int damage = MobService.gI().mobAttackPlayer(this, pl);
                int percentPST = pl.nPoint.tlPST;
                if (percentPST != 0) {
                    int damePST = pl.nPoint.calLimit(pl.nPoint.calPercent(damage, percentPST));
                    if (damePST >= this.point.hp) {
                        damePST = this.point.hp - 1;
                    }
                    if (this.point.hp > 1) {
                        this.injured(pl, damePST, false);
                    }
                }

                MobService.gI().sendMobAttackMe(this, pl, damage);
                MobService.gI().sendMobAttackPlayer(this, pl);
            }
            this.lastTimeAttackPlayer = System.currentTimeMillis();
        }
    }

    public Player getPlayerCanAttack() {
        int distance = 300;
        long idTarget = getRandomTargetID();

        Player plAttack = zone.findPlayerByMobAttack(idTarget);

        if (plAttack != null && !plAttack.isDie()) {
            // Log.warning("TIm thay player " + plAttack.id);
            int dis = Util.getDistance(plAttack, this);
            if (dis <= distance && !plAttack.isDie()) {
                return plAttack;
            } else {
                plAttack = null;
            }
        }
        if (this.level > 7) {// lv chủ động đánh người
            distance = 100;
            try {
                List<Player> players = this.zone.getNotBosses();
                for (Player pl : players) {
                    if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh && !pl.isMiniPet
                            && !pl.nPoint.buffDefenseSatellite) {
                        int dis = Util.getDistance(pl, this);
                        if (dis <= distance) {
                            plAttack = pl;
                            distance = dis;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Log.warning("Loi mob tan cong player");
            }
        }

        return plAttack;
    }

    private void addPlayerAttack(Player pl) {
    }

    public void setDie(Player plAtt) {
        this.lastTimeDie = System.currentTimeMillis();
    }
}
