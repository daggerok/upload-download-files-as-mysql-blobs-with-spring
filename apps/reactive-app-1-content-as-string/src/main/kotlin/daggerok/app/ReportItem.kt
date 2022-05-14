package daggerok.app

import java.time.Instant
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME

@Table("report_items")
data class ReportItem(
    @Id val id: Long? = null,
    val jobId: Long = -1,
    val name: String = "",
    val content: String = "",
    @CreatedDate @LastModifiedDate @DateTimeFormat(iso = DATE_TIME) val lastModifiedAt: Instant? = Instant.now(),
)
