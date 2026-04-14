package nro.models.player;

import nro.attr.Attribute;
import nro.card.Card;
import nro.card.CollectionBook;
import nro.consts.ConstAttribute;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.clan.Buff;
import nro.models.intrinsic.Intrinsic;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.skill.Skill;
import nro.power.PowerLimit;
import nro.power.PowerLimitManager;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.services.*;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class NPoint {

    public static final byte MAX_LIMIT = 10;

    private Player player;
    public boolean isCrit;
    public boolean isCrit100;

    private Intrinsic intrinsic;
    private int percentDameIntrinsic;
    public int dameAfter;

    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;

    public byte limitPower;
    public long power;
    public long tiemNang;

    public int hp, hpMax, hpg;
    public int mp, mpMax, mpg;
    public int dame, dameg;
    public int def, defg;
    public int crit, critg;
    public byte speed = 5;
    public int hpMokey;
    public boolean teleport;

    /**
     * Chỉ số cộng thêm
     */
    public int hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd;

    /**
     * //+#% sức đánh chí mạng
     */
    public List<Integer> tlDameCrit;

    public boolean buffExpSatellite, buffDefenseSatellite;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Integer> tlHp, tlMp;

    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Integer> tlDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Integer> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public int hpHoi, mpHoi, mpHoiCute;

    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public short tlHutHp, tlHutMp, tlHutHpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public short tlHutHpMpXQ;
    /**
     * Tỉ lệ sức đánh cho đệ tử
     */
    public short tlDameDeTu;
    /**
     * Tỉ lệ phản sát thương
     */
    public short tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Integer> tlTNSM;
    public int tlTNSMPet;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public short tlNeDon;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public List<Integer> tlSDDep;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;
    public List<Integer> tlSpeed;
    public int mstChuong;
    public int tlGiamst;
    /**
     * Chỉ số cộng thêm
     */
    public int dameSKillSpecical;
    /**
     * Thời gian hồi chiêu cuối
     */
    public int timeSKillSpecical;
    /**
     * Chỉ số dame hấp thụ
     */
    public int dameHapThu;
    /**
     * Tỉ lệ dame % máu của dối phương
     */
    public short tlDameHpTarger, tlDameChuongHpTarger;
    /**
     * Giảm #% thời gian bị mù
     */
    public short tlGiamChoang;
    /**
     * Giảm #% thời gian bị mù
     */
    public short tlSatThuongLua;
    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public short tlXuyenGiapCanChien, tlXuyenGiapChuong;
    /**
     * Tấn công+#% lên Boss
     */
    public short tlDameBoss;
    /**
     * + #% đấm Dragon, Galick, Demin
     */
    public short tlDameSkillDam;
    /**
     * Sự kiện
     */

    public short tlRoiEvent_id;
    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wornTrainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;

    public short tlHpGiamODo;

    private PowerLimit powerLimit;
    public boolean wearingDrabula;
    public boolean wearingMabu;
    public boolean wearingBuiBui;

    public boolean wearingNezuko;
    public boolean wearingTanjiro;
    public boolean wearingInosuke;
    public boolean wearingInoHashi;
    public boolean wearingZenitsu;
    public int tlDameChuong;
    public boolean xDameChuong;
    public boolean wearingYacon;
    public boolean wearingRedNoelHat;
    public boolean wearingGrayNoelHat;
    public boolean wearingBlueNoelHat;
    public boolean wearingNoelHat;
    public boolean isBocPha;
    public boolean isHaDocDoiThu;
    public boolean wearingCarrot;
    public boolean wearingKhangTDHS;
    public boolean wearingTNSMClan;
    public boolean wearingBiNgo;
    public boolean wearingBongBang;
    public int tlPhatNo;

    public boolean isDoPhaLe;

    public NPoint(Player player) {
        this.player = player;
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlSDDep = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
        this.tlSpeed = new ArrayList<>();
    }

    public int getexp() {
        int[] expTable = {5000, 10000, 20000, 40000, 80000, 120000, 240000, 500000};
        if (player.typetrain >= 0 && player.typetrain < expTable.length) {
            return expTable[player.typetrain];
        } else {
            return 0;
        }
    }

    public void initPowerLimit() {
        powerLimit = PowerLimitManager.getInstance().get(limitPower);
    }

    /*-------------------------------------------------------------------------*/
    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {
        try {
            if (this.player.pet != null) {
                this.player.pet.nPoint.setPointWhenWearClothes();
            }
            this.setPointWhenWearClothes();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPoint(ItemOption io) {
        switch (io.optionTemplate.id) {
            case 0: // Tấn công +#

                this.dameAdd += io.param;
                break;
            case 2: // HP, KI+#000
                this.hpAdd += io.param * 1000;
                this.mpAdd += io.param * 1000;
                break;
            case 3: // vô hiệu vả biến st chưởng thành ki
                this.mstChuong += io.param;
                break;
            case 5: // +#% sức đánh chí mạng

                this.tlDameCrit.add(io.param);
                break;
            case 6: // HP+#
                this.hpAdd += io.param;
                break;
            case 7: // KI+#
                this.mpAdd += io.param;
                break;
            case 8: // Hút #% HP, KI xung quanh mỗi 5 giây
                this.tlHutHpMpXQ += io.param;
                break;
            case 14: // Chí mạng+#%

                this.critAdd += io.param;
                break;
            case 19: // Tấn công+#% khi đánh quái
                this.tlDameAttMob.add(io.param);
                break;
            case 22: // HP+#K

                this.hpAdd += io.param * 1000;
                break;
            case 23: // MP+#K

                this.mpAdd += io.param * 1000;
                break;
            case 24:
                this.wearingBuiBui = true;
                break;
            case 25:
                this.wearingYacon = true;
                break;
            case 26:
                this.wearingDrabula = true;
                this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                break;
            case 29:
                this.wearingMabu = true;
                break;
            case 27: // +# HP/30s
                this.hpHoiAdd += io.param;
                break;
            case 28: // +# KI/30s
                this.mpHoiAdd += io.param;
                break;
            case 33: // dịch chuyển tức thời
                this.teleport = true;
                break;
            case 47: // Giáp+#
                this.defAdd += io.param;
                break;
            case 48: // HP/KI+#
                this.hpAdd += io.param;
                this.mpAdd += io.param;
                break;
            case 49: // Tấn công+#%
            case 50: // Sức đánh+#%
            case 221:// Bóng tối Luyện Ngục +#% KI

                this.tlDame.add(io.param);
                break;
            case 77: // HP+#%
            case 197: // HP+#% ( không cùng với ép spl)
            case 219: // Bóng tối Ma vương +#% sức đánh

                this.tlHp.add(io.param);
                break;
            case 74:// tương đương với id event
                this.tlRoiEvent_id = (short) io.param;
                break;
            case 98: // Xuyên giáp #% chưởng
                this.tlXuyenGiapChuong += io.param;
                break;
            case 99: // Xuyên giáp #% cận chiến
                this.tlXuyenGiapCanChien += io.param;
                break;
            case 175: // +#% giảm thời gian mù
                this.tlGiamChoang += io.param;
                break;
            case 80: // HP+#%/30s
                this.tlHpHoi += io.param;
                break;
            case 81: // MP+#%/30s
                this.tlMpHoi += io.param;
                break;
            case 88: // Cộng #% exp khi đánh quái
                this.tlTNSM.add(io.param);
                break;
            case 94: // Giáp #%
                this.tlDef.add(io.param);
                break;
            case 95: // Biến #% tấn công thành HP
                this.tlHutHp += io.param;
                break;
            case 96: // Biến #% tấn công thành MP
                this.tlHutMp += io.param;
                break;
            case 97: // Phản #% sát thương
                this.tlPST += io.param;
                break;
            case 79: // Đệ tử #% sức đánh
                this.tlDameDeTu += io.param;
                break;
            case 100: // +#% vàng từ quái
                this.tlGold += io.param;
                break;
            case 101: // +#% TN,SM

                this.tlTNSM.add(io.param);
                break;
            case 103: // %# KI
            case 198: // %# KI (không cùng với ép spl)
            case 220:// Bóng tối Quỷ vương +#% HP

                this.tlMp.add(io.param);
                break;
            case 104: // Biến #% tấn công quái thành HP
                this.tlHutHpMob += io.param;
                break;
            case 105: // Vô hình khi không đánh quái và boss
                this.wearingVoHinh = true;
                break;
            case 106: // Không ảnh hưởng bởi cái lạnh
                this.isKhongLanh = true;
                break;
            case 108: // #% Né đòn
                this.tlNeDon += io.param;
                break;
            case 109: // Hôi, giảm #% HP
                this.tlHpGiamODo += io.param;
                break;
            case 110:
                this.isDoPhaLe = true;
                break;
            case 114:
                this.tlSpeed.add(io.param);
                break;
            case 115: // Biến cà rốt
                this.wearingCarrot = true;
                break;
            case 163: // Biến người xung quanh thành Bí Ngô
                this.wearingBiNgo = true;
                break;
            case 116: // Kháng TDHS
                this.wearingKhangTDHS = true;
                break;
            case 117: // Đẹp +#% SĐ cho mình và người xung quanh
                this.tlSDDep.add(io.param);
                break;
            case 147: // +#% sức đánh
                this.tlDame.add(io.param);
                break;
            case 153: //#% tỉ lệ phát nổ sau khi chết
                this.tlPhatNo = io.param;
                break;
            case 156: // Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                this.tlSubSD += 50;
                this.tlTNSM.add(io.param);
                this.tlGold += io.param;
                break;
            case 160:
                this.tlTNSMPet += io.param;
                break;
            case 162: // Cute hồi #% KI/s bản thân và xung quanh
                this.mpHoiCute += io.param;
                break;
            case 173: // Phục hồi #% HP và KI cho đồng đội
                this.tlHpHoiBanThanVaDongDoi += io.param;
                this.tlMpHoiBanThanVaDongDoi += io.param;
                break;
            case 189:
                this.wearingNezuko = true;
                break;
            case 190:
                this.wearingTanjiro = true;
                break;
            case 191:
                this.wearingInoHashi = true;
                break;
            case 192:
                this.wearingInosuke = true;
                break;
            case 193:
                this.wearingZenitsu = true;
                break;
            case 194:
                this.tlDameChuong = 3;
                break;
            case 195:
                this.tlDameChuong = 4;
                break;
            case 201: // + #% đấm Dragon
            case 202: // + #% đấm Demon
            case 203: // + #% đấm Galick
                this.tlDameSkillDam += io.param;
                break;
            case 212: // Tấn công+#% lên Boss
                this.tlDameBoss += io.param;
                break;
            case 241:// skill đặc biệt gây dame thêm
                this.dameSKillSpecical += io.param;
                break;
            case 232:// gây sát thương thêm % hp đối phương
                this.tlDameHpTarger += io.param;
                break;
            case 165:// Sát thương lửa
                this.tlSatThuongLua += io.param;
                break;
            case 168:// Hấp thụ sức mạnh rồi bộc phá
                this.isBocPha = true;
                break;
            case 169:// Cơ hội hạ độc đối thủ
                this.isHaDocDoiThu = true;
                break;

            case 225:// $(Ở gần mỗi 1 thành viên bang +20% tiềm năng sức mạnh)
                this.wearingTNSMClan = true;
                break;
            // case 254:// Ngũ sắc: +#% thời gian hồi chiêu cuối
            // this.timeSKillSpecical += io.param;
            // break;

        }
    }

    private void setPointWhenWearClothes() {
        resetPoint();
        for (Item item : this.player.inventory.itemsBody) {
            if (item.isNotNullItem()) {
                int tempID = item.template.id;
                if (tempID >= 592 && tempID <= 594) {
                    teleport = true;
                }
                for (ItemOption io : item.itemOptions) {
                    setPoint(io);
                }
            }
        }
        List<Item> itemsBody = player.inventory.itemsBody;
        // if (!player.isBoss && !player.isMiniPet) {
        // Item pants = itemsBody.get(1);
        // if (pants.isNotNullItem() && pants.getId() >= 691 && pants.getId() >= 693) {
        // player.event.setUseQuanHoa(true);
        // }
        // }
        if (Manager.EVENT_SEVER == 3) {
            if (!this.player.isBoss && !this.player.isMiniPet) {
                if (itemsBody.get(5).isNotNullItem()) {
                    int tempID = itemsBody.get(5).getId();
                    switch (tempID) {
                        case 450:
                        case 451:
                            wearingBongBang = true;
                        case 386:
                        case 389:
                        case 392:
                            wearingGrayNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        case 387:
                        case 390:
                        case 393:
                            wearingRedNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        case 388:
                        case 391:
                        case 394:
                            wearingBlueNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        default:
                            wearingRedNoelHat = false;
                            wearingBlueNoelHat = false;
                            wearingGrayNoelHat = false;
                            wearingNoelHat = false;
                    }
                }
            }
        }
        CollectionBook book = player.getCollectionBook();

        if (book != null) {
            List<Card> cards = book.getCards();
            if (cards != null) {
                for (Card c : cards) {
                    if (c.getLevel() > 0) {
                        int index = 0;
                        for (ItemOption o : c.getCardTemplate().getOptions()) {
                            if ((index == 0 || c.isUse()) && c.getLevel() >= o.activeCard) {
                                setPoint(o);
                            }
                            index++;
                        }
                    }
                }
            }
        }
        if (this.player.fusion != null) {
            if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                    || this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                    || this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                Item btc = InventoryService.gI().findItemBag(this.player,
                        this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 ? 921
                                : this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 ? 1451 : 1608);
                if (btc != null && btc.itemOptions.size() > 0) {
                    for (ItemOption io : btc.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: // Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: // Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: // HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: // HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: // MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: // Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: // KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: // #% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 101: // #% Né đòn
                                this.tlTNSM.add(io.param);
                                break;
                            case 5: // +#% sức đánh chí mạng
                                this.tlDameCrit.add(io.param);
                                break;
                        }
                    }
                }
            }
        }

        setDameTrainArmor();
        setBasePoint();
    }

    private void setDameTrainArmor() {
        if (!this.player.isPet && !this.player.isBoss && !this.player.isMiniPet) {
            try {
                if (player.inventory.itemsBody == null) {
                    return;
                }
                Item gtl = this.player.inventory.itemsBody.get(6);
                if (gtl.isNotNullItem()) {
                    this.wearingTrainArmor = true;
                    this.wornTrainArmor = true;
                    this.player.inventory.trainArmor = gtl;
                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
                } else {
                    if (this.wornTrainArmor) {
                        this.wearingTrainArmor = false;
                        for (ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                            if (io.optionTemplate.id == 9 && io.param > 0) {
                                this.tlDame
                                        .add(ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Logger.logException(NPoint.class, e, "Lỗi giáp luyện tập" + this.player.name);
            }
        }
    }

    private void setNeDon() {
        // ngọc rồng đen 6 sao
        // if (this.player.rewardBlackBall.timeOutOfDateReward[5] >
        // System.currentTimeMillis()) {
        // this.tlNeDon += RewardBlackBall.R6S;
        // }
    }

    private void setHpHoi() {
        this.hpHoi = (int) calPercent(this.hpMax, 1);
        this.hpHoi += this.hpHoiAdd;
        this.hpHoi += calPercent(this.hpMax, this.tlHpHoi);
        this.hpHoi += calPercent(this.hpMax, this.tlHpHoiBanThanVaDongDoi);
        if (this.player.effectSkin.isNezuko) {
            this.hpHoi += calPercent(this.hpMax, 3);
        }
        if (this.player.fusion.typeFusion != 0) {
            if (this.player.pet != null) {
                if (PetService.gI().isDeTuNangCap(this.player.pet)) {
                    // if (this.player.pet.isMabu) {
                    // this.hpHoi += calPercent(this.hpMax, 15);
                    // }
                }
            }
        }
    }

    private void setMpHoi() {
        this.mpHoi = (int) calPercent(this.mpMax, 1);
        this.mpHoi += this.mpHoiAdd;
        this.mpHoi += calPercent(this.mpMax, this.tlMpHoi);
        this.mpHoi += calPercent(this.mpMax, this.tlMpHoiBanThanVaDongDoi);
        if (this.player.effectSkin.isNezuko) {
            this.mpHoi += calPercent(this.mpMax, 3);
        }
        if (this.player.fusion.typeFusion != 0) {
            if (this.player.pet != null) {
                if (PetService.gI().isDeTuNangCap(this.player.pet)) {
                    // if (this.player.pet.isMabu) {
                    // this.mpHoi += calPercent(this.mpMax, 15);
                    // }
                }
            }
        }
    }

    private void setHpMax() {
        this.hpMax = this.hpg;
        this.hpMax += this.hpAdd;
        // đồ
        for (Integer tl : this.tlHp) {
            this.hpMax += calPercent(this.hpMax, tl);
        }
        // set nappa
        if (this.player.setClothes.nappa == 5) {
            this.hpMax += calPercent(this.hpMax, 100);
        }
        //set tinh ấn
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.tinhan == 5) {
            this.hpMax += calPercent(this.hpMax, 15);
        }

        // +hp đệ
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {

            this.hpMax += this.player.pet.nPoint.hpMax;
            // if (this.player.setClothes.pikkoroDaimao == 5) {
            // hpMax_ += calPercent(hpMax_, 200);
            // }
            if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                this.hpMax += calPercent(this.hpMax, 10);
            }
            if (this.player.pet != null) {
                if (PetService.gI().isDeTuNangCap(this.player.pet)) {
                    switch (this.player.pet.LevelZeno) {
                        case 0:
                            this.hpMax += calPercent(this.hpMax, 10);
                            break;
                        // if (this.player.pet.isMabu) {
                        // hpMax_ += calPercent(hpMax_, 10);
                        // }
                        case 1:
                            this.hpMax += calPercent(this.hpMax, 20);
                            break;
                        case 2:
                            this.hpMax += calPercent(this.hpMax, 30);
                            break;
                        case 3:
                            this.hpMax += calPercent(this.hpMax, 35);
                            break;
                        case 4:
                            this.hpMax += calPercent(this.hpMax, 40);
                            break;
                        case 5:
                            this.hpMax += calPercent(this.hpMax, 45);
                            break;
                        default:
                            break;
                    }
                }
            }

        }
        // ngọc rồng đen 2 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            this.hpMax += calPercent(this.hpMax, RewardBlackBall.R2S);
        }

        if (this.player.effectSkill != null && this.player.effectSkill.isSaiYan) {
            int calLeveSY = this.player.effectSkill.levelSaiYan * 3;
            this.hpMax += calPercent(this.hpMax, calLeveSY);
        }

        // pet zeno
        // if (this.player.isPet && ((Pet) this.player).isMabu
        // && (((Pet) this.player).master.fusion.typeFusion ==
        // ConstPlayer.HOP_THE_PORATA
        // || ((Pet) this.player).master.fusion.typeFusion ==
        // ConstPlayer.HOP_THE_PORATA2)) {
        // switch (((Pet) this.player).LevelZeno) {
        // case 0:
        // this.hpMax += calPercent(this.hpMax, 10);
        // break;
        // case 1:
        // this.hpMax += calPercent(this.hpMax, 15);
        // break;
        // case 2:
        // this.hpMax += calPercent(this.hpMax, 20);
        // break;
        // case 3:
        // this.hpMax += calPercent(this.hpMax, 25);
        // break;
        // case 4:
        // this.hpMax += calPercent(this.hpMax, 30);
        // break;
        // }
        // }
        // phù
        // huýt sáo
        if (!this.player.isPet
                || (this.player.isPet
                && ((Pet) this.player).status != Pet.FUSION)) {
            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
                this.hpMax += calPercent(this.hpMax, this.player.effectSkill.tiLeHPHuytSao);
            }
        }

        // ###### ITEM TIME ######
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isCaRot) {
                this.hpMax += calPercent(this.hpMax, 5);
            }
            // bổ huyết
            if (this.player.itemTime.isUseBoHuyet) {
                this.hpMax *= 2;
            }
            // bổ huyết 2
            if (this.player.itemTime.isUseBoHuyet2) {
                this.hpMax += calPercent(this.hpMax, 120);
            }
            if (this.player.itemTime.isUseThitSuon) {
                this.hpMax *= 1.05;
            }
            if (this.player.itemTime.isUseGroup_2_2 && this.player.zone != null
                    && MapService.gI().isMapCold(this.player.zone.map.mapId)) {
                this.hpMax += calPercent(this.hpMax, 20);
            }
            // Dấu ấn rồng thiên
            if (this.player.itemTime.isUseGroup_3_3) {
                this.hpMax += calPercent(this.hpMax, 20);
            }
            if (this.player.itemTime.isUseGroup_7_2) {
                this.hpMax += calPercent(this.hpMax, 10);
            }

            if (this.player.itemTime.isUseBanhTet) {
                this.hpMax += calPercent(this.hpMax, 15);
            }
            if (this.player.itemTime.isUseGroup_5_3) {
                this.hpMax += calPercent(this.hpMax, 10);
            }
            if (this.player.itemTime.isUseGroup_5_9) {
                this.hpMax += calPercent(this.hpMax, 3);
            }

        }
        if (this.player.zone != null) {
            if (MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
                this.hpMax *= this.player.effectSkin.xHPKI;
            } else if (MapService.gI().isMapCold(this.player.zone.map)
                    && !this.isKhongLanh) {
                this.hpMax /= 2;
            }
        }

        if (!player.isBoss) {
            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.HP);
            if (at != null && !at.isExpired()) {
                this.hpMax += calPercent(this.hpMax, at.getValue());
            }
        }

        if (!player.isPet && player.inventory != null && player.inventory.itemsBox_ct_pet != null) {
            int itemCount = 0;
            for (Item item : player.inventory.itemsBox_ct_pet) {
                if (item != null && item.isNotNullItem()) {
                    itemCount++;
                }
            }
            int percentIncrease = itemCount / 10; // Mỗi 10 vật phẩm tăng 1%
            if (percentIncrease > 0) {
                this.hpMax += calPercent(this.hpMax, percentIncrease);
                //  Service.getInstance().sendThongBao(player, "Tăng HP +" + percentIncrease + "% do rương có " + itemCount + " vật phẩm!");
            }
        } else if (player.inventory == null || player.inventory.itemsBox_ct_pet == null) {
            Logger.logException(this.getClass(), new Exception("itemsBox_ct_pet is null for player: " + player.name));
        }

        if (player.getBuff() == Buff.BUFF_HP) {
            this.hpMax += calPercent(this.hpMax, 20);
        }
        if (this.player.charms.tdPhuHP > System.currentTimeMillis()) {
            this.hpMax += calPercent(this.hpMax, 10);
        }
        if (this.player.setClothes.setLevel7 == 5) {
            this.hpMax += calPercent(this.hpMax, 3);
        }
        if (this.player.setClothes.setLevel8 == 5) {
            this.hpMax += calPercent(this.hpMax, 5);
        }
        // Top nạp point
        int topNapPoint = this.player.inventory.topNap;
        if (topNapPoint > 0) {
            if (topNapPoint == 1) {
                this.hpMax += (this.hpMax * 20 / 100);
            } else if (topNapPoint == 2) {
                this.hpMax += (this.hpMax * 15 / 100);
            } else if (topNapPoint == 3) {
                this.hpMax += (this.hpMax * 10 / 100);
            } else if (topNapPoint == 4) {
                this.hpMax += (this.hpMax * 5 / 100);
            }
        }

        // Vip point
        int vipPoint = this.player.inventory.vip_point;
        if (vipPoint > 0) {
            if (vipPoint >= 10 && vipPoint < 50) {
                this.hpMax += (this.hpMax * 5 / 100); // vip 0
            } else if (vipPoint >= 50 && vipPoint < 200) {
                this.hpMax += (this.hpMax * 10 / 100); // vip 1
            } else if (vipPoint >= 200 && vipPoint < 900) {
                this.hpMax += (this.hpMax * 15 / 100); // vip 2
            } else if (vipPoint >= 900 && vipPoint < 2500) {
                this.hpMax += (this.hpMax * 20 / 100);// vip 3
            } else if (vipPoint >= 2500 && vipPoint < 5000) {
                this.hpMax += (this.hpMax * 25 / 100); // vip 3
            } else if (vipPoint >= 5000) {
                this.hpMax += (this.hpMax * 30 / 100); // vip 4
            }
        }
        if (Manager.EVENT_POINT_TET_2024 >= 5000) {
            this.hpMax += (this.hpMax * 15 / 100);
            if (Manager.EVENT_POINT_TET_2024 >= 10000) {
                this.hpMax += (this.hpMax * 15 / 100);
            }
        }

        if (player.isPl() && player.pet != null) {
            if (player.fusion.typeFusion >= 4) {
                switch (PetService.gI().getTypePet(player.pet)) {
                    case 1:
                        this.hpMax += calPercent(this.hpMax, 5);
                        break;
                    case 2:
                        this.hpMax += calPercent(this.hpMax, 10);
                        break;
                    case 3:
                        this.hpMax += calPercent(this.hpMax, 15);
                        break;
                    case 4:
                        this.hpMax += calPercent(this.hpMax, 30);
                        break;
                    default:
                        break;
                }
            }
        }
        // monkey
        if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
            if (player.effectSkill.isMonkey) {
                int percent = 30 + (player.effectSkill.levelMonkey * 10);
                long gethpMokey = calPercent(this.hpMax, percent);
                this.hpMax += gethpMokey;
            }
        }

        if (this.hpMax > 2_000_000_000) {
            this.hpMax = 2_000_000_000;
        }
    }

    // (hp sư phụ + hp đệ tử ) + 15%
    // (hp sư phụ + 15% +hp đệ tử)
    private void setHp() {
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    private void setMpMax() {
        this.mpMax = this.mpg;
        this.mpMax += this.mpAdd;
        long mpMax_ = this.mpMax;
        // đồ
        for (Integer tl : this.tlMp) {
            mpMax_ += calPercent(mpMax_, tl);
        }
        if (this.player.setClothes.picolo == 5) {
            mpMax_ += calPercent(mpMax_, 100);
        }

        // hợp thể
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            mpMax_ += this.player.pet.nPoint.mpMax;
            // if (this.player.setClothes.pikkoroDaimao == 5) {
            // mpMax_ += calPercent(mpMax_, 200);
            // }
//            if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                mpMax_ += calPercent(mpMax_, 10);
//            }
            if (this.player.pet != null) {
                if (PetService.gI().isDeTuNangCap(this.player.pet)) {
                    if (this.player.pet.LevelZeno == 0) {
                        mpMax_ += calPercent(mpMax_, 10);
                    } else if (this.player.pet.LevelZeno == 1) {
                        mpMax_ += calPercent(mpMax_, 20);
                    } else if (this.player.pet.LevelZeno == 2) {
                        mpMax_ += calPercent(mpMax_, 30);
                    } else if (this.player.pet.LevelZeno == 3) {
                        mpMax_ += calPercent(mpMax_, 35);
                    } else if (this.player.pet.LevelZeno == 4) {
                        mpMax_ += calPercent(mpMax_, 40);
                    } else if (this.player.pet.LevelZeno == 5) {
                        mpMax_ += calPercent(mpMax_, 45);
                    }
                    // if (this.player.pet.isMabu) {
                    // mpMax_ += calPercent(mpMax_, 10);
                    // }
                }

            }
        }
        // ngọc rồng đen 3 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            mpMax_ += calPercent(mpMax_, RewardBlackBall.R3S);
        }
        // pet mabư
        // if (this.player.isPet && ((Pet) this.player).isMabu
        // && (((Pet) this.player).master.fusion.typeFusion ==
        // ConstPlayer.HOP_THE_PORATA
        // || ((Pet) this.player).master.fusion.typeFusion ==
        // ConstPlayer.HOP_THE_PORATA2)) {
        // switch (((Pet) this.player).LevelZeno) {
        // case 0:
        // this.mpMax += calPercent(this.mpMax, 10);
        // break;
        // case 1:
        // this.mpMax += calPercent(this.mpMax, 15);
        // break;
        // case 2:
        // this.mpMax += calPercent(this.mpMax, 20);
        // break;
        // case 3:
        // this.mpMax += calPercent(this.mpMax, 25);
        // break;
        // case 4:
        // this.mpMax += calPercent(this.mpMax, 30);
        // break;

        // }
        // }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isCaRot) {
                mpMax_ += calPercent(mpMax_, 5);
            }
            // bổ khí
            if (this.player.itemTime.isUseBoKhi) {
                mpMax_ *= 2;
            }
            // bổ khí 2
            if (this.player.itemTime.isUseBoKhi2) {
                mpMax_ += calPercent(mpMax_, 120);
            }
            if (this.player.itemTime.isUseThitThan) {
                mpMax_ *= 1.05;
            }
            if (this.player.itemTime.isUseGroup_2_2 && this.player.zone != null
                    && MapService.gI().isMapCold(this.player.zone.map.mapId)) {
                mpMax_ += calPercent(mpMax_, 20);
            }
            // Dấu ấn rồng thiên
            if (this.player.itemTime.isUseGroup_3_3) {
                mpMax_ += calPercent(mpMax_, 20);
            }
            if (this.player.itemTime.isUseGroup_7_2) {
                mpMax_ += calPercent(mpMax_, 10);
            }
            if (this.player.itemTime.isUseGroup_5_3) {
                mpMax_ += calPercent(mpMax_, 10);
            }
            if (this.player.itemTime.isUseGroup_5_9) {
                mpMax_ += calPercent(mpMax_, 3);
            }
        }

        if (!player.isPet && player.inventory != null && player.inventory.itemsBox_ct_pet != null) {
            int itemCount = 0;
            for (Item item : player.inventory.itemsBox_ct_pet) {
                if (item != null && item.isNotNullItem()) {
                    itemCount++;
                }
            }
            int percentIncrease = itemCount / 10; // Mỗi 10 vật phẩm tăng 1%
            if (percentIncrease > 0) {
                this.mpMax += calPercent(this.mpMax, percentIncrease);
                //   Service.getInstance().sendThongBao(player, "Tăng KI +" + percentIncrease + "% do rương có " + itemCount + " vật phẩm!");
            }
        } else if (player.inventory == null || player.inventory.itemsBox_ct_pet == null) {
            Logger.logException(this.getClass(), new Exception("itemsBox_ct_pet is null for player: " + player.name));
        }

        // phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            mpMax_ *= this.player.effectSkin.xHPKI;
        }
        // xiên cá
        if (this.player.effectFlagBag.useXienCa) {
            mpMax_ += calPercent(mpMax_, 15);
        }
        // Kiem z
        if (this.player.effectFlagBag.useKiemz) {
            mpMax_ += calPercent(mpMax_, 20);
        }
        //set nguyệt ấn
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.nhatan == 5) {
            mpMax_ += calPercent(mpMax_, 15);
        }
        if (this.player.effectFlagBag.useDieuRong) {
            mpMax_ += calPercent(mpMax_, 30);
        }
        if (this.player.effectFlagBag.useHoaVang || this.player.effectFlagBag.useHoaHong) {
            mpMax_ += calPercent(mpMax_, 20);
        }
        if (!player.isBoss) {
            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.KI);
            if (at != null && !at.isExpired()) {
                mpMax_ += calPercent(mpMax_, at.getValue());
            }
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhTet) {
                mpMax_ += calPercent(mpMax_, 15);
            }
        }
        if (player.getBuff() == Buff.BUFF_KI) {
            mpMax_ += calPercent(mpMax_, 20);
        }
        if (this.player.charms.tdPhuKI > System.currentTimeMillis()) {
            mpMax_ += calPercent(mpMax_, 10);
        }
        if (this.player.setClothes.setLevel7 == 5) {
            mpMax_ += calPercent(mpMax_, 3);
        }
        if (this.player.setClothes.setLevel8 == 5) {
            mpMax_ += calPercent(mpMax_, 5);
        }
        if (this.player.effectSkill != null && this.player.effectSkill.isSaiYan) {
            int calLeveSY = this.player.effectSkill.levelSaiYan * 3;
            mpMax_ += calPercent(mpMax_, calLeveSY);
        }
        // Top nạp point
        int topNapPoint = this.player.inventory.topNap;
        if (topNapPoint > 0) {
            if (topNapPoint == 1) {
                mpMax_ += (mpMax_ * 20 / 100);
            } else if (topNapPoint == 2) {
                mpMax_ += (mpMax_ * 15 / 100);
            } else if (topNapPoint == 3) {
                mpMax_ += (mpMax_ * 10 / 100);
            } else if (topNapPoint == 4) {
                mpMax_ += (mpMax_ * 5 / 100);
            }
        }
        // Vip point
        int vipPoint = this.player.inventory.vip_point;
        if (vipPoint > 0) {
            if (vipPoint >= 10 && vipPoint < 50) {
                mpMax_ += (mpMax_ * 5 / 100); // vip 0
            } else if (vipPoint >= 50 && vipPoint < 200) {
                mpMax_ += (mpMax_ * 10 / 100); // vip 1
            } else if (vipPoint >= 200 && vipPoint < 900) {
                mpMax_ += (mpMax_ * 15 / 100); // vip 2
            } else if (vipPoint >= 900 && vipPoint < 2500) {
                mpMax_ += (mpMax_ * 20 / 100);// vip 3
            } else if (vipPoint >= 2500 && vipPoint < 5000) {
                mpMax_ += (mpMax_ * 25 / 100); // vip 3
            } else if (vipPoint >= 5000) {
                mpMax_ += (mpMax_ * 30 / 100); // vip 4
            }

        }
        if (Manager.EVENT_POINT_TET_2024 >= 5000) {
            mpMax_ += (mpMax_ * 15 / 100);
            if (Manager.EVENT_POINT_TET_2024 >= 10000) {
                mpMax_ += (mpMax_ * 15 / 100);
            }
        }

        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                this.mpMax += calPercent(this.mpMax, percent);
            }
        }
        if (player.isPl() && player.pet != null) {
            if (player.fusion.typeFusion >= 4) {
                switch (PetService.gI().getTypePet(player.pet)) {
                    case 1:
                        this.mpMax += calPercent(this.mpMax, 5);
                        break;
                    case 2:
                        this.mpMax += calPercent(this.mpMax, 10);
                        break;
                    case 3:
                        this.mpMax += calPercent(this.mpMax, 15);
                        break;
                    case 4:
                        this.mpMax += calPercent(this.mpMax, 30);
                        break;
                    default:
                        break;
                }
            }
        }

        if (mpMax_ > 2_000_000_000) {
            mpMax_ = 2_000_000_000;
        }
        this.mpMax = (int) mpMax_;
    }

    private void setMp() {
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    private void setDame() {
        this.dame = this.dameg;
        this.dame += this.dameAdd;
        // đồ
        for (Integer tl : this.tlDame) {
            this.dame += calPercent(this.dame, tl);
        }
        for (Integer tl : this.tlSDDep) {
            this.dame += calPercent(this.dame, tl);
        }

        //set nguyệt ấn
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.nguyetan == 5) {
            this.dame += calPercent(this.dame, 15);
        }
        // hợp thể
        if (this.player.fusion.typeFusion != 0) {
            this.dame += this.player.pet.nPoint.dame;
            // if (this.player.setClothes.pikkoroDaimao == 5) {
            // this.dame += calPercent(this.dame, 200);
            // }
//            if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                this.dame += calPercent(this.dame, 10);
//            }
            if (this.player.pet != null) {
                if (PetService.gI().isDeTuNangCap(this.player.pet)) {
                    if (this.player.pet.LevelZeno == 0) {
                        this.dame += calPercent(this.dame, 10);
                    } else if (this.player.pet.LevelZeno == 1) {
                        this.dame += calPercent(this.dame, 20);
                    } else if (this.player.pet.LevelZeno == 2) {
                        this.dame += calPercent(this.dame, 30);
                    } else if (this.player.pet.LevelZeno == 3) {
                        this.dame += calPercent(this.dame, 35);
                    } else if (this.player.pet.LevelZeno == 4) {
                        this.dame += calPercent(this.dame, 40);
                    } else if (this.player.pet.LevelZeno == 5) {
                        this.dame += calPercent(this.dame, 45);
                    }
                    if (player.isPl() && player.pet != null) {
                        if (player.fusion.typeFusion >= 4) {
                            switch (PetService.gI().getTypePet(player.pet)) {
                                case 1:
                                    this.dame += calPercent(this.dame, 5);
                                    break;
                                case 2:
                                    this.dame += calPercent(this.dame, 10);
                                    break;
                                case 3:
                                    this.dame += calPercent(this.dame, 15);
                                    break;
                                case 4:
                                    this.dame += calPercent(this.dame, 30);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
        // pet zeno
        // if (this.player.isPet && ((Pet) this.player).isMabu
        // && (((Pet) this.player).master.fusion.typeFusion ==
        // ConstPlayer.HOP_THE_PORATA
        // || ((Pet) this.player).master.fusion.typeFusion ==
        // ConstPlayer.HOP_THE_PORATA2)) {
        // switch (((Pet) this.player).LevelZeno) {
        // case 0:
        // this.dame += calPercent(this.dame, 10);
        // break;
        // case 1:
        // this.dame += calPercent(this.dame, 15);
        // break;
        // case 2:
        // this.dame += calPercent(this.dame, 20);
        // break;
        // case 3:
        // this.dame += calPercent(this.dame, 25);
        // break;
        // case 4:
        // this.dame += calPercent(this.dame, 30);
        // break;

        // }
        // }
        // thức ăn
        if (!this.player.isPet && this.player.itemTime.isEatMeal
                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
            this.dame += calPercent(this.dame, 10);
        }
        if (this.player.isPet && ((Pet) this.player).master.nPoint.tlDameDeTu > 0) {
            this.dame += calPercent(this.dame, ((Pet) this.player).master.nPoint.tlDameDeTu);
        }
        if (this.player.effectSkill != null && this.player.effectSkill.isSaiYan) {
            int calLeveSY = this.player.effectSkill.levelSaiYan * 3;
            this.dame += calPercent(this.dame, calLeveSY);
        }
        // cuồng nộ
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isChuoi) {
                this.dame += calPercent(this.dame, 5);
            }
            if (this.player.itemTime.isUseCuongNo) {
                this.dame *= 2;
            }
            // cuồng nộ 2
            if (this.player.itemTime.isUseCuongNo2) {
                this.dame += calPercent(dame, 120);
            }
            if (this.player.itemTime.isUseDauVe) {
                this.dame *= 1.05;
            }

            if (this.player.itemTime.isUseBanhChung) {
                dame += calPercent(dame, 10);
            }
            if (this.player.itemTime.isUseBanhTrungThu_1) {
                dame += calPercent(dame, 10);
            }
            if (this.player.itemTime.isUseBanhTrungThu_2) {
                dame += calPercent(dame, 15);
            }
            if (this.player.itemTime.isUseBanhTrungThu_3) {
                dame += calPercent(dame, 20);
            }
            if (this.player.itemTime.isUseBanhTrungThu_4) {
                dame += calPercent(dame, 25);
            }
            if (this.player.itemTime.isUseGroup_2_2 && this.player.zone != null
                    && MapService.gI().isMapCold(this.player.zone.map.mapId)) {
                dame += calPercent(dame, 20);
            }
            // Dấu ấn rồng thiên
            if (this.player.itemTime.isUseGroup_3_3) {
                dame += calPercent(dame, 20);
            }

            if (this.player.itemTime.isUseGroup_5_4) {
                dame += calPercent(dame, 10);
            }
            if (this.player.itemTime.isUseGroup_5_9) {
                dame += calPercent(dame, 3);
            }
        }
        if (!player.isPet && player.inventory != null && player.inventory.itemsBox_ct_pet != null) {
            int itemCount = 0;
            for (Item item : player.inventory.itemsBox_ct_pet) {
                if (item != null && item.isNotNullItem()) {
                    itemCount++;
                }
            }
            int percentIncrease = itemCount / 10; // Mỗi 10 vật phẩm tăng 1%
            if (percentIncrease > 0) {
                this.dame += calPercent(this.dame, percentIncrease);
                //    Service.getInstance().sendThongBao(player, "Tăng dame +" + percentIncrease + "% do rương có " + itemCount + " vật phẩm!");
            }
        } else if (player.inventory == null || player.inventory.itemsBox_ct_pet == null) {
            Logger.logException(this.getClass(), new Exception("itemsBox_ct_pet is null for player: " + player.name));
        }
        // giảm dame
        this.dame -= calPercent(this.dame, tlSubSD);
        // map cold
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.dame /= 2;
        }
       if (this.player.itemTime.isUseGroup_7_1) {
            this.dame += calPercent(this.dame, 10);
        }
        // ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            this.dame += calPercent(this.dame, RewardBlackBall.R1S);
        }
        if (!player.isBoss) {
            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.SUC_DANH);
            if (at != null && !at.isExpired()) {
                this.dame += calPercent(dame, at.getValue());
            }
        }

        if (player.getBuff() == Buff.BUFF_ATK) {
            dame += calPercent(dame, 20);
        }
        if (this.player.charms.tdPhuSD > System.currentTimeMillis()) {
            dame += calPercent(dame, 10);
        }
        if (this.player.setClothes.setLevel7 == 5) {
            dame += calPercent(dame, 3);
        }
        if (this.player.setClothes.setLevel8 == 5) {
            dame += calPercent(dame, 5);
        }
        // Top nạp point
        int topNapPoint = this.player.inventory.topNap;
        if (topNapPoint > 0) {
            if (topNapPoint == 1) {
                this.dame += ((long) this.dame * 20 / 100);
            } else if (topNapPoint == 2) {
                this.dame += ((long) this.dame * 15 / 100);
            } else if (topNapPoint == 3) {
                this.dame += ((long) this.dame * 10 / 100);
            } else if (topNapPoint == 4) {
                this.dame += ((long) this.dame * 5 / 100);
            }
        }
        // Vip point
        int vipPoint = this.player.inventory.vip_point;
        if (vipPoint > 0) {
            if (vipPoint >= 10 && vipPoint < 50) {
                this.dame += ((long) this.dame * 5 / 100); // vip 0
            } else if (vipPoint >= 50 && vipPoint < 200) {
                this.dame += ((long) this.dame * 10 / 100); // vip 1
            } else if (vipPoint >= 200 && vipPoint < 900) {
                this.dame += ((long) this.dame * 15 / 100); // vip 2
            } else if (vipPoint >= 900 && vipPoint < 2500) {
                this.dame += ((long) this.dame * 20 / 100);// vip 3
            } else if (vipPoint >= 2500 && vipPoint < 5000) {
                this.dame += ((long) this.dame * 25 / 100); // vip 3
            } else if (vipPoint >= 5000) {
                this.dame += ((long) this.dame * 30 / 100); // vip 4
            }

        }
        if (Manager.EVENT_POINT_TET_2024 >= 7000) {
            this.dame += ((long) this.dame * 15 / 100);
            if (Manager.EVENT_POINT_TET_2024 >= 10000) {
                this.dame += ((long) this.dame * 15 / 100);

            }
        }
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                this.dame += calPercent(this.dame, percent);
            }
        }
    }

    private void setDef() {
        this.def = this.defg * 4;
        this.def += this.defAdd;
        // đồ
        for (Integer tl : this.tlDef) {
            this.tlGiamst += tl;
        }
        if (tlGiamst > 60) {
            tlGiamst = 60;
        }
        // ngọc rồng đen 5 sao

        if (this.player.effectSkin.isInosuke) {
            this.def += calPercent(this.def, 50);
        }
        if (this.player.effectSkin.isInoHashi) {
            this.def += calPercent(this.def, 60);
        }
    }

    private void setCrit() {
        this.crit = this.critg;
        this.crit += this.critAdd;

        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhTrungThu_1) {
                crit += 10;
            }
            if (this.player.itemTime.isUseBanhTrungThu_2) {
                crit += 15;
            }
            if (this.player.itemTime.isUseBanhTrungThu_3) {
                crit += 20;
            }
            if (this.player.itemTime.isUseBanhTrungThu_4) {
                crit += 25;
            }
        }

        // biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 110;
        }
        if (player.getBuff() == Buff.BUFF_CRIT) {
            crit += 10;
        }

    }

    private void setCritDame() {
        if (this.player.rewardBlackBall.timeOutOfDateReward[6] > System.currentTimeMillis()) {
            this.tlDameCrit.add(RewardBlackBall.R7S);
        }
        if (this.player.effectSkin.isTanjiro) {
            this.tlDameCrit.add(30);
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhChung) {
                this.tlDameCrit.add(10);
            }
            if (this.player.itemTime.isCaChua) {
                this.tlDameCrit.add(5);
            }
        }
        if (this.player.fusion.typeFusion != 0) {
            if (this.player.pet != null) {
                if (PetService.gI().isDeTuNangCap(this.player.pet)) {
                    if (this.player.pet.isBU) {
                        this.tlDameCrit.add(15);
                    } else if (this.player.pet.isGoku) {
                        this.dameSKillSpecical += 15;
                    }
                }

            }
        }
    }

    private void setSpeed() {
        for (Integer tl : this.tlSpeed) {
            this.speed += calPercent(this.speed, tl);
        }
        if (this.player.effectSkin.isSlow) {
            this.speed = 1;
        }
    }

    private void resetPoint() {
        this.hpAdd = 0;
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlDame.clear();
        this.tlDameAttMob.clear();
        this.tlDameCrit.clear();
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlDameDeTu = 0;
        this.tlXuyenGiapCanChien = 0;
        this.tlXuyenGiapChuong = 0;
        this.tlGiamChoang = 0;
        this.tlRoiEvent_id = 0;
        this.tlSatThuongLua = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlDameCrit.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlSDDep.clear();
        this.tlSubSD = 0;
        this.tlHpGiamODo = 0;
        this.teleport = false;
        this.tlSpeed.clear();
        this.speed = 10;
        this.mstChuong = 0;
        this.tlGiamst = 0;
        this.tlTNSMPet = 0;
        this.tlDameChuong = 0;
        this.tlDameBoss = 0;
        this.tlDameSkillDam = 0;
        this.dameSKillSpecical = 0;
        this.timeSKillSpecical = 0;
        this.tlDameHpTarger = 0;
        this.tlPhatNo = 0;
        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.wearingDrabula = false;
        this.wearingNezuko = false;
        this.wearingZenitsu = false;
        this.wearingInosuke = false;
        this.wearingInoHashi = false;
        this.wearingTanjiro = false;
        this.wearingMabu = false;
        this.wearingBuiBui = false;
        this.xDameChuong = false;
        this.wearingYacon = false;
        this.isBocPha = false;
        this.isHaDocDoiThu = false;
        this.wearingCarrot = false;
        this.wearingKhangTDHS = false;
        this.wearingTNSMClan = false;
        this.wearingBiNgo = false;
        this.isDoPhaLe = false;
    }

    public void addHp(int hp) {
        int getHp = addInt(this.hp, hp);
        this.hp = getHp;
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    public void addMp(int mp) {
        long getmp = addInt(this.mp, mp);
        this.mp = calLimit(getmp);
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    public void setHp(long hp) {
        if (hp > this.hpMax) {
            this.hp = this.hpMax;
        } else {
            this.hp = (int) hp;
        }
    }

    public void setDame(int dame) {
        if (dame > this.dameg) {
            this.dame = this.dameg;
        } else {
            this.dame = dame;
        }
    }

    public void setMp(long mp) {
        if (mp > this.mpMax) {
            this.mp = this.mpMax;
        } else {
            this.mp = (int) mp;
        }
    }

    private void setIsCrit() {
        if (intrinsic != null && intrinsic.id == 25
                && this.getCurrPercentHP() <= intrinsic.param1) {
            isCrit = true;
        } else if (isCrit100) {
            isCrit100 = false;
            isCrit = true;
        } else {
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }

    public int getDameAttack(boolean isAttackMob) {
        setIsCrit();
        long dameAttack = this.dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        int percentDameSkill = 0;
        int percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
                if (intrinsic.id == 1) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (tlDameSkillDam > 0) {
                    percentXDame += tlDameSkillDam;
                }
                break;
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku == 5) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.SieuVietKame == 5) {
                    percentXDame = 50;
                }
                if (tlDameChuong > 0) {
                    if (Util.canDoWithTime(this.player.lastTimexDameChuong, 60000) && Util.isTrue(30, 100)) {
                        percentXDame += (tlDameChuong * 100);
                        this.player.lastTimexDameChuong = System.currentTimeMillis();
                    }
                }
                break;
            case Skill.GALICK:
                if (intrinsic.id == 16) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {
                    percentXDame = 100;
                }
                if (tlDameSkillDam > 0) {
                    percentXDame += tlDameSkillDam;
                }
                break;
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (tlDameChuong > 0) {
                    if (Util.canDoWithTime(this.player.lastTimexDameChuong, 60000) && Util.isTrue(30, 100)) {
                        percentXDame += (tlDameChuong * 100);
                        this.player.lastTimexDameChuong = System.currentTimeMillis();
                    }
                }
                break;
            case Skill.DEMON:
                if (intrinsic.id == 8) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (tlDameSkillDam > 0) {
                    percentXDame += tlDameSkillDam;
                }

                break;
            case Skill.MASENKO:
                if (intrinsic.id == 9) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (tlDameChuong > 0) {
                    if (Util.canDoWithTime(this.player.lastTimexDameChuong, 60000) && Util.isTrue(30, 100)) {
                        percentXDame += (tlDameChuong * 100);
                        this.player.lastTimexDameChuong = System.currentTimeMillis();
                    }
                }
                break;
            case Skill.KAIOKEN:
                percentDameSkill = skillSelect.damage;
                // if (player.setClothes.thienXinHang == 5) {
                // percentXDame = 100;
                // }
                break;
            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.ocTieu == 5) {
                    percentXDame = 100;
                }
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                dameAttack *= 2;
                long minDame = dameAttack - calPercent(dameAttack, 5);
                long maxDame = dameAttack + calPercent(dameAttack, 5);
                if (minDame > 2_000_000_000) {
                    return 2_000_000_000;
                }
                if (maxDame > 2_000_000_000) {
                    maxDame = 2_000_000_000;
                }
                dameAttack = Util.nextInt((int) (minDame),
                        (int) (maxDame));

                return (int) dameAttack;
            case Skill.MAKANKOSAPPO:
                percentDameSkill = skillSelect.damage;
                long dameSkill = calPercent(this.mpMax, percentDameSkill);
                if (this.player.setClothes.SieuVietLaze == 5) {
                    dameSkill *= 1.1;
                }
                // if (this.player.setClothes.picolo == 5) {
                // dameSkill += calPercent(dameSkill, 120);
                // }
                if (dameSKillSpecical > 0) {
                    dameSkill += ((dameSkill * dameSKillSpecical) / 100);
                }
                if (dameSkill > 2_000_000_000) {
                    dameSkill = 2_000_000_000;
                }
                return (int) dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                long totalHP = 0;
                if (player.zone != null) {
                    totalHP = player.zone.getTotalHP();
                }
                long damage = ((totalHP / 10) + (this.dame * 4));

                if (this.player.setClothes.kirin == 5) {
                    damage *= 2;
                }
                if (dameSKillSpecical > 0) {
                    damage += ((damage * dameSKillSpecical) / 100);
                }
                if (damage > 2_000_000_000) {
                    damage = 2_000_000_000;
                }
                return (int) damage;
        }
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }
        if (percentDameSkill != 0) {
            dameAttack = calPercent(dameAttack, percentDameSkill);
        }
        dameAttack += calPercent(dameAttack, percentDameIntrinsic);
        dameAttack += calPercent(dameAttack, dameAfter);
        if (SkillUtil.isUseSkillDam(player) && Util.isTrue(1, 6)) {
            dameAttack += calPercent(dameAttack, this.tlSatThuongLua);
        }
        if (isAttackMob) {
            for (Integer tl : this.tlDameAttMob) {
                dameAttack += calPercent(dameAttack, tl);
            }
        }
        dameAfter = 0;
        // if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu >
        // System.currentTimeMillis()) {
        // dameAttack *= 2;
        // }
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTuMabu3 > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (this.isBocPha && this.dameHapThu > 0) {
            dameAttack += this.dameHapThu;
            this.dameHapThu = 0;
        }
        if (isCrit) {
            int ptDameChiMang = 100;
            for (Integer tl : this.tlDameCrit) {
                ptDameChiMang += tl;
            }
            dameAttack += calPercent(dameAttack, ptDameChiMang);
        }

        dameAttack += calPercent(dameAttack, percentXDame);
        // System.out.println(dameAttack);
        long minDame = dameAttack - calPercent(dameAttack, 5);
        long maxDame = dameAttack + calPercent(dameAttack, 5);
        if (minDame > 2_000_000_000) {
            return 2_000_000_000;
        }
        if (maxDame > 2_000_000_000) {
            maxDame = 2_000_000_000;
        }
        dameAttack = Util.nextInt((int) (minDame),
                (int) (maxDame));

        // check activation set
        return (int) dameAttack;
    }

    public int getDameAttackSkillNotFocus() {
        setIsCrit();
        long dameAttack = this.dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        int percentDameSkill = 0;
        int percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        switch (skillSelect.template.id) {

        }
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }
        if (percentDameSkill != 0) {
            dameAttack = calPercent(dameAttack, percentDameSkill);
        }
        dameAttack += calPercent(dameAttack, percentDameIntrinsic);
        dameAttack += calPercent(dameAttack, dameAfter);
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            int ptDameChiMang = 0;
            for (Integer tl : this.tlDameCrit) {
                ptDameChiMang += tl;
            }
            dameAttack += calPercent(dameAttack, ptDameChiMang);
        }
        dameAttack += calPercent(dameAttack, percentXDame);
        dameAttack = Util.nextInt((int) (dameAttack - calPercent(dameAttack, 5)),
                (int) (dameAttack + calPercent(dameAttack, 5)));
        return (int) dameAttack;
    }

    public int getCurrPercentHP() {
        if (this.hpMax == 0) {
            return 100;
        }
        return (int) ((long) this.hp * 100 / this.hpMax);
    }

    public int getCurrPercentMP() {
        return (int) ((long) this.mp * 100 / this.mpMax);
    }

    public void setFullHpMp() {
        this.hp = this.hpMax;
        this.mp = this.mpMax;
    }

    public void subHP(int sub) {
        this.hp -= sub;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void subMP(int sub) {
        this.mp -= sub;
        if (this.mp < 0) {
            this.mp = 0;
        }
    }

    public long calSucManhTiemNang(long tiemNang) {
        if (power < getPowerLimit()) {
            for (Integer tl : this.tlTNSM) {
                tiemNang += calPercent(tiemNang, tl);
            }
            if (this.player.cFlag != 0) {
                if (this.player.cFlag == 8) {
                    tiemNang += calPercent(tiemNang, 10);
                } else {
                    tiemNang += calPercent(tiemNang, 5);
                }
            }
            if (buffExpSatellite) {
                tiemNang += calPercent(tiemNang, 20);
            }
            if (player.isPet) {
                Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.TNSM);
                if (at != null && !at.isExpired()) {
                    tiemNang += calPercent(tiemNang, at.getValue());
                }
            }

            long tn = tiemNang;
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
                tiemNang += tn;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
                tiemNang += tn * 2;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 3;
            }
            // ngọc rồng đen 4 sao
            if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {
                tiemNang += calPercent(tiemNang, RewardBlackBall.R4S);
            }
            if (this.intrinsic != null && this.intrinsic.id == 24) {
                tiemNang += calPercent(tiemNang, this.intrinsic.param1);
            }
            if (MapService.gI().isMapDaiHaiTrinh(this.player.zone.map.mapId)) {
                tiemNang += tn * 2;
            } else if (MapService.gI().isMapRungNguyenSinh177(this.player.zone.map.mapId)) {
               tiemNang = 10;
            } else if (MapService.gI().isMapDoanhTrai(player.zone.map.mapId)) {
                tiemNang += tn * 3;
            } else if (MapService.gI().isLanhDia(player.zone.map.mapId)) {
                tiemNang /= 5;
            } else if (MapService.gI().isMapNguHanhSon(player.zone.map.mapId)) {
                tiemNang += tn * 2;
            } else if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
                tiemNang += tn * 3;   
            } else if (MapService.gI().isMapTuongLai(player.zone.map.mapId)) {
                tiemNang += calPercent(tiemNang, 50);

            } else if (MapService.gI().isMapCold(player.zone.map.mapId)) {
                tiemNang += tn;
            } else if (player.zone.map.mapId == 155) {// hành tinh ngục tù
                if (this.player.charms.tdPhuTNSM > System.currentTimeMillis()) {
                    tiemNang += calPercent(tiemNang, 100);
                }

            } else if (player.zone.map.mapId == 179
                    || player.zone.map.mapId == 180
                    || player.zone.map.mapId == 181) {
                if(player.nPoint.power <= 100_000_000_000L){
                     tiemNang += tn * 2;
                }
               
            }
            
            long tnsm2 = tiemNang;
            if (this.player.isPet) {
                int tltnsm = ((Pet) this.player).master.nPoint.tlTNSMPet;
                if (tltnsm > 0) {
                    tiemNang += calPercent(tiemNang, tltnsm);
                }
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                    tiemNang += tn * 2;
                }
                if (((Pet) this.player).master.charms.tdDeTuMabu3 > System.currentTimeMillis()) {
                    tiemNang += tn;
                }
                
                if (MapService.gI().isMapCereal(((Pet) this.player).master.zone.map.mapId)) {                    
                    if (((Pet) this.player).master.charms.tdDeTuMabu > System.currentTimeMillis()) {
                        tiemNang += tn * 3;
                    }
                }
                // item x2 x3 x4
                if (((Pet) this.player).master.itemTime != null) {
                    if (((Pet) this.player).master.itemTime.isUseGroup_7_5) {
                        tiemNang += calPercent(tiemNang, 100);
                    }
                    if (((Pet) this.player).master.itemTime.isUseHoangHoa) {
                        tiemNang += calPercent(tiemNang, 100);
                    }
                    if (((Pet) this.player).master.itemTime.isUseX2TNSM) {
                        tiemNang += (long) tnsm2;
                    }
                    if (((Pet) this.player).master.itemTime.isUseX3TNSM) {
                        tiemNang += (long) tnsm2 * 2;
                    }
                    if (((Pet) this.player).master.itemTime.isUseX4TNSM) {
                        tiemNang += (long) tnsm2 * 3;
                    }

                    if (((Pet) this.player).master.itemTime.isUseDauVe) { // mâm ngũ quả
                        tiemNang += (long) (tnsm2 / 2);
                    }
                    if (((Pet) this.player).master.itemTime.isUseGroup_2_1
                            && MapService.gI().isMapCold(((Pet) this.player).master.zone.map.mapId)) {
                        tiemNang += (long) tnsm2;
                    }
//                    if (((Pet) this.player).master.itemTime.isUseGroup_7_3) {
//                        tiemNang += tnsm2 * 100l;
//                    }
                    if (((Pet) this.player).master.itemTime.isKeoBayTay) {
                        tiemNang += tnsm2 * 2;
                    }
                    if (((Pet) this.player).master.itemTime.isUseGroup_5_7) {
                        tiemNang += tnsm2 * 5;
                    }
                    if (((Pet) this.player).master.itemTime.isUseGroup_5_8) {
                        tiemNang += tnsm2;
                    }
                    // if (((Pet) this.player).master.itemTime.isnoc) {
                    // tiemNang += (long) tiemNang;
                    // }

                }
            }
            if (this.player.itemTime != null) {
                if (player.itemTime.isUseGroup_7_4) {
                    tiemNang += calPercent(tiemNang, 100);
                }
                if (player.itemTime.isUseHoangHoa) {
                    tiemNang += calPercent(tiemNang, 100);
                }

                if (this.player.itemTime.isUseX2TNSM) {
                    tiemNang += (long) tnsm2;
                }
                if (this.player.itemTime.isUseX3TNSM) {
                    tiemNang += (long) tnsm2 * 2;
                }
                if (this.player.itemTime.isUseX4TNSM) {
                    tiemNang += (long) tnsm2 * 3;
                }
                // Mâm ngũ quả
                if (this.player.itemTime.isUseDauVe) {
                    tiemNang += (long) (tnsm2 / 2);
                }
                // thẻ người tuyết
                if (this.player.itemTime.isUseGroup_2_1
                        && MapService.gI().isMapCold(this.player.zone.map.mapId)) {
                    tiemNang += (long) tnsm2;
                }
                if (this.player.itemTime.isUseGroup_7_3) {
                    tiemNang += tnsm2 * 100l;
                }
                if (this.player.itemTime.isUseGroup_5_6) {
                    tiemNang += tnsm2 * 2;
                }
                if (this.player.itemTime.isUseGroup_5_7) {
                    tiemNang += tnsm2 * 5;
                }
                if (this.player.itemTime.isUseGroup_5_8) {
                    tiemNang += tnsm2;
                }
            }

           if (this.player.setClothes.nhat_an == 5) {
               tiemNang += calPercent(tiemNang, 200);
           }
            if (!player.isPet) {
                if (Manager.EVENT_POINT_TET_2024 >= 1000) {
                    tiemNang += (long) tnsm2;
                    if (Manager.EVENT_POINT_TET_2024 >= 10000) {
                        tiemNang += (long) tnsm2;
                    }
                }
            }

            tiemNang *= Manager.RATE_EXP_SERVER;
            tiemNang = calSubTNSM(tiemNang);
            if (player.zone.map.mapId == 126 || MapService.gI().isMapBlackBallWar(player.zone.map.mapId)) {
                tiemNang = 10;
            }
            if (tiemNang <= 0) {
                tiemNang = 1;
            }
        } else {
            tiemNang = 10;
        }

        return tiemNang;
    }


