package daggerok.app

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant

data class ReportItemDocument(
    val id: Long? = null,
    @JsonIgnore val jobId: Long = -1,
    val name: String = "",
    val sizeInBytes: Int = -1,
    val lastModifiedAt: Instant? = null,
)

fun ReportItem.toDocument(): ReportItemDocument =
    ReportItemDocument(id, jobId, name, sizeInBytes = content.size, lastModifiedAt)

class MissingFileException : RuntimeException("File not found")
