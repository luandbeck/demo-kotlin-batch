package com.example.demo.jobs

import com.example.demo.domain.Transacao
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.integration.async.AsyncItemProcessor
import org.springframework.batch.integration.async.AsyncItemWriter
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.transaction.PlatformTransactionManager
import java.util.concurrent.Future

@Configuration
class BatchAsyncConfig(
    private val jobRepository: JobRepository,
    private val platformTransactionManager: PlatformTransactionManager
) {

    @Bean
    fun importTransactionAsync(jobRepository: JobRepository, importTransactionAsyncStep: Step): Job {
        return JobBuilder("importTransactionAsync", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(importTransactionAsyncStep)
            .build()
    }

    @Bean
    fun importTransactionAsyncStep(
        reader: ItemReader<Transacao>,
        asyncProcessor: ItemProcessor<Transacao, Future<Transacao>>,
        asyncWriter: ItemWriter<Future<Transacao>>
    ) =
        StepBuilder("importTransactionAsyncStep", jobRepository)
            .chunk<Transacao, Future<Transacao>>(1000, platformTransactionManager)
            .reader(reader)
            .processor(asyncProcessor)
            .writer(asyncWriter)
            .build()

    @Bean
    fun asyncProcessor(
        processor: ItemProcessor<Transacao, Transacao>?,
        taskExecutor: TaskExecutor?
    ): ItemProcessor<Transacao, Future<Transacao>> {
        val asyncProcessor: AsyncItemProcessor<Transacao, Transacao> = AsyncItemProcessor<Transacao, Transacao>()
        asyncProcessor.setTaskExecutor(taskExecutor!!)
        asyncProcessor.setDelegate(processor!!)
        return asyncProcessor
    }


    @Bean
    fun asyncWriter(writer: ItemWriter<Transacao>): ItemWriter<Future<Transacao>> {
        val asyncWriter = AsyncItemWriter<Transacao>()
        asyncWriter.setDelegate(writer)
        return asyncWriter
    }

}