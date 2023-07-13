package dev.toke.kpopapi.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "ADDRESS")
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id : UUID?,
    @Column(name = "streetAddress")
    var streetAddress: String,
    @Column(name = "suburb")
    var suburb: String,
    @Column(name = "postCode")
    var postCode: String,
    @OneToMany(mappedBy = "address", cascade = [CascadeType.ALL], orphanRemoval = false)
    var citizens: List<Citizen> = mutableListOf()
)
