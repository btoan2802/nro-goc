package nro.models.item;

import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.utils.Util;

public class ItemTime {

    // id item text
    public static final byte DOANH_TRAI = 0;
    public static final byte BAN_DO_KHO_BAU = 1;
    public static final byte BAU_CUA = 2;

    public static final int TIME_ITEM = 600000;
    public static final int TIME_PHAO_HOA = 5000;
    public static final int TIME_OPEN_POWER = 86400000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_20_MIN = 20 * 60 * 1000; // 1_200_000

    public static final int TIME_EAT_MEAL = 600000;
    public static final int TIME_60_MIN = 3600000;
    public static final int TIME_90_MIN = 5400000;
    public static final int TIME_120_MIN = 7200000;
    public static final int TIME_150_MIN = 9000000;
    private Player player;
    public boolean isUseBoHuyet2;
    public boolean isUseBoKhi2;
    public boolean isUseGiapXen2;
    public boolean isUseCuongNo2;
    public long lastTimeBoHuyet2;
    public long lastTimeBoKhi2;
    public long lastTimeGiapXen2;
    public long lastTimeCuongNo2;
    

    public boolean isUseBanhChung;
    public boolean isUseBanhTet;
    public long lastTimeBanhChung;
    public long lastTimeBanhTet;
    public boolean isUseBoHuyet;
    public boolean isUseBoKhi;
    public boolean isUseGiapXen;
    public boolean isUseCuongNo;
    public boolean isUseAnDanh;
    public long lastTimeBoHuyet;
    public long lastTimeBoKhi;
    public long lastTimeGiapXen;
    public long lastTimeCuongNo;
    public long lastTimeAnDanh;

    public boolean isUseMayDo;
    public long lastTimeUseMayDo;

    public boolean isOpenPower;
    public long lastTimeOpenPower;

    public boolean isUseTDLT;
    public long lastTimeUseTDLT;
    public int timeTDLT;

    public boolean isEatMeal;
    public long lastTimeEatMeal;
    public int iconMeal;

    public boolean isUseMdSkh;
    public long lastTimeMdSkh;

    public boolean isUseBohoaHong;
    public long lastTimeBohoaHong;

    public long lastTimeUseMaydoBongtoi;
    public boolean isUseMaydoBongtoi;
    public long lastTimeUseX2TNSM;
    public boolean isUseX2TNSM;
    public long lastTimeUseX3TNSM;
    public boolean isUseX3TNSM;
    public long lastTimeUseX4TNSM;
    public boolean isUseX4TNSM;

    public boolean isUseThitSuon;
    public long lastTimeuseThitSuon;

    public boolean isUseThitThan;
    public long lastTimeuseThitThan;

    public boolean isUseDauVe;
    public long lastTimeUseDauve;

