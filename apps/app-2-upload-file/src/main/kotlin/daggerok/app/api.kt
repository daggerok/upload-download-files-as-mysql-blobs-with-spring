package daggerok.app

import java.lang.RuntimeException
import java.time.Instant

data class ReportItemDTO(
    val id: Long? = null,
    val jobId: Long = -1,
    val name: String = "",
    val content: String = "",
    val createdAt: Instant? = null,
)

fun ReportItemDTO.toEntity(): ReportItem =
    ReportItem(id, jobId, name, content.toByteArray(charset = Charsets.UTF_8), createdAt)

fun ReportItem.toDTO(): ReportItemDTO =
    ReportItemDTO(id, jobId, name, content = String(bytes = content, Charsets.UTF_8), createdAt)

class MissingFilenameException : RuntimeException("File name not found")
