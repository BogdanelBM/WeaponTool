package com.example.bogdanbm.weapontool;

/**
 * Created by BogdanBM on 11/10/2017.
 */

public class ObjectTask {
    int id;
    String description;
    int weaponId;

    public ObjectTask() {

    }

    @Override
    public String toString() {
        return "ObjectTask{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", weaponId=" + weaponId +
                '}';
    }
}