    public long lastTimeUsePhaoHoa;
    public boolean isUseUsePhaoHoa;
    // Group 1
    public long lastTimeUseGroup_1_1;
    public boolean isUseGroup_1_1;
    public long lastTimeUseGroup_1_2;
    public boolean isUseGroup_1_2;
    public long lastTimeUseGroup_1_3;
    public boolean isUseGroup_1_3;
    // Group 2
    public long lastTimeUseGroup_2_1;
    public boolean isUseGroup_2_1;
    public long lastTimeUseGroup_2_2;
    public boolean isUseGroup_2_2;
    public long lastTimeUseGroup_2_3;
    public boolean isUseGroup_2_3;
    // Group 3
    public long lastTimeUseGroup_3_1;
    public boolean isUseGroup_3_1;
    public long lastTimeUseGroup_3_2;
    public boolean isUseGroup_3_2;
    public long lastTimeUseGroup_3_3;
    public boolean isUseGroup_3_3;
    // Group 4
    public long lastTimeUseGroup_4_1;
    public boolean isUseGroup_4_1;
    public long lastTimeUseGroup_4_2;
    public boolean isUseGroup_4_2;
    public long lastTimeUseGroup_4_3;
    public boolean isUseGroup_4_3;
    // Bánh trung thu
    public long lastTimeBanhTrungThu_1;
    public boolean isUseBanhTrungThu_1;
    public long lastTimeBanhTrungThu_2;
    public boolean isUseBanhTrungThu_2;
    public long lastTimeBanhTrungThu_3;
    public boolean isUseBanhTrungThu_3;
    public long lastTimeBanhTrungThu_4;
    public boolean isUseBanhTrungThu_4;
    // Group 5 10 phút
    public long lastTimeUseGroup_5_1;
    public boolean isUseGroup_5_1;
    public long lastTimeUseGroup_5_2;
    public boolean isUseGroup_5_2;
    public long lastTimeUseGroup_5_3;
    public boolean isUseGroup_5_3;
    public long lastTimeUseGroup_5_4;
    public boolean isUseGroup_5_4;
    public long lastTimeUseGroup_5_5;
    public boolean isUseGroup_5_5;
    public long lastTimeUseGroup_5_6;
    public boolean isUseGroup_5_6;
    public long lastTimeUseGroup_5_7;
    public boolean isUseGroup_5_7;
    public long lastTimeUseGroup_5_8;
    public boolean isUseGroup_5_8;
    public long lastTimeUseGroup_5_9;
    public boolean isUseGroup_5_9;
    public long lastTimeUseGroup_5_10;
    public boolean isUseGroup_5_10;
    // Group 6 30 phút
    public long lastTimeUseGroup_6_1;
    public boolean isUseGroup_6_1;
    public long lastTimeUseGroup_6_2;
    public boolean isUseGroup_6_2;
    public long lastTimeUseGroup_6_3;
    public boolean isUseGroup_6_3;
    public long lastTimeUseGroup_6_4;
    public boolean isUseGroup_6_4;
    public long lastTimeUseGroup_6_5;
    public boolean isUseGroup_6_5;
    // Group 6 60 phút
    public long lastTimeUseGroup_7_1;
    public boolean isUseGroup_7_1;
    public long lastTimeUseGroup_7_2;
    public boolean isUseGroup_7_2;
    public long lastTimeUseGroup_7_3;
    public boolean isUseGroup_7_3;
    public long lastTimeUseGroup_7_4;
    public boolean isUseGroup_7_4;
    public long lastTimeUseGroup_7_5;
    public boolean isUseGroup_7_5;

    public long lastTimeUseHoangHoa;
    public boolean isUseHoangHoa;

    public long lastTimeHuyHieu;
    public boolean isHuyHieu;

    public long lastTimeCaChua;
    public boolean isCaChua;

    public long lastTimeCaRot;
    public boolean isCaRot;

    public long lastTimeChuoi;
    public boolean isChuoi;

    public long lastTimeKeoBanTay;
    public boolean isKeoBayTay;

    public long lastTimeDaiHaiTrinh;
    public boolean isDaiHaiTrinh;

    public ItemTime(Player player) {
        this.player = player;
    }

