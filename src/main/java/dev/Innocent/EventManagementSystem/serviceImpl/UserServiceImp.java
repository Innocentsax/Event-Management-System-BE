package dev.Innocent.EventManagementSystem.serviceImpl;


import dev.Innocent.EventManagementSystem.dto.PasswordRequestDto;
import dev.Innocent.EventManagementSystem.exception.InvalidPasswordException;
import dev.Innocent.EventManagementSystem.exception.ResourceNotFoundException;
import dev.Innocent.EventManagementSystem.exception.UserNotFoundException;
import dev.Innocent.EventManagementSystem.model.EmailVerification;
import dev.Innocent.EventManagementSystem.model.User;
import dev.Innocent.EventManagementSystem.repository.EmailVerificationRepository;
import dev.Innocent.EventManagementSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;



    @Transactional
    public void updatePassword(PasswordRequestDto passwordRequestDto) throws UserNotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(passwordRequestDto.getUsername());

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = optionalUser.get();


        if (!passwordEncoder.matches(passwordRequestDto.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid old password");
        }

        String newPasswordEncoded = passwordEncoder.encode(passwordRequestDto.getNewPassword());
        user.setPassword(newPasswordEncoded);

        user.setPassword(passwordRequestDto.getNewPassword());


        userRepository.save(user);
    }

    @Transactional
    public ResponseEntity<String> resetPassword(String token, PasswordRequestDto passwordRequestDto) throws ResourceNotFoundException {
        EmailVerification verification = emailVerificationRepository.findByToken(token);
        if (verification == null){
            throw new ResourceNotFoundException("invalid reset password token");
        }



        User user = verification.getUser();

        if (passwordRequestDto.getNewPassword().equals(passwordRequestDto.getConfirmPassword())) {
            user.setPassword(passwordRequestDto.getConfirmPassword());
            userRepository.save(user);
        }

        return new ResponseEntity<>("Password Reset Successfully", HttpStatus.OK);
    }


}
