package dev.toke.kpopapi.controllers

import dev.toke.kpopapi.dtos.AddressDTO
import dev.toke.kpopapi.services.AddressService
import jakarta.validation.Valid
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/addresses")
@Validated
class AddressController(val addressService: AddressService) {
    companion object: KLogging()

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllAddresses(
        @RequestParam("searchTerm", required = false) searchTerm: String?) : List<AddressDTO> {
        logger.info("Retrieving all addresses")
        return searchTerm?.let {
            addressService.getAllAddressesBySearchTerm(it)
        } ?: addressService.getAllAddresses()

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addAddress(@RequestBody @Valid newAddress: AddressDTO): AddressDTO? {
        logger.info("addAddress is called")
        return try {
            logger.info("Successfully added the address")
            addressService.addAddress(newAddress)
        } catch (ex: Exception) {
            logger.error("Expected error while attempting to add an address")
            throw ex
        }
    }
}