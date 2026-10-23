package negraoipr.com.ms_user.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_email_requests")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmailRequestModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID emailRequestId;

    private UUID userId;

    private String status;

    private UUID processedEventId;

}
