package nro.models.bot.iboss;

import nro.models.player.Player;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public interface BotInterface extends IBotStatus {

    void update();

    void rewards(Player pl); // phần thưởng sau khi bị chết

    Player getPlayerAttack() throws Exception; // lấy ra 1 player để đánh

    void joinMap();

    void leaveMap();

    boolean talk();

    void generalRewards(Player player);

    void baseRewards(Player player, int max_count);
}
