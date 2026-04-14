package nro.manager;

import nro.models.Tournaments;
import nro.models.player.Player;
import nro.services.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;

public class TournamentsManager extends AbsManager<Tournaments> {

    @Getter
    private List<Player> queue = new CopyOnWriteArrayList<>();

    @Override
    public Tournaments findByID(int id) {
        return null;
    }

    public void update() {
        List<Tournaments> r = new ArrayList<>();
        for (Tournaments tournaments : list) {
            tournaments.update();
            if (tournaments.isFinish()) {
                r.add(tournaments);
            }
        }
        list.removeAll(r);
    }

    public void addPlayer(Player player) {
        if (queue.contains(player)) {
            Service.getInstance().sendThongBao(player, "Bạn đã đăng ký rồi!");
            return;
        }
        this.queue.add(player);
        if (this.queue.size() > 1) {
            Player[] players = new Player[2];
            for (int i = 0; i < 2; i++) {
                players[i] = this.queue.remove(0);
            }
            Tournaments tournaments = new Tournaments(players);
            tournaments.start();

            add(tournaments);
        }
        Service.getInstance().sendThongBao(player, "Đã đăng ký thành công");
    }

    public static TournamentsManager gI() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {

        private static final TournamentsManager INSTANCE = new TournamentsManager();
    }
}
