package nro.models.player;

import java.util.Arrays;
import java.util.List;
import nro.consts.ConstIgnoreName;
import nro.consts.ConstMap;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.server.Manager;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Util;

public class Referee101 extends Player {

    private long lastTimeMove;
    private static final short[][] RANDOM_OUTFIT = {
        {1293, 1294, 1295},
        {1095, 1096, 1097}, {1098, 1099, 1100}, {1101, 1102, 1103},
        {1290, 1291, 1292}, {1287, 1288, 1289}, {1302, 1303, 1304},
        {1293, 1294, 1295}, {736, 737, 738}, {642, 643, 644},
        {499, 500, 501}, {573, 574, 575}, {579, 580, 581},
        {576, 577, 578}, {566, 567, 568}, {264, 265, 266},
        {377, 378, 379}, {353, 354, 355},
        {630, 631, 632}
    };

    public void initReferee() {
        init();
    }

    private Zone z;
    int rd = Util.nextInt(RANDOM_OUTFIT.length - 1);

    @Override
    public short getHead() {
        return RANDOM_OUTFIT[rd][0];
    }

    @Override
    public short getBody() {
        return RANDOM_OUTFIT[rd][1];
    }

    @Override
    public short getLeg() {
        return RANDOM_OUTFIT[rd][2];
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

    @Override
    public int version() {
        return 214;
    }

    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(50, 100);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
    }

    @Override
    public void update() {

    }

    private void init() {
        int id = 999;
        for (Map m : Manager.MAPS) {
            if (m.mapId == ConstMap.DAO_KAME) {
                for (Zone z : m.zones) {
                    if (z.zoneId < 5) {
                        for (int i = 0; i < 5; i++) {
                            Referee101 pl = new Referee101();
                            pl.gender = 0;
                            pl.id = id++;
                            int rdname = Util.nextInt(ConstIgnoreName.NameFor_Referee.length - 1);
                            pl.name = ConstIgnoreName.NameFor_Referee[rdname] + Util.nextInt(50, 75);
                            pl.nPoint.power = Util.nextInt(200_000_000);
                            pl.nPoint.hpMax = Util.nextInt(200_000);
                            pl.nPoint.hpg = Util.nextInt(200_000);
                            pl.nPoint.hp = Util.nextInt(200_000);
                            pl.nPoint.setFullHpMp();
                            pl.location.x = Util.nextInt(946, 1234);
                            pl.location.y = 408;
                            joinMap(z, pl);
                            z.setReferee(pl);
                        }
                    }
                }
            } else if (m.mapId == ConstMap.LANG_ARU) {
                for (Zone z : m.zones) {
                    if (z.zoneId < 3) {
                        for (int i = 0; i < 7; i++) {
                            Referee101 pl = new Referee101();
                            pl.gender = 0;
                            pl.id = id++;
                            int rdname = Util.nextInt(ConstIgnoreName.NameFor_Referee.length - 1);
                            pl.name = ConstIgnoreName.NameFor_Referee[rdname] + Util.nextInt(32, 50);
                            pl.nPoint.power = Util.nextInt(200_000_000);
                            pl.nPoint.hpMax = Util.nextInt(200_000);
                            pl.nPoint.hpg = Util.nextInt(200_000);
                            pl.nPoint.hp = Util.nextInt(200_000);
                            pl.nPoint.setFullHpMp();
                            pl.location.x = Util.nextInt(215, 632);
                            pl.location.y = 432;
                            joinMap(z, pl);
                            z.setReferee101(pl);
                        }
                    }
                }
            } else if (m.mapId == ConstMap.LANG_KAKAROT) {
                for (Zone z : m.zones) {
                    if (z.zoneId < 3) {
                        for (int i = 0; i < 7; i++) {
                            Referee101 pl = new Referee101();
                            pl.gender = 0;
                            pl.id = id++;
                            int rdname = Util.nextInt(ConstIgnoreName.NameFor_Referee.length - 1);
                            pl.name = ConstIgnoreName.NameFor_Referee[rdname] + Util.nextInt(9, 32);
                            pl.nPoint.power = Util.nextInt(200_000_000);
                            pl.nPoint.hpMax = Util.nextInt(200_000);
                            pl.nPoint.hpg = Util.nextInt(200_000);
                            pl.nPoint.hp = Util.nextInt(200_000);
                            pl.nPoint.setFullHpMp();
                            pl.location.x = Util.nextInt(242, 917);
                            pl.location.y = 408;
                            joinMap(z, pl);
                            z.setReferee101(pl);
                        }
                    }
                }
            } else if (m.mapId == ConstMap.LANG_MORI) {
                for (Zone z : m.zones) {
                    if (z.zoneId < 3) {
                        for (int i = 0; i < 7; i++) {
                            Referee101 pl = new Referee101();
                            pl.gender = 0;
                            pl.id = id++;
                            int rdname = Util.nextInt(ConstIgnoreName.NameFor_Referee.length - 1);
                            pl.name = ConstIgnoreName.NameFor_Referee[rdname] + Util.nextInt(0, 9);
                            pl.nPoint.power = Util.nextInt(200_000_000);
                            pl.nPoint.hpMax = Util.nextInt(200_000);
                            pl.nPoint.hpg = Util.nextInt(200_000);
                            pl.nPoint.hp = Util.nextInt(200_000);
                            pl.nPoint.setFullHpMp();
                            pl.location.x = Util.nextInt(299, 956);
                            pl.location.y = 432;
                            joinMap(z, pl);
                            z.setReferee101(pl);
                        }
                    }
                }
            }
        }
    }

    public void initForAdmin(Player pla) {
        int id = 19101019;
        Referee101 pl = new Referee101();
        pl.gender = 0;
        pl.id = id + Util.nextInt(-1999, 1999);
        int rdname = Util.nextInt(ConstIgnoreName.NameFor_Referee.length - 1);
        pl.name = ConstIgnoreName.NameFor_Referee[rdname] + Util.nextInt(0, 999);
        pl.nPoint.power = Util.nextInt(10_000_000, 2_000_000_000);
        pl.nPoint.hpMax = Util.nextInt(200_000);
        pl.nPoint.hpg = Util.nextInt(200_000);
        pl.nPoint.hp = Util.nextInt(200_000);
        pl.nPoint.setFullHpMp();
        pl.location.x = pla.location.x;
        pl.location.y = pla.location.y;
        joinMap(pla.zone, pl);
        pla.zone.setReferee(pl);
    }
}
