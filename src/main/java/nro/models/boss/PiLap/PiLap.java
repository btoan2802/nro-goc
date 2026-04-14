package nro.models.boss.PiLap;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class PiLap extends FutureBoss {

    public PiLap() {
        super(BossFactory.PiLap, BossData.PILAP);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = new ItemMap(this.zone, 635, 1,
                pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);
        itemMap.options.add(new ItemOption(50, Util.nextInt(20, 28)));
        itemMap.options.add(new ItemOption(77, Util.nextInt(20, 28)));
        itemMap.options.add(new ItemOption(103, Util.nextInt(20, 28)));
        itemMap.options.add(new ItemOption(95, Util.nextInt(5, 10)));
        itemMap.options.add(new ItemOption(96, Util.nextInt(5, 10)));
        if (Util.isTrue(99, 100)) {
            itemMap.options.add(new ItemOption(93, Util.nextInt(1, 3)));
        }
        Service.getInstance().dropItemMap(zone, itemMap);
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        int dame = 0;
        if (this.isDie()) {
            return dame;
        } else {
            if (damage > 1) {
                damage = 1;
            }
            dame = super.injured(plAtt, damage, piercing, isMobAttack);
            if (this.isDie()) {
                notifyPlayeKill(plAtt);
                die();
            }
            return dame;
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
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{};
        this.textTalkAfter = new String[]{};
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            int x = Util.nextInt(100, this.zone.map.mapWidth - 100);
            short listBoss[] = {BossFactory.Su, BossFactory.Mai};
            CreatBossTogether(zone, listBoss, x);
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP, x);
            ServerNotify.gI().notify("Tiểu đội Pilap vừa xuất hiện tại " + this.zone.map.mapName);
        }
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.TENNIS_SPACE_SHIP);
        super.leaveMap();
        this.setJustRest();
    }
}
