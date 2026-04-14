package nro.models.auction;

import nro.models.item.Item;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.server.Client;
import nro.server.ServerManager;
import nro.server.ServerNotify;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.utils.TimeUtil;
import nro.utils.Util;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstNpc;
import nro.models.item.CaiTrang;
import nro.models.item.ItemOption;
import nro.server.Manager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class AuctionService implements Runnable {
   private static AuctionService I;
   private final List<PlayerWinAuction> playerWinAuctions = new ArrayList();
   private boolean started;
   private final int PRICE_PUT_PRICE = 100000000;
   private final float DEPOSIT = 10.0F;
   private final String[][] textsNpcDauGia = new String[][]{{"Chào mừng các bạn đã tới với buổi đấu giá ngày hôm nay!", "Phiên đấu giá tiếp theo sẽ bắt đầu vào lúc %s và kết thúc lúc %s", "Hãy nhớ có mặt tại đây đúng giờ để tham gia nhé", "Nhiều vật phẩm quý giá đang đợi bạn đó..."}, {"Chào mừng các bạn đã tới với buổi đấu giá ngày hôm nay!", "Mức giá khởi điểm cho sản phẩm ngày hôm nay là %s", "Buổi đấu giá hôm nay bao gồm %s", "%s đang là người chơi trả giá cao nhất với giá trị %s", "Hãy mau tham gia để có thể sở hữu vật phẩm này, chỉ xuất hiện trong buổi đấu giá ngày hôm nay"}};
   private int indexTextChat;
   private boolean isEnded;
   private List auctions = new ArrayList();
   private Auction currentAuction;
   private List<PlayerPutPrice> playerPutPrices = new ArrayList();
   private int tallestPrice;
   private long lastTimeUpdate;
   private int timeUpdate = 500;
   private long lastTimeNotifyAuction;
   private int timeNotifyAuction = 120000;

   public static AuctionService gI() {
      if (I == null) {
         I = new AuctionService();
      }

      I.start();
      return I;
   }

   private void start() {
      if (!this.started && ServerManager.isRunning) {
         this.started = true;
         (new Thread(I, "Update đấu giá")).start();
      }
   }

   public void run() {
     while (ServerManager.isRunning) {
         this.update();

         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var2) {
         }
      }

   }

   private AuctionService() {
      this.initTest();
   } 

   public static void main(String[] args) throws Exception {
      System.out.println(TimeUtil.stringToTime("7/1/2024 15:52", "dd/MM/yyyy HH:mm"));
      System.out.println(TimeUtil.stringToTime("7/1/2024 15:55", "dd/MM/yyyy HH:mm"));
   }

   public void loadAuction() {
      try {
         for(Object o : (JSONArray)JSONValue.parse(new FileReader("image/data/auction/auction.kenit"))) {
            JSONObject dataAuction = (JSONObject)JSONValue.parse(String.valueOf(o));
            long startTime = Long.parseLong(String.valueOf(dataAuction.get("startTime")));
            long endTime = Long.parseLong(String.valueOf(dataAuction.get("endTime")));
            int startingPrice = Integer.parseInt(String.valueOf(dataAuction.get("startingPrice")));
            int stepPrice = Integer.parseInt(String.valueOf(dataAuction.get("stepPrice")));
            TypePriceAuction type = TypePriceAuction.valueOf(String.valueOf(dataAuction.get("typePriceAuction")));
            String dataItems = String.valueOf(dataAuction.get("itemsAuction"));
            Auction auction = new Auction(startTime, endTime, startingPrice, type, stepPrice);
            auction.loadItems(dataItems);
            this.auctions.add(auction);
            auction.name = String.valueOf(dataAuction.get("name"));
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private void initTest() {
   }

   public void putPlayerPrice(Player player, int price) {
      if (!this.playerPutPrices.isEmpty() && ((PlayerPutPrice)this.playerPutPrices.get(0)).getPlayerId() == player.id) {
         Service.getInstance().sendThongBao(player, "Bạn không thể trả giá với chính mình!");
      } else if (!this.isEnded && this.currentAuction != null && !this.currentAuction.isEnd()) {
         if (!this.currentAuction.isStart()) {
            Service.getInstance().sendThongBao(player, "Phiên đấu giá chưa bắt đầu");
         } else if (player.inventory.gold < 100000000L) {
            Service.getInstance().sendThongBao(player, "Bạn không đủ vàng cho lần trả giá này, còn thiếu " + Util.numberToMoney(100000000L - player.inventory.gold) + " vàng");
         } else if (this.tallestPrice > 100 && price > this.tallestPrice + this.tallestPrice / 2) {
            Service.getInstance().sendThongBao(player, "Bạn không thể trả giá cao hơn 150% so với người trả trước! Chỉ có thể trả giá thấp hơn " + (this.tallestPrice + this.tallestPrice / 2) + ".");
         } else if (price < this.tallestPrice + this.currentAuction.getStepPrice()) {
            Service.getInstance().sendThongBao(player, "Hiện tại đã có người trả giá " + this.tallestPrice + ", bước giá cho phiên hiện tại là " + this.currentAuction.getStepPrice() + ", bạn chỉ có thể trả giá lớn hơn hoặc bằng " + (this.tallestPrice + this.currentAuction.getStepPrice()));
         } else if (price < this.currentAuction.getStartingPrice()) {
            Service.getInstance().sendThongBao(player, "Không thể trả giá thấp hơn mức giá khởi điểm");
         } else {
            switch (this.currentAuction.getTypePriceAuction()) {
               case GOLD_BAR:
                  Item goldbar = null;

                  try {
                     goldbar = InventoryServiceNew.gI().findItemBag(player, 457);
                  } catch (Exception var7) {
                  }

                  if (goldbar == null || goldbar.quantity < price) {
                     Service.getInstance().sendThongBao(player, "Bạn không có đủ thỏi vàng để thực hiện");
                     return;
                  } else {
                     PlayerPutPrice playerPutPrice = null;

                     for(PlayerPutPrice p : this.playerPutPrices) {
                        if (p.getPlayerId() == player.id) {
                           playerPutPrice = p;
                           this.playerPutPrices.remove(p);
                           break;
                        }
                     }

                     if (playerPutPrice == null) {
                        playerPutPrice = new PlayerPutPrice();
                        playerPutPrice.setPlayerId(player.id);
                        playerPutPrice.setPlayerName(player.name);
                        playerPutPrice.setTypePriceAuction(this.currentAuction.getTypePriceAuction());
                     }

                     this.playerPutPrices.add(0, playerPutPrice);
                     this.tallestPrice = price;
                     playerPutPrice.setPrice(price);
                     int quantityGoldbarHold = Math.round((float)price * 10.0F / 100.0F);
                     playerPutPrice.addQuantityHold(quantityGoldbarHold);
                     Service.getInstance().sendThongBao(player, "Đặt giá thành công! Npc Đấu giá giữ của bạn " + quantityGoldbarHold + " thỏi vàng.");
                     InventoryServiceNew.gI().subQuantityItemsBag(player, goldbar, quantityGoldbarHold);
                     InventoryServiceNew.gI().sendItemBags(player);
                     ItemTimeService.gI().sendTextAuction(player);
                     // chưa làm kỹ
                     ServerNotify.gI().notify(player.name + " vừa trả giá " + price + " " + this.currentAuction.getTypePriceAuction().name + " cho vật phẩm đấu giá! Hãy mau tới làng Kakarot tham gia phiên đấu giá nào.");
                  }
               default:
                  Inventory var10000 = player.inventory;
                  var10000.gold -= 100000000L;
                  Service.getInstance().sendMoney(player);
            }
         }
      } else {
         Service.getInstance().sendThongBao(player, "Phiên đấu giá đã kết thúc");
      }
   }

   public void update() {
      if (!this.isEnded) {
         this.notifyAuction();
         if (Util.canDoWithTime(this.lastTimeUpdate, (long)this.timeUpdate)) {
            this.lastTimeUpdate = System.currentTimeMillis();
            if (this.currentAuction != null) {
               if (!this.currentAuction.isEnd()) {
                  return;
               }

               this.finishAuction();
               this.reset();
               this.currentAuction = null;
               if (this.auctions.isEmpty()) {
               }
            } else {
               long nearlyTime = 86400000L;
               Auction auction = null;

               for(int i = this.auctions.size() - 1; i >= 0; --i) {
                  Auction auct = (Auction)this.auctions.get(i);
                  long currentTime = System.currentTimeMillis();
                  if (currentTime >= auct.getStartTime() && currentTime <= auct.getEndTime()) {
                     auction = auct;
                     break;
                  }

                  if (currentTime > auct.getEndTime()) {
                     this.auctions.remove(i);
                  } else if (currentTime < auct.getStartTime() && auct.getStartTime() - currentTime < nearlyTime) {
                     nearlyTime = auct.getStartTime() - currentTime;
                     auction = auct;
                  }
               }

               this.currentAuction = auction;
               this.auctions.remove(auction);
            }

         }
      }
   }

   private void notifyAuction() {
      if (Util.canDoWithTime(this.lastTimeNotifyAuction, (long)this.timeNotifyAuction)) {
         this.lastTimeNotifyAuction = System.currentTimeMillis();
         if (!this.isEnded && this.currentAuction != null && !this.currentAuction.isEnd()) {
            if (this.currentAuction.isStart()) {
               String text = "Phiên đấu giá đang diễn ra, hãy tới làng Kakarot tham dự để nhận được quà hấp dẫn!";
               ServerNotify.gI().notify(text); // chưa làm kỹ
            } else {
               String text = "Phiên đấu giá sắp tới sẽ bắt đầu vào lúc " + TimeUtil.formatTime(this.currentAuction.getStartTime(), "HH:mm") + ", hãy tới làng Kakarot để tham dự buổi đấu giá!";
               ServerNotify.gI().notify(text); // chưa làm kỹ
            }

         }
      }
   }

   private void finishAuction() {
      for(PlayerPutPrice p : this.playerPutPrices) {
         Player player = Client.gI().getPlayer(p.getPlayerId());
         if (player != null) {
            switch (p.getTypePriceAuction()) {
               case GOLD_BAR:
                  Item goldbar = ItemService.gI().createNewItem((short)457, p.getQuantityHold());
                  InventoryServiceNew.gI().addItemBag(player, goldbar);
                  InventoryServiceNew.gI().sendItemBags(player);
                  Service.getInstance().sendThongBao(player, "Bạn vừa được hoàn trả " + p.getQuantityHold() + " thỏi vàng trong phiên đấu giá");
            }
         }
      }

      this.exchangeItems();
   }

   private void exchangeItems() {
      if (this.playerPutPrices != null && !this.playerPutPrices.isEmpty()) {
         PlayerPutPrice playerPutPrice = (PlayerPutPrice)this.playerPutPrices.remove(0);
         Player player = Client.gI().getPlayer(playerPutPrice.getPlayerId());
         if (player == null) {
            this.exchangeItems();
         } else {
            switch (playerPutPrice.getTypePriceAuction()) {
               case GOLD_BAR:
                  Item goldbar = null;

                  try {
                     goldbar = InventoryServiceNew.gI().findItemBag(player, 457);
                  } catch (Exception var7) {
                  }

                  if (goldbar == null || goldbar.quantity < playerPutPrice.getPrice()) {
                     InventoryServiceNew.gI().subQuantityItemsBag(player, goldbar, playerPutPrice.getQuantityHold());
                     Service.getInstance().sendThongBao(player, "Bạn bị phạt " + playerPutPrice.getQuantityHold() + " " + playerPutPrice.getTypePriceAuction().name + " do bạn không đủ thỏi vàng nhận vật phẩm đấu giá");
                     this.exchangeItems();
                     return;
                  } else {
                     String itemName = "";

                     for(Item item : this.currentAuction.getItemsAuction()) {
                        itemName = itemName + item.template.name + ", ";
                        player.inventory.itemsReward.add(item); // nhận item
                     }

                     InventoryServiceNew.gI().subQuantityItemsBag(player, goldbar, playerPutPrice.getPrice());
                     InventoryServiceNew.gI().sendItemBags(player);
                     String text = "Chúc mừng " + player.name + " vừa thắng cược trong phiên đấu giá với mức cược " + playerPutPrice.getPrice() + " thỏi vàng";
                     ServerNotify.gI().notify(text); // chưa làm kỹ
                     ServerNotify.gI().notify(text); // chưa làm kỹ
                     PlayerWinAuction p = new PlayerWinAuction();
                     p.name = player.name;
                     p.itemName = itemName.substring(0, itemName.length() - 2);
                     p.cost = playerPutPrice.getPrice();
                     p.type = playerPutPrice.getTypePriceAuction();
                     this.playerWinAuctions.add(p);
                  }
               default:
            }
         }
      }
   }

   private void reset() {
      for(PlayerPutPrice playerPutPrice : this.playerPutPrices) {
         if (playerPutPrice != null) {
            playerPutPrice.dispose();
         }
      }

      this.playerPutPrices.clear();
      this.tallestPrice = 0;
   }

   public String getTextNpcDauGiaSay() {
      if (!this.isEnded && this.currentAuction != null && !this.currentAuction.isEnd()) {
         if (this.currentAuction.isStart()) {
            String text = "Phiên đấu giá đang diễn ra với mức giá khởi điểm là " + this.currentAuction.getTextStartingPrice() + " (bước giá " + this.currentAuction.getStepPrice() + ")";
            if (!this.playerPutPrices.isEmpty()) {
               text = text + "\nNgười chơi: " + ((PlayerPutPrice)this.playerPutPrices.get(0)).getPlayerName() + " đang trả giá " + ((PlayerPutPrice)this.playerPutPrices.get(0)).getPrice() + " " + this.currentAuction.getTypePriceAuction().name;
            }

            text = text + "\nCậu có muốn tham gia không?";
            return text;
         } else {
            String text = "Phiên đấu giá sắp tới sẽ bắt đầu vào lúc " + TimeUtil.formatTime(this.currentAuction.getStartTime(), "HH:mm") + ", hãy có mặt đúng giờ tại đây để tham gia nhé.";
            return text;
         }
      } else {
         return "Phiên đấu giá đã kết thúc, vui lòng đợi tới phiên giá tiếp theo!";
      }
   }

   public String getTextInfoPlayersWin() {
      String subLine = new String(new byte[]{8});
      String text = "Danh sách người chơi thắng phiên đấu giá" + subLine;
      int index = 1;

      for(PlayerWinAuction p : this.playerWinAuctions) {
         text = text + index + ". " + p.getName() + ": " + p.getItemName() + " (" + p.getCost() + " " + p.getType().name + ")" + subLine;
      }

      return text;
   }

   public String getTextInfoPlayersJoin() {
      String text = "Danh sách người chơi trả giá cho sản phẩm" + new String(new byte[]{8});
      if (this.playerPutPrices.size() < 10) {
         int index = 1;

         for(PlayerPutPrice p : this.playerPutPrices) {
            text = text + index + ". " + p.getPlayerName() + " trả giá " + p.getPrice() + " " + p.getTypePriceAuction().name + " (tổng cọc: " + p.getQuantityHold() + " " + p.getTypePriceAuction().name + ")" + new String(new byte[]{8});
            ++index;
         }
      } else {
         for(int i = 0; i < 10; ++i) {
            PlayerPutPrice p = (PlayerPutPrice)this.playerPutPrices.get(i);
            text = text + (i + 1) + ". " + p.getPlayerName() + " trả giá " + p.getPrice() + " " + p.getTypePriceAuction().name + " (tổng cọc: " + p.getQuantityHold() + " " + p.getTypePriceAuction().name + ")" + new String(new byte[]{8});
         }
      }

      return text;
   }

   public String getTextChatNpcDauGia() {
      if (!this.isEnded && this.currentAuction != null) {
         String text = null;
         if (!this.currentAuction.isStart()) {
            if (this.indexTextChat >= this.textsNpcDauGia[0].length) {
               this.indexTextChat = 0;
            }

            text = this.textsNpcDauGia[0][this.indexTextChat];
            switch (this.indexTextChat) {
               case 1:
                  text = String.format(text, TimeUtil.formatTime(this.currentAuction.getStartTime(), "HH:mm"), TimeUtil.formatTime(this.currentAuction.getEndTime(), "HH:mm"));
            }
         } else {
            if (this.indexTextChat >= this.textsNpcDauGia[0].length) {
               this.indexTextChat = 0;
            }

            if (this.indexTextChat == 3 && this.playerPutPrices.isEmpty()) {
               ++this.indexTextChat;
            }

            text = this.textsNpcDauGia[1][this.indexTextChat];
            switch (this.indexTextChat) {
               case 1:
                  text = String.format(text, this.currentAuction.getTextStartingPrice());
                  break;
               case 2:
                  text = String.format(text, this.currentAuction.getTextItems());
                  break;
               case 3:
                  text = String.format(text, ((PlayerPutPrice)this.playerPutPrices.get(0)).getPlayerName(), ((PlayerPutPrice)this.playerPutPrices.get(0)).getPrice() + " " + ((PlayerPutPrice)this.playerPutPrices.get(0)).getTypePriceAuction().name);
            }
         }

         ++this.indexTextChat;
         return text;
      } else {
         return "Buổi đấu giá ngày hôm nay đã kết thúc, hẹn các bạn vào lần tới!";
      }
   }

   public boolean isJoined(Player player) {
      if (player == null) {
         return false;
      } else {
         return !this.isEnded && this.currentAuction != null && !this.currentAuction.isEnd() ? this.playerPutPrices.stream().anyMatch((p) -> p.getPlayerId() == player.id) : false;
      }
   }

   public long getTimeFinish() {
      return !this.isEnded && this.currentAuction != null && !this.currentAuction.isEnd() ? this.currentAuction.getEndTime() : -1L;
   }

   public boolean isStarted() {
      return !this.isEnded && this.currentAuction != null && !this.currentAuction.isEnd() ? this.currentAuction.isStart() : false;
   }

   public void showItemsAuction(Player player) {
      if (!this.isEnded && this.currentAuction != null && !this.currentAuction.isEnd()) {
         if (!this.currentAuction.isStart()) {
            Service.getInstance().sendThongBao(player, "Phiên đấu giá chưa bắt đầu");
         } else {
               player.iDMark.setShopId(ConstNpc.MENU_ITEM_DAUGIA);
            try {
               Message msg = new Message(-44);
               msg.writer().writeByte(4);
               msg.writer().writeByte(1);
               msg.writer().writeUTF("Vật phẩm");
               msg.writer().writeByte(this.currentAuction.getItemsAuction().size());

               for(Item item : this.currentAuction.getItemsAuction()) {
                  msg.writer().writeShort(item.template.id);
                  msg.writer().writeUTF("\n|7|Vật phẩm đấu giá");
                  msg.writer().writeByte(item.itemOptions.size() + 1);

                  for(ItemOption io : item.itemOptions) {
                     msg.writer().writeByte(io.optionTemplate.id);
                     msg.writer().writeShort(io.param);
                  }

                  msg.writer().writeByte(31);
                  msg.writer().writeShort(item.quantity);
                  msg.writer().writeByte(1);
                  if (item.template.type == 5) { //code lại theo sever
                    CaiTrang ct = Manager.gI().getCaiTrangByItemId(item.template.id);
                    msg.writer().writeByte(1);
                    msg.writer().writeShort(ct.getID()[0]);
                    msg.writer().writeShort(ct.getID()[1]);
                    msg.writer().writeShort(ct.getID()[2]);
                    msg.writer().writeShort(ct.getID()[3]);
                  } else {
                     msg.writer().writeByte(0);
                  }
               }

               player.sendMessage(msg);
               msg.cleanup();
            } catch (Exception e) {
               e.printStackTrace();
            }

         }
      } else {
         Service.getInstance().sendThongBao(player, "Phiên đấu giá đã kết thúc");
      }
   }

   private class PlayerWinAuction {
      private String name;
      private String itemName;
      private int cost;
      private TypePriceAuction type;

      public PlayerWinAuction() {
      }

      public String getName() {
         return this.name;
      }

      public String getItemName() {
         return this.itemName;
      }

      public int getCost() {
         return this.cost;
      }

      public TypePriceAuction getType() {
         return this.type;
      }

      public void setName(String name) {
         this.name = name;
      }

      public void setItemName(String itemName) {
         this.itemName = itemName;
      }

      public void setCost(int cost) {
         this.cost = cost;
      }

      public void setType(TypePriceAuction type) {
         this.type = type;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof PlayerWinAuction)) {
            return false;
         } else {
            PlayerWinAuction other = (PlayerWinAuction)o;
            if (!other.canEqual(this)) {
               return false;
            } else if (this.getCost() != other.getCost()) {
               return false;
            } else {
               Object this$name = this.getName();
               Object other$name = other.getName();
               if (this$name == null) {
                  if (other$name != null) {
                     return false;
                  }
               } else if (!this$name.equals(other$name)) {
                  return false;
               }

               Object this$itemName = this.getItemName();
               Object other$itemName = other.getItemName();
               if (this$itemName == null) {
                  if (other$itemName != null) {
                     return false;
                  }
               } else if (!this$itemName.equals(other$itemName)) {
                  return false;
               }

               Object this$type = this.getType();
               Object other$type = other.getType();
               if (this$type == null) {
                  if (other$type != null) {
                     return false;
                  }
               } else if (!this$type.equals(other$type)) {
                  return false;
               }

               return true;
            }
         }
      }

      protected boolean canEqual(Object other) {
         return other instanceof PlayerWinAuction;
      }

      public int hashCode() {
         int PRIME = 59;
         int result = 1;
         result = result * 59 + this.getCost();
         Object $name = this.getName();
         result = result * 59 + ($name == null ? 43 : $name.hashCode());
         Object $itemName = this.getItemName();
         result = result * 59 + ($itemName == null ? 43 : $itemName.hashCode());
         Object $type = this.getType();
         result = result * 59 + ($type == null ? 43 : $type.hashCode());
         return result;
      }

      public String toString() {
         return "AuctionService.PlayerWinAuction(name=" + this.getName() + ", itemName=" + this.getItemName() + ", cost=" + this.getCost() + ", type=" + this.getType() + ")";
      }
   }
}
