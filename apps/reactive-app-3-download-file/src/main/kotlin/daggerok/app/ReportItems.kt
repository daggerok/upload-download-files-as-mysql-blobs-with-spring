package daggerok.app

import org.springframework.data.domain.Sort
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReportItems : ReactiveCrudRepository<ReportItem, Long> {
    fun findFirstByName(@Param("name") filename: String): Mono<ReportItem>
    fun findAllByNameContaining(@Param("name") filename: String, sort: Sort = Sort.by("lastModifiedAt", "id").descending()): Flux<ReportItem>
    fun findAll(sort: Sort = Sort.by("lastModifiedAt", "id").descending()): Flux<ReportItem>
}
