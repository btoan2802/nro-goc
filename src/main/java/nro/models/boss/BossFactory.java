package nro.models.boss;

import java.util.HashMap;
import nro.consts.ConstEvent;
import nro.consts.ConstMap;
import nro.models.boss.BillBiNgo.BillBiNgo;
import nro.models.boss.Bossbaby.Baby;
import nro.models.boss.Bossbaby.Babycadic;
import nro.models.boss.Bossbaby.Babykhivang;
import nro.models.boss.DaiHaiTrinh.*;
import nro.models.boss.Doraemon.Chaien;
import nro.models.boss.Doraemon.Doraemon;
import nro.models.boss.Doraemon.Nobita;
import nro.models.boss.Doraemon.Sizuka;
import nro.models.boss.Doraemon.Suneo;
import nro.models.boss.FideGold.BroLy_Green;
import nro.models.boss.bill.*;
import nro.models.boss.boss_bdkb_new.*;
import nro.models.boss.bosstuonglai.*;
import nro.models.boss.broly.*;
import nro.models.boss.cell.*;
import nro.models.boss.chill.*;
import nro.models.boss.cold.*;
import nro.models.boss.event.Dranoel;
import nro.models.boss.event.HoaHong;
import nro.models.boss.event.KarinKid;
import nro.models.boss.event.Qilin;
import nro.models.boss.event.SantaClaus;
import nro.models.boss.event.Tuanloc;
import nro.models.boss.event.Event15.XenConEvent;
import nro.models.boss.event.Event16.ThoDaiCa;
import nro.models.boss.event.Event17.*;
import nro.models.boss.event.NguHanhSon.DuongTang;
import nro.models.boss.event.NguHanhSon.NgoKhong;
import nro.models.boss.Game.SoiHecQuyn;
import nro.models.boss.Game.Xinbato;
import nro.models.boss.fide.*;
import nro.models.boss.mabu_planet.*;
import nro.models.boss.mabu_war.*;
import nro.models.boss.nappa.*;
import nro.models.boss.quy_lao.JackyChun;
import nro.models.boss.quy_lao.QuyLao;
import nro.models.boss.robotsatthu.*;
import nro.models.boss.testdame.Maydosucmanh;
import nro.models.boss.tieudoisatthu.*;
import nro.models.boss.tieudoisatthu_nm.*;

import nro.models.boss.xencon.*;
import nro.models.boss.uub.uub;

import nro.models.boss.FideGold.Fide_Gold;
import nro.models.boss.FideGold.Fide_New;
import nro.models.boss.FideGold.Super_Broly_Green;
import nro.models.boss.HatchiJack.Dr_Lychee;
import nro.models.boss.HatchiJack.Hatchiyack;
import nro.models.boss.KamiOren.Kami;
import nro.models.boss.KamiOren.Kamioren;
import nro.models.boss.KamiOren.Oren;
import nro.models.boss.Khidot.Khidot1;
import nro.models.boss.Khidot.Khidot2;
import nro.models.boss.Khidot.Khidot3;
import nro.models.boss.NgucTu.*;
import nro.models.boss.PiLap.Mai;
import nro.models.boss.PiLap.PiLap;
import nro.models.boss.PiLap.Su;
import nro.models.boss.Rongnhi.Rongnhi1;
import nro.models.boss.Rongnhi.Rongnhi2;
import nro.models.boss.Rongnhi.Rongnhi3;
import nro.models.boss.Rongnhi.Rongnhi4;
import nro.models.boss.Rongnhi.Rongnhi5;
import nro.models.boss.Rongnhi.Rongnhi6;
import nro.models.boss.Rongnhi.Rongnhi7;
import nro.models.boss.Rongnhi.Thocungtrang;
import nro.models.boss.SuperZamasu.*;
import nro.models.boss.tieudoiBoJack.*;
import nro.models.boss.traidat.BULMA;
import nro.models.boss.traidat.LyTieuNuong;
import nro.models.boss.traidat.POCTHO;

import nro.models.map.Map;
import nro.models.map.Zone;
import nro.models.map.mabu.MabuWar;
import nro.server.Manager;
import nro.services.MapService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.Util;

import org.apache.log4j.Logger;

public class BossFactory {

