package negraoipr.com.ms_email.dtos;

import java.util.UUID;

public record EmailRecordDTO(
        UUID userId,
        String emailTo,
        String subject,
        String text,
        UUID emailRequestId
) {
}
