package src.backend.model;

import java.sql.ResultSet;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("java:S1068") // Incurred by Lombok

@Setter
@Getter
public final class Player extends ModelTemplate {

    public Player() {
        this.experiencePoints = 0;
        this.expToNextLevel = 100;
    }

    public Player(ResultSet resultSet) {
        super(resultSet);
    }

    private UUID id;
    private int experiencePoints;
    private int expToNextLevel;
}
