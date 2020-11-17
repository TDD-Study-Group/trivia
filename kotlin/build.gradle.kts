import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

plugins {
    jacoco
    kotlin("jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("com.dorongold.task-tree")
}

repositories {
    jcenter()
    mavenCentral()
}

val junitVersion: String by project
val detektVersion: String by project
val mockkVersion: String by project
val hamkrestVersion: String by project

dependencies {
    testImplementation(kotlin("test-junit5"))

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))

    testImplementation("org.junit.jupiter:junit-jupiter")
    //testImplementation("org.junit.vintage:junit-vintage-engine")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.natpryce:hamkrest:$hamkrestVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-console")

    testImplementation("org.junit.jupiter:junit-jupiter-api") {
        because("we want to use JUnit 5")
    }
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine") {
        because("Cucumber relies on jupiter-engine to resolve tests")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.test {
    useJUnitPlatform {}

    testLogging {
        events(PASSED, SKIPPED, FAILED)
    }
}

detekt {
    toolVersion =
        "1.8.0"                                 // Version of the Detekt CLI that will be used. When unspecified the latest detekt version found will be used. Override to stay on the same version.
    input = files(
        "src/main/java",
        "src/main/kotlin"
    )     // The directories where detekt looks for source files. Defaults to `files("src/main/java", "src/main/kotlin")`.
    parallel =
        false                                      // Builds the AST in parallel. Rules are always executed in parallel. Can lead to speedups in larger projects. `false` by default.
    //config = files("path/to/config.yml")                  // Define the detekt configuration(s) you want to use. Defaults to the default detekt configuration.
    buildUponDefaultConfig =
        false                        // Interpret config files as updates to the default config. `false` by default.
    //baseline = file("path/to/baseline.xml")               // Specifying a baseline file. All findings stored in this file in subsequent runs of detekt.
    disableDefaultRuleSets =
        false                        // Disables all default detekt rulesets and will only run detekt with custom rules defined in plugins passed in with `detektPlugins` configuration. `false` by default.
    debug =
        false                                         // Adds debug output during task execution. `false` by default.
    ignoreFailures =
        true                                // If set to `true` the build does not fail when the maxIssues count was reached. Defaults to `false`.
    reports {
        xml {
            enabled = true                                // Enable/Disable XML report (default: true)
            destination =
                file("build/reports/detekt.xml")  // Path where XML report will be stored (default: `build/reports/detekt/detekt.xml`)
        }
        html {
            enabled = true                                // Enable/Disable HTML report (default: true)
            destination =
                file("build/reports/detekt.html") // Path where HTML report will be stored (default: `build/reports/detekt/detekt.html`)
        }
        txt {
            enabled = true                                // Enable/Disable TXT report (default: true)
            destination =
                file("build/reports/detekt.txt") // Path where TXT report will be stored (default: `build/reports/detekt/detekt.txt`)
        }
        custom {
            reportId = "CustomJsonReport"                   // The simple class name of your custom report.
            destination = file("build/reports/detekt.json") // Path where report will be stored
        }
    }
}

tasks {
    withType<Detekt> {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        this.jvmTarget = "1.8"
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Release candidate")
                }
            }
        }
    }

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}