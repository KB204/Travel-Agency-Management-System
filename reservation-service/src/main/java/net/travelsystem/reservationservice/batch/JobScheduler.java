package net.travelsystem.reservationservice.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JobScheduler {
    private final Job dbToFileJob;
    private final JobLauncher jobLauncher;

    public JobScheduler(Job dbToFileJob, JobLauncher jobLauncher) {
        this.dbToFileJob = dbToFileJob;
        this.jobLauncher = jobLauncher;
    }

    @Scheduled(cron = "0/30 * * * *") // job will run every 30 seconds
    public void triggerJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String fileName = LocalDateTime.now().toString().concat("_reservations.csv");

        var jobParameters = new JobParametersBuilder()
                .addString("output.file.name",fileName)
                .addDate("processed",new Date())
                .toJobParameters();

        this.jobLauncher.run(dbToFileJob, jobParameters);
    }
}
