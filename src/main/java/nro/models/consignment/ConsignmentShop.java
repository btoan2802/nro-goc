package nro.models.consignment;

import lombok.Getter;
import nro.dialog.ConfirmDialog;
import nro.jdbc.DBService;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.NpcService;
import nro.services.Service;
import nro.services.func.Trade;
import nro.services.func.TransactionService;
import nro.utils.Log;
import nro.utils.Logger;
import nro.utils.Util;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author outcast c-cute hột me 😳
 */
public class ConsignmentShop {

    private static final ConsignmentShop INSTANCE = new ConsignmentShop();

    private static final byte CONSIGN = 0;

    private static final byte CCANCEL_CONSIGN = 1;

    private static final byte GET_MONEY = 2;

    private static final byte BUY = 3;

    private static final byte NEXT_PAGE = 4;

    private static final byte UP_TOP = 5;

    private static final ReentrantLock lock = new ReentrantLock();
    private BlockingQueue<PlayerConsignRequest> requestQueue = new ArrayBlockingQueue<>(100);

    public static ConsignmentShop getInstance() {
        return INSTANCE;
    }

    @Getter
    private List<ConsignmentItem> list = new ArrayList<>();

    private Map<Long, ConsignmentItem> mapItemsExpired = new HashMap<>();

    private String[] tabName = {"Trang bị", "Phụ kiện", "Hỗ trợ", "Linh tinh", ""};

    private void addToQueue(PlayerConsignRequest request) {
        requestQueue.add(request); // Thêm yêu cầu vào hàng đợi
    }

