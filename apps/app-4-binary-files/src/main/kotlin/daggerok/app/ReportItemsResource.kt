package daggerok.app

import javax.servlet.http.HttpServletResponse
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@RestController
data class ReportItemsResource(private val reportItems: ReportItems) {

    @GetMapping("/download")
    fun download(@RequestParam("file", required = false, defaultValue = "") file: String,
                 @RequestParam("id", required = false, defaultValue = "-1") id: Long,
                 response: HttpServletResponse) {
        val reportItem = reportItems.findById(id).orElseThrow { MissingFileException() }
        response.setHeader("Content-Disposition", "attachment; filename=${reportItem.name}")
        FileCopyUtils.copy(reportItem.content, response.outputStream)
    }

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): ReportItemDocument {
        val filename = file.originalFilename ?: throw MissingFilenameException()
        val reportItem = reportItems.save(ReportItem(name = filename, content = file.bytes))
        redirectAttributes.addFlashAttribute("message", "done with $filename")
        return reportItem.toDocument()
    }

    @GetMapping
    fun getAll(): MutableList<ReportItemDocument> =
        reportItems.findAll()
            .map { it.toDocument() }
            .toMutableList()

    @PostMapping
    fun post(@RequestBody reportItem: ReportItemDTO) =
        reportItems.save(reportItem.toEntity())
            .toDocument()
}
