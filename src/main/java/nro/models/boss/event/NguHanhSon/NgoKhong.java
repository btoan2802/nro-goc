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
public class NgoKhong extends FutureBoss {

    public NgoKhong() {
        super(BossFactory.NGO_KHONG_EVENT, BossData.NGO_KHONG_EVENT);
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

        if (!generalRewards(pl, (byte) 11, (byte) 35)) {
            try {
                ItemMap itemMap = null;
                int x = this.location.x;
                if (x < 0 || x >= this.zone.map.mapWidth) {
                    return;
                }
                short listItem[] = {16, 17, 18, 19, 20};
                int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
                itemMap = new ItemMap(pl.zone, Util.randomItem(listItem), 1, x, y, pl.id);
                Service.getInstance().dropItemMap(zone, itemMap);
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
        this.textTalkBefore = new String[]{"|-1|Hù ú khẹt khẹt",
            "|-1|Lão Tôn xin chào",
            "|-2|Con khỉ này là Tôn Ngộ Không à!",
            "|-1|Đến ngũ hành sơn này nộp mạng à haahaa."
        };
        this.textTalkMidle = new String[]{"|-1|Tiếp chiêu",
            "|-1|Nam mô!!",
            "|-1|72 phép thần thông",
            "|-1|Có ngon thì mang đào tiên ra đây ta tha mạng",
            "|-1|Hù ú khẹt khẹt",
            "|-1|Thiết bảng ngàn cân"
        };
        this.textTalkAfter = new String[]{"|-1|Ta sẽ còn tiếp tục hahaa"};
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }

}
