package negraoipr.com.ms_user.repositories;

import negraoipr.com.ms_user.models.EmailRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmailRequestRepository extends JpaRepository<EmailRequestModel, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select request from EmailRequestModel request where request.emailRequestId = :id")
    Optional<EmailRequestModel> findForUpdate(@Param("id") UUID id);
}
