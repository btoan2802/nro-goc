package nro.models.boss.fide;

import nro.consts.ConstItem;
import nro.consts.ConstOption;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 * @copyright Phong Vũ
 */
public class FideGold extends Boss {

    public FideGold() {
        super(BossFactory.FIDEGOLD, BossData.FIDEGOLD);
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

        try {
            int x = this.location.x;
            if (x < 0 || x >= this.zone.map.mapWidth) {
                return;
            }
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            ItemMap itemMap = new ItemMap(this.zone, (short) 628, 1,
                    x, y, pl.id);

            itemMap.options.add(new ItemOption(50, Util.nextInt(12, 18)));
            itemMap.options.add(new ItemOption(77, Util.nextInt(12, 18)));
            itemMap.options.add(new ItemOption(103, Util.nextInt(12, 18)));
            itemMap.options.add(new ItemOption(ConstOption.KHONG_ANH_HUONG_LANH, 1));
            itemMap.options.add(new ItemOption(93, Util.nextInt(1, 3)));
            Service.getInstance().dropItemMap(this.zone, itemMap);
        } catch (Exception e) {
            // TODO: handle exception
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
        this.textTalkMidle = new String[]{"|-1|Oải rồi hả?", "|-1|Ê cố lên nhóc",
            "|-1|Chán", "|-1|Ta có nhầm không nhỉ"};

    }

    @Override
    public void leaveMap() {

        super.leaveMap();
        if (!ChangeMapService.gI().TimeBossFideGold()) {
            BossFactory.setActiveFideGold();
            BossManager.gI().removeBoss(this);
        }
    }

}
