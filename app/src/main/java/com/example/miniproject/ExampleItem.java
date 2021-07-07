package com.example.miniproject;

public class ExampleItem {
    private String place,item,itemId;

//    public ExampleItem(String place,String item)
//    {
//        this.place = place;
//        this.item = item;
//    }

    public ExampleItem()
    {

    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "ExampleItem{" +
                "place='" + place + '\'' +
                ", item='" + item + '\'' +
                '}';
    }
}
