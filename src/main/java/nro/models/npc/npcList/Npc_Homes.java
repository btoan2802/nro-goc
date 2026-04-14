package nro.models.npc.npcList;

import java.text.DecimalFormat;
import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.NpcMethod;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.services.func.Input;
import nro.services.func.ShopService;

public class Npc_Homes extends Npc {

    public Npc_Homes(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con cố gắng theo %1 học thành tài,"
                                + " đừng lo lắng cho ta.".replaceAll("%1", player.gender == 0 ? "Quy lão Kamê" : (player.gender == 1 ? "Trưởng lão Guru" : "Vua Vegeta")),
                        "Chức năng\nQuy đổi",
                        "Nhận\nNgọc xanh\n(Miễn phí)",
                        "Nhận\nHồng ngọc\n(Miễn phí)",
                        "Gift-Code",
                        "Giftcode\n Riêng",
                        "Nhận\nĐệ tử",
                        "Khu\n Test Dame",
                        "Đóng");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0:
                        this.createOtherMenu(player, 1, "Con đang có " + fm.format(player.getSession().VND) + " VNĐ"
                                + "\n|2|Con muốn làm gì nào ?\n"
                                + "|7|Mở GD khi hoàn thành nhiệm vụ fide",
                                 "Đổi\nThỏi Vàng", "Đóng");
                        break;
                    case 1:
                        NpcMethod.gI().NhanNgocXanh(player, this);
                        break;
                    case 2:
                        NpcMethod.gI().NhanHongNgoc(player, this);
                        break;
                    case 3:
                        Input.gI().createFormGiftCode(player);
                        break;
                    case 4:
                        Input.gI().createFormGiftCodeVip(player);
                        break;
                    case 5:
                        NpcMethod.gI().NhanDeTu(player, this);
                        break;
                    case 6:
                        ChangeMapService.gI().changeMapBySpaceShip(player, 182, -1,
                                254);
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == 1) {
                switch (select) {
//                    case 0:
//                        OpenThanhVienFree(player);
//                        break;
//                    case 1:
//                        OpenThanhVienFree(player);
//                        break;
                        case 0:
                   Input.gI().createFormDoiVND(player);
                        break;     
                    default:
                        break;
                }
            }      
        }
    }

    DecimalFormat fm = new DecimalFormat("##,###");

    public void OpenThanhVien(Player player) {
        if (!player.getSession().actived) {
            int vang_tru = 10_000;
            if (player.getSession().VND >= vang_tru) {
                PlayerDAO.subVND(player.getSession().userId, vang_tru);
                PlayerDAO.ActivedPlayer(player.getSession().userId);
                player.getSession().actived = true;
                player.getSession().VND -= vang_tru;
                Service.getInstance().sendThongBao(player, "Kích hoạt thành viên thành công !");
            } else {
                Service.getInstance().sendThongBao(player, "Còn thiếu " + fm.format(vang_tru - player.getSession().VND) + " để thực hiện !");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Con đã là thành viên rồi mà !");
        }
    }

    public void OpenThanhVienFree(Player player) {
        if (!player.getSession().actived) {
            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_21_3) {
                PlayerDAO.ActivedPlayer(player.getSession().userId);
                player.getSession().actived = true;
                Service.getInstance().sendThongBao(player, "Kích hoạt thành viên thành công !");
            } else {
                Service.getInstance().sendThongBao(player, "Bạn chưa qua nhiệm vụ fide");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Con đã là thành viên rồi mà !");
        }
    }
}
