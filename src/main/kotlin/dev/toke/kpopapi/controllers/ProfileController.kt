package dev.toke.kpopapi.controllers

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.math.log

@RestController
@RequestMapping("/api/v1/profile")
class ProfileController {
    @Value("\${message}")
    lateinit var profileMessage: String
    companion object: KLogging()

    @GetMapping("")
    fun whichProfile(): String {
        logger.info("Checking which profile.")
        return profileMessage
    }
}