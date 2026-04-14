package nro.models.boss.event;

import java.util.Random;
import nro.jdbc.daos.PlayerDAO;

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
public class Tuanloc extends Boss {

    public Tuanloc() {
        super(BossFactory.TUANLOC, BossData.TUANLOC);
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
    public void rewards(Player plKill) {
        int[] itemDos = new int[]{649, 1165};
        int randomDo = new Random().nextInt(itemDos.length);
        if (Util.isTrue(100, 100)) {
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], Util.nextInt(1,3), this.location.x, this.location.y, plKill.id));
        }
        PlayerDAO.addDiemTuanLoc(plKill, plKill.id, 1);
plKill.diemtuanloc++; // chỉ để hiển thị

    }

   
    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }
 @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, 50, piercing, isMobAttack);
    }

    @Override
    public void initTalk() {

        this.textTalkMidle = new String[]{
            "|-1|Cho kẹo hay bị ghẹo??",};
        this.textTalkAfter = new String[]{"|-1|Ta sẽ sớm quay lại"};
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
    }

}
