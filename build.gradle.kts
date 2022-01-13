/*
plugins {
    kotlin("multiplatform") version "1.6.10"
}

group = "me.francesco"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    */
/*val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }*//*

    mingwX64("native").apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
        compilations.getByName("main") {
            val libcurl by cinterops.creating {
                defFile(project.file("src\\nativeInterop\\cinterop\\libcurl.def"))
                packageName("libcurl")
                compilerOpts("-IC:\\Users\\Francesco\\IdeaProjects\\http-client\\src\\nativeInterop\\cinterop\\curl")
                includeDirs("C:\\Users\\Francesco\\IdeaProjects\\http-client\\src\\nativeInterop\\cinterop\\curl")
            }
        }
    }
    sourceSets {
        val nativeMain by getting
        val nativeTest by getting
    }
}
*/

plugins {
    kotlin("multiplatform") version "1.6.10"
}

repositories {
    mavenCentral()
}

val WIN_LIBRARY_PATH =
    "c:\\msys64\\mingw64\\bin;c:\\Tools\\msys64\\mingw64\\bin;C:\\Tools\\msys2\\mingw64\\bin"

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")

    val nativeTarget = when {
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {

        val OS_NAME = System.getProperty("os.name").toLowerCase()

        val HOST_NAME = when {
            OS_NAME.startsWith("linux") -> "linux"
            OS_NAME.startsWith("windows") -> "windows"
            OS_NAME.startsWith("mac") -> "macos"
            else -> error("Unknown os name `$OS_NAME`")
        }

        val paths = if (HOST_NAME == "windows") {
            listOf(
                "C:/msys64/mingw64/include/curl",
                "C:/Tools/msys64/mingw64/include/curl",
                "C:/Tools/msys2/mingw64/include/curl"
            )
        } else {
            error("Unknown host name `$HOST_NAME`")
        }

        binaries {
            executable {
                entryPoint = "main"
            }
        }

        this.compilations.getByName("main") {
            cinterops.create("libcurl") {
                defFile = File(projectDir, "src/nativeInterop/cinterop/libcurl.def")
                includeDirs.headerFilterOnly(paths)

                afterEvaluate {
                    if (this.name == "mingwX64") {
                        val winTests =
                            tasks.getByName("mingwX64Test") as org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
                        winTests.environment("PATH", WIN_LIBRARY_PATH)
                    }
                }
            }
        }

    }
}
