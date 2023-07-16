package dev.toke.kpopapi.services

import dev.toke.kpopapi.dtos.CitizenDTO
import dev.toke.kpopapi.exceptions.AddressNotFoundException
import dev.toke.kpopapi.exceptions.CitizenNotFoundException
import dev.toke.kpopapi.models.Citizen
import dev.toke.kpopapi.repositories.CitizenRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CitizenService(
    val citizenRepository: CitizenRepository,
    val addressService: AddressService,
) {

    companion object : KLogging()


    fun getAllCitizens(): List<CitizenDTO> {
        return citizenRepository.findAll().map { CitizenDTO(it.id, it.firstName, it.lastName, it.dob) }
    }

    fun getCitizenById(id: UUID): CitizenDTO? {
        val result = citizenRepository.findById(id).get()
        return result.address?.id?.let {
            CitizenDTO(
                id = result.id,
                firstName = result.firstName,
                lastName = result.lastName,
                dob = result.dob,
                addressId = it
            )
        }
    }

    fun addCitizen(citizen: CitizenDTO): CitizenDTO? {
        val addressOptional = addressService.getAddressById(citizen.addressId!!)

        if(!addressOptional.isPresent) throw AddressNotFoundException("Address with id ${citizen.addressId} is not found")

        return addressOptional.let { address ->
            val citizenEntity = citizen.let {
                Citizen(id = null, it.firstName, it.lastName, it.dob, address.get())
            }

            logger.info("Saving citizen in progress ...")
            citizenRepository.save(citizenEntity)

            logger.info("Saved citizen is: $citizenEntity")

            return citizenEntity.let {
                it.address?.id?.let { it1 -> CitizenDTO(it.id, it.firstName, it.lastName, it.dob, addressId = it1) }
            }
        }

    }

    fun updateCitizen(citizenId: UUID, citizen: CitizenDTO): CitizenDTO? {
        val existingCitizen = citizenRepository.findById(citizenId)
        return if(existingCitizen.isPresent) {
            existingCitizen.get().let {
                it.firstName = citizen.firstName
                it.lastName = citizen.lastName
                it.dob = citizen.dob
                citizenRepository.save(it)
                logger.info("Successfully updated citizen with id $citizenId")
                it.address?.id?.let { it1 -> CitizenDTO(it.id, it.firstName, it.lastName, it.dob, addressId = it1) }
            }
        } else {
            logger.info("Cannot find citizen with id $citizenId")
            throw CitizenNotFoundException("Citizen not found for id $citizenId")
        }
    }

    fun deleteCitizen(citizenId: UUID) {
        val existingCitizen = citizenRepository.findById(citizenId)
        if(existingCitizen.isPresent) {
            citizenRepository.deleteById(citizenId)
            logger.info("Successfully deleted citizen with id $citizenId")
        }
        else {
            logger.info("Cannot find citizen with id $citizenId")
            throw CitizenNotFoundException("Citizen with id $citizenId is not found.")
        }
    }

    fun findCitizensByYearOfBirth(year: String): List<CitizenDTO> {
        val result = citizenRepository.findCitizensByYearOfBirth(year)
        return result.let {
            it.map { c ->
                CitizenDTO(c.id, c.firstName, c.lastName, c.dob, c.address?.id)
            }
        }
    }

}