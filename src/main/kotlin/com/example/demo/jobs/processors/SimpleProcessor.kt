package com.example.demo.jobs.processors

import com.example.demo.domain.Transacao
import com.example.demo.integrations.BlockIntegration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SimpleProcessor(private val blockIntegration: BlockIntegration) {

    private val log: Logger = LoggerFactory.getLogger(SimpleProcessor::class.java)

    @Bean
    fun processor(): ItemProcessor<Transacao, Transacao> {
        return ItemProcessor<Transacao, Transacao> {
            log.info("Processing item before blocking: $it")
            val response = this.blockIntegration.block(it)
            log.info("Processing item after blocking: $it")
            response
        }
    }

}