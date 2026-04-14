package nro.services;

import nro.consts.ConstAchive;
import nro.consts.ConstPlayer;
import nro.models.intrinsic.Intrinsic;
import nro.models.map.Zone;
import nro.models.mob.Mob;
import nro.models.mob.MobMe;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.pvp.PVP;
import nro.models.skill.Hit;
import nro.models.skill.Skill;
import nro.models.skill.SkillNotFocus;
import nro.server.io.Message;
import nro.services.func.PVPServcice;
import nro.utils.Log;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import nro.services.func.RadaService;

public class SkillService {

    private static SkillService i;

    private SkillService() {

    }

    public static SkillService gI() {
        if (i == null) {
            i = new SkillService();
        }
        return i;
    }

    public boolean useSkill(Player player, Player plTarget, Mob mobTarget) {
        try {
            if (player.playerSkill == null) {
                return false;
            }
            if (player.playerSkill.skillSelect.template.type == 2 && canUseSkillWithMana(player)
                    && canUseSkillWithCooldown(player)) {
                useSkillBuffToPlayer(player, plTarget);
                return true;
            }

            if (player.playerSkill.skillSelect.template.id == 11) {
                if (player.playerSkill.prepareLaze) {
                    if (System.currentTimeMillis() - player.playerSkill.lastTimeForturn >= 2000) {
                        player.playerSkill.plTarget = player.playerSkill.plTarget != null ? player.playerSkill.plTarget : plTarget;
                        player.playerSkill.mobTarget = player.playerSkill.mobTarget != null ? player.playerSkill.mobTarget : mobTarget;
                        // bắn laze
                        makankosapo(player, player.playerSkill.plTarget, mobTarget);
                    }
                }
            }
            if ((player.effectSkill.isHaveEffectSkill()
                    && (player.playerSkill.skillSelect.template.id != Skill.TU_SAT
                    && player.playerSkill.skillSelect.template.id != Skill.QUA_CAU_KENH_KHI
                    && player.playerSkill.skillSelect.template.id != Skill.MAKANKOSAPPO))
                    || (plTarget != null && !canAttackPlayer(player, plTarget))
                    || (mobTarget != null && mobTarget.isDie())
                    || !canUseSkillWithMana(player) || !canUseSkillWithCooldown(player)) {
                return false;
            }
            if (player.effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(player);
            }
            if (player.effectSkill.isCharging) {
                EffectSkillService.gI().stopCharge(player);
            }
            if (player.isPet) {
                // ((Pet) player).lastTimeMoveIdle = System.currentTimeMillis();
            }
            switch (player.playerSkill.skillSelect.template.type) {
                case 1:
                    useSkillAttack(player, plTarget, mobTarget);
                    // Service.getInstance().releaseCooldownSkill(player);
                    break;
                case 3:
                    useSkillAlone(player);
                    break;
                default:
                    return false;
            }
        } catch (Exception e) {
            Log.error(SkillService.class, e, "Errrrrrrr UseSkill");
        }
        return true;
    }

    public void Update(Player player) {
        // ############# BOM #############
        if (player.playerSkill.prepareTuSat
                && Util.canDoWithTime(player.playerSkill.lastTimeTuSat, 1500)) {
            // nổ
            player.playerSkill.prepareTuSat = false;
            int rangeBom = SkillUtil.getRangeBom(player.playerSkill.skillSelect.point);
            long dame = player.nPoint.hpMax;
//            if (player.setClothes.SieuVietHp == 5) {
//                dame += player.nPoint.calPercent(dame, 70);
//            }
            // if (player.setClothes.vegeta == 5) {
            // dame += player.nPoint.calPercent(dame, 100);
            // }
            if (player.nPoint.dameSKillSpecical > 0) {
                dame += (dame * player.nPoint.dameSKillSpecical / 100);
            }
            dame = player.nPoint.calLimit(dame);
            // if (player.effectSkill.isMonkey) {
            // int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
            // dame -= percent * (dame / 100);
            // }
            for (Mob mob : player.zone.mobs) {
                if (Util.getDistance(player, mob) <= rangeBom) {
                    mob.injured(player, (int) dame, true);
                }
            }
            List<Player> playersMap = null;
            if (player.isBoss) {
                playersMap = player.zone.getNotBosses();
            } else {
                playersMap = player.zone.getHumanoids();
            }
            if (!player.zone.map.isMapOffline) {
                for (Player pl : playersMap) {
                    if (!player.equals(pl) && !pl.isDie() && canAttackPlayer(player, pl)
                            && Util.getDistance(player, pl) <= rangeBom) {

                        int dmg = pl.isBoss ? (player.effectSkill.isMonkey ? (int) (dame /3) : (int) (dame / 2)) : (int) dame;

                        pl.injured(player, dmg, false, false);

                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.getInstance().Send_Info_NV(pl);
                    }
                }
            }
            affterUseSkill(player, player.playerSkill.skillSelect.template.id);
            player.injured(null, 2100000000, true, false);
            if (!player.isDie()) {
                player.injured(null, 2100000000, true, false);
                if (!player.isDie()) {
                    player.injured(null, 2100000000, true, false);
                }
            }
        }
        // ############# MAKANKOSAPOO ###############
//        if (player.playerSkill.prepareLaze && player.playerSkill.activeSkill
//                && Util.canDoWithTime(player.playerSkill.lastTimeLaze, 2000)) {
//            player.playerSkill.prepareLaze = !player.playerSkill.prepareLaze;
//            player.playerSkill.activeSkill = !player.playerSkill.activeSkill;
//            if (player.playerSkill.plTarget == null && player.playerSkill.mobTarget == null) {
//                player.playerSkill.plTarget = player.zone.getPlayerSaveSkill(player.playerSkill.getPlayerTargetId());
//            }
//            if (!player.isBoss && player.playerSkill.plTarget == null && player.playerSkill.mobTarget == null) {
//                List<Player> getBossCanAttack = new ArrayList<>();
//                for (Player boss : player.zone.getBosses()) {
//                    if (canAttackPlayer(player, boss)) {
//                        getBossCanAttack.add(boss);
//                    }
//                }
//                if (getBossCanAttack.size() > 0) {
//                    player.playerSkill.plTarget = getBossCanAttack.get(0);
//                }
//
//            }
//            if (player.playerSkill.plTarget != null && canAttackPlayer(player, player.playerSkill.plTarget)) {
//                playerAttackPlayer(player, player.playerSkill.plTarget, false);
//            }
//            if (player.playerSkill.mobTarget != null) {
//                playerAttackMob(player, player.playerSkill.mobTarget, false, true);
//                // mobTarget.attackMob(player, false, true);
//            }
//            PlayerService.gI().sendInfoHpMpMoney(player);
//        }
        // ############# QCKK #############
        if (player.playerSkill.prepareQCKK && player.playerSkill.activeSkill
                && Util.canDoWithTime(player.playerSkill.lastTimeQCKK, 3000)) {
            List<Mob> mobs;
            // ném cầu
            player.playerSkill.prepareQCKK = !player.playerSkill.prepareQCKK;
            player.playerSkill.activeSkill = !player.playerSkill.activeSkill;
            mobs = new ArrayList<>();
            if (player.playerSkill.plTarget == null && player.playerSkill.mobTarget == null) {
                player.playerSkill.plTarget = player.zone.getPlayerSaveSkill(player.playerSkill.getPlayerTargetId());
            }
            if (!player.isBoss && player.playerSkill.plTarget == null && player.playerSkill.mobTarget == null) {
                List<Player> getBossCanAttack = new ArrayList<>();
                for (Player boss : player.zone.getBosses()) {
                    if (canAttackPlayer(player, boss)) {
                        getBossCanAttack.add(boss);
                    }
                }
                if (getBossCanAttack.size() > 0) {
                    player.playerSkill.plTarget = getBossCanAttack.get(0);
                }

            }
            if (player.playerSkill.plTarget != null) {// nếu mục tiêu là người
                playerAttackPlayer(player, player.playerSkill.plTarget, false);
            }
            if (player.playerSkill.mobTarget != null) { // nếu mục tiêu là mod
                playerAttackMob(player, player.playerSkill.mobTarget, false, true);

            }
            for (Mob mob : player.zone.mobs) {
                if (!mob.equals(player.playerSkill.mobTarget) && !mob.isDie()
                        && Util.getDistance(player, mob) <= SkillUtil
                        .getRangeQCKK(player.playerSkill.skillSelect.point)) {
                    mobs.add(mob);
                }
            }
            for (Mob mob : mobs) {
                mob.injured(player, player.nPoint.getDameAttack(true), true);
            }
            List<Player> playersMap = null;
            if (player.isBoss) {
                playersMap = player.zone.getNotBosses();
            } else {
                playersMap = player.zone.getHumanoids();
            }
            if (!player.zone.map.isMapOffline) {
                for (Player pl : playersMap) {
                    if (!player.equals(pl) && canAttackPlayer(player, pl)
                            && Util.getDistance(player, pl) <= SkillUtil
                            .getRangeQCKK(player.playerSkill.skillSelect.point)) {
                        pl.injured(player, pl.nPoint.hp / 10, false, false);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.getInstance().Send_Info_NV(pl);
                    }
                }
            }
            affterUseSkill(player, player.playerSkill.skillSelect.template.id);
            PlayerService.gI().sendInfoHpMpMoney(player);
        }
    }