    // id boss
    public static final short SUPER_BROLY = -2;
    public static final short TRUNG_UY_TRANG = -3;
    public static final short TRUNG_UY_XANH_LO = -4;
    public static final short TRUNG_UD_THEP = -5;
    public static final short NINJA_AO_TIM = -6;
    public static final short NINJA_AO_TIM_FAKE_1 = -7;
    public static final short NINJA_AO_TIM_FAKE_2 = -8;
    public static final short NINJA_AO_TIM_FAKE_3 = -9;
    public static final short NINJA_AO_TIM_FAKE_4 = -10;
    public static final short NINJA_AO_TIM_FAKE_5 = -11;
    public static final short NINJA_AO_TIM_FAKE_6 = -12;
    public static final short ROBOT_VE_SI_1 = -13;
    public static final short ROBOT_VE_SI_2 = -14;
    public static final short ROBOT_VE_SI_3 = -15;
    public static final short ROBOT_VE_SI_4 = -16;
    public static final short XEN_BO_HUNG_1 = -17;
    public static final short XEN_BO_HUNG_2 = -18;
    public static final short XEN_BO_HUNG_HOAN_THIEN = -19;
    public static final short XEN_BO_HUNG = -20;
    public static final short XEN_CON = -21;
    public static final short SIEU_BO_HUNG = -22;
    public static final short KUKU = -23;
    public static final short MAP_DAU_DINH = -24;
    public static final short RAMBO = -25;
    public static final short COOLER = -26;
    public static final short COOLER2 = -27;
    public static final short SO4 = -28;
    public static final short SO3 = -29;
    public static final short SO2 = -30;
    public static final short SO1 = -31;
    public static final short TIEU_DOI_TRUONG = -32;
    public static final short FIDE_DAI_CA_1 = -33;
    public static final short FIDE_DAI_CA_2 = -34;
    public static final short FIDE_DAI_CA_3 = -35;
    public static final short ANDROID_19 = -36;
    public static final short ANDROID_20 = -37;
    public static final short ANDROID_13 = -38;
    public static final short ANDROID_14 = -39;
    public static final short ANDROID_15 = -40;
    public static final short PIC = -41;
    public static final short POC = -42;
    public static final short KINGKONG = -43;
    public static final short SUPER_BROLY_RED = -44;
    public static final short LUFFY = -45;
    public static final short ZORO = -46;
    public static final short SANJI = -47;
    public static final short USOPP = -48;
    public static final short FRANKY = -49;
    public static final short BROOK = -50;
    public static final short NAMI = -51;
    public static final short CHOPPER = -52;
    public static final short ROBIN = -53;
    public static final short WHIS = -54;
    public static final short WHISTL = -594;
    public static final short BILL = -55;
    public static final short CHILL = -56;
    public static final short CHILL2 = -57;
    public static final short BULMA = -58;
    public static final short POCTHO = -59;
    public static final short CHICHITHO = -60;
    public static final short BLACKGOKU = -61;
    public static final short SUPERBLACKGOKU = -62;
    public static final short SANTA_CLAUS = -63;
    public static final short MABU_MAP = -64;
    public static final short SUPER_BU = -65;
    public static final short BU_TENK = -66;
    public static final short DRABULA_TANG1 = -67;
    public static final short BUIBUI_TANG2 = -68;
    public static final short BUIBUI_TANG3 = -69;
    public static final short YACON_TANG4 = -70;
    public static final short DRABULA_TANG5 = -71;
    public static final short GOKU_TANG5 = -72;
    public static final short CADIC_TANG5 = -73;
    public static final short DRABULA_TANG6 = -74;
    public static final short XEN_MAX = -75;
    public static final short HOA_HONG = -76;
    public static final short SOI_HEC_QUYN = -77;
    public static final short O_DO = -78;
    public static final short XINBATO = -79;
    public static final short CHA_PA = -80;
    public static final short PON_PUT = -81;
    public static final short CHAN_XU = -82;
    public static final short TAU_PAY_PAY = -83;
    public static final short YAMCHA = -84;
    public static final short JACKY_CHUN = -85;
    public static final short THIEN_XIN_HANG = -86;
    public static final short LIU_LIU = -87;
    public static final short THIEN_XIN_HANG_CLONE = -88;
    public static final short THIEN_XIN_HANG_CLONE1 = -89;
    public static final short THIEN_XIN_HANG_CLONE2 = -90;
    public static final short THIEN_XIN_HANG_CLONE3 = -91;
    public static final short QILIN = -92;
    public static final short NGO_KHONG = -93;
    public static final short BAT_GIOI = -94;
    public static final short FIDEGOLD = -95;
    public static final short CUMBER = -96;
    public static final short CUMBER2 = -97;
    public static final short S_ZMAS = -98;
    public static final short RAITI_TD = -99;
    public static final short RAITI_NM = -100;
    public static final short RAITI_XD = -101;
    public static final short DUONG_TANK = -102;
    public static final short THAN_MEO = -103;
    public static final short DR_LYCHEE = -104;
    public static final short HATCHIYACK = -105;

