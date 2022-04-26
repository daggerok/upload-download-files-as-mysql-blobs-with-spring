package daggerok.app

import org.springframework.data.jpa.repository.JpaRepository

interface ReportItems : JpaRepository<ReportItem, Long>
