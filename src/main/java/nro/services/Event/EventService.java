package nro.services.Event;

import nro.models.item.ItemOptionTemplate;
import nro.models.item.ItemTemplate;
import nro.attr.Attribute;
import nro.attr.AttributeManager;
import nro.consts.ConstAttribute;
import nro.consts.ConstEvent;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.consts.ConstOption;
import nro.consts.ConstPlayer;
import nro.consts.ConstTask;
import nro.jdbc.daos.PlayerDAO;
import nro.models.clan.ClanMember;
import nro.models.consignment.ConsignmentItem;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.map.phoban.DoanhTrai;
import nro.models.npc.Npc;
import nro.models.npc.NpcFactory;
import nro.models.player.Inventory;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.shop.ItemShop;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.server.SettingGame;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineServiceNew;
import nro.services.func.Input;
import nro.services.func.ShopService;
import nro.services.func.SummonDragon;
import nro.services.func.lr.LuckyRoundGold;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventService {

    private static EventService i;

    public static EventService gI() {
        if (i == null) {
            i = new EventService();
        }
        return i;
    }

    public static String getMenuSuKien(int id) {
        switch (id) {
            case ConstEvent.KHONG_CO_SU_KIEN:
                return "Sự kiện\nđang\ncập nhật";
            case ConstEvent.SU_KIEN_HALLOWEEN:
                return "Sự Kiện\nHaloween";
            case ConstEvent.SU_KIEN_20_11:
                return "Sự Kiện\n 20/11";
            case ConstEvent.SU_KIEN_NOEL:
                return "Sự Kiện\n Giáng Sinh";
            case ConstEvent.SU_KIEN_TET:
                return "Sự Kiện\n Tết Nguyên\nĐán 2024";
            case ConstEvent.SU_KIEN_8_3:
                return "Sự Kiện\n 8/3";
            case ConstEvent.SU_KIEN_HOA_QUA:
                return "Hộp quà\nBí ẩn";
            case ConstEvent.SU_KIEN_RAI_TI:
                return "Đổi\nCapsule";
            case ConstEvent.SU_KIEN_20_11_2023:
                return "Tặng bó\nHoa hồng";
            case ConstEvent.SU_KIEN_TET_2024:
                return "Sự kiện\nTết 2024";
            case ConstEvent.SU_KIEN_DAY_BIEN:
                return "Sự kiện\nKhám Phá\nĐại Dương";
            case 13:
                return "Sự kiện\n8/3";
            case ConstEvent.SU_KIEN_HE_2024:
                return "Sự kiện\nHè 2024";
            case ConstEvent.SU_KIEN_HALLOWEEN_2024:
                return "Sự kiện\nHalloween\n2024";
        }
        return "Sự kiện\nđang\ncập nhật!";
    }

    public void openMenuSuKien(Player player, Npc npc, int tempId, int select) {
        switch (Manager.EVENT_SEVER) {
            case 0:
                break;
            case 1:// hlw
                switch (select) {
                    case 0:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item keo = InventoryService.gI().finditemnguyenlieuKeo(player);
                            Item banh = InventoryService.gI().finditemnguyenlieuBanh(player);
                            Item bingo = InventoryService.gI().finditemnguyenlieuBingo(player);

                            if (keo != null && banh != null && bingo != null) {
                                Item GioBingo = ItemService.gI().createNewItem((short) 2016, 1);

                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, keo, 10);
                                InventoryService.gI().subQuantityItemsBag(player, banh, 10);
                                InventoryService.gI().subQuantityItemsBag(player, bingo, 10);

                                GioBingo.itemOptions.add(new ItemOption(74, 0));
                                InventoryService.gI().addItemBag(player, GioBingo, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x10 Nguyên Liệu Kẹo, Bánh Quy, Bí Ngô để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 1:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item ve = InventoryService.gI().finditemnguyenlieuVe(player);
                            Item giokeo = InventoryService.gI().finditemnguyenlieuGiokeo(player);

                            if (ve != null && giokeo != null) {
                                Item Hopmaquy = ItemService.gI().createNewItem((short) 2017, 1);
                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, ve, 3);
                                InventoryService.gI().subQuantityItemsBag(player, giokeo, 3);

                                Hopmaquy.itemOptions.add(new ItemOption(74, 0));
                                InventoryService.gI().addItemBag(player, Hopmaquy, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 2:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item ve = InventoryService.gI().finditemnguyenlieuVe(player);
                            Item giokeo = InventoryService.gI().finditemnguyenlieuGiokeo(player);
                            Item hopmaquy = InventoryService.gI().finditemnguyenlieuHopmaquy(player);

                            if (ve != null && giokeo != null && hopmaquy != null) {
                                Item HopQuaHLW = ItemService.gI().createNewItem((short) 2012, 1);
                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, ve, 3);
                                InventoryService.gI().subQuantityItemsBag(player, giokeo, 3);
                                InventoryService.gI().subQuantityItemsBag(player, hopmaquy, 3);

                                HopQuaHLW.itemOptions.add(new ItemOption(74, 0));
                                HopQuaHLW.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, HopQuaHLW, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player,
                                        "Đổi quà hộp quà sự kiện Halloween thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x3 Hộp Ma Quỷ, x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                }
                break;
            case 2:// 20/11
                switch (select) {
                    case 3:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            int evPoint = player.event.getEventPoint();
                            if (evPoint >= 999) {
                                Item HopQua = ItemService.gI().createNewItem((short) 2021, 1);
                                player.event.setEventPoint(evPoint - 999);

                                HopQua.itemOptions.add(new ItemOption(74, 0));
                                HopQua.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, HopQua, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được Hộp Quà Teacher Day");
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần 999 điểm tích lũy để đổi");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    // case 4:
                    // ShopService.gI().openShopSpecial(player, npc, ConstNpc.SHOP_HONG_NGOC, 0,
                    // -1);
                    // break;
                    default:
                        int n = 0;
                        switch (select) {
                            case 0:
                                n = 1;
                                break;
                            case 1:
                                n = 10;
                                break;
                            case 2:
                                n = 99;
                                break;
                        }

                        if (n > 0) {
                            Item bonghoa = InventoryService.gI().finditemBongHoa(player, n);
                            if (bonghoa != null) {
                                int evPoint = player.event.getEventPoint();
                                player.event.setEventPoint(evPoint + n);

                                InventoryService.gI().subQuantityItemsBag(player, bonghoa, n);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
                                int pre;
                                int next;
                                String text = null;
                                AttributeManager am = ServerManager.gI().getAttributeManager();
                                switch (tempId) {
                                    case ConstNpc.THAN_MEO_KARIN:
                                        pre = Manager.EVENT_COUNT_THAN_MEO / 999;
                                        Manager.EVENT_COUNT_THAN_MEO += n;
                                        next = Manager.EVENT_COUNT_THAN_MEO / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.TNSM, 3600);
                                            text = "Toàn bộ máy chủ tăng được 20% TNSM cho đệ tử khi đánh quái trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.QUY_LAO_KAME:
                                        pre = Manager.EVENT_COUNT_QUY_LAO_KAME / 999;
                                        Manager.EVENT_COUNT_QUY_LAO_KAME += n;
                                        next = Manager.EVENT_COUNT_QUY_LAO_KAME / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.VANG, 3600);
                                            text = "Toàn bộ máy chủ được tăng 100% vàng từ quái trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.THUONG_DE:
                                        pre = Manager.EVENT_COUNT_THUONG_DE / 999;
                                        Manager.EVENT_COUNT_THUONG_DE += n;
                                        next = Manager.EVENT_COUNT_THUONG_DE / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.KI, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% KI trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.THAN_VU_TRU:
                                        pre = Manager.EVENT_COUNT_THAN_VU_TRU / 999;
                                        Manager.EVENT_COUNT_THAN_VU_TRU += n;
                                        next = Manager.EVENT_COUNT_THAN_VU_TRU / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.HP, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% HP trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.BILL:
                                        pre = Manager.EVENT_COUNT_THAN_HUY_DIET / 999;
                                        Manager.EVENT_COUNT_THAN_HUY_DIET += n;
                                        next = Manager.EVENT_COUNT_THAN_HUY_DIET / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.SUC_DANH, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% Sức đánh trong 60 phút.";
                                        }
                                        break;
                                }
                                if (text != null) {
                                    Service.getInstance().sendThongBaoAllPlayer(text);
                                }

                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
                        }
                }
                break;
           case 3:
                
                switch (select) {
                    case 3:
                        
                        break;
                
                    default:
                        int n = 0;
                        switch (select) {
                            case 0:
                                n = 1;
                                break;
                            case 1:
                                n = 10;
                                break;
                            case 2:
                                n = 99;
                                break;
                        }

                        if (n > 0) {
                            Item bonghoa = InventoryService.gI().finditemVo(player, n);
                            if (bonghoa != null) {
                                // int evPoint = player.event.getEventPoint();
                                // player.event.setEventPoint(evPoint + n);

                                InventoryService.gI().subQuantityItemsBag(player, bonghoa, n);
                                // Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
                                int pre;
                                int next;
                                String text = null;
                                AttributeManager am = ServerManager.gI().getAttributeManager();
                                switch (tempId) {
                                    
                                    case ConstNpc.QUY_LAO_KAME:
                                        pre = Manager.EVENT_COUNT_QUY_LAO_KAME / 999;
                                        Manager.EVENT_COUNT_QUY_LAO_KAME += n;
                                        next = Manager.EVENT_COUNT_QUY_LAO_KAME / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.SUC_DANH, 3600);
                                            text = "Toàn bộ máy chủ được tăng 10% SD trong 60 phút.";
                                        }
                                        break;

                                    
                                }
                                if (text != null) {
                                    Service.getInstance().sendThongBaoAllPlayer(text);
                                }

                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Cần ít nhất " + n + " Vớ để có thể tặng");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " Vớ để có thể tặng");
                        }
                }
                break;
            case 4: // sự kiện tết
                switch (select) {
                    case 0:
                        if (!player.event.isReceivedLuckyMoney()) {
                            Calendar cal = Calendar.getInstance();
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            if (day >= 10 && day <= 22) {
                                Item goldBar = ItemService.gI().createNewItem((short) ConstItem.THIEP_CHUC_TET, 1);
                                goldBar.itemOptions.add(new ItemOption(74, 0));
                                goldBar.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                                InventoryService.gI().addItemBag(player, goldBar, 99999);
                                InventoryService.gI().sendItemBags(player);
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                player.event.setReceivedLuckyMoney(true);
                                Service.getInstance().sendThongBao(player,
                                        "Nhận lì xì thành công,chúc bạn năm mới dui dẻ");
                            } else if (day > 24) {
                                Service.getInstance().sendThongBao(player, "Hết tết rồi còn đòi lì xì");
                            } else {
                                Service.getInstance().sendThongBao(player, "Đã tết đâu mà đòi lì xì");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn đã nhận lì xì rồi");
                        }
                        break;
                    // case 1:
                    // ShopService.gI().openShopNormal(player, npc, ConstNpc.SHOP_SU_KIEN_TET, 1,
                    // -1);
                    // break;
                    case 1:
                        Input.gI().createFormDoiMamNguQua(player);
                        break;
                    case 2:
                        Service.getInstance().sendThongBaoOK(player, "Tiêu diệt Karin Kid Lân để nhận trái cây\n"
                                + "Thu thập đủ 5 loại trái cây: Cầu sung dừa đủ xoài mang đến đây\n");
                        break;
                }
                break;
            case ConstEvent.SU_KIEN_8_3:
                switch (select) {
                    case 3:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            int evPoint = player.event.getEventPoint();
                            if (evPoint >= 999) {
                                Item capsule = ItemService.gI().createNewItem((short) 2052, 1);
                                player.event.setEventPoint(evPoint - 999);

                                capsule.itemOptions.add(new ItemOption(74, 0));
                                capsule.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, capsule, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được Capsule Hồng");
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần 999 điểm tích lũy để đổi");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    default:
                        int n = 0;
                        switch (select) {
                            case 0:
                                n = 1;
                                break;
                            case 1:
                                n = 10;
                                break;
                            case 2:
                                n = 99;
                                break;
                        }

                        if (n > 0) {
                            Item bonghoa = InventoryService.gI().finditemBongHoa(player, n);
                            if (bonghoa != null) {
                                int evPoint = player.event.getEventPoint();
                                player.event.setEventPoint(evPoint + n);
                                InventoryService.gI().subQuantityItemsBag(player, bonghoa, n);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
                        }
                }
                break;
            case ConstEvent.SU_KIEN_20_11_2023:
                switch (select) {
                    case 0:
                        Item boHoaHong = InventoryService.gI().findItemBagByTemp(player, 1340);
                        switch (select) {
                            case 0: {
                                if (boHoaHong != null) {
                                    if (InventoryService.gI().getCountEmptyBag(player) > 1) {
                                        short[] listVpRandom = {17, 20, 1341, 1308, 1309, 1311};
                                        Item itemRandom = null;

                                        if (Util.isTrue(10, 100)) {
                                            itemRandom = ItemService.gI().createNewItem(listVpRandom[2]);
                                        } else if (Util.isTrue(25, 100)) {
                                            itemRandom = ItemService.gI().createNewItem(listVpRandom[0]);
                                        } else {
                                            itemRandom = ItemService.gI().createNewItem(listVpRandom[1]);
                                        }
                                        if (itemRandom.template.id == 1341) {
                                            itemRandom.itemOptions.add(new ItemOption(50, Util.nextInt(30, 40)));
                                            itemRandom.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                            itemRandom.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                            if (Util.isTrue(75, 100)) {
                                                itemRandom.itemOptions.add(new ItemOption(93, 3));
                                            } else if (Util.isTrue(75, 100)) {
                                                itemRandom.itemOptions.add(new ItemOption(93, 7));
                                            }
                                        }
                                        InventoryService.gI().subQuantityItemsBag(player, boHoaHong, 1);
                                        InventoryService.gI().addItemBag(player, itemRandom,
                                                itemRandom.template.type == 5 ? 1 : 99);
                                        InventoryService.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player,
                                                "Bạn vừa tặng thành công bó hoa và nhận được "
                                                + itemRandom.template.name + " từ Quy lão");
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Hãy nhớ chừa ô trống để nhận quà từ Quy Lão nhé :33!");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Ngươi phải tỉa Bó hoa hồng trước chứ!");
                                }
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }
                break;
            case ConstEvent.SU_KIEN_HOA_QUA:
                Item du = InventoryService.gI().findItemBagByTemp(player, 1179);
                Item xoai = InventoryService.gI().findItemBagByTemp(player, 1180);
                Item na = InventoryService.gI().findItemBagByTemp(player, 1177);
                Item mamhqua = InventoryService.gI().findItemBagByTemp(player, 1182);
                switch (select) {
                    case 0:
                        if (du != null && du.quantity >= 10 && xoai != null && xoai.quantity >= 10 && na != null
                                && na.quantity >= 10 && mamhqua != null && mamhqua.quantity >= 10) {
                            InventoryService.gI().subQuantityItemsBag(player, du, 10);
                            InventoryService.gI().subQuantityItemsBag(player, xoai, 10);
                            InventoryService.gI().subQuantityItemsBag(player, na, 10);
                            InventoryService.gI().subQuantityItemsBag(player, mamhqua, 10);
                            Item hqua = ItemService.gI().createNewItem((short) 1184);
                            InventoryService.gI().addItemBag(player, hqua, 999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            Service.getInstance().sendThongBao(player,
                                    "Bạn vừa nhận được " + hqua.template.name + " từ ông Quy Lão");
                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Hãy mang đến đủ cho Quy Lão X10 đu đủ, x10 xoài, x10 mãng cầu và x10 mâm hoa quả mới có thể đổi hộp quà bí ẩn");
                            break;
                        }
                        break;
                    case 1:
                        if (du != null && du.quantity >= 100 && xoai != null && xoai.quantity >= 100 && na != null
                                && na.quantity >= 100 && mamhqua != null && mamhqua.quantity >= 100) {
                            InventoryService.gI().subQuantityItemsBag(player, du, 100);
                            InventoryService.gI().subQuantityItemsBag(player, xoai, 100);
                            InventoryService.gI().subQuantityItemsBag(player, na, 100);
                            InventoryService.gI().subQuantityItemsBag(player, mamhqua, 100);
                            Item hqua = ItemService.gI().createNewItem((short) 1184, 10);
                            InventoryService.gI().addItemBag(player, hqua, 999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            Service.getInstance().sendThongBao(player,
                                    "Bạn vừa nhận được " + hqua.template.name + " từ ông Quy Lão");
                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Hãy mang đến đủ cho Quy Lão X100 đu đủ, x100 xoài, x100 mãng cầu và x100 mâm hoa quả mới có thể đổi hộp quà bí ẩn");
                            break;
                        }
                        break;
                    case 2:
                        if (du != null && du.quantity >= 1000 && xoai != null && xoai.quantity >= 1000 && na != null
                                && na.quantity >= 1000 && mamhqua != null && mamhqua.quantity >= 1000) {
                            InventoryService.gI().subQuantityItemsBag(player, du, 1000);
                            InventoryService.gI().subQuantityItemsBag(player, xoai, 1000);
                            InventoryService.gI().subQuantityItemsBag(player, na, 1000);
                            InventoryService.gI().subQuantityItemsBag(player, mamhqua, 1000);
                            Item hqua = ItemService.gI().createNewItem((short) 1184, 100);
                            InventoryService.gI().addItemBag(player, hqua, 999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            Service.getInstance().sendThongBao(player,
                                    "Bạn vừa nhận được  " + hqua.template.name + " từ ông Quy Lão");
                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Hãy mang đến đủ cho Quy Lão X1000 đu đủ, x100 xoài, x1000 mãng cầu và x1000 mâm hoa quả mới có thể đổi hộp quà bí ẩn");
                            break;
                        }
                        break;
                }
                break;
            case ConstEvent.SU_KIEN_TET_2024:
                switch (select) {
                    case 0:
                        Input.gI().createFormDoiMamNguQua(player);
                        break;
                }
                break;
            case ConstEvent.SU_KIEN_DAY_BIEN: {
                switch (select) {
                    case 0:
                        doi_qua_su_kien_day_bien(player, 1);
                        break;
                    case 1:
                        doi_qua_su_kien_day_bien(player, 5);
                        break;
                    case 2:
                        doi_qua_su_kien_day_bien(player, 10);
                        break;
                    case 3:
                        Service.getInstance().showtopEvent(player);
                        break;
                }

            }

            break;
            case 13:
                switch (select) {
                    case 0:
                        Item hoa_do = InventoryService.gI().findItemBagByTemp(player, 610);
                        Item hoa_xanh = InventoryService.gI().findItemBagByTemp(player, 1098);
                        if (hoa_do != null && hoa_do.quantity >= 99 && hoa_xanh != null && hoa_xanh.quantity >= 1) {
                            InventoryService.gI().subQuantityItemsBag(player, hoa_do, 99);
                            InventoryService.gI().subQuantityItemsBag(player, hoa_xanh, 1);
                            short item_list[] = {1188, 1202, 16, 17, 18, 19, 20};
                            Item item = ItemService.gI()
                                    .createNewItem((short) item_list[Util.nextInt(item_list.length - 1)], 1);
                            if (item.template.id == 1188 || item.template.id == 1202) {
                                item.itemOptions.add(new ItemOption(ConstOption.TAN_CONG_PT, Util.nextInt(5, 10)));
                                item.itemOptions.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(5, 10)));
                                item.itemOptions.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(5, 10)));
                                if (Util.isTrue(95, 100)) {
                                    item.itemOptions.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(3, 10)));
                                }
                            }

                            InventoryService.gI().addItemBag(player, item, 999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendMoney(player);
                            Service.getInstance().sendThongBao(player,
                                    "Bạn vừa nhận được  " + item.template.name + " từ Quy Lão");

                        } else {
                            Service.getInstance().sendThongBao(player,
                                    "Hãy mang đến đủ cho Quy Lão x99 hoa đỏ và x1 hoa xanh mới có thể đổi hộp quà bí ẩn");
                            break;
                        }
                }
                break;
            case ConstEvent.SU_KIEN_HE_2024:
                switch (select) {
                    case 0:
                        doi_qua_su_kien_he_2024(player, 1);
                        break;
                    case 1:
                        doi_qua_su_kien_he_2024(player, 5);
                        break;
                    case 2:
                        doi_qua_su_kien_he_2024(player, 20);
                        break;
                }
                break;
            case ConstEvent.SU_KIEN_TRUNG_THU_2024:
                switch (select) {
                    case 0:
                        EventTrungThu2024_1(player, 1);
                        break;
                    case 1:
                        EventTrungThu2024_2(player, 1);
                        break;
                    case 2:
                        EventTrungThu2024_Lam_Banh_VIP(player, false, 1);
                        break;
                    case 3:
                        EventTrungThu2024_Lam_Banh_VIP(player, true, 1);
                        break;
                    case 4:
                        Service.getInstance().showtopEvent(player);
                        break;
                }
                break;
        }
    }

    public void List_Event(Player player, Npc npc) {
        switch (Manager.EVENT_SEVER) {
            case 1:
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        "Sự kiện Halloween chính thức tại Ngọc Rồng "
                        + Manager.SERVER_NAME + "\n"
                        + "Chuẩn bị x10 nguyên liệu Kẹo, Bánh Quy, Bí ngô để đổi Giỏ Kẹo cho ta nhé\n"
                        + "Nguyên Liệu thu thập bằng cách đánh quái tại các hành tinh được chỉ định\n"
                        + "Tích lũy 3 Giỏ Kẹo +  3 Vé mang qua đây ta sẽ cho con 1 Hộp Ma Quỷ\n"
                        + "Tích lũy 3 Giỏ Kẹo, 3 Hộp Ma Quỷ + 3 Vé \nmang qua đây ta sẽ cho con 1 hộp quà thú vị.",
                        "Đổi\nGiỏ Kẹo", "Đổi Hộp\nMa Quỷ", "Đổi Hộp\nQuà Halloween",
                        "Từ chối");
                break;
            case 2:
                Attribute at = ServerManager.gI().getAttributeManager()
                        .find(ConstAttribute.VANG);
                String text = "Sự kiện 20/11 chính thức tại Ngọc Rồng "
                        + Manager.SERVER_NAME + "\n "
                        + "Số điểm hiện tại của bạn là : "
                        + player.event.getEventPoint()
                        + "\nTổng số hoa đã tặng trên toàn máy chủ "
                        + Manager.EVENT_COUNT_QUY_LAO_KAME % 999 + "/999";
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        at != null && !at.isExpired() ? text
                        + "\nToàn bộ máy chủ được nhân đôi số vàng rơi ra từ quái,thời gian còn lại "
                        + at.getTime() / 60 + " phút."
                        : text + "\nKhi tặng đủ 999 bông hoa toàn bộ máy chủ được nhân đôi số vàng rơi ra từ quái trong 60 phút",
                        "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông",
                        "Đổi\nHộp quà");
                break;
            case 3:
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        "Sự kiên giáng sinh 2022 " + Manager.SERVER_NAME
                        + "\nKhi đội mũ len bất kì đánh quái sẽ có cơ hội nhận được kẹo giáng sinh"
                        + "\nĐem 99 kẹo giáng sinh tới đây để đổi 1 Vớ,tất giáng sinh\nChúc bạn một mùa giáng sinh vui vẻ",
                        "Đổi\nTất giáng sinh");
                break;
            case 4: // sự kiện tết
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        "Sự kiên Tết 2024 của máy chủ " + SettingGame.NAME_GAME
                        + "\nBạn đang có: " + player.event.getEventPoint()
                        + " điểm sự kiện\n"
                        + "\nTổng lượt bắn pháo hoa toàn máy chủ: "
                        + Manager.EVENT_POINT_TET_2024
                        + " điểm sự kiện\nChúc bạn năm mới vui vẻ",
                        "Nhận Lìxì", "Làm mâm\nngũ quả", "Thông tin\nSự kiện");
                break;
            case 5:
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        "Sự kiện 8/3 chính thức tại Ngọc Rồng "
                        + Manager.SERVER_NAME + "\nBạn đang có: "
                        + player.event.getEventPoint()
                        + " điểm sự kiện\nChúc bạn chơi game dui dẻ",
                        "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông",
                        "Đổi Capsule");
                break;
            case 6:
                npc.createOtherMenu(player, ConstNpc.SU_KIEN_HOA_QUA,
                        "Dạo này ta hơi thèm hoa quả ngươi có thể mang: \n+ X10 Quả đu đủ\n+ X10 Quả xoài\n+ X10 Quả Mãng Cầu\nVà X10 Mâm hoa quả để đổi lấy hộp quà bí ẩn từ ta ",
                        "Đổi X1 hộp quà", "Đổi X10 hộp quà", "Đổi X100 hộp quà");
                break;
            case 7:
                npc.createOtherMenu(player, ConstNpc.SU_KIEN_RAITI,
                        "Ngươi có chắc là muốn lấy Capsule từ ta?\nNgười cần mang tới cho ta X10 Capsule bạc hoặc X10 Capsule vàng cùng với X10 Hộp Capsule\n Ta sẽ đổi cho ngươi rương báu tương xứng!",
                        "Đổi\nCapsule Bạc", "Đổi\nCapsule Vàng", "Dạ hoy!~");
                break;
            case 8:
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        "|2|Nhân ngày Nhà giáo việt nam \nTa đang rất mong chờ ngày thành tài của các con"
                        + "\n|4|(Năm nay một loài hoa hồng rất đẹp đã nở , Quy Lão rất thích nó"
                        + "\nhãy mang đến bó hoa hồng ấy và có thể\nQuy lão sẽ ban cho ngươi một điều ước 'nho nhỏ' đấy !)",
                        "Tặng\n Bó hoa ", "Chúc thầy\n 20/11\n vui vẻ");
                break;
            case 10:
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        "|2|Tết đến rồi, hãy mang đến cho ta 5 loại trái cây để làm Mâm ngũ quả nhé",
                        "Làm\nmâm\nngũ quả", "đóng");
                break;
            case 12:
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        "Sự kiện " + SettingGame.NAME_GAME
                        + "\nHãy vào đảo kho báu thu thập cho ta các vật phẩm sự kiện, ta sẽ kho ngươi rương kho báu hải tặc"
                        + "\nĐổi x99 ngọc trai + x99 con cá + x99 con sứa + x5 cá mập + x5 bạch tuộc để nhận quà"
                        + "\n(hạ quái nhận được ngọc trai, con cá, con sứa; hạ boss nhận được cá mập, bạch tuộc) ",
                        "Đổi rương\nx1", "Đổi rương\nx5", "Đổi rương\nx10", "Top điểm\nĐại Dương", "đóng");
                break;
            case 13:
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        "Sự kiện 8/3 đang diễn ra\n Hãy tìm giúp Quy lão x99 hoa hồng quà x1 hoa xanh, Quy lão sẽ cho ngươi lại món quà nhỏ đấy",
                        "Giao hoa", "đóng");
                break;
            case ConstEvent.SU_KIEN_HE_2024:
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        "Sự kiện hè đang diễn ra\n Hãy đế đảo côn trùng để tìm 5 con Bọ Kiến Vương Hai Sừng và 5 con Bọ Hung Tê Giác Vàng cho ta nhé!",
                        "Giao bọ\n1 lần", "Giao bọ\n5 lần", "Giao bọ\n20 lần", "đóng");
                break;
            case ConstEvent.SU_KIEN_TRUNG_THU_2024:
                npc.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                        "Các cư dân thu thập đủ nguyên liệu để làm bánh trung thu với công thức sau:\n"
                        + "+ Bánh gạo nướng: 100 Gạo tẻ + 10 Đậu xanh + 01 Trứng ( Ăn vào 10% HP,KI )\n"
                        + "+ Bánh đậu xanh: 100 Nếp dẻo + 10 Đậu xanh + 01 Lạp xưởng ( Ăn vào 10% SD )\n"
                        + "- Công thức đổi bánh trung thu thượng hạng:"
                        + "+ Đổi bánh thượng hạng 01: 01 Bánh gạo nướng + 01 bánh đậu xanh\n"
                        + "+ Đổi bánh thượng hạng 02: 02 Bánh gạo nướng + 02 bánh đậu xanh\n",
                        "Làm bánh\ngạo nướng",
                        "Làm bánh\nđậu xanh", "Làm bánh\nthượng hạng\n loại 1",
                        "Làm bánh\nthượng hạng\n loại 2", "Top\n làm bánh",
                        "đóng");
                break;
            case ConstEvent.SU_KIEN_HALLOWEEN_2024:
                npc.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                        "Con hãy đến gặp Npc sự kiện ở các làng để tham gia hoạt động nhé",
                        "đóng");
                break;
        }
    }

    public void doi_qua_su_kien_day_bien(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 500_000_000L * quantity) {
                    int ID_ITEM_NHAN = 1400; // RƯơng kho báu

                    int id_cau = 1395; // item 1
                    int id_dua = 1396; // item 2
                    int id_du = 1397; // item 3
                    int id_xoai = 1398; // item 4 bạch tuộc
                    int id_sung = 1399; // item 5 cá mập
                    int sl_doi_1 = 99;
                    int sl_doi_2 = 5;
                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                    Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_dua);
                    Item item_3 = InventoryService.gI().findItemBagByTemp(player, id_du);
                    Item item_4 = InventoryService.gI().findItemBagByTemp(player, id_xoai);
                    Item item_5 = InventoryService.gI().findItemBagByTemp(player, id_sung);

                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " ngọc trai để đổi");
                        return;
                    }
                    if (item_2 == null || item_2.quantity < sl_doi_1 * quantity) {
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " con cá để đổi");
                        return;
                    }
                    if (item_3 == null || item_3.quantity < sl_doi_1 * quantity) {
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " con sứa để đổi");
                        return;
                    }
                    if (item_4 == null || item_4.quantity < sl_doi_2 * quantity) {
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_2 * quantity) + " bạch tuộc để đổi");
                        return;
                    }
                    if (item_5 == null || item_5.quantity < sl_doi_2 * quantity) {
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_2 * quantity) + " cá mập để đổi");
                        return;
                    }

                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_1 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_3, sl_doi_1 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_4, sl_doi_2 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_5, sl_doi_2 * quantity);

                    player.inventory.gold -= 500_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi mâm ngũ quả cần 500 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void doi_qua_su_kien_he_2024(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {

                int ID_ITEM_NHAN = 1277; // RƯơng kho báu

                int id_cau = 1245; // item 1
                int id_dua = 1246; // item 2

                int sl_doi_1 = 5;

                Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_dua);

                if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                    Service.getInstance().sendThongBao(player,
                            "Cần x" + (sl_doi_1 * quantity) + " Bọ Kiến Vương Hai Sừng để đổi");
                    return;
                }
                if (item_2 == null || item_2.quantity < sl_doi_1 * quantity) {
                    Service.getInstance().sendThongBao(player,
                            "Cần x" + (sl_doi_1 * quantity) + " Bọ Hung Tê Giác Vàng để đổi");
                    return;
                }

                InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_1 * quantity);

                Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                InventoryService.gI().addItemBag(player, itemNhan, 999);
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void doi_bo_hung_Random(Player player, int quantity) {
        // Đổi nhận ngẫu nhiên 1 trong 2 con bọ
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {

            int id_cau = 1247; // item 1
            int id_dua = 1248; // item 2
            int id_du = 1249; // item 3
            int id_xoai = 1240; // item 4

            int sl_doi_1 = 20;
            int sl_doi_2 = 2;
            Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
            Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_dua);
            Item item_3 = InventoryService.gI().findItemBagByTemp(player, id_du);
            Item item_4 = InventoryService.gI().findItemBagByTemp(player, id_xoai);

            if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                Service.getInstance().sendThongBao(player,
                        "Cần x" + (sl_doi_1 * quantity) + " Bọ Kẹp Kìm để đổi");
                return;
            }
            if (item_2 == null || item_2.quantity < sl_doi_1 * quantity) {
                Service.getInstance().sendThongBao(player,
                        "Cần x" + (sl_doi_1 * quantity) + " Bọ Cánh Cứng để đổi");
                return;
            }
            if (item_3 == null || item_3.quantity < sl_doi_1 * quantity) {
                Service.getInstance().sendThongBao(player,
                        "Cần x" + (sl_doi_1 * quantity) + " Ngài Đêm để đổi");
                return;
            }
            if (item_4 == null || item_4.quantity < sl_doi_2 * quantity) {
                Service.getInstance().sendThongBao(player,
                        "Cần x" + (sl_doi_2 * quantity) + " Que đốt để đổi");
                return;
            }

            InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
            InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_1 * quantity);
            InventoryService.gI().subQuantityItemsBag(player, item_3, sl_doi_1 * quantity);
            InventoryService.gI().subQuantityItemsBag(player, item_4, sl_doi_2 * quantity);
            for (int i = 0; i < quantity; i++) {
                int ID_ITEM_NHAN = Util.nextInt(1245, 1246); // RƯơng kho báu
                Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                InventoryService.gI().addItemBag(player, itemNhan, 999);
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);
            }

        } else {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang!");
            return;
        }

    }

    public void EventTrungThu2024_1(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 50_000_000L * quantity) {
                    int ID_ITEM_NHAN = 1336; // NHẬN ĐƯỢC

                    int id_cau = 1328; // item 1
                    int id_dua = 1332; // item 2
                    int id_xoai = 1332; // item 3

                    int sl_doi_1 = 15;
                    int sl_doi_2 = 10;
                    int sl_doi_3 = 1;
                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                    Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_dua);
                    Item item_3 = InventoryService.gI().findItemBagByTemp(player, id_xoai);

                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    if (item_2 == null || item_2.quantity < sl_doi_2 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_dua, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_2 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    if (item_3 == null || item_3.quantity < sl_doi_3 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_xoai, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_3 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_2 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_3, sl_doi_3 * quantity);

                    player.inventory.gold -= 50_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 50 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void EventTrungThu2024_2(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 500_000_000L * quantity) {
                    int ID_ITEM_NHAN = 1337; // NHẬN ĐƯỢC

                    int id_cau = 1329; // item 1
                    int id_dua = 1330; // item 2
                    int id_xoai = 1327; // item 3

                    int sl_doi_1 = 30;
                    int sl_doi_2 = 20;
                    int sl_doi_3 = 2;
                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                    Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_dua);
                    Item item_3 = InventoryService.gI().findItemBagByTemp(player, id_xoai);

                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    if (item_2 == null || item_2.quantity < sl_doi_2 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_dua, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_2 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    if (item_3 == null || item_3.quantity < sl_doi_3 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_xoai, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_3 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_2 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_3, sl_doi_3 * quantity);

                    player.inventory.gold -= 500_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 500 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void EventTrungThu2024_3(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 100_000_000L * quantity) {
                    int ID_ITEM_NHAN = 1627; // NHẬN ĐƯỢC

                    int id_cau = 1336; // item 1
                    int id_dua = 1337; // item 2

                    int sl_doi_1 = 1;
                    int sl_doi_2 = 1;
                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                    Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_dua);

                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    if (item_2 == null || item_2.quantity < sl_doi_2 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_dua, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_2 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_2 * quantity);

                    player.inventory.gold -= 100_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 500 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void EventTrungThu2024_4(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 200_000_000L * quantity) {
                    int ID_ITEM_NHAN = 1628; // NHẬN ĐƯỢC

                    int id_cau = 1336; // item 1
                    int id_dua = 1337; // item 2

                    int sl_doi_1 = 2;
                    int sl_doi_2 = 2;

                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                    Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_dua);

                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    if (item_2 == null || item_2.quantity < sl_doi_2 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_dua, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_2 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_2 * quantity);

                    player.inventory.gold -= 200_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 500 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void EventTrungThu2024_5(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 100_000_000L * quantity) {
                    int ID_ITEM_NHAN = 1597; // NHẬN ĐƯỢC
                    int id_cau = 462; // item 1
                    int sl_doi_1 = 199;
                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                    player.inventory.gold -= 100_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 15)));
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.HP, Util.nextInt(10, 15)));
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KI, Util.nextInt(10, 15)));
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);
                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 100 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void EventTrungThu2024_6(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 200_000_000L * quantity) {
                    int ID_ITEM_NHAN = 1626; // NHẬN ĐƯỢC

                    int id_cau = 462; // item 1
                    int sl_doi_1 = 99;
                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);

                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);

                    player.inventory.gold -= 200_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(10, 13)));
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.HP, Util.nextInt(10, 13)));
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KI, Util.nextInt(10, 13)));
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 100 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void EventTrungThu2024_7(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 100_000_000L * quantity) {
                    int ID_ITEM_NHAN = 1340; // NHẬN ĐƯỢC
                    int id_cau = 462; // item 1
                    int sl_doi_1 = 50;
                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);

                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);

                    player.inventory.gold -= 100_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 100 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void EventTrungThu2024_8(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 100_000_000L * quantity) {
                    int ID_ITEM_NHAN = 1620; // NHẬN ĐƯỢC
                    int id_cau = 462; // item 1
                    int sl_doi_1 = 40;
                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);

                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);

                    player.inventory.gold -= 100_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 100 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void EventTrungThu2024_Lam_Banh_VIP(Player player, boolean isVIP2, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 500_000_000L * quantity) {
                    int ID_ITEM_NHAN = 1338; // NHẬN ĐƯỢC

                    int id_cau = 1336; // item 1
                    int id_dua = 1337; // item 2

                    int sl_doi_1 = 1;
                    int sl_doi_2 = 1;
                    if (isVIP2) {
                        sl_doi_1 = 2;
                        sl_doi_2 = 2;
                        ID_ITEM_NHAN = 1339;
                    }

                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                    Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_dua);

                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    if (item_2 == null || item_2.quantity < sl_doi_2 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_dua, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_2 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_2 * quantity);
                    int eventPoint = 1;
                    if (isVIP2) {
                        eventPoint = 2;
                    }
                    player.event.addEventPoint(eventPoint);
                    player.inventory.gold -= 500_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name + " và " + eventPoint
                            + " điểm sự kiện");

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 500 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void EventTrungThu2024_Doi_Carrot(Player player, byte type, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 500_000_000L * quantity) {
                    int ID_ITEM_NHAN = -1; // NHẬN ĐƯỢC
                    int id_cau = 462; // item 1
                    int sl_doi_1 = 1;

                    if (type == 1) {
                        sl_doi_1 = 199;
                        ID_ITEM_NHAN = 739;
                    } else if (type == 2) {
                        sl_doi_1 = 99;
                        ID_ITEM_NHAN = 463;
                    } else if (type == 3) {
                        sl_doi_1 = 50;
                        ID_ITEM_NHAN = 1340;
                    }

                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                    player.inventory.gold -= 500_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    ItemService.gI().OptionAllItem(itemNhan, 95);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 500 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }
    }

    public void NhanQuaEventMoiNgay(Player player, Npc npc) {
        if (!player.event.isReceivedLuckyMoney()) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            Item goldBar = ItemService.gI().createNewItem((short) 1340, 1);
            goldBar.itemOptions.add(new ItemOption(74, 0));
            goldBar.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
            InventoryService.gI().addItemBag(player, goldBar, 99999);
            InventoryService.gI().sendItemBags(player);
            PlayerService.gI().sendInfoHpMpMoney(player);
            player.event.setReceivedLuckyMoney(true);
            Service.getInstance().sendThongBao(player,
                    "Bạn vừa nhận được " + goldBar.template.name);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi, hãy chờ đến ngày mai");
        }
    }

    public void NguHanhSon_1(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 500_000_000L * quantity) {
                    int ID_ITEM_NHAN = 544 + player.gender; // NHẬN ĐƯỢC
                    int id_cau = 543; // item 1
                    int sl_doi_1 = 99;

                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                    player.inventory.gold -= 500_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    ItemService.gI().OptionAllItem(itemNhan, 95);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 500 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }
    }

    public void NguHanhSon_2(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                if (player.inventory.gold > 500_000_000L * quantity) {
                    short listItemNhan[] = {1333, 1334, 1335};
                    short ID_ITEM_NHAN = Util.randomItem(listItemNhan);

                    int id_1 = 537; // item 1
                    int id_2 = 538; // item 2
                    int id_3 = 539; // item 3
                    int id_4 = 540; // item 4

                    int sl_doi_1 = 99;
                    int sl_doi_2 = 99;
                    int sl_doi_3 = 99;
                    int sl_doi_4 = 99;

                    Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_1);
                    Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_2);
                    Item item_3 = InventoryService.gI().findItemBagByTemp(player, id_3);
                    Item item_4 = InventoryService.gI().findItemBagByTemp(player, id_4);
                    if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_1, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    if (item_2 == null || item_2.quantity < sl_doi_2 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_2, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_2 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    if (item_3 == null || item_3.quantity < sl_doi_3 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_3, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_3 * quantity) + " " + itemNotify.template.name);
                        return;
                    }
                    if (item_4 == null || item_4.quantity < sl_doi_4 * quantity) {
                        Item itemNotify = ItemService.gI().createNewItem((short) id_4, 1);
                        Service.getInstance().sendThongBao(player,
                                "Cần x" + (sl_doi_4 * quantity) + " " + itemNotify.template.name);
                        return;
                    }

                    InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_2 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_3, sl_doi_3 * quantity);
                    InventoryService.gI().subQuantityItemsBag(player, item_4, sl_doi_4 * quantity);
                    player.inventory.gold -= 500_000_000L * quantity;
                    Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                    ItemService.gI().OptionAllItem(itemNhan, 97);
                    itemNhan.itemOptions.add(new ItemOption(ConstOption.KHONG_THE_GD, 1));
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

                } else {
                    Service.getInstance().sendThongBao(player,
                            "Mỗi vật phẩm đổi cần 500 triệu vàng");
                    return;
                }
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    // SỰ KIỆN HALLOWEEN
    public void Halloween_1(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {

                int ID_ITEM_NHAN = 2016; // NHẬN ĐƯỢC
                int id_cau = 2013; // item 1
                int sl_doi_1 = 3;

                Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                    Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                    Service.getInstance().sendThongBao(player,
                            "Cần " + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                    return;
                }
                InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);

                Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);

                InventoryService.gI().addItemBag(player, itemNhan, 999);
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }
    }

    public void Halloween_2(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {

                short ID_ITEM_NHAN = 1302;

                int id_1 = 2014; // item 1
                int id_2 = 2013; // item 2

                int sl_doi_1 = 3;
                int sl_doi_2 = 3;

                Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_1);
                Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_2);

                if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                    Item itemNotify = ItemService.gI().createNewItem((short) id_1, 1);
                    Service.getInstance().sendThongBao(player,
                            "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                    return;
                }
                if (item_2 == null || item_2.quantity < sl_doi_2 * quantity) {
                    Item itemNotify = ItemService.gI().createNewItem((short) id_2, 1);
                    Service.getInstance().sendThongBao(player,
                            "Cần x" + (sl_doi_2 * quantity) + " " + itemNotify.template.name);
                    return;
                }

                InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_2 * quantity);

                Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);

                InventoryService.gI().addItemBag(player, itemNhan, 999);
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void Halloween_3(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {

                short ID_ITEM_NHAN = 2015;

                int id_1 = 1302; // item 1
                int id_2 = 2016; // item 2

                int sl_doi_1 = 99;
                int sl_doi_2 = 99;

                Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_1);
                Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_2);

                if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                    Item itemNotify = ItemService.gI().createNewItem((short) id_1, 1);
                    Service.getInstance().sendThongBao(player,
                            "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                    return;
                }
                if (item_2 == null || item_2.quantity < sl_doi_2 * quantity) {
                    Item itemNotify = ItemService.gI().createNewItem((short) id_2, 1);
                    Service.getInstance().sendThongBao(player,
                            "Cần x" + (sl_doi_2 * quantity) + " " + itemNotify.template.name);
                    return;
                }

                InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_2 * quantity);

                Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);

                InventoryService.gI().addItemBag(player, itemNhan, 999);
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void Halloween_4(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {
                short listItem[] = {450, 451};
                int ID_ITEM_NHAN = Util.randomItem(listItem); // NHẬN ĐƯỢC
                int id_cau = 2015; // item 1
                int sl_doi_1 = 10;

                Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_cau);
                if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                    Item itemNotify = ItemService.gI().createNewItem((short) id_cau, 1);
                    Service.getInstance().sendThongBao(player,
                            "Cần " + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                    return;
                }
                InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);
                ItemService.gI().OptionAllItem(itemNhan, sl_doi_1);
                itemNhan.itemOptions.add(new ItemOption(ConstOption.VAT_PHAM_SU_KIEN, 17));
                InventoryService.gI().addItemBag(player, itemNhan, 999);
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }
    }

    // PHASE 2
    public void Halloween_note_2_1(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (quantity > 0 && quantity < 999) {

                short ID_ITEM_NHAN = 2015;

                int id_1 = 2014; // item 1
                int id_2 = 2013; // item 2

                int sl_doi_1 = 99;
                int sl_doi_2 = 99;

                Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_1);
                Item item_2 = InventoryService.gI().findItemBagByTemp(player, id_2);

                if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                    Item itemNotify = ItemService.gI().createNewItem((short) id_1, 1);
                    Service.getInstance().sendThongBao(player,
                            "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                    return;
                }
                if (item_2 == null || item_2.quantity < sl_doi_2 * quantity) {
                    Item itemNotify = ItemService.gI().createNewItem((short) id_2, 1);
                    Service.getInstance().sendThongBao(player,
                            "Cần x" + (sl_doi_2 * quantity) + " " + itemNotify.template.name);
                    return;
                }

                InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                InventoryService.gI().subQuantityItemsBag(player, item_2, sl_doi_2 * quantity);

                Item itemNhan = ItemService.gI().createNewItem((short) ID_ITEM_NHAN, quantity);

                InventoryService.gI().addItemBag(player, itemNhan, 999);
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được " + quantity + " " + itemNhan.template.name);

            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy!");
            return;
        }

    }

    public void Halloween_note_2_2(Player player, int quantity) {
        if (InventoryService.gI().getCountEmptyBag(player) > 7) {
            if (quantity > 0 && quantity < 999) {

                short ID_ITEM_NHAN = 702;

                int id_1 = 2015; // item 1

                int sl_doi_1 = 10;

                Item item_1 = InventoryService.gI().findItemBagByTemp(player, id_1);

                if (item_1 == null || item_1.quantity < sl_doi_1 * quantity) {
                    Item itemNotify = ItemService.gI().createNewItem((short) id_1, 1);
                    Service.getInstance().sendThongBao(player,
                            "Cần x" + (sl_doi_1 * quantity) + " " + itemNotify.template.name);
                    return;
                }
                InventoryService.gI().subQuantityItemsBag(player, item_1, sl_doi_1 * quantity);
                for (int i = 0; i < 7; i++) {
                    Item itemNhan = ItemService.gI().createNewItem((short) (ID_ITEM_NHAN + i), quantity);
                    InventoryService.gI().addItemBag(player, itemNhan, 999);
                }

                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player,
                        "Bạn vừa nhận được bộ ngọc rồng bí ngô");

            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng nhập đúng số lượng!");
                return;
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy, cần 7 ô trống!");
            return;
        }

    }

}
