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

/**
 * Class that serves for managing Quartz and mailing components such as {@link Session},
 * {@link Scheduler}, {@link Job}, {@link Trigger},
 */
@Log4j
public class EmailScheduleManager {

    @Getter
    private static Session session;

    private static volatile EmailScheduleManager scheduleManager;
    private static Scheduler scheduler;
    private static Map<String, TriggerKey> triggerKeys = Collections.synchronizedMap(new HashMap<>());

    public void subscribe(RecordEntity recordEntity, String key, Date triggerDate, Class<? extends Job> jobClass) {
        TriggerKey triggerKey = new TriggerKey(key + recordEntity.getId());
        triggerKeys.put(key + recordEntity.getId(), triggerKey);

        JobDetail job = JobBuilder.newJob(jobClass).build();
        job.getJobDataMap().put(EmailConstants.EMAIL_PARAM, recordEntity.getReader().getEmail());
        job.getJobDataMap().put(EmailConstants.READER_NAME_PARAM, recordEntity.getReader().getName());
        job.getJobDataMap().put(EmailConstants.BOOK_TITLE_PARAM, recordEntity.getBook().getTitle());
        job.getJobDataMap().put(EmailConstants.DUE_DATE_PARAM, recordEntity.getDueDate().toString());
        try {
            scheduler.scheduleJob(job, TriggerFactory.createSimpleTrigger(triggerKey, triggerDate));
        } catch (SchedulerException e) {
            log.error(e);
        }
    }

    public void unsubscribe(RecordDto recordDto, String key) {
        try {
            if (triggerKeys.containsKey(key + recordDto.getId())) {
                scheduler.unscheduleJob(triggerKeys.get(key + recordDto.getId()));
                triggerKeys.remove(key + recordDto.getId());
            }
        } catch (SchedulerException e) {
            log.error(e);
        }
    }

    private EmailScheduleManager() {
        init();
    }

    private void init() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            Properties properties = new Properties();
            properties.load(EmailScheduleManager.class.getClassLoader().getResourceAsStream("email.properties"));
            session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EmailConstants.LIBRARY_EMAIL, EmailConstants.LIBRARY_EMAIL_PASSWORD);
                }
            });
        } catch (SchedulerException | IOException e) {
            log.error(e);
        }
    }

    public static EmailScheduleManager getInstance() {
        EmailScheduleManager localInstance = scheduleManager;
        if (localInstance == null) {
            synchronized (EmailScheduleManager.class) {
                localInstance = scheduleManager;
                if (localInstance == null) {
                    scheduleManager = localInstance = new EmailScheduleManager();
                }
            }
        }
        return localInstance;
    }
}
