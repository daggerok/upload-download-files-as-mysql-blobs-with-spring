package daggerok

import daggerok.app.ReportItems
import org.apache.logging.log4j.kotlin.logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import reactor.test.StepVerifier

@SpringBootTest(
    classes = [ContentAsByteArrayReactiveApp::class],
    webEnvironment = RANDOM_PORT,
)
class ContentAsByteArrayReactiveAppTests @Autowired constructor(val reportItems: ReportItems) {

    @BeforeEach
    fun setUp() {
        reportItems.deleteAll()
            .subscribe { log.info { "Remove: $it" } }
    }

    @Test
    fun `should test context`() {
        // given
        StepVerifier.create(reportItems.deleteAll())
            .verifyComplete()
    }

    companion object { val log = logger() }
}
