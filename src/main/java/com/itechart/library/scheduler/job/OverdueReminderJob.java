package com.itechart.library.scheduler.job;

import com.itechart.library.scheduler.ScheduleManager;
import lombok.extern.log4j.Log4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.stringtemplate.v4.ST;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Log4j
public class OverdueReminderJob implements Job {

    private static final Session session = ScheduleManager.getSession();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Message message = new MimeMessage(session);
            JobDataMap jobParam = context.getJobDetail().getJobDataMap();
            message.setFrom(new InternetAddress("booklibrary2004@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("kvedrok@gmail.com"));
            //message.addRecipient(Message.RecipientType.TO, new InternetAddress(jobParam.getString("email")));
            message.setSubject("Book library. You have unreturned book.");

            ST msgText = new ST("""
                    Hey, <readerName>. We are sure you had an extremely good reason not to return the book, \
                    but nevertheless we look forward to its return. If you return it soon, we promise not to do anything with you.
                    «<bookTitle>» was expected to be returned on <dueDate>. Hope to see you soon.
                    
                    Contact the library:
                    Tel: 8-800-555-35-35
                    Web site: localhost:8080
                    """);
            msgText.add("readerName", jobParam.getString("readerName"));
            msgText.add("bookTitle", jobParam.getString("bookTitle"));
            msgText.add("dueDate", jobParam.getString("dueDate"));
            message.setText(msgText.render());

            Transport.send(message);
            log.info("Overdue reminder email sent to " + jobParam.getString("email"));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
