DROP TABLE IF EXISTS quest;
CREATE TABLE quest (
    id UUID PRIMARY KEY,
    quest_name VARCHAR(64),
    quest_description VARCHAR(8096),
    occurrence_type VARCHAR(64),
    due_date TIMESTAMP,
    experience_points INT
);

INSERT INTO quest (id, quest_name, quest_description, occurrence_type, due_date, experience_points) VALUES (random_uuid(), 'Quest 1', 'Description 1', 'RECURRING', parsedatetime('20240801000000','yyyyMMddHHmmss'), 10);
INSERT INTO quest (id, quest_name, quest_description, occurrence_type, due_date, experience_points) VALUES (random_uuid(), 'Quest 2', 'Description 2', 'DAILY', parsedatetime('20241001000000','yyyyMMddHHmmss'), 25);
INSERT INTO quest (id, quest_name, quest_description, occurrence_type, due_date, experience_points) VALUES (random_uuid(), 'Quest 3', 'Description 3', 'MONTHLY', parsedatetime('20240901000000','yyyyMMddHHmmss'), 25);
INSERT INTO quest (id, quest_name, quest_description, occurrence_type, due_date, experience_points) VALUES (random_uuid(), 'Quest 4', 'Description 4', 'YEARLY', parsedatetime('20240111000000','yyyyMMddHHmmss'), 25);