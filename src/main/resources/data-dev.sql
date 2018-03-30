
-- seed database
INSERT INTO users (id, age, first_name, last_name) VALUES
  (nextval('hibernate_sequence'), 17, 'Gustavo', 'Paulo'),
  (nextval('hibernate_sequence'), 6, 'Estheban', 'Paulo'),
  (nextval('hibernate_sequence'), 39, 'Jhonny', 'Paulo'),
  (nextval('hibernate_sequence'), 38, 'Rossmy', 'Segura'),
  (nextval('hibernate_sequence'), 15, 'Gabrielys', 'Segura'),
  (nextval('hibernate_sequence'), 18, 'José', 'Meignen');