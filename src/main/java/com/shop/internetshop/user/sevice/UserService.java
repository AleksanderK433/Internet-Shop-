package com.shop.internetshop.user.sevice;

import com.shop.internetshop.user.dto.UserRegistrationDto;
import com.shop.internetshop.user.dto.UserResponseDto;
import com.shop.internetshop.user.exception.InvalidPasswordException;
import com.shop.internetshop.user.exception.UserAlreadyExistsException;
import com.shop.internetshop.user.exception.UserNotFoundException;
import com.shop.internetshop.user.model.User;
import com.shop.internetshop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Uzytkownik nie znaleziony"+username));
    }

    public UserResponseDto registerUser(UserRegistrationDto registrationDto){

        if (!registrationDto.password().equals(registrationDto.confirmPassword())) {
            throw new InvalidPasswordException("Hasła nie są identyczne");
        }

        if (userRepository.existsByUsername(registrationDto.username())){
            throw new UserAlreadyExistsException("Username już istnieje: " + registrationDto.username());
        }

        if (userRepository.existsByEmail(registrationDto.email())){
            throw new UserNotFoundException("Użytkownik nie znaleziony: " + registrationDto.email());
        }

        User user = new User(
                registrationDto.username(),
                registrationDto.email(),
                passwordEncoder.encode(registrationDto.password())
        );

        User savedUser = userRepository.save(user);

        return UserResponseDto.fromUser(savedUser);
    }

    public UserResponseDto findUserByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony: " + username));
        return UserResponseDto.fromUser(user);
    }

    public UserResponseDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony: " + email));
        return UserResponseDto.fromUser(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
