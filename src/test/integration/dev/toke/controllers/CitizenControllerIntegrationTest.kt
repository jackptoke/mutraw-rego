package dev.toke.controllers

import dev.toke.kpopapi.KpopApiApplication
import dev.toke.kpopapi.dtos.CitizenDTO
import dev.toke.kpopapi.models.Citizen
import dev.toke.kpopapi.repositories.CitizenRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatusCode
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import org.springframework.test.web.reactive.server.returnResult
import org.springframework.web.util.UriComponentsBuilder
import util.citizenEntityList

@SpringBootTest(classes = [KpopApiApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CitizenControllerIntegrationTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var citizenRepository: CitizenRepository

    @BeforeEach
    fun setUp() {
        citizenRepository.deleteAll()
        val citizens = citizenEntityList()
        citizenRepository.saveAll(citizens)
    }

    @Test
    fun addCourseTest() {
        val newCitizen = CitizenDTO(null, "Amy", "Toke", "23/01/1988")
        val result = webTestClient.post().uri("/api/v1/citizens")
            .bodyValue(newCitizen)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CitizenDTO::class.java).returnResult()

        Assertions.assertEquals("Amy", result.responseBody?.firstName)
        Assertions.assertEquals("Toke", result.responseBody?.lastName)
        Assertions.assertEquals("23/01/1988", result.responseBody?.dob)
        Assertions.assertNotNull(result.responseBody?.id)
    }

    @Test
    fun getAllCitizens() {

        val result = webTestClient.get()
            .uri("/api/v1/citizens")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(CitizenDTO::class.java).returnResult().responseBody

        Assertions.assertEquals(6, result?.size)
    }

    @Test
    fun getCitizenById() {
        val citizens = webTestClient.get().uri("/api/v1/citizens")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CitizenDTO::class.java).returnResult().responseBody

        val result = webTestClient.get()
            .uri("/api/v1/citizens/${citizens?.get(0)?.id}")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(CitizenDTO::class.java).returnResult().responseBody

        Assertions.assertNotNull(result?.id)
        Assertions.assertEquals(citizens?.get(0)?.firstName, result?.firstName)
        Assertions.assertEquals(citizens?.get(0)?.lastName, result?.lastName)
        Assertions.assertEquals(citizens?.get(0)?.id, result?.id)
    }

    @Test
    fun updateCitizen() {
        val citizens = webTestClient.get().uri("/api/v1/citizens")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CitizenDTO::class.java).returnResult().responseBody

        val updatedCitizen = citizens?.get(0)
        updatedCitizen?.firstName = "Jackie"

        val result = webTestClient.patch()
            .uri("/api/v1/citizens/${updatedCitizen?.id}")
            .bodyValue(updatedCitizen!!)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody(CitizenDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(updatedCitizen.firstName, result?.firstName)
    }

    @Test
    fun deleteCitizen() {
        val citizens = webTestClient.get().uri("/api/v1/citizens")
            .exchange()
            .expectStatus().isNoContent
            .expectBodyList(CitizenDTO::class.java)
            .returnResult().responseBody

        val citizenToDelete = citizens?.get(0)

         webTestClient
            .delete()
            .uri("/api/v1/citizens/${citizenToDelete?.id}")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
    }

    @Test
    fun findCitizenByYearOfBirth() {
        val uri = UriComponentsBuilder.fromUriString("/api/v1/citizens")
            .queryParam("yearOfBirth", "1982")
            .toUriString()

        val citizens = webTestClient.get().uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CitizenDTO::class.java)
            .returnResult().responseBody

        Assertions.assertEquals(1, citizens?.size)
        Assertions.assertEquals("Jack", citizens!![0].firstName)
    }

}