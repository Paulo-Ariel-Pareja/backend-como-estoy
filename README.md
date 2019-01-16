# REST backend-como-estoy BY Paulo Ariel Pareja para "PodemosAprender (Grupo)" https://www.facebook.com/groups/571508276552938/.
Esta api esta orientada a generar mensajes para compartir, sin necesidad de registrarse, podran leer tus mensajes con solo tener tu URL.

el usuario se genera con la siguiente informacion:
usuario, clave personal (publica, String entre 10 y 90 caracteres  y clave privada (String entre 20 y 90 caracteres) haciendo POST a "api/usuarios"

para publicar un mensaje  se debera proporcionar:
clave privada, coordenadas (String) y mensaje (String entre 1 y 90 caracteres) mediante PUT a
"api/usuarios/{ USUARIO }/{ CLAVE PUBLICA }"

para leer un mensaje se realizara mediante GET a "api/usuarios/{ USUARIO }/{ CLAVE PUBLICA }"
