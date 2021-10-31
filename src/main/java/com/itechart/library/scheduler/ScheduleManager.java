package com.itechart.library.scheduler;

import com.itechart.library.model.dto.RecordDto;
import com.itechart.library.model.entity.RecordEntity;
import com.itechart.library.scheduler.trigger.TriggerFactory;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j
public class ScheduleManager {

    @Getter
    private static Session session;

    private static volatile ScheduleManager scheduleManager;
    private static Scheduler scheduler;
    private static final Map<String, TriggerKey> triggerKeys = Collections.synchronizedMap(new HashMap<>());

    public void scheduleJob(RecordEntity recordEntity, String key, Date triggerDate, Class<? extends Job> jobClass) {
        TriggerKey triggerKey = new TriggerKey(key + recordEntity.getId());
        triggerKeys.put(key + recordEntity.getId(), triggerKey);

        JobDetail job = JobBuilder.newJob(jobClass).build();
        job.getJobDataMap().put("email", recordEntity.getReader().getEmail());
        job.getJobDataMap().put("readerName", recordEntity.getReader().getName());
        job.getJobDataMap().put("bookTitle", recordEntity.getBook().getTitle());
        job.getJobDataMap().put("dueDate", recordEntity.getDueDate().toString());
        try {
            scheduler.scheduleJob(job, TriggerFactory.createSimpleTrigger(triggerKey, triggerDate));
        } catch (SchedulerException e) {
            log.error(e);
        }
    }

    public void unscheduleJob(RecordDto recordDto, String key) {
        try {
            if (triggerKeys.containsKey(key + recordDto.getId())) {
                scheduler.unscheduleJob(triggerKeys.get(key + recordDto.getId()));
                triggerKeys.remove(key + recordDto.getId());
            }
        } catch (SchedulerException e) {
            log.error(e);
        }
    }

    private ScheduleManager(){
    }

    public static ScheduleManager getInstance() {
        ScheduleManager localInstance = scheduleManager;
        if (localInstance == null) {
            synchronized (ScheduleManager.class) {
                localInstance = scheduleManager;
                if (localInstance == null) {
                    try {
                        scheduleManager = localInstance = new ScheduleManager();
                        scheduler = StdSchedulerFactory.getDefaultScheduler();
                        scheduler.start();
                        Properties properties = new Properties();
                        properties.load(ScheduleManager.class.getClassLoader().getResourceAsStream("email.properties"));
                        session = Session.getInstance(properties, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication("booklibrary2004@gmail.com", "rerernjnfv");
                            }
                        });
                    } catch (SchedulerException | IOException e) {
                        log.error(e);
                    }
                }
            }
        }
        return localInstance;
    }
}
