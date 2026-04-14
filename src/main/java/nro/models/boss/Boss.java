package nro.models.boss;

import nro.consts.ConstItem;
import nro.consts.ConstMap;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.event.Event;
import nro.lib.RandomCollection;
import nro.models.boss.cdrd.CBoss;
import nro.models.boss.iboss.BossInterface;
import nro.models.boss.mabu_war.BossMabuWar;
import nro.models.boss.nappa.Kuku;
import nro.models.boss.nappa.MapDauDinh;
import nro.models.boss.nappa.Rambo;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.server.ServerNotify;
import nro.server.SettingGame;
import nro.services.*;
import nro.services.*;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.Util;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nro.jdbc.daos.PlayerDAO;

/**
 * @Stole By Arriety
 */
public abstract class Boss extends Player implements BossInterface {

    // type dame
    public static final byte DAME_NORMAL = 0;
    public static final byte DAME_PERCENT_HP_HUND = 1;
    public static final byte DAME_PERCENT_MP_HUND = 2;
    public static final byte DAME_PERCENT_HP_THOU = 3;
    public static final byte DAME_PERCENT_MP_THOU = 4;
    public static final byte DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN = 5;

    // type hp
    public static final byte HP_NORMAL = 0;
    public static final byte HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN = 1;

    protected static final byte DO_NOTHING = 71;
    protected static final byte RESPAWN = 77;
    protected static final byte JUST_RESPAWN = 75; // khởi tạo lại, rồi chuyển sang nghỉ
    protected static final byte REST = 0; // boss chưa xuất hiện
    protected static final byte JUST_JOIN_MAP = 1; // vào map rồi chuyển sang nói chuyện lúc đầu
    protected static final byte TALK_BEFORE = 2; // chào hỏi chuyển sang trạng thái khác
    protected static final byte ATTACK = 3;
    protected static final byte IDLE = 4;
    protected static final byte DIE = 5;
    protected static final byte TALK_AFTER = 6;
    protected static final byte LEAVE_MAP = 7;

    // --------------------------------------------------------------------------
    protected BossData data;
    @Setter
    protected byte status;
    protected short[] outfit;
    protected byte typeDame;
    protected byte typeHp;
    protected int percentDame;
    protected short[] mapJoin;

    protected byte indexTalkBefore;
    protected String[] textTalkBefore;
    protected byte indexTalkAfter;
    protected String[] textTalkAfter;
    protected String[] textTalkMidle;

    protected long lastTimeTalk;
    protected int timeTalk;
    protected byte indexTalk;
    protected boolean doneTalkBefore;
    protected boolean doneTalkAffter;
    protected boolean notNotify;
    private long lastTimeRest;
    // thời gian nghỉ chuẩn bị đợt xuất hiện sau
    protected int secondTimeRestToNextTimeAppear = 1800;

    protected int maxIdle;
    protected int countIdle;

    private final List<Skill> skillsAttack;
    private final List<Skill> skillsSpecial;

    protected Player plAttack;
    protected int targetCountChangePlayerAttack;
    protected int countChangePlayerAttack;

    private long lastTimeStartLeaveMap;
    private int timeDelayLeaveMap = 2000;

    protected boolean joinMapIdle;

    private int timeAppear = 0;
    private long lastTimeUpdate;
    private int TIME_RESEND_LOCATION = 15;
    private int timeChatS;
    private int timeChatM;

    protected void changeStatus(byte status) {
        this.status = status;
    }

    public Boss(short id, BossData data) {
        super();
        this.id = id;
        this.skillsAttack = new ArrayList<>();
        this.skillsSpecial = new ArrayList<>();
        this.data = data;
        this.isBoss = true;
        this.initTalk();
        this.respawn();
        setJustRest();
        if (!(this instanceof CBoss)) {
            BossManager.gI().addBoss(this);
        }
    }

