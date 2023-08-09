package com.example.demo.jobs.readers

import com.example.demo.domain.Transacao
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.DefaultLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.batch.item.file.transform.FieldSet
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.time.LocalDate

@Configuration
class SimpleReader {

    @Bean
    fun reader(): ItemReader<Transacao> {
        return FlatFileItemReaderBuilder<Transacao>()
            .name("transacoesFileReader")
            .resource(ClassPathResource("files/transacoes.csv"))
            .linesToSkip(1)
            .delimited()
            .names("id", "data", "nome", "valor", "tipo")
            .addComment("--")
            .fieldSetMapper { fieldSet: FieldSet ->
                Transacao(
                    fieldSet.readLong("id"),
                    LocalDate.parse(fieldSet.readString("data")), fieldSet.readString("nome"),
                    fieldSet.readBigDecimal("valor"), fieldSet.readString("tipo")
                )
            }
            .build()
    }

//Nao funcionou, provavel problema com nome das colunas
    //    @Bean
//    fun reader(): ItemReader<Transacao> {
//        return FlatFileItemReaderBuilder<Transacao>()
//            .name("transacoesFileReader")
//            .resource(FileSystemResource("files/transacoes.csv"))
//            .linesToSkip(1) // Pule a primeira linha se contiver cabeçalho
//            .lineMapper(lineMapper())
//            .build()
//    }


    //Nao funcionou, provavel problema com nome das colunas
    private fun lineMapper(): DefaultLineMapper<Transacao> {
        val lineTokenizer = DelimitedLineTokenizer()
        lineTokenizer.setDelimiter(",") // Define o delimitador do CSV
        lineTokenizer.setNames("id", "dataTransacao", "nomePessoa", "valorReais", "tipo") // Nomes das colunas

        val fieldSetMapper = BeanWrapperFieldSetMapper<Transacao>()
        fieldSetMapper.setTargetType(Transacao::class.java)

        val lineMapper = DefaultLineMapper<Transacao>()
        lineMapper.setLineTokenizer(lineTokenizer)
        lineMapper.setFieldSetMapper(fieldSetMapper)

        return lineMapper
    }
}