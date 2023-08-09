package com.example.demo.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.math.BigDecimal
import java.time.LocalDate

@Entity
data class Transacao(
    @Id
    val id: Long? = null,
    val dataTransacao: LocalDate? = null,
    val nomePessoa: String? = null,
    val valorReais: BigDecimal? = null,
    val tipo: String? = null
)
