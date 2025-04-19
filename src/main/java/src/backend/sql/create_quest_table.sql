DROP TABLE IF EXISTS quest;
CREATE TABLE quest (
    id UUID PRIMARY KEY,
    quest_name VARCHAR(64),
    quest_description VARCHAR(8096),
    occurrence_type VARCHAR(64),
    due_date TIMESTAMP,
    experience_points INT,
    todo BOOLEAN
);