-- Inserções para a tabela 'role'
INSERT INTO role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO role (authority) VALUES ('ROLE_PROFISSIONAL');

-- Inserções para a tabela 'espaco'
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Mesa Individual Alpha', 'Mesa espaçosa com cadeira ergonômica e acesso a ponto de energia.', 'MESA_INDIVIDUAL');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Mesa Individual Beta', 'Mesa compacta, ideal para foco total e com iluminação individual.', 'MESA_INDIVIDUAL');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Mesa Individual Gamma', 'Mesa com vista para a janela, ambiente inspirador.', 'MESA_INDIVIDUAL');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Mesa Individual Delta', 'Mesa em área silenciosa, perfeita para concentração.', 'MESA_INDIVIDUAL');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Sala de Reunião Atlântida', 'Sala para até 8 pessoas, equipada com projetor e quadro branco.', 'SALA_REUNIAO');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Sala de Reunião Pacífico', 'Sala ampla para 12 pessoas, com sistema de videoconferência.', 'SALA_REUNIAO');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Sala de Reunião Índico', 'Sala privativa para 4 pessoas, ideal para pequenas equipes.', 'SALA_REUNIAO');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Sala de Reunião Ártico', 'Sala de reuniões informal com pufes e ambiente descontraído.', 'SALA_REUNIAO');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('ESCRITORIO Privativo 1', 'ESCRITORIO mobiliado para equipes de até 5 pessoas.', 'ESCRITORIO');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('ESCRITORIO Privativo 2', 'ESCRITORIO com isolamento acústico para máxima privacidade.', 'ESCRITORIO');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('ESCRITORIO Privativo 3', 'ESCRITORIO com varanda e vista para a cidade.', 'ESCRITORIO');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('ESCRITORIO Privativo 4', 'ESCRITORIO amplo com área de descanso integrada.', 'ESCRITORIO');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Cabine de Chamadas A', 'Cabine acústica para chamadas telefônicas e videoconferências.', 'CABINE_PARA_CHAMADAS');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Cabine de Chamadas B', 'Cabine com ventilação e iluminação individual.', 'CABINE_PARA_CHAMADAS');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Cabine de Chamadas C', 'Cabine confortável para uma pessoa.', 'CABINE_PARA_CHAMADAS');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Cabine de Chamadas D', 'Cabine equipada com carregador USB.', 'CABINE_PARA_CHAMADAS');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Laboratório de TI 1', 'Sala equipada com 10 computadores de alto desempenho.', 'SALA_COM_COMPUTADORES');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Laboratório de TI 2', 'SALA_COM_COMPUTADORES e software de edição de vídeo.', 'SALA_COM_COMPUTADORES');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Laboratório de TI 3', 'Sala com ambiente climatizado e suporte técnico.', 'SALA_COM_COMPUTADORES');
INSERT INTO espaco (nome, descricao, tipo) VALUES ('Estação de Design', 'SALA_COM_COMPUTADORES equipados com suíte Adobe e tablets.', 'SALA_COM_COMPUTADORES');

-- Inserções para a tabela 'servico'
INSERT INTO servico (nome, descricao) VALUES ('Café Premium', 'Acesso ilimitado a café gourmet moído na hora.');
INSERT INTO servico (nome, descricao) VALUES ('Impressão e Cópia', 'Pacote de 100 impressões P&B e coloridas.');
INSERT INTO servico (nome, descricao) VALUES ('Armário Individual', 'Locker privativo para guardar pertences com segurança.');
INSERT INTO servico (nome, descricao) VALUES ('Endereço Fiscal', 'Utilização do nosso endereço para fins comerciais e correspondência.');
INSERT INTO servico (nome, descricao) VALUES ('Consultoria de TI', 'Suporte técnico especializado para seus equipamentos.');
INSERT INTO servico (nome, descricao) VALUES ('Acesso 24/7', 'Acesso ao coworking a qualquer hora, todos os dias da semana.');
INSERT INTO servico (nome, descricao) VALUES ('Mentoria de Negócios', 'Sessões de mentoria com especialistas de mercado.');
INSERT INTO servico (nome, descricao) VALUES ('Internet de Alta Velocidade', 'Link de fibra óptica dedicado com alta performance.');
INSERT INTO servico (nome, descricao) VALUES ('Estacionamento', 'Vaga de estacionamento coberta e com segurança.');
INSERT INTO servico (nome, descricao) VALUES ('Serviço de Recepção', 'Atendimento telefônico personalizado e recepção de clientes.');
INSERT INTO servico (nome, descricao) VALUES ('Limpeza Diária', 'Serviço de limpeza e higienização do seu espaço.');
INSERT INTO servico (nome, descricao) VALUES ('Aulas de Yoga', 'Aulas de yoga e meditação para relaxamento.');
INSERT INTO servico (nome, descricao) VALUES ('Happy Hour Semanal', 'Evento de networking com bebidas e petiscos.');
INSERT INTO servico (nome, descricao) VALUES ('Cozinha Compartilhada', 'Acesso a cozinha equipada com micro-ondas, geladeira e utensílios.');
INSERT INTO servico (nome, descricao) VALUES ('Descontos em Parceiros', 'Clube de vantagens com descontos em restaurantes e academias.');
INSERT INTO servico (nome, descricao) VALUES ('Correio e Encomendas', 'Gerenciamento de recebimento e envio de correspondências.');
INSERT INTO servico (nome, descricao) VALUES ('Aluguel de Equipamentos', 'Locação de projetores, microfones e outros equipamentos audiovisuais.');
INSERT INTO servico (nome,descricao) VALUES ('Apoio Administrativo', 'Serviços de secretariado e apoio administrativo sob demanda.');
INSERT INTO servico (nome, descricao) VALUES ('Acesso à Rede de Contatos', 'Participação em eventos exclusivos para membros.');
INSERT INTO servico (nome, descricao) VALUES ('Massagem Relaxante', 'Sessões de massoterapia para alívio do estresse.');

