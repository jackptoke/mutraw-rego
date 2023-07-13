package dev.toke.kpopapi.repositories

import dev.toke.kpopapi.dtos.CitizenDTO
import dev.toke.kpopapi.models.Citizen
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CitizenRepository: CrudRepository<Citizen, UUID> {
    fun findByFirstNameContaining(name: String): List<Citizen>

    @Query(value = "SELECT * FROM CITIZEN WHERE first_name like %?1% OR last_name like %?1%", nativeQuery = true)
    fun findCitizensByNameContaining(name: String): List<Citizen>

    @Query(value = "SELECT * FROM CITIZEN WHERE dob like %?1", nativeQuery = true)
    fun findCitizensByYearOfBirth(year: String) : List<Citizen>

}