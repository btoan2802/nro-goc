package nro.services.func;

import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.consts.ConstOption;
import nro.lib.RandomCollection;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.player.Player;
import nro.server.ServerLog;
import nro.server.ServerNotify;
import nro.server.SettingGame;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Logger;
import nro.utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import nro.services.InventoryServiceNew;

public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 1000;
    private static final int COST_DAP_DO_KICH_HOAT = 500000000;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;
    private static final int COST_GIA_HAN_CAI_TRANG = 20;
    private static final int COST_NC_ZAMAS_GOKUBL = 200;
    private static final int OK_BONG_TAI_3 = 500;
    private static final int COST_NANG_CAP_NGOC_BOI = 10000; // Chi phí 10,000 vàng
    private static final int[] NGOC_BOI_IDS = {1559, 1560, 1561, 1562, 1563, 1564, 1565, 1566, 1567}; // Danh sách ID ngọc bội

    private static final int TIME_COMBINE = 500;

    private static final byte MAX_STAR_ITEM = 8;
    private static final byte MAX_LEVEL_ITEM = 7;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;
    public static final int NANG_CAP_VAT_PHAM_BAO_VE = 533;
    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI = 502;
    public static final int DOI_VE_HUY_DIET = 503;
    public static final int DAP_SET_KICH_HOAT = 504;
    public static final int DOI_MANH_KICH_HOAT = 505;
    public static final int AN_TRANG_BI = 1300;
    public static final int TAY_AN_TRANG_BI = 13011;
    

    public static final int NANG_CAP_VAT_PHAM = 506;
    public static final int NANG_CAP_BONG_TAI = 507;

    public static final int LAM_PHEP_NHAP_DA = 508;
    public static final int NHAP_NGOC_RONG = 509;
    public static final int CHE_TAO_DO_THIEN_SU = 510;
    public static final int DAP_SET_KICH_HOAT_CAO_CAP = 511;
    public static final int GIA_HAN_CAI_TRANG = 512;
    public static final int NANG_CAP_DO_THIEN_SU = 513;
    public static final int PHA_LE_HOA_TRANG_BI_X10 = 514;
    public static final int CHUYEN_SPL = 50111;
    public static final int TAY_SPL = 501111;
    public static final int DAP_DO_THIEN_SU = 515;
    public static final int DOI_DO_THAN_LINH_THANH_HUY_DIET = 516;
    public static final int NANG_CAP_CHI_SO_BONG_TAI = 517;
    public static final int NANG_CAP_SKH_THUONG = 518;
    public static final int NANG_CAP_SKH_VIP = 519;
    public static final int CHE_TAO_BO_KEO_KINH_DI = 527;
    public static final int CHE_TAO_GIO_KEO_KINH_DI = 528;
    private static final short ID_DA_BAO_VE = 987;
    private static final short ID_DA_NANG_CAP = 1345;
    public static final int BONG_TOI_TRANG_BI = 529;
    public static final int DLETE_BONG_TOI_TRANG_BI = 530;
    public static final int CHE_BIEN_TRA_HOA_CUC = 531;
    public static final int NANG_SKH = 532;
    public static final int DELETE_OPTION_THIEN_SU = 533;
    public static final int GHEP_RUONG_GOD = 535;
    public static final int DOI_RUONG_GO = 536;
    public static final int CHE_TAO_TRANG_BI = 537;
    public static final int TAY_CHI_SO_KHONG_THEGD = 538;
    public static final int NANG_CAP_PHU_KIEN = 539;
    public static final int THANH_TAY_PHU_KIEN = 540;

    public static final int NANG_CAP_BONG_TOI = 541;
    public static final int THANH_TAY_BONG_TOI = 542;
    public static final int NANG_CAP_BONG_TAI_3 = 1553;
    public static final int NANG_CAP_CHI_SO_BONG_TAI_3 = 1554;
    
    public static final int NANG_CAP_BONG_TAI_4 = 1555;
    public static final int NANG_CAP_CHI_SO_BONG_TAI_4 = 1556;

    public static final int NANG_CAP_ZENO = 543;
    public static final int EP_SAO_ZENO = 544;
    public static final int GHEP_CAI_TRANG_2 = 545;
    public static final int NANG_CAP_SKH_THUONG_GOLD_BAR = 546;
    public static final int PHAN_TACH_HUY_DIET_LAY_MANH = 547;

    public static final int DOI_DO_THIEN_SU = 548;
    public static final int DOI_DO_HUY_DIET = 549;

    public static final int PHA_LE_HOA_LINH_THU = 550;
    public static final int EP_PHA_LE_LINH_THU = 551;

    public static final int NANG_CAP_THIEN_TU = 552;

    public static final int THANG_HOA_NGOC_BOI = 553;
    public static final int THANG_CAP_NGOC_BOI = 554;
    public static final int THANG_HOA_NGOC_BOI_DE_TU = 555;
    public static final int TRAO_DOI_XU_HADES = 670;

    private static final int GOLD_BONG_TAI = 500_000_000;
    private static final int COST = 500_000_000;
    private static final int GEM_BONG_TAI = 5_000;
    private static final int RATIO_BONG_TAI = 100;
    private static final int RUBY_ONE_SKH = 500;

    private final Npc baHatMit;
    private final Npc whis;
    private final Npc bill;
    private final Npc toribot;
    private final Npc thuongDeNew;
    private final Npc hatmittht;
//    private final Npc daiThienSu;
    private final Npc thongoc;
    private final Npc drBrief;
    private final Npc fu;
    private final Npc thuongNhan;
    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.whis = NpcManager.getNpc(ConstNpc.WHIS);
        this.bill = NpcManager.getNpc(ConstNpc.BILL);
        this.toribot = NpcManager.getNpc(ConstNpc.TORIBOT);
        this.thuongDeNew = NpcManager.getNpc(ConstNpc.THUONG_DE_NEW);
        this.hatmittht = NpcManager.getNpc(ConstNpc.HAT_MIT_HT);
//        this.daiThienSu = NpcManager.getNpc(ConstNpc.NPC_64);
        this.thuongNhan = NpcManager.getNpc(ConstNpc.THUONG_NHAN);
        this.fu = NpcManager.getNpc(ConstNpc.FU);
        this.thongoc = NpcManager.getNpc(ConstNpc.THO_NGOC);
        this.drBrief = NpcManager.getNpc(ConstNpc.DR_DRIEF);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case EP_PHA_LE_LINH_THU:
                menu_EP_SAO_LINH_THU(player);
                break;
            case NANG_CAP_THIEN_TU:
                menu_NANG_CAP_THIEN_TU(player);
                break;
            case AN_TRANG_BI:
                menu_AN_TRANG_BI(player);
                break;
            case TAY_AN_TRANG_BI:
                    menu_TAY_AN_TRANG_BI(player);
                    break;
            case GHEP_RUONG_GOD:
                menu_Gep_Ruong_Gold(player);
                break;
            case CHE_BIEN_TRA_HOA_CUC:
                menu_che_bien_tra_hoa_cuc(player);
                break;
            case DELETE_OPTION_THIEN_SU:
                menu_Delete_Option_Thien_Su(player);
                break;
            case DLETE_BONG_TOI_TRANG_BI:
                menu_Delete_Bong_Toi_Trang_Bi(player);
                break;
            case BONG_TOI_TRANG_BI:
                menu_Bong_Toi_Trang_Bi(player);
                break;
            case CHE_TAO_GIO_KEO_KINH_DI:
                menu_Che_Tao_Gio_Keo_Kinh_Di(player);
                break;
            case CHE_TAO_BO_KEO_KINH_DI:
                menu_Che_Tao_Bo_Keo_Kinh_Di(player);
                break;
            case NANG_CAP_BONG_TAI:
                menu_Nang_Cap_Bong_Tai(player);
                break;
            case NANG_CAP_CHI_SO_BONG_TAI:
                menu_Nang_Cap_Chi_So_Bong_Tai(player);
                break;
            case DAP_DO_THIEN_SU:
                menu_Dap_Do_Thien_Su(player);
                break;
            case EP_SAO_TRANG_BI:
                menu_Ep_Sao_Trang_Bi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
            case PHA_LE_HOA_TRANG_BI_X10:
                menu_Pha_Le_Hoa_Trang_Bi(player);
                break;
            case CHUYEN_SPL:
                menu_ChuyenSaoPhaLe(player);
                break;
            case TAY_SPL:
                menu_TaySaoPhaLe(player);
                break;    
            case PHA_LE_HOA_LINH_THU:
                menu_Pha_le_hoa_linh_thu(player);
                break;
            case NHAP_NGOC_RONG:
                menu_Nhap_Ngoc_Rong(player);
                break;
            case DOI_RUONG_GO:
                menu_Doi_Ruong(player);
                break;
            case NANG_CAP_SKH_THUONG:
                // menu_Nang_Cap_SKH_Thuong(player);
                break;
            case NANG_CAP_SKH_THUONG_GOLD_BAR:
                menu_Nang_Cap_SKH_Thuong_GOLD_BAR(player);
                break;
            case NANG_CAP_SKH_VIP: // vip
                menu_Nang_Cap_SKH_Vip_1(player);
                break;
            case NANG_CAP_VAT_PHAM:
                menu_Nang_Cap_Vat_Pham(player);
                break;
            case CHE_TAO_TRANG_BI:
                menu_Che_Tao_Vat_Pham(player);
                // menu_Tay_Chi_So_Khong_The_GD(player);
                break;
            case DOI_VE_HUY_DIET:
                menu_Doi_Ve_Huy_Diet(player);
                break;
            case DOI_DO_THAN_LINH_THANH_HUY_DIET:
                menu_Doi_Do_Than_Linh_Thanh_Huy_Diet(player);
                break;
            case DAP_SET_KICH_HOAT:
                menu_Dap_Set_Kich_Hoat(player);
                break;
            case DOI_MANH_KICH_HOAT:
                menu_Doi_Manh_Kich_Hoat(player);
                break;
            case DAP_SET_KICH_HOAT_CAO_CAP:
                menu_Dap_Set_Kich_Hoat_Cao_Cap(player);
                break;
            case GIA_HAN_CAI_TRANG:
                menu_Gia_Han_Cai_Trang(player);
                break;
            case THANG_CAP_NGOC_BOI:
                menu_THANG_CAP_NGOC_BOI(player);
                break;
            case THANG_HOA_NGOC_BOI:
            case THANG_HOA_NGOC_BOI_DE_TU:
                menu_THANG_HOA_NGOC_BOI(player);
                break;
            case NANG_CAP_DO_THIEN_SU:
                menu_Nang_Cap_Do_Thien_Su(player);
                break;
            case TRAO_DOI_XU_HADES:
                menu_TRAO_DOI_XU_HADES(player);
                break;
            case NANG_CAP_PHU_KIEN:
                menu_Nang_Cap_Phu_Kien(player);
                break;
            case THANH_TAY_PHU_KIEN:
                menu_Tay_Chi_So_Phu_Kien(player);
                break;
            case NANG_CAP_ZENO:
                menu_Pha_Le_Hoa_Zeno(player);
                break;
            case EP_SAO_ZENO:
                menu_Ep_Sao_Zeno(player);
                break;
            case NANG_CAP_BONG_TOI:
                menu_Nang_Cap_Bong_Toi(player);
                break;
            case THANH_TAY_BONG_TOI:
                menu_Tay_Chi_So_Bong_Toi(player);
                break;
            case NANG_CAP_BONG_TAI_3:
                menu_Nang_Cap_Bong_Tai_3(player);
                break;
            case NANG_CAP_CHI_SO_BONG_TAI_3:
                menu_Nang_Cap_Chi_So_Bong_Tai_3(player);
                break;
             case NANG_CAP_BONG_TAI_4:
                menu_Nang_Cap_Bong_Tai_4(player);
                break;
            case NANG_CAP_CHI_SO_BONG_TAI_4:
                menu_Nang_Cap_Chi_So_Bong_Tai_4(player);
                break;    
            case GHEP_CAI_TRANG_2:
                menu_Ghep_cai_trang_2(player);
                break;
            case PHAN_TACH_HUY_DIET_LAY_MANH:
                menu_Tach_Do_Huy_Diet(player);
                break;
            case DOI_DO_THIEN_SU:
                DoidoThienSu(player);
                break;

        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player) {
//        System.out.println("[COMBINE] Player " + player.name 
//    + " | TYPE = " + player.combineNew.typeCombine);

        TransactionService.gI().cancelTrade(player);
        if (Util.canDoWithTime(player.combineNew.lastTimeCombine, TIME_COMBINE)) {
            // if (false) {
            // Service.getInstance().sendThongBao(player, "Tính năng đang tạm khóa");
            // return;
            // }
            switch (player.combineNew.typeCombine) {
                case EP_SAO_TRANG_BI:
                    if (player.combineNew.epSao == 1) {
                        epSaoTrangBi(player);
                    } else if (player.combineNew.epSao == 2) {
                        epSaoTrangBi1(player);
                    }
                    break;
                case PHA_LE_HOA_TRANG_BI:
                    phaLeHoaTrangBi(player);
                    break;
                case CHUYEN_SPL:
                    chuyenSaoPhaLe(player);
                    break;
                case TAY_SPL:
                    taySaoPhaLe(player);
                    break;    
                case CHUYEN_HOA_TRANG_BI:

                    break;
                case NHAP_NGOC_RONG:
                    nhapNgocRong(player);
                    break;
                case DOI_RUONG_GO:
                    nhapNgocRong(player);
                    break;
                case NANG_CAP_VAT_PHAM:
                    nangCapVatPham(player);
                    break;
                case CHE_TAO_TRANG_BI:
                    cheTaoTrangBi(player);
                    break;
                case NANG_CAP_BONG_TAI:
                    nangCapBongTai(player);
                    break;
                case NANG_CAP_CHI_SO_BONG_TAI:
                    nangCapChiSoBongTai(player);
                    break;
                case DOI_VE_HUY_DIET:
                    // doiVeHuyDiet(player);
                    break;
                case DOI_DO_THAN_LINH_THANH_HUY_DIET:
                    doiTrangbiHuyDiet(player);
                    break;
                case DAP_SET_KICH_HOAT:
                    // dapDoKichHoat(player);
                    break;
                case NANG_CAP_SKH_THUONG:
                    // nangCapSKH_THUONG(player);
                    break;
                case NANG_CAP_SKH_THUONG_GOLD_BAR:
                    nangCapSKH_THUONG_GOLD_BAR(player);
                    break;
                case NANG_CAP_SKH_VIP:
                    nangCapSKH_VIP_1(player);
                    break;
                case DOI_MANH_KICH_HOAT:
                    doiManhKichHoat(player);
                    break;
                case DAP_SET_KICH_HOAT_CAO_CAP:
                    // dapDoKichHoatCaoCap(player);
                    break;
                case GIA_HAN_CAI_TRANG:
                    giaHanCaiTrang(player);
                    break;
                case THANG_CAP_NGOC_BOI:
                    Thangcapngocboi(player);
                    break;
                case THANG_HOA_NGOC_BOI:
                    ThanghoaNgocBoi(player);
                    break;
                case NANG_CAP_DO_THIEN_SU:
                    // nangCapDoThienSu(player);
                    break;
                case TRAO_DOI_XU_HADES:
                    traoDoiXuHades(player);
                    break;
                case DAP_DO_THIEN_SU:
                    // nangcapthiensu(player);
                    break;
                case CHE_TAO_GIO_KEO_KINH_DI:
                    chetaogiokeo(player);
                    break;
                case CHE_TAO_BO_KEO_KINH_DI:
                    chetaobokeo(player);
                    break;
                case BONG_TOI_TRANG_BI:
                    BongToi_HoaTrangBi(player);
                    break;
                case DLETE_BONG_TOI_TRANG_BI:
                    Delete_BongtoiTrangbi(player);
                    break;
                case CHE_BIEN_TRA_HOA_CUC:
                    // ChebienTra_HoaCuc(player);
                    break;
                case DELETE_OPTION_THIEN_SU:
                    // Delete_ThienSuOption(player);
                    break;
                case GHEP_RUONG_GOD:
                    // GhepRuongCTGod(player);
                    break;
                case NANG_CAP_PHU_KIEN:
                    nangCapPhuKien(player);
                    break;
                case THANH_TAY_PHU_KIEN:
                    thanhTayPhuKien(player);
                    break;
                case NANG_CAP_ZENO:
                    phaLeHoaZeno(player);
                    break;
                case EP_SAO_ZENO:
                    epSaoZeno(player);
                    break;
                case NANG_CAP_BONG_TOI:
                    nangCapBongToi(player);
                    break;
                case THANH_TAY_BONG_TOI:
                    thanhTayBongToi(player);
                    break;
                case NANG_CAP_BONG_TAI_3:
                    nangCapBongTai_3(player);
                    break;
                case NANG_CAP_CHI_SO_BONG_TAI_3:
                    nangCapChiSoBongTai_3(player);
                    break;
                case NANG_CAP_BONG_TAI_4:
                    nangCapBongTai_4(player);
                    break;
                case NANG_CAP_CHI_SO_BONG_TAI_4:
                    nangCapChiSoBongTai_4(player);
                    break;    
                case GHEP_CAI_TRANG_2:
                    Ghepcaitrang_2(player);
                    break;
                case PHAN_TACH_HUY_DIET_LAY_MANH:
                    phanTachHuyDiet(player);
                    break;
                case DOI_DO_THIEN_SU:
                    RUnDoiDoThienSu(player);
                    break;
                case EP_PHA_LE_LINH_THU:
                    EP_SAO_LINH_THU(player);
                    break;
                case NANG_CAP_THIEN_TU:
                    nangcapthientu(player);
                    break;
                case AN_TRANG_BI:
                    anTrangBi(player);
                    break;
                case TAY_AN_TRANG_BI:
                    tayAnTrangBi(player);
                    break;
            }
            player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
            player.combineNew.clearParamCombine();
            player.combineNew.lastTimeCombine = System.currentTimeMillis();
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ loại 2
     *
     * @param player
     */
    public void startCombine_2(Player player) {
        TransactionService.gI().cancelTrade(player);
        if (Util.canDoWithTime(player.combineNew.lastTimeCombine, TIME_COMBINE)) {
            switch (player.combineNew.typeCombine) {
                case NHAP_NGOC_RONG:
                    Nhap_NROX10(player);
                    break;
                case NANG_CAP_PHU_KIEN:
                    NangCapPhuKienX10(player);
                    break;
                case NANG_CAP_VAT_PHAM:
                    NangCapVatPhamX10(player);
                    break;
                case NANG_CAP_BONG_TOI:
                    bongToiX10(player);
                    break;

            }
            player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
            player.combineNew.clearParamCombine();
            player.combineNew.lastTimeCombine = System.currentTimeMillis();
        }
    }

    private void GhepRuongCTGod(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            Item Ngoctrai = null;
            Item Daquy = null;
            Item RuongRong = null;
            Item KimCuong = null;
            for (Item it : player.combineNew.itemsCombine) {
                switch (it.template.id) {
                    case 1382:
                        Ngoctrai = it;
                        break;
                    case 1383:
                        Daquy = it;
                        break;
                    case 1384:
                        RuongRong = it;
                        break;
                    case 1385:
                        KimCuong = it;
                        break;
                    default:
                        break;
                }
            }
            if (RuongRong != null && Daquy != null && Daquy.quantity >= 20
                    && Ngoctrai != null && Ngoctrai.quantity >= 20
                    && KimCuong != null && KimCuong.quantity >= 20) {
                Item ruongCtGod = ItemService.gI().createNewItem((short) 1386);
                InventoryService.gI().subQuantityItemsBag(player, Ngoctrai, 20);
                InventoryService.gI().subQuantityItemsBag(player, Daquy, 20);
                InventoryService.gI().subQuantityItemsBag(player, RuongRong, 1);
                InventoryService.gI().subQuantityItemsBag(player, KimCuong, 20);
                ruongCtGod.itemOptions.add(new ItemOption(30, 1));
                InventoryService.gI().addItemBag(player, ruongCtGod, 99);
                InventoryService.gI().sendItemBags(player);
                sendEffectCombineDB(player, ruongCtGod.template.iconID);
                reOpenItemCombine(player);

            }
        }
    }

    private void ChebienTra_HoaCuc(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            long gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            if (player.combineNew.itemsCombine.size() == 4) {
                Item tratuoi = null;
                Item niatre = null;
                Item quetre = null;
                Item hoacuc = null;
                for (Item item : player.combineNew.itemsCombine) {
                    if (item.template.id == 1328) {
                        tratuoi = item;
                    } else if (item.template.id == 1329) {
                        niatre = item;
                    } else if (item.template.id == 1330) {
                        quetre = item;
                    } else if (item.template.id == 1332) {
                        hoacuc = item;
                    }
                }
                if (tratuoi != null && tratuoi.quantity >= 99
                        && niatre != null && niatre.quantity >= 99
                        && quetre != null && quetre.quantity >= 99
                        && hoacuc != null) {
                    player.inventory.gold -= gold;
                    InventoryService.gI().subQuantityItemsBag(player, tratuoi, 99);
                    InventoryService.gI().subQuantityItemsBag(player, niatre, 99);
                    InventoryService.gI().subQuantityItemsBag(player, quetre, 99);
                    InventoryService.gI().subQuantityItemsBag(player, hoacuc, 1);
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        Item hopTra = ItemService.gI().createNewItem((short) 1339);
                        InventoryService.gI().addItemBag(player, hopTra, 99);
                        InventoryService.gI().sendItemBags(player);
                        sendEffectSuccessCombine(player);
                        sendEffectOpenItem(player, hoacuc.template.iconID, hopTra.template.iconID);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }

            }
        }
    }

    private void Delete_ThienSuOption(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item dothiensu = null;
            for (Item item_ : player.combineNew.itemsCombine) {
                if (item_.template.id >= 1048 && item_.template.id <= 1062) {
                    dothiensu = item_;
                }
            }
            if (dothiensu != null) {
                short[] listOp = {216, 97, 213, 214, 215, 5, 201, 202, 203};
                ItemOption HaveOpotionThienSu = null;
                for (ItemOption io : dothiensu.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 5:
                            HaveOpotionThienSu = io;
                            break;
                        case 97:
                            HaveOpotionThienSu = io;
                            break;
                        case 201:
                            HaveOpotionThienSu = io;
                            break;
                        case 202:
                            HaveOpotionThienSu = io;
                            break;
                        case 203:
                            HaveOpotionThienSu = io;
                            break;
                        case 213:
                            HaveOpotionThienSu = io;
                            break;
                        case 214:
                            HaveOpotionThienSu = io;
                            break;
                        case 215:
                            HaveOpotionThienSu = io;
                            break;
                        case 216:
                            HaveOpotionThienSu = io;
                            break;
                    }
                }
                if (HaveOpotionThienSu != null) {
                    player.inventory.gold -= 500_000_000;
                    dothiensu.itemOptions.remove(HaveOpotionThienSu);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    sendEffectSuccessCombine(player);
                    player.combineNew.itemsCombine.clear();
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void Delete_BongtoiTrangbi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item buagiaihachoa = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1311)
                    .findFirst().get();
            Item trangBiHacHoa = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHacHoa).findFirst()
                    .get();
            if (buagiaihachoa != null && trangBiHacHoa != null) {
                if (Util.isTrue(100, 100)) {
                    ItemOption option_218 = new ItemOption();
                    ItemOption option_219 = new ItemOption();
                    ItemOption option_220 = new ItemOption();
                    ItemOption option_221 = new ItemOption();
                    for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                        if (itopt.optionTemplate.id == 218) {
                            option_218 = itopt;
                        }
                        if (itopt.optionTemplate.id == 219) {
                            option_219 = itopt;
                        }
                        if (itopt.optionTemplate.id == 220) {
                            option_220 = itopt;
                        }
                        if (itopt.optionTemplate.id == 221) {
                            option_221 = itopt;
                        }
                    }
                    if (option_218 != null) {
                        trangBiHacHoa.itemOptions.remove(option_218);
                    }
                    if (option_219 != null) {
                        trangBiHacHoa.itemOptions.remove(option_219);
                    }
                    if (option_220 != null) {
                        trangBiHacHoa.itemOptions.remove(option_220);
                    }
                    if (option_221 != null) {
                        trangBiHacHoa.itemOptions.remove(option_221);
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().subQuantityItemsBag(player, buagiaihachoa, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                player.combineNew.itemsCombine.clear();
                reOpenItemCombine(player);
            }
        }
    }

    private void BongToi_HoaTrangBi(Player player) {
    if (player.combineNew.itemsCombine.size() == 2) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            Item trangBiPhapSu = null;
            Item daPhapSu = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 5 || item.template.type == 11 || item.template.type == 98) {
                        trangBiPhapSu = item;
                    } else if (item.template.id == 1308) {
                        daPhapSu = item;
                    }
                }
            }
            if (daPhapSu != null && trangBiPhapSu != null) {
                if (!CheckNangCapBongToi(trangBiPhapSu, daPhapSu)) {
                    Service.getInstance().sendThongBaoOK(player, "Vật phẩm và loại đá không phù hợp");
                    return;
                }
                int level = 0;
                for (ItemOption io : trangBiPhapSu.itemOptions) {
                    if (io.optionTemplate.id == 218) {
                        level = io.param;
                        break;
                    }
                }
                if (level >= 8) {
                    Service.getInstance().sendThongBaoOK(player, "Vật phẩm đã đạt tối đa Pháp Sư");
                    return;
                }

                player.combineNew.ratioCombine = getRatioBongToiTrangBi(level);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    List<Integer> idOptionPhapSu = Arrays.asList(219, 220, 221);
                    int randomOption = idOptionPhapSu.get(Util.nextInt(0, 2));

                    if (!trangBiPhapSu.haveOption(218)) {
                        trangBiPhapSu.itemOptions.add(new ItemOption(218, 1));
                    } else {
                        for (ItemOption itopt : trangBiPhapSu.itemOptions) {
                            if (itopt.optionTemplate.id == 218) {
                                itopt.param += 1;
                                break;
                            }
                        }
                    }

                    if (!trangBiPhapSu.haveOption(randomOption)) {
                        int val = getValuePhapSu(randomOption, level);
                        trangBiPhapSu.itemOptions.add(new ItemOption(randomOption, val));
                    } else {
                        for (ItemOption itopt : trangBiPhapSu.itemOptions) {
                            if (itopt.optionTemplate.id == randomOption) {
                                itopt.param += getValuePhapSu(randomOption, level);
                                break;
                            }
                        }
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }

                int sl = getCountDaNangCapBongToi(level);
                player.inventory.gold -= 500_000_000L;
                InventoryService.gI().subQuantityItemsBag(player, daPhapSu, sl);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            } else {
                Service.getInstance().sendThongBao(player, "Cần 1 trang bị và 1 Đá Pháp Sư ");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }
}

