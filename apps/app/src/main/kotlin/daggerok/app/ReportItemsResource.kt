package daggerok.app

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@RestController
data class ReportItemsResource(private val reportItems: ReportItems) {

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): ReportItemDTO {
        val filename = file.originalFilename ?: throw MissingFilenameException()
        val reportItem = reportItems.save(ReportItem(name = filename, content = file.bytes))
        redirectAttributes.addFlashAttribute("message", "done with $filename")
        return reportItem.toDTO()
    }

    @GetMapping
    fun getAll(): MutableList<ReportItemDTO> =
        reportItems.findAll()
            .map { it.toDTO() }
            .toMutableList()

    @PostMapping
    fun post(@RequestBody reportItem: ReportItemDTO) =
        reportItems.save(reportItem.toEntity())
            .toDTO()
}
