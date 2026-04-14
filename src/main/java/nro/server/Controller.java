package nro.server;

import nro.consts.*;
import nro.data.DataGame;
import nro.data.ItemData;
import nro.jdbc.DBService;
import nro.models.consignment.ConsignmentShop;
import nro.models.map.war.BlackBallWar;
import nro.models.npc.NpcManager;
import nro.models.player.Player;
import nro.models.skill.PlayerSkill;
import nro.noti.NotiManager;
import nro.resources.Resources;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.*;
import nro.services.func.*;
import nro.utils.Log;
import nro.utils.Util;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nro.models.boss.Boss;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.sendEff.SendEffect;

public class Controller {

    private static Controller instance;

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    private static final Logger logger = Logger.getLogger(Controller.class);

    public void onMessage(Session _session, Message _msg) {
        long st = System.currentTimeMillis();
        try {
            Player player = _session.player;
            byte cmd = _msg.command;
            // System.out.println("CMD receive: " + cmd);
            if (Manager.debug) {
                System.out.println("CMD receive: " + cmd);
            }
            switch (cmd) {
                case Cmd.KIGUI:
                    ConsignmentShop.getInstance().handler(player, _msg);
                    // KyGuiService.gI().handler(player, _msg);
                    break;
                case Cmd.ACHIEVEMENT:
                    TaskService.gI().rewardAchivement(player, _msg.reader().readByte());
                    break;
                case Cmd.RADA_CARD:
                    RadaService.getInstance().controller(player, _msg);
                    break;
                case -127:
                    if (player != null) {
                        LuckyRoundService.gI().readOpenBall(player, _msg);
                    }
                    break;
                case -125:
                    if (player != null) {
                        Input.gI().doInput(player, _msg);
                    }
                    break;
                case 112:
                    if (player != null) {
                        IntrinsicService.gI().showMenu(player);
                    }
                    break;
                case -34:
                    if (player != null) {
                        switch (_msg.reader().readByte()) {
                            case 1:
                                player.magicTree.openMenuTree();
                                break;
                            case 2:
                                player.magicTree.loadMagicTree();
                                break;
                        }
                    }
                    break;
                case -99:
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerEnemy(player, _msg);
                    }
                    break;
                case 18:
                    if (player != null) {
                        FriendAndEnemyService.gI().goToPlayerWithYardrat(player, _msg);
                    }
                    break;
                case -72:
                    if (player != null) {
                        FriendAndEnemyService.gI().chatPrivate(player, _msg);
                    }
                    break;
                case -80:
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerFriend(player, _msg);
                    }
                    break;
                case -59:
                    if (player != null) {
                        PVPServcice.gI().controller(player, _msg);
                    }
                    break;
                case -86:
                    if (player != null) {
                        TransactionService.gI().controller(player, _msg);
                    }
                    break;
                case -107:

                    if (player != null) {
                        Service.getInstance().sendChiSoPetGoc(player);
                        Service.getInstance().showInfoPet(player);
                    }
                    break;
                case -108:
                    if (player != null && player.pet != null) {
                        player.pet.changeStatus(_msg.reader().readByte());
                    }
                    break;
                case 6: // buy item
                    if (player != null) {
                        byte typeBuy = _msg.reader().readByte();
                        int tempId = _msg.reader().readShort();
                        int quantity = 0;
                        try {
                            quantity = _msg.reader().readShort();
                        } catch (Exception e) {
                        }
                        if (!Manager.is_reload_shop) {
                            ShopService.gI().buyItem(player, typeBuy, tempId);
                        } else {
                            Service.getInstance().sendThongBao(player, "Cửa hàng đang tải dữ liệu, hãy thử lại");
                        }
                    }
                    break;
                case 7: // sell item
                    if (player != null) {
                        int action = _msg.reader().readByte();
                        int where = _msg.reader().readByte();
                        int index = _msg.reader().readShort();
                        if (action == 0) {
                            ShopService.gI().showConfirmSellItem(player, where,
                                    !player.isVersionAbove(220) ? index - 3 : index);
                        } else {
                            ShopService.gI().sellItem(player, where, index);
                        }
                    }
                    break;
                case 29:
                    if (player != null) {
                        Service.getInstance().openZoneUI(player);
                    }
                    break;
                case 21:
                    if (player != null) {
                        int zoneId = _msg.reader().readByte();
                        ChangeMapService.gI().changeZone(player, zoneId);
                    }
                    break;
                case -71:
                    if (player != null) {
                        ChatGlobalService.gI().chat(player, _msg.reader().readUTF());
                    }
                    break;
                case -79:
                    if (player != null) {
                        Service.getInstance().getPlayerMenu(player, _msg.reader().readInt());
                    }
                    break;
                case -113:
                    if (player != null) {
                        PlayerSkill playerSkill = player.playerSkill;
                        int len = _msg.reader().available();

                        for (int i = 0; i < player.playerSkill.skillShortCut.length; i++) {
                            byte b = _msg.reader().readByte();
                            playerSkill.skillShortCut[i] = b;
                        }
                        playerSkill.sendSkillShortCut();
                    }
                    break;
                case -118: {
                    if (player == null) {
                        break;
                    }

                    int id = _msg.reader().readInt();

                    // Chặn member tuyệt đối
                    if (!player.isAdmin()) {
                        Service.getInstance().sendThongBao(player, "Chức năng này chỉ dành cho Admin.");
                        break;
                    }

                    if (id == -1) {
                        break;
                    }

                    for (Boss bosse : BossManager.gI().getBosses()) {
                        if (bosse != null && bosse.id == id && !bosse.isDie() && bosse.zone != null) {
                            ChangeMapService.gI().changeMapYardrat(player, bosse.zone,
                                    bosse.location.x + Util.nextInt(-10, 10),
                                    bosse.zone.map.yPhysicInTop(bosse.location.x, bosse.location.y - 24));
                            break;
                        }
                    }
                    break;
                }

                    // }
                    // if (idaction == 1) {
                    // int plId = _msg.reader().readInt();
                    // Player pl_target = Client.gI().getPlayer(plId);
                    // if (pl_target != null && plId > 0) {
                    // ChangeMapService.gI().changeMapInYard(pl_target, player.zone.map.mapId,
                    // player.zone.zoneId, player.location.x - 50);
                    // Service.getInstance().sendThongBao(player, "Triệu hồi thành công");
                    // Service.getInstance().sendThongBao(pl_target, "Bạn đã được triệu hồi thông
                    // qua dương cảnh");
                    // } else if (plId < 0) {
                    // Boss be = BossManager.gI().getBossById((byte) plId);
                    // if (be != null && !be.isDie()) {
                    // ChangeMapService.gI().changeMapInYard(be, player.zone.map.mapId,
                    // player.zone.zoneId, player.location.x - 50);
                    // } else {
                    // NpcService.gI().createMenuConMeo(player, ConstNpc.IGNORE_MENU, -1, "|7| No
                    // active Boss ", "Close");
                    //
                    // }
                    // } else {
                    // NpcService.gI().createMenuConMeo(player, ConstNpc.IGNORE_MENU, -1, "|7| No
                    // active Boss ", "Close");
                    // }
                    // break;
                    // }
                    // if (idaction == 2) {
                    // int plId = _msg.reader().readInt();
                    // Player pl_target = Client.gI().getPlayer(plId);
                    // if (pl_target != null && plId > 0) {
                    // if (!pl_target.isPet && !pl_target.isBoss) {
                    // NpcService.gI().createMenuConMeo(player, ConstNpc.IGNORE_MENU, -1, " "
                    // + " HP : " + pl_target.nPoint.hpMax
                    // + "\nKI : " + pl_target.nPoint.mpMax, "Close");
                    // }
                    // } else if (plId < 0) {
                    // Boss be = BossManager.gI().getBossById((byte) plId);
                    // if (be != null && !be.isDie()) {
                    // System.out.println("BE : " + be.name);
                    // NpcService.gI().createMenuConMeo(player, ConstNpc.IGNORE_MENU, -1, "|7| BOSS
                    // INFOR "
                    // + " HP : " + be.nPoint.hpMax
                    // + "\nKI : " + be.nPoint.mpMax, "Close");
                    // } else {
                    // NpcService.gI().createMenuConMeo(player, ConstNpc.IGNORE_MENU, -1, "|7| No
                    // active Boss ", "Close");
                    // }
                    // } else {
                    // NpcService.gI().createMenuConMeo(player, ConstNpc.IGNORE_MENU, -1, "|7|
                    // OFFLINE PLAYER ", "Close");
                    // }
                    //
                    // }
//                    break;
                case -101:
                    login2(_session, _msg);
                    break;
                case -103:
                    if (player != null) {
                        byte act = _msg.reader().readByte();
                        if (act == 0) {
                            Service.getInstance().openFlagUI(player);
                        } else if (act == 1) {
                            Service.getInstance().chooseFlag(player, _msg.reader().readByte());
                        } else {
                            // Util.log("id map" + player.map.id);
                        }
                    }
                    break;
                case -7:
                    if (player != null) {
                        int toX = player.location.x;
                        int toY = player.location.y;
                        try {
                            byte b = _msg.reader().readByte();
                            toX = _msg.reader().readShort();
                            toY = _msg.reader().readShort();
                        } catch (Exception e) {
                        }
                        PlayerService.gI().playerMove(player, toX, toY);
                    }
                    break;
                case Cmd.GET_IMAGE_SOURCE:
                    // System.out.println("-74");
                    Resources.getInstance().downloadResources(_session, _msg);
                    break;
                case -81:
                    if (player != null) {
                        _msg.reader().readByte();
                        int[] indexItem = new int[_msg.reader().readByte()];
                        for (int i = 0; i < indexItem.length; i++) {
                            indexItem[i] = _msg.reader().readByte();
                        }
                        // CombineService.gI().showInfoCombine(player, indexItem);
                        CombineServiceNew.gI().showInfoCombine(player, indexItem);
                    }
                    break;
                case -87:
                    DataGame.updateData(_session);
                    break;
                case Cmd.FINISH_UPDATE:
                    _session.finishUpdate();
                    break;
                case Cmd.REQUEST_ICON:
                    int id = _msg.reader().readInt();
                    Resources.getInstance().downloadIconData(_session, id);
                    break;
                case Cmd.GET_IMG_BY_NAME:
                    Resources.getInstance().downloadIBN(_session, _msg.reader().readUTF());
                    break;
                case -66:
                    int effId = _msg.reader().readShort();
                    Resources.effData(_session, effId);
                    break;
                case -62:
                    if (player != null) {
                        FlagBagService.gI().sendIconFlagChoose(player, _msg.reader().readByte());
                    }
                    break;
                case -63:
                    if (player != null) {
                        FlagBagService.gI().sendIconEffectFlag(player, _msg.reader().readByte());
                    }
                    break;
                case Cmd.BACKGROUND_TEMPLATE:
                    int bgId = _msg.reader().readShort();
                    Resources.getInstance().downloadBGTemplate(_session, bgId);
                    break;
                case 22:
                    if (player != null) {
                        _msg.reader().readByte();
                        NpcManager.getNpc(ConstNpc.DAU_THAN).confirmMenu(player, _msg.reader().readByte());
                    }
                    break;
                case -33:
                case -23:
                    if (player != null) {
                        player.zone.changeMapWaypoint(player);
                        Service.getInstance().hideWaitDialog(player);
                    }
                    break;
                case -45:
                    if (player != null) {
                        Service.getInstance().useSkillNotFocus(player, _msg);
                    }
                    break;
                case -46:
                    if (player != null) {
                        ClanService.gI().getClan(player, _msg);
                    }
                    break;
                case -51:
                    if (player != null) {
                        ClanService.gI().clanMessage(player, _msg);
                    }
                    break;
                case -54:
                    if (player != null) {
                        ClanService.gI().clanDonate(player, _msg);
                        // Service.getInstance().sendThongBao(player, "Can not invoke clan donate");
                    }
                    break;
                case -49:
                    if (player != null) {
                        ClanService.gI().joinClan(player, _msg);
                    }
                    break;
                case -50:
                    if (player != null) {
                        ClanService.gI().sendListMemberClan(player, _msg.reader().readInt());
                    }
                    break;
                case -56:
                    if (player != null) {
                        ClanService.gI().clanRemote(player, _msg);
                    }
                    break;
                case -47:
                    if (player != null) {
                        ClanService.gI().sendListClan(player, _msg.reader().readUTF());
                    }
                    break;
                case -55:
                    if (player != null) {
                        ClanService.gI().showMenuLeaveClan(player);
                    }
                    break;
                case -57:
                    if (player != null) {
                        ClanService.gI().clanInvite(player, _msg);
                    }
                    break;
                case -40:
                    UseItem.gI().getItem(_session, _msg);
                    break;
                case -41:
                    Service.getInstance().sendCaption(_session, _msg.reader().readByte());
                    break;
                case -43:
                    if (player != null) {
                        UseItem.gI().doItem(player, _msg);
                    }
                    break;
                case -91:
                    if (player != null) {
                        switch (player.iDMark.getTypeChangeMap()) {
                            case ConstMap.CHANGE_CAPSULE:
                                UseItem.gI().choseMapCapsule(player, _msg.reader().readByte());
                                break;
                            case ConstMap.CHANGE_BLACK_BALL:
                                BlackBallWar.gI().changeMap(player, _msg.reader().readByte());
                                break;
                        }
                    }
                    break;
                case -39:
                    if (player != null) {
                        // finishLoadMap
                        ChangeMapService.gI().finishLoadMap(player);
                        if (player.zone.map.mapId == (21 + player.gender)) {
                            if (player.mabuEgg != null) {
                                player.mabuEgg.sendMabuEgg();
                            }
                        }
                        if (player.zone.map.mapId == 187) {
                            if (player.kaminEgg != null) {
                                player.kaminEgg.sendKaminEgg();
                            }
                        }
                        EffectMapService.gI().sendEffEvent(player);
                    }
                    break;
                case 11:
                    byte modId = _msg.reader().readByte();
                    Resources.requestMobTemplate(_session, modId);
                    break;
                case 44:
                    if (player != null) {
                        String text = _msg.reader().readUTF();
                        Service.getInstance().chat(player, text);
                    }
                    break;
                case 32:
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        int select = _msg.reader().readByte();
                        MenuController.getInstance().doSelectMenu(player, npcId, select);
                    }
                    break;
                case 33:
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        MenuController.getInstance().openMenuNPC(_session, npcId, player);
                    }
                    break;
                case 34:
                    if (player != null) {
                        int selectSkill = _msg.reader().readShort();
                        SkillService.gI().selectSkill(player, selectSkill);
                    }
                    break;
                case 54:
                    if (player != null) {
                        Service.getInstance().attackMob(player, (int) (_msg.reader().readByte()));
                    }
                    break;
                case -60:
                    if (player != null) {
                        int playerId = _msg.reader().readInt();
                        Service.getInstance().attackPlayer(player, playerId);
                    }
                    break;
                case -27:
                    _session.sendSessionKey();
                    break;
                case -111:
                    System.out.println("send image version");
                    DataGame.sendDataImageVersion(_session);
                    break;
                case -20:
                    if (player != null && !player.isDie()) {
                        int itemMapId = _msg.reader().readShort();
                        ItemMapService.gI().pickItem(player, itemMapId, false);
                    }
                    break;
                case -28:
                    messageNotMap(_session, _msg);
                    break;
                case -29:
                    messageNotLogin(_session, _msg);
                    break;
                case -30:
                    messageSubCommand(_session, _msg);
                    break;
                case 42:
