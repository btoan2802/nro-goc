package nro.models.boss;

import nro.consts.ConstPlayer;
import nro.models.skill.Skill;
import lombok.Builder;

public class BossData {

    public static final int _0_GIAY = 0;
    public static final int _1_GIAY = 1;
    public static final int _5_GIAY = 5;
    public static final int _10_GIAY = 10;
    public static final int _30_GIAY = 30;
    public static final int _1_PHUT = 60;
    public static final int _3_PHUT = 180;
    public static final int _5_PHUT = 300;
    public static final int _10_PHUT = 600;
    public static final int _15_PHUT = 900;
    public static final int _20_PHUT = 1200;
    public static final int _30_PHUT = 1800;
    public static final int _45_PHUT = 2700;
    public static final int _1_GIO = 3600;

    // --------------------------------------------------------------------------
    public String name;

    public byte gender;

    public byte typeDame;

    public byte typeHp;

    public int dame;

    public int[][] hp;

    public short[] outfit;

    public short[] mapJoin;

    public int[][] skillTemp;

    public int secondsRest;

    public boolean joinMapIdle;

    public int timeDelayLeaveMap = -1;

    @Builder
    public BossData(String name, byte gender, byte typeDame, byte typeHp, int dame, int[][] hp,
            short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
    }

    public BossData(String name, byte gender, byte typeDame, byte typeHp, int dame, int[][] hp,
            short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, boolean joinMapIdle) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.joinMapIdle = joinMapIdle;
    }

    public BossData(String name, byte gender, byte typeDame, byte typeHp, int dame, int[][] hp,
            short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, int timeDelayLeaveMap) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.timeDelayLeaveMap = timeDelayLeaveMap;
    }

    public BossData(String name, byte gender, byte typeDame, byte typeHp, int dame, int[][] hp,
            short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, boolean joinMapIdle,
            int timeDelayLeaveMap) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.joinMapIdle = joinMapIdle;
        this.timeDelayLeaveMap = timeDelayLeaveMap;
    }

    /////////////////////////////////////////////////////////// Biệt đội Pilap
    public static final BossData SU = new BossData(
            "Su", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            135000, // dame
            new int[][]{{100}}, // hp
            new short[]{618, 619, 620}, // outfit
            new short[]{27, 28, 29}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    public static final BossData MAI = new BossData(
            "Mai", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            135000, // dame
            new int[][]{{100}}, // hp
            new short[]{615, 616, 617}, // outfit
            new short[]{27, 28, 29}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData PILAP = new BossData(
            "PiLap", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            135000, // dame
            new int[][]{{100}}, // hp
            new short[]{612, 613, 614}, // outfit
            new short[]{27, 28, 29}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    ///////////////////////////////////////////////////////////////// Biệt đội Pilap
    // --------------------------------------------------------------------------Broly
    public static final BossData BROLY = new BossData(
            "Broly %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_PERCENT_HP_HUND, // type dame
            Boss.HP_NORMAL, // type hp
            1, // dame
            new int[][]{{5000, 15000}}, // hp
            new short[]{291, 292, 293}, // outfit
            new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38}, // map
            // join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _0_GIAY // số giây nghỉ
    );

    public static final BossData SUPER_BROLY = new BossData(
            "Super Broly %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{50_000_000}}, // hp
            new short[]{294, 295, 296}, // outfit
            new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38}, // map
            // join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _10_GIAY);

    public static final BossData SUPER_BROLY_RED = new BossData(
            "Super Broly Red %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new int[][]{{50_000_000}}, // hp
            new short[]{2000, 295, 296}, // outfit
            new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38}, // map
            // join
            // new short[]{14}, //map join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _10_PHUT);

    ///////////////////////////////////////////////////////////////////////////////////////// SUPER
    ///////////////////////////////////////////////////////////////////////////////////////// ZAMASU
    public static final BossData SUPER_ZAMAS = new BossData(
            "Super Zamas %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{1302, 1303, 1304}, // outfit
            new short[]{98}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _10_PHUT);

    //////////////////////////////////////////////////////////////////////////////////////////
    // --------------------------------------------------------------------------Boss
    ////////////////////////////////////////////////////////////////////////////////////////// hải
    ////////////////////////////////////////////////////////////////////////////////////////// tặc
    public static final BossData LUFFY = new BossData(
            "Luffy", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new int[][]{{12000000}}, // hp
            new short[]{582, 583, 584}, // outfit
            new short[]{137}, // map join
            new int[][]{ // skill
                {Skill.GALICK, 7, 1000}, {Skill.GALICK, 6, 1000}, {Skill.GALICK, 5, 1000},
                {Skill.GALICK, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData ZORO = new BossData(
            "Zoro", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new int[][]{{11000000}}, // hp
            new short[]{585, 586, 587}, // outfit
            new short[]{137}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 7, 1000}, {Skill.DRAGON, 6, 1000}, {Skill.DRAGON, 5, 1000},
                {Skill.DRAGON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData SANJI = new BossData(
            "Sanji", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new int[][]{{10000000}}, // hp
            new short[]{588, 589, 590}, // outfit
            new short[]{137}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData USOPP = new BossData(
            "Usopp", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new int[][]{{7000000}}, // hp
            new short[]{597, 598, 599}, // outfit
            new short[]{136}, // map join
            new int[][]{ // skill
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 1, 1000},},
            _0_GIAY, true);

    public static final BossData FRANKY = new BossData(
            "Franky", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new int[][]{{8000000}}, // hp
            new short[]{594, 595, 596}, // outfit
            new short[]{136}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000},
                {Skill.ANTOMIC, 7, 5000}
            },
            _0_GIAY, true);

    public static final BossData BROOK = new BossData(
            "Brook", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new int[][]{{9000000}}, // hp
            new short[]{591, 592, 593}, // outfit
            new short[]{136}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData NAMI = new BossData(
            "Nami", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new int[][]{{4000000}}, // hp
            new short[]{600, 601, 602}, // outfit
            new short[]{138}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData CHOPPER = new BossData(
            "Chopper", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new int[][]{{5000000}}, // hp
            new short[]{606, 607, 608}, // outfit
            new short[]{138}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData TRUNG_UY_XANH_LO_2 = new BossData(
            "Trung uý Xanh Lơ", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            35000, // dame
            new int[][]{{900000000}}, // hp
            new short[]{135, 136, 137}, // outfit
            new short[]{166, 167}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480},
                {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420},
                {Skill.DEMON, 7, 400},
                {Skill.KAMEJOKO, 2, 1500},
                {Skill.THAI_DUONG_HA_SAN, 3, 15000}, {Skill.THAI_DUONG_HA_SAN, 7, 30000}
            },
            _0_GIAY);

    public static final BossData TRUNG_UY_THEP_2 = new BossData(
            "Trung uý Thép", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            35000, // dame
            new int[][]{{1500000000}}, // hp
            new short[]{129, 130, 131}, // outfit
            new short[]{166, 167}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480},
                {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420},
                {Skill.DEMON, 7, 400},
                {Skill.KAMEJOKO, 2, 1500},
                {Skill.KHIEN_NANG_LUONG, 3, 65000}
            },
            _0_GIAY);
    public static final BossData ROBOT_VE_SI_2 = new BossData(
            "Rôbốt Vệ Sĩ", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            35000, // dame
            new int[][]{{2000000000}}, // hp
            new short[]{138, 139, 140}, // outfit
            new short[]{166, 167}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480},
                {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420},
                {Skill.DEMON, 7, 400},
                {Skill.KAMEJOKO, 2, 1500},
                {Skill.KHIEN_NANG_LUONG, 3, 65000}
            },
            _10_PHUT);
    public static final BossData BILL_BI_NGO = new BossData(
            "Bill Bí Ngô", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            35000, // dame
            new int[][]{{500000000}}, // hp
            new short[]{754, 755, 756}, // outfit
            new short[]{5, 13, 20}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480},
                {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420},
                {Skill.DEMON, 7, 400},
                {Skill.KAMEJOKO, 2, 1500},
                {Skill.THAI_DUONG_HA_SAN, 3, 15000}, {Skill.SOCOLA, 7, 30000}
            },
            _5_PHUT);
    public static final BossData HON_MA = new BossData(
            "Hồn ma", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            35000, // dame
            new int[][]{{500000000}}, // hp
            new short[]{891, 892, 893}, // outfit
            new short[]{178}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480},
                {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420},
                {Skill.DEMON, 7, 400},
                {Skill.KAMEJOKO, 2, 1500},
                {Skill.THAI_DUONG_HA_SAN, 3, 15000}, {Skill.SOCOLA, 7, 30000}
            },
            _5_PHUT);
    public static final BossData ROBIN = new BossData(
            "Robin", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new int[][]{{6000000}}, // hp
            new short[]{603, 604, 605}, // outfit
            new short[]{138}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);
    // ########### LUFFY NEW ############
    public static final BossData LUFFY_NEW = new BossData(
            "Luffy", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{582, 583, 584}, // outfit
            new short[]{181}, // map join
            new int[][]{ // skill
                {Skill.GALICK, 7, 1000}, {Skill.GALICK, 6, 1000}, {Skill.GALICK, 5, 1000},
                {Skill.GALICK, 4, 1000}
            },
            _45_PHUT, true);
    public static final BossData ZORO_NEW = new BossData(
            "Zoro", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{585, 586, 587}, // outfit
            new short[]{181}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);

    public static final BossData SANJI_NEW = new BossData(
            "Sanji", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{588, 589, 590}, // outfit
            new short[]{181}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData USOPP_NEW = new BossData(
            "Usopp", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{597, 598, 599}, // outfit
            new short[]{181}, // map join
            new int[][]{ // skill
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 1, 1000},},
            _0_GIAY, true);

    public static final BossData FRANKY_NEW = new BossData(
            "Franky", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{594, 595, 596}, // outfit
            new short[]{181}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000},
                {Skill.ANTOMIC, 7, 5000}
            },
            _0_GIAY);

    public static final BossData BROOK_NEW = new BossData(
            "Brook", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{591, 592, 593}, // outfit
            new short[]{181}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000}
            },
            _0_GIAY);

    public static final BossData NAMI_NEW = new BossData(
            "Nami", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{600, 601, 602}, // outfit
            new short[]{181}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData CHOPPER_NEW = new BossData(
            "Chopper", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{606, 607, 608}, // outfit
            new short[]{181}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);
    public static final BossData ROBIN_NEW = new BossData("Robin", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{603, 604, 605}, // outfit
            new short[]{181}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                {Skill.DEMON, 4, 1000}},
            _0_GIAY, true);
    // --------------------------------------------------------------------------Boss
    // DORAEMON
    public static final BossData DORAEMON = new BossData(
            "Đôrêmon", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{200_000_000}}, // hp
            new short[]{790, 791, 792}, // outfit
            new short[]{6}, // map join
            new int[][]{ // skill
                {Skill.GALICK, 7, 1000}, {Skill.GALICK, 6, 1000}, {Skill.GALICK, 5, 1000},
                {Skill.GALICK, 4, 1000}
            },
            _15_PHUT, true);
    public static final BossData NOBITA = new BossData(
            "Nobita", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{200_000_000}}, // hp
            new short[]{844, 845, 846}, // outfit
            new short[]{6}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData SUNEO = new BossData(
            "Xêkô", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{200_000_000}}, // hp
            new short[]{850, 851, 852}, // outfit
            new short[]{6}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    public static final BossData CHAIEN = new BossData(
            "Chaien", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{200_000_000}}, // hp
            new short[]{847, 848, 849}, // outfit
            new short[]{6}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData SIZUKA = new BossData(
            "Xuka", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{200_000_000}}, // hp
            new short[]{802, 803, 804}, // outfit
            new short[]{6}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);

    // --------------------------------------------------------------------------Boss
    // doanh trại
    public static final BossData TRUNG_UY_TRANG = new BossData(
            "Trung uý Trắng", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, // type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, // type hp
            50, // dame
            new int[][]{{50}}, // hp
            new short[]{141, 142, 143}, // outfit
            new short[]{59}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480},
                {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420},
                {Skill.DEMON, 7, 400}
            },
            _0_GIAY);

    public static final BossData TRUNG_UY_XANH_LO = new BossData(
            "Trung uý Xanh Lơ", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, // type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, // type hp
            20, // dame
            new int[][]{{30}}, // hp
            new short[]{135, 136, 137}, // outfit
            new short[]{62}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480},
                {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420},
                {Skill.DEMON, 7, 400},
                {Skill.KAMEJOKO, 2, 1500},
                {Skill.THAI_DUONG_HA_SAN, 3, 15000}, {Skill.THAI_DUONG_HA_SAN, 7, 30000}
            },
            _0_GIAY);

    public static final BossData TRUNG_UY_THEP = new BossData(
            "Trung uý Thép", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, // type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, // type hp
            100, // dame
            new int[][]{{300}}, // hp
            new short[]{129, 130, 131}, // outfit
            new short[]{55}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 300}, {Skill.DRAGON, 3, 500},
                {Skill.DEMON, 1, 100}, {Skill.DEMON, 2, 300}, {Skill.DEMON, 3, 500},
                {Skill.GALICK, 1, 100},
                {Skill.MASENKO, 1, 100}, {Skill.MASENKO, 2, 100}
            },
            _0_GIAY);

    public static final BossData NINJA_AO_TIM = new BossData(
            "Ninja áo tím", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, // type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, // type hp
            40, // dame
            new int[][]{{150}}, // hp
            new short[]{123, 124, 125}, // outfit
            new short[]{54}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    public static final BossData NINJA_AO_TIM_FAKE = new BossData(
            "Ninja áo tím", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, // type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, // type hp
            75, // dame
            new int[][]{{100}}, // hp
            new short[]{123, 124, 125}, // outfit
            new short[]{54}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    public static final BossData ROBOT_VE_SI = new BossData(
            "Rôbốt Vệ Sĩ", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, // type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, // type hp
            50, // dame
            new int[][]{{120}}, // hp
            new short[]{138, 139, 140}, // outfit
            new short[]{57}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    //////////////////// -------------------------------------------------------------------
    //////////////////// RAITI
    public static final BossData RAITI_TD = new BossData(
            "RAITI", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            12000, // dame
            new int[][]{{100}}, // hp
            new short[]{490, 491, 492}, // outfit
            new short[]{42, 0, 1, 2, 3, 4, 5, 6, 27, 28, 29, 30}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_PHUT);

    public static final BossData RAITI_NM = new BossData(
            "RAITI", // name
            ConstPlayer.NAMEC, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            120000, // dame
            new int[][]{{100}}, // hp
            new short[]{490, 491, 492}, // outfit
            new short[]{43, 7, 8, 9, 10, 11, 12, 13, 31, 32, 33, 34}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_PHUT);
    public static final BossData RAITI_XD = new BossData(
            "RAITI", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            12000, // dame
            new int[][]{{100}}, // hp
            new short[]{490, 491, 492}, // outfit
            new short[]{44, 14, 15, 16, 17, 18, 19, 20, 35, 36, 37, 38}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _0_GIAY);
    /////////////////////// -------------------------------------------------------------------------
    // --------------------------------------------------------------------------Boss
    /////////////////////// xên ginder
    public static final BossData XEN_BO_HUNG_1 = new BossData(
            "Xên bọ hung 1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            12000, // dame
            new int[][]{{400_000_000}}, // hp
            new short[]{228, 229, 230}, // outfit
            new short[]{100}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _15_PHUT);

    public static final BossData XEN_BO_HUNG_2 = new BossData(
            "Xên bọ hung 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            16000, // dame
            new int[][]{{600_000_000}}, // hp
            new short[]{231, 232, 233}, // outfit
            new short[]{100}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    public static final BossData XEN_BO_HUNG_HOAN_THIEN = new BossData(
            "Xên hoàn thiện", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{800_000_000}}, // hp
            new short[]{234, 235, 236}, // outfit
            new short[]{100}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    // --------------------------------------------------------------------------Boss
    // xên võ đài
    public static final BossData XEN_BO_HUNG = new BossData(
            "Xên bọ hung", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{234, 235, 236}, // outfit
            new short[]{103}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100},
                {Skill.THAI_DUONG_HA_SAN, 5, 45000},
                {Skill.TU_SAT, 7, 100}
            },
            _15_PHUT, false);

    public static final BossData XEN_CON = new BossData(
            "Xên con", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            5000, // dame
            new int[][]{{900_000_000}}, // hp
            new short[]{264, 265, 266}, // outfit
            new short[]{103}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    public static final BossData XEN_CON_EVENT = new BossData(
            "Xên con", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new int[][]{{200000000}}, // hp
            new short[]{264, 265, 266}, // outfit
            new short[]{168, 169, 170}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _3_PHUT);
    //
    public static final BossData BO_XUONG_1 = new BossData(
            "Bộ xương 1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            5000, // dame
            new int[][]{{1000}}, // hp
            new short[]{545, 548, 549}, // outfit
            new short[]{5, 29, 6, 28, 27}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_PHUT);
    public static final BossData BO_XUONG_2 = new BossData(
            "Bộ xương 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            5000, // dame
            new int[][]{{1000}}, // hp
            new short[]{547, 548, 549}, // outfit
            new short[]{13, 33, 31, 32, 34, 10}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_PHUT);
    public static final BossData BO_XUONG_3 = new BossData(
            "Bộ xương 3", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            5000, // dame
            new int[][]{{1000}}, // hp
            new short[]{546, 548, 549}, // outfit
            new short[]{20, 35, 36, 37, 38, 19}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_PHUT);
    public static final BossData THO_DAI_CA = new BossData(
            "Thỏ đại ca", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new int[][]{{100000000}}, // hp
            new short[]{403, 404, 405}, // outfit
            new short[]{42, 43, 44}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _3_PHUT);
    public static final BossData SIEU_BO_HUNG = new BossData(
            "Siêu bọ hung", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            25000, // dame
            new int[][]{{1_800_000_000}}, // hp
            new short[]{234, 235, 236}, // outfit
            new short[]{103}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _0_GIAY);
    // --------------------------------------------------------------------------Boss
    // Đường tăng
    public static final BossData DUONG_TANG = new BossData(
            "Đường tăng", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{50_000_000}}, // hp
            new short[]{467, 468, 469}, // outfit
            new short[]{122, 123, 124}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _3_PHUT);
    public static final BossData NGO_KHONG_EVENT = new BossData(
            "Ngộ Không", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{50_000_000}}, // hp
            new short[]{462, 463, 464}, // outfit
            new short[]{122, 123, 124}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData RONG_NHI_1SAO = new BossData(
            "Rồng nhí 1 Sao", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1_800_000_000}}, // hp
            new short[]{1528, 1529, 1530}, // outfit
            new short[]{10, 11, 12, 13, 17, 18, 19, 24, 25, 26, 27, 28, 29, 30, 31, 32}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData RONG_NHI_2SAO = new BossData(
            "Rồng nhí 2 Sao", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1_200_000_000}}, // hp
            new short[]{1531, 1532, 1533}, // outfit
            new short[]{10, 11, 12, 13, 17, 18, 19, 24, 25, 26, 27, 28, 29, 30, 31, 32}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData RONG_NHI_3SAO = new BossData(
            "Rồng nhí 3 Sao", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1_200_000_000}}, // hp
            new short[]{1534, 1535, 1536}, // outfit
            new short[]{10, 11, 12, 13, 17, 18, 19, 24, 25, 26, 27, 28, 29, 30, 31, 32}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData RONG_NHI_4SAO = new BossData(
            "Rồng nhí 4 Sao", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1_200_000_000}}, // hp
            new short[]{1537, 1538, 1539}, // outfit
            new short[]{10, 11, 12, 13, 17, 18, 19, 24, 25, 26, 27, 28, 29, 30, 31, 32}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData RONG_NHI_5SAO = new BossData(
            "Rồng nhí 5 Sao", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1_200_000_000}}, // hp
            new short[]{1540, 1541, 1542}, // outfit
            new short[]{10, 11, 12, 13, 17, 18, 19, 24, 25, 26, 27, 28, 29, 30, 31, 32}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData RONG_NHI_6SAO = new BossData(
            "Rồng nhí 6 Sao", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1_200_000_000}}, // hp
            new short[]{1543, 1544, 1545}, // outfit
            new short[]{10, 11, 12, 13, 17, 18, 19, 24, 25, 26, 27, 28, 29, 30, 31, 32}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData RONG_NHI_7SAO = new BossData(
            "Rồng nhí 7 Sao", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1_200_000_000}}, // hp
            new short[]{1546, 1547, 1548}, // outfit
            new short[]{10, 11, 12, 13, 17, 18, 19, 24, 25, 26, 27, 28, 29, 30, 31, 32}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);
    
     public static final BossData THO_CUNG_TRANG = new BossData(
            "Thỏ mặt trăng", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1_200_000_000}}, // hp
            new short[]{1667, 1668, 1669}, // outfit
            new short[]{189, 191}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);
    public static final BossData KHI_DOT_1 = new BossData(
            "Khỉ đột 01", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1000}}, // hp
            new short[]{192, 193, 194}, // outfit
            new short[]{42, 43, 44}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData KHI_DOT_2 = new BossData(
            "Khỉ đột 02", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1000}}, // hp
            new short[]{195, 193, 194}, // outfit
            new short[]{42, 43, 44}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData KHI_DOT_3 = new BossData(
            "Khỉ đột 03", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1000}}, // hp
            new short[]{196, 193, 194}, // outfit
            new short[]{42, 43, 44}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData BABY = new BossData(
            "Baby", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1_900_000_000}}, // hp
            new short[]{1549, 1550, 1551}, // outfit
            new short[]{10, 11, 12, 13, 17, 18, 19, 24, 25, 26, 27, 28, 29, 30, 31, 32}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData BABY_CADIC = new BossData(
            "Baby Cadíc", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{1_950_000_000}}, // hp
            new short[]{1552, 1553, 1554}, // outfit
            new short[]{10, 11, 12, 13, 17, 18, 19, 24, 25, 26, 27, 28, 29, 30, 31, 32}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData BABY_KHI_VANG = new BossData(
            "Baby khỉ vàng", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            100000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{1555, 1556, 1557}, // outfit
            new short[]{10, 11, 12, 13, 17, 18, 19, 24, 25, 26, 27, 28, 29, 30, 31, 32}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);
    // --------------------------------------------------------------------------Boss
    // nappa
    public static final BossData KUKU = new BossData(
            "Kuku", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{50_000_000}}, // hp
            new short[]{159, 160, 161}, // outfit
            new short[]{68, 69, 70, 71, 72}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);
    public static final BossData MAP_DAU_DINH = new BossData(
            "Mập đầu đinh", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new int[][]{{70_000_000}}, // hp
            new short[]{165, 166, 167}, // outfit
            new short[]{64, 65, 63, 66, 67}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);
    public static final BossData RAMBO = new BossData(
            "Rambo", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{100_000_000}}, // hp
            new short[]{162, 163, 164}, // outfit
            new short[]{73, 74, 75, 76, 77}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);

    // --------------------------------------------------------------------------Boss
    // cold
    public static final BossData BONG_BANG_GOLD = new BossData(
            "Băng Sơn Vàng", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            11000, // dame
            new int[][]{{500000000}}, // hp
            new short[]{1391, 1392, 1393}, // outfit
            new short[]{110}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);
    public static final BossData COOLER = new BossData(
            "Cooler", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            22000, // dame
            new int[][]{{1500000000}}, // hp
            new short[]{317, 318, 319}, // outfit
            new short[]{110}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData COOLER2 = new BossData(
            "Cooler 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            25000, // dame
            new int[][]{{2000000000}}, // hp
            new short[]{320, 321, 322}, // outfit
            new short[]{110}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    // ------------------------- Tiểu đội Bojack ---------------------------//
    public static final BossData BIDO = new BossData(
            "Bido", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{1500000000}}, // hp
            new short[]{335, 336, 337}, // outfit
            new short[]{4, 5, 6}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.MASENKO, 5, 1700}
            },
            _0_GIAY);
    public static final BossData KOGU = new BossData(
            "Kogu", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{1500000000}}, // hp
            new short[]{329, 330, 331}, // outfit
            new short[]{4, 5, 6}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.MASENKO, 5, 1700}
            },
            _0_GIAY);
    public static final BossData BUJIN = new BossData(
            "Bujin", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{1500000000}}, // hp
            new short[]{341, 342, 343}, // outfit
            new short[]{4, 5, 6}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.MASENKO, 5, 1700}
            },
            _0_GIAY);

    public static final BossData ZANGYA = new BossData(
            "Zangya", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{1500000000}}, // hp
            new short[]{332, 333, 334}, // outfit
            new short[]{4, 5, 6}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.MASENKO, 5, 1700},
                {Skill.TROI, 1, 10000}
            },
            _0_GIAY);
    public static final BossData BOJACK = new BossData(
            "Bojack", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{1500000000}}, // hp
            new short[]{323, 324, 325}, // outfit
            new short[]{4, 5, 6}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                {Skill.MASENKO, 5, 1700},
                {Skill.TROI, 1, 10000}
            },
            _10_PHUT);

    public static final BossData S_BOJACK = new BossData(
            "Super Bojack", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{2000000000}}, // hp
            new short[]{326, 327, 328}, // outfit
            new short[]{4, 5, 6}, // map join
            new int[][]{ // skill
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.TROI, 1, 10000}
            },
            _0_GIAY);
    ////////////////////////////////////////////////////////////////////////////////////////
    // --------------------------------------------------------------------------Tiểu
    // đội sát thủ
    public static final BossData SO4 = new BossData(
            "Số 4", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new int[][]{{160_000_000}}, // hp
            new short[]{168, 169, 170}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    public static final BossData SO3 = new BossData(
            "Số 3", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            17000, // dame
            new int[][]{{180_000_000}}, // hp
            new short[]{174, 175, 176}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData SO2 = new BossData(
            "Số 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{200_000_000}}, // hp
            new short[]{171, 172, 173}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData SO1 = new BossData(
            "Số 1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{220_000_000}}, // hp
            new short[]{177, 178, 179}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData TIEU_DOI_TRUONG = new BossData(
            "Tiểu đội trưởng", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            25000, // dame
            new int[][]{{250_000_000}}, // hp
            new short[]{180, 181, 182}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT, true);

    public static final BossData SO4_NAMEK = new BossData(
            "Số 4", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new int[][]{{450_000_000}}, // hp
            new short[]{168, 169, 170}, // outfit
            new short[]{8, 9, 10, 11, 12, 13, 25, 31, 32, 33, 34, 43}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    public static final BossData SO3_NAMEK = new BossData(
            "Số 3", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            17000, // dame
            new int[][]{{550_000_000}}, // hp
            new short[]{174, 175, 176}, // outfit
            new short[]{8, 9, 10, 11, 12, 13, 25, 31, 32, 33, 34, 43}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData SO2_NAMEK = new BossData(
            "Số 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{650_000_000}}, // hp
            new short[]{171, 172, 173}, // outfit
            new short[]{8, 9, 10, 11, 12, 13, 25, 31, 32, 33, 34, 43}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData SO1_NAMEK = new BossData(
            "Số 1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{750_000_000}}, // hp
            new short[]{177, 178, 179}, // outfit
            new short[]{8, 9, 10, 11, 12, 13, 25, 31, 32, 33, 34, 43}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData TIEU_DOI_TRUONG_NAMEK = new BossData(
            "Tiểu đội trưởng", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            25000, // dame
            new int[][]{{900_000_000}}, // hp
            new short[]{180, 181, 182}, // outfit
            new short[]{8, 9, 10, 11, 12, 13, 25, 31, 32, 33, 34, 43}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT, true);

    // --------------------------------------------------------------------------Fide
    // đại ca
    public static final BossData FIDE_DAI_CA_1 = new BossData(
            "Fide đại ca 1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{200_000_000}}, // hp
            new short[]{183, 184, 185}, // outfit
            new short[]{80}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData FIDE_DAI_CA_2 = new BossData(
            "Fide đại ca 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            22000, // dame
            new int[][]{{250_000_000}}, // hp
            new short[]{186, 187, 188}, // outfit
            new short[]{80}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData FIDE_DAI_CA_3 = new BossData(
            "Fide đại ca 3", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new int[][]{{300_000_000}}, // hp
            new short[]{189, 190, 191}, // outfit
            new short[]{80}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    // --------------------------------------------------------------------------
    public static final BossData BROLY_NEW = new BossData(
            "Broly", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            25000, // dame
            new int[][]{{500000000}}, // hp
            new short[]{291, 292, 293}, // outfit
            new short[]{0, 7, 14, 42, 43, 44}, // map
            // join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _0_GIAY // số giây nghỉ
    );

    public static final BossData SUPER_BROLY_NEW = new BossData(
            "Super Broly Huyền Thoại", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            70000, // dame
            new int[][]{{1200000000}}, // hp
            new short[]{294, 295, 296}, // outfit
            new short[]{0, 7, 14, 42, 43, 44}, // map
            // join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _10_PHUT);
    public static final BossData FIDE_DAI_CA_3_NEW = new BossData(
            "Fide đại ca", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new int[][]{{500000000}}, // hp
            new short[]{189, 190, 191}, // outfit
            new short[]{0, 7, 14, 42, 43, 44}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    public static final BossData FIDE_DAI_CA_GOLD_NEW = new BossData(
            "Fide vàng", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            65000, // dame
            new int[][]{{1200000000}}, // hp
            new short[]{502, 503, 504}, // outfit
            new short[]{0, 7, 14, 42, 43, 44}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    // --------------------------------------------------------------------------
    public static final BossData ANDROID_19 = new BossData(
            "Android 19", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{200_000_000}}, // hp
            new short[]{249, 250, 251}, // outfit
            new short[]{93}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData ANDROID_20 = new BossData(
            "Dr.Kôrê", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{220_000_000}}, // hp
            new short[]{255, 256, 257}, // outfit
            new short[]{93}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT,
            true);

    public static final BossData ANDROID_13 = new BossData(
            "Android 13", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new int[][]{{250_000_000}}, // hp
            new short[]{252, 253, 254}, // outfit
            new short[]{104}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);

    public static final BossData ANDROID_14 = new BossData(
            "Android 14", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            25000, // dame
            new int[][]{{250_000_000}}, // hp
            new short[]{246, 247, 248}, // outfit
            new short[]{104}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);

    public static final BossData ANDROID_15 = new BossData(
            "Android 15", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{250_000_000}}, // hp
            new short[]{261, 262, 263}, // outfit
            new short[]{104}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData PIC = new BossData(
            "Pic", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            22000, // dame
            new int[][]{{400_000_000}}, // hp
            new short[]{237, 238, 239}, // outfit
            new short[]{97}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData POC = new BossData(
            "Poc", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            22000, // dame
            new int[][]{{450_000_000}}, // hp
            new short[]{240, 241, 242}, // outfit
            new short[]{97}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData KINGKONG = new BossData(
            "King Kong", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            25000, // dame
            new int[][]{{500_000_000}}, // hp
            new short[]{243, 244, 245}, // outfit
            new short[]{97}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT, true);

    // --------------------------------------------------------------------------Boss
    // berus
    public static final BossData WHISTL = new BossData(
            "Whis Tương Lai", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            33000, // dame
            new int[][]{{2000000000}}, // hp
            new short[]{838, 839, 840}, // outfit
            new short[]{97, 98}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);
    public static final BossData WHIS = new BossData(
            "Thần Thiên Sứ", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            33000, // dame
            new int[][]{{2000000000}}, // hp
            new short[]{838, 839, 840}, // outfit
            new short[]{154}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData BILL = new BossData(
            "Thần Hủy Diệt", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            33000, // dame
            new int[][]{{2000000000}}, // hp
            new short[]{508, 509, 510}, // outfit
            new short[]{154}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);

    // --------------------------------------------------------------------------Boss
    // CHILLED
    public static final BossData CHILL = new BossData(
            "Chilled", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{1_500_000_000}}, // hp
            new short[]{1024, 1025, 1026}, // outfit
            new short[]{163}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData CHILL2 = new BossData(
            "Chilled 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{1_850_000_000}}, // hp
            new short[]{1021, 1022, 1023}, // outfit
            new short[]{163}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData LTN = new BossData(
            "Lý Cô Nương", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            300000, // dame
            new int[][]{{2000}}, // hp
            new short[]{1385, 1386, 1387}, // outfit
            new short[]{6}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData BULMA = new BossData(
            "Bulma Hồng", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            1000, // dame
            new int[][]{{2000}}, // hp
            new short[]{1095, 1096, 1097}, // outfit
            new short[]{44, 43, 42, 0, 1, 2, 3, 4, 5, 6, 27, 28, 29, 30, 7, 8,
                9, 10, 11, 12, 13, 31, 32, 33, 34, 14, 15, 16, 17, 18, 19, 20, 35, 36, 37, 38}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData POCTHO = new BossData(
            "Poc Bulma Đen", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            1000, // dame
            new int[][]{{2000}}, // hp
            new short[]{1101, 1102, 1103}, // outfit
            new short[]{44, 43, 42, 0, 1, 2, 3, 4, 5, 6, 27, 28, 29, 30, 7, 8,
                9, 10, 11, 12, 13, 31, 32, 33, 34, 14, 15, 16, 17, 18, 19, 20, 35, 36, 37, 38}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData CHICHITHO = new BossData(
            "Bulma Huỷ Diệt Đỏ", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            300000, // dame
            new int[][]{{1_500_000_000}}, // hp
            new short[]{1098, 1099, 1100}, // outfit
            new short[]{183}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);

    public static final BossData MABU_CMD = new BossData(
            "MABU", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            135000, // dame
            new int[][]{{20000000}}, // hp
            new short[]{297, 298, 299}, // outfit
            new short[]{122, 123, 124}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    // --------------------------------------------------------------------------Boss
    // CHILLED
    public static final BossData DR_LYCHEE = new BossData(
            "Dr Lychee", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{500_000_000}}, // hp
            new short[]{742, 743, 744}, // outfit
            new short[]{139, 140}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData HATCHIYACK = new BossData(
            "Hatchiyack", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            35000, // dame
            new int[][]{{1_200_000_000}}, // hp
            new short[]{639, 640, 641}, // outfit
            new short[]{139, 140}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    public static final BossData KARIN_KID = new BossData(
            "Karin Kid Lân", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            1000, // dame
            new int[][]{{50000000}}, // hp
            new short[]{1198, 1199, 1200}, // outfit
            new short[]{0, 7, 14}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT);
    public static final BossData QUY_LAO_NEW = new BossData(
            "Quy Lão", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{1200000000}}, // hp
            new short[]{554, 555, 556}, // outfit
            new short[]{47}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _0_GIAY);

    public static final BossData JACKY_CHUN_NEW = new BossData(
            "Jacky Chun", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new int[][]{{1_800_000_000}}, // hp
            new short[]{356, 357, 358}, // outfit
            new short[]{47}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 5, 1000}, {Skill.KAMEJOKO, 7, 1200},},
            _5_PHUT,
            true);
    // ############## MABU_NEW #################
    public static final BossData DRABUBRA_NEW = new BossData(
            "Drabura", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{200000000}}, // hp
            new short[]{418, 419, 420}, // outfit
            new short[]{114}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _1_PHUT);
    public static final BossData BUIBUI_NEW = new BossData(
            "Bui Bui", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{300000000}}, // hp
            new short[]{451, 452, 453}, // outfit
            new short[]{114}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _0_GIAY);
    
    public static final BossData YACON_NEW = new BossData(
            "Yacôn", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{300000000}}, // hp
            new short[]{415, 416, 417}, // outfit
            new short[]{114}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _0_GIAY);

    public static final BossData MABU_HOI_MAP = BossData.builder()
            .name("Mabư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(750000)
            .hp(new int[][]{{2000000000}})
            .outfit(new short[]{297, 298, 299})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData MAJIN_VEGETA_NEW = new BossData(
            "Vegeta Majin", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{700000000}}, // hp
            new short[]{1000, 1001, 1002}, // outfit
            new short[]{114}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _0_GIAY);

    public static final BossData MABU_1_NEW = new BossData(
            "Mabư", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{500000000}}, // hp
            new short[]{297, 298, 299}, // outfit
            new short[]{114}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _0_GIAY);
    public static final BossData MABU_2_NEW = new BossData(
            "Mabư Ốm", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new int[][]{{500000000}}, // hp
            new short[]{1319, 1320, 1321}, // outfit
            new short[]{114}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _0_GIAY);
    public static final BossData MABU_3_NEW = new BossData(
            "Super Bư", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            35000, // dame
            new int[][]{{1500000000}}, // hp
            new short[]{427, 428, 429}, // outfit
            new short[]{114}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _0_GIAY);
    public static final BossData MABU_4_NEW = new BossData(
            "Bư Tênk", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            35000, // dame
            new int[][]{{1200000000}}, // hp
            new short[]{439, 440, 441}, // outfit
            new short[]{114}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _0_GIAY);
    public static final BossData MABU_5_NEW = new BossData(
            "Bư Han", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            25000, // dame
            new int[][]{{1500000000}}, // hp
            new short[]{427, 428, 429}, // outfit
            new short[]{114}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _0_GIAY);
    public static final BossData MABU_6_NEW = new BossData(
            "Kid Bư", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            55000, // dame
            new int[][]{{2000000000}}, // hp
            new short[]{439, 440, 441}, // outfit
            new short[]{114}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                {Skill.DRAGON, 7, 7000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},},
            _0_GIAY);
    // ##################### KAMOREN #################
    public static final BossData KAMI = new BossData(
            "Kami", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            45000, // dame
            new int[][]{{1000_000_000}}, // hp
            new short[]{1287, 1288, 1289}, // outfit
            new short[]{196, 197, 198}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _0_GIAY);
    public static final BossData OREN = new BossData(
            "Oren", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{1400_000_000}}, // hp
            new short[]{1290, 1291, 1292}, // outfit
             new short[]{196, 197, 198}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _5_PHUT);
    public static final BossData KAMIOREN = new BossData(
            "Kamioren", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            50000, // dame
            new int[][]{{1_850_000_000}}, // hp
            new short[]{1293, 1294, 1295}, // outfit
             new short[]{196, 197, 198}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _0_GIAY);
    // ########## OTHER ##########
    public static final BossData HEC_QUYN_EVENT = new BossData(
            "Sói hẹc quyn", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            5000, // dame
            new int[][]{{5000000}}, // hp
            new short[]{394, 395, 396}, // outfit
            // BossFactory.MAP_APPEARED_QILIN, // map join
            new short[]{5, 14, 0, 7}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000},
                {Skill.DRAGON, 2, 1000},
                {Skill.DRAGON, 3, 1000},},
            _3_PHUT);
    // ########## OTHER ##########
    public static final BossData AN_TROM = new BossData(
            "Ăn trộm", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            5000, // dame
            new int[][]{{5000000}}, // hp
            new short[]{201, 202, 203}, // outfit
            // BossFactory.MAP_APPEARED_QILIN, // map join
            new short[]{5, 14, 0, 7}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000},
                {Skill.DRAGON, 2, 1000},
                {Skill.DRAGON, 3, 1000},},
            _3_PHUT);
    public static final BossData O_DO_EVENT = new BossData(
            "Ở dơ", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            5000, // dame
            new int[][]{{5000000}}, // hp
            new short[]{400, 401, 402}, // outfit
            // BossFactory.MAP_APPEARED_QILIN, // map join
            new short[]{5, 14, 0, 7}, // map join
            new int[][]{ // skill
                {Skill.DRAGON, 1, 1000},
                {Skill.DRAGON, 2, 1000},
                {Skill.DRAGON, 3, 1000},},
            _3_PHUT);
    public static final BossData XINBATO_EVENT = new BossData(
            "Xinbatô", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            5000, // dame
            new int[][]{{5000000}}, // hp
            new short[]{359, 360, 361}, // outfit
            // BossFactory.MAP_APPEARED_QILIN, // map join
            new short[]{5, 14, 0, 7}, // map join
            new int[][]{},
            _3_PHUT);
    public static final BossData TEST_DAME = new BossData(
            "Máy đo sức mạnh", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            0, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{1316, 1317, 1318}, // outfit
            new short[]{182, 183, 184}, // map join
            new int[][]{ // skill
            },
            _0_GIAY);
    //
    // public static final BossData BROLYDEN = new BossData(
    // "S.Broly Black", //name
    // ConstPlayer.XAYDA, //gender
    // Boss.DAME_NORMAL, //type dame
    // Boss.HP_NORMAL, //type hp
    // 300000, //dame
    // new int[][]{{1000000000}}, //hp
    // new short[]{1080, 1081, 1082}, //outfit
    // new short[]{14}, //map join
    // new int[][]{ //skill
    // {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
    // {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
    // {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4,
    // 1500}, {Skill.KAMEJOKO, 2, 1000},
    // {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
    // {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
    // {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
    // {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
    // {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    // },
    // _5_PHUT
    // );
    //
    // public static final BossData BROLYXANH = new BossData(
    // "S.Broly SNamếc", //name
    // ConstPlayer.XAYDA, //gender
    // Boss.DAME_NORMAL, //type dame
    // Boss.HP_NORMAL, //type hp
    // 300000, //dame
    // new int[][]{{1000000000}}, //hp
    // new short[]{1086, 1087, 1088}, //outfit
    // new short[]{14}, //map join
    // new int[][]{ //skill
    // {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
    // {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
    // {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4,
    // 1500}, {Skill.KAMEJOKO, 2, 1000},
    // {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
    // {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
    // {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
    // {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
    // {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    // },
    // _5_PHUT
    // );
    //
    // public static final BossData BROLYVANG = new BossData(
    // "S.Broly SSJ", //name
    // ConstPlayer.XAYDA, //gender
    // Boss.DAME_NORMAL, //type dame
    // Boss.HP_NORMAL, //type hp
    // 300000, //dame
    // new int[][]{{1000000000}}, //hp
    // new short[]{1083, 1084, 1085}, //outfit
    // new short[]{14}, //map join
    // new int[][]{ //skill
    // {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
    // {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
    // {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4,
    // 1500}, {Skill.KAMEJOKO, 2, 1000},
    // {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
    // {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
    // {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
    // {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
    // {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    // },
    // _5_PHUT
    // );
    //
    public static final BossData BLACKGOKU = new BossData(
            "Black Goku %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            35000, // dame
            new int[][]{{1_500_000_000}}, // hp
            new short[]{550, 551, 552}, // outfit
            new short[]{92, 93, 94}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _15_PHUT);

    public static final BossData SUPERBLACKGOKU = new BossData(
            "Super Black Goku %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            45000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{553, 551, 552}, // outfit
            new short[]{92, 93, 94}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _0_GIAY);
    // Zamasu new
    public static final BossData BLACK_NEW = new BossData(
            "Super Black Goku", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            45000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{553, 551, 552, 37}, // outfit
            new short[]{92, 93, 94}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _0_GIAY, true);

    public static final BossData ZAMASU_NEW = new BossData(
            "Zamasu", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new int[][]{{1_800_000_000}}, // hp
            new short[]{433, 904, 905}, // outfit
            new short[]{92, 93, 94}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _0_GIAY, true);

    public static final BossData SUPER_ZAMASU_NEW = new BossData(
            "Super Zamasu", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            50000, // dame
            new int[][]{{2_000_000_000}}, // hp
            new short[]{903, 904, 905}, // outfit
            new short[]{92, 93, 94}, // map join
            new int[][]{ // skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _0_GIAY);

            public static final BossData DRANOEL = new BossData(
                "Drabura Frost", // name
                ConstPlayer.XAYDA, // gender
                Boss.DAME_NORMAL, // type dame
                Boss.HP_NORMAL, // type hp
                300000, // dame
                new int[][]{{2_000_000_000}}, // hp
                new short[]{1207, 1208, 1209}, // outfit
                new short[]{199}, // map join
                new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 50000}
                },
                _15_PHUT);    
      public static final BossData UUB = new BossData(
                "Uub", // name
                ConstPlayer.XAYDA, // gender
                Boss.DAME_NORMAL, // type dame
                Boss.HP_NORMAL, // type hp
                300000, // dame
                new int[][]{{2_000_000_000}}, // hp
                new short[]{946, 947, 948}, // outfit
                new short[]{196, 198}, // map join
                new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 50000}
                },
                _15_PHUT);    
    public static final BossData HOA_HONG = BossData.builder()
            .name("Hoa Hồng")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new int[][]{{100}})
            .outfit(new short[]{706, 707, 708})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{})
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData SANTA_CLAUS = BossData.builder()
            .name("Ông già Nôen")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new int[][]{{500000}})
            .outfit(new short[]{657, 658, 659})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{})
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData QILIN = BossData.builder()
            .name("Chú Lùn Noel")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new int[][]{{5000000}})
        //     .outfit(new short[]{763, 764, 765})
            .outfit(new short[]{1415, 1416, 1417})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{})
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData MABU_MAP = BossData.builder()
            .name("Mabư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(750000)
            .hp(new int[][]{{2000000000}})
            .outfit(new short[]{297, 298, 299})
            .mapJoin(new short[]{5})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
        {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
        {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
        {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
        {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData SUPER_BU = BossData.builder()
            .name("Super Bư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new int[][]{{50000000}})
            .outfit(new short[]{427, 428, 429})
            .mapJoin(new short[]{114})
            .skillTemp(new int[][]{
        {Skill.DEMON, 1, 5000},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData BU_TENK = BossData.builder()
            .name("Bư Tênk")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new int[][]{{100000000}})
            .outfit(new short[]{439, 440, 441})
            .mapJoin(new short[]{114})
            .skillTemp(new int[][]{
        {Skill.DEMON, 1, 5000},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData DRABULA_TANG1 = BossData.builder()
            .name("Drabula")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new int[][]{{250000000}})
            .outfit(new short[]{418, 419, 420})
            .mapJoin(new short[]{114})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
        {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
        {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
        {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
        {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData DRABULA_TANG5 = BossData.builder()
            .name("Drabula")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new int[][]{{500000000}})
            .outfit(new short[]{418, 419, 420})
            .mapJoin(new short[]{119})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
        {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
        {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
        {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
        {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData DRABULA_TANG6 = BossData.builder()
            .name("Drabula")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new int[][]{{1500000000}})
            .outfit(new short[]{418, 419, 420})
            .mapJoin(new short[]{120})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
        {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
        {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
        {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
        {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData BUIBUI_TANG2 = BossData.builder()
            .name("BuiBui")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new int[][]{{700000000}})
            .outfit(new short[]{451, 452, 453})
            .mapJoin(new short[]{115})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
        {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
        {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
        {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
        {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData BUIBUI_TANG3 = BossData.builder()
            .name("BuiBui")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new int[][]{{1000000000}})
            .outfit(new short[]{451, 452, 453})
            .mapJoin(new short[]{117})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
        {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
        {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
        {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
        {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData CALICH_TANG5 = BossData.builder()
            .name("Ca Đíc")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new int[][]{{1500000000}})
            .outfit(new short[]{103, 16, 17})
            .mapJoin(new short[]{119})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
        {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
        {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
        {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
        {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData GOKU_TANG5 = BossData.builder()
            .name("Gôku")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new int[][]{{1500000000}})
            .outfit(new short[]{101, 1, 2})
            .mapJoin(new short[]{119})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
        {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
        {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
        {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
        {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData YACON_TANG4 = BossData.builder()
            .name("Yacôn")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new int[][]{{1200000000}})
            .outfit(new short[]{415, 416, 417})
            .mapJoin(new short[]{118})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
        {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
        {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
        {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
        {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
        {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData XEN_MAX = BossData.builder()
            .name("Xên Max")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new int[][]{{2_000_000_000}})
            .outfit(new short[]{1296, 1297, 1298})
            .mapJoin(new short[]{97, 98, 99})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}, {Skill.TAI_TAO_NANG_LUONG, 6, 30000}
    })
            .secondsRest(_20_PHUT)
            .build();

    public static final BossData SOI_HEC_QUYN = BossData.builder()
            .name("Sói Hẹc Quyn")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(40000)
            .hp(new int[][]{{10000000}})
            .outfit(new short[]{394, 395, 396})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData O_DO = BossData.builder()
            .name("Ở Dơ")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(50000)
            .hp(new int[][]{{50000000}})
            .outfit(new short[]{400, 401, 402})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData XINBATO = BossData.builder()
            .name("Xinbatô")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(60000)
            .hp(new int[][]{{100000000}})
            .outfit(new short[]{359, 360, 361})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData CHA_PA = BossData.builder()
            .name("Cha pa")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(65000)
            .hp(new int[][]{{200000000}})
            .outfit(new short[]{362, 363, 364})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData PON_PUT = BossData.builder()
            .name("Pon put")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(70000)
            .hp(new int[][]{{250000000}})
            .outfit(new short[]{365, 366, 367})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData CHAN_XU = BossData.builder()
            .name("Chan xư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(75000)
            .hp(new int[][]{{400000000}})
            .outfit(new short[]{371, 372, 373})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData TAU_PAY_PAY = BossData.builder()
            .name("Tàu Pảy Pảy")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(80000)
            .hp(new int[][]{{500000000}})
            .outfit(new short[]{92, 93, 94})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData YAMCHA = BossData.builder()
            .name("Yamcha")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(90000)
            .hp(new int[][]{{1000000000}})
            .outfit(new short[]{374, 375, 376})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData JACKY_CHUN = BossData.builder()
            .name("Jacky Chun")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(100000)
            .hp(new int[][]{{1200000000}})
            .outfit(new short[]{356, 357, 358})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData THIEN_XIN_HANG = BossData.builder()
            .name("Thiên Xin Hăng")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(150000)
            .hp(new int[][]{{1500000000}})
            .outfit(new short[]{368, 369, 370})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.THAI_DUONG_HA_SAN, 1, 15000}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData THIEN_XIN_HANG_CLONE = BossData.builder()
            .name("Thiên Xin Hăng")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(75000)
            .hp(new int[][]{{500000000}})
            .outfit(new short[]{368, 369, 370})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.THAI_DUONG_HA_SAN, 1, 15000}
    })
            .secondsRest(_0_GIAY)
            .build();
    public static final BossData LIU_LIU = BossData.builder()
            .name("Liu Liu")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new int[][]{{1_800_000_000}})
            .outfit(new short[]{397, 398, 399})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData NGO_KHONG = BossData.builder()
            .name("Tôn Ngộ Không")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new int[][]{{500000000}})
            .outfit(new short[]{462, 463, 464})
            .mapJoin(new short[]{124})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData BAT_GIOI = BossData.builder()
            .name("Chư Bát Giới")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new int[][]{{500000000}})
            .outfit(new short[]{465, 466, 467})
            .mapJoin(new short[]{124})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData FIDEGOLD = BossData.builder()
            .name("Fide Vàng  %1")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new int[][]{{1000000000}})
            .outfit(new short[]{502, 503, 504})
            .mapJoin(new short[]{6})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData CUMBER = BossData.builder()
            .name("Cumber")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new int[][]{{1800_000_000}})
            .outfit(new short[]{1281, 1282, 1283})
            .mapJoin(new short[]{193, 194, 195})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_15_PHUT)
            .build();

    public static final BossData CUMBER2 = BossData.builder()
            .name("Super Cumber")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new int[][]{{2_000_000_000}})
            .outfit(new short[]{1284, 1285, 1286})
            .mapJoin(new short[]{193, 194, 195})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();

            public static final BossData TUANLOC = BossData.builder()
            .name("Tuần Lộc %1")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(20000)
            .hp(new int[][]{{10000}})
            .outfit(new short[]{718, 719, 720})
            .mapJoin(new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38})
            .skillTemp(new int[][]{{Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500}})
            .secondsRest(_5_PHUT)
            .build();
    public static final BossData THAN_MEO = BossData.builder()
            .name("Thần Moè")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new int[][]{{200}})
            .outfit(new short[]{89, 90, 91})
            .mapJoin(new short[]{250})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
        {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
        {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_0_GIAY)
            .build();
}
