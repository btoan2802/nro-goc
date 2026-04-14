package nro.models.boss;

import nro.models.player.Player;

public abstract class BossDontReset extends Boss {

    public BossDontReset(short id, BossData data) {
        super(id, data);
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void setJustRest() {
        this.setJustRestToFuture();
    }

}
