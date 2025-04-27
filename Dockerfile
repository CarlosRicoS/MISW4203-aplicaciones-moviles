# Base Ubuntu
FROM docker.io/library/ubuntu:20.04

# Install dependencies
RUN dpkg --add-architecture i386 && apt-get update -y && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y \
    openjdk-17-jdk \
    wget \
    curl \
    unzip \
    git \
    python3 \
    python3-pip \
    lib32stdc++6 \
    lib32z1 && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install Android SDK
RUN mkdir -p /opt/android-sdk/cmdline-tools && \
    cd /opt/android-sdk/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-8512546_latest.zip && \
    unzip commandlinetools-linux-8512546_latest.zip && \
    mv cmdline-tools latest

# Configure environment variables
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH
ENV KEYSTORE_FILE=''
ENV KEYSTORE_PASS=''
ENV KEY_ALIAS=''
ENV KEY_PASS=''
ENV APK_OUTPUT_NAME=''
ENV API_HOST='' 

# Pre-create required folder for sdkmanager
RUN mkdir -p /root/.android && touch /root/.android/repositories.cfg

# Install SDK packages
RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "platforms;android-30" "build-tools;30.0.3" "cmdline-tools;latest"

# Install Gradle
RUN wget https://services.gradle.org/distributions/gradle-8.11.1-bin.zip -P /tmp && \
    unzip -d /opt/gradle /tmp/gradle-8.11.1-bin.zip && \
    rm /tmp/gradle-8.11.1-bin.zip && \
    ln -s /opt/gradle/gradle-8.11.1/bin/gradle /usr/local/bin/gradle

# Default command
WORKDIR /app
CMD ["sh", "build_release.sh"]
