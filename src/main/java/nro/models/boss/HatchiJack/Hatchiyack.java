package nro.models.boss.HatchiJack;

import nro.models.boss.*;
import nro.consts.ConstOption;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @author @copyright
 *
 */
public class Hatchiyack extends FutureBoss {

    public Hatchiyack() {
        super(BossFactory.HATCHIYACK, BossData.HATCHIYACK);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        int x = this.location.x;
        if (x < 0 || x >= this.zone.map.mapWidth) {
            return;
        }
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        itemMap = new ItemMap(this.zone, 729, 1, x, y, pl.id);
        itemMap.options.add(new ItemOption(ConstOption.SUC_DANH_PT, 25));
        itemMap.options.add(new ItemOption(ConstOption.HP_PT, 25));
        itemMap.options.add(new ItemOption(ConstOption.KI_PT, 25));
        itemMap.options.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG, Util.nextInt(20, 35)));
        itemMap.options.add(new ItemOption(ConstOption.CHI_MANG, 10));

        if (Util.isTrue(93, 100)) {
            itemMap.options.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 3)));
        }

        if (itemMap != null) {
            Service.getInstance().dropItemMap(zone, itemMap);
        }
        if (!generalRewards(pl, (byte) 11, (byte) 25)) {
            baseRewards(pl, 10, 15, (byte) 4);
        }

    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        int dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            if (plAtt != null) {
                int skill = plAtt.playerSkill.skillSelect.template.id;
                if (skill == Skill.DRAGON || skill == Skill.DEMON || skill == Skill.GALICK
                        || skill == Skill.LIEN_HOAN) {
                    if (damage > this.nPoint.hpMax / 100) {
                        damage = this.nPoint.hpMax / 100;
                    }
                } else {
                    damage = 1;
                    Service.getInstance().chat(plAtt, "Trời ơi, chưởng hoàn toàn vô hiệu lực với hắn..");
                }
                dame = super.injured(plAtt, damage, piercing, isMobAttack);
                if (this.isDie()) {
                    rewards(plAtt);
                    notifyPlayeKill(plAtt);
                    die();
                }
            }

            return dame;
        }
    }

    @Override
    public void initTalk() {

        textTalkAfter = new String[]{"|-1|Ta sẽ tiêu diệt tất cả các ngươi, lũ sâu bọ"};
    }

    @Override
    public void leaveMap() {

        BossManager.gI().setJustRest(BossFactory.DR_LYCHEE);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
