package negraoipr.com.ms_email.dtos;

import java.util.UUID;

public record EmailResultDTO(
        UUID eventId,
        UUID emailRequestId,
        UUID userId,
        String status
) {
}
