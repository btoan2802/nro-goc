package nro.models.npc.npcList;

import nro.consts.ConstNpc;
import nro.models.item.Item;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;

public class Vados extends Npc {

    public Vados(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 0:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta là người đang giữ rương quà cho ngươi, nếu có bất kì món quà nào hãy tới gặp ta để nhận."
                            + "\n Nhớ nhận ngay để không bị mất khi có quà mới nhé!",
                            "Bảng\n xếp hạng", "Xếp hạng\n Boss", "Xếp hạng\n Thăm quan", "Từ chối");
                    break;
                case 5:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Xin chào !\n"
                            + "|6|Ta đang chuẩn bị những món quà bí mật để chuẩn bị tặng cho Quy Lão đấy\n"
                            + "|4|Nhân ngày 20/11 này ta sẽ dành tặng cho Lão những đóa hoa tuyệt vời nhất "
                            + "nhưng hiện tại ta đang thiếu 1 số vật phẩm con có thể thu thập giùm ta không?",
                            "Bảng xếp hạng", "Đóa\n Hồng tươi", "Rương Quà", "Trang trí\n Thiệp", "Đóng");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 0:
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Service.getInstance().showTopPower(player);
                                break;
                            case 1:
                                //   Service.getInstance().showtopboss(player);
                                break;
                            case 2:
                                Service.getInstance().showTopPauCua(player);
                                break;
                        }
                    }
                    break;
                case 5:
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Service.getInstance().showTopSK(player);
                                break;
                            case 1:
                                CheTaoDoaHongTuoi(player);
                                break;
                            case 2:
                                this.createOtherMenu(player, 1, "|2|Con muốn thực hiện chế tạo loại rương nào dưới đây :\n"
                                        + "|4|+Rương thường: X10 Đóa hoa tươi,X1 Vỏ hộp quà,X1 Thỏi vàng\n"
                                        + "+Rương V.I.P: X15 Đóa hoa tươi,X1 Vỏ hộp quà và X1 Thỏi vàng\n"
                                        + "|8|Mở rương càng nhiều cơ hội trúng quà lớn càng cao đấy nhé !",
                                        "Thường", "VIP", "Đóng");
                                break;
                            case 3:
                                this.createOtherMenu(player, 2, "|2|Con muốn thực hiện trang trí loại nào dưới đây :\n"
                                        + "|3|+Thiệp Tri Ân : X10 Mảnh giấy,X99 Keo dán và X1 Bút màu"
                                        + "\n|4|+Thiệp thường : X10 Thiệp tri ân, X1 Vỏ hộp quà và X1 Thỏi vàng\n"
                                        + "+Thiệp V.I.P : X15 Thiệp tri ân,X1 Vỏ hộp quà và X1 Thỏi vàng\n"
                                        + "|8|Hãy lự chọn thật kỹ càng nhé !",
                                        "Thường", "VIP", "Thiệp \n Tri Ân", "Đóng");

                                break;
                            default:
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == 1) {
                        switch (select) {
                            case 0:
                            case 1:
                                CheTaoRuongQua(player, select);
                                break;
                            default:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 2) {
                        switch (select) {
                            case 0:
                            case 1:
                                TrangTriThiepThiAn(player, select);
                                break;
                            case 2:
                                CheTaoThiepTriAn(player);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    void CheTaoDoaHongTuoi(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (InventoryService.gI().getQuantity(player, 610) >= 99
                    && InventoryService.gI().getQuantity(player, 1496) >= 99
                    && InventoryService.gI().getQuantity(player, 1498) >= 1) {
                Item hoa = ItemService.gI().createNewItem((short) 1500);
                InventoryService.gI().addItemBag(player, hoa);
                InventoryService.gI().subQuantityItemsBag(player, (short) 610, 99);
                InventoryService.gI().subQuantityItemsBag(player, (short) 1496, 99);
                InventoryService.gI().subQuantityItemsBag(player, (short) 1498, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + hoa.getName());
            } else {
                Service.getInstance().sendThongBao(player, "Hãy mang đủ X99 Hoa hồng, X99 Giấy trắng và X1 Kéo để làm thành 1 Đóa Hoa Tươi !");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void CheTaoRuongQua(Player player, int type) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            int[] sl = {10, 3, 1};
            int[] slVIP = {15, 5, 1};
            int[] slR = (type == 1) ? slVIP : sl;
            if (InventoryService.gI().getQuantity(player, 1500) >= slR[0]
                    && InventoryService.gI().getQuantity(player, 1499) >= slR[1]
                    && InventoryService.gI().getQuantity(player, 457) >= slR[2]) {

                Item ruong = ItemService.gI().createNewItem((short) (1503 + type));
                InventoryService.gI().addItemBag(player, ruong);
                player.pointSK += 2;

                InventoryService.gI().subQuantityItemsBag(player, (short) 1500, slR[0]);
                InventoryService.gI().subQuantityItemsBag(player, (short) 1499, slR[1]);
                InventoryService.gI().subQuantityItemsBag(player, (short) 457, slR[2]);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance()
                        .sendThongBao(player, "Bạn vừa nhận được " + ruong.getName() + " và nhận được 2 điểm sự kiện !");
            } else {
                Service.getInstance().sendThongBao(player, "Hãy mang đủ đến X" + slR[0] + " Đóa hoa tươi, X" + slR[1] + " Hộp quà cũ và X" + slR[2] + " thỏi vàng để thực hiện !");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void CheTaoThiepTriAn(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (InventoryService.gI().getQuantity(player, 1496) >= 99
                    && InventoryService.gI().getQuantity(player, 1497) >= 99
                    && InventoryService.gI().getQuantity(player, 1506) >= 1) {

                Item vp = ItemService.gI().createNewItem((short) 1505);
                InventoryService.gI().addItemBag(player, vp);

                InventoryService.gI().subQuantityItemsBag(player, (short) 1496, 99);
                InventoryService.gI().subQuantityItemsBag(player, (short) 1497, 99);
                InventoryService.gI().subQuantityItemsBag(player, (short) 1506, 1);

                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + vp.getName());

            } else {
                Service.getInstance().sendThongBao(player, "Hãy mang đủ X99 Mảnh giấy trắng,X99 Kéo dán và X1 Bút màu để chế tạo!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void TrangTriThiepThiAn(Player player, int type) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            int[] sl = {10, 3, 1, 2};
            int[] slVIP = {15, 5, 1, 5};
            int[] slR = (type == 1) ? slVIP : sl;
            if (InventoryService.gI().getQuantity(player, 1505) >= slR[0]
                    && InventoryService.gI().getQuantity(player, 1499) >= slR[1]
                    && InventoryService.gI().getQuantity(player, 457) >= slR[2]) {

                Item thiep = ItemService.gI().createNewItem((short) (1501 + type));
                InventoryService.gI().addItemBag(player, thiep);
                player.pointSK += slR[3];

                InventoryService.gI().subQuantityItemsBag(player, (short) 1505, slR[0]);
                InventoryService.gI().subQuantityItemsBag(player, (short) 1499, slR[1]);
                InventoryService.gI().subQuantityItemsBag(player, (short) 457, slR[2]);
                InventoryService.gI().sendItemBags(player);

                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + thiep.getName() + " và " + slR[3] + " điểm sự kiện !");
            } else {
                Service.getInstance().sendThongBao(player, "Hãy mang đủ đến X" + slR[0] + " Thiệp tri ân, X" + slR[1] + " Hộp quà cũ và X" + slR[2] + " thỏi vàng để thực hiện !");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đày !");
        }
    }
}
