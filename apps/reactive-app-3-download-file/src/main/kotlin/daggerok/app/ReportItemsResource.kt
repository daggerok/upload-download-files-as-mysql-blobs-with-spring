package daggerok.app

import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ZeroCopyHttpOutputMessage
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

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

    // @PostMapping("/upload")
    // @Transactional(readOnly = false)
    // fun uploadFile(@RequestPart("file") file: FilePart) =
    //     Mono.from(file.content())
    //         .map {
    //             ByteArray(it.readableByteCount()).apply {
    //                 it.read(this)
    //                 DataBufferUtils.release(it)
    //             }
    //         }
    //         .map { ReportItem(name = file.filename(), content = it) }
    //         .flatMap(reportItems::save)
    //         .map { UploadFileDocument(id = it.id, filename = it.name) }

    @PostMapping("/upload")
    @Transactional(readOnly = false)
    fun uploadFile(@RequestPart("file") fileStream: Flux<FilePart>): Mono<UploadFileDocument> =
        fileStream.map { it.filename() to it.content() }
            .flatMap { (filename, contentStream) ->
                contentStream
                    .map {
                        ByteArray(it.readableByteCount()).apply {
                            it.read(this)
                            DataBufferUtils.release(it)
                        }
                    }
                    .map { ReportItem(name = filename, content = it) }
                    .flatMap(reportItems::save)
            }
            .map { UploadFileDocument(id = it.id, filename = it.name) }
            .toMono()

    @GetMapping("/download/{id}") // may be post as well
    fun downloadFile(@PathVariable("id") id: Long, response: ServerHttpResponse): Mono<Void> =
        reportItems.findById(id)
            .map {
                val dataBuffer: DataBuffer = response.bufferFactory().allocateBuffer(it.content.size)
                it.name to dataBuffer.write(it.content)
            }
            .flatMap { (filename, dataBuffer) ->
                val zeroCopyResponse = response as ZeroCopyHttpOutputMessage
                zeroCopyResponse.headers[HttpHeaders.CONTENT_DISPOSITION] = "attachment; filename=${filename}"
                zeroCopyResponse.headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_OCTET_STREAM_VALUE
                zeroCopyResponse.writeWith(Mono.just(dataBuffer))
            }
}
