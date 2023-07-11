package dev.toke.kpopapi.dtos

import java.util.UUID

data class CitizenDTO(
    var id: UUID?,
    var firstName: String,
    val lastName: String,
    val dob: String
    )
