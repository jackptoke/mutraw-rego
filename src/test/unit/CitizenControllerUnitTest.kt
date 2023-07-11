import com.google.gson.Gson
import com.ninjasquad.springmockk.MockkBean
import dev.toke.kpopapi.KpopApiApplication
import dev.toke.kpopapi.controllers.CitizenController
import dev.toke.kpopapi.dtos.CitizenDTO
import dev.toke.kpopapi.models.Citizen
import dev.toke.kpopapi.services.CitizenService
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
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

        every { citizenServiceMock.getAllCitizens() } returns emptyList<CitizenDTO>()

        val result = webTestClient.get()
            .uri("/api/v1/citizens")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(CitizenDTO::class.java)
            .returnResult()

        Assertions.assertEquals(emptyList<CitizenDTO>(), result.responseBody)
    }

    @Test
    fun getCitizenById() {
        val id = UUID.fromString("bd04e311-46dd-46b3-899e-05d8e8e17805")

        every { citizenServiceMock.getCitizenById(any()) } returns null

        val result = webTestClient.get()
            .uri("/api/v1/citizens/{id}", id)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody().isEmpty

        Assertions
            .assertEquals(null, result.responseBody)
    }
}