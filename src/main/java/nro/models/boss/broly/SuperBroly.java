package nro.models.boss.broly;

import nro.consts.ConstOption;
import nro.consts.ConstRatio;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.ServerNotify;
import nro.services.NpcMethod;
import nro.services.PetService;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
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
public class SuperBroly extends Broly {

    public SuperBroly() {
        super(BossFactory.SUPER_BROLY, BossData.SUPER_BROLY);
        this.nPoint.defg = (short) 3000;

    }

    public SuperBroly(short id, BossData data) {
        super(id, data);
        this.nPoint.defg = (short) 3000;

    }

    @Override
    public void attack() {
        try {
            if (!charge()) {
                Player pl = getPlayerAttack();
                if (pl != null) {
                    this.playerSkill.skillSelect = this.getSkillAttack();
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                    false);
                        }
                        this.effectCharger();
                        try {
                            SkillService.gI().useSkill(this, pl, null);
                        } catch (Exception e) {
                            Log.error(SuperBroly.class, e);
                        }
                    } else {
                        goToPlayer(pl, false);
                    }
                    if (Util.isTrue(5, ConstRatio.PER100)) {
                        this.changeIdle();
                    }
                }
            }
        } catch (Exception ex) {
            Log.error(SuperBroly.class, ex);
        }
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            //   this.nPoint.hpg = Util.nextInt(16_000, 45_000) * 1000;
            this.nPoint.calPoint();
            this.nPoint.setFullHpMp();
            int x = Util.nextInt(50, zone.map.mapWidth - 50);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);

        }
    }

    @Override
    public Zone getMapCanJoin(int mapId) {
        return super.getMapCanJoin(mapId);
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt != null) {
                int skill = plAtt.playerSkill.skillSelect.template.id;
                if (skill == Skill.KAMEJOKO || skill == Skill.ANTOMIC || skill == Skill.MASENKO) {
                    damage = 1;
                    Service.getInstance().chat(plAtt, "Trời ơi, chưởng hoàn toàn vô hiệu lực với hắn..");
                } else if (skill == Skill.DEMON || skill == Skill.DRAGON || skill == Skill.GALICK
                        || skill == Skill.LIEN_HOAN || skill == Skill.KAIOKEN) {
                    if (damage > 400_000) {
                        damage = 400_000;
                    }
                }
            }
            return super.injured(plAtt, damage, piercing, isMobAttack);
        } else {
            return 0;
        }
    }

    @Override
    public Player getPlayerAttack() {
        if (countChangePlayerAttack < targetCountChangePlayerAttack
                && plAttack != null && plAttack.zone != null && plAttack.zone.equals(this.zone)
                && !plAttack.effectSkin.isVoHinh) {
            if (!plAttack.isDie()) {
                this.countChangePlayerAttack++;
                return plAttack;
            } else {
                plAttack = null;
            }
        } else {
            this.targetCountChangePlayerAttack = Util.nextInt(10, 20);
            this.countChangePlayerAttack = 0;
            plAttack = this.zone.getRandomPlayerInMap();
        }
        return plAttack;
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
    }

    @Override
    public void rewards(Player pl) {
        if (pl.pet == null) {
            PetService.gI().createNormalPet(pl);
        }
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

}
