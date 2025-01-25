package test.com.spacex.dragons.repository;

import com.spacex.dragons.repository.SpaceXRepository;
import com.spacex.dragons.models.RocketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SpaceXRepositoryTest {
    private SpaceXRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new SpaceXRepository();
    }

    @Test
    public void testAddMission() {
        repository.addMission("Mars Mission");
        Assertions.assertTrue(repository.getMissionSummary().stream().anyMatch(s -> s.contains("Mars Mission")));
    }

    @Test
    public void testAssignRocketToMission() {
        repository.addRocket("Falcon Heavy");
        repository.addMission("Lunar Mission");
        repository.assignRocketToMission("Falcon Heavy", "Lunar Mission");

        Assertions.assertTrue(repository.getMissionSummary().stream().anyMatch(s -> s.contains("Lunar Mission - IN_PROGRESS - Dragons: 1")));
    }

    @Test
    public void testChangeRocketStatus() {
        repository.addRocket("Dragon XL");
        repository.changeRocketStatus("Dragon XL", RocketStatus.IN_REPAIR);

        Assertions.assertTrue(repository.getMissionSummary().stream().noneMatch(s -> s.contains("In Progress")));
    }

    @Test
    public void testMissionSummaryOrder() {
        repository.addMission("A Mission");
        repository.addMission("B Mission");
        repository.addRocket("Rocket 1");
        repository.assignRocketToMission("Rocket 1", "B Mission");

        List<String> summary = repository.getMissionSummary();
        Assertions.assertEquals("B Mission", summary.get(0).split(" - ")[0]);
    }
}

