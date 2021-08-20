package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.entities.Role;
import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.exception_handling.EmailExistsException;
import com.prime.rush_hour.exception_handling.UserNotFoundException;
import com.prime.rush_hour.mapstruct.mappers.UserMapperImpl;
import com.prime.rush_hour.repositories.RoleRepository;
import com.prime.rush_hour.repositories.UserRepository;
import com.prime.rush_hour.security.authorization.ApplicationUserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceLayerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Spy
    private UserMapperImpl userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void canGetAllUsers(){
        List<User> predefinedList = new ArrayList<>();

        User u1 = new User(1, "Nikola", "Bulat", "bulatn@gmail.com", "123");
        User u2 = new User(2, "Mirjana", "Bulat", "bulatz@gmail.com", "123");
        User u3 = new User(3, "Zdravko", "Bulat", "bulatm@gmail.com", "123");

        predefinedList.add(u1);
        predefinedList.add(u2);
        predefinedList.add(u3);

        when(userRepository.findAll()).thenReturn(predefinedList);

        List<UserGetDto> fetchedList = userService.get();
        assertThat(fetchedList).usingRecursiveComparison().isEqualTo(predefinedList);
    }

    @Test
    void canGetUserByEmail(){
        User predefinedUser = new User(1, "Nikola", "Bulat", "bulatn@gmail.com", "123");

        when(userRepository.findByEmail(predefinedUser.getEmail())).thenReturn(Optional.of(predefinedUser));

        UserGetDto fetchedUser = userService.get(predefinedUser.getEmail());
        assertThat(fetchedUser).usingRecursiveComparison().isEqualTo(predefinedUser);
    }
    //TODO: VIDI ZA OVO! (I U ACTIVITIJU)
    @Test
    void willThrowWhenUserDoesntExist(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.get("bulatn@gmail.com")).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void canAddUser(){
        String password = "123";
        UserPostDto user = new UserPostDto(1, "Nikola", "Bulat", "bulatn@gmail.com", password);
        //List<ApplicationUserRole> roleTypes = List.of(ApplicationUserRole.USER);

        //when(roleRepository.findByName(ApplicationUserRole.USER)).thenReturn(Optional.of(new Role(ApplicationUserRole.USER)));


        //TODO: Napravi da bude opstije
        when(passwordEncoder.encode(password)).thenReturn(password);
        userService.create(user, null);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(userMapper.userPostDtoToUser(user)).usingRecursiveComparison().isEqualTo(capturedUser);
    }

    @Test
    void willThrowWhenUserAlreadyExists(){
        UserPostDto user = new UserPostDto(1, "Nikola", "Bulat", "bulatn@gmail.com", "123");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(user, null)).isInstanceOf(EmailExistsException.class);
    }



}
