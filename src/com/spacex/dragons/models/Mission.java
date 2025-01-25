package com.spacex.dragons.models;

import java.util.ArrayList;
import java.util.List;

public class Mission {
    private final String name;
    private MissionStatus status;
    private final List<Rocket> rockets;

    public Mission(String name) {
        this.name = name;
        this.status = MissionStatus.SCHEDULED;
        this.rockets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public List<Rocket> getRockets() {
        return rockets;
    }

    public void addRocket(Rocket rocket) {
        rockets.add(rocket);
    }
}
