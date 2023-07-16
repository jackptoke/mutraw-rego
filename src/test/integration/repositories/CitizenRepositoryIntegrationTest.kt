package repositories

import dev.toke.kpopapi.KpopApiApplication
import dev.toke.kpopapi.models.Address
import dev.toke.kpopapi.repositories.AddressRepository
import dev.toke.kpopapi.repositories.CitizenRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import util.citizenEntityList
import java.util.stream.Stream

@ContextConfiguration(classes = [KpopApiApplication::class])
@DataJpaTest()
@ActiveProfiles("test")
class CitizenRepositoryIntegrationTest {

    @Autowired
    lateinit var citizenRepository: CitizenRepository

    @Autowired
    lateinit var addressRepository: AddressRepository

    @BeforeEach
    fun setUp() {
        citizenRepository.deleteAll()
        addressRepository.deleteAll()
        val address = Address(id = null, "63 Tobin Street", "Ararat", "3377")
        addressRepository.save(address)
        val citizens = citizenEntityList(address)
        citizenRepository.saveAll(citizens)
    }

    @Test
    fun findByFirstNameContaining() {
        val citizens = citizenRepository.findByFirstNameContaining("iah")
        println("Citizens: $citizens")

        Assertions.assertEquals(2, citizens.size)
    }

    @Test
    fun findCitizensByNameContaining() {
        val citizens = citizenRepository.findCitizensByNameContaining(name = "el")
        println("Citizens with name containing 'ok' : $citizens")

        Assertions.assertEquals(2, citizens.size)
    }

    @ParameterizedTest
    @MethodSource("citizenAndName")
    fun findCitizensByName_Approach2(name: String, count: Int) {
        val citizens = citizenRepository.findCitizensByNameContaining(name = name)

        Assertions.assertEquals(count, citizens.size)
    }

    @ParameterizedTest
    @MethodSource("citizenAndYearOfBirth")
    fun findCitizensByYearOfBirth(year: String, count: Int) {
        val citizens = citizenRepository.findCitizensByYearOfBirth(year)

        Assertions.assertEquals(count, citizens.size)
    }

    companion object {
        @JvmStatic
        fun citizenAndName() : Stream<Arguments> {
            return Stream.of(Arguments.arguments("ok", 6), Arguments.arguments("el", 2))
        }

        @JvmStatic
        fun citizenAndYearOfBirth() : Stream<Arguments> {
            return Stream.of(Arguments.arguments("", 6),
                Arguments.arguments("2013", 1))
        }
    }
}