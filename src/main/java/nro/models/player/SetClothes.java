package nro.models.player;

import nro.models.item.Item;
import nro.models.item.ItemOption;

public class SetClothes {

    private Player player;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte songoku;
    public byte thienXinHang;
    public byte kirin;

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;

    public byte kakarot;
    public byte vegeta;
    public byte nappa;

    public byte sietViet;

    public int ctHaiTac = -1;
    public int ctDrSlum = -1;
    public byte huydietClothers;
    public byte godClothes;

    public byte SieuVietKame;
    public byte SieuVietHp;
    public byte SieuVietLaze;
    public byte setLevel7;
    public byte setLevel8;
    public byte nhat_an;
    public byte tinhan;
    public byte nguyetan;
    public byte nhatan;

    public void setup() {
        setDefault();
        setupSKT();
        setupAN();
        setThanLinh();
        sethuydiet();
        setupSetLevel();
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 612:
                case 613:
                case 614:
                    this.ctDrSlum = ct.template.id;
                    break;
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
                    this.ctHaiTac = ct.template.id;
                    break;
            }
        }
    }

    private void setupAN() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSett = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 34:
                            isActSett = true;
                            tinhan++;
                            break;
                        case 35:
                            isActSett = true;
                            nguyetan++;
                            break;
                        case 36:
                            isActSett = true;
                            nhatan++;
                            break;
                    }
                    if (isActSett) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupSetLevel() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 72:// cấp
                            if (io.param >= 6) {
                                setLevel7++;
                            }
                            if (io.param == 7) {
                                setLevel8++;
                            }
                            if (setLevel8 == 5) {
                                setLevel7 = 0;
                            }
                            break;
                    }
                }
            } else {
                break;
            }
        }
    }

    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 216:
                        case 217:
                            isActSet = true;
                            nhat_an++;
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                            kirin++;
                            break;
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                            break;

                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;

                        case 135:
                        case 138:
                            isActSet = true;
                            nappa++;
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            vegeta++;
                            break;
                        case 224:
                        case 225:
                            isActSet = true;
                            sietViet++;
                            break;
                        case 227:
                        case 230:
                            isActSet = true;
                            SieuVietKame++;
                            break;
                        case 228:
                        case 231:
                            isActSet = true;
                            SieuVietHp++;
                            break;
                        case 229:
                        case 232:
                            isActSet = true;
                            SieuVietLaze++;
                            break;
                    }
                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    // checksetthanlinh
    public void setThanLinh() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 555 && item.template.id <= 568) {
                    godClothes++;
                }
            }
        }
    }

    // check set huy diet
    public void sethuydiet() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 650 && item.template.id <= 663) {
                    huydietClothers++;
                }
            }
        }
    }

    private void setDefault() {
        this.songoku = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.kakarot = 0;
        this.vegeta = 0;
        this.nappa = 0;
        this.ctHaiTac = -1;
        this.ctDrSlum = -1;
        this.godClothes = 0;
        this.huydietClothers = 0;
        this.sietViet = 0;
        this.SieuVietKame = 0;
        this.SieuVietHp = 0;
        this.SieuVietLaze = 0;
        this.nhat_an = 0;
        this.setLevel7 = 0;
        this.setLevel8 = 0;
        this.tinhan = 0;
        this.nhatan = 0;
        this.nguyetan = 0;
    }

    public void dispose() {
        this.player = null;
    }
}
