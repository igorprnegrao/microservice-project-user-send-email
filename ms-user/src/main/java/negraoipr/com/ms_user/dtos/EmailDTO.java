package negraoipr.com.ms_user.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class EmailDTO {

    private UUID emailRequestId;
    private UUID userId;
    private String emailTo;
    private String subject;
    private String text;

}
