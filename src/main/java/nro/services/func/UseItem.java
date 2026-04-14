package nro.services.func;

import nro.consts.*;
import nro.dialog.MenuDialog;
import nro.dialog.MenuRunable;
import nro.event.Event;
import nro.lib.RandomCollection;
import nro.manager.MiniPetManager;
import nro.manager.NamekBallManager;
import nro.manager.PetFollowManager;
import nro.models.boss.Boss;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.Game.SoiHecQuyn;
import nro.models.boss.Game.Xinbato;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.MinipetTemplate;
import nro.models.map.*;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.map.war.NamekBallWar;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.player.Inventory;
import nro.models.player.MiniPet;
import nro.models.player.PetFollow;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.models.task.TaskMain;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.*;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;
import org.apache.log4j.Logger;

import com.mysql.cj.x.protobuf.MysqlxCursor.Open;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nro.data.DataGame;
import nro.jdbc.daos.PlayerDAO;
import nro.models.npc.specialnpc.KaminEgg;
import nro.models.player.NPoint;
import nro.sendEff.SendEffect;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;
    private static final int ITEM_BODY_TO_BOX_PET_CT = 8;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static UseItem instance;
    private static final Logger logger = Logger.getLogger(UseItem.class);

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(Session session, Message msg) {
        Player player = session.player;
        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    if (player.activeBoxType == 1) {
                        InventoryService.gI().itemBoxToBodyOrBag(player, index);
                        TaskService.gI().checkDoneTaskGetItemBox(player);
                    } else if (player.activeBoxType == 2) {
                        InventoryService.gI().itemBoxToBodyOrBag_ct_pet(player, index);
                    } else {
                        Service.getInstance().sendThongBao(player, "Vui lòng mở rương trước!");
                    }
                    break;
                case ITEM_BAG_TO_BOX:
                    if (player.activeBoxType == 1) {
                        InventoryService.gI().itemBagToBox(player, index);
                    } else if (player.activeBoxType == 2) {
                        InventoryService.gI().itemBagToBox_ct_pet(player, index);
                    } else {
                        Service.getInstance().sendThongBao(player, "Vui lòng mở rương trước!");
                    }
                    break;
                case ITEM_BODY_TO_BOX:
                    if (player.activeBoxType == 1) {
                        InventoryService.gI().itemBodyToBox(player, index);
                    } else if (player.activeBoxType == 2) {
                        InventoryService.gI().itemBodyToBox_ct_pet(player, index);
                    } else {
                        Service.getInstance().sendThongBao(player, "Vui lòng mở rương trước!");
                    }
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryService.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryService.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    Item item = player.inventory.itemsBag.get(index);
                    if (item != null && item.template != null && item.template.type == 39) {
                        Service.getInstance().sendThongBao(player, "Vui lòng tới Thợ Ngọc\ntại Đảo Kame để Thăng \nhoa cho đệ tử sử dụng!");
                    } else {
                        InventoryService.gI().itemBagToPetBody(player, index);
                    }
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryService.gI().itemPetBodyToBag(player, index);
                    break;
//                case ITEM_BODY_TO_BOX_PET_CT:
//                    InventoryService.gI().itemBagToBox_ct_pet(player, index);
//                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            PlayerService.gI().sendPetFollow(player);
            Service.getInstance().point(player);
        } catch (Exception e) {
            Log.error(UseItem.class, e);

        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            if (index >= 0 && index < player.inventory.itemsBag.size()) {
                                Item item = player.inventory.itemsBag.get(index);
                                if (item.isNotNullItem()) {

                                    if (item.template.type == 22) {
                                        if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                                            msg = new Message(-43);
                                            msg.writer().writeByte(type);
                                            msg.writer().writeByte(where);
                                            msg.writer().writeByte(index);
                                            msg.writer().writeUTF("Bạn có muốn dùng "
                                                    + player.inventory.itemsBag.get(index).template.name + "?");
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không thể dùng ở map này");
                                        }
                                    } else if (item.template.type == 7) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Bạn chắc chắn học "
                                                + player.inventory.itemsBag.get(index).template.name + "?");
                                        player.sendMessage(msg);
                                    } else if (player.isVersionAbove(220) && item.template.type == 23
                                            || item.template.type == 24 || item.template.type == 11) {
                                        InventoryService.gI().itemBagToBody(player, index);
                                    } else if (item.template.id == 401) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF(
                                                "Sau khi đổi đệ sẽ mất toàn bộ trang bị trên người đệ tử nếu chưa tháo");
                                        player.sendMessage(msg);
                                    } else if (item.getType() == 98) {
                                        MinipetTemplate temp = MiniPetManager.gI().findByID(item.getId());
                                        if (temp == null) {
                                            System.err.println("khong tim thay minipet id: " + item.getId());
                                        }
                                        MiniPet.callMiniPet(player, item.template.id, item.template.name);
                                        InventoryService.gI().itemBagToBody(player, index);
                                    } else if (item.getType() == 73) {
                                        InventoryService.gI().itemBagToBody(player, index);
                                        Service.getInstance().sendEffPlayer(player);
                                    } else if (item.getType() == 99) {
                                        PetFollow pet = PetFollowManager.gI().findByID(item.getId());
                                        player.setPetFollow(pet);
                                        InventoryService.gI().itemBagToBody(player, index);
                                        PlayerService.gI().sendPetFollow(player);
                                    } else {
                                        useItem(player, item, index);
                                    }
                                }
                            }
                        } else {
                            InventoryService.gI().eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            if (index >= 0 && index < player.inventory.itemsBody.size()) {
                                item = player.inventory.itemsBody.get(index);
                            }
                        } else {
                            if (index >= 0 && index < player.inventory.itemsBag.size()) {
                                item = player.inventory.itemsBag.get(index);
                            }
                        }
                        if (item != null && item.isNotNullItem()) {
                            msg = new Message(-43);
                            msg.writer().writeByte(type);
                            msg.writer().writeByte(where);
                            msg.writer().writeByte(index);
                            msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                            player.sendMessage(msg);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryService.gI().throwItem(player, where, index);
                    break;
                case ACCEPT_USE_ITEM:
                    if (index >= 0 && index < player.inventory.itemsBag.size()) {
                        Item item = player.inventory.itemsBag.get(index);
                        if (item.isNotNullItem()) {
                            useItem(player, item, index);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            Log.error(UseItem.class, e);
        }
    }

    public void useSatellite(Player player, Item item) {
        Satellite satellite = null;
        if (player.zone != null) {
            int count = player.zone.getSatellites().size();
            if (count < 3) {
                switch (item.template.id) {
                    case ConstItem.VE_TINH_TRI_LUC:
                        satellite = new SatelliteMP(player.zone, ConstItem.VE_TINH_TRI_LUC, player.location.x,
                                player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_TRI_TUE:
                        satellite = new SatelliteExp(player.zone, ConstItem.VE_TINH_TRI_TUE, player.location.x,
                                player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_PHONG_THU:
                        satellite = new SatelliteDefense(player.zone, ConstItem.VE_TINH_PHONG_THU, player.location.x,
                                player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_SINH_LUC:
                        satellite = new SatelliteHP(player.zone, ConstItem.VE_TINH_SINH_LUC, player.location.x,
                                player.location.y, player);
                        break;
                }
                if (satellite != null) {
                    InventoryService.gI().subQuantityItemsBag(player, item, 1);
                    Service.getInstance().dropItemMapForMe(player, satellite);
                    Service.getInstance().dropItemMap(player.zone, satellite);
                }
            } else {
                Service.getInstance().sendThongBaoOK(player,
                        "Số lượng vệ tinh có thể đặt trong khu vực đã đạt mức tối đa.");
            }
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (Event.isEvent() && Event.getInstance().useItem(pl, item)) {
            return;
        }
        if (item.template.strRequire <= pl.nPoint.power) {
            int type = item.getType();
            switch (type) {
                case 6:
                    InventoryService.gI().eatPea(pl);
                    break;
                case 33:
                    RadaService.getInstance().useItemCard(pl, item);
                    break;
                case 39:
                    Service.getInstance().sendThongBao(pl, "Vui lòng tới Thợ Ngọc\ntại Đảo Kame để Thăng\nhoa sử dụng");
                    break;
                case 22:
                    useSatellite(pl, item);
                    break;
                case 29:
                    useItemTime(pl, item);
                    break;
                case 99:
                    break;
                default:
                    switch (item.template.id) {
                        case 1483:
                            if (pl.kaminEgg == null) {
                                KaminEgg.createKaminEgg(pl);
                                Service.getInstance().sendThongBao(pl, "Bạn nhận được Trứng linh thú, hãy kiểm tra tại Nhà máy bunma !");
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                pl.kaminEgg.sendKaminEgg();

                                pl.zone.mapInfo(pl);
                                DataGame.updateMap(pl.getSession());
                            } else {
                                Service.getInstance().sendThongBao(pl, "Bạn đang sở hữu trứng tại sân sau siêu thị rồi mà !");
                            }
                            break;
                        case 1478:
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            IntrinsicService.gI().OpenVIPByItem(pl);
                            break;
                        case 1994:
                        case 1995:
                        case 1996:
                        case 1479:
                        case 881:
                        case 882:
                        case 880:
                        case 1998:
                        case 1492:
                        case 1435:
                        case 1238:
                            useItemTime(pl, item);
                            break;
                        case 1991:
                            this.openBoxItem(pl, item, 0);
                        case 1983:
                            this.openBoxItem1(pl, item, 0);    
                            break;
                        case 1489:
                            StarBangHoi1489(pl, item);
                            break;
                        case 1491:
                            useTuiCaiTrangSSj1491(pl, item);
                            break;
                        case 1570:
                            if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
                                Service.getInstance().sendThongBaoOK(pl, "Cần 1 ô hành trang để mở");
                                return;
                            }
                            int[] rdngocboi = {1559, 1560, 1561, 1562, 1563, 1564, 1565, 1566, 1567};
                            int chonngocboi = rdngocboi[Util.nextInt(0, rdngocboi.length - 1)];
                            Item ngocboi = ItemService.gI().createNewItem((short) chonngocboi);
                            int randomOption1 = Util.nextInt(0, 2);
                            switch (randomOption1) {
                                case 0 ->
                                    ngocboi.itemOptions.add(new ItemOption(0, Util.nextInt(100, 400)));
                                case 1 ->
                                    ngocboi.itemOptions.add(new ItemOption(6, Util.nextInt(1000, 4000)));
                                case 2 ->
                                    ngocboi.itemOptions.add(new ItemOption(7, Util.nextInt(1000, 4000)));
                            }
                            int randomOption2 = Util.nextInt(0, 2);
                            switch (randomOption2) {
                                case 0 ->
                                    ngocboi.itemOptions.add(new ItemOption(50, 1));
                                case 1 ->
                                    ngocboi.itemOptions.add(new ItemOption(77, 1));
                                case 2 ->
                                    ngocboi.itemOptions.add(new ItemOption(103, 1));
                            }
                            ngocboi.itemOptions.add(new ItemOption(248, 0));
                            ngocboi.itemOptions.add(new ItemOption(30, 0));

                            InventoryService.gI().addItemBag(pl, ngocboi, 0);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ngocboi.template.name);
                            break;
                        case 1493:
                            useRuongCaitrang1493(pl, item);
                            break;
                        case 1551:
                            Rongnhi38(pl, item);
                            break;
                        case 1550:
                            Rongnhi1550(pl, item);
                            break;
                        case 1503:
                            useRuongThuong1503(pl, item);
                            break;
                        case 1504:
                            useRuongVip1504(pl, item);
                            break;
                        case 1501:
                            useThiepThuong1501(pl, item);
                            break;
                        case 1502:
                            useThiepVIP1502(pl, item);
                            break;
                        case 1507:
                            useVeDaLinhThu1507(pl, item);
                            break;
                        case 1506:
                            dao(pl, item);
                            break;
                        case 1477:
                            if (pl.pet != null) {
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.MENU_ACP_MO_TRUNG_BU, -1,
                                        "|2|Oáp! Bạn có muốn sử dụng trứng không?!\n"
                                        + "|6|Khi mở trứng sẽ nhận được ngẫu nhiên 1 trong 3 loại sau :"
                                        + "\nFide nhí : tăng 5% mọi chỉ số khi hợp thể"
                                        + "\n Bư nhí : tăng 10% mọi chỉ số khi hợp thể"
                                        + "\n Xên nhỉ : tăng 15% mọi chỉ số khi hợp thể\n"
                                        + "|7|Bạn có tự tin vào may mắn của mình không nhỉ?!",
                                        "Mở ngay", "Đóng");
                            } else {
                                Service.getInstance().sendThongBao(pl, "Phải có đệ tử trước khi mở trứng !");
                            }
                            break;
                        case 1552:
                            if (pl.pet != null) {
                                NpcService.gI().createMenuConMeo(pl, ConstNpc.MENU_ACP_MO_UUB, -1,
                                        "|2|Bạn có muốn sử dụng đệ uub không?!\n"
                                        + "Uub : tăng 30% mọi chỉ số khi hợp thể\n"
                                        + "Ưu điểm : chỉ số lúc mới mở ra cũng sẽ cao hơn bình thường\n"
                                        + "|7|Đây là loại đệ cao nhất hiện tại?!",
                                        "Mở ngay", "Đóng");
                            } else {
                                Service.getInstance().sendThongBao(pl, "Phải có đệ tử trước khi mở , nếu chưa có nhận free ở nhà !");
                            }
                            break;
                        case ConstItem.GOI_10_RADA_DO_NGOC:
                            findNamekBall(pl, item);
                            break;
                        case 2052:
                            capsule8thang3(pl, item);
                            break;
                        case ConstItem.CAPSULE_THOI_TRANG_30_NGAY:
                            capsuleThoiTrang(pl, item);
                            break;
                        // case 2039:
                        // openboxsukien(pl, item, ConstEvent.SU_KIEN_TET);
                        // break;
                        case 570:
                            openWoodChest(pl, item);
                            break;
                        case 648:
                            openboxsukien(pl, item, 3);
                            break;

                        case 1255:
                            if (pl.clan != null) {
                                if (pl.clan.maxMember < 15) {
                                    pl.clan.maxMember++;
                                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                    InventoryService.gI().sendItemBags(pl);
                                    Service.getInstance().sendThongBao(pl, "Bang hội đã có thể mời thêm 1 thành viên");
                                } else {
                                    Service.getInstance().sendThongBao(pl, "Bang hội của bạn đã đạt đến tối đa");
                                }
                            } else {
                                Service.getInstance().sendThongBao(pl, "Hãy vào bang hội trước");
                            }
                            break;
                        case 1256:
                            Input.gI().createFormChangeName(pl, pl);
                            break;
                        case 2024:
                            hopQuaTanThu(pl, item);
                            break;
                        case 992:
                            ChangeMapService.gI().goToPrimaryForest(pl);
                            break;
                        case 2023:
                            if (!pl.getSession().actived) {
                                Service.getInstance().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để có thể sử dụng");
                                return;
                            }
                            Input.gI().createFormTangRuby(pl);
                            break;
                        case 1999:
                            openHopQuaCaitrang1999(pl, item);
                            break;
                        case 1300:
                            openBokeo1300(pl, item);
                            break;
                        case 1344:
                            OpenHopPet1344(pl, item);
                            break;
                        case 1301:
                            openGioKeo1301(pl, item);
                            break;
                        // case 1354:
                        // openRuongBau1354(pl, item);
                        // break;
                        case 2006: // phiếu cải trang hải tặc
                        case 2007: // phiếu cải trang hải tặc
                        case 2008: // phiếu cải trang hải tặc
                            openPhieuCaiTrangHaiTac(pl, item);
                            break;
                        case 2012: // Hop Qua HÂLLLOWWEN
                            openRuongHalloween2012(pl, item);
                            break;
                        case 2020: // phiếu cải trang 20/10
                            openbox2010(pl, item);
                            break;
                        case 2021:
                            openboxsukien(pl, item, 2);
                            break;
                        case 211: // nho tím
                        case 212: // nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 380: // cskb
                            Input.gI().createFormOpenCSKB(pl);
                            break;
                        case 521: // tdlt
                            useTDLT(pl, item);
                            break;
                        case 571:
                            openRuongbac(pl, item);
                            break;
                        case 572:
                            openRuongvang(pl, item);
                            break;
                        case 627:
                            open_627(pl, item);
                            break;
                        case 1228:
                            OpenRuongskhthuong(pl, item);
                            break;
                        case 1599:
                            Openbongtai3(pl, item);
                            break;
                        case 1970:
                            Openluoihai(pl, item);
                            break;    
                        case 454: // bông tai
                            usePorata(pl);
                            break;
                        // case 1386:
                        // OpenRuongCtGod1386(pl, item);
                        // break;

                        case 1973:
                            binhexp(pl, item);
                            break;
                         case 1971:
                            nrthuong(pl, item);
                            break;
                        case 1974:
                            nrbang(pl, item);
                            break;
                        case 921:// bông tai
                            UseItem.gI().usePorata2(pl);
                            break;
                        case 1451:// bông tai
                            UseItem.gI().usePorata3(pl);
                            break;
                        case 1608:// bông tai
                            UseItem.gI().usePorata4(pl);
                            break;
                        case 457:
                            closeTab(pl);
                            Input.gI().ceateFormBanThoiVang(pl);
                            break;
                       
                        case 193: // gói 10 viên capsule
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: // capsule đặc biệt
                            openCapsuleUI(pl);
                            break;
                        case 1184:
                            hopquabian(pl, item);
                            break;
                        case 401: // đổi đệ tử
                            changePet(pl, item);
                            break;
                        case 1347:
                            openChangePetMenu(pl, item);
                            break;
                        case 402: // sách nâng chiêu 1 đệ tử
                        case 403: // sách nâng chiêu 2 đệ tử
                        case 404: // sách nâng chiêu 3 đệ tử
                        case 759: // sách nâng chiêu 4 đệ tử
                            upSkillPet(pl, item);
                            break;
                        case 568:
                            quaTrung(pl, item);
                            break;
                        case ConstItem.CAPSULE_TET_2022:
                            openCapsuleTet2024(pl, item);
                            break;
                        // case 1339:
                        // RewardService.gI().HoptraHoaCuc(pl, item.template.id);
                        // break;
                        case 1997:
                            RewardService.gI().OpenHopThanlinh(pl, item.template.id);
                            break;
                        case 1429:
//                            openTuiVangLon(pl, item);
                            closeTab(pl);
                            Input.gI().ceateFormBanThoiVang1(pl);
                            // openTuiPrivate_1347(pl, item);
                            break;
                        case 553:
                            openPhaoHoa(pl, item);
                            break;
                        case 554:
                            openSieuPhaoHoa(pl, item);
                            break;
                        case 399:
                            openThiepChucTet(pl, item);
                            break;
                        case 2039:
                            openHopQuaTet2024(pl, item);
                            break;
                        case 1353:
                            OpenRuongChonTrangBi(pl, item);
                            break;
                        case 1354:
                            hopQuaShare(pl, item);
                            break;
                        case 1125:
                            OpenRuongBuffSetTnsm(pl, item);
                            break;
                        case 1376:
                            open_mo_noi_tai(pl, item);
                            break;
                        case 1380:
                            Open_Cai_Trang_Tan_Thu(pl, item);
                            break;
                        case 757:
                            open757(pl, item);
                            break;
                        case 1381:
                            RuongThuongNhiemVuFide(pl, item);
                            break;
                        case 1382:
                            RuongThuongNhiemVuRobotSatthu(pl, item);
                            break;
                        case 1383:
                            RuongThuongNhiemVuXenBoHung(pl, item);
                            break;
                        case 1385: // 5 sao
                            OpenRuongSao(pl, item, 10, 5);
                            break;
                        case 1386: // 6 sao
                            OpenRuongSao(pl, item, 11, 6);
                            break;
                        case 1448: // 8 sao
                            OpenRuongSao(pl, item, 0, 8);
                            break;
                        case 1387: // bùa
                            bua_de_tu(pl, item);
                            break;
                        case 1384:
                            openHuyDiet1384(pl, item);
                            break;
                        case 1388:
                            OpenRuongChonTrangBi(pl, item);
                            break;
                        case 1389:
                            OpenRuongChonTrangBi(pl, item);
                            break;
                        case 1393:
                            OpenRuongChonTrangBi(pl, item);
                            break;
                        case 1400:
                            open_Kho_bau_hai_tac(pl, item);
                            break;
                        case 1403:
                            changeSkill2(pl, item);
                            break;
                        case 1404:
                            changeSkill3(pl, item);
                            break;
                        case 1405:
                            changeSkill4(pl, item);
                            break;
                        case 1406:
                            openChonSKH(pl, item);
                            break;
                        case 1407:
                            Open_Qua_Den_Bu(pl, item);
                            break;
                        case 1355:
                            Open_Hop_Qua_SucManh(pl, item);
                            break;
                        case 1356:
                            open_ruong_may_man(pl, item);
                            break;
                        case 1359:
                            open_ruong_cuoi(pl, item);
                            break;
                        case 1360: // quà đền bù
                            break;
                        case 1363:// Capsule kỳ bí cấp 2
                            Open_Ruong_Item_Cap_2(pl, item);
                            break;
                        case 1416: // Giảm hồi chiêu
                            resetSkill(pl, item);
                            break;
                        case 1419: // bỏ qua nhiệm vụ
                            OverTask(pl, item);
                            break;
                        case 1969:
                            openNRB(pl, item);
                            break;
                        case 460:
                            cucXuong(pl, item);
                            break;
                        case 456:
                            binhNuocXinBato(pl, item);
                            break;
                        case 1187:
                            capsuleTet(pl, item);
                            break;
                        case 573:
                            openCapsuleBac(pl, item);
                            break;
                        case 574:
                            openCapsuleVang(pl, item);
                            break;
                        case 1277:
                            openRuongHe2024(pl, item);
                            break;
                        case 1423:
                            openCapsuleHit(pl, item);
                            break;
                        case 1312:
                            openCapsuleThoiKhong(pl, item);
                            break;
                        case 1425:
                            openRuongDiemThuong(pl, item);
                            break;
                        case 1372:
                            ComboX3ThuocSucManh(pl, item);
                            break;
                        case 397:
                            openHopQuaNho(pl, item);
                            break;
                        case 1426:
                            OpenRuongChonHanhTinh(pl, item);
                            break;
                        case 1171:
                            chulun(pl, item);
                            break;
                        case 1460:
                            OpenRuongChonHanhTinh1(pl, item);
                            break;
                        case 1427:
                            addSideTask(pl, item);
                            break;
                        case 1428:
                            paySideTask(pl, item);
                            break;
                        case 1402:
                            openRuongKhoBauTheBai(pl, item);
                            break;
                        case 1430:
                            openGoiCheTaoLon(pl, item);
                            break;
                        case 1434:
                            BlackBall(pl, item);
                            break;
                        case 1437:
                            openRuongDeoLung1437(pl, item);
                            break;
                        case 1438:
                            openRuongCaiTrang1438(pl, item);
                            break;
                        case 1442:
                            openRuongPet1442(pl, item);
                            break;
                        case 1443:
                            openRuongLinhThu1443(pl, item);
                            break;
                        case 1444:
                            openRuongThuCuoi1444(pl, item);
                            break;
                        case 1032:
                            extendClan(pl, item);
                            break;
                        case 1033:
                        case 1034:
                        case 1035:
                            OpenRuongChonTrangBi(pl, item);
                            break;
                        case 1323:// Đá hóa thần
                            openRuongHoaThan(pl, item);
                            break;
                        case 1627:// Bánh thượng hạng 1
                            openRuongEventTrungThu_1_1338(pl, item);
                            break;
                        case 1628:// Bánh thượng hạng 2
                            openRuongEventTrungThu_2_1339(pl, item);
                            break;
                        case 1340:// Hộp quà trung thu
                            openRuongEventTrungThu_Hop_Qua_1340(pl, item);
                            break;
                        case 1013:// Túi may mắn
                            OpenRuongMayMan(pl, item);
                            break;
                         
                        case 1972:
                            hopquabanggia(pl, item);
                            break;
//                        case 1348:// Túi vật phẩm
//                            OpenHopBuff(pl, item);
//                            break;
                        case 818:// Rương sự kiện
                            openRuongHalloween818(pl, item);
                            break;
                        case 702:
                        case 703:
                        case 704:
                        case 705:
                        case 706:
                        case 707:
                        case 708:
                            OpenGoiRongBang(pl, item);
                            break;
                        case 1446:
                            OpenRuong_Halloween_17_1(pl, item);
                            break;
                        default:
                            switch (item.template.type) {
                                case 7: // sách học, nâng skill
                                    learnSkill(pl, item);
                                    break;
                                case 12: // ngọc rồng các loại
                                    // Service.getInstance().sendThongBaoOK(pl, "Bảo trì tính năng.");
                                    controllerCallRongThan(pl, item);
                                    break;
                                case 11: // item flag bag
                                    useItemChangeFlagBag(pl, item);
                                    break;
                                case 73:
                                    InventoryService.gI().itemBagToBody(pl, indexBag);
                                    SendEffect.getInstance().sendDanhhieu(pl, item.template.id);
                                    break;
//                                 case 39:
//                                    InventoryService.gI().itemBagToBody(pl, indexBag);
//                                    //SendEffect.getInstance().sendDanhhieu(pl, item.template.id);
//                                    break;
                                case 74:
                                    InventoryService.gI().itemBagToBody(pl, indexBag);
                                    SendEffect.getInstance().sendChanThienTu(pl, item.template.id);
                                    break;

                            }
                    }
                    break;
            }
            InventoryService.gI().sendItemBags(pl);

        } else {
            Service.getInstance().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    private void OpenRuongChonHanhTinh(Player player, Item item) {
        if (item.quantity <= 0) {
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 5) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 5 ô trống trong hành trang");
            return;
        }
        NpcService.gI().createMenuConMeo(player, item.template.id, -1,
                "Hãy chọn hành tinh của bạn: ", "Trái đất",
                "Namếc", "Xayda");
    }

    private void openChangePetMenu(Player player, Item item) {
    if (player.pet == null) {
        Service.getInstance().sendThongBao(player, "Bạn chưa có đệ tử");
        return;
    }

    NpcService.gI().createMenuConMeo(player, item.template.id, -1,
                "Hãy chọn hành tinh của bạn: ", "Trái đất",
                "Namếc", "Xayda");
}

    private void OpenRuongChonHanhTinh1(Player player, Item item) {
        if (item.quantity <= 0) {
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 5) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 5 ô trống trong hành trang");
            return;
        }
        NpcService.gI().createMenuConMeo(player, item.template.id, -1,
                "Hãy chọn hành tinh của bạn: ", "Trái đất",
                "Namếc", "Xayda");
    }
    
    private void open_mo_noi_tai(Player pl, Item item) {
        if (item == null || item.quantity < 1) {
            return;
        }
        if (IntrinsicService.gI().open_max(pl)) {
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        }
    }

    private void open757(Player pl, Item item) { // túi vàng
        if (item.quantity <= 0) {
            return;
        }

        int golds = 0;
        CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 930);
        if (Util.isTrue(2, 100)) {
            // 5tr đến 100tr
            golds = (Util.nextInt(5000, 20000)) * 5325;
            pl.inventory.gold += golds;
        } else {
            if (Util.isTrue(8, 100)) {
                // max 40tr
                golds = (Util.nextInt(5000, 20000) * 2102);
                pl.inventory.gold += golds;
            } else {
                // max 8tr
                golds = (Util.nextInt(5000, 20000) * 435);
                pl.inventory.gold += golds;
            }

        }

        if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
            pl.inventory.gold = Inventory.LIMIT_GOLD;
            Service.getInstance().sendThongBao(pl, "Số vàng trong hành đã đạt giới hạn");
        }
        if (golds > 1000000) {
            Service.getInstance().sendThongBao(pl,
                    "Bạn vừa nhận được " + (golds / 1000000) + " triệu vàng");
        }

        PlayerService.gI().sendInfoHpMpMoney(pl);

        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);

    }

    private void addSideTask(Player player, Item item) {
        if (player.playerTask.sideTask.leftTask < 5) {
            player.playerTask.sideTask.leftTask++;
            Service.getInstance().sendThongBao(player,
                    "Bạn được tăng thêm 1 lượt nhận nhiệm vụ hằng ngày");
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player,
                    "Tối đa 5 nhiệm vụ");
        }
    }

    private void paySideTask(Player player, Item item) {
        if (player.playerTask.sideTask.template != null) {
            if (player.playerTask.sideTask.isDone()) {
                Service.getInstance().sendThongBao(player,
                        "Nhiệm vụ đã hoàn thành rồi");
                return;
            }
            player.playerTask.sideTask.setOkeSideTask();
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player,
                    "Bạn đã hoàn thành nhiệm vụ, hãy đến gặp Lý Tiểu Nương để nhận thưởng");
        } else {
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn chưa có nhiệm vụ hằng ngày");
        }
    }

    private void open_627(Player pl, Item item) {
        if (pl != null && InventoryService.gI().getCountEmptyBag(pl) > 4) {
            Item ao = ItemService.gI().createNewItem((short) (0 + pl.gender));
            Item quan = ItemService.gI().createNewItem((short) (6 + pl.gender));
            Item gang = ItemService.gI().createNewItem((short) (21 + pl.gender));
            Item giay = ItemService.gI().createNewItem((short) (27 + pl.gender));
            Item rda = ItemService.gI().createNewItem((short) 12);

            RewardService.gI().initBaseOptionClothes(ao);
            RewardService.gI().initBaseOptionClothes(quan);
            RewardService.gI().initBaseOptionClothes(gang);
            RewardService.gI().initBaseOptionClothes(giay);
            RewardService.gI().initBaseOptionClothes(rda);

            int random = Util.nextInt(0, 2);
            RewardService.gI().initActivationOptionRandomFist(pl.gender,
                    ao.template.type,
                    ao.itemOptions, random);
            RewardService.gI().initActivationOptionRandomFist(pl.gender,
                    quan.template.type,
                    quan.itemOptions, random);
            RewardService.gI().initActivationOptionRandomFist(pl.gender,
                    gang.template.type,
                    gang.itemOptions, random);
            RewardService.gI().initActivationOptionRandomFist(pl.gender,
                    giay.template.type,
                    giay.itemOptions, random);
            RewardService.gI().initActivationOptionRandomFist(pl.gender,
                    rda.template.type,
                    rda.itemOptions, random);

            InventoryService.gI().addItemBag(pl, ao, 1);
            InventoryService.gI().addItemBag(pl, quan, 1);
            InventoryService.gI().addItemBag(pl, gang, 1);
            InventoryService.gI().addItemBag(pl, giay, 1);
            InventoryService.gI().addItemBag(pl, rda, 1);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được set "
                    + ao.template.name);

        } else {
            Service.getInstance().sendThongBao(pl, "Hành trang phải còn ít nhất 5 chỗ trống");
        }
    }
    
    private void OpenRuongskhthuong(Player pl, Item item) {
        if (pl == null || pl.nPoint.power < 50_000_000_000l) {
        Service.getInstance().sendThongBao(
                pl,
                "Bạn cần đạt ít nhất 50 tỷ sức mạnh mới có thể mở rương này!"
        );
        return;
        }
        if (pl != null && InventoryService.gI().getCountEmptyBag(pl) > 0) {
            List<Item> list = new ArrayList<>();
            Item ao   = ItemService.gI().createNewItem((short) (0 + pl.gender));
            Item quan = ItemService.gI().createNewItem((short) (6 + pl.gender));
            Item gang = ItemService.gI().createNewItem((short) (21 + pl.gender));
            Item giay = ItemService.gI().createNewItem((short) (27 + pl.gender));
            Item rda  = ItemService.gI().createNewItem((short) 12);
            list.add(ao);
            list.add(quan);
            list.add(gang);
            list.add(giay);
            list.add(rda);
            Item chosen = list.get(Util.nextInt(0, list.size() - 1));
            RewardService.gI().initBaseOptionClothes(chosen);
            chosen.itemOptions.removeIf(opt -> 
                    opt.optionTemplate.id >= 127 && opt.optionTemplate.id <= 135
            );
            int skhId = ItemService.gI().randomSKHId1(pl.gender);
            ItemService.gI().AddOptionSKH(chosen, skhId);
            InventoryService.gI().addItemBag(pl, chosen, 1);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            Service.getInstance().sendThongBao(pl,
                    "Bạn vừa nhận được " + chosen.template.name);

        } else {
            Service.getInstance().sendThongBao(pl, "Hành trang phải còn ít nhất 1 chỗ trống");
        }
    }
    
    private void Openbongtai3(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) <= 0) {
            Service.getInstance().sendThongBao(pl, "Hành trang phải còn ít nhất 1 chỗ trống");
            return;
        }
        int rd = Util.nextInt(1, 100); // 1 → 100
        int qty;
        if (rd <= 50) {
            qty = Util.nextInt(1, 3); // 50% ra 1–3
        } else if (rd <= 90) {
            qty = Util.nextInt(4, 6); // 35% ra 4–6
        } else {
            qty = Util.nextInt(7, 10); // 15% ra 7–10
        }
        for (int i = 0; i < qty; i++) {
            Item reward = ItemService.gI().createNewItem((short) 1601);
            InventoryService.gI().addItemBag(pl, reward, 1);
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn nhận được " + qty + " Mảnh vỡ Porata 3");
    }
    
    private void Openluoihai(Player pl, Item item) { 
        if (InventoryService.gI().getCountEmptyBag(pl) <= 0) {
            Service.getInstance().sendThongBao(pl, "Hành trang phải còn ít nhất 1 chỗ trống");
            return;
        }
        Item reward = ItemService.gI().createNewItem((short) 1618);
        reward.itemOptions.add(new ItemOption(50, Util.nextInt(5, 20)));
        reward.itemOptions.add(new ItemOption(77, Util.nextInt(5, 20)));
        reward.itemOptions.add(new ItemOption(103, Util.nextInt(5, 20)));
        if (Util.isTrue(60, 100)) {
                    reward.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                }
        reward.itemOptions.add(new ItemOption(30, 0));
        InventoryService.gI().addItemBag(pl, reward, 1);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn nhận được 1 lưỡi hái !");
    }

    private void OverTask(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        int idTask = TaskService.gI().getIdTask(player);
        if (idTask < ConstTask.TASK_19_0) {
            TaskMain taskMain = TaskService.gI().getTaskMainById(player,
                    19);
            taskMain.subTasks.get(0).count = 0;
            taskMain.index = 0;
            player.playerTask.taskMain = taskMain;
            Service.getInstance().sendThongBao(player,
                    "Bạn được tăng tốc đến nhiệm vụ Kuku");
            TaskService.gI().sendTaskMain(player);
            InventoryService.gI().sendItemBags(player);
        } else if (!player.isAdmin() && idTask >= ConstTask.TASK_28_0) {
            Service.getInstance().sendThongBao(player,
                    "Nhiệm vụ của bạn đã vượt mức có thể sử dụng thẻ hoặc đã max nhiệm vụ");
        } else {
            if (player.isAdmin()) {
                // Log.warning("Sub task " + player.playerTask.taskMain.subTasks
                // .get(player.playerTask.taskMain.index).maxCount);
                player.playerTask.taskMain.subTasks
                        .get(player.playerTask.taskMain.index).count = (short) (player.playerTask.taskMain.subTasks
                        .get(player.playerTask.taskMain.index).maxCount - 1);
                TaskService.gI().sendTaskMain(player);
                TaskService.gI().doneTask(player, idTask);
                Service.getInstance().sendThongBao(player, "Thành công (chỉ dành cho admin)");
                // InventoryService.gI().subQuantityItemsBag(player, item, 1);
                InventoryService.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Tối đa đến nhiệm vụ Kuku");
                InventoryService.gI().sendItemBags(player);
            }

        }

    }

    private void Open_Qua_Den_Bu(Player pl, Item item) {
        // if (item.quantity < 1) {
        // return;
        // }
        // if (InventoryService.gI().getCountEmptyBag(pl) < 2) {
        // Service.getInstance().sendThongBao(pl, "Bạn cần có ít nhất 2 ô trống trong
        // hành trang");
        // return;
        // }
        // InventoryService.gI().subQuantityItemsBag(pl, item, 1);

        // Item trangBi = ItemService.gI().createNewItem((short) (853 + pl.gender));
        // trangBi.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 38));
        // trangBi.itemOptions.add(new ItemOption(ConstOption.HP_PT, 38));
        // trangBi.itemOptions.add(new ItemOption(ConstOption.KI_PT, 38));
        // trangBi.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, 38));
        // trangBi.itemOptions.add(new ItemOption(ConstOption.HUT_PT_KI, 38));
        // trangBi.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 35));
        // trangBi.itemOptions.add(new ItemOption(241, 50));
        // trangBi.itemOptions.add(new ItemOption(236, 1));
        // trangBi.itemOptions.add(new ItemOption(30, 1));
        // trangBi.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 3));
        // Item phuKien = ItemService.gI().createNewItem((short) (856 + pl.gender));
        // phuKien.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 35));
        // phuKien.itemOptions.add(new ItemOption(ConstOption.HP_PT, 35));
        // phuKien.itemOptions.add(new ItemOption(ConstOption.KI_PT, 35));
        // phuKien.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, 200));
        // phuKien.itemOptions.add(new ItemOption(30, 1));
        // phuKien.itemOptions.add(new ItemOption(236, 1));
        // phuKien.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, 3));
        // if (trangBi != null) {
        // InventoryService.gI().addItemBag(pl, trangBi, 1);
        // InventoryService.gI().sendItemBags(pl);
        // Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " +
        // trangBi.template.name);
        // }
        // if (phuKien != null) {
        // InventoryService.gI().addItemBag(pl, phuKien, 1);
        // InventoryService.gI().sendItemBags(pl);
        // Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " +
        // phuKien.template.name);
        // }
    }

    private void Open_Hop_Qua_SucManh(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 2 ô trống trong hành trang");
            return;
        }
        int id_item = 1994 + Util.nextInt(0, 1);
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        Item thuoc1 = ItemService.gI().createNewItem((short) id_item);
        thuoc1.itemOptions.add(new ItemOption(30, 1));

        if (thuoc1 != null) {

            InventoryService.gI().addItemBag(player, thuoc1, 1);

            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn vừa nhận được 1 " + thuoc1.template.name);

        }
    }

    private void ComboX3ThuocSucManh(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 2 ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        Item thuoc1 = ItemService.gI().createNewItem((short) 1994, 3);
        thuoc1.itemOptions.add(new ItemOption(30, 1));
        Item thuoc2 = ItemService.gI().createNewItem((short) 1995, 3);
        thuoc2.itemOptions.add(new ItemOption(30, 1));

        if (thuoc1 != null && thuoc2 != null) {
            InventoryService.gI().addItemBag(player, thuoc1, 1);
            InventoryService.gI().addItemBag(player, thuoc2, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn vừa nhận được 1 " + thuoc1.template.name);

        }
    }

    private void Open_Ruong_Item_Cap_2(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        int id_item = Util.nextInt(1150, 1153);
        Item thuoc1 = ItemService.gI().createNewItem((short) id_item);
        if (thuoc1 != null) {
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == ConstOption.KHONG_THE_GD) {
                    thuoc1.itemOptions.add(io);
                    break;
                }
            }
            CombineServiceNew.gI().sendEffectOpenItem(player, (short) item.template.iconID,
                    (short) thuoc1.template.iconID);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().addItemBag(player, thuoc1, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player,
                    "Bạn vừa nhận được " + thuoc1.template.name);

        }
    }

    private void Open_Cai_Trang_Tan_Thu(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) < 2) {
            Service.getInstance().sendThongBao(pl, "Bạn cần có ít nhất 2 ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);

        Item trangBi = ItemService.gI().createNewItem((short) (607 + pl.gender));

        trangBi.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 15));
        trangBi.itemOptions.add(new ItemOption(ConstOption.HP_PT, 15));
        trangBi.itemOptions.add(new ItemOption(ConstOption.KI_PT, 15));
        trangBi.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, 15));
        trangBi.itemOptions.add(new ItemOption(ConstOption.HUT_PT_KI, 15));
        trangBi.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, 50));
        trangBi.itemOptions.add(new ItemOption(30, 1));
        short idDeoLung = 815;
        if (pl.gender == 1) {
            idDeoLung = 817;
        } else if (pl.gender == 2) {
            idDeoLung = 816;
        }
        Item phuKien = ItemService.gI().createNewItem(idDeoLung);

        phuKien.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 5));
        phuKien.itemOptions.add(new ItemOption(ConstOption.HP_PT, 5));
        phuKien.itemOptions.add(new ItemOption(ConstOption.KI_PT, 5));
        phuKien.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, 15));
        phuKien.itemOptions.add(new ItemOption(30, 1));

        if (trangBi != null) {
            InventoryService.gI().addItemBag(pl, trangBi, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + trangBi.template.name);
        }
        if (phuKien != null) {
            InventoryService.gI().addItemBag(pl, phuKien, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + phuKien.template.name);
        }

    }

    private void binhexp(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) < 3) {
            Service.getInstance().sendThongBao(pl, "Bạn cần có ít nhất 2 ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);

        Item x2 = ItemService.gI().createNewItem((short)1994);
        x2.itemOptions.add(new ItemOption(30, 1));
        
        Item x3 = ItemService.gI().createNewItem((short)1995);
        x3.itemOptions.add(new ItemOption(30, 1));

        Item x4 = ItemService.gI().createNewItem((short)1996);
        x4.itemOptions.add(new ItemOption(30, 1));

        if (x2 != null) {
            InventoryService.gI().addItemBag(pl, x2, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + x2.template.name);
        }
        if (x3 != null) {
            InventoryService.gI().addItemBag(pl, x3, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + x3.template.name);
        }
        if (x4 != null) {
            InventoryService.gI().addItemBag(pl, x4, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + x4.template.name);
        }

    }

    private void nrbang(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) < 7) {
            Service.getInstance().sendThongBao(player, "Cần 7 ô hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        for (int i = 925; i <= 931; i++) {
            item = ItemService.gI().createNewItem((short) i);
            InventoryService.gI().addItemBag(player, item, 0);
        }
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendThongBao(player, "Thành công nhận ngọc rồng băng từ 1- 7 sao");
    }
        


    private void nrthuong(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) < 7) {
            Service.getInstance().sendThongBao(player, "Cần 7 ô hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        for (int i = 14; i <= 20; i++) {
            item = ItemService.gI().createNewItem((short) i);
            InventoryService.gI().addItemBag(player, item, 0);
        }
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendThongBao(player, "Thành công nhận ngọc rồng từ 1- 7 sao");
    }
    private void OpenHopBuff(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        Input.gI().createFormBuff(player);
    }

    private void openTuiVangLon(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        long checkGold = player.inventory.gold + 50_000_000;
        if (checkGold > player.inventory.getGoldLimit()) {
            Service.getInstance().sendThongBao(player,
                    "Túi vàng đã đầy, hãy chừa 500tr vàng nhé");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        player.inventory.gold += 50_000_000;
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        Service.getInstance().sendThongBao(player,
                "Bạn vừa nhận được 500 triệu vàng");

    }

    private void RuongThuongNhiemVuFide(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 2 ô trống trong hành trang");
            return;
        }
        if (player.inventory.gold + 1000000000 > player.inventory.getGoldLimit()) {
            Service.getInstance().sendThongBao(player,
                    "Túi vàng đã đầy, hãy chừa 1 tỷ nhé");

            return;
        }
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        Item phuKien = ItemService.gI().createNewItem((short) 795);

        phuKien.itemOptions.add(new ItemOption(ConstOption.HP_PT, 5));
        phuKien.itemOptions.add(new ItemOption(ConstOption.KI_PT, 5));
        phuKien.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, 15));
        phuKien.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, 15));
        phuKien.itemOptions.add(new ItemOption(30, 1));

        if (phuKien != null) {
            player.inventory.gold += 1000000000;
            InventoryService.gI().addItemBag(player, phuKien, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn vừa nhận được " + phuKien.template.name + " và 1 tỷ vàng");

        }

    }

    private void bua_de_tu(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        // InventoryService.gI().subQuantityItemsBag(player, item, 1);
        // player.charms.addTimeCharms(2025, 61);
        // Service.getInstance().sendThongBao(player,
        // "Đệ tử của ngươi được x2 tnsm thêm 1 giờ");

    }

    private void RuongThuongNhiemVuRobotSatthu(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBaoOK(player, "Bạn cần có ít nhất 2 ô trống trong hành trang");
            return;
        }
        if (player.inventory.gold + 2000000000l > player.inventory.getGoldLimit()) {
            Service.getInstance().sendThongBao(player,
                    "Túi vàng đã đầy, hãy chừa 2 tỷ nhé");

            return;
        }
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        Item phuKien = ItemService.gI().createNewItem((short) 738);
        phuKien.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 26));
        phuKien.itemOptions.add(new ItemOption(ConstOption.HP_PT, 26));
        phuKien.itemOptions.add(new ItemOption(ConstOption.KI_PT, 26));
        phuKien.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 8));
        phuKien.itemOptions.add(new ItemOption(30, 1));

        if (phuKien != null) {
            player.inventory.gold += 2000000000l;
            InventoryService.gI().addItemBag(player, phuKien, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            Service.getInstance().sendThongBaoOK(player,
                    "Bạn vừa nhận được " + phuKien.template.name + " và 2 tỷ vàng");
        }
    }

    private void RuongThuongNhiemVuXenBoHung(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 2 ô trống trong hành trang");
            return;
        }
        if (player.inventory.gold + 5000000000l > player.inventory.getGoldLimit()) {
            Service.getInstance().sendThongBaoOK(player,
                    "Túi vàng đã đầy, hãy chừa 5 tỷ nhé");

            return;
        }
        InventoryService.gI().subQuantityItemsBag(player, item, 1);
        Item phuKien = ItemService.gI().createNewItem((short) 1243);
        phuKien.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, 10));
        phuKien.itemOptions.add(new ItemOption(ConstOption.HP_PT, 10));
        phuKien.itemOptions.add(new ItemOption(ConstOption.KI_PT, 10));
        phuKien.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, 8));
        phuKien.itemOptions.add(new ItemOption(30, 1));

        if (phuKien != null) {
            player.inventory.gold += 5000000000l;
            InventoryService.gI().addItemBag(player, phuKien, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + phuKien.template.name + " và 5 tỷ vàng");
        }

    }

    private void OpenRuongChonTrangBi(Player player, Item item) {
        if (item.quantity <= 0) {
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        NpcService.gI().createMenuConMeo(player, item.template.id, -1,
                "Hãy chọn loại trang bị của bạn: ", "Áo",
                "Quần", "Găng", "Giày", "Rada\n(Nhẫn)");
    }

    private void OpenGoiRongBang(Player player, Item item) {

        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        for (int i = 0; i < 7; i++) {
            Item nrx = InventoryService.gI().findItemBag(player, 702 + i);
            if (nrx == null || nrx.quantity < 1) {
                Item it = ItemService.gI().createNewItem((short) (702 + i));
                Service.getInstance().sendThongBao(player, "Bạn còn thiếu " + it.template.name);
                return;
            }
        }
        // SummonDragon.gI().activeDragonNew_2(player, true, (byte) 51);
        NpcService.gI().createMenuConMeo(player, item.template.id, -1,
                "Ta có 4 điều ước, hãy nhanh chọn trước khi ta bay đi mất:",
                "Capsule\nHalloween",
                "Hộp quà\nHalloween", "Đổi\nchiêu 2 - 3 \nđệ tử", "Combo\nbình thuốc\nx2 3 4 tnsm");
    }

    private void openChonSKH(Player player, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 5) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 5 ô trống trong hành trang");
            return;
        }
        if (player.gender == 0) {
            NpcService.gI().createMenuConMeo(player, item.template.id, -1,
                    "Hãy chọn set kích hoạt của bạn: ", "Kaioken",
                    "Kirin", "Sôngôku");
        } else if (player.gender == 1) {
            NpcService.gI().createMenuConMeo(player, item.template.id, -1,
                    "Hãy chọn set kích hoạt của bạn: ", "Piccolo",
                    "Dende", "Pikkoro\nDaimao");
        } else if (player.gender == 2) {
            NpcService.gI().createMenuConMeo(player, item.template.id, -1,
                    "Hãy chọn set kích hoạt của bạn: ", "Kakarot",
                    "Ca Đic", "Nappa");
        }
    }

    // EVENT 17
    private void OpenRuong_Halloween_17_1(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) < 7) {
            Service.getInstance().sendThongBao(pl, "Bạn cần có ít nhất 7 ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);

        for (int i = 0; i < 7; i++) {

            Item trangBi = ItemService.gI().createNewItem((short) (702 + i));

            if (trangBi != null) {
                InventoryService.gI().addItemBag(pl, trangBi, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + trangBi.template.name);
            }
        }

    }

    private void OpenRuongTanThu(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) < 5) {
            Service.getInstance().sendThongBao(pl, "Bạn cần có ít nhất 5 ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        int gender = pl.gender;
        short idItem;
        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                gender = 0;
            }
            idItem = ConstItem.doSKHVip[i][gender][0]; // cấp độ trang bị -1
            Item trangBi = ItemService.gI().createNewItem((short) idItem);
            RewardService.gI().initBaseOptionClothes(trangBi);
            trangBi.itemOptions.add(new ItemOption(216, 1));
            trangBi.itemOptions.add(new ItemOption(217, 1));
            trangBi.itemOptions.add(new ItemOption(30, 1));

            if (trangBi != null) {
                InventoryService.gI().addItemBag(pl, trangBi, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + trangBi.template.name);
            }
        }

    }

    private void OpenRuongMayMan(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        // set kích hoạt may mắn qd
        if (InventoryService.gI().getCountEmptyBag(pl) < 5) {
            Service.getInstance().sendThongBao(pl, "Bạn cần có ít nhất 5 ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        int gender = pl.gender;
        short idItem;
        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                gender = 0;
            }
            idItem = ConstItem.doSKHVip[i][gender][0]; // cấp độ trang bị -1
            Item trangBi = ItemService.gI().createNewItem((short) idItem);
            RewardService.gI().initBaseOptionClothes(trangBi);
            trangBi.itemOptions.add(new ItemOption(244, 1));
            trangBi.itemOptions.add(new ItemOption(245, 1));
            trangBi.itemOptions.add(new ItemOption(30, 1));

            if (trangBi != null) {
                InventoryService.gI().addItemBag(pl, trangBi, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + trangBi.template.name);
            }
        }

    }

    private void OpenRuongBuffSetTnsm(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) < 5) {
            Service.getInstance().sendThongBao(pl, "Bạn cần có ít nhất 5 ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        int gender = pl.gender;
        short idItem;
        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                gender = 0;
            }
            idItem = ConstItem.doSKHVip[i][gender][11]; // cấp độ trang bị -1
            Item trangBi = ItemService.gI().createNewItem((short) idItem);
            RewardService.gI().initBaseOptionClothes(trangBi);
            trangBi.itemOptions.add(new ItemOption(216, 1));
            trangBi.itemOptions.add(new ItemOption(217, 1));
            trangBi.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, 500));
            trangBi.itemOptions.add(new ItemOption(30, 1));

            if (trangBi != null) {
                InventoryService.gI().addItemBag(pl, trangBi, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + trangBi.template.name);
            }
        }

    }

    private void openGoiCheTaoLon(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) < 5) {
            Service.getInstance().sendThongBao(pl, "Bạn cần có ít nhất 5 ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        int gender = pl.gender;
        short idItem;
        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                gender = 0;
            }
            idItem = ConstItem.doSKHVip[i][gender][8]; // cấp độ trang bị -1
            Item trangBi = ItemService.gI().createNewItem((short) idItem);
            RewardService.gI().initBaseOptionClothes(trangBi);
            trangBi.itemOptions.add(new ItemOption(236, 1));
            trangBi.itemOptions.add(new ItemOption(30, 1));

            if (trangBi != null) {
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().addItemBag(pl, trangBi, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + trangBi.template.name);
            }
        }

    }

    private void OpenRuongSao(Player pl, Item item, int level, int star) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) < 5) {
            Service.getInstance().sendThongBao(pl, "Bạn cần có ít nhất 5 ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        int gender = pl.gender;
        short idItem;
        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                gender = 0;
            }
            idItem = ConstItem.doSKHVip[i][gender][level]; // cấp độ trang bị -1
            Item trangBi = ItemService.gI().createNewItem((short) idItem);
            RewardService.gI().initBaseOptionClothes(trangBi);
            trangBi.itemOptions.add(new ItemOption(107, star));// 3 sao
            trangBi.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, star));// 3 sao
            if (trangBi != null) {
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().addItemBag(pl, trangBi, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + trangBi.template.name);
            }
        }
    }

    private void OpenRuongCtGod1386(Player player, Item itInput) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            Item Ctgod = ItemService.gI().createNewItem((short) 1358);
            int rdParamCrit = Util.nextInt(1, 150);
            if (rdParamCrit < 70) {
                Ctgod.itemOptions.add(new ItemOption(77, Util.nextInt(1, 5)));
                Ctgod.itemOptions.add(new ItemOption(103, Util.nextInt(1, 5)));
                Ctgod.itemOptions.add(new ItemOption(5, rdParamCrit));
                if (Util.isTrue(30, 100)) {
                    Ctgod.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                }
            } else if (rdParamCrit > 70) {
                Ctgod.itemOptions.add(new ItemOption(77, Util.nextInt(1, 5)));
                Ctgod.itemOptions.add(new ItemOption(103, Util.nextInt(1, 5)));
                Ctgod.itemOptions.add(new ItemOption(5, rdParamCrit));
                if (Util.isTrue(99, 100)) {
                    Ctgod.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                }
            }
            CombineServiceNew.gI().sendEffectOpenItem(player, itInput.template.iconID, Ctgod.template.iconID);
            InventoryService.gI().subQuantityItemsBag(player, itInput, 1);
            InventoryService.gI().addItemBag(player, Ctgod, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    private void findNamekBall(Player pl, Item item) {
        List<NamekBall> balls = NamekBallManager.gI().getList();
        StringBuffer sb = new StringBuffer();
        for (NamekBall namekBall : balls) {
            Map m = namekBall.zone.map;
            sb.append(namekBall.getIndex() + 1).append(" Sao: ").append(m.mapName)
                    .append(namekBall.getHolderName() == null ? "" : " - " + namekBall.getHolderName()).append("\n");
        }
        final int star = Util.nextInt(0, 6);
        final NamekBall ball = NamekBallManager.gI().findByIndex(star);
        final Inventory inventory = pl.inventory;
        MenuDialog menu = new MenuDialog(sb.toString(),
                new String[]{"Đến ngay\nViên " + (star + 1) + " Sao\n 50tr Vàng",
                    "Đến ngay\nViên " + (star + 1) + " Sao\n 5 Hồng ngọc"},
                new MenuRunable() {
            @Override
            public void run() {
                switch (getIndexSelected()) {
                    case 0:
                        if (inventory.gold < 50000000) {
                            Service.getInstance().sendThongBao(pl, "Không đủ tiền");
                            return;
                        }
                        inventory.subGold(50000000);
                        ChangeMapService.gI().changeMap(pl, ball.zone, ball.x, ball.y);
                        break;
                    case 1:
                        if (inventory.ruby < 5) {
                            Service.getInstance().sendThongBao(pl, "Không đủ tiền");
                            return;
                        }
                        inventory.subRuby(5);
                        ChangeMapService.gI().changeMap(pl, ball.zone, ball.x, ball.y);
                        break;
                }
                if (pl.isHoldNamecBall) {
                    NamekBallWar.gI().dropBall(pl);
                }
                Service.getInstance().sendMoney(pl);
            }
        });
        menu.show(pl);
        InventoryService.gI().sendItemBags(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
    }

    private void capsuleThoiTrang(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem(
                    (short) Util.nextInt(ConstItem.CAI_TRANG_GOKU_THOI_TRANG, ConstItem.CAI_TRANG_CA_DIC_THOI_TRANG));
            it.itemOptions.add(new ItemOption(50, 30));
            it.itemOptions.add(new ItemOption(77, 30));
            it.itemOptions.add(new ItemOption(103, 30));
            it.itemOptions.add(new ItemOption(106, 0));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }
    }

    private void quaTrung(Player pl, Item item) {

        if (pl.mabuEgg == null) {

            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            MabuEgg.createMabuEgg(pl);
            if (pl.zone.map.mapId == 21 || pl.zone.map.mapId == 22 || pl.zone.map.mapId == 23) {
                ChangeMapService.gI().changeMapInYard(pl, pl.gender * 7, 1, 300);
            }
            Service.getInstance().sendThongBao(pl, "Mở quả trứng thành công, hãy quay về nhà để xem");
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn đã có trứng đệ tử ở nhà rồi");
        }

    }

    private void cucXuong(Player pl, Item item) {
        List<Player> bosses = pl.zone.getBosses();
        boolean checkSoi = false;

        synchronized (bosses) {
            for (Player bossPlayer : bosses) {
                if (bossPlayer.id == BossFactory.HEC_QUYN_EVENT) {
                    if (!pl.isDie()) {
                        checkSoi = true;
                    }
                }

            }
        }

        if (!checkSoi) {
            Service.getInstance().sendThongBao(pl, "Không tìm thấy Sói hẹc quyn");
            return;
        } else {

            Boss soihecQuyn = BossManager.gI().getBossById(BossFactory.HEC_QUYN_EVENT);
            if (soihecQuyn != null) {
                if (((SoiHecQuyn) soihecQuyn).checkNhatXuong() == true) {
                    Service.getInstance().sendThongBao(pl, "Sói đã no rồi, hãy đến sớm hơn");
                    return;
                } else {
                    ((SoiHecQuyn) soihecQuyn).NhatXuong();
                    Service.getInstance().chat(soihecQuyn, "Ê, miếng xương ngon quá");
                }
            }

            ItemMap itemMap = null;
            int x = pl.location.x;
            if (x < 0 || x >= pl.zone.map.mapWidth) {
                return;
            }
            int y = pl.zone.map.yPhysicInTop(x, pl.location.y - 24);
            itemMap = new ItemMap(pl.zone, 460, 1, x, y, pl.id);
            itemMap.isPickedUp = true;
            itemMap.createTime -= 23000;
            if (itemMap != null) {

                Service.getInstance().dropItemMap(pl.zone, itemMap);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short[] listItem = {16, 17, 1401, 441, 442, 443, 444, 445, 446, 447, 465, 466, 463, 464, 467,
                468,
                469,
                470,
                471};
            short idItem = listItem[Util.nextInt(listItem.length - 1)];
            Item it = ItemService.gI().createNewItem(idItem);

            ItemService.gI().OptionAllItem(it, 97);

            if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                InventoryService.gI().addItemBag(pl, it, 0);

            } else {
                Service.getInstance().sendThongBao(pl, "Hành trang không đủ chỗ trống.");
            }
            try {

                Thread.sleep(5000);
            } catch (Exception e) {

            }
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + it.template.name);

        }

        InventoryService.gI().sendItemBags(pl);

    }

    private void binhNuocXinBato(Player pl, Item item) {
        List<Player> bosses = pl.zone.getBosses();
        boolean checkSoi = false;

        synchronized (bosses) {
            for (Player bossPlayer : bosses) {
                if (bossPlayer.id == BossFactory.XINBATO_EVENT) {
                    if (!pl.isDie()) {
                        checkSoi = true;
                    }
                }

            }
        }

        if (!checkSoi) {
            Service.getInstance().sendThongBao(pl, "Hãy đi tìm Xinbatô để cho nước nhé");
            return;
        } else {

            Boss soihecQuyn = BossManager.gI().getBossById(BossFactory.XINBATO_EVENT);
            if (soihecQuyn != null) {
                if (((Xinbato) soihecQuyn).checkNhatXuong() == true) {
                    Service.getInstance().sendThongBao(pl, "Đã có người giúp đỡ Xinbatô rồi");
                    return;
                } else {
                    if (item.quantity < 99) {
                        Service.getInstance().chat(soihecQuyn, "Tôi cần x99 bình nước để cứu dân làng");
                        return;
                    }
                    ((Xinbato) soihecQuyn).NhatXuong();
                    Service.getInstance().chat(soihecQuyn, "Cảm ơn đã giúp đỡ dân làng chúng tôi");
                }
            }

            ItemMap itemMap = null;
            int x = pl.location.x;
            if (x < 0 || x >= pl.zone.map.mapWidth) {
                return;
            }
            int y = pl.zone.map.yPhysicInTop(x, pl.location.y - 24);
            itemMap = new ItemMap(pl.zone, 456, 1, x, y, pl.id);
            itemMap.isPickedUp = true;
            itemMap.createTime -= 23000;
            if (itemMap != null) {

                Service.getInstance().dropItemMap(pl.zone, itemMap);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 99);
            InventoryService.gI().sendItemBags(pl);
            short[] listItem = {16, 17, 1401, 642, 643, 1150, 1151, 1152, 1153, 1158, 1159, 1160, 1161, 1162, 1163,
                1164};
            short idItem = listItem[Util.nextInt(listItem.length - 1)];
            Item it = ItemService.gI().createNewItem(idItem);

            ItemService.gI().OptionAllItem(it, 95);

            if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                InventoryService.gI().addItemBag(pl, it, 0);

            } else {
                Service.getInstance().sendThongBao(pl, "Hành trang không đủ chỗ trống.");
            }
            try {

                Thread.sleep(5000);
            } catch (Exception e) {

            }
            Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + it.template.name);

        }

        InventoryService.gI().sendItemBags(pl);

    }

    private void hopquabian(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            if (Util.isTrue(1, 5)) {
                int id = Util.getOne(865, 1258);
                Item kiem = ItemService.gI().createNewItem((short) id);
                kiem.itemOptions.add(new ItemOption(50, Util.nextInt(8, 12)));
                kiem.itemOptions.add(new ItemOption(77, Util.nextInt(8, 12)));
                kiem.itemOptions.add(new ItemOption(103, Util.nextInt(8, 12)));
                kiem.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                short icon1 = item.template.iconID;
                short icon2 = kiem.template.iconID;
                CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
                InventoryService.gI().addItemBag(pl, kiem, 1);
                InventoryService.gI().sendItemBags(pl);
            } else if (Util.isTrue(1, 3)) {
                Item arrale = ItemService.gI().createNewItem((short) 914);
                arrale.itemOptions.add(new ItemOption(50, Util.nextInt(30, 35)));
                arrale.itemOptions.add(new ItemOption(77, Util.nextInt(30, 35)));
                arrale.itemOptions.add(new ItemOption(103, Util.nextInt(30, 35)));
                arrale.itemOptions.add(new ItemOption(106, 0));
                arrale.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                short icon1 = item.template.iconID;
                short icon2 = arrale.template.iconID;
                CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
                InventoryService.gI().addItemBag(pl, arrale, 1);
                InventoryService.gI().sendItemBags(pl);
            } else if (Util.isTrue(1, 2)) {
                Item nezuko = ItemService.gI().createNewItem((short) 1091);
                if (Util.isTrue(1, 2)) {
                    nezuko.itemOptions.add(new ItemOption(5, Util.nextInt(80, 120)));
                } else {
                    nezuko.itemOptions.add(new ItemOption(5, Util.nextInt(60, 90)));
                }
                nezuko.itemOptions.add(new ItemOption(77, Util.nextInt(10, 15)));
                nezuko.itemOptions.add(new ItemOption(103, Util.nextInt(10, 15)));
                nezuko.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                short icon1 = item.template.iconID;
                short icon2 = nezuko.template.iconID;
                CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
                InventoryService.gI().addItemBag(pl, nezuko, 1);
                InventoryService.gI().sendItemBags(pl);
            } else if (Util.isTrue(1, 10)) {
                Item Zblue = ItemService.gI().createNewItem((short) 865);
                Zblue.itemOptions.add(new ItemOption(50, Util.nextInt(8, 12)));
                Zblue.itemOptions.add(new ItemOption(77, Util.nextInt(8, 12)));
                Zblue.itemOptions.add(new ItemOption(103, Util.nextInt(8, 12)));
                short icon1 = item.template.iconID;
                short icon2 = Zblue.template.iconID;
                CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
                InventoryService.gI().addItemBag(pl, Zblue, 1);
                InventoryService.gI().sendItemBags(pl);
            } else if (Util.isTrue(1, 10)) {
                Item ZRed = ItemService.gI().createNewItem((short) 1258);
                ZRed.itemOptions.add(new ItemOption(50, Util.nextInt(8, 12)));
                ZRed.itemOptions.add(new ItemOption(77, Util.nextInt(8, 12)));
                ZRed.itemOptions.add(new ItemOption(103, Util.nextInt(8, 12)));
                short icon1 = item.template.iconID;
                short icon2 = ZRed.template.iconID;
                CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
                InventoryService.gI().addItemBag(pl, ZRed, 1);
                InventoryService.gI().sendItemBags(pl);
            } else if (Util.isTrue(1, 100)) {
                Item arrale = ItemService.gI().createNewItem((short) 914);
                arrale.itemOptions.add(new ItemOption(50, Util.nextInt(30, 35)));
                arrale.itemOptions.add(new ItemOption(77, Util.nextInt(30, 35)));
                arrale.itemOptions.add(new ItemOption(103, Util.nextInt(30, 35)));
                arrale.itemOptions.add(new ItemOption(106, 0));
                short icon1 = item.template.iconID;
                short icon2 = arrale.template.iconID;
                CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
                InventoryService.gI().addItemBag(pl, arrale, 1);
                InventoryService.gI().sendItemBags(pl);
            } else if (Util.isTrue(1, 200)) {
                Item nezuko = ItemService.gI().createNewItem((short) 1091);
                nezuko.itemOptions.add(new ItemOption(5, Util.nextInt(60, 120)));
                nezuko.itemOptions.add(new ItemOption(77, Util.nextInt(10, 15)));
                nezuko.itemOptions.add(new ItemOption(103, Util.nextInt(10, 15)));
                short icon1 = item.template.iconID;
                short icon2 = nezuko.template.iconID;
                CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
                InventoryService.gI().addItemBag(pl, nezuko, 1);
                InventoryService.gI().sendItemBags(pl);
            } else {
                int id = Util.getOne(865, 1258);
                Item kiem = ItemService.gI().createNewItem((short) id);
                kiem.itemOptions.add(new ItemOption(50, Util.nextInt(8, 12)));
                kiem.itemOptions.add(new ItemOption(77, Util.nextInt(8, 12)));
                kiem.itemOptions.add(new ItemOption(103, Util.nextInt(8, 12)));
                kiem.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                short icon1 = item.template.iconID;
                short icon2 = kiem.template.iconID;
                CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
                InventoryService.gI().addItemBag(pl, kiem, 1);
                InventoryService.gI().sendItemBags(pl);
            }
        }
    }

    private void openCapsuleTet2022(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
            return;
        }
        RandomCollection<Integer> rdItemID = new RandomCollection<>();
        rdItemID.add(1, ConstItem.PHAO_HOA);
        rdItemID.add(1, ConstItem.CAY_TRUC);
        rdItemID.add(1, ConstItem.NON_HO_VANG);
        if (pl.gender == 0) {
            rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN);
            rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN);
        } else if (pl.gender == 1) {
            rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN_847);
            rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN_755);
        } else {
            rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN_848);
            rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN_756);
        }
        rdItemID.add(1, ConstItem.CAI_TRANG_HO_VANG);
        rdItemID.add(1, ConstItem.HO_MAP_VANG);
        // rdItemID.add(2, ConstItem.SAO_PHA_LE);
        // rdItemID.add(2, ConstItem.SAO_PHA_LE_442);
        // rdItemID.add(2, ConstItem.SAO_PHA_LE_443);
        // rdItemID.add(2, ConstItem.SAO_PHA_LE_444);
        // rdItemID.add(2, ConstItem.SAO_PHA_LE_445);
        // rdItemID.add(2, ConstItem.SAO_PHA_LE_446);
        // rdItemID.add(2, ConstItem.SAO_PHA_LE_447);
        rdItemID.add(2, ConstItem.DA_LUC_BAO);
        rdItemID.add(2, ConstItem.DA_SAPHIA);
        rdItemID.add(2, ConstItem.DA_TITAN);
        rdItemID.add(2, ConstItem.DA_THACH_ANH_TIM);
        rdItemID.add(2, ConstItem.DA_RUBY);
        rdItemID.add(3, ConstItem.VANG_190);
        int itemID = rdItemID.next();
        Item newItem = ItemService.gI().createNewItem((short) itemID);
        if (newItem.template.type == 9) {
            newItem.quantity = Util.nextInt(10, 50) * 1000000;
        } else if (newItem.template.type == 14 || newItem.template.type == 30) {
            newItem.quantity = 10;
        } else {
            switch (itemID) {
                case ConstItem.CAY_TRUC: {
                    RandomCollection<ItemOption> rdOption = new RandomCollection<>();
                    rdOption.add(2, new ItemOption(77, 15));// %hp
                    rdOption.add(2, new ItemOption(103, 15));// %hp
                    rdOption.add(1, new ItemOption(50, 15));// %hp
                    newItem.itemOptions.add(rdOption.next());
                }
                break;

                case ConstItem.HO_MAP_VANG: {
                    newItem.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                    newItem.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                    newItem.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                }
                break;

                case ConstItem.NON_HO_VANG:
                case ConstItem.CAI_TRANG_HO_VANG:
                case ConstItem.NON_TRAU_MAY_MAN:
                case ConstItem.NON_TRAU_MAY_MAN_847:
                case ConstItem.NON_TRAU_MAY_MAN_848:
                case ConstItem.NON_CHUOT_MAY_MAN:
                case ConstItem.NON_CHUOT_MAY_MAN_755:
                case ConstItem.NON_CHUOT_MAY_MAN_756:
                    newItem.itemOptions.add(new ItemOption(77, 30));
                    newItem.itemOptions.add(new ItemOption(103, 30));
                    newItem.itemOptions.add(new ItemOption(50, 30));
                    break;
            }
            RandomCollection<Integer> rdDay = new RandomCollection<>();
            rdDay.add(6, 3);
            rdDay.add(3, 7);
            rdDay.add(1, 15);
            int day = rdDay.next();
            newItem.itemOptions.add(new ItemOption(93, day));
        }
        short icon1 = item.template.iconID;
        short icon2 = newItem.template.iconID;
        if (newItem.template.type == 9) {
            Service.getInstance().sendThongBao(pl,
                    "Bạn nhận được " + Util.numberToMoney(newItem.quantity) + " " + newItem.template.name);
        } else if (newItem.quantity == 1) {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được " + newItem.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được x" + newItem.quantity + " " + newItem.template.name);
        }
        CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().addItemBag(pl, newItem, 99);
        InventoryService.gI().sendItemBags(pl);
    }

    private int randClothes(int level) {
        return ConstItem.LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime),
                TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            int param = 8;
            if (item.itemOptions != null) {
                for (ItemOption option : item.itemOptions) {
                    if (option.optionTemplate.id == 72) {
                        param = option.param;
                        break;
                    }
                }
            }
            if (InventoryService.gI().getCountEmptyBag(pl) < param) {
                Service.getInstance().sendThongBao(pl, "Hãy chừa " + param + " ô trống để mở.");
                return;
            }
            int gold = 0;
            int[] listItem = {17, 18, 19, 20, 441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225};
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param < 8) {
                gold = 100000 * param;
                listClothesReward = new int[]{randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 3);
            } else if (param < 10) {
                gold = 250000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 4);
            } else {
                gold = 500000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 5);
                // int ruby = Util.nextInt(1, 5);
                // pl.inventory.ruby += ruby;
                // pl.textRuongGo.add(text + "|1| " + ruby + " Hồng Ngọc");
            }
            for (var i : listClothesReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionClothes(itemReward);
                if (param >= 9) {
                    RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{
                        new RewardService.RatioStar((byte) 3, 1, 3),
                        new RewardService.RatioStar((byte) 4, 1, 4), // new RewardService.RatioStar((byte) 5, 1, 5),
                    // new RewardService.RatioStar((byte) 6, 1, 7),
                    });
                } else {
                    RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{
                        new RewardService.RatioStar((byte) 1, 1, 2),
                        new RewardService.RatioStar((byte) 2, 1, 3), // new RewardService.RatioStar((byte) 3, 1, 4),
                    // new RewardService.RatioStar((byte) 4, 1, 5),
                    });
                }
                InventoryService.gI().addItemBag(pl, itemReward, 0);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            for (var i : listItemReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
                itemReward.quantity = Util.nextInt(1, 5);
                InventoryService.gI().addItemBag(pl, itemReward, 0);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            if (param >= 11) {
                short manhHuyDiet[] = {457, 16, 17, 757};
                itemReward = ItemService.gI().createNewItem(manhHuyDiet[Util.nextInt(manhHuyDiet.length)]);
                itemReward.quantity = 2;
                InventoryService.gI().addItemBag(pl, itemReward, 0);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                    "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK [" + pl.textRuongGo.size() + "]");
            pl.inventory.addGold(gold);
            InventoryService.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl,
                    "Hãy đợi " + TimeUtil.getTimeRemaining(item.createTime, 86400) + " nữa");
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 865: // kiem Z
                if (!player.effectFlagBag.useKiemz) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useKiemz = !player.effectFlagBag.useKiemz;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 994: // vỏ ốc
                break;
            case 995: // cây kem
                break;
            case 996: // cá heo
                break;
            case 997: // con diều
                break;
            case 998: // diều rồng
                if (!player.effectFlagBag.useDieuRong) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useDieuRong = !player.effectFlagBag.useDieuRong;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 999: // mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: // xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: // phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 954:
                if (!player.effectFlagBag.useHoaVang) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHoaVang = !player.effectFlagBag.useHoaVang;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 955:
                if (!player.effectFlagBag.useHoaHong) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHoaHong = !player.effectFlagBag.useHoaHong;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 852:
                if (!player.effectFlagBag.useGayTre) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useGayTre = !player.effectFlagBag.useGayTre;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.getInstance().point(player);
        Service.getInstance().sendFlagBag(player);
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            boolean isItem = false; // kiểm tra đệ có mặt đồ không
            for (int i = 0; i < player.pet.inventory.itemsBody.size(); i++) {
                Item checkItem = player.pet.inventory.itemsBody.get(i);
                if (checkItem.isNotNullItem()) {
                    isItem = true;
                    break;
                }
            }
            if (isItem) {
                Service.getInstance().sendThongBao(player, "Hãy tháo hết trang bị trên người đệ tử");
                return;
            }

            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);

        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    public void hopQuaTanThu(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 14) {
            int gender = pl.gender;
            int[] id = {gender, 6 + gender, 21 + gender, 27 + gender, 12};
            int[] soluong = {1, 1, 1, 1, 1, 1, 10, 10, 10, 10, 10, 10, 10};
            int[] option = {0, 0, 0, 0, 0, 73, 95, 96, 97, 98, 99, 100, 101};
            int[] param = {0, 0, 0, 0, 0, 0, 5, 5, 5, 3, 3, 5, 5};
            int arrLength = id.length - 1;

            for (int i = 0; i < arrLength; i++) {
                if (i < 5) {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    RewardService.gI().initBaseOptionClothes(item);
                    item.itemOptions.add(new ItemOption(107, 4));
                    InventoryService.gI().addItemBag(pl, item, 0);
                } else {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    item.quantity = soluong[i];
                    // item.itemOptions.add(new ItemOption(option[i], param[i]));
                    InventoryService.gI().addItemBag(pl, item, 0);
                }
            }

            int[] idpet = {916, 917, 918, 942, 943, 944, 1046, 1039, 1040};

            Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
            item.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
            item.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
            item.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
            item.itemOptions.add(new ItemOption(93, 3));
            InventoryService.gI().addItemBag(pl, item, 0);

            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Chúc bạn chơi game vui vẻ");
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 14 ô trống để nhận thưởng");
        }
    }

    public void hopQuaShare(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) >= 1) {
            Item item = ItemService.gI().createNewItem((short) 1614);
            item.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
            item.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
            item.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
            item.itemOptions.add(new ItemOption(101, 99));
            InventoryService.gI().addItemBag(pl, item, 0);

            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Chúc bạn chơi game vui vẻ");
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 1 ô trống để nhận thưởng");
        }
    }
    
    public void dao(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) >= 1) {
            Item item = ItemService.gI().createNewItem((short) 1415);
            item.itemOptions.add(new ItemOption(50, 20));
            item.itemOptions.add(new ItemOption(77, 20));
            item.itemOptions.add(new ItemOption(103, 20));
            item.itemOptions.add(new ItemOption(208, 1));
            InventoryService.gI().addItemBag(pl, item, 0);

            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Chúc bạn chơi game vui vẻ");
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 1 ô trống để nhận thưởng");
        }
    }
    
    public void openNRB(Player pl, Item item) {
    if (InventoryService.gI().getCountEmptyBag(pl) > 0) {

        short itemId = (short) Util.nextInt(925, 931);

        Item vatphammora = ItemService.gI().createNewItem(itemId);

        vatphammora.itemOptions.add(new ItemOption(93, 30));

        InventoryService.gI().subQuantityItemsBag(pl, item, 1);

        InventoryService.gI().addItemBag(pl, vatphammora, 99);
        InventoryService.gI().sendItemBags(pl);
        Service.getInstance().sendMoney(pl);

        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        icon[1] = vatphammora.template.iconID;
        CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);

    } else {
        Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
    }
}
    
    private void openbox2010(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {17, 16, 15, 675, 676, 677, 678, 679, 680, 681, 580, 581, 582};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            Item it = ItemService.gI().createNewItem(temp[index]);

            if (temp[index] >= 15 && temp[index] <= 17) {
                it.itemOptions.add(new ItemOption(73, 0));

            } else if (temp[index] >= 580 && temp[index] <= 582 || temp[index] >= 675 && temp[index] <= 681) { // cải
                // trang

                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(95, Util.nextInt(5, 15)));
                it.itemOptions.add(new ItemOption(96, Util.nextInt(5, 15)));

                if (Util.isTrue(1, 200)) {
                    it.itemOptions.add(new ItemOption(74, 0));
                } else {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                }

            } else {
                it.itemOptions.add(new ItemOption(73, 0));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;

            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void capsule8thang3(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {17, 16, 15, 675, 676, 677, 678, 679, 680, 681, 580, 581, 582, 1154, 1155, 1156, 860, 1041,
                1042, 1043, 1103, 1104, 1105, 1106, 954, 955};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            Item it = ItemService.gI().createNewItem(temp[index]);

            if (Util.isTrue(30, 100)) {
                int ruby = Util.nextInt(1, 5);
                pl.inventory.ruby += ruby;
                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 7743);
                PlayerService.gI().sendInfoHpMpMoney(pl);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn nhận được " + ruby + " Hồng Ngọc");
                return;
            }
            if (it.template.type == 5) { // cải trang

                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(117, Util.nextInt(10, 20)));

            } else if (it.template.id == 954 || it.template.id == 955) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
            }

            if (it.template.type == 5 || it.template.id == 954 || it.template.id == 955) {
                if (Util.isTrue(1, 200)) {
                    it.itemOptions.add(new ItemOption(74, 0));
                } else {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                }
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;

            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    public void openRuongvang(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item vatphammora = null;
            short[] icon = new short[2];
            if (Util.isTrue(1, 5)) {
                Item caytruc = vatphammora = ItemService.gI().createNewItem((short) 852);
                caytruc.itemOptions.add(new ItemOption(50, Util.nextInt(8, 12)));
                caytruc.itemOptions.add(new ItemOption(77, Util.nextInt(8, 12)));
                caytruc.itemOptions.add(new ItemOption(103, Util.nextInt(8, 12)));
                caytruc.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            } else if (Util.isTrue(1, 5)) {
                Item haoquang = vatphammora = ItemService.gI().createNewItem((short) 1142);
                haoquang.itemOptions.add(new ItemOption(50, Util.nextInt(8, 12)));
                haoquang.itemOptions.add(new ItemOption(77, Util.nextInt(8, 12)));
                haoquang.itemOptions.add(new ItemOption(103, Util.nextInt(8, 12)));
                haoquang.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            } else if (Util.isTrue(1, 3)) {
                Item biden = vatphammora = ItemService.gI().createNewItem((short) 990);
                biden.itemOptions.add(new ItemOption(77, Util.nextInt(35, 45)));
                biden.itemOptions.add(new ItemOption(80, Util.nextInt(10, 20)));
                biden.itemOptions.add(new ItemOption(3, 100));
                biden.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            } else if (Util.isTrue(1, 3)) {
                Item toctim = vatphammora = ItemService.gI().createNewItem((short) 1210);
                toctim.itemOptions.add(new ItemOption(103, Util.nextInt(35, 45)));
                toctim.itemOptions.add(new ItemOption(81, Util.nextInt(10, 20)));
                toctim.itemOptions.add(new ItemOption(3, 100));
                toctim.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            } else if (Util.isTrue(1, 40)) {
                Item haoquang = vatphammora = ItemService.gI().createNewItem((short) 1142);
                haoquang.itemOptions.add(new ItemOption(50, Util.nextInt(8, 12)));
                haoquang.itemOptions.add(new ItemOption(77, Util.nextInt(8, 12)));
                haoquang.itemOptions.add(new ItemOption(103, Util.nextInt(8, 12)));
            } else if (Util.isTrue(1, 40)) {
                Item caytruc = vatphammora = ItemService.gI().createNewItem((short) 852);
                caytruc.itemOptions.add(new ItemOption(50, Util.nextInt(8, 12)));
                caytruc.itemOptions.add(new ItemOption(77, Util.nextInt(8, 12)));
                caytruc.itemOptions.add(new ItemOption(103, Util.nextInt(8, 12)));
            } else if (Util.isTrue(1, 150)) {
                Item biden = vatphammora = ItemService.gI().createNewItem((short) 990);
                biden.itemOptions.add(new ItemOption(77, Util.nextInt(35, 45)));
                biden.itemOptions.add(new ItemOption(80, Util.nextInt(10, 20)));
                biden.itemOptions.add(new ItemOption(3, 100));
            } else if (Util.isTrue(1, 150)) {
                Item toctim = vatphammora = ItemService.gI().createNewItem((short) 1210);
                toctim.itemOptions.add(new ItemOption(103, Util.nextInt(35, 45)));
                toctim.itemOptions.add(new ItemOption(81, Util.nextInt(10, 20)));
                toctim.itemOptions.add(new ItemOption(3, 100));
            } else {
                Item caytruc = vatphammora = ItemService.gI().createNewItem((short) 852);
                caytruc.itemOptions.add(new ItemOption(50, Util.nextInt(8, 12)));
                caytruc.itemOptions.add(new ItemOption(77, Util.nextInt(8, 12)));
                caytruc.itemOptions.add(new ItemOption(103, Util.nextInt(8, 12)));
                caytruc.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 2 ô trống trong hành trang");
        }
    }

    private void openRuongHoaThan(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {

            short idItem = (short) (1324 + pl.gender);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 97);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openRuongPet1442(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {892, 893, 916, 917, 918, 942, 943, 944};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 90);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openRuongLinhThu1443(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {1295, 1296, 1285, 1286, 1288};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 90);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openRuongHalloween818(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {742, 739};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 95);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openRuongHalloween2012(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {1273, 1271, 1269};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 95);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openRuongEventTrungThu_1_1338(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {1263, 1445, 17, 18, 19, 20, 933, 381, 382, 383, 384};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 90);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openRuongEventTrungThu_2_1339(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {1118, 517, 17, 18, 19, 20, 1150, 1151, 1152, 1153};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 90);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openRuongEventTrungThu_Hop_Qua_1340(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {1333, 1334, 1335, 17, 18, 19, 20, 462, 1328, 1329, 1330, 1331, 1119, 1120};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 90);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            Service.getInstance().sendThongBao(pl, "Bạn nhận được " + vatphammora.template.name + " và 1 điểm sự kiện!");
            InventoryService.gI().sendItemBags(pl);
            pl.RuongbauPoint += 1;
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openRuongThuCuoi1444(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {1292, 1293, 1294, 1322, 1413, 1414};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 97);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openRuongDeoLung1437(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {1415, 1349, 1350, 1274, 1258, 1230, 1231, 1206};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);

            vatphammora.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(6, 10)));
            vatphammora.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(6, 10)));
            vatphammora.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(6, 10)));
            if (Util.isTrue(99, 100)) {
                vatphammora.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(6, 7)));
            }

            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openRuongCaiTrang1438(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {1038, 1037};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 99);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void extendClan(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (pl.clan != null) {
            ClanService.gI().ExtendClan(pl);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn chưa tham gia vào bang");
        }

    }

    private void openRuongbac(Player pl, Item item) {
        Item chiaKhoa = InventoryService.gI().findItemBag(pl, (short) 1378);
        if (chiaKhoa == null || chiaKhoa.quantity < 1) {
            Service.getInstance().sendThongBao(pl, "Cần một chìa khóa bạc để mở rương bạc");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemList[] = {15, 16, 629, 1394, 1994, 1995, 1372, 1039, 1040, 1206, 743, 849, 472, 473, 942, 943,
                944, 1284, 996, 998, 680, 679};
            short idItem = Util.randomItem(itemList);
            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 97);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().subQuantityItemsBag(pl, chiaKhoa, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openTuiPrivate_1347(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item vatphammora = null;
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (Util.isTrue(1, 2)) {
                Item cat = vatphammora = ItemService.gI().createNewItem((short) 1230);
                cat.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                cat.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
                cat.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
                if (Util.isTrue(97, 100)) {
                    cat.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                }
            } else {
                Item cat = vatphammora = ItemService.gI().createNewItem((short) 1231);
                cat.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                cat.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
                cat.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
                if (Util.isTrue(97, 100)) {
                    cat.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                }
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 2 ô trống trong hành trang");
        }

    }

    public void openBoxItem(Player pl, Item item, int idBox) {
        try {
            switch (idBox) {
                case 0:
                    if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                        short[] icon = new short[2];
                        icon[0] = item.template.iconID;

                        Item it = ItemService.gI().createNewItem((short) 884);
                        it.itemOptions.add(new ItemOption(50, Util.nextInt(8, 20)));
                        it.itemOptions.add(new ItemOption(77, Util.nextInt(8, 20)));
                        it.itemOptions.add(new ItemOption(103, Util.nextInt(8, 20)));
                        if (Util.isTrue(85, 100)) {
                            it.itemOptions.add(new ItemOption(5, Util.nextInt(10, 25)));
                        } else if (Util.isTrue(85, 100)) {
                            it.itemOptions.add(new ItemOption(5, Util.nextInt(25, 30)));
                        } else {
                            it.itemOptions.add(new ItemOption(5, Util.nextInt(30, 35)));
                        }
                        
                        icon[1] = it.template.iconID;
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                        InventoryService.gI().addItemBag(pl, it, 0);
                        InventoryService.gI().sendItemBags(pl);
                        Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã nhận được: " + it.template.name + " x" + it.quantity);
                        break;
                    } else {
                        Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                    }
                    break;
                case 1:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openBoxItem1(Player pl, Item item, int idBox) {
        try {
            switch (idBox) {
                case 0:
                    if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                        short[] icon = new short[2];
                        icon[0] = item.template.iconID;
                        int tempID = 1205;
                        Item it = ItemService.gI().createNewItem((short) tempID);

                        it.itemOptions.add(new ItemOption(77, Util.nextInt(8, 35)));
                        it.itemOptions.add(new ItemOption(103, Util.nextInt(8, 35)));
                        it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 150)));
                        it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 25)));
                        it.itemOptions.add(new ItemOption(30, (1)));
                        icon[1] = it.template.iconID;
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                        InventoryService.gI().addItemBag(pl, it, 0);
                        InventoryService.gI().sendItemBags(pl);
                        Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã nhận được: " + it.template.name + " x" + it.quantity);
                        break;
                    } else {
                        Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                    }
                    break;
                case 1:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void capsuleTet(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            int ct = Util.nextInt(1155, 1157);
            int dnc = Util.nextInt(220, 224);
            int nr = Util.nextInt(17, 19);
            int nrBang = Util.nextInt(925, 931);

            if (Util.isTrue(5, 90)) {
                int ruby = Util.nextInt(10_000_000, 50_000_000);
                pl.inventory.gold += ruby;
                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 7743);
                PlayerService.gI().sendInfoHpMpMoney(pl);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn nhận được " + ruby + " Vàng");
            } else {
                int[] temp = {dnc, nr, 1588, 1587, 884, 1195, 1196, 674};
                byte index = (byte) Util.nextInt(0, temp.length - 1);
                short[] icon = new short[2];
                icon[0] = item.template.iconID;
                Item it = ItemService.gI().createNewItem((short) temp[index]);

                if (temp[index] >= 220 && temp[index] <= 224) { // da nang cap
                    it.quantity = 5;
                } else if (temp[index] == 1587) { // mu noel do
                    it.itemOptions.add(new ItemOption(50, Util.nextInt(25, 35)));
                    it.itemOptions.add(new ItemOption(77, Util.nextInt(25, 35)));
                    it.itemOptions.add(new ItemOption(103, Util.nextInt(25, 35)));
                    it.itemOptions.add(new ItemOption(215, Util.nextInt(15, 25)));
                    it.itemOptions.add(new ItemOption(30, 0));
                    if (Util.isTrue(90, 100)) {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                    }
                } else if (temp[index] == 1588) { // mu noel do
                    it.itemOptions.add(new ItemOption(50, Util.nextInt(25, 35)));
                    it.itemOptions.add(new ItemOption(77, Util.nextInt(25, 35)));
                    it.itemOptions.add(new ItemOption(103, Util.nextInt(25, 35)));
                    it.itemOptions.add(new ItemOption(214, Util.nextInt(15, 25)));
                    it.itemOptions.add(new ItemOption(30, 0));
                    if (Util.isTrue(90, 100)) {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                    }
                } else if (temp[index] == 884) { // mu noel do
                    it.itemOptions.add(new ItemOption(50, Util.nextInt(25, 35)));
                    it.itemOptions.add(new ItemOption(77, Util.nextInt(25, 35)));
                    it.itemOptions.add(new ItemOption(103, Util.nextInt(25, 35)));
                    it.itemOptions.add(new ItemOption(5, Util.nextInt(20, 40)));
                    it.itemOptions.add(new ItemOption(30, 0));
                    if (Util.isTrue(90, 100)) {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                    }
                } else {
                    it.itemOptions.add(new ItemOption(73, 0));
                }
                PlayerDAO.addDiemTetDuong(pl, pl.id, 1);
                pl.tetduong++; // chỉ để hiển thị
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                icon[1] = it.template.iconID;
                CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + item.template.name + " và 1 điểm sự kiện Tết Dương Lịch");
                InventoryService.gI().addItemBag(pl, it, 0);
                InventoryService.gI().sendItemBags(pl);
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }
    public void openboxsukien(Player pl, Item item, int idsukien) {
        try {
            switch (idsukien) {
                case 1:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] temp = {16, 15, 865, 999, 1000, 1001, 739, 742, 743};
                            int[][] gold = {{5000, 20000}};
                            byte index = (byte) Util.nextInt(0, temp.length - 1);
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;

                            Item it = ItemService.gI().createNewItem(temp[index]);

                            if (temp[index] >= 15 && temp[index] <= 16) {
                                it.itemOptions.add(new ItemOption(73, 0));

                            } else if (temp[index] == 865) {

                                it.itemOptions.add(new ItemOption(30, 0));

                                if (Util.isTrue(1, 30)) {
                                    it.itemOptions.add(new ItemOption(93, 365));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 999) { // mèo mun
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1000) { // xiên cá
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1001) { // Phóng heo
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else if (temp[index] == 739) { // cải trang Billes

                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 45)));

                                if (Util.isTrue(1, 100)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else if (temp[index] == 742) { // cải trang Caufila

                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 45)));

                                if (Util.isTrue(1, 100)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 743) { // chổi bay
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else {
                                it.itemOptions.add(new ItemOption(73, 0));
                            }
                            InventoryService.gI().addItemBag(pl, it, 0);
                            icon[1] = it.template.iconID;

                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryService.gI().sendItemBags(pl);

                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                        break;
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                case ConstEvent.SU_KIEN_20_11:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] temp = {16, 15, 1039, 954, 955, 710, 711, 1040, 2023, 999, 1000, 1001};
                            byte index = (byte) Util.nextInt(0, temp.length - 1);
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            Item it = ItemService.gI().createNewItem(temp[index]);
                            if (temp[index] >= 15 && temp[index] <= 16) {
                                it.itemOptions.add(new ItemOption(73, 0));
                            } else if (temp[index] == 1039) {
                                it.itemOptions.add(new ItemOption(50, 10));
                                it.itemOptions.add(new ItemOption(77, 10));
                                it.itemOptions.add(new ItemOption(103, 10));
                                it.itemOptions.add(new ItemOption(30, 0));
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                            } else if (temp[index] == 954) {
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(79, 80)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 955) {
                                it.itemOptions.add(new ItemOption(50, 20));
                                it.itemOptions.add(new ItemOption(77, 20));
                                it.itemOptions.add(new ItemOption(103, 20));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(79, 80)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 710) {// cải trang quy lão kame
                                it.itemOptions.add(new ItemOption(50, 22));
                                it.itemOptions.add(new ItemOption(77, 20));
                                it.itemOptions.add(new ItemOption(103, 20));
                                it.itemOptions.add(new ItemOption(194, 0));
                                it.itemOptions.add(new ItemOption(160, 35));
                                if (Util.isTrue(99, 100)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 711) { // cải trang jacky chun
                                it.itemOptions.add(new ItemOption(50, 23));
                                it.itemOptions.add(new ItemOption(77, 21));
                                it.itemOptions.add(new ItemOption(103, 21));
                                it.itemOptions.add(new ItemOption(195, 0));
                                it.itemOptions.add(new ItemOption(160, 50));
                                if (Util.isTrue(99, 100)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1040) {
                                it.itemOptions.add(new ItemOption(50, 10));
                                it.itemOptions.add(new ItemOption(77, 10));
                                it.itemOptions.add(new ItemOption(103, 10));
                                it.itemOptions.add(new ItemOption(30, 0));
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                            } else if (temp[index] == 2023) {
                                it.itemOptions.add(new ItemOption(30, 0));
                            } else if (temp[index] == 999) { // mèo mun
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1000) { // xiên cá
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1001) { // Phóng heo
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else {
                                it.itemOptions.add(new ItemOption(73, 0));
                            }
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            icon[1] = it.template.iconID;
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            InventoryService.gI().addItemBag(pl, it, 0);
                            int ruby = Util.nextInt(1, 5);
                            pl.inventory.ruby += ruby;
                            InventoryService.gI().sendItemBags(pl);
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().sendThongBao(pl, "Bạn được tặng kèm " + ruby + " Hồng Ngọc");
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case ConstEvent.SU_KIEN_NOEL:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            int ct = Util.nextInt(1155, 1157);
                            int dnc = Util.nextInt(220, 224);
                            int nr = Util.nextInt(17, 19);
                            int nrBang = Util.nextInt(925, 931);

                            if (Util.isTrue(5, 90)) {
                                int ruby = Util.nextInt(10_000_000, 50_000_000);
                                pl.inventory.gold += ruby;
                                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 7743);
                                PlayerService.gI().sendInfoHpMpMoney(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryService.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Bạn nhận được " + ruby + " Vàng");
                            } else {
                                int[] temp = { dnc, nr, nrBang, ct, 936, 746, 1172};
                                byte index = (byte) Util.nextInt(0, temp.length - 1);
                                short[] icon = new short[2];
                                icon[0] = item.template.iconID;
                                Item it = ItemService.gI().createNewItem((short) temp[index]);

                                if (temp[index] >= 220 && temp[index] <= 224) { // da nang cap
                                    it.quantity = 10;
                                } else if (temp[index] >= 1155 && temp[index] <= 1157) { // mu noel do
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(25, 35)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(25, 35)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(25, 35)));
                                    it.itemOptions.add(new ItemOption(101, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(241, Util.nextInt(2, 5)));
                                    it.itemOptions.add(new ItemOption(106, 0));
                                     if (Util.isTrue(80, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                                    }
                                } else if (temp[index] == 936) { // tuan loc
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(8, 20)));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                     if (Util.isTrue(90, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                                    }
                                } else if (temp[index] == 746) { // xe truot tuyet
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(8, 20)));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(90, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                                    }
                                } else if (temp[index] == nrBang) { // xe truot tuyet
                                    it.itemOptions.add(new ItemOption(93, 30));
//                                    if (Util.isTrue(80, 100)) {
//                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
//                                    }
                                } else if (temp[index] == 1172) { // tuan loc
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(8, 20)));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                     if (Util.isTrue(90, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                                    }
                                } else {
                                    it.itemOptions.add(new ItemOption(73, 0));
                                }
                                pl.event.addEventPoint(1);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                icon[1] = it.template.iconID;
                                CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                                Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + item.template.name + " và 1 điểm sự kiện noel");
                                InventoryService.gI().addItemBag(pl, it, 0);
                                InventoryService.gI().sendItemBags(pl);
                            }
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case ConstEvent.SU_KIEN_TET:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            RandomCollection<Integer> rd = Manager.HOP_QUA_TET;
                            int tempID = rd.next();
                            Item it = ItemService.gI().createNewItem((short) tempID);
                            if (it.template.type == 11) {// FLAGBAG
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(5, 20)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(5, 20)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(5, 20)));
                            } else if (tempID >= 1159 && tempID <= 1161) {
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(106, 0));
                            } else if (tempID == ConstItem.CAI_TRANG_SSJ_3_WHITE) {
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(5, Util.nextInt(10, 25)));
                                it.itemOptions.add(new ItemOption(104, Util.nextInt(5, 15)));
                            }
                            int type = it.template.type;
                            if (type == 5 || type == 11) {// cải trang & flagbag
                                if (Util.isTrue(199, 200)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                                it.itemOptions.add(new ItemOption(199, 0));// KHÔNG THỂ GIA HẠN
                            } else if (type == 23) {// thú cưỡi
                                if (Util.isTrue(199, 200)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                                }
                            }
                            if (tempID >= ConstItem.MANH_AO && tempID <= ConstItem.MANH_GANG_TAY) {
                                it.quantity = Util.nextInt(5, 15);
                            } else {
                                it.itemOptions.add(new ItemOption(74, 0));
                            }
                            icon[1] = it.template.iconID;
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            InventoryService.gI().addItemBag(pl, it, 0);
                            InventoryService.gI().sendItemBags(pl);
                            break;
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error("Lỗi mở hộp quà", e);
        }
    }

    private void openPhaoHoa(Player player, Item itemUse) { // xong
        if (itemUse.quantity < 1) {
            return;
        }

        // sự kiện tết
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        short[] listGift = {380, 17, 18, 19, 20, 16, 18, 19, 20, 16, 2031, 2032, 1177, 1178, 1179, 1180, 1181};

        useItemTime(player, itemUse);

        int random = (short) Util.nextInt((short) listGift.length);
        short idItem = listGift[random];
        Item item = ItemService.gI().createNewItem((short) idItem);
        ItemService.gI().OptionAllItem(item, 95);
        InventoryServiceNew.gI().subQuantityItemsBag(player,
                itemUse, 1);
        InventoryService.gI().addItemBag(player, item, 999);
        InventoryService.gI().sendItemBags(player);
        Manager.EVENT_POINT_TET_2024++;
        Service.getInstance().sendThongBao(player, "Bạn vừa bắn pháo hoa, nhận được " + item.template.name
                + " và 1 điểm pháo hoa cho toàn bộ máy chủ");

    }

    private void openRuongHe2024(Player player, Item itemUse) { // xong
        if (itemUse.quantity < 1) {
            return;
        }

        // sự kiện hè
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        InventoryServiceNew.gI().subQuantityItemsBag(player,
                itemUse, 1);
        short[] listGift = {16, 1150, 1151, 1152, 1153, 638, 1420, 1421, 1422,
            994, 995, 996, 997, 998, 1234, 1235, 1236, 1370};

        int random = (short) Util.nextInt((short) listGift.length - 1);
        short idItem = listGift[random];
        Item item = ItemService.gI().createNewItem((short) idItem);
        ItemService.gI().OptionAllItem(item, 95);
        player.event.addEventPoint(1);
        InventoryService.gI().addItemBag(player, item, 999);
        InventoryService.gI().sendItemBags(player);

        Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item.template.name + " và 1 điểm sự kiện hè");

    }

    private void openCapsuleHit(Player player, Item itemUse) { // xong
        if (itemUse.quantity < 1) {
            return;
        }

        // sự kiện hè
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        InventoryServiceNew.gI().subQuantityItemsBag(player,
                itemUse, 1);
        Item item = ItemService.gI().createNewItem((short) 884);
        ItemService.gI().OptionAllItem(item, 96);

        InventoryService.gI().addItemBag(player, item, 999);
        InventoryService.gI().sendItemBags(player);

        Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item.template.name);

    }

    private void openCapsuleThoiKhong(Player player, Item itemUse) {
        if (itemUse.quantity < 1) {
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
            return;
        }

        InventoryServiceNew.gI().subQuantityItemsBag(player,
                itemUse, 1);
        short[] listItem = {898, 904, 883, 2040};
        short idItem = Util.randomItem(listItem);
        Item item2 = ItemService.gI().createNewItem(idItem);

        if (item2 != null) {
            ItemService.gI().OptionAllItem(item2, 99);
            InventoryServiceNew.gI().addItemBag(player, item2);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item2.template.name);
        }

        InventoryServiceNew.gI().sendItemBags(player);

    }

    private void openRuongKhoBauTheBai(Player pl, Item item) {

        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {

            short idItem = 0;
            if (Util.isTrue(80, 100)) {// dễ ra
                short itemList_de[] = {15, 16, 17, 1994, 1995, 1195, 1196, 1417, 1182};
                idItem = Util.randomItem(itemList_de);
            } else if (Util.isTrue(80, 100)) { // khó ra
                short itemList_kho[] = {1372, 1279, 1287, 1292, 1259, 1378, 893};
                idItem = Util.randomItem(itemList_kho);
            } else { // khó ra hơn
                short itemList_kho[] = {14, 15};
                idItem = Util.randomItem(itemList_kho);
            }

            Item vatphammora = ItemService.gI().createNewItem(idItem);
            ItemService.gI().OptionAllItem(vatphammora, 99);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            icon[1] = vatphammora.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vatphammora, 99);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy để ít nhất 1 ô trống trong hành trang");
        }

    }

    private void openCapsuleBac(Player player, Item itemUse) {
        if (itemUse.quantity < 1) {
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
            return;
        }

        int star = Util.nextInt(3, 5);
        int levelItem = Util.nextInt(8, 10);
        InventoryServiceNew.gI().subQuantityItemsBag(player,
                itemUse, 1);
        if (Util.isTrue(50, 100)) {
            short idItem = ConstItem.doSKHVip[Util.nextInt(0, 4)][player.gender][levelItem];
            Item item2 = ItemService.gI().createNewItem(idItem);

            if (item2 != null) {
                ItemService.gI().OptionAllItem(item2, 0);
                item2.itemOptions.add(new ItemOption(ConstOption.SAO_PHA_LE_CHUA_EP, star));
                InventoryServiceNew.gI().addItemBag(player, item2);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item2.template.name);
            }
        } else {
            short[] listItem = {16, 17, 741, 937, 941, 954, 955, 1994, 1150, 1151, 1152, 1153};
            short idItem = Util.randomItem(listItem);
            Item item2 = ItemService.gI().createNewItem(idItem, 1);
            if (item2 != null) {
                ItemService.gI().OptionAllItem(item2, 0);

                InventoryServiceNew.gI().addItemBag(player, item2);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item2.template.name);
            }
        }

        InventoryServiceNew.gI().sendItemBags(player);

    }

    private void openCapsuleVang(Player player, Item itemUse) {
        if (itemUse.quantity < 1) {
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
            return;
        }
        // int star = Util.nextInt(3, 6);
        // int levelItem = Util.nextInt(11, 13);
        // InventoryServiceNew.gI().subQuantityItemsBag(player,
        // itemUse, 1);
        // short idItem = ConstItem.doSKHVip[Util.nextInt(0,
        // 4)][player.gender][levelItem];
        // Item item2 = ItemService.gI().createNewItem(idItem);

        // if (item2 != null) {
        // ItemService.gI().OptionAllItem(item2, 0);
        // item2.itemOptions.add(new ItemOption(ConstOption.SAO_PHA_LE_CHUA_EP, star));
        // InventoryServiceNew.gI().addItemBag(player, item2);
        // Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " +
        // item2.template.name);
        // }
        InventoryServiceNew.gI().sendItemBags(player);

    }

    private void openRuongDiemThuong(Player player, Item itemUse) {
        if (itemUse.quantity < 1) {
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
            return;
        }
        short listItem[] = {14, 15, 16, 733, 734, 735, 1270, 1207, 1281, 800, 852, 1197, 1231,
            1276, 1294, 1294, 1418, 1372, 1415, 1410, 1411, 860, 464, 1369, 463};
        short idItem = Util.randomItem(listItem);
        Item item2 = ItemService.gI().createNewItem(idItem);
        if (item2 != null) {
            ItemService.gI().OptionAllItem(item2, 95);
            InventoryServiceNew.gI().subQuantityItemsBag(player,
                    itemUse, 1);
            InventoryServiceNew.gI().addItemBag(player, item2);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item2.template.name);
        }

        InventoryServiceNew.gI().sendItemBags(player);

    }

    private void openHopQuaNho(Player player, Item itemUse) {
        if (itemUse.quantity < 1) {
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
            return;
        }

        short idItem[] = {15, 16, 17, 18, 19, 20, 757, 342, 343, 344, 345, 380, 381, 382, 383, 384, 385, 1994, 1150,
            1151, 1152, 1153, 1362};
        Item item2 = ItemService.gI().createNewItem(Util.randomItem(idItem));

        if (item2 != null) {
            ItemService.gI().OptionAllItem(item2, 0);
            InventoryServiceNew.gI().subQuantityItemsBag(player,
                    itemUse, 1);
            InventoryServiceNew.gI().addItemBag(player, item2);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + item2.template.name);
        }

        InventoryServiceNew.gI().sendItemBags(player);

    }

    private void changeSkill2(Player pl, Item item) {
        if (item.quantity <= 0) {
            return;
        }
        if (pl.pet != null) {
            if (pl.pet.playerSkill.skills.get(1).skillId != -1) {
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                InventoryServiceNew.gI().sendItemBags(pl);
                pl.pet.openSkill2();
                Service.getInstance().sendThongBao(pl, "Đổi chiêu 2 đệ tử thành công");
                // if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                // pl.pet.openSkill3();
                // }\
            } else {
                Service.getInstance().sendThongBao(pl, "Ít nhất đệ tử phải có chiêu 2!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn chưa có đệ tử");
            return;
        }

    }

    private void BlackBall(Player player, Item itemUse) {
        if (itemUse.quantity < 1) {
            return;
        }
        int star = 0;
        for (ItemOption io : itemUse.itemOptions) {
            if (io.optionTemplate.id == ConstOption.CAP) {
                star = io.param;
            }
        }
        if (star > 0 && star < 8) {
            player.rewardBlackBall.reward((byte) star);
            Service.getInstance().sendThongBao(player, "Chúc mừng bang hội của bạn đã "
                    + "dành chiến thắng ngọc rồng sao đen " + star + " sao");
            if (player.clan != null) {
                try {
                    List<Player> players = player.clan.membersInGame;
                    List<Player> playerGive = new ArrayList<>();
                    for (Player pl : players) {
                        if (pl != null && !player.equals(pl)) {
                            playerGive.add(pl);
                        }
                    }
                    for (Player pl : playerGive) {
                        pl.rewardBlackBall.reward((byte) star);
                        Service.getInstance().sendThongBao(pl, "Chúc mừng bang hội của bạn đã "
                                + "dành chiến thắng ngọc rồng sao đen " + star + " sao");

                    }
                } catch (Exception e) {
                    Log.error(UseItem.class, e,
                            "Loi ban thuong ngoc rong den "
                            + star + " sao cho clan " + player.clan.id);
                }
            }
        } else {
            Service.getInstance().sendThongBao(player, "Có lỗi xảy ra");
        }

        InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    private void resetSkill(Player player, Item itemUse) {
        if (itemUse.quantity < 1) {
            return;
        }
        if (player.isAdmin() || Util.canDoWithTime(player.lastTimeResetSkill, 20000)) {
            player.lastTimeResetSkill = System.currentTimeMillis();
            for (Skill skill : player.playerSkill.skills) {
                skill.lastTimeUseThisSkill = 0;

            }
            Service.getInstance().sendTimeSkill(player);
            Service.getInstance().sendThongBao(player, "Kỹ năng của bạn đã được phục hồi");
            Service.getInstance().chat(player, "Phục hồi kỹ năng");
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Hãy đợi 20 giây");
        }

    }

    private void changeSkill3(Player pl, Item item) {
        if (item.quantity <= 0) {
            return;
        }
        if (pl.pet != null) {
            if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                InventoryServiceNew.gI().sendItemBags(pl);
                pl.pet.openSkill3();
                Service.getInstance().sendThongBao(pl, "Đổi chiêu 3 đệ tử thành công");
                // if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                // pl.pet.openSkill3();
                // }\
            } else {
                Service.getInstance().sendThongBao(pl, "Ít nhất đệ tử phải có chiêu 3!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn chưa có đệ tử");
            return;
        }

    }

    private void changeSkill4(Player pl, Item item) {
        if (item.quantity <= 0) {
            return;
        }
        if (pl.pet != null) {
            if (pl.pet.playerSkill.skills.get(3).skillId != -1) {
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                InventoryServiceNew.gI().sendItemBags(pl);
                pl.pet.openSkill4();
                Service.getInstance().sendThongBao(pl, "Đổi chiêu 4 đệ tử thành công");
                // if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                // pl.pet.openSkill3();
                // }\
            } else {
                Service.getInstance().sendThongBao(pl, "Ít nhất đệ tử phải có chiêu 4!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn chưa có đệ tử");
            return;
        }

    }

    private void openSieuPhaoHoa(Player player, Item itemUse) { // xong
        if (itemUse.quantity < 1) {
            return;
        }

        // sự kiện tết
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        short[] listGift = {15, 16, 1195, 1196, 849, 1185, 1186, 2031, 2032, 1279};// 2031, 2032,

        useItemTime(player, itemUse);
        // player.event.addEventPoint(1);
        int random = (short) Util.nextInt((short) listGift.length);
        short idItem = listGift[random];
        Item item = ItemService.gI().createNewItem((short) idItem);
        int quantity = 1;
        if (idItem == 15) {

        } else if (idItem == 849) {// ván bay pháo thăng thiên
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(1, 3)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 3)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3)));
            item.itemOptions.add(new ItemOption(ConstOption.NE_DON, Util.nextInt(1, 3)));
            if (Util.isTrue(80, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }

        } else if (idItem == 1185) {// vật phẩm đeo lưng 1
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(3, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(3, 10)));
            if (Util.isTrue(80, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }
        } else if (idItem == 1186) {// vật phẩm đeo lưng 2
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(3, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(3, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(3, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(8, 10)));
            if (Util.isTrue(80, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }

        } // else if (idItem == 1360) {// cải trang bông băng
        // item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(15,
        // 25)));
        // item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(15,
        // 25)));
        // item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(15,
        // 25)));
        // item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(10,
        // 20)));
        // item.itemOptions.add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH,
        // Util.nextInt(1)));
        // if (Util.isTrue(80, 100)) {
        // item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1,
        // 5)));
        // }
        // }
        else if (idItem == 1279) {// linh thú
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(1, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 12)));
            item.itemOptions.add(new ItemOption(5, Util.nextInt(1, 7)));

            if (Util.isTrue(80, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }

        } else {
            quantity = Util.nextInt(1, 4);
        }
        item.quantity = quantity;
        InventoryService.gI().addItemBag(player, item, 999);
        InventoryService.gI().sendItemBags(player);
        InventoryServiceNew.gI().subQuantityItemsBag(player,
                itemUse, 1);
        Manager.EVENT_POINT_TET_2024 += 5;
        Service.getInstance().sendThongBao(player,
                "Bạn vừa bắn pháo hoa, nhận được " + quantity + " " + item.template.name
                + " , và 1 điểm sự kiện cho bản thân, 5 điểm pháo hoa cho toàn bộ máy chủ");

    }

    private void openHopQuaTet2024(Player player, Item itemUse) { // xong
        if (itemUse.quantity < 1) {
            return;
        }
        // sự kiện tết
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        short[] listGift = {15, 16, 1150, 1151, 1152, 897, 2031, 2032, 1195,
            1196, 849, 1185, 1186, 1244, 1284, 1285, 1201, 898};

        // player.event.addEventPoint(1);
        int random = (short) Util.nextInt((short) listGift.length);
        short idItem = listGift[random];
        Item item = ItemService.gI().createNewItem((short) idItem);
        int quantity = 1;
        if (idItem == 15) {

        } else if (idItem == 1185 || idItem == 1186) {// rùa bay
            item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 7)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 10)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 10)));
            if (Util.isTrue(99, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }
        } else if (idItem == 897) {// rùa bay
            item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 2)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 3)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 4)));
            if (Util.isTrue(50, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }

        } else if (idItem == 1292) {// Blue Fly Ice Dragon (ván bay)
            item.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(1, 5)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 5)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 5)));

            if (Util.isTrue(90, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }

        } else if (idItem == 1289) {// vật phẩm đeo lưng
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(5, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 3)));
            if (Util.isTrue(95, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }
        } else if (idItem == 1244 || idItem == 1285) {// MINI pet
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(1, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 12)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 12)));
            if (Util.isTrue(95, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }
        } else if (idItem == 898) {// cải trang ssj4
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(30, 35)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(30, 35)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(30, 35)));
            item.itemOptions.add(new ItemOption(ConstOption.GIAP, Util.nextInt(15, 25)));
            item.itemOptions.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(5, 8)));
            item.itemOptions.add(new ItemOption(235, 1));
            if (Util.isTrue(99, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }
        } else if (idItem == 1201) {// cải trang ssj4
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(30, 35)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(30, 35)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(30, 35)));
            item.itemOptions.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(1, 5)));
            if (Util.isTrue(99, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }

        } else if (idItem == 1284) {// linh thú
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(1, 10)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 10)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 10)));
            if (Util.isTrue(95, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }

        } else if (idItem == 2060) {// linh thú2
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(1, 10)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(1, 10)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(1, 10)));
            if (Util.isTrue(95, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }

        } else {
            quantity = Util.nextInt(1, 4);
        }
        item.quantity = quantity;
        InventoryService.gI().addItemBag(player, item, 999);
        InventoryService.gI().sendItemBags(player);
        InventoryService.gI().subQuantityItemsBag(player, itemUse, 1);
        Service.getInstance().sendThongBao(player,
                "Bạn vừa  nhận được " + quantity + " " + item.template.name
                + " , và 1 điểm sự kiện");

    }

    private void open_Kho_bau_hai_tac(Player player, Item itemUse) { // xong
        if (itemUse.quantity < 1) {
            return;
        }
        // sự kiện tết
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        short[] listGift = {15, 16, 1150, 1151, 1152, 1153, 16, 1150, 1151, 1152, 1153, 1234, 1235, 1236, 996, 1142,
            1259, 1292, 1287, 1283, 1994,
            1995, 1996, 987, 1376, 898, 898};

        int random = (short) Util.nextInt((short) listGift.length);
        short idItem = listGift[random];
        Item item = ItemService.gI().createNewItem((short) idItem);
        ItemService.gI().OptionAllItem(item, 0);
        int quantity = 1;
        switch (idItem) {
            case 15:
            case 1996:
                break;
            case 1234:
            case 1235:
            case 1236: {// cải trang
                if (Util.isTrue(95, 100)) {
                    item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
                }
            }
            break;
            case 898:
            case 996:
            case 1142:
            case 1259:
            case 1292:
            case 1287:
            case 1283: {// cải trang
                if (Util.isTrue(90, 100)) {
                    item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
                }
            }
            break;
            default:
                quantity = Util.nextInt(1, 4);
                break;
        }

        item.quantity = quantity;
        InventoryService.gI().subQuantityItemsBag(player, itemUse, 1);
        InventoryService.gI().addItemBag(player, item, 999);
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendThongBao(player,
                "Bạn vừa  nhận được " + quantity + " " + item.template.name
                + " , và 1 điểm kho báu ");

    }

    private void open_ruong_cuoi(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1413, 1414};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(temp[index]);
            if (it.template.id == 1413 || it.template.id == 1414) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 5)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 5)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 5)));
                if (Util.isTrue(99, 100)) {
                    it.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(5, 7)));// hsd
                }

            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;

            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void open_ruong_may_man(Player player, Item itemUse) { // xong
        if (itemUse.quantity < 1) {
            return;
        }
        // sự kiện tết
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        short[] listGift = {1410, 1412};

        int random = (short) Util.nextInt((short) listGift.length);
        short idItem = listGift[random];
        Item item = ItemService.gI().createNewItem((short) idItem);
        ItemService.gI().OptionAllItem(item, 0);
        int quantity = 1;
        switch (idItem) {
            case 15:
            case 1996:
                break;

            case 1410:
            case 1412: {// cải trang
                item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(25, 35)));
                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(25, 35)));
                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(25, 35)));
                if (Util.isTrue(90, 100)) {
                    item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
                }
            }
            break;

            default:
                quantity = Util.nextInt(1, 4);
                break;
        }

        item.quantity = quantity;
        InventoryService.gI().subQuantityItemsBag(player, itemUse, 1);
        InventoryService.gI().addItemBag(player, item, 999);
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendThongBao(player,
                "Bạn vừa  nhận được " + quantity + " " + item.template.name);

    }

    private void openCapsuleTet2024(Player player, Item itemUse) { // xong
        if (itemUse.quantity < 1) {
            return;
        }

        // sự kiện tết
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        short ct = (short) (843 + player.gender);
        if (player.pet != null && Util.isTrue(1, 2)) {
            ct = (short) (843 + player.pet.gender);
        }
        short[] listGift = {16, 1150, 1151, 1152, 1196, 1195, 553, 554, 17, 18, 19, 20, 380, 380, 17, 18,
            19, 20, 380, 380, ct};

        int random = (short) Util.nextInt((short) listGift.length);
        short idItem = listGift[random];
        Item item = ItemService.gI().createNewItem((short) idItem);
        if (idItem == 843 || idItem == 844 || idItem == 845) {
            item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(15, 25)));
            item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(15, 25)));
            item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(15, 25)));
            item.itemOptions.add(new ItemOption(ConstOption.GIAP_PT, Util.nextInt(5, 25)));
            item.itemOptions.add(new ItemOption(ConstOption.HOI_HP_30_S, Util.nextInt(15, 65)));
            item.itemOptions.add(new ItemOption(ConstOption.HOI_KI_30_S, Util.nextInt(15, 65)));
            item.itemOptions.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(80, 150)));

            if (Util.isTrue(99, 100)) {
                item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 5)));
            }
        }
        InventoryService.gI().addItemBag(player, item, 999);
        InventoryService.gI().sendItemBags(player);
        InventoryService.gI().subQuantityItemsBag(player, itemUse, 1);
        Service.getInstance().sendThongBao(player,
                "Bạn vừa nhận được " + item.template.name);

    }

    private void openThiepChucTet(Player player, Item itemUse) { // xong
        if (itemUse.quantity < 1) {
            return;
        }

        // sự kiện tết
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn cần có ít nhất 1 ô trống trong hành trang");
            return;
        }
        short[] listGift = {16, 1150, 1151, 1152, 2032, 2031};

        int random = (short) Util.nextInt((short) listGift.length);
        short idItem = listGift[random];
        Item item = ItemService.gI().createNewItem((short) idItem);

        InventoryService.gI().subQuantityItemsBag(player, itemUse, 1);
        InventoryService.gI().addItemBag(player, item, 999);
        InventoryService.gI().sendItemBags(player);

        Service.getInstance().sendThongBaoOK(player,
                "Chúc mừng năm mới 2024, bạn vừa nhận được " + item.template.name);

    }

    private void openboxkichhoat(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 441, 442, 447, 2010, 2009, 865, 938, 939, 940, 16, 17, 18, 19, 20, 946,
                947, 948, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3 && index >= 0) {
                pl.inventory.addGold(Util.nextInt(gold[0][0], gold[0][1]));
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {

                Item it = ItemService.gI().createNewItem(temp[index]);
                if (temp[index] == 441) {
                    it.itemOptions.add(new ItemOption(95, 5));
                } else if (temp[index] == 442) {
                    it.itemOptions.add(new ItemOption(96, 5));
                } else if (temp[index] == 447) {
                    it.itemOptions.add(new ItemOption(101, 5));
                } else if (temp[index] >= 2009 && temp[index] <= 2010) {
                    it.itemOptions.add(new ItemOption(30, 0));
                } else if (temp[index] == 865) {
                    it.itemOptions.add(new ItemOption(30, 0));
                    if (Util.isTrue(1, 20)) {
                        it.itemOptions.add(new ItemOption(93, 365));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else if (temp[index] >= 938 && temp[index] <= 940) {
                    it.itemOptions.add(new ItemOption(77, 35));
                    it.itemOptions.add(new ItemOption(103, 35));
                    it.itemOptions.add(new ItemOption(50, 35));
                    if (Util.isTrue(1, 50)) {
                        it.itemOptions.add(new ItemOption(116, 0));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else if (temp[index] >= 946 && temp[index] <= 948) {
                    it.itemOptions.add(new ItemOption(77, 35));
                    it.itemOptions.add(new ItemOption(103, 35));
                    it.itemOptions.add(new ItemOption(50, 35));
                    if (Util.isTrue(1, 20)) {
                        it.itemOptions.add(new ItemOption(93, 365));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else {
                    it.itemOptions.add(new ItemOption(73, 0));
                }
                InventoryService.gI().addItemBag(pl, it, 0);
                icon[1] = it.template.iconID;

            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryService.gI().addItemBag(pl, ct, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openHopQuaCaitrang1999(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {

            Item ct = ItemService.gI().createNewItem((short) (1319 + pl.gender));
            ct.itemOptions.add(new ItemOption(50, Util.nextInt(25, 33)));
            ct.itemOptions.add(new ItemOption(77, Util.nextInt(25, 33)));
            ct.itemOptions.add(new ItemOption(103, Util.nextInt(25, 33)));
            if (Util.isTrue(97, 10)) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));
            }
            InventoryService.gI().addItemBag(pl, ct, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

       void chulun(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            short list[] = {1162, 1163, 1164, 17, 18, 1150, 1151, 1152, 1153,20};
            Item vp = ItemService.gI().createNewItem(list[Util.nextInt(list.length)]);

            switch (vp.getId()) {
                case 1162://laze
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(3, 5)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(3, 5)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(5, 15)));
                    vp.itemOptions.add(new ItemOption(214, Util.nextInt(1, 10)));
                    if (Util.isTrue(95, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                case 1163://bom
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(3, 5)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(5, 15)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(3, 5)));
                    vp.itemOptions.add(new ItemOption(215, Util.nextInt(1, 10)));
                    if (Util.isTrue(95, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                case 1164://sdcm
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(3, 5)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(3, 5)));
                    vp.itemOptions.add(new ItemOption(5, Util.nextInt(10, 30)));
                    if (Util.isTrue(95, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                default:
                    break;
            }
            player.event.addEventPoint(1);
            InventoryService.gI().addItemBag(player, vp);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 1 điểm sự kiện noel và " + vp.getName());
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void hopquabanggia(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            short list[] = {1371, 17, 18,20, 925, 926, 927, 928, 929, 930, 931};
            Item vp = ItemService.gI().createNewItem(list[Util.nextInt(list.length)]);
vp.itemOptions.add(new ItemOption(93,30));
            switch (vp.getId()) {
                case 1371: // laze
    // option cố định
                    
    vp.itemOptions.add(new ItemOption(74, 0));
    vp.itemOptions.add(new ItemOption(30, 0));

    // random 1 trong 3 option
    int rd = Util.nextInt(3); // 0,1,2
    switch (rd) {
        case 0:
            vp.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
            break;
        case 1:
            vp.itemOptions.add(new ItemOption(77, Util.nextInt(5, 20)));
            break;
        case 2:
            vp.itemOptions.add(new ItemOption(103, Util.nextInt(5, 20)));
            break;
    }

    // op phụ (nếu có)
    if (Util.isTrue(99, 100)) {
        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
    }
    break;

                default:
                    break;
            }
            InventoryService.gI().addItemBag(player, vp);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + vp.getName());
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }
    private void OpenHopPet1344(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] listVp = {1992, 1993};
            Item vpOpen = ItemService.gI().createNewItem(listVp[Util.nextInt(listVp.length)], 1);
            vpOpen.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
            vpOpen.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
            vpOpen.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
            if (Util.isTrue(90, 100)) {
                vpOpen.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            }
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, vpOpen.template.iconID);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, vpOpen, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Ít nhất có 1 ô trống trong hành trang");
        }
    }

    private void openBokeo1300(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] a = {644, 645, 646, 1274, 1324, 1325};
            Item ct = ItemService.gI().createNewItem(a[Util.nextInt(a.length)]);
            if (ct.template.type == 5) {
                ct.itemOptions.add(new ItemOption(50, Util.nextInt(33, 43)));
                ct.itemOptions.add(new ItemOption(77, Util.nextInt(33, 43)));
                ct.itemOptions.add(new ItemOption(103, Util.nextInt(33, 43)));

            } else if (ct.template.type == 11) {
                ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 15)));
                ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 15)));
            }
            if (Util.isTrue(85, 100)) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
            }
            InventoryService.gI().addItemBag(pl, ct, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openRuongBau1354(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item key = InventoryService.gI().findItemBag(pl, 1353);
            if (key != null) {
                short[] a = {17, 18, 19, 20, 1345};
                Item vpCreate = ItemService.gI().createNewItem(a[Util.nextInt(a.length)]);
                vpCreate.itemOptions.add(new ItemOption(30, 1));
                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, vpCreate.template.iconID);
                InventoryService.gI().addItemBag(pl, vpCreate, 99);
                InventoryService.gI().subQuantityItemsBag(pl, key, 1);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBags(pl);
                pl.RuongbauPoint += 1;
            } else {
                Service.getInstance().sendThongBao(pl, "Rương đã bị khóa , yêu cầu chìa khóa để mở rương!");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openGioKeo1301(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] a = {1272, 1273, 1324, 1326};
            Item ct = ItemService.gI().createNewItem(a[Util.nextInt(a.length)]);
            if (ct.template.id == 1272) {
                ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 15)));
                ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 15)));
            } else if (ct.template.id == 1273) {
                ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
            }
            if (Util.isTrue(85, 100)) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 7)));
            }
            if (ct.template.type == 11) {
                ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 15)));
                ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 15)));
                if (Util.isTrue(85, 100)) {
                    ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                }
            }
            InventoryService.gI().addItemBag(pl, ct, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openHuyDiet1384(Player pl, Item item) {
        if (item.quantity < 1) {
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) < 5) {
            Service.getInstance().sendThongBao(pl, "Bạn cần có ít nhất 5 ô trống trong hành trang");
            return;
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        int gender = pl.gender;
        short idItem;
        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                gender = 0;
            }
            idItem = ConstItem.doSKHVip[i][gender][13]; // cấp độ trang bị -1
            Item trangBi = ItemService.gI().createNewItem((short) idItem);
            RewardService.gI().initBaseOptionClothes(trangBi);
            if (trangBi != null) {
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().addItemBag(pl, trangBi, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn vừa nhận được " + trangBi.template.name);
            }
        }

    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.getInstance().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    public boolean openCSKB_auto(Player pl) {
        Item item = InventoryService.gI().findItemBag(pl, (short) 380);
        if (item == null || item.quantity < 1) {
            return false;
        }
        CombineServiceNew.gI().sendEffectOpenItem(pl, (short) 2759, (short) 2759);
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);

            if (index <= 3) {
                pl.inventory.addGold(Util.nextInt(gold[0][0], gold[0][1]));
                PlayerService.gI().sendInfoHpMpMoney(pl);
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);

            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            return true;
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
            return false;
        }
    }

    private void useItemTime(Player pl, Item item) {
        boolean updatePoint = false;
        switch (item.template.id) {
            case 1492:
                pl.itemTime.lastTimeDaiHaiTrinh = System.currentTimeMillis();
                pl.itemTime.isDaiHaiTrinh = true;
                break;
            case 880:
                pl.itemTime.lastTimeChuoi = System.currentTimeMillis();
                pl.itemTime.isChuoi = true;
                updatePoint = true;
                break;
            case 882:
                pl.itemTime.lastTimeCaRot = System.currentTimeMillis();
                pl.itemTime.isCaRot = true;
                updatePoint = true;
                break;
            case 881:
                pl.itemTime.lastTimeCaChua = System.currentTimeMillis();
                pl.itemTime.isCaChua = true;
                updatePoint = true;
                break;
            case 1479:
                pl.itemTime.lastTimeHuyHieu = System.currentTimeMillis();
                pl.itemTime.isHuyHieu = true;
                break;
            case 1182: // mâm ngũ quả
                pl.itemTime.lastTimeUseDauve = System.currentTimeMillis();
                pl.itemTime.isUseDauVe = true;
                updatePoint = true;
                break;
            case 1476:
                pl.itemTime.lastTimeUseHoangHoa = System.currentTimeMillis();
                pl.itemTime.isUseHoangHoa = true;
                break;
            // case 1356:
            // pl.itemTime.lastTimeuseThitThan = System.currentTimeMillis();
            // pl.itemTime.isUseThitThan = true;
            // updatePoint = true;
            // break;
            // case 1355:
            // pl.itemTime.lastTimeuseThitSuon = System.currentTimeMillis();
            // pl.itemTime.isUseThitSuon = true;
            // updatePoint = true;
            // break;
            case 1343:
                pl.itemTime.lastTimeUseMaydoBongtoi = System.currentTimeMillis();
                pl.itemTime.isUseMaydoBongtoi = true;
                break;
            // case 1337:
            // pl.itemTime.lastTimeBohoaHong = System.currentTimeMillis();
            // pl.itemTime.isUseBohoaHong = true;
            // break;
            case 1998:
                if (pl.getSession().actived) {
                    if (pl.itemTime.isUseMdSkh) {
                        pl.itemTime.lastTimeMdSkh += (30 * 60 * 1000);
                    } else {
                        pl.itemTime.lastTimeMdSkh = System.currentTimeMillis();
                        pl.itemTime.isUseMdSkh = true;
                    }
                } else {
                    Service.getInstance().sendThongBao(pl,
                            "Bạn phải mở thành viên thì mới có thể sử dụng vật phẩm này");
                }
                break;
            case 382: // bổ huyết
                if (pl.itemTime.isUseBoHuyet2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                updatePoint = true;
                break;
            case 383: // bổ khí
                if (pl.itemTime.isUseBoKhi2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                updatePoint = true;
                break;
            case 384: // giáp xên
                if (pl.itemTime.isUseGiapXen2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                updatePoint = true;
                break;
            case 381: // cuồng nộ
                if (pl.itemTime.isUseCuongNo2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                updatePoint = true;
                break;
            case 385: // ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case ConstItem.BO_HUYET_2: // bổ huyết 2
                if (pl.itemTime.isUseBoHuyet) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet2 = true;
                updatePoint = true;
                break;
            case ConstItem.BO_KHI_2: // bổ khí 2
                if (pl.itemTime.isUseBoKhi) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi2 = true;
                updatePoint = true;
                break;
            case ConstItem.GIAP_XEN_BO_HUNG_2: // giáp xên 2
                if (pl.itemTime.isUseGiapXen) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen2 = true;
                updatePoint = true;
                break;
            case ConstItem.CUONG_NO_2: // cuồng nộ 2
                if (pl.itemTime.isUseCuongNo) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo2 = true;
                updatePoint = true;
                break;
            case 379: // máy dò
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 663: // bánh pudding
            case 664: // xúc xíc
            case 665: // kem dâu
            case 666: // mì ly
            case 667: // sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                updatePoint = true;
                break;
            case 1195: // xí muội hoa anh đào
                pl.itemTime.lastTimeBanhChung = System.currentTimeMillis();
                pl.itemTime.isUseBanhChung = true;
                updatePoint = true;
                break;
            case 1196: // xí muội hoa mai
                pl.itemTime.lastTimeBanhTet = System.currentTimeMillis();
                pl.itemTime.isUseBanhTet = true;
                updatePoint = true;
                break;
            // x2 x3 x4
            case ConstItem.X2TNSM:
                pl.itemTime.lastTimeUseX2TNSM = System.currentTimeMillis();
                pl.itemTime.isUseX2TNSM = true;
                updatePoint = true;
                break;
            case ConstItem.X3TNSM:
                pl.itemTime.lastTimeUseX3TNSM = System.currentTimeMillis();
                pl.itemTime.isUseX3TNSM = true;
                updatePoint = true;
                break;
            case ConstItem.X4TNSM:
                pl.itemTime.lastTimeUseX4TNSM = System.currentTimeMillis();
                pl.itemTime.isUseX4TNSM = true;
                updatePoint = true;
                break;
            case ConstItem.PHAO_BONG:
            case ConstItem.SIEU_PHAO_BONG_X100:
                pl.itemTime.lastTimeUsePhaoHoa = System.currentTimeMillis();
                pl.itemTime.isUseUsePhaoHoa = true;
                //     Service.getInstance().rsDanhHieu(pl);
                updatePoint = true;
                break;
            case ConstItem.BINH_CHUA_COMMESON:
                pl.itemTime.lastTimeUseGroup_4_1 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_4_1 = true;
                updatePoint = true;
                break;
            case 1251:
                pl.itemTime.lastTimeUseGroup_3_1 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_3_1 = true;
                updatePoint = true;
                break;
            case 1250:
                pl.itemTime.lastTimeUseGroup_1_1 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_1_1 = true;
                updatePoint = true;
                break;
            case 1373: // Máy dò capsule kỳ bí cấp 2
                pl.itemTime.lastTimeUseGroup_3_2 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_3_2 = true;
                updatePoint = true;
                break;
            case 1238:
                pl.itemTime.lastTimeUseGroup_1_2 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_1_2 = true;
                updatePoint = true;
                break;
            case 465: // Bánh trung thu 1
                if (pl.itemTime.isUseBanhTrungThu_2 || pl.itemTime.isUseBanhTrungThu_3
                        || pl.itemTime.isUseBanhTrungThu_4) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sử dụng một loại bánh trung thu cùng lúc");
                    return;
                }
                pl.itemTime.lastTimeBanhTrungThu_1 = System.currentTimeMillis();
                pl.itemTime.isUseBanhTrungThu_1 = true;
                updatePoint = true;
                break;
            case 466: // Bánh trung thu 2
                if (pl.itemTime.isUseBanhTrungThu_1 || pl.itemTime.isUseBanhTrungThu_3
                        || pl.itemTime.isUseBanhTrungThu_4) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sử dụng một loại bánh trung thu cùng lúc");
                    return;
                }
                pl.itemTime.lastTimeBanhTrungThu_2 = System.currentTimeMillis();
                pl.itemTime.isUseBanhTrungThu_2 = true;
                updatePoint = true;
                break;
            case 472: // Bánh trung thu 3
                if (pl.itemTime.isUseBanhTrungThu_2 || pl.itemTime.isUseBanhTrungThu_1
                        || pl.itemTime.isUseBanhTrungThu_4) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sử dụng một loại bánh trung thu cùng lúc");
                    return;
                }
                pl.itemTime.lastTimeBanhTrungThu_3 = System.currentTimeMillis();
                pl.itemTime.isUseBanhTrungThu_3 = true;
                updatePoint = true;
                break;
            case 473: // Bánh trung thu 4
                if (pl.itemTime.isUseBanhTrungThu_2 || pl.itemTime.isUseBanhTrungThu_3
                        || pl.itemTime.isUseBanhTrungThu_1) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sử dụng một loại bánh trung thu cùng lúc");
                    return;
                }
                pl.itemTime.lastTimeBanhTrungThu_4 = System.currentTimeMillis();
                pl.itemTime.isUseBanhTrungThu_4 = true;
                updatePoint = true;
                break;
            case 1431: // Thẻ người tuyểt
                pl.itemTime.lastTimeUseGroup_2_1 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_2_1 = true;
                updatePoint = false;
                break;
            case 1432: // Tảng băng
                pl.itemTime.lastTimeUseGroup_2_2 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_2_2 = true;
                updatePoint = true;
                break;
            case 1435:// Máy dò nâng cấp
                pl.itemTime.lastTimeUseGroup_5_1 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_5_1 = true;
                break;
            case 1436:// Máy dò cường hóa
                pl.itemTime.lastTimeUseGroup_5_2 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_5_2 = true;
                break;
            case 1113:// Hô lô siêu sức mạnh
                pl.itemTime.lastTimeUseGroup_7_3 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_7_3 = true;
                break;
            case 1336: // Bánh gạo nướng
                pl.itemTime.lastTimeUseGroup_5_3 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_5_3 = true;
                updatePoint = true;
                break;
            case 1337: // Bánh đậu xanh
                pl.itemTime.lastTimeUseGroup_5_4 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_5_4 = true;
                updatePoint = true;
                break;
            case 462: // carot
                pl.itemTime.lastTimeUseGroup_5_5 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_5_5 = true;
                updatePoint = true;
                break;
            case 579: // Đuôi khỉ
                pl.itemTime.lastTimeUseGroup_5_6 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_5_6 = true;
                updatePoint = true;
                break;
            case 901:
                pl.itemTime.lastTimeKeoBanTay = System.currentTimeMillis();
                pl.itemTime.isKeoBayTay = true;
                break;
            case 1445: // Đậu thần Zeno
                pl.itemTime.lastTimeUseGroup_5_7 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_5_7 = true;
                updatePoint = true;
                break;
            case 1014: // Bình siêu sức mạnh
                pl.itemTime.lastTimeUseGroup_5_8 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_5_8 = true;
                updatePoint = true;
                break;
            case 1015: // Lon Coca
                pl.itemTime.lastTimeUseGroup_5_9 = System.currentTimeMillis();
                pl.itemTime.isUseGroup_5_9 = true;
                updatePoint = true;
                break;

        }
        if (updatePoint) {
            Service.getInstance().point(pl);
        }
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13), SummonDragon.DRAGON_SHENRON);
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON, -1,
                            "Bạn chỉ có thể gọi rồng 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        } // else if (tempId == SummonDragon.NGOC_RONG_SIEU_CAP) {
        // SummonDragon.gI().openMenuSummonShenron(pl, (byte) 1015,
        // SummonDragon.DRAGON_BLACK_SHENRON);
        // }
        else if (tempId >= SummonDragon.NGOC_RONG_BANG[0] && tempId <= SummonDragon.NGOC_RONG_BANG[6]) {
            switch (tempId) {
                case 925:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) 925, SummonDragon.DRAGON_ICE_SHENRON);
                    break;
                default:
                    Service.getInstance().sendThongBao(pl, "Bạn chỉ có thể gọi rồng băng từ ngọc 1 sao");
                    break;
            }
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 7) {
                    Service.getInstance().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                    level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil
                                    .createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.getInstance().sendThongBao(pl,
                                    "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id),
                                    level);
                            // System.out.println(curSkill.template.name + " - " + curSkill.point);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp "
                                    + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryService.gI().sendItemBags(pl);
                }
            } else {
                Service.getInstance().sendThongBao(pl, "Không thể thực hiện");

            }
        } catch (Exception e) {
            Log.error(UseItem.class, e);
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata2(Player pl) {
        if (pl.fusion.typeFusion == 120) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else if (pl.pet != null) {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion2(true);
            } else {
                pl.pet.unFusion();
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void usePorata3(Player pl) {
        if (pl.fusion.typeFusion == 120) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else if (pl.pet != null) {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion3(true);
            } else {
                pl.pet.unFusion();
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void usePorata4(Player pl) {
        if (pl.fusion.typeFusion == 120) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else if (pl.pet != null) {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion4(true);
            } else {
                pl.pet.unFusion();
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void openCapsuleUI(Player pl) {
        if (pl.isHoldNamecBall) {
            NamekBallWar.gI().dropBall(pl);
            Service.getInstance().sendFlagBag(pl);
        }
        if (pl.escortedBoss != null) {
            Service.getInstance().sendThongBao(pl, "Không thể dùng capsule khi Dắt theo lân con!");
            return;
        }
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        int zoneId = -1;
        if (index < 0 || index >= pl.mapCapsule.size()) {
            return;
        }

        Zone zoneChose = pl.mapCapsule.get(index);
        if (index != 0 || zoneChose.map.mapId == 21 || zoneChose.map.mapId == 22 || zoneChose.map.mapId == 23) {
            if (!(pl.zone != null && pl.zone instanceof ZSnakeRoad)) {
                pl.mapBeforeCapsule = pl.zone;
            } else {
                pl.mapBeforeCapsule = null;
            }
        } else {
            zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
            pl.mapBeforeCapsule = null;
        }
        ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: // skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: // skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: // skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: // skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
            }
        } catch (Exception e) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    void StarBangHoi1489(Player pl, Item item) {
        if (pl.clan != null) {
            if (pl.clan.maxMember < 20) {
                pl.clan.maxMember++;
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Chúc mừng bang đã mở thêm 1 slot !");
            } else {
                Service.getInstance().sendThongBao(pl, "Đã đạt giới hạn bang hội !");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy vào bang hội trước");
        }
    }

    void useTuiCaiTrangSSj1491(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            Item vp = ItemService.gI().createNewItem((short) (Util.getOne(1456, 1458)));
            vp.itemOptions.add(new ItemOption(50, Util.nextInt(20, 27)));
            vp.itemOptions.add(new ItemOption(77, Util.nextInt(20, 27)));
            vp.itemOptions.add(new ItemOption(103, Util.nextInt(20, 27)));
            vp.itemOptions.add(new ItemOption(101, Util.nextInt(20, 31)));

            InventoryService.gI().addItemBag(player, vp);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + vp.getName());

        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void useRuongCaitrang1493(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_CHON_CAI_TRANG, 0, "|2|Ngươi được phép chọn 1 trong 3 cải trang sau đây :",
                    "Broly\n Trái đất", "Broly\n Namek", "Broly\n Xayda", "Đóng");
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void Rongnhi38(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_CHON_PET_NHI, 0, "|2|Bạn đang mở trứng Vàng\n Bạn được phép chọn 1 trong 7 Pet sau đây\n"
                    + "Sẽ Random chỉ số cực kì ngon\n"
                    + "Tỉ lệ vĩnh viễn cao",
                    "Rồng nhí\n7 Sao", "Rồng nhí\n6 Sao", "Rồng nhí\n5 Sao", "Rồng nhí\n4 Sao", "Rồng nhí\n3 Sao", "Rồng nhí\n2 Sao", "Rồng nhí\n1 Sao", "Đóng");
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void Rongnhi1550(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_CHON_PET_NHI_THUONG, 0, "|2|Bạn đang mở trứng Nhí\n Bạn được phép chọn 1 trong 7 Pet sau đây\n"
                    + "Sẽ Random chỉ số cực kì ngon\n"
                    + "Tỉ lệ vĩnh viễn cao",
                    "Rồng nhí\n7 Sao", "Rồng nhí\n6 Sao", "Rồng nhí\n5 Sao", "Rồng nhí\n4 Sao", "Rồng nhí\n3 Sao", "Rồng nhí\n2 Sao", "Rồng nhí\n1 Sao", "Đóng");
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    public void AcceptChonCaitrang1493(Player player, int select) {
        if (select <= 2) {
            if (InventoryService.gI().existItemBag(player, 1493)) {
                Item ct = ItemService.gI().createNewItem((short) (1018 + select));
                switch (ct.getId()) {
                    case 1018:
                        ct.itemOptions.add(new ItemOption(50, 25));
                        ct.itemOptions.add(new ItemOption(77, 25));
                        ct.itemOptions.add(new ItemOption(103, 25));
                        ct.itemOptions.add(new ItemOption(5, 35));
                        break;
                    case 1019:
                        ct.itemOptions.add(new ItemOption(50, 25));
                        ct.itemOptions.add(new ItemOption(77, 25));
                        ct.itemOptions.add(new ItemOption(103, 25));
                        ct.itemOptions.add(new ItemOption(214, 25));
                        break;
                    case 1020:
                        ct.itemOptions.add(new ItemOption(50, 25));
                        ct.itemOptions.add(new ItemOption(77, 25));
                        ct.itemOptions.add(new ItemOption(103, 25));
                        ct.itemOptions.add(new ItemOption(215, 20));
                        break;
                }

                InventoryService.gI().addItemBag(player, ct);
                InventoryService.gI().subQuantityItemsBag(player, (short) 1493, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + ct.getName());
            } else {
                Service.getInstance().sendThongBao(player, "Đã xảy ra lỗi ! Vui lòng báo lại cho ADMIN !");
            }
        }
    }

    public void AcceptChonPetgnhi(Player player, int select) {
        if (select <= 6) {
            if (InventoryService.gI().existItemBag(player, 1551)) {
                Item ct = ItemService.gI().createNewItem((short) (1542 + select));
                switch (ct.getId()) {
                    case 1542:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 45)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 47)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 47)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(79, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1543:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 45)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 47)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 47)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(79, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1544:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 45)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 47)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 47)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(79, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1545:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 45)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 47)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 47)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(79, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1546:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 45)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 47)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 47)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(79, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1547:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 45)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 47)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 47)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(79, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1548:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 44)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 45)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 47)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 47)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(79, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;

                }

                InventoryService.gI().addItemBag(player, ct);
                InventoryService.gI().subQuantityItemsBag(player, (short) 1551, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + ct.getName());
            } else {
                Service.getInstance().sendThongBao(player, "Đã xảy ra lỗi ! Vui lòng báo lại cho ADMIN !");
            }
        }
    }

    public void AcceptChonPetgnhiThuongh(Player player, int select) {
        if (select <= 6) {
            if (InventoryService.gI().existItemBag(player, 1550)) {
                Item ct = ItemService.gI().createNewItem((short) (1542 + select));
                switch (ct.getId()) {
                    case 1542:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 5)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 10)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(99, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1543:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 5)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 10)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(99, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1544:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 5)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 10)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(99, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1545:
                       ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 5)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 10)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(99, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1546:
                       ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 5)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 10)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(99, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1547:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 5)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 10)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(99, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;
                    case 1548:
                        ct.itemOptions.add(new ItemOption(50, Util.nextInt(5, 15)));
                        ct.itemOptions.add(new ItemOption(77, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(103, Util.nextInt(5, 17)));
                        ct.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                        ct.itemOptions.add(new ItemOption(14, Util.nextInt(1, 5)));
                        ct.itemOptions.add(new ItemOption(241, Util.nextInt(1, 10)));
                        // Tỷ lệ 99% để thêm item option 93
                        if (Util.isTrue(99, 100)) {
                            ct.itemOptions.add(new ItemOption(93, Util.nextInt(3, 5)));
                        }
                        break;

                }

                InventoryService.gI().addItemBag(player, ct);
                InventoryService.gI().subQuantityItemsBag(player, (short) 1550, 1);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + ct.getName());
            } else {
                Service.getInstance().sendThongBao(player, "Đã xảy ra lỗi ! Vui lòng báo lại cho ADMIN !");
            }
        }
    }

    void useRuongThuong1503(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            short list[] = {16, 17, 18, 1150, 1151, 1152, 1153, 674, 1316, 1317};
            Item vp = ItemService.gI().createNewItem(list[Util.nextInt(list.length)]);

            switch (vp.getId()) {
                case 1316:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(15, 30)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(15, 30)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(15, 30)));
                    vp.itemOptions.add(new ItemOption(116, 1));
                    vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                    break;
                case 1317:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(15, 30)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(15, 30)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(15, 30)));
                    if (Util.isTrue(99, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                default:
                    break;
            }
            InventoryService.gI().addItemBag(player, vp);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + vp.getName());
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void useRuongVip1504(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            short list[] = {16, 17, 1195, 1196, 1495, 1314, 1315, 1316};
            Item vp = ItemService.gI().createNewItem(list[Util.nextInt(list.length)]);
            switch (vp.getId()) {
                case 1495:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                    if (Util.isTrue(99, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                case 1315:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(214, Util.nextInt(7, 15)));
                    if (Util.isTrue(99, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                case 1314:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(5, Util.nextInt(7, 25)));
                    if (Util.isTrue(99, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                case 1316:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(215, Util.nextInt(7, 15)));
                    if (Util.isTrue(99, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                default:
                    break;
            }
            InventoryService.gI().addItemBag(player, vp);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + vp.getName());
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void useThiepThuong1501(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            short list[] = {16, 17, 18, 1275, 1294, 674};
            Item vp = ItemService.gI().createNewItem(list[Util.nextInt(list.length)]);

            switch (vp.getId()) {
                case 1275:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(7, 30)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(7, 30)));
                    if (Util.isTrue(99, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                    }
                    break;
                case 1294:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                    if (Util.isTrue(99, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                    }
                    break;
                default:
                    break;
            }
            InventoryService.gI().addItemBag(player, vp);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + vp.getName());
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    void useThiepVIP1502(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            short list[] = {16, 17, 1341, 1195, 1196, 1259, 1494};
            Item vp = ItemService.gI().createNewItem(list[Util.nextInt(list.length)]);

            switch (vp.getId()) {
                case 1341:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(15, 32)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(15, 32)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(15, 32)));
                    if (Util.isTrue(99, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                case 1259:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
                    if (Util.isTrue(99, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                case 1494:
                    vp.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
                    vp.itemOptions.add(new ItemOption(208, 1));
                    if (Util.isTrue(99, 100)) {
                        vp.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    }
                    break;
                default:
                    break;
            }
            InventoryService.gI().addItemBag(player, vp);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + vp.getName());
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    public void useVeDaLinhThu1507(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            Item da = ItemService.gI().createNewItem((short) (Util.nextInt(1484, 1488)));
            InventoryService.gI().addItemBag(player, da);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + da.getName());
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    public void closeTab(Player pl) {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(7);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
