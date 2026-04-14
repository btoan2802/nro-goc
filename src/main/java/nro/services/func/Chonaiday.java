package nro.services.func;

import java.util.ArrayList;
import java.util.List;
import nro.models.item.Item;
import nro.models.player.Player;
import nro.server.Client;
import nro.server.Manager;
import static nro.server.Manager.lastTimeADdTotalgoldBaucua;
import nro.server.ServerLog;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;

public class Chonaiday implements Runnable {

    public boolean ketQuaBau;
    public boolean ketQuaCua;
    public boolean ketQuaTom;
    public boolean ketQuaCa;

    public int TotalGoldBau;
    public int TotalGoldCua;
    public int TotalGoldTom;
    public int TotalGoldCA;
    public int TotalHuou;
    public int TotalGa;

    public long lastTimeEnd;

    public List<Player> PLayerBau = new ArrayList<>();
    public List<Player> PlayerCua = new ArrayList<>();
    public List<Player> PlayerTom = new ArrayList<>();
    public List<Player> PlayerCa = new ArrayList<>();
    public List<Player> Playerhuu = new ArrayList<>();
    public List<Player> PLayerGa = new ArrayList<>();

    public int x, y, z;
    private static Chonaiday instance;

    public static Chonaiday gI() {
        if (instance == null) {
            instance = new Chonaiday();
        }
        return instance;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (((lastTimeEnd - System.currentTimeMillis()) / 1000) <= 0) {
                    int x, y, z = 0;
                    if (Manager.cheat == false) {
                        x = Util.nextInt(1, 6);
                        y = Util.nextInt(1, 6);
                        z = Util.nextInt(1, 6);
                    } else {
                        if (Manager.x > 0) {
                            x = Manager.x;
                        } else {
                            x = Util.nextInt(1, 6);
                        }
                        if (Manager.y > 0) {
                            y = Manager.y;
                        } else {
                            y = Util.nextInt(1, 6);
                        }
                        if (Manager.z > 0) {
                            z = Manager.z;
                        } else {
                            z = Util.nextInt(1, 6);
                        }
                    }
                    this.x = x;
                    this.y = y;
                    this.z = z;
                    if (x == y && y == z) {
                        switch (x) {
                            case 1:
                                Win_Round_Same_X(PLayerBau, 1, 4);
                                Fail_Round_X(PlayerCua, 1);
                                Fail_Round_X(PlayerTom, 1);
                                Fail_Round_X(PlayerCa, 1);
                                Fail_Round_X(Playerhuu, 1);
                                Fail_Round_X(PLayerGa, 1);
                                break;
                            case 2:
                                Win_Round_Same_X(PlayerCua, 2, 4);
                                Fail_Round_X(PLayerBau, 1);
                                Fail_Round_X(PlayerTom, 1);
                                Fail_Round_X(PlayerCa, 1);
                                Fail_Round_X(Playerhuu, 1);
                                Fail_Round_X(PLayerGa, 1);
                                break;
                            case 3:
                                Win_Round_Same_X(PlayerTom, 3, 4);
                                Fail_Round_X(PLayerBau, 1);
                                Fail_Round_X(PlayerCua, 1);
                                Fail_Round_X(PlayerCa, 1);
                                Fail_Round_X(Playerhuu, 1);
                                Fail_Round_X(PLayerGa, 1);
                                break;
                            case 4:
                                Win_Round_Same_X(PlayerCa, 4, 4);
                                Fail_Round_X(PLayerBau, 1);
                                Fail_Round_X(PlayerTom, 1);
                                Fail_Round_X(PlayerCua, 1);
                                Fail_Round_X(Playerhuu, 1);
                                Fail_Round_X(PLayerGa, 1);
                                break;
                            case 5:
                                Win_Round_Same_X(Playerhuu, 5, 4);
                                Fail_Round_X(PLayerBau, 1);
                                Fail_Round_X(PlayerTom, 1);
                                Fail_Round_X(PlayerCua, 1);
                                Fail_Round_X(PlayerCa, 1);
                                Fail_Round_X(PLayerGa, 1);
                                break;
                            case 6:
                                Win_Round_Same_X(PLayerGa, 6, 4);
                                Fail_Round_X(PLayerBau, 1);
                                Fail_Round_X(PlayerTom, 1);
                                Fail_Round_X(PlayerCua, 1);
                                Fail_Round_X(PlayerCa, 1);
                                Fail_Round_X(Playerhuu, 1);
                                break;
                        }
                    } else if (x == y) {
                        switch (x) {
                            case 1:
                                Win_Round_Same_X(PLayerBau, 1, 3);
                                Result_Round(z, 1);
                                break;
                            case 2:
                                Win_Round_Same_X(PlayerCua, 2, 3);
                                Result_Round(z, 1);
                                break;
                            case 3:
                                Win_Round_Same_X(PlayerTom, 3, 3);
                                Result_Round(z, 1);
                                break;
                            case 4:
                                Win_Round_Same_X(PlayerCa, 4, 3);
                                Result_Round(z, 1);
                                break;
                            case 5:
                                Win_Round_Same_X(Playerhuu, 5, 3);
                                Result_Round(z, 1);

                                break;
                            case 6:
                                Win_Round_Same_X(PLayerGa, 6, 3);
                                Result_Round(z, 1);
                                break;
                        }
                    } else if (y == z) {
                        switch (y) {
                            case 1:
                                Win_Round_Same_X(PLayerBau, 1, 3);
                                Result_Round(x, 1);
                                break;
                            case 2:
                                Win_Round_Same_X(PlayerCua, 2, 3);
                                Result_Round(x, 1);
                                break;
                            case 3:
                                Win_Round_Same_X(PlayerTom, 3, 3);
                                Result_Round(x, 1);
                                break;
                            case 4:
                                Win_Round_Same_X(PlayerCa, 4, 3);
                                Result_Round(x, 1);
                                break;
                            case 5:
                                Win_Round_Same_X(Playerhuu, 5, 3);
                                Result_Round(x, 1);

                                break;
                            case 6:
                                Win_Round_Same_X(PLayerGa, 6, 3);
                                Result_Round(x, 1);
                                break;
                        }
                    } else if (x == z) {
                        switch (x) {
                            case 1:
                                Win_Round_Same_X(PLayerBau, 1, 3);
                                Result_Round(y, 1);
                                break;
                            case 2:
                                Win_Round_Same_X(PlayerCua, 2, 3);
                                Result_Round(y, 1);
                                break;
                            case 3:
                                Win_Round_Same_X(PlayerTom, 3, 3);
                                Result_Round(y, 1);
                                break;
                            case 4:
                                Win_Round_Same_X(PlayerCa, 4, 3);
                                Result_Round(y, 1);
                                break;
                            case 5:
                                Win_Round_Same_X(Playerhuu, 5, 3);
                                Result_Round(y, 1);
                                break;
                            case 6:
                                Win_Round_Same_X(PLayerGa, 6, 3);
                                Result_Round(y, 1);
                                break;
                        }
                    } else if (x != y && y != z && x != z) {
                        Result_Round(x, 1);
                        Result_Round(y, 2);
                        Result_Round(z, 3);
                    }

                    SendThongBaoOkLast();
                    ReSetGoldALl();
                    if (Manager.cheat == true) {
                        Manager.cheat = false;
                        Manager.x = 0;
                        Manager.y = 0;
                        Manager.z = 0;
                    }
                    this.PLayerBau.clear();
                    this.PlayerCua.clear();
                    this.PlayerTom.clear();
                    this.PlayerCa.clear();
                    this.Playerhuu.clear();
                    this.PLayerGa.clear();

                    this.TotalGoldBau = 0;
                    this.TotalGoldCua = 0;
                    this.TotalGoldTom = 0;
                    this.TotalGoldCA = 0;
                    this.TotalHuou = 0;
                    this.TotalGa = 0;

                    this.lastTimeEnd = System.currentTimeMillis() + 60000;
                } else if (Manager.BotBauCua == true && ((Chonaiday.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 5) {
                    if (Util.canDoWithTime(lastTimeADdTotalgoldBaucua, 10000)) {
                        int rdom = Util.nextInt(1, 6);
                        lastTimeADdTotalgoldBaucua = System.currentTimeMillis();
                        switch (rdom) {
                            case 1:
                                Chonaiday.gI().TotalGoldBau += Util.nextInt(20);
                                break;
                            case 2:
                                Chonaiday.gI().TotalGoldCua += Util.nextInt(20);
                                break;
                            case 3:
                                Chonaiday.gI().TotalGoldTom += Util.nextInt(20);
                                break;
                            case 4:
                                Chonaiday.gI().TotalGoldCA += Util.nextInt(20);
                                break;
                            case 5:
                                Chonaiday.gI().TotalHuou += Util.nextInt(20);
                                break;
                            case 6:
                                Chonaiday.gI().TotalGa += Util.nextInt(20);
                                break;
                        }
                    }
                }

                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void Win_Round_Same_X(List<Player> List, int type, int Xtruyen) {
        if (!List.isEmpty()) {
            for (Player p : List) {
                if (p != null && Client.gI().getPlayer(p.name) != null) {
                    TransactionService.gI().cancelTrade(p);
                    int GoldReward = GetGoldFromType(p, type);
                    Item tv = ItemService.gI().createNewItem((short) 457, (GoldReward * Xtruyen));

                    ServerLog.LogBauCua(p.name, GoldReward, getNameBauCua(type));
                    InventoryService.gI().addItemBag(p, tv, 999);
                    InventoryService.gI().sendItemBags(p);
                    Service.getInstance().sendThongBao(p, "Kết quả vé tham quan rút ra: \n"
                            + getNameBauCua(x) + " : " + getNameBauCua(y)
                            + " : " + getNameBauCua(z)
                            + "\nChúc mừng bạn đã chiến thắng đợt "
                            + "\n Và nhận được " + tv.quantity + " Xu");
                }
            }
        }
    }

    private void SendThongBaoOkLast() {
        for (int i = 0; i < this.PLayerBau.size(); i++) {
            Player p = this.PLayerBau.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                Service.getInstance().sendThongBaoOK(p, "Kết quả hệ thống xóc ra : \n"
                        + getNameBauCua(x) + " : " + getNameBauCua(y)
                        + " : " + getNameBauCua(z));
            }
        }
        for (int i = 0; i < this.PlayerCua.size(); i++) {
            Player p = this.PlayerCua.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                Service.getInstance().sendThongBaoOK(p, "Kết quả vé tham quan rút ra : \n"
                        + getNameBauCua(x) + " : " + getNameBauCua(y)
                        + " : " + getNameBauCua(z));
            }
        }
        for (int i = 0; i < this.PlayerTom.size(); i++) {
            Player p = this.PlayerTom.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                Service.getInstance().sendThongBaoOK(p, "Kết quả vé tham quan rút ra : \n"
                        + getNameBauCua(x) + " : " + getNameBauCua(y)
                        + " : " + getNameBauCua(z));
            }
        }
        for (int i = 0; i < this.PlayerCa.size(); i++) {
            Player p = this.PlayerCa.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                Service.getInstance().sendThongBaoOK(p, "Kết quả vé tham quan rút ra : \n"
                        + getNameBauCua(x) + " : " + getNameBauCua(y)
                        + " : " + getNameBauCua(z));
            }
        }
        for (int i = 0; i < this.Playerhuu.size(); i++) {
            Player p = this.Playerhuu.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                Service.getInstance().sendThongBaoOK(p, "Kết quả vé tham quan rút ra : \n"
                        + getNameBauCua(x) + " : " + getNameBauCua(y)
                        + " : " + getNameBauCua(z));
            }
        }
        for (int i = 0; i < this.PLayerGa.size(); i++) {
            Player p = this.PLayerGa.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                Service.getInstance().sendThongBaoOK(p, "Kết quả vé tham quan rút ra : \n"
                        + getNameBauCua(x) + " : " + getNameBauCua(y)
                        + " : " + getNameBauCua(z));
            }
        }
    }

    private void Win_Round_X(List<Player> list, int type, int round) {
        if (!list.isEmpty()) {
            for (Player p : list) {
                if (p != null && Client.gI().getPlayer(p.name) != null) {
                    TransactionService.gI().cancelTrade(p);
                    int goldReward = GetGoldFromType(p, type);
//                    System.out.println("GET GOLD : " + goldReward);
                    Item rewardItem = ItemService.gI().createNewItem((short) 457, goldReward * 2);
                    ServerLog.LogBauCua(p.name, goldReward, getNameBauCua(type));
                    InventoryService.gI().addItemBag(p, rewardItem, 999);
                    InventoryService.gI().sendItemBags(p);
                    Service.getInstance().sendThongBao(p, "Kết quả vé tham quan rút ra : \n"
                            + getNameBauCua(x) + " : " + getNameBauCua(y)
                            + " : " + getNameBauCua(z)
                            + "\nChúc mừng bạn đã chiến thắng đợt " + round
                            + "\n Và nhận được " + goldReward + " xu");
                }
            }
        }
    }

    private void Fail_Round_X(List<Player> List, int Round) {
        if (!List.isEmpty()) {
            for (int i = 0; i < List.size(); i++) {
                Player p = List.get(i);
                if (p != null && Client.gI().getPlayer(p.name) != null) {
                    TransactionService.gI().cancelTrade(p);
                    Service.getInstance().sendThongBao(p, "Kết quả vé tham quan rút ra : \n"
                            + getNameBauCua(x) + " : " + getNameBauCua(y)
                            + " : " + getNameBauCua(z)
                            + "\nBạn đã trắng tay đợt " + Round);
                }
            }
        }
    }

    private void Result_Round(int SO, int round) {
        switch (SO) {            // BẦU
            case 1:
                Win_Round_X(PLayerBau, 1, round);
                Fail_Round_X(PlayerCua, round);
                Fail_Round_X(PlayerTom, round);
                Fail_Round_X(PlayerCa, round);
                Fail_Round_X(Playerhuu, round);
                Fail_Round_X(PLayerGa, round);
                break;
            case 2:    // CUA 
                Win_Round_X(PlayerCua, 2, round);
                Fail_Round_X(PLayerBau, round);
                Fail_Round_X(PlayerTom, round);
                Fail_Round_X(PlayerCa, round);
                Fail_Round_X(Playerhuu, round);
                Fail_Round_X(PLayerGa, round);
                break;
            case 3: // tôm
                Win_Round_X(PlayerTom, 3, round);
                Fail_Round_X(PLayerBau, round);
                Fail_Round_X(PlayerCua, round);
                Fail_Round_X(PlayerCa, round);
                Fail_Round_X(Playerhuu, round);
                Fail_Round_X(PLayerGa, round);
                break;
            case 4: // cá
                Win_Round_X(PlayerCa, 4, round);
                Fail_Round_X(PLayerBau, round);
                Fail_Round_X(PlayerTom, round);
                Fail_Round_X(PlayerCua, round);
                Fail_Round_X(Playerhuu, round);
                Fail_Round_X(PLayerGa, round);
                break;
            case 5:
                Win_Round_X(Playerhuu, 5, round);
                Fail_Round_X(PLayerBau, round);
                Fail_Round_X(PlayerTom, round);
                Fail_Round_X(PlayerCua, round);
                Fail_Round_X(PlayerCa, round);
                Fail_Round_X(PLayerGa, round);
                break;
            case 6:
                Win_Round_X(PLayerGa, 6, round);
                Fail_Round_X(PLayerBau, round);
                Fail_Round_X(PlayerTom, round);
                Fail_Round_X(PlayerCua, round);
                Fail_Round_X(PlayerCa, round);
                Fail_Round_X(Playerhuu, round);
                break;
        }
    }

    private void ReSetGoldALl() {
        for (int i = 0; i < this.PLayerBau.size(); i++) {
            Player p = this.PLayerBau.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                p.GoldBau = 0;
            }
        }
        for (int i = 0; i < this.PlayerCua.size(); i++) {
            Player p = this.PlayerCua.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                p.GoldCua = 0;
            }
        }
        for (int i = 0; i < this.PlayerTom.size(); i++) {
            Player p = this.PlayerTom.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                p.GoldTom = 0;
            }
        }
        for (int i = 0; i < this.PlayerCa.size(); i++) {
            Player p = this.PlayerCa.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                p.GoldCa = 0;
            }
        }
        for (int i = 0; i < this.Playerhuu.size(); i++) {
            Player p = this.Playerhuu.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                p.GoldHuou = 0;
            }
        }
        for (int i = 0; i < this.PLayerGa.size(); i++) {
            Player p = this.PLayerGa.get(i);
            if (p != null && Client.gI().getPlayer(p.name) != null) {
                p.GoldGa = 0;
            }
        }

    }

    private int GetGoldFromType(Player p, int type) {
        switch (type) {
            case 1:
                return (p.GoldBau);
            case 2:
                return (p.GoldCua);
            case 3:
                return (p.GoldTom);
            case 4:
                return (p.GoldCa);
            case 5:
                return (p.GoldHuou);
            case 6:
                return (p.GoldGa);
            default:
                return 0;
        }
    }

    public void addPlayerBau(Player pl) {
        if (!PLayerBau.contains(pl)) {
            PLayerBau.add(pl);
        }
    }

    public void addPlayerCua(Player pl) {
        if (!PlayerCua.contains(pl)) {
            PlayerCua.add(pl);
        }
    }

    public void addPlayerTom(Player pl) {
        if (!PlayerTom.contains(pl)) {
            PlayerTom.add(pl);
        }
    }

    public void addPlayerCa(Player pl) {
        if (!PlayerCa.contains(pl)) {
            PlayerCa.add(pl);
        }
    }

    public void addplayerHuou(Player pl) {
        if (!Playerhuu.contains(pl)) {
            Playerhuu.add(pl);
        }
    }

    public void addPlayerGa(Player pl) {
        if (!PLayerGa.contains(pl)) {
            PLayerGa.add(pl);
        }
    }

    public String getNameBauCua(int so) {
        switch (so) {
            case 1:
                return "Bầu";
            case 2:
                return "Cua";
            case 3:
                return "Tôm";
            case 4:
                return "Cá";
            case 5:
                return " Hươu";
            case 6:
                return "Gà";
            default:
                return "0";
        }
    }

}
