package dev.toke.kpopapi.services

import dev.toke.kpopapi.dtos.CitizenDTO
import dev.toke.kpopapi.exceptions.CitizenNotFoundException
import dev.toke.kpopapi.models.Citizen
import dev.toke.kpopapi.repositories.CitizenRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CitizenService(val citizenRepository: CitizenRepository) {

    companion object : KLogging()


    fun getAllCitizens(): List<CitizenDTO> {
        return citizenRepository.findAll().map { CitizenDTO(it.id, it.firstName, it.lastName, it.dob) }
    }

    fun getCitizenById(id: UUID): CitizenDTO? {
        val result = citizenRepository.findById(id).get()
        return CitizenDTO(
                id = result.id,
                firstName = result.firstName,
                lastName = result.lastName,
                dob = result.dob
            )

    }

    fun addCitizen(citizen: CitizenDTO): CitizenDTO {
        val citizenEntity = citizen.let {
            Citizen(id = null, it.firstName, it.lastName, it.dob)
        }
        citizenRepository.save(citizenEntity)

        logger.info("Saved citizen is: $citizenEntity")

        return citizenEntity.let {
            CitizenDTO(it.id, it.firstName, it.lastName, it.dob)
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
                CitizenDTO(it.id, it.firstName, it.lastName, it.dob)
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
        return citizenRepository.findCitizensByYearOfBirth(year).map { c -> CitizenDTO(c.id, c.firstName, c.lastName, c.dob) }

    }

}