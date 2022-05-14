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
    @Column(nullable = false, updatable = true)
    val content: String = "",

    // @Lob // Use columnDefinition if you want to serialize mysql blob as kotlin string
    // @Column(nullable = false, updatable = true, columnDefinition="LONGBLOB NOT NULL")
    // val content: String = "",
    //
    // @Lob // No needs to use columnDefinition with bytes
    // @Suppress("ArrayInDataClass")
    // @Column(nullable = false, updatable = true)
    // val content: ByteArray = ByteArray(0),

    // @CreatedDate
    @UpdateTimestamp
    @LastModifiedDate
    // @CreationTimestamp
    @DateTimeFormat(iso = DATE_TIME)
    @Column(nullable = false, updatable = true)
    // @DateTimeFormat(iso = DATE_TIME, style = "SSSXXX")
    // @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    // @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val createdAt: Instant? = null, // Instant.now(),
)
