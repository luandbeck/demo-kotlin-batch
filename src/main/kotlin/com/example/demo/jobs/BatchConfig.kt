package com.example.demo.jobs

import com.example.demo.domain.Transacao
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class BatchConfig(
    private val jobRepository: JobRepository,
    private val platformTransactionManager: PlatformTransactionManager
) {

    private val log: Logger = LoggerFactory.getLogger(BatchConfig::class.java)


    @Bean
    fun importTransaction(jobRepository: JobRepository, importTransactionStep: Step): Job {
        return JobBuilder("importTransaction", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(importTransactionStep)
            .build()
    }

    @Bean
    fun importTransactionStep(
        reader: ItemReader<Transacao>,
        processor: ItemProcessor<Transacao, Transacao>,
        writer: ItemWriter<Transacao>
    ) =
        StepBuilder("importTransactionStep", jobRepository)
            .chunk<Transacao, Transacao>(1000, platformTransactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build()
}