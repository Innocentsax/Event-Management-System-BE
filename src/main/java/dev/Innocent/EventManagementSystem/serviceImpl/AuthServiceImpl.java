package dev.Innocent.EventManagementSystem.serviceImpl;


import dev.Innocent.EventManagementSystem.configuration.JwtService;
import dev.Innocent.EventManagementSystem.dto.AuthenticationResponse;
import dev.Innocent.EventManagementSystem.dto.LoginDto;
import dev.Innocent.EventManagementSystem.dto.UserDto;
import dev.Innocent.EventManagementSystem.dto.UserResponseDto;
import dev.Innocent.EventManagementSystem.exception.BadRequestException;
import dev.Innocent.EventManagementSystem.exception.InvalidTokenException;
import dev.Innocent.EventManagementSystem.exception.ResourceNotFoundException;
import dev.Innocent.EventManagementSystem.exception.UserNotFoundException;
import dev.Innocent.EventManagementSystem.mail.EmailService2;
import dev.Innocent.EventManagementSystem.model.EmailVerification;
import dev.Innocent.EventManagementSystem.model.PasswordReset;
import dev.Innocent.EventManagementSystem.model.Token;
import dev.Innocent.EventManagementSystem.model.User;
import dev.Innocent.EventManagementSystem.repository.EmailVerificationRepository;
import dev.Innocent.EventManagementSystem.repository.PasswordResetRepository;
import dev.Innocent.EventManagementSystem.repository.TokenRepository;
import dev.Innocent.EventManagementSystem.repository.UserRepository;
import dev.Innocent.EventManagementSystem.service.AuthService;
import dev.Innocent.EventManagementSystem.validation.EmailValidator;
import dev.Innocent.EventManagementSystem.validation.PasswordValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final HttpServletRequest request;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailServiceImpl emailService;
    private final JavaMailSender sender;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetRepository passwordResetRepository;
    private final EmailService2 emailService2;
    private static String activeProfile;
    @Value("${spring.profiles.active:}")
    public void setActiveProfile(String activeProfileValue) {
        activeProfile = activeProfileValue;
    }



    @Override
    public ResponseEntity<UserResponseDto> signUp(UserDto userDto) {
        String protocol = "";


        if (!EmailValidator.isValid(userDto.getUsername())) {
            throw new BadRequestException("Invalid email format", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByUsernameIgnoreCase(userDto.getUsername()).isPresent()) {
            throw new BadRequestException("User already exists", HttpStatus.BAD_REQUEST);
        }

        if (!PasswordValidator.isValid(userDto.getPassword())) {
            throw new BadRequestException("Invalid password format", HttpStatus.CREATED);

        }

        User user = map2Entity(userDto);
       user.setPassword(passwordEncoder.encode(user.getPassword()));
        String verificationToken = UUID.randomUUID().toString();

        //localhost:8090/api/v1/auth/register/verify?token=1320e202-98ba-4b3d-987e-94abfa2c5a75

        String url = request.getServerName()+":"+request.getServerPort()+request.getContextPath()
                    +"/api/v1/auth/register/verify?token="+verificationToken;
        user.setRole(userDto.getRole());
        User user1 = userRepository.save(user);
        EmailVerification verification = new EmailVerification();
        verification.setUser(user1);
        verification.setToken(verificationToken);
        verification.calculateExpirationTime();
        emailVerificationRepository.save(verification);
        //Remove tag cause simple mail message doesn't support tags.
        if(activeProfile.equals("prod")) {
            protocol = "https://";
        }else {
            protocol = "http://";
        }
        String message = "<a href=\"" +protocol+ url + "\"><h4>Click this to verify your email</h4></a>";

    //localhost:8090/api/v1/auth/register/verify?token=9341f3b3-c0de-4d2d-9a27-a6265f5e5835

//       emailService.sendEmail(user.getUsername(), EmailServiceImpl.REG_SUBJECT, message);
        emailService2.sendMail(user.getUsername() ,EmailServiceImpl.REG_SUBJECT, message);
       UserResponseDto response = UserResponseDto.builder()
               .message("User created successfully")
               .httpStatus(HttpStatus.CREATED)
               .firstName(user1.getFirstName())
               .email(user1.getUsername())
               .id(user1.getId())
               .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public UserDto map2UserDTO(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setDOB(userDto.getDOB());
        return userDto;
    }

    public static User map2Entity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(userDto.getPassword());
        user.setDOB(userDto.getDOB());
        return user;
    }
    @Override
    public AuthenticationResponse loginUser(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        User users1 = userRepository.findByUsernameIgnoreCase(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginDto.getUsername()));
        String jwtToken = jwtService.generateToken(users1);
        revokeAllToken(users1);

        saveUserToken(users1, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void revokeAllToken(User user) {
        List<Token> tokenList = tokenRepository.findAllValidTokenByUser(user.getId());
        if (tokenList.isEmpty()) {
            return;
        }
        for (Token token : tokenList) {
            token.setRevoked(true);
            token.setExpired(true);
            tokenRepository.saveAll(tokenList);
        }
    }

    private void saveUserToken(User savedUser, String jwtToken) {
        Token token = Token.builder()
                .token(jwtToken)
                .users(savedUser)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);

    }
    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails= (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            User user = userRepository.findByUsernameIgnoreCase(email).get();
            System.out.println(user);
            revokeAllToken(user);
        }
    }

    public ResponseEntity<String> verifyEmail(String verificationToken) {
        EmailVerification verification = emailVerificationRepository.findByToken(verificationToken);

        if (verification == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification token not found");
        }

        if (isTokenExpired(verification)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification token has expired");
        }

        User user = verification.getUser();
        user.setVerified(true);
        userRepository.save(user);

        verification.setVerified(true);
        emailVerificationRepository.save(verification);

        return ResponseEntity.ok("Email verified successfully");
    }

    private boolean isTokenExpired(EmailVerification verification) {
        Date expirationTime = verification.getExpirationTime();
        return expirationTime != null && expirationTime.before(new Date());
    }

    @Override
    public void initiatePasswordReset(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with this email: " + username));
        PasswordReset passwordReset = new PasswordReset(user);
        String resetToken = passwordReset.generateToken();
        passwordReset.setToken(resetToken);
        passwordResetRepository.save(passwordReset);
        sendPasswordResetEmail(user.getUsername(), resetToken);
    }

    private void sendPasswordResetEmail(String email, String resetToken) {
        String resetPasswordLink = "http://localhost:8080/reset-password?token=" + resetToken;
        String subject = "Password Reset";
        String content = "Click the link below to reset your password:\n" + resetPasswordLink;
        emailService2.sendMail(email, subject,  content);

//        emailService.sendEmail(email, subject, content);
    }
    public void verifyPasswordToken(String resetToken) {
        try {
            PasswordReset passwordReset = passwordResetRepository.findByToken(resetToken);

            if (passwordReset == null) {
                throw new ResourceNotFoundException("Resource not found");
            }
            if (isTokenValid(passwordReset.getExpirationTime(), resetToken)) {
                throw new InvalidTokenException("Invalid token");
            }
        } catch (ResourceNotFoundException | InvalidTokenException e) {
        }
    }

    public boolean isTokenValid(Date expiryDate, String token) {

        if(token.isEmpty()){
            throw new InvalidTokenException("Invalid token.");
        }
        if (expiryDate.before(new Date())) {
            return true;
        }
        return false;
    }
}