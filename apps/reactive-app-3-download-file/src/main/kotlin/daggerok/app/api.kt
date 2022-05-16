package daggerok.app

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant

data class ReportItemDocument(
    val id: Long? = null,
    @JsonIgnore val jobId: Long = -1,
    val name: String = "",
    val content: String = "",
    val lastModifiedAt: Instant? = null,
)

fun ReportItem.toDocument(): ReportItemDocument =
    ReportItemDocument(id, jobId, name, content = String(bytes = content, Charsets.UTF_8), lastModifiedAt)

data class UploadFileDocument(
    @JsonIgnore val id: Long? = -1,
    @JsonIgnore val filename: String = "",
    val message: String = "File(id=$id, filename=$filename) was saved",
)

class MissingFileException : RuntimeException("File not found")