    public void update() {
        boolean update = false;
        if (isDaiHaiTrinh) {
            if (Util.canDoWithTime(lastTimeDaiHaiTrinh, TIME_60_MIN)) {
                isDaiHaiTrinh = false;
            }
        }

        if (isKeoBayTay) {
            if (Util.canDoWithTime(lastTimeKeoBanTay, TIME_ITEM)) {
                isKeoBayTay = false;
            }
        }

        if (isChuoi) {
            if (Util.canDoWithTime(lastTimeChuoi, TIME_ITEM)) {
                isChuoi = false;
            }
        }
        if (isCaRot) {
            if (Util.canDoWithTime(lastTimeCaRot, TIME_ITEM)) {
                isCaRot = false;
            }
        }
        if (isCaChua) {
            if (Util.canDoWithTime(lastTimeCaChua, TIME_ITEM)) {
                isCaChua = false;
            }
        }
        if (isHuyHieu) {
            if (Util.canDoWithTime(lastTimeHuyHieu, TIME_ITEM)) {
                isHuyHieu = false;
            }
        }
        if (isUseHoangHoa) {
            if (Util.canDoWithTime(lastTimeUseHoangHoa, TIME_ITEM)) {
                isUseHoangHoa = false;
            }
        }
        if (isUseDauVe) {
            if (Util.canDoWithTime(lastTimeUseDauve, TIME_ITEM)) {
                isUseDauVe = false;
                update = true;
            }
        }
        if (isUseThitThan) {
            if (Util.canDoWithTime(lastTimeuseThitSuon, TIME_ITEM)) {
                isUseThitThan = false;
                update = true;
            }
        }
        if (isUseThitSuon) {
            if (Util.canDoWithTime(lastTimeuseThitSuon, TIME_ITEM)) {
                isUseThitSuon = false;
                update = true;
            }
        }
        if (isUseMaydoBongtoi) {
            if (Util.canDoWithTime(lastTimeUseMaydoBongtoi, TIME_MAY_DO)) {
                isUseMaydoBongtoi = false;
            }
        }
        if (isUseBohoaHong) {
            if (Util.canDoWithTime(lastTimeBohoaHong, TIME_MAY_DO)) {
                isUseBohoaHong = false;
            }
        }
        if (isUseMdSkh) {
            if (Util.canDoWithTime(lastTimeMdSkh, TIME_MAY_DO)) {
                isUseMdSkh = false;
            }
        }
        if (isEatMeal) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
                isEatMeal = false;
                update = true;
            }
        }
        if (isUseBoHuyet) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseBoHuyet = false;
                update = true;
            }
        }
        if (isUseBoKhi) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
                isUseBoKhi = false;
                update = true;
            }
        }
        if (isUseGiapXen) {
            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
                isUseGiapXen = false;
            }
        }
        if (isUseCuongNo) {
            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
                isUseCuongNo = false;
                update = true;
            }
        }
        if (isUseAnDanh) {
            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
                isUseAnDanh = false;
            }
        }
        if (isUseBanhChung) {
            if (Util.canDoWithTime(lastTimeBanhChung, TIME_ITEM)) {
                isUseBanhChung = false;
            }
        }
        if (isUseBanhTet) {
            if (Util.canDoWithTime(lastTimeBanhTet, TIME_ITEM)) {
                isUseBanhTet = false;
            }
        }
        if (isUseBoHuyet2) {
            if (Util.canDoWithTime(lastTimeBoHuyet2, TIME_ITEM)) {
                isUseBoHuyet2 = false;
                update = true;
            }
        }
        if (isUseBoKhi2) {
            if (Util.canDoWithTime(lastTimeBoKhi2, TIME_ITEM)) {
                isUseBoKhi2 = false;
                update = true;
            }
        }
        if (isUseGiapXen2) {
            if (Util.canDoWithTime(lastTimeGiapXen2, TIME_ITEM)) {
                isUseGiapXen2 = false;
            }
        }
        if (isUseCuongNo2) {
            if (Util.canDoWithTime(lastTimeCuongNo2, TIME_ITEM)) {
                isUseCuongNo2 = false;
                update = true;
            }
        }
        if (isOpenPower) {
            if (Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
                player.nPoint.limitPower++;
                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
                }
                player.nPoint.initPowerLimit();
                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
                isOpenPower = false;
            }
        }
        if (isUseMayDo) {
            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
                isUseMayDo = false;
            }
        }
        if (isUseTDLT) {
            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
                this.isUseTDLT = false;
                ItemTimeService.gI().sendCanAutoPlay(this.player);
            }
        }
        if (isUseBanhChung) {
            if (Util.canDoWithTime(lastTimeBanhChung, TIME_ITEM)) {
                isUseBanhChung = false;
                update = true;
            }
        }
        if (isUseBanhTet) {
            if (Util.canDoWithTime(lastTimeBanhTet, TIME_ITEM)) {
                isUseBanhTet = false;
                update = true;
            }
        }
        // x2 x3 x4
        if (isUseX2TNSM) {
            if (Util.canDoWithTime(lastTimeUseX2TNSM, TIME_ITEM)) {
                isUseX2TNSM = false;
                update = true;
            }
        }
        if (isUseX3TNSM) {
            if (Util.canDoWithTime(lastTimeUseX3TNSM, TIME_ITEM)) {
                isUseX3TNSM = false;
                update = true;
            }
        }
        if (isUseX4TNSM) {
            if (Util.canDoWithTime(lastTimeUseX4TNSM, TIME_ITEM)) {
                isUseX4TNSM = false;
                update = true;
            }
        }
        if (isUseUsePhaoHoa) {
            if (Util.canDoWithTime(lastTimeUsePhaoHoa, TIME_PHAO_HOA)) {
                isUseUsePhaoHoa = false;
                // Service.getInstance().rsDanhHieu(player);
                update = true;
            }
        }
        // Group 1
        if (isUseGroup_1_1) {
            if (Util.canDoWithTime(lastTimeUseGroup_1_1, TIME_ITEM)) {
                isUseGroup_1_1 = false;
                update = true;
            }
        }
        if (isUseGroup_1_2) {
            if (Util.canDoWithTime(lastTimeUseGroup_1_2, TIME_ITEM)) {
                isUseGroup_1_2 = false;
                update = true;
            }
        }
        if (isUseGroup_1_3) {
            if (Util.canDoWithTime(lastTimeUseGroup_1_3, TIME_ITEM)) {
                isUseGroup_1_3 = false;
                update = true;
            }
        }
        // Group 2
        if (isUseGroup_2_1) {
            if (Util.canDoWithTime(lastTimeUseGroup_2_1, TIME_ITEM)) {
                isUseGroup_2_1 = false;
                update = true;
            }
        }
        if (isUseGroup_2_2) {
            if (Util.canDoWithTime(lastTimeUseGroup_2_2, TIME_ITEM)) {
                isUseGroup_2_2 = false;
                update = true;
            }
        }
        if (isUseGroup_2_3) {
            if (Util.canDoWithTime(lastTimeUseGroup_2_3, TIME_ITEM)) {
                isUseGroup_2_3 = false;
                update = true;
            }
        }
        // Group 3
        if (isUseGroup_3_1) {
            if (Util.canDoWithTime(lastTimeUseGroup_3_1, TIME_MAY_DO)) {
                isUseGroup_3_1 = false;
                update = true;
            }
        }
        if (isUseGroup_3_2) {
            if (Util.canDoWithTime(lastTimeUseGroup_3_2, TIME_MAY_DO)) {
                isUseGroup_3_2 = false;
                update = true;
            }
        }
        if (isUseGroup_3_3) {
            if (Util.canDoWithTime(lastTimeUseGroup_3_3, TIME_60_MIN)) {
                isUseGroup_3_3 = false;
                update = true;
            }
        }
        // Group 4
        if (isUseGroup_4_1) {
            if (Util.canDoWithTime(lastTimeUseGroup_4_1, TIME_90_MIN)) {
                isUseGroup_4_1 = false;
                update = true;
            }
        }
        if (isUseGroup_4_2) {
            if (Util.canDoWithTime(lastTimeUseGroup_4_2, TIME_90_MIN)) {
                isUseGroup_4_2 = false;
                update = true;
            }
        }
        if (isUseGroup_4_3) {
            if (Util.canDoWithTime(lastTimeUseGroup_4_3, TIME_90_MIN)) {
                isUseGroup_4_3 = false;
                update = true;
            }
        }
        // Bánh trung thu
        if (isUseBanhTrungThu_1) {
            if (Util.canDoWithTime(lastTimeBanhTrungThu_1, TIME_60_MIN)) {
                isUseBanhTrungThu_1 = false;
                update = true;
            }
        }
        if (isUseBanhTrungThu_2) {
            if (Util.canDoWithTime(lastTimeBanhTrungThu_2, TIME_90_MIN)) {
                isUseBanhTrungThu_2 = false;
                update = true;
            }
        }
        if (isUseBanhTrungThu_3) {
            if (Util.canDoWithTime(lastTimeBanhTrungThu_3, TIME_120_MIN)) {
                isUseBanhTrungThu_3 = false;
                update = true;
            }
        }
        if (isUseBanhTrungThu_4) {
            if (Util.canDoWithTime(lastTimeBanhTrungThu_4, TIME_150_MIN)) {
                isUseBanhTrungThu_4 = false;
                update = true;
            }
        }
        // Group 5 10 phút
        if (isUseGroup_5_1) {
            if (Util.canDoWithTime(lastTimeUseGroup_5_1, TIME_ITEM)) {
                isUseGroup_5_1 = false;
                update = true;
            }
        }
        if (isUseGroup_5_2) {
            if (Util.canDoWithTime(lastTimeUseGroup_5_2, TIME_ITEM)) {
                isUseGroup_5_2 = false;
                update = true;
            }
        }
        if (isUseGroup_5_3) {
            if (Util.canDoWithTime(lastTimeUseGroup_5_3, TIME_ITEM)) {
                isUseGroup_5_3 = false;
                update = true;
            }
        }
        if (isUseGroup_5_4) {
            if (Util.canDoWithTime(lastTimeUseGroup_5_4, TIME_ITEM)) {
                isUseGroup_5_4 = false;
                update = true;
            }
        }
        if (isUseGroup_5_5) {
            if (Util.canDoWithTime(lastTimeUseGroup_5_5, TIME_ITEM)) {
                isUseGroup_5_5 = false;
                update = true;
            }
        }
        if (isUseGroup_5_6) {
            if (Util.canDoWithTime(lastTimeUseGroup_5_6, TIME_ITEM)) {
                isUseGroup_5_6 = false;
                update = true;
            }
        }
        if (isUseGroup_5_7) {
            if (Util.canDoWithTime(lastTimeUseGroup_5_7, TIME_ITEM)) {
                isUseGroup_5_7 = false;
                update = true;
            }
        }
        if (isUseGroup_5_8) {
            if (Util.canDoWithTime(lastTimeUseGroup_5_8, TIME_ITEM)) {
                isUseGroup_5_8 = false;
                update = true;
            }
        }
        if (isUseGroup_5_9) {
            if (Util.canDoWithTime(lastTimeUseGroup_5_9, TIME_ITEM)) {
                isUseGroup_5_9 = false;
                update = true;
            }
        }
        if (isUseGroup_5_10) {
            if (Util.canDoWithTime(lastTimeUseGroup_5_10, TIME_ITEM)) {
                isUseGroup_5_10 = false;
                update = true;
            }
        }
        // Group 6 30 phút
        if (isUseGroup_6_1) {
            if (Util.canDoWithTime(lastTimeUseGroup_6_1, TIME_ITEM)) {
                isUseGroup_6_1 = false;
                update = true;
            }
        }
        if (isUseGroup_6_2) {
            if (Util.canDoWithTime(lastTimeUseGroup_6_2, TIME_MAY_DO)) {
                isUseGroup_6_2 = false;
                update = true;
            }
        }
        if (isUseGroup_6_3) {
            if (Util.canDoWithTime(lastTimeUseGroup_6_3, TIME_MAY_DO)) {
                isUseGroup_6_3 = false;
                update = true;
            }
        }
        if (isUseGroup_6_4) {
            if (Util.canDoWithTime(lastTimeUseGroup_6_4, TIME_MAY_DO)) {
                isUseGroup_6_4 = false;
                update = true;
            }
        }
        if (isUseGroup_6_5) {
            if (Util.canDoWithTime(lastTimeUseGroup_6_5, TIME_MAY_DO)) {
                isUseGroup_6_5 = false;
                update = true;
            }
        }
        // Group 7 60 phút
        if (isUseGroup_7_1) {
            if (Util.canDoWithTime(lastTimeUseGroup_7_1, TIME_MAY_DO)) {
                isUseGroup_7_1 = false;
                update = true;
            }
        }
        if (isUseGroup_7_2) {
            if (Util.canDoWithTime(lastTimeUseGroup_7_2, TIME_60_MIN)) {
                isUseGroup_7_2 = false;
                update = true;
            }
        }
        if (isUseGroup_7_3) {
            if (Util.canDoWithTime(lastTimeUseGroup_7_3, TIME_60_MIN)) {
                isUseGroup_7_3 = false;
                update = true;
            }
        }
        if (isUseGroup_7_4) {
            if (Util.canDoWithTime(lastTimeUseGroup_7_4, TIME_60_MIN)) {
                isUseGroup_7_4 = false;
                update = true;
            }
        }
        if (isUseGroup_7_5) {
            if (Util.canDoWithTime(lastTimeUseGroup_7_5, TIME_60_MIN)) {
                isUseGroup_7_5 = false;
                update = true;
            }
        }
        if (update) {
            Service.getInstance().point(player);
        }
    }

    public void dispose() {
        this.player = null;
    }
}
