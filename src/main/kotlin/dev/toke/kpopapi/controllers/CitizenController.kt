package dev.toke.kpopapi.controllers

import dev.toke.kpopapi.dtos.CitizenDTO
import dev.toke.kpopapi.services.CitizenService
import jakarta.validation.Valid
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/citizens")
@Validated
class CitizenController(val citizenService: CitizenService) {

    companion object: KLogging()

    @GetMapping
    fun getAllCitizens(@RequestParam("yearOfBirth", required = false) yearOfBirth: String?):List<CitizenDTO> {
        logger.info("Get all citizens")
        return yearOfBirth?.let {
            citizenService.findCitizensByYearOfBirth(yearOfBirth)
        } ?: citizenService.getAllCitizens()
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getCitizen(@PathVariable("id") id: UUID): CitizenDTO? {
        logger.info("Get detail for citizen $id")
        return citizenService.getCitizenById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCitizen(@RequestBody @Valid newCitizen: CitizenDTO) : CitizenDTO? {
        return try {
            logger.info("Create a new citizen into the database")
            citizenService.addCitizen(newCitizen)
        }catch (ex: Exception) {
            logger.error("Failed to create a new citizen.")
            throw ex
        }
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCitizen(@RequestBody @Valid updatedCitizen: CitizenDTO, @PathVariable id: UUID): CitizenDTO? {
        logger.info("Updating citizen with id $id")
        return citizenService.updateCitizen(id, updatedCitizen)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCitizen(@PathVariable id: UUID) {
        logger.info("Deleting citizen with id $id")
        citizenService.deleteCitizen(id)
    }
}