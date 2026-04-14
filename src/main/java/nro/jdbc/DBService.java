package nro.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class DBService {

    public static String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static String URL = "jdbc:mysql://localhost:3306/nro_goc";
    public static String DB_HOST = "localhost";
    public static int DB_PORT = 3306;
    public static String DB_NAME = "nro_goc";
    public static String DB_USER = "btoan";
    public static String DB_PASSWORD = "toanhotface";
    public static int MAX_CONN = 5;
    private static final Connection[] connections = new Connection[20];

    private static DBService i;
    public static String dbName;

    private ConnPool connPool;

    public static DBService gI() {
        if (i == null) {
            i = new DBService();
        }
        return i;
    }

    private DBService() {
        this.connPool = ConnPool.gI();
    }

    public synchronized Connection getConnectionForLogin() throws SQLException {
        if (connections[0] != null) {
            if (!connections[0].isValid(10)) {
                connections[0].close();
            }
        }
        if (connections[0] == null || connections[0].isClosed()) {
            try {
                connections[0] = getConnection();
                return getConnectionForLogin();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[0];
    }

    public synchronized Connection getConnectionForLogout() throws SQLException {
        if (connections[1] != null) {
            if (!connections[1].isValid(10)) {
                connections[1].close();
            }
        }
        if (connections[1] == null || connections[1].isClosed()) {
            try {
                connections[1] = getConnection();
                return getConnectionForLogout();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[1];
    }

    public synchronized Connection getConnectionForSaveData() throws SQLException {
        if (connections[2] != null) {
            if (!connections[2].isValid(10)) {
                connections[2].close();
            }
        }
        if (connections[2] == null || connections[2].isClosed()) {
            try {
                connections[2] = getConnection();
                return getConnectionForSaveData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[2];
    }

    public synchronized Connection getConnectionForGame() throws SQLException {
        if (connections[3] != null) {
            if (!connections[3].isValid(10)) {
                connections[3].close();
            }
        }
        if (connections[3] == null || connections[3].isClosed()) {
            try {
                connections[3] = getConnection();
                return getConnectionForGame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[3];
    }

    public Connection getConnectionForClan() throws SQLException {
        if (connections[4] != null) {
            if (!connections[4].isValid(10)) {
                connections[4].close();
            }
        }
        if (connections[4] == null || connections[4].isClosed()) {
            try {
                connections[4] = getConnection();
                return getConnectionForClan();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[4];
    }

    public Connection getConnectionForAutoSave() throws SQLException {
        if (connections[5] != null) {
            if (!connections[5].isValid(10)) {
                connections[5].close();
            }
        }
        if (connections[5] == null || connections[5].isClosed()) {
            try {
                connections[5] = getConnection();
                return getConnectionForAutoSave();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[5];
    }

    public Connection getConnectionForSaveHistory() throws SQLException {
        if (connections[6] != null) {
            if (!connections[6].isValid(10)) {
                connections[6].close();
            }
        }
        if (connections[6] == null || connections[6].isClosed()) {
            try {
                connections[6] = getConnection();
                return getConnectionForSaveHistory();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[6];
    }

    public Connection getConnectionForGetPlayer() throws SQLException {
        if (connections[7] != null) {
            if (!connections[7].isValid(10)) {
                connections[7].close();
            }
        }
        if (connections[7] == null || connections[7].isClosed()) {
            try {
                connections[7] = getConnection();
                return getConnectionForGetPlayer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[7];
    }

    public Connection getConnectionCreatPlayer() throws SQLException {
        if (connections[8] != null) {
            if (!connections[8].isValid(10)) {
                connections[8].close();
            }
        }
        if (connections[8] == null || connections[8].isClosed()) {
            try {
                connections[8] = getConnection();
                return getConnectionCreatPlayer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[8];
    }

    public Connection getConnectionForKyGui() throws SQLException {
        if (connections[9] != null) {
            if (!connections[9].isValid(10)) {
                connections[9].close();
            }
        }
        if (connections[9] == null || connections[9].isClosed()) {
            try {
                connections[9] = getConnection();
                return getConnectionForKyGui();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[9];
    }

    public Connection getConnectionForTop() throws SQLException {
        if (connections[10] != null) {
            if (!connections[10].isValid(10)) {
                connections[10].close();
            }
        }
        if (connections[10] == null || connections[10].isClosed()) {
            try {
                connections[10] = getConnection();
                return getConnectionForTop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[10];
    }

    public Connection getConnectionForCode() throws SQLException {
        if (connections[11] != null) {
            if (!connections[11].isValid(10)) {
                connections[11].close();
            }
        }
        if (connections[11] == null || connections[11].isClosed()) {
            try {
                connections[11] = getConnection();
                return getConnectionForCode();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[11];
    }

    public Connection getConnectionForHisCode() throws SQLException {
        if (connections[12] != null) {
            if (!connections[12].isValid(10)) {
                connections[12].close();
            }
        }
        if (connections[12] == null || connections[12].isClosed()) {
            try {
                connections[12] = getConnection();
                return getConnectionForHisCode();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[12];
    }

    public Connection getConnectionForShareFanpage() throws SQLException {
        if (connections[13] != null) {
            if (!connections[13].isValid(10)) {
                connections[13].close();
            }
        }
        if (connections[13] == null || connections[13].isClosed()) {
            try {
                connections[13] = getConnection();
                return getConnectionForShareFanpage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[13];
    }

    public Connection getConnectionForQuaThanhVien() throws SQLException {
        if (connections[14] != null) {
            if (!connections[14].isValid(10)) {
                connections[14].close();
            }
        }
        if (connections[14] == null || connections[14].isClosed()) {
            try {
                connections[14] = getConnection();
                return getConnectionForQuaThanhVien();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[14];
    }

    public Connection getConnectionForDebug() throws SQLException {
        if (connections[15] != null) {
            if (!connections[15].isValid(10)) {
                connections[15].close();
            }
        }
        if (connections[15] == null || connections[15].isClosed()) {
            try {
                connections[15] = getConnection();
                return getConnectionForDebug();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[15];
    }

    public Connection getConnectionForHistGoldBar() throws SQLException {
        if (connections[16] != null) {
            if (!connections[16].isValid(10)) {
                connections[16].close();
            }
        }
        if (connections[16] == null || connections[16].isClosed()) {
            try {
                connections[16] = getConnection();
                return getConnectionForHistGoldBar();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[16];
    }

    public Connection getConnectionAutoSavePlayer() throws SQLException {
        if (connections[17] != null) {
            if (!connections[17].isValid(10)) {
                connections[17].close();
            }
        }
        if (connections[17] == null || connections[17].isClosed()) {
            try {
                connections[17] = getConnection();
                return getConnectionAutoSavePlayer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[17];
    }

    public Connection getConnectionForLoadData() throws SQLException {
        if (connections[18] != null) {
            if (!connections[18].isValid(10)) {
                connections[18].close();
            }
        }
        if (connections[18] == null || connections[18].isClosed()) {
            try {
                connections[18] = getConnection();
                return getConnectionForLoadData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return connections[18];
    }

    public Connection getConnection() throws Exception {
        return DBHika.getConnection();
    }

    public void release(Connection con) {
        // this.connPool.free(con);
    }

    public int currentActive() {
        return -1;
    }

    public int currentIdle() {
        return -1;
    }

}
