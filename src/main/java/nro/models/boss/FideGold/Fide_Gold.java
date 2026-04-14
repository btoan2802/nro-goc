package nro.models.boss.FideGold;

import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.ServerNotify;
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
public class Fide_Gold extends FutureBoss {

    public Fide_Gold() {
        super(BossFactory.FIDE_GOLD_NEW, BossData.FIDE_DAI_CA_GOLD_NEW);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie()) {
            return 0;
        } else {
            if (plAtt != null) {
                if (Util.isTrue(10, 100)) {
                    damage = 1;
                    Service.getInstance().chat(this, "Xí hụt..");
                }
            }
            int dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                rewards(plAtt);
            }
            return dame;
        }
    }
    
    @Override
    public void rewards(Player pl) {
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        try {
            if (!generalRewards(pl, (byte) 12, (byte) 25)) {
                baseRewards(pl, 10, 25, (byte) 3);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        BossPointEven(pl);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{
            "|-1|Ta cảm thấy nguồn sức mạnh kinh khủng đang chạy trong người ta rồi",
            "|-2|Hắn ta là...bán thần!"
        };
        this.textTalkMidle = new String[]{"|-1|Tấn công bọn chúng đi Broly",
            "|-1|Gaaaaaa",
            "|-2|Sức mạnh kinh khủng quá!!",
            "|-2|Chúng ta thua mất!!",
            "|-1|Hô hô hô",
            "|-1|Chết đi Gôku",
            "|-1|Các ngươi yếu quá đấy!",
            "|-1|Chỉ có thế thôi sao"
        };
        this.textTalkAfter = new String[]{"|-1|Ta sẽ sớm quay trở lại, các ngươi chờ đấy"};

    }

    public void leaveMap() {
        Boss fide1 = BossManager.gI().getBossById(BossFactory.BROLY_NEW);
        Boss fide2 = BossManager.gI().getBossById(BossFactory.BROLY_LEGEND_NEW);
        if ((fide1 == null && fide2 == null)
                || (fide2 != null && fide2.isBossDie())) {
            BossFactory.createBossAffterLeaveMap(BossFactory.FIDE_NEW, true);
        }

        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    public void joinMap() {

        if (this.zone != null) {
            ChangeMapService.gI().changeMap(this, zone, this.location.x, this.location.y);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }
    }
}
