package nro.models.boss.quy_lao;

import nro.consts.ConstOption;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

public class QuyLao extends Boss {

    public QuyLao() {
        super(BossFactory.QUY_LAO_NEW, BossData.QUY_LAO_NEW);
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }

    @Override
    public void leaveMap() {
        BossManager.gI().getBossById(BossFactory.JAYKY_CHUN_NEW).changeToAttack();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        // ItemMap itemMap = null;
        // int x = this.location.x;
        // if (x < 0 || x >= this.zone.map.mapWidth) {
        // return;
        // }
        // int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        // itemMap = new ItemMap(this.zone, 710, 1, x, y, pl.id);
        // itemMap.options.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(15,
        // 25)));
        // itemMap.options.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(15, 25)));
        // itemMap.options.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(15, 25)));
        // itemMap.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(90,
        // 110)));
        // if (itemMap != null) {
        // Service.getInstance().dropItemMap(zone, itemMap);
        // }
        // TaskService.gI().checkDoneTaskKillBoss(pl, this);
        if (!generalRewards(pl, (byte) 99, (byte) 15)) {
            baseRewards(pl, 8, 12, (byte) 3);
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
        this.textTalkMidle = new String[]{"|-1|Hô hô hô",
            "|-1|Hãy xem đòn Kamejoko của ta",
            "|-1|Yếu quá",
            "|-1|Ngươi sẽ không bao giờ thắng được đâu!!",
            "|-2|Ta sẽ không bao giờ đầu hàng!!",};
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null) {
            switch (plAtt.playerSkill.skillSelect.template.id) {
                case Skill.KAMEJOKO:
                case Skill.MASENKO:
                case Skill.ANTOMIC:

                    return 0;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

}