//     public long calSubTNSM(long tiemNang) {
//     if (power >= 110_000_000_000L) {
//     tiemNang -= calPercent(tiemNang, 99);
//     } else if (power >= 100_000000000L) {
//     tiemNang -= calPercent(tiemNang, 99);
//     } else if (power >= 90_000000000L) {
//     tiemNang -= calPercent(tiemNang, 98);
//     } else if (power >= 80_000_000_000L) {
//     tiemNang -= calPercent(tiemNang, 95);
//     }
//     return tiemNang;
//     }

public long calSubTNSM(long tiemNang) {
    int ratioSm = 2;

    // Nếu là pet thì bỏ qua bảng này
    if (player.isPet) {
    if (power >= 150_000_000_000L) {
        return 1;
    }
    if (power >= 100_000_000_000L) {
        tiemNang /= (150 * ratioSm);    
    } else if (power >= 90_000_000_000L) {
        tiemNang /= (120 * ratioSm);
    }else if (power >= 80_000_000_000L) {
        tiemNang /= (100 * ratioSm);
    }else if (power >= 70_000_000_000L) {
        tiemNang /= (50 * ratioSm);   
    }
    return tiemNang;
}

    // Player -> bảng mốc như cũ
    if (power >= 150_000_000_000L) {
        return 1;
    }
    if (power >= 650_500_000_000L) {
        tiemNang /= (2000000 * ratioSm);
    } else if (power >= 605_000_000_000L) {
        tiemNang /= (96000 * ratioSm);
    } else if (power >= 500_000_000_000L) {
        tiemNang /= (48000 * ratioSm);
    } else if (power >= 400_000_000_000L) {
        tiemNang /= (24000 * ratioSm);
    } else if (power >= 300_000_000_000L) {
        tiemNang /= (12000 * ratioSm);
    } else if (power >= 200_000_000_000L) {
        tiemNang /= (2500 * ratioSm);
    } else if (power >= 140_000_000_000L) {
        tiemNang /= (1500 * ratioSm);
    } else if (power >= 130_000_000_000L) {
        tiemNang /= (900 * ratioSm);
    } else if (power >= 120_000_000_000L) {
        tiemNang /= (800 * ratioSm);
    } else if (power >= 110_000_000_000L) {
        tiemNang /= (700 * ratioSm);
    } else if (power >= 100_000_000_000L) {
        tiemNang /= (500 * ratioSm);
    } else if (power >= 97_000_000_000L) {
        tiemNang /= (400 * ratioSm);     
    } else if (power >= 94_000_000_000L) {
        tiemNang /= (300 * ratioSm);    
    } else if (power >= 90_000_000_000L) {
        tiemNang /= (200 * ratioSm);
    } else if (power >= 80_000_000_000L) {
        tiemNang /= (100 * ratioSm);
    } else if (power >= 70_000_000_000L) {
        tiemNang /= (40 * ratioSm);
    } else if (power >= 60_000_000_000L) {
        tiemNang /= (30 * ratioSm);
    }

    return tiemNang;
}

