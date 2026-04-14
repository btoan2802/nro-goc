/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.services;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import nro.models.player.Player;
import nro.server.Client;
import nro.utils.Util;

/**
 *
 * @author Administrator
 */
public class ConSoMayManService {

    private static ConSoMayManService i = new ConSoMayManService();

    public long countdownTime = 130;// 2 phut
    public int winningNumber = 0;
    private int nextWinningNumber = Util.nextInt(000, 999);
    public final long gemCost = 500;
    public final long gem = 5;
    private final List<LuckyNumberData> playersData = new ArrayList<>();
    private final List<Integer> recentWinningNumbers = new ArrayList<>();
    private final List<String> namePlayerTop = new ArrayList<>();
    private Timer timer;
    private TimerTask task;

    public static ConSoMayManService gI() {
        return i;
    }

    public void activate(int delay) {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                updateGame();
            }
        };
        timer.schedule(task, delay, delay);
    }

    // input
    public void addPlayerData(Player player, int chosenNumber) {
        if (String.valueOf(chosenNumber).length() != 3) {
            Service.getInstance().sendThongBao(player, "Số phải có đúng 3 chữ số.");
            return;
        }
        if (!validatePlayerInput(player)) {
            return;
        }

        // gioi han mua so
        if (playersData.stream().filter(d -> d.playerId == player.id).count() >= 10) {
            Service.getInstance().sendThongBao(player, "Bạn đã chọn 10 số rồi không thể chọn thêm.");
            return;
        }

        // kiem tra nguoi chs khong chon so bi trung
        if (playersData.stream().anyMatch(data -> data.playerId == player.id && data.chosenNumber == chosenNumber)) {
            Service.getInstance().sendThongBao(player, "Số này bạn đã chọn rồi vui lòng chọn số khác.");
            return;
        }

        LuckyNumberData data = new LuckyNumberData(player.id, chosenNumber);
        playersData.add(data);
        updatePlayerBalance(player);
        Service.getInstance().sendQuaySo(player, (byte) 0, strNumber(player.id), null, null);
    }

    private boolean validatePlayerInput(Player player) {
        if (player.inventory.gem < this.gem) {
            Service.getInstance().sendThongBao(player, "Bạn không đủ ngọc để thực hiện.");
            return false;
        }
        return true;
    }

    private void updatePlayerBalance(Player player) {
        player.inventory.subGem((int) gem);
        Service.getInstance().sendMoney(player);
    }

    public void updateGame() {
        if (countdownTime > 0) {
            countdownTime--;
        }
        if (countdownTime <= 0) {
            this.countdownTime = 130;
            resetGame(nextWinningNumber);
            this.nextWinningNumber = Util.nextInt(000, 999);
        }
    }

    public void resetGame(int winningNumber) {
        this.winningNumber = winningNumber;
        this.addSoMayMan(this.winningNumber);

        Set<Long> rewardedPlayers = new HashSet<>();

        for (LuckyNumberData data : playersData) {
            Player player = Client.gI().getPlayer(data.playerId);
            if (player != null && !rewardedPlayers.contains(player.id)) {
                if (data.chosenNumber == winningNumber) {
                    Service.getInstance().sendQuaySo(player, (byte) 1, "",
                            String.valueOf(winningNumber),
                            getWinningMessage(data.playerId, data));
                    rewardedPlayers.add(player.id);
                } else {
                    Service.getInstance().sendQuaySo(player, (byte) 1, "",
                            String.valueOf(winningNumber),
                            "Con số trúng thưởng là " + winningNumber + ". Chúc bạn may mắn lần sau!");
                }
            }
        }
        playersData.clear();
    }

    private String getWinningMessage(long playerId, LuckyNumberData data) {
        if (data.playerId == playerId && data.chosenNumber == winningNumber) {
            Player player = Client.gI().getPlayer(data.playerId);
            if (player != null) {
                System.out.println("Cộng gem: " + gemCost);
                player.inventory.addGem((int) gemCost);
                Service.getInstance().sendMoney(player);
                this.addNamePlayerTop(player.name);
                return "Chúc mừng " + player.name
                        + ", bạn đã thắng " + gemCost
                        + " ngọc với con số may mắn " + winningNumber;
            }
        }
        return "Con số trúng thưởng là " + winningNumber + ". Chúc bạn may mắn lần sau!";
    }

    private void addSoMayMan(int number) {
        if (this.recentWinningNumbers.size() >= 5) {
            this.recentWinningNumbers.remove(0);
        }
        this.recentWinningNumbers.add(number);
    }

    private void addNamePlayerTop(String name) {
        if (this.namePlayerTop.size() >= 5) {
            this.namePlayerTop.remove(0);
        }
        namePlayerTop.add(name);
    }

    public String strNumber(long id) {
        return playersData.stream()
                .filter(d -> d.playerId == id)
                .map(d -> String.valueOf(d.chosenNumber))
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    public String topUpGem(long id) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);

        long totalGem = 0;

        for (LuckyNumberData d : playersData) {
            if (d.playerId == id) {
                totalGem += gemCost;
            }
        }

        if (totalGem >= 1000000000) {
            return num.format((double) totalGem / 1000000000) + " Tỷ ngọc xanh";
        } else if (totalGem >= 1000000) {
            return num.format((double) totalGem / 1000000) + " Tr ngọc xanh";
        } else if (totalGem >= 1000) {
            return num.format((double) totalGem / 1000) + " k ngọc xanh";
        } else {
            return num.format(totalGem) + " ngọc xanh";
        }
    }

    public String getNameListTop() {
        return String.join(", ", namePlayerTop);
    }

    public String srtDataKetQua() {
        return recentWinningNumbers.isEmpty() ? "" : String.join(", ", recentWinningNumbers.stream().map(String::valueOf).toArray(String[]::new));
    }

    public static class LuckyNumberData {

        public long playerId;
        public int chosenNumber;

        public LuckyNumberData(long playerId, int chosenNumber) {
            this.playerId = playerId;
            this.chosenNumber = chosenNumber;
        }
    }
}
