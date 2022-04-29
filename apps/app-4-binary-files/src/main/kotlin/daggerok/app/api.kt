package daggerok.app

import java.lang.RuntimeException
import java.time.Instant

data class ReportItemDTO(
    override val id: Long? = null,
    override val jobId: Long = -1,
    override val name: String = "",
    val content: String = "",
    override val createdAt: Instant? = null,
) : ReportItemDocument(id, jobId, name, createdAt)

open class ReportItemDocument(
    open val id: Long? = null,
    open val jobId: Long = -1,
    open val name: String = "",
    open val createdAt: Instant? = null,
)

fun ReportItemDTO.toEntity(): ReportItem =
    ReportItem(id, jobId, name, content.toByteArray(charset = Charsets.UTF_8), createdAt)

fun ReportItem.toDocument(): ReportItemDocument =
    ReportItemDocument(id, jobId, name, createdAt)

class MissingFilenameException : RuntimeException("File name not found")

class MissingFileException : RuntimeException("File not found")
