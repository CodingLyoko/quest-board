package src.backend.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import src.shared.OccurrenceType;

@SuppressWarnings("java:S1068") // Incurred by Lombok

@Setter
@Getter
@NoArgsConstructor
public class Quest extends ModelTemplate {

    public Quest(ResultSet resultSet) throws SQLException {
        super(resultSet);
    }

    private UUID id;
    private String questName;
    private String questDescription;
    private OccurrenceType occurrenceType;
    private Timestamp dueDate;
    private int experiencePoints;
    private Boolean todo;
    private Boolean strictDueDate;

    @Override
    public String toString() {
        return questName;
    }
}