//                    Service.getInstance().CreatAcc(_session, _msg);
                    break;
                case -15: // về nhà
                    if (player != null) {
                        player.isGoHome = true;
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                        player.isGoHome = false;
                    }
                    break;
                case -16: // hồi sinh
                    if (player != null) {
                        if (player.zone.map.mapId == 188) {
                            // Không cho hồi sinh nếu player đang ở map 188
                            break;
                        }
                        PlayerService.gI().hoiSinh(player);
                    }
                    break;
                default:
                    // Util.log("CMD: " + cmd);
                    break;
            }
            if (_session.logCheck) {
                // System.out.println("Time do controller (" + cmd + "): " +
                // (System.currentTimeMillis() - st) + " ms");
            }
        } catch (Exception e) {
            logger.error("Err controller message command: " + _msg.command, e);
            // Log.logException(Controller.class, e);
            // Log.warning("Lỗi controller message command: " + _msg.command);
        }
    }

    public void messageNotLogin(Session session, Message msg) {
        if (msg != null) {
            try {
                byte cmd = msg.reader().readByte();
                switch (cmd) {
                    case 0:
                        session.login(msg.reader().readUTF(), msg.reader().readUTF());
                        break;
                    case 2:
                        session.setClientType(msg);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Log.error(Controller.class, e);
            }
        }
    }

    public void messageNotMap(Session _session, Message _msg) {
        if (_msg != null) {
            try {
                Player player = _session.player;
                byte cmd = _msg.reader().readByte();
                // System.out.println("CMD receive -28 / " + cmd);
                switch (cmd) {
                    case 2:
                        createChar(_session, _msg);
                        break;
                    case 6:
                        DataGame.createMap(_session);
                        break;
                    case 7:
                        DataGame.updateSkill(_session);
                        break;
                    case 8:
                        ItemData.updateItem(_session);
                        break;
                    case 10:
                        DataGame.sendMapTemp(_session, _msg.reader().readUnsignedByte());
                        break;
                    // case 13:
                    // //client ok
                    // if (player != null) {
                    // Service.getInstance().player(player);
                    // Service.getInstance().Send_Caitrang(player);
                    // player.zone.load_Another_To_Me(player);
                    //
                    // // -64 my flag bag
                    // Service.getInstance().sendFlagBag(player);
                    //
                    // // -113 skill shortcut
                    // player.playerSkill.sendSkillShortCut();
                    // // item time
                    // ItemTimeService.gI().sendAllItemTime(player);
                    //
                    // // send current task
                    // TaskService.gI().sendInfoCurrentTask(player);
                    // }
                    // break;
                    default:
                        break;
                }
            } catch (IOException e) {
                Log.error(Controller.class, e);
            }
        }
    }

    public void messageSubCommand(Session _session, Message _msg) {
        if (_msg != null) {
            try {
                Player player = _session.player;
                byte command = _msg.reader().readByte();
                switch (command) {
                    case 16:
                        byte type = _msg.reader().readByte();
                        short point = _msg.reader().readShort();
                        if (player != null && player.nPoint != null) {
                            player.nPoint.increasePoint(type, point, false);
                        }
                        break;
                    case 18:
                        byte type1 = _msg.reader().readByte();
                        short point1 = _msg.reader().readShort();
                        if (player != null && player.pet != null) {
                            player.pet.nPoint.increasePoint(type1, point1, true);
                        }
                        break;
                    case 64:
                        int playerId = _msg.reader().readInt();
                        int menuId = _msg.reader().readShort();
                        SubMenuService.gI().controller(player, playerId, menuId);
                        break;
                    case -99:
                        byte type2 = _msg.reader().readByte();
                        short point2 = _msg.reader().readShort();
                        if (player.pet != null && player.pet.nPoint != null) {
                            player.nPoint.increasePointPet(type2, point2);
                        }
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }

    public void createChar(Session session, Message msg) {
        if (!Maintenance.isRuning) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean created = false;
            try (Connection con = DBService.gI().getConnectionCreatPlayer();) {
                String name = msg.reader().readUTF();
                int gender = msg.reader().readByte();
                int hair = msg.reader().readByte();
                if (name.length() <= 15 && name.length() >= 5) {
                    ps = con.prepareStatement("select * from player where name = ? or account_id = ?");
                    ps.setString(1, name);
                    ps.setInt(2, session.userId);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        Service.getInstance().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                    } else {
                        // boolean isValid = validateName(name);

                        // if (!isValid) {
                        // Service.getInstance().sendThongBaoOK(session,
                        // "Tên nhân vật không được chứa ký tự đặc biệt");
                        // }
                        if (Util.haveSpecialCharacter(name)) {
                            Service.getInstance().sendThongBaoOK(session,
                                    "Tên nhân vật không được chứa ký tự đặc biệt");
                        } else {
                            boolean isNotIgnoreName = true;
                            for (String n : ConstIgnoreName.IGNORE_NAME) {
                                if (name.equals(n)) {
                                    Service.getInstance().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                                    isNotIgnoreName = false;
                                    break;
                                }
                            }
                            if (isNotIgnoreName) {
                                created = PlayerService.gI().createPlayer(con, session.userId, name.toLowerCase(),
                                        gender, hair);
                            }
                        }
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(session, "Tên nhân vật tối thiểu 5 kí tự và tối đa 15 ký tự");
                }
            } catch (Exception e) {
                Log.error(Controller.class, e);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                }
            }
            if (created) {
                session.finishUpdate();
            }
        }
    }

    public void login2(Session session, Message msg) {
        //Service.getInstance().switchToRegisterScr(session);
        Service.getInstance().sendThongBaoOK(session,
                "Vui lòng đăng ký tài khoản tại " + SettingGame.NAME_GAME);

    }

    private static boolean validateName(String input) {
        // Biểu thức chính quy để chỉ chấp nhận các ký tự tiếng Việt, dấu cách và số
        String regex = "^[\\p{L}\\p{N}\\s]+$";

        // Khớp biểu thức chính quy với chuỗi đầu vào
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public void sendInfo(Session session) {
        Player player = session.player;
        DataGame.sendDataItemBG(session);
        // -82 set tile map
        DataGame.sendTileSetInfo(session);

        // 112 my info intrinsic
        IntrinsicService.gI().sendInfoIntrinsic(player);

        // -42 my point
        Service.getInstance().point(player);

        // clear vật phẩm sự kiện
        //clearVTSK(player);
        // 40 task
        TaskService.gI().sendTaskMain(player);

        // -22 reset all
        Service.getInstance().clearMap(player);

        // -53 my clan
        ClanService.gI().sendMyClan(player);

        // -69 max statima
        PlayerService.gI().sendMaxStamina(player);

        // -68 cur statima
        PlayerService.gI().sendCurrentStamina(player);

        // -97 năng động
        // -107 have pet
        Service.getInstance().sendHavePet(player);

        // -119 top rank
        Service.getInstance().sendTopRank(player);

        // -50 thông tin bảng thông báo
        // -24 join map - map info
        player.zone.load_Me_To_Another(player);
        player.zone.mapInfo(player);
        // -70 thông báo bigmessage
        // check activation set
        player.setClothes.setup();
        if (player.pet != null) {
            player.pet.setClothes.setup();
        }
        if (player.inventory.itemsBody.get(11).isNotNullItem()) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    SendEffect.getInstance().sendChanThienTu(player, (short) player.inventory.itemsBody.get(11).template.id);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Loi send vongchantt player: " + player.name);
                    Service.getInstance().sendThongBaoFromAdmin(player, "Da co loi xay ra");
                }
            }).start();
        }
        if (player.inventory.itemsBody.get(12).isNotNullItem()) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    SendEffect.getInstance().sendDanhhieu(player, (short) player.inventory.itemsBody.get(12).template.id);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Loi send danhuieu player: " + player.name);
                    Service.getInstance().sendThongBaoFromAdmin(player, "Da co loi xay ra");
                }
            }).start();
        }
        if (player.charms.tdDeTuMabu2 - System.currentTimeMillis() > 0) {
            ItemTimeService.gI().sendTextBanDoKhoBauNew(player);
        }
        // last time use skill
        Service.getInstance().sendTimeSkill(player);

        if (TaskService.gI().getIdTask(player) == ConstTask.TASK_0_0) {
            NpcService.gI().createTutorial(player, -1,
                    "Chào mừng " + player.name + " đến với " + SettingGame.NAME_GAME + "\n"
                    + "Nhiệm vụ đầu tiên của bạn là di chuyển\n"
                    + "Bạn hãy di chuyển nhân vật theo mũi tên chỉ hướng");
        }
        if (!Service.getInstance().ThongBaoNhanTop(player)) {
            Service.getInstance().sendThongBao(player, "Sever " + SettingGame.NAME_GAME + "\n"
                    // + "Dev Degsin : kenit2k3\n"
                    // + "- Update Rương sưu tầm ( Thử Nghiệm )\n"
                    // + "- Tại Tương lai bunma\n"
                    // + "Code :  Dicvip1 đến Dicvip7 !!!\n"
                    // + "Chức năng tái chế ( thử nghiệm)\n"
                    // + "Tái chế lại trang bị rác npc toribot\n"
                    + "|7|Chúc anh em chơi game vui vẻ tại " + SettingGame.NAME_GAME);
        }
        InventoryService.gI().CheckAndRestShopTime(player);
        //NotiManager.getInstance().sendAlert(player);
        NotiManager.getInstance().sendNoti(player);
        ConsignmentShop.getInstance().sendExpirationNotification(player);
        Util.setTimeout(() -> PlayerService.gI().sendPetFollow(player), 500);
        player.timeFixInventory = System.currentTimeMillis() + 500;

        //   Service.getInstance().rsDanhHieu(player);
    }

