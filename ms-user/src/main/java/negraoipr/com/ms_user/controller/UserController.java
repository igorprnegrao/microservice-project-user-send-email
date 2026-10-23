package negraoipr.com.ms_user.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import negraoipr.com.ms_user.dtos.UserRecordDTO;
import negraoipr.com.ms_user.models.UserModel;
import negraoipr.com.ms_user.producers.UserProducer;
import negraoipr.com.ms_user.service.UserService;
import org.apache.catalina.mbeans.MBeanUtils;
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
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDTO dto){
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

}
