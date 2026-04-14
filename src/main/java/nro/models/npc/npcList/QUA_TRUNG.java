package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.consts.ConstPlayer;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.services.Event.EventService;
import nro.services.func.ShopService;
import nro.utils.Util;

public class QUA_TRUNG extends Npc {

    public QUA_TRUNG(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    private final long COST_AP_TRUNG_NHANH = 2000000000L;

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 187:
                    if (player.kaminEgg != null) {
                        player.kaminEgg.sendKaminEgg();
                        if (player.kaminEgg.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG,
                                    "|2|Bạn muốn nở trứng linh thú nhanh hơn không ?!",
                                    "Hủy bỏ\ntrứng",
                                    "Tăng tốc", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG,
                                    "|2|Tôi đã ấp đủ thời gian rồi, bạn hãy mở tôi nhé !",
                                    "Nở", "Hủy bỏ\ntrứng", "Đóng");
                        }
                    } else {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn cần có trứng Linh thú mua tại Bunnma để có thể ấp tại đây !", "Đóng");
                    }
                    break;
                default:
                    player.mabuEgg.sendMabuEgg();
                    if (player.mabuEgg.getSecondDone() != 0) {
                        this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG,
                                "Oa oa oa....",
                                "Hủy bỏ\ntrứng",
                                "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                    } else {
                        this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG,
                                "Oa oa oa...", "Nở",
                                "Hủy bỏ\ntrứng", "Đóng");
                    }
                    break;
            }

        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 187:
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.CAN_NOT_OPEN_EGG:
                            if (select == 0) {
                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                        "Bạn có chắc chắn muốn hủy bỏ trứng?", "Đồng ý", "Từ chối");
                            } else if (select == 1) {
                                this.createOtherMenu(player, 1, "Ngươi muốn tăng tốc thời gian nở trứng sao ?\n"
                                        + "Ta có thể tăng tốc thời gian nhưng sẽ phải đổi lại vật phẩm đấy nhé !\n"
                                        + "+ 1 Ngày : 30 Hồn linh thú và X2 Đá ngũ sắc\n"
                                        + "+ 5 Ngày : 99 Hồn linh thú và X5 Đá ngũ sắc\n"
                                        + "Ngươi có chắc muốn tăng tốc thời gian nở trứng không ?",
                                        "1 Ngày", "5 Ngày", "Đóng");
                            }
                            break;
                        case ConstNpc.CAN_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                            "Bạn có chắc chắn cho trứng nở?",
                                            "Nở ngay", "Từ chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng?", "Đồng ý",
                                            "Từ chối");
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_OPEN_EGG:
                            if (select == 0) {
                                player.kaminEgg.openEgg();
                            }
                            break;
                        case ConstNpc.CONFIRM_DESTROY_EGG:
                            if (select == 0) {
                                player.kaminEgg.destroyEgg();
                            }
                            break;
                        case 1:
                            switch (select) {
                                case 0:
                                case 1:
                                    NoTrungNhanh(player, select);
                                    break;
                                default:
                                    break;
                            }
                            break;
                    }
                    break;
                default:
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.CAN_NOT_OPEN_EGG:
                            if (select == 0) {
                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                        "Bạn có chắc chắn muốn hủy bỏ trứng?", "Đồng ý", "Từ chối");
                            } else if (select == 1) {
                                if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                    player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                    player.mabuEgg.timeDone = 0;
                                    Service.getInstance().sendMoney(player);

                                    player.mabuEgg.sendMabuEgg();
                                    Service.getInstance().sendThongBao(player,
                                            "Ấp nhanh thành công");
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn không đủ vàng để thực hiện, còn thiếu "
                                            + Util.numberToMoney(
                                                    (COST_AP_TRUNG_NHANH - player.inventory.gold))
                                            + " vàng");
                                }
                            }
                            break;
                        case ConstNpc.CAN_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                            "Bạn có chắc chắn cho trứng nở?\n"
                                            + "Đệ tử của bạn sẽ được thay thế bằng đệ tử Mabư",
                                            "Đệ tử\nTrái Đất", "Đệ tử\nNamếc",
                                            "Đệ tử\nXayda",
                                            "Từ chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng?", "Đồng ý",
                                            "Từ chối");
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                    break;
                                case 1:
                                    player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                    break;
                                case 2:
                                    player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_DESTROY_EGG:
                            if (select == 0) {
                                player.mabuEgg.destroyEgg();
                            }
                            break;
                    }
                    break;
            }

        }
    }

    void NoTrungNhanh(Player player, int select) {
        int slHon = -1;
        int slDNS = -1;
        switch (select) {
            case 0:
                slHon = 30;
                slDNS = 2;
                break;
            case 1:
                slHon = 99;
                slDNS = 5;
                break;
        }
        if (InventoryService.gI().getQuantity(player, 1490) >= slHon
                && InventoryService.gI().getQuantity(player, 674) >= slDNS) {

            InventoryService.gI().subQuantityItemsBag(player, (short) 1490, slHon);
            InventoryService.gI().subQuantityItemsBag(player, (short) 674, slDNS);
            InventoryService.gI().sendItemBags(player);
            switch (select) {
                case 0:
                    player.kaminEgg.subTimeDone(1, 0, 0, 0);
                    Service.getInstance().sendThongBao(player, "Thực hiện tăng tốc trứng 1 ngày thành công !");
                    break;
                case 1:
                    player.kaminEgg.subTimeDone(5, 0, 0, 0);
                    Service.getInstance().sendThongBao(player, "Thực hiện tăng tốc trứng 5 ngày thành công !");
                    break;
            }
            player.kaminEgg.sendKaminEgg();
        } else {
            Service.getInstance().sendThongBao(player, "Không đủ " + slHon + " Hồn linh thú hoặc " + slDNS + " Đá ngũ sắc để thực hiện !");
        }
    }
}
