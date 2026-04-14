package nro.sendEff;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.Service;

/**
 *
 * @Revision - kenit2k3
 */
public class SendEffect {

    private static SendEffect instance;

    public static SendEffect getInstance() {
        if (instance == null) {
            instance = new SendEffect();
        }
        return instance;
    }

    public void sendChanThienTu(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            int shortValue = -1; // Default value in case none of the conditions match
            switch (id) {
                case 1508:
                    shortValue = 120;
                    break;
                case 1509:
                    shortValue = 121;
                    break;
                case 1510:
                    shortValue = 122;
                    break;
                case 1511:
                    shortValue = 123;
                    break;
                case 1512:
                    shortValue = 124;
                    break;
                case 1513:
                    shortValue = 125;
                    break;
                case 1514:
                    shortValue = 126;
                    break;
                case 1515:
                    shortValue = 127;
                    break;
                case 1516:
                    shortValue = 128;
                    break;

                default:
                    break;
            }
            me.writer().writeShort(shortValue);
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendChanThienTuAll(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (id == 1508) {
                me.writer().writeShort(120);
            }
            if (id == 1509) {
                me.writer().writeShort(121);
            }
            if (id == 1510) {
                me.writer().writeShort(122);
            }
            if (id == 1511) {
                me.writer().writeShort(123);
            }
            if (id == 1512) {
                me.writer().writeShort(124);
            }
            if (id == 1513) {
                me.writer().writeShort(125);
            }
            if (id == 1514) {
                me.writer().writeShort(126);
            }
            if (id == 1515) {
                me.writer().writeShort(127);
            }
            if (id == 1516) {
                me.writer().writeShort(128);
            }

            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(0);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendDanhhieu(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            int shortValue = -1; // Default value in case none of the conditions match
            switch (id) {
                case 1525:
                    shortValue = 80;
                    break;
                case 1526:
                    shortValue = 81;
                    break;
                case 1527:
                    shortValue = 82;
                    break;
                case 1528:
                    shortValue = 83;
                    break;
                case 1529:
                    shortValue = 84;
                    break;
                case 1530:
                    shortValue = 85;
                    break;
                case 1531:
                    shortValue = 225;
                    break;
                case 1532:
                    shortValue = 87;
                    break;
                case 1533:
                    shortValue = 88;
                    break;
                case 1534:
                    shortValue = 89;
                    break;
                case 1535:
                    shortValue = 90;
                    break;
                case 1536:
                    shortValue = 91;
                    break;
                case 1537:
                    shortValue = 92;
                    break;
                case 1538:
                    shortValue = 93;
                    break;
                case 1539:
                    shortValue = 97;
                    break;
                case 1621:
                    shortValue = 95;
                    break;
                case 1540:
                    shortValue = 355;
                    break;
                case 1541:
                    shortValue = 99;
                    break;
                case 1629:
                    shortValue = 356;
                    break;
                case 1630:
                    shortValue = 357;
                    break;
                case 1631:
                    shortValue = 358;
                    break;
                case 1632:
                    shortValue = 359;
                    break;
                case 1633:
                    shortValue = 360;
                    break;
                case 1634:
                    shortValue = 361;
                    break;
                case 1635:
                    shortValue = 362;
                    break;

                default:
                    break;
            }

            me.writer().writeShort(shortValue);
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(0);
            Service.getInstance().sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendDanhhieuAll(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (id == 1525) {
                me.writer().writeShort(80);
            }
            if (id == 1526) {
                me.writer().writeShort(81);
            }
            if (id == 1527) {
                me.writer().writeShort(82);
            }
            if (id == 1528) {
                me.writer().writeShort(83);
            }
            if (id == 1529) {
                me.writer().writeShort(84);
            }
            if (id == 1530) {
                me.writer().writeShort(85);
            }
            if (id == 1621) {
                me.writer().writeShort(95);
            }
            if (id == 1531) {
                me.writer().writeShort(225);
            }
            if (id == 1532) {
                me.writer().writeShort(87);
            }
            if (id == 1533) {
                me.writer().writeShort(88);
            }
            if (id == 1534) {
                me.writer().writeShort(89);
            }
            if (id == 1535) {
                me.writer().writeShort(90);
            }
            if (id == 1536) {
                me.writer().writeShort(91);
            }
            if (id == 1537) {
                me.writer().writeShort(92);
            }
            if (id == 1538) {
                me.writer().writeShort(93);
            }
            if (id == 1539) {
                me.writer().writeShort(97);
            }
            if (id == 1540) {
                me.writer().writeShort(355);
            }

            if (id == 1629) {
                me.writer().writeShort(356);
            }
            if (id == 1630) {
                me.writer().writeShort(357);
            }
            if (id == 1631) {
                me.writer().writeShort(358);
            }
            if (id == 1632) {
                me.writer().writeShort(359);
            }
            if (id == 1633) {
                me.writer().writeShort(360);
            }
            if (id == 1634) {
                me.writer().writeShort(361);
            }
            if (id == 1635) {
                me.writer().writeShort(362);
            }

            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(0);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message createMessage2(Player player, Player p2, int danhHieuCode, int ycongdanhhieu) throws IOException {
        Message me = new Message(-128);
        me.writer().writeByte(0);
        me.writer().writeInt((int) player.id);
        me.writer().writeShort(danhHieuCode);
        me.writer().writeByte(1);
        me.writer().writeByte(-1);
        me.writer().writeShort(50);
        me.writer().writeByte(-1);
        if (ycongdanhhieu != 0) {
            me.writer().writeByte(ycongdanhhieu);
        }
        me.writer().writeByte(-1);
        Service.getInstance().sendMessAllPlayerInMap(player.zone, me);
        me.cleanup();
        return me;
    }

    private Message createMessage(Player player, int danhHieuCode, int ycongdanhhieu) throws IOException {
        Message me = new Message(-128);
        me.writer().writeByte(0);
        me.writer().writeInt((int) player.id);
        me.writer().writeShort(danhHieuCode);
        me.writer().writeByte(1);
        me.writer().writeByte(-1);
        me.writer().writeShort(50);
        me.writer().writeByte(-1);
        if (ycongdanhhieu != 0) {
            me.writer().writeByte(ycongdanhhieu);
        }
        me.writer().writeByte(-1);
        Service.getInstance().sendMessAllPlayerInMap(player, me);
        me.cleanup();
        return me;
    }

    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            Service.getInstance().sendMessAllPlayerInMap(player, me);
            if (player.inventory.itemsBody.get(12).isNotNullItem()) {
                sendDanhhieu(player, player.inventory.itemsBody.get(12).template.id);
            }
            if (player.inventory.itemsBody.get(11).isNotNullItem()) {
                sendChanThienTu(player, player.inventory.itemsBody.get(11).template.id);
            }
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    private void executeInBackground(Runnable task) {
        executor.submit(task);
    }

    private void sendDanhHieuIfTimeExists(Player player, short danhHieuId, long lastTime) {
        try {
            Thread.sleep(1000);

        } catch (Exception e) {

        }
    }

}
