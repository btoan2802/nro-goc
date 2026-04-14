package nro.models.boss.event.Event16;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.EffSkinService;
import nro.services.ItemService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class ThoDaiCa extends FutureBoss {

    public ThoDaiCa() {
        super(Util.randomBossId(), BossData.THO_DAI_CA);
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
                    itemMap = new ItemMap(zone, 462, 1, x, y,
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
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl != null && !pl.isDie() && !pl.isMiniPet) {
                this.playerSkill.skillSelect = this.getSkillAttack();
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                    false);
                        } else {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 30)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                    false);
                            if (pl.effectSkin != null && !pl.effectSkin.isCarrot) {
                                EffSkinService.gI().setCarrot(pl);
                                this.chat("Băt tay nào!!!");
                            }
                        }
                    }
                    SkillService.gI().useSkill(this, pl, null);
                    checkPlayerDie(pl);
                } else {
                    goToPlayer(pl, false);
                }
            }
        } catch (Exception ex) {
            Log.error(Boss.class, ex);
        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {

        damage = Util.nextInt(1000, 5000) * 1000;
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkMidle = new String[]{"|-1|Tới số rồi",
            "|-1|Ha ha ha! Mắt mày mù à? Ta là trùm khu này!!",
            "|-1|Gọi ta là đại ca đi",
            "|-1|Carrottt",
            "|-1|Hahaha mày đây rồi",};
        this.textTalkAfter = new String[]{"|-1|Hẹn gặp lại hahaa"};
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }

}
