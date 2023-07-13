package dev.toke.kpopapi.repositories

import dev.toke.kpopapi.models.Address
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface AddressRepository: CrudRepository<Address, UUID> {
    @Query(value = "SELECT * FROM ADDRESS WHERE streetAddress LIKE %?1% OR suburb LIKE %?1% OR postCode = ?1", nativeQuery = true)
    fun findAddressesByQueries(searchTerm: String): List<Address>
}