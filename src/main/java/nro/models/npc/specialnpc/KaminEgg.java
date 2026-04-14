package nro.models.npc.specialnpc;

import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.services.func.ChangeMapService;
import nro.services.PetService;
import nro.models.player.Player;
import nro.utils.Util;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Log;

public class KaminEgg {

    private static final long DEFAULT_TIME_DONE = 2592000000L;

    private Player player;
    public long lastTimeCreate;
    public long timeDone;

    private final short id = 50;

    public KaminEgg(Player player, long lastTimeCreate, long timeDone) {
        this.player = player;
        this.lastTimeCreate = lastTimeCreate;
        this.timeDone = timeDone;
    }

    public static void createKaminEgg(Player player) {
        player.kaminEgg = new KaminEgg(player, System.currentTimeMillis(), DEFAULT_TIME_DONE);
    }

    public void sendKaminEgg() {
        Message msg;
        try {
            msg = new Message(-122);
            msg.writer().writeShort(this.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(15073);
            msg.writer().writeByte(0);
            msg.writer().writeInt(this.getSecondDone());
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(KaminEgg.class, e);
        }
    }

    public int getSecondDone() {
        int seconds = (int) ((lastTimeCreate + timeDone - System.currentTimeMillis()) / 1000);
        return seconds > 0 ? seconds : 0;
    }

    public void openEgg() {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            try {
                destroyEgg();
                short[] temp = {2054, 1288, 2053, 1283, 1278};

                Item lthu = ItemService.gI().createNewItem(temp[Util.nextInt(temp.length)]);

                lthu.itemOptions.add(new ItemOption(50, Util.nextInt(3, 5)));
                lthu.itemOptions.add(new ItemOption(77, Util.nextInt(3, 5)));
                lthu.itemOptions.add(new ItemOption(103, Util.nextInt(3, 5)));

                switch (lthu.getId()) {
                    case 1283:
                        lthu.itemOptions.add(new ItemOption(106, 1));
                        break;
                    case 2053:
                        lthu.itemOptions.add(new ItemOption(212, 5));
                        break;
                    case 2054:
                        lthu.itemOptions.add(new ItemOption(5, 3));
                        break;
                    case 1288:
                        lthu.itemOptions.add(new ItemOption(8, 10));
                        break;
                    case 1278:
                        lthu.itemOptions.add(new ItemOption(247, 0));
                        break;
                }
                InventoryService.gI().addItemBag(player, lthu);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + lthu.getName());
                Thread.sleep(4000);

                player.kaminEgg = null;

            } catch (Exception e) {
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }

    public void destroyEgg() {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(101);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        this.player.kaminEgg = null;
    }

    public void subTimeDone(int d, int h, int m, int s) {
        this.timeDone -= ((d * 24 * 60 * 60 * 1000) + (h * 60 * 60 * 1000) + (m * 60 * 1000) + (s * 1000));
        sendKaminEgg();
    }

    public void dispose() {
        this.player = null;
    }
}
