package nro.server;

import nro.attr.AttributeManager;
import nro.jdbc.DBService;
import nro.jdbc.daos.AccountDAO;
import nro.jdbc.daos.HistoryTransactionDAO;
import nro.jdbc.daos.PlayerDAO;
import nro.login.LoginSession;
import nro.manager.ConsignManager;
import nro.manager.TopManager;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.consignment.ConsignmentShop;
import nro.models.map.BigMobManager;
import nro.models.map.challenge.MartialCongressManager;
import nro.models.map.dhvt.DaiHoiService;
import nro.models.map.dungeon.DungeonManager;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.DoanhTrai;
import nro.models.player.Player;
import nro.server.io.Session;
import nro.services.ClanService;
import nro.services.MapService;
import nro.services.giftcode.GiftCodeNew;
import nro.services.giftcode.RequestService;
import nro.utils.Log;
import nro.utils.TimeUtil;
import nro.utils.Util;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import nro.manager.TournamentsManager;
import nro.models.boss.Boss;
import nro.models.map.MapTemplate;
import nro.services.ConSoMayManService;

public class ServerManager {

    public static long lastTimeADdTotalgoldBaucua;
    public static String timeStart;

    public static final Map CLIENTS = new HashMap();

    public static String NAME = "";
    public static int PORT = 14446;

    private Controller controller;

    private static ServerManager instance;

    public static ServerSocket listenSocket;
    public static boolean isRunning;

    @Getter
    private LoginSession login;
    public static boolean updateTimeLogin;
    @Getter
    @Setter
    private AttributeManager attributeManager;
    private long lastUpdateAttribute;
    @Getter
    private DungeonManager dungeonManager;

    public void init() {
        Manager.gI();
        HistoryTransactionDAO.deleteHistory();
        BossFactory.initBoss();
        this.controller = new Controller();
        if (updateTimeLogin) {
            AccountDAO.updateLastTimeLoginAllAccount();
        }
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager.gI().run();
      
    }