    public static final short KOGU = -106;
    public static final short BOJACK = -107;
    public static final short ZANGYA = -108;
    public static final short BIDO = -109;
    public static final short BUJIN = -110;
    public static final short S_BOJACK = -111;

    public static final short BROLY = -112;
    public static final short WHIS_CLONE1 = -113;
    public static final short BILL_CLONE1 = -114;

    public static final short WHIS_CLONE2 = -115;
    public static final short BILL_CLONE2 = -116;

    public static final short Mai = -117;
    public static final short Su = -118;
    public static final short PiLap = -119;

    public static final short SO4_NAMEK = -120;
    public static final short SO3_NAMEK = -121;
    public static final short SO2_NAMEK = -122;
    public static final short SO1_NAMEK = -123;
    public static final short TIEU_DOI_TRUONG_NAMEK = -124;
    public static final short BONG_BANG_GOLD = -125;
    public static final short FIDE_NEW = -126;
    public static final short FIDE_GOLD_NEW = -127;
    public static final short BROLY_NEW = -128;
    public static final short BROLY_LEGEND_NEW = -129;
    public static final short ZAMASU_NEW = -130;
    public static final short BLACK_GOKU_NEW = -131;
    public static final short SUPER_ZAMASU_NEW = -132;
    public static final short KARIN_KID = -133;
    public static final short XEN_CON_1 = -134;
    public static final short XEN_CON_2 = -135;
    public static final short XEN_CON_3 = -136;
    public static final short XEN_CON_4 = -137;
    public static final short XEN_CON_5 = -138;
    public static final short XEN_CON_6 = -139;
    public static final short XEN_CON_7 = -140;
    public static final short BILL_BI_NGO = -141;

    public static final short QUY_LAO_NEW = -142;
    public static final short JAYKY_CHUN_NEW = -143;

    public static final short DRABUBRA_NEW = -144;
    public static final short BUIBUI_NEW = -145;
    public static final short YACON_NEW = -146;
    public static final short MAJIN_VEGETA_NEW = -147;

    public static final short MABU_1 = -148;
    public static final short MABU_2 = -149;
    public static final short MABU_3 = -150;
    public static final short MABU_4 = -151;
    public static final short MABU_5 = -152;
    public static final short MABU_6 = -153;

    public static final short TRUNG_UY_XANH_LO_NEW = -154;
    public static final short TRUNG_UY_THEP_NEW = -155;
    public static final short ROBOT_VE_SI_NEW = -156;
    public static final short XEN_CON_EVENT = -157;

    public static final short HEC_QUYN_EVENT = -158;
    public static final short O_DO_EVENT = -159;
    public static final short XINBATO_EVENT = -160;
    public static final short BOSS_EVENT_TRUNG_THU = -161;
    public static final short DUONG_TANG = -162;
    //
    public static final short KAMI = -163;
    public static final short OREN = -164;
    public static final short KAMIOREN = -165;
    public static final short NGO_KHONG_EVENT = -166;
    //
    public static final short LUFFY_NEW = -167;
    public static final short ZORO_NEW = -168;
    public static final short SANJI_NEW = -169;
    public static final short USOPP_NEW = -170;
    public static final short FRANKY_NEW = -171;
    public static final short BROOK_NEW = -172;
    public static final short NAMI_NEW = -173;
    public static final short CHOPPER_NEW = -174;
    public static final short ROBIN_NEW = -175;
    public static final short HON_MA = -176;
    //
    public static final short DORAEMON = -177;
    public static final short NOBITA = -178;
    public static final short SIZUKA = -179;
    public static final short CHAIEN = -180;
    public static final short SUNEO = -181;
    //
    public static final short BO_XUONG_1 = -182;
    public static final short BO_XUONG_2 = -183;
    public static final short BO_XUONG_3 = -184;
    public static final short LTN = -185;