    public void handler(Player player, Message m) throws Exception {
        if (player == null || m == null) {
            return; // Or handle this case according to your application's logic
        }

        if (!Util.canDoWithTime(player.lastTimeDelay, 500)) {
            Service.getInstance().sendThongBao(player, "Thao tác quá nhanh");
            return;
        }

        player.lastTimeDelay = System.currentTimeMillis();

        DataInputStream dis = m.reader();
        byte action = dis.readByte();
        PlayerConsignRequest request = null;

        try {
            short itemID = -1;
            byte monneyType = -1;
            int money = -1;
            int quantity = -1;

            switch (action) {
                case CONSIGN: {
                    itemID = dis.readShort();
                    monneyType = dis.readByte();
                    money = dis.readInt();
                    if (player.isVersionAbove(222)) {
                        quantity = dis.readInt();
                    } else {
                        quantity = dis.readByte();
                    }
                    if (quantity > 0) {
                        request = new PlayerConsignRequest(player, action, itemID, monneyType, money, quantity);
                    }
                    break;
                }
                case BUY:
                case GET_MONEY:
                case CCANCEL_CONSIGN: {
                    itemID = dis.readShort();
                    request = new PlayerConsignRequest(player, action, itemID, monneyType, money, quantity);
                    break;
                }
                case NEXT_PAGE: {
                    byte tab = dis.readByte();
                    byte page = dis.readByte();
                    nextPage(player, tab, page);
                    break;
                }
                case UP_TOP: {
                    itemID = dis.readShort();
                    upTop(player, itemID);
                    break;
                }
                default:
                    // Handle unsupported action if needed
                    break;
            }

            if (request != null) {
                addToQueue(request);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the IOException appropriately
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log other exceptions appropriately
        }
    }

    public void processRequests() throws Exception {
        while (true) {
            if (!requestQueue.isEmpty()) {
                PlayerConsignRequest request = requestQueue.poll(); // Lấy yêu cầu từ hàng đợi
                if (request != null) {
                    processRequest(request); // Xử lý yêu cầu
                }
            } else {
                Thread.sleep(300); // Nghỉ 1 giây trước khi kiểm tra lại hàng đợi
            }
        }
    }

    private void processRequest(PlayerConsignRequest request) throws Exception {
        Player player = request.getPlayer();
        boolean locked = lock.tryLock(); // Thử khóa
        if (!locked) {
            // Xử lý khi không thể khóa
            return;
        }
        try {
            if (player != null) {
                byte action = request.getActionID();
                short itemID = request.getItemID();
                byte monneyType = request.getMonneyType();
                int money = request.getMoney();
                int quantity = request.getQuantity();
                switch (action) {
                    case CONSIGN: {
                        if (quantity > 0) {
                            consign(player, itemID, monneyType, money, quantity);
                        }
                    }
                    break;
                    case BUY: {
                        buy(player, itemID, monneyType);
                    }
                    break;
                    case GET_MONEY: {
                        getMoney(player, itemID);
                    }
                    break;
                    case CCANCEL_CONSIGN: {
                        cancelConsign(player, itemID);
                    }
                    break;
                    case NEXT_PAGE: {
                        // Đã ký rồi
                    }
                    break;
                    case UP_TOP: {
                        upTop(player, itemID);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // closeResources(rs, ps, con);
            lock.unlock(); // Mở khóa

            // Sau khi xử lý xong, xóa yêu cầu ra khỏi hàng đợi
            requestQueue.remove(request);
        }
    }

    public ConsignmentItem getItemBuy(int id) { // lấy item trong shop
        for (ConsignmentItem it : list) {
            if (it != null && it.getIdOrder() == id) {
                return it;
            }
        }
        return null;
    }

    public ConsignmentItem getItemBuy(Player pl, int id) {// lấy item từ shop thuộc về layer
        for (ConsignmentItem it : list) {
            if (it != null && it.getIdOrder() == id && it.getConsignorID() == pl.id) {
                return it;
            }
        }
        return null;
    }

    private void upTop(Player player, short itemID) {
        TransactionService.gI().cancelTrade(player);
        ConfirmDialog confirmDialog = new ConfirmDialog(
                "Bạn có muốn đưa vật phẩm này của bản thân lên trang đầu?\nYêu cầu 1 thỏi vàng", () -> {
                    ConsignmentItem consignmentItem = findItemConsign(player.id, itemID);
                    if (consignmentItem == null || consignmentItem.isSold()) {
                        Service.getInstance().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
                        return;
                    }
                    if (consignmentItem.isUpTop()) {
                        Service.getInstance().sendThongBao(player, "Vật phẩm này đã up top rồi");
                        return;
                    }
                    if (ItemService.gI().SubThoiVang(player, (short) 1)) {
                        consignmentItem.setUpTop(true);
                        Service.getInstance().sendThongBao(player,
                                "Vật phẩm " + consignmentItem.template.name + " của bạn đã up top thành công");
                    } else {
                        NpcService.gI().createTutorial(player, -1, "Bạn không có đủ thỏi vàng");
                        return;
                    }

                    Service.getInstance().sendMoney(player);
                    show(player);
                });
        confirmDialog.show(player);
    }

    private void cancelConsign(Player player, short itemID) {
        TransactionService.gI().cancelTrade(player);
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            NpcService.gI().createTutorial(player, -1, "Cần 1 ô trống trong hành trang");
            return;
        }
        ConsignmentItem item = findItemConsign(player.id, itemID);

        if (item == null) {
            Service.getInstance().sendThongBao(player, "không tìm thấy vật phẩm");
            return;
        }
        if (item.isSold()) {
            Service.getInstance().sendThongBao(player, "Vật phẩm đã bán");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong hành trang");
            return;
        }
        Item itemCopy = item;
        if (removeItem(item)) {
            InventoryService.gI().addItemBag(player, itemCopy, itemCopy.quantity);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Hủy kí gửi thành công");
            insertHisKyGuiSell(player, item, 0, "Hủy");
        }
        show(player);
    }

    public void addItem(ConsignmentItem item) {
        synchronized (list) {
            list.add(item);
        }
    }

    public void addExpiredItem(ConsignmentItem item) {
        mapItemsExpired.put(item.getConsignorID(), item);
    }

    private boolean removeItem(ConsignmentItem item) {
        synchronized (list) {
            return list.remove(item);
        }
    }

    public int getMaxId() {
        try {
            if (list == null || list.isEmpty()) {
                return 0; // or throw an appropriate exception
            }

            List<Integer> id = new ArrayList<>();
            list.stream()
                    .filter(Objects::nonNull)
                    .forEach(it -> id.add(it.getIdOrder()));

            if (id.isEmpty()) {
                return 0; // or handle as appropriate for your application
            }

            return Collections.max(id);
        } catch (Exception e) {
            Log.error(ConsignmentShop.class, e, "Error finding max id");
            return 0;
        }
    }

    private void consign(Player player, short itemID, byte monneyType, int money, int quantity) {
        TransactionService.gI().cancelTrade(player);
        boolean activeKyGui = true;
        if (!activeKyGui) {
            Service.getInstance().sendThongBao(player, "Chức năng ký gửi đang tạm bảo trì, vui lòng quay lại sau");
            return;
        }
        if (quantity < 1 || quantity > 999) {
            Service.getInstance().sendThongBao(player, "Chỉ có thể kí gửi tối đa x999");
            return;
        }

        if (money < 5 || money >= 5000) {
            Service.getInstance().sendThongBao(player, "Số thỏi vàng ký gửi từ 5 đến 5000 thỏi");
            return;
        }
        if (itemID < 0 || itemID > 3000) {
            Service.getInstance().sendThongBao(player, "Có lỗi xảy ra");
            return;
        }

        Item item = ItemService.gI().copyItem(player.inventory.itemsBag.get(itemID));
        if (item == null) {
            Service.getInstance().sendThongBao(player, "Không tìm thấy vật phẩm");
            return;
        }
        if (maxLimitConsig(player.id, item.template.id)) {
            Service.getInstance().sendThongBao(player, "Bạn chỉ có thể ký gửi tối đa 5 vật phẩm giống nhau");
            return;
        }
        // Item item = InventoryService.gI().findItem(player, itemID, quantity);

        if (item.template.id == 457) {
            Service.getInstance().sendThongBao(player, "Không thể ký gửi thỏi vàng");
            return;
        }
        if (!item.canConsign()) {
            Service.getInstance().sendThongBao(player, "Không thể ký gửi vật phẩm");
            return;
        }

        if (quantity > item.quantity) {
            Service.getInstance().sendThongBao(player, "Bạn chỉ có x" + item.quantity + " "
                    + item.template.name);
            return;
        }

        if (ItemService.gI().SubThoiVang(player, (short) 1)) {

            ConsignmentItem consignmentItem = ItemService.gI().convertToConsignmentItem(item);
            // if (monneyType == 0) {
            // mặt định là thỏi vàng
            consignmentItem.setPriceGold(money);
            // } else {
            // consignmentItem.setPriceGem(money);
            // }
            InventoryService.gI().subQuantityItemsBag(player, player.inventory.itemsBag.get(itemID), quantity);
            consignmentItem.createTime = System.currentTimeMillis();
            consignmentItem.setConsignorID(player.id);
            consignmentItem.setIdOrder(getMaxId() + 1);
            consignmentItem.setTab(getTabByType(consignmentItem.template.type));
            consignmentItem.quantity = quantity;
            addItem(consignmentItem);

            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            Service.getInstance().sendThongBao(player, "Kí gửi vật phẩm thành công");
            insertHisKyGuiSell(player, item, money, "Bán");

        } else {
            NpcService.gI().createTutorial(player, -1, "Phí ký gửi vật phẩm là 1 thỏi vàng");
        }
        show(player);

    }

    private void buy(Player player, short itemID, byte monneyType) {
        TransactionService.gI().cancelTrade(player);
        if (!player.getSession().actived) {
            NpcService.gI().createTutorial(player, -1, "Hãy mở thành viên để mua vật phẩm ký gửi");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            NpcService.gI().createTutorial(player, -1, "Cần 1 ô trống trong hành trang");
            return;
        }
        ConsignmentItem item = findItemConsignCanBuy(player.id, itemID);
        if (item == null) {
            NpcService.gI().createTutorial(player, -1, "Vật phẩm không tồn tại hoặc đã có người mua");
            return;
        }
        if (item.getConsignorID() == player.id) {
            NpcService.gI().createTutorial(player, -1, "Không thể mua vật phẩm của chính mình");
            return;
        }

        if (!item.canConsign()) {
            NpcService.gI().createTutorial(player, -1, "Vật phẩm ký gửi đã lỗi, không thể mua!!");
            return;
        }
        if (item.isSold()) {
            NpcService.gI().createTutorial(player, -1, "Vật phẩm đã được bán");
            return;
        }

        // if (monneyType == 0) {
        int money = item.getPriceGold();
        if (money <= 0) {
            NpcService.gI().createTutorial(player, -1, "Có lỗi xảy ra");
            return;
        }
        if (ItemService.gI().SubThoiVang(player, (short) money)) {
            item.setSold(true);
            InventoryService.gI().addItemBag(player, item, 999);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            Service.getInstance().sendThongBao(player, "Mua vật phẩm thành công");
            insertHisKyGuiSell(player, item, money, "Mua");
        } else {
            NpcService.gI().createTutorial(player, -1, "Bạn không đủ thỏi vàng");
            return;
        }
        // player.inventory.subGold(money);
        // } else {
        // if (inventory.ruby < money) {
        // NpcService.gI().createTutorial(player, -1, "Bạn không đủ hồng ngọc");
        // return;
        // }
        // player.inventory.subRuby(money);
        // }

        show(player);
        return;

    }

    private void getMoney(Player player, short itemID) {
        TransactionService.gI().cancelTrade(player);
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            NpcService.gI().createTutorial(player, -1, "Cần 1 ô trống trong hành trang");
            return;
        }
        ConsignmentItem item = findItemConsign(player.id, itemID);
        if (item == null) {
            NpcService.gI().createTutorial(player, -1, "Vật phẩm không tồn tại");
            return;
        }

        if (item.isSold()) {
            ConsignmentItem itemCopy = item;
            if (removeItem(item)) {
                if (itemCopy.getPriceGold() > 0) {
                    int ck = 1;
                    if ((int) player.nPoint.calPercent(itemCopy.getPriceGold(), 5) >= 1) {
                        ck = (int) player.nPoint.calPercent(itemCopy.getPriceGold(), 5);
                    }
                    int quanlityTv = itemCopy.getPriceGold() - ck;
                    if (quanlityTv <= 0) {
                        quanlityTv = 1;
                    }
                    Item thoiVang = ItemService.gI().createNewItem((short) 457);
                    thoiVang.quantity = quanlityTv;
                    Logger.errorSaveHistGoldBar(player, quanlityTv, (byte) 1,
                            "Nhận ký gửi từ bán item " + itemCopy.template.name);
                    InventoryService.gI().addItemBag(player, thoiVang, 1);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player,
                            "Nhận " + quanlityTv + " thỏi vàng thành công (Phí 10%)");
                    insertHisKyGuiSell(player, itemCopy, quanlityTv, "Nhận tiền");
                    // player.inventory.gold += item.getPriceGold() - (item.getPriceGold() * 10 /
                    // 100);
                } else if (itemCopy.getPriceGem() > 0) {
                    // player.inventory.ruby += item.getPriceGem() - (item.getPriceGem() * 10 /
                    // 100);
                }
            }
            Service.getInstance().sendMoney(player);

            show(player);
            return;
        } else {
            Service.getInstance().sendThongBao(player,
                    "Vật phảm chưa được bán");
        }

    }

    private ConsignmentItem findItemConsignCanBuy(long consignerID, short itemID) {
        for (ConsignmentItem consignmentItem : list) {
            if (consignmentItem.getConsignorID() != consignerID && consignmentItem.getIdOrder() == itemID) {
                return consignmentItem;
            }
        }
        return null;
    }

    private ConsignmentItem findItemConsign(long consignerID, short itemID) {
        for (ConsignmentItem consignmentItem : list) {
            if (consignmentItem.getConsignorID() == consignerID && consignmentItem.getIdOrder() == itemID) {
                return consignmentItem;
            }
        }
        return null;
    }

    private boolean maxLimitConsig(long consignerID, short itemID) {
        // kiểm gia số lượng vật phẩm bằng với template.id truyền vào(cùng id người bán)
        int maxCount = 5;
        int quantity = 0;
        for (ConsignmentItem consignmentItem : list) {
            if (consignmentItem.getConsignorID() == consignerID && consignmentItem.template.id == itemID) {
                quantity += 1;
            }
        }

        return quantity > maxCount;
    }

    private List<ConsignmentItem> getItemConsignByTab(Player player, byte tab, int... max) {
        List<ConsignmentItem> items = new ArrayList<>();
        List<ConsignmentItem> listSort = new ArrayList<>();
        List<ConsignmentItem> listSort2 = new ArrayList<>();

        for (ConsignmentItem item : list) {
            if (item != null && item.getTab() == tab && !item.isSold()) {
                items.add(item);
            }
        }

        Collections.sort(items, (item1, item2) -> Boolean.compare(item2.template.isUpToUp, item1.template.isUpToUp));

        if (max.length == 2) {
            int startIndex = Math.min(max[0], items.size());
            int endIndex = Math.min(max[1], items.size());
            listSort.addAll(items.subList(startIndex, endIndex));
        } else if (max.length == 1) {
            int endIndex = Math.min(max[0], items.size());
            listSort.addAll(items.subList(0, endIndex));
        } else {
            listSort.addAll(items);
        }

        for (ConsignmentItem item : listSort) {
            if (item != null) {
                listSort2.add(item);
            }
        }
        return listSort2;
    }

    private List<ConsignmentItem> getItemCanConsign(Player player) {
        List<ConsignmentItem> items = new ArrayList<>();
        list.stream().filter((it) -> (it != null && it.getConsignorID() == player.id)).forEachOrdered((it) -> {
            items.add(it);
        });
        player.inventory.itemsBag.stream()
                .filter((item) -> (item.isNotNullItem() && item.canConsign()))
                .forEachOrdered((it) -> {
                    ConsignmentItem consignmentItem = ItemService.gI().convertToConsignmentItem(it);
                    consignmentItem.setIdOrder(InventoryService.gI().getIndexBag(player, it));
                    consignmentItem.setConsignorID(-1);
                    consignmentItem.setTab((byte) 4);
                    consignmentItem.setPriceGem(-1);
                    consignmentItem.setPriceGold(-1);
                    consignmentItem.setSold(false);
                    items.add(consignmentItem);
                });
        return items;
    }

    // private boolean canConsign(int type) {
    // return (type >= 0 && type < 5) || type == 12 || type == 27 || type == 29;
    // }
    public static void insertHisKyGui(Player player, ConsignmentItem item, String type) {

        String playerString = player.name + " (" + player.id + ")";
        String amountString = "Giá vàng: " + item.getPriceGold() + ", giá ngọc: "
                + item.getPriceGem();
        String itemString = "";
        Item itemKyGui = ItemService.gI().createNewItem(item.getId());
        itemString = "Vật phẩm: " + itemKyGui.template.name + ", số lượng: "
                + item.quantity;
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForKyGui();) {
            ps = con.prepareStatement("insert history_kygui"
                    + "(player, amount, item, time, type, idKyGui) "
                    + "values (?,?,?,?,?,?)");
            ps.setString(1, playerString);
            ps.setString(2, amountString);
            ps.setString(3, itemString);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setString(5, type);
            ps.setInt(6, item.getId());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
        }

        // DBService.gI().executeUpdate("insert into history_kygui values()",
        // playerString, amountString,
        // itemString, new Timestamp(System.currentTimeMillis()), type, item.id);
    }

