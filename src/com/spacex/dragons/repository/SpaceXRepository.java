package com.spacex.dragons.repository;

import com.spacex.dragons.models.Mission;
import com.spacex.dragons.models.MissionStatus;
import com.spacex.dragons.models.Rocket;
import com.spacex.dragons.models.RocketStatus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpaceXRepository {
    private final Map<String, Rocket> rockets = new HashMap<>();
    private final Map<String, Mission> missions = new HashMap<>();

    public void addRocket(String rocketName) {
        if (!rockets.containsKey(rocketName)) {
            rockets.put(rocketName, new Rocket(rocketName));
        }
    }

    public void addMission(String missionName) {
        if (!missions.containsKey(missionName)) {
            missions.put(missionName, new Mission(missionName));
        }
    }

    public void assignRocketToMission(String rocketName, String missionName) {
        Rocket rocket = rockets.get(rocketName);
        Mission mission = missions.get(missionName);

        if (rocket == null || mission == null) {
            throw new IllegalArgumentException("Rocket or Mission does not exist.");
        }

        if (rocket.getStatus() != RocketStatus.ON_GROUND) {
            throw new IllegalStateException("Rocket is not available for assignment.");
        }

        rocket.setStatus(RocketStatus.IN_SPACE);
        mission.addRocket(rocket);
        updateMissionStatus(mission);
    }

    public void changeRocketStatus(String rocketName, RocketStatus status) {
        Rocket rocket = rockets.get(rocketName);
        if (rocket != null) {
            rocket.setStatus(status);
            missions.values().forEach(this::updateMissionStatus);
        }
    }

    public void changeMissionStatus(String missionName, MissionStatus status) {
        Mission mission = missions.get(missionName);
        if (mission != null) {
            mission.setStatus(status);
        }
    }

    public List<String> getMissionSummary() {
        return missions.values().stream()
                .sorted(Comparator.comparingInt((Mission m) -> m.getRockets().size()).reversed()
                        .thenComparing(Mission::getName))
                .map(m -> String.format("%s - %s - Dragons: %d", m.getName(), m.getStatus(), m.getRockets().size()))
                .collect(Collectors.toList());
    }

    private void updateMissionStatus(Mission mission) {
        if (mission.getRockets().isEmpty()) {
            mission.setStatus(MissionStatus.SCHEDULED);
        } else if (mission.getRockets().stream().anyMatch(r -> r.getStatus() == RocketStatus.IN_REPAIR)) {
            mission.setStatus(MissionStatus.PENDING);
        } else {
            mission.setStatus(MissionStatus.IN_PROGRESS);
        }
    }
}
