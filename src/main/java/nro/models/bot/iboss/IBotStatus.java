package nro.models.bot.iboss;

import nro.models.player.Player;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public interface IBotStatus extends IBotInit {

    void attack(); // attack

    void idle(); // trong lúc attack có thể đứng nghỉ

    void checkPlayerDie(Player pl); // attack player nào đó rồi kiểm tra

    void die();

    void respawn();
}
