package util

import dev.toke.kpopapi.dtos.CitizenDTO
import dev.toke.kpopapi.models.Citizen
import java.util.UUID

fun citizenEntityList() = listOf(
    Citizen(null, "Jack", "Toke", "02/12/1982"),
    Citizen(null, "Amy", "Toke", "23/01/1988"),
    Citizen(null, "Josiah", "Toke", "20/04/2013"),
    Citizen(null, "Jeremiah", "Toke", "04/09/2014"),
    Citizen(null, "Adrielle", "Toke", "12/06/2017"),
    Citizen(null, "Israel", "Toke", "12/12/2022")
)

fun citizenDTO(id: UUID? = null,
               firstName: String = "John",
               lastName: String = "Kitevski",
               dob: String = "16/08/1965") = CitizenDTO(id, firstName, lastName, dob)
