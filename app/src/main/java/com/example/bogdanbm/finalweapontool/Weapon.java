package com.example.bogdanbm.finalweapontool;

/**
 * Created by BogdanBM on 1/8/2018.
 */

public class Weapon {
    String id;
    String description;
    String type;
    int ammoType;
    int weight;
    int price;

    public Weapon()
    {

    }

    public Weapon(String id, String description, String type, int ammoType, int weight, int price) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.ammoType = ammoType;
        this.weight = weight;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", ammoType=" + ammoType +
                ", weight=" + weight +
                ", price=" + price +
                '}';
    }
}
