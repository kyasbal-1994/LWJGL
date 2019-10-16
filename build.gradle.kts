import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("jvm") version "1.3.41"
    id("application")
}

group = "testlwjgl"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}


dependencies {
    var lwjglNatives = when ( OperatingSystem.current() ) {
        OperatingSystem.WINDOWS-> "natives-windows"
        OperatingSystem.LINUX-> "natives-linux"
        OperatingSystem.MAC_OS-> "natives-macos"
        else->error("Invalid OS")
    }

    val lwjglVersion = "3.1.6"
    implementation(kotlin("stdlib-jdk8"))
    compile("org.lwjgl:lwjgl:$lwjglVersion")
    compile("org.lwjgl:lwjgl-opengl:$lwjglVersion")
    compile("org.lwjgl:lwjgl-opengles:$lwjglVersion")
    compile("org.lwjgl:lwjgl-egl:$lwjglVersion")
    compile("org.lwjgl:lwjgl-glfw:$lwjglVersion")
    compile("org.lwjgl:lwjgl:$lwjglVersion:$lwjglNatives")
    compile("org.lwjgl:lwjgl-opengl:$lwjglVersion:$lwjglNatives")
    compile("org.lwjgl:lwjgl-opengles:$lwjglVersion:$lwjglNatives")
    compile("org.lwjgl:lwjgl-glfw:$lwjglVersion:$lwjglNatives")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}