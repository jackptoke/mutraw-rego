package dev.toke.controllers

import dev.toke.kpopapi.KpopApiApplication
import dev.toke.kpopapi.dtos.AddressDTO
import dev.toke.kpopapi.repositories.AddressRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import util.addressDTO
import util.addressEntityList

@SpringBootTest(classes = [KpopApiApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class AddressControllerIntegrationTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var addressRepository: AddressRepository

    @BeforeEach
    fun setUp() {
        addressRepository.deleteAll()
        val addresses = addressEntityList()
        addressRepository.saveAll(addresses)
    }

    @Test
    fun getAllAddresses_returnAddresses() {
        val result = webTestClient.get()
            .uri("/api/v1/addresses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(AddressDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(3, result?.size)
    }

    @Test
    fun addAddress_returnAddress() {
        val address = addressDTO()

        val result = webTestClient.post()
            .uri("/api/v1/addresses")
            .bodyValue(address)
            .exchange()
            .expectStatus().isCreated
            .expectBody(AddressDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertNotNull(result)
        Assertions.assertEquals("43 MacQuarie Drive", result?.streetAddress)
        Assertions.assertEquals("Wyndham Vale", result?.suburb)
    }
}