package com.example.bogdanbm.finalweapontool;

/**
 * Created by BogdanBM on 1/8/2018.
 */

public class Task {
    String id;
    String weaponId;
    String userId;
    String description;

    public Task(String id, String weaponId, String userId, String description) {
        this.id = id;
        this.weaponId = weaponId;
        this.userId = userId;
        this.description = description;
    }

    public Task()
    {

    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", weaponId='" + weaponId + '\'' +
                ", userId='" + userId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
