package dev.toke.kpopapi.services

import dev.toke.kpopapi.dtos.AddressDTO
import dev.toke.kpopapi.models.Address
import dev.toke.kpopapi.repositories.AddressRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class AddressService(val addressRepository: AddressRepository) {
    companion object: KLogging()

    fun getAllAddresses(): List<AddressDTO> {
        logger.info("Retrieving all addresses")
        return addressRepository.findAll()
            .map { a -> AddressDTO(id = a.id, streetAddress = a.streetAddress, suburb = a.suburb, postCode = a.postCode) }
    }

    fun getAllAddressesBySearchTerm(searchTerm: String): List<AddressDTO> {
        logger.info("Retrieving addresses that contain the search term")
        return addressRepository.findAddressesByQueries(searchTerm)
            .map { a -> AddressDTO(id = a.id, streetAddress = a.streetAddress, suburb = a.suburb, postCode = a.postCode) }
    }

    fun getAddressById(id: UUID) : Optional<Address> {
        logger.info("Retrieving an address by its id")
        return addressRepository.findById(id)
    }


    fun addAddress(address: AddressDTO): AddressDTO {
        val addressEntity = address.let {
            Address(null, it.streetAddress, it.suburb, it.postCode)
        }
        addressRepository.save(addressEntity)
        logger.info("Saved address: ${ addressEntity.streetAddress }")

        return addressEntity.let {
            AddressDTO(it.id, it.streetAddress, it.suburb, it.postCode)
        }
    }

}