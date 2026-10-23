package negraoipr.com.ms_user.service;

import lombok.RequiredArgsConstructor;
import negraoipr.com.ms_user.dtos.EmailResultDTO;
import negraoipr.com.ms_user.dtos.EmailRequestStatusDTO;
import negraoipr.com.ms_user.dtos.UserCreatedDTO;
import negraoipr.com.ms_user.models.EmailRequestModel;
import negraoipr.com.ms_user.models.UserModel;
import negraoipr.com.ms_user.producers.UserProducer;
import negraoipr.com.ms_user.repositories.EmailRequestRepository;
import negraoipr.com.ms_user.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProducer userProducer;
    private final EmailRequestRepository emailRequestRepository;

    @Transactional
    public UserCreatedDTO save(UserModel userModel){

        userModel = userRepository.save(userModel);

        var emailRequest = new EmailRequestModel();
        emailRequest.setEmailRequestId(UUID.randomUUID());
        emailRequest.setUserId(userModel.getUserId());
        emailRequest.setStatus("PENDING");

        emailRequestRepository.save(emailRequest);

        userProducer.publishMessageEmail(userModel, emailRequest.getEmailRequestId());
        return new UserCreatedDTO(
                userModel.getUserId(),
                userModel.getName(),
                userModel.getEmail(),
                emailRequest.getEmailRequestId()
        );
    }

    @Transactional
    public void updateEmailStatus(EmailResultDTO result) {
        if (result == null || result.eventId() == null
                || result.emailRequestId() == null || result.userId() == null) {
            throw new IllegalArgumentException("Resultado deve informar eventId, emailRequestId e userId");
        }
        if (!"SENT".equals(result.status()) && !"ERROR".equals(result.status())) {
            throw new IllegalArgumentException("Status de email inválido: " + result.status());
        }

        var request = emailRequestRepository.findForUpdate(result.emailRequestId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Solicitação de email não encontrada: " + result.emailRequestId()));

        if (!result.userId().equals(request.getUserId())) {
            throw new IllegalArgumentException("Solicitação não pertence ao usuário informado");
        }
        if (result.eventId().equals(request.getProcessedEventId())) {
            return;
        }
        // Neste fluxo, cada solicitação recebe somente um resultado final.
        if (request.getProcessedEventId() != null) {
            throw new IllegalArgumentException("Solicitação já possui um resultado final");
        }

        request.setStatus(result.status());
        request.setProcessedEventId(result.eventId());
    }


    @Transactional(readOnly = true)
    public EmailRequestStatusDTO findEmailRequest(UUID userId, UUID emailRequestId) {
        var request = emailRequestRepository
                .findByEmailRequestIdAndUserId(emailRequestId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Solicitação de email não encontrada para este usuário"));

        return new EmailRequestStatusDTO(
                request.getEmailRequestId(), request.getUserId(), request.getStatus());
    }

    @Transactional(readOnly = true)
    public UserModel findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                ));
    }
}
