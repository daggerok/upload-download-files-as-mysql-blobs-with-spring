package daggerok

import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Locale
import java.util.TimeZone
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ContentAsByteArrayApp

fun main(args: Array<String>) {
    runApplication<ContentAsByteArrayApp>(*args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.from(ZoneOffset.UTC)))
        Locale.setDefault(Locale.US)
    }
}
