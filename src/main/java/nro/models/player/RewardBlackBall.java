package nro.models.player;

import nro.services.Service;
import nro.utils.Logger;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.util.Date;
import nro.models.item.Item;
import nro.models.map.war.BlackBallWar;
import nro.services.InventoryService;
import nro.services.ItemService;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class RewardBlackBall {

    private static final int TIME_REWARD = 79200000;

    public static final int R1S = 10; // +SDG
    public static final int R2S = 15; // +HP
    public static final int R3S = 15; // +KI
    public static final int R4S = 35; // TNSM
    public static final int R5S = 10; // ĐÁ NÂNG CẤP
    public static final int R6S = 10; // SAO PHA LÊ
//    public static final int R7S = 20; // THỎI VÀNG
    public static final int R7S = 5; // % sdcm

    public static final int TIME_WAIT = 3600000;

    private Player player;

    public long[] timeOutOfDateReward;
    public long[] lastTimeGetReward;

    public RewardBlackBall(Player player) {
        this.player = player;
        this.timeOutOfDateReward = new long[7];
        this.lastTimeGetReward = new long[7];
    }

    public void reward(byte star) {
        this.timeOutOfDateReward[star - 1] = System.currentTimeMillis() + TIME_REWARD;
        this.lastTimeGetReward[star - 1] = 1;
        Service.getInstance().point(player);
    }

    public void getRewardSelect(byte select) {
        int index = 0;
        for (int i = 0; i < timeOutOfDateReward.length; i++) {
            if (timeOutOfDateReward[i] > System.currentTimeMillis()) {
                index++;
                if (index == select + 1) {
                    getReward(i + 1);
                    break;
                }
            }
        }
    }

    private void getReward(int R7S) {
        if (timeOutOfDateReward[R7S - 1] > System.currentTimeMillis()) {
            // && Util.canDoWithTime(lastTimeGetReward[R7S - 1], TIME_WAIT)
            switch (R7S) {
                case 1:
                case 2:
                case 3:
                case 4:
                    Service.getInstance().sendThongBao(player, "Phần thưởng chỉ số tự động nhận");
                    break;
                case 5:
                    if (lastTimeGetReward[R7S - 1] == 1) {
                        if (lastTimeGetReward[R7S - 1] == 0) {
                            Service.getInstance().sendThongBao(player,
                                    "Bạn không có phần thưởng sao đen này");
                        } else if (lastTimeGetReward[R7S - 1] == 99) {
                            Service.getInstance().sendThongBao(player,
                                    "Bạn đã nhận thưởng sao đen rồi");
                        } else {
                            if (player.getSession().actived) {
                                if (InventoryService.gI().getCountEmptyBag(player) >= 5) {
                                    lastTimeGetReward[R7S - 1] = 99;
                                    for (int i = 0; i < 5; i++) {
                                        Item tv = ItemService.gI().createNewItem((short) (220 + i),
                                                RewardBlackBall.R5S);
                                        InventoryService.gI().addItemBag(player, tv, 999);
                                    }
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn vừa nhận được " + RewardBlackBall.R5S + " viên đá nâng cấp các loại");

                                } else {
                                    Service.getInstance().sendThongBao(player, "Hành trang cần 5 ô trống!");
                                }

                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Hãy mở thành viên tại trang chủ để nhận phần thưởng!");
                            }
                        }
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Bạn không có phần thưởng sao đen");
                    }
                    break;
                case 6:
                    if (lastTimeGetReward[R7S - 1] == 1) {
                        if (lastTimeGetReward[R7S - 1] == 0) {
                            Service.getInstance().sendThongBao(player,
                                    "Bạn không có phần thưởng sao đen này");
                        } else if (lastTimeGetReward[R7S - 1] == 99) {
                            Service.getInstance().sendThongBao(player,
                                    "Bạn đã nhận thưởng sao đen rồi");
                        } else {
                            if (player.getSession().actived) {
                                if (InventoryService.gI().getCountEmptyBag(player) >= 7) {
                                    lastTimeGetReward[R7S - 1] = 99;
                                    for (int i = 0; i < 7; i++) {
                                        Item tv = ItemService.gI().createNewItem((short) (441 + i),
                                                RewardBlackBall.R6S);
                                        InventoryService.gI().addItemBag(player, tv, 999);
                                    }
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn vừa nhận được " + RewardBlackBall.R6S + " viên đá nâng cấp các loại");

                                } else {
                                    Service.getInstance().sendThongBao(player, "Hành trang cần 7 ô trống!");
                                }

                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Hãy mở thành viên tại trang chủ để nhận phần thưởng!");
                            }
                        }
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Bạn không có phần thưởng sao đen");
                    }
                    break;
                case 7:
                    if (lastTimeGetReward[R7S - 1] == 1) {
                        if (lastTimeGetReward[R7S - 1] == 0) {
                            Service.getInstance().sendThongBao(player,
                                    "Bạn không có phần thưởng sao đen này");
                        } else if (lastTimeGetReward[R7S - 1] == 99) {
                            Service.getInstance().sendThongBao(player,
                                    "Bạn đã nhận thưởng sao đen rồi");
                        } else {
                            if (player.getSession().actived) {
                                if (InventoryService.gI().getCountEmptyBag(player) >= 1) {
                                    lastTimeGetReward[R7S - 1] = 99;
//                                    Item tv = ItemService.gI().createNewItem((short) 457, RewardBlackBall.R7S);
//                                    InventoryService.gI().addItemBag(player, tv, 20);
//                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + RewardBlackBall.R7S/* + " " + tv.template.name*/);
                                    Logger.errorSaveHistGoldBar(player, RewardBlackBall.R7S, (byte) 4,
                                            "Nhận thưởng ngọc rồng sao đen " + R7S + " sao");
                                    // lastTimeGetReward[R7S - 1] = (System.currentTimeMillis() + (60 * 60 * 24 *
                                    // 1000));
                                } else {
                                    Service.getInstance().sendThongBao(player, "Hành trang cần 1 ô trống!");
                                }

                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Hãy mở thành viên tại trang chủ để nhận phần thưởng!");
                            }
                        }
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Bạn không có phần thưởng sao đen");
                    }

                    // if (player.inventory.gold + R7S <= player.inventory.getGoldLimit()) {
                    // player.inventory.gold += R7S;
                    // Service.getInstance().sendMoney(player);
                    // lastTimeGetReward[star - 1] = System.currentTimeMillis();
                    // } else {
                    // Service.getInstance().sendThongBao(player, "Vàng sau khi nhận vượt quá tối
                    // đa!");
                    // }
                    break;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn không có phần thưởng sao đen hôm nay");
            // Service.getInstance().sendThongBao(player, "Chưa thể nhận phần quà ngay lúc
            // này, vui lòng đợi "
            // + TimeUtil.diffDate(new Date(lastTimeGetReward[R7S - 1]),
            // new Date(lastTimeGetReward[R7S - 1] + TIME_WAIT),
            // TimeUtil.MINUTE)
            // + " phút nữa");
            // Service.getInstance().sendThongBao(player, "Chưa thể nhận phần quà ngay lúc
            // này, vui lòng đợi "
            // + TimeUtil.diffDate(new Date(lastTimeGetReward[R7S - 1]),
            // new Date(lastTimeGetReward[R7S - 1] + TIME_WAIT),
            // TimeUtil.MINUTE)
            // + " phút nữa");
        }
    }

    public void dispose() {
        this.player = null;
    }
}
