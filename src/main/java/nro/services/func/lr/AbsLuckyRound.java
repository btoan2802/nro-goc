/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.services.func.lr;

import nro.consts.Cmd;
import nro.consts.ConstTask;
import nro.models.item.Item;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kitakeyos - Hoàng Hữu Dũng
 */
public abstract class AbsLuckyRound {

    protected int price;
    protected int ticket;
    protected List<Integer> icons = new ArrayList<>();

    public void payAndGetStarted(Player player, byte quantity) {
        if (Manager.is_reload_shop) {
            Service.getInstance().sendThongBao(player, "Vòng quay đang cập nhật, vui lòng quay lại sau");
            return;
        }
        if (quantity < 1 || quantity > 100) {
            Service.getInstance().sendThongBao(player, "Tối đa 100 lần");
            return;
        }
        if (quantity > InventoryService.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall)) {
            Service.getInstance().sendThongBao(player, "Rương phụ đã đầy");
            return;
        }
        int taskId = TaskService.gI().getIdTask(player);

        if (player.getSession().actived || taskId >= ConstTask.TASK_20_0) {
            long cost = (long) quantity * 5_000_000_000L;
        if (player.inventory.gold >= cost) {
            player.inventory.gold -= cost;
            Service.getInstance().sendMoney(player);
            player.GapthuPoint += quantity;
            List<Item> list = reward(player, quantity);
            result(player, list);

            InventoryService.gI().sendItemBags(player);
            } else {
                Service.getInstance().sendThongBao(player, "Bạn không đủ vàng");
            }
        } else {
            Service.getInstance().sendThongBao(player,
                    "Yêu cầu mở thành viên hoặc đã hoàn thành nhiệm vụ tiểu đội sát thủ");
        }
    }

    public void payAndGetStarted1(Player player, byte quantity) {
        if (Manager.is_reload_shop) {
            Service.getInstance().sendThongBao(player, "Vòng quay đang cập nhật, vui lòng quay lại sau");
            return;
        }
        if (quantity < 1 || quantity > 100) {
            Service.getInstance().sendThongBao(player, "Tối đa 100 lần");
            return;
        }
        if (quantity > InventoryService.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall)) {
            Service.getInstance().sendThongBao(player, "Rương phụ đã đầy");
            return;
        }
        int taskId = TaskService.gI().getIdTask(player);

        if (player.getSession().actived || taskId >= ConstTask.TASK_20_0) {
            // Thay đổi cách kiểm tra và trừ hồng ngọc theo cách mới
            int cost = quantity * 2; // Giả sử mỗi lần quay cần 5000 hồng ngọc
            if (player.inventory.ruby >= cost) { // Kiểm tra nếu đủ ruby
                player.inventory.ruby -= cost; // Trừ ruby sau khi kiểm tra
                Service.getInstance().sendMoney(player); // Gửi cập nhật tiền

                InventoryService.gI().sendItemBags(player);
                player.GapthuPoint += quantity;
                List<Item> list = reward(player, quantity);
                result(player, list);
            } else {
                Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc");
            }
        } else {
            Service.getInstance().sendThongBao(player,
                    "Yêu cầu mở thành viên hoặc đã hoàn thành nhiệm vụ tiểu đội sát thủ");
        }
    }

    public abstract boolean checkMoney(Player player, int price);

    public abstract void payWithMoney(Player player, int price);

    public abstract List<Item> reward(Player player, byte quantity);

    public void openUI(Player player, byte type) {
        try {
            Message ms = new Message(Cmd.LUCKY_ROUND);
            DataOutputStream ds = ms.writer();
            ds.writeByte(0);
            ds.writeByte(icons.size());
            for (int icon : icons) {
                ds.writeShort(icon);
            }
            ds.writeByte(type); // type price
            ds.writeInt(price); // price
            ds.writeShort(ticket); // id ticket
            player.sendMessage(ms);
            ms.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void result(Player player, List<Item> items) {
        try {
            Message ms = new Message(Cmd.LUCKY_ROUND);
            DataOutputStream ds = ms.writer();
            ds.writeByte(1);
            ds.writeByte(items.size());
            for (Item item : items) {
                ds.writeShort(item.template.iconID);
            }
            player.sendMessage(ms);
            ms.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void addItemToBox(Player player, List<Item> items) {
        for (Item item : items) {
            InventoryService.gI().addItemNotUpToUpQuantity(player.inventory.itemsBoxCrackBall, item);
        }
    }
}
