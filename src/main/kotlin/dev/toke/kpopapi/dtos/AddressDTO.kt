package dev.toke.kpopapi.dtos

import jakarta.validation.constraints.NotBlank
import java.util.UUID


data class AddressDTO(
    val id: UUID?,
    @get:NotBlank(message = "addressDTO.streetAddress must not be blank")
    var streetAddress: String,
    @get:NotBlank(message = "addressDTO.suburb must not be blank")
    var suburb: String,
    @get:NotBlank(message = "addressDTO.postcode must not be blank")
    var postCode:String
)
