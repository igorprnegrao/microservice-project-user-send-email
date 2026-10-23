package negraoipr.com.ms_email.service;

import lombok.RequiredArgsConstructor;
import negraoipr.com.ms_email.dtos.EmailResultDTO;
import negraoipr.com.ms_email.models.EmailModel;
import negraoipr.com.ms_email.enums.StatusEmail;
import negraoipr.com.ms_email.producer.EmailResultProducer;
import negraoipr.com.ms_email.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    private final EmailResultProducer emailResultProducer;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Transactional
    public EmailModel sendEmail(EmailModel emailModel) {
        try {
            emailModel.setSentDateEmail(LocalDateTime.now());
            emailModel.setEmailFrom(emailFrom);


            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            javaMailSender.send(message);

            emailModel.setStatusEmail(StatusEmail.SENT);

        } catch (MailException e) {

            emailModel.setStatusEmail(StatusEmail.ERROR);
        }

        var savedEmail = emailRepository.save(emailModel);


        var result = new EmailResultDTO(
                UUID.randomUUID(),
                savedEmail.getEmailRequestId(),
                savedEmail.getUserId(),
                savedEmail.getStatusEmail().name()
        );

        emailResultProducer.publishResult(result);
        return savedEmail;

    }
}
