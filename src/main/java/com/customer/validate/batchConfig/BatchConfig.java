package com.customer.validate.batchConfig;


import com.customer.validate.processor.DataProcessor;
import com.customer.validate.reader.Reader;
import com.customer.validate.reader.XmlReader;
import com.customer.validate.writer.Writer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@EnableBatchProcessing
public class BatchConfig  {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean
    @StepScope
    public Reader reader(){return new Reader();
    }

    @Bean
    @StepScope
    public XmlReader xmlReader(){return new XmlReader();
    }


    @Bean
    @StepScope
    public DataProcessor processor(){
        return new DataProcessor();
    }

    @Bean
    @StepScope
    public Writer writer(){
        return new Writer();
    }


    @Bean
    public Job flatFileJob(@Qualifier("FlatFileStep") Step FlatFileStep) {
        return jobBuilderFactory.get("flatFileJob")
                .incrementer(new RunIdIncrementer())
                .flow(FlatFileStep).end().build();
    }

    @Bean
    public Step FlatFileStep() {
        return stepBuilderFactory.get("FlatFileStep").chunk(10)
                .reader(reader()).processor(processor())
                .writer(writer()).build();
    }

    @Bean
    public Job xmlJob(@Qualifier("xmlStep") Step FlatFileStep) {
        return jobBuilderFactory.get("xmlJob")
                .incrementer(new RunIdIncrementer())
                .flow(FlatFileStep).end().build();
    }

    @Bean
    public Step xmlStep() {
        return stepBuilderFactory.get("xmlStep").chunk(10)
                .reader(xmlReader()).processor(processor())
                .writer(writer()).build();
    }
}
