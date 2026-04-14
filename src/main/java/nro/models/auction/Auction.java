package nro.models.auction;

import nro.models.item.Item;
import nro.services.ItemService;
import java.util.ArrayList;
import java.util.List;
import nro.models.item.ItemOption;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Auction {
   public String name;
   private List<Item> itemsAuction = new ArrayList();
   private long startTime;
   private long endTime;
   private int startingPrice;
   private TypePriceAuction typePriceAuction;
   private int stepPrice;

   public Auction(long startTime, long endTime, int startingPrice, TypePriceAuction typePriceAuction, int stepPrice) {
      this.startTime = startTime;
      this.endTime = endTime;
      this.startingPrice = startingPrice;
      this.typePriceAuction = typePriceAuction;
      this.stepPrice = stepPrice;
   }

   public String getTextStartingPrice() {
      return this.startingPrice + " " + this.typePriceAuction.name;
   }

   public String getTextItems() {
      String text = "";

      for(Item item : this.itemsAuction) {
         text = text + item.quantity + " " + item.template.name + ", ";
      }

      return text.substring(0, text.length() - 2);
   }

   public boolean isStart() {
      long currentTime = System.currentTimeMillis();
      return currentTime >= this.startTime && currentTime <= this.endTime;
   }

   public boolean isEnd() {
      return System.currentTimeMillis() > this.endTime;
   }

   public void loadItems(String json) {
      JSONArray dataArray = (JSONArray)JSONValue.parse(json);

      for(int i = 0; i < dataArray.size(); ++i) {
         JSONObject itemInfo = (JSONObject)JSONValue.parse(String.valueOf(dataArray.get(i)));
         Item item = ItemService.gI().createNewItem((short) Integer.parseInt(String.valueOf(itemInfo.get("temp_id"))), Integer.parseInt(String.valueOf(itemInfo.get("quantity"))));
         JSONArray options = (JSONArray)JSONValue.parse(String.valueOf(itemInfo.get("options")));

         for(int j = 0; j < options.size(); ++j) {
            JSONObject option = (JSONObject)JSONValue.parse(String.valueOf(options.get(j)));
           ItemOption io = new ItemOption(Integer.parseInt(String.valueOf(option.get("id"))), Integer.parseInt(String.valueOf(option.get("param"))));
            item.itemOptions.add(io);
         }

         this.itemsAuction.add(item);
      }

   }

   public void dispose() {
      if (this.itemsAuction != null) {
         this.itemsAuction.clear();
         this.itemsAuction = null;
      }

   }

   public String getName() {
      return this.name;
   }

   public List<Item> getItemsAuction() {
      return this.itemsAuction;
   }

   public long getStartTime() {
      return this.startTime;
   }

   public long getEndTime() {
      return this.endTime;
   }

   public int getStartingPrice() {
      return this.startingPrice;
   }

   public TypePriceAuction getTypePriceAuction() {
      return this.typePriceAuction;
   }

   public int getStepPrice() {
      return this.stepPrice;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setItemsAuction(List itemsAuction) {
      this.itemsAuction = itemsAuction;
   }

   public void setStartTime(long startTime) {
      this.startTime = startTime;
   }

   public void setEndTime(long endTime) {
      this.endTime = endTime;
   }

   public void setStartingPrice(int startingPrice) {
      this.startingPrice = startingPrice;
   }

   public void setTypePriceAuction(TypePriceAuction typePriceAuction) {
      this.typePriceAuction = typePriceAuction;
   }

   public void setStepPrice(int stepPrice) {
      this.stepPrice = stepPrice;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Auction)) {
         return false;
      } else {
         Auction other = (Auction)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getStartTime() != other.getStartTime()) {
            return false;
         } else if (this.getEndTime() != other.getEndTime()) {
            return false;
         } else if (this.getStartingPrice() != other.getStartingPrice()) {
            return false;
         } else if (this.getStepPrice() != other.getStepPrice()) {
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

            Object this$itemsAuction = this.getItemsAuction();
            Object other$itemsAuction = other.getItemsAuction();
            if (this$itemsAuction == null) {
               if (other$itemsAuction != null) {
                  return false;
               }
            } else if (!this$itemsAuction.equals(other$itemsAuction)) {
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
      return other instanceof Auction;
   }

   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      long $startTime = this.getStartTime();
      result = result * 59 + (int)($startTime >>> 32 ^ $startTime);
      long $endTime = this.getEndTime();
      result = result * 59 + (int)($endTime >>> 32 ^ $endTime);
      result = result * 59 + this.getStartingPrice();
      result = result * 59 + this.getStepPrice();
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $itemsAuction = this.getItemsAuction();
      result = result * 59 + ($itemsAuction == null ? 43 : $itemsAuction.hashCode());
      Object $typePriceAuction = this.getTypePriceAuction();
      result = result * 59 + ($typePriceAuction == null ? 43 : $typePriceAuction.hashCode());
      return result;
   }

   public String toString() {
      return "Auction(name=" + this.getName() + ", itemsAuction=" + this.getItemsAuction() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", startingPrice=" + this.getStartingPrice() + ", typePriceAuction=" + this.getTypePriceAuction() + ", stepPrice=" + this.getStepPrice() + ")";
   }
}
