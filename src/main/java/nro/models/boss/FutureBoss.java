package nro.models.boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nro.models.player.Player;
import nro.server.Client;
import nro.services.Service;
import nro.utils.Util;

public abstract class FutureBoss extends Boss {

    public HashMap<Long, Integer> topDame = new HashMap<>();

    public FutureBoss(short id, BossData data) {
        super(id, data);
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        damage = (damage / 100) * 70;
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }
}
