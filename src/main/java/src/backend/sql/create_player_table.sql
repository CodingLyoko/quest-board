DROP TABLE IF EXISTS player;
CREATE TABLE player (
    id UUID PRIMARY KEY,
    experience_points INT,
    exp_to_next_level INT
);