-- Inserções para a tabela 'usuario'
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Ana Silva', 'Desenvolvedora Full Stack', 'ana.silva@email.com', '(11) 98765-4321', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Bruno Costa', 'Designer UX/UI', 'bruno.costa@email.com', '(21) 91234-5678', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Carla Martins', 'Gerente de Projetos', 'carla.martins@email.com', '(31) 99876-5432', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Daniel Almeida', 'Analista de Sistemas', 'daniel.almeida@email.com', '(41) 98765-1234', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Eduarda Ferreira', 'Especialista em Marketing Digital', 'eduarda.ferreira@email.com', '(51) 91234-8765', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Fábio Souza', 'Engenheiro de Software', 'fabio.souza@email.com', '(61) 99876-4321', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Gabriela Lima', 'Analista de Dados', 'gabriela.lima@email.com', '(71) 98765-5678', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Heitor Oliveira', 'Arquiteto de Soluções', 'heitor.oliveira@email.com', '(81) 91234-4321', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Isabela Pereira', 'Consultora de Negócios', 'isabela.pereira@email.com', '(91) 99876-8765', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('João Rodrigues', 'Desenvolvedor Back-End', 'joao.rodrigues@email.com', '(11) 98765-8765', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Larissa Barbosa', 'Desenvolvedora Front-End', 'larissa.barbosa@email.com', '(21) 91234-1234', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Marcos Santos', 'Analista de Qualidade', 'marcos.santos@email.com', '(31) 99876-1234', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Natália Correia', 'Product Owner', 'natalia.correia@email.com', '(41) 98765-5432', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Otávio Mendes', 'Scrum Master', 'otavio.mendes@email.com', '(51) 91234-5432', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Patrícia Gomes', 'Advogada', 'patricia.gomes@email.com', '(61) 99876-5432', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Rafael Azevedo', 'Contador', 'rafael.azevedo@email.com', '(71) 98765-4321', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Sofia Ribeiro', 'Jornalista', 'sofia.ribeiro@email.com', '(81) 91234-8765', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Thiago Ramos', 'Fotógrafo', 'thiago.ramos@email.com', '(91) 99876-4321', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Vanessa Nogueira', 'Tradutora', 'vanessa.nogueira@email.com', '(11) 98765-1234', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('William Dias', 'Consultor Financeiro', 'william.dias@email.com', '(21) 91234-5678', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');
INSERT INTO usuario (nome, cargo, email, telefone, senha) VALUES ('Admin', 'Admin', 'admin@admin.com', '(99) 99999-9999', '$2a$10$ZhadxzdYzVeN23JXcwr4yuyMa3d1UgFd3eDrjRZj4Aq8M3u1fAsKK');

-- Inserções para a tabela de associação 'usuario_role'
INSERT INTO usuario_role (usuario_id, role_id) VALUES (1, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (2, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (3, 1); -- Admin
INSERT INTO usuario_role (usuario_id, role_id) VALUES (4, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (5, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (6, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (7, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (8, 1); -- Admin
INSERT INTO usuario_role (usuario_id, role_id) VALUES (9, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (10, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (11, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (12, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (13, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (14, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (15, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (16, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (17, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (18, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (19, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (20, 2);
INSERT INTO usuario_role (usuario_id, role_id) VALUES (21, 1);

-- Inserções para a tabela 'reserva'
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (1, 1, 1, '2024-07-29T09:00:00Z', '2024-07-29T18:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (2, 5, 2, '2024-07-30T10:00:00Z', '2024-07-30T12:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (3, 9, 3, '2024-08-01T08:30:00Z', '2024-08-01T17:30:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (4, 13, 4, '2024-08-02T14:00:00Z', '2024-08-02T15:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (5, 17, 5, '2024-08-05T09:00:00Z', '2024-08-05T13:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (6, 2, 6, '2024-08-06T11:00:00Z', '2024-08-06T18:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (7, 6, 7, '2024-08-07T15:00:00Z', '2024-08-07T16:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (8, 10, 8, '2024-08-08T09:00:00Z', '2024-08-12T18:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (9, 14, 9, '2024-08-13T13:00:00Z', '2024-08-13T13:30:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (10, 18, 10, '2024-08-14T10:00:00Z', '2024-08-14T12:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (11, 3, 11, '2024-08-15T09:00:00Z', '2024-08-15T18:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (12, 7, 12, '2024-08-16T14:00:00Z', '2024-08-16T16:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (13, 11, 13, '2024-08-19T09:00:00Z', '2024-08-23T18:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (14, 15, 14, '2024-08-20T16:00:00Z', '2024-08-20T16:30:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (15, 19, 15, '2024-08-21T09:00:00Z', '2024-08-21T13:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (16, 4, 16, '2024-08-22T08:00:00Z', '2024-08-22T12:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (17, 8, 17, '2024-08-26T10:00:00Z', '2024-08-26T11:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (18, 12, 18, '2024-08-27T09:00:00Z', '2024-08-27T18:00:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (19, 16, 19, '2024-08-28T14:00:00Z', '2024-08-28T14:30:00Z');
INSERT INTO reserva (usuario_id, espaco_id, servico_id, entrada, saida) VALUES (20, 20, 20, '2024-08-29T13:00:00Z', '2024-08-29T17:00:00Z');