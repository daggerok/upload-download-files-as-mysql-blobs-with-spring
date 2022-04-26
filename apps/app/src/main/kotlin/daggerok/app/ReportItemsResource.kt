package daggerok.app

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
data class ReportItemsResource(private val reportItems: ReportItems) {

    @GetMapping
    fun getAll(): MutableList<ReportItem> =
        reportItems.findAll()

    @PostMapping
    fun post(@RequestBody reportItem: ReportItem) =
        reportItems.save(reportItem)
}
