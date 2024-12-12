package com.ccsw.tutorial_batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

public class CleanTasklet implements Tasklet, InitializingBean {

    private Resource directory;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        File dir = resolveDirectory();

        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                boolean deleted = file.delete();
                if (!deleted) {
                    throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
                }
            }
        }
        return RepeatStatus.FINISHED;
    }

    public void setDirectoryResource(Resource directory) {
        this.directory = directory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (directory == null) {
            throw new UnexpectedJobExecutionException("Directory must be set");
        }
    }

    private File resolveDirectory() throws IOException {
        if (directory.getURI().getScheme().equals("file")) {
            return directory.getFile();
        } else {
            throw new UnexpectedJobExecutionException("The provided resource is not a physical directory");
        }
    }
}