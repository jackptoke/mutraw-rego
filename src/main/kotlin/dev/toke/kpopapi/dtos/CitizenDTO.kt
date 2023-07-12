package dev.toke.kpopapi.dtos

import jakarta.validation.constraints.NotBlank
import java.util.UUID


data class CitizenDTO(
    var id: UUID?,
    @get:NotBlank(message = "citizenDTO.firstName must not be blank")
    var firstName: String,
    @get:NotBlank(message = "citizenDTO.lastName must not be blank")
    val lastName: String,
    @get:NotBlank(message = "citizenDTO.dob must not be blank")
    val dob: String
    )
