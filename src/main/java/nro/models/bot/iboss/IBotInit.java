package nro.models.bot.iboss;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public interface IBotInit extends IBotOutfit {

    void init(); // khởi tạo respawn

    void initTalk(); // khởi tạo hội thoại

    void dropItemReward(int tempId, int playerId, int... quantity); // rớt item thưởng
}
