package negraoipr.com.ms_user.dtos;

import java.util.UUID;

public record EmailResultDTO(
        UUID eventId,
        UUID emailRequestId,
        UUID userId,
        String status
) {
}
