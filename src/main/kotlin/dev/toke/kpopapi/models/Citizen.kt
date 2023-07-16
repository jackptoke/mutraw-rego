package dev.toke.kpopapi.models

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "CITIZEN")
data class Citizen(
    @Id // @GeneratedValue(strategy = GenerationType.AUTO)  if Int is used as an ID
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,
    @Column(name = "firstName")
    var firstName: String,
    @Column(name = "lastName")
    var lastName: String,
    @Column(name = "dob")
    var dob: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ADDRESS_ID", nullable = true)
    var address: Address? = null
) {
    override fun toString(): String {
        return "Citizen(id=$id, firstName=$firstName, lastName=$lastName, dob=$dob, addressId=${address?.id})"
    }
}
