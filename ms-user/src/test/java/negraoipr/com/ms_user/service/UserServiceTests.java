package negraoipr.com.ms_user.service;

import negraoipr.com.ms_user.dtos.EmailResultDTO;
import negraoipr.com.ms_user.models.EmailRequestModel;
import negraoipr.com.ms_user.models.UserModel;
import negraoipr.com.ms_user.producers.UserProducer;
import negraoipr.com.ms_user.repositories.EmailRequestRepository;
import negraoipr.com.ms_user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {
    private final UserRepository users = mock(UserRepository.class);
    private final UserProducer producer = mock(UserProducer.class);
    private final EmailRequestRepository requests = mock(EmailRequestRepository.class);
    private final UserService service = new UserService(users, producer, requests);

    @Test
    void createsRequestWithSavedUserIdAndPublishesSameRequestId() {
        var input = new UserModel();
        var saved = new UserModel();
        saved.setUserId(UUID.randomUUID());
        when(users.save(input)).thenReturn(saved);

        assertSame(saved, service.save(input));

        var captor = ArgumentCaptor.forClass(EmailRequestModel.class);
        var order = inOrder(users, requests, producer);
        order.verify(users).save(input);
        order.verify(requests).save(captor.capture());
        var request = captor.getValue();
        assertEquals(saved.getUserId(), request.getUserId());
        assertEquals("PENDING", request.getStatus());
        assertNotNull(request.getEmailRequestId());
        order.verify(producer).publishMessageEmail(saved, request.getEmailRequestId());
    }

    @Test
    void recordsResultAndIgnoresRedeliveryOfSameEvent() {
        var request = new EmailRequestModel(UUID.randomUUID(), UUID.randomUUID(), "PENDING", null);
        var result = new EmailResultDTO(UUID.randomUUID(), request.getEmailRequestId(), request.getUserId(), "SENT");
        when(requests.findForUpdate(request.getEmailRequestId())).thenReturn(Optional.of(request));

        service.updateEmailStatus(result);
        service.updateEmailStatus(result);

        assertEquals("SENT", request.getStatus());
        assertEquals(result.eventId(), request.getProcessedEventId());
        verifyNoInteractions(users, producer);
    }

    @Test
    void rejectsResultForAnotherUserWithoutChangingRequest() {
        var request = new EmailRequestModel(UUID.randomUUID(), UUID.randomUUID(), "PENDING", null);
        when(requests.findForUpdate(request.getEmailRequestId())).thenReturn(Optional.of(request));
        var result = new EmailResultDTO(UUID.randomUUID(), request.getEmailRequestId(), UUID.randomUUID(), "SENT");

        assertThrows(IllegalArgumentException.class, () -> service.updateEmailStatus(result));
        assertEquals("PENDING", request.getStatus());
        assertNull(request.getProcessedEventId());
    }
}
