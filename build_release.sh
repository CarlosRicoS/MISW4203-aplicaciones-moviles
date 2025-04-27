#!/bin/sh

# === CONFIGURACIÓN (Con valores por defecto y posibilidad de sobrescribir con variables de entorno) ===
KEY_ALIAS="${KEY_ALIAS:-my-key-alias}"
KEYSTORE_FILE="${KEYSTORE_FILE:-my-release-key.keystore}"
KEYSTORE_PASS="${KEYSTORE_PASS:-tu_password}"
KEY_PASS="${KEY_PASS:-tu_password}"
KEYSTORE_PROPERTIES_FILE="keystore.properties"
APK_OUTPUT_NAME="${APK_OUTPUT_NAME:-app-release.apk}"

# === VERIFICAR SI SE PASÓ LA RUTA AL KEYSTORE A TRAVÉS DE UNA VARIABLE DE ENTORNO ===
if [ ! -f "./app/$KEYSTORE_FILE" ]; then
  echo "Generando keystore..."
  keytool -genkeypair \
    -alias "$KEY_ALIAS" \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -keystore "./app/$KEYSTORE_FILE" \
    -storepass "$KEYSTORE_PASS" \
    -keypass "$KEY_PASS" \
    -dname "CN=TuNombre, OU=Unidad, O=Organizacion, L=Ciudad, S=Estado, C=CO"
else
  echo "Keystore ya existe. Omitiendo generación."
fi

# === GENERAR keystore.properties ===
echo "Creando archivo $KEYSTORE_PROPERTIES_FILE..."
cat > "$KEYSTORE_PROPERTIES_FILE" <<EOF
storeFile=$KEYSTORE_FILE
storePassword=$KEYSTORE_PASS
keyAlias=$KEY_ALIAS
keyPassword=$KEY_PASS
EOF

# === Compilar Wrapper ===
gradle wrapper

# === GENERAR APK DE RELEASE ===
echo "Iniciando build..."
export API_HOST="${API_HOST:-https://backvynils-q6yc.onrender.com/}"
./gradlew assembleRelease --refresh-dependencies

# === COPIAR APK A RAÍZ DEL PROYECTO ===
APK_PATH="app/build/outputs/apk/release/app-release.apk"
if [ -f "$APK_PATH" ]; then
  cp "$APK_PATH" "./$APK_OUTPUT_NAME"
  echo "✅ APK copiado a raíz como $APK_OUTPUT_NAME"
else
  echo "❌ No se encontró el APK generado en $APK_PATH"
  exit 1
fi
./gradlew clean
rm -rf "$KEYSTORE_PROPERTIES_FILE" ./app/$KEYSTORE_FILE
echo "✅ Build finalizado exitosamente."

