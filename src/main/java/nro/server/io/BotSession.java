// package nro.server.io;

// import nro.data.DataGame;
// import nro.jdbc.daos.BotGk;
// import nro.jdbc.daos.GodGK;
// import nro.models.item.Item;
// import nro.models.item.ItemOption;
// import nro.models.player.Player;
// import nro.resources.Resources;
// import nro.server.*;
// import nro.server.*;
// import nro.server.model.AntiLogin;
// import nro.services.*;
// import nro.utils.Log;
// import nro.utils.Logger;
// import nro.utils.Util;
// import lombok.Setter;
// import nro.services.ItemService;
// import nro.services.ItemTimeService;
// import nro.services.Service;
// import nro.services.TaskService;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// public class BotSession {

// private static final Map<String, AntiLogin> ANTILOGIN = new HashMap<>();

// private static final int TIME_WAIT_READ_MESSAGE = 180000;

// public boolean logCheck;
// public int id;
// public Player player;
// public byte timeWait = 50;

// public boolean connected;

// static final byte[] KEYS = { 0 };
// byte curR, curW;

// Thread sendThread;
// Thread receiveThread;
// Thread doControllerThread;

// Controller controller;

// public String ipAddress;
// public boolean isAdmin;
// public int userId;
// public String uu;
// public String pp;

// public int typeClient;
// public byte zoomLevel;
// public boolean isSetClientType;

// public long lastTimeLogout;
// public boolean loginSuccess, joinedGame, dataLoadFailed;

// public long lastTimeReadMessage;

// public boolean actived;

// public int goldBar;
// public List<Item> itemsReward;
// public String dataReward;
// public int ruby;
// public int diemTichNap;
// public int server;// server account hiện tại
// public int version;
// @Setter
// private boolean logging;

// public void update() {
// if (Util.canDoWithTime(lastTimeReadMessage, TIME_WAIT_READ_MESSAGE)) {
// // Client.gI().kickSession(this);
// }
// }

// public String getName() {
// if (this.player != null) {
// return this.player.name;
// } else {
// return "Bot";
// }
// }

// // public void johnMaps(Player pl) {

// // this.player.joinMap(pl.zone, this.player);
// // }

// public void login(String username, String password) {

// logging = true;
// // AntiLogin al = ANTILOGIN.get(this.ipAddress);
// // if (al == null) {
// // al = new AntiLogin();
// // ANTILOGIN.put(this.ipAddress, al);
// // }
// // if (!al.canLogin()) {
// // Logger.warning("Lỗi đăng nhập bot");
// // return;
// // }

// if (this.player != null) {
// return;
// } else {
// Player player = null;
// try {
// this.uu = username;
// this.pp = password;
// boolean status = BotGk.login(this); // kiểm tra tài khoản mật khẩu
// if (status) {
// finishUpdate();
// } else {
// Logger.warning("Đăng nhập bot thất bại");
// }

// } catch (Exception e) {
// e.printStackTrace();
// if (player != null) {
// player.dispose();
// }
// }
// }
// }

// private void finishUpdate() {
// if (loginSuccess && !joinedGame) {
// player = BotGk.loadPlayer(this);// tải dữ liệu người chơi từ database
// if (!dataLoadFailed) {
// if (player != null) {
// enter();
// } else {
// // Service.getInstance().switchToCreateChar(this);
// }
// } else {
// Logger.warning("Lỗi tải dữ liệu bot");
// }
// }
// }

// public void enter() {
// if (!joinedGame) {
// joinedGame = true;
// player.nPoint.initPowerLimit();
// if (player.pet != null) {
// player.pet.nPoint.initPowerLimit();
// }
// player.nPoint.calPoint();
// player.nPoint.setHp(player.nPoint.hp);
// player.nPoint.setMp(player.nPoint.mp);
// player.zone.addPlayer(player);
// player.loaded = true;
// if (player.pet != null) {
// player.pet.nPoint.calPoint();
// player.pet.nPoint.setHp(player.pet.nPoint.hp);
// player.pet.nPoint.setMp(player.pet.nPoint.mp);
// }

// // Client.gI().put(player);

// // Service.getInstance().player(player);
// // Service.getInstance().Send_Caitrang(player);

// // // -64 my flag bag
// // Service.getInstance().sendFlagBag(player);

// // // -113 skill shortcut
// // player.playerSkill.sendSkillShortCut();
// // // item time
// // ItemTimeService.gI().sendAllItemTime(player);

// // // send current task
// // TaskService.gI().sendInfoCurrentTask(player);

// // nhận quà đăng nhập hàng ngày
// // if (Manager.EVENT_SEVER == 3) {
// // RewardService.gI().rewardFirstTimeLoginPerDay(player);
// // }
// }
// }
// }
