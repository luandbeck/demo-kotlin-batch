package com.example.demo

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.time.LocalDateTime

@SpringBootApplication
class DemoKotlinBatchApplication

fun main(args: Array<String>) {
    val context = SpringApplication.run(DemoKotlinBatchApplication::class.java, *args)
    val builder = JobParametersBuilder()
    builder.addString("timeStamp", LocalDateTime.now().toString())
    val jobName = context.environment.getProperty("spring.batch.job.default")
    if (jobName != null) {
        val jobLauncher = context.getBean(
            JobLauncher::class.java
        )
        val job = context.getBean(jobName, Job::class.java)
        var parameters = builder.toJobParameters()
        parameters = job.jobParametersIncrementer!!.getNext(parameters)
        try {
            jobLauncher.run(job, parameters)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        context.close()
    }
}