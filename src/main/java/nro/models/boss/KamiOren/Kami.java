package nro.models.boss.KamiOren;

import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.server.io.Message;
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
public class Kami extends FutureBoss {

    public Kami() {
        super(BossFactory.KAMI, BossData.KAMI);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
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

    public void NonPk() {
        this.changeToIdle();
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"|-1|Sức mạnh của 2 nhân bản vô song",
            "|-1|Các ngươi chịu thua đi",
            "|-1|Hạ bọn chúng đi Oren",
            "|-1|Ta nghĩ đã đến lúc hợp nhất rồi đấy",
            "|-2|Tên này quá mạnh?"
        };
        this.textTalkAfter = new String[]{"|-1|Không xong rồi, hợp thể thôi",
            "|-2|Ngươi nói gì chứ?"};

    }

    @Override
    public void joinMap() {
    }

    @Override
    public void leaveMap() {
        Boss SuperCheck = BossManager.gI().getBossById(BossFactory.OREN);
        if (SuperCheck == null || SuperCheck.isBossDie()) {
            fusionEffect();
            CreatBossLastDie(BossFactory.KAMIOREN, expoff);
        }

        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    private void fusionEffect() {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte((byte) 6);
            msg.writer().writeInt((int) this.id);
            Service.getInstance().sendMessAllPlayerInMap(this, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    @Override
    public void doneChatS() {

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
}
