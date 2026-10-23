package negraoipr.com.ms_user.consumers;

import lombok.RequiredArgsConstructor;
import negraoipr.com.ms_user.dtos.EmailResultDTO;
import negraoipr.com.ms_user.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailResultConsumer {

    private final UserService userService;

    @RabbitListener(queues = "${broker.queue.email-result.name}")
    public void receiveResult(EmailResultDTO emailResult){

        userService.updateEmailStatus(emailResult);
    }


}
