package nro.models.player;

import nro.card.Card;
import nro.card.CollectionBook;
import nro.consts.ConstAchive;
import nro.consts.ConstMob;
import nro.consts.ConstPlayer;
import nro.consts.ConstTask;
import nro.data.DataGame;
import nro.dialog.ConfirmDialog;
import nro.models.clan.Buff;
import nro.models.item.CaiTrang;
import nro.models.boss.event.EscortedBoss;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.intrinsic.IntrinsicPlayer;
import nro.models.item.Item;
import nro.models.item.ItemTime;
import nro.models.map.ItemMap;
import nro.models.map.TrapMap;
import nro.models.map.Zone;
import nro.models.map.war.BlackBallWar;
import nro.models.map.mabu.MabuWar;
import nro.models.map.war.NamekBallWar;
import nro.models.mob.Mob;
import nro.models.mob.MobMe;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.npc.specialnpc.MagicTree;
import nro.models.pvp.PVP;
import nro.models.skill.PlayerSkill;
import nro.models.task.TaskPlayer;
import nro.server.Client;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.*;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineNew;
import nro.services.func.PVPServcice;
import nro.services.giftcode.RequestService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;
import lombok.Getter;
import lombok.Setter;
import java.util.Timer;

import io.netty.channel.epoll.EpollServerSocketChannelConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import nro.models.boss.Boss;
import nro.models.boss.BossManager;
import static nro.models.boss.BossManager.BOSSES_IN_GAME;
import nro.models.map.war.DaichienWar;
import nro.models.npc.specialnpc.KaminEgg;
import nro.resources.Resources;
import nro.server.ServerNotify;

public class Player {

    public int joindaichien;
    public int joinfree;
        
    public int vip;//vip 1 2 3
     public int vip1;
     public int diemtuanloc;
     public int tetduong;
    
    public long lastTimeSetSkillTime = 0;
    public int activeBoxType = 0;
    public int server;
    public byte[] buyLimit;
    public int bosspoint;
    public PlayerEvent event;
    public List<String> textRuongGo = new ArrayList<>();
    public boolean receivedWoodChest;
    public boolean receivedTopDhVT;
    public int goldChallenge;
    public int levelWoodChest;
    public boolean isInvisible;
    public boolean sendMenuGotoNextFloorMabuWar;
    public boolean isChanthientu;
    public boolean isDanhhieu;
    public long lastTimeBabiday;
    public long lastTimeChangeZone;
    public long lastTimeChatGlobal;
    public long lastTimeChatPrivate;
    public long lastTimeChangeMap;
    public Date firstTimeLogin;
    private Session session;
    public byte countSaveFail;
    public boolean beforeDispose;
    public boolean lockPK;
    public Timer timerDHVT;
    public Player _friendGiaoDich;
    public byte typetrain;
    public int expoff;
    public boolean istrain;
    public boolean isTrainning;
    public boolean IsTraing_type2;
    public boolean isChallenge;
    public boolean ischallenge_type2;
    public boolean seebossnotnpc;
    public long lastTimeWish;
    public boolean isWish;
    public long timeFixInventory;
    public boolean isPet;
    public boolean isBoss;
    public boolean isMiniPet;
    public int playerTradeId = -1;
    public Player playerTrade;

    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public Zone zone;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;

    private List<Mob> mobEnery;
    public Pet pet;
    public MiniPet minipet;

    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    public KaminEgg kaminEgg;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public CombineNew combineNew;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public Gift gift;
    public NPoint nPoint;
    public RewardBlackBall rewardBlackBall;
    public EffectFlagBag effectFlagBag;
    public Clan clan;
    public ClanMember clanMember;

    public ListFriendEnemy<Friend> friends;
    public ListFriendEnemy<Enemy> enemies;
    public String chatVip;
    protected boolean actived = false;
    public boolean loaded;

    public long id;
    public String name;
    public byte gender;
    public boolean isNewMember;
    public short head;

