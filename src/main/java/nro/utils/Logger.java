package nro.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import nro.jdbc.DBService;
import nro.models.player.Player;
import nro.server.SettingGame;

public class Logger {

    private static final String RESET = "\033[0m";

    private static final String RED = "\033[4;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String PURPLE = "\033[0;35m";
    private static final String BLUE = "\033[0;34m";
    private static final String YELLOW = "\u001B[33m";

    /**
     * Note: System.out.print
     */
    public static void log(String text) {
        Log.warning(text);

    }

    public static void log(String color, String text) {
        Log.warning(color + text + "\n" + RESET);
    }

    /**
     * Note: System.out.print
     */
    public static void success(String text) {
        Log.warning(GREEN + text + "\n" + RESET);
    }

    /**
     * Note: System.out.print
     */
    public static void warning(String text) {
        Log.warning(BLUE + text + "\n" + RESET);
    }

    /**
     * Note: System.out.print
     */
    public static void error(String text) {
        Log.warning(RED + text + "\n" + RESET);
    }

    public static void activeDebugMode(String text) {
        if (SettingGame.ACTIVE_DEBUG_MODE) {
            errorSaveSQL(text);
        }

    }

    public static void errorSaveSQL(String text) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForDebug();) {
            ps = con.prepareStatement("insert history_debug"
                    + "(debug, time) "
                    + "values (?,?)");
            ps.setString(1, text);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
        }
        Log.warning(RED + text + "\n" + RESET);

    }

    public static void errorSaveHistGoldBar(Player player, int valueGoldBar, byte type, String note) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForHistGoldBar();) {
            ps = con.prepareStatement("insert history_receive_goldbar"
                    + "(player_id, player_name, gold_receive, type, note, time_receive	) "
                    + "values (?,?,?,?,?,?)");
            ps.setLong(1, player.id);
            ps.setString(2, player.name);
            ps.setInt(3, valueGoldBar);
            ps.setByte(4, type);
            ps.setString(5, note);
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
        }
        // Log.warning(RED + text + "\n" + RESET);

    }

    public static void logException(Class clazz, Exception ex, String... log) {
        try {
            if (log != null && log.length > 0) {
                log(PURPLE, log[0] + "\n");
            }
            StackTraceElement stackTraceElements[] = (new Throwable()).getStackTrace();
            String nameMethod = stackTraceElements[1].getMethodName();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String detail = sw.toString();
            String[] arr = detail.split("\n");
            Logger.warning("Có lỗi tại class: ");
            Logger.error(clazz.getName());
            Logger.warning(" - tại phương thức: ");
            Logger.error(nameMethod + "\n");
            Logger.warning("Chi tiết lỗi:\n");
            for (String str : arr) {
                Logger.error(str + "\n");
            }
            Logger.log("--------------------------------------------------------\n");
        } catch (Exception e) {
        }
    }
}
