package dev.toke.kpopapi.services

import dev.toke.kpopapi.dtos.CitizenDTO
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

    fun updateCitizen(citizen: CitizenDTO): CitizenDTO? {
        val updatedCitizen = citizenRepository
            .save(Citizen(citizen.id, citizen.firstName, citizen.lastName, citizen.dob))
        return CitizenDTO(updatedCitizen.id, updatedCitizen.firstName, updatedCitizen.lastName, updatedCitizen.dob)
    }

    fun mapToDTO(citizen: Citizen): CitizenDTO {
        return CitizenDTO(
                id = citizen.id,
                firstName = citizen.firstName,
                lastName = citizen.lastName,
                dob = citizen.dob
            )
    }

    fun mapToCitizen(citizen: CitizenDTO): Citizen {
        return Citizen(
            id = citizen.id,
            firstName = citizen.firstName,
            lastName = citizen.lastName,
            dob = citizen.dob
        )
    }
}