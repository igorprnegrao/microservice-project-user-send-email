package negraoipr.com.ms_email.producer;

import lombok.RequiredArgsConstructor;
import negraoipr.com.ms_email.dtos.EmailResultDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailResultProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.email-result.name}")
    private String routingKey;

    public void publishResult(EmailResultDTO result) {
        rabbitTemplate.convertAndSend("", routingKey, result);
    }

}
