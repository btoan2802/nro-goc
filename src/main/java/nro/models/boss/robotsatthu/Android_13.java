package nro.models.boss.robotsatthu;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.player.Player;
import nro.services.TaskService;
import nro.utils.Logger;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 */
public class Android_13 extends Boss {

    public Android_13() {
        super(BossFactory.ANDROID_13, BossData.ANDROID_13);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        generalRewards(pl, (byte) 13, (byte) 25);
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        // if (!generalRewards(pl, (byte) 10, (byte) 25)) {
        // baseRewards(pl, 8, 12, (byte) 3);
        // }
    }

    @Override
    public void idle() {
    }

    @Override
    public void checkPlayerDie(Player pl) {
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"|-1|Sôn..gôku",
            "|-2|Lại là tiến sĩ Kôrê à.. rốt cuộc ông ta đã tạo ra bao nhiêu rôbốt nhân tạo thế nhỉ?",
            "|-1|Bọn ta là rôbốt sát thủ, sinh ra từ máy tính ngài Kôrê,..",
            "|-1|..cho một mục tiêu duy nhất là giết Sôngôku!",
            "|-2|Máy tính? Để giết Gôku sao?",
            "|-1|Mong muốn trả thù Gôku của ngài Kôrê đã được lưu hết vào máy tính..",
            "|-1|Bọn ta sinh ra từ lòng căm thù ngày một tăng bên trong chiếc máy tính có chứa mong muốn trả thù",
            "|-1|Mục tiêu của bọn ta chỉ là Gôku, nhưng mà.. nếu ngươi mà cản đường thì là chuyện khác!"};
        this.textTalkMidle = new String[]{"|-1|Ê cố lên nhóc",
            "|-1|Chán", "|-1|Hãy cho ta biết Gôku đang ở đâu"};
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        Boss adr15 = BossManager.gI().getBossById(BossFactory.ANDROID_15);
        if (adr15 != null) {
            adr15.setJustRest();
        }
    }

}
