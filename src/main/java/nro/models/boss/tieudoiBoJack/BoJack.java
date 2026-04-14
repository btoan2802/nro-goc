package nro.models.boss.tieudoiBoJack;

import nro.consts.ConstItem;
import nro.consts.ConstOption;
import nro.models.boss.*;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.ServerNotify;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class BoJack extends FutureBoss {

    public BoJack() {
        super(BossFactory.BOJACK, BossData.BOJACK);
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
            }
            return dame;
        }
    }
    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        int x = this.location.x;
        if (x < 0 || x >= this.zone.map.mapWidth) {
            return;
        }
        int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

        itemMap = new ItemMap(this.zone, 427, 1, x, y, pl.id);
        itemMap.options.add(new ItemOption(ConstOption.SUC_DANH_PT, Util.nextInt(15, 23)));
        itemMap.options.add(new ItemOption(ConstOption.HP_PT, Util.nextInt(15, 23)));
        itemMap.options.add(new ItemOption(ConstOption.KI_PT, Util.nextInt(15, 23)));
        itemMap.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 5)));
        itemMap.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 10)));
        // if (Util.isTrue(3, 5)) {
        // itemMap.options.add(new ItemOption(236, 1));
        // }
        itemMap.options.add(new ItemOption(ConstOption.HSD_NGAY, Util.nextInt(1, 2)));

        if (itemMap != null) {
            Service.getInstance().dropItemMap(zone, itemMap);
        }

        if (!generalRewards(pl, (byte) 8, (byte) 10)) {
        }

    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (!troi()) {
                if (pl != null && !pl.isDie() && !pl.isMiniPet) {
                    this.playerSkill.skillSelect = this.getSkillAttack();
                    if (!effectSkill.useTroi) {
                        if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                            if (SkillUtil.isUseSkillChuong(this)) {
                                goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                        Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                        false);

                            }
                            // this.effectTroi();
                            try {
                                SkillService.gI().useSkill(this, pl, null);
                            } catch (Exception e) {
                                Log.error(S_Bojack.class, e);
                            }
                            checkPlayerDie(pl);
                        } else {
                            goToPlayer(pl, false);
                        }
                    }
                }

            } else {
                this.chat("Bố mài trói mài nè con!!!");
            }
        } catch (Exception ex) {
            Log.error(Boss.class, ex);
        }
    }

    protected boolean troi() {
        if (this.effectSkill.useTroi && Util.isTrue(5, 100)) {
            this.effectSkill.useTroi = false;
            return false;
        }
        if (Util.isTrue(10, 20)) {
            for (Skill skill : this.playerSkill.skills) {
                if (skill.template.id == Skill.TROI) {
                    this.playerSkill.skillSelect = skill;
                    if (SkillService.gI().canUseSkillWithCooldown(this)
                            && SkillService.gI().useSkill(this, plAttack, null)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void initTalk() {
        this.textTalkMidle = new String[]{"|-1|Ta là Bojack", "|-1|Ê cố lên nhóc",
            "|-1|Chán", "|-1|Ta có nhầm không nhỉ"};
    }

    @Override
    public void leaveMap() {
        CreatBossLastDie(BossFactory.S_BOJACK, expoff);
        super.leaveMap();
        BossManager.gI().removeBoss(this);

    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            short listBossTogether[] = {BossFactory.BIDO, BossFactory.BUJIN, BossFactory.KOGU, BossFactory.ZANGYA};
            int x = Util.nextInt(100, zone.map.mapWidth - 100);
            CreatBossTogether(zone, listBossTogether, x);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Boss " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        }
    }

}
