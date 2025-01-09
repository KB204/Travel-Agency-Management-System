package net.travelsystem.reservationservice.config;

import net.travelsystem.reservationservice.batch.ReservationProcessor;
import net.travelsystem.reservationservice.entities.Reservation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.util.Collections;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private final ReservationProcessor processor;
    private final JobRepository repository;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(ReservationProcessor processor, JobRepository repository, DataSource dataSource, PlatformTransactionManager transactionManager) {
        this.processor = processor;
        this.repository = repository;
        this.dataSource = dataSource;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job databaseToFileJob(Step fromReservationsToFile) {
        return new JobBuilder("databaseToFileJob",repository)
                .incrementer(new RunIdIncrementer())
                .start(fromReservationsToFile)
                .build();
    }

    @Bean
    public Step fromReservationsToFile(FlatFileItemWriter<Reservation> flatFileItemWriter, JdbcPagingItemReader<Reservation> reservationJdbcPagingItemReader) {
        return new StepBuilder("from db to file",repository)
                .<Reservation,Reservation>chunk(20,transactionManager)
                .reader(reservationJdbcPagingItemReader)
                .processor(processor)
                .writer(flatFileItemWriter)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Reservation> reservationJdbcPagingItemReader(PagingQueryProvider pagingQueryProvider) {
        return new JdbcPagingItemReaderBuilder<Reservation>()
                .name("Reservations paging reader")
                .dataSource(dataSource)
                .queryProvider(pagingQueryProvider)
                .rowMapper(new DataClassRowMapper<>(Reservation.class))
                .pageSize(10)
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean queryProvider() {
        var queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setSelectClause("SELECT identifier, status, reservation_date, total_price, nbr_tickets");
        queryProvider.setFromClause("FROM reservation");
        queryProvider.setWhereClause("WHERE status = 'PENDING'");
        queryProvider.setDataSource(dataSource);
        queryProvider.setDatabaseType(DatabaseType.POSTGRES.name());
        queryProvider.setSortKeys(Collections.singletonMap("reservation_date", Order.DESCENDING));
        return queryProvider;
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Reservation> flatFileItemReader(@Value("#{jobParameters['output.file.name']}") String outputFile, @Value("${file.upload-dir}") String uploadDir) {
        String filePath = Paths.get(uploadDir,outputFile).toString();
        return new FlatFileItemWriterBuilder<Reservation>()
                .name("File of pending reservations")
                .resource(new FileSystemResource(filePath))
                .headerCallback(writer -> writer.append("En tête du fichier"))
                .delimited()
                .delimiter(";")
                .sourceType(Reservation.class)
                .names("identifier","status","reservationDate","totalPrice","nbrTickets")
                .shouldDeleteIfEmpty(Boolean.TRUE)
                .append(Boolean.FALSE)
                .build();
    }
}
