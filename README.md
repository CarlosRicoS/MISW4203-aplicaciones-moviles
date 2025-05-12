# MISW4203-aplicaciones-moviles

## Construcción de la APK

**IMPORTANTE: Si ya habías instalado la aplicación anteriormente, por favor desinstálala antes de seguir con estos pasos.**

**IMPORTANTE: Los siguientes pasos son compatibles con arquitecturas x64/i386, arquitecturas ARM no son compatibles.**

### Descripción de variables de entorno

Tenga en cuenta las siguientes variables de entorno para ajustar el comando que realizará el build de la APK.

| Nombre Variable | Función |
| --- | --- |
| KEYSTORE_FILE | Nombre del archivo keystore |
| KEYSTORE_PASS | Contraseña del KEYSTORE_FILE |
| KEY_ALIAS | Alias de la llave dentro del keystore que firmará la aplicación |
| KEY_PASS | Contraseña específica de la  llave seleccionada con *KEY_ALIAS* |
| APK_OUTPUT_NAME | Nombre resultante del archivo .APK, por defecto es `app-release.apk` |
| API_HOST | Dirección URL dónde se conectará con el proyecto BackVynils (Ej: <http://localhost:3000/>). Es muy importante que la variable termine en `/`.|

### Ejecutar script de release

Antes de iniciar con el proceso de construcción del release, se debe validar:

1. Validar que Docker se encuentre instalado y en ejecución.
2. Ubicarse en la raíz del proyecto y ejecutar uno de los siguientes comandos dependiendo del sistema operativo.

Linux

``` Shell
docker run --name android-build --rm -v $(pwd):/app -e API_HOST=http://35.170.246.148:3000/ ghcr.io/carlosricos/misw4203-aplicaciones-moviles/android-build:latest
```

Windows Powershell

``` Powershell
docker run --name android-build --rm -v "$($pwd.Path):/app" -e API_HOST=http://35.170.246.148:3000/ ghcr.io/carlosricos/misw4203-aplicaciones-moviles/android-build:latest
```

Si se desea editar los parámetros de configuración, se deben agregar como variables de entorno a la ejecución del contenedor por medio del flag *-e*, por ejemplo:

``` Shell
    -e VARIABLE_NAME=value
```

**IMPORTANTE: Si la variable `API_HOST` no se indica al momento de construir el APK, por defecto se apuntará a la URL de prueba `https://backvynils-q6yc.onrender.com/`. Buscando no depender de un recurso no controlador por nosotros, decidimos publicar el backend en un host privado con la IP `35.170.246.148`, y por este motivo se utiliza esta IP en los scripts de ejemplo.**

Cuando se termine de ejecutar el proceso, se generará en la raíz del proyecto, el archivo *.APK* para instalar en el dispositivo.

## Instalación de la APK

Una vez conectado el dispositivo Android configurado para la instalación y depuración de la APK (Modo desarrollador) o lanzado el emulador, se procede a ejecutar el siguiente comando para verificar que el dispositivo sea visible.

``` Shell
adb devices
```

Si se logra evidenciar el dispositivo en la lista del paso anterior, se procede a realizar la instalación desde la raíz del proyecto con el siguiente comando utilizando el nombre del archivo apk resultante:

``` Shell
adb install -r -d <<APK_OUTPUT_NAME>>
```