    public void makankosapo(Player player, Player plTarget, Mob mobTarget) {
        player.playerSkill.prepareLaze = false;
        if (plTarget != null) {
            playerAttackPlayer(player, plTarget, false);
        }
        if (mobTarget != null) {
            playerAttackMob(player, mobTarget, false, true);
            // mobTarget.attackMob(player, false, true);
        }
        affterUseSkill(player, player.playerSkill.skillSelect.template.id);
    }

    private void useSkillAttack(Player player, Player plTarget, Mob mobTarget) {
        if (!player.isBoss) {
            if (player.isPet) {
                if (player.nPoint.stamina > 0) {
                    player.nPoint.numAttack++;
                    boolean haveCharmPet = ((Pet) player).master.charms.tdDeTu > System.currentTimeMillis();
                    if (haveCharmPet ? player.nPoint.numAttack >= 5 : player.nPoint.numAttack >= 2) {
                        player.nPoint.numAttack = 0;
                        player.nPoint.stamina--;
                    }
                } else {
                    ((Pet) player).askPea();
                    return;
                }
            } else {
                if (player.nPoint.stamina > 0) {
                    if (player.charms.tdDeoDai < System.currentTimeMillis()) {
                        player.nPoint.numAttack++;
                        if (player.nPoint.numAttack == 5) {
                            player.nPoint.numAttack = 0;
                            player.nPoint.stamina--;
                            PlayerService.gI().sendCurrentStamina(player);
                        }
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Thể lực đã cạn kiệt, hãy nghỉ ngơi để lấy lại sức");
                    return;
                }
            }
        }
        List<Mob> mobs;
        boolean miss = false;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.KAIOKEN: // kaioken
                int hpUse = player.nPoint.calLimit(player.nPoint.calPercent(player.nPoint.hpMax, 10));
                if (player.nPoint.hp <= hpUse) {
                    break;
                } else {
                    player.nPoint.setHp(player.nPoint.mp - hpUse);
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    Service.getInstance().Send_Info_NV(player);
                }
            case Skill.DRAGON:
            case Skill.DEMON:
            case Skill.GALICK:
            case Skill.LIEN_HOAN:
                if (plTarget != null && Util.getDistance(player, plTarget) > Skill.RANGE_ATTACK_CHIEU_DAM) {
                    miss = true;
                }
                if (mobTarget != null && Util.getDistance(player, mobTarget) > Skill.RANGE_ATTACK_CHIEU_DAM) {
                    miss = true;
                }
            case Skill.KAMEJOKO:
            case Skill.MASENKO:
            case Skill.ANTOMIC:
                if (plTarget != null) {
                    playerAttackPlayer(player, plTarget, miss);
                }
                if (mobTarget != null) {
                    playerAttackMob(player, mobTarget, miss, false);
                }
                if (player.mobMe != null) {
                    player.mobMe.attack(plTarget, mobTarget);
                }
//                if (player.id >= 0) {
//                    player.playerTask.achivements.get(ConstAchive.NOI_CONG_CAO_CUONG).count++;
//                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            // ******************************************************************
            case Skill.QUA_CAU_KENH_KHI:
                // if (plTarget != null) {
                // Logger.warning(" Check chung Có player");
                // }

                if (!player.playerSkill.prepareQCKK) {
                    // bắt đầu tụ quả cầu
                    player.playerSkill.prepareQCKK = !player.playerSkill.prepareQCKK;
                    player.playerSkill.lastTimeQCKK = System.currentTimeMillis();
                    player.playerSkill.activeSkill = false;
                    player.playerSkill.plTarget = plTarget;
                    player.playerSkill.mobTarget = mobTarget;
                    try {
                        if (plTarget != null && !player.equals(plTarget)) {
                            player.playerSkill.setPlayerTargetId(plTarget.id);
                        }
                    } catch (Exception e) {
                        Log.warning("Loi get id qckk");
                        // TODO: handle exception
                    }

                    sendPlayerPrepareSkill(player, 4000);
                } else {
                    player.playerSkill.activeSkill = true;
                    player.playerSkill.plTarget = plTarget;
                    if (plTarget != null && !player.equals(plTarget)) {
                        player.playerSkill.setPlayerTargetId(plTarget.id);
                    }
                    player.playerSkill.mobTarget = mobTarget;

                }
                PlayerService.gI().sendInfoHpMpMoney(player);
                break;
            case Skill.MAKANKOSAPPO:
                if (!player.playerSkill.prepareLaze) {
                    // bắt đầu nạp laze
                    player.playerSkill.prepareLaze = !player.playerSkill.prepareLaze;
                    player.playerSkill.lastTimeForturn = System.currentTimeMillis();
                    player.playerSkill.plTarget = plTarget;
                    player.playerSkill.mobTarget = mobTarget;
                    sendPlayerPrepareSkill(player, 3000);
                } else {
                    player.playerSkill.plTarget = plTarget;
                    player.playerSkill.mobTarget = mobTarget;
                }
                PlayerService.gI().sendInfoHpMpMoney(player);
                break;
            case Skill.SOCOLA:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.SOCOLA);
                int timeSocola = SkillUtil.getTimeSocola();
                if (plTarget != null) {
                    EffectSkillService.gI().setSocola(plTarget, System.currentTimeMillis(), timeSocola);
                    Service.getInstance().Send_Caitrang(plTarget);
                    ItemTimeService.gI().sendItemTime(plTarget, 3780, timeSocola / 1000);
                }
                if (mobTarget != null) {
                    EffectSkillService.gI().sendMobToSocola(player, mobTarget, timeSocola);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                int timeChoangDCTT = SkillUtil.getTimeDCTT(player.playerSkill.skillSelect.point);
                if (plTarget != null) {
                    Service.getInstance().setPos(player, plTarget.location.x, plTarget.location.y);
                    playerAttackPlayer(player, plTarget, miss);
                    EffectSkillService.gI().setBlindDCTT(plTarget, System.currentTimeMillis(), timeChoangDCTT);
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                    PlayerService.gI().sendInfoHpMpMoney(plTarget);
                    ItemTimeService.gI().sendItemTime(plTarget, 3779, timeChoangDCTT / 1000);
                }
                if (mobTarget != null) {
                    Service.getInstance().setPos(player, mobTarget.location.x, mobTarget.location.y);
//                    mobTarget.attackMob(player, false, false);
                    playerAttackMob(player, mobTarget, false, false);
                    mobTarget.effectSkill.setStartBlindDCTT(System.currentTimeMillis(), timeChoangDCTT);
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                }
                player.nPoint.isCrit100 = true;
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.THOI_MIEN:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.THOI_MIEN);
                int timeSleep = SkillUtil.getTimeThoiMien(player.playerSkill.skillSelect.point);
                if (plTarget != null) {
                    EffectSkillService.gI().setThoiMien(plTarget, System.currentTimeMillis(), timeSleep);
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT,
                            EffectSkillService.SLEEP_EFFECT);
                    ItemTimeService.gI().sendItemTime(plTarget, 3782, timeSleep / 1000);
                }
                if (mobTarget != null) {
                    mobTarget.effectSkill.setThoiMien(System.currentTimeMillis(), timeSleep);
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT,
                            EffectSkillService.SLEEP_EFFECT);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TROI:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.TROI);
                int timeHold = SkillUtil.getTimeTroi(player.playerSkill.skillSelect.point);
                EffectSkillService.gI().setUseTroi(player, System.currentTimeMillis(), timeHold);
                if (plTarget != null && (!plTarget.playerSkill.prepareQCKK && !plTarget.playerSkill.prepareLaze
                        && !plTarget.playerSkill.prepareTuSat)) {
                    player.effectSkill.plAnTroi = plTarget;
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT,
                            EffectSkillService.HOLD_EFFECT);
                    EffectSkillService.gI().setAnTroi(plTarget, player, System.currentTimeMillis(), timeHold);
                }
                if (mobTarget != null) {
                    player.effectSkill.mobAnTroi = mobTarget;
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT,
                            EffectSkillService.HOLD_EFFECT);
                    mobTarget.effectSkill.setTroi(System.currentTimeMillis(), timeHold);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
        }
        if (!player.isBoss) {
            player.effectSkin.lastTimeAttack = System.currentTimeMillis();
        }
    }

    private void useSkillAlone(Player player) {
        List<Mob> mobs;
        List<Player> players;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.THAI_DUONG_HA_SAN:
                int timeStun = SkillUtil.getTimeStun(player.playerSkill.skillSelect.point);
                if (player.setClothes.thienXinHang == 5) {
                    timeStun *= 2;
                }

                mobs = new ArrayList<>();
                players = new ArrayList<>();
                if (player.zone == null || player.zone.map == null) {
    return; // Hoặc break/continue tuỳ vị trí trong hàm
}
                if (!player.zone.map.isMapOffline) {
                    List<Player> playersMap = player.zone.getHumanoids();
                    for (Player pl : playersMap) {
                        if (pl != null && !player.equals(pl)) {
                            int distance = Util.getDistance(player, pl);
                            int rangeStun = SkillUtil.getRangeStun(player.playerSkill.skillSelect.point);
                            if (distance <= rangeStun && canAttackPlayer(player, pl)) {// &&
                                // (!pl.playerSkill.prepareQCKK
                                // && !pl.playerSkill.prepareLaze
                                // &&
                                // !pl.playerSkill.prepareTuSat)
                                if (player.isPet && ((Pet) player).master.equals(pl)) {
                                    continue;
                                }
                                if (pl.nPoint.wearingKhangTDHS) {
                                    continue;
                                }
                                int timeGiamChoang = 0;
                                if (pl.nPoint.tlGiamChoang > 0) {
                                    if (pl.nPoint.tlGiamChoang >= 100) {
                                        continue;
                                    }
                                    timeGiamChoang = (int) pl.nPoint.calPercent(timeStun, pl.nPoint.tlGiamChoang);
                                }
                                EffectSkillService.gI().startStun(pl, System.currentTimeMillis(),
                                        timeStun - timeGiamChoang);

                                if (pl.typePk != ConstPlayer.NON_PK) {
                                    players.add(pl);
                                }
                            }
                        }
                    }
                }
                for (Mob mob : player.zone.mobs) {
                    if (Util.getDistance(player, mob) <= SkillUtil.getRangeStun(player.playerSkill.skillSelect.point)) {
                        mob.effectSkill.startStun(System.currentTimeMillis(), timeStun);
                        mobs.add(mob);
                    }
                }
                EffectSkillService.gI().sendEffectBlindThaiDuongHaSan(player, players, mobs, timeStun);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.DE_TRUNG:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.DE_TRUNG);
                if (player.mobMe != null) {
                    player.mobMe.mobMeDie();
                }
                player.mobMe = new MobMe(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.SAYIAN_HOA:
                EffectSkillService.gI().sendEffectSaiYan(player);
                EffectSkillService.gI().setIsSaiYan(player);
                EffectSkillService.gI().sendEffectSaiYan(player);
                Service.getInstance().sendSpeedPlayer(player, 0);
                Service.getInstance().Send_Caitrang(player);
                Service.getInstance().sendSpeedPlayer(player, -1);
                PlayerService.gI().sendInfoHpMp(player);
                Service.getInstance().point(player);
                Service.getInstance().Send_Info_NV(player);
                ItemTimeService.gI().sendAllItemTime(player);
                Service.getInstance().sendInfoPlayerEatPea(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.NAP_NANG_LUONG:
                if (player.effectSkill.isSaiYan) {
                    Skill skill = SkillUtil.getSkillbyId(player, Skill.SAYIAN_HOA);
                    if (player.effectSkill.levelSaiYan - 1 < skill.point) {
                        player.effectSkill.levelSaiYan++;
                        EffectSkillService.gI().sendEffectSaiYan(player);
                        RadaService.getInstance().setIDAuraEff(player, (byte) EffectSkillService.gI()
                                .getIdaura(player.gender, player.effectSkill.levelSaiYan));
                        Service.getInstance().sendSpeedPlayer(player, 0);
                        Service.getInstance().Send_Caitrang(player);
                        Service.getInstance().sendSpeedPlayer(player, -1);
                        PlayerService.gI().sendInfoHpMp(player);
                        Service.getInstance().point(player);
                        Service.getInstance().Send_Info_NV(player);
                        Service.getInstance().sendInfoPlayerEatPea(player);
                        affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                    } else {
                        Service.getInstance().sendThongBao(player, "Hình thái tối đa !");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Hoán hình cấp 1 trước !");
                }
                break;
            case Skill.BIEN_KHI:
                EffectSkillService.gI().sendEffectMonkey(player);
                EffectSkillService.gI().setIsMonkey(player);
                EffectSkillService.gI().sendEffectMonkey(player);

                Service.getInstance().sendSpeedPlayer(player, 0);
                Service.getInstance().Send_Caitrang(player);
                Service.getInstance().sendSpeedPlayer(player, -1);
                if (!player.isPet) {
                    PlayerService.gI().sendInfoHpMp(player);
                }
                Service.getInstance().point(player);
                Service.getInstance().Send_Info_NV(player);
                Service.getInstance().sendInfoPlayerEatPea(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.KHIEN_NANG_LUONG:
                EffectSkillService.gI().setStartShield(player);
                EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT,
                        EffectSkillService.SHIELD_EFFECT);
                ItemTimeService.gI().sendItemTime(player, 3784, player.effectSkill.timeShield / 1000);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.HUYT_SAO:
                int tileHP = SkillUtil.getPercentHPHuytSao(player.playerSkill.skillSelect.point);
                if (player.zone != null) {
                    if (!player.zone.map.isMapOffline) {
                        List<Player> playersMap = player.zone.getHumanoids();
                        for (Player pl : playersMap) {
                            if (pl.effectSkill.useTroi) {
                                EffectSkillService.gI().removeUseTroi(pl);
                            }
                            if (!pl.isBoss && pl.gender != ConstPlayer.NAMEC
                                    && player.cFlag == pl.cFlag) {
                                EffectSkillService.gI().setStartHuytSao(pl, tileHP);
                                EffectSkillService.gI().sendEffectPlayer(pl, pl, EffectSkillService.TURN_ON_EFFECT,
                                        EffectSkillService.HUYT_SAO_EFFECT);
                                pl.nPoint.calPoint();
                                long getHp = (long) pl.nPoint.hp + pl.nPoint.calPercent(pl.nPoint.hp, tileHP);
                                pl.nPoint.setHp(
                                        pl.nPoint.calLimit(getHp));
                                Service.getInstance().point(pl);
                                Service.getInstance().Send_Info_NV(pl);
                                ItemTimeService.gI().sendItemTime(pl, 3781, 30);
                                PlayerService.gI().sendInfoHpMp(pl);
                            }

                        }
                    } else {
                        EffectSkillService.gI().setStartHuytSao(player, tileHP);
                        EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT,
                                EffectSkillService.HUYT_SAO_EFFECT);
                        player.nPoint.calPoint();
                        long getHp = (long) player.nPoint.hp + player.nPoint.calPercent(player.nPoint.hp, tileHP);
                        player.nPoint.setHp(
                                player.nPoint.calLimit(getHp));
                        Service.getInstance().point(player);
                        Service.getInstance().Send_Info_NV(player);
                        ItemTimeService.gI().sendItemTime(player, 3781, 30);
                        PlayerService.gI().sendInfoHpMp(player);
                    }
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TAI_TAO_NANG_LUONG:
                EffectSkillService.gI().startCharge(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TU_SAT:
                if (!player.playerSkill.prepareTuSat) {
                    // gồng tự sát
                    player.playerSkill.prepareTuSat = true;
                    player.playerSkill.lastTimeTuSat = System.currentTimeMillis();
                    sendPlayerPrepareBom(player, 2000);
                } else {
                    // // nổ
                    // player.playerSkill.prepareTuSat = false;
                    // int rangeBom = SkillUtil.getRangeBom(player.playerSkill.skillSelect.point);
                    // int dame = player.nPoint.hpMax;
                    // if (player.setClothes.SieuVietHp == 5) {
                    // dame += (int) player.nPoint.calPercent(dame, 70);
                    // }
                    // if (player.nPoint.dameSKillSpecical > 0) {
                    // dame += (int) ((long) (dame * player.nPoint.dameSKillSpecical) / 100);
                    // }
                    // // if (player.effectSkill.isMonkey) {
                    // // int percent =
                    // SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                    // // dame -= percent * (dame / 100);
                    // // }
                    // for (Mob mob : player.zone.mobs) {
                    // if (Util.getDistance(player, mob) <= rangeBom) {
                    // mob.injured(player, dame, true);
                    // }
                    // }
                    // List<Player> playersMap = null;
                    // if (player.isBoss) {
                    // playersMap = player.zone.getNotBosses();
                    // } else {
                    // playersMap = player.zone.getHumanoids();
                    // }
                    // if (!player.zone.map.isMapOffline) {
                    // for (Player pl : playersMap) {
                    // if (!player.equals(pl) && canAttackPlayer(player, pl)
                    // && Util.getDistance(player, pl) <= rangeBom) {
                    // pl.injured(player, pl.isBoss ? dame / 2 : dame, false, false);
                    // PlayerService.gI().sendInfoHpMpMoney(pl);
                    // Service.getInstance().Send_Info_NV(pl);
                    // }
                    // }
                    // }
                    // affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                    // player.injured(null, 2100000000, true, false);
                }
                break;
        }
        if (player.playerTask.achivements.size() > 0) {
            player.playerTask.achivements.get(ConstAchive.KY_NANG_THANH_THAO).count++;
        }
    }

    private void useSkillBuffToPlayer(Player player, Player plTarget) {
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.TRI_THUONG:
                List<Player> players = new ArrayList();
                int percentTriThuong = SkillUtil.getPercentTriThuong(player.playerSkill.skillSelect.point);
                int point = player.playerSkill.skillSelect.point;
                if (canHsPlayer(player, plTarget)) {
                    players.add(plTarget);
                    List<Player> playersMap = player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!pl.equals(plTarget)) {
                            if (canHsPlayer(player, plTarget) && Util.getDistance(player, pl) <= 300) {
                                players.add(pl);
                            }
                        }
                    }
                    playerAttackPlayer(player, plTarget, false);
                    for (Player pl : players) {
                        boolean isDie = pl.isDie();
                        int hpHoi = pl.nPoint.calLimit(pl.nPoint.calPercent(pl.nPoint.hpMax, percentTriThuong));
                        int mpHoi = pl.nPoint.calLimit(pl.nPoint.calPercent(pl.nPoint.mpMax, percentTriThuong));
                        pl.nPoint.addHp(hpHoi);
                        pl.nPoint.addMp(mpHoi);
                        if (isDie) {
                            Service.getInstance().hsChar(pl, hpHoi, mpHoi);
                            PlayerService.gI().sendInfoHpMp(pl);
                        } else {
                            Service.getInstance().Send_Info_NV(pl);
                            PlayerService.gI().sendInfoHpMp(pl);
                        }
                    }
                    int hpHoiMe = player.nPoint.calLimit(player.nPoint.calPercent(player.nPoint.hp, percentTriThuong));
                    player.nPoint.addHp(hpHoiMe);
                    PlayerService.gI().sendInfoHp(player);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
        }
    }

    private void phanSatThuong(Player plAtt, Player plTarget, int dame) {
        int percentPST = plTarget.nPoint.tlPST;
        if (percentPST != 0) {
            int damePST = plAtt.nPoint.calLimit(plAtt.nPoint.calPercent(dame, percentPST));
            Message msg;
            try {
                msg = new Message(56);
                msg.writer().writeInt((int) plAtt.id);
                if (damePST >= plAtt.nPoint.hp) {
                    damePST = plAtt.nPoint.hp - 1;
                }
                damePST = plAtt.injured(null, damePST, true, false);
                msg.writer().writeInt(plAtt.nPoint.hp);
                msg.writer().writeInt(damePST);
                msg.writer().writeBoolean(false);
                msg.writer().writeByte(36);
                Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(SkillService.class, e);
            }
        }
    }

    private void hutHPMP(Player player, int dame, boolean attackMob) {
        int tiLeHutHp = player.nPoint.getTileHutHp(attackMob);
        int tiLeHutMp = player.nPoint.getTiLeHutMp();
        long hpHoi = player.nPoint.calPercent(dame, tiLeHutHp);
        long mpHoi = player.nPoint.calPercent(dame, tiLeHutMp);
        if (hpHoi > 0 || mpHoi > 0) {
            PlayerService.gI().hoiPhuc(player, player.nPoint.calLimit(hpHoi), player.nPoint.calLimit(mpHoi));
        }
    }

    private void playerAttackPlayer(Player plAtt, Player plInjure, boolean miss) {
        if (plInjure.effectSkill.anTroi) {
            plAtt.nPoint.isCrit100 = true;
        }
        if (plAtt.lastTimeBan2 > 0
                && !Util.canDoWithTime(plAtt.lastTimeBan2, 86400000)) {
            Service.getInstance().sendThongBaoOK(plAtt,
                    "Tài khoản của bạn đang tạm khóa 24h do sử dụng tool, bạn không thể sử dụng kỹ năng gây sát thương trong lúc này");
            miss = true;
        }
        if (plAtt.nPoint.isHaDocDoiThu && Util.isTrue(1, 10)) {
            EffectSkillService.gI().startDoc(plInjure);

        }
        if (plAtt.nPoint.wearingCarrot && plInjure.effectSkin != null) {
            EffSkinService.gI().setCarrot(plInjure);
        }
        long getDame = plAtt.nPoint.getDameAttack(false);
        if (plInjure.isBoss && plAtt.nPoint.tlDameBoss > 0) {
            getDame += plAtt.nPoint.calPercent(getDame, plAtt.nPoint.tlDameBoss);
        }
//        int dameHit = plInjure.injured(plAtt, miss ? 0 : plAtt.nPoint.calLimit(getDame), false, false);
        int dameHit = plInjure.injured(plAtt, plAtt.nPoint.calLimit(getDame), false, false);

        phanSatThuong(plAtt, plInjure, dameHit);
        hutHPMP(plAtt, dameHit, false);
        Service.getInstance().sendThongBaoTopDame(plAtt, dameHit);
        SendDame(plAtt, plInjure, dameHit);
//        long tnsmNhan = plAtt.nPoint.calPercent(dameHit, 5);
//        if (tnsmNhan > 100_000) {
//            tnsmNhan = 100_000;
//        }
//        Service.getInstance().addSMTN(plInjure, (byte) 2,
//                tnsmNhan, false);

        if (plAtt.nPoint.tlDameHpTarger > 0 && !plInjure.isDie()
                && plInjure.nPoint.hp > plAtt.nPoint.calPercent(plInjure.nPoint.hpMax, 10)) {
            int dameHit2 = (int) plAtt.nPoint.calPercent(plInjure.nPoint.hp, plAtt.nPoint.tlDameHpTarger);
            if (plInjure.isBoss) {
                dameHit2 /= 5;
            }
//            int setDameHit = plInjure.injured(plAtt, miss ? 0 : dameHit2, false, false);
            int setDameHit = plInjure.injured(plAtt, dameHit2, false, false);

            phanSatThuong(plAtt, plInjure, setDameHit);
            hutHPMP(plAtt, setDameHit, false);
            SendDame(plAtt, plInjure, setDameHit);

        }
    }

    private void SendDame(Player plAtt, Player plInjure, int dameHit) {
        Message msg;
        try {
            msg = new Message(-60);
            msg.writer().writeInt((int) plAtt.id); // id pem
            msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId); // skill pem
            msg.writer().writeByte(1); // số người pem
            msg.writer().writeInt((int) plInjure.id); // id ăn pem
            byte typeSkill = SkillUtil.getTyleSkillAttack(plAtt.playerSkill.skillSelect);
            msg.writer().writeByte(typeSkill == 2 ? 0 : 1); // read continue
            msg.writer().writeByte(typeSkill); // type skill
            msg.writer().writeInt(dameHit); // dame ăn
            msg.writer().writeBoolean(plInjure.isDie()); // is die
            msg.writer().writeBoolean(plAtt.nPoint.isCrit); // crit
            if (typeSkill != 1) {
                Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
                msg.cleanup();
            } else {
                plInjure.sendMessage(msg);
                msg.cleanup();
                msg = new Message(-60);
                msg.writer().writeInt((int) plAtt.id); // id pem
                msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId); // skill pem
                msg.writer().writeByte(1); // số người pem
                msg.writer().writeInt((int) plInjure.id); // id ăn pem
                msg.writer().writeByte(typeSkill == 2 ? 0 : 1); // read continue
                msg.writer().writeByte(0); // type skill
                msg.writer().writeInt(dameHit); // dame ăn
                msg.writer().writeBoolean(plInjure.isDie()); // is die
                msg.writer().writeBoolean(plAtt.nPoint.isCrit); // crit
                Service.getInstance().sendMessAnotherNotMeInMap(plInjure, msg);
                msg.cleanup();
            }
            Service.getInstance().addSMTN(plInjure, (byte) 2, 1, false);
        } catch (Exception e) {
            Log.error(SkillService.class, e);
        }
    }

    private void playerAttackMob(Player plAtt, Mob mob, boolean miss, boolean dieWhenHpFull) {
        if (!mob.isDie()) {
            if (plAtt.effectSkin.isVoHinh) {
                plAtt.effectSkin.isVoHinh = false;
            }
            long dameHit = plAtt.nPoint.getDameAttack(true);

            if (plAtt.charms.tdBatTu > System.currentTimeMillis() && plAtt.nPoint.hp == 1) {
                dameHit = 0;
            }
            if (plAtt.charms.tdManhMe > System.currentTimeMillis()) {
                dameHit += (long) plAtt.nPoint.calPercent(dameHit, 50);
            }
            if (plAtt.isPet) {
                if (((Pet) plAtt).charms.tdDeTu > System.currentTimeMillis()) {
                    dameHit *= 2;
                }
                if (((Pet) plAtt).master.itemTime != null && ((Pet) plAtt).master.itemTime.isUseGroup_1_2) {
                    dameHit *= 2;
                }
            }
            // if (plAtt.zone.map.mapId == 168) {
            // if (!plAtt.isPet) {
            // miss = true;
            // }
            // if (plAtt.inventory.itemsBody != null) {
            // if (plAtt.inventory.itemsBody.get(5).isNotNullItem()) {
            // if (plAtt.inventory.itemsBody.get(5).template.id != 1320) {
            // miss = true;
            // }
            // } else {
            // miss = true;
            // }
            // }
            // }
            if (miss) {
                dameHit = 0;
            }
            dameHit = plAtt.nPoint.calLimit(dameHit);

            hutHPMP(plAtt, (int) dameHit, true);
            sendPlayerAttackMob(plAtt, mob);
            mob.injured(plAtt, (int) dameHit, dieWhenHpFull);
            if (plAtt.nPoint.tlDameHpTarger > 0 && !mob.isDie()
                    && mob.point.hp > plAtt.nPoint.calPercent(mob.point.maxHp, 10)) {
                long dameHit2 = plAtt.nPoint.calPercent(mob.point.hp, plAtt.nPoint.tlDameHpTarger);
                // hutHPMP(plAtt, (int) dameHit2, true);
                mob.injured(plAtt, (int) dameHit2, dieWhenHpFull);
            }
        }
    }

    private void sendPlayerPrepareSkill(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(4);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(affterMiliseconds);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPlayerPrepareBom(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(7);
            msg.writer().writeInt((int) player.id);
            // msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(104);
            msg.writer().writeShort(affterMiliseconds);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public boolean canUseSkillWithMana(Player player) {
        if (player.playerSkill.skillSelect != null) {
            if (player.playerSkill.skillSelect.template.id == Skill.KAIOKEN) {
                int hpUse = player.nPoint.hpMax / 100 * 10;
                if (player.nPoint.hp <= hpUse) {
                    return false;
                }
            }
            switch (player.playerSkill.skillSelect.template.manaUseType) {
                case 0:
                    if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                        return true;
                    } else {
                        return false;
                    }
                case 1:
                    int mpUse = (int) (player.nPoint.mpMax * player.playerSkill.skillSelect.manaUse / 100);
                    if (player.nPoint.mp >= mpUse) {
                        return true;
                    } else {
                        return false;
                    }
                case 2:
                    if (player.nPoint.mp > 0) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean canUseSkillWithCooldown(Player player) {
        return Util.canDoWithTime(player.playerSkill.skillSelect.lastTimeUseThisSkill,
                player.playerSkill.skillSelect.coolDown - 50);
    }

    private void affterUseSkill(Player player, int skillId) {
        Intrinsic intrinsic = player.playerIntrinsic.intrinsic;
        switch (skillId) {
            case Skill.DICH_CHUYEN_TUC_THOI:
                if (intrinsic.id == 6) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.THOI_MIEN:
                if (intrinsic.id == 7) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.SOCOLA:
                if (intrinsic.id == 14) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.TROI:
                if (intrinsic.id == 22) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
        }
        setMpAffterUseSkill(player);
        setLastTimeUseSkill(player, skillId);
    }

    private void setMpAffterUseSkill(Player player) {
        if (player.playerSkill.skillSelect != null) {
            switch (player.playerSkill.skillSelect.template.manaUseType) {
                case 0:
                    if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                        player.nPoint.setMp(player.nPoint.mp - player.playerSkill.skillSelect.manaUse);
                    }
                    break;
                case 1:
                    int mpUse = (int) (player.nPoint.mpMax * player.playerSkill.skillSelect.manaUse / 100);
                    if (player.nPoint.mp >= mpUse) {
                        player.nPoint.setMp(player.nPoint.mp - mpUse);
                    }
                    break;
                case 2:
                    player.nPoint.setMp(0);
                    break;
            }
            PlayerService.gI().sendInfoHpMpMoney(player);
        }
    }

    private void setLastTimeUseSkill(Player player, int skillId) {
        Intrinsic intrinsic = player.playerIntrinsic.intrinsic;
        int subTimeParam = 0;
        switch (skillId) {
            case Skill.LIEN_HOAN:
                if (!player.isBoss && !player.isPet) {
                    try {

                        for (Skill skill : player.playerSkill.skills) {
                            if (skill.template.id == Skill.DEMON) {
                                skill.lastTimeUseThisSkill = System.currentTimeMillis();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        Log.warning("loi skill lien hoan");
                        // TODO: handle exception

                    }
                }
                break;
            case Skill.DEMON:
                if (!player.isBoss && !player.isPet) {
                    try {
                        for (Skill skill : player.playerSkill.skills) {
                            if (skill.template.id == Skill.LIEN_HOAN) {
                                skill.lastTimeUseThisSkill = System.currentTimeMillis();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        Log.warning("loi skill demon");
                        // TODO: handle exception
                    }
                }
                break;
            case Skill.TRI_THUONG:
                if (intrinsic.id == 10) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.THAI_DUONG_HA_SAN:
                if (intrinsic.id == 3) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.QUA_CAU_KENH_KHI:
                if (intrinsic.id == 4) {
                    subTimeParam = intrinsic.param1;
                }
                if (player.nPoint.timeSKillSpecical > 0) {
                    subTimeParam += player.nPoint.timeSKillSpecical;
                }
                break;
            case Skill.KHIEN_NANG_LUONG:
                if (intrinsic.id == 5 || intrinsic.id == 15 || intrinsic.id == 20) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.MAKANKOSAPPO:
                if (intrinsic.id == 11) {
                    subTimeParam = intrinsic.param1;
                }
                if (player.nPoint.timeSKillSpecical > 0) {
                    subTimeParam += player.nPoint.timeSKillSpecical;
                }
                break;
            case Skill.DE_TRUNG:
                if (intrinsic.id == 12) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.TU_SAT:
                if (intrinsic.id == 19) {
                    subTimeParam = intrinsic.param1;
                }
                if (player.nPoint.timeSKillSpecical > 0) {
                    subTimeParam += player.nPoint.timeSKillSpecical;
                }
                break;
            case Skill.HUYT_SAO:
                if (intrinsic.id == 21) {
                    subTimeParam = intrinsic.param1;
                }
                break;
        }

        int coolDown = player.playerSkill.skillSelect.coolDown;
        player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis()
                - (player.nPoint.calPercent(coolDown, subTimeParam));
        if (subTimeParam != 0) {
            Service.getInstance().sendTimeSkill(player);
        }
    }

    private boolean canHsPlayer(Player player, Player plTarget) {
        if (plTarget == null) {
            return false;
        }
        if (plTarget.isBoss) {
            return false;
        }
        if (plTarget.typePk == ConstPlayer.PK_ALL) {
            return false;
        }
        if (plTarget.typePk == ConstPlayer.PK_PVP) {
            return false;
        }
        if (player.cFlag != 0) {
            if (plTarget.cFlag != 0 && plTarget.cFlag != player.cFlag) {
                return false;
            }
        } else if (plTarget.cFlag != 0) {
            return false;
        }
        return true;
    }

    private boolean canAttackPlayer(Player pl1, Player pl2) {

        if (pl2 != null && !pl1.isDie() && !pl2.isDie()) {
            PVP pvp = PVPServcice.gI().findPvp(pl1);
            if (pvp != null) {
                if ((pvp.player1.equals(pl1) && pvp.player2.equals(pl2))
                        || (pvp.player1.equals(pl2) && pvp.player2.equals(pl1))) {
                    return true;
                } else {
                    return false;
                }
            }
            if (pl1.typePk > 0 || pl2.typePk > 0) {
                return true;
            }
            if ((pl1.cFlag != 0 && pl2.cFlag != 0)
                    && (pl1.cFlag == 8 || pl2.cFlag == 8 || pl1.cFlag != pl2.cFlag)) {
                return true;
            }

            return false;
        } else {
            return false;
        }
    }

    private void sendPlayerAttackMob(Player plAtt, Mob mob) {
        Message msg;
        try {
            msg = new Message(54);
            msg.writer().writeInt((int) plAtt.id);
            msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId);
            msg.writer().writeByte(mob.id);
            Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
            msg.cleanup();

        } catch (Exception e) {

        }
    }

    public void selectSkill(Player player, int skillId) {
        Skill skillBefore = player.playerSkill.skillSelect;
        for (Skill skill : player.playerSkill.skills) {
            if (skill.skillId != -1 && skill.template.id == skillId) {
                player.playerSkill.skillSelect = skill;
                switch (skillBefore.template.id) {
                    case Skill.DRAGON:
                    case Skill.KAMEJOKO:
                    case Skill.DEMON:
                    case Skill.MASENKO:
                    case Skill.LIEN_HOAN:
                    case Skill.GALICK:
                    case Skill.ANTOMIC:
                        switch (skill.template.id) {
                            case Skill.DRAGON:
                            case Skill.KAMEJOKO:
                            case Skill.DEMON:
                            case Skill.MASENKO:
                            case Skill.LIEN_HOAN:
                            case Skill.GALICK:
                            case Skill.ANTOMIC:
                                // skill.lastTimeUseThisSkill = System.currentTimeMillis() + (skill.coolDown /
                                // 100);
                                break;
                        }
                        break;
                }
                break;
            }
        }
    }

    public void useSKillNotFocus(Player player, short skillID, short xPlayer, short yPlayer, byte dir, short x,
            short y) {
        try {
            if (canUseSkillWithMana(player) && canUseSkillWithCooldown(player)) {
                Skill skillSelect = player.playerSkill.skillSelect;
                if (skillSelect instanceof SkillNotFocus skill) {
                    if (player.location.x != xPlayer || player.location.y != yPlayer) {
                        return;
                    }
                    int skillRange = skill.getRange();
                    int range = xPlayer + (dir == 1 ? skillRange : -skillRange);
                    sendEffStartSkillNotFocus(player, skillID, dir, 5000, (byte) 0);
                    Util.setTimeout(() -> {
                        List<Mob> mobs = new ArrayList<>();
                        List<Player> players = new ArrayList<>();
                        Hit hit = new Hit();
                        int dameAttack = player.nPoint.getDameAttackSkillNotFocus();
                        for (Mob mob : player.zone.mobs) {
                            if (player.location.y == mob.location.y) {
                                if (dir == 1) {// phải
                                    if (mob.location.x >= xPlayer && Util.getDistanceByDir(player.location.x,
                                            mob.location.x, dir) <= skillRange) {
                                        mobs.add(mob);
                                    }
                                } else {// trái
                                    if (mob.location.x <= xPlayer && Util.getDistanceByDir(player.location.x,
                                            mob.location.x, dir) >= skillRange) {
                                        mobs.add(mob);
                                    }
                                }
                                hit.addTarget(mob.id, 0);
                            }
                        }

                        for (Player p : player.zone.getPlayers()) {
                            if (SkillService.i.canAttackPlayer(player, p)) {
                                if (Math.abs(yPlayer - player.location.y) <= 100) {
                                    if (dir == 1) {// phải
                                        if (p.location.x >= xPlayer && Util.getDistanceByDir(player.location.x,
                                                p.location.x, dir) <= skillRange) {
                                            players.add(p);
                                        }
                                    } else {// trái
                                        if (p.location.x <= xPlayer && Util.getDistanceByDir(player.location.x,
                                                p.location.x, dir) >= skillRange) {
                                            players.add(p);
                                        }
                                    }
                                    hit.addTarget((int) player.id, 1);
                                }
                            }
                        }
                        sendEffEndUseSkillNotFocus(player, skillID, range, skill.getTimeDame(), hit);
                        if (skillID == Skill.MAFUBA) {
//                            Util.setTimeout(() -> {
                            try {
                                Thread.sleep(skill.getTimePre());
                            } catch (InterruptedException e) {
                            }
                            int timeSocola = SkillUtil.getTimeSocola();
                            Zone z = player.zone;
                            for (Map.Entry<Integer, Integer> entry : hit.getTargets().entrySet()) {
                                int type = entry.getValue();
                                if (type == 0) {
                                    Mob mobTarget = z.findMobByID(entry.getKey());
                                    if (mobTarget != null) {
                                        EffectSkillService.gI().sendMobToSocola(player, mobTarget, timeSocola);
                                    }
                                } else {
                                    Player plTarget = z.findPlayerByID(entry.getKey());
                                    if (plTarget != null) {
                                        EffectSkillService.gI().setSocola(plTarget, System.currentTimeMillis(), timeSocola);
                                        Service.getInstance().Send_Caitrang(plTarget);
                                        ItemTimeService.gI().sendItemTime(plTarget, 3780, timeSocola / 1000);
                                    }
                                }
                            }
//                            }, skill.getTimePre());
                        } else {
                            for (int i = 0; i < 10; i++) {
                                if (i == 9) {
                                    hit.addHit((dameAttack + (dameAttack / 2)));
                                } else {
                                    hit.addHit(dameAttack);
                                }
                            }
//                            dealDamageSkillNotFocus(player, players, mobs, hit);
                        }
                    }, skill.getTimePre());
                }
            }
        } catch (Exception e) {
            Log.error(SkillService.class, e);
        }
    }

    public int TimeDelayUseSkill(Player player) {
        int timeDelay = 0;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.MAKANKOSAPPO:
                timeDelay = Util.nextInt(25, 35);
                break;
            case Skill.QUA_CAU_KENH_KHI:
                timeDelay = Util.nextInt(30, 40);
                break;
            case Skill.TU_SAT:
                timeDelay = Util.nextInt(20, 30);
                break;
            case Skill.TROI:
                timeDelay = SkillUtil.getTimeTroi(player.playerSkill.skillSelect.point);
                break;
        }

        return timeDelay * 100;
    }

    private void sendEffStartSkillNotFocus(Player player, short skillID, byte dir, int timePre, byte isFly) {
        try {
            Message m = new Message(-45);
            DataOutputStream ds = m.writer();
            ds.writeByte(20);
            ds.writeInt((int) player.id);
            ds.writeShort(skillID);
            ds.writeByte(player.gender + 1);// typeFrame
            ds.writeByte(dir);
            ds.writeShort(timePre);
            ds.writeByte(isFly);// isfly
            ds.writeByte(player.gender);// typepaint
            ds.writeByte(0);// typeItem
            ds.flush();
            Service.getInstance().sendMessAllPlayerInMap(player.zone, m);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEffEndUseSkillNotFocus(Player player, short skillID, int x, int time, Hit hits) {
        Message m = new Message(-45);
        DataOutputStream ds = m.writer();
        try {
            ds.writeByte(21);
            ds.writeInt((int) player.id);
            ds.writeShort(skillID);
            ds.writeShort(x);
            ds.writeShort(player.location.y);
            ds.writeShort(time);
            ds.writeShort(player.location.y);

            ds.writeByte(player.gender);// type paint
            Map<Integer, Integer> targets = hits.getTargets();
            ds.writeByte(targets.size());
            for (Map.Entry<Integer, Integer> entry : targets.entrySet()) {
                int type = entry.getValue();
                ds.writeByte(type);
                if (type == 0) {
                    ds.writeByte(entry.getKey());
                } else {
                    ds.writeInt(entry.getKey());
                }
            }

            ds.writeByte(0);// type item
            ds.flush();
            Service.getInstance().sendMessAllPlayerInMap(player.zone, m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dealDamageSkillNotFocus(Player player, List<Player> players, List<Mob> mobs, Hit hit) {
        List<Integer> hits = hit.getHits();
        final int maxHit = hits.size();
        final int[] damageCount = {0};
        Timer timer = player.playerSkill.timer;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                int damage = hits.get(damageCount[0]);
                damageCount[0]++;
                for (Player p : players) {
                    p.injured(player, damage, false, false);
                }
                for (Mob mob : mobs) {
                    mob.injured(player, damage, false);
                }
                if (damageCount[0] >= maxHit) {
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                    cancel();
                }
            }
        }, 0, 500);
    }
}
