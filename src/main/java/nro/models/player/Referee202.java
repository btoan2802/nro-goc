package nro.models.player;

import nro.consts.ConstIgnoreName;
import nro.models.map.Zone;
import nro.services.MapService;
import nro.utils.Util;

public class Referee202 extends Player {

    private static final short[][] RANDOM_OUTFIT = {
        {0, 1, 2}, {9, 10, 11}, {6, 16, 17},};

    private Zone z;

    @Override
    public short getHead() {
        return RANDOM_OUTFIT[this.gender][0];
    }

    @Override
    public short getBody() {
        return RANDOM_OUTFIT[this.gender][1];
    }

    @Override
    public short getLeg() {
        return RANDOM_OUTFIT[this.gender][2];
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

    @Override
    public int version() {
        return 214;
    }

    @Override
    public void update() {
    }

    public void initForAdmin(Player pla) {
        int id = 190912391;
        Referee202 pl = new Referee202();
        pl.gender = (byte) Util.nextInt(3);
        pl.id = id + Util.nextInt(-19999, 19999);
        int rdname = Util.nextInt(ConstIgnoreName.NameFor_Referee.length - 1);
        if (Util.isTrue(2, 10)) {
            pl.name = ConstIgnoreName.NameFor_Referee[rdname] + Util.nextInt(50, 75);
        } else {
            pl.name = ConstIgnoreName.NameFor_Referee[rdname];
        }
        pl.nPoint.power = Util.nextInt(2000, 1000000);
        pl.nPoint.hpMax = Util.nextInt(150, 1000);
        pl.nPoint.hpg = Util.nextInt(150);
        pl.nPoint.hp = Util.nextInt(200_000);
        pl.nPoint.setFullHpMp();
        pl.location.x = pla.location.x;
        pl.location.y = pla.location.y;
        joinMap(pla.zone, pl);
        pla.zone.setReferee(pl);
    }
}
