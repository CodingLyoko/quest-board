DROP TABLE IF EXISTS player;
CREATE TABLE player (
    id UUID PRIMARY KEY,
    max_health_points INT,
    current_health_points INT,
    max_action_points INT,
    current_action_points INT,
    experience_points INT
);