package controllers

import com.ninjasquad.springmockk.MockkBean
import dev.toke.kpopapi.KpopApiApplication
import dev.toke.kpopapi.controllers.CitizenController
import dev.toke.kpopapi.dtos.CitizenDTO
import dev.toke.kpopapi.models.Address
import dev.toke.kpopapi.services.CitizenService
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import util.citizenDTO
import util.citizenEntityList
import java.lang.RuntimeException
import java.util.UUID

@ContextConfiguration(classes = [KpopApiApplication::class])
@WebMvcTest(CitizenController::class)
@AutoConfigureWebTestClient
class CitizenControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var citizenServiceMock: CitizenService

    @Test
    fun getAllCitizens() {

        val address = Address(UUID.randomUUID(), "63 Tobin Street", "Ararat", "3377")
        every { citizenServiceMock.getAllCitizens() } returns citizenEntityList(address).map { c -> CitizenDTO(c.id, c.firstName, c.lastName, c.dob) }

        val result = webTestClient.get()
            .uri("/api/v1/citizens")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(CitizenDTO::class.java)
            .returnResult()

        Assertions.assertEquals(6, result.responseBody?.size)
    }

    @Test
    fun getCitizenById() {
        val id = UUID.fromString("bd04e311-46dd-46b3-899e-05d8e8e17805")
        val address = Address(UUID.randomUUID(), "63 Tobin Street", "Ararat", "3377")
        every { citizenServiceMock.getCitizenById(any()) } returns citizenEntityList(address).map { c -> CitizenDTO(c.id, c.firstName, c.lastName, c.dob) }[0]

        val result = webTestClient.get()
            .uri("/api/v1/citizens/{id}", id)
            .exchange()
            .expectStatus().isOk
            .expectBody(CitizenDTO::class.java).returnResult().responseBody

        Assertions
            .assertEquals("Jack", result?.firstName)
    }

    @Test
    fun addCitizen() {
        val newCitizen = citizenDTO(id = UUID.randomUUID(), addressId = UUID.randomUUID())

        every { citizenServiceMock.addCitizen(any())} returns newCitizen

        val result = webTestClient.post()
            .uri("/api/v1/citizens")
            .bodyValue(newCitizen)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CitizenDTO::class.java).returnResult().responseBody

        Assertions.assertEquals("John", result?.firstName)
    }

    @Test
    fun addCitizen_validationException() {
        val newCitizen = citizenDTO(id = UUID.randomUUID(), firstName = "", lastName = "", dob = "", addressId = UUID.randomUUID())

        every { citizenServiceMock.addCitizen(any())} returns citizenDTO(id = UUID.randomUUID(), addressId = UUID.randomUUID())

        val result = webTestClient.post()
            .uri("/api/v1/citizens")
            .bodyValue(newCitizen)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        // Utilising error message from GlobalErrorHandler
        Assertions.assertEquals("citizenDTO.dob must not be blank;citizenDTO.firstName must not be blank;citizenDTO.lastName must not be blank", result)
    }

    @Test
    fun addCitizen_runtimeException() {
        val citizenDTO = citizenDTO(null, "Josiah", "Toke", addressId = UUID.randomUUID())

        val errorMessage = "Unexpected error occurred."
        every { citizenServiceMock.addCitizen(any()) } throws Exception(errorMessage)

        val response = webTestClient
            .post()
            .uri("/api/v1/citizens")
            .bodyValue(citizenDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(errorMessage, response)
    }

    @Test
    fun updateCitizen() {
        val newCitizen = citizenDTO(id = UUID.randomUUID(), addressId = UUID.randomUUID())

        every { citizenServiceMock.updateCitizen(any(), any())} returns citizenDTO(id = newCitizen.id, "Jack", "Toke", "02/12/1982", newCitizen.addressId!!)

        val result = webTestClient.patch()
            .uri("/api/v1/citizens/{id}", newCitizen.id)
            .bodyValue(CitizenDTO(null,"Jack", "Toke", "02/12/1982"))
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(CitizenDTO::class.java)
            .returnResult().responseBody

        Assertions.assertEquals("02/12/1982", result?.dob)
    }

    @Test
    fun updateCitizenWithBadData() {
        val newCitizen = citizenDTO(id = UUID.randomUUID(), addressId = UUID.randomUUID())

        every { citizenServiceMock.updateCitizen(any(), any())} returns citizenDTO(id = newCitizen.id, "Jack", "Toke", "02/12/1982", newCitizen.addressId!!)

        webTestClient.patch()
            .uri("/api/v1/citizens/{id}", newCitizen.id)
            .bodyValue(CitizenDTO(null,"", "", "02/12/1982"))
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun deleteCitizen() {
        val citizen = citizenDTO(id = UUID.randomUUID(), addressId = UUID.randomUUID())
        every { citizenServiceMock.deleteCitizen(any())} just runs //returns Unit

        webTestClient.delete()
            .uri("/api/v1/citizens/${citizen.id}")
            .exchange()
            .expectStatus().isNoContent
    }
}