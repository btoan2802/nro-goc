package nro.server;

import com.sun.management.OperatingSystemMXBean;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;
import javax.swing.Timer;

import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.NpcService;
import nro.services.Service;

public class panel extends JPanel implements ActionListener {

    // ======= Fonts & UI =======
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_SECT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_BASE = new Font("Segoe UI", Font.PLAIN, 13);

    // ======= Nút điều khiển cũ =======
    private JButton baotri, thaydoiexp, thaydoisk, chatserver, kickplayer, doitien, tileroi, tileNcap, loaddb;

    // ======= Khu trạng thái & giá trị hiện tại =======
    private final JLabel lblStatus = new JLabel("Sẵn sàng.");
    private final JLabel lblValues = new JLabel();

    // ======= Dashboard =======
    private JProgressBar cpuBar, memBar;
    private JLabel cpuLbl, memLbl, uptimeLbl, sessionsLbl, playersLbl, botsLbl, threadsLbl, serverLbl, domainLbl, expLbl, eventLbl, kmLbl, roiLbl, ncapLbl;
    private JCheckBox autoRefreshChk;
    private JSpinner intervalSpinner;
    private final Timer autoTimer;

    // ======= Online Tab =======
    private JTable onlineTable;
    private DefaultTableModel onlineModel;
    private TableRowSorter<TableModel> onlineSorter;
    private JTextField searchField;
    private JButton btnRefreshOnline, btnBuffSelected, btnAnnounceAll;

    // ======= Tabs =======
    private final JTabbedPane tabs;

