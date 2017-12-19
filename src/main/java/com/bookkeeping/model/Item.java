package com.bookkeeping.model;

import com.bookkeeping.constants.Constants;
import com.bookkeeping.utilities.ControllerUtilities;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by chandanm on 7/9/16.
 */
public class Item extends InventoryObj {
    private String barcode;
    private String value;
    private String productType;
    private String brand;
    private String itemType;
    private String size;
    private String group; //still not defined what it is
    private String itemGroup; //male,female,unisex if we want to

    //price info
    private float listPrice;
    private float discount;
    private float purchasePrice;
    /*
    public Item(String barcode) {
        super();
        this.barcode = barcode;
        this.value = barcode;
        this.setUniqueKey(ControllerUtilities.formatUniqueKey(barcode));
    }
    */

    public Item(String barcode, String productType, String brand, String itemType,float purchasePrice) {
        this.barcode = barcode;
        this.productType = productType;
        this.brand = brand;
        this.itemType = itemType;
        this.purchasePrice = ControllerUtilities.formatDecimalValue(purchasePrice);
        String[] arr = {barcode,productType,brand,itemType};
        this.value = ControllerUtilities.formatUniqueKey(StringUtils.join(arr, Constants.UNIQUE_KEY_SEPARATOR));
        this.setUniqueKey(ControllerUtilities.formatUniqueKey(barcode));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = ControllerUtilities.replaceSpaceWithSeparator(productType);
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = ControllerUtilities.replaceSpaceWithSeparator(brand);
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = ControllerUtilities.replaceSpaceWithSeparator(itemType);
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = ControllerUtilities.replaceSpaceWithSeparator(group);
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = ControllerUtilities.replaceSpaceWithSeparator(itemGroup);
    }

    public float getListPrice() {
        return listPrice;
    }

    public void setListPrice(float listPrice) {
        this.listPrice = listPrice;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public static Item createItem(int i, int j, String dateStr) {
        Item item = new Item("barcode_"+i,"productType_"+(j%200),"brand_"+j+"_"+i%3,"itemType_"+(j%10),i);
        item.setEventDate(dateStr);
        return item;
    }




}