// ✅ Hàm riêng gọn gàng xử lý giá trị chỉ số mỗi cấp
private int getValuePhapSu(int optionId, int level) {
    switch (optionId) {
        case 219:
        case 220:
            return switch (level) {
                case 0, 1 -> 1;
                case 2 -> 2;
                case 3 -> 3;
                case 4 -> 5;
                case 5 -> 7;
                case 6 -> 9;
                case 7 -> 12;
                default -> 0;
            };
        case 221:
            return switch (level) {
                case 0, 1 -> 1;
                case 2, 3 -> 2;
                case 4 -> 3;
                case 5 -> 4;
                case 6 -> 5;
                case 7 -> 8;
                default -> 0;
            };
        default:
            return 0;
    }
}

    private void phanTachHuyDiet(Player player) {
        Item tvKhoa = InventoryService.gI().findItemBag(player, 1429);
        if (player.combineNew.itemsCombine.size() >= 1 && player.combineNew.itemsCombine.size() <= 5) {
            if (tvKhoa.quantity < (1000
                    * player.combineNew.itemsCombine.size())) {
                Service.getInstance().sendThongBao(player, "Không đủ thoi vàng để thực hiện");
                return;
            }
            // if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            // Service.getInstance().sendThongBao(player, "Hành trang cần một ô trống");
            // return;
            // }
            String npcSay = "";
            String notifi = "";
            for (int i = 0; i < player.combineNew.itemsCombine.size(); i++) {
                Item item = player.combineNew.itemsCombine.get(i);
                if (item.isNotNullItem() && item.isDTL()) {
                    // if (item.isNotPhanTach()) {
                    // notifi = "Phân tách dừng lại vì trang bị " + item.template.name
                    // + " là trang bị nhận từ quà chia sẽ Fanpage, không thể phân tách";
                    // break;
                    // }
                    if (tvKhoa.quantity >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                        InventoryService.gI().subQuantityItemsBag(player, tvKhoa, COST_DOI_VE_DOI_DO_HUY_DIET);
                        short idHuyDiet = (short) 674;
                        Item HuyDiet = ItemService.gI().createNewItem(idHuyDiet, 1);

                        if (HuyDiet != null) {
                            InventoryService.gI().subQuantityItemsBag(player, item, 1);
                            InventoryService.gI().addItemBag(player, HuyDiet, 1);
                            sendEffectOpenItem(player, item.template.iconID, HuyDiet.template.iconID);
                        }

                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);

                        npcSay += HuyDiet.template.name + "\n";
                    } else {
                        notifi = "Chuyển hóa dừng lại vì bạn không đủ vàng";
                        break;
                    }
                }
            }
            reOpenItemCombine(player);
            if (npcSay != "") {
                Service.getInstance().sendThongBao(player, "Chuyển hóa thành công, bạn vừa nhận được\n" + npcSay);
            } else {
                Service.getInstance().sendThongBao(player,
                        notifi);
            }

        }
    }

    private void Ghepcaitrang_2(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            Item tvKhoa = InventoryService.gI().findItemBag(player, 1429);
        if (tvKhoa == null || tvKhoa.quantity < 5000) {
            Service.getInstance().sendThongBao(player, "Cần 5K TV khóa để nâng cấp!");
            return;
        }
            Item caiTrang_1 = null;
            Item caiTrang_2 = null;
            Item BTC2 = null;
            Item Da = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 2041) {
                    caiTrang_1 = item;
                } else if (item.template.id == 898) {
                    caiTrang_2 = item;
                } else if (item.template.id == 921) {
                    BTC2 = item;
                } else if (item.template.id == 1345) {
                    Da = item;
                }
            }
            if (caiTrang_1 != null && caiTrang_1.quantity >= 1
                    && caiTrang_2 != null && caiTrang_2.quantity >= 1
                    && BTC2 != null && BTC2.quantity >= 1
                    && Da != null && Da.quantity >= 10) {
                boolean countIOBTC2 = false;
                for (ItemOption io : BTC2.itemOptions) {
                    if (io.optionTemplate.id == 5 || io.optionTemplate.id == 14 || io.optionTemplate.id == 50
                            || io.optionTemplate.id == 77 || io.optionTemplate.id == 80 || io.optionTemplate.id == 81
                            || io.optionTemplate.id == 94 || io.optionTemplate.id == 101 || io.optionTemplate.id == 103
                            || io.optionTemplate.id == 108) {
                        countIOBTC2 = true;
                        break;
                    }
                }
                if (!countIOBTC2) {
                    return;
                }
                player.combineNew.ratioCombine = 50;
                InventoryService.gI().subQuantityItemsBag(player, tvKhoa, 5000);

                InventoryService.gI().subQuantityItemsBag(player, Da, 10);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    Item gioItem = ItemService.gI().createNewItem((short) 1313);
                    gioItem.itemOptions.add((new ItemOption(ConstOption.TAN_CONG_PT, 40)));
                    gioItem.itemOptions.add((new ItemOption(ConstOption.HP_PT, 40)));
                    gioItem.itemOptions.add((new ItemOption(ConstOption.KI_PT, 40)));
                    sendEffectSuccessCombine(player);
                    InventoryService.gI().addItemBag(player, gioItem, 99);
                    InventoryService.gI().subQuantityItemsBag(player, caiTrang_1, 1);
                    InventoryService.gI().subQuantityItemsBag(player, caiTrang_2, 1);
                    InventoryService.gI().subQuantityItemsBag(player, BTC2, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + gioItem.template.name);
                } else {
                    InventoryService.gI().sendItemBags(player);
                    sendEffectFailCombine(player);
                    reOpenItemCombine(player);
                }

            }
        }
    }

    private void chetaogiokeo(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            if (player.inventory.ruby < 1000) {
                Service.getInstance().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }
            Item keobingo = null;
            Item keonaonguoi = null;
            Item daybuoc = null;
            Item giodung = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1297) {
                    keobingo = item;
                } else if (item.template.id == 1298) {
                    keonaonguoi = item;
                } else if (item.template.id == 1299) {
                    daybuoc = item;
                } else if (item.template.id == 1305) {
                    giodung = item;
                }
            }
            if (keobingo != null && keobingo.quantity >= 99
                    && keonaonguoi != null && keonaonguoi.quantity >= 99
                    && giodung != null && daybuoc != null) {

                player.combineNew.ratioCombine = RATIO_BONG_TAI;
                player.inventory.ruby -= 1000;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    Item gioItem = ItemService.gI().createNewItem((short) 1301);
                    sendEffectSuccessCombine(player);
                    InventoryService.gI().addItemBag(player, gioItem, 99);
                    InventoryService.gI().subQuantityItemsBag(player, keobingo, 99);
                    InventoryService.gI().subQuantityItemsBag(player, keonaonguoi, 99);
                    InventoryService.gI().subQuantityItemsBag(player, daybuoc, 1);
                    InventoryService.gI().subQuantityItemsBag(player, giodung, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, keobingo, 99);
                    InventoryService.gI().subQuantityItemsBag(player, keonaonguoi, 99);
                    InventoryService.gI().subQuantityItemsBag(player, daybuoc, 1);
                    InventoryService.gI().subQuantityItemsBag(player, giodung, 1);
                    InventoryService.gI().sendItemBags(player);
                    sendEffectFailCombine(player);
                    reOpenItemCombine(player);
                }

            }
        }
    }

    private void chetaobokeo(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            long gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item keobingo = null;
            Item keonaonguoi = null;
            Item daybuoc = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1297) {
                    keobingo = item;
                } else if (item.template.id == 1298) {
                    keonaonguoi = item;
                } else if (item.template.id == 1299) {
                    daybuoc = item;
                }
            }
            if (keobingo != null && keobingo.quantity >= 99
                    && keonaonguoi != null && keonaonguoi.quantity >= 99
                    && daybuoc != null) {

                player.combineNew.goldCombine = 200_000_000;
                player.combineNew.ratioCombine = RATIO_BONG_TAI;
                player.inventory.gold -= gold;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    Item bokeo = ItemService.gI().createNewItem((short) 1300);
                    sendEffectSuccessCombine(player);
                    InventoryService.gI().addItemBag(player, bokeo, 99);
                    InventoryService.gI().subQuantityItemsBag(player, keobingo, 99);
                    InventoryService.gI().subQuantityItemsBag(player, keonaonguoi, 99);
                    InventoryService.gI().subQuantityItemsBag(player, daybuoc, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, keobingo, 99);
                    InventoryService.gI().subQuantityItemsBag(player, keonaonguoi, 99);
                    InventoryService.gI().subQuantityItemsBag(player, daybuoc, 1);
                    InventoryService.gI().sendItemBags(player);
                    sendEffectFailCombine(player);
                    reOpenItemCombine(player);
                }

            }
        }
    }

    public void phaLeHoaTrangBiX10(Player player, int quan) {
        boolean flag = false;
        for (int i = 0; i < quan; i++) { // số lần pha lê hóa
            if (player == null) {
                break;
            }
            if (!phaLeHoaTrangBi(player)) {
                Service.getInstance().sendThongBao(player,
                        "Pha lê hóa tự động dừng lại lần dập " + (i + 1));
                flag = true;
                break;
            }
        }
        if (!flag) {
            Service.getInstance().sendThongBao(player, "Thất bại sau " + quan + " lần, chúc bạn may mắn lần sau !");
        }
    }

    public void phaLeHoaLinhThu(Player player, int quan) {
        boolean flag = false;
        for (int i = 0; i < quan; i++) { // số lần pha lê hóa
            if (player == null) {
                break;
            }
            if (!Phe_Le_Hoa_Linh_Thu(player)) {
                Service.getInstance().sendThongBao(player,
                        "Pha lê hóa tự động dừng lại lần dập " + (i + 1));
                flag = true;
                break;
            }
        }
        if (!flag) {
            Service.getInstance().sendThongBao(player, "Thất bại sau " + quan + " lần, chúc bạn may mắn lần sau !");
        }
    }

    private void NangCapPhuKienX10(Player player) {
        for (int i = 0; i < 500; i++) { // số lần pha lê hóa
            if (!nangCapPhuKien(player)) {
                Service.getInstance().sendThongBao(player,
                        "Nâng cấp tự động dừng lại lần dập " + (i + 1));
                break;
            } else {

            }
        }

    }

    private void Nhap_NROX10(Player player) {
        for (int i = 0; i < 20; i++) { // số lần pha lê hóa
            if (!nhapNgocRong(player)) {
                break;
            }
        }
    }

    private void nangCapDoThienSu(Player player) {
        if (player.combineNew.itemsCombine.size() > 1) {
            int ratioLuckyStone = 0, ratioRecipe = 0, ratioUpgradeStone = 0;
            List<Item> list = new ArrayList<>();
            Item angelClothes = null;
            Item craftingRecipe = null;
            for (Item item : player.combineNew.itemsCombine) {
                int id = item.template.id;
                if (item.isNotNullItem()) {
                    if (isAngelClothes(id)) {
                        angelClothes = item;
                    } else if (isLuckyStone(id)) {
                        ratioLuckyStone += getRatioLuckyStone(id);
                        list.add(item);
                    } else if (isUpgradeStone(id)) {
                        ratioUpgradeStone += getRatioUpgradeStone(id);
                        list.add(item);
                    } else if (isCraftingRecipe(id)) {
                        ratioRecipe += getRatioCraftingRecipe(id);
                        craftingRecipe = item;
                        list.add(item);
                    }
                }
            }
            boolean canUpgrade = true;
            for (ItemOption io : angelClothes.itemOptions) {
                int optId = io.optionTemplate.id;
                if (optId == 41) {
                    canUpgrade = false;
                }
            }
            if (canUpgrade) {
                if (angelClothes != null && craftingRecipe != null) {
                    int ratioTotal = (20 + ratioUpgradeStone + ratioRecipe);
                    int ratio = ratioTotal > 75 ? ratio = 75 : ratioTotal;
                    if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                        if (Util.isTrue(ratio, 150)) {
                            int num = 0;
                            if (Util.isTrue(ratioLuckyStone, 150)) {
                                num = 15;
                            } else if (Util.isTrue(5, 100)) {
                                num = Util.nextInt(10, 15);
                            } else if (Util.isTrue(20, 100)) {
                                num = Util.nextInt(1, 10);
                            }
                            RandomCollection<Integer> rd = new RandomCollection<>();
                            rd.add(50, 1);
                            rd.add(25, 2);
                            rd.add(10, 3);
                            rd.add(5, 4);
                            int color = rd.next();
                            for (ItemOption io : angelClothes.itemOptions) {
                                int optId = io.optionTemplate.id;
                                switch (optId) {
                                    case 47: // giáp
                                    case 6: // hp
                                    case 26: // hp/30s
                                    case 22: // hp k
                                    case 0: // sức đánh
                                    case 7: // ki
                                    case 28: // ki/30s
                                    case 23: // ki k
                                    case 14: // crit
                                        io.param += ((long) io.param * num / 100);
                                        break;
                                }
                            }
                            angelClothes.itemOptions.add(new ItemOption(41, color));
                            for (int i = 0; i < color; i++) {
                                angelClothes.itemOptions
                                        .add(new ItemOption(Util.nextInt(201, 212), Util.nextInt(1, 10)));
                            }
                            sendEffectSuccessCombine(player);
                            Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã nâng cấp thành công");
                        } else {
                            sendEffectFailCombine(player);
                            Service.getInstance().sendThongBao(player, "Chúc bạn đen nốt lần sau");
                        }
                        for (Item it : list) {
                            InventoryService.gI().subQuantityItemsBag(player, it, 1);
                        }
                        player.inventory.subGold(COST_DAP_DO_KICH_HOAT);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                    }
                }
            }
        }
    }

    private void traoDoiXuHades(Player player) {
        long totalGold = 0;
        int totalXu = 0;
        for (Item item : player.combineNew.itemsCombine) {
            if (item == null || !this.isDoThanLinh(item.template.id)) {
                return;
            }
            long gold = item.template.type == 4 ? 1500000000 : item.template.type == 2 ? 1000000000 : 500000000;
            int dongXu = item.template.type == 4 ? 3 : item.template.type == 2 ? 2 : 1;
            totalGold += gold;
            totalXu += dongXu;
        }
        if (player.inventory.gold < totalGold) {
            Service.getInstance().sendThongBao(player, "Bạn không có đủ vàng để thực hiện");
            return;
        }
        for (Item item : player.combineNew.itemsCombine) {
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        }
        player.inventory.gold -= totalGold;
        Item dongXu = ItemService.gI().createNewItem((short) 1604, totalXu);
        InventoryServiceNew.gI().addItemBag(player, dongXu);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        this.reOpenItemCombine(player);
    }

    public void ThanghoaNgocBoi(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item ngocBoi = null;
            int indexBag = -1;
            for (int i = 0; i < player.combineNew.itemsCombine.size(); i++) {
                Item item = player.combineNew.itemsCombine.get(i);
                if (item.isNotNullItem()) {
                    for (int id : NGOC_BOI_IDS) {
                        if (item.template.id == id) {
                            ngocBoi = item;
                            indexBag = InventoryService.gI().getIndexBag(player, item);
                            break;
                        }
                    }
                }
            }
            if (ngocBoi != null && indexBag != -1 && player.inventory.gem >= COST_NANG_CAP_NGOC_BOI) {
                // Kiểm tra ô body trống (giả sử ô 13)
                if (player.inventory.itemsBody.get(13) == null || !player.inventory.itemsBody.get(13).isNotNullItem()) {
                    player.inventory.subGem(COST_NANG_CAP_NGOC_BOI);
                    sendEffectSuccessCombine(player);
                    // Mặc ngọc bội vào ô body (ô 13)
                    InventoryService.gI().itemBagToBody(player, indexBag);
                    InventoryService.gI().sendItemBags(player);
                    InventoryService.gI().sendItemBody(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Ô ngọc bội trên người đã có vật phẩm, hãy tháo ra trước!");
                }
            }
        }
    }

    public void ThanghoaNgocBoidetu(Player player) {
        if (player.pet == null) {
            Service.getInstance().sendThongBao(player, "Bạn chưa có đệ tử!");
            return;
        }

        if (player.combineNew.itemsCombine.size() == 1) {
            Item ngocBoi = null;
            int indexBag = -1;
            for (int i = 0; i < player.combineNew.itemsCombine.size(); i++) {
                Item item = player.combineNew.itemsCombine.get(i);
                if (item.isNotNullItem()) {
                    for (int id : NGOC_BOI_IDS) {
                        if (item.template.id == id) {
                            ngocBoi = item;
                            indexBag = InventoryService.gI().getIndexBag(player, item);
                            break;
                        }
                    }
                }
            }
            if (ngocBoi != null && indexBag != -1 && player.inventory.gold >= COST_NANG_CAP_NGOC_BOI) {
                if (player.pet.inventory.itemsBody.get(8) == null || !player.pet.inventory.itemsBody.get(8).isNotNullItem()) {
                    player.inventory.subGold(COST_NANG_CAP_NGOC_BOI);
                    sendEffectSuccessCombine(player);
                    InventoryService.gI().itemBagToPetBody(player, indexBag);
                    InventoryService.gI().sendItemBags(player);
                    //    InventoryService.gI().sendPetItemBody(player); // Sửa để gửi body của pet
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Ô ngọc bội trên người đệ tử đã có vật phẩm, hãy tháo ra trước!");
                }
            }
        }
    }

    private void Thangcapngocboi(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2) {
            Item ngocBoi = null, nguyenLieu = null, baoNgoc = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 1559 && item.template.id <= 1567) {
                        ngocBoi = item;
                    } else if (item.template.id == 1568) {
                        nguyenLieu = item;
                    } else if (item.template.id == 1569) {
                        baoNgoc = item;
                    }
                }
            }
            if (ngocBoi != null && nguyenLieu != null) {
                int level = 0;
                int chucPhuc = 0;
                for (ItemOption io : ngocBoi.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                    } else if (io.optionTemplate.id == 248) {
                        chucPhuc = io.param;
                    }
                }
                int requiredStones = level + 1;
                if (level < 7) {
                    if (level >= 4 && baoNgoc == null) {
                        Service.getInstance().sendThongBao(player, "Cần 1 Bùa Bảo Ngọc để nâng cấp từ cấp 4!");
                        return;
                    }
                    if (nguyenLieu.quantity < requiredStones) {
                        Service.getInstance().sendThongBao(player, "Cần " + requiredStones + " đá  để nâng cấp!");
                        return;
                    }
                    if (InventoryService.gI().getCountEmptyBag(player) > 0
                            && player.inventory.ruby >= COST_GIA_HAN_CAI_TRANG) {
                        player.inventory.subRuby(COST_GIA_HAN_CAI_TRANG);
                        // Kiểm tra tỷ lệ thành công
                        Random rand = new Random();
                        boolean success = (chucPhuc >= 200) || (rand.nextInt(100) < 10); // 99% hoặc 100% nếu chucPhuc >= 200
                        if (success) {
                            // Tăng cấp option 72
                            boolean updated = false;
                            for (ItemOption io : ngocBoi.itemOptions) {
                                if (io.optionTemplate.id == 72) {
                                    io.param++;
                                    updated = true;
                                    break;
                                }
                            }
                            if (!updated) {
                                ngocBoi.itemOptions.add(new ItemOption(72, 1)); // Thêm option 72 nếu chưa có
                            }
                            // Tăng chỉ số cho các option 47, 6, 0, 7, 14, 22, 23, 193, 50, 77, 103 nếu đã tồn tại
                            int[] upgradeableOptions = {47, 6, 0, 7, 14, 22, 23, 193, 50, 77, 103};
                            for (int optionId : upgradeableOptions) {
                                for (ItemOption io : ngocBoi.itemOptions) {
                                    if (io.optionTemplate.id == optionId) {
                                        io.param = io.param < 10 ? io.param + 1 : io.param + (io.param * 10 / 100);
                                        break;
                                    }
                                }
                            }
                            // Tăng chúc phúc ngẫu nhiên 5-10 điểm
                            updated = false;
                            for (ItemOption io : ngocBoi.itemOptions) {
                                if (io.optionTemplate.id == 248) {
                                    int additionalChucPhuc = Util.nextInt(5, 10);
                                    io.param = Math.min(io.param + additionalChucPhuc, 200); // Giới hạn 200
                                    updated = true;
                                    break;
                                }
                            }
                            if (!updated) {
                                ngocBoi.itemOptions.add(new ItemOption(248, Util.nextInt(5, 10))); // Thêm option 248 nếu chưa có
                            }
                            // Reset chúc phúc nếu đạt 200
                            if (chucPhuc >= 200) {
                                for (ItemOption io : ngocBoi.itemOptions) {
                                    if (io.optionTemplate.id == 248) {
                                        io.param = 0;
                                        break;
                                    }
                                }
                            }
                            sendEffectSuccessCombine(player);
                            Service.getInstance().sendThongBao(player, "Nâng cấp ngọc bội thành công ! Cấp mới " + (level + 1));
                        } else {
                            // Tăng chúc phúc ngẫu nhiên 5-10 điểm dù thất bại
                            boolean updated = false;
                            for (ItemOption io : ngocBoi.itemOptions) {
                                if (io.optionTemplate.id == 248) {
                                    int additionalChucPhuc = Util.nextInt(5, 10);
                                    io.param = Math.min(io.param + additionalChucPhuc, 200); // Giới hạn 200
                                    updated = true;
                                    break;
                                }
                            }
                            if (!updated) {
                                ngocBoi.itemOptions.add(new ItemOption(248, Util.nextInt(5, 10))); // Thêm option 248 nếu chưa có
                            }
                            Service.getInstance().sendThongBao(player, "Nâng cấp thất bại ! ");
                        }
                        InventoryService.gI().subQuantityItemsBag(player, nguyenLieu, requiredStones);
                        if (baoNgoc != null) {
                            InventoryService.gI().subQuantityItemsBag(player, baoNgoc, 1); // Trừ bùa bảo ngọc
                        }
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                    } else {
                        Service.getInstance().sendThongBao(player, "Không đủ ruby hoặc full hành trang!");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Ngọc bội đã đạt cấp tối đa");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Cần 1 ngọc bội  và đá Thăng cấp!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Cần 1 ngọc bội và 1 đá Thăng cấp!");
        }
    }

    private void giaHanCaiTrang(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item caitrang = null, vegiahan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 5) {
                        caitrang = item;
                    } else if (item.template.id == 2022) {
                        vegiahan = item;
                    }
                }
            }
            if (caitrang != null && vegiahan != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.ruby >= COST_GIA_HAN_CAI_TRANG) {
                    ItemOption expiredDate = null;
                    boolean canBeExtend = true;
                    for (ItemOption io : caitrang.itemOptions) {
                        if (io.optionTemplate.id == 93) {
                            expiredDate = io;
                        }
                        if (io.optionTemplate.id == 199) {
                            canBeExtend = false;
                        }
                    }
                    if (canBeExtend) {
                        if (expiredDate.param > 0) {
                            player.inventory.subRuby(COST_GIA_HAN_CAI_TRANG);
                            sendEffectSuccessCombine(player);
                            expiredDate.param++;
                            InventoryService.gI().subQuantityItemsBag(player, vegiahan, 1);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            reOpenItemCombine(player);
                        }
                    }
                }
            }
        }
    }

    private void dapDoKichHoatCaoCap(Player player) {
        if (player.combineNew.itemsCombine.size() <= 4) {
            switch (player.combineNew.itemsCombine.size()) {
                case 2: {
                    Item it = player.combineNew.itemsCombine.get(0), it1 = player.combineNew.itemsCombine.get(1);
                    if (!isActivationClothes(it) || !isDestroyClothes(it1.template.id)) {
                        it = null;
                    }
                    if (it != null) {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0
                                && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                            player.inventory.subGold(COST_DAP_DO_KICH_HOAT);
                            int soluongitem = ConstItem.LIST_ITEM_CLOTHES[0][0].length;
                            int id;
                            if (Util.isTrue(98, 100)) {
                                if (Util.isTrue(60, 100)) {
                                    id = (Util.nextInt(0, soluongitem - 7));// random từ bậc 1 đến bậc 6
                                } else {
                                    id = (Util.nextInt(5, soluongitem - 2));// random từ bậc 6 đến bậc 12
                                }
                            } else {
                                id = soluongitem - 1; // đồ thần linh
                            }
                            System.out.println("nro.services.func.CombineServiceNew.dapDoKichHoatCaoCap() : "
                                    + player.combineNew.ratioCombine);
                            if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                                sendEffectSuccessCombine(player);
                                int gender = it.template.gender;
                                if (gender == 3) {
                                    gender = 0;
                                }
                                Item item = ItemService.gI().createNewItem(
                                        (short) ConstItem.LIST_ITEM_CLOTHES[gender][it.template.type][id]);
                                RewardService.gI().initBaseOptionClothes(item);
                                RewardService.gI().initActivationOption(
                                        item.template.gender < 3 ? item.template.gender : player.gender,
                                        item.template.type, item.itemOptions);
                                InventoryService.gI().addItemBag(player, item, 0);
                                InventoryService.gI().subQuantityItemsBag(player, it, 1);
                                InventoryService.gI().subQuantityItemsBag(player, it1, 1);
                                sendEffectOpenItem(player, it.template.iconID, item.template.iconID);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                                reOpenItemCombine(player);
                            } else {
                                sendEffectFailCombine(player);
                                InventoryService.gI().subQuantityItemsBag(player, it, 1);
                                InventoryService.gI().subQuantityItemsBag(player, it1, 1);

                                sendEffectOpenItem(player, it.template.iconID, (short) 930);
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                        }
                    }
                }
                break;
                case 3: {
                    Item it = player.combineNew.itemsCombine.get(0), it1 = player.combineNew.itemsCombine.get(1),
                            it2 = player.combineNew.itemsCombine.get(2);
                    if (!isActivationClothes(it) || !isDestroyClothes(it1.template.id)
                            || !isDestroyClothes(it2.template.id)) {
                        it = null;
                    }
                    if (it != null) {
                        if (InventoryService.gI().getCountEmptyBag(player) > 0
                                && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                            player.inventory.subGold(COST_DAP_DO_KICH_HOAT);
                            int soluongitem = ConstItem.LIST_ITEM_CLOTHES[0][0].length;
                            int id;
                            if (Util.isTrue(98, 100)) {
                                if (Util.isTrue(60, 100)) {
                                    id = (Util.nextInt(0, soluongitem - 7));// random từ bậc 1 đến bậc 6
                                } else {
                                    id = (Util.nextInt(5, soluongitem - 2));// random từ bậc 6 đến bậc 12
                                }
                            } else {
                                id = soluongitem - 1; // đồ thần linh
                            }
                            sendEffectSuccessCombine(player);
                            int gender = it.template.gender;
                            if (gender == 3) {
                                gender = 0;
                            }
                            Item item = ItemService.gI()
                                    .createNewItem((short) ConstItem.LIST_ITEM_CLOTHES[gender][it.template.type][id]);
                            RewardService.gI().initBaseOptionClothes(item);
                            RewardService.gI().initActivationOption(
                                    item.template.gender < 3 ? item.template.gender : player.gender, item.template.type,
                                    item.itemOptions);
                            InventoryService.gI().addItemBag(player, item, 0);
                            sendEffectOpenItem(player, it.template.iconID, item.template.iconID);
                            InventoryService.gI().subQuantityItemsBag(player, it, 1);
                            InventoryService.gI().subQuantityItemsBag(player, it1, 1);
                            InventoryService.gI().subQuantityItemsBag(player, it2, 1);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            reOpenItemCombine(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                        }
                    }
                }
                break;
                default:
                    break;
            }
        }
    }

    private void nangCapSKH_THUONG(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                if (player.inventory.gold < COST) {
                    this.thuongDeNew.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con cần 500 triệu vàng để nâng cấp",
                            "Đóng");
                    return;
                }
                player.inventory.gold -= COST;
                Item itemHDMain = player.combineNew.itemsCombine.get(0);
                if (!itemHDMain.isCanSKH()) {
                    this.thuongDeNew.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Yêu cầu trang bị hủy diệt được nâng cấp từ đồ thần linh mới có thể nâng SKH",
                            "Đóng");
                    return;
                }
                if (itemHDMain.isDHD()) {
                    byte gender = itemHDMain.template.gender;
                    if (itemHDMain.template.gender == 3 || itemHDMain.template.type == 4) { // Rada
                        gender = player.gender;
                    }
                    short itemId = ConstItem.doSKHVip[itemHDMain.template.type][gender][0];
                    int skhId = ItemService.gI().randomSKHId(gender);
                    Item item = ItemService.gI().createNewItem((short) itemId);
                    CombineServiceNew.gI().sendEffectOpenItem(player, itemHDMain.template.iconID,
                            item.template.iconID);
                    RewardService.gI().initBaseOptionClothes(item);
                    ItemService.gI().AddOptionSKH(item, skhId);
                    InventoryService.gI().addItemBag(player, item, 1);
                    InventoryService.gI().subQuantityItemsBag(player, itemHDMain, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nâng cấp được " + item.template.name + " kích hoạt");
                    player.combineNew.itemsCombine.clear();
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Cần 1 món đồ huỷ diệt");
                }

            } else {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");

            }
        } else {
            Service.getInstance().sendThongBao(player, "Cần 1 món huỷ diệt");
        }
    }

    private void nangCapSKH_VIP_1(Player player) {
    if (player.combineNew.itemsCombine.size() != 5) {
        Service.getInstance().sendThongBao(player, "Cần đủ 5 món trang bị Thần Linh để nâng cấp!");
        return;
    }
    if (InventoryService.gI().getCountEmptyBag(player) == 0) {
        Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống trong hành trang!");
        return;
    }
    for (Item it : player.combineNew.itemsCombine) {
        if (!it.isDTL()) {
            Service.getInstance().sendThongBao(player, "Tất cả vật phẩm phải là trang bị Thần Linh!");
            return;
        }
    }
    for (Item it : player.combineNew.itemsCombine) {
        InventoryService.gI().subQuantityItemsBag(player, it, it.quantity);
    }
    InventoryService.gI().sendItemBags(player);
    byte gender = player.gender;
    int[][] skhByPlanet = {
        {555, 556, 562, 563, 561},
        {557, 558, 564, 565, 561},
        {559, 560, 566, 567, 561}
    };
    short itemId = (short) skhByPlanet[gender][Util.nextInt(0, skhByPlanet[gender].length - 1)];
    Item newItem = ItemService.gI().createNewItem(itemId);
    RewardService.gI().initBaseOptionClothes(newItem);
    newItem.itemOptions.removeIf(op -> op.optionTemplate.id == 21);
    newItem.itemOptions.add(new ItemOption(21, 100));
    int skhId = ItemService.gI().randomSKHId(player.gender);
    ItemService.gI().AddOptionSKH(newItem, skhId);
    InventoryService.gI().addItemBag(player, newItem, 1);
    InventoryService.gI().sendItemBags(player);
    CombineServiceNew.gI().sendEffectOpenItem(player, (short) 555, newItem.template.iconID);
    Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name + " kích hoạt!");
    ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa tạo thành công " + newItem.template.name + " từ 5 món Thần Linh!");
    player.combineNew.itemsCombine.clear();
    reOpenItemCombine(player);
}

    public void chuyenSaoPhaLe(Player player) {
    if (player.combineNew.itemsCombine.size() != 3) {
        Service.getInstance().sendThongBao(player, "Dữ liệu không hợp lệ!");
        return;
    }
    Item itemCoSao = null, itemNhan = null;
    for (Item it : player.combineNew.itemsCombine) {
        if (getSao(it) > 0) itemCoSao = it;
        else if (it.template.id != 1429) itemNhan = it;
    }
    if (InventoryService.gI().getQuantityItemBagById(player, 1429) < 20000) {
        Service.getInstance().sendThongBao(player, "Không đủ 20.000 Thỏi vàng khóa!");
        return;
    }
    int star = getSao(itemCoSao);
    InventoryService.gI().subQuantityItemsBagById(player, 1429, 20000);
    InventoryService.gI().subQuantityItemsBag(player, itemCoSao, 1);
    boolean found107 = false;
    for (ItemOption io : itemNhan.itemOptions) {
        if (io.optionTemplate.id == 107) {
            io.param = star;
            found107 = true;
            break;
        }
    }
    if (!found107) itemNhan.itemOptions.add(new ItemOption(107, star));
    InventoryService.gI().sendItemBags(player);
    player.combineNew.itemsCombine.clear();
    sendEffectSuccessCombine(player);
    try {
        Thread.sleep(600); // delay nhẹ cho hiệu ứng xoay
    } catch (Exception e) {}
    Service.getInstance().sendThongBao(player, "Chuyển thành công " + star + " Sao Pha Lê!");
}
private int getSao(Item item) {
    for (ItemOption io : item.itemOptions)
        if (io.optionTemplate.id == 102)
            return io.param;
    return 0;
}

