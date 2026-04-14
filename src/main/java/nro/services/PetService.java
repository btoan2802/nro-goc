package nro.services;

import nro.consts.ConstPlayer;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class PetService {

    private static PetService i;

    public static PetService gI() {
        if (i == null) {
            i = new PetService();
        }
        return i;
    }

    public static String[][] nameDetu = {{"", "Fide nhí", "Bư nhí", "Xên nhí", "Uub", "Bill", "Bill"},
    {"Black Gôku", "Black Gôku Rose", "Black Gôku White", "Black Gôku White", "Black Gôku White",
        "Black Gôku White"},
    {"Xên con", "Xên bọ hung", "Xên bọ hung 2", "Xên hoàn thiện", "Siêu bọ hung", "Siêu bọ hung"},
    {"Fide đại ca", "Fide đại ca", "Fide đại ca", "Golden Fide", "Golden Fide", "Golden Fide"},
    {"Gôku", "Gôku SSJ 1", "Gôku SSJ 3", "Gôku Blue", "Gôku God", "Gôku God"}};

    public static byte maxLevelPet = 4;

    public void createNormalPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    public void creatTrungTrungPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                // int ratio = Util.nextInt(0, 100);
                int ratio = Util.nextInt(100); // 100% ra fide
                if (ratio <= 15) {
                    createNewPet(player, false, false, true, false, false);
                } else if (ratio <= 25) {
                    createNewPet(player, false, true, false, false, false);
                } else {
                    createNewPet(player, true, false, false, false, false);
                }

                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy nhận con làm đệ tử!");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createNormalPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, false, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createMabuPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                // int ratio = Util.nextInt(0, 100);
                int ratio = 10; // 100% ra fide
                if (ratio <= 20) {
                    createNewPet(player, true, false, false, false, false);

                } else if (ratio <= 40) {
                    createNewPet(player, false, true, false, false, false);

                } else if (ratio <= 60) {
                    createNewPet(player, false, false, true, false, false);

                } else if (ratio <= 80) {
                    createNewPet(player, false, false, false, true, false);

                }
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy nhận con làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createMabuPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                // int ratio = Util.nextInt(0, 100);
                int ratio = 10; // 100% ra fide
                if (ratio <= 20) { // mabu
                    createNewPet(player, true, false, false, false, false, (byte) gender);

                } else if (ratio <= 40) {// black gk
                    createNewPet(player, false, true, false, false, false, (byte) gender);

                } else if (ratio <= 60) { // xel
                    createNewPet(player, false, false, true, false, false, (byte) gender);

                } else if (ratio <= 80) {// fide
                    createNewPet(player, false, false, false, true, false, (byte) gender);

                }
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy nhận con làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    // Super Black Gôku
    public void createWuKongPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, true, false, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Biến hình");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createWuKongPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, true, false, false, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Biến hình");
            } catch (Exception e) {
            }
        }).start();
    }

    // Super Black Gôku
    public void createGoku1Pet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, false, true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Biến hình");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createGoku1Pet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, false, true);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Biến hình");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createHeoPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, true, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Biến hình");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createHeoPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, true, false, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Biến hình");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createSatangPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, true, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Biến hình");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createSatangPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, true, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Biến hình");
            } catch (Exception e) {
            }
        }).start();
    }

    public void changeWuKOngPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createWuKongPet(player, gender, limitPower);
    }

    public void changeWukongPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createWuKongPet(player, limitPower);
    }

    public void changeGoku1Pet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGoku1Pet(player, gender, limitPower);
    }

    public void changeGoku1Pet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGoku1Pet(player, limitPower);
    }

    public void changeHeoPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createHeoPet(player, gender, limitPower);
    }

    public void changeHeoPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createHeoPet(player, limitPower);
    }

    public void changeNormalPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, gender, limitPower);
    }

    public void changeSatangPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createSatangPet(player, gender, limitPower);
    }

    public void changeSatAngPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createSatangPet(player, limitPower);
    }

    public void changeNormalPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, limitPower);
    }

    public void changeMabuPet(Player player) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, limitPower);
    }

    public void changeMabuPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, gender, limitPower);
    }

    public void changeNamePet(Player player, String name) {
        if (!InventoryService.gI().existItemBag(player, 400)) {
            Service.getInstance().sendThongBao(player, "Bạn cần thẻ đặt tên đệ tử, mua tại Santa");
            return;
        } else if (Util.haveSpecialCharacter(name)) {
            Service.getInstance().sendThongBao(player, "Tên không được chứa ký tự đặc biệt");
            return;
        } else if (name.length() > 10) {
            Service.getInstance().sendThongBao(player, "Tên quá dài");
            return;
        }
        MapService.gI().exitMap(player.pet);
        player.pet.baseName = "" + name.toLowerCase().trim();
//        player.pet.name = "$[ Cấp : " + (player.pet.LevelZeno + 1) + " ] "
//                + player.pet.baseName; // tên hiển thị
        player.pet.name = "$" + player.pet.baseName;
        InventoryService.gI().subQuantityItemsBag(player, InventoryService.gI().findItemBagByTemp(player, 400), 1);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã đặt cho con tên " + name);
            } catch (Exception e) {
            }
        }).start();
    }

    private int[] getDataPetNormal() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; // hp
        petData[1] = Util.nextInt(40, 105) * 20; // mp
        petData[2] = Util.nextInt(20, 45); // dame
        petData[3] = Util.nextInt(9, 50); // def
        petData[4] = Util.nextInt(0, 2); // crit
        return petData;
    }

    private int[] getDataPetMabu() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; // hp
        petData[1] = Util.nextInt(40, 105) * 20; // mp
        petData[2] = Util.nextInt(50, 120); // dame
        petData[3] = Util.nextInt(9, 50); // def
        petData[4] = Util.nextInt(0, 2); // crit
        return petData;
    }

    private int[] getDuongTangpet() {
        int[] hpmp = {1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 150) * 20; // hp
        petData[1] = Util.nextInt(40, 150) * 20; // mp
        petData[2] = Util.nextInt(50, 200); // dame
        petData[3] = Util.nextInt(9, 50); // def
        petData[4] = Util.nextInt(0, 2); // crit
        return petData;
    }

    public boolean isDeTuNangCap(Pet player) {
        if (player.isMabu || player.isBU || player.isCell || player.isFide
                || player.isGoku) {

            return true;
        }
        return false;
    }

    public String getNameDeTu(Pet player) {
        int typePet = 0;
        if (player.isMabu) {
            typePet = 0;
        } else if (player.isFide) {
            typePet = 1;
        } else if (player.isBU) {
            typePet = 2;
        } else if (player.isCell) {
            typePet = 3;
        } else if (player.isGoku) {
            typePet = 4;
        } else {
            return "Đệ tử";
        }
        return nameDetu[0][typePet];
    }

    public int getTypePet(Pet player) {
        int typePet = 0;
        if (player.isFide) {
            typePet = 1;
        } else if (player.isBU) {
            typePet = 2;
        } else if (player.isCell) {
            typePet = 3;
        } else if (player.isGoku) {
            typePet = 4;
        } else {
            typePet = 0;
        }
        return typePet;
    }

    public String getNameDeTuNewLevel(Pet player) {
        int typePet = 0;
        if (player.isMabu) {
            typePet = 0;
        } else if (player.isBU) {
            typePet = 1;
        } else if (player.isCell) {
            typePet = 2;
        } else if (player.isFide) {
            typePet = 3;
        } else if (player.isGoku) {
            typePet = 4;
        } else {
            return "Đệ tử";
        }
        return nameDetu[typePet][player.LevelZeno + 1];
    }

    private void createNewPet(Player player, boolean isFide, boolean isBu, boolean isCell, boolean isMabu,
            boolean isGoku, byte... gender) {
        if (player.pet != null) {
            MapService.gI().exitMap(player.pet);
        }

        int[] data = isMabu ? getDataPetMabu()
                : isBu ? getDuongTangpet()
                        : isCell ? getDuongTangpet()
                                : isFide ? getDuongTangpet() : isGoku ? getDuongTangpet() : getDataPetNormal();
        Pet pet = new Pet(player);
        // if (pet.isMabu) {
        pet.isMabu = isMabu;

        pet.isFide = isFide;
        pet.isBU = isBu;
        pet.isCell = isCell;

        pet.isGoku = isGoku;

        pet.LevelZeno = 0;
        // }
        pet.baseName = getNameDeTu(pet);

        //  pet.name = "$[ Cấp : " + (pet.LevelZeno + 1) + " ] " + pet.baseName; // tên
        // hiển thị
        pet.name = "$" + pet.baseName;

        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.nPoint.power = isMabu ? 1500000
                : isBu ? 1500000 : isCell ? 1500000 : isFide ? 1500000 : isGoku ? 15000000 : 2000;

        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 9; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 5; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.calPoint();
        pet.nPoint.setFullHpMp();
        player.pet = pet;
        player.pet.joinMapMaster();
    }

    // --------------------------------------------------------------------------
}
