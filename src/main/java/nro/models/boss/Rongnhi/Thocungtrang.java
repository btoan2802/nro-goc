package nro.models.boss.Rongnhi;

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
public class Thocungtrang extends FutureBoss {

    public Thocungtrang() {
        super(BossFactory.THO_CUNG_TRANG, BossData.THO_CUNG_TRANG);
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
                int RageItem = Util.nextInt(10, 15);
                // int RageItem = 1;
                for (int i = 0; i < RageItem; i++) {
                    ItemMap itemMap = null;
                    x += 16;
                    if (x < 0 || x >= this.zone.map.mapWidth) {
                        return;
                    }
                    int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
                    itemMap = new ItemMap(zone, 1623, 1, x, y,
                            pl.id);
                    if (itemMap != null) {
                        Service.getInstance().dropItemMap(zone, itemMap);
                    }
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
        this.textTalkBefore = new String[]{"|-1|Hế lô em,anh đứng đây từ chiều",
            "|-1|Mày hiểu thế là sao chứ? Cuối cùng tao đã có thể giết mày!",
            "|-2|Tao lại sợ mày quá cơ,cho bố cái địa chỉ!",
            "|-1|Mày làm tao phấn khích rồi đấy hahaha.."
        };
        this.textTalkMidle = new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
            "|-1|Ha ha ha! Mắt mày mù à? Nhìn máy đo chỉ số đi!!",
            "|-1|Định chạy trốn hả, hử",
            "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
            "|-1|Hahaha mày đây rồi",
            "|-1|Tao đã có lệnh từ đại ca Fide rồi"
        };
        this.textTalkAfter = new String[]{"|-2|Đẹp trai nó phải thế"};
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }

}
