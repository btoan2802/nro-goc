package nro.models.npc.npcList;

import static nro.server.Manager.EVENT_COUNT_QUY_LAO_KAME;
import static nro.server.Manager.EVENT_SEVER;

import java.util.HashMap;

import nro.attr.Attribute;
import nro.consts.ConstAttribute;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.jdbc.daos.PlayerDAO;
import nro.models.boss.event.EscortedBoss;
import nro.models.boss.event.Qilin;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.npc.Npc;
import nro.models.npc.NpcFactory;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.services.BanDoKhoBauService;
import nro.services.ClanService;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.Event.EventService;
import nro.services.func.ChangeMapService;
import nro.services.func.Input;
import nro.utils.Util;

public class QuyLaoKame extends Npc {

    private final String[] textChat = new String[]{"Là lá la la..", "Buồn không em!", "Đông lạnh giá"};

    // Constructor
    public QuyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        super(mapId, status, cx, cy, tempId, avatar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 5:
                    EscortedBoss escortedBoss = player.getEscortedBoss();
                    if (escortedBoss != null && escortedBoss instanceof Qilin) {
                        this.createOtherMenu(player, ConstNpc.ESCORT_QILIN_MENU,
                                "Ah con đã tìm thấy Ông Già Noel\nTa sẽ thưởng cho con 1 Cá Tuyết.",
                                "Đồng ý", "Từ chối");
                    } else {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            String dua_top = " ";
                            if (player.isDuaTop) {
                                // dua_top = "|2|Con đang nằm trong danh sách đua top sức mạnh
                                // tuần";
                            }
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Chào con, con muốn ta giúp gì nào?\n"
                                    + "|7|Map kinh nghiệm x2 TNSM đó nha!\n"
//                                    + "|7|Đem x99 mảnh trứng tới đây ta đổi cho x1 trứng nhí!"
                                    ,
                                    "Giải tán\nbang hội",
                                    "Kho báu\nDưới Biển\n",
                                    "Map\n Kinh nghiệm",
                                     "Hồi Skill",
                                     "Tặng\nVớ","Đóng"
//                                    "Đổi mảnh\nTrứng Rồng"
                                    // "Mốc Quà"
                                    );
                            // "Đổi Quà\nSự Kiện",
                        }
                    }
                    break;
                case 179:
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào con, con muốn ta giúp gì nào?\nỞ đây sẽ được x2 TNSM đấy!",
                            "Về đảo Kame",
                            "Từ chối");
                    break;
            }

        }
    }

    @Override
    public void update() {
        //   System.out.println("chạy chạy");
        if (Util.canDoWithTime(this.lastTimeChat, (long) this.timeChat)) {
            this.lastTimeChat = System.currentTimeMillis();
            this.npcChat(this.textChat[Util.nextInt(0, this.textChat.length - 1)]);
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (mapId) {
                case 5:
                    FMap5(player, select);
                    break;
                case 179:
                    FMap179(player, select);
                    break;
            }
        }
    }

    private void FMap5(Player player, int select) {
        if (player.iDMark.isBaseMenu()) {
            switch (select) {
                case 0:
                    if (player.clan != null) {
                        this.createOtherMenu(player, ConstNpc.MENU_CONFIRM_DESTROY_CLAN, "|8|Con chắc chắn muốn hủy bỏ Bang hội chứ ?", "Đồng ý", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Bạn không có bang hội nào để giải tán.");
                    }
                    break;
                case 1: {
                    if (player.clan != null) {
                        if (player.clan.banDoKhoBau != null) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                    "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                    + player.clan.banDoKhoBau.level
                                    + "\nCon có muốn đi theo không?",
                                    "Đồng ý", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                    "Đây là bản đồ kho báu hải tặc \nCác con cứ yên tâm lên đường\n"
                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                    "Chọn\ncấp độ", "Từ chối");
                        }
                    } else {
                        this.npcChat(player,
                                "Con phải có bang hội ta mới có thể cho con đi");
                    }
                }

                break;
                case 2:
//                    if (TaskService.gI().getIdTask(player) <= ConstTask.TASK_19_3) {
//
//                        Service.getInstance().sendThongBao(player,
//                                "Hãy hoàn thành nhiệm vụ tiêu diệt Rambo trước");
//                        return;
//                    }
                    if (player.nPoint.power >= 10_000_000_000l) {
                        Item csbac = InventoryService.gI().findItemBagByTemp(player, 1447);
                        if (player.charms.tdDeTuMabu2 - System.currentTimeMillis() > 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 179, -1,
                                    627);
                        } else if (csbac != null && csbac.quantity >= 1) {
                            InventoryService.gI().subQuantityItemsBag(player, csbac, 1);
                            InventoryService.gI().sendItemBags(player);
                            player.charms.addTimeCharms(2076, 60);
                            ChangeMapService.gI().changeMapBySpaceShip(player, 179, -1,
                                    627);
                            Service.getInstance().sendThongBao(player,
                                    "Bạn vừa tiến vào bản đồ kinh nghiệm, bạn có 1h vào bản đồ miễn phí, sau 1h phi thuyền sẽ tự đưa về nhà");
                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Con cần phải có vé đại kinh nghiệm để vào bản đồ");
                            break;
                        }

                    } else {
                        Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh lớn hơn 10 tỷ");
                    }

                    break;
                case 3:
                    if (player.inventory.gold >= 250_000_000) {
                        long cooldownTime = 30 * 1000;
                        if (System.currentTimeMillis() - player.lastTimeSetSkillTime >= cooldownTime) {
                            for (Skill skill : player.playerSkill.skills) {
                                skill.lastTimeUseThisSkill = 0;
                            }
                            Service.getInstance().sendTimeSkill(player);
                            player.lastTimeSetSkillTime = System.currentTimeMillis();
                            player.inventory.gold -= 250_000_000;
                            Service.getInstance().sendMoney(player);
                        } else {
                            long remainingTime = (cooldownTime - (System.currentTimeMillis() - player.lastTimeSetSkillTime)) / 1000;
                            Service.getInstance().sendThongBao(player, "Bạn cần đợi " + remainingTime + " giây nữa để thiết lập kỹ năng lại!");
                        }
                        Service.getInstance().sendThongBao(player, "Bạn đã hồi skill thành công");
                    } else {
                        Service.getInstance().sendThongBao(player, "Cần thêm " + Util.numberToMoney(250_000_000 - player.inventory.gold) + " vàng nữa để thực hiện !");
                    }
                    break;
                case 33: {
                    Item itemRequired = InventoryService.gI().findItemBagByTemp(player, 1549);
                    if (itemRequired != null && itemRequired.quantity >= 99) {
                        InventoryService.gI().subQuantityItemsBag(player, itemRequired, 99);
                        Item newItem = ItemService.gI().createNewItem((short) 1550);
                        InventoryService.gI().addItemBag(player, newItem, 1);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn vừa nhận được " + newItem.template.name + "!");
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Con cần đủ x99 mảnh trứng để đổi trứng rồng!");
                    }
                }
                break;
                case 4:
                     switch (EVENT_SEVER) {
                        case 3:
                                    Attribute at = ServerManager.gI().getAttributeManager()
                                            .find(ConstAttribute.SUC_DANH);
                                    String text = "|5|Sự kiện Noel chính thức tại Ngọc Rồng "
                                            + Manager.SERVER_NAME + "\n "
                                            + "\n|0|Tổng số Vớ đã tặng trên toàn máy chủ "
                                            + EVENT_COUNT_QUY_LAO_KAME % 999 + "/999";
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                                            at != null && !at.isExpired() ? text
                                            + "\n|7|Toàn bộ máy chủ được tăng 10% SD,thời gian còn lại "
                                            + at.getTime() / 60 + " phút."
                                            : text + "\n|7|Khi tặng đủ 999 Vớ, toàn bộ máy chủ được tăng 10% SD trong 60 phút\nCó tác dụng khi out ra vào lại",
                                            "Tặng 1\n Vớ", "Tặng\n10 Vớ", "Tặng\n99 Vớ");
                     
                                    break;
                                }
                                break;
                case 55:
                    this.createOtherMenu(player, ConstNpc.MENU_NHAN_THUONG_QL,
                            "Nhận thưởng tự động...", "Điểm danh\nHàng ngày",
                            "Quà\n Mốc nạp", "Từ chối");
                    break;
                case 6:
                    // EventService.gI().NhanQuaEventMoiNgay(player, this);
                    break;
                case 8:
                    // ChangeMapService.gI().changeMapInYard(player, 173, -1,
                    // Util.nextInt(600, 800));

                    break;

            }
        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CONFIRM_DESTROY_CLAN) {
            if (select == 0) {
                ClanService.gI().RemoveClanAll(player);
            }
        } else if (player.iDMark.getIndexMenu() == ConstNpc.SU_KIEN_RAITI) {
            if (select == 0) {
                this.createOtherMenu(player, 909,
                        "Hãy lựa chọn số lượng rương bạc mà con muốn đổi !", "Đổi\n1 rương",
                        "Đổi\n10 rương", "Đổi\n 100 rương", "Đóng");
            }
            if (select == 1) {
                this.createOtherMenu(player, 910,
                        "Hãy lựa chọn số lượng rương vàng mà con muốn đổi !",
                        "Đổi\n1 rương", "Đổi\n10 rương", "Đổi\n 100 rương", "Đóng");
            }
        } else if (player.iDMark.getIndexMenu() == ConstNpc.SU_KIEN_HOA_QUA) {
            EventService.gI().openMenuSuKien(player, this, tempId, select);
        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_SUKIEN) {
            EventService.gI().openMenuSuKien(player, this, tempId, select);
        } else if (player.iDMark.getIndexMenu() == 859) {
            switch (select) {
                case 0:
                    this.openShop(player, ConstNpc.SHOP_DIEM_DANH, 2);
                    break;
                case 1:
                    this.openShop(player, ConstNpc.SHOP_ONLINE, 4);
                    break;
                case 2:
                    // this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    // "Vật phẩm đang được cập nhật, hãy quay lại sau nhé",
                    // "Đóng");
                    this.openShop(player, ConstNpc.SHOP_POWER, 6);
                    // this.openShop(player, ConstNpc.SHOP_FREE_DAY, 3);
                    break;
                case 3:
                    // this.openShop(player, ConstNpc.SHOP_MOC_NAP, 5);
                    break;
            }
        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
            switch (select) {
                case 0:
                    if (player.isAdmin()
                            || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                        ChangeMapService.gI().goToDBKB(player);
                    } else {
                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                    }
                    break;

            }
        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NHAN_THUONG_QL) {
            switch (select) {
                case 0:
                    if (player.getSession().actived) {
                        this.openShop(player, ConstNpc.SHOP_DIEM_DANH, 2);
                    } else {
                        Service.getInstance().sendThongBaoFromAdmin(player, "Bạn cần kích hoạt thành viên "
                                + "để mở Shop Điểm Danh,"
                                + " nhằm tránh lạm phát!");
                    }
                    break;
                case 1:
                    this.openShop(player, ConstNpc.SHOP_MOC_NAP, 5);
                    break;
            }
        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
            switch (select) {
                case 0:
                    if (player.isAdmin()
                            || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                        Input.gI().createFormChooseLevelBDKB(player);
                    } else {
                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                    }
                    break;
            }

        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
            switch (select) {
                case 0:
                    int level = Byte.parseByte(String.valueOf(NpcFactory.PLAYERID_OBJECT.get(player.id)));
                    if (level >= 1 && level <= 110) {
                        BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) level);
                    } else {
                        this.npcChat(player, "Con hãy chọn cấp từ 1 đến 110");
                    }

                    break;
            }

        } else if (player.iDMark.getIndexMenu() == ConstNpc.ESCORT_QILIN_MENU) {
            switch (select) {
                case 0: {
                    if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                        this.npcChat(player,
                                "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                        return;
                    }
                    EscortedBoss escortedBoss = player.getEscortedBoss();
                    if (escortedBoss != null) {
                        escortedBoss.stopEscorting();
                        Item item = ItemService.gI()
                                .createNewItem((short) 1166);
                        item.quantity = 1;
                        item.itemOptions.add(new ItemOption(74, 0));
                        item.itemOptions.add(new ItemOption(93, 30));
                        InventoryService.gI().addItemBag(player, item, 0);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn nhận được " + item.template.name);
                    }
                }
                break;
            }
        } else if (player.iDMark.getIndexMenu() == 909) {
            Item hopcs = InventoryService.gI().findItemBagByTemp(player, 796);
            switch (select) {
                case 0: {
                    Item csbac = InventoryService.gI().findItemBagByTemp(player, 573);
                    if (hopcs != null && csbac != null && hopcs.quantity >= 10
                            && csbac.quantity >= 10) {
                        InventoryService.gI().subQuantityItemsBag(player, hopcs, 10);
                        InventoryService.gI().subQuantityItemsBag(player, csbac, 10);
                        Item rb = ItemService.gI().createNewItem((short) 571);
                        InventoryService.gI().addItemBag(player, rb, 99);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn vừa nhận được " + rb.template.name);
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Con phải có đủ X10 Hộp Capsule và X10 Capsule Bạc mới có thể đổi được Rương bạc từ ta!");
                        break;
                    }
                    break;
                }
                case 1: {
                    Item csbac = InventoryService.gI().findItemBagByTemp(player, 573);
                    if (hopcs != null && csbac != null && hopcs.quantity >= 100
                            && csbac.quantity >= 100) {
                        InventoryService.gI().subQuantityItemsBag(player, hopcs, 100);
                        InventoryService.gI().subQuantityItemsBag(player, csbac, 100);
                        Item rb = ItemService.gI().createNewItem((short) 571, 10);
                        InventoryService.gI().addItemBag(player, rb, 999);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn vừa nhận được " + rb.template.name);
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Con phải có đủ X100 Hộp Capsule và X100 Capsule Bạc mới có thể đổi được Rương bạc từ ta!");
                        break;
                    }
                    break;
                }
                case 2: {
                    Item csbac = InventoryService.gI().findItemBagByTemp(player, 573);
                    if (hopcs != null && csbac != null && hopcs.quantity >= 1000
                            && csbac.quantity >= 1000) {
                        InventoryService.gI().subQuantityItemsBag(player, hopcs, 1000);
                        InventoryService.gI().subQuantityItemsBag(player, csbac, 1000);
                        Item rb = ItemService.gI().createNewItem((short) 571, 100);
                        InventoryService.gI().addItemBag(player, rb, 999);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn vừa nhận được " + rb.template.name);
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Con phải có đủ X1000 Hộp Capsule và X1000 Capsule Bạc mới có thể đổi được Rương bạc từ ta!");
                        break;
                    }
                }
                break;
            }
        } else if (player.iDMark.getIndexMenu() == 910) {
            Item hopcs = InventoryService.gI().findItemBagByTemp(player, 796);
            switch (select) {
                case 0: {
                    Item csv = InventoryService.gI().findItemBagByTemp(player, 574);
                    if (hopcs != null && csv != null && hopcs.quantity >= 10
                            && csv.quantity >= 10) {
                        InventoryService.gI().subQuantityItemsBag(player, hopcs, 10);
                        InventoryService.gI().subQuantityItemsBag(player, csv, 10);
                        Item rv = ItemService.gI().createNewItem((short) 572);
                        InventoryService.gI().addItemBag(player, rv, 999);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn vừa nhận được " + rv.template.name);
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Con phải có đủ X10 Hộp Capsule và X10 Capsule Vàng mới có thể đổi được Rương bạc từ ta!");
                        break;
                    }
                    break;
                }
                case 1: {
                    Item csv = InventoryService.gI().findItemBagByTemp(player, 574);
                    if (hopcs != null && csv != null && hopcs.quantity >= 100
                            && csv.quantity >= 100) {
                        InventoryService.gI().subQuantityItemsBag(player, hopcs, 100);
                        InventoryService.gI().subQuantityItemsBag(player, csv, 100);
                        Item rv = ItemService.gI().createNewItem((short) 572, 10);
                        InventoryService.gI().addItemBag(player, rv, 999);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn vừa nhận được X10" + rv.template.name);
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Con phải có đủ X100 Hộp Capsule và X100 Capsule Vàng mới có thể đổi được Rương bạc từ ta!");
                        break;
                    }
                    break;
                }
                case 2: {
                    Item csv = InventoryService.gI().findItemBagByTemp(player, 574);
                    if (hopcs != null && csv != null && hopcs.quantity >= 1000
                            && csv.quantity >= 1000) {
                        InventoryService.gI().subQuantityItemsBag(player, hopcs, 1000);
                        InventoryService.gI().subQuantityItemsBag(player, csv, 1000);
                        Item rv = ItemService.gI().createNewItem((short) 572, 100);
                        InventoryService.gI().addItemBag(player, rv, 999);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player,
                                "Bạn vừa nhận được " + rv.template.name);
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Con phải có đủ X1000 Hộp Capsule và X1000 Capsule Vàng mới có thể đổi được Rương bạc từ ta!");
                        break;
                    }
                    break;
                }
            }
        }

    }

    private void FMap179(Player player, int select) {
        if (player.iDMark.isBaseMenu()) {
            switch (select) {
                case 0:
                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1,
                            627);
                    break;
                case 1:
                    break;
            }
        }

    }

}
