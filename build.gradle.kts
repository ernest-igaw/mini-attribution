import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
	java
	kotlin("jvm") version "2.0.0" apply false
	id("io.spring.dependency-management") version "1.1.3" apply false
	id("org.springframework.boot") version "3.4.1"
	kotlin("plugin.spring") version "2.0.0" apply false
	kotlin("plugin.allopen") version "2.0.0" apply false
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

allprojects {
	group = "com.dfinery"
	version = "0.0.1-SNAPSHOT"
	val springCloudAwsVersion: String by extra("3.3.0")

	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	java {
		sourceCompatibility = JavaVersion.VERSION_17
	}

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-validation")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
		implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:$springCloudAwsVersion"))
		implementation("io.awspring.cloud:spring-cloud-aws-starter-dynamodb")
		implementation("io.awspring.cloud:spring-cloud-aws-starter-secrets-manager")
		implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-reflect")

		implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")

		runtimeOnly("com.h2database:h2")
		runtimeOnly("com.mysql:mysql-connector-j")

		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}

	tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
		compilerOptions {
			apiVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}