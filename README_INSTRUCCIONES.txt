# Seguridad Cloud - Proyecto APSTI

## Requisitos
- JDK 21 instalado.
- Visual Studio Code con Extension Pack for Java (recomendado).
- Conexión a Internet la primera vez para que Maven descargue las dependencias.

## Ejecutar en VS Code (Windows)
1. Extrae `proyect_APSTI.zip`.
2. Abre en VS Code la carpeta `proyect_APSTI` (la que contiene `pom.xml` y `mvnw.cmd`).
3. Abre Terminal > New Terminal.
4. Ejecuta:

   ```powershell
   $env:APP_LOGIN_USERNAME = "admin@seguridadcloud.com"
   $env:APP_LOGIN_PASSWORD = Read-Host "Escribe una contraseña segura para el administrador"
   .\mvnw.cmd spring-boot:run
   ```

5. Espera el mensaje `Started ProyectApstiApplication`.
6. Abre `http://localhost:8080`.

Las variables se guardan solo en esa terminal. Si abres una nueva, vuelve a definirlas. Usa el correo configurado en `APP_LOGIN_USERNAME` y la contraseña que escribiste en `APP_LOGIN_PASSWORD` para iniciar sesión como administrador.

## Datos de demostración
La primera vez que se abre Administración de usuarios, se crean tres usuarios de ejemplo. Sus contraseñas de demostración se guardan cifradas con BCrypt (`Demo123!`).

Los usuarios registrados con estado `Activo` también pueden iniciar sesión con su correo y contraseña. Los usuarios `Inactivo` no pueden acceder. El nombre del perfil solo identifica al usuario; para iniciar sesión se usan el correo y la contraseña.

## Funciones
- Inicio de sesión protegido con Spring Security.
- Listado de usuarios.
- Registro y edición.
- Eliminación con confirmación.
- Base de datos H2 local persistente en la carpeta `datos`.
- Diseño adaptable a celular y computadora.

## Detener
En la terminal presiona `Ctrl + C`.

## Importante
No borres la carpeta `datos` si quieres conservar los usuarios registrados. La carpeta se excluye de Git para no publicar los datos locales de usuarios.
