package daggerok.app

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

/**
 * This component customizes error response.
 *
 * Adds `api` map with supported endpoints
 */
@Component
class RestApiErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(request: ServerRequest?, options: ErrorAttributeOptions): MutableMap<String, Any> =
        super.getErrorAttributes(request, options.including(MESSAGE)).apply {
            val baseUrl = request?.uri()?.let { "${it.scheme}://${it.authority}" } ?: ""
            val api = mapOf(
                "Upload file => POST" to "$baseUrl/upload",
                "List saved upload entities => GET" to baseUrl,
                "Download file => GET" to "$baseUrl/download/{id}",
            )
            put("api", api)
        }
}
