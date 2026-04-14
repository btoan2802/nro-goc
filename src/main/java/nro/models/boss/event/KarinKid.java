package nro.models.boss.event;

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
public class KarinKid extends FutureBoss {

    public KarinKid() {
        super(BossFactory.KARIN_KID, BossData.KARIN_KID);
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
        // if (Util.isTrue(1, 10)) {
        // int[] tempId = new int[]{138, 142, 146, 150, 154, 158, 162, 166, 170, 174,
        // 178, 182, 186};
        // ItemMap itemMap = new ItemMap(this.zone, tempId[Util.nextInt(0, tempId.length
        // - 1)],
        // 1, pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y -
        // 24), pl.id);
        // RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id,
        // itemMap.itemTemplate.type, itemMap.options);
        // RewardService.gI().initStarOption(itemMap, new RewardService.RatioStar[]{
        // new RewardService.RatioStar((byte) 1, 1, 2),
        // new RewardService.RatioStar((byte) 2, 1, 3),
        // new RewardService.RatioStar((byte) 3, 1, 4),
        // new RewardService.RatioStar((byte) 4, 1, 5),
        // new RewardService.RatioStar((byte) 5, 1, 50),
        // new RewardService.RatioStar((byte) 6, 1, 100)
        // });
        // Service.getInstance().dropItemMap(this.zone, itemMap);
        // }
        // TaskService.gI().checkDoneTaskKillBoss(pl, this);
        // generalRewards(pl);
        baseRewards(pl, 6, 12, (byte) 5);

    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        int dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            damage = this.nPoint.hpMax / 40;
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                rewards(plAtt);
                notifyPlayeKill(plAtt);
                die();
            }
            return dame;
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
        this.textTalkBefore = new String[]{"|-1|Tùng tùng xèn xèn",
            "|-1|Các con có muốn nhận lì xì không!",
            "|-2|Chúc mừng năm mới",
            "|-1|Năm mới vui vẻ"
        };
        this.textTalkMidle = new String[]{"|-1|Tùng tùng xèn xèn",
            "|-1|Happy new year",
            "|-1|Năm mới an khang thịnh vượng",
            "|-1|Lại đây ta phát cho lì xì",
            "|-2|Năm mới vui vẻ",
            "|-1|Hãy cho ta thấy sức mạnh của các con"
        };
        this.textTalkAfter = new String[]{"|-2|Rơi nhiều quà thế"};
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }

}
