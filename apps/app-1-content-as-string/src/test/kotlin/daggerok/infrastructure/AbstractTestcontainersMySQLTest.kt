package daggerok.infrastructure

import daggerok.ContentAsStringApp
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(classes = [ContentAsStringApp::class])
abstract class AbstractTestcontainersMySQLTest {

    companion object {
        data class TestMySQLContainer(val image: String = "mysql:8.0.24") : MySQLContainer<TestMySQLContainer>(image)

        @Container
        val mysqlContainer: TestMySQLContainer = TestMySQLContainer().withDatabaseName("myapp")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl)
            registry.add("spring.datasource.password", mysqlContainer::getPassword)
            registry.add("spring.datasource.username", mysqlContainer::getUsername)
        }
    }
}
