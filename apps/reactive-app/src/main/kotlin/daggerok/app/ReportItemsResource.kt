package daggerok.app

import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.data.domain.Sort
import org.springframework.http.codec.multipart.FilePart
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@Transactional(readOnly = true)
data class ReportItemsResource(private val reportItems: ReportItems) {

    @GetMapping("/")
    fun getUploads(@RequestParam("id", required = false, defaultValue = "") ids: List<Long>,
                   @RequestParam("filename", required = false, defaultValue = "") filenames: List<String>): Publisher<ReportItemDocument> =
        when {
            ids.isNotEmpty() ->
                Flux.fromIterable(ids)
                    .flatMap { reportItems.findById(it) }
                    .map { it.toDocument() }
                    .switchIfEmpty(Mono.error(MissingFileException()))
            filenames.isNotEmpty() ->
                Flux.fromIterable(filenames)
                    .filter(String::isNotBlank)
                    .flatMap { reportItems.findAllByNameContaining(it) }
                    .map(ReportItem::toDocument)
            else ->
                reportItems.findAll(Sort.by("lastModifiedAt", "id").descending())
                    .map(ReportItem::toDocument)
        }

    @PostMapping("/upload")
    @Transactional(readOnly = false)
    fun uploadFile(@RequestPart("file") file: FilePart) =
        Mono.from(file.content())
            .map {
                ByteArray(it.readableByteCount()).apply {
                    it.read(this)
                    DataBufferUtils.release(it)
                }
            }
            .map { ReportItem(name = file.filename(), content = it) }
            .flatMap(reportItems::save)
            .map { UploadFileDocument(filename = it.name) }
}
