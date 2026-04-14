package nro.models.npc.npcList;

import nro.consts.ConstMap;
import nro.consts.ConstNpc;
import nro.models.map.war.DaichienWar;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.services.func.ShopService;
import nro.utils.Log;
import nro.utils.Util; // Assuming numberToMoney is in Util class

/**
 *
 * @author KENLT
 */
public class TrongtaiB extends Npc {

    public TrongtaiB(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
public void openBaseMenu(Player player) {
    if (canOpenNpc(player)) {
        if (this.mapId == 188) {
            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ đưa cậu về", "OK", "Từ chối");
        }
        DaichienWar.gI().setTime();
        if (this.mapId == 13) {
            try {
                if (DaichienWar.gI().isEventOpen()) {
                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_DCB,
                            "Map đại chiến đang mở, "
                            + "ngươi có muốn tham gia không?",
                            "Tham gia", "Cửa hàng\nĐặc Biệt", "Bảng\n Xếp hạng", "Hướng dẫn\nthêm", "Đóng");
                } else {
                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_DCB,
                            "Chưa đến giờ thi đấu\nThời gian từ: 12h00 - 13h00 và 20h00 - 21h00", "Cửa Hàng", "Bảng Xếp Hạng", "Hướng dẫn", "Đóng");
                }
            } catch (Exception ex) {
                Log.error("Lỗi mở menu Npc trọng tài");
            }
        }
    }
}

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (player.iDMark.getIndexMenu()) {
                case ConstNpc.MENU_OPEN_DCB:
                    if (select == 0) {
                        if (!player.getSession().actived) {
                            Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để có thể sử dụng");
                            return;
                        }
                        if (player.joinfree < 1) {
                            this.createOtherMenu(player, ConstNpc.OPEN_THAM_GIA_DC_FREE,
                                    "Mỗi ngày ngươi có một lượt tham gia miễn phí\n"
                                    + "Ngươi sẽ có 15 phút để tham gia, ngươi đã sẵn sàng?", "Tham gia\n(Miễn phí)", "Từ chối");
                        } else if (player.joindaichien < 5) {
                            long cost = (long) (player.joindaichien == 0 ? 1 : player.joindaichien) * 100_000_000;
                            this.createOtherMenu(player, ConstNpc.OPEN_THAM_GIA_DC_FREE,
                                    "Ngươi có muốn trả phí để tiếp tục tham gia không?", "Tham gia\n(" + Util.numberToMoney(cost) + ")", "Từ chối");
                        } else {
                            Service.getInstance().sendThongBaoFromAdmin(player, "Ngươi đã hết lượt tham gia Đại Chiến! Hãy quay lại vào ngày mai!");
                        }
                    } else if (select == 1) {
                        ShopService.gI().openShopSpecial(player, this,
                                ConstNpc.SHOP_BANG_HOI, 0, -1);
                    } else if (select == 2) {
                          Service.getInstance().showTopClanPoint(player);
                    } else if (select == 3) {
                        Service.getInstance().sendThongBaoFromAdmin(player, "Thời gian diễn ra Đại chiến Bang hội từ 8h - 22h hàng ngày\n"
                                + " Hạ gục người chơi khác hoặc mộc nhân để nhận điểm tích lũy Hồng Ngọc\n"
                                + " Gặp Npc Trọng tài để mua các vật phẩm giá trị bằng Hồng Ngọc\n");
                    }
                    break;
                case ConstNpc.MENU_NOT_OPEN_DCB:
                    if (select == 0) {
                        ShopService.gI().openShopSpecial(player, this,
                                ConstNpc.SHOP_BANG_HOI, 0, -1);
                    } else if (select == 1) {
                         Service.getInstance().showTopClanPoint(player);
                    } else if (select == 2) {
                        Service.getInstance().sendThongBaoFromAdmin(player, "Thời gian diễn ra Đại chiến Bang hội từ 8h - 22h hàng ngày\n"
                                + " Hạ gục người chơi khác hoặc mộc nhân để nhận điểm tích lũy Hồng Ngọc\n"
                                + " Gặp Npc Trọng tài để mua các vật phẩm giá trị bằng Hồng Ngọc\n");
                    } else if (select == 3) {
                        Service.getInstance().sendThongBao(player, "cửa hàng");
                    }
                    break;
                case ConstNpc.OPEN_THAM_GIA_DC_FREE:
                    if (select == 0) {
                        if (!player.getSession().actived) {
                            Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để có thể sử dụng");
                            return;
                        }
                        if (player.joinfree < 1) {
                            player.joinfree++;
                            ChangeMapService.gI().changeMap(player, ConstMap.DAI_CHIEN_BANG, -1, 190, 312);
                            Service.getInstance().sendThongBao(player, "Đã tham gia Đại Chiến miễn phí, còn " + (1 - player.joinfree) + " lượt miễn phí");
                        } else if (player.joindaichien < 5) {
                            long cost = (long) (player.joindaichien == 0 ? 1 : player.joindaichien) * 100_000_000;
                            if (player.inventory.gold >= cost) {
                                player.inventory.gold -= cost;
                                Service.getInstance().sendMoney(player);
                                player.joindaichien = player.joindaichien == 0 ? 1 : player.joindaichien * 2;
                                ChangeMapService.gI().changeMap(player, ConstMap.DAI_CHIEN_BANG, -1, 190, 312);
                                Service.getInstance().sendThongBao(player, "Đã tham gia Đại Chiến, mất " + Util.numberToMoney(cost) + " vàng");
                            } else {
                                Service.getInstance().sendThongBao(player, "Ngươi không đủ " + Util.numberToMoney(cost) + " vàng để tham gia!");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Ngươi đã hết lượt tham gia Đại Chiến! Hãy quay lại vào ngày mai!");
                        }
                    }
                    break;
            }
            if (this.mapId == 188) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            ChangeMapService.gI().changeMap(player, ConstMap.DAO_GURU, -1, 244, 264);
                            return;
                    }
                }
            }
        }
    }
}