//    public long calSubTNSM(long tiemNang) {
////        if (!player.isAdmin()) {
//            int ratioSm =5;
//            if (power >= 150_000_000_000L) {
//            return 1;
//            }
//            
//            if (player.isPet && power >= 150_000_000_000L) {
//            return 1;
//            } else if (player.isPet && power > 100_000_000_000L) {
//            tiemNang /= (600 * ratioSm);
//            } else if (player.isPet && power > 60_000_000_000L) {
//            tiemNang /= (30 * ratioSm);
//            
//            if (power >= 650_500_000_000L) {
//                tiemNang /= (2000000 * ratioSm);
//            } else if (power >= 605_000_000_000L) {
//                tiemNang /= (96000 * ratioSm);
//            } else if (power >= 500000000000L) {
//                tiemNang /= (48000 * ratioSm);
//            } else if (power >= 400_000_000_000L) {
//                tiemNang /= (24000 * ratioSm);
//            } else if (power >= 300000000000L) {
//                tiemNang /= (12000 * ratioSm);
//            } else if (power >= 200000000000L) {
//                tiemNang /= (2500 * ratioSm);
//                
//            } else if (power >= 140000000000L) {
//                tiemNang /= (1500 * ratioSm); 
//                
//            } else if (power >= 130000000000L) {
//                tiemNang /= (900 * ratioSm);  
//                
//            } else if (power >= 120000000000L) {
//                tiemNang /= (800 * ratioSm);    
//                
//            } else if (power >= 110000000000L) {
//                tiemNang /= (700 * ratioSm);    
//                
//            } else if (power >= 100000000000L) {
//                tiemNang /= (600 * ratioSm);
//                
//            } else if (power >= 90000000000L) {
//                tiemNang /= (500 * ratioSm);
//                
//            } else if (power >= 80000000000L) {
//                tiemNang /= (100 * ratioSm);
//                
//            } else if (power >= 70000000000L) {
//                tiemNang /= (40 * ratioSm);
//                
//            } else if (power >= 60_000_000_000L) {
//                tiemNang /= (30 * ratioSm);
//            }
//        }
//        return tiemNang;
//    }

    public short getTileHutHp(boolean isMob) {
        if (isMob) {
            return (short) (this.tlHutHp + this.tlHutHpMob);
        } else {
            return this.tlHutHp;
        }
    }

    public short getTiLeHutMp() {
        return this.tlHutMp;
    }

    public int subDameInjureWithDeff(int dame, int tlXuyenGiap) {
        int def = this.def;
        def -= calPercent(def, tlXuyenGiap);
        dame -= def;
        if (this.player.itemTime.isUseGiapXen) {
            dame /= 2;
        }
        if (this.player.itemTime.isUseGiapXen2) {
            dame -= calPercent(dame, 60);
        }
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }

    public int subDameInjureWithDeff(int dame) {
        int def = this.def;
        // def -= calPercent(def, tlXuyenGiap);
        dame -= def;
        if (this.player.itemTime.isUseGiapXen) {
            dame /= 2;
        }
        if (this.player.itemTime.isUseGiapXen2) {
            dame -= calPercent(dame, 60);
        }
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }
    /*------------------------------------------------------------------------*/
    public boolean canOpenPower() {
        return this.power >= getPowerLimit();
    }

    public long getPowerLimit() {
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    public long getPowerNextLimit() {
        PowerLimit powerLimit = PowerLimitManager.getInstance().get(limitPower + 1);
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    // **************************************************************************
    // POWER - TIEM NANG
    public void powerUp(long power) {
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(long tiemNang) {
        this.tiemNang += tiemNang;
    }

     private boolean doUseTiemNangPet(long tiemNang) {
        if (player.pet.nPoint.tiemNang < tiemNang) {
            Service.getInstance().sendThongBaoOK(player, "Đệ không đủ tiềm năng");
            return false;
        }
        if (player.pet.nPoint.tiemNang >= tiemNang) {
            player.pet.nPoint.tiemNang -= tiemNang;
            return true;
        }
        return false;
    }
    public void increasePointPet(byte type, short point) {
        if (powerLimit == null) {
            return;
        }
        if (point <= 0) {
            return;
        }
        boolean updatePoint = false;
        long tiemNangUse = 0;
        if (type == 0) {
            int pointHp = point * 20;
            tiemNangUse = point * (2 * (player.pet.nPoint.hpg + 1000) + pointHp - 20) / 2;
            if ((player.pet.nPoint.hpg + pointHp) <= powerLimit.getHp()) {
                if (doUseTiemNangPet(tiemNangUse)) {
                    player.pet.nPoint.hpg += pointHp;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh cho đệ");
                return;
            }
        }
        if (type == 1) {
            int pointMp = point * 20;
            tiemNangUse = point * (2 * (player.pet.nPoint.mpg + 1000) + pointMp - 20) / 2;
            if ((player.pet.nPoint.mpg + pointMp) <= powerLimit.getMp()) {
                if (doUseTiemNangPet(tiemNangUse)) {
                    player.pet.nPoint.mpg += pointMp;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh đệ");
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2 * player.pet.nPoint.dameg + point - 1) / 2 * 100;
            if ((player.pet.nPoint.dameg + point) <= powerLimit.getDamage()) {
                if (doUseTiemNangPet(tiemNangUse)) {
                    player.pet.nPoint.dameg += point;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh đệ");
                return;
            }
        }
        if (type == 3) {
            tiemNangUse = 2 * (player.pet.nPoint.defg + 5) / 2 * 100000;
            if ((player.pet.nPoint.defg + point) <= powerLimit.getDefense()) {
                if (doUseTiemNangPet(tiemNangUse)) {
                    player.pet.nPoint.defg += point;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh đệ");
                return;
            }
        }
        if (type == 4) {
            tiemNangUse = 50000000L;
            for (int i = 0; i < player.pet.nPoint.critg; i++) {
                tiemNangUse *= 5L;
            }
            if ((player.pet.nPoint.critg + point) <= powerLimit.getCritical()) {
                if (doUseTiemNangPet(tiemNangUse)) {
                    player.pet.nPoint.critg += point;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh đệ");
                return;
            }
        }
        if (updatePoint) {
            Service.getInstance().showInfoPet(player);
        }
    }
    public void increasePoint(byte type, short point, boolean manualForPet) {
        if (powerLimit == null) {
            return;
        }
        if (point <= 0) {
            return;
        }
        long tiemNangUse = 0;
        boolean check = false;
        switch (type) {
            case 0:
                long hpOld = hpg;
                switch (point) {
                    case 1:
                        tiemNangUse = hpOld + 1000;
                        break;
                    case 10:
                        tiemNangUse = 10 * (2 * (hpOld + 1000) + 180) / 2;
                        break;
                    case 100:
                        tiemNangUse = 100 * (2 * (hpOld + 1000) + 1980) / 2;
                        break;
                    default:
//                        Service.getInstance().sendThongBao(player.isPet ? ((Pet) player).master : player, "Giá trị nhập vào không chính xác");
                        return;
                }
                if (tiemNang < tiemNangUse) {
                    if (player.isPet) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn không có đủ tiềm năng để cộng điểm");
                    }
                    return;
                }
                long hpNew = hpOld + 20 * point;
                if (hpNew > powerLimit.getHp()) {
                    if (player.isPet) {
                        Service.getInstance().sendThongBaoOK(player, "Hãy mở giới hạn để cộng điểm này");
                    }
                    return;
                }
                this.hpg = (int) hpNew;
                check = true;
                break;
            case 1:
                long mpOld = mpg;
                switch (point) {
                    case 1:
                        tiemNangUse = mpOld + 1000;
                        break;
                    case 10:
                        tiemNangUse = 10 * (2 * (mpOld + 1000) + 180) / 2;
                        break;
                    case 100:
                        tiemNangUse = 100 * (2 * (mpOld + 1000) + 1980) / 2;
                        break;
                    default:
//                        Service.getInstance().sendThongBao(player.isPet ? ((Pet) player).master : player, "Giá trị nhập vào không chính xác");
                        return;
                }
                if (tiemNang < tiemNangUse) {
                    if (player.isPet) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn không có đủ tiềm năng để cộng điểm");
                    }
                    return;
                }
                long mpNew = mpOld + 20 * point;
                if (mpNew > powerLimit.getMp()) {
                    if (player.isPet) {
                        Service.getInstance().sendThongBaoOK(player, "Hãy mở giới hạn để cộng điểm này");
                    }
                    return;
                }
                mpg = (int) mpNew;
                check = true;
                break;
            case 2:
                long damageOld = this.dameg;
                switch (point) {
                    case 1:
                        tiemNangUse = damageOld * 100;
                        break;
                    case 10:
                        tiemNangUse = 10 * (2 * damageOld + 9) / 2 * 100;
                        break;
                    case 100:
                        tiemNangUse = 100 * (2 * damageOld + 99) / 2 * 100;
                        break;
                    default:
//                        Service.getInstance().sendThongBao(player, "Giá trị nhập vào không chính xác");
                        return;
                }
                if (tiemNang < tiemNangUse) {
                    if (player.isPet) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn không có đủ tiềm năng để cộng điểm");
                    }
                    return;
                }
                long damageNew = damageOld + 1 * point;
                if (damageNew > powerLimit.getDamage()) {
                    if (player.isPet) {
                        Service.getInstance().sendThongBaoOK(player, "Hãy mở giới hạn để cộng điểm này");
                    }
                    return;
                }
                dameg = (int) damageNew;
                check = true;
                break;
            case 3:
                int defOld = this.defg;
                tiemNangUse = 2 * (defOld + 5) / 2 * 100000;
                if (tiemNang < tiemNangUse) {
                    if (player.isPet) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn không có đủ tiềm năng để cộng điểm");
                    }
                    return;
                }
                if (defOld >= powerLimit.getDefense()) {
                    if (player.isPet) {
                        Service.getInstance().sendThongBaoOK(player, "Hãy mở giới hạn để cộng điểm này");
                    }
                    return;
                }
                defg += 1;
                check = true;
                break;
            case 4:
                int critOld = critg;
                tiemNangUse = 50000000;
                for (byte i = 0; i < critOld; i++) {
                    tiemNangUse *= 5;
                }
                if (tiemNang < tiemNangUse) {
                    if (player.isPet) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn không có đủ tiềm năng để cộng điểm");
                    }
                    return;
                }
                if (critOld >= powerLimit.getCritical()) {
                    if (player.isPet) {
                        Service.getInstance().sendThongBaoOK(player, "Hãy mở giới hạn để cộng điểm này");
                    }
                    return;
                }
                critg += 1;
                check = true;
                break;
        }
        this.tiemNang -= tiemNangUse;
        TaskService.gI().checkDoneTaskUseTiemNang(player);
        if (check) {
            Service.getInstance().point(player);
            if (manualForPet) {
                if (player.isPet) {
                    Service.getInstance().sendChiSoPetGoc(((Pet) player).master);
                    Service.getInstance().showInfoPet(((Pet) player).master);
                }
                Service.getInstance().point(((Pet) player).master);
            }
        }
    }

    private boolean doUseTiemNang(long tiemNang) {
        if (this.tiemNang < tiemNang) {
            Service.getInstance().sendThongBaoOK(player, "Bạn không đủ tiềm năng");
            return false;
        }
        if (this.tiemNang >= tiemNang) {
            this.tiemNang -= tiemNang;
            TaskService.gI().checkDoneTaskUseTiemNang(player);
            return true;
        }
        return false;
    }

    // --------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiStamina;
    private long lastTimeTrungDoc;

    public void update() {
        if (player != null && player.effectSkill != null) {
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
                int tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {

                    PlayerService.gI().hoiPhuc(player, calLimit(calPercent(hpMax, tiLeHoiPhuc)),
                            calLimit(calPercent(mpMax, tiLeHoiPhuc)));
                    if (player.effectSkill.countCharging % 3 == 0) {
                        Service.getInstance().chat(player, "Phục hồi năng lượng " + getCurrPercentHP() + "%");
                    }
                } else {
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {
                    EffectSkillService.gI().stopCharge(player);
                }
            }
            // độc
            if (player.effectSkill.isAnDoc && player.effectSkill.countDoc < 10) {
                if (Util.canDoWithTime(lastTimeTrungDoc, 1000)) {

                    int hpTruMoiGiay = (int) calPercent(hpMax, 1);
                    if (player.isBoss) {
                        hpTruMoiGiay /= 3;
                    }
                    if (!player.isDie() && hp - hpTruMoiGiay > 5) {
                        player.injured(null, hpTruMoiGiay, true, false);
                        PlayerService.gI().sendInfoHpMpMoney(player);
                        Service.getInstance().Send_Info_NV(player);
                        if (player.effectSkill.countDoc % 3 == 0) {
                            Service.getInstance().chat(player, "Trúng độc rồi");
                        }
                    } else {
                        EffectSkillService.gI().stopDoc(player);
                    }
                    if (++player.effectSkill.countDoc >= 10) {
                        EffectSkillService.gI().stopDoc(player);
                    }

                    this.lastTimeTrungDoc = System.currentTimeMillis();
                }
            }
            if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {
                PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }
            if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();
                if (!this.player.isBoss && !this.player.isPet) {
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
        // hồi phục 30s
        // hồi phục thể lực
    }

    private void setBasePoint() {
        setHpMax();
        setMpMax();
        setDame();
        setDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setNeDon();
        setCritDame();
        setSpeed();
        setAttributeOverLimit();
    }

    public void setAttributeOverLimit() {
        int max = Integer.MAX_VALUE;
        int min = -100000000;
        if (this.hpMax < 0) {
            if (this.hpMax < min) {
                this.hpMax = max;
            } else {
                this.hpMax = 1;
            }
        }
        if (this.mpMax < 0) {
            if (this.mpMax < min) {
                this.mpMax = max;
            } else {
                this.mpMax = 1;
            }
        }
        if (this.dame < 0) {
            if (this.dame < min) {
                this.dame = max;
            } else {
                this.dame = 1;
            }
        }
        if (this.def < 0) {
            if (this.def < min) {
                this.def = max;
            } else {
                this.def = 1;
            }
        }
        if (this.crit < 0) {
            if (this.crit < min) {
                this.crit = max;
            } else {
                this.crit = 1;
            }
        }
        setHp();
        setMp();
    }

    public long calPercent(long param, int percent) {
        return param * percent / 100;
    }

    public int addInt(long param1, long param2) {
        long res = param1 + param2;

        return (int) calLimit(res);
    }

    public int calLimit(long param) {
        if (param > 2_000_000_000) {
            param = 2_000_000_000;
        }
        return (int) param;
    }

    public void dispose() {
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlSDDep = null;
        this.tlTNSM = null;
        this.tlDameCrit = null;
        this.tlSpeed = null;
    }
}
