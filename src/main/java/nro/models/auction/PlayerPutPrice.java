package nro.models.auction;

public class PlayerPutPrice {
   private long playerId;
   private String playerName;
   private int price;
   private TypePriceAuction typePriceAuction;
   private int quantityHold;
   private long lastTimeUpdate = System.currentTimeMillis();

   public void addQuantityHold(int add) {
      this.quantityHold += add;
      this.lastTimeUpdate = System.currentTimeMillis();
   }

   public void dispose() {
      this.playerName = null;
      this.typePriceAuction = null;
   }

   public long getPlayerId() {
      return this.playerId;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public int getPrice() {
      return this.price;
   }

   public TypePriceAuction getTypePriceAuction() {
      return this.typePriceAuction;
   }

   public int getQuantityHold() {
      return this.quantityHold;
   }

   public long getLastTimeUpdate() {
      return this.lastTimeUpdate;
   }

   public void setPlayerId(long playerId) {
      this.playerId = playerId;
   }

   public void setPlayerName(String playerName) {
      this.playerName = playerName;
   }

   public void setPrice(int price) {
      this.price = price;
   }

   public void setTypePriceAuction(TypePriceAuction typePriceAuction) {
      this.typePriceAuction = typePriceAuction;
   }

   public void setQuantityHold(int quantityHold) {
      this.quantityHold = quantityHold;
   }

   public void setLastTimeUpdate(long lastTimeUpdate) {
      this.lastTimeUpdate = lastTimeUpdate;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PlayerPutPrice)) {
         return false;
      } else {
         PlayerPutPrice other = (PlayerPutPrice)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getPlayerId() != other.getPlayerId()) {
            return false;
         } else if (this.getPrice() != other.getPrice()) {
            return false;
         } else if (this.getQuantityHold() != other.getQuantityHold()) {
            return false;
         } else if (this.getLastTimeUpdate() != other.getLastTimeUpdate()) {
            return false;
         } else {
            Object this$playerName = this.getPlayerName();
            Object other$playerName = other.getPlayerName();
            if (this$playerName == null) {
               if (other$playerName != null) {
                  return false;
               }
            } else if (!this$playerName.equals(other$playerName)) {
               return false;
            }

            Object this$typePriceAuction = this.getTypePriceAuction();
            Object other$typePriceAuction = other.getTypePriceAuction();
            if (this$typePriceAuction == null) {
               if (other$typePriceAuction != null) {
                  return false;
               }
            } else if (!this$typePriceAuction.equals(other$typePriceAuction)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PlayerPutPrice;
   }

   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      long $playerId = this.getPlayerId();
      result = result * 59 + (int)($playerId >>> 32 ^ $playerId);
      result = result * 59 + this.getPrice();
      result = result * 59 + this.getQuantityHold();
      long $lastTimeUpdate = this.getLastTimeUpdate();
      result = result * 59 + (int)($lastTimeUpdate >>> 32 ^ $lastTimeUpdate);
      Object $playerName = this.getPlayerName();
      result = result * 59 + ($playerName == null ? 43 : $playerName.hashCode());
      Object $typePriceAuction = this.getTypePriceAuction();
      result = result * 59 + ($typePriceAuction == null ? 43 : $typePriceAuction.hashCode());
      return result;
   }

   public String toString() {
      return "PlayerPutPrice(playerId=" + this.getPlayerId() + ", playerName=" + this.getPlayerName() + ", price=" + this.getPrice() + ", typePriceAuction=" + this.getTypePriceAuction() + ", quantityHold=" + this.getQuantityHold() + ", lastTimeUpdate=" + this.getLastTimeUpdate() + ")";
   }
}
