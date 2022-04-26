package daggerok

import com.fasterxml.jackson.databind.ObjectMapper
import daggerok.infrastructure.AbstractTestcontainersMySQLTest
import org.apache.logging.log4j.kotlin.logger
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

class ContentAsStringAppTests @Autowired constructor(val objectMapper: ObjectMapper, val applicationContext: ApplicationContext) :
    AbstractTestcontainersMySQLTest() {

    @Test
    fun `should test context`() {
        log.info { "mysqlContainer: $mysqlContainer" }
        log.info { "mysqlContainer.jdbcUrl: ${mysqlContainer.jdbcUrl}" }
        log.info { "mysqlContainer.username: ${mysqlContainer.username}" }
        log.info { "mysqlContainer.password: ${mysqlContainer.password}" }

        val environment = applicationContext.environment
        log.info { "spring.datasource.url: ${environment.getProperty("spring.datasource.url")}" }
        log.info { "spring.datasource.username: ${environment.getProperty("spring.datasource.username")}" }
        log.info { "spring.datasource.password: ${environment.getProperty("spring.datasource.password")}" }
    }

    @Test
    fun `should test context again`() {
        log.info { "mysqlContainer: $mysqlContainer" }
        log.info { "mysqlContainer.jdbcUrl: ${mysqlContainer.jdbcUrl}" }
        log.info { "mysqlContainer.username: ${mysqlContainer.username}" }
        log.info { "mysqlContainer.password: ${mysqlContainer.password}" }

        val environment = applicationContext.environment
        log.info { "spring.datasource.url: ${environment.getProperty("spring.datasource.url")}" }
        log.info { "spring.datasource.username: ${environment.getProperty("spring.datasource.username")}" }
        log.info { "spring.datasource.password: ${environment.getProperty("spring.datasource.password")}" }
    }

    companion object { val log = logger() }
}
