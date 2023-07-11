package dev.toke.kpopapi.models

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "Citizen")
data class Citizen(
    @Id // @GeneratedValue(strategy = GenerationType.AUTO)  if Int is used as an ID
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,
    val firstName: String,
    val lastName: String,
    val dob: String
)
