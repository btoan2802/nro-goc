package nro.server;

import lombok.Getter;
import nro.attr.Attribute;
import nro.attr.AttributeManager;
import nro.attr.AttributeTemplateManager;
import nro.card.CardManager;
import nro.consts.ConstItem;
import nro.consts.ConstMap;
import nro.consts.ConstPlayer;
import nro.data.DataGame;
import nro.event.Event;
import nro.jdbc.DBService;
import nro.jdbc.daos.AccountDAO;
import nro.jdbc.daos.ShopDAO;
import nro.lib.RandomCollection;
import nro.manager.*;
import nro.models.*;
import nro.noti.NotiManager;
import nro.power.CaptionManager;
import nro.power.PowerLimitManager;
import nro.services.ItemService;
import nro.services.MapService;
import nro.utils.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nro.models.player.Referee101;
import nro.utils.Util;

public final class SettingGame {

    private static SettingGame i;
    public static short Item_Tang_Them = -1;
    public static short ItemRac[] = {20, 20, 220, 221, 222, 223, 224, 220, 221, 222, 223, 224, 220, 221, 222, 223, 224};
    public static boolean is_up_skh = false;
    public static boolean is_option_level = false;
    public static boolean ACTIVE_DEBUG_MODE = false;
    public static byte SECOND_WAIT_LOGIN = 20;
    public static int MAX_PER_IP = 15000;
    public static String NAME_GAME = "Ngọc Rồng Hero";
    public static String LinkIp = "";
    public static String LINK_GAME = "https://ngocronghero.com";
    public static String LIST_CODE_FREE = "";
    public static short RATIO_PHA_LE_HOA = 10;
    public static short RATIO_NANG_CAP = 10;
    public static short RATIO_RAC = 5;
    public static short RATIO_DTL_COLD = 10;
    public static short RATIO_GANG_TAY = 10;

    public static int num7Sao = 1000;
    public static int num8Sao = 2000;

    @Getter
    public GameConfig gameConfig;

    public static SettingGame gI() {
        if (i == null) {
            i = new SettingGame();
        }
        return i;
    }
}
