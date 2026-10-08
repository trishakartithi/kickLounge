package com.example.kicklounge;

public class CartItem {
    public String name;
    public String key;

    public CartItem() { }

    public CartItem(String name, String key) {

        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }
}

