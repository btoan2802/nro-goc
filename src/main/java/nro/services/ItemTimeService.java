package nro.services;

import nro.consts.ConstPlayer;
import nro.models.auction.AuctionService;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.DoanhTrai;
import nro.models.player.Fusion;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.func.Chonaiday;
import nro.utils.Log;

import static nro.models.item.ItemTime.*;

public class ItemTimeService {

    private static ItemTimeService i;

    public static ItemTimeService gI() {
        if (i == null) {
            i = new ItemTimeService();
        }
        return i;
    }

    public void sendAllItemTime(Player player) {
        sendTextDoanhTrai(player);
        this.sendTextAuction(player);
        sendTextBanDoKhoBau(player);
        if (player.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
            sendItemTime(player, player.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                    (int) ((Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion)) / 1000));
        }
        if (player.itemTime.isDaiHaiTrinh) {
            sendItemTime(player, 26038,
                    (int) ((TIME_60_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeDaiHaiTrinh)) / 1000));
        }
        if (player.itemTime.isChuoi) {
            sendItemTime(player, 26382,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeChuoi)) / 1000));
        }
        if (player.itemTime.isCaRot) {
            sendItemTime(player, 26384,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCaRot)) / 1000));
        }
        if (player.itemTime.isCaChua) {
            sendItemTime(player, 26383,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCaChua)) / 1000));
        }
        if (player.itemTime.isHuyHieu) {
            sendItemTime(player, 26075,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeHuyHieu)) / 1000));
        }

        if (player.itemTime.isUseHoangHoa) {
            sendItemTime(player, 26036,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseHoangHoa)) / 1000));
        }

        if (player.itemTime.isUseThitSuon) {
            sendItemTime(player, 15036,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeuseThitSuon)) / 1000));
        }
        if (player.itemTime.isUseThitThan) {
            sendItemTime(player, 15037,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeuseThitThan)) / 1000));
        }
        if (player.itemTime.isUseDauVe) {
            sendItemTime(player, 10889,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseDauve)) / 1000));
        }
        if (player.itemTime.isUseMaydoBongtoi) {
            sendItemTime(player, 22181,
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMaydoBongtoi))
                    / 1000));
        }
        if (player.itemTime.isUseBohoaHong) {
            sendItemTime(player, 11806,
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeBohoaHong)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet) {
            sendItemTime(player, 2755,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) / 1000));
        }
        if (player.itemTime.isUseMdSkh) {
            sendItemTime(player, 22181,
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeMdSkh)) / 1000));

        }
        if (player.itemTime.isUseBoKhi) {
            sendItemTime(player, 2756,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) / 1000));
        }
        if (player.itemTime.isUseGiapXen) {
            sendItemTime(player, 2757,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) / 1000));
        }
        if (player.itemTime.isUseCuongNo) {
            sendItemTime(player, 2754,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) / 1000));
        }
        if (player.itemTime.isUseAnDanh) {
            sendItemTime(player, 2760,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) / 1000));
        }
        if (player.itemTime.isOpenPower) {
            sendItemTime(player, 3783,
                    (int) ((TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower))
                    / 1000));
        }
        if (player.itemTime.isUseMayDo) {
            sendItemTime(player, 2758,
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) / 1000));
        }
        if (player.itemTime.isEatMeal) {
            sendItemTime(player, player.itemTime.iconMeal,
                    (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) / 1000));
        }
        if (player.itemTime.isUseBanhChung) {
            sendItemTime(player, 10905,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhChung)) / 1000));
        }
        if (player.itemTime.isUseBanhTet) {
            sendItemTime(player, 10904,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTet)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet2) {
            sendItemTime(player, 10714,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2)) / 1000));
        }
        if (player.itemTime.isUseBoKhi2) {
            sendItemTime(player, 10715,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2)) / 1000));
        }
        if (player.itemTime.isUseGiapXen2) {
            sendItemTime(player, 10712,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2)) / 1000));
        }
        if (player.itemTime.isUseCuongNo2) {
            sendItemTime(player, 10716,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2)) / 1000));
        }
        // x2 x3 x4
        if (player.itemTime.isUseX2TNSM) {
            sendItemTime(player, 22386,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseX2TNSM)) / 1000));
        }
        if (player.itemTime.isUseX3TNSM) {
            sendItemTime(player, 22387,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseX3TNSM)) / 1000));
        }
        if (player.itemTime.isUseX4TNSM) {
            sendItemTime(player, 22388,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseX4TNSM)) / 1000));
        }
        // Group 1 icon group
        if (player.itemTime.isUseGroup_1_1) {
            sendItemTime(player, 11257,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_1_1)) / 1000));
        }
        if (player.itemTime.isUseGroup_1_2) {
            sendItemTime(player, 11247,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_1_2)) / 1000));
        }
        if (player.itemTime.isUseGroup_1_3) {
            sendItemTime(player, 22388,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_1_3)) / 1000));
        }
        // Group 2 icon group
        if (player.itemTime.isUseGroup_2_1) {
            sendItemTime(player, 22472,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_2_1)) / 1000));
        }
        if (player.itemTime.isUseGroup_2_2) {
            sendItemTime(player, 22471,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_2_2)) / 1000));
        }
        if (player.itemTime.isUseGroup_2_3) {
            sendItemTime(player, 22388,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_2_3)) / 1000));
        }
        // Group 3 icon group
        if (player.itemTime.isUseGroup_3_1) {
            sendItemTime(player, 11258, // vợt bắt bọ
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_3_1)) / 1000));
        }
        if (player.itemTime.isUseGroup_3_2) {
            sendItemTime(player, 22181,
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_3_2)) / 1000));
        }
        if (player.itemTime.isUseGroup_3_3) {
            sendItemTime(player, 12759,
                    (int) ((TIME_60_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_3_3)) / 1000));
        } // Group 4 icon group
        if (player.itemTime.isUseGroup_4_1) { // giảm 90% dame quái
            sendItemTime(player, 5829,
                    (int) ((TIME_90_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_4_1)) / 1000));
        }
        if (player.itemTime.isUseGroup_4_2) {
            sendItemTime(player, 22388,
                    (int) ((TIME_90_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_4_2)) / 1000));
        }
        if (player.itemTime.isUseGroup_4_3) {
            sendItemTime(player, 22388,
                    (int) ((TIME_90_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_4_3)) / 1000));
        }
        // Bánh trung thu
        if (player.itemTime.isUseBanhTrungThu_1) {
            sendItemTime(player, 4042,
                    (int) ((TIME_60_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu_1))
                    / 1000));
        }
        if (player.itemTime.isUseBanhTrungThu_2) {
            sendItemTime(player, 4043,
                    (int) ((TIME_90_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu_2))
                    / 1000));
        }
        if (player.itemTime.isUseBanhTrungThu_3) {
            sendItemTime(player, 4125,
                    (int) ((TIME_120_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu_3))
                    / 1000));
        }
        if (player.itemTime.isUseBanhTrungThu_4) {
            sendItemTime(player, 4126,
                    (int) ((TIME_150_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTrungThu_4))
                    / 1000));
        }
        // Group 5 10 phút
        if (player.itemTime.isUseGroup_5_1) {
            sendItemTime(player, 1712,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_1)) / 1000));
        }
        if (player.itemTime.isUseGroup_5_2) {
            sendItemTime(player, 1713,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_2)) / 1000));
        }
        if (player.itemTime.isUseGroup_5_3) {
            sendItemTime(player, 14415,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_3)) / 1000));
        }
        if (player.itemTime.isUseGroup_5_4) {
            sendItemTime(player, 14418,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_4)) / 1000));
        }
        if (player.itemTime.isUseGroup_5_5) {
            sendItemTime(player, 4083,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_5)) / 1000));
        }
        if (player.itemTime.isKeoBayTay) {
            sendItemTime(player, 14423,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeKeoBanTay)) / 1000));
        }
        if (player.itemTime.isUseGroup_5_6) {
            sendItemTime(player, 5072,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_6)) / 1000));
        }
        if (player.itemTime.isUseGroup_5_7) {
            sendItemTime(player, 26329,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_7)) / 1000));
        }
        if (player.itemTime.isUseGroup_5_8) {
            sendItemTime(player, 5829,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_8)) / 1000));
        }
        if (player.itemTime.isUseGroup_5_9) {
            sendItemTime(player, 18062,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_9)) / 1000));
        }
        if (player.itemTime.isUseGroup_5_10) {
            sendItemTime(player, 421,
                    (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_5_10)) / 1000));
        }
        // Group 6 30 phút
        if (player.itemTime.isUseGroup_6_1) {
            sendItemTime(player, 421,
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_6_1)) / 1000));
        }
        if (player.itemTime.isUseGroup_6_2) {
            sendItemTime(player, 421,
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_6_2)) / 1000));
        }
        if (player.itemTime.isUseGroup_6_3) {
            sendItemTime(player, 421,
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_6_3)) / 1000));
        }
        if (player.itemTime.isUseGroup_6_4) {
            sendItemTime(player, 421,
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_6_4)) / 1000));
        }
        if (player.itemTime.isUseGroup_6_5) {
            sendItemTime(player, 421,
                    (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_6_5)) / 1000));
        } // Group 7 60 phút
        if (player.itemTime.isUseGroup_7_1) {
            sendItemTime(player, 8579,
                    (int) ((TIME_20_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_7_1)) / 1000));
        }
        if (player.itemTime.isUseGroup_7_2) {
            sendItemTime(player, 8580,
                    (int) ((TIME_20_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_7_2)) / 1000));
        }
        if (player.itemTime.isUseGroup_7_3) {
            sendItemTime(player, 26040,
                    (int) ((TIME_60_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_7_3)) / 1000));
        }
        if (player.itemTime.isUseGroup_7_4) {
            sendItemTime(player, 8581,
                    (int) ((TIME_60_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_7_4)) / 1000));
        }
        if (player.itemTime.isUseGroup_7_5) {
            sendItemTime(player, 8582,
                    (int) ((TIME_60_MIN - (System.currentTimeMillis() - player.itemTime.lastTimeUseGroup_7_5)) / 1000));
        }
    }

    // bật tđlt
    public void turnOnTDLT(Player player, Item item) {
        int min = 0;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                min = io.param;
                io.param = 0;
                break;
            }
        }
        player.itemTime.isUseTDLT = true;
        player.itemTime.timeTDLT = min * 60 * 1000;
        player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();
        sendCanAutoPlay(player);
        sendItemTime(player, 4387, player.itemTime.timeTDLT / 1000);
        InventoryService.gI().sendItemBags(player);
    }

    // tắt tđlt
    public void turnOffTDLT(Player player, Item item) {
        player.itemTime.isUseTDLT = false;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                io.param = (short) ((player.itemTime.timeTDLT
                        - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000);
                break;
            }
        }
        sendCanAutoPlay(player);
        removeItemTime(player, 4387);
        InventoryService.gI().sendItemBags(player);
    }

    public void sendCanAutoPlay(Player player) {
        Message msg;
        try {
            msg = new Message(-116);
            msg.writer().writeByte(player.itemTime.isUseTDLT ? 1 : 0);
            player.sendMessage(msg);
        } catch (Exception e) {
            Log.error(ItemTimeService.class, e);
        }
    }

    public void sendTextDoanhTrai(Player player) {
        if (player.clan != null && !player.clan.haveGoneDoanhTrai
                && player.clan.timeOpenDoanhTrai != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenDoanhTrai) / 1000);
            int secondsLeft = (DoanhTrai.TIME_DOANH_TRAI / 1000) - secondPassed;
            sendTextTime(player, DOANH_TRAI, "Doanh trại độc nhãn", secondsLeft);
        }
    }

    public void sendTextAuction(Player player) {
        if (AuctionService.gI().isJoined(player)) {
            int second = (int) (AuctionService.gI().getTimeFinish() - System.currentTimeMillis()) / 1000;
            this.sendTextTime(player, (byte) 4, "Phiên đấu giá", second);
        }
    }

    public void sendTextBanDoKhoBau(Player player) {
        if (player.clan != null
                && player.clan.timeOpenBanDoKhoBau != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenBanDoKhoBau) / 1000);
            int secondsLeft = (BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000) - secondPassed;
            sendTextTime(player, BAN_DO_KHO_BAU, "Bản đồ kho báu", secondsLeft);
        }
    }

    public void sendTextBanDoKhoBauNew(Player player) {
        int secondsLeft = (int) (player.charms.tdDeTuMabu2 - System.currentTimeMillis())
                / 1000;// giây
        if (secondsLeft > 0) {

            sendTextTime(player, BAN_DO_KHO_BAU, "Đản đồ kinh nghiệm", secondsLeft);
        }
    }

    public void sendTextBauCua(Player player) {
        int time = (int) ((Chonaiday.gI().lastTimeEnd - System.currentTimeMillis()) / 1000);

        sendTextTime(player, BAU_CUA, "Vận May", time);

    }

    public void removeTextDoanhTrai(Player player) {
        removeTextTime(player, DOANH_TRAI);
    }

    public void removeTextTime(Player player, byte id) {
        sendTextTime(player, id, "", 0);
    }

    private void sendTextTime(Player player, byte id, String text, int seconds) {
        Message msg;
        try {
            msg = new Message(65);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(seconds);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemTime(Player player, int itemId, int time) {
        Message msg;
        try {
            msg = new Message(-106);
            msg.writer().writeShort(itemId);
            msg.writer().writeShort(time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void removeItemTime(Player player, int itemTime) {
        sendItemTime(player, itemTime, 0);
    }

}
