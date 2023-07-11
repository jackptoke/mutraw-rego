package dev.toke.kpopapi.repositories

import dev.toke.kpopapi.models.Citizen
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CitizenRepository: CrudRepository<Citizen, UUID> {
}