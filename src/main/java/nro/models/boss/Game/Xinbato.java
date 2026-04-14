/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.models.boss.Game;

import nro.consts.ConstMap;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;
import org.apache.log4j.Logger;

public class Xinbato extends Boss {

    private int mapID;

    private boolean checkNhatXuong = false;
    private long lastTimeNhatXuong = 0;
    private long lastTimRestPawn;

    public Xinbato() {
        super(BossFactory.XINBATO_EVENT, BossData.XINBATO_EVENT);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {

    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    public void NhatXuong() {
        checkNhatXuong = true;
        lastTimeNhatXuong = System.currentTimeMillis();
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"|-1|Quê hương tôi bị vỡ ống nước",
            "|-1|xin hãy giúp đỡ dân làng chúng tôi", "|-1|Khát nước quá", "|-1|Hãy cho tôi nước"};
        this.textTalkAfter = new String[]{};
    }

    @Override
    public void changeToAttack() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
        changeStatus(ATTACK);
    }

    @Override
    public void joinMap() {
        this.zone = getMapCanJoin(mapID);
        int x = Util.nextInt(50, this.zone.map.mapWidth - 50);
        ChangeMapService.gI().changeMap(this, this.zone, x, this.zone.map.yPhysicInTop(x, 0));
        this.lastTimRestPawn = System.currentTimeMillis();
        this.lastTimeNhatXuong = 0;
        this.checkNhatXuong = false;
    }

    public boolean checkNhatXuong() {
        return checkNhatXuong;
    }

    @Override
    public void attack() {
        if (lastTimeNhatXuong > 0) {
            if (Util.canDoWithTime(lastTimeNhatXuong, 5000)) {
                lastTimeNhatXuong = 0;
                checkNhatXuong = false;
                super.leaveMap();
                setJustRest();
                changeStatus(DIE);
                // super.respawn();
                // setJustRestToFuture();
            }
        }
        if (Util.canDoWithTime(lastTimRestPawn, 180000)) {
            lastTimRestPawn = System.currentTimeMillis();
            super.leaveMap();
            setJustRest();
            changeStatus(DIE);
            // super.respawn();
        }
        if (Util.isTrue(50, ConstRatio.PER100)) {
            int x = location.x + Util.nextInt(-50, 50);
            if (this.zone == null) {
                return;
            }
            if (x < 35) {
                x = 35;
            } else if (x > this.zone.map.mapWidth - 35) {
                x = this.zone.map.mapWidth - 35;
            }
            int y = location.y;
            if (location.y > 50) {
                y = this.zone.map.yPhysicInTop(x, y - 50);
            }
            goToXY(x, y, false);
        }

    }

    @Override
    public boolean talk() {
        switch (status) {
            case TALK_BEFORE:
                if (this.textTalkBefore == null || this.textTalkBefore.length == 0) {
                    return true;
                }
                if (Util.canDoWithTime(lastTimeTalk, 3000)) {
                    if (indexTalkBefore < textTalkBefore.length) {
                        this.chat(textTalkBefore[indexTalkBefore++]);
                        if (indexTalkBefore >= textTalkBefore.length) {
                            return true;
                        }
                        lastTimeTalk = System.currentTimeMillis();
                    } else {
                        return true;
                    }
                }
                break;
            case IDLE:
            case ATTACK:
                if (this.textTalkMidle == null || this.textTalkMidle.length == 0) {
                    return true;
                }
                if (Util.canDoWithTime(lastTimeTalk, Util.nextInt(5000, 8000))) {
                    this.chat(textTalkMidle[Util.nextInt(0, textTalkMidle.length - 1)]);
                    lastTimeTalk = System.currentTimeMillis();
                }
                break;
            case TALK_AFTER:
                if (this.textTalkAfter == null || this.textTalkAfter.length == 0) {
                    return true;
                }
                if (Util.canDoWithTime(lastTimeTalk, 2000)) {
                    this.chat(textTalkAfter[indexTalkAfter++]);
                    if (indexTalkAfter >= textTalkAfter.length) {
                        return true;
                    }
                    if (indexTalkAfter > textTalkAfter.length - 1) {
                        indexTalkAfter = 0;
                    }
                    lastTimeTalk = System.currentTimeMillis();
                }
                break;
        }
        return false;
    }

}