    public static void insertHisKyGuiSell(Player player, Item item, int amount, String status) {

        String playerString = player.name + " (" + player.id + ")";
        String amountString = "Giá vàng: " + amount;
        String itemString = "Vật phẩm: " + item.template.name + ", số lượng: "
                + item.quantity;
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForKyGui();) {
            ps = con.prepareStatement("insert history_kygui"
                    + "(player, amount, item, time, type, idKyGui) "
                    + "values (?,?,?,?,?,?)");
            ps.setString(1, playerString);
            ps.setString(2, amountString);
            ps.setString(3, itemString);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setString(5, status);
            ps.setInt(6, -1);
            ps.executeUpdate();
            ps.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte getTabByType(byte type) {
        byte tab = -1;
        if (type >= 0 && type <= 2) {
            tab = 0;
        } else if ((type >= 3 && type <= 4) || type == 33) {
            tab = 1;
        } else if (type == 29) {
            tab = 2;
        } else {
            tab = 3;
        }
        return tab;
    }

    private void nextPage(Player player, byte tab, int page) {
        Message msg = new Message(-100);
        try {
            int maxPage = (byte) (list.size() / 20 > 0 ? list.size() / 20 : 1);
            DataOutputStream ds = msg.writer();
            ds.writeByte(tab);
            ds.writeByte(maxPage);
            ds.writeByte(page);
            List<ConsignmentItem> list = getItemConsignByTab(player, tab, (byte) (page * 20), (byte) (page * 20 + 20));
            for (ConsignmentItem item : list) {
                ds.writeShort(item.template.id);
                ds.writeShort(item.template.id);
                ds.writeInt(item.getPriceGold());
                ds.writeInt(item.getPriceGem());

                ds.writeByte(0);

                if (player.isVersionAbove(222)) {
                    ds.writeInt(item.quantity);
                } else {
                    ds.writeByte(item.quantity);
                }
                ds.writeByte(item.getConsignorID() == player.id ? 0 : 1); // isMe
                ds.writeByte(item.itemOptions.size());
                for (ItemOption option : item.itemOptions) {
                    ds.writeByte(option.optionTemplate.id);
                    ds.writeShort(option.param);
                }
                ds.writeByte(0);
                ds.writeByte(0);
            }
            showItemCanConsign(player, ds);
            ds.flush();
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(Player player) {
        Message msg = new Message(-44);
        try {
            int tabLength = tabName.length;
            int maxPage = (byte) (list.size() / 20 > 0 ? list.size() / 20 : 1);
            DataOutputStream ds = msg.writer();
            ds.writeByte(2);
            ds.writeByte(tabLength);
            for (byte i = 0; i < tabLength - 1; i++) {
                List<ConsignmentItem> list = getItemConsignByTab(player, i);
                ds.writeUTF(tabName[i]);
                ds.writeByte(maxPage); // max page
                ds.writeByte(list.size());
                for (ConsignmentItem item : list) {
                    ds.writeShort(item.template.id);
                    ds.writeShort(item.getIdOrder());
                    ds.writeInt(item.getPriceGold());
                    ds.writeInt(item.getPriceGem());

                    ds.writeByte(0);

                    if (player.isVersionAbove(222)) {
                        ds.writeInt(item.quantity);
                    } else {
                        ds.writeByte(item.quantity);
                    }
                    ds.writeByte(item.getConsignorID() == player.id ? 0 : 1); // isMe
                    ds.writeByte(item.itemOptions.size() + 1);
                    for (ItemOption option : item.itemOptions) {
                        ds.writeByte(option.optionTemplate.id);
                        ds.writeShort(option.param);
                    }
                    if (item.getConsignorID() == player.id) {
                        ds.writeByte((byte) 39);
                        ds.writeShort((short) 1);
                    } else {
                        ds.writeByte(231);
                        ds.writeShort((short) 1);
                    }
                    ds.writeByte(0);
                    ds.writeByte(0);
                }
            }
            showItemCanConsign(player, ds);
            ds.flush();
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showItemCanConsign(Player player, DataOutputStream ds) throws IOException {
        List<ConsignmentItem> items = getItemCanConsign(player);
        ds.writeUTF("");
        ds.writeByte(0); // max page
        ds.writeByte(items.size());

        for (ConsignmentItem item : items) {
            ds.writeShort(item.template.id);
            ds.writeShort(item.getIdOrder());
            ds.writeInt(item.getPriceGold());
            ds.writeInt(item.getPriceGem());
            if (item.getConsignorID() == -1) {
                ds.writeByte(0);
            } else if (item.isSold()) {
                ds.writeByte(2);
            } else {
                ds.writeByte(1);
            }
            if (player.isVersionAbove(222)) {
                ds.writeInt(item.quantity);
            } else {
                ds.writeByte(item.quantity);
            }
            ds.writeByte(item.getConsignorID() == player.id ? 0 : 1); // isMe
            ds.writeByte(item.itemOptions.size());
            for (ItemOption option : item.itemOptions) {
                ds.writeByte(option.optionTemplate.id);
                ds.writeShort(option.param);
            }
            ds.writeByte(0);
            ds.writeByte(0);
        }
    }

    public int getDaysExpried(Long createTime) {
        long now = System.currentTimeMillis();
        long elapsedTimeMillis = now - createTime;
        long elapsedDays = elapsedTimeMillis / (24 * 60 * 60 * 1000);
        return (int) elapsedDays;
    }

    public void showExpiringItems(Player player) {
        if (mapItemsExpired.containsKey(player.id)) {
            StringBuilder sb = new StringBuilder();
            sb.append("|1|Danh sách vật phẩm sắp hết hạn:\n\n");
            for (Map.Entry<Long, ConsignmentItem> entry : mapItemsExpired.entrySet()) {
                ConsignmentItem item = entry.getValue();
                sb.append("- ").append(item.template.name).append("\n");
            }
            sb.append("Vật phẩm sẽ bị xóa nếu quá hạn 2 ngày");
            NpcService.gI().createMenuConMeo(player, -1, -1, sb.toString(), "OK");
            return;
        }
        Service.getInstance().sendThongBao(player, "Không có vật phẩm nào sắp hết hạn kí gửi");
    }

    public void sendExpirationNotification(Player player) {
        if (mapItemsExpired.containsKey(player.id)) {
            Service.getInstance().sendThongBao(player, "Bạn có vật phẩm sắp hết hạn đang kí gửi tại siêu thị");
        }
    }

    private static class PlayerConsignRequest {

        private final Player player;
        private final byte actionID;
        private final short itemID;
        private final byte monneyType;
        private final int money;
        private final int quantity;

        public PlayerConsignRequest(Player player, byte actionID, short itemID, byte monneyType, int money,
                int quantity) {
            this.player = player;
            this.actionID = actionID;
            this.itemID = itemID;
            this.monneyType = monneyType;
            this.money = money;
            this.quantity = quantity;
        }

        public Player getPlayer() {
            return player;
        }

        public byte getActionID() {
            return actionID;
        }

        public short getItemID() {
            return itemID;
        }

        public byte getMonneyType() {
            return monneyType;
        }

        public int getMoney() {
            return money;
        }

        public int getQuantity() {
            return quantity;
        }
    }

}
