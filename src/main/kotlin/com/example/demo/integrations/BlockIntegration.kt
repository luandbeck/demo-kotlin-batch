package com.example.demo.integrations

import com.example.demo.domain.Transacao
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class BlockIntegration(private val restTemplate: RestTemplate) {

    fun block(transaction: Transacao): Transacao {
        val url = "https://api.kanye.rest/"
        val response = restTemplate.getForObject(url, String::class.java)
        return transaction
    }

}