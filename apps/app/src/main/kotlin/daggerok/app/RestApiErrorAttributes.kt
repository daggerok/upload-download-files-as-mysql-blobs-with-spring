package daggerok.app

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

/**
 * This component customizes error response.
 *
 * Adds `api` map with supported endpoints
 */
@Component
class RestApiErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(webRequest: WebRequest, options: ErrorAttributeOptions): MutableMap<String, Any> =
        super.getErrorAttributes(webRequest, options).apply {
            val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
            val api = mapOf(
                "Get all report file items => GET" to baseUrl,
                "Create report file item => POST" to baseUrl,
            )
            put("api", api)
        }
}
