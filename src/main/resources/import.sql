insert into usuarios (usuario, clave_personal) values ('paulo', 'estaesmiclave');

insert into mensajes (coordenadas, create_at, mensaje) values ('123132', '2019-01-13', 'este es un mensaje1');
insert into mensajes (coordenadas, create_at, mensaje) values ('234234234', '2019-01-14', 'este es un mensaje2');

insert into usuarios_mensaje (usuario_id, mensaje_id) values (1, 1);
insert into usuarios_mensaje (usuario_id, mensaje_id) values (1, 2);