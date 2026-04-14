package nro.models.player;

import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.mob.Mob;
import nro.server.io.Message;
import nro.services.*;
import nro.utils.Log;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class EffectSkin {

    private static final String[] textOdo = new String[]{
        "Hôi quá", "Tránh ra đi thằng ở dơ", "Mùi gì kinh quá vậy?",
        "Kinh tởm quá", "Biến đi thằng ở dơ", "Kính ngài ở dơ"
    };

    private Player player;

    public EffectSkin(Player player) {
        this.player = player;
        this.xHPKI = 1;
    }

    public long lastTimeAttack;
    private long lastTimeOdo;
    private long lastTimeCute;
    private long lastTimeXenHutHpKi;

    public long lastTimeAddTimeTrainArmor;
    public long lastTimeSubTimeTrainArmor;

    public boolean isVoHinh;
    public boolean isHoaDa;
    public long lastTimeHoaDa;
    public int timeHoaDa;

    public long lastTimeXHPKI;
    public int xHPKI;

    public long lastTimeUpdateCTHT;
    public long lastTimeUpdateTNSMClan;
    public long lastTimeInoHashi;
    private long lastTimeEffNezuko;
    private long lastTimeEffTanjiro;
    private long lastTimeEffInoHashi;
    private long lastTimeEffZenitsu;
    private long lastTimeEffInosuke;
    public long lastTimeZenitsu;
    public long lastTimeDrabula;
    public long lastTimeNezuko;
    public int timeNezuko;
    public boolean isNezuko;
    public boolean isTanjiro;
    public boolean isZenitsu;
    public boolean isInosuke;
    public boolean isInoHashi;
    public long lastTimeTanjiro;
    public int timeTanjiro;
    public long lastTimeInosuke;
    public int timeInosuke;
    public int timeZenitsu;
    public int timeInoHashi;
    public boolean isSocola;
    public int timeSocola;
    public long lastTimeSocola;

    public boolean isCarrot;
    public int timeCarrot;
    public long lastTimesCarrot;

    public long lastTimeMabu;

    public long lastTimeBiNgo;
    public boolean isBiNgo;
    public int timeBiNgo;

    public long lastTimeBuiBui;
    public int timeSlow;
    public long lastTimeSlow;
    public boolean isSlow;
    public long lastTimexDameChuong;
    public long lastTimeInvisible;
    public int timeInvisible;
    public long lastTimeYacon;

    public void update() {
        updateVoHinh();
        if (this.player.effectSkin != null && this.player != null && this.player.zone != null
                && !this.player.zone.map.isMapOffline) {
            updateOdo();
            updateCute();
            updateXenHutXungQuanh();
            updateNezuko();
            updateTanjiro();
            updateInosuke();
            updateInoHashi();
            updateZenitsu();
            updateDrabula();
            updateMabu();
            updateBuiBui();
            updateCTTNSMClan();
            updateBiNgo();
            // setxDameChuong();
            updateYacon();
        }
        if (!this.player.isBoss && !this.player.isPet) {
            updateTrainArmor();
        }
        if (xHPKI != 1 && Util.canDoWithTime(lastTimeXHPKI, 1800000)) {
            xHPKI = 1;
            Service.getInstance().point(player);
        }
        updateCTHaiTac();
        updateCTDrSlum();
        if (isNezuko && (Util.canDoWithTime(lastTimeEffNezuko, timeNezuko))) {
            EffSkinService.gI().removeCuongNo(this.player);
        }
        if (isTanjiro && (Util.canDoWithTime(lastTimeEffTanjiro, timeTanjiro))) {
            EffSkinService.gI().removeTanjiro(this.player);
        }
        if (isInosuke && (Util.canDoWithTime(lastTimeEffInosuke, timeInosuke))) {
            EffSkinService.gI().removeInosuke(this.player);
        }
        if (isInoHashi && (Util.canDoWithTime(lastTimeEffInoHashi, timeInoHashi))) {
            EffSkinService.gI().removeInoHashi(this.player);
        }
        if (isHoaDa && (Util.canDoWithTime(lastTimeHoaDa, timeHoaDa))) {
            EffSkinService.gI().removeHoaDa(this.player);
        }
        if (isSocola && (Util.canDoWithTime(lastTimeSocola, timeSocola))) {
            EffSkinService.gI().removeSocola(this.player);
        }
        if (isBiNgo && (Util.canDoWithTime(lastTimeBiNgo, timeBiNgo))) {
            EffSkinService.gI().removeBiNgo(this.player);
        }
        if (isCarrot && (Util.canDoWithTime(lastTimesCarrot, timeCarrot))) {
            EffSkinService.gI().removeCarrot(this.player);
        }
        if (isSlow && (Util.canDoWithTime(lastTimeSlow, timeSlow))) {
            EffSkinService.gI().removeSlow(this.player);
        }
        if (player.isInvisible && (Util.canDoWithTime(lastTimeInvisible, timeInvisible))) {
            EffSkinService.gI().removeInvisible(this.player);
        }
    }

    private void updateYacon() {
        try {
            if (this.player.nPoint.wearingYacon) {
                if (Util.canDoWithTime(lastTimeYacon, 10000)) {
                    Service.getInstance().chat(this.player, "Đố anh bắt được emm");
                    EffSkinService.gI().setInvisible(this.player, System.currentTimeMillis(), 5000);
                    this.lastTimeYacon = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("update Yacon()" + e.getMessage());
        }
    }

    private void setxDameChuong() {
        try {
            if (this.player.nPoint.tlDameChuong > 0) {
                if (Util.canDoWithTime(lastTimexDameChuong, 60000)) {
                    this.player.nPoint.xDameChuong = true;
                    this.lastTimexDameChuong = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("update setxDameChuong()" + e.getMessage());
        }
    }

    private void updateBuiBui() {
        try {
            if (this.player.nPoint.wearingBuiBui) {
                if (Util.canDoWithTime(lastTimeBuiBui, 3000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        Service.getInstance().chat(pl, "Nặng vãi ò.....");
                        EffSkinService.gI().setSlow(pl, System.currentTimeMillis(), 3000);
                    }
                    this.lastTimeBuiBui = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("updateBuiBui() err: " + e.getMessage());
        }
    }

    private void updateMabu() {
        try {
            if (this.player.nPoint.wearingMabu) {
                if (Util.canDoWithTime(lastTimeMabu, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        Service.getInstance().chat(this.player, "Phẹt.....");
                        Service.getInstance().chat(pl, "hự.....");
                        EffSkinService.gI().setSocola(pl, System.currentTimeMillis(), 5000);
                        Service.getInstance().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 3780, 5000 / 1000);
                    }
                    this.lastTimeMabu = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("updateMabu() err" + e.getMessage());
        }
    }

    private void updateDrabula() {
        try {
            if (this.player.nPoint.wearingDrabula) {
                if (Util.canDoWithTime(lastTimeDrabula, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        Service.getInstance().chat(this.player, "Phọt.....");
                        Service.getInstance().chat(pl, "hự.....");
                        EffSkinService.gI().setHoaDa(pl, System.currentTimeMillis(), 5000);
                        EffSkinService.gI().sendEffectPlayer(this.player, pl, EffSkinService.TURN_ON_EFFECT,
                                EffSkinService.STONE_EFFECT);
                        Service.getInstance().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 4392, 5000 / 1000);
                    }
                    this.lastTimeDrabula = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("updateDrabula() err" + e.getMessage());
        }
    }

    private void updateBiNgo() {
        try {
            if (this.player.nPoint.wearingBiNgo) {
                if (Util.canDoWithTime(lastTimeBiNgo, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        Service.getInstance().chat(this.player, "Hóa bí ngô");
                        Service.getInstance().chat(pl, "Ôi không...");
                        EffSkinService.gI().setSocola(pl, System.currentTimeMillis(), 30000);
                        Service.getInstance().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 7057, 30000 / 1000);
                    }
                    this.lastTimeBiNgo = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("updateMabu() err" + e.getMessage());
        }
    }

    private void updateNezuko() {
        try {
            if (this.player.nPoint.wearingNezuko) {
                if (Util.canDoWithTime(lastTimeEffNezuko, 120000)) {
                    Service.getInstance().chat(this.player, "Grừuuuuuu............");
                    EffSkinService.gI().setCuongNo(this.player, System.currentTimeMillis(), 10000);
                    this.lastTimeEffNezuko = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("updateNezuko() err" + e.getMessage());
        }
    }

    private void updateTanjiro() {
        try {
            if (this.player.nPoint.wearingTanjiro) {
                if (Util.canDoWithTime(lastTimeEffTanjiro, 120000)) {
                    Service.getInstance().chat(this.player, "Hơi Thở của Nước......");
                    EffSkinService.gI().setTanjiro(this.player, System.currentTimeMillis(), 15000);
                    this.lastTimeEffTanjiro = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("updateTanjiro() err" + e.getMessage());
        }
    }

    private void updateInoHashi() {
        try {
            if (this.player.nPoint.wearingInoHashi) {
                if (Util.canDoWithTime(lastTimeEffInoHashi, 120000)) {
                    Service.getInstance().chat(this.player, "Hơi thở của dã thú....");
                    EffSkinService.gI().setInoHashi(this.player, System.currentTimeMillis(), 20000);
                    this.lastTimeEffInoHashi = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("updateInoHashi() err" + e.getMessage());
        }
    }

    private void updateInosuke() {
        try {
            if (this.player.nPoint.wearingInosuke) {
                if (Util.canDoWithTime(lastTimeEffInosuke, 120000)) {
                    Service.getInstance().chat(this.player, "Hơi thở của dã thú....");
                    EffSkinService.gI().setInosuke(this.player, System.currentTimeMillis(), 20000);

                    this.lastTimeEffInosuke = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("updateInosuke() err" + e.getMessage());
        }
    }

    private void updateZenitsu() {
        try {
            if (this.player.nPoint.wearingZenitsu) {
                if (Util.canDoWithTime(lastTimeEffZenitsu, 2000)) {
                    Service.getInstance().chat(this.player, "Khò khò.......");
                    List<Player> players = new ArrayList<>();

                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Player pl : players) {
                        Service.getInstance().chat(pl, "Hoang mang quá @@");
                    }
                    this.lastTimeEffZenitsu = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("updateZenitsu() err" + e.getMessage());
        }
    }

    private void updateCTHaiTac() {
        if (this.player.setClothes.ctHaiTac != -1
                && this.player.zone != null
                && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
            int count = 0;
            int[] cts = new int[9];
            cts[this.player.setClothes.ctHaiTac - 618] = this.player.setClothes.ctHaiTac;
            List<Player> players = new ArrayList<>();
            players.add(player);
            try {
                for (Player pl : player.zone.getNotBosses()) {
                    if (!player.equals(pl) && pl.setClothes.ctHaiTac != -1 && Util.getDistance(player, pl) <= 300) {
                        cts[pl.setClothes.ctHaiTac - 618] = pl.setClothes.ctHaiTac;
                        players.add(pl);
                    }
                }
            } catch (Exception e) {
            }
            for (int i = 0; i < cts.length; i++) {
                if (cts[i] != 0) {
                    count++;
                }
            }
            for (Player pl : players) {
                Item ct = pl.inventory.itemsBody.get(5);
                if (ct.isNotNullItem() && ct.template.id >= 618 && ct.template.id <= 626) {
                    for (ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 147
                                || io.optionTemplate.id == 77
                                || io.optionTemplate.id == 103) {
                            io.param = count * 3;
                        }
                    }
                }
                if (!pl.isPet && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
                    InventoryService.gI().sendItemBody(pl);
                }
                pl.effectSkin.lastTimeUpdateCTHT = System.currentTimeMillis();
            }
        }
    }

    private void updateCTDrSlum() {
        if (this.player.setClothes.ctDrSlum != -1
                && this.player.zone != null
                && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
            int count = 0;
            int[] cts = new int[9];
            cts[this.player.setClothes.ctDrSlum - 612] = this.player.setClothes.ctDrSlum;
            List<Player> players = new ArrayList<>();
            players.add(player);
            try {
                for (Player pl : player.zone.getNotBosses()) {
                    if (!player.equals(pl) && pl.setClothes.ctDrSlum != -1 && Util.getDistance(player, pl) <= 300) {
                        cts[pl.setClothes.ctDrSlum - 612] = pl.setClothes.ctDrSlum;
                        players.add(pl);
                    }
                }
            } catch (Exception e) {
            }
            for (int i = 0; i < cts.length; i++) {
                if (cts[i] != 0) {
                    count++;
                }
            }
            for (Player pl : players) {
                Item ct = pl.inventory.itemsBody.get(5);
                if (ct.isNotNullItem() && ct.template.id >= 612 && ct.template.id <= 614) {
                    for (ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 147 // sức đánh
                                || io.optionTemplate.id == 50 // sức đánh
                                || io.optionTemplate.id == 77 // HP
                                || io.optionTemplate.id == 103) { // KI
                            io.param = count * 10;
                        } else if (io.optionTemplate.id == 148 // Tốc chạy
                                || io.optionTemplate.id == 101 // tnsm
                                || io.optionTemplate.id == 114) { // tốc chạy
                            io.param = count * 33;
                        }
                    }
                }
                if (!pl.isPet && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
                    InventoryService.gI().sendItemBody(pl);
                }
                pl.effectSkin.lastTimeUpdateCTHT = System.currentTimeMillis();
            }
        }
    }

    private void updateCTTNSMClan() {
        if (this.player.nPoint.wearingTNSMClan
                && this.player.zone != null
                && Util.canDoWithTime(lastTimeUpdateTNSMClan, 5000)) {
            int count = 0;

            try {
                for (Player pl : player.zone.getNotBosses()) {
                    if (this.player.clan != null && !player.equals(pl) && pl.clan != null
                            && pl.clan.id == player.clan.id
                            && Util.getDistance(player, pl) <= 500) {
                        count++;
                    }
                }
            } catch (Exception e) {
            }

            Item ct = this.player.inventory.itemsBody.get(5);
            if (ct.isNotNullItem()) {
                for (ItemOption io : ct.itemOptions) {
                    if (io.optionTemplate.id == 101) { // TNSM
                        io.param = count * 20;
                    }
                }

                if (!this.player.isPet) {
                    InventoryService.gI().sendItemBody(this.player);
                }
                this.player.effectSkin.lastTimeUpdateTNSMClan = System.currentTimeMillis();
            }
        }
    }

    private void updateXenHutXungQuanh() {
        try {
            int param = this.player.nPoint.tlHutHpMpXQ;
            if (param > 0) {
                if (!this.player.isDie() && Util.canDoWithTime(lastTimeXenHutHpKi, 5000)) {
                    int hpHut = 0;
                    int mpHut = 0;
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (pl != null && !this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Mob mob : this.player.zone.mobs) {
                        if (mob != null && mob.point.getHP() > 1) {
                            if (Util.getDistance(this.player, mob) <= 200) {
                                int subHp = mob.point.getHpFull() * param / 100;
                                if (subHp >= mob.point.getHP()) {
                                    subHp = mob.point.getHP() - 1;
                                }
                                hpHut += subHp;
                                mob.injured(null, subHp, false);
                            }
                        }
                    }
                    for (Player pl : players) {
                        if (pl != null) {
                            int subHp = pl.nPoint.hpMax * param / 100;
                            int subMp = pl.nPoint.mpMax * param / 100;
                            if (subHp >= pl.nPoint.hp) {
                                subHp = pl.nPoint.hp - 1;
                            }
                            if (subMp >= pl.nPoint.mp) {
                                subMp = pl.nPoint.mp - 1;
                            }
                            hpHut += subHp;
                            mpHut += subMp;
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().Send_Info_NV(pl);
                            pl.injured(null, subHp, true, false);
                        }
                    }
                    this.player.nPoint.addHp(hpHut);
                    this.player.nPoint.addMp(mpHut);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.getInstance().Send_Info_NV(this.player);
                    this.lastTimeXenHutHpKi = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error(EffectSkin.class, e, "update xenHutXungQuanh() err");
        }
    }

    private void updateOdo() {
        try {
            int param = this.player.nPoint.tlHpGiamODo;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimeOdo, 300_000)) {
                    List<Player> players = new ArrayList<>();

                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Player pl : players) {
                        if (!pl.isMiniPet) {
                            int subHp = pl.nPoint.hpMax * param / 100;
                            if (subHp >= pl.nPoint.hp) {
                                subHp = pl.nPoint.hp - 1;
                            }
                            Service.getInstance().chat(pl, textOdo[Util.nextInt(0, textOdo.length - 1)]);
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().Send_Info_NV(pl);
                            pl.injured(null, subHp, true, false);
                        }
                    }
                    this.lastTimeOdo = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("update ODo err" + e.getMessage());
        }
    }

    // cute
    private void updateCute() {
        try {
            int param = this.player.nPoint.mpHoiCute;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimeCute, 1000)) {
                    List<Player> players = new ArrayList<>();

                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Player pl : players) {
                        if (!pl.isMiniPet) {
                            int hoiPhuc = (int) pl.nPoint.calPercent(pl.nPoint.mpMax, param);
                            if (Util.isTrue(1, 100)) {
                                Service.getInstance().chat(pl, "Wow, cute quá");
                            }
                            PlayerService.gI().hoiPhuc(pl, 0, hoiPhuc);
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().Send_Info_NV(pl);
                        }
                    }
                    int hoiPhucMe = (int) this.player.nPoint.calPercent(this.player.nPoint.mpMax, param);
                    PlayerService.gI().hoiPhuc(this.player, 0, hoiPhucMe);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.getInstance().Send_Info_NV(this.player);
                    this.lastTimeCute = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Log.error("update cute err" + e.getMessage());
        }
    }

    // giáp tập luyện
    private void updateTrainArmor() {
        if (Util.canDoWithTime(lastTimeAddTimeTrainArmor, 60000) && !Util.canDoWithTime(lastTimeAttack, 30000)) {
            if (this.player.nPoint.wearingTrainArmor) {
                Item trainArmor = this.player.inventory.trainArmor;
                if (trainArmor != null) {
                    int trainArmorID = trainArmor.template.id;
                    int maxTime = 0;
                    switch (trainArmorID) {
                        case 529:
                        case 534:
                            maxTime = 100;
                            break;
                        case 530:
                        case 535:
                            maxTime = 1000;
                            break;
                        case 531:
                        case 536:
                            maxTime = 10000;
                            break;
                        case 533:
                            maxTime = 15000;
                            break;
                    }
                    for (ItemOption io : trainArmor.itemOptions) {
                        if (io.optionTemplate.id == 9) {
                            if (io.param < maxTime) {
                                io.param++;
                                InventoryService.gI().sendItemBody(player);
                            }
                            break;
                        }
                    }
                }
            }
            this.lastTimeAddTimeTrainArmor = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeSubTimeTrainArmor, 60000)) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            for (Item item : this.player.inventory.itemsBox) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            this.lastTimeSubTimeTrainArmor = System.currentTimeMillis();
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().point(this.player);
        }
    }

    public void sendPlayerPrepareBom(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(7);
            msg.writer().writeInt((int) player.id);
            // msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(104);
            msg.writer().writeShort(affterMiliseconds);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void updateVoHinh() {
        if (this.player.nPoint.wearingVoHinh) {
            if (Util.canDoWithTime(lastTimeAttack, 5000)) {
                isVoHinh = true;
            } else {
                isVoHinh = false;
            }
        }
    }

    public void dispose() {
        this.player = null;
    }
}
