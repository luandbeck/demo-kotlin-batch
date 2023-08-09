package com.example.demo.jobs.writers

import com.example.demo.domain.Transacao
import com.example.demo.integrations.TransacaoRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SimpleWriter(private val transacaoRepository: TransacaoRepository) {

    private val log: Logger = LoggerFactory.getLogger(SimpleWriter::class.java)

    //Um commit por chunk
    @Bean
    fun writer(): ItemWriter<Transacao> {
        return ItemWriter {
            transacaoRepository.saveAll(it)
            log.info("Chunk completed with ${it.size()} items")
        }
    }

    //Um commit por transacao
//    @Bean
//    fun writer(): ItemWriter<Transacao> {
//        return ItemWriter<Transacao> { transacoes ->
//            transacoes.forEach {
//                transacaoRepository.save(it)
//                log.info("Writing item: $it")
//            }
//            log.info("Chunk completed with ${transacoes.size()} items")
//        }
//    }
}