    @Override
    public void init() {
        this.name = data.name.replaceAll("%1", String.valueOf(Util.nextInt(0, 100)));
        this.gender = data.gender;
        this.typeDame = data.typeDame;
        this.typeHp = data.typeHp;
        this.nPoint.power = 1;
        this.nPoint.mpg = 1832002;
        int dame = data.dame;
        int hp = 1;
        if (data.secondsRest != -1) {
            this.secondTimeRestToNextTimeAppear = data.secondsRest;
        }

        int[] arrHp = data.hp[Util.nextInt(0, data.hp.length - 1)];
        if (arrHp.length == 1) {
            hp = arrHp[0];
        } else {
            hp = Util.nextInt(arrHp[0], arrHp[1]);
        }
        switch (this.typeHp) {
            case HP_NORMAL:
                this.nPoint.hpg = hp;
                break;
            case HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN:

                break;
        }

        switch (this.typeDame) {
            case DAME_NORMAL:
                this.nPoint.dameg = dame;
                break;
            case DAME_PERCENT_HP_HUND:
                this.percentDame = dame;
                this.nPoint.dameg = this.nPoint.hpg * dame / 100;
                break;
            case DAME_PERCENT_MP_HUND:
                this.percentDame = dame;
                this.nPoint.dameg = this.nPoint.mpg * dame / 100;
                break;
            case DAME_PERCENT_HP_THOU:
                this.percentDame = dame;
                this.nPoint.dameg = this.nPoint.hp * dame / 1000;
                break;
            case DAME_PERCENT_MP_THOU:
                this.percentDame = dame;
                this.nPoint.dameg = this.nPoint.mpg * dame / 1000;
                break;
            case DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN:

                break;
        }
        this.nPoint.calPoint();
        this.outfit = data.outfit;
        this.mapJoin = data.mapJoin;
        if (data.timeDelayLeaveMap != -1) {
            this.timeDelayLeaveMap = data.timeDelayLeaveMap;
        }
        this.joinMapIdle = data.joinMapIdle;
        initSkill();
    }

    @Override
    public int version() {
        return 214;
    }

    protected void initSkill() {
        this.playerSkill.skills.clear();
        this.skillsAttack.clear();
        this.skillsSpecial.clear();
        int[][] skillTemp = data.skillTemp;
        for (int i = 0; i < skillTemp.length; i++) {
            Skill skill = SkillUtil.createSkill(skillTemp[i][0], skillTemp[i][1]);
            skill.coolDown = skillTemp[i][2];
            this.playerSkill.skills.add(skill);
            switch (skillTemp[i][0]) {
                case Skill.DRAGON:
                case Skill.DEMON:
                case Skill.GALICK:
                case Skill.KAMEJOKO:
                case Skill.MASENKO:
                case Skill.ANTOMIC:
                    this.skillsAttack.add(skill);
                    break;
                case Skill.TAI_TAO_NANG_LUONG:
                case Skill.THAI_DUONG_HA_SAN:
                case Skill.BIEN_KHI:
                case Skill.THOI_MIEN:
                case Skill.TROI:
                case Skill.KHIEN_NANG_LUONG:
                case Skill.SOCOLA:
                case Skill.DE_TRUNG:
                    this.skillsSpecial.add(skill);
                    break;
            }
        }
    }

