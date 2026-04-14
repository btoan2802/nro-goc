package nro.models.boss.bosstuonglai;

import nro.consts.ConstItem;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.server.ServerNotify;
import nro.services.EffectSkillService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import nro.models.boss.FutureBoss;
import nro.services.TaskService;

/**
 * @author Phong Vũ
 * @copyright Phong Vũ
 */
public class Superblackgoku extends FutureBoss {

    static final int MAX_HP = 16777080;
    private static final int DIS_ANGRY = 100;

    private static final int HP_CREATE_SUPER_1 = 1000000;
    private static final int HP_CREATE_SUPER_2 = 2000000;
    private static final int HP_CREATE_SUPER_3 = 4000000;
    private static final int HP_CREATE_SUPER_4 = 6000000;
    private static final int HP_CREATE_SUPER_5 = 7000000;
    private static final int HP_CREATE_SUPER_6 = 10000000;
    private static final int HP_CREATE_SUPER_7 = 13000000;
    private static final int HP_CREATE_SUPER_8 = 14000000;
    private static final int HP_CREATE_SUPER_9 = 15000000;
    private static final int HP_CREATE_SUPER_10 = 16000000;

    private static final byte RATIO_CREATE_SUPER_10 = 10;
    private static final byte RATIO_CREATE_SUPER_20 = 20;
    private static final byte RATIO_CREATE_SUPER_30 = 30;
    private static final byte RATIO_CREATE_SUPER_40 = 40;
    private static final byte RATIO_CREATE_SUPER_50 = 50;
    private static final byte RATIO_CREATE_SUPER_60 = 60;
    private static final byte RATIO_CREATE_SUPER_70 = 70;
    private static final byte RATIO_CREATE_SUPER_80 = 80;
    private static final byte RATIO_CREATE_SUPER_90 = 90;
    private static final byte RATIO_CREATE_SUPER_100 = 100;

    private final Map angryPlayers;
    private final List<Player> playersAttack;

    public Superblackgoku() {
        super(BossFactory.SUPERBLACKGOKU, BossData.SUPERBLACKGOKU);
        this.angryPlayers = new HashMap();
        this.playersAttack = new LinkedList<>();
    }

    protected Superblackgoku(byte id, BossData bossData) {
        super(id, bossData);
        this.angryPlayers = new HashMap();
        this.playersAttack = new LinkedList<>();
    }

