package nro.models.boss.cell;

import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class XenBoHung extends FutureBoss {

    public XenBoHung() {
        super(BossFactory.XEN_BO_HUNG, BossData.XEN_BO_HUNG);
    }

    public boolean callXen = false;

    @Override
    protected boolean useSpecialSkill() {
        this.playerSkill.skillSelect = this.getSkillSpecial();
        if (SkillService.gI().canUseSkillWithCooldown(this)) {
            SkillService.gI().useSkill(this, null, null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void attack() {
        if (BossManager.gI().getBossById(BossFactory.XEN_CON) != null) {
            PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
            this.changeIdle();
            return;
        }

        // FIX: Nếu chết theo luồng attack() thì vẫn phải rewards để rơi đồ + tính NV
        if (this.isDie()) {
            try {
                rewards(getPlayerAttack()); // có thể null, rewards() đã check null
            } catch (Exception e) {
                Log.error(XenBoHung.class, e);
            }
            tuSat();
            die();
            return;
        }

        try {
            Player pl = getPlayerAttack();
            if (pl != null) {
                if (!useSpecialSkill()) {
                    this.playerSkill.skillSelect = this.getSkillAttack();
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50),
                                    false);
                        }
                        SkillService.gI().useSkill(this, pl, null);
                        checkPlayerDie(pl);
                    } else {
                        goToPlayer(pl, false);
                    }
                }
            }
        } catch (Exception ex) {
            Log.error(XenBoHung.class, ex);
        }
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (this.isDie() || this.playerSkill.prepareTuSat) {
            return 0;
        } else {

            if (!callXen && this.nPoint.hp < 400000000) {
                this.callXen();
                return 0;
            }

            int dame = super.injuredNotCheckDie(plAtt, damage, piercing);

            // FIX CHÍNH: chết là PHẢI rewards() -> mới rơi đồ + tính NV, sau đó die() 1 lần
            if (this.isDie()) {
                try {
                    notifyPlayeKill(plAtt);
                } catch (Exception e) {
                    Log.error(XenBoHung.class, e);
                }

                try {
                    rewards(plAtt);
                } catch (Exception e) {
                    Log.error(XenBoHung.class, e);
                }

                die();
            }

            return dame;
        }
    }

    public void callXen() {
        Service.getInstance().chat(this, "Những đứa con của ta, hãy tiêu diệt bọn chúng");
        this.changeToIdle();
        this.callXen = true;

        short listBossTogether[] = {BossFactory.XEN_CON_1, BossFactory.XEN_CON_2, BossFactory.XEN_CON_3,
            BossFactory.XEN_CON_4, BossFactory.XEN_CON_5, BossFactory.XEN_CON_6, BossFactory.XEN_CON_7};
        int x = Util.nextInt(100, zone.map.mapWidth - 100);
        CreatBossTogether(zone, listBossTogether, x);

    }

    public void callBack() {
        if (checkXen()) {
            Service.getInstance().chat(this, "Vậy thì ta sẽ tiếp chiêu các ngươi");
            this.changeToAttack();
        }

    }

    private boolean checkXen() {
        for (int i = 0; i < 7; i++) {
            Boss cellCon = null;
            cellCon = BossManager.gI().getBossById((short) (BossFactory.XEN_CON_1 - (short) i));
            if (cellCon != null && !cellCon.isDie()) {
                return false;
            }
        }
        return true;
    }

    private void tuSat() {
        try {
            this.nPoint.hpg = 2000000000;
            this.nPoint.hp = 1;
            ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
            PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
            PlayerService.gI().changeTypePK(this, ConstPlayer.PK_ALL);
            this.playerSkill.skillSelect = this.getSkillById(Skill.TU_SAT);
            SkillService.gI().useSkill(this, null, null);
            Thread.sleep(3000);
            SkillService.gI().useSkill(this, null, null);
        } catch (Exception e) {
            Log.error(XenBoHung.class, e);
        }
    }

    @Override
    public void joinMap() {
        if (BossManager.gI().getBossById(BossFactory.SIEU_BO_HUNG) == null) {
            super.joinMap();
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void rewards(Player pl) {
        if (pl != null) {
            try {
                TaskService.gI().checkDoneTaskKillBoss(pl, this);
                try {
                    int x = this.location.x + 14;
                    if (x < 0 || x >= this.zone.map.mapWidth) {
                        return;
                    }
                    int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);

                    ItemMap itemMap = new ItemMap(this.zone, 17, 1, x, y, pl.id);
                    if (itemMap != null) {
                        Service.getInstance().dropItemMap(zone, itemMap);
                    }
                    generalRewards(pl, (byte) 11, (byte) 25);
                } catch (Exception e) {
                    Log.error(XenBoHung.class, e);
                }
                BossPointEven(pl);
            } catch (Exception e) {
                Log.error(XenBoHung.class, e);
            }
        }
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void doneChatS() {
        this.changeToAttack();
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"|-1|Ta cho các ngươi 5 giây để chuẩn bị", "|-1|Cuộc chơi bắt đầu.."};
        this.textTalkMidle = new String[]{"|-1|Kame Kame Haaaaa!!",
            "|-1|Mi khá đấy nhưng so với ta chỉ là hạng tôm tép",
            "|-1|Tất cả nhào vô hết đi", "|-1|Cứ chưởng tiếp đi. haha",
            "|-1|Các ngươi yếu thế này sao hạ được ta đây. haha",
            "|-1|Khi công pháo!!", "|-1|Cho mi biết sự lợi hại của ta"};
        this.textTalkAfter = new String[]{};
    }

    @Override
    public void leaveMap() {
        callXen = false;
        CreatBossLastDie(BossFactory.SIEU_BO_HUNG, this.location.x);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