    public static final short RONG_NHI_1SAO = -186;
    public static final short RONG_NHI_2SAO = -187;
    public static final short RONG_NHI_3SAO = -188;
    public static final short RONG_NHI_4SAO = -189;
    public static final short RONG_NHI_5SAO = -190;
    public static final short RONG_NHI_6SAO = -191;
    public static final short RONG_NHI_7SAO = -192;

    public static final short BABY = -193;
    public static final short BABY_CADIC = -194;
    public static final short BABY_KHI_VANG = -195;
    public static final short AN_TROM = -196;

    public static final short THO_CUNG_TRANG = -197;
    public static final short KHI_DOT_1 = -198;
    public static final short KHI_DOT_2 = -199;
    public static final short KHI_DOT_3 = -200;

    public static final short UUB = -201;
    public static final short TUANLOC = -202;
    public static final short DRANOEL = -203;

    public static boolean isInitFideGold;
    private static final Logger logger = Logger.getLogger(BossFactory.class);

    public static final short[] MAP_APPEARED_QILIN = {ConstMap.VACH_NUI_ARU_42, ConstMap.VACH_NUI_MOORI_43,
        ConstMap.VACH_NUI_KAKAROT,
        ConstMap.LANG_ARU, ConstMap.LANG_MORI, ConstMap.LANG_KAKAROT
        
    };

    private BossFactory() {

    }

    public static boolean isYar(byte id) {
        return (id == GOKU_TANG5
                || id == CADIC_TANG5 || id == MABU_MAP || id == SUPER_BROLY
                || id == Su || id == Mai || id == PiLap || id == MABU_MAP || id == RONG_NHI_1SAO
                || id == RONG_NHI_2SAO || id == XINBATO_EVENT || id == HEC_QUYN_EVENT
                | id == TRUNG_UY_XANH_LO_NEW | id == TRUNG_UY_THEP_NEW
                || id == RONG_NHI_3SAO || id == RONG_NHI_4SAO || id == RONG_NHI_5SAO
                || id == RONG_NHI_6SAO || id == RONG_NHI_7SAO || id == SUPER_BROLY || id == YACON_TANG4
                || id == BUIBUI_TANG2 || id == BUIBUI_TANG2 || id == BU_TENK|| id == BUIBUI_NEW || id == BUIBUI_TANG2 || id == BUIBUI_TANG3 || id == SUPER_BU
                || id == DRABULA_TANG1 || id == DRABULA_TANG6 || id == DRABULA_TANG5 || id == BROLY);
    }

