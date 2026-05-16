CREATE TABLE settings (
    setting_name VARCHAR NOT NULL,
    setting_value VARCHAR,
    CONSTRAINT settings_fk PRIMARY KEY (setting_name)
);
