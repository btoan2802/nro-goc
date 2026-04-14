package nro.models;

import lombok.Getter;
import lombok.Setter;
import nro.consts.ConstMap;
import nro.consts.ConstPlayer;
import nro.manager.TournamentsManager;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.services.*;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

@Setter
@Getter
public class Tournaments {

    private long lastTimeUpdate;
    private boolean isFinish;
    private int time;
    private Player[] players;
    private int winner;

    public Tournaments(Player[] players) {
        this.players = players;
        this.winner = -1;
    }

    public void update() {
        if (time > 10 && !isFinish) {
            for (Player player : players) {
                if (player.isDie()) {
                    winner = getWinner(player);
                }
            }
            if (winner != -1) {
                end();
            }
        }
        if (Util.canDoWithTime(lastTimeUpdate, 1000)) {
            time++;
        }
    }

    private void end() {
        isFinish = true;
        Zone z = MapService.gI().getMapWithRandZone(52);
        for (Player player : players) {
            PlayerService.gI().hoiSinh(player);
            PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
            ChangeMapService.gI().changeMap(player, z, Util.nextInt(300, 400), 336);
            Util.setTimeout(() -> {
//                ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
            }, 500);
        }
    }

    private int getWinner(Player player) {
        return player == players[0] ? 1 : 0;
    }

    private Player getOpponents(Player player) {
        if (players[0] == player) {
            return players[1];
        }
        return players[0];
    }

    private int getIndex(Player player) {
        if (players[0] == player) {
            return 0;
        }
        return 1;
    }

    public void start() {
        Zone zone = getMapChalllenge(ConstMap.DAI_HOI_VO_THUAT_113);
        for (Player player : players) {
            joinMap(player, zone);
            player.inventory.gold -= 500000000;
            Service.getInstance().sendMoney(player);
        }
        ready();
    }

    private void ready() {
        for (Player player : players) {
            PlayerService.gI().setPos(player, 335 + (getIndex(player) * 101), 264, 0);
            EffectSkillService.gI().startStun(player, System.currentTimeMillis(), 10000);
            ItemTimeService.gI().sendItemTime(player, 3779, 10000 / 1000);
            Util.setTimeout(() -> {
                PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                PlayerService.gI().sendTypePkToPlayer(player, getOpponents(player));
            }, 10000);
        }
    }

    private void joinMap(Player player, Zone zone) {
        if (zone != null) {
            ChangeMapService.gI().changeMap(player, zone, player.location.x, 360);
            Util.setTimeout(() -> {
                Service.getInstance().sendThongBao(player, "Số thứ tự của ngươi là 1\n chuẩn bị thi đấu nhé");
            }, 500);
        } else {

        }
    }

    public Zone getMapChalllenge(int mapId) {
        Map map = MapService.gI().getMapById(mapId);
        for (Zone z : map.zones) {
            if (z.getPlayers().size() == 0) {
                return z;
            }
        }
        return null;
    }
}
