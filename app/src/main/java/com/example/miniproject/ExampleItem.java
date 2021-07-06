package com.example.miniproject;

public class ExampleItem {
    private String place,item;

    public ExampleItem(String place,String item)
    {
        this.place = place;
        this.item = item;
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
}
