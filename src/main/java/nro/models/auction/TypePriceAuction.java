package nro.models.auction;

import nro.server.Manager;

public enum TypePriceAuction {
   GOLD_BAR("thỏi vàng"),
   GOLD("vàng"),
   RUBY("hồng ngọc"),
   GEM("ngọc xanh");

   public String name;

   private TypePriceAuction(String name) {
      this.name = name;
   }
}
