insert into usuarios (usuario, clave_personal, clave_privada) values ('paulo', 'estaEsMiClavePublica', '$2a$10$AGCf3k8eqOZJhrQ5Q6X6RO51qtUcgnMtoogjJlw5G.rkxkeuTRtbS');

insert into mensajes (coordenadas, create_at, mensaje) values ('123132', '2019-01-13', 'este es un mensaje1');
insert into mensajes (coordenadas, create_at, mensaje) values ('234234234', '2019-01-14', 'este es un mensaje2');

insert into usuarios_mensaje (usuario_id, mensaje_id) values (1, 1);
insert into usuarios_mensaje (usuario_id, mensaje_id) values (1, 2);