    @Override
    public void initTalk() {
        this.textTalkAfter = new String[]{"|-1|Các ngươi chờ đấy, ta sẽ quay lại sau"};
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie()) {
            return 0;
        } else {
            if (plAtt != null) {
                if (Util.isTrue(10, 100)) {
                    damage = 1;
                    Service.getInstance().chat(this, "Xí hụt..");
                }
            }
            int dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
            }
            return dame;
        }
    }
    
    @Override
    public void attack() {
        try {
            if (!charge()) {
                Player pl = getPlayerAttack();
                this.playerSkill.skillSelect = this.getSkillAttack();
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    }
                    this.effectCharger();
                    try {
                        SkillService.gI().useSkill(this, pl, null);
                    } catch (Exception e) {
                        Log.error(Blackgoku.class, e);
                    }
                    checkPlayerDie(pl);
                } else {
                    goToPlayer(pl, false);
                }
                if (Util.isTrue(5, ConstRatio.PER100)) {
                    this.changeIdle();
                }
            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void idle() {
        if (this.countIdle >= this.maxIdle) {
            this.maxIdle = Util.nextInt(0, 3);
            this.countIdle = 0;
            this.changeAttack();
        } else {
            this.countIdle++;
        }
    }

    @Override
    public Player getPlayerAttack() throws Exception {
        if (countChangePlayerAttack < targetCountChangePlayerAttack
                && plAttack != null && plAttack.zone != null && plAttack.zone.equals(this.zone)
                && !plAttack.effectSkin.isVoHinh) {
            if (!plAttack.isDie()) {
                this.countChangePlayerAttack++;
                return plAttack;
            } else {
                plAttack = null;
            }
        } else {
            this.targetCountChangePlayerAttack = Util.nextInt(10, 20);
            this.countChangePlayerAttack = 0;
            plAttack = this.zone.getRandomPlayerInMap();
        }
        return plAttack;
    }

    private void addPlayerAttack(Player plAtt) {
        boolean haveInList = false;
        for (Player pl : playersAttack) {
            if (pl.equals(plAtt)) {
                haveInList = true;
                break;
            }
        }
        if (!haveInList) {
            playersAttack.add(plAtt);
            Service.getInstance().chat(this, "Mi làm ta nổi giận rồi "
                    + plAtt.name.replaceAll("$", "").replaceAll("#", ""));
        }
    }

    protected boolean charge() {
        if (this.effectSkill.isCharging && Util.isTrue(15, 100)) {
            this.effectSkill.isCharging = false;
            return false;
        }
        if (Util.isTrue(1, 20)) {
            for (Skill skill : this.playerSkill.skills) {
                if (skill.template.id == Skill.TAI_TAO_NANG_LUONG) {
                    this.playerSkill.skillSelect = skill;
                    if (this.nPoint.getCurrPercentHP() < Util.nextInt(0, 100)
                            && SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().useSkill(this, null, null)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void goToXY(int x, int y, boolean isTeleport) {
        EffectSkillService.gI().stopCharge(this);
        super.goToXY(x, y, isTeleport);
    }

    protected void effectCharger() {
        if (Util.isTrue(15, ConstRatio.PER100)) {
            EffectSkillService.gI().sendEffectCharge(this);
        }
    }

    private boolean isInListPlayersAttack(Player player) {
        for (Player pl : playersAttack) {
            if (player.equals(pl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkPlayerDie(Player pl) {
        if (pl.isDie()) {
            Service.getInstance().chat(this, "Chừa nha " + plAttack.name + " động vào ta chỉ có chết.");
            this.angryPlayers.put(pl, 0);
            this.playersAttack.remove(pl);
            this.plAttack = null;
        }
    }

    @Override
    public void joinMap() {
        this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        int x = Util.nextInt(50, this.zone.map.mapWidth - 50);
        ChangeMapService.gI().changeMap(this, this.zone, x, this.zone.map.yPhysicInTop(x, 0));
        ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName + "");
    }

    @Override
    public Zone getMapCanJoin(int mapId) {
        return super.getMapCanJoin(mapId);
    }

    @Override
    public void leaveMap() {

        Boss boss = BossManager.gI().getBossById(BossFactory.BLACKGOKU);
        if (boss != null) {
            boss.setJustRest();
        }
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void die() {
        this.secondTimeRestToNextTimeAppear = Util.nextInt(20, 30);
        super.die();
    }

    @Override
public void rewards(Player plKill) {
    TaskService.gI().checkDoneTaskKillBoss(plKill, this);

    int x = this.location.x;
    int y = this.location.y;

    // ===== HIẾM NHẤT =====
    int[] itemRare = {561, 562, 564, 566};

    // ===== ĐỠ HIẾM HƠN =====
    int[] itemLessRare = {
        555, 556, 557, 558, 559,
        560, 563, 565, 567
    };

    int roll = Util.nextInt(100); // 0 - 99

    // 10% ITEM THƯỜNG (ID 16) - LÊN ĐẦU
    if (roll < 15) {
        this.dropItemReward(16, (int) plKill.id);
        return;
    }

    // 5% ĐỠ HIẾM
    if (roll < 20) {
        int itemId = itemLessRare[Util.nextInt(itemLessRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }

    // 2% HIẾM NHẤT
    if (roll < 22) {
        int itemId = itemRare[Util.nextInt(itemRare.length)];
        Service.getInstance().dropItemMap(
            zone,
            Util.ratiItem(zone, itemId, 1, x, y, plKill.id)
        );
        return;
    }

    // ===== CÒN LẠI =====
    generalRewards(plKill, (byte) 12, (byte) 25);
}

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

}