//    private void clearVTSK(Player player) {
//        if (player != null && player.inventory != null && player.inventory.itemsBag != null) {
//            // Xử lý itemsBag
//            player.inventory.itemsBag.stream()
//                    .filter(item -> item.isNotNullItem() && item.template.id == 884)
//                    .forEach(item -> {
//                        for (ItemOption io : item.itemOptions) {
//                            if (io.optionTemplate.id == 5) {
//                                io.param = 10; // Đặt giá trị option về 0 (hoặc giảm giá trị tùy logic)
//                                // Nếu muốn xóa option, có thể xóa khỏi item.itemOptions
//                                // item.itemOptions.remove(io); // Uncomment nếu cần xóa option
//                            }
//                        }
//                    });
//
//            // Xử lý itemsBox
//            player.inventory.itemsBox.stream()
//                    .filter(item -> item.isNotNullItem() && item.template.id == 884)
//                    .forEach(item -> {
//                        for (ItemOption io : item.itemOptions) {
//                            if (io.optionTemplate.id == 5) {
//                                io.param = 10; // Đặt giá trị option về 0
//                                // item.itemOptions.remove(io); // Uncomment nếu cần xóa option
//                            }
//                        }
//                    });
//
//            // Xử lý itemsBody
//            player.inventory.itemsBody.stream()
//                    .filter(item -> item.isNotNullItem() && item.template.id == 884)
//                    .forEach(item -> {
//                        for (ItemOption io : item.itemOptions) {
//                            if (io.optionTemplate.id == 5) {
//                                io.param = 10; // Đặt giá trị option về 0
//                                // item.itemOptions.remove(io); // Uncomment nếu cần xóa option
//                            }
//                        }
//                    });
//
//            // Gửi lại thông tin túi đồ để cập nhật client
//            InventoryService.gI().sendItemBags(player);
//        }
//    }
}