//private void taySaoPhaLe(Player player) {
//    if (player.combineNew.itemsCombine.size() != 2) {
//        Service.getInstance().sendThongBao(player,
//                "Hãy đặt 1 trang bị có Sao Pha Lê đã ép + 20 Đá tẩy!");
//        return;
//    }
//    Item trangBi = null, daTay = null;
//    for (Item it : player.combineNew.itemsCombine) {
//        if (it.template.id == 1630) {
//            daTay = it; // Đá tẩy
//        } else if (hasOption(it, 102)) {
//            trangBi = it; // Trang bị có SPL
//        }
//    }
//    if (trangBi == null) {
//        Service.getInstance().sendThongBao(player, "Không tìm thấy trang bị có Sao Pha Lê đã ép!");
//        return;
//    }
//    if (daTay == null || daTay.quantity < 20) {
//        Service.getInstance().sendThongBao(player, "Cần 20 Đá tẩy!");
//        return;
//    }
//    Item thoiVangKhoa = InventoryServiceNew.gI().findItemBag(player, 1429);
//    if (thoiVangKhoa == null || thoiVangKhoa.quantity < 10000) {
//        Service.getInstance().sendThongBao(player, "Cần 10.000 Thỏi vàng khóa để thực hiện!");
//        return;
//    }
//    int soSao = getParamOption(trangBi, 102);
//    if (soSao <= 0) {
//        Service.getInstance().sendThongBao(player, "Trang bị này không có Sao Pha Lê hợp lệ!");
//        return;
//    }
//    InventoryServiceNew.gI().subQuantityItemsBag(player, daTay, 20);
//    InventoryServiceNew.gI().subQuantityItemsBag(player, thoiVangKhoa, 10000);
//    InventoryServiceNew.gI().sendItemBags(player);
//    trangBi.itemOptions.removeIf(op ->
//            op.optionTemplate.id == 102 ||(op.optionTemplate.id >= 95 && op.optionTemplate.id <= 103) || op.optionTemplate.id == 77 || op.optionTemplate.id == 50
//    );
//    trangBi.itemOptions.add(new ItemOption(107, soSao));
//    sendEffectSuccessCombine(player);
//    Service.getInstance().sendThongBao(player,
//            "Tẩy Sao Pha Lê thành công!\n(Đã giữ nguyên " + soSao + " sao)");
//}
public void taySaoPhaLe(Player player) {
    if (player.combineNew.itemsCombine.size() != 2) {
        Service.getInstance().sendThongBao(player,
                "Hãy đặt 1 trang bị có Sao Pha Lê đã ép + 20 Đá tẩy");
        return;
    }
    Item trangBi = null, datay = null, tv = null;

    for (Item it : player.combineNew.itemsCombine) {
        if (it.template.id == 1630) {
            datay = it;//đá
        } else if (hasOption(it, 102)) { // có option 102 = đã ép sao
            trangBi = it;
        }
    }
    if (trangBi == null) {
        Service.getInstance().sendThongBao(player, "Không tìm thấy trang bị có Sao Pha Lê đã ép!");
        return;
    }
    if (datay == null || datay.quantity < 20) {
        Service.getInstance().sendThongBao(player, "Cần 20 Đá tẩy!");
        return;
    }
    Item thoiVangKhoa = InventoryServiceNew.gI().findItemBag(player, 1429);
    if (thoiVangKhoa == null || thoiVangKhoa.quantity < 10000) {
        Service.getInstance().sendThongBao(player, "Cần 10.000 Thỏi vàng khóa để thực hiện!");
        return;
    }
    int soSaoDaEp = getParamOption(trangBi, 102);
    if (soSaoDaEp <= 0) {
        Service.getInstance().sendThongBao(player, "Trang bị này không có Sao Pha Lê hợp lệ!");
        return;
    }
    int tongSoSao = getParamOption(trangBi, 107);
    if (tongSoSao <= 0) {
        tongSoSao = soSaoDaEp;
    }
    InventoryService.gI().subQuantityItemsBag(player, datay, 20);
    InventoryService.gI().subQuantityItemsBag(player, thoiVangKhoa, 10000);
    Service.getInstance().sendMoney(player);
    trangBi.itemOptions.removeIf(op ->
            op.optionTemplate.id == 102                              // số sao đã ép
            || (op.optionTemplate.id >= 95 && op.optionTemplate.id <= 101) // option buff từ sao
            || op.optionTemplate.id == 50
            || op.optionTemplate.id == 77
            || op.optionTemplate.id == 103
    );
    if (tongSoSao > 0) {
        ItemOption opt107 = null;
        for (ItemOption op : trangBi.itemOptions) {
            if (op.optionTemplate.id == 107) {
                opt107 = op;
                break;
            }
        }
        if (opt107 != null) {
            opt107.param = tongSoSao;
        } else {
            trangBi.itemOptions.add(new ItemOption(107, tongSoSao));
        }
    }
    InventoryService.gI().sendItemBags(player);
    sendEffectSuccessCombine(player);
    Service.getInstance().sendThongBao(player,
            "Tẩy Sao Pha Lê thành công!\nNhận lại trang bị " + tongSoSao + " sao (chưa ép).");
}
private boolean hasOption(Item item, int optionId) {
    if (item == null || item.itemOptions == null) return false;
    for (ItemOption op : item.itemOptions) {
        if (op.optionTemplate.id == optionId) {
            return true;
        }
    }
    return false;
}
private int getParamOption(Item item, int optionId) {
    if (item == null || item.itemOptions == null) return 0;
    for (ItemOption op : item.itemOptions) {
        if (op.optionTemplate.id == optionId) {
            return op.param;
        }
    }
    return 0;
}

    private void nangCapSKH_THUONG_GOLD_BAR(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            if (InventoryService.gI().getCountEmptyBag(player) > 0) {

                Item itemHDMain = player.combineNew.itemsCombine.get(0);
                if (itemHDMain.isDHD()) {

                    // Kiểm tra và trừ ruby thay vì vàng
                    if (player.inventory.ruby >= 200) {
                        player.inventory.ruby -= 200;
                        Service.getInstance().sendMoney(player);

                        byte gender = itemHDMain.template.gender;
                        if (itemHDMain.template.gender == 3 || itemHDMain.template.type == 4) { // Rada
                            gender = player.gender;
                        }

                        short itemId = ConstItem.doSKHVip[itemHDMain.template.type][gender][0];
                        int skhId = ItemService.gI().randomSKHId(gender);
                        Item item = ItemService.gI().createNewItem((short) itemId);
                        CombineServiceNew.gI().sendEffectOpenItem(player, itemHDMain.template.iconID,
                                item.template.iconID);
                        RewardService.gI().initBaseOptionClothes(item);
                        ItemService.gI().AddOptionSKH(item, skhId);
                        InventoryService.gI().addItemBag(player, item, 1);
                        InventoryService.gI().subQuantityItemsBag(player, itemHDMain, 1);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn vừa nâng cấp được " + item.template.name + " kích hoạt");
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bạn cần 200 ruby để nâng cấp",
                                "Đóng");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Cần 1 món đồ huỷ diệt");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Cần 1 món huỷ diệt");
        }
    }

    private void tienHoaSetKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Cần 1 món kích hoạt và 1 món hủy diệt");
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < COST) {
                Service.getInstance().sendThongBao(player, "Con cần 500 triệu vàng để chế tạo");
                return;
            }
            //
            if (isCoupleTienHoaKichHoat(player.combineNew.itemsCombine.get(0),
                    player.combineNew.itemsCombine.get(1))) {

                Item itemKHMain = null;
                Item itemHD = null;
                if (player.combineNew.itemsCombine.get(0).isSKH()) {
                    itemKHMain = player.combineNew.itemsCombine.get(0);
                    itemHD = player.combineNew.itemsCombine.get(1);
                } else {
                    itemKHMain = player.combineNew.itemsCombine.get(1);
                    itemHD = player.combineNew.itemsCombine.get(0);
                }
                if (itemKHMain == null || itemHD == null) {
                    Service.getInstance().sendThongBao(player, "Cần 1 trang bị kích hoạt và 1 trang bị hủy diệt");
                    return;
                }
                // Lấy cấp độ trang bị
                int level1 = 0;
                int length = ConstItem.doSKHVip[itemKHMain.template.type][itemKHMain.template.gender].length;
                for (int i = 0; i < length; i++) {
                    if (ConstItem.doSKHVip[itemKHMain.template.type][itemKHMain.template.gender][i] == itemKHMain.template.id) {
                        level1 = i;
                        break;
                    }
                }
                if (level1 > 13) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cấp độ trang bị kích hoạt tối đa là hủy diệt",
                            "Đóng");
                    return;
                }
                CombineServiceNew.gI().sendEffectOpenItem(player, itemKHMain.template.iconID,
                        itemHD.template.iconID);
                player.inventory.gold -= COST;
                if (Util.isTrue(1, 100)) {// tỉ lệ thành công
                    byte gender = itemKHMain.template.gender;

                    int idSkh = 0;
                    List<ItemOption> itemOptionOld = new ArrayList<>();
                    // lấy id kích hoạt
                    for (ItemOption io : itemKHMain.itemOptions) {
                        if (io.optionTemplate.id >= 127 && io.optionTemplate.id <= 135) {
                            idSkh = io.optionTemplate.id;
                        } else if (isChiSoEpSpl(io.optionTemplate.id)) {
                            // lấy chỉ số cũ
                            itemOptionOld.add(io);
                        }
                    }

                    // tạo item
                    Item item = ItemService.gI()
                            .createNewItem((short) ConstItem.doSKHVip[itemKHMain.template.type][gender][level1 + 1]);

                    RewardService.gI().initBaseOptionClothes(item);
                    ItemService.gI().AddOptionSKH(item, idSkh);
                    // add lại chỉ sô cũ
                    for (ItemOption io : itemOptionOld) {
                        item.itemOptions.add(io);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, itemKHMain, 1);
                    InventoryService.gI().subQuantityItemsBag(player, itemHD, 1);
                    InventoryService.gI().addItemBag(player, item, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    player.combineNew.itemsCombine.clear();

                    sendEffectSuccessCombine(player);
                    reOpenItemCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, itemKHMain, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);

                    sendEffectFailCombine(player);
                    reOpenItemCombine(player);
                }

            } else {
                Service.getInstance().sendThongBao(player, "Cần 1 trang bị kích hoạt và 1 trang bị hủy diệt");
                return;
            }

        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");

        }
    }

    private void doiManhKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
            Item nr1s = null, doThan = null, buaBaoVe = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 14) {
                    nr1s = it;
                } else if (it.template.id == 2010) {
                    buaBaoVe = it;
                } else if (it.template.id >= 555 && it.template.id <= 567) {
                    doThan = it;
                }
            }
            if (nr1s != null && doThan != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                    player.inventory.gold -= COST_DOI_MANH_KICH_HOAT;
                    int tiLe = buaBaoVe != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) 2009);
                        item.itemOptions.add(new ItemOption(30, 0));
                        InventoryService.gI().addItemBag(player, item, 0);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, nr1s, 1);
                    InventoryService.gI().subQuantityItemsBag(player, doThan, 1);
                    if (buaBaoVe != null) {
                        InventoryService.gI().subQuantityItemsBag(player, buaBaoVe, 1);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            }
        }
    }

    private void dapDoKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            Item dhd = null, dtl = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 650 && item.template.id <= 662) {
                        dhd = item;
                    } else if (item.template.id >= 555 && item.template.id <= 567) {
                        dtl = item;
                    }
                }
            }
            if (dhd != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
                    int tiLe = dtl != null ? 100 : 40;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI()
                                .createNewItem((short) getTempIdItemC0(dhd.template.gender, dhd.template.type));
                        RewardService.gI().initBaseOptionClothes(item);
                        RewardService.gI().initActivationOption(
                                item.template.gender < 3 ? item.template.gender : player.gender, item.template.type,
                                item.itemOptions);
                        InventoryService.gI().addItemBag(player, item, 0);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, dhd, 1);
                    if (dtl != null) {
                        InventoryService.gI().subQuantityItemsBag(player, dtl, 1);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiVeHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                    player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
                    Item ticket = ItemService.gI().createNewItem((short) (2001 + item.template.type));
                    InventoryService.gI().subQuantityItemsBag(player, item, 1);
                    InventoryService.gI().addItemBag(player, ticket, 99);
                    sendEffectOpenItem(player, item.template.iconID, ticket.template.iconID);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiTrangbiHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() >= 1 && player.combineNew.itemsCombine.size() <= 5) {
            Item tv = InventoryService.gI().findItemBag(player, 1429);
            if (tv.quantity < (long) ((long) COST_DOI_VE_DOI_DO_HUY_DIET
                    * player.combineNew.itemsCombine.size())) {
                Service.getInstance().sendThongBao(player, "Không đủ TVK để thực hiện");
                return;
            }
            String npcSay = "";
            String notifi = "";
            for (int i = 0; i < player.combineNew.itemsCombine.size(); i++) {
                Item item = player.combineNew.itemsCombine.get(i);
                Item thucAn = InventoryService.gI().findMealChangeDestroyClothes(player);
                if (thucAn == null) {
                    notifi = "Nâng cấp dừng lại vì bạn không đủ x99 thức ăn";
                    break;
                }
                if (item.isNotNullItem() && item.isDTL()) {
                    if (tv.quantity >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                        InventoryService.gI().subQuantityItemsBag(player, tv, COST_DOI_VE_DOI_DO_HUY_DIET);
                        Item HuyDiet = ItemService.gI().createNewItem((short) (item.template.id + 95));
                        RewardService.gI().initBaseOptionClothes(HuyDiet);

                        if (HuyDiet != null) {

                            InventoryService.gI().subQuantityItemsBag(player, item, 1);
                            InventoryService.gI().subQuantityItemsBag(player, thucAn, 99);
                            InventoryService.gI().addItemBag(player, HuyDiet, 1);
                            sendEffectOpenItem(player, item.template.iconID, HuyDiet.template.iconID);
                        }

                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);

                        npcSay += HuyDiet.template.name + "\n";
                    } else {
                        notifi = "Nâng cấp dừng lại vì bạn không đủ hồng ngọc";
                        break;
                    }
                }
            }
            reOpenItemCombine(player);
            if (npcSay != "") {
                Service.getInstance().sendThongBao(player, "Nâng cấp thành công, bạn vừa nhận được\n" + npcSay);
            } else {
                Service.getInstance().sendThongBao(player,
                        notifi);
            }

        }
    }

     private void epSaoTrangBi1(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; // sao pha lê đã ép
            int starEmpty = 0; // lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                ItemOption optionStar = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                int soluong = daPhaLe.quantity;
                if (soluong < starEmpty - star) {
                    Service.getInstance().sendThongBao(player, "Không đủ sao pha lê để ép full");
                    return;
                }
                if (star == 0) {
                    Service.getInstance().sendThongBao(player, "Vui lòng ép trước 1 sao");
                    return;
                }
                for (int i = star; i < starEmpty; i++) {
                    player.inventory.subGem(gem);
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    ItemOption option = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(102, 1));
                    }
                    InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
                }
                sendEffectSuccessCombine(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
     
    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; // sao pha lê đã ép
            int starEmpty = 0; // lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                ItemOption optionStar = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.subGem(gem);
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    ItemOption option = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(102, 1));
                    }

                    InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private boolean phaLeHoaTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            return phaLeHoaTrangBi_old(player);
        } else if (player.combineNew.itemsCombine.size() == 2) {
            return phaLeHoaTrangBi_new(player);
        } else {
            return false;
        }
    }

    private boolean phaLeHoaTrangBi_new(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            long gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            int countDaNangCap = player.combineNew.countDaNangCap;
            if (isCouplePhaLeHoa(player.combineNew.itemsCombine.get(0),
                    player.combineNew.itemsCombine.get(1))) {
                Item item = null;
                Item daNangCap = null;
                if (isTrangBiPhaLeHoa(player.combineNew.itemsCombine.get(0))) {
                    item = player.combineNew.itemsCombine.get(0);
                    daNangCap = player.combineNew.itemsCombine.get(1);
                } else {
                    item = player.combineNew.itemsCombine.get(1);
                    daNangCap = player.combineNew.itemsCombine.get(0);
                }
                if (daNangCap.quantity < countDaNangCap) {
                    return false;
                }
                if (item.template.type >= 5) {
                    Service.getInstance().sendThongBao(player,
                            "Chỉ có thể pha lê hóa trang bị áo quần găng giày rada bằng đá pha lê hóa");
                    return false;
                }
                if (player.inventory.gold < gold) {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return false;
                } else if (player.inventory.gem < gem) {
                    Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                    return false;
                }

                int star = 0;
                ItemOption optionStar = null;

                for (ItemOption io : item.itemOptions) {

                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.subGem(gem);

                    if (optionStar == null) {
                        item.itemOptions.add(new ItemOption(107, 1));
                    } else {
                        optionStar.param++;
                    }
                    sendEffectSuccessCombine(player);
                    if (optionStar != null && optionStar.param >= 7) {
                        ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa " + "thành công "
                                + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        ServerLog.logCombine(player.name, item.template.name, optionStar.param);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, daNangCap,
                            player.combineNew.countDaNangCap);
                    InventoryService.gI().sendItemBags(player);

                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                    return false;

                } else {
                    return false;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không thể pha lê hóa trang bị");
                return false;
            }

        }
        return false;
    }

    private boolean phaLeHoaTrangBi_old(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {

            long gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return false;
            } else if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return false;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                ItemOption optionStar = null;
                for (ItemOption io : item.itemOptions) {

                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.combineNew.ratioCombine = getRatioPhaLeHoaBip(star);
                    player.inventory.gold -= gold;
                    player.inventory.subGem(gem);
                    int ratio = 300;// tỉ lệ cơ bản
                    switch (star) {
                        case 1:
                            ratio = 100;
                            break;
                        case 2:
                        case 3:
                            ratio = 120;
                            break;
                        case 4:
                            ratio = 150;
                            break;
                        case 5:
                            ratio = 180;
                            break;
                        case 6:
                            ratio = 190;
                            break;
                        case 7:
                            ratio = 250;
                            break;
                        case 8:
                            ratio = 300;
                            break;
                    }
                    // cộng thêm tỉ lệ trên data
                    ratio += Util.calPercent(ratio, (SettingGame.RATIO_PHA_LE_HOA * 50));
                    // Logger.activeDebugMode("Ratio pha le hoa: " + star + " sao , ration: " +
                    // ratio);
                    // int themX = 2;
                    // if (star == 4) {
                    // themX = 3;
                    // } else if (star == 5) {
                    // themX = 3;
                    // } else if (star == 6) {
                    // themX = 4;
                    // } else if (star == 7) {
                    // themX = 4;
                    // } else if (star == 8) {
                    // themX = 7;
                    // }
                    if (star < 6) {
                        if (Util.isTrue(player.combineNew.ratioCombine, ratio)) {
                            // Logger.error("Tile: " + (player.combineNew.ratioCombine) + "/ " + ratio);
                            if (optionStar == null) {
                                item.itemOptions.add(new ItemOption(107, 1));
                            } else {
                                optionStar.param++;
                            }
                            if (optionStar != null && optionStar.param >= 7) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa " + "thành công "
                                        + item.template.name + " lên " + optionStar.param + " sao pha lê");
                                ServerLog.logCombine(player.name, item.template.name, optionStar.param);
                            }
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            reOpenItemCombine(player);
                            return false;
                        } else {
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            reOpenItemCombine(player);
                            return true;
                        }
                    } else if (star == 6) {
                        if (player.numDap < SettingGame.num7Sao) {
                            player.numDap++;
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            reOpenItemCombine(player);
                            return true;
                        } else {
                            player.numDap = 0;
                            optionStar.param++;
                            if (optionStar != null && optionStar.param >= 7) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa " + "thành công "
                                        + item.template.name + " lên " + optionStar.param + " sao pha lê");
                                ServerLog.logCombine(player.name, item.template.name, optionStar.param);
                            }
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            reOpenItemCombine(player);
                            return false;
                        }
                    } else if (star == 7) {
                        if (player.numDap < SettingGame.num8Sao) {
                            player.numDap++;
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            reOpenItemCombine(player);
                            return true;
                        } else {
                            player.numDap = 0;
                            optionStar.param++;
                            if (optionStar != null && optionStar.param >= 7) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa " + "thành công "
                                        + item.template.name + " lên " + optionStar.param + " sao pha lê");
                                ServerLog.logCombine(player.name, item.template.name, optionStar.param);
                            }
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            reOpenItemCombine(player);
                            return false;
                        }
                    }

                } else {
                    return false;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không thể pha lê hóa trang bị");
                return false;
            }

        }
        return false;
    }

    private boolean isOptionDontAddZeno(ItemOption io) {
        if (io.optionTemplate.id == 50 || io.optionTemplate.id == 77 || io.optionTemplate.id == 103
                || io.optionTemplate.id == 102 || io.optionTemplate.id == 107
                || io.optionTemplate.id == 196 || io.optionTemplate.id == 199) {
            return true;
        }
        return false;
    }

    private void epSaoZeno(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            long gold = player.combineNew.goldCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item trangBi = player.combineNew.itemsCombine.get(0);
            Item daPhaLe = player.combineNew.itemsCombine.get(1);

            if (!isTrangBiZeno(trangBi) || !isTrangBiZeno(trangBi)) {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 2 cải trang có chỉ số (Có thể ghép) mua tại Mr Santa",
                        "Đóng");
            }

            int star = 0; // sao pha lê đã ép
            int starEmpty = 0; // lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                if (!isTrangBiNguyenLieuZeno(daPhaLe)) {
                    Service.getInstance().sendThongBao(player, "Nguyên liệu không phù hợp");
                    return;
                }
                if (!checkZenoHoaOK(trangBi, daPhaLe)) {
                    Service.getInstance().sendThongBao(player, "Trang bị này đã ép vào rồi");
                    return;
                }
                ItemOption optionStar = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }

                if (star < starEmpty) {
                    player.inventory.subGem(gem);
                    player.inventory.gold -= gold;

                    // List<ItemOption> optionBase = new ArrayList<>();
                    for (ItemOption io : daPhaLe.itemOptions) {
                        if (isOptionDontAddZeno(io)) {
                            continue;
                        }
                        boolean checkOptionIsset = false;
                        for (ItemOption io2 : trangBi.itemOptions) {
                            if (isOptionDontAddZeno(io2)) {
                                continue;
                            }
                            if (io2.optionTemplate.id == io.optionTemplate.id) {
                                io2.param += io.param;
                                checkOptionIsset = true;
                                break;
                            }
                        }
                        if (!checkOptionIsset) {
                            trangBi.itemOptions.add(io);
                        }

                    }

                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(102, 1));
                    }
                    // if (optionStar != null && optionStar.param == 7) {
                    // Item superZeno = ItemService.gI().createNewItem((short) (601 +
                    // player.gender));

                    // List<ItemOption> ioBase = new ArrayList<ItemOption>();
                    // for (ItemOption ioB : trangBi.itemOptions) {
                    // ioBase.add(ioB);
                    // }
                    // for (ItemOption ioB : ioBase) {
                    // if (ioB.optionTemplate.id != 196) {
                    // superZeno.itemOptions.add(ioB);
                    // }
                    // }
                    // InventoryService.gI().subQuantityItemsBag(player, trangBi,
                    // 1);
                    // InventoryService.gI().addItemBag(player, superZeno, 0);
                    // Service.getInstance().sendThongBao(player,
                    // "Pha lê hỏa cải trang thành công, cải trang của bạn đạt cấp độ tối đa");
                    // } else {
                    // Service.getInstance().sendThongBao(player, "Pha lê hỏa cải trang thành
                    // công");
                    // }
                    InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }

                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private boolean phaLeHoaZeno(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            long gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            int countDaNangCap = player.combineNew.countDaNangCap;
            if (isCoupleZeno(player.combineNew.itemsCombine.get(0),
                    player.combineNew.itemsCombine.get(1))) {
                Item item = null;
                Item daNangCap = null;
                if (player.combineNew.itemsCombine.get(0).template.type == 5) {
                    item = player.combineNew.itemsCombine.get(0);
                    daNangCap = player.combineNew.itemsCombine.get(1);
                } else {
                    item = player.combineNew.itemsCombine.get(1);
                    daNangCap = player.combineNew.itemsCombine.get(0);
                }
                // if (!item.isTrangBiGhepMain()) {
                // Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                // return false;
                // }
                if (daNangCap.quantity < countDaNangCap) {
                    return false;
                }

                if (player.inventory.gold < gold) {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return false;
                } else if (player.inventory.gem < gem) {
                    Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                    return false;
                }

                int star = 0;

                ItemOption optionStar = null;

                for (ItemOption io : item.itemOptions) {

                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.subGem(gem);
                    float ratiohienthi = getRatioZeno(star);
                    if (player.isAdmin()) {
                        ratiohienthi = 120;
                    }
                    if (Util.isTrue(ratiohienthi, 150)) {

                        if (optionStar == null) {
                            item.itemOptions.add(new ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }

                        Service.getInstance().sendThongBao(player, "Pha lê hỏa cải trang thành công");

                        sendEffectSuccessCombine(player);

                    } else {
                        sendEffectFailCombine(player);
                        Service.getInstance().sendThongBao(player, "Nâng cấp thất bại");
                    }

                    InventoryService.gI().subQuantityItemsBag(player, daNangCap,
                            player.combineNew.countDaNangCap);
                    InventoryService.gI().sendItemBags(player);

                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                    return false;

                } else {
                    return false;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không thể pha lê hóa trang bị");
                return false;
            }

        }
        return false;
    }

    private boolean nhapNgocRong(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem()) {
                    if ((item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                        Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                        InventoryService.gI().addItemBag(player, nr, 0);
                        InventoryService.gI().subQuantityItemsBag(player, item, 7);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                        sendEffectCombineDB(player, item.template.iconID);
                        Service.getInstance().sendThongBao(player,
                                "Bạn vừa nhận được " + nr.template.name);
                        return true;
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Hết vật phẩm nguyên liệu");
                        return false;
                    }
                } else {
                    Service.getInstance().sendThongBao(player,
                            "Hết vật phẩm nguyên liệu");
                    return false;
                }
            } else {
                Service.getInstance().sendThongBao(player,
                        "Hãy bỏ ngọc rồng vào");
                return false;
            }
        } else {
            Service.getInstance().sendThongBao(player,
                    "Hành trang không đủ ô trống");
            return false;
        }

    }

    private void doi_ruong_go(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem()) {
                    if (item.template.id == 570 && item.quantity >= 1) {
                        int param = Util.nextInt(8, 11);
                        Item nr = ItemService.gI().createNewItem((short) (570));
                        nr.itemOptions.add(new ItemOption(72, param));
                        InventoryService.gI().subQuantityItemsBag(player, item, 1);
                        InventoryService.gI().addItemBag(player, nr, 0);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                        sendEffectCombineDB(player, nr.template.iconID);
                        Service.getInstance().sendThongBao(player,
                                "Phù phép rương gỗ thành công, rương của con phù phép được cấp " + param);
                    }
                }
            }
        }
    }

    private void nangcapthiensu(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            long gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item dohd = null;
            Item da = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isAngelClothes(item.template.id)) {
                    dohd = item;
                } else if (isLuckyStone(item.template.id)) {
                    da = item;
                }
            }

            ItemOption dadap = null;
            ItemOption cap = null;
            if (dohd != null) {
                for (ItemOption io : dohd.itemOptions) {
                    if (io.optionTemplate.id == 30) {
                        dadap = io;
                    }
                    if (io.optionTemplate.id == 72) {
                        cap = io;
                    }
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 1 trang bị Thiên sứ chưa nâng cấp chỉ số hay nâng cấp", "Đóng");
            }
            if (dadap != null && dadap.param < 1 && cap == null) {
                if (dohd != null && da != null) {
                    player.inventory.gold -= gold;
                    int bounus = Util.nextInt(1, 5);
                    player.combineNew.ratioCombine = getratiodamayma(da.template.id);
                    InventoryService.gI().subQuantityItemsBag(player, da, 1);
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        switch (dohd.template.type) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                short[] listOp = {216, 97, 213, 214, 215, 5, 201, 202, 203};
                                int rdIdop = listOp[Util.nextInt(listOp.length)];
                                dohd.itemOptions.add(new ItemOption(rdIdop, bounus));
                                break;
                        }
                        sendEffectSuccessCombine(player);
                        bill.npcChat(player, "Chúc mừng bạn đã nâng cấp thành công");
                    } else {
                        sendEffectFailCombine(player);
                        bill.npcChat(player, "Nâng cấp thất bại");
                    }
                    player.combineNew.itemsCombine.clear();
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapBongTai(Player player) {

        Item tv = InventoryService.gI().findItemBag(player, 1429);
        if (tv == null || tv.quantity < 20000) return;
        if (player.combineNew.itemsCombine.size() < 2) return;

        Item bt = null, manh = null;

        for (Item it : player.combineNew.itemsCombine) {
            if (it.template.id == 454) bt = it;
            else if (it.template.id == 933) manh = it;
        }

        if (bt == null || manh == null || manh.quantity < 99) return;

        InventoryService.gI().subQuantityItemsBag(player, tv, 20000);
        InventoryService.gI().subQuantityItemsBag(player, manh, 99);

        bt.template = ItemService.gI().getTemplate(921);

        CombineServiceNew.gI().sendEffectOpenItem(player, (short) 3896, (short) 7993);

        InventoryService.gI().sendItemBags(player);
        reOpenItemCombine(player);
        }

    private void nangCapChiSoBongTai(Player player) {         
    if (player.combineNew.itemsCombine.size() == 3) {
        Item bongTai = null;
        Item manhHon = null;
        Item Xlam = null;
        Item tvKhoa = null;
        for (Item it : player.combineNew.itemsCombine) {
            if (it.getId() == 921) {
                bongTai = it;
            }
            if (it.getId() == 934) {
                manhHon = it;
            }
            if (it.getId() == 935) {
                Xlam = it;
            }
        }
        tvKhoa = InventoryService.gI().findItemBag(player, 1429);
        if (bongTai != null && manhHon != null && Xlam != null
                && manhHon.quantity >= 99 && Xlam.quantity >= 5
                && tvKhoa != null && tvKhoa.quantity >= 2000) {
            if (Util.isTrue(40, 100)) {
                List<ItemOption> listRemove = new ArrayList<>();
                if (bongTai.itemOptions != null) {
                    for (ItemOption ios : bongTai.itemOptions) {
                        if (IsChiSoBongTai(ios.optionTemplate.id)) {
                            listRemove.add(ios);
                        }
                    }
                }
                for (ItemOption op : listRemove) {
                    bongTai.itemOptions.remove(op);
                }
                int listChiSo[] = {5, 14, 50, 77, 80, 81, 94, 101, 103, 108};
                int idChiSo = listChiSo[Util.nextInt(listChiSo.length)];
                bongTai.itemOptions.add(new ItemOption(idChiSo, Util.nextInt(3, 12)));
                CombineServiceNew.gI().sendEffectOpenItem(player, bongTai.template.iconID, bongTai.template.iconID);
                Service.getInstance().sendThongBao(player, "Bạn vừa nâng cấp thành công " + bongTai.template.name);
            } else {
                CombineServiceNew.gI().sendEffectFailCombine(player);
                Service.getInstance().sendThongBao(player, "Nâng cấp thất bại");
            }
            InventoryService.gI().subQuantityItemsBag(player, manhHon, 99);
            InventoryService.gI().subQuantityItemsBag(player, Xlam, 5);
            InventoryService.gI().subQuantityItemsBag(player, tvKhoa, 2000);
            InventoryService.gI().sendItemBags(player);
            reOpenItemCombine(player);
        }
    }
}

    private void cheTaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() >= 4 && player.combineNew.itemsCombine.size() <= 5) {

            if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                Service.getInstance().sendThongBao(player, "Hành trang cần 1 ô trống");
                return;
            }
            if (player.inventory.gold < COST_DOI_VE_DOI_DO_HUY_DIET) {
                Service.getInstance().sendThongBao(player, "Cần 500 triệu vàng để chế tạo");
                return;
            }
            int count_bua = 0;
            int count_trang_bi = 0;
            int count_da_may_man = 0;
            short[] sendIcon = new short[2];
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1394) {
                    count_bua += 1;
                    sendIcon[0] = item.template.iconID;
                } else if (item.isCheTao()) {
                    count_trang_bi += 1;
                } else if (item.template.id == 1307) {
                    count_da_may_man += 1;
                }
            }
            if (count_bua != 1 || count_trang_bi != 3) {
                this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 3 trang bị có chỉ số {Có thể chế tạo} và 1 bùa pháp sư để chế tạo",
                        "Đóng");
                return;
            }
            short id_item = -1;
            int ratioMayMan = 0;
            if (count_da_may_man == 1) {
                ratioMayMan = 5;
            }
            // if (Util.isTrue(20 - ratioMayMan, 100)) {
            // short listRac[] = {};
            // id_item = Util.randomItem(listRac);
            //
            // } else
            if (Util.isTrue(30 - ratioMayMan, 100)) {
                int randomType = RewardService.gI().generateTypeTrangBi();
                int gender = Util.nextInt(0, player.gender);
                if (Util.isTrue((3 + count_da_may_man), 100)) {// ra đồ thần
                    id_item = ConstItem.doSKHVip[randomType][gender][Util
                            .nextInt(11, 13)];

                } else {// ra đồ thường
                    id_item = ConstItem.doSKHVip[randomType][gender][Util
                            .nextInt(7, 10)];
                }
            } else {
                if (count_da_may_man == 1) {
                    // vip có cải trang
                    id_item = ItemService.gI().getRandomPhuKien(-1, player);
                } else {
                    // cùi không có cải trang
                    short listTypeCui[] = {98, 23, 99};
                    if (Util.isTrue(4, 100)) {
                        listTypeCui = new short[]{5, 11};
                    }
                    id_item = ItemService.gI().getRandomPhuKien(Util.randomItem(listTypeCui), player);
                }
            }
            if (id_item == -1) {
                this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Chế tạo thất bại, hãy thử lại",
                        "Đóng");
            }
            Item item_che_tao = ItemService.gI().createNewItem((short) id_item);
            if (count_da_may_man == 0) {
                ItemService.gI().OptionAllItem(item_che_tao, 97);
            } else {
                ItemService.gI().OptionAllItem(item_che_tao, 92);
            }

            boolean isHSD = false;
            if (item_che_tao != null && is_body_item(item_che_tao)) {
                if (item_che_tao.template.type <= 4) {// sao pha lê
                    item_che_tao.itemOptions.add(new ItemOption(107, Util.nextInt(0, 6)));
                }

                item_che_tao.itemOptions.add(new ItemOption(236, 1));// có thể chế tạo
            }
            sendIcon[1] = item_che_tao.template.iconID;
            player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
            CombineServiceNew.gI().sendEffectOpenItem(player, sendIcon[0], sendIcon[1]);
            for (Item item : player.combineNew.itemsCombine) {
                InventoryService.gI().subQuantityItemsBag(player, item, 1);
            }
            InventoryService.gI().addItemBag(player, item_che_tao, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);

            reOpenItemCombine(player);
            if (item_che_tao.isDHD() || item_che_tao.isDTL() || (item_che_tao.template.type == 5 && !isHSD)) {
                String vinhVien = " ";
                if (item_che_tao.template.type == 5 && !isHSD) {
                    vinhVien = " vĩnh viễn ";
                }
                ServerNotify.gI()
                        .notify(player.name + " vừa chế tạo được " + item_che_tao.template.name + vinhVien
                                + "tại NPC Toribot");
            }
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item_che_tao.template.name);

        } else {
            this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 3 trang bị có chỉ số {Có thể chế tạo} và 1 bùa pháp sư để chế tạo",
                    "Đóng");
        }

    }

    private void cheTaoTrangBi_DaMayMan(Player player) {
        if (player.combineNew.itemsCombine.size() == 5) {

            if (InventoryService.gI().getCountEmptyBag(player) < 1) {
                Service.getInstance().sendThongBao(player, "Hành trang cần 1 ô trống");
                return;
            }
            if (player.inventory.gold < COST_DOI_VE_DOI_DO_HUY_DIET) {
                Service.getInstance().sendThongBao(player, "Cần 500 triệu vàng để chế tạo");
                return;
            }
            int count_bua = 0;
            int count_trang_bi = 0;
            int count_da_may_man = 0;
            Item bua = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1394) {
                    count_bua += 1;
                    bua = item;
                } else if (item.isCheTao()) {
                    count_trang_bi += 1;
                } else if (item.template.id == 1307) {
                    count_da_may_man += 1;
                }
            }
            if (count_bua != 1 || count_trang_bi != 3 || count_da_may_man != 1) {
                this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 3 trang bị có chỉ số {Có thể chế tạo} và 1 bùa pháp sư và 1 đá may mắn",
                        "Đóng");
                return;
            }
            short id_item = 20;
            if (Util.isTrue(40, 100)) {
                if (Util.isTrue(90, 100)) {// ra đồ thường
                    id_item = ConstItem.doSKHVip[Util.nextInt(4)][Util.nextInt(player.gender)][Util
                            .nextInt(7, 11)];
                } else {
                    id_item = ConstItem.doSKHVip[Util.nextInt(4)][Util.nextInt(player.gender)][Util
                            .nextInt(12, 13)];
                }
            } else {
                id_item = ConstItem.items_Che_Tao_Thuong[Util.nextInt(ConstItem.items_Che_Tao_Thuong.length - 1)];
            }

            Item item_che_tao = ItemService.gI().createNewItem((short) id_item);
            ItemService.gI().OptionAllItem(item_che_tao, 90);
            boolean isHSD = false;
            if (item_che_tao != null && is_body_item(item_che_tao)) {
                if (item_che_tao.template.type <= 4) {
                    item_che_tao.itemOptions.add(new ItemOption(107, Util.nextInt(3, 6)));
                }
                item_che_tao.itemOptions.add(new ItemOption(236, 1));// có thể chế tạo
            }

            player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
            CombineServiceNew.gI().sendEffectOpenItem(player, bua.template.iconID, item_che_tao.template.iconID);
            for (Item item : player.combineNew.itemsCombine) {
                InventoryService.gI().subQuantityItemsBag(player, item, 1);
            }
            InventoryService.gI().addItemBag(player, item_che_tao, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);

            reOpenItemCombine(player);
            if (item_che_tao.isDHD() || item_che_tao.isDTL() || (item_che_tao.template.type == 5 && !isHSD)) {
                String vinhVien = " ";
                if (item_che_tao.template.type == 5 && !isHSD) {
                    vinhVien = " vĩnh viễn ";
                }
                ServerNotify.gI()
                        .notify(player.name + " vừa chế tạo được " + item_che_tao.template.name + vinhVien
                                + "tại NPC Toribot");
            }
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item_che_tao.template.name);

        } else {
            this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 3 trang bị có chỉ số {Có thể chế tạo} và 1 bùa pháp sư để chế tạo",
                    "Đóng");
        }

    }

    public void dapchisobongtai(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            long gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            Item nuocphep = null;
            Item ngocmman = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 921) {
                    bongTai = item;
                } else if (item.template.id == 933) {
                    manhVo = item;
                } else if (item.template.id == 934) {
                    nuocphep = item;
                } else if (item.template.id == 935) {
                    ngocmman = item;
                }
            }
            if (bongTai != null && manhVo != null && nuocphep != null && ngocmman != null
                    && manhVo.quantity >= 99 && nuocphep.quantity >= 99 && ngocmman.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryService.gI().subQuantityItemsBag(player, nuocphep, 99);
                InventoryService.gI().subQuantityItemsBag(player, manhVo, 99);
                InventoryService.gI().subQuantityItemsBag(player, ngocmman, 99);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(921);
                    bongTai.itemOptions.clear();
                    bongTai.itemOptions.add(new ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 4);
                    if (rdUp == 0) {
                        bongTai.itemOptions.add(new ItemOption(50, Util.nextInt(5, 8)));
                    } else if (rdUp == 1) {
                        bongTai.itemOptions.add(new ItemOption(77, Util.nextInt(5, 8)));
                    } else if (rdUp == 2) {
                        bongTai.itemOptions.add(new ItemOption(103, Util.nextInt(5, 8)));
                    } else if (rdUp == 3) {
                        bongTai.itemOptions.add(new ItemOption(14, Util.nextInt(5, 8)));
                    } else if (rdUp == 4) {
                        bongTai.itemOptions.add(new ItemOption(5, Util.nextInt(5, 8)));
                    }
                    bongTai.itemOptions.add(new ItemOption(30, 0));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    // private void nangCapVatPham(Player player) {
    // if (player.combineNew.itemsCombine.size() == 2) {
    // if (isCoupleItemNangCap(player.combineNew.itemsCombine.get(0),
    // player.combineNew.itemsCombine.get(1))) {
    // int countDaNangCap = player.combineNew.countDaNangCap;
    // int gold = player.combineNew.goldCombine;
    // if (player.inventory.gold < gold) {
    // Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
    // return;
    // }
    // Item trangBi = null;
    // Item daNangCap = null;
    // Item veBaoVe = InventoryService.gI().findBuaBaoVeNangCap(player);
    // if (player.combineNew.itemsCombine.get(0).template.type < 5) {
    // trangBi = player.combineNew.itemsCombine.get(0);
    // daNangCap = player.combineNew.itemsCombine.get(1);
    // } else {
    // trangBi = player.combineNew.itemsCombine.get(1);
    // daNangCap = player.combineNew.itemsCombine.get(0);
    // }
    // if (daNangCap.quantity < countDaNangCap) {
    // return;
    // }
    // int level = 0;
    // ItemOption optionLevel = null;
    // for (ItemOption io : trangBi.itemOptions) {
    // if (io.optionTemplate.id == 72) {
    // level = io.param;
    // optionLevel = io;
    // break;
    // }
    // }
    // if (level < MAX_LEVEL_ITEM) {
    // player.inventory.gold -= gold;
    // ItemOption option = null;
    // ItemOption option2 = null;
    // for (ItemOption io : trangBi.itemOptions) {
    // if (io.optionTemplate.id == 47 || io.optionTemplate.id == 6 ||
    // io.optionTemplate.id == 0
    // || io.optionTemplate.id == 7 || io.optionTemplate.id == 14 ||
    // io.optionTemplate.id == 22
    // || io.optionTemplate.id == 23) {
    // option = io;
    // } else if (io.optionTemplate.id == 27 || io.optionTemplate.id == 28) {
    // option2 = io;
    // }
    // }
    // float ratioCombine;
    // if (player.iDMark.isUseTuiBaoVeNangCap && veBaoVe != null) {
    // ratioCombine = 100;
    // InventoryService.gI().subQuantityItemsBag(player, veBaoVe, 1);
    // } else {
    // ratioCombine = player.combineNew.ratioCombine;
    // }
    // if (Util.isTrue(ratioCombine, 100)) {
    // option.param += (option.param * 10 / 100);
    // if (option2 != null) {
    // option2.param += (option2.param * 10 / 100);
    // }
    // if (optionLevel == null) {
    // trangBi.itemOptions.add(new ItemOption(72, 1));
    // } else {
    // optionLevel.param++;
    // }
    // if (optionLevel != null && optionLevel.param >= 5) {
    // ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp " +
    // "thành công "
    // + trangBi.template.name + " lên +" + optionLevel.param);
    // }
    // sendEffectSuccessCombine(player);
    // } else {
    // if (level == 2 || level == 4 || level == 6) {
    // option.param -= (option.param * 10 / 100);
    // if (option2 != null) {
    // option2.param -= (option2.param * 10 / 100);
    // }
    // optionLevel.param--;
    // }
    // sendEffectFailCombine(player);
    // }
    // InventoryService.gI().subQuantityItemsBag(player, daNangCap,
    // player.combineNew.countDaNangCap);
    // InventoryService.gI().sendItemBags(player);
    // Service.getInstance().sendMoney(player);
    // reOpenItemCombine(player);
    // }
    // }
    // }
    // }
    private boolean nangCapVatPham(Player player) {
        TransactionService.gI().cancelTrade(player);
        if (player.combineNew.itemsCombine.size() == 2) {
            Item trangBi = null;
            Item daNangCap = null;
            if (player.combineNew.itemsCombine.get(0).template.type < 5) {
                trangBi = player.combineNew.itemsCombine.get(0);
                daNangCap = player.combineNew.itemsCombine.get(1);
            } else {
                trangBi = player.combineNew.itemsCombine.get(1);
                daNangCap = player.combineNew.itemsCombine.get(0);
            }
            if (isCoupleItemNangCap(trangBi,
                    daNangCap)) {
                SetNangCapVatPham(player, trangBi, daNangCap, null);
            } else {
                Service.getInstance().sendThongBao(player, "Trang bị không phù hợp");
            }
            return false;
        } else if (player.combineNew.itemsCombine.size() == 3) {
            Item trangBi = null;
            Item daNangCap = null;
            Item daBaoVe = null;
            if (player.combineNew.itemsCombine.get(0).template.type < 5) {
                trangBi = player.combineNew.itemsCombine.get(0);
                if (player.combineNew.itemsCombine.get(1).template.type == 14) {
                    daNangCap = player.combineNew.itemsCombine.get(1);
                    daBaoVe = player.combineNew.itemsCombine.get(2);
                } else {
                    daNangCap = player.combineNew.itemsCombine.get(2);
                    daBaoVe = player.combineNew.itemsCombine.get(1);
                }
            } else if (player.combineNew.itemsCombine.get(1).template.type < 5) {
                trangBi = player.combineNew.itemsCombine.get(1);
                if (player.combineNew.itemsCombine.get(0).template.type == 14) {
                    daNangCap = player.combineNew.itemsCombine.get(0);
                    daBaoVe = player.combineNew.itemsCombine.get(2);
                } else {
                    daNangCap = player.combineNew.itemsCombine.get(2);
                    daBaoVe = player.combineNew.itemsCombine.get(0);
                }
            } else if (player.combineNew.itemsCombine.get(2).template.type < 5) {
                trangBi = player.combineNew.itemsCombine.get(2);
                if (player.combineNew.itemsCombine.get(0).template.type == 14) {
                    daNangCap = player.combineNew.itemsCombine.get(0);
                    daBaoVe = player.combineNew.itemsCombine.get(1);
                } else {
                    daNangCap = player.combineNew.itemsCombine.get(1);
                    daBaoVe = player.combineNew.itemsCombine.get(0);
                }
            } else {
                Service.getInstance().sendThongBao(player, "Nâng cấp vật phẩm gồm trang bị, đá nâng cấp và đá bảo vệ");
                return false;
            }
            if (daBaoVe.template.id != 987) {
                Service.getInstance().sendThongBao(player, "Hãy mang cho ta đá bảo vệ để bảo hộ trang bị");
                return false;
            }
            if (isCoupleItemNangCap(trangBi,
                    daNangCap)) {
                if (SetNangCapVatPham(player, trangBi, daNangCap, daBaoVe)) {
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                    return true;
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            } else {
                Service.getInstance().sendThongBao(player, "Trang bị không phù hợp");
                return false;
            }
            return false;
        } else {
            Service.getInstance().sendThongBao(player, "Trang bị không phù hợp");
            return false;
        }
    }

    private boolean SetNangCapVatPham(Player player, Item trangBi, Item daNangCap, Item daBaoVe) {

        int countDaNangCap = player.combineNew.countDaNangCap;
        long gold = player.combineNew.goldCombine;
        if (player.inventory.gold < gold) {
            Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
            return false;
        }

        if (daNangCap.quantity < countDaNangCap) {
            Service.getInstance().sendThongBao(player, "Không đủ đá nâng cấp để thực hiện");
            return false;
        }
        int level = 0;
        ItemOption optionLevel = null;

        for (ItemOption io : trangBi.itemOptions) {
            if (io.optionTemplate.id == 72) {
                level = io.param;
                optionLevel = io;
                break;
            }
        }
        ItemOption optionNangCap = null;
        for (ItemOption io : trangBi.itemOptions) {
            if (io.optionTemplate.id == 201) {
                optionNangCap = io;
                break;
            }
        }
        if (level < MAX_LEVEL_ITEM) {

            player.inventory.gold -= gold;
            ItemOption option = null;
            ItemOption option2 = null;
            for (ItemOption io : trangBi.itemOptions) {
                if (io.optionTemplate.id == 47
                        || io.optionTemplate.id == 6
                        || io.optionTemplate.id == 0
                        || io.optionTemplate.id == 7
                        || io.optionTemplate.id == 14
                        || io.optionTemplate.id == 22
                        || io.optionTemplate.id == 23
                        || io.optionTemplate.id == 193) {
                    option = io;
                } else if (io.optionTemplate.id == 27
                        || io.optionTemplate.id == 28
                        || io.optionTemplate.id == 194
                        || io.optionTemplate.id == 195) {
                    option2 = io;
                }
            }
            float ratio = player.combineNew.ratioCombine;
            int ratio_rage = 120;
            ratio_rage += SettingGame.RATIO_NANG_CAP * 50;
            if (Util.isTrue(ratio, ratio_rage)) {
                if (Util.calPercent(option.param, 10) == 0) {
                    option.param += 1;
                } else {
                    option.param += Util.calPercent(option.param, 10);
                }

                if (option2 != null) {
                    if (Util.calPercent(option2.param, 10) == 0) {
                        option2.param += 1;
                    } else {
                        option2.param += Util.calPercent(option2.param, 10);
                    }

                }
                if (optionLevel == null) {
                    trangBi.itemOptions.add(new ItemOption(72, 1));
                    trangBi.itemOptions.add(new ItemOption(205, 1));
                } else {
                    optionLevel.param++;
                }
                if (SettingGame.is_option_level) {
                    if (optionNangCap == null) {
                        // Logger.warning("chi so moi");
                        trangBi.itemOptions.add(new ItemOption(201, 2));
                    } else {
                        // Logger.warning("chi so moi");
                        if (optionLevel != null) {
                            optionNangCap.param = optionLevel.param * 2;
                        }

                    }
                }

                if (optionLevel != null && optionLevel.param >= 6) {
                    ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
                            + "thành công " + trangBi.template.name + " lên +" + optionLevel.param);
                }
                InventoryService.gI().subQuantityItemsBag(player, daNangCap,
                        player.combineNew.countDaNangCap);
                sendEffectSuccessCombine(player);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
                return false;
            } else {
                InventoryService.gI().subQuantityItemsBag(player, daNangCap,
                        player.combineNew.countDaNangCap);
                if (level > 2 && level % 2 != 0) {

                    if (daBaoVe == null || daBaoVe.quantity < 1) {
                        option.param -= (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param -= (option2.param * 10 / 100);
                        }
                        optionLevel.param--;
                        if (SettingGame.is_option_level) {
                            if (optionLevel != null) {
                                optionNangCap.param = optionLevel.param * 2;
                            }
                        }

                        sendEffectFailCombine(player);
                        InventoryService.gI().sendItemBags(player);
                        return false;
                    } else {
                        InventoryService.gI().subQuantityItemsBag(player, daBaoVe, 1);
                        InventoryService.gI().sendItemBags(player);
                        sendEffectFailCombine(player);
                        return true;
                    }

                }

            }
            Service.getInstance().sendMoney(player);
        } else {
            Service.getInstance().sendThongBao(player, "Trang bị đã đạt cấp tối đa");
            return false;
        }
        return true;

    }

    private int setParamNguSac(int option) {
        int param = 0;
        switch (option) {
            case 244: // Ngũ sắc: +#% HP
                param = 5;
                break;
            case 245: // Ngũ sắc: +#% KI
                param = 5;
                break;
            case 246: // Ngũ sắc: +#% sức đánh
                param = 5;
                break;
            case 247: // Ngũ sắc: +#% TNSM
                param = 5;
                break;
            case 248: // Ngũ sắc: +#% chí mạng
                param = 3;
                break;
            case 249: // Ngũ sắc: +# HP
                param = 5;
                break;
            case 250: // Ngũ sắc: +# KI
                param = 5;
                break;
            case 251: // Ngũ sắc: +# tấn công
                param = 500;
                break;
            case 252: // Ngũ sắc: +#% sát thương chí mạng
                param = 5;
                break;
            case 253: // Ngũ sắc: +#% sát thương chiêu cuối
                param = 5;
                break;
            case 254: // Ngũ sắc: +#% thời gian hồi chiêu cuối
                param = 2;
                break;
        }
        return param;
    }

    private boolean nangCapPhuKien(Player player) {
        try {
            boolean isSusces = false;
            TransactionService.gI().cancelTrade(player);
            if (player.combineNew.itemsCombine.size() == 2) {
                if (isCouplePhuKien(player.combineNew.itemsCombine.get(0),
                        player.combineNew.itemsCombine.get(1))) {
                    Item trangBi = null;
                    Item daNangCap = null;
                    if (player.combineNew.itemsCombine.get(0).template.type == 5
                            || player.combineNew.itemsCombine.get(0).template.type == 32) {
                        trangBi = player.combineNew.itemsCombine.get(0);
                        daNangCap = player.combineNew.itemsCombine.get(1);
                    } else {
                        trangBi = player.combineNew.itemsCombine.get(1);
                        daNangCap = player.combineNew.itemsCombine.get(0);
                    }

                    int countDaNangCap = player.combineNew.countDaNangCap;
                    long gold = player.combineNew.goldCombine;
                    int level = 0;
                    ItemOption optionLevel = null;

                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 72) {
                            level = io.param;
                            optionLevel = io;
                            break;
                        }
                    }
                    ItemOption optionNangCap = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (isChiSoNguSac(io.optionTemplate.id)) {
                            optionNangCap = io;
                            break;
                        }
                    }
                    if (player.inventory.gold < gold) {
                        Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                        return false;
                    }
                    if (daNangCap.quantity < countDaNangCap) {
                        Service.getInstance().sendThongBao(player, "Không đủ đá ngũ sắc");
                        return false;
                    }
                    if (level < MAX_LEVEL_ITEM) {
                        player.inventory.gold -= gold;
                        float ratio = player.combineNew.ratioCombine;
                        if (Util.isTrue(ratio, 170)) {
                            isSusces = true;
                            if (optionLevel == null) {
                                trangBi.itemOptions.add(new ItemOption(72, 1));
                            } else {
                                optionLevel.param++;
                            }
                            if (optionNangCap == null) {
                                int randomOptionId = Util.nextInt(244, 254);
                                trangBi.itemOptions.add(new ItemOption(randomOptionId, setParamNguSac(randomOptionId)));
                                // chỉ số mới

                            } else {
                                if (optionLevel != null) {
                                    optionNangCap.param = optionLevel.param
                                            * setParamNguSac(optionNangCap.optionTemplate.id);
                                }
                            }

                            if (optionLevel != null && optionLevel.param >= 6) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
                                        + "thành công " + trangBi.template.name + " lên +" + optionLevel.param);
                            }
                            sendEffectSuccessCombine(player);
                            Service.getInstance().sendThongBao(player, "Nâng cấp thành công");
                        } else {
                            sendEffectFailCombine(player);
                            Service.getInstance().sendThongBao(player, "Nâng cấp thất bại");
                        }

                        InventoryService.gI().subQuantityItemsBag(player, daNangCap,
                                player.combineNew.countDaNangCap);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                        if (isSusces) {
                            return false;

                        } else {
                            return true;
                        }

                    } else {
                        Service.getInstance().sendThongBao(player, "Trang bị đã đạt cấp tối đa");
                        return false;
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Cần phụ kiện và đá ngũ sắc");
                    return false;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Cần phụ kiện và đá ngũ sắc");
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    private void thanhTayPhuKien(Player player) {
        try {
            if (player.combineNew.itemsCombine.size() == 2) {
                if (isCoupleThanhTay(player.combineNew.itemsCombine.get(0),
                        player.combineNew.itemsCombine.get(1))) {
                    Item trangBi = null;
                    Item daNangCap = null;
                    if (player.combineNew.itemsCombine.get(0).template.type == 5
                            || player.combineNew.itemsCombine.get(0).template.type == 32) {
                        trangBi = player.combineNew.itemsCombine.get(0);
                        daNangCap = player.combineNew.itemsCombine.get(1);
                    } else {
                        trangBi = player.combineNew.itemsCombine.get(1);
                        daNangCap = player.combineNew.itemsCombine.get(0);
                    }

                    int countDaNangCap = player.combineNew.countDaNangCap;
                    long gold = player.combineNew.goldCombine;
                    int level = 0;

                    if (player.inventory.gold < gold) {
                        Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                        return;
                    }
                    if (daNangCap.quantity < countDaNangCap) {
                        Service.getInstance().sendThongBao(player, "Không đủ đá suy vong");
                        return;
                    }
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 72) {
                            level = io.param;
                            break;
                        }
                    }
                    if (level > 0) {
                        Service.getInstance().sendThongBao(player, "Thanh tẩy phụ kiện thành công");
                        player.inventory.gold -= gold;
                        List<ItemOption> listRemove = new ArrayList<>();
                        for (ItemOption op : trangBi.itemOptions) {
                            if (op.optionTemplate.id == 72 || isChiSoNguSac(op.optionTemplate.id)) {
                                listRemove.add(op);
                                // trangBi.itemOptions.remove(op);
                                // break;
                            }
                        }
                        for (ItemOption op : listRemove) {
                            // if (isChiSoNguSac(op.optionTemplate.id)) {
                            trangBi.itemOptions.remove(op);
                            // break;
                            // }
                        }

                        InventoryService.gI().subQuantityItemsBag(player, daNangCap,
                                player.combineNew.countDaNangCap);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        sendEffectSuccessCombine(player);
                        reOpenItemCombine(player);

                    } else {
                        Service.getInstance().sendThongBao(player, "Trang bị không có chỉ số ngũ sắc để xóa");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Cần phụ kiện và đá suy vong");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Cần phụ kiện và đá suy vong");
                return;
            }

        } catch (Exception e) {
            Logger.warning("Loi tay phu kien");
        }
    }

    // --------------------------------------------------------------------------
    /**
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    private void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void sendEffectCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(8);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    // --------------------------------------------------------------------------Ratio,
    // cost combine
    private int getRatioDaMayMan(int id) {
        switch (id) {
            case 1079:
                return 10;
            case 1080:
                return 20;
            case 1081:
                return 30;
            case 1082:
                return 40;
            case 1083:
                return 50;
        }
        return 0;
    }

    private int getRatioDaNangCap(int id) {
        switch (id) {
            case 1074:
                return 10;
            case 1075:
                return 20;
            case 1076:
                return 30;
            case 1077:
                return 40;
            case 1078:
                return 50;
        }
        return 0;
    }

    private long getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 150000000;
            case 1:
                return 300000000;
            case 2:
                return 600000000;
            case 3:
                return 900000000;
            case 4:
                return 1500000000;
            case 5:
                return 5000000000L;
            case 6:
                return 5000000000L;
            case 7:
                return 20000000000L;
        }
        return 0;
    }

    private long getGoldPhaLeHoaLinhThu(int star) {
        switch (star) {
            case 0:
                return 150000000;
            case 1:
                return 300000000;
            case 2:
                return 600000000;
            case 3:
                return 900000000;
            case 4:
                return 1500000000;
            case 5:
                return 5000000000L;
            case 6:
                return 5000000000L;
            case 7:
                return 20000000000L;
        }
        return 0;
    }

    private float getRatioPhaLeHoaBip(int star) {//chuẩn
        switch (star) {
            case 0:// 1s
                return 50f;
            case 1:
                return 16f;
            case 2:
                return 8f;
            case 3:
                return 3f;
            case 4:
                return 2f;
            case 5:
                return 1f;
            case 6:
                return 0.08f;
            case 7:
                return 0.05f;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) {//bịp
        switch (star) {
            case 0:
                return 70f;
            case 1:
                return 50f;
            case 2:
                return 40f;
            case 3:
                return 30f;
            case 4:
                return 10f;
            case 5:
                return 6f;
            case 6:
                return 1f;
            case 7:
                return 0.1f;
        }
        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 80;
            case 8:
                return 90;
        }
        return 0;
    }

    private float getRatioZeno(int star) {
        switch (star) {
            case 0:
                return 80f;
            case 1:
                return 60f;
            case 2:
                return 40f;
            case 3:
                return 25;
            case 4:
                return 10f;
            case 5:
                return 6f;
            case 6:
                return 5f;
            case 7:
                return 1.5f;
        }
        return 0;
    }

    private int getCountZeno(int level) {
        switch (level) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
        }
        return 9;
    }

    private int getCountDaMayMan(int level) {
        switch (level) {
            case 0:
                return 1;
            case 1:
                return 1;
            case 2:
                return 1;
            case 3:
                return 1;
            case 4:
                return 2;
            case 5:
                return 3;
            case 6:
                return 5;
            case 7:
                return 10;
        }
        return 10;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
            case 5:
                return 50;
            case 6:
                return 100;
        }
        return 0;
    }

    private boolean CheckNangCapBongToi(Item trangBi, Item daBongtoi) {
        if (trangBi != null && daBongtoi != null) {
            if (trangBi.template.type == 5 && daBongtoi.template.id == 1308) {
                return true;
            } else if (trangBi.template.type == 11 && daBongtoi.template.id == 1308) {
                return true;
            } else if (trangBi.template.type == 98 && daBongtoi.template.id == 1308) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean is_body_item(Item trangBi) {
        int idType = trangBi.template.type;
        if (idType <= 5 || idType == 11 || idType == 99 || idType == 23 || idType == 24) {
            return true;
        }
        return false;

    }

    private String CheckNameDaBongToi(Item tbi) {
        if (tbi != null) {
            switch (tbi.template.type) {
                case 5:
                    return "Đá pháp sư";
                case 11:
                    return "Đá pháp sư";
                case 98:
                    return "Đá pháp sư";
                default:
                    return "Không phù hợp";
            }
        } else {
            return " Không phù hợp ";
        }
    }

    private float getRatioBongToiTrangBi(int star) {
        switch (star) {
            case 0:
                return 100f;
            case 1:
                return 100f;
            case 2:
                return 100f;
            case 3:
                return 100f;
            case 4:
                return 100f;
            case 5:
                return 50f;
            case 6:
                return 50f;
            case 7:
                return 50f;
        }
        return 0;
    }

    private int getParamBongToiInLevel(int level) {
        switch (level) {
            case 0:
                return 1;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            case 5:
                return 7;
            case 6:
                return 8;
            case 7:
                return 10;
        }
        return 0;
    }

    private int getCountDaNangCapBongToi(int level) {
        switch (level) {
            case 0:
                return 20;
            case 1:
                return 20;
            case 2:
                return 20;
            case 3:
                return 20;
            case 4:
                return 20;
            case 5:
                return 20;
            case 6:
                return 20;
            case 7:
                return 20;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
        }
        return 0;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 100000;
            case 1:
                return 700000;
            case 2:
                return 3000000;
            case 3:
                return 15000000;
            case 4:
                return 70000000;
            case 5:
                return 230000000;
            case 6:
                return 500000000;
            case 7:
                return 2000000000;
        }
        return 2000000000;
    }

    private int getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 10;
            case 4:
                return 7;
            case 5:
                return 3;
            case 6:
                return 1;
        }
        return 0;
    }

    // --------------------------------------------------------------------------check
    public boolean isAngelClothes(int id) {
        if (id >= 1048 && id <= 1062) {
            return true;
        }
        return false;
    }

    public boolean isDestroyClothes(int id) {
        if (id >= 650 && id <= 662) {
            return true;
        }
        return false;
    }

    public boolean isActivationClothes(Item item) {
        for (int i = 0; i < item.itemOptions.size(); i++) {
            if (item.itemOptions.get(i).optionTemplate.id >= 127 && item.itemOptions.get(i).optionTemplate.id <= 144) {
                return true;
            }
        }
        return false;
    }

    private String getTypeTrangBi(int type) {
        switch (type) {
            case 0:
                return "Áo";
            case 1:
                return "Quần";
            case 2:
                return "Găng";
            case 3:
                return "Giày";
            case 4:
                return "Nhẫn";
        }
        return "";
    }

    private String getGenderTrangBi(int type) {
        switch (type) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namek";
            case 2:
                return "Xayda";
        }
        return "";
    }

    public boolean isManhTrangBi(Item it) {
        switch (it.template.id) {
            case 1066:
            case 1067:
            case 1068:
            case 1069:
            case 1070:
                return true;
        }
        return false;
    }

    public boolean isCraftingRecipe(int id) {
        switch (id) {
            case 1071:
            case 1072:
            case 1073:
            case 1084:
            case 1085:
            case 1086:
                return true;
        }
        return false;
    }

    public int getRatioCraftingRecipe(int id) {
        switch (id) {
            case 1071:
                return 0;
            case 1072:
                return 0;
            case 1073:
                return 0;
            case 1084:
                return 10;
            case 1085:
                return 10;
            case 1086:
                return 10;
        }
        return 0;
    }

    public boolean isUpgradeStone(int id) {
        switch (id) {
            case 1074:
            case 1075:
            case 1076:
            case 1077:
            case 1078:
                return true;
        }
        return false;
    }

    public int getRatioUpgradeStone(int id) {
        switch (id) {
            case 1074:
                return 10;
            case 1075:
                return 20;
            case 1076:
                return 30;
            case 1077:
                return 40;
            case 1078:
                return 50;
        }
        return 0;
    }

    public boolean isLuckyStone(int id) {
        switch (id) {
            case 1079:
            case 1080:
            case 1081:
            case 1082:
            case 1083:
                return true;
        }
        return false;
    }

    public int getRatioLuckyStone(int id) {
        switch (id) {
            case 1079:
                return 10;
            case 1080:
                return 20;
            case 1081:
                return 30;
            case 1082:
                return 40;
            case 1083:
                return 50;
        }
        return 0;
    }

    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi == null || daNangCap == null) {
            return false;
        }
        if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
            return true;
        } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
            return true;
        } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
            return true;
        } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
            return true;
        } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCouplePhuKien(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type == 5 || item1.template.type == 32) {
                trangBi = item1;
            } else {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type == 5 || item2.template.type == 32) {
                trangBi = item2;
            } else {
                daNangCap = item2;
            }
        }
        if (trangBi == null || daNangCap == null) {
            return false;
        }
        if ((trangBi.template.type == 5 || trangBi.template.type == 32)
                && daNangCap.template.id == 674) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCoupleThanhTay(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type == 5 || item1.template.type == 32) {
                trangBi = item1;
            } else {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type == 5 || item2.template.type == 32) {
                trangBi = item2;
            } else {
                daNangCap = item2;
            }
        }
        if (trangBi == null || daNangCap == null) {
            return false;
        }
        if ((trangBi.template.type == 5 || trangBi.template.type == 32)
                && daNangCap.template.id == 1418) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDoThanLinh(int tempId) {
        return tempId >= 555 && tempId <= 567;
    }

    private boolean isChiSoNguSac(List<ItemOption> trangBi) {
        if (trangBi != null) {
            for (ItemOption io : trangBi) {
                if (isChiSoNguSac(io.optionTemplate.id)) {
                    return true;
                }
            }
        }

        return false;

    }

    private boolean isChiSoNguSac(int trangBi) {
        if (trangBi >= 244 && trangBi <= 254) {
            return true;
        }
        return false;
    }

    // private boolean isCoupleItemNangCap(Item item1, Item item2) {
    // Item trangBi = null;
    // Item daNangCap = null;
    // if (item1 != null && item1.isNotNullItem()) {
    // if (item1.template.type < 5) {
    // trangBi = item1;
    // } else if (item1.template.type == 14) {
    // daNangCap = item1;
    // }
    // }
    // if (item2 != null && item2.isNotNullItem()) {
    // if (item2.template.type < 5) {
    // trangBi = item2;
    // } else if (item2.template.type == 14) {
    // daNangCap = item2;
    // }
    // }
    // if (trangBi != null && daNangCap != null) {
    // if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
    // return true;
    // } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
    // return true;
    // } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
    // return true;
    // } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
    // return true;
    // } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
    // return true;
    // } else {
    // return false;
    // }
    // } else {
    // return false;
    // }
    // }
    private boolean isTrangBiNguyenLieuZeno(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type == 5) {
                if (item.itemOptions != null) {
                    for (ItemOption io : item.itemOptions) {
                        if (io.optionTemplate.id == 196) {
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiZeno(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type == 5) {
                // && item.isTrangBiGhep()
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {
        return item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20);
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            switch (item.template.type) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 32:
                    return true;
            }
            return false;

        } else {
            return false;
        }
    }

    private boolean isCouplePhaLeHoa(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem() && item2 != null && item2.isNotNullItem()) {
            if (isTrangBiPhaLeHoa(item1)) {
                trangBi = item1;
                daNangCap = item2;
            } else {
                trangBi = item2;
                daNangCap = item1;
            }

        }

        if (trangBi == null || daNangCap == null) {
            return false;
        }

        if (isTrangBiPhaLeHoa(trangBi) && daNangCap.template.id == 1079) {
            return true;
        } else {
            return false;
        }
    }

    private int getTileBongToi(int level) {
        switch (level) {
            case 0:
                return 20;
            case 1:
                return 10;
            case 2:
                return 7;
            case 3:
                return 3;
            case 4:
                return 3;
            case 5:
                return 3;
            case 6:
                return 1;
            case 7:
                return 1;
        }
        return 1;
    }

    private int getGoldBongToi(int level) {

        return 500000000;
    }

    private boolean isChiSoBongToi(int trangBi) {
        if (trangBi >= 219 && trangBi <= 221) {
            return true;
        }
        return false;
    }

    private boolean isCoupleZeno(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type == 5) {
                trangBi = item1;
            } else {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type == 5) {
                trangBi = item2;
            } else {
                daNangCap = item2;
            }
        }
        if (trangBi == null || daNangCap == null) {
            return false;
        }
        // if (trangBi.isTrangBiGhep() && daNangCap.template.id == 1441) {
        // return true;
        // } else {
        return false;
        // }
    }

    private boolean isCoupleBongToi(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (isTypeBongToi(item1)) {
                trangBi = item1;
            } else {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (isTypeBongToi(item2)) {
                trangBi = item2;
            } else {
                daNangCap = item2;
            }
        }
       if (trangBi == null || daNangCap == null) {
    return false;
}

        if (isTypeBongToi(trangBi) && daNangCap.template.id == 1085) {

            return true;
        } else {
            return false;
        }
    }
//5 ct, 11 dl, 23 24 ván bay, 98 pet, 99 linh thú
    private boolean isTypeBongToi(Item trangBi) {
        if (trangBi.template.type == 5 || trangBi.template.type == 11
                 || trangBi.template.type == 23 || trangBi.template.type == 24
                || trangBi.template.type == 98 || trangBi.template.type == 99) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCoupleTayBongToi(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (isTypeBongToi(item1)) {
                trangBi = item1;
            } else {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (isTypeBongToi(item2)) {
                trangBi = item2;
            } else {
                daNangCap = item2;
            }
        }
        if (trangBi == null || daNangCap == null) {
            return false;
        }
        if ((isTypeBongToi(trangBi))
                && daNangCap.template.id == 1086) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isChiSoEpSpl(int idOption) {

        switch (idOption) {
            case 77: // 7s
            case 103: // 6s
            case 80: // 5s
            case 81: // 4a
            case 50: // 3s
            case 94: // 2s
            case 108: // 1s
            case 95: // spl
            case 96: // spl
            case 97: // spl
            case 98: // spl
            case 99: // spl
            case 10: // spl
            case 101: // spl
            case 102: // spl
            case 107: // spl
                return true;
            default:
                return false;
        }
    }

    private boolean isChiSoEpSplDisplay(int idOption) {

        switch (idOption) {
            case 77: // 7s
            case 103: // 6s
            case 80: // 5s
            case 81: // 4a
            case 50: // 3s
            case 94: // 2s
            case 108: // 1s
            case 95: // spl
            case 96: // spl
            case 97: // spl
            case 98: // spl
            case 99: // spl
            case 10: // spl
            case 101: // spl
            case 107: // spl
            case 102: // spl
                return true;
            default:
                return false;
        }
    }

    private boolean isCoupleTienHoaKichHoat(Item item1, Item item2) {
        Item trangBi_skh = null;
        Item trangBi_hd = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.isSKH()) {
                trangBi_skh = item1;
            } else if (item1.isDHD()) {
                trangBi_hd = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.isSKH()) {
                trangBi_skh = item2;
            } else if (item2.isDHD()) {
                trangBi_hd = item2;
            }
        }
        if (trangBi_skh != null && trangBi_skh.isNotNullItem() && trangBi_hd != null && trangBi_hd.isNotNullItem()) {
            return true;
        }
        return false;
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        switch (daPhaLe.template.id) {
            case 1484:
                return 5;
            case 1485:
            case 1486:
            case 1488:
                return 3;
            case 1487:
                return 2;

            case 931:
            case 20:
                return 5; // +5%hp
            case 930:
            case 19:
                return 5; // +5%ki
            case 929:
            case 18:
                return 5; // +5%hp/30s
            case 928:
            case 17:
                return 5; // +5%ki/30s
            case 927:
            case 16:
                return 3; // +3%sđ
            case 926:
            case 15:
                return 2; // +2%giáp
            case 925:
            case 14:
                return 2; // +2%né đòn
            case 441:
                return 5;
            case 442:
                return 5;
            case 443:
                return 10;
            case 444:
                return 10;
            case 445:
                return 10;
            case 446:
                return 10;
            case 447:
                return 5;
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        switch (daPhaLe.template.id) {
            case 1487:
                return 5;
            case 20:
            case 931:
            case 1485:
                return 77;
            case 19:
            case 930:
            case 1486:
                return 103;
            case 18:
            case 929:
                return 80;
            case 17:
            case 928:
                return 81;
            case 16:
            case 927:
            case 1488:
                return 50;
            case 15:
            case 926:
            case 1484:
                return 94;
            case 14:
            case 925:
                return 108;
            case 441:
                return 95;
            case 442:
                return 96;
            case 443:
                return 97;
            case 444:
                return 98;
            case 445:
                return 99;
            case 446:
                return 100;
            case 447:
                return 101;
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    // Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    private int getratiodamayma(int da) {
        switch (da) {
            case 1079:
                return 10;
            case 1080:
                return 20;
            case 1081:
                return 30;
            case 1082:
                return 40;
            case 1083:
                return 50;
        }
        return 0;
    }

    // --------------------------------------------------------------------------Text
    // tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case PHA_LE_HOA_LINH_THU:
                return "Ta sẽ đục lỗ\n Linh vật của ngươi";
            case AN_TRANG_BI:
                return "Ấn trang bị sẽ giúp\ntrang bị của ngươi gia tăng\nđáng kể chỉ số";
             case TAY_AN_TRANG_BI:
                return "Tẩy ấn trang bị";
            case THANG_HOA_NGOC_BOI:
                return "Ta sẽ Thăng hoa \n trang bị Ngọc Bội\n của ngươi để sử dụng";
            case THANG_CAP_NGOC_BOI:
                return "Ta sẽ Thăng cấp \n cho trang bị Ngọc Bội\n của ngươi mạnh hơn";
            case NANG_CAP_THIEN_TU:
                return "Mang Chân mệnh tới đây ta \nBan sức mạnh cho";
            case EP_PHA_LE_LINH_THU:
                return "Ta sẽ ép sao\n Linh vật của ngươi !";
            case DOI_DO_THIEN_SU:
                return "Ta sẽ giao tiếp với\ncác thiên sứ giúp ngươi !";
            case GHEP_RUONG_GOD:
                return "Ta sẽ triệu hoán\n rương cổ cho ngươi";
            case DELETE_OPTION_THIEN_SU:
                return "Ta sẽ phù hộ \n cho trang bị Thiên sứ của ngươi";
            case BONG_TOI_TRANG_BI:
                return "Ta sẽ bóng tối hoá\n trang bị của ngươi";
            case DLETE_BONG_TOI_TRANG_BI:
                return "Ta sẽ thanh tẩy \n bóng tối trang bị của ngươi";
            case DOI_DO_THAN_LINH_THANH_HUY_DIET:
                return "Ta sẽ phù phép\ntrang bị Thần Linh của ngươi\nthành trang bị Huỷ Diệt";
            case EP_SAO_TRANG_BI:
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai của ngươi\ntrở lên mạnh mẽ";
            case NANG_CAP_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PHA_LE_HOA_TRANG_BI:
            case PHA_LE_HOA_TRANG_BI_X10:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case CHUYEN_SPL:
                return "CHUYỂN HÓA\nSAO PHA LÊ";   
            case TAY_SPL:
                return "TẨY\nSAO PHA LÊ\nTRANG BỊ";        
            case THANG_HOA_NGOC_BOI_DE_TU:
                return "Cái đầu cặc";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case DOI_RUONG_GO:
                return "Ta sẽ phù phép\ncho rương gỗ cấp 0\nthành rương gỗ cấp cao hơn";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case CHE_TAO_TRANG_BI:
                return "Ta sẽ chế tạo cho ngươi trang bị hoàn toàn mới";
            case DOI_VE_HUY_DIET:
                return "Ta sẽ đưa ngươi 1 vé đổi đồ\nhủy diệt, đổi lại ngươi phải đưa ta\n 1 món đồ thần linh tương ứng";
            case DAP_SET_KICH_HOAT:
                return "Ta sẽ giúp ngươi chuyển hóa\n1 món đồ hủy diệt\nthành 1 món đồ kích hoạt";
            case NANG_CAP_SKH_THUONG:
                return "Ta sẽ phù phép \ncho trang bị của \nngươi thành kích hoạt";
            case NANG_CAP_SKH_THUONG_GOLD_BAR:
                return "Ta sẽ phù phép \ncho trang bị của \nngươi thành kích hoạt";
            case NANG_CAP_SKH_VIP:
                return "Ta sẽ phù phép \ncho trang bị của \nngươi thành kích hoạt đến cấp độ thần linh";
            case DOI_MANH_KICH_HOAT:
                return "Ta sẽ giúp ngươi biến hóa\nviên ngọc 1 sao và 1 món đồ\nthần linh thành mảnh kích hoạt";
            case DAP_SET_KICH_HOAT_CAO_CAP:
                return "Ta sẽ giúp ngươi chuyển hóa\ntrang bị kích hoạt của ngươi\n trở nên mạnh mẽ hơn ";
            case GIA_HAN_CAI_TRANG:
                return "Ta sẽ phù phép\n cho trang bị của mi\n thêm hạn sử dụng";
            case DAP_DO_THIEN_SU:
                return "Nâng cấp\n trang bị thiên sứ";
            case CHE_TAO_GIO_KEO_KINH_DI:
                return "Ta sẽ phù phép\ncho ngươi chế tạo giỏ kẹo";
            case CHE_TAO_BO_KEO_KINH_DI:
                return "Ta sẽ phù phép\ncho ngươi chế tạo bó kẹo";
            case CHE_BIEN_TRA_HOA_CUC:
                return "Đem đến nguyên liệu thơm ngon\n tặng thầy cô";
            case NANG_CAP_PHU_KIEN:
                return "Ta sẽ phù phép\ncho phụ kiện của ngươi\ntrở lên mạnh mẽ";
            case TRAO_DOI_XU_HADES:
                return "Trao đổi đổ thần\nlấy đồng xu";
            case THANH_TAY_PHU_KIEN:
                return "Ta sẽ thanh tẩy\ncho phụ kiện của ngươi\nmất chỉ số ngũ sắc";
            case NANG_CAP_ZENO:
                return "Ta sẽ phù phép\ncho cải trang của ngươi\ntrở thành trang bị pha lê";
            case EP_SAO_ZENO:
                return "Ta sẽ phù phép\ncho cải trang của ngươi\ntrở nên mạnh mẽ hơn";
            case NANG_CAP_BONG_TOI:
                return "Ta sẽ phù phép\ncho phụ kiện của ngươi\ntrở lên mạnh mẽ";
            case THANH_TAY_BONG_TOI:
                return "Ta sẽ thanh tẩy\ncho phụ kiện của ngươi\nmất chỉ số bóng tối";
            case GHEP_CAI_TRANG_2:
                return "Ta sẽ hợp nhất\ncho cải trang và bông tai cấp 2\ncủa ngươi mạnh hơn";
            case PHAN_TACH_HUY_DIET_LAY_MANH:
                return "Ta sẽ chuyển hóa\ncho đồ thần linh của ngươi\nthành đá ngũ sắc";
            case NANG_CAP_BONG_TAI_3:
                return "Ta sẽ phù phép\ncho bông tai của ngươi\ntrở lên mạnh mẽ";
            case NANG_CAP_CHI_SO_BONG_TAI_3:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case NANG_CAP_BONG_TAI_4:
                return "Ta sẽ phù phép\ncho bông tai của ngươi\ntrở lên mạnh mẽ";
            case NANG_CAP_CHI_SO_BONG_TAI_4:
                return "Ta sẽ phù phép\ncho trang bị porata c4 của ngươi\ntrở lên mạnh mẽ";    
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case NANG_CAP_BONG_TAI_3:
                return "Nâng cấp Bông Tai Porata Cấp con cần :\n Bông tai Porata cấp 2\n+ X999 Mảnh vỡ bông tai cấp 3\nSau đó chọn 'Nâng Cấp' ";
            case NANG_CAP_CHI_SO_BONG_TAI_3:
                return "Nếu Con muốn thay đổi chỉ số hiện tại của Bông Tai Porata Cấp 3 con cần:\n+ Bông Tai PoraTa Cấp 3\n+x99 Mảnh hồn bông tai cấp 3\n+x5 Đá Porata3 \nSau đó chọn 'Nâng Cấp' ";
             case NANG_CAP_BONG_TAI_4:
                return "Nâng cấp Bông Tai Porata Cấp con cần :\n Bông tai Porata cấp 3\n+ X9999 Mảnh vỡ bông tai cấp 4\nSau đó chọn 'Nâng Cấp' ";
            case NANG_CAP_CHI_SO_BONG_TAI_4:
                return "Nếu Con muốn thay đổi chỉ số hiện tại của Bông Tai Porata Cấp 4 con cần:\n+ Bông Tai PoraTa Cấp 4\n+x99 Mảnh hồn bông tai cấp 4\n+x5 Đá Porata4 \nSau đó chọn 'Nâng Cấp' ";     
            case PHA_LE_HOA_LINH_THU:
                return "Vào hành trang\n Chọn:\n -X1 Linh thú\nSau đó chọn 'Nâng cấp'";
            case EP_PHA_LE_LINH_THU:
                return "Vào hành trang\n Chọn: \n-X1 Linh thú Nâng cấp\n -1 X1 Đá tinh thạch (Tùy chọn)\n Sau đó chọn 'Nâng cấp'";
            case DOI_DO_THIEN_SU:
                return "Vào hành trang\n Chọn: \n-x999 Mảnh thiên sứ\n-x1 Đá nâng cấp\n-x1 Công thức VIP\n-X1 Đá may mắn (Tùy chọn)\nSau đó chọn 'Nâng cấp'";
            case GHEP_RUONG_GOD:
                return "Vào hành trang\n Chọn:\n- x1 Rương rỗng\n- x20 Ngọc trai\n- x20 Đá quý\n- x20 kim cương \n Sau đó ấn 'Nâng cấp'";
            case DELETE_OPTION_THIEN_SU:
                return "Vào hành trang\n Chọn 1 trang bị Thiên sứ đã được phù hộ\n Sau đó chọn 'Nâng cấp'";
            case BONG_TOI_TRANG_BI:
                return "Vào hành trang\n Chọn 1 vật phẩm có thể pháp sư \n(cải trang , đeo lưng, pet) và chọn 20 đá pháp sư \n Sau đó chọn 'Nâng cấp'";
            case DLETE_BONG_TOI_TRANG_BI:
                return "Vào hành trang\n Chọn 1 vật phẩm đã pháp sư\n và 30 Đá ánh sáng \n Sau đó chọn 'Xóa ngay' ";
            case CHE_BIEN_TRA_HOA_CUC:
                return "Vào hành trang\n Chọn x99 Lá trà tươi , x99 Nia tre \n x99 Que tre và Hoa cúc\nSau đó chọn 'Chế biến ngay' ";
            case CHE_TAO_GIO_KEO_KINH_DI:
                return "Chọn x99 kẹo bí ngô\n x99 kẹo não người\n x1 giỏ đựng\n x1 dây buộc \nSau đó chọn 'Nâng cấp'";
            case CHE_TAO_BO_KEO_KINH_DI:
                return "Chọn x99 kẹo bí ngô \n x99 kẹo não người\n và x1 dây buộc \n Sau đó chọn 'Nâng cấp'";
            case AN_TRANG_BI:
                return "Vào hành trang\nChọn 1 trang bị chưa ấn,\n1 viên ngọc rồng 2 sao\nvà 20 đá ma thuật\nSau đó nhấn 'Ấn trang bị'";
             case TAY_AN_TRANG_BI:
                return "Vào hành trang\nChọn 1 trang bị đã khảm ấn,\n 10 đá ma thuật\n Sau đó nhấn 'Tẩy Ấn trang bị'";
            case DOI_DO_THAN_LINH_THANH_HUY_DIET:
                return "Vào hành trang\nChọn nhiều trang bị thần linh\nsau đó chọn 'Nâng cấp'";
            case NANG_CAP_THIEN_TU:
                return "Vào hành trang\nchọn Chân thiên tử \nchọn Tinh thể và Ma quái \nSau đó chọn 'Nâng cấp'";
            case THANG_HOA_NGOC_BOI:
                return "Vào hành trang\nChọn trang bị Ngọc Bội\nSau đó chọn 'Nâng cấp'";
            case THANG_CAP_NGOC_BOI:
                return "Vào hành trang\nChọn trang bị Ngọc Bội\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI:
                return "Nâng cấp Bông Tai Porata Cấp con cần :\n Bông tai Porata cấp 1\n+ 99 Mảnh vỡ bông tai\nSau đó chọn 'Nâng Cấp' ";
            case NANG_CAP_CHI_SO_BONG_TAI:
                return "Nếu Con muốn thay đổi chỉ số hiện tại của Bông Tai Porata Cấp 2 con cần:\n+ Bông Tai PoraTa Cấp 2\n+x99 Mảnh hồn bông tai\n+x5 Đá xanh lam \nSau đó chọn 'Nâng Cấp' ";
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI_X10:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'\n Khi nâng cấp thành công hoặc đủ 5 lần thì sẽ dừng lại";
            case CHUYEN_SPL:
                return "Vào hành trang chọn 3 món gồm:\n- 1 trang bị đã ép sao pha lê (sẽ mất),\n- 1 trang bị chưa có sao cùng loại\n- 20.000 Thỏi vàng khóa.\n\nLưu ý: Chỉ chuyển sao.";    
            case TAY_SPL:
                return "Đặt 2 món gồm:\n- Trang bị có sao pha lê đã ép\n- 20 Đá tẩy\nTốn 10.000 TVàng khóa\nSau đó chọn 'Nâng cấp'!";
            case THANG_HOA_NGOC_BOI_DE_TU:
                return "Cái đầu cặc";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case DOI_RUONG_GO:
                return "Vào hành trang\nChọn 1 rương gỗ chưa có cấp\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case CHE_TAO_TRANG_BI:
                return "vào hành trang\nChọn 3 trang bị\n(Có chứa chỉ sô{có thể chế tạo})\nChọn bùa pháp sư\n"
                        + "Sau đó chọn 'Chế tạo'";
            case DOI_VE_HUY_DIET:
                return "Vào hành trang\nChọn món đồ thần linh tương ứng\n(Áo, quần, găng, giày hoặc nhẫn)\nSau đó chọn 'Đổi'";
            case DAP_SET_KICH_HOAT:
                return "Vào hành trang\nChọn món đồ hủy diệt tương ứng\n(Áo, quần, găng, giày hoặc nhẫn)\n(Có thể thêm 1 món đồ thần linh bất kỳ để tăng tỉ lệ)\nSau đó chọn 'Đập'";
            case NANG_CAP_SKH_THUONG:
                return "Vào hành trang chọn\n1 món trang bị hủy diệt \n(Áo, quần, găng, giày, nhẫn)\nSau đó chọn nâng cấp";
            case NANG_CAP_SKH_THUONG_GOLD_BAR:
                return "Vào hành trang chọn\n1 món trang bị hủy diệt \n(Áo, quần, găng, giày, nhẫn)\nSau đó chọn nâng cấp";
            case NANG_CAP_SKH_VIP:
                return "Vào hành trang chọn\n5 món trang bị Thần linh\n(Áo, quần, găng, giày, nhẫn) \nSau đó chọn nâng cấp\nMón SKH Thần linh (random)";
            case DOI_MANH_KICH_HOAT:
                return "Vào hành trang\nChọn món đồ thần linh tương ứng\n(Áo, quần, găng,giày hoặc nhẫn)\nSau đó chọn 'Đổi'";
            case DAP_SET_KICH_HOAT_CAO_CAP:
                return "Vào hành trang\nChọn 1 trang bị kích hoạt thường\nvà 1 trang bị huỷ diệt ta\nsẽ cho ngươi 50% tỉ lệ thành công\n(Nếu có thêm 1 trang bị huỷ diệt khác ta sẽ cho ngươi 100% tỉ lệ thành công)\nSau đó chọn 'Nâng cấp;";
            case GIA_HAN_CAI_TRANG:
                return "Vào hành trang \n Chọn cải trang có hạn sử dụng \n Chọn thẻ gia hạn \n Sau đó chọn gia hạn";
            case DAP_DO_THIEN_SU:
                return "Cần 1 Trang bị thiên sứ\n và\nĐá may mắn (tùy chọn)";
            case NANG_CAP_PHU_KIEN:
                return "Vào hành trang\nChọn trang bị\n(Cải trang, giáp luyện tập)\nChọn đá ngũ sắc để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case TRAO_DOI_XU_HADES:
                return "Vào hành trang\nChọn những vật phẩm thần linh muốn trao đổi\nSau đó nhấn 'Đổi'";
            case THANH_TAY_PHU_KIEN:
                return "Vào hành trang\nChọn trang bị\n(Cải trang, giáp luyện tập)\nChọn đá suy vong để thanh tấy\n"
                        + "Sau đó chọn 'Thanh tẩy'";
            case NANG_CAP_ZENO:
                return "Chọn trang bị\n(Cải trang Himmel + Đá mặt trăng)\nSau đó chọn 'Nâng cấp'";
            case EP_SAO_ZENO:
                return "Chọn trang bị\nCải trang Himmel \n và cải trang có chỉ số (có thể ghép)\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TOI:
                return "vào hành trang\nChọn trang bị\n(Cải trang, phụ kiện, pet, vpdl, giáp luyện tập)\nChọn Ngọc pháp sư để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case THANH_TAY_BONG_TOI:
                return "vào hành trang\nChọn trang bị\nChọn Bùa tẩy pháp sư để thanh tẩy\n"
                        + "Sau đó chọn 'Thanh tẩy'";
            case GHEP_CAI_TRANG_2:
                return "vào hành trang\nChọn 2 cải trang\nvà 1 bông tai cấp 2\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHAN_TACH_HUY_DIET_LAY_MANH:
                return "Vào hành trang chọn\n1 món trang bị thần linh \n(Áo, quần, găng, giày, nhẫn)\nSau đó chọn 'Chuyển hóa' ";
            default:
                return "";
        }
    }

    // MENU
    private void menu_Pha_Le_Hoa_Zeno(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            if (isCoupleZeno(player.combineNew.itemsCombine.get(0),
                    player.combineNew.itemsCombine.get(1))) {
                Item trangBi = null;
                Item daNangCap = null;
                if (player.combineNew.itemsCombine.get(0).template.type == 5) {
                    trangBi = player.combineNew.itemsCombine.get(0);
                    daNangCap = player.combineNew.itemsCombine.get(1);
                } else {
                    trangBi = player.combineNew.itemsCombine.get(1);
                    daNangCap = player.combineNew.itemsCombine.get(0);
                }
                // if (!trangBi.isTrangBiGhepMain()) {
                // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                // "Hãy chọn 1 cải trang Himmel (mua ở Mr Santa) và đá mặt trăng", "Đóng");
                // return;
                // }
                int star = 0;

                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.combineNew.goldCombine = 500_000_000;
                    player.combineNew.gemCombine = getGemPhaLeHoa(star);
                    player.combineNew.countDaNangCap = getCountZeno(star);
                    float ratiohienthi = getRatioZeno(star);
                    String npcSay = trangBi.template.name + "\n|2|";
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id != 102) {
                            npcSay += io.getOptionString() + "\n";
                        }
                    }
                    npcSay += "|7|Tỉ lệ thành công: " + ratiohienthi + "%" + "\n"
                            + (player.combineNew.countDaNangCap > daNangCap.quantity ? "|7|" : "|1|")
                            + "Cần " + player.combineNew.countDaNangCap + " " + daNangCap.template.name
                            + "\n";
                    if (player.combineNew.goldCombine <= player.inventory.gold) {
                        npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                    } else {
                        npcSay += "Còn thiếu "
                                + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                + " vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Vật phẩm đã đạt cấp độ tối đa", "Đóng");
                }

            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 cải trang Himmel (mua ở Mr Santa) và đá mặt trăng", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 cải trang Himmel (mua ở Mr Santa) và đá mặt trăng", "Đóng");
        }
    }

    private void menu_tien_hoa_skh(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {

            if (isCoupleTienHoaKichHoat(player.combineNew.itemsCombine.get(0),
                    player.combineNew.itemsCombine.get(1))) {

                Item itemKHMain = null;
                Item itemHD = null;
                if (player.combineNew.itemsCombine.get(0).isSKH()) {
                    itemKHMain = player.combineNew.itemsCombine.get(0);
                    itemHD = player.combineNew.itemsCombine.get(1);
                } else {
                    itemKHMain = player.combineNew.itemsCombine.get(1);
                    itemHD = player.combineNew.itemsCombine.get(0);
                }
                if (itemKHMain == null || itemHD == null) {
                    Service.getInstance().sendThongBao(player, "Cần 1 trang bị kích hoạt và hủy diệt");
                    return;
                }
                // Lấy cấp độ trang bị
                int level1 = 0;
                int length = ConstItem.doSKHVip[itemKHMain.template.type][itemKHMain.template.gender].length;
                for (int i = 0; i < length; i++) {
                    if (ConstItem.doSKHVip[itemKHMain.template.type][itemKHMain.template.gender][i] == itemKHMain.template.id) {
                        level1 = i;
                        break;
                    }
                }
                if (level1 > 13) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cấp độ trang bị kích hoạt tối đa là hủy diệt",
                            "Đóng");
                    return;
                }
                int idSkh = 0;
                String optionTxt = "";
                // lấy id kích hoạt
                for (ItemOption io : itemKHMain.itemOptions) {
                    if (isChiSoEpSplDisplay(io.optionTemplate.id)) {
                        // lấy chỉ số cũ
                        optionTxt += ItemService.gI().getItemOptionTemplate(io.optionTemplate.id).name
                                .replaceAll("#", io.param + "") + "\n";
                    } else if (io.optionTemplate.id >= 127 && io.optionTemplate.id <= 135) {
                        idSkh = io.optionTemplate.id;
                    }
                }

                String npcSay = "Con sẽ nhận được "
                        + player.combineNew.itemsCombine.stream().filter(Item::isSKH).findFirst().get()
                                .typeName()
                        + " ngẫu nhiên có chỉ số: \n"
                        + "|2|" + ItemService.gI().getItemOptionTemplate(idSkh).name + "\n"
                        + optionTxt
                        + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                if (player.inventory.gold < COST) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng",
                            "Đóng");
                    return;
                }
                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                        npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
            } else {

                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần một trang bị kích hoạt và trang bị hủy diệt", "Đóng");
            }

        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 1 trang bị kích hoạt và 1 món hủy diệt", "Đóng");
        }
    }

    private void menu_Gep_Ruong_Gold(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.combineNew.itemsCombine.size() == 4) {
                Item Ngoctrai = null;
                Item Daquy = null;
                Item RuongRong = null;
                Item KimCuong = null;

                for (Item it : player.combineNew.itemsCombine) {
                    switch (it.template.id) {
                        case 1382:
                            Ngoctrai = it;
                            break;
                        case 1383:
                            Daquy = it;
                            break;
                        case 1384:
                            RuongRong = it;
                            break;
                        case 1385:
                            KimCuong = it;
                            break;
                        default:
                            break;
                    }
                }
                if (RuongRong != null && Daquy != null && Daquy.quantity >= 20
                        && Ngoctrai != null && Ngoctrai.quantity >= 20
                        && KimCuong != null && KimCuong.quantity >= 20) {
                    player.combineNew.ratioCombine = 100f;
                    String npcSay = "|2|Triệu Hoán Rương vật phẩm God";
                    npcSay += "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine;
                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Triệu Hoán\n Ngay");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Không đủ số lượng vật phẩm cẩn thiết, hãy kiểm tra lại vật phẩm của ngươi!",
                            "Đóng");
                }

            } else {
                this.baHatMit
                        .createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy đảm bảo đưa ta đủ Kim cương , Ngọc trai "
                                + ",Đá quý và rương rỗng , ta mới có thể triệu hoán giúp ngươi!",
                                "Đóng");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    private void menu_Nang_Cap_Bong_Tai(Player player) {
        if (player.inventory.gold < COST_DOI_VE_DOI_DO_HUY_DIET) { // bông tai
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 20K TV khóa để nâng cấp",
                    "Đóng");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 2) {
            Item bongTai = null;
            Item manhBongTai = null;

            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == 933) {
                    manhBongTai = it;
                }
                if (it.getId() == 454) {
                    bongTai = it;
                }
            }
            if (bongTai != null && manhBongTai != null && manhBongTai.quantity >= 99) {
                String npcSay = "|2|Con có muốn dùng nguyên liệu để nâng cấp\n|1|"
                        + "Con sẽ nhận được bông tai cấp 2 tăng 10% chỉ số khi hợp thể\n"
                        + "và chỉ số phụ cộng thêm(tối đa 15%)\n"
                        + "|2|Tỉ lệ 100% \n";
                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                        npcSay, "Nâng cấp\n20K TV khóa", "Từ chối");
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy bỏ vào đủ X99 mảnh vỡ bông tai và X1 Bông tai Porata", "Đóng");
            }

        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Để nâng cấp bông tai Porata, con cần 1 Bông tai Porata và 99 mảnh vỡ bông tai",
                    "Đóng");
        }
    }

    private void menu_Che_Tao_Vat_Pham(Player player) {
        if (player.inventory.gold < COST_DOI_VE_DOI_DO_HUY_DIET) {
            this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 500 triệu vàng để chế tạo",
                    "Đóng");
            return;
        }
        if (player.combineNew.itemsCombine.size() >= 4
                && player.combineNew.itemsCombine.size() <= 5) {
            int count_bua = 0;
            int count_trang_bi = 0;
            int count_da_may_man = 0;
            for (Item itemC : player.combineNew.itemsCombine) {
                if (itemC.template.id == 1394) {
                    count_bua += 1;
                } else if (itemC.isCheTao()) {
                    count_trang_bi += 1;
                } else if (itemC.template.id == 1307) {
                    count_da_may_man += 1;
                }
            }

            if (count_bua != 1 || count_trang_bi != 3) {
                this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 3 trang bị có chỉ số {Có thể chế tạo} và 1 bùa pháp sư để chế tạo",
                        "Đóng");
                return;
            }
            String npcSay = "|2|Ngươi có muốn dùng 3 trang bị trên để chế tạo, sau khi chế tạo, vật phẩm sẽ biến mất\n|1|"
                    + "Ngươi sẽ nhận được một vật phẩm ngẫu nhiên hoàn toàn mới \n"
                    + "|2|Tỉ lệ thành công 100%" + (count_da_may_man == 1 ? ", tỉ lệ may mắn cộng thêm 20%" : "")
                    + "\n";

            this.toribot.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    npcSay, "Chế tạo\n500tr vàng", "Từ chối");
        } else {
            this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 3 trang bị có chỉ số {Có thể chế tạo} và 1 bùa pháp sư để chế tạo (có thể thêm đá may mắn nếu có)",
                    "Đóng");
        }
    }

    private void menu_Nang_Cap_Bong_Tai_2(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            Item bongTai = null;
            Item manhVo = null;
            Item nuocphep = null;
            Item ngocmman = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 454) {
                    bongTai = item;
                } else if (item.template.id == 933) {
                    manhVo = item;
                } else if (item.template.id == 934) {
                    nuocphep = item;
                } else if (item.template.id == 935) {
                    ngocmman = item;
                }
            }
            if (bongTai != null && manhVo != null && nuocphep != null && ngocmman != null
                    && manhVo.quantity >= 999 && nuocphep.quantity >= 99 && ngocmman.quantity >= 99) {
                player.combineNew.goldCombine = GOLD_BONG_TAI;
                player.combineNew.gemCombine = GEM_BONG_TAI;
                player.combineNew.ratioCombine = RATIO_BONG_TAI;
                String npcSay = "|2|Sau khi nâng cấp thành công sẽ nhận được :"
                        + "\n+ Bông tai Porata cấp 2 kèm ngẫu nhiên 1 chỉ số";
                npcSay += "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%"
                        + "\n";
                if (player.combineNew.goldCombine <= player.inventory.gold) {
                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + "vàng";
                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                } else {
                    npcSay += "Còn thiếu "
                            + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                            + " vàng";
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 1 Bông tai Porata cấp 1 , X999 Mảnh vỡ bông tai, X99 Mảnh hồn bông tai và X99 Đá xanh lam để nâng cấp",
                        "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Nếu con muốn nâng cấp Bông Tai Porata Cấp 2 con cần :"
                    + "\n+ Bông tai Porata cấp 1"
                    + "\n+ X999 Mảnh vỡ bông tai"
                    + "\n+ X99 Mảnh hồn bông tai"
                    + "\n+ X99 Đá xanh lam ",
                    "Đóng");
        }
    }

    private boolean checkZenoHoaOK(Item trangBi, Item daNangCap) {
        boolean isCt_1 = false; // 114 (147, 114)
        boolean isCt_2 = false; // 5 (5)
        boolean isCt_3 = false; // 106 (95, 96)
        boolean isCt_4 = false; // 116 (197)
        boolean isCt_5 = false; // 14 (14)
        boolean isCt_6 = false; // 47 (147)
        boolean isCt_7 = false; // 241 (147, 197)
        boolean isCt_8 = false; // 212 (26)

        for (ItemOption io : trangBi.itemOptions) {
            switch (io.optionTemplate.id) {
                case 114:
                    isCt_1 = true;
                    break;
                case 5:
                    isCt_2 = true;
                    break;
                case 106:
                    isCt_3 = true;
                    break;
                case 116:
                    isCt_4 = true;
                    break;
                case 14:
                    isCt_5 = true;
                    break;
                case 47:
                    isCt_6 = true;
                    break;
                case 241:
                    isCt_7 = true;
                    break;
                case 26:
                    isCt_8 = true;
                    break;
            }

        }
        for (ItemOption io : daNangCap.itemOptions) {
            switch (io.optionTemplate.id) {
                case 114:
                    if (isCt_1) {
                        return false;
                    }
                    break;
                case 5:
                    if (isCt_2) {
                        return false;
                    }
                    break;
                case 106:
                    if (isCt_3) {
                        return false;
                    }
                    break;
                case 116:
                    if (isCt_4) {
                        return false;
                    }
                    break;
                case 14:
                    if (isCt_5) {
                        return false;
                    }
                    break;
                case 47:
                    if (isCt_6) {
                        return false;
                    }
                    break;
                case 241:
                    if (isCt_7) {
                        return false;
                    }
                    break;
                case 26:
                    if (isCt_8) {
                        return false;
                    }
                    break;
            }
        }

        return true;
    }

    private boolean IsChiSoBongTai(int id) {
        switch (id) {
            case 5:
            case 14:
            case 50:
            case 77:
            case 80:
            case 81:
            case 94:
            case 101:
            case 103:
            case 108:
                return true;

        }
        return false;
    }

    private void menu_Nang_Cap_Chi_So_Bong_Tai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item bongTai = null;
            Item manhHon = null;
            Item Xlam = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == 921) {
                    bongTai = it;
                }
                if (it.getId() == 934) {
                    manhHon = it;
                }
                if (it.getId() == 935) {
                    Xlam = it;
                }
            }
            if (bongTai != null && manhHon != null && Xlam != null
                    && manhHon.quantity >= 99 && Xlam.quantity >= 5) {
                String npcSay = "|2|Con có muốn dùng nguyên liệu để nâng cấp\n|1|"
                        + "Bông tai cấp 2 của con sẽ nhận được một chỉ số ngẫu nhiên khi hợp thể\n"
                        // + "và chỉ số phụ cộng thêm(tối đa 15%)\n"
                        + "|2|Tỉ lệ 50% \n";

                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                        npcSay, "Nâng cấp\n2K TV khóa", "Từ chối");
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy bỏ vào 1 Porata cấp 2 , 99 Mảnh hồn và x5 Đá xanh lam !", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy bỏ vào 1 Porata cấp 2 , 99 Mảnh hồn và x5 Đá xanh lam !", "Đóng");
        }
    }

    private void menu_Ep_Sao_Zeno(Player player) {
        if (player.inventory.gold < COST_DAP_DO_KICH_HOAT) {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Chi phí ép cần 500tr vàng",
                    "Đóng");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 2) {
            Item trangBi = player.combineNew.itemsCombine.get(0);
            Item daPhaLe = player.combineNew.itemsCombine.get(1);

            if (!isTrangBiZeno(trangBi) || !isTrangBiZeno(trangBi)) {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 1 cải trang Himmel và 1 cải trang có chỉ số (Có thể ghép) mua tại Mr Santa",
                        "Đóng");
            }
            // if (!trangBi.isTrangBiGhepMain()) {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
            // "Cần 1 cải trang Himmel và 1 cải trang có chỉ số (Có thể ghép) mua tại Mr
            // Santa",
            // "Đóng");
            // return;
            // }
            int star = 0; // sao pha lê đã ép
            int starEmpty = 0; // lỗ sao pha lê
            player.combineNew.goldCombine = COST_DAP_DO_KICH_HOAT;
            if (trangBi != null && daPhaLe != null) {

                if (!isTrangBiNguyenLieuZeno(daPhaLe)) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Vật phẩm không phù hợp",
                            "Đóng");
                    return;
                }

                if (trangBi.template.id == daPhaLe.template.id) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Không thể ép trang bị giống nhau",
                            "Đóng");
                    return;
                }
                // kiểm tra trên cải trang nhận
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }

                if (!checkZenoHoaOK(trangBi, daPhaLe)) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            daPhaLe.template.name + " đã được ép vào trước đó, hãy dùng cải trang khác",
                            "Đóng");
                    return;
                }
                if (star < starEmpty) {

                    player.combineNew.gemCombine = getGemEpSao(star);

                    String npcSay = "|2|Hiện tại " + trangBi.template.name + "\n|0|";
                    String optin_old = "";
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id != 72) {
                            optin_old += io.getOptionString() + "\n";
                        }
                    }
                    if (optin_old != "") {
                        npcSay += optin_old;
                    }
                    // String option = "";

                    // for (ItemOption io : trangBi.itemOptions) {
                    // if (io.optionTemplate.id == 50 || io.optionTemplate.id == 77 ||
                    // io.optionTemplate.id == 103) {
                    // for (ItemOption ioDaPhaLe : daPhaLe.itemOptions) {
                    // if (ioDaPhaLe.optionTemplate.id == io.optionTemplate.id) {
                    // int param = io.param + ioDaPhaLe.param;
                    // String OptionBase = io.optionTemplate.name;
                    // option += OptionBase.replaceAll("#", String.valueOf(param)) + "\n";
                    // break;
                    // }
                    // }
                    // } else if (io.optionTemplate.id == 107 || io.optionTemplate.id == 102
                    // || io.optionTemplate.id == 196) {
                    // continue;
                    // } else {
                    // option += io.optionTemplate.name;
                    // option += "\n";
                    // }
                    // }
                    // npcSay += "|2|Sau khi nâng cấp \n|7|"
                    // + option + "\n";
                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Trang bị không còn ô sao trống để ghép", "Đóng");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần một cải trang Himmel có sao pha lê và cải trang có chỉ số (có thể ép) 2", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần một cải trang Himmel có sao pha lê và cải trang có chỉ số (có thể ép) 1", "Đóng");
        }
    }

    private void menu_Ep_Sao_Trang_Bi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; // sao pha lê đã ép
                    int starEmpty = 0; // lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = "|5|" + trangBi.template.name + "\n|2|";
                            for (ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name
                                        .replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\n " + player.combineNew.gemCombine + " ngọc\n" + "1 lần",
                                    "Nâng cấp\n " + 1000 + " ngọc\n" + "Full sao");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
    }

    private void menu_Pha_Le_Hoa_Trang_Bi(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            menu_Pha_Le_Hoa_Trang_Bi_OLD(player);
        } else if (player.combineNew.itemsCombine.size() == 2) {
            menu_Pha_Le_Hoa_Trang_Bi_new(player);
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                    "Đóng");
        }

    }

    private void menu_Pha_Le_Hoa_Trang_Bi_new(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            if (isCouplePhaLeHoa(player.combineNew.itemsCombine.get(0),
                    player.combineNew.itemsCombine.get(1))) {
                Item trangBi = null;
                Item daNangCap = null;
                if (isTrangBiPhaLeHoa(player.combineNew.itemsCombine.get(0))) {
                    trangBi = player.combineNew.itemsCombine.get(0);
                    daNangCap = player.combineNew.itemsCombine.get(1);
                } else {
                    trangBi = player.combineNew.itemsCombine.get(1);
                    daNangCap = player.combineNew.itemsCombine.get(0);
                }
                int star = 0;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                    player.combineNew.gemCombine = getGemPhaLeHoa(star);
                    player.combineNew.countDaNangCap = getCountDaMayMan(star);

                    String npcSay = trangBi.template.name + "\n|2|";
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id != 102) {
                            npcSay += io.getOptionString() + "\n";
                        }
                    }
                    npcSay += "|7|Tỉ lệ thành công: 100%" + "\n"
                            + (player.combineNew.countDaNangCap > daNangCap.quantity ? "|7|" : "|2|")
                            + "Cần " + player.combineNew.countDaNangCap + " " + daNangCap.template.name
                            + "\n";
                    if (player.combineNew.goldCombine <= player.inventory.gold) {
                        npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc", "Nâng cấp\ntự động");
                    } else {
                        npcSay += "Còn thiếu "
                                + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                + " vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                }

            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 trang bị và đá pha lê siêu cấp", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 trang bị và đá pha lê siêu cấp", "Đóng");
        }
    }

    private void menu_Pha_Le_Hoa_Trang_Bi_OLD(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            if (player.inventory.gold < 0) {
                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ vàng để thực hiện", "Đóng");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                    player.combineNew.gemCombine = getGemPhaLeHoa(star);
                    player.combineNew.ratioCombine = getRatioPhaLeHoaBip(star);
                    float ratiohienthi = getRatioPhaLeHoa(star);
                    String npcSay = item.template.name + "\n|2|";
                    for (ItemOption io : item.itemOptions) {
                        if (io.optionTemplate.id != 102) {
                            npcSay += io.getOptionString() + "\n";
                        }
                    }
                    npcSay += "|7|Tỉ lệ thành công: " + ratiohienthi + "%" + "\n";

                    if (player.combineNew.goldCombine <= player.inventory.gold) {
                        npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "1\nlần", "10\n lần", "50\n lần", "100\n lần ", "200\n lần", "1000\n lần", "Hủy");
                    } else {
                        npcSay += "Còn thiếu "
                                + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                + " vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ",
                        "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                    "Đóng");
        }

    }

    private void menu_ChuyenSaoPhaLe(Player player) {
    if (player.combineNew.itemsCombine.size() != 3) {
        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Chọn đúng 3 món:\n- 1 trang bị đã ép sao \n- 1 trang bị cùng loại\n- 20.000 Thỏi vàng khóa", "Đóng");
        return;
    }

    Item itemCoSao = null, itemNhan = null, tvk = null;

for (Item it : player.combineNew.itemsCombine) {
    if (it == null || it.template == null) {
        Service.getInstance().sendThongBao(player, "Có vật phẩm lỗi. Vui lòng bỏ ra và thử lại.");
        return; // hoặc continue nếu bạn muốn bỏ qua item lỗi
    }

    if (it.template.id == 1429) {
        tvk = it;
    } else if (getSao(it) > 0) {
        itemCoSao = it;
    } else {
        itemNhan = it;
    }
}


    if (itemCoSao == null || itemNhan == null || tvk == null) {
        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Thiếu trang bị đã ép sao hoặc chưa đủ 20.000 Thỏi vàng khóa!", "Đóng");
        return;
    }

    if (itemCoSao.template.type != itemNhan.template.type) {
        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Trang bị phải cùng loại: Áo→Áo, Quần→Quần, Găng→Găng...", "Đóng");
        return;
    }

    int star = getSao(itemCoSao);
    String npcSay = "|1|Xác nhận chuyển Sao Pha Lê\n\n"
            + "|2|Từ: " + itemCoSao.template.name + "\n"
            + "Sang: " + itemNhan.template.name + "\n"
            + "|7|Số sao chuyển: " + star + "\n"
            + "|7|Tỉ lệ thành công: 100%\n"
            + "|1|Tiêu tốn: 20.000 Thỏi vàng khóa";
    
    baHatMit.createOtherMenu(player, ConstNpc.MENU_CHUYEN_SPL, npcSay, "Xác nhận");
}

    private void menu_TaySaoPhaLe(Player player) {
    if (player.combineNew.itemsCombine.size() != 2) {
        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Chọn đúng 2 món:\n"
                + "- 1 trang bị có Sao Pha Lê đã ép\n"
                + "- 20 Đá tẩy\n"
                + "- Tốn 10.000 Thỏi vàng khóa khi thực hiện tẩy.",
                "Đóng");
        return;
    }
    Item trangBi = null, daTay = null;
    for (Item it : player.combineNew.itemsCombine) {
        if (it.template.id == 1630) daTay = it; 
        else if (hasOption(it, 102)) trangBi = it; 
    }
    if (trangBi == null) {
        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Không tìm thấy trang bị có Sao Pha Lê đã ép!", "Đóng");
        return;
    }
    if (daTay == null || daTay.quantity < 20) {
        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Thiếu 20 Đá tẩy!", "Đóng");
        return;
    }
    Item tvk = InventoryServiceNew.gI().findItemBag(player, 1429);
    if (tvk == null || tvk.quantity < 10000) {
        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần 10.000 Thỏi vàng khóa trong túi để thực hiện!", "Đóng");
        return;
    }
    int soSao = getParamOption(trangBi, 102);
    String npcSay = "|1|Xác nhận Tẩy Sao Pha Lê\n\n"
            + "|2|Trang bị: " + trangBi.template.name + "\n"
            + "|7|Số sao hiện tại: " + soSao + "\n"
            + "|7|Sau tẩy: Sao đã ép sẽ bị xóa, giữ nguyên số sao chưa ép\n"
            + "|1|Tỉ lệ thành công: 100%\n"
            + "|1|Tiêu tốn:\n- 20 Đá tẩy\n- 10.000 Thỏi vàng khóa";
    baHatMit.createOtherMenu(player, ConstNpc.MENU_TAY_SPL, npcSay, "Xác nhận\nTẩy Sao");
}
  
    private void menu_Nhap_Ngoc_Rong(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.combineNew.itemsCombine.size() == 1) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem()) {
                    if ((item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                        String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n" + "1 viên "
                                + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                + "|7|Cần 7 " + item.template.name;
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                "Làm phép\ntự động",
                                "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    }
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống",
                    "Đóng");
        }
    }

    private void menu_Doi_Ruong(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.combineNew.itemsCombine.size() == 1) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem()) {
                    if (item.template.id == 570 && item.quantity >= 1) {
                        if (item.itemOptions != null) {
                            for (ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id == 72) {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Yêu cầu rương cấp 0", "Đóng");
                                    return;
                                }
                            }
                        }

                        String npcSay = "|2|Con có muốn phù phép " + item.template.name
                                + " thành rương gỗ có cấp ngẫu nhiên không\n"
                                + "|7|Cần 7 " + item.template.name;
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép",
                                "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 rương gỗ", "Đóng");
                    }
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 1 rương gỗ", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống",
                    "Đóng");
        }
    }

    private void menu_Nang_Cap_SKH_Vip_1(Player player) {
    // Phải đủ 5 món Thần Linh
    if (player.combineNew.itemsCombine.size() != 5) {
        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần 5 món trang bị Thần Linh để nâng cấp!", "Đóng");
        return;
    }
    for (Item it : player.combineNew.itemsCombine) {
        if (!it.isDTL()) {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Chỉ chấp nhận trang bị Thần Linh!", "Đóng");
            return;
        }
    }
    String npcSay = "|2|Con muốn dùng 5 món Thần Linh để tạo 1 món Kích Hoạt Thần Linh ngẫu nhiên?\n"
            + "|7|Sau khi nâng cấp, 5 món cũ sẽ biến mất!";
    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
            npcSay, "Nâng cấp\nMiễn phí", "Từ chối");
}

    private void menu_Nang_Cap_SKH_Thuong(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            if (!player.combineNew.itemsCombine.get(0).isDHD()) {
                this.thuongDeNew.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Yêu cầu 1 món trang bị huỷ diệt",
                        "Đóng");
                return;
            }
            if (!player.combineNew.itemsCombine.get(0).isCanSKH()) {
                this.thuongDeNew.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Yêu cầu trang bị hủy diệt được nâng cấp từ đồ thần linh mới có thể nâng SKH",
                        "Đóng");
                return;
            }
            if (player.inventory.gold < COST) {
                this.thuongDeNew.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng",
                        "Đóng");
                return;
            }
            String npcSay = "|2|Đồ kích hoạt sẽ dựa vào món đồ con bỏ vào\n|7|"
                    + "Con sẽ nhận được "
                    + player.combineNew.itemsCombine.get(0).typeName()
                    + " kích hoạt ngẫu nhiên\n"
                    + "|1|Cần " + Util.numberToMoney(COST) + " vàng";
            this.thuongDeNew.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
        } else {
            this.thuongDeNew.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 1 trang bị hủy diệt", "Đóng");

        }
    }

    private void menu_Nang_Cap_SKH_Thuong_GOLD_BAR(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            if (!player.combineNew.itemsCombine.get(0).isDHD()) {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Yêu cầu 1 món trang bị huỷ diệt",
                        "Đóng");
                return;
            }

            // Kiểm tra số lượng ruby thay vì kiểm tra item ID 861
            if (player.inventory.ruby < 200) {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 200 Ruby để nâng cấp set kích hoạt",
                        "Đóng");
                return;
            }

            String npcSay = "|2|Đồ kích hoạt sẽ dựa vào món đồ con bỏ vào\n|7|"
                    + "Con sẽ nhận được "
                    + player.combineNew.itemsCombine.get(0).typeName()
                    + " kích hoạt ngẫu nhiên\n"
                    + "|1|Cần 200 Ruby";
            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    npcSay, "Nâng cấp\n 200\nRuby", "Từ chối");
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 1 trang bị hủy diệt", "Đóng");
        }
    }

    private void menu_Nang_Cap_Bong_Toi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            if (isCoupleBongToi(player.combineNew.itemsCombine.get(0),
                    player.combineNew.itemsCombine.get(1))) {
                Item trangBi = null;
                Item daNangCap = null;
                if (isTypeBongToi(player.combineNew.itemsCombine.get(0))) {

                    trangBi = player.combineNew.itemsCombine.get(0);
                    daNangCap = player.combineNew.itemsCombine.get(1);
                } else {
                    trangBi = player.combineNew.itemsCombine.get(1);
                    daNangCap = player.combineNew.itemsCombine.get(0);
                }

                int level = 0;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 222) {
                        level = io.param;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.combineNew.goldCombine = getGoldBongToi(level);
                    player.combineNew.ratioCombine = getTileBongToi(level);
                    player.combineNew.countDaNangCap = getCountDaNangCapBongToi(level);

                    String npcSay = "|2|Hiện tại " + trangBi.template.name + " (+" + level + ")\n|0|";
                    String optin_old = "";
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id != 222) {
                            optin_old += io.getOptionString() + "\n";
                        }
                    }
                    if (optin_old != "") {
                        npcSay += optin_old;
                    }
                    String option = "Chỉ số pháp sư ngẫu nhiên";


                    npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                            // + option.replaceAll("#", String.valueOf(param))
                            + option
                            + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                            + (player.combineNew.countDaNangCap > daNangCap.quantity ? "|7|" : "|1|")
                            + "Cần " + player.combineNew.countDaNangCap + " " + daNangCap.template.name
                            + "\n"
                            + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                    if (player.combineNew.countDaNangCap > daNangCap.quantity) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - daNangCap.quantity)
                                + " " + daNangCap.template.name);
                    } else if (player.combineNew.goldCombine > player.inventory.gold) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                npcSay,
                                "Còn thiếu\n"
                                + Util.numberToMoney(
                                        (player.combineNew.goldCombine - player.inventory.gold))
                                + " vàng");
                    } else {

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay,
                                "Nâng cấp \n" + Util.numberToMoney(player.combineNew.goldCombine)
                                + " vàng",
                                "Nâng cấp\ntự động",
                                "Từ chối");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 trang bị và Ngọc pháp sư", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 trang bị và Ngọc pháp sư!!", "Đóng");
        }
    }

    private void menu_Nang_Cap_Bong_Tai_3(Player player) {
    Item tv = InventoryService.gI().findItemBag(player, 1429);
    if (tv == null || tv.quantity < 50_000) {
        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần 50K TV khóa để nâng cấp", "Đóng");
        return;
    }

    if (player.combineNew.itemsCombine.size() == 2) {
        Item bongTai = null;
        Item manhBongTai = null;

        for (Item it : player.combineNew.itemsCombine) {
            if (it == null || it.template == null) {
                return;
            }

            int id = it.template.id;
            if (id == 1601) manhBongTai = it;
            else if (id == 921) bongTai = it;
        }

        if (bongTai != null && manhBongTai != null && manhBongTai.quantity >= 999) {
            String npcSay = "|2|Con có muốn dùng nguyên liệu để nâng cấp\n|1|"
                    + "Con sẽ nhận được bông tai cấp 2 tăng 10% chỉ số khi hợp thể\n"
                    + "và chỉ số phụ cộng thêm(tối đa 15%)\n"
                    + "|2|Tỉ lệ 100% \n";
            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                    npcSay, "Nâng cấp\n50K TV khóa", "Từ chối");
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy bỏ vào đủ X999 mảnh vỡ bông tai cấp 3 và X1 Bông tai Porata", "Đóng");
        }
    } else {
        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần đặt đúng 2 món: X1 Bông tai Porata và X999 mảnh vỡ bông tai cấp 3", "Đóng");
    }
}


    private void menu_Nang_Cap_Chi_So_Bong_Tai_3(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item bongTai = null;
            Item manhHon = null;
            Item Xlam = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == 1451) {
                    bongTai = it;
                }
                if (it.getId() == 1602) {
                    manhHon = it;
                }
                if (it.getId() == 1603) {
                    Xlam = it;
                }
            }
            if (bongTai != null && manhHon != null && Xlam != null
                    && manhHon.quantity >= 99 && Xlam.quantity >= 5) {
                String npcSay = "|2|Con có muốn dùng nguyên liệu để nâng cấp\n|1|"
                        + "Bông tai cấp 3 của con sẽ nhận được một chỉ số ngẫu nhiên khi hợp thể\n"
                        // + "và chỉ số phụ cộng thêm(tối đa 15%)\n"
                        + "|2|Tỉ lệ 40% \n";

                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                        npcSay, "Nâng cấp\n5K TV khóa", "Từ chối");
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy bỏ vào 1 Porata cấp 3 , X99 Mảnh hồn cấp 3 và x5 Đá xanh lục !", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy bỏ vào 1 Porata cấp 3 , X99 Mảnh hồn cấp 3 và x5 Đá xanh lục !", "Đóng");
        }
    }
    
    private void menu_Nang_Cap_Bong_Tai_4(Player player) {
        if (player.inventory.ruby < OK_BONG_TAI_3) { // bông tai
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 500 ruby để nâng cấp",
                    "Đóng");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 2) {
            Item bongTai = null;
            Item manhBongTai = null;

            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == 1609) {
                    manhBongTai = it;
                }
                if (it.getId() == 1451) {
                    bongTai = it;
                }
            }
            if (bongTai != null && manhBongTai != null && manhBongTai.quantity >= 999) {
                String npcSay = "|2|Con có muốn dùng nguyên liệu để nâng cấp\n|1|"
                        + "Con sẽ nhận được bông tai cấp 4 tăng 10% chỉ số khi hợp thể\n"
                        + "và chỉ số phụ cộng thêm(tối đa 20%)\n"
                        + "|2|Tỉ lệ 100% \n";
                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                        npcSay, "Nâng cấp\n500 ruby", "Từ chối");
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy bỏ vào đủ X999 mảnh vỡ bông tai và X1 Bông tai Porata", "Đóng");
            }

        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Để nâng cấp bông tai Porata, con cần 1 Bông tai Porata Cấp 3 và 999 mảnh vỡ bông tai cấp 3",
                    "Đóng");
        }
    }

    private void menu_Nang_Cap_Chi_So_Bong_Tai_4(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item bongTai = null;
            Item manhHon = null;
            Item Xlam = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == 1608) {
                    bongTai = it;
                }
                if (it.getId() == 1610) {
                    manhHon = it;
                }
                if (it.getId() == 1611) {
                    Xlam = it;
                }
            }
            if (bongTai != null && manhHon != null && Xlam != null
                    && manhHon.quantity >= 99 && Xlam.quantity >= 5) {
                String npcSay = "|2|Con có muốn dùng nguyên liệu để nâng cấp\n|1|"
                        + "Bông tai cấp 4 của con sẽ nhận được một chỉ số ngẫu nhiên khi hợp thể\n"
                        // + "và chỉ số phụ cộng thêm(tối đa 15%)\n"
                        + "|2|Tỉ lệ 50% \n";

                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                        npcSay, "Nâng cấp\n1000Ruby", "Từ chối");
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy bỏ vào 1 Porata cấp 4 , X99 Mảnh hồn cấp 4 và x5 Đá xanh lục !", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy bỏ vào 1 Porata cấp 4 , X99 Mảnh hồn cấp 4 và x5 Đá xanh lục !", "Đóng");
        }
    }

    private void menu_Tay_Chi_So_Bong_Toi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            if (isCoupleTayBongToi(player.combineNew.itemsCombine.get(0),
                    player.combineNew.itemsCombine.get(1))) {
                Item trangBi = null;
                Item daNangCap = null;
                if (isTypeBongToi(player.combineNew.itemsCombine.get(0))) {
                    trangBi = player.combineNew.itemsCombine.get(0);
                    daNangCap = player.combineNew.itemsCombine.get(1);
                } else {
                    trangBi = player.combineNew.itemsCombine.get(1);
                    daNangCap = player.combineNew.itemsCombine.get(0);
                }

                int level = 0;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 222) {
                        level = io.param;
                        break;
                    }
                }
                if (level > 0) {
                    player.combineNew.goldCombine = 500000000;
                    player.combineNew.countDaNangCap = 1;
                    String npcSay = "|2|Hiện tại " + trangBi.template.name + " (+" + level + ")\n|0|";
                    String optin_old = "";
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id != 222) {
                            if (!isChiSoBongToi(io.optionTemplate.id)) {
                                optin_old += io.getOptionString() + "\n";
                            }
                        }
                    }
                    if (optin_old != "") {
                        npcSay += optin_old;
                    }

                    npcSay += "|2|Sau khi thanh tẩy (+ 0)\n|7|"
                            + optin_old
                            + "\n|7|Tỉ lệ thành công: 100%\n"
                            + (player.combineNew.countDaNangCap > daNangCap.quantity ? "|7|" : "|1|")
                            + "Cần " + player.combineNew.countDaNangCap + " " + daNangCap.template.name
                            + "\n"
                            + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                    if (player.combineNew.countDaNangCap > daNangCap.quantity) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - daNangCap.quantity)
                                + " " + daNangCap.template.name);
                    } else if (player.combineNew.goldCombine > player.inventory.gold) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                npcSay,
                                "Còn thiếu\n"
                                + Util.numberToMoney(
                                        (player.combineNew.goldCombine - player.inventory.gold))
                                + " vàng");
                    } else {

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay,
                                "Thanh tẩy \n" + Util.numberToMoney(player.combineNew.goldCombine)
                                + " vàng",
                                "Từ chối");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Trang bị không có chỉ số pháp sư để tẩy", "Đóng");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 trang bị và đá tẩy", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 trang bị và đá tẩy", "Đóng");
        }
    }

    private void menu_Nang_Cap_Vat_Pham(Player player) {
        Item trangBi = null;
        Item daNangCap = null;
        Item daBaoVe = null;
        if (player.combineNew.itemsCombine.size() == 2) {

            if (player.combineNew.itemsCombine.get(0).template.type < 5) {
                trangBi = player.combineNew.itemsCombine.get(0);
                daNangCap = player.combineNew.itemsCombine.get(1);
            } else if (player.combineNew.itemsCombine.get(0).template.type == 14) {
                trangBi = player.combineNew.itemsCombine.get(1);
                daNangCap = player.combineNew.itemsCombine.get(0);
            } else {
                Service.getInstance().sendThongBao(player, "Nâng cấp vật phẩm gồm trang bị, đá nâng cấp");
                return;
            }
        } else if (player.combineNew.itemsCombine.size() == 3) {

            if (player.combineNew.itemsCombine.get(0).template.type < 5) {
                trangBi = player.combineNew.itemsCombine.get(0);
                if (player.combineNew.itemsCombine.get(1).template.type == 14) {
                    daNangCap = player.combineNew.itemsCombine.get(1);
                    daBaoVe = player.combineNew.itemsCombine.get(2);
                } else {
                    daNangCap = player.combineNew.itemsCombine.get(2);
                    daBaoVe = player.combineNew.itemsCombine.get(1);
                }
            } else if (player.combineNew.itemsCombine.get(1).template.type < 5) {
                trangBi = player.combineNew.itemsCombine.get(1);
                if (player.combineNew.itemsCombine.get(0).template.type == 14) {
                    daNangCap = player.combineNew.itemsCombine.get(0);
                    daBaoVe = player.combineNew.itemsCombine.get(2);
                } else {
                    daNangCap = player.combineNew.itemsCombine.get(2);
                    daBaoVe = player.combineNew.itemsCombine.get(0);
                }
            } else if (player.combineNew.itemsCombine.get(2).template.type < 5) {
                trangBi = player.combineNew.itemsCombine.get(2);
                if (player.combineNew.itemsCombine.get(0).template.type == 14) {
                    daNangCap = player.combineNew.itemsCombine.get(0);
                    daBaoVe = player.combineNew.itemsCombine.get(1);
                } else {
                    daNangCap = player.combineNew.itemsCombine.get(1);
                    daBaoVe = player.combineNew.itemsCombine.get(0);
                }
            } else {
                Service.getInstance().sendThongBao(player, "Nâng cấp vật phẩm gồm trang bị, đá nâng cấp và đá bảo vệ");
                return;
            }
            if (daBaoVe.template.id != 987) {
                Service.getInstance().sendThongBao(player, "Nâng cấp vật phẩm gồm trang bị, đá nâng cấp và đá bảo vệ");
                return;
            }
        }

        if (isCoupleItemNangCap(trangBi,
                daNangCap)) {

            int level = 0;
            for (ItemOption io : trangBi.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    level = io.param;
                    break;
                }
            }
            if (level < MAX_LEVEL_ITEM) {
                player.combineNew.goldCombine = getGoldNangCapDo(level);
                player.combineNew.ratioCombine = getTileNangCapDo(level);
                player.combineNew.countDaNangCap = getCountDaNangCapDo(level);

                String npcSay = "|2|Hiện tại " + trangBi.template.name + " (+" + level + ")\n|0|";
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id != 72) {
                        npcSay += io.getOptionString() + "\n";
                    }
                }
                String option = null;
                int param = 0;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 47
                            || io.optionTemplate.id == 6
                            || io.optionTemplate.id == 0
                            || io.optionTemplate.id == 7
                            || io.optionTemplate.id == 14
                            || io.optionTemplate.id == 22
                            || io.optionTemplate.id == 23
                            || io.optionTemplate.id == 193) {
                        option = io.optionTemplate.name;
                        if (io.param * 10 / 100 == 0) {
                            param = io.param + 1;
                        } else {
                            param = io.param + (io.param * 10 / 100);
                        }

                        break;
                    }
                }
                npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                        + (option != null ? option.replaceAll("#", String.valueOf(param)) : "")
                        + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                        + (player.combineNew.countDaNangCap > daNangCap.quantity ? "|7|" : "|1|")
                        + "Cần " + player.combineNew.countDaNangCap + " " + daNangCap.template.name
                        + "\n"
                        + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                        + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                if (level > 2) {
                    npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                    npcSay += "\nBạn đang có "
                            + ItemService.gI().getQuantityItemOnBag(player, (short) ID_DA_BAO_VE)
                            + " đá bảo vệ, dùng đá bảo vệ sẽ không bị rớt cấp và có thể dùng tính năng đập từ động";
                }
                if (player.combineNew.countDaNangCap > daNangCap.quantity) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - daNangCap.quantity)
                            + " " + daNangCap.template.name);
                } else if (player.combineNew.goldCombine > player.inventory.gold) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            npcSay,
                            "Còn thiếu\n"
                            + Util.numberToMoney(
                                    (player.combineNew.goldCombine - player.inventory.gold))
                            + " vàng");
                } else {

                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay,
                            "Nâng cấp \n" + Util.numberToMoney(player.combineNew.goldCombine)
                            + " vàng",
                            "Nâng cấp \n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + "\nTự động",
                            "Từ chối");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
        }
    }

    private void menu_Nang_Cap_Phu_Kien(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            if (isCouplePhuKien(player.combineNew.itemsCombine.get(0),
                    player.combineNew.itemsCombine.get(1))) {
                Item trangBi = null;
                Item daNangCap = null;
                if (player.combineNew.itemsCombine.get(0).template.type == 5
                        || player.combineNew.itemsCombine.get(0).template.type == 32) {
                    trangBi = player.combineNew.itemsCombine.get(0);
                    daNangCap = player.combineNew.itemsCombine.get(1);
                } else {
                    trangBi = player.combineNew.itemsCombine.get(1);
                    daNangCap = player.combineNew.itemsCombine.get(0);
                }

                int level = 0;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.combineNew.goldCombine = getGoldPhaLeHoa(level);
                    player.combineNew.ratioCombine = getTileNangCapDo(level);
                    player.combineNew.countDaNangCap = getCountDaNangCapDo(level);

                    String npcSay = "|2|Hiện tại " + trangBi.template.name + " (+" + level + ")\n|0|";
                    String optin_old = "";
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id != 72) {
                            optin_old += io.getOptionString() + "\n";
                        }
                    }
                    if (optin_old != "") {
                        npcSay += optin_old;
                    }
                    String option = null;
                    int param = 0;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (isChiSoNguSac(io.optionTemplate.id)) {
                            option = io.optionTemplate.name;
                            param = (level + 1) * setParamNguSac(io.optionTemplate.id);
                            break;
                        }
                    }
                    if (option == null) {
                        option = "Chỉ số ngũ sắc ngẫu nhiên";
                    }

                    npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                            + option.replaceAll("#", String.valueOf(param))
                            + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                            + (player.combineNew.countDaNangCap > daNangCap.quantity ? "|7|" : "|1|")
                            + "Cần " + player.combineNew.countDaNangCap + " " + daNangCap.template.name
                            + "\n"
                            + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                    if (player.combineNew.countDaNangCap > daNangCap.quantity) {
                        this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - daNangCap.quantity)
                                + " " + daNangCap.template.name);
                    } else if (player.combineNew.goldCombine > player.inventory.gold) {
                        this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                npcSay,
                                "Còn thiếu\n"
                                + Util.numberToMoney(
                                        (player.combineNew.goldCombine - player.inventory.gold))
                                + " vàng");
                    } else {

                        this.toribot.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay,
                                "Nâng cấp \n" + Util.numberToMoney(player.combineNew.goldCombine)
                                + " vàng",
                                "Nâng cấp\ntự động",
                                "Từ chối");

                    }
                } else {
                    this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                }
            } else {
                this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 cải trang hoặc giáp luyện tập và đá ngũ sắc", "Đóng");
            }
        } else {
            this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 cải trang hoặc giáp luyện tập và đá ngũ sắc", "Đóng");
        }
    }

    private void menu_Tay_Chi_So_Phu_Kien(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            if (isCoupleThanhTay(player.combineNew.itemsCombine.get(0),
                    player.combineNew.itemsCombine.get(1))) {
                Item trangBi = null;
                Item daNangCap = null;
                if (player.combineNew.itemsCombine.get(0).template.type == 5
                        || player.combineNew.itemsCombine.get(0).template.type == 32) {
                    trangBi = player.combineNew.itemsCombine.get(0);
                    daNangCap = player.combineNew.itemsCombine.get(1);
                } else {
                    trangBi = player.combineNew.itemsCombine.get(1);
                    daNangCap = player.combineNew.itemsCombine.get(0);
                }

                int level = 0;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        break;
                    }
                }
                if (level > 0) {
                    player.combineNew.goldCombine = 500000000;
                    player.combineNew.countDaNangCap = 1;
                    String npcSay = "|2|Hiện tại " + trangBi.template.name + " (+" + level + ")\n|0|";
                    String optin_old = "";
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id != 72) {
                            if (!isChiSoNguSac(io.optionTemplate.id)) {
                                optin_old += io.getOptionString() + "\n";
                            }

                        }
                    }
                    if (optin_old != "") {
                        npcSay += optin_old;
                    }

                    npcSay += "|2|Sau khi thanh tẩy (+ 0)\n|7|"
                            + optin_old
                            + "\n|7|Tỉ lệ thành công: 100%\n"
                            + (player.combineNew.countDaNangCap > daNangCap.quantity ? "|7|" : "|1|")
                            + "Cần " + player.combineNew.countDaNangCap + " " + daNangCap.template.name
                            + "\n"
                            + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                    if (player.combineNew.countDaNangCap > daNangCap.quantity) {
                        this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - daNangCap.quantity)
                                + " " + daNangCap.template.name);
                    } else if (player.combineNew.goldCombine > player.inventory.gold) {
                        this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                npcSay,
                                "Còn thiếu\n"
                                + Util.numberToMoney(
                                        (player.combineNew.goldCombine - player.inventory.gold))
                                + " vàng");
                    } else {

                        this.toribot.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay,
                                "Thanh tẩy \n" + Util.numberToMoney(player.combineNew.goldCombine)
                                + " vàng",
                                "Từ chối");
                    }
                } else {
                    this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Trang bị không có chỉ số ngũ sắc để tẩy", "Đóng");
                }
            } else {
                this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 cải trang hoặc giáp luyện tập và đá suy bong", "Đóng");
            }
        } else {
            this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 cải trang hoặc giáp luyện tập và đá suy vong", "Đóng");
        }
    }

    private void menu_Doi_Ve_Huy_Diet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                String ticketName = "Vé đổi " + (item.template.type == 0 ? "áo"
                        : item.template.type == 1 ? "quần"
                                : item.template.type == 2 ? "găng" : item.template.type == 3 ? "giày" : "nhẫn")
                        + " hủy diệt";
                String npcSay = "|6|Ngươi có chắc chắn muốn đổi\n|7|" + item.template.name + "\n";
                for (ItemOption io : item.itemOptions) {
                    npcSay += "|2|" + io.getOptionString() + "\n";
                }
                npcSay += "|6|Lấy\n|7|" + ticketName + "\n|6|Với giá "
                        + Util.numberToMoney(COST_DOI_VE_DOI_DO_HUY_DIET) + " vàng không?";
                if (player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Đổi",
                            "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Còn thiếu\n"
                            + Util.numberToMoney(COST_DOI_VE_DOI_DO_HUY_DIET - player.inventory.gold) + " vàng",
                            "Đóng");
                }

            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 trang bị thần linh ngươi muốn trao đổi", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 trang bị thần linh ngươi muốn trao đổi", "Đóng");
        }
    }

    private void menu_Doi_Do_Than_Linh_Thanh_Huy_Diet(Player player) {
        Item thucAn = InventoryService.gI().findMealChangeDestroyClothes(player);
        Item tv = InventoryService.gI().findItemBag(player, 1429);
        if (thucAn == null) {
            this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy mang theo x99 thức ăn đến cho ta", "Đóng");
            return;
        }
        if (player.combineNew.itemsCombine.size() > 0 && player.combineNew.itemsCombine.size() <= 5) {
            String npcSay = "Ngươi có chắc chắn muốn đổi\n|2|";
            int count_do_than = 0;

            for (int i = 0; i < player.combineNew.itemsCombine.size(); i++) {
                Item item = player.combineNew.itemsCombine.get(i);
                if (item.isNotNullItem() && item.isDTL()) {
                    npcSay += item.template.name + "\n";
                    // for (ItemOption io : item.itemOptions) {
                    // npcSay += "|2|" + io.getOptionString() + "\n";
                    // }
                    count_do_than++;
                }
            }
            if (count_do_than >= 1) {
                npcSay += "|1|Lấy " + count_do_than
                        + " món trang bị Huỷ Diệt được phù phép (0-15%) tương ứng\nPhù phép mỗi lần sẽ cần "
                        + Util.numberToMoney(COST_DOI_VE_DOI_DO_HUY_DIET) + " TVK";
                if (tv.quantity >= (long) (COST_DOI_VE_DOI_DO_HUY_DIET * count_do_than)) {
                    this.bill.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Đổi", "Từ chối");
                } else {
                    npcSay += "|7|Còn thiếu "
                            + Util.numberToMoney((long) ((long) (COST_DOI_VE_DOI_DO_HUY_DIET * count_do_than)
                                    - player.inventory.gold))
                            + " TVK";
                    this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                            "Đóng");
                }
            } else {
                this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 đến 5 trang bị thần linh ngươi muốn trao đổi", "Đóng");
            }
        } else {
            this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 đến 5 trang bị thần linh ngươi muốn trao đổi", "Đóng");
        }
    }

    private void menu_Tay_Chi_So_Khong_The_GD(Player player) {
        if (player.combineNew.itemsCombine.size() > 0 && player.combineNew.itemsCombine.size() <= 5) {
            int count_do_than = 0;
            for (int i = 0; i < player.combineNew.itemsCombine.size(); i++) {
                Item item = player.combineNew.itemsCombine.get(i);
                if (item.isNotNullItem() && item.isDTL()) {

                    for (ItemOption op : item.itemOptions) {
                        if (op.optionTemplate.id == ConstOption.KHONG_THE_GD) {
                            item.itemOptions.remove(op);
                            count_do_than++;
                            break;
                        }
                    }

                }
            }

            if (count_do_than >= 1) {
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
                this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Tẩy thành công", "Đóng");
                return;
            } else {
                this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Chức năng chỉ dành cho trang bị thần linh bị lỗi", "Đóng");
                return;
            }
        } else {
            this.toribot.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 đến 5 trang bị thần linh ngươi muốn trao đổi", "Đóng");
            return;
        }

    }

    private void menu_Dap_Set_Kich_Hoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
            Item dhd = null, dtl = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 650 && item.template.id <= 662) {
                        dhd = item;
                    } else if (item.template.id >= 555 && item.template.id <= 567) {
                        dtl = item;
                    }
                }
            }
            if (dhd != null) {
                String npcSay = "|6|" + dhd.template.name + "\n";
                for (ItemOption io : dhd.itemOptions) {
                    npcSay += "|2|" + io.getOptionString() + "\n";
                }
                if (dtl != null) {
                    npcSay += "|6|" + dtl.template.name + "\n";
                    for (ItemOption io : dtl.itemOptions) {
                        npcSay += "|2|" + io.getOptionString() + "\n";
                    }
                }
                npcSay += "Ngươi có muốn chuyển hóa thành\n";
                npcSay += "|1|" + getNameItemC0(dhd.template.gender, dhd.template.type)
                        + " (ngẫu nhiên kích hoạt)\n|7|Tỉ lệ thành công " + (dtl != null ? "100%" : "40%")
                        + "\n|2|Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";
                if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Còn thiếu\n"
                            + Util.numberToMoney(player.inventory.gold - COST_DAP_DO_KICH_HOAT) + " vàng");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Ta cần 1 món đồ hủy diệt của ngươi để có thể chuyển hóa 1", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Ta cần 1 món đồ hủy diệt của ngươi để có thể chuyển hóa 2", "Đóng");
        }
    }

    private void menu_Doi_Manh_Kich_Hoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2
                || player.combineNew.itemsCombine.size() == 3) {
            Item nr1s = null, doThan = null, buaBaoVe = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 14) {
                    nr1s = it;
                } else if (it.template.id == 2010) {
                    buaBaoVe = it;
                } else if (it.template.id >= 555 && it.template.id <= 567) {
                    doThan = it;
                }
            }

            if (nr1s != null && doThan != null) {
                int tile = 50;
                String npcSay = "|6|Ngươi có muốn trao đổi\n|7|" + nr1s.template.name
                        + "\n|7|" + doThan.template.name
                        + "\n";
                for (ItemOption io : doThan.itemOptions) {
                    npcSay += "|2|" + io.getOptionString() + "\n";
                }
                if (buaBaoVe != null) {
                    tile = 100;
                    npcSay += buaBaoVe.template.name
                            + "\n";
                    for (ItemOption io : buaBaoVe.itemOptions) {
                        npcSay += "|2|" + io.getOptionString() + "\n";
                    }
                }

                npcSay += "|6|Lấy\n|7|Mảnh kích hoạt\n"
                        + "|1|Tỉ lệ " + tile + "%\n"
                        + "|6|Với giá " + Util.numberToMoney(COST_DOI_MANH_KICH_HOAT) + " vàng không?";

                if (player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Đổi", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            npcSay, "Còn thiếu\n"
                            + Util.numberToMoney(COST_DOI_MANH_KICH_HOAT - player.inventory.gold) + " vàng",
                            "Đóng");

                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");

            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");

        }
    }

    private void menu_Dap_Set_Kich_Hoat_Cao_Cap(Player player) {
        if (player.combineNew.itemsCombine.size() <= 3) {
            switch (player.combineNew.itemsCombine.size()) {
                case 2: {
                    Item it = player.combineNew.itemsCombine.get(0),
                            it1 = player.combineNew.itemsCombine.get(1);
                    if (!isActivationClothes(it) || !isDestroyClothes(it1.template.id)) {
                        it = null;
                    }
                    if (it != null) {
                        String npcSay = "|1|" + it.template.name + "\n" + it1.template.name + "\n";
                        npcSay += "Ngươi có muốn chuyển hóa thành\n";
                        npcSay += "|7|" + getTypeTrangBi(it.template.type)
                                + " cấp bậc ngẫu nhiên (set kích hoạt ngẫu nhiên)\n|2|Cần "
                                + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";
                        float tile50 = player.combineNew.ratioCombine = 50f;
                        npcSay += "\n|7| Tỉ lệ thành công : " + tile50 + "%";
                        if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                    "Còn thiếu\n"
                                    + Util.numberToMoney(player.inventory.gold - COST_DAP_DO_KICH_HOAT)
                                    + " vàng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Nếu ngươi muốn chuyển hoá trang bị kích hoạt VIP\nTa có 2 lựa chọn cho ngươi sau đây :"
                                + "\n|7|Đưa cho ta 1 trang bị kích hoạt thường và 1 trang bị Huỷ Diệt"
                                + "\n|2|Ta sẽ cho ngươi tỉ lệ 50% thành công"
                                + "\n|7|Đưa cho ta 1 trang bị kích hoạt thường và 2 trang bị Huỷ Diệt\n"
                                + "|2|Ta sẽ cho ngươi tỉ lệ 100% thành công",
                                "Đóng");
                    }
                    break;
                }
                case 3: {
                    Item it = player.combineNew.itemsCombine.get(0),
                            it1 = player.combineNew.itemsCombine.get(1),
                            it2 = player.combineNew.itemsCombine.get(2);
                    if (!isActivationClothes(it) || !isDestroyClothes(it1.template.id)
                            || !isDestroyClothes(it2.template.id)) {
                        it = null;
                    }
                    if (it != null) {
                        String npcSay = "|1|" + it.template.name + "\n" + it1.template.name + "\n"
                                + it2.template.name + "\n";
                        npcSay += "Ngươi có muốn chuyển hóa thành\n";
                        npcSay += "|7|" + getTypeTrangBi(it.template.type)
                                + " cấp bậc ngẫu nhiên (set kích hoạt ngẫu nhiên)\n|2|Cần "
                                + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";
                        float tile50 = player.combineNew.ratioCombine = 100f;
                        npcSay += "\n|7| Tỉ lệ thành công : " + tile50 + "%";
                        if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                    "Còn thiếu\n"
                                    + Util.numberToMoney(player.inventory.gold - COST_DAP_DO_KICH_HOAT)
                                    + " vàng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Nếu ngươi muốn chuyển hoá trang bị kích hoạt VIP\nTa có 2 lựa chọn cho ngươi sau đây :"
                                + "\n|7|Đưa cho ta 1 trang bị kích hoạt thường và 1 trang bị Huỷ Diệt"
                                + "\n|2|Ta sẽ cho ngươi tỉ lệ 50% thành công"
                                + "\n|7|Đưa cho ta 1 trang bị kích hoạt thường và 2 trang bị Huỷ Diệt\n"
                                + "|2|Ta sẽ cho ngươi tỉ lệ 100% thành công",
                                "Đóng");
                    }
                    break;
                }
                default:
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Nếu ngươi muốn chuyển hoá trang bị kích hoạt VIP\nTa có 2 lựa chọn cho ngươi sau đây :"
                            + "\n|7|Đưa cho ta 1 trang bị kích hoạt thường và 1 trang bị Huỷ Diệt"
                            + "\n|2|Ta sẽ cho ngươi tỉ lệ 50% thành công"
                            + "\n|7|Đưa cho ta 1 trang bị kích hoạt thường và 2 trang bị Huỷ Diệt\n"
                            + "|2|Ta sẽ cho ngươi tỉ lệ 100% thành công",
                            "Đóng");
                    break;
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Ta cần 3 món đồ hủy diệt của ngươi để có thể chuyển hóa", "Đóng");
        }
    }

    private void menu_THANG_CAP_NGOC_BOI(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2) {
            Item ngocBoi = null, nguyenLieu = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 1559 && item.template.id <= 1567) {
                        ngocBoi = item;
                    } else if (item.template.id == 1568) {
                        nguyenLieu = item;
                    }
                }
            }
            int level = 0;
            int chucPhuc = 0;
            boolean needsBaoNgoc = false;
            int requiredStones = 0;
            if (ngocBoi != null && nguyenLieu != null) {
                for (ItemOption io : ngocBoi.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        if (level >= 4) {
                            needsBaoNgoc = true;
                        }
                    } else if (io.optionTemplate.id == 248) {
                        chucPhuc = io.param;
                    }
                }
                requiredStones = level + 1;
                if (level < 7) {
                    String npcSay = "Hiện tại " + ngocBoi.template.name + " (+" + level + ")\n";
                    // Hiển thị thông tin hiện tại (tất cả chỉ số trừ cấp độ và trúc phúc)
                    for (ItemOption io : ngocBoi.itemOptions) {
                        if (io.optionTemplate.id != 72 && io.optionTemplate.id != 248 && io.optionTemplate.id != 30) {
                            npcSay += io.getOptionString() + "\n";
                        }
                    }

                    // Dự đoán sau nâng cấp, hiển thị tất cả chỉ số thay đổi
                    StringBuilder predictStats = new StringBuilder();
                    for (ItemOption io : ngocBoi.itemOptions) {
                        String option = null;
                        int param = 0;
                        if (io.optionTemplate.id == 50 || io.optionTemplate.id == 77 || io.optionTemplate.id == 103) {
                            option = io.optionTemplate.name;
                            param = io.param + 1; // Tăng 1% mỗi cấp
                        } else if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23
                                || io.optionTemplate.id == 193) {
                            option = io.optionTemplate.name;
                            if (io.param * 10 / 100 == 0) {
                                param = io.param + 1;
                            } else {
                                param = io.param + (io.param * 10 / 100);
                            }
                        }
                        if (option != null) {
                            predictStats.append(option.replaceAll("#", String.valueOf(param))).append("\n");
                        }
                    }
                    npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|1|"
                            + (predictStats.length() > 0 ? predictStats.toString() : "Không có chỉ số thay đổi\n")
                            + "|0|Cần " + requiredStones + " " + nguyenLieu.template.name + "\n"
                            + (COST_GIA_HAN_CAI_TRANG > player.inventory.ruby ? "" : "");

                    if (needsBaoNgoc) {
                        npcSay += "Cần thêm 1 Bùa Bảo Ngọc";
                    }

                    if (player.inventory.ruby >= COST_GIA_HAN_CAI_TRANG) {
                        if (nguyenLieu.quantity >= requiredStones) {
                            this.thongoc.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp", "Từ Chối");
                        } else {
                            this.thongoc.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                    "Còn thiếu\n" + (requiredStones - nguyenLieu.quantity) + " " + nguyenLieu.template.name);
                        }
                    } else {
                        this.thongoc.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                "Còn thiếu\n"
                                + Util.numberToMoney(COST_GIA_HAN_CAI_TRANG - player.inventory.ruby)
                                + " Ruby");
                    }
                } else {
                    this.thongoc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Ngọc bội đã đạt cấp tối đa", "Đóng");
                }
            } else {
                this.thongoc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Ta cần 1 ngọc bội và " + (level + 1) + " nguyên liệu đá thăng cấp", "Đóng");
            }
        } else {
            this.thongoc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Ta cần 1 ngọc bội và 1 đá thăng cấp", "Đóng");
        }
    }

    public void menu_THANG_HOA_NGOC_BOI(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item ngocBoi = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    for (int id : NGOC_BOI_IDS) {
                        if (item.template.id == id) {
                            ngocBoi = item;
                            break;
                        }
                    }
                }
            }
            if (ngocBoi != null) {
                String npcSay = "Trang bị " + "\"" + ngocBoi.template.name + "\"" + "\n"
                        + "|0|Nâng hoa giúp sử dụng Ngọc Bội\n"
                        + "Tỷ lệ thành công: 100%\n"
                        + "|2|Cần 10.000 ngọc xanh";
                if (player.inventory.gem >= COST_NANG_CAP_NGOC_BOI) {
                    this.thongoc.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Thăng hoa\nBản thân", "Thăng hoa\nĐệ tử");
                } else {
                    this.thongoc.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                            "Còn thiếu " + Util.numberToMoney(COST_NANG_CAP_NGOC_BOI - player.inventory.gem) + " Ngọc");
                }
            } else {
                this.thongoc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Ta cần một viên ngọc bội (Bạch ngọc, Hỏa ngọc, Kim ngọc, ...)! ", "Đóng");
            }
        } else {
            this.thongoc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Ta chỉ cần một viên ngọc bội!", "Đóng");
        }
    }

    private void menu_Gia_Han_Cai_Trang(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item caitrang = null, vegiahan = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 5) {
                        caitrang = item;
                    } else if (item.template.id == 2022) {
                        vegiahan = item;
                    }
                }
            }
            int expiredDate = 0;
            boolean canBeExtend = true;
            if (caitrang != null && vegiahan != null) {
                for (ItemOption io : caitrang.itemOptions) {
                    if (io.optionTemplate.id == 93) {
                        expiredDate = io.param;
                    }
                    if (io.optionTemplate.id == 199) {
                        canBeExtend = false;
                    }
                }
                if (canBeExtend) {
                    if (expiredDate > 0) {
                        String npcSay = "|2|" + caitrang.template.name + "\n"
                                + "Sau khi gia hạn +1 ngày \n Tỷ lệ thành công: 100% \n" + "|7|Cần 20 Ruby";
                        if (player.inventory.ruby >= COST_GIA_HAN_CAI_TRANG) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Gia hạn");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                    "Còn thiếu\n"
                                    + Util.numberToMoney(player.inventory.ruby - COST_GIA_HAN_CAI_TRANG)
                                    + " Ruby");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần cải trang có hạn sử dụng và thẻ gia hạn", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Vật phẩm này không thể gia hạn", "Đóng");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Ta Cần cải trang có hạn sử dụng và thẻ gia hạn", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Ta Cần cải trang có hạn sử dụng và thẻ gia hạn", "Đóng");
        }
    }

    private void menu_TRAO_DOI_XU_HADES(Player player) {
        String npcSay = "|0|Ngươi có muốn trao đổi";
        for (Item item : player.combineNew.itemsCombine) {
            if (item == null || !this.isDoThanLinh(item.template.id)) {
                this.thuongNhan.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Chỉ có thể trao đổi bằng những vật phẩm thần linh", "Đóng");
                return;
            }
            long gold = item.template.type == 4 ? 1500000000 : item.template.type == 2 ? 1000000000 : 500000000;
            int dongXu = item.template.type == 4 ? 3 : item.template.type == 2 ? 2 : 1;
            npcSay += "\n|2|" + item.template.name + " (cần " + Util.numberToMoney(gold)
                    + " vàng) (nhận được " + dongXu + " đồng xu )";
        }
        npcSay += "\n|1|Ngươi có đồng ý muốn trao đổi không?";
        this.thuongNhan.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                npcSay, "Đổi", "Từ chối");

    }

    private void menu_Nang_Cap_Do_Thien_Su(Player player) {
        if (player.combineNew.itemsCombine.size() > 1) {
            int ratioLuckyStone = 0, ratioRecipe = 0, ratioUpgradeStone = 0, countLuckyStone = 0,
                    countUpgradeStone = 0;
            Item angelClothes = null;
            Item craftingRecipe = null;
            for (Item item : player.combineNew.itemsCombine) {
                int id = item.template.id;
                if (item.isNotNullItem()) {
                    if (isAngelClothes(id)) {
                        angelClothes = item;
                    } else if (isLuckyStone(id)) {
                        ratioLuckyStone += getRatioLuckyStone(id);
                        countLuckyStone++;
                    } else if (isUpgradeStone(id)) {
                        ratioUpgradeStone += getRatioUpgradeStone(id);
                        countUpgradeStone++;
                    } else if (isCraftingRecipe(id)) {
                        ratioRecipe += getRatioCraftingRecipe(id);
                        craftingRecipe = item;
                    }
                }
            }
            if (angelClothes == null) {
                return;
            }
            boolean canUpgrade = true;
            for (ItemOption io : angelClothes.itemOptions) {
                int optId = io.optionTemplate.id;
                if (optId == 41) {
                    canUpgrade = false;
                }
            }
            if (angelClothes.template.gender != craftingRecipe.template.gender) {
                this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vui lòng chọn đúng công thức chế tạo",
                        "Đóng");
                return;
            }
            if (canUpgrade) {
                if (craftingRecipe != null) {
                    if (countLuckyStone < 2 && countUpgradeStone < 2) {
                        int ratioTotal = (20 + ratioUpgradeStone + ratioRecipe);
                        int ratio = ratioTotal > 75 ? ratio = 75 : ratioTotal;
                        String npcSay = "|1| Nâng cấp " + angelClothes.template.name + "\n|7|";
                        npcSay += ratioRecipe > 0 ? " Công thức VIP (+" + ratioRecipe + "% tỉ lệ thành công)\n"
                                : "";
                        npcSay += ratioUpgradeStone > 0
                                ? "Đá nâng cấp cấp " + ratioUpgradeStone / 10 + " (+" + ratioUpgradeStone
                                + "% tỉ lệ thành công)\n"
                                : "";
                        npcSay += ratioLuckyStone > 0
                                ? "Đá nâng may mắn cấp " + ratioLuckyStone / 10 + " (+" + ratioLuckyStone
                                + "% tỉ lệ tối đa các chỉ số)\n"
                                : "";
                        npcSay += "Tỉ lệ thành công: " + ratio + "%\n";
                        npcSay += "Phí nâng cấp: " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";
                        if (player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
                            this.whis.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp");
                        } else {
                            this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                                    "Còn thiếu\n"
                                    + Util.numberToMoney(player.inventory.gold - COST_DAP_DO_KICH_HOAT)
                                    + " vàng");
                        }
                    } else {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ có thể sự dụng tối đa 1 loại nâng cấp và đá may mắn", "Đóng");
                    }
                } else {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Người cần ít nhất 1 trang bị thiên sứ và 1 công thức để có thể nâng cấp", "Đóng");
                }
            } else {
                this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Mỗi vật phẩm chỉ có thể nâng cấp 1 lần", "Đóng");
            }
        } else {
            this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Người cần ít nhất 1 trang bị thiên sứ và 1 công thức để có thể nâng cấp", "Đóng");
        }
    }

    private void menu_Dap_Do_Thien_Su(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang không còn chỗ trống",
                        "Đóng");
                return;
            }
            Item dohd = null;
            Item da = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (isAngelClothes(item.template.id)) {
                        dohd = item;
                    } else if (isLuckyStone(item.template.id)) {
                        da = item;
                    }
                }
            }

            ItemOption HaveOpotionThienSu = null;
            for (ItemOption io : dohd.itemOptions) {
                switch (io.optionTemplate.id) {
                    case 5:
                        HaveOpotionThienSu = io;
                        break;
                    case 97:
                        HaveOpotionThienSu = io;
                        break;
                    case 201:
                        HaveOpotionThienSu = io;
                        break;
                    case 202:
                        HaveOpotionThienSu = io;
                        break;
                    case 203:
                        HaveOpotionThienSu = io;
                        break;
                    case 213:
                        HaveOpotionThienSu = io;
                        break;
                    case 214:
                        HaveOpotionThienSu = io;
                        break;
                    case 215:
                        HaveOpotionThienSu = io;
                        break;
                    case 216:
                        HaveOpotionThienSu = io;
                        break;
                }
            }

            if (HaveOpotionThienSu == null) {
                if (dohd != null && da != null) {
                    player.combineNew.goldCombine = 500_000_000;
                    player.combineNew.ratioCombine = getratiodamayma(da.template.id);
                    String text = dohd.template.name;
                    String npcSay = text + "\n|2|";
                    for (ItemOption io : dohd.itemOptions) {
                        npcSay += io.getOptionString() + "\n";
                    }
                    npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                    npcSay += "\n|1|Sau khi nâng cấp chỉ số sẽ tăng ngẫu nhiên từ 0-15%";
                    if (player.combineNew.goldCombine <= player.inventory.gold) {
                        bill.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp\ncần " + player.combineNew.goldCombine + " vàng ");
                    } else {
                        npcSay += "Còn thiếu "
                                + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                + " vàng";
                        bill.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                    }
                } else {
                    this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị Thiên sứ chưa nâng cấp chỉ số hay nâng cấp", "Đóng");
                }
            } else {
                this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần 1 trang bị Thiên sứ chưa nâng cấp chỉ số hay nâng cấp", "Đóng");
            }
        } else {
            this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 1 trang bị Thiên sứ chưa nâng cấp chỉ số hay nâng cấp", "Đóng");
        }
    }

    private void menu_Che_Tao_Bo_Keo_Kinh_Di(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item keobingo = null;
            Item keonaonguoi = null;
            Item daybuoc = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1297) {
                    keobingo = item;
                } else if (item.template.id == 1298) {
                    keonaonguoi = item;
                } else if (item.template.id == 1299) {
                    daybuoc = item;
                }
            }
            if (keobingo != null && keobingo.quantity >= 99
                    && keonaonguoi != null && keonaonguoi.quantity >= 99
                    && daybuoc != null) {

                player.combineNew.goldCombine = 200_000_000;
                player.combineNew.ratioCombine = RATIO_BONG_TAI;

                String npcSay = "|2|Chế tạo bó kẹo kinh dị" + "\n";
                npcSay += "|2|Tỉ lệ thành công: 50%  \n";
                if (player.combineNew.goldCombine <= player.inventory.gold) {
                    npcSay += "|2|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng\n";
                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Nâng cấp " + Util.numberToMoney(player.combineNew.goldCombine) + "vàng\n");
                } else {
                    npcSay += "Còn thiếu "
                            + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                            + " vàng";
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần x99 Kẹo bí ngô , x99 kẹo não người và x1 dây buộc", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần x99 Kẹo bí ngô , x99 kẹo não người và x1 dây buộc", "Đóng");

        }
    }

    private void menu_Che_Tao_Gio_Keo_Kinh_Di(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            Item keobingo = null;
            Item keonaonguoi = null;
            Item daybuoc = null;
            Item giodung = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1297) {
                    keobingo = item;
                } else if (item.template.id == 1298) {
                    keonaonguoi = item;
                } else if (item.template.id == 1299) {
                    daybuoc = item;
                } else if (item.template.id == 1305) {
                    giodung = item;
                }
            }
            if (keobingo != null && keobingo.quantity >= 99
                    && keonaonguoi != null && keonaonguoi.quantity >= 99
                    && daybuoc != null && giodung != null) {

                player.combineNew.ratioCombine = RATIO_BONG_TAI;

                String npcSay = "|2|Chế tạo giỏ kẹo kinh dị" + "\n";
                npcSay += "|2|Tỉ lệ thành công: 50%  \n";
                if (player.inventory.ruby > 1000) {
                    npcSay += "|2|Cần 1000 hồng ngọc\n";
                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Nâng cấp \n1000 hồng ngọc\n");
                } else {
                    npcSay += "Còn thiếu " + Util.numberToMoney(1000 - player.inventory.ruby) + " hồng ngọc";
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần x99 Kẹo bí ngô , x99 kẹo não người , x1 dây buộc và x1 giỏ đựng", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần x99 Kẹo bí ngô , x99 kẹo não người , x1 dây buộc và x1 giỏ đựng", "Đóng");

        }
    }

    private void menu_Tach_Do_Huy_Diet(Player player) {
        Item tvKhoa = InventoryService.gI().findItemBag(player, 1429);
        if (player.combineNew.itemsCombine.size() > 0 && player.combineNew.itemsCombine.size() <= 5) {
            String npcSay = "Ngươi có chắc chắn chuyển hóa đổi\n|2|";
            int count_do_than = 0;
            for (int i = 0; i < player.combineNew.itemsCombine.size(); i++) {
                Item item = player.combineNew.itemsCombine.get(i);
                if (item.isNotNullItem() && item.isDTL()) {
                    npcSay += item.template.name + "\n";
                    count_do_than++;
                }
            }
            npcSay += "lấy\n";
            for (int i = 0; i < player.combineNew.itemsCombine.size(); i++) {
                short id = (short) 674;
                Item item = ItemService.gI().createNewItem(id);
                npcSay += "x2 " + item.template.name + "\n";
                count_do_than++;
            }
            if (count_do_than >= 1) {
                npcSay += "Phù phép mỗi lần sẽ cần "
                        + Util.numberToMoney(COST_DOI_VE_DOI_DO_HUY_DIET) + "  thoi vàng";
                if (tvKhoa.quantity >= (COST_DOI_VE_DOI_DO_HUY_DIET * count_do_than)) {
                    this.whis.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Chuyển hóa", "Từ chối");
                } else {
                    npcSay += "|7|Còn thiếu "
                            + Util.numberToMoney(((long) (COST_DOI_VE_DOI_DO_HUY_DIET * count_do_than)
                                    - tvKhoa.quantity))
                            + " Thoi vàng";
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                            "Đóng");
                }
            } else {
                this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 đến 5 trang bị thần linh ngươi muốn chuyển hóa", "Đóng");
            }
        } else {
            this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 đến 5 trang bị thần linh ngươi muốn chuyển hóa", "Đóng");
        }
    }

    private void menu_Ghep_cai_trang_2(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            Item caiTrang_1 = null;
            Item caiTrang_2 = null;
            Item BTC2 = null;
            Item Da = null;
            Item tv = InventoryService.gI().findItemBag(player, 1429);
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 2041) {
                    caiTrang_1 = item;
                } else if (item.template.id == 898) {
                    caiTrang_2 = item;
                } else if (item.template.id == 921) {
                    BTC2 = item;
                } else if (item.template.id == 1345) {
                    Da = item;
                }
            }
            if (caiTrang_1 != null && caiTrang_1.quantity >= 1
                    && caiTrang_2 != null && caiTrang_2.quantity >= 1
                    && BTC2 != null
                    && Da != null && Da.quantity >= 10) {
                boolean countIOBTC2 = false;
                for (ItemOption io : BTC2.itemOptions) {
                    if (io.optionTemplate.id == 5 || io.optionTemplate.id == 14 || io.optionTemplate.id == 50
                            || io.optionTemplate.id == 77 || io.optionTemplate.id == 80 || io.optionTemplate.id == 81
                            || io.optionTemplate.id == 94 || io.optionTemplate.id == 101 || io.optionTemplate.id == 103
                            || io.optionTemplate.id == 108) {
                        countIOBTC2 = true;
                        break;
                    }
                }
                if (!countIOBTC2) {
                    this.fu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Yêu cầu bông tai cấp 2 có chỉ số",
                            "Đóng");
                    return;
                }

                player.combineNew.ratioCombine = 50;

                String npcSay = "|2|Ghép cải trang" + "\n";
                npcSay += "|2|Tỉ lệ thành công: 50%  \n";
                if (tv.quantity >= 5_000) {
                    npcSay += "|2|Cần 5K TV\n";
                    fu.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Nâng cấp", "từ chối");
                } else {
                    npcSay += "Còn thiếu 5K TV";
                    fu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                }
            } else {
                this.fu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần Cải trang Super Black Goku , Cải trang Zamasu, Bông Tai Cấp 2, x10 đá thiên thần", "Đóng");
            }
        } else {
            this.fu.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần Cải trang Super Black Goku , Cải trang Zamasu, Bông Tai Cấp 2, x10 đá thiên thần", "Đóng");

        }
    }

//    private void menu_Bong_Toi_Trang_Bi(Player player) {
//        if (player.combineNew.itemsCombine.size() == 2) {
//            if (player.inventory.gold < 500_000_000) {
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con cần thêm "
//                        + (Util.numberToMoney(500000000L - player.inventory.gold)) + " vàng, nạp thêm đy con",
//                        "Đóng");
//                return;
//            }
//            Item itemDoBongToi = null;
//            Item itemDaBongToi = null;
//            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
//                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
//                    if (player.combineNew.itemsCombine.get(j).template.type == 5
//                            || player.combineNew.itemsCombine.get(j).template.type == 11
//                            || player.combineNew.itemsCombine.get(j).template.type == 72) {
//                        itemDoBongToi = player.combineNew.itemsCombine.get(j);
//                    } else if (player.combineNew.itemsCombine.get(j).template.id >= 1308
//                            && player.combineNew.itemsCombine.get(j).template.id <= 1312) {
//                        itemDaBongToi = player.combineNew.itemsCombine.get(j);
//                    }
//                }
//            }
//            if (itemDoBongToi != null) {
//                int level = 0;
//                for (ItemOption io : itemDoBongToi.itemOptions) {
//                    if (io.optionTemplate.id == 218) {
//                        level = io.param;
//                        break;
//                    }
//                }
//                if (CheckNangCapBongToi(itemDoBongToi, itemDaBongToi)
//                        && itemDaBongToi != null && itemDaBongToi.quantity > getCountDaNangCapDo(level)) {
//                    if (level < MAX_LEVEL_ITEM) {
//                        player.combineNew.ratioCombine = getRatioBongToiTrangBi(level);
//                        int sl = getCountDaNangCapDo(level);
//                        String nameda = CheckNameDaBongToi(itemDoBongToi);
//                        String npcSay = "|7|Trang bị nâng cấp bóng tối \"" + itemDoBongToi.template.name
//                                + "\"\n"
//                                + "|0|Sau khi nâng cấp được thêm\n ngẫu nhiên chỉ số bóng tối HP, KI hoặc SD\n"
//                                + "Tỷ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
//                                + "|2|Cần 500 Triệu vàng và " + sl + " " + nameda;
//                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
//                                npcSay, "Nâng cấp", "Từ chối");
//
//                    } else {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Trang bị của ngươi đã đạt bóng tối cấp thượng cổ", "Đóng");
//                    }
//                } else {
//                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                            "Hãy chọn 1 trang bị và đủ số lượng đá " + CheckNameDaBongToi(itemDoBongToi),
//                            "Đóng");
//                }
//            } else {
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                        "Hãy chọn 1 trang bị và đủ số lượng đá ", "Đóng");
//            }
//        } else {
//            if (player.combineNew.itemsCombine.size() > 3) {
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
//                return;
//            }
//            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                    "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
//        }
//    }

    private void menu_Bong_Toi_Trang_Bi(Player player) {
    if (player.combineNew.itemsCombine.size() == 2) {
        if (player.inventory.gold < 500_000_000) {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con cần thêm "
                    + (Util.numberToMoney(500000000L - player.inventory.gold)) + " vàng, nạp thêm đy con",
                    "Đóng");
            return;
        }
        Item itemDoBongToi = null;
        Item itemDaBongToi = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                if (item.template.type == 5 || item.template.type == 11 || item.template.type == 98) {
                    itemDoBongToi = item;
                }
                else if (item.template.id == 1308) {
                    itemDaBongToi = item;
                }
            }
        }
        if (itemDoBongToi != null) {
            int level = 0;
            for (ItemOption io : itemDoBongToi.itemOptions) {
                if (io.optionTemplate.id == 218) {
                    level = io.param;
                    break;
                }
            }
            if (itemDaBongToi != null && CheckNangCapBongToi(itemDoBongToi, itemDaBongToi)
                    && itemDaBongToi.quantity >= getCountDaNangCapBongToi(level)) {
                if (level < MAX_LEVEL_ITEM) {
                    player.combineNew.ratioCombine = getRatioBongToiTrangBi(level);
                    int sl = getCountDaNangCapBongToi(level);
                    String nameda = CheckNameDaBongToi(itemDoBongToi);
                    String npcSay = "|7|Trang bị nâng cấp bóng tối \"" + itemDoBongToi.template.name + "\"\n"
                            + "|0|Sau khi nâng cấp được thêm\n ngẫu nhiên chỉ số pháp sư HP, KI hoặc SD\n"
                            + "Tỷ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                            + "|2|Cần 500 Triệu vàng và " + sl + " " + nameda;
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Trang bị của ngươi đã đạt pháp sư cấp thượng cổ", "Đóng");
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Hãy chọn 1 trang bị và đủ số lượng đá pháp sư ", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy chọn 1 trang bị và 20 viên đá pháp sư ", "Đóng");
        }
    } else {
        if (player.combineNew.itemsCombine.size() > 3) {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất bớt đồ đi con, ta không thèm", "Đóng");
            return;
        }
        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Hãy chọn 1 trang bị và 20 viên đá pháp sư ", "Đóng");
    }
}

    private void menu_Delete_Option_Thien_Su(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.combineNew.itemsCombine.size() == 1) {
                Item dothiensu = null;
                for (Item item_ : player.combineNew.itemsCombine) {
                    if (item_.template.id >= 1048 && item_.template.id <= 1062) {
                        dothiensu = item_;
                    }
                }
                if (player.inventory.gold < 500_000_000L) {
                    this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nghèo quá con ơi!", "Đóng");
                    return;
                }
                if (dothiensu != null) {
                    short[] listOp = {216, 97, 213, 214, 215, 5, 201, 202, 203};
                    ItemOption HaveOpotionThienSu = null;
                    for (ItemOption io : dothiensu.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 5:
                                HaveOpotionThienSu = io;
                                break;
                            case 97:
                                HaveOpotionThienSu = io;
                                break;
                            case 201:
                                HaveOpotionThienSu = io;
                                break;
                            case 202:
                                HaveOpotionThienSu = io;
                                break;
                            case 203:
                                HaveOpotionThienSu = io;
                                break;
                            case 213:
                                HaveOpotionThienSu = io;
                                break;
                            case 214:
                                HaveOpotionThienSu = io;
                                break;
                            case 215:
                                HaveOpotionThienSu = io;
                                break;
                            case 216:
                                HaveOpotionThienSu = io;
                                break;
                        }
                    }
                    if (HaveOpotionThienSu != null) {
                        String npcSay = "Trang bị được xóa phù hộ Thiên sứ \"" + dothiensu.template.name
                                + "\"\n"
                                + "|0|Tỉ lệ thành công: 100%\n"
                                + "|2|Cần 500 triệu vàng";
                        this.bill.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Xóa ngay", "Từ chối");
                    } else {
                        this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Trang bị Thiên sứ đã nâng cấp mới có thể xóa dữ liệu nâng cấp!", "Đóng");
                    }
                } else {
                    this.bill.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Yêu cầu trang bị Thiên sứ!",
                            "Đóng!");
                }
            }

        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
        }
    }

    private void bongToiX10(Player player) {
        for (int i = 0; i < 500; i++) { // số lần pha lê hóa
            if (!nangCapBongToi(player)) {
                Service.getInstance().sendThongBao(player,
                        "Bóng tối hóa tự động dừng lại lần đập " + (i + 1));
                break;
            }
        }
    }

    private void NangCapVatPhamX10(Player player) {
        for (int i = 0; i < 100; i++) { // số lần pha lê hóa
            if (!nangCapVatPham(player)) {
                Service.getInstance().sendThongBao(player,
                        "Nâng cấp vật phẩm tự động dừng lại lần đập " + (i + 1));
                break;
            }
        }
    }

    private boolean nangCapBongToi(Player player) {
        try {
            boolean isSusces = false;
            TransactionService.gI().cancelTrade(player);
            if (player.combineNew.itemsCombine.size() == 2) {
                if (isCoupleBongToi(player.combineNew.itemsCombine.get(0),
                        player.combineNew.itemsCombine.get(1))) {
                    Item trangBi = null;
                    Item daNangCap = null;
                    if (isTypeBongToi(player.combineNew.itemsCombine.get(0))) {
                        trangBi = player.combineNew.itemsCombine.get(0);
                        daNangCap = player.combineNew.itemsCombine.get(1);
                    } else {
                        trangBi = player.combineNew.itemsCombine.get(1);
                        daNangCap = player.combineNew.itemsCombine.get(0);
                    }

                    int countDaNangCap = player.combineNew.countDaNangCap;
                    long gold = player.combineNew.goldCombine;
                    int level = 0;
                    ItemOption optionLevel = null;

                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 222) {
                            level = io.param;
                            optionLevel = io;
                            break;
                        }
                    }
                    ItemOption optionNangCap_1 = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 219) {
                            optionNangCap_1 = io;
                            break;
                        }
                    }
                    ItemOption optionNangCap_2 = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 220) {
                            optionNangCap_2 = io;
                            break;
                        }
                    }
                    ItemOption optionNangCap_3 = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 221) {
                            optionNangCap_3 = io;
                            break;
                        }
                    }
                    if (player.inventory.gold < gold) {
                        Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                        return false;
                    }
                    if (daNangCap.quantity < countDaNangCap) {
                        Service.getInstance().sendThongBao(player, "Không đủ Ngọc pháp sư");
                        return false;
                    }
                    if (level < MAX_LEVEL_ITEM) {
                        player.inventory.gold -= gold;
                        float ratio = player.combineNew.ratioCombine;
                        if (Util.isTrue(ratio, 130)) {
                            isSusces = true;
                            if (optionLevel == null) {
                                trangBi.itemOptions.add(new ItemOption(222, 1));
                            } else {
                                optionLevel.param++;
                            }

                            int param_level = getParamBongToiInLevel(level);
                            int random_option = Util.nextInt(0, 2);
                            if (random_option == 0) { // 198 Bóng tối Ma vương +#% sức đánh
                                if (optionNangCap_1 == null) {
                                    trangBi.itemOptions.add(new ItemOption(219, param_level));
                                } else {
                                    optionNangCap_1.param = param_level;
                                }
                            } else if (random_option == 1) {// 199 Bóng tối Quỷ vương +#% sức đánh
                                if (optionNangCap_2 == null) {
                                    trangBi.itemOptions.add(new ItemOption(220, param_level));
                                } else {
                                    optionNangCap_2.param = param_level;
                                }
                            } else if (random_option == 2) {// 200 Bóng tối Luyện ngục +#% sức đánh
                                if (optionNangCap_3 == null) {
                                    trangBi.itemOptions.add(new ItemOption(221, param_level));
                                } else {
                                    optionNangCap_3.param = param_level;
                                }
                            } else {
                                Service.getInstance().sendThongBao(player, "Có lỗi xảy ra. vui lòng báo cho Admin");
                            }

                            if (optionLevel != null && optionLevel.param >= 6) {
                                ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp pháp sư "
                                        + "thành công " + trangBi.template.name + " lên +" + optionLevel.param);
                            }
                            sendEffectSuccessCombine(player);
                            Service.getInstance().sendThongBao(player, "Nâng cấp thành công");
                        } else {
                            sendEffectFailCombine(player);
                            Service.getInstance().sendThongBao(player, "Nâng cấp thất bại");
                        }

                        InventoryService.gI().subQuantityItemsBag(player, daNangCap,
                                player.combineNew.countDaNangCap);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        reOpenItemCombine(player);
                        if (isSusces) {
                            return false;

                        } else {
                            return true;
                        }

                    } else {
                        Service.getInstance().sendThongBao(player, "Trang bị đã đạt cấp tối đa");
                        return false;
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Cần phụ kiện và Ngọc pháp sư");
                    return false;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Cần phụ kiện và Ngọc pháp sư");
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    private void nangCapBongTai_3(Player player) {
    if (player.combineNew.itemsCombine.size() == 2) {
        Item tvKhoa = InventoryService.gI().findItemBag(player, 1429);
        if (tvKhoa == null || tvKhoa.quantity < 50000) {
            Service.getInstance().sendThongBao(player, "Cần 50K TV khóa để nâng cấp!");
            return;
        }
        Item bongTai = null;
        Item manhBongTai = null;
        for (Item it : player.combineNew.itemsCombine) {
            if (it.getId() == 1601) {
                manhBongTai = it;
            }
            if (it.getId() == 921) {
                bongTai = it;
            }
        }
        if (bongTai != null && manhBongTai != null && manhBongTai.quantity >= 999) {
            bongTai.template = ItemService.gI().getTemplate(1451);
            CombineServiceNew.gI().sendEffectOpenItem(player, (short) 3896, (short) 22593);
            InventoryService.gI().subQuantityItemsBag(player, manhBongTai, 999);
            InventoryService.gI().subQuantityItemsBag(player, tvKhoa, 50000);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);

            reOpenItemCombine(player);
        }
    }
}

    private void nangCapChiSoBongTai_3(Player player) {
    if (player.combineNew.itemsCombine.size() == 3) {
        Item bongTai = null;
        Item manhHon = null;
        Item Xlam = null;
        for (Item it : player.combineNew.itemsCombine) {
            if (it.getId() == 1451) bongTai = it;
            if (it.getId() == 1602) manhHon = it;
            if (it.getId() == 1603) Xlam = it;
        }
        if (bongTai != null && manhHon != null && Xlam != null
                && manhHon.quantity >= 99 && Xlam.quantity >= 5) {
            Item tvKhoa = InventoryService.gI().findItemBag(player, 1429);
            if (tvKhoa == null || tvKhoa.quantity < 5000) {
                Service.getInstance().sendThongBao(player, "Bạn cần 5.000 TV khóa để nâng cấp!");
                return;
            }
            if (Util.isTrue(37, 100)) {
                List<ItemOption> listRemove = new ArrayList<>();
                if (bongTai.itemOptions != null) {
                    for (ItemOption ios : bongTai.itemOptions) {
                        if (IsChiSoBongTai(ios.optionTemplate.id)) {
                            listRemove.add(ios);
                        }
                    }
                }
                for (ItemOption op : listRemove) {
                    bongTai.itemOptions.remove(op);
                }
                int listChiSo[] = {5, 14, 50, 77, 80, 81, 94, 101, 103, 108};
                int idChiSo = listChiSo[Util.nextInt(listChiSo.length)];
                bongTai.itemOptions.add(new ItemOption(idChiSo, Util.nextInt(10, 20)));

                CombineServiceNew.gI().sendEffectOpenItem(player, bongTai.template.iconID, bongTai.template.iconID);
                Service.getInstance().sendThongBao(player, "Bạn vừa nâng cấp thành công " + bongTai.template.name);
            } else {
                CombineServiceNew.gI().sendEffectFailCombine(player);
                Service.getInstance().sendThongBao(player, "Nâng cấp thất bại");
            }
            InventoryService.gI().subQuantityItemsBag(player, tvKhoa, 5000);
            InventoryService.gI().subQuantityItemsBag(player, manhHon, 99);
            InventoryService.gI().subQuantityItemsBag(player, Xlam, 5);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        }
    }
}
    
    private void nangCapBongTai_4(Player player) {

        if (player.combineNew.itemsCombine.size() == 2) {
            if (player.inventory.ruby < 500) { // bông tai
                return;
            }
            Item bongTai = null;
            Item manhBongTai = null;

            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == 1609) {
                    manhBongTai = it;
                }
                if (it.getId() == 1451) {
                    bongTai = it;
                }
            }
            if (bongTai != null && manhBongTai != null && manhBongTai.quantity >= 999) {

                bongTai.template = ItemService.gI().getTemplate(1608);

                CombineServiceNew.gI().sendEffectOpenItem(player, (short) 3896, (short) 22593);

                InventoryService.gI().subQuantityItemsBag(player, manhBongTai, 999);

                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);

                player.inventory.ruby -= 500;

                reOpenItemCombine(player);
            }

        }
    }

    private void nangCapChiSoBongTai_4(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item bongTai = null;
            Item manhHon = null;
            Item Xlam = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.getId() == 1608) {
                    bongTai = it;
                }
                if (it.getId() == 1610) {
                    manhHon = it;
                }
                if (it.getId() == 1611) {
                    Xlam = it;
                }
            }
            if (bongTai != null && manhHon != null && Xlam != null
                    && manhHon.quantity >= 99 && Xlam.quantity >= 5) {
                if (Util.isTrue(30, 100)) {
                    List<ItemOption> listRemove = new ArrayList<>();
                    if (bongTai.itemOptions != null) {
                        for (ItemOption ios : bongTai.itemOptions) {
                            if (IsChiSoBongTai(ios.optionTemplate.id)) {
                                listRemove.add(ios);
                            }
                        }
                    }

                    for (ItemOption op : listRemove) {
                        bongTai.itemOptions.remove(op);
                    }
                    int listChiSo[] = {5, 14, 50, 77, 80, 81, 94, 101, 103, 108};
                    int idChiSo = listChiSo[Util.nextInt(listChiSo.length)];

                    bongTai.itemOptions.add(new ItemOption(idChiSo, Util.nextInt(3, 15)));
                    CombineServiceNew.gI().sendEffectOpenItem(player, bongTai.template.iconID, bongTai.template.iconID);
                    Service.getInstance().sendThongBao(player, "Bạn vừa nâng cấp thành công " + bongTai.template.name);
                } else {
                    CombineServiceNew.gI().sendEffectFailCombine(player);
                    Service.getInstance().sendThongBao(player, "Nâng cấp thất bại");
                }

                player.inventory.ruby -= 1000;
                InventoryService.gI().subQuantityItemsBag(player, manhHon, 99);
                InventoryService.gI().subQuantityItemsBag(player, Xlam, 5);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void thanhTayBongToi(Player player) {
        try {
            if (player.combineNew.itemsCombine.size() == 2) {
                if (isCoupleTayBongToi(player.combineNew.itemsCombine.get(0),
                        player.combineNew.itemsCombine.get(1))) {
                    Item trangBi = null;
                    Item daNangCap = null;
                    if (isTypeBongToi(player.combineNew.itemsCombine.get(0))) {
                        trangBi = player.combineNew.itemsCombine.get(0);
                        daNangCap = player.combineNew.itemsCombine.get(1);
                    } else {
                        trangBi = player.combineNew.itemsCombine.get(1);
                        daNangCap = player.combineNew.itemsCombine.get(0);
                    }

                    int countDaNangCap = player.combineNew.countDaNangCap;
                    long gold = player.combineNew.goldCombine;
                    int level = 0;

                    if (player.inventory.gold < gold) {
                        Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                        return;
                    }
                    if (daNangCap.quantity < countDaNangCap) {
                        Service.getInstance().sendThongBao(player, "Không đủ đá tẩy");
                        return;
                    }
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 222) {
                            level = io.param;
                            break;
                        }
                    }
                    if (level > 0) {

                        player.inventory.gold -= gold;
                        List<ItemOption> Io_List_Remove = new ArrayList<>();

                        for (ItemOption op : trangBi.itemOptions) {
                            if (op.optionTemplate.id == 222 || isChiSoBongToi(op.optionTemplate.id)) {
                                Io_List_Remove.add(op);
                            }
                        }
                        for (ItemOption op : Io_List_Remove) {
                            trangBi.itemOptions.remove(op);
                        }
                        Service.getInstance().sendThongBao(player, "Thanh tẩy trang bị thành công");
                        InventoryService.gI().subQuantityItemsBag(player, daNangCap,
                                player.combineNew.countDaNangCap);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        sendEffectSuccessCombine(player);
                        reOpenItemCombine(player);

                    } else {
                        Service.getInstance().sendThongBao(player, "Trang bị không có chỉ số pháp sư để tẩy");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Cần trang bị và đá ánh sáng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Cần trang bị và đá ánh sáng");
                return;
            }

        } catch (Exception e) {
            Logger.warning("Loi tay phu kien");
        }
    }

    private void menu_Delete_Bong_Toi_Trang_Bi(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.combineNew.itemsCombine.size() == 2) {
                Item Dalight = null;
                Item ItemBongtoi = null;
                for (Item item_ : player.combineNew.itemsCombine) {
                    if (item_.template.id == 1311) {
                        Dalight = item_;
                    } else if (item_.isTrangBiHacHoa()) {
                        ItemBongtoi = item_;
                    }
                }

                if (Dalight != null && ItemBongtoi != null) {
                    int optbongtoi = 0;
                    for (ItemOption io : ItemBongtoi.itemOptions) {
                        if (io.optionTemplate.id == 219
                                || io.optionTemplate.id == 220
                                || io.optionTemplate.id == 221) {
                            optbongtoi = 1;
                        }
                    }
                    if (optbongtoi == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần có ít nhất 1 Đá áng sánh và 1 trang Bị đã bóng tối", "Đóng");
                        return;
                    }
                    String npcSay = "Trang bị được xóa dòng bóng tối \"" + ItemBongtoi.template.name + "\"\n"
                            + "|0|Tỉ lệ thành công: 100%\n"
                            + "|2|Cần 0 vàng";

                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Xóa ngay", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần có ít nhất 1 Đá áng sánh và 1 trang Bị đã bóng tối", "Đóng");
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Cần có trang bị Pháp sư hóa");
            }
        } else {
            Service.getInstance().sendThongBaoOK(player, "Không đủ hành trang");
        }

    }

    private void menu_che_bien_tra_hoa_cuc(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            Item tratuoi = null;
            Item niatre = null;
            Item quetre = null;
            Item hoacuc = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1328) {
                    tratuoi = item;
                } else if (item.template.id == 1329) {
                    niatre = item;
                } else if (item.template.id == 1330) {
                    quetre = item;
                } else if (item.template.id == 1332) {
                    hoacuc = item;
                }
            }
            if (tratuoi != null && tratuoi.quantity >= 99
                    && niatre != null && niatre.quantity >= 99
                    && quetre != null && quetre.quantity >= 99
                    && hoacuc != null) {
                player.combineNew.goldCombine = 50_000_000;
                player.combineNew.ratioCombine = 50f;
                String npcSay = "|2|Chế biến hộp trà hoa cúc";
                npcSay += "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine;
                npcSay += "\n|6| Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                        "Chế biến\nngay");
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Cần x99 Lá trà tươi , x99 Nia tre , x99 Que tre và Hoa cúc", "Đóng");
            }
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần x99 Lá trà tươi , x99 Nia tre , x99 Que tre và Hoa cúc", "Đóng");
        }
    }

    public void DoidoThienSu(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 1) {
            if (player.setClothes.huydietClothers == 5) {
                Item mts = null;
                Item da = null;
                Item cT = null;
                Item dmm = null;

                if (player.combineNew.itemsCombine.size() <= 4) {
                    for (Item it : player.combineNew.itemsCombine) {
                        if (it.isMTS()) {
                            mts = it;
                        }
                        if (it.isCT()) {
                            cT = it;
                        }
                        if (it.isDANANGCAP()) {
                            da = it;
                        }
                        if (it.isDMM()) {
                            dmm = it;
                        }
                    }
                    if (mts != null && mts.quantity >= 999 && cT != null) {
                        if (InventoryService.gI().getQuantity(player, 1429) > 5000) {
                            String Npcsay = "|1|Chế tạo " + getTypeTrangBi(mts.getType()) + " Thiên Sứ " + getGenderTrangBi(cT.getType())
                                    + "\n Mạnh hơn trang bị Hủy Diệt từ 20% đến 35%"
                                    + "\n|2|Mảnh ghép " + mts.quantity + "/999 (Thất bại -99 mảnh ghép)";
                            if (da != null) {
                                Npcsay += "\n" + da.getName() + " (Thêm " + (getRateThienSu(da.getId()) * 10) + " tỉ lệ thành công)";
                            }
                            if (dmm != null) {
                                Npcsay += "\n" + dmm.getName() + " (Thêm " + (getRateThienSu(dmm.getId()) * 10) + " tỉ lệ có chỉ số thưởng)";
                            }

                            Npcsay += "\nTỉ lệ thành công: " + (da != null ? ((getRateThienSu(da.getId()) * 10) + 35) : "35") + "%";

                            Npcsay += "\n|1|Phí nâng cấp: 5K TVK";

                            this.whis.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, Npcsay, "Đồng ý", "Từ chối");
                        } else {
                            this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ 5K TVK trong hành trang !", "Đóng");
                        }
                    } else {
                        this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy bỏ vào đủ X99 Mảnh thiên sứ,"
                                + "X1 Đá nâng cấp , Công thức và 1 loại Đá may mắn !", "Đóng");
                    }

                } else {
                    this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chỉ bỏ vào Mảnh thiên sứ và 1 loại đá nâng cấp !", "Đóng");
                }

            } else {
                this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy trang bị đủ 5 trang bị Hủy diệt "
                        + "trước khi đổi !", "Đóng");
            }
        } else {
            this.whis.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chừa 1 chỗ trống trong hành trang !", "Đóng");
        }
    }

    int getRateThienSu(int id) {
        switch (id) {
            case 1074:
            case 1080:
                return 1;
            case 1075:
            case 1081:
                return 2;
            case 1076:
            case 1082:
                return 3;
            case 1077:
            case 1083:
                return 4;
            case 1078:
            case 1084:
                return 5;
            default:
                return 0;
        }
    }

    private boolean isTrangBiAn(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type <= 5) {
                // if (item.template.id >= 650 && item.template.id <= 662) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

//    public void RUnDoiDoThienSu(Player player) {
//        if (player.setClothes.huydietClothers == 5) {
//            Item mts = null;
//            Item da = null;
//            Item cT = null;
//            Item dmm = null;
//
//            if (player.combineNew.itemsCombine.size() <= 4) {
//                for (Item it : player.combineNew.itemsCombine) {
//                    if (it.isMTS()) {
//                        mts = it;
//                    }
//                    if (it.isCT()) {
//                        cT = it;
//                    }
//                    if (it.isDANANGCAP()) {
//                        da = it;
//                    }
//                    if (it.isDMM()) {
//                        dmm = it;
//                    }
//                }
//
//                if (mts != null && mts.quantity >= 999 && cT != null) {
//                    if (InventoryService.gI().getQuantity(player, 1429) > 5000) {
//
//                        Item it = new Item().GetOptionItem(ConstItem.itemids_TS[mts.template.gender][cT.template.gender]);
//
//                        int rate = da != null ? ((getRateThienSu(da.getId()) * 10) + 35) : 35;
//
//                        if (Util.isTrue(rate, 100)) {
//
//                            int opt = -1;
//                            if (Util.isTrue(30, 100)) {
//                                opt = Util.nextInt(15);
//                            } else {
//                                opt = Util.nextInt(7);
//                            }
//
//                            for (ItemOption io : it.itemOptions) {
//                                if (io != null) {
//                                    switch (io.optionTemplate.id) {
//                                        case 47:
//                                        case 22:
//                                        case 0:
//                                        case 23:
//                                        case 14:
//                                            io.param += io.param * (opt / 100);
//                                            break;
//                                    }
//                                }
//                            }
//
//                            if (dmm != null) {
//                                if (Util.isTrue(getRateThienSu(dmm.getId()) * 10, 100)) {
//                                    int numSoThuong = Util.nextInt(3);
//                                    if (numSoThuong > 0) {
//                                        int list[] = {50, 77, 103, 5, 94, 108};
//                                        it.itemOptions.add(new ItemOption(41, numSoThuong));
//                                        for (int i = 0; i < numSoThuong; i++) {
//                                            int rd = list[Util.nextInt(list.length - 1)];
//                                            int pr = -1;
//                                            if (Util.isTrue(30, 100)) {
//                                                pr = Util.nextInt(1, 5);
//                                            } else {
//                                                pr = Util.nextInt(1, 3);
//                                            }
//                                            it.itemOptions.add(new ItemOption(rd, pr));
//                                        }
//                                    }
//                                }
//                            }
//
//                            InventoryService.gI().addItemBag(player, it);
//                            InventoryService.gI().subQuantityItemsBag(player, mts, 999);
//
//                            sendEffectSuccessCombine(player);
//                        } else {
//                            InventoryService.gI().subQuantityItemsBag(player, mts, 99);
//
//                            sendEffectFailCombine(player);
//                        }
//
//                        if (da != null) {
//                            InventoryService.gI().subQuantityItemsBag(player, da, 1);
//                        }
//                        if (dmm != null) {
//                            InventoryService.gI().subQuantityItemsBag(player, dmm, 1);
//                        }
//
//                        InventoryService.gI().subQuantityItemsBag(player, (short) 1429, 5000);
//
//                        InventoryService.gI().subQuantityItemsBag(player, cT, 1);
//
//                        InventoryService.gI().sendItemBags(player);
//                        reOpenItemCombine(player);
//
//                    }
//                }
//
//            }
//        }
//    }
    public void RUnDoiDoThienSu(Player player) {
    if (player.setClothes.huydietClothers != 5) {
        Service.getInstance().sendThongBao(player, "Cần mặc đủ 5 món Hủy Diệt!");
        return;
    }

    Item mts = null, da = null, ct = null, dmm = null;

    if (player.combineNew.itemsCombine.size() > 4) {
        Service.getInstance().sendThongBao(player, "Nguyên liệu không hợp lệ!");
        return;
    }

    for (Item it : player.combineNew.itemsCombine) {
        if (it.isMTS()) mts = it;
        else if (it.isCT()) ct = it;
        else if (it.isDANANGCAP()) da = it;
        else if (it.isDMM()) dmm = it;
    }

    if (mts == null || mts.quantity < 999 || ct == null) {
        Service.getInstance().sendThongBao(player, "Thiếu Mảnh Thiên Sứ hoặc Chân Thân!");
        return;
    }

    if (InventoryService.gI().getQuantity(player, 1429) < 5000) {
        Service.getInstance().sendThongBao(player, "Không Thỏi vàng khóa!");
        return;
    }

    int rate = da != null ? (getRateThienSu(da.getId()) * 10 + 35) : 35;

    if (Util.isTrue(rate, 100)) {
        Item itemTS = new Item()
                .GetOptionItem(ConstItem.itemids_TS[mts.template.type][ct.template.gender]);

        /* ===== TĂNG CHỈ SỐ ===== */
        int opt = Util.isTrue(30, 100) ? Util.nextInt(15) : Util.nextInt(7);
        for (ItemOption io : itemTS.itemOptions) {
            if (io == null) continue;
             if (io.optionTemplate.id == 21) {
            io.param = 100;
            continue;
            }
            switch (io.optionTemplate.id) {
                case 47:
                case 22:
                case 0:
                case 23:
                case 14:
                    io.param += io.param * opt / 100;
                    break;
            }
        }

        /* ===== OPTION DMM ===== */
        if (dmm != null && Util.isTrue(getRateThienSu(dmm.getId()) * 10, 100)) {
            int soThuong = Util.nextInt(3);
            if (soThuong > 0) {
                int[] list = {50, 77, 103, 5, 94, 108};
                itemTS.itemOptions.add(new ItemOption(41, soThuong));
                for (int i = 0; i < soThuong; i++) {
                    int rd = list[Util.nextInt(list.length)];
                    int pr = Util.isTrue(30, 100)
                            ? Util.nextInt(1, 5)
                            : Util.nextInt(1, 3);
                    itemTS.itemOptions.add(new ItemOption(rd, pr));
                }
            }
        }

        InventoryService.gI().addItemBag(player, itemTS);
        InventoryService.gI().subQuantityItemsBag(player, mts, 999);

        sendEffectSuccessCombine(player);
        Service.getInstance().sendThongBao(player, " Ép đồ Thiên Sứ thành công!");
    } else {
        InventoryService.gI().subQuantityItemsBag(player, mts, 99);

        sendEffectFailCombine(player);
        Service.getInstance().sendThongBao(player, " Ép đồ Thiên Sứ thất bại!");
    }

    if (da != null) InventoryService.gI().subQuantityItemsBag(player, da, 1);
    if (dmm != null) InventoryService.gI().subQuantityItemsBag(player, dmm, 1);

    InventoryService.gI().subQuantityItemsBag(player, (short) 1429, 5000);
    InventoryService.gI().subQuantityItemsBag(player, ct, 1);

    InventoryService.gI().sendItemBags(player);
    reOpenItemCombine(player);
}


    public void menu_EP_SAO_LINH_THU(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item spl = null;
            Item lthu = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.isLTHUNANGCAP()) {
                    lthu = it;
                }
                if (it.isTinhThachNangCap()) {
                    spl = it;
                }
            }
            if (spl != null && lthu != null) {
                int star = 0; // sao pha lê đã ép
                int starEmpty = 0; // lỗ sao pha lê
                for (ItemOption io : lthu.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.combineNew.gemCombine = getGemEpSao(star);
                    String npcSay = lthu.template.name + "\n|2|";
                    for (ItemOption io : lthu.itemOptions) {
                        if (io.optionTemplate.id != 102) {
                            npcSay += io.getOptionString() + "\n";
                        }
                    }
                    if (spl.template.type == 30) {
                        for (ItemOption io : spl.itemOptions) {
                            npcSay += "|7|" + io.getOptionString() + "\n";
                        }
                    } else {
                        npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(spl)).name
                                .replaceAll("#", getParamDaPhaLe(spl) + "") + "\n";
                    }

                    npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                    drBrief.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                }
            } else {
                this.drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Phải bỏ vào Linh thú và Tinh thạch dùng để ép sao !", "Đóng");
            }
        } else {
            this.drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chỉ bỏ vào Linh thú và Tinh thạch dùng để ép sao !", "Đóng");

        }
    }

    public void menu_AN_TRANG_BI(Player player) {
        if (player.combineNew.itemsCombine.size() != 3) {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 1 trang bị chưa ấn, 1 viên ngọc rồng 2 sao và 20 đá ma thuật", "Đồng ý");
            return;
        }
        Item trangBi = null,
                nr1s = null;
        Item daMaThuat = null;
        forItem:
        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.type < 5) {
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 34 || io.optionTemplate.id == 35 || io.optionTemplate.id == 36) {
                        break forItem;
                    }
                }
                trangBi = item;
            } else if (item.template.id == 15) {
                nr1s = item;
            } else if (item.template.id == 1596) {
                daMaThuat = item;
            }
        }
        if (trangBi == null || nr1s == null || daMaThuat == null || daMaThuat.quantity < 20) {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 1 trang bị chưa ấn, 1 viên ngọc rồng 2 sao và 20 đá ma thuật", "Đồng ý");
            return;
        }
        int goldAnTrangBi = 200000000;
        player.combineNew.goldCombine = goldAnTrangBi;

        String npcSay = "|2|" + trangBi.template.name + "\n|1|";
        for (ItemOption io : trangBi.itemOptions) {
            npcSay += io.getOptionString() + "\n";
        }
        npcSay += "|2|Ấn trang bị cần\n|1|" + Util.numberToMoney(goldAnTrangBi) + " vàng\n|2|Ngươi có muốn ấn không?";
        if (player.inventory.gold < goldAnTrangBi) {
            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay,
                    "Còn thiếu\n" + Util.numberToMoney(goldAnTrangBi - player.inventory.gold) + " vàng");
        } else {
            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Ấn trang bị", "Từ chối");
        }
    }

    public void menu_TAY_AN_TRANG_BI(Player player) {
    // Phải có đúng 2 ô trong rương ghép
    if (player.combineNew.itemsCombine.size() != 2) {
        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần 1 trang bị đã khảm ấn và 10 Đá Ma Thuật để tẩy ấn", "Đồng ý");
        return;
    }

    Item trangBi = null;
    int tongDaMaThuat = 0;

    // Quét 2 ô trong rương ghép
    for (Item item : player.combineNew.itemsCombine) {

        // Đá ma thuật (ID 1596)
        if (item.template.id == 1596) {
            tongDaMaThuat += item.quantity;
            continue;
        }

        // Lọc trang bị (type 0-5)
        if (trangBi == null && item.template.type >= 0 && item.template.type <= 5) {

            // Kiểm tra xem có ấn hay không
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 34
                        || io.optionTemplate.id == 35
                        || io.optionTemplate.id == 36) {
                    trangBi = item;
                    break;
                }
            }
        }
    }

    // Kiểm tra thiếu trang bị hoặc không có ấn hoặc thiếu đá
    if (trangBi == null || tongDaMaThuat < 10) {
        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần 1 trang bị đã khảm ấn và 10 Đá Ma Thuật để tẩy ấn", "Đồng ý");
        return;
    }

    // Giá tẩy
    int goldTayAn = 100_000_000;
    player.combineNew.goldCombine = goldTayAn;

    // Tạo chuỗi hiển thị thông tin trang bị
    StringBuilder npcSay = new StringBuilder();
    npcSay.append("|2|").append(trangBi.template.name).append("\n|1|");

    for (ItemOption io : trangBi.itemOptions) {
        npcSay.append(io.getOptionString()).append("\n");
    }

    npcSay.append("|2|Tẩy ấn trang bị này sẽ xóa các dòng ấn\n")
            .append("|1|Giá: ").append(Util.numberToMoney(goldTayAn)).append(" vàng\n")
            .append("|2|Ngươi có muốn tẩy không?");

    // Kiểm tra vàng
    if (player.inventory.gold < goldTayAn) {
        this.baHatMit.createOtherMenu(
                player,
                ConstNpc.IGNORE_MENU,
                npcSay.toString(),
                "Còn thiếu\n" + Util.numberToMoney(goldTayAn - player.inventory.gold) + " vàng"
        );
    } else {
        this.baHatMit.createOtherMenu(
                player,
                ConstNpc.MENU_TAY_AN,
                npcSay.toString(),
                "Tẩy ấn",
                "Từ chối"
        );
    }
}
    
    public void menu_NANG_CAP_THIEN_TU(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item thientu = null,
                    maquai = null,
                    tinhthe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id == 1517) {
                        maquai = item;
                    } else if (item.template.id == 1518) {
                        tinhthe = item;
                    } else if (item.template.id == 1508 || item.template.id == 1509
                            || item.template.id == 1510 || item.template.id == 1511
                            || item.template.id == 1512 || item.template.id == 1513
                            || item.template.id == 1514 || item.template.id == 1515) {
                        thientu = item;
                    } else {
                        this.hatmittht.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chỉ được đặt Chân Thiên Tử, Ma Quái và Tinh Thể", "Đóng");
                        return;
                    }
                }
            }
            if (thientu == null || maquai == null || tinhthe == null) {
                this.hatmittht.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta cần Chân Thiên Tử + Ma Quái + Tinh Thể", "Đóng");
                return;
            }
            int level1_1 = 0, level1_2 = 0, level1_3 = 0, level1_4 = 0;
            for (ItemOption io : thientu.itemOptions) {
                if (io.optionTemplate.id == 50) {
                    level1_1 = io.param;
                    break;
                }
            }
            for (ItemOption io : thientu.itemOptions) {
                if (io.optionTemplate.id == 77) {
                    level1_2 = io.param;
                    break;
                }
            }
            for (ItemOption io : thientu.itemOptions) {
                if (io.optionTemplate.id == 103) {
                    level1_3 = io.param;
                    break;
                }
            }
            for (ItemOption io : thientu.itemOptions) {
                if (io.optionTemplate.id == 5) {
                    level1_4 = io.param;
                    break;
                }
            } 
            Item tvk = InventoryServiceNew.gI().findItemBag(player, 1429);
            if (tvk == null || tvk.quantity < 20_000) {
                Service.getInstance().sendThongBao(player, "Chuẩn bị đủ 20K Thỏi Vàng hãy đến tìm ta");
                return;
            }
            
            if (thientu.template.id == 1515) {
                Service.getInstance().sendThongBaoOK(player, "Cấp trang bị thiên tử đã đạt tối đa");
            } else {
                String npcSay = "|6|" + thientu.template.name + "\n";
                for (ItemOption io : thientu.itemOptions) {
                    npcSay += "|2|" + io.getOptionString() + "\n";
                }
                npcSay += "|1|Ngươi có muốn nâng cấp chân thiên tử không?\n"
                        + "Chỉ số Random tăng theo cấp độ\n"
                        + "Cấp 1 cần 10 Tinh thể, Ma Quái\n"
                        + "Mỗi cấp tăng x10 nguyên liệu\n"
                        + "|7|Cần 20K Thỏi Vàng";
                this.hatmittht.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng Cấp");
            }
        }
    }

    public void EP_SAO_LINH_THU(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            Item daPhaLe = null;
            Item trangBi = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.isLTHUNANGCAP()) {
                    trangBi = it;
                }
                if (it.isTinhThachNangCap()) {
                    daPhaLe = it;
                }
            }
            if (daPhaLe != null && trangBi != null) {
                int star = 0; // sao pha lê đã ép
                int starEmpty = 0; // lỗ sao pha lê
                if (trangBi != null && daPhaLe != null) {
                    ItemOption optionStar = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 102) {
                            star = io.param;
                            optionStar = io;
                        } else if (io.optionTemplate.id == 107) {
                            starEmpty = io.param;
                        }
                    }
                    if (star < starEmpty) {
                        int optionId = getOptionDaPhaLe(daPhaLe);
                        int param = getParamDaPhaLe(daPhaLe);
                        ItemOption option = null;
                        for (ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == optionId) {
                                option = io;
                                break;
                            }
                        }
                        if (option != null) {
                            option.param += param;
                        } else {
                            trangBi.itemOptions.add(new ItemOption(optionId, param));
                        }
                        if (optionStar != null) {
                            optionStar.param++;
                        } else {
                            trangBi.itemOptions.add(new ItemOption(102, 1));
                        }

                        InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
                        sendEffectSuccessCombine(player);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void anTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() != 3) {
            return;
        }
        Item trangBi = null,
                nr1s = null,
                daMaThuat = null;
        forItem:
        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.type < 5) {
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 34 || io.optionTemplate.id == 35 || io.optionTemplate.id == 36) {
                        break forItem;
                    }
                }
                trangBi = item;
            } else if (item.template.id == 15) {
                nr1s = item;
            } else if (item.template.id == 1596) {
                daMaThuat = item;
            }
        }
        if (trangBi == null || nr1s == null || daMaThuat == null || daMaThuat.quantity < 20) {
            return;
        }
        long goldAn = player.combineNew.goldCombine;
        if (player.inventory.gold < goldAn) {
            return;
        }
        player.inventory.gold -= goldAn;

        trangBi.itemOptions.add(new ItemOption(Util.nextInt(34, 36), 1));

        InventoryServiceNew.gI().subQuantityItemsBag(player, nr1s, 1);
        InventoryServiceNew.gI().subQuantityItemsBag(player, daMaThuat, 20);
        this.sendEffectSuccessCombine(player);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        reOpenItemCombine(player);
    }
    
    private void tayAnTrangBi(Player player) {

    // Phải có 2 ô trong rương ghép
    if (player.combineNew.itemsCombine.size() != 2) {
        Service.getInstance().sendThongBao(player,
                "Hãy đặt 1 trang bị đã khảm ấn và 10 Đá ma thuật!");
        return;
    }

    Item trangBi = null;
    int tongDaMaThuat = 0;
    Item itemDaStack = null;

    // Quét 2 ô
    for (Item item : player.combineNew.itemsCombine) {

        // Đá Ma Thuật (ID 1596)
        if (item.template.id == 1596) {
            tongDaMaThuat += item.quantity;
            itemDaStack = item;
            continue;
        }

        // Lọc trang bị (type 0-5)
        if (trangBi == null && item.template.type >= 0 && item.template.type <= 5) {

            // Kiểm tra xem có ấn hay không
            if (item.itemOptions != null) {
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 34
                            || io.optionTemplate.id == 35
                            || io.optionTemplate.id == 36) {
                        trangBi = item;
                        break;
                    }
                }
            }
        }
    }

    // Không có trang bị khảm ấn
    if (trangBi == null) {
        Service.getInstance().sendThongBao(player,
                "Trang bị này chưa được khảm ấn!");
        return;
    }

    // Không đủ đá
    if (tongDaMaThuat < 10) {
        Service.getInstance().sendThongBao(player,
                "Cần ít nhất 10 Đá ma thuật để tẩy ấn!");
        return;
    }

    // Xóa ấn
    trangBi.itemOptions.removeIf(io ->
            io.optionTemplate.id == 34 ||
            io.optionTemplate.id == 35 ||
            io.optionTemplate.id == 36);

    // Trừ đá (chỉ trừ từ stack tìm thấy)
    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDaStack, 10);

    // Gửi hiệu ứng
    this.sendEffectSuccessCombine(player);

    // Cập nhật túi đồ
    InventoryServiceNew.gI().sendItemBags(player);

    // Thông báo
    Service.getInstance().sendThongBao(player,
            "Tẩy ấn trang bị thành công!");

    // Mở lại menu ghép
    reOpenItemCombine(player);
}

    
    private void nangcapthientu(Player player) {
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.getInstance().sendThongBao(player, "Không đủ vật phẩm");
            return;
        }
        Item thientu = getItemById(player, 1508, 1509, 1510, 1511, 1512, 1513, 1514, 1515);
        Item maquai = getItemById(player, 1517);
        Item tinhthe = getItemById(player, 1518);

        if (thientu == null || maquai == null || tinhthe == null) {
            Service.getInstance().sendThongBao(player, "Không đủ vật phẩm");
            return;
        }
        
            Item tvk = InventoryServiceNew.gI().findItemBag(player, 1429);
            if (tvk == null || tvk.quantity < 20_000) {
                Service.getInstance().sendThongBao(player, "Chuẩn bị đủ 20K Thỏi Vàng hãy đến tìm ta");
                return;
            }
        int[] levels = getOptionLevels(thientu);
        InventoryService.gI().subQuantityItemsBag(player, tvk, 20_000);
        processUpgrade(player, thientu, maquai, tinhthe, levels);
    }

    private Item getItemById(Player player, int... ids) {
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                for (int id : ids) {
                    if (item.template.id == id) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    private int[] getOptionLevels(Item item) {
        int[] levels = new int[9]; // Chứa các level của option
        for (ItemOption io : item.itemOptions) {
            switch (io.optionTemplate.id) {
                case 0:
                    levels[0] = io.param;
                    break;
                case 5:
                    levels[1] = io.param;
                    break;
                case 14:
                    levels[2] = io.param;
                    break;
                case 50:
                    levels[4] = io.param;
                    break;
                case 77:
                    levels[5] = io.param;
                    break;
                case 103:
                    levels[6] = io.param;
                    break;
                case 72:
                    levels[7] = io.param;
                    break;
            }
        }
        return levels;
    }

    private int[] getTierData(int itemId) {
    // {id, tỷ lệ %, tinh thể, máu quái}
    int[][] data = {
        {1508, 80, 10, 10},
        {1509, 70, 20, 20},
        {1510, 60, 30, 30},
        {1511, 25, 40, 40},
        {1512, 15, 50, 50},
        {1513, 10, 60, 60},
        {1514, 5, 70, 70},
        // 1515 là cấp cuối, không nâng
    };
    for (int[] row : data) {
        if (row[0] == itemId) {
            return row; // trả về {id, rate, tinhthe, maquai}
        }
    }
    return null;
}

    private void processUpgrade(Player player, Item thientu, Item maquai, Item tinhthe, int[] levels) {

    boolean done = false;

    int[] tier = getTierData(thientu.template.id);
    if (tier == null) {
        Service.getInstance().sendThongBao(player, "Thiên Tử đã đạt cấp tối đa!");
        done = true;
    }

    if (!done) {
        int rate = tier[1];
        int needTinhThe = tier[2];
        int needMaQuai = tier[3];

        // kiểm tra số lượng
        if (tinhthe.quantity < needTinhThe || maquai.quantity < needMaQuai) {
            Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu yêu cầu cấp này!");
            done = true;
        }

        if (!done) {
            // trừ nguyên liệu
            InventoryService.gI().subQuantityItemsBag(player, tinhthe, needTinhThe);
            InventoryService.gI().subQuantityItemsBag(player, maquai, needMaQuai);
            

            int random = Util.nextInt(1, 100);
            boolean success = random <= rate;

            if (success) {
                InventoryService.gI().subQuantityItemsBag(player, thientu, 1);

                Item newItem = ItemService.gI().createNewItem((short) (thientu.template.id + 1));
                addItemOptions(newItem, levels);
                InventoryService.gI().addItemBag(player, newItem, 1);

                sendEffectSuccessCombine(player);
                Service.getInstance().sendThongBao(player, 
                    "Nâng cấp thành công (" + rate + "%)");
            } else {
                Service.getInstance().sendThongBao(player, "Thất bại! Bạn vẫn giữ được Thiên Tử");
            }
        }
    }

    // LUÔN GỬI CUỐI CÙNG
    InventoryService.gI().sendItemBags(player);
    Service.getInstance().sendMoney(player);
    reOpenItemCombine(player);
}


//    private void processUpgrade(Player player, Item thientu, Item maquai, Item tinhthe, Item tvk, int[] levels) {
//        InventoryService.gI().subQuantityItemsBag(player, tinhthe, 10);
//        InventoryService.gI().subQuantityItemsBag(player, maquai, 10);
//        InventoryService.gI().subQuantityItemsBag(player, thientu, 1);
//        InventoryService.gI().subQuantityItemsBag(player, tvk, 100_000);
//        
//        Item item = ItemService.gI().createNewItem((short) (thientu.template.id + 1));
//        addItemOptions(item, levels);
//        InventoryService.gI().addItemBag(player, item, 1);
//        
//        sendEffectSuccessCombine(player);
//        InventoryService.gI().sendItemBags(player);
//        Service.getInstance().sendMoney(player);
//        reOpenItemCombine(player);
//    }

    private void addItemOptions(Item item, int[] levels) {
        item.itemOptions.add(new ItemOption(5, levels[2] + 1));
        item.itemOptions.add(new ItemOption(0, levels[0] + Util.nextInt(20, 80)));
        item.itemOptions.add(new ItemOption(14, levels[1] + 1));
        item.itemOptions.add(new ItemOption(50, levels[4] + Util.nextInt(1, 3)));
        item.itemOptions.add(new ItemOption(77, levels[5] + Util.nextInt(1, 3)));
        item.itemOptions.add(new ItemOption(103, levels[6] + Util.nextInt(1, 3)));
        item.itemOptions.add(new ItemOption(72, levels[7] + 1));
    }

//    private void menu_Pha_le_hoa_linh_thu(Player player) {
//        if (player.combineNew.itemsCombine.size() == 1) {
//            if (player.inventory.gold < 0) {
//                drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ vàng để thực hiện", "Đóng");
//                return;
//            }
//            Item item = player.combineNew.itemsCombine.get(0);
//            if (item.isLTHUNANGCAP()) {
//                int star = 0;
//                for (ItemOption io : item.itemOptions) {
//                    if (io.optionTemplate.id == 107) {
//                        star = io.param;
//                        break;
//                    }
//                }
//                if (star < 5) {
//                    player.combineNew.goldCombine = getGoldPhaLeHoaLinhThu(star);
//                    player.combineNew.gemCombine = getGemPhaLeHoa(star);
//                    player.combineNew.ratioCombine = getRatioPhaLeHoaBip(star);
//                    float ratiohienthi = getRatioPhaLeHoa(star);
//                    String npcSay = item.template.name + "\n|2|";
//                    for (ItemOption io : item.itemOptions) {
//                        if (io.optionTemplate.id != 102) {
//                            npcSay += io.getOptionString() + "\n";
//                        }
//                    }
//
//                    npcSay += "|7|Tỉ lệ thành công: " + ratiohienthi + "%" + "\n";
//
//                    if (player.combineNew.goldCombine <= player.inventory.gold) {
//                        npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
//                        drBrief.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
//                                "1\nlần", "10\n lần", "50\n lần", "100\n lần ", "200\n lần", "1000\n lần", "Hủy");
//                    } else {
//                        npcSay += "Còn thiếu "
//                                + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
//                                + " vàng";
//                        drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
//                    }
//                } else {
//                    this.drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                            "Linh thú chỉ có thể pha lê hóa 5 lỗ !", "Đóng");
//                }
//            } else {
//                this.drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chỉ có thể pha lê hóa linh thú !",
//                        "Đóng");
//            }
//        } else {
//            this.drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
//                    "Đóng");
//        }
//    }
private void menu_Pha_le_hoa_linh_thu(Player player) {
    // Kiểm tra có đúng 1 vật phẩm
    if (player.combineNew.itemsCombine.size() != 1) {
        this.drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Hãy chọn 1 Linh Thú để pha lê hóa!", "Đóng");
        return;
    }

    Item item = player.combineNew.itemsCombine.get(0);
    if (!item.isLTHUNANGCAP()) {
        this.drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Chỉ có thể pha lê hóa Linh Thú!", "Đóng");
        return;
    }

    // Lấy cấp sao hiện tại
    int star = 0;
    for (ItemOption io : item.itemOptions) {
        if (io.optionTemplate.id == 107) {
            star = io.param;
            break;
        }
    }

    if (star >= 8) {
        this.drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Linh Thú đã đạt cấp pha lê tối đa (8 sao)!", "Đóng");
        return;
    }

    // Thiết lập thông tin combine (hiển thị)
    player.combineNew.goldCombine = 0; // Không dùng vàng
    player.combineNew.gemCombine = 0;  // Không dùng ngọc
    player.combineNew.ratioCombine = getTiLePhaLeHoaLinhThu(star); // 40% tỉ lệ thật

    String npcSay = item.template.name + "\n|2|";
    for (ItemOption io : item.itemOptions) {
        if (io.optionTemplate.id != 102) {
            npcSay += io.getOptionString() + "\n";
        }
    }

    npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine +"\n";
    npcSay += "|1|Cần 10.000 Thỏi Vàng Khóa để thực hiện\n";

    // Kiểm tra người chơi có đủ 10k TVK không
    Item tvk = InventoryServiceNew.gI().findItemBag(player, 1429);
    if (tvk == null || tvk.quantity < 10_000) {
        npcSay += "|5|Bạn không đủ Thỏi Vàng Khóa để thực hiện!";
        this.drBrief.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
        return;
    }

    // Hiển thị menu chọn số lần
    this.drBrief.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
            "1\nlần", "10\nlần");
}

//    private boolean Phe_Le_Hoa_Linh_Thu(Player player) {
//        if (!player.combineNew.itemsCombine.isEmpty()) {
//
//            long gold = player.combineNew.goldCombine;
//            int gem = player.combineNew.gemCombine;
//            if (player.inventory.gold < gold) {
//                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
//                return false;
//            } else if (player.inventory.gem < gem) {
//                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
//                return false;
//            }
//            Item item = player.combineNew.itemsCombine.get(0);
//            if (item.isLTHUNANGCAP()) {
//                int star = 0;
//                ItemOption optionStar = null;
//                for (ItemOption io : item.itemOptions) {
//
//                    if (io.optionTemplate.id == 107) {
//                        star = io.param;
//                        optionStar = io;
//                        break;
//                    }
//                }
//                if (star < 8) {
//                    player.inventory.gold -= gold;
//                    player.inventory.subGem(gem);
//                    int ratio = (int) getRatioPhaLeHoaBip(star);
//                    if (player.numLinhThu >= ratio) {
//                        player.numLinhThu = 0;
//                        if (optionStar == null) {
//                            item.itemOptions.add(new ItemOption(107, 1));
//                        } else {
//                            optionStar.param++;
//                        }
//                        InventoryService.gI().sendItemBags(player);
//                        Service.getInstance().sendMoney(player);
//                        reOpenItemCombine(player);
//                        return false;
//                    } else {
//                        player.numLinhThu++;
//                        InventoryService.gI().sendItemBags(player);
//                        Service.getInstance().sendMoney(player);
//                        reOpenItemCombine(player);
//                        return true;
//                    }
//
//                } else {
//                    return false;
//                }
//            } else {
//                Service.getInstance().sendThongBao(player, "Không thể pha lê hóa trang bị");
//                return false;
//            }
//        }
//        return false;
//    }
   private boolean Phe_Le_Hoa_Linh_Thu(Player player) {

    if (player.combineNew.itemsCombine.isEmpty()) {
        return false;
    }

    Item item = player.combineNew.itemsCombine.get(0);

    // Không đúng item linh thú để pha lê
    if (!item.isLTHUNANGCAP()) {
        Service.getInstance().sendThongBao(player, "Không thể pha lê hóa linh thú này!");
        return false;
    }

    // Kiểm tra thỏi vàng khóa
    Item thoiVangKhoa = InventoryServiceNew.gI().findItemBag(player, 1429);
    if (thoiVangKhoa == null || thoiVangKhoa.quantity < 10000) {
        Service.getInstance().sendThongBao(player, "Cần 10.000 Thỏi vàng khóa để pha lê hóa Linh Thú!");
        return false;
    }

    // Lấy sao hiện tại
    int star = 0;
    ItemOption optionStar = null;
    for (ItemOption io : item.itemOptions) {
        if (io.optionTemplate.id == 107) {
            star = io.param;
            optionStar = io;
            break;
        }
    }

    // max 8 sao
    if (star >= 8) {
        Service.getInstance().sendThongBao(player, "Linh Thú đã đạt cấp sao tối đa!");
        return false;
    }

    // Trừ 10k TVK trước khi đập
    InventoryServiceNew.gI().subQuantityItemsBag(player, thoiVangKhoa, 10000);

    // Gửi lại hành trang sau khi trừ nguyên liệu
    InventoryService.gI().sendItemBags(player);

    int tile = getTiLePhaLeHoaLinhThu(star);
    boolean success = Util.isTrue(tile, 100);

    if (success) {
        // Tăng sao
        if (optionStar == null) {
            item.itemOptions.add(new ItemOption(107, 1));
        } else {
            optionStar.param++;
        }

        // Gửi thông báo
        Service.getInstance().sendThongBao(player,
                "Pha lê hóa thành công! Sao hiện tại: " + (star + 1));

        // Gửi lại thông tin
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        reOpenItemCombine(player);

        // *** RETURN FALSE === DỪNG AUTO ĐẬP ***
        return false;
    }

    // FAIL → cho phép đập tiếp
    InventoryService.gI().sendItemBags(player);
    Service.getInstance().sendMoney(player);
    reOpenItemCombine(player);

    return true; // tiếp tục auto đập
}

private int getTiLePhaLeHoaLinhThu(int star) {
    switch (star) {
        case 0:
            return 40; 
        case 1:
            return 30;
        case 2:
            return 25; 
        case 3:
            return 20; 
        case 4:
            return 15; 
        case 5:
            return 6; 
        case 6:
            return 4; 
        case 7:
            return 2; 
            
        default:
            return 0;
    }
}

}
