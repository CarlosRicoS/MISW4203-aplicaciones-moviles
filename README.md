# MISW4203-aplicaciones-moviles

## Construcción de la APK

### Descripción de variables de entorno

Tenga en cuenta las siguientes variables de entorno para ajustar el comando que realizará el build de la APK.

| Nombre Variable | Función |
| --- | --- |
| KEYSTORE_FILE | Nombre del archivo keystore |
| KEYSTORE_PASS | Contraseña del KEYSTORE_FILE |
| KEY_ALIAS | Alias de la llave dentro del keystore que firmará la aplicación |
| KEY_PASS | Contraseña específica de la  llave seleccionada con *KEY_ALIAS* |
| APK_OUTPUT_NAME | Nombre resultante del archivo .APK |
| API_HOST | Dirección URL dónde se conectará con el proyecto BackVynils (Ej: <http://localhost:8080/>)|

### Ejecutar script de release

Antes de iniciar con el proceso de construcción del release, se debe validar:

1. Validar que Docker se encuentre instalado y en ejecución.
2. Ubicarse en la raíz del proyecto y ejecutar uno de los siguientes comandos dependiendo del sistema operativo.

Linux

``` Shell
docker run --name android-build --rm -v $(pwd):/app -e VARIABLE_NAME=value ghcr.io/carlosricos/misw4203-aplicaciones-moviles/android-build:latest
```

Windows Powershell

``` Powershell
docker run --name android-build --rm -v "$($pwd.Path):/app"  -e VARIABLE_NAME=value ghcr.io/carlosricos/misw4203-aplicaciones-moviles/android-build:latest
```

Si se desea editar los parámetros de configuración, se deben agregar como variables de entorno a la ejecución del contenedor por medio del flag *-e*, por ejemplo:

``` Shell
    -e VARIABLE_NAME=value
```

Cuando se termine de ejecutar el proceso, se generará en la raíz del proyecto, el archivo *.APK* para instalar en el dispositivo.

## Instalación de la APK

Una vez conectado el dispositivo Android configurado para la instalación y depuración de la APK, se procede a ejecutar el siguiente comando para verificar que el dispositivo sea visible.

``` Shell
adb devices
```

Si se logra evidencia el dispositivo, se procede a realizar la instalación desde la raíz del proyecto con el siguiente comando:

``` Shell
adb install -r -d *.apk 
```
