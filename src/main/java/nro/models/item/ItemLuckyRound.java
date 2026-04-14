/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.item;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kitak
 */
public class ItemLuckyRound {

    public ItemTemplate temp;
    public int ratio;
    public int typeRatio;
    public List<ItemOptionLuckyRound> itemOptions;

    public ItemLuckyRound() {
        this.itemOptions = new ArrayList<>();
    }
}
