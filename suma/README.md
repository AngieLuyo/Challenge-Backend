# Challenge-Backend
Requisitos previos:

- Docker instalado en tu sistema
- Conocimientos básicos de Docker y PostgreSQL
- Un proyecto de API existente que desees agregar el servicio de historial de llamadas

Paso 1: Crear un contenedor Docker para PostgreSQL

- Crea un archivo llamado docker-compose.yml en la raíz de tu proyecto con el siguiente contenido

- Reemplaza tu_usuario, tu_contraseña y tu_base_de_datos con tus propios valores.
- Ejecuta el comando docker-compose up -d para crear y iniciar el contenedor Docker para PostgreSQL.
Paso 2: Crear la base de datos y la tabla para el historial de llamadas

- Conecta a la base de datos PostgreSQL utilizando un cliente como pgAdmin o psql.