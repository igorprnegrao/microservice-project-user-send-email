package negraoipr.com.ms_user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import negraoipr.com.ms_user.dtos.UserRecordDTO;
import negraoipr.com.ms_user.dtos.UserCreatedDTO;
import negraoipr.com.ms_user.dtos.EmailRequestStatusDTO;
import negraoipr.com.ms_user.models.UserModel;
import negraoipr.com.ms_user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @PostMapping
    public ResponseEntity<UserCreatedDTO> saveUser(@RequestBody @Valid UserRecordDTO dto){
        var UserModel = new UserModel();
        BeanUtils.copyProperties(dto,UserModel);
        var userSaved = userService.save(UserModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> findById(@PathVariable("userId") UUID userId){
        var findUser = userService.findById(userId);
        return ResponseEntity.ok(findUser);
    }

    @GetMapping("/{userId}/email-requests/{emailRequestId}")
    public ResponseEntity<EmailRequestStatusDTO> findEmailRequest(
            @PathVariable("userId") UUID userId,
            @PathVariable("emailRequestId") UUID emailRequestId) {

        var buscaStatus = userService.findEmailRequest(userId, emailRequestId);

        return ResponseEntity.ok(buscaStatus);
    }

}
