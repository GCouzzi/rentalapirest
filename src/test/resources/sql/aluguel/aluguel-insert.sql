INSERT INTO automoveis (id, marca, modelo, cor, placa, valor_por_minuto, status) VALUES (11, 'Chevrolet', 'Tracker', 'Cinza', 'ZZZ-0000', 0.50, 'LIVRE');
INSERT INTO automoveis (id, marca, modelo, cor, placa, valor_por_minuto, status) VALUES (21, 'Ford', 'Ka', 'Branco', 'ZZZ-0001', 0.20, 'LIVRE');
INSERT INTO automoveis (id, marca, modelo, cor, placa, valor_por_minuto, status) VALUES (31, 'Honda', 'Accord', 'Preto', 'ZZZ-0002', 1, 'ALUGADO');

INSERT INTO usuario_automovel (id, recibo, data_inicio, id_usuario, id_automovel) VALUES (10, '20240327123951', '2024-03-27 12:39:51.437793', 1, 11);
INSERT INTO usuario_automovel (id, recibo, data_inicio, id_usuario, id_automovel) VALUES (20, '20240326133951', '2024-03-26 13:39:51.437793', 2, 21);
INSERT INTO usuario_automovel (id, recibo, data_inicio, id_usuario, id_automovel) VALUES (30, '20240325143951', '2024-03-25 14:39:51.437793', 3, 31);