///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package nro.models.npc.npcList;
//
//import nro.consts.ConstNpc;
//import nro.models.auction.AuctionService;
//import nro.models.npc.Npc;
//import nro.models.player.Player;
//import nro.services.NpcMethod;
//import nro.services.NpcService;
//import nro.services.Service;
//import nro.services.func.CombineServiceNew;
//import nro.services.func.Input;
//import nro.services.func.ShopService;
//import nro.utils.Util;
//
///**
// *
// * @author KENIT
// */
//public class Daugia extends Npc {
//
//    public Daugia(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        super(mapId, status, cx, cy, tempId, avartar);
//    }
//
//    @Override
//    public void openBaseMenu(Player player) {
//        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                "Ngươi muốn gì",
//                "Trả giá",
//                "Thông tin\nvật phẩm\nđấu giá",
//                "Thông tin\nngười chơi\ntham gia",
//                "Hướng dẫn",
//                "Thông tin\nngười chơi\nchiến thắng",
//                "Từ chối");
//    }
//
//    public void update() {
//        super.update();
//        this.chat();
//    }
//
//    private void chat() {
//        if (Util.canDoWithTime(this.lastTimeChat, (long) this.timeChat)) {
//            this.lastTimeChat = System.currentTimeMillis();
//            this.timeChat = Util.nextInt(3, 7) * 1000;
//            this.npcChat(AuctionService.gI().getTextChatNpcDauGia());
//        }
//    }
//
//    @Override
//    public void confirmMenu(Player player, int select) {
//        if (canOpenNpc(player)) {
//            if (player.iDMark.isBaseMenu()) {
//                switch (select) {
//                    case 0:
//                        if (!AuctionService.gI().isStarted()) {
//                            Service.getInstance().sendThongBao(player, "Hiện tại chưa có phiên đấu giá nào diễn ra");
//                            return;
//                        }
//                        Input.gI().createFormPutPriceAuction(player);
//                        break;
//                    case 1:
//                        AuctionService.gI().showItemsAuction(player);
//                        break;
//                    case 2:
//                        if (AuctionService.gI().isStarted()) {
//                            NpcService.gI().createTutorial(player, this.avartar, AuctionService.gI().getTextInfoPlayersJoin());
//                        } else {
//                            Service.getInstance().sendThongBao(player, "Hiện tại chưa có phiên đấu giá nào diễn ra");
//                        }
//                        break;
//                    case 3:
//                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DAU_GIA);
//                        break;
//                    case 4:
//                        NpcService.gI().createTutorial(player, this.avartar, AuctionService.gI().getTextInfoPlayersWin());
//                }
//            }
//        }
//    }
//}
