package controllers

import com.ninjasquad.springmockk.MockkBean
import dev.toke.kpopapi.KpopApiApplication
import dev.toke.kpopapi.controllers.AddressController
import dev.toke.kpopapi.dtos.AddressDTO
import dev.toke.kpopapi.services.AddressService
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import util.addressDTO
import java.util.UUID

@ContextConfiguration(classes = [KpopApiApplication::class])
@WebMvcTest(AddressController::class)
@AutoConfigureWebTestClient
class AddressControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var addressServiceMock: AddressService

    @Test
    fun addAddress_returnAddress() {
        val newAddress = addressDTO(id = UUID.randomUUID())

        every { addressServiceMock.addAddress(any()) } returns newAddress

        val result = webTestClient.post()
            .uri("/api/v1/addresses")
            .bodyValue(addressDTO())
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(AddressDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertNotNull(result?.id)
    }

    @Test
    fun addAddressWithBlankDetail_ReturnsValidationError() {
        val newAddress = addressDTO(id = null, streetAddress = "", suburb = "", postCode = "")

        every { addressServiceMock.addAddress(any()) } returns addressDTO(id=UUID.randomUUID())

        val result = webTestClient.post()
            .uri("/api/v1/addresses")
            .bodyValue(newAddress)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("addressDTO.postcode must not be blank;addressDTO.streetAddress must not be blank;addressDTO.suburb must not be blank", result)
    }
}