package br.com.alldirect.luan.todolist.user;

// REMOVER esta linha (não use User do H2):
// import org.h2.engine.User;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping({"", "/"})
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUserName(userModel.getUserName());

        if (user != null ){
            System.err.println("usuario ja existe");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usuário já existe");
        }

       var passwordHashred =  BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());


        userModel.setPassword(passwordHashred);
        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
