package negraoipr.com.ms_user.dtos;

import java.util.UUID;

public record UserCreatedDTO(
        UUID userId,
        String name,
        String email,
        UUID emailRequestId
) {}
