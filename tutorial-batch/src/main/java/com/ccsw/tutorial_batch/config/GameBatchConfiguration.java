package com.ccsw.tutorial_batch.config;

import com.ccsw.tutorial_batch.Processor.GameItemProcessor;
import com.ccsw.tutorial_batch.model.Game;
import com.ccsw.tutorial_batch.model.GameExport;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class GameBatchConfiguration {

    private static final String QUERY_GAMES = """
                SELECT id, title AS title, age_recommended AS ageRecommended, stock
                    FROM games
            """;

    @Bean
    public JdbcCursorItemReader<Game> gameItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Game>()
                .name("gameItemReader")
                .dataSource(dataSource)
                .sql(QUERY_GAMES)
                .rowMapper((rs, rowNum) -> {
                    Game game = new Game();
                    game.setId(rs.getLong("id"));
                    game.setTitle(rs.getString("title"));
                    game.setAgeRecommended(rs.getInt("ageRecommended"));
                    game.setStock(rs.getInt("stock"));
                    return game;
                })
                .build();
    }

    @Bean
    public ItemProcessor<Game, GameExport> gameItemProcessor() {
        return new GameItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<GameExport> gameItemWriter() {
        return new FlatFileItemWriterBuilder<GameExport>()
                .name("gameItemWriter")
                .resource(new FileSystemResource("target/games-data.csv"))
                .delimited()
                .delimiter(";") // Usa ; como delimitador
                .names("title", "availability") // Campos de GameExport
                .build();
    }

    @Bean
    public Step gameStep(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager,
                         JdbcCursorItemReader<Game> gameItemReader,
                         ItemProcessor<Game, GameExport> gameItemProcessor,
                         FlatFileItemWriter<GameExport> gameItemWriter) {
        return new StepBuilder("gameStep", jobRepository)
                .<Game, GameExport>chunk(10, transactionManager)
                .reader(gameItemReader)
                .processor(gameItemProcessor)
                .writer(gameItemWriter)
                .build();
    }

    @Bean
    public Job gameJob(JobRepository jobRepository, Step gameStep) {
        return new JobBuilder("gameJob", jobRepository)
                .start(gameStep)
                .build();
    }
}