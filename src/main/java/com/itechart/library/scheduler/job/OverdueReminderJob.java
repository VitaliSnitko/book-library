package com.itechart.library.scheduler.job;

import com.itechart.library.scheduler.EmailConstants;
import com.itechart.library.scheduler.EmailScheduleManager;
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

/**
 * Serves for sending email that notifies user about overdue date, i.e. getting behind of deadline
 */
@Log4j
public class OverdueReminderJob implements Job {

    private static final Session session = EmailScheduleManager.getSession();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Message message = new MimeMessage(session);
            JobDataMap jobParam = context.getJobDetail().getJobDataMap();
            message.setFrom(new InternetAddress(EmailConstants.LIBRARY_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(jobParam.getString(EmailConstants.EMAIL_PARAM)));
            message.setSubject(EmailConstants.OVERDUE_MSG_SUBJECT);

            ST msgTemplate = new ST(EmailConstants.OVERDUE_MSG_TEXT);
            msgTemplate.add(EmailConstants.READER_NAME_PARAM, jobParam.getString(EmailConstants.READER_NAME_PARAM));
            msgTemplate.add(EmailConstants.BOOK_TITLE_PARAM, jobParam.getString(EmailConstants.BOOK_TITLE_PARAM));
            msgTemplate.add(EmailConstants.DUE_DATE_PARAM, jobParam.getString(EmailConstants.DUE_DATE_PARAM));
            message.setText(msgTemplate.render());

            Transport.send(message);
            log.info("Overdue reminder. Email sent to " + jobParam.getString(EmailConstants.EMAIL_PARAM));
        } catch (MessagingException e) {
            log.error(e);
        }
    }
}
