package negraoipr.com.ms_email.consumers;

import lombok.RequiredArgsConstructor;
import negraoipr.com.ms_email.dtos.EmailRecordDTO;
import negraoipr.com.ms_email.models.EmailModel;
import negraoipr.com.ms_email.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void ListenEmailQueue(@Payload EmailRecordDTO  emailRecordDTO) {
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(emailRecordDTO, emailModel);

        emailService.sendEmail(emailModel);
    }

}
