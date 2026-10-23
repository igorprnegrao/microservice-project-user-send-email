package negraoipr.com.ms_user.producers;

import lombok.RequiredArgsConstructor;
import negraoipr.com.ms_user.dtos.EmailDTO;
import negraoipr.com.ms_user.models.UserModel;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProducer {

    private  final RabbitTemplate rabbitTemplate;

    @Value("${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(UserModel userModel, UUID emailRequestId){
        var emailDto = new EmailDTO();
        emailDto.setEmailRequestId(emailRequestId);
        emailDto.setUserId(userModel.getUserId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Cadastro realizado com sucesso!");
        emailDto.setText(userModel.getName() + " seja bemvindo(a)! \n Agradecemos o seu cadastro, aproveite " +
                "agora todos os recursos da nossa plataforma!");

        rabbitTemplate.convertAndSend("",routingKey, emailDto);
    }

}
