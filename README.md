# MISW4203-aplicaciones-moviles

## Construcción de la APK

### Ajustar variables de entorno

El primer paso consiste en ajustar las variables de entorno.

| Nombre Variable | Función |
| --- | --- |
| KEYSTORE_FILE | Nombre del archivo keystore |
| KEYSTORE_PASS | Contraseña del KEYSTORE_FILE |
| KEY_ALIAS | Alias de la llave dentro del keystore que firmará la aplicación |
| KEY_PASS | Contraseña específica de la  llave seleccionada con *KEY_ALIAS* |
| APK_OUTPUT_NAME | Nombre resultante del archivo .APK |
| API_HOST | Dirección URL dónde se conectará con el proyecto BackVynils (Ej: <http://localhost:8080/>)|

Si se desea sobreescrbir alguno de estos valores se debe realizar por medio del siguiente comando.

``` Shell
export VARIABLE=VALOR
```

### Ejecutar script de release

Antes de iniciar con el proceso de construcción del release, se debe validar:

1. Validar que el Android SDK se encuentre instalado.
2. Validar que el archivo **build_release.sh** tenga permisos de ejecución.

Posteriormente, desde la raíz del proyecto se debe ejecutar el siguiente comando.

``` Shell
./build_release.sh
```

Cuando se termine de ejecutar el proceso, se generará en la raíz del proyecto, el archivo *.APK* para instalar en el dispositivo.

## Instalación de la APK

Una vez conectado el dispositivo Android configurado para la instalación y depuración de la APK, se procede a ejecutar el siguiente ocmando para verificar que el dispositivo sea visible.

``` Shell
adb devices
```

Si se logra evidencia el dispositivo, se procede a realizar la instalación desde la raíz del proyecto con el siguiente comando:

``` Shell
adb install -r -d *.apk 
```
