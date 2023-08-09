package com.example.demo.integrations

import com.example.demo.domain.Transacao
import org.springframework.data.jpa.repository.JpaRepository

interface TransacaoRepository : JpaRepository<Transacao, Long> {
}