    public int napDau; // 0 = chưa nhận, 1 = đã nhận

    public int tongNap;
    public byte typePk;

    public long lastTimeInMap188;
    public long lastTimeNotifyTimeLeft;
    public boolean isInMap188 = false;
    public int reviveCount; // Tracks number of resurrections in map 188
    public long lastDeathTime;

    public long lastTimeNotifyTimeHoldBlackBall;
    public long lastTimeHoldBlackBall;
    public int tempIdBlackBallHold = -1;
    public int tempIdNamecBallHold = -1;
    public boolean isHoldBlackBall;
    public boolean isHoldNamecBall;

    public byte cFlag;
    public long lastTimeChangeFlag;
    public long lastTimeTrade;

    public boolean haveTennisSpaceShip;
    private byte useSpaceShip;

    public boolean isGoHome;

    public boolean justRevived;
    public long lastTimeRevived;

    public boolean immortal;
    public static final long LIMIT_GOLD = 100_000_000_000l;
    public long lastTimeBan;
    public long lastTimeBan2;
    public long lastTimeUpdate;
    public boolean isBan;
    public long lastTimeSendlaiNoti;
    public boolean isDuaTop;
    public boolean isGotoFuture;
    public long lastTimeGoToFuture;
    public boolean isgotoPrimaryForest;
    public long lastTimePrimaryForest;
    public long lastTimeEatPea;
    public long lastTimeResetSkill;
    public boolean isGoToBDKB;
    public long lastTimeGoToBDKB;
    public long lastTimeAnXienTrapBDKB;
    public long lastTimeUpdateShopTime;
    public long lastTimeDelay;
    private short powerPoint;
    private short percentPowerPont;

    public long lastTimePickItem;
    public long lastTimexDameChuong;
    public int GapthuPoint;

    public int RuongbauPoint;
    public String time_create;
    public int PauCuaPoint;
    public int GoldBau;
    public int GoldCua;
    public int GoldTom;
    public int GoldCa;
    public int GoldHuou;
    public int GoldGa;

    public boolean[] mocThuongDe;
    public int numDap;
    public boolean lock;
    public int numLinhThu;
    public int pointSK;
    public int pointThoiVang;

    @Setter
    @Getter
    private CollectionBook collectionBook;
    @Getter
    @Setter
    private boolean isSaving, isDisposed;
    @Getter
    @Setter
    private boolean interactWithKarin;
    @Getter
    @Setter
    public EscortedBoss escortedBoss;
    @Setter
    @Getter
    private ConfirmDialog confirmDialog;
    @Getter
    @Setter
    public byte[] rewardLimit;
    @Setter
    @Getter
    private PetFollow petFollow;
    @Setter
    @Getter
    private Buff buff;

    public Player() {
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory(this);
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer(this);
        rewardBlackBall = new RewardBlackBall(this);
        effectFlagBag = new EffectFlagBag(this);
        // ----------------------------------------------------------------------
        iDMark = new IDMark();
        combineNew = new CombineNew();
        mobEnery = new ArrayList<>();
        playerTask = new TaskPlayer(this);
        friends = new ListFriendEnemy<>(this);
        enemies = new ListFriendEnemy<>(this);
        itemTime = new ItemTime(this);
        charms = new Charms(this);
        gift = new Gift(this);
        effectSkin = new EffectSkin(this);
        event = new PlayerEvent(this);
        buyLimit = new byte[13];
        this.lastTimeInMap188 = 0;
        this.isInMap188 = false;
        buff = Buff.NONE;
        lastTimeUpdateShopTime = System.currentTimeMillis();
    }

    // --------------------------------------------------------------------------
    public short getPowerPoint() {
        return powerPoint;
    }

    public void addPowerPoint(int value) {
        powerPoint += value;
    }

    public short getPercentPowerPont() {
        return percentPowerPont;
    }

    public void addPercentPowerPoint(int value) {
        percentPowerPont += value;
    }