    public void run() {
        JFrame frame = new JFrame(Manager.SERVER_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ServerManager.gI().close(100);
                frame.dispose();
            }
        });
        ImageIcon icon = new ImageIcon("resources\\normal\\image\\2\\icon\\13220.png");
        frame.setIconImage(icon.getImage());
        JPanel panel = new panel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        new AutoMaintenance(19, 0, 0).start();
        isRunning = true;
        activeCommandLine();
        activeGame();
        activeLogin();
        autoTask();
        System.out.println("Run");
        activeServerSocket();
    }

    public void activeLogin() {
        login = new LoginSession();
        login.connect(Manager.loginHost, Manager.loginPort);
    }

    private void activeServerSocket() {
        try {
            Log.log("Start server......... Current thread: " + Thread.activeCount());
            listenSocket = new ServerSocket(PORT);
            while (isRunning) {
                try {
                    Socket sc = listenSocket.accept();
                    String ip = (((InetSocketAddress) sc.getRemoteSocketAddress()).getAddress()).toString().replace("/",
                            "");
                    if (canConnectWithIp(ip)) {
                        Session session = new Session(sc, controller, ip);
                        session.ipAddress = ip;
                    } else {
                        sc.close();
                    }
                } catch (Exception e) {
                    // Logger.logException(ServerManager.class, e);
                }
            }
            listenSocket.close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e, "Lỗi mở port");
            System.exit(0);
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    public void disconnect(Session session) {
        Object o = CLIENTS.get(session.ipAddress);
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.ipAddress, n);
        }
    }

    private void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("baotri")) {
                    new Thread(() -> {
                        Maintenance.gI().start(5);
                        Log.error("Bao tri sau 5s");
                    }).start();
                } else if (line.equals("baotri2")) {
                    new Thread(() -> {
                        Maintenance.gI().start(90);
                        Log.error("Bao tri sau 90s");
                    }).start();
                } else if (line.equals("saveclan")) {
                    new Thread(() -> {
                        ClanService.gI().close();
                        Log.error("Save clan oklkkkkkkkk");
                    }).start();
                } else if (line.equals("athread")) {
                    Log.error("Debug server: " + Thread.activeCount());
                } else if (line.equals("client")) {
                    String stx = Client.gI().show_cmd();
                    Log.error("Online: " + stx);
                } else if (line.equals("thongbao")) {
                    ServerNotify.gI().notify("Debug server: " + Thread.activeCount());
                } else if (line.equals("nplayer")) {
                    Log.error("Player in game: " + Client.gI().getPlayers().size());
                } else if (line.equals("close")) {
                    new Thread(() -> {
                        Client.gI().close();
                    }).start();
                }
            }
        }, "Active line").start();
    }

    private void activeGame() {
        long delay = 500;
        new Thread(() -> {
            while (isRunning) {
                long l1 = System.currentTimeMillis();
                BossManager.gI().updateAllBoss();
                long l2 = System.currentTimeMillis() - l1;
                if (l2 < delay) {
                    try {
                        Thread.sleep(delay - l2);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "Update boss").start();
        new Thread(() -> {
            while (isRunning) {
                long l1 = System.currentTimeMillis();
                nro.services.func.Chonaiday.gI().run();
                long l2 = System.currentTimeMillis() - l1;
                if (l2 < delay) {
                    try {
                        Thread.sleep(delay - l2);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "Update BAUCUA").start();
        new Thread(() -> {
            while (isRunning) {
                BossManager.gI().SendTbaoBosss();
                try {
                    Thread.sleep((5 * 60000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Update TbaoBoss").start();

        new Thread(() -> {
            while (isRunning) {
                long start = System.currentTimeMillis();
                for (DoanhTrai dt : DoanhTrai.DOANH_TRAIS) {
                    dt.update();
                }
                for (BanDoKhoBau bdkb : BanDoKhoBau.BAN_DO_KHO_BAUS) {
                    bdkb.update();
                }
                long timeUpdate = System.currentTimeMillis() - start;
                // System.out.println("time update all boss: " + timeUpdate);
                if (timeUpdate < delay) {
                    try {
                        Thread.sleep(delay - timeUpdate);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "Update pho ban").start();
//        new Thread(() -> {
//            while (isRunning) {
//                try {
//                    long start = System.currentTimeMillis();
//                    if (attributeManager != null) {
//                        attributeManager.update();
//                        if (Util.canDoWithTime(lastUpdateAttribute, 600000)) {
//                            Manager.gI().updateEventCount();
//                        }
//                    }
//                    long timeUpdate = System.currentTimeMillis() - start;
//                    if (timeUpdate < delay) {
//                        Thread.sleep(delay - timeUpdate);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "Update Event Count").start();
        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    if (attributeManager != null) {
                        attributeManager.update();
                        if (Util.canDoWithTime(lastUpdateAttribute, 600000)) {
                            Manager.gI().updateAttributeServer();
                        }
                    }
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update Attribute Server").start();
        dungeonManager = new DungeonManager();
        dungeonManager.start();
        new Thread(dungeonManager, "Phó bản").start();
        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    MartialCongressManager.gI().update();
                    TournamentsManager.gI().update();
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update dai hoi vo thuat").start();
        new Thread(() -> {
            DaiHoiService.gI().initDaiHoiVoThuat();
        }, "Update DHVT").start();
        new Thread(() -> {
            try {
                GiftCodeNew.gI().processRequests();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }, "Update GifCode").start();
        new Thread(() -> {
            try {
                RequestService.gI().processRequests();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }, "Update Request").start();
        new Thread(() -> {
            try {
                ConsignmentShop.getInstance().processRequests();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }, "Update ConsignmentShop").start();

    }

    public void close(long delay) {
        try {
            dungeonManager.shutdown();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            Manager.gI().updateEventCount();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            Manager.gI().updateAttributeServer();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            Client.gI().close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }

        // try {
        // KyGuiManager.gI().close();
        // } catch (Exception e) {
        // Log.error(ServerManager.class, e);
        // }
        try {
            ConsignManager.getInstance().save();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            ClanService.gI().close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        Client.gI().close();
        Log.success("SUCCESSFULLY MAINTENANCE!...................................");
        System.exit(0);
    }

    public void saveAll(boolean updateTimeLogout) {
        try {
            List<Player> list = Client.gI().getPlayers();
            Connection conn = DBService.gI().getConnectionForAutoSave();
            for (Player player : list) {
                try {
                    PlayerDAO.updateTimeLogout = updateTimeLogout;
                    PlayerDAO.updatePlayer(player, conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void AutoUpdate(Player player) {

        try {
            if (player != null) {
                Connection conn = DBService.gI().getConnectionAutoSavePlayer();
                PlayerDAO.updateTimeLogout = false;
                PlayerDAO.updatePlayer(player, conn);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void autoTask() {
        // System.out.println("auto save delay: " + delay + "ms");
        // ScheduledExecutorService autoSave = Executors.newScheduledThreadPool(1);
        // autoSave.scheduleWithFixedDelay(() -> {
        // saveAll(false);
        // }, 300000, 300000, TimeUnit.MILLISECONDS);

        ScheduledExecutorService autoTopPower = Executors.newScheduledThreadPool(1);
        autoTopPower.scheduleWithFixedDelay(() -> {
            //TopManager.getInstance().loadTopThoiVang();

            TopManager.getInstance().load();
            TopManager.getInstance().loadTopGapthu();
//            TopManager.getInstance().loadtopboss();
            // TopManager.getInstance().loadTopRuongBau();
            TopManager.getInstance().loadTopBaucuA();
           TopManager.getInstance().loadtopEvent();     
//            BigMobManager.getInstance().createBigMob();

//            ConsignManager.getInstance().save();
            TopManager.getInstance().loadTopNV();
            TopManager.getInstance().loadTopSK();
            // ClanService.gI().close();
        }, 0, 600000, TimeUnit.MILLISECONDS);

        ConSoMayManService.gI().activate(1000);

        ScheduledExecutorService autoBoss = Executors.newScheduledThreadPool(1);
        autoBoss.scheduleWithFixedDelay(() -> {
            BossFactory.initBossFideGold();
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }
}
