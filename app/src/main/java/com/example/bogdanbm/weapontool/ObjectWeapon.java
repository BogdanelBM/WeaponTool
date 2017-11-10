package com.example.bogdanbm.weapontool;

/**
 * Created by BogdanBM on 11/10/2017.
 */

public class ObjectWeapon {
    int id;
    String description;
    String type;
    int ammoType;
    int weight;
    int price;

    public ObjectWeapon() {

    }

    @Override
    public String toString()
    {
        return "WEAPON = description: " + description+ ", type: " + type+
                ", ammoType: " + ammoType+ ", weight: " + weight+
                ", price: " + price+ ".";
    }
}