    public panel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));
        setBackground(Color.WHITE);

        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(FONT_BASE);
        tabs.addTab("Điều khiển", buildControlsTab());
        tabs.addTab("Dashboard", buildDashboardTab());
        tabs.addTab("Online", buildOnlineTab());

        add(tabs, BorderLayout.CENTER);

        // Thanh trạng thái (status bar)
        JPanel status = new JPanel(new BorderLayout());
        status.setBorder(new EmptyBorder(8, 0, 0, 0));
        status.setBackground(Color.WHITE);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        JLabel dot = new JLabel("\u25CF"); // chấm tròn
        dot.setForeground(new Color(0, 153, 51));
        dot.setFont(new Font("Dialog", Font.BOLD, 12));
        lblStatus.setFont(FONT_BASE.deriveFont(Font.ITALIC));
        left.add(dot);
        left.add(lblStatus);

        status.add(left, BorderLayout.WEST);
        add(status, BorderLayout.SOUTH);

        // Khởi tạo & refresh đầu
        refreshValues();
        refreshDashboard();
        refreshOnlineTable();

        // auto refresh dashboard
        autoTimer = new Timer(2000, e -> refreshDashboard());
        autoTimer.setRepeats(true);
        autoTimer.start();
    }

    // ===================== TAB 1: ĐIỀU KHIỂN =====================
    private JPanel buildControlsTab() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(Color.WHITE);

        // tiêu đề
        JLabel title = new JLabel("BẢNG ĐIỀU KHIỂN MÁY CHỦ", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        root.add(title, BorderLayout.NORTH);

        // lưới nút
        JPanel grid = new JPanel(new GridLayout(4, 2, 10, 10));
        grid.setBorder(new EmptyBorder(4, 0, 0, 0));
        grid.setBackground(Color.WHITE);

        baotri = createButton("Bảo trì máy chủ", "Đưa máy chủ vào bảo trì X giây");
        thaydoiexp = createButton("Đổi EXP server", "Đặt hệ số EXP");
        thaydoisk = createButton("Thiết lập sự kiện", "Đặt mã sự kiện");
        chatserver = createButton("Thông báo server", "Gửi thông báo đến tất cả người chơi");
        kickplayer = createButton("Đá tất cả người chơi", "Ngắt kết nối toàn bộ client");
        doitien = createButton("Khuyến mãi nạp", "Đặt hệ số khuyến mãi đổi tiền");
        tileroi = createButton("Tỉ lệ rơi toàn server", "Đặt tỉ lệ rơi dạng a/b");
        tileNcap = createButton("Tỉ lệ nâng cấp đồ", "Đặt hệ số nâng cấp (>= 0)");
        loaddb = createButton("Load Shop", "Nạp lại dữ liệu database (map, item, shop, ...)");
        grid.add(baotri);
        grid.add(thaydoiexp);
        grid.add(thaydoisk);
        grid.add(chatserver);
        grid.add(kickplayer);
        grid.add(doitien);
        grid.add(tileroi);
        grid.add(tileNcap);
        grid.add(loaddb);

        root.add(grid, BorderLayout.CENTER);

        // khu hiển thị giá trị hiện tại
        JPanel valuesWrap = new JPanel(new BorderLayout());
        valuesWrap.setBackground(new Color(250, 250, 250));
        valuesWrap.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(6, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230)),
                        new EmptyBorder(10, 12, 10, 12)
                )
        ));

        JLabel cap = new JLabel("Giá trị hiện tại");
        cap.setFont(FONT_SECT);
        lblValues.setFont(FONT_BASE);

        valuesWrap.add(cap, BorderLayout.NORTH);
        valuesWrap.add(lblValues, BorderLayout.CENTER);
        root.add(valuesWrap, BorderLayout.SOUTH);

        return root;
    }

    private JButton createButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(FONT_SECT);
        button.setFocusPainted(false);
        button.setBackground(new Color(245, 247, 250));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 236)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        button.setToolTipText(tooltip);
        return button;
    }

    private void refreshValues() {
        String html = "<html>"
                + "<b>EXP:</b> x" + Manager.RATE_EXP_SERVER
                + " &nbsp; | &nbsp; <b>Sự kiện:</b> " + Manager.EVENT_SEVER
//                + " &nbsp; | &nbsp; <b>K.mãi nạp:</b> x" + Manager.KHUYEN_MAI_NAP
//                + "<br><b>Tỉ lệ rơi:</b> " + Manager.TILE_ROI_A + "/" + Manager.TILE_ROI_B
//                + " &nbsp; | &nbsp; <b>Tỉ lệ nâng cấp:</b> x" + Manager.TILE_NCAP
                + "</html>";
        lblValues.setText(html);
    }

    // ===================== TAB 2: DASHBOARD =====================
    private JPanel buildDashboardTab() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(4, 4, 4, 4));
        root.setBackground(Color.WHITE);

        JPanel head = new JPanel(new GridLayout(2, 1));
        head.setOpaque(false);
        serverLbl = new JLabel("Server: " + Manager.SERVER_NAME + "  |  SV: " + Manager.SERVER + "  |  Port: " + ServerManager.PORT);
        domainLbl = new JLabel("Domain: " + Manager.DOMAIN + "  |  Bắt đầu: " + ServerManager.timeStart);
        serverLbl.setFont(FONT_BASE);
        domainLbl.setFont(FONT_BASE);
        head.add(serverLbl);
        head.add(domainLbl);
        root.add(head, BorderLayout.NORTH);

        JPanel mid = new JPanel(new GridLayout(2, 1, 10, 10));
        mid.setOpaque(false);

        // dòng 1: CPU/MEM bars
        JPanel bars = new JPanel(new GridLayout(2, 1, 8, 8));
        bars.setOpaque(false);

        JPanel cpuRow = new JPanel(new BorderLayout(8, 4));
        cpuRow.setOpaque(false);
        cpuLbl = new JLabel("CPU: --");
        cpuLbl.setFont(FONT_BASE);
        cpuBar = new JProgressBar(0, 100);
        cpuBar.setStringPainted(true);
        cpuRow.add(cpuLbl, BorderLayout.WEST);
        cpuRow.add(cpuBar, BorderLayout.CENTER);

        JPanel memRow = new JPanel(new BorderLayout(8, 4));
        memRow.setOpaque(false);
        memLbl = new JLabel("RAM: --");
        memLbl.setFont(FONT_BASE);
        memBar = new JProgressBar(0, 100);
        memBar.setStringPainted(true);
        memRow.add(memLbl, BorderLayout.WEST);
        memRow.add(memBar, BorderLayout.CENTER);

        bars.add(cpuRow);
        bars.add(memRow);
        mid.add(bars);

        // dòng 2: các chỉ số
        JPanel stats = new JPanel(new GridLayout(3, 4, 10, 6));
        stats.setOpaque(false);
        sessionsLbl = new JLabel("Sessions: --");
        playersLbl = new JLabel("Online: --");
        botsLbl = new JLabel("Bots: --");
        threadsLbl = new JLabel("Threads: --");
        uptimeLbl = new JLabel("Uptime: " );
        expLbl = new JLabel("EXP: x" + Manager.RATE_EXP_SERVER);
        eventLbl = new JLabel("Sự kiện: " + Manager.EVENT_SEVER);
        kmLbl = new JLabel("KM Nạp: x" );
        roiLbl = new JLabel("Rơi: ");
        ncapLbl = new JLabel("Nâng cấp: x" );

        JLabel[] arr = {sessionsLbl, playersLbl, botsLbl, threadsLbl, uptimeLbl, expLbl, eventLbl, kmLbl, roiLbl, ncapLbl};
        for (JLabel l : arr) {
            l.setFont(FONT_BASE);
            stats.add(l);
        }
        // chèn placeholder để cân lưới 3x4 (10 mục + 2 khoảng trống)
        stats.add(new JLabel(""));
        stats.add(new JLabel(""));

        mid.add(stats);
        root.add(mid, BorderLayout.CENTER);

        // đáy: điều khiển refresh
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottom.setOpaque(false);
        JButton refreshBtn = new JButton("Làm mới ngay");
        refreshBtn.setFont(FONT_BASE);
        refreshBtn.addActionListener(e -> refreshDashboard());

        autoRefreshChk = new JCheckBox("Tự động làm mới");
        autoRefreshChk.setSelected(true);
        autoRefreshChk.setFont(FONT_BASE);

        intervalSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 60, 1));
        ((JSpinner.DefaultEditor) intervalSpinner.getEditor()).getTextField().setColumns(3);
        intervalSpinner.setFont(FONT_BASE);
        intervalSpinner.addChangeListener(e -> {
            int ms = ((Integer) intervalSpinner.getValue()) * 1000;
            autoTimer.setDelay(ms);
        });
        autoRefreshChk.addActionListener(e -> {
            boolean on = autoRefreshChk.isSelected();
            if (on && !autoTimer.isRunning()) {
                autoTimer.start();
            }
            if (!on && autoTimer.isRunning()) {
                autoTimer.stop();
            }
        });

        bottom.add(refreshBtn);
        bottom.add(new JLabel("Chu kỳ (giây):"));
        bottom.add(intervalSpinner);
        bottom.add(autoRefreshChk);

        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private void refreshDashboard() {
        DecimalFormat pct = new DecimalFormat("0.0");

        // CPU/RAM
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            double cpu = osBean.getSystemCpuLoad();               // 0..1
            long totalMem = osBean.getTotalPhysicalMemorySize();
            long freeMem = osBean.getFreePhysicalMemorySize();
            long usedMem = totalMem - freeMem;
            double mem = (totalMem == 0) ? 0 : (usedMem * 100.0 / totalMem);

            int cpuInt = (int) Math.round(Math.max(0, Math.min(100, cpu * 100.0)));
            int memInt = (int) Math.round(Math.max(0, Math.min(100, mem)));

            cpuBar.setValue(cpuInt);
            cpuBar.setString(cpuInt + "%");
            cpuLbl.setText("CPU: " + pct.format(cpu * 100) + "%");

            memBar.setValue(memInt);
            memBar.setString(memInt + "%");
            memLbl.setText("RAM: " + pct.format(mem) + "%");
        } catch (Throwable t) {
            cpuLbl.setText("CPU: n/a");
            memLbl.setText("RAM: n/a");
        }

        // Chỉ số
        try {
            int sessions = Client.gI().getSessions().size();
            int players = Client.gI().getPlayers().size();
            int bots = 0;
            try {
                // bots = Client.gI().bots.size();
            } catch (Throwable ignored) {
            }
            int threads = ManagementFactory.getThreadMXBean().getThreadCount();

            sessionsLbl.setText("Sessions: " + sessions);
            playersLbl.setText("Online: " + players);
            botsLbl.setText("Bots: " + bots);
            threadsLbl.setText("Threads: " + threads);
            uptimeLbl.setText("Uptime: ");

            expLbl.setText("EXP: x" + Manager.RATE_EXP_SERVER);
            eventLbl.setText("Sự kiện: " + Manager.EVENT_SEVER);
            kmLbl.setText("KM Nạp: x" );
            roiLbl.setText("Rơi: ");
            ncapLbl.setText("Nâng cấp: x" );
        } catch (Throwable ignored) {
        }
    }

    // ===================== TAB 3: ONLINE =====================
    private JPanel buildOnlineTab() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(Color.WHITE);

        // Top bar: search + actions
        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setOpaque(false);

        JPanel searchWrap = new JPanel(new BorderLayout(6, 6));
        searchWrap.setOpaque(false);
        JLabel searchLbl = new JLabel("Tìm người chơi (lọc live): ");
        searchLbl.setFont(FONT_BASE);
        searchField = new JTextField();
        searchField.setFont(FONT_BASE);
        searchField.putClientProperty("JTextField.placeholderText", "Nhập một phần tên...");
        searchWrap.add(searchLbl, BorderLayout.WEST);
        searchWrap.add(searchField, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        btnRefreshOnline = new JButton("Làm mới");
        btnRefreshOnline.setFont(FONT_BASE);
        btnBuffSelected = new JButton("Buff đồ (đã chọn)");
        btnBuffSelected.setFont(FONT_BASE);
        btnAnnounceAll = new JButton("Thông báo toàn server");
        btnAnnounceAll.setFont(FONT_BASE);

        actions.add(btnRefreshOnline);
        actions.add(btnBuffSelected);
        actions.add(btnAnnounceAll);

        top.add(searchWrap, BorderLayout.CENTER);
        top.add(actions, BorderLayout.EAST);
        root.add(top, BorderLayout.NORTH);

        // Table model
        onlineModel = new DefaultTableModel(new Object[]{
            "ID", "Tên", "SM", "Map", "Khu", "HP%", "Hành tinh", "Trạng thái"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // giúp sorter hiểu kiểu số
                switch (columnIndex) {
                    case 0:
                        return Integer.class; // ID
                    case 2:
                        return String.class;  // SM hiển thị đã format
                    case 4:
                        return Integer.class; // Khu
                    default:
                        return String.class;
                }
            }
        };

        onlineTable = new JTable(onlineModel);
        onlineTable.setFont(FONT_BASE);
        onlineTable.getTableHeader().setFont(FONT_BASE.deriveFont(Font.BOLD));
        onlineTable.setRowHeight(24);
        onlineTable.setFillsViewportHeight(true);
        onlineTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        onlineTable.setAutoCreateRowSorter(true);

        onlineSorter = new TableRowSorter<>(onlineModel);
        onlineTable.setRowSorter(onlineSorter);

        // Renderers canh lề
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.CENTER);
        center.setHorizontalAlignment(SwingConstants.CENTER);
        right.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumnModel cm = onlineTable.getColumnModel();
        cm.getColumn(0).setPreferredWidth(60);   // ID
        cm.getColumn(1).setPreferredWidth(160);  // Tên
        cm.getColumn(2).setPreferredWidth(120);  // SM
        cm.getColumn(3).setPreferredWidth(140);  // Map
        cm.getColumn(4).setPreferredWidth(60);   // Khu
        cm.getColumn(5).setPreferredWidth(60);   // HP%
        cm.getColumn(6).setPreferredWidth(90);   // Hành tinh
        cm.getColumn(7).setPreferredWidth(100);  // Trạng thái

        cm.getColumn(0).setCellRenderer(right);   // ID
        cm.getColumn(1).setCellRenderer(left);    // Tên
        cm.getColumn(2).setCellRenderer(right);   // SM (dù là String đã format)
        cm.getColumn(3).setCellRenderer(left);    // Map
        cm.getColumn(4).setCellRenderer(right);   // Khu
        cm.getColumn(5).setCellRenderer(center);  // HP%
        cm.getColumn(6).setCellRenderer(center);  // Hành tinh
        cm.getColumn(7).setCellRenderer(center);  // Trạng thái

        JScrollPane scroll = new JScrollPane(onlineTable);
        scroll.getViewport().setBackground(Color.WHITE);
        root.add(scroll, BorderLayout.CENTER);

        // events
        btnRefreshOnline.addActionListener(e -> refreshOnlineTable());
        btnBuffSelected.addActionListener(e -> onBuffSelected());
        btnAnnounceAll.addActionListener(e -> onThongBao());

        // live filter cho search
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String text = searchField.getText();
                if (text == null || text.trim().isEmpty()) {
                    onlineSorter.setRowFilter(null);
                } else {
                    onlineSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text.trim())));
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }
        });

        // popup menu
        JPopupMenu menu = new JPopupMenu();
        JMenuItem copyName = new JMenuItem("Copy tên");
        copyName.addActionListener(e -> copySelectedName());
        menu.add(copyName);
        onlineTable.setComponentPopupMenu(menu);

        return root;
    }

    private void refreshOnlineTable() {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));

        onlineModel.setRowCount(0);
        List<Player> list = Client.gI().getPlayers();
        for (Player pl : list) {
            if (pl == null) {
                continue;
            }

            String name = pl.name == null ? "" : pl.name;

            String mapName = "";
            Integer zoneId = null;
            String hpPct = "";
            try {
                if (pl.zone != null && pl.zone.map != null) {
                    mapName = pl.zone.map.mapName;
                    zoneId = pl.zone.zoneId;
                }
                long hp = (long) pl.nPoint.hp;
                long hpMax = (long) Math.max(1, pl.nPoint.hpMax);
                int pct = (int) Math.max(0, Math.min(100, Math.round(hp * 100.0 / hpMax)));
                hpPct = pct + "%";
            } catch (Throwable ignored) {
            }

            String planet = getPlanetName(pl.gender);
            // String status = pl.isAdmin() ? "Admin" : (pl.isBot ? "Bot" : "Người chơi");
            String status = pl.isAdmin() ? "Admin" : "Người chơi";

            // SM hiển thị đẹp (ngăn cách nghìn)
            String sm = nf.format(pl.nPoint.power);

            onlineModel.addRow(new Object[]{
                (int) pl.id,
                name,
                sm,
                mapName,
                zoneId == null ? -1 : zoneId,
                hpPct,
                planet,
                status
            });
        }

        // Chọn sort mặc định: Online theo tên
        if (onlineTable.getRowSorter() != null) {
            List<RowSorter.SortKey> keys = new ArrayList<>();
            keys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
            onlineTable.getRowSorter().setSortKeys(keys);
        }

        setStatus("Đã tải danh sách online: " + onlineModel.getRowCount());
    }

    // ===================== BUFF ITEM (đa chọn) =====================
    private void onBuffSelected() {
        int[] rows = onlineTable.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Chọn ít nhất 1 người chơi trong bảng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // ==== Panel nhập thông tin buff (hướng dẫn format) ====
        JTextField idItemField = new JTextField();
        JTextField optionField = new JTextField();
        JSpinner amountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));

        JLabel hint = new JLabel("<html>"
                + "Options (tuỳ chọn): dùng <b>-</b>, <b>:</b>, <b>=</b>, <b>x</b> giữa id–param; dùng <b>;</b>, <b>,</b>, <b>|</b> giữa các cặp.<br>"
                + "Ví dụ: <code>77-5;103:10|200=1, 300x15</code></html>");
        hint.setFont(FONT_BASE.deriveFont(11f));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setOpaque(false);
        form.add(new JLabel("ID Item:"));
        form.add(idItemField);
        form.add(new JLabel("Số lượng:"));
        form.add(amountSpinner);
        form.add(new JLabel("Options:"));
        form.add(optionField);

        JPanel wrap = new JPanel(new BorderLayout(6, 6));
        wrap.setOpaque(false);
        wrap.add(form, BorderLayout.CENTER);
        wrap.add(hint, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(
                this, wrap, "Buff Item cho người chơi đã chọn",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // ==== Validate dữ liệu vào ====
        final int idItemBuff;
        final int slItemBuff;
        try {
            String idStr = idItemField.getText() == null ? "" : idItemField.getText().trim();
            if (idStr.isEmpty()) {
                showError("Vui lòng nhập ID Item");
                return;
            }
            if (idStr.startsWith("+")) {
                idStr = idStr.substring(1);
            }
            if (idStr.startsWith("-")) {
                showError("ID Item không được âm");
                return;
            }
            idItemBuff = Integer.parseInt(idStr);
            if (idItemBuff < 0) {
                showError("ID Item phải >= 0");
                return;
            }

            slItemBuff = (Integer) amountSpinner.getValue();
            if (slItemBuff <= 0) {
                showError("Số lượng phải >= 1");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("ID Item phải là số nguyên hợp lệ");
            return;
        }

        // Parse options linh hoạt
        final List<ParsedOption> parsedOptions;
        try {
            parsedOptions = parseItemOptions(optionField.getText());
        } catch (IllegalArgumentException ex) {
            showError("Lỗi định dạng Options: " + ex.getMessage());
            return;
        }

        // ==== Thực thi buff từng người ====
        int buffed = 0;
        for (int viewRow : rows) {
            int modelRow = onlineTable.convertRowIndexToModel(viewRow);
            Object idObj = onlineModel.getValueAt(modelRow, 0);
            if (!(idObj instanceof Integer)) {
                continue;
            }

            int pid = (Integer) idObj;
            Player target = Client.gI().getPlayer(pid);
            if (target == null) {
                continue;
            }

            try {
                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff, slItemBuff);
                applyOptionsToItem(itemBuffTemplate, parsedOptions);

                InventoryService.gI().addItemBag(target, itemBuffTemplate, 99);
                InventoryService.gI().sendItemBags(target);

                String txtBuff = "Buff cho " + target.name + ": " + slItemBuff + " " + itemBuffTemplate.template.name;
                NpcService.gI().createTutorial(target, 24, txtBuff);

                buffed++;
            } catch (Exception ex) {
                Service.getInstance().sendThongBao(target, "Buff thất bại!");
            }
        }
        setStatus("Đã buff item cho " + buffed + " người chơi.");
    }

    // ===== Utils cho định dạng Option =====
    private static class ParsedOption {

        final int id;
        final int param;

        ParsedOption(int id, int param) {
            this.id = id;
            this.param = param;
        }
    }

    /**
     * Chuẩn hoá và parse chuỗi option. Hỗ trợ: - Giữa id và param: "-", ":",
     * "=", "x" (vd: 77-5, 77:5, 77=5, 77x5) - Giữa các cặp: ";", ",", "|" (vd:
     * 77-5;103-10 | 200-1, 300-15) - Bỏ qua khoảng trắng, dòng mới.
     */
    private List<ParsedOption> parseItemOptions(String raw) {
        List<ParsedOption> out = new ArrayList<>();
        if (raw == null) {
            return out;
        }

        String s = raw.trim().replace('\n', ';').replace('\r', ';');
        if (s.isEmpty()) {
            return out;
        }

        String[] pairs = s.split("[;|,]+");
        int idx = 0;
        for (String pair : pairs) {
            idx++;
            String p = pair.trim();
            if (p.isEmpty()) {
                continue;
            }

            String[] kv = p.split("[-:=xX]");
            if (kv.length != 2) {
                throw new IllegalArgumentException(
                        "Cặp #" + idx + " không đúng dạng 'id-param' (nhận - : = x). Giá trị: '" + p + "'"
                );
            }
            String kStr = kv[0].trim();
            String vStr = kv[1].trim();
            if (kStr.isEmpty() || vStr.isEmpty()) {
                throw new IllegalArgumentException(
                        "Cặp #" + idx + " thiếu id hoặc param. Giá trị: '" + p + "'"
                );
            }
            try {
                if (kStr.startsWith("+")) {
                    kStr = kStr.substring(1);
                }
                if (vStr.startsWith("+")) {
                    vStr = vStr.substring(1);
                }
                if (kStr.startsWith("-") || vStr.startsWith("-")) {
                    throw new NumberFormatException("Không cho phép số âm");
                }
                int id = Integer.parseInt(kStr);
                int param = Integer.parseInt(vStr);
                if (id < 0 || param < 0) {
                    throw new NumberFormatException("Âm");
                }
                out.add(new ParsedOption(id, param));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Cặp #" + idx + " phải là số nguyên không âm. Giá trị: '" + p + "'");
            }
        }
        return out;
    }

    private void applyOptionsToItem(Item item, List<ParsedOption> options) {
        if (item == null || options == null || options.isEmpty()) {
            return;
        }
        for (ParsedOption po : options) {
            item.itemOptions.add(new ItemOption(po.id, po.param));
        }
    }

    private void copySelectedName() {
        int row = onlineTable.getSelectedRow();
        if (row < 0) {
            return;
        }
        int model = onlineTable.convertRowIndexToModel(row);
        String name = String.valueOf(onlineModel.getValueAt(model, 1));
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new java.awt.datatransfer.StringSelection(name), null);
        setStatus("Đã copy tên: " + name);
    }

    private String getPlanetName(int gender) {
        switch (gender) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "-";
        }
    }

    // ===================== HANDLERS CHO NÚT Ở TAB ĐIỀU KHIỂN =====================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        try {
            if (src == baotri) {
                onBaoTri();
            } else if (src == thaydoiexp) {
                onDoiExp();
            } else if (src == thaydoisk) {
                onSuKien();
            } else if (src == chatserver) {
                onThongBao();
            } else if (src == kickplayer) {
                onKickAll();
            } else if (src == doitien) {
                onKhuyenMaiNap();
            } else if (src == tileroi) {
                onTiLeRoi();
            } else if (src == tileNcap) {
                onTiLeNangCap();
            } else if (src == loaddb) {
                onLoadDatabase();
            }
        } catch (Exception ex) {
            showError("Đã xảy ra lỗi: " + ex.getMessage());
        }
        refreshValues();
        refreshDashboard();
    }

    private void onBaoTri() {
        Integer seconds = askInt("Bảo trì máy chủ", "Nhập số giây bảo trì:", 3, 1, 3600);
        if (seconds == null) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận đưa máy chủ vào bảo trì trong " + seconds + " giây?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        Maintenance.gI().start(seconds);
        setStatus("Đang bảo trì trong " + seconds + " giây...");
        System.out.println("------------- TIEN HANH BAO TRI! -------------\n");
    }

    private void onDoiExp() {
        Byte exp = askByte("Bảng EXP Server",
                "EXP hiện tại: x" + Manager.RATE_EXP_SERVER + "\nNhập EXP mới (0-127):",
                Manager.RATE_EXP_SERVER);
        if (exp == null) {
            return;
        }
        Manager.RATE_EXP_SERVER = exp;
        setStatus("Đổi EXP: x" + exp);
        System.out.println("------------- TANG EXP HIEN TAI: x" + exp + " LAN -------------\n");
    }

    private void onSuKien() {
        Byte sk = askByte("Bảng Sự Kiện",
                "Sự kiện hiện tại: " + Manager.EVENT_SEVER + "\nNhập mã sự kiện (0-127):",
                (byte) Manager.EVENT_SEVER);
        if (sk == null) {
            return;
        }
        Manager.EVENT_SEVER = sk;
        setStatus("Sự kiện hiện tại: " + sk);
        System.out.println("------------- SU KIEN HIEN TAI: " + sk + " -------------\n");
    }

    /**
     * Thông báo toàn server
     */
    private void onThongBao() {
        String chat = askText("Thông Báo Server", "Nhập nội dung thông báo:", 512);
        if (chat == null || chat.isBlank()) {
            return;
        }
        Message msg = new Message(93);
        try {
            msg.writer().writeUTF(chat);
            Service.getInstance().sendMessAllPlayer(msg);
            Service.getInstance().sendBigMessAllPlayer(2126, chat);
            setStatus("Đã gửi thông báo toàn server.");
            System.out.println("------------- THONG BAO: " + chat + " -------------\n");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            showError("Gửi thông báo thất bại: " + ex.getMessage());
        } finally {
            msg.cleanup();
        }
    }

    private void onKickAll() {
        int c = JOptionPane.showConfirmDialog(this,
                "Xác nhận đá TẤT CẢ người chơi?",
                "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c != JOptionPane.YES_OPTION) {
            return;
        }
        Client.gI().close();
        setStatus("Đã gửi lệnh kick toàn bộ người chơi.");
    }

    private void onKhuyenMaiNap() {
//        Byte km = askByte("Khuyến mãi nạp",
//                "Giá trị hiện tại: x" + Manager.KHUYEN_MAI_NAP + "\nNhập hệ số mới (0-127):",
//                Manager.KHUYEN_MAI_NAP);
//        if (km == null) {
//            return;
//        }
//        Manager.KHUYEN_MAI_NAP = km;
//        setStatus("Khuyến mãi quy đổi: x" + km);
//        System.out.println("------------- KHUYEN MAI QUY DOI DANG: x" + km + " LAN -------------\n");
    }

    private void onTiLeRoi() {
//        Ratio r = askRatio("Tỉ lệ rơi Toàn Server", Manager.TILE_ROI_A, Manager.TILE_ROI_B);
//        if (r == null) {
//            return;
//        }
//        Manager.TILE_ROI_A = r.a;
//        Manager.TILE_ROI_B = r.b;
//        setStatus("Tỉ lệ rơi: " + r.a + "/" + r.b);
//        System.out.println("------------- TI LE ROI: " + r.a + "/" + r.b + " -------------\n");
    }

    private void onTiLeNangCap() {
//        Integer v = askInt("Tỉ lệ Nâng cấp đồ",
//                "Giá trị hiện tại: x" + Manager.TILE_NCAP + "\nNhập hệ số mới (>= 0):",
//                Manager.TILE_NCAP, 0, Integer.MAX_VALUE);
//        if (v == null) {
//            return;
//        }
//        Manager.TILE_NCAP = v;
//        setStatus("Tỉ lệ nâng cấp: x" + v);
//        System.out.println("------------- TI LE NANG CAP DO TOAN SERVER: " + v + " LAN -------------\n");
    }

    // ===================== Helpers UI =====================
    private void setStatus(String text) {
        lblStatus.setText(text);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private Byte askByte(String title, String message, byte preset) {
        String s = (String) JOptionPane.showInputDialog(
                this, message, title, JOptionPane.PLAIN_MESSAGE, null, null, String.valueOf(preset));
        if (s == null) {
            return null;
        }
        try {
            int v = Integer.parseInt(s.trim());
            if (v < 0 || v > 127) {
                throw new NumberFormatException("byte 0-127");
            }
            return (byte) v;
        } catch (NumberFormatException ex) {
            showError("Vui lòng nhập số nguyên hợp lệ trong khoảng 0–127.");
            return null;
        }
    }

    private Integer askInt(String title, String message, int preset, int min, int max) {
        SpinnerNumberModel model = new SpinnerNumberModel(preset, min, max, 1);
        JSpinner spinner = new JSpinner(model);
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setColumns(6);

        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.add(new JLabel(message), BorderLayout.NORTH);
        p.add(spinner, BorderLayout.CENTER);

        int r = JOptionPane.showConfirmDialog(this, p, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return null;
        }
        return (Integer) spinner.getValue();
    }

    private String askText(String title, String message, int maxLen) {
        JTextArea area = new JTextArea(5, 36);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.add(new JLabel(message), BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        int r = JOptionPane.showConfirmDialog(this, p, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return null;
        }

        String txt = area.getText();
        if (txt != null && txt.length() > maxLen) {
            showError("Vượt quá độ dài cho phép (" + maxLen + ")");
            return null;
        }
        return txt;
    }

    private Ratio askRatio(String title, int aPreset, int bPreset) {
        JSpinner a = new JSpinner(new SpinnerNumberModel(aPreset, 1, Integer.MAX_VALUE, 1));
        JSpinner b = new JSpinner(new SpinnerNumberModel(bPreset, 1, Integer.MAX_VALUE, 1));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Nhập a:"));
        row1.add(a);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Nhập b:"));
        row2.add(b);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(row1);
        container.add(row2);

        int r = JOptionPane.showConfirmDialog(this, container, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return null;
        }

        int av = (Integer) a.getValue();
        int bv = (Integer) b.getValue();
        if (bv == 0) {
            showError("b phải khác 0");
            return null;
        }

        return new Ratio(av, bv);
    }

    private static class Ratio {

        final int a, b;

        Ratio(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    private void onLoadDatabase() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận load lại toàn bộ dữ liệu từ database?\n(thao tác có thể mất vài giây)",
                "Xác nhận Load Database", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        new Thread(() -> {
            try {
                Manager.gI().ReloadShop();
//                Manager.gI().loadDatabase();
                SwingUtilities.invokeLater(() -> {
                    setStatus("Đã load lại dữ liệu từ database.");
                    JOptionPane.showMessageDialog(this, "Load database thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    refreshValues();
                    refreshDashboard();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> showError("Load database thất bại: " + ex.getMessage()));
            }
        }, "ReloadDB").start();
    }

    // ===================== MAIN (tiện test nhanh) =====================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ngọc Rồng BILL - Admin");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new Panel());
            frame.setSize(980, 680);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
