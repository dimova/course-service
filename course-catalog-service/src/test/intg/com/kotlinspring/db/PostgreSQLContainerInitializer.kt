package com.kotlinspring.db


import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName


abstract class PostgreSQLContainerInitializer {
    companion object {
        private val IMAGE_NAME = DockerImageName.parse("postgres:15-alpine")
            .asCompatibleSubstituteFor("postgres")

        val postgresContainer = PostgreSQLContainer(IMAGE_NAME).apply {
            withDatabaseName("testdb")
            withUsername("postgres")
            withPassword("secret")
            // This is the key for M1/M2/M3 Mac Minis
            // withCreateContainerCmdModifier { it.withPlatform("linux/amd64") }
            withStartupTimeout(java.time.Duration.ofMinutes(2))
            setWaitStrategy(Wait.forListeningPort())
        }
        init {
            postgresContainer.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureDatasource(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
        }
    }
}
