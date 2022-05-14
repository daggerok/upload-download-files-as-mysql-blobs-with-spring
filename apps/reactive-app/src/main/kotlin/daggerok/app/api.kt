package daggerok.app

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant

data class ReportItemDTO(
    val id: Long? = null,
    val jobId: Long = -1,
    val name: String = "",
    val content: String = "",
    val lastModifiedAt: Instant? = null,
)

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
    @JsonIgnore val filename: String = "",
    val message: String = "file $filename was saved",
)

class MissingFileException : RuntimeException("File not found")
