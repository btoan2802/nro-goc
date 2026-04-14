package nro.models.boss.event.NguHanhSon;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class DuongTang extends FutureBoss {

    public DuongTang() {
        super(BossFactory.DUONG_TANG, BossData.DUONG_TANG);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void joinMap() {
        super.joinMap();

    }

    @Override
    public void rewards(Player pl) {

        if (pl != null) {
            try {
                int x = this.location.x;
                ItemMap itemMap = null;
                if (x < 0 || x >= this.zone.map.mapWidth) {
                    return;
                }
                short listItem[] = {537, 538, 539, 540, 543, 541, 933, 934, 925, 926, 927, 928, 929, 930, 931};
                int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
                itemMap = new ItemMap(zone, Util.randomItem(listItem), 1, x, y,
                        pl.id);
                if (itemMap != null) {
                    Service.getInstance().dropItemMap(zone, itemMap);

                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"|-1|Đừng tin lời hắn ta",
            "|-1|Ta là đường tăng thật đây!",
            "|-2|Ta thấy sai sai!",
            "|-1|Sư phụ đây con.."
        };
        this.textTalkMidle = new String[]{"|-1|Tiếp chiêu",
            "|-1|Nam mô!!",
            "|-1|Sư phụ đây mà",
            "|-1|Mang cho ta quả hồng đào đi con",
            "|-1|Tên kia là giả",
            "|-1|Đừng đánh sư phụ mà"
        };
        this.textTalkAfter = new String[]{"|-1|Ta sẽ còn tiếp tục hahaa"};
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }

}
