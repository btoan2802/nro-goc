package nro.models.boss.iboss;

import nro.models.player.Player;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public interface BossInterface extends IBossStatus {

    void update();

    void rewards(Player pl); // phần thưởng sau khi bị chết

    Player getPlayerAttack() throws Exception; // lấy ra 1 player để đánh

    void joinMap();

    void leaveMap();

    void doneChatS();

    void doneChatA();

    void setJustRest();

    boolean talk();

    boolean generalRewards(Player player, byte maxLevel, byte ratio);

    void baseRewards(Player player, int min_count, int max_count, byte type);
}