    @Override
    public void update() {
        super.update();
        try {
            if (!this.effectSkill.isHaveEffectSkill()
                    && !this.effectSkill.isCharging) {
                this.immortalMp();
                switch (this.status) {
                    case RESPAWN:
                        respawn();
                        break;
                    case JUST_RESPAWN:
                        this.changeStatus(REST);
                        break;
                    case REST:
                        if (Util.canDoWithTime(lastTimeRest, secondTimeRestToNextTimeAppear * 1000)) {
                            this.changeStatus(JUST_JOIN_MAP);
                        }
                        break;
                    case JUST_JOIN_MAP:
                        joinMap();
                        if (this.zone != null) {
                            changeStatus(TALK_BEFORE);
                        }
                        break;
                    case TALK_BEFORE:
                        if (talk()) {
                            if (!this.joinMapIdle) {
                                doneChatS();
                            } else {
                                this.changeStatus(IDLE);
                            }
                        }
                        break;
                    case ATTACK:
                        this.talk();
                        if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze
                                || this.playerSkill.prepareQCKK) {
                            break;
                        } else {
                            this.attack();
                        }
                        break;
                    case IDLE:
                        this.idle();
                        break;
                    case DIE:
                        if (this.joinMapIdle) {
                            this.changeToIdle();
                        }
                        changeStatus(TALK_AFTER);
                        break;
                    case TALK_AFTER:
                        if (talk()) {
                            this.doneChatA();
                            changeStatus(LEAVE_MAP);
                            this.lastTimeStartLeaveMap = System.currentTimeMillis();
                        }
                        break;
                    case LEAVE_MAP:
                        if (Util.canDoWithTime(lastTimeStartLeaveMap, timeDelayLeaveMap)) {
                            this.leaveMap();
                            this.changeStatus(RESPAWN);
                        }
                        break;
                    case DO_NOTHING:

                        break;
                }
            }
            if (Util.canDoWithTime(lastTimeUpdate, 60000)) {
                if (timeAppear >= TIME_RESEND_LOCATION) {
                    if (this.zone != null && this.notNotify && !(this instanceof BossMabuWar)) {
                        ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
                        timeAppear = 0;
                    }
                } else {
                    timeAppear++;
                }
                lastTimeUpdate = System.currentTimeMillis();
            }
        } catch (Exception e) {
            Log.error(Boss.class, e);
        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        int dame = 0;
        if (this.isDie()) {
            return dame;
        } else {

            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                try {
                    rewards(plAtt);
                    notifyPlayeKill(plAtt);
                    die();
                } catch (Exception e) {
                    die();
                }
            }
            return dame;
        }
    }

    public void CreatBossTogether(Zone zone, short listBoss[], int x) {
        if (!Manager.is_reload_boss) {
            if (zone == null) {
                return;
            }

            for (short idBoss : listBoss) {
                // Boss checkBoss = BossManager.gI().getBossById(idBoss);
                // if (checkBoss != null) {
                // if (checkBoss.isBossDie()) {
                // checkBoss.leaveMap();
                // }
                // }
                x += 25;
                if (x >= zone.map.mapWidth) {
                    x = zone.map.mapWidth - 1;
                }
                if (x < 0) {
                    x = 5;
                }
                Boss boss = BossFactory.createBoss(idBoss);
                ChangeMapService.gI().changeMapBySpaceShip(boss, zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
                ServerNotify.gI().notify("Boss " + boss.name + " vừa xuất hiện tại " + boss.zone.map.mapName);

            }
        }
    }

    public void CreatBossLastDie(short idBoss, int x) {
        if (!Manager.is_reload_boss) {
            if (zone == null) {
                return;
            }
            Boss boss = BossFactory.createBoss(idBoss);
            boss.zone = zone;
            boss.location.x = this.location.x;
            boss.location.y = this.location.y;

            ServerNotify.gI().notify("Boss " + boss.name + " vừa xuất hiện tại " + boss.zone.map.mapName);
        }

    }

    public void ChangeToAttackTogether(short idBossTogether) {
        Boss bossTogether = BossManager.gI().getBossById(idBossTogether);
        if (bossTogether != null && !bossTogether.isBossDie()) {
            bossTogether.changeToAttack();
        }
    }

    protected void notifyPlayeKill(Player player) {
        if (player != null) {
            ServerNotify.gI().notify(player.name + " vừa tiêu diệt được " + this.name + " mọi người đều ngưỡng mộ");
        }
    }

    public int injuredNotCheckDie(Player plAtt, int damage, boolean piercing) {
        if (this.isDie()) {
            return 0;
        } else {
            int dame = super.injured(plAtt, damage, piercing, false);
            return dame;
        }
    }

    protected Skill getSkillAttack() {
        return skillsAttack.get(Util.nextInt(0, skillsAttack.size() - 1));
    }

    protected Skill getSkillSpecial() {
        return skillsSpecial.get(Util.nextInt(0, skillsSpecial.size() - 1));
    }

    protected Skill getSkillById(int skillId) {
        return SkillUtil.getSkillbyId(this, skillId);
    }

    @Override
    public void die() {
        setJustRest();
        changeStatus(DIE);
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }
    }

    public Zone getMapCanJoin(int mapId) {
        try {
            Zone map = MapService.gI().getMapWithRandZone(mapId);
            if (map != null) {
                if (map.isBossCanJoin(this)) {
                    return map;
                } else {
                    return getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }

    }

    @Override
    public void leaveMap() {
        MapService.gI().exitMap(this);
    }

    @Override
    public void doneChatS() {
        changeToAttack();
    }

    @Override
    public void doneChatA() {

    }

    // private boolean chatS_old() {
    // if (this.textTalkBefore == null || this.textTalkBefore.length == 0) {
    // return true;
    // }
    // if (Util.canDoWithTime(lastTimeTalk, 5000)) {
    // if (indexTalkBefore < textTalkBefore.length) {
    // this.chat(textTalkBefore[indexTalkBefore++]);
    // if (indexTalkBefore >= textTalkBefore.length) {
    // return true;
    // }
    // lastTimeTalk = System.currentTimeMillis();
    // } else {
    // return true;
    // }
    // }
    // }
    private boolean chatOk(String textChat) {
        // Logger.warning("Boss " + this.name + " chat " + textChat);
        try {
            int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
            textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
            return this.chat(prefix, textChat); // nếu lỗi sẽ rutun false và đóng chat
        } catch (Exception r) {
            return false;
        }

    }

    @Override
    public boolean talk() {
        switch (status) {
            case TALK_BEFORE:
                if (this.textTalkBefore == null || this.textTalkBefore.length == 0) {
                    return true;
                }

                if (Util.canDoWithTime(lastTimeTalk, timeChatS)) {
                    if (indexTalkBefore < textTalkBefore.length) {
                        String textChat = textTalkBefore[indexTalkBefore];
                        if (!chatOk(textChat)) {
                            return true;
                        }
                        this.lastTimeTalk = System.currentTimeMillis();
                        this.timeChatS = textChat.length() * 100;
                        if (this.timeChatS > 2000) {
                            this.timeChatS = 2000;
                        }
                        this.indexTalkBefore++;
                    } else {
                        return true;
                    }
                }
                break;
            case IDLE:
            case ATTACK:
                if (plAttack == null || this.textTalkMidle == null || this.textTalkMidle.length == 0) {
                    return true;
                }

                if (Util.canDoWithTime(lastTimeTalk, timeChatM)) {
                    timeChatM = Util.nextInt(3000, 20000);
                    lastTimeTalk = System.currentTimeMillis();
                    String textChat = textTalkMidle[Util.nextInt(0, textTalkMidle.length - 1)];
                    chatOk(textChat);

                }
                break;
            case TALK_AFTER:
                if (this.textTalkAfter == null || this.textTalkAfter.length == 0) {
                    return true;
                }
                if (Util.canDoWithTime(lastTimeTalk, 1000)) {
                    String textChat = textTalkAfter[indexTalkAfter++];
                    if (!chatOk(textChat)) {
                        return true;
                    }
                    if (indexTalkAfter >= textTalkAfter.length) {
                        return true;
                    }
                    if (indexTalkAfter > textTalkAfter.length - 1) {
                        indexTalkAfter = 0;
                    }
                    lastTimeTalk = System.currentTimeMillis();
                }
                break;
        }
        return false;
    }

    @Override
    public void respawn() {
        this.init();
        this.indexTalkBefore = 0;
        this.indexTalkAfter = 0;
        this.nPoint.setFullHpMp();
        this.changeStatus(JUST_RESPAWN);
    }

    protected void goToPlayer(Player pl, boolean isTeleport) {
        goToXY(pl.location.x, pl.location.y, isTeleport);
    }

    protected void goToXY(int x, int y, boolean isTeleport) {
        if (!isTeleport) {
            byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
            byte move = (byte) Util.nextInt(50, 100);
            PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
        } else {
            ChangeMapService.gI().changeMapYardrat(this, this.zone, x, y);
        }
    }

    protected int getRangeCanAttackWithSkillSelect() {
        int skillId = this.playerSkill.skillSelect.template.id;
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        } else {
            return Skill.RANGE_ATTACK_CHIEU_DAM;
        }
    }

    @Override
    public Player getPlayerAttack() throws Exception {
        if (countChangePlayerAttack < targetCountChangePlayerAttack
                && plAttack != null && plAttack.zone != null
                && plAttack.zone.equals(this.zone)) {
            if (!plAttack.isDie() && !plAttack.effectSkin.isVoHinh && !plAttack.isMiniPet) {
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
        }
        return plAttack;
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

    private void immortalMp() {
        this.nPoint.mp = this.nPoint.mpg;
    }

    protected abstract boolean useSpecialSkill();

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public short getHead() {
        return this.outfit[0];
    }

    @Override
    public short getBody() {
        return this.outfit[1];
    }

    @Override
    public short getLeg() {
        return this.outfit[2];
    }

    @Override
    public short getFlagBag() {
        if (this.outfit.length > 3) {
            return this.outfit[3];
        } else {
            return -1;
        }

    }

    // status
    protected void changeIdle() {
        this.changeStatus(IDLE);
    }

    /**
     * Đổi sang trạng thái tấn công
     */
    protected void changeAttack() {
        this.changeStatus(ATTACK);
    }

    @Override
    public void setJustRest() {
        this.lastTimeRest = System.currentTimeMillis();
    }

    public void setJustRestToFuture() {
        this.lastTimeRest = System.currentTimeMillis() + 8640000000L;
    }

    @Override
    public void dropItemReward(int tempId, int playerId, int... quantity) {
        try {
            if (!this.zone.map.isMapOffline && this.zone.map.type == ConstMap.MAP_NORMAL) {
                int x = this.location.x + Util.nextInt(-30, 30);
                if (x < 30) {
                    x = 30;
                } else if (x > zone.map.mapWidth - 30) {
                    x = zone.map.mapWidth - 30;
                }
                int y = this.location.y;
                if (y > 24) {
                    y = this.zone.map.yPhysicInTop(x, y - 24);
                }
                ItemMap itemMap = new ItemMap(this.zone, tempId,
                        (quantity != null && quantity.length == 1) ? quantity[0] : 1, x, y, playerId);
                Service.getInstance().dropItemMap(itemMap.zone, itemMap);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void BossPointEven(Player player) {
        if (player != null) {
            player.bosspoint += 1;
            //PlayerDAO.addbosspoint((int) player.id, 1);
            Service.getInstance().sendThongBao(player, "Bạn nhann 1 diem boss");
        }
    }

    @Override
public boolean generalRewards(Player player, byte maxLevel, byte ratio) {
    if (player == null || player.zone == null) {
        return false;
    }

    try {
        int x = player.location.x + 16;
        if (x < 0 || x >= this.zone.map.mapWidth) {
            x = player.location.x; // fallback an toàn
        }

        int y = player.zone.map.yPhysicInTop(x, player.location.y - 24);

        // Danh sách item chắc chắn rơi
        short[] items = {381, 382, 383, 384, 880, 881, 882};

        // Random đúng 1 item
        short itemId = items[Util.nextInt(0, items.length - 1)];

        ItemMap itemMap = new ItemMap(
                this.zone,
                itemId,
                1,
                x,
                y,
                player.id
        );

        RewardService.gI().RewardBoss(itemMap);
        Service.getInstance().dropItemMap(zone, itemMap);

        return true;

    } catch (Exception e) {
        return false;
    }
}

    @Override
public void baseRewards(Player player, int min_count, int max_count, byte type) {
    if (player == null || player.zone == null) {
        return;
    }

    try {
        // ===== TỌA ĐỘ RƠI GIỐNG generalRewards1 =====
        int x = player.location.x + 16;
        if (x < 0 || x >= player.zone.map.mapWidth) {
            x = player.location.x;
        }

        int y = player.zone.map.yPhysicInTop(x, player.location.y - 24);

        // ===== ITEM LẤY TỪ generalRewards1 =====
        short[] items = {
            1066, 1067, 1068, 1069, 1070
        };

        // ===== RANDOM 1 ITEM =====
        short itemId = items[Util.nextInt(0, items.length - 1)];

        // ===== DROP CHẮC 1 MÓN =====
        ItemMap itemMap = new ItemMap(
                player.zone,
                itemId,
                1,
                x,
                y,
                player.id
        );

        RewardService.gI().RewardBoss(itemMap);
        Service.getInstance().dropItemMap(player.zone, itemMap);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void RewarDDaNangcapLinhThu(Player player) {
        if (player != null) {
            if (Util.isTrue(8, 100)) {
                try {
                    int x = this.location.x;
                    ItemMap itemMap = null;
                    x += 16;
                    if (x < 0 || x >= this.zone.map.mapWidth) {
                        return;
                    }
                    int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
                    itemMap = new ItemMap(this.zone, (short) Util.nextInt(1484, 1488), 1, x, y, player.id);
                    ItemService.gI().AddOptionItemMap(itemMap);
                    Service.getInstance().dropItemMap(zone, itemMap);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        }
    }

    public boolean isBossDie() {
        if (this.isDie() || this.zone == null) {
            return true;
        }
        return false;
    }

    /**
     * Đổi trạng thái máu trắng -> đỏ, chuyển trạng thái tấn công
     */
    public void changeToAttack() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
        changeStatus(ATTACK);
    }

    /**
     * Đổi trạng thái máu đỏ -> trắng, chuyển trạng thái đứng
     */
    public void changeToIdle() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
        changeStatus(IDLE);
    }

    protected void chat(String text) {
        Service.getInstance().chat(this, text);
    }

    protected boolean chat(int prefix, String textChat) {
        try {
            if (prefix == -1) {
                this.chat(textChat);
            } else if (prefix == -2) {
                Player plMap = this.zone.getRandomPlayerInMap();
                if (plMap != null && !plMap.isDie() && Util.getDistance(this, plMap) <= 600) {
                    Service.getInstance().chat(plMap, textChat);
                }
            } else if (prefix == -3) {
                Player bossRandom = this.zone.getBossInMap();
                if (bossRandom != null && !bossRandom.isDie() && Util.getDistance(this, bossRandom) <= 600) {
                    Service.getInstance().chat(bossRandom, textChat);
                }
            } else if (prefix >= 0) {
                this.chat(textChat);
            }
        } catch (Exception e) {
            return false;
            // TODO: handle exception
        }

        return true;
    }

}
