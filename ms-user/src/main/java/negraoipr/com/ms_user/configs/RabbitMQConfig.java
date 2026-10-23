package negraoipr.com.ms_user.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    @Bean
    public JacksonJsonMessageConverter messageConverter() {

        return new JacksonJsonMessageConverter();

    }


    @Bean
    public Queue emailResultQueue(
            @Value("${broker.queue.email-result.name}") String queueName) {
        return new Queue(queueName, true);
    }
}
