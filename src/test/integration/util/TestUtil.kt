package util

import dev.toke.kpopapi.dtos.AddressDTO
import dev.toke.kpopapi.dtos.CitizenDTO
import dev.toke.kpopapi.models.Address
import dev.toke.kpopapi.models.Citizen
import java.util.UUID

fun citizenEntityList(address: Address) = listOf(
    Citizen(null, "Jack", "Toke", "02/12/1982", address),
    Citizen(null, "Amy", "Toke", "23/01/1988", address),
    Citizen(null, "Josiah", "Toke", "20/04/2013", address),
    Citizen(null, "Jeremiah", "Toke", "04/09/2014", address),
    Citizen(null, "Adrielle", "Toke", "12/06/2017", address),
    Citizen(null, "Israel", "Toke", "12/12/2022", address)
)

fun citizenDTO(id: UUID? = null,
               firstName: String = "John",
               lastName: String = "Kitevski",
               dob: String = "16/08/1965", addressId: UUID) = CitizenDTO(id, firstName, lastName, dob, addressId    )

fun addressEntityList() = listOf(
    Address(null, streetAddress = "18 Waugh Street", suburb = "Charlton", postCode = "3525"),
    Address(null, streetAddress = "63 Tobin Street", suburb = "Ararat", postCode = "3377"),
    Address(null, streetAddress = "17 Ribblesdale Ave", suburb = "Werribee", postCode = "3030")
)

fun addressDTO(id: UUID? = null,
               streetAddress: String = "43 MacQuarie Drive",
               suburb: String = "Wyndham Vale",
               postCode: String = "3030") = AddressDTO(id, streetAddress, suburb, postCode)