    public static void initBoss() {
        new Thread(() -> {
            try {
//                 createBoss(BONG_BANG_GOLD);
//                createBoss(PiLap);
//                createBoss(RONG_NHI_7SAO);
//                createBoss(RONG_NHI_6SAO);
//                createBoss(RONG_NHI_5SAO);
//                createBoss(RONG_NHI_4SAO);
//                createBoss(RONG_NHI_3SAO);
//                createBoss(RONG_NHI_2SAO);
//                createBoss(RONG_NHI_1SAO);
//========================== RƠI BÙA BẢO NGỌC =========================
                createBoss(BABY);
                createBoss(BABY_CADIC);
                createBoss(BABY_KHI_VANG);
                 createBoss(WHISTL);
                createBoss(COOLER);
//============================= NHIỆM VỤ ======================================
                createBoss(KUKU);
                createBoss(MAP_DAU_DINH);
                createBoss(RAMBO);
                
               
                
                createBoss(TIEU_DOI_TRUONG);
                
                createBoss(FIDE_DAI_CA_1);
                
                createBoss(BOJACK);
                
                createBoss(ANDROID_15);
                
                createBoss(ANDROID_20);
                
                createBoss(KINGKONG);
                
                createBoss(XEN_BO_HUNG_1);
                
                createBoss(XEN_BO_HUNG);
                
                createBoss(XEN_MAX);
                createBoss(XEN_MAX);
                
                createBoss(BLACKGOKU);
                
                createBoss(CHILL);
                
                createBoss(WHIS);
                
                createBoss(CUMBER);
                createBoss(TUANLOC);
                createBoss(TUANLOC);
                createBoss(TUANLOC);
                createBoss(TUANLOC);
                createBoss(TUANLOC);
                createBoss(TUANLOC);
                createBoss(TUANLOC);
                createBoss(TUANLOC);
                createBoss(TUANLOC);
                createBoss(TUANLOC);
                // createBoss(SANTA_CLAUS);
                // createBoss(S_ZMAS);
//                createBoss(SUPER_BROLY);
//                createBoss(SUPER_BROLY);
//                createBoss(SUPER_BROLY);
//                createBoss(SUPER_BROLY);
//                createBoss(SUPER_BROLY);
//                createBoss(SUPER_BROLY);
//                createBoss(SUPER_BROLY);
                // createBoss(SUPER_BROLY);
//                createBoss(UUB);
//                createBoss(KHI_DOT_1);
//                createBoss(KHI_DOT_2);
//                createBoss(KHI_DOT_3);
//                createBoss(THO_CUNG_TRANG);


//                createBoss(JAYKY_CHUN_NEW);
//                createBoss(YACON_NEW);
//                createBoss(BOSS_EVENT_TRUNG_THU);
//                createBoss(BOSS_EVENT_TRUNG_THU);
//                createBoss(BOSS_EVENT_TRUNG_THU);
//                createBoss(BOSS_EVENT_TRUNG_THU);
//                createBoss(BOSS_EVENT_TRUNG_THU);
//                createBoss(BOSS_EVENT_TRUNG_THU);
//                createBoss(BOSS_EVENT_TRUNG_THU);

                // createBoss(KARIN_KID);
                // createBoss(DR_LYCHEE);
//                createBoss(ROBOT_VE_SI_NEW);
//                createBoss(BO_XUONG_1);
//                   createBoss(BO_XUONG_2);
//                   createBoss(BO_XUONG_3);
                //    createBoss(HON_MA);
                createBoss(TIEU_DOI_TRUONG_NAMEK);
                // createBoss(DUONG_TANG);
//                 createBoss(OREN);
//                createBoss(BILL_BI_NGO);
//                createBoss(LUFFY_NEW);
//                createBoss(LTN);
//                createBoss(BULMA);
//                createBoss(POCTHO);

                // EVENT
//                createBoss(HEC_QUYN_EVENT);
//                createBoss(XINBATO_EVENT);
                switch (Manager.EVENT_SEVER) {
//                    case ConstEvent.SU_KIEN_HE_2024:
//                        createBoss(XEN_CON_EVENT);
//                        break;
//                    case ConstEvent.SU_KIEN_TRUNG_THU_2024:
//                        createBoss(BOSS_EVENT_TRUNG_THU);
//                        createBoss(BOSS_EVENT_TRUNG_THU);
//                        createBoss(BOSS_EVENT_TRUNG_THU);
//                        createBoss(BOSS_EVENT_TRUNG_THU);
//                        createBoss(BOSS_EVENT_TRUNG_THU);
//                        break;
//                   case ConstEvent.SU_KIEN_HALLOWEEN_2024:
//                   
//                       break;
                }
                // for (int i = 0; i < 5; i++) {
                // createBoss(SUPER_BROLY);
                // }
                // for (Map map : Manager.MAPS) {
                // if (map != null && !map.zones.isEmpty()) {
                // if (!map.isMapOffline && map.type == ConstMap.MAP_NORMAL
                // && map.tileId > 0 && !MapService.gI().isMapVS(map.mapId)) {
                // if (map.mapWidth > 50 && map.mapHeight > 50) {
                // if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_20_11) {
                // new HoaHong(map.mapId);
                // }
                // if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_NOEL) {
                // new SantaClaus(map.mapId);
                // }
                // }
                // }
                // }
                // }
                // if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_TET) {
                // for (int mapID : MAP_APPEARED_QILIN) {
                // new Qilin(mapID);
                // }
                // }
                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_NOEL) {
                for (int mapID : MAP_APPEARED_QILIN) {
                new Qilin(mapID);
                }
                }
                createBoss(DRANOEL);
                createBossTestDame();
            } catch (Exception e) {
                logger.error("Err initboss", e);
            }
        }).start();
    }

    public static void initBossFideGold() {
        if (!BossFactory.isInitFideGold && ChangeMapService.gI().TimeBossFideGold()) {
            BossFactory.isInitFideGold = true;
            createBoss(FIDEGOLD);
        }

    }

    public static void setActiveFideGold() {
        BossFactory.isInitFideGold = false;
    }

    public static void initBossMabuWar() {
        new Thread(() -> {
            createBoss(YACON_NEW);
            createBoss(MABU_1);
            for (short mapid : BossData.DRABULA_TANG1.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Drabula_Tang1(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.DRABULA_TANG6.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Drabula_Tang6(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.GOKU_TANG5.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Goku_Tang5(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.CALICH_TANG5.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Calich_Tang5(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.BUIBUI_TANG2.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new BuiBui_Tang2(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.BUIBUI_TANG3.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new BuiBui_Tang3(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.YACON_TANG4.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Yacon_Tang4(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
        }).start();
    }

    public static void createBossAffterLeaveMap(short bossId, boolean setJustRest) {
        if (!Manager.is_reload_boss) {
            if (setJustRest) {
                createBoss(bossId).setJustRest();
            } else {
                createBoss(bossId);
            }

        }
    }

    public static Boss createBoss(short bossId) {
        Boss boss = null;
        switch (bossId) {
            case POCTHO:
                boss = new POCTHO();
                break;
            case BULMA:
                boss = new BULMA();
                break;
            case LTN:
                boss = new LyTieuNuong();
                break;
            case BONG_BANG_GOLD:
                boss = new BongBangGold();
                break;
            case Mai:
                boss = new Mai();
                break;
            case Su:
                boss = new Su();
                break;
            case PiLap:
                boss = new PiLap();
                break;
            case BABY:
                boss = new Baby();
                break;
            case BABY_CADIC:
                boss = new Babycadic();
                break;
            case BABY_KHI_VANG:
                boss = new Babykhivang();
                break;
            case BILL_CLONE1:
                boss = new BillClone1();
                break;
            case BILL_CLONE2:
                boss = new BillClone2();
                break;
            case WHIS_CLONE1:
                boss = new whisClone1();
                break;
            case WHIS_CLONE2:
                boss = new whisclone2();
                break;
            case S_BOJACK:
                boss = new S_Bojack();
                break;
            case BOJACK:
                boss = new BoJack();
                break;
            case BIDO:
                boss = new Bido();
                break;
            case RONG_NHI_1SAO:
                boss = new Rongnhi1();
                break;
            case RONG_NHI_2SAO:
                boss = new Rongnhi2();
                break;
            case RONG_NHI_3SAO:
                boss = new Rongnhi3();
                break;
            case RONG_NHI_4SAO:
                boss = new Rongnhi4();
                break;
            case RONG_NHI_5SAO:
                boss = new Rongnhi5();
                break;
            case RONG_NHI_6SAO:
                boss = new Rongnhi6();
                break;
            case RONG_NHI_7SAO:
                boss = new Rongnhi7();
                break;

            case BUJIN:
                boss = new BuJin();
                break;
            case KOGU:
                boss = new Kogu();
                break;
            case ZANGYA:
                boss = new Zangya();
                break;
            case S_ZMAS:
                boss = new SuperZamax();
                break;
            case BROLY:
                boss = new Broly();
                break;
            case SUPER_BROLY:
                boss = new SuperBroly();
                break;
            case XEN_BO_HUNG_1:
                boss = new XenBoHung1();
                break;
            case XEN_BO_HUNG_2:
                boss = new XenBoHung2();
                break;
            case XEN_BO_HUNG_HOAN_THIEN:
                boss = new XenBoHungHoanThien();
                break;
            case XEN_BO_HUNG:
                boss = new XenBoHung();
                break;
            case XEN_CON:
                boss = new XenCon();
                break;
            case SIEU_BO_HUNG:
                boss = new SieuBoHung();
                break;
            case KUKU:
                boss = new Kuku();
                break;
            case MAP_DAU_DINH:
                boss = new MapDauDinh();
                break;
            case RAMBO:
                boss = new Rambo();
                break;
            case COOLER:
                boss = new Cooler();
                break;
            case COOLER2:
                boss = new Cooler2();
                break;
            case SO4:
                boss = new So4();
                break;
            case SO3:
                boss = new So3();
                break;
            case SO2:
                boss = new So2();
                break;
            case SO1:
                boss = new So1();
                break;
            case TIEU_DOI_TRUONG:
                boss = new TieuDoiTruong();
                break;
            case SO4_NAMEK:
                boss = new So4Namek();
                break;
            case SO3_NAMEK:
                boss = new So3Namek();
                break;
            case SO2_NAMEK:
                boss = new So2Namek();
                break;
            case SO1_NAMEK:
                boss = new So1Namek();
                break;
            case TIEU_DOI_TRUONG_NAMEK:
                boss = new TieuDoiTruongNamek();
                break;
            case FIDE_DAI_CA_1:
                boss = new FideDaiCa1();
                break;
            case FIDE_DAI_CA_2:
                boss = new FideDaiCa2();
                break;
            case FIDE_DAI_CA_3:
                boss = new FideDaiCa3();
                break;
            case ANDROID_19:
                boss = new Android19();
                break;
            case ANDROID_20:
                boss = new Android20();
                break;
            case SUPER_BROLY_RED:
                boss = new SuperBrolyRed();
                break;
            case ANDROID_15:
                boss = new Android_15();
                break;
            case ANDROID_14:
                boss = new Android_14();
                break;
            case ANDROID_13:
                boss = new Android_13();
                break;
            case POC:
                boss = new Poc();
                break;
            case PIC:
                boss = new Pic();
                break;
            case KINGKONG:
                boss = new KingKong();
                break;
            case WHIS:
                boss = new Whis();
                break;
            case WHISTL:
                boss = new WhisTL();
                break;
            case BILL:
                boss = new Bill();
                break;
            case CHILL:
                boss = new Chill();
                break;
            case CHILL2:
                boss = new Chill2();
                break;
            case HEC_QUYN_EVENT:
                boss = new SoiHecQuyn();
                break;
            case XINBATO_EVENT:
                boss = new Xinbato();
                break;
            // case BROLYDEN:
            // boss = new Brolyden();
            // break;
            // case BROLYXANH:
            // boss = new Brolyxanh();
            // break;
            // case BROLYVANG:
            // boss = new Brolyvang();
            // break;
            case UUB:
                boss = new uub();
                break;
            case BLACKGOKU:
                boss = new Blackgoku();
                break;
            case SUPERBLACKGOKU:
                boss = new Superblackgoku();
                break;
            case MABU_MAP:
                boss = new Mabu_Tang6();
                break;
            case XEN_MAX:
                boss = new XenMax();
                break;

                 case DRANOEL:
                boss = new Dranoel();
                break;
            case FIDEGOLD:
                boss = new FideGold();
                break;
            case CUMBER:
                boss = new Cumber();
                break;
            case CUMBER2:
                boss = new SuperCumber();
                break;
            case DR_LYCHEE:
                boss = new Dr_Lychee();
                break;
            case HATCHIYACK:
                boss = new Hatchiyack();
                break;
            case FIDE_NEW:
                boss = new Fide_New();
                break;
            case FIDE_GOLD_NEW:
                boss = new Fide_Gold();
                break;
            case BROLY_NEW:
                boss = new BroLy_Green();
                break;
            case BROLY_LEGEND_NEW:
                boss = new Super_Broly_Green();
                break;
            case ZAMASU_NEW:
                boss = new ZamasuNew();
                break;
            case BLACK_GOKU_NEW:
                boss = new SuperBlackNew();
                break;
            case SUPER_ZAMASU_NEW:
                boss = new SuperZamasuNew();
                break;
            case KARIN_KID:
                boss = new KarinKid();
                break;
            case XEN_CON_1:
                boss = new XenCon1();
                break;
            case XEN_CON_2:
                boss = new XenCon2();
                break;
            case XEN_CON_3:
                boss = new XenCon3();
                break;
            case XEN_CON_4:
                boss = new XenCon4();
                break;
            case XEN_CON_5:
                boss = new XenCon5();
                break;
            case XEN_CON_6:
                boss = new XenCon6();
                break;
            case XEN_CON_7:
                boss = new XenCon7();
                break;
            case JAYKY_CHUN_NEW:
                boss = new JackyChun();
                break;
            case KHI_DOT_1:
                boss = new Khidot1();
                break;
            case KHI_DOT_2:
                boss = new Khidot2();
                break;
               case BILL_BI_NGO:
                boss = new BillBiNgo();
                break;
            case THO_CUNG_TRANG:
                boss = new Thocungtrang();
                break;
            case KHI_DOT_3:
                boss = new Khidot3();
                break;
            case QUY_LAO_NEW:
                boss = new QuyLao();
                break;
            case DRABUBRA_NEW:
                boss = new Drabubra();
                break;
            case BUIBUI_NEW:
                boss = new Buibui();
                break;
            case YACON_NEW:
                boss = new Yacon();
                break;
            case MAJIN_VEGETA_NEW:
                boss = new VegataMajin();
                break;
            case MABU_1:
                boss = new Mabu_1();
                break;
            case MABU_2:
                boss = new Mabu_2();
                break;
            case MABU_3:
                boss = new Mabu_3();
                break;
            case MABU_4:
                boss = new Mabu_4();
                break;
            case MABU_5:
                boss = new Mabu_5();
                break;
            case MABU_6:
                boss = new Mabu_6();
                break;
            case ROBOT_VE_SI_NEW:
                boss = new RobotVeSiNew();
                break;
            case TRUNG_UY_THEP_NEW:
                boss = new TrungUyThepNew();
                break;
            case TRUNG_UY_XANH_LO_NEW:
                boss = new TrungUyXanhLoNew();
                break;
            case XEN_CON_EVENT:
                boss = new XenConEvent();
                break;
            case BOSS_EVENT_TRUNG_THU:
                boss = new ThoDaiCa();
                break;
            case TUANLOC:
                boss = new Tuanloc();
                break;
            case DUONG_TANG:
                boss = new DuongTang();
                break;
            case KAMI:
                boss = new Kami();
                break;
            case OREN:
                boss = new Oren();
                break;
            case KAMIOREN:
                boss = new Kamioren();
                break;
            case NGO_KHONG_EVENT:
                boss = new NgoKhong();
                break;
            case HON_MA:
                boss = new HonMa();
                break;
            case LUFFY_NEW:
                boss = new LuffyNew();
                break;
            case BROOK_NEW:
                boss = new BrookNew();
                break;
            case CHOPPER_NEW:
                boss = new ChopperNew();
                break;
            case FRANKY_NEW:
                boss = new FrankyNew();
                break;
            case NAMI_NEW:
                boss = new NamiNew();
                break;
            case ROBIN_NEW:
                boss = new RobinNew();
                break;
            case SANJI_NEW:
                boss = new SanjiNew();
                break;
            case USOPP_NEW:
                boss = new UsoppNew();
                break;
            case ZORO_NEW:
                boss = new ZoroNew();
                break;
            case DORAEMON:
                boss = new Doraemon();
                break;
            case NOBITA:
                boss = new Nobita();
                break;
            case SIZUKA:
                boss = new Sizuka();
                break;
            case CHAIEN:
                boss = new Chaien();
                break;
            case SUNEO:
                boss = new Suneo();
                break;
            case BO_XUONG_1:
                boss = new BoXuong1();
                break;
            case BO_XUONG_2:
                boss = new BoXuong2();
                break;
            case BO_XUONG_3:
                boss = new BoXuong3();
                break;

        }
        return boss;
    }

    private static void createBossTestDame() {
        // Map ID to coordinates [x, y]
        HashMap<Integer, int[]> coordinates = new HashMap<>();
        coordinates.put(182, new int[]{255, 384}); // Map 42
        

        int[] listMap = {182};
        for (int i = 0; i < listMap.length; i++) {
            Map map_lang_kkr = MapService.gI().getMapById(listMap[i]);
            try {
                for (int j = 0; j < 1; j++) {
                    if (map_lang_kkr != null) {
                        Zone zone = map_lang_kkr.zones.get(j);
                        if (zone != null) {
                            int[] coords = coordinates.getOrDefault(listMap[i], new int[]{zone.map.mapWidth / 2, 0});
                            int x = coords[0];
                            int y = coords[1];
                            new Maydosucmanh(zone, x, y); // Pass x and y to constructor
                        }
                    }
                }
            } catch (Exception e) {
                Log.warning("Loi create boss dame: " + e.getMessage());
            }
        }

    }
}
