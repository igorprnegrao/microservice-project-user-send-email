package negraoipr.com.ms_user.dtos;

import java.util.UUID;

public record EmailRequestStatusDTO(UUID emailRequestId, UUID userId, String status) {}