    public void resetPowerPoint() {
        percentPowerPont = 0;
        powerPoint = 0;
    }

    public void setUseSpaceShip(byte useSpaceShip) {
        // 0 - không dùng
        // 1 - tàu vũ trụ theo hành tinh
        // 2 - dịch chuyển tức thời
        // 3 - tàu tenis
        this.useSpaceShip = useSpaceShip;
    }

    public byte getUseSpaceShip() {
        return this.useSpaceShip;
    }

    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        } else {
            return true;
        }
    }

    public boolean isPl() {
        return this != null && !this.isPet && !this.isMiniPet && !this.isBoss;
    }

    public boolean isPlnPet() {
        return this != null && !this.isMiniPet && !this.isBoss;
    }

    // --------------------------------------------------------------------------
    public void setSession(Session session) {
        this.session = session;
    }

    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }

    public Session getSession() {
        return this.session;
    }

    public int version() {
        return session.version;
    }

    public boolean isVersionAbove(int version) {
        return version() >= version;
    }

    public void setfight(byte typeFight, byte typeTatget) {

        try {
            if (typeFight == (byte) 0 && typeTatget == (byte) 0) {
                this.isTrainning = true;
            }
            if (typeFight == (byte) 0 && typeTatget == (byte) 1) {
                this.IsTraing_type2 = true;
            }
            if (typeFight == (byte) 1 && typeTatget == (byte) 0) {
                this.isChallenge = true;
            }
            if (typeFight == (byte) 1 && typeTatget == (byte) 1) {
                this.ischallenge_type2 = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rsfight() {
        if (this.isTrainning) {
            this.isTrainning = false;
        }
        if (this.IsTraing_type2) {
            this.IsTraing_type2 = false;
        }
        if (this.isChallenge) {
            this.isChallenge = false;
        }
        if (this.ischallenge_type2) {
            this.ischallenge_type2 = false;
        }
    }
    // public boolean IsTry0() {
    // if (this.istry && this.isfight){
    // return true;
    // }
    // return false;
    // }
    // public boolean IsTry1() {
    // if (this.istry && this.isfight1){
    // return true;
    // }
    // return false;
    // }
    // public boolean IsFigh0() {
    // if (this.istry && this.isfight1){
    // return true;
    // }
    // return false;
    // }

    public void update() {
        if (!this.beforeDispose) {
            try {
                if (!isBan) {
                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }

                    if (effectSkill != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (minipet != null) {
                        minipet.update();
                    }
                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    if (event != null) {
                        event.update();
                    }
                    if (this.lastTimeWish != 0 && Util.canDoWithTime(this.lastTimeWish, 6000)) {
                        lastTimeWish = 0;
                    }
                    if (!isPet && !isBoss && !isMiniPet && !isDie()) {
                        checkBDKB_NEW();
                    }
                    if (fusion != null && fusion.typeFusion != ConstPlayer.HOP_THE_PORATA3 && this.isPl()) {
                        Service.getInstance().addEffect1(this, 504);
                    }
                    if (this.isPl()) {
                        if (MapService.gI().isVegetable(this.zone.map.mapId)) {
                            if (!InventoryService.gI().existItemBag(this, 992)) {
                                ChangeMapService.gI().changeMapBySpaceShip(this, 21 + this.gender, -1, -1);
                                Service.getInstance().sendThongBao(this, "Bạn đã được phi thuyền chở về nhà vì không còn tồn tại Nhẫn thời không Sai lệch !");
                            }
                        }
                        if (this.zone.map.mapId == 45) {
                            if (this.location.y > 576) {
                                ChangeMapService.gI().changeMapInYard(this, 46, -1, 342);
                            }
                        }
                    }
                    if (!this.isBoss && !this.isPet && !this.isMiniPet
                            && Util.canDoWithTime(lastTimeUpdateShopTime, 60000)) {
                        // kiểm tra shop mỗi phút
                        lastTimeUpdateShopTime = System.currentTimeMillis();
                        InventoryService.gI().CheckAndRestShopTime(this);
                        this.inventory.timeOnline += 1;

                        RequestService.gI().RegisterCMD(this, RequestService.AUTO_SAVE);

                    }
                    BlackBallWar.gI().update(this);
                    DaichienWar.gI().update(this);
                    if (!this.isBoss && !this.isPet && !this.isMiniPet) {
                        MabuWar.gI().update(this);
                        if (this.server != Manager.SERVER) {
                            PlayerService.gI().banPlayer(this);
                        }
                        if (Util.canDoWithTime(lastTimeUpdate, 60000)) {
                            this.playerTask.achivements.get(ConstAchive.HOAT_DONG_CHAM_CHI).count++;
                        }
                    }
                    if (isGotoFuture && Util.canDoWithTime(lastTimeGoToFuture, 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                        this.isGotoFuture = false;
                    }
                    if (isGoToBDKB && Util.canDoWithTime(lastTimeGoToBDKB, 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                        this.isGoToBDKB = false;
                    }

                    // if (isGoToBDKB && Util.canDoWithTime(lastTimeGoToBDKB, 3000)) {
                    // ItemTimeService.gI().sendTextBanDoKhoBauNew(this);
                    // ChangeMapService.gI().changeMapBySpaceShip(this, 164, -1, 35);
                    // this.isGoToBDKB = false;
                    // Service.getInstance().sendThongBao(playerTrade,
                    // "Bạn đang trong đảo kho báu, nơi những vật phẩm quý báu được chôn tại đây.
                    // Bạn chỉ có 30p để tìm kho báu,"
                    // + "nếu hết 30p mà bạn không có bản đồ trong hành trang, bạn sẽ được phi
                    // thuyền đưa về");
                    // }
                    if (isgotoPrimaryForest && Util.canDoWithTime(lastTimePrimaryForest, 6000)) {
                        ChangeMapService.gI().changeMap(this, 161, -1, 169, 312);
                        this.isgotoPrimaryForest = false;
                    }
                    if (pet != null) {
                        if (pet.LevelZeno == 1) {
                            Service.getInstance().addEffect(this, 56);
                        } else if (pet.LevelZeno == 2) {
                            Service.getInstance().addEffect(this, 57);
                        } else if (pet.LevelZeno >= 3) {
                            Service.getInstance().addEffect(this, 58);
                        }
                    }

                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                } else {
                    if (Util.canDoWithTime(lastTimeBan, 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                Log.error(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    private void checkLocation() {
        if (this.location.x > this.zone.map.mapWidth || this.location.x < 0
                || this.location.y > this.zone.map.mapHeight || this.location.y < 0) {
            if (this.inventory.gold >= 500000000) {
                this.inventory.subGold(500000000);
            } else {
                this.inventory.gold = 0;
            }
            PlayerService.gI().sendInfoHpMpMoney(this);
            ChangeMapService.gI().changeMapNonSpaceship(this, this.gender + 21, 400, 336);
            Service.getInstance().sendBigMessage(this, 1139, "|1|Do phát hiện có hành vi bất thường nên\n "
                    + "chúng tôi đã đưa bạn về nhà và xử phạt 500Tr vàng\n"
                    + "|7|nếu còn tiếp tục tái phạm sẽ khóa vĩnh viễn");
        }
    }

    // --------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     */
    private static final short[][] idOutfitFusion = {
        {380, 381, 382},
        {383, 384, 385},
        {391, 392, 393},
        //c2
        {870, 871, 872},
        {873, 874, 875},
        {867, 868, 869},
        //c3
        {1579, 1581, 1582},
        {1587, 1589, 1590},
        {1583, 1585, 1586},
        //c4
        {1661, 1662, 1663},
        {1664, 1665, 1666},
        {1658, 1659, 1660},};
    private static final short[][] headHoaSieuThan = {
        {1418, 1429, 1433, 1437, 1440},
        {1441, 1444, 1447, 1450, 1453},
        {1472, 1456, 1459, 1462, 1465}
    };

    private static final short[][] idHoaHinh = {
        {1380, 1422, 1423},
        {1386, 1427, 1428},
        {1375, 1470, 1471}};

    private static final short[] idOutfitFusion_ZAMASU = {1302, 1303, 1304};
    private static final short[] idOutfitFusion_VIP = {1367, 1368, 1369};

    public byte getAura() {
        if (Manager.TOP_PLAYERS.contains(this.name)) {
            return 1;
        }
        if (this.effectSkill.isSaiYan) {
            int point = this.effectSkill.levelSaiYan;
            switch (this.gender) {
                case 0:
                    return (byte) (20 + point);
                case 1:
                    return (byte) (26 + point);
                case 2:
                    return (byte) (32 + point);
                default:
                    return -1;
            }
        }
        CollectionBook book = getCollectionBook();
        if (book != null) {
            Card card = book.getCards().stream().filter(t -> t.isUse() && t.getCardTemplate().getAura() != -1).findAny()
                    .orElse(null);
            if (card != null) {
                return (byte) card.getCardTemplate().getAura();
            }
        }
        return -1;
    }

    public boolean checkSkinFusion() {
        if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            Short idct = inventory.itemsBody.get(5).template.id;
            if (idct >= 601 && idct <= 603 || idct >= 639 && idct <= 641) {
                return true;
            }
        }
        return false;
    }

    private boolean is_2_cai_trang_fusion() {
        if (inventory != null && pet != null && pet.inventory.itemsBody.get(5).isNotNullItem()
                && inventory.itemsBody.get(5).isNotNullItem()) {
            if ((inventory.itemsBody.get(5).template.id == 2062
                    && this.pet.inventory.itemsBody.get(5).template.id == 2041)
                    || (inventory.itemsBody.get(5).template.id == 2041
                    && this.pet.inventory.itemsBody.get(5).template.id == 2062)) {

                return true;
            }
        }
        return false;
    }

    private boolean is_zamasu_fusion() {
        if (inventory != null && pet != null && inventory.itemsBody.get(5).isNotNullItem()) {
            if ((inventory.itemsBody.get(5).template.id == 898
                    && pet.isBU)) {
                return true;
            }
        }
        return false;
    }

    public short getHead() {
        if (this.id == 1000000) {
            return 412;
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill != null && effectSkill.isSaiYan) {
            return headHoaSieuThan[this.gender][this.effectSkill.levelSaiYan - 1];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (effectSkin != null && effectSkin.isCarrot) {
            return 406;
        } else if (effectSkin != null && effectSkin.isBiNgo) {
            return 760;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 454;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {

            if (is_zamasu_fusion()) {
                return idOutfitFusion_ZAMASU[0];
            }
            if (checkSkinFusion()) {
                CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
                return (short) (ct.getID()[0] != -1 ? ct.getID()[0] : inventory.itemsBody.get(5).template.part);
            } else if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];

            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {

                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];

            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][0];    
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
            if (checkSkinFusion()) {
                return this.head;
            }
            if (ct != null) {
                return (short) (ct.getID()[0] != -1 ? ct.getID()[0] : inventory.itemsBody.get(5).template.part);
            }
        }
        return this.head;
    }

    public short getBody() {
        if (this.id == 1000000) {
            return 413;
        } else if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill != null && effectSkill.isSaiYan) {
            return idHoaHinh[this.gender][1];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else if (effectSkin != null && effectSkin.isCarrot) {
            return 407;
        } else if (effectSkin != null && effectSkin.isBiNgo) {
            return 761;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 455;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (is_zamasu_fusion()) {
                return idOutfitFusion_ZAMASU[1];
            }
            if (checkSkinFusion()) {
                CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
                return (short) ct.getID()[1];
            }
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][1];    
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
            if (checkSkinFusion()) {
                if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
                    if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
                        return inventory.itemsBody.get(0).template.part;
                    }
                }
            }
            if (ct != null && ct.getID()[1] != -1) {
                return (short) ct.getID()[1];
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {
        if (this.id == 1000000) {
            return 414;
        } else if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill != null && effectSkill.isSaiYan) {
            return idHoaHinh[this.gender][2];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else if (effectSkin != null && effectSkin.isCarrot) {
            return 408;
        } else if (effectSkin != null && effectSkin.isBiNgo) {
            return 762;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 456;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {

            if (is_zamasu_fusion()) {
                return idOutfitFusion_ZAMASU[2];
            }
            if (checkSkinFusion()) {
                CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
                return (short) ct.getID()[2];
            }
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][2];
             } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][2];    
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            if (checkSkinFusion()) {
                if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
                    return inventory.itemsBody.get(1).template.part;
                }
            }
            CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
            if (ct != null && ct.getID()[2] != -1) {
                return (short) ct.getID()[2];
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public short getFlagBag() {
        if (this.isHoldBlackBall) {
            return 31;
        } else if (this.isHoldNamecBall) {
            return 30;
        }
        if (this.inventory.itemsBody.size() >= 8
                && this.inventory.itemsBody.get(7).isNotNullItem()) {
            return this.inventory.itemsBody.get(7).template.part;
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    public short getMount() {
        if (this.isVersionAbove(220)) {
            for (Item item : inventory.itemsBody) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 24) {
                        if (item.template.gender == 3 || item.template.gender == this.gender) {
                            return item.template.id;
                        } else {
                            return -1;
                        }
                    }
                    if (item.template.type == 23) {
                        if (item.template.id < 500) {
                            return item.template.id;
                        } else {
                            // change ibn.json not here
                            Object mount = DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
                            if (mount == null) {
                                return -1;
                            }
                            return (short) mount;
                        }
                    }
                }
            }
        } else {
            for (Item item : inventory.itemsBag) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 24) {
                        if (item.template.gender == 3 || item.template.gender == this.gender) {
                            return item.template.id;
                        } else {
                            return -1;
                        }
                    }
                    if (item.template.type == 23) {
                        if (item.template.id < 500) {
                            return item.template.id;
                        } else {
                            // change ibn.json not here
                            Object mount = DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
                            if (mount == null) {
                                return -1;
                            }
                            return (short) mount;
                        }
                    }
                }
            }
        }
        return -1;
    }

    // --------------------------------------------------------------------------
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {

        if (!this.isDie()) {
            if (this.isMiniPet) {
                return 0;
            }
            int mstChuong = this.nPoint.mstChuong;
            int giamst = this.nPoint.tlGiamst;
            short tlXuyenGiap = 0;

            if (plAtt != null) {
                // if (this.pet != null && this.pet.status < 3) {
                // this.pet.angry(plAtt);
                // }
                // if (!this.isBoss && plAtt.nPoint.xDameChuong &&
                // SkillUtil.isUseSkillChuong(plAtt)) {
                // damage = plAtt.nPoint.tlDameChuong * damage;
                // plAtt.nPoint.xDameChuong = false;
                // }
                if (SkillUtil.isUseSkillChuong(plAtt)) {
                    tlXuyenGiap = plAtt.nPoint.tlXuyenGiapChuong;
                    if (mstChuong > 0) {
                        PlayerService.gI().hoiPhuc(this, 0,
                                plAtt.nPoint.calLimit(plAtt.nPoint.calPercent(damage, mstChuong)));
                        damage = 0;
                    }
                } else if (SkillUtil.isUseSkillDam(plAtt)) {
                    tlXuyenGiap = plAtt.nPoint.tlXuyenGiapCanChien;
                }
                if (tlXuyenGiap > 100) {
                    tlXuyenGiap = 100;

                }
            }
            if (!SkillUtil.isUseSkillBoom(plAtt)) {
                if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                    return 0;
                }
            }
            damage = this.nPoint.subDameInjureWithDeff(damage, tlXuyenGiap);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }
            if (giamst > 0) {
                if (tlXuyenGiap > 0) {
                    giamst -= nPoint.calPercent(giamst, tlXuyenGiap);
                }

                if (giamst > 0) {
                    damage -= nPoint.calPercent(damage, giamst);
                }
            }
            if (this.nPoint.isBocPha && this.nPoint.dameHapThu < (this.nPoint.hpMax / 2)) {
                this.nPoint.dameHapThu += this.nPoint.calPercent(damage, 10);
                // hấp thụ 10% dame tối đa 50%
            }

            this.nPoint.subHP(damage);
            if (isDie()) {
                if (plAtt != null) {
                    if (MapService.gI().isMapMabuWar(plAtt.zone.map.mapId)) {
                        plAtt.addPowerPoint(5);
                        Service.getInstance().sendPowerInfo(plAtt, "TL", plAtt.getPowerPoint());
                    }
                }
                setDie(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    public void AddMobEnermy(Mob mob) {
        if (mobEnery.contains(mob) || mob.mobDontSavePlayer()) {
            return;
        }
        if (mobEnery.size() >= 20) {
            Mob firstMob = mobEnery.get(0); // Lấy phần tử đầu tiên
            RemoveMobEnemy(firstMob);
        }
        // Log.warning("Thêm mob");
        mobEnery.add(mob);
    }

    public void RemoveMobEnemy(Mob mob) {
        try {
            if (mobEnery.contains(mob)) {
                mob.removeTargetID(this);
                mobEnery.remove(mob);

            }
        } catch (Exception e) {
            Log.error(getClass(), e, "Loi xoa mob");
            // TODO: handle exception
        }

    }

    public void ClearAllMobEnemy() {
        try {
            List<Mob> baseMob = new ArrayList<>();
            for (Mob mob : mobEnery) {
                baseMob.add(mob);
            }
            for (Mob mob : baseMob) {
                RemoveMobEnemy(mob);
            }

        } catch (Exception e) {
            Log.error(getClass(), e, "Loi xoa mob all");
            // mobEnery.clear();
        }
    }

    public void checkBDKB_NEW() {
        if (MapService.gI().isMapDaiHaiTrinh(this.zone.map.mapId)
                && this.charms.tdDeTuMabu2 - System.currentTimeMillis() <= 0) {
            Service.getInstance().sendThongBao(this,
                    "Thời gian trong bản đồ đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(this, this.gender + 21, -1, 250);
        }
    }

    private void setDie(Player plAtt) {
        if (this.isPl()) {
            long vangtru = this.nPoint.power / 1000000;
            if (vangtru > 32000) {
                vangtru = 32000;
            }
            int vang = (int) vangtru - Util.nextInt(10, 100);

            if (this.inventory.gold >= vang && vang >= 1) {
                this.inventory.gold -= vang;
                Service.getInstance().sendMoney(this);
                vang = vang * 95 / 100;
                if (vang < 10000) {
                    Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, 189, vang, this.location.x, this.location.y, this.id));
                } else if (vang < 20000) {
                    Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, 188, vang, this.location.x, this.location.y, this.id));
                } else {
                    Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, 190, vang, this.location.x, this.location.y, this.id));
                }
            }
        }
        if (this.zone.map.mapId == 188) {
            rewardPK(plAtt);
        }
        // xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.getInstance().point(this);
        }
        // xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        // xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        // xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.getInstance().charDie(this);
        // add kẻ thù
        if (!this.isPet && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        if (this.effectSkin.isSocola) {
            reward(plAtt);
        }
        if (MapService.gI().isMapMabuWar(this.zone.map.mapId)) {
            if (this.powerPoint < 20) {
                this.powerPoint = 0;
            }
            if (this.percentPowerPont < 100) {
                this.percentPowerPont = 0;
            }
        }
        // kết thúc pk
        PVPServcice.gI().finishPVP(this, PVP.TYPE_DIE);
        BlackBallWar.gI().dropBlackBall(this);
        if (isHoldNamecBall) {
            NamekBallWar.gI().dropBall(this);
        }
        // xóa danh sách mob
        // Log.warning("Đã set die");
        ClearAllMobEnemy();
    }

    public void rewardPK(Player pl) {
        if (pl != null && pl.clan != null && pl.zone != null) {
            int x = this.location.x;
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            ItemMap itemMap = new ItemMap(this.zone, 861, 5, x, y, pl.id);
            List<ItemMap> itemList = new ArrayList<>();
            itemList.add(itemMap);
            MobService.gI().hutItem(pl, itemList);
                pl.clanMember.clanPoint++;
            Service.getInstance().sendThongBao(pl, "Bạn nhận được 5 hồng ngọc");
        }
    }

    public void reward(Player pl) {
        if (pl != null) {
            int x = this.location.x;
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            ItemMap itemMap = new ItemMap(this.zone, 516, 1, x, y, pl.id);
            RewardService.gI().initBaseOptionClothesMap(itemMap);
            if (itemMap != null) {
                Service.getInstance().dropItemMap(zone, itemMap);
            }
        }
    }

    // --------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    public boolean isAdmin() {
        if (this.session == null) {
            return false;
        }
        return this.session.isAdmin;
    }

    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
        this.immortal = true;
    }

    public void dispose() {
        if (escortedBoss != null) {
            escortedBoss.stopEscorting();
        }
        isDisposed = true;
        // if (pet != null) {
        // pet.dispose();
        // pet = null;
        // }
        // playerTrade = null;
        // if (mapBlackBall != null) {
        // mapBlackBall.clear();
        // mapBlackBall = null;
        // }
        // zone = null;
        // mapBeforeCapsule = null;
        // if (mapCapsule != null) {
        // mapCapsule.clear();
        // mapCapsule = null;
        // }
        // if (mobMe != null) {
        // mobMe.dispose();
        // mobMe = null;
        // }
        // location = null;
        // if (setClothes != null) {
        // setClothes.dispose();
        // setClothes = null;
        // }
        // if (effectSkill != null) {
        // effectSkill.dispose();
        // effectSkill = null;
        // }
        // if (mabuEgg != null) {
        // mabuEgg.dispose();
        // mabuEgg = null;
        // }
        // if (playerTask != null) {
        // playerTask.dispose();
        // playerTask = null;
        // }
        // if (itemTime != null) {
        // itemTime.dispose();
        // itemTime = null;
        // }
        // if (fusion != null) {
        // fusion.dispose();
        // fusion = null;
        // }
        // if (magicTree != null) {
        // magicTree.dispose();
        // magicTree = null;
        // }
        // if (playerIntrinsic != null) {
        // playerIntrinsic.dispose();
        // playerIntrinsic = null;
        // }
        // if (inventory != null) {
        // inventory.dispose();
        // inventory = null;
        // }
        // if (playerSkill != null) {
        // playerSkill.dispose();
        // playerSkill = null;
        // }
        // if (combineNew != null) {
        // combineNew.dispose();
        // combineNew = null;
        // }
        // iDMark = null;
        // if (charms != null) {
        // charms.dispose();
        // charms = null;
        // }
        // if (effectSkin != null) {
        // effectSkin.dispose();
        // effectSkin = null;
        // }
        // if (gift != null) {
        // gift.dispose();
        // gift = null;
        // }
        // if (nPoint != null) {
        // nPoint.dispose();
        // nPoint = null;
        // }
        // if (rewardBlackBall != null) {
        // rewardBlackBall.dispose();
        //
        // rewardBlackBall = null;
        // }
        // if (effectFlagBag != null) {
        // effectFlagBag.dispose();
        // effectFlagBag = null;
        // }
        // effectFlagBag = null;
        // clan = null;
        // clanMember = null;
        // friends = null;
        // enemies = null;
        // session = null;
        // name = null;
    }
}
