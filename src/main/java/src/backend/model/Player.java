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
        this.maxHealthPoints = 100;
        this.currentHealthPoints = 100;
        this.maxActionPoints = 10;
        this.currentActionPoints = 10;
        this.experiencePoints = 0;
    }

    public Player(ResultSet resultSet) {
        super(resultSet);
    }

    private UUID id;
    private int maxHealthPoints;
    private int currentHealthPoints;
    private int maxActionPoints;
    private int currentActionPoints;
    private int experiencePoints;

    @Override
    public String toString() {
        return "HP: " + this.currentHealthPoints + "\nAP: " + this.currentActionPoints + "\nEXP: " + this.experiencePoints;
    }
}
