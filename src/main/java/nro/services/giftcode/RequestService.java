package nro.services.giftcode;

import nro.jdbc.DBService;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.server.ServerManager;
import nro.services.NpcMethod;
import nro.services.Service;

import nro.utils.Util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class RequestService {

    private static RequestService instance;
    private static final ReentrantLock lock = new ReentrantLock();
    private BlockingQueue<PlayerCodeRequest> requestQueue = new ArrayBlockingQueue<>(300);
    public static final int GIVE_GOLD_BAR = 457;
    public static final int AUTO_SAVE = 500;
    public static final int SHARE_FANPAGE = 501;
    public static final int GIVE_ITEM_SHOP_WEB = 502;

    public static RequestService gI() {
        if (instance == null) {
            instance = new RequestService();
        }
        return instance;
    }

    public void RegisterCMD(Player player, int cmd) throws Exception {

        if (!Util.canDoWithTime(player.lastTimeDelay, 300)) {
            Service.getInstance().sendThongBao(player, "Thao tác quá nhanh");
            return;
        }
        player.lastTimeDelay = System.currentTimeMillis();
        // Service.getInstance().sendThongBao(player, "Đang thực hiện");
        // Tạo yêu cầu và thêm vào hàng đợi
        try {
            Thread.sleep(500);
            if (player != null) {
                PlayerCodeRequest request = new PlayerCodeRequest(player, cmd);
                addToQueue(request);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void addToQueue(PlayerCodeRequest request) {
        requestQueue.add(request); // Thêm yêu cầu vào hàng đợi
    }

    public void processRequests() throws Exception {
        while (true) {
            if (!requestQueue.isEmpty()) {
                PlayerCodeRequest request = requestQueue.poll(); // Lấy yêu cầu từ hàng đợi
                if (request != null) {
                    processRequest(request); // Xử lý yêu cầu
                }
            } else {
                Thread.sleep(300); // Nghỉ 1 giây trước khi kiểm tra lại hàng đợi
            }
        }
    }

    private void processRequest(PlayerCodeRequest request) throws Exception {
        int cmd = request.getCmd();
        Player player = request.getPlayer();

        boolean locked = lock.tryLock(); // Thử khóa
        if (!locked) {
            // Xử lý khi không thể khóa
            return;
        }
        try {
            if (player != null) {
                switch (cmd) {
                    case GIVE_GOLD_BAR: {
                        NpcMethod.gI().NhanThoiVang(player);
                    }
                    break;
                    case AUTO_SAVE: {
                        ServerManager.gI().AutoUpdate(player);
                    }
                    break;
                    case SHARE_FANPAGE: {
                        NpcMethod.gI().ShareFanpage(player);
                    }
                    break;
                    case GIVE_ITEM_SHOP_WEB: {
                        NpcMethod.gI().ItemShopWeb(player);
                    }
                    break;
                    default:
                        Service.getInstance().sendThongBaoOK(player, "Lỗi không xác định, hãy thử lại");
                }
            }
        } catch (Exception e) {
            handleException(player, "Lỗi không xác định, hãy thử lại", e);
        } finally {
            // closeResources(rs, ps, con);
            lock.unlock(); // Mở khóa
            requestQueue.remove(request);
        }
    }

    private void handleException(Player player, String message, Exception e) {
        e.printStackTrace();
        if (player != null) {
            Service.getInstance().sendThongBaoOK(player, message);
        }

    }

    private static class PlayerCodeRequest {

        private final Player player;
        private final int CMD;

        public PlayerCodeRequest(Player player, int cmd) {
            this.player = player;
            this.CMD = cmd;
        }

        public Player getPlayer() {
            return player;
        }

        public int getCmd() {
            return CMD;
        }
    }
}
