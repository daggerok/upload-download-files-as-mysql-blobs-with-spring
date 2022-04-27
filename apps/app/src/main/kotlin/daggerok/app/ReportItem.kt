package daggerok.app

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME

@Entity
@Table(name = "report_items")
data class ReportItem(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, updatable = false)
    val id: Long? = null,

    @Column(nullable = false, updatable = false)
    val jobId: Long = -1,

    @Column(nullable = false, updatable = true)
    val name: String = "",

    @Lob
    @Suppress("ArrayInDataClass")
    @Column(nullable = false, updatable = true)
    val content: ByteArray = ByteArray(0),

    @UpdateTimestamp
    @LastModifiedDate
    @DateTimeFormat(iso = DATE_TIME)
    @Column(nullable = false, updatable = true)
    val createdAt: Instant? = null,
)
