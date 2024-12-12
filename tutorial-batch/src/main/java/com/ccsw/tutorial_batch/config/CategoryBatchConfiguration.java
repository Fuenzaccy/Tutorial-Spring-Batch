package com.ccsw.tutorial_batch.config;

import com.ccsw.tutorial_batch.Processor.CategoryItemProcessor;
import com.ccsw.tutorial_batch.listener.JobCategoryCompletionNotificationListener;
import com.ccsw.tutorial_batch.model.Category;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class CategoryBatchConfiguration {

    @Bean
    public ItemReader<Category> readerCategory() {
        return new FlatFileItemReaderBuilder<Category>().name("categoryItemReader")
                .resource(new ClassPathResource("category-list.csv"))
                .delimited()
                .names(new String[]{"name", "type", "characteristics"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Category.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<Category, Category> processorCategory() {
        return new CategoryItemProcessor();
    }

    @Bean
    public ItemWriter<Category> databaseWriterCategory(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Category>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO category (name, type, characteristics) VALUES (:name, :type, :characteristics)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public ItemWriter<Category> fileWriterCategory() {
        return new FlatFileItemWriterBuilder<Category>()
                .name("categoryFileWriter")
                .resource(new FileSystemResource("target/category-output.txt"))
                .lineAggregator(new DelimitedLineAggregator<>() {{
                    setDelimiter(";");
                }})
                .build();
    }

    @Bean
    public ItemWriter<Category> compositeWriterCategory(ItemWriter<Category> databaseWriterCategory,
                                                        ItemWriter<Category> fileWriterCategory) {
        return new CompositeItemWriterBuilder<Category>()
                .delegates(databaseWriterCategory, fileWriterCategory)
                .build();
    }

    @Bean
    public Step step1Category(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                              ItemReader<Category> readerCategory, ItemProcessor<Category, Category> processorCategory,
                              ItemWriter<Category> compositeWriterCategory) {
        return new StepBuilder("step1Category", jobRepository)
                .<Category, Category>chunk(10, transactionManager)
                .reader(readerCategory)
                .processor(processorCategory)
                .writer(compositeWriterCategory) // Usa el CompositeItemWriter aquí
                .build();
    }

    @Bean
    public Job jobCategory(JobRepository jobRepository, JobCategoryCompletionNotificationListener listener, Step step1Category) {
        return new JobBuilder("jobCategory", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1Category)
                .end()
                .build();
    }
}