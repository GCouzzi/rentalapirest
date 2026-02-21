INSERT IGNORE INTO usuarios
(username, password, nome_completo, cpf, telefone, email, role)
VALUES
('userAdmin',
 '$2a$12$d9rgTJJuif0MgYkw8XlY3.v75dAP6R7UDQVI4ECtOnGo5iZRMEQ8W',
 'User Admin',
 '12345678909',
 '27999999999',
 'admin@admin.com',
 'ROLE_ADMIN');