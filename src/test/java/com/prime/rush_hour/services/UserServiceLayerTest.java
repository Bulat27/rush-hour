package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.dtos.UserPutDto;
import com.prime.rush_hour.entities.Role;
import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.exception_handling.AdminCannotBeModifiedException;
import com.prime.rush_hour.exception_handling.EmailExistsException;
import com.prime.rush_hour.exception_handling.UserNotFoundException;
import com.prime.rush_hour.mapstruct.mappers.UserMapperImpl;
import com.prime.rush_hour.repositories.RoleRepository;
import com.prime.rush_hour.repositories.UserRepository;
import com.prime.rush_hour.security.authorization.ApplicationUserRole;
import com.prime.rush_hour.security.configuration.InitialAdminConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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
    private PasswordEncoder passwordEncoder;

    @Mock
    private InitialAdminConfiguration initialAdminConfig;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private static UserPostDto userPostDto;
    private static final String PASSWORD = "123";
    private static final String EMAIL = "bulatn@gmail.com";

    @BeforeEach
    public void initBeforeEach(){
        user = new User(1, "Nikola", "Bulat", EMAIL, "123");
    }
    
    @BeforeAll
    public static void initBeforeAll(){
        userPostDto = new UserPostDto(1, "Nikola", "Bulat", EMAIL, PASSWORD);
    }

    @Test
    void getAllShouldReturnAllUsers(){
        List<User> predefinedList = new ArrayList<>();
        
        User u2 = new User(2, "Mirjana", "Bulat", "bulatz@gmail.com", "123");
        User u3 = new User(3, "Zdravko", "Bulat", "bulatm@gmail.com", "123");

        predefinedList.add(user);
        predefinedList.add(u2);
        predefinedList.add(u3);

        when(userRepository.findAll()).thenReturn(predefinedList);

        List<UserGetDto> fetchedList = userService.get();
        assertThat(fetchedList).usingRecursiveComparison().isEqualTo(predefinedList);
    }

    @Test
    void getByEmailShouldReturnUserByEmail(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserGetDto fetchedUser = userService.get(user.getEmail());
        assertThat(fetchedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void willThrowWhenUserDoesntExist(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.get(EMAIL);
        });
    }

    @Test
    void createUserShouldAddUser(){
        List<ApplicationUserRole> roleTypes = List.of(ApplicationUserRole.USER);

        when(roleRepository.findByName(ApplicationUserRole.USER)).thenReturn(Optional.of(new Role(ApplicationUserRole.USER)));

        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        userService.create(userPostDto, roleTypes);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        User userWithRole = userMapper.userPostDtoToUser(userPostDto);
        userWithRole.addRole(new Role(ApplicationUserRole.USER));

        assertThat(userWithRole).usingRecursiveComparison().isEqualTo(capturedUser);
    }


    @Test
    void willThrowWhenUserAlreadyExists(){
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        Assertions.assertThrows(EmailExistsException.class, () -> {
            userService.create(userPostDto, null);
        });
    }

    @Test
    void updateByNameShouldUpdateAllTheFieldsOfUser(){
        user.addRole(new Role(ApplicationUserRole.USER));
        UserPutDto newUser = new UserPutDto("Kevin", "Durant", EMAIL, PASSWORD, List.of(ApplicationUserRole.ADMIN));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userRepository.save(user)).thenReturn(user);
        when(initialAdminConfig.getEmail()).thenReturn("");
        when(roleRepository.findByName(ApplicationUserRole.ADMIN)).thenReturn(Optional.of(new Role(ApplicationUserRole.ADMIN)));

        UserGetDto updatedUser = userService.update(newUser);

        assertThat(user.getRoles().stream().anyMatch(r -> r.getName().equals(ApplicationUserRole.ADMIN))).isTrue();
        assertThat(user.getRoles().stream().anyMatch(r -> r.getName().equals(ApplicationUserRole.USER))).isFalse();
        assertThat(user.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(userMapper.userToUserGetDto(user)).usingRecursiveComparison().isEqualTo(updatedUser);
    }

    @Test
    void updateByNameShouldUpdateSomeFieldsOfUserIncludingPassword(){
        UserPutDto newUser = new UserPutDto(null, null, EMAIL, PASSWORD, null);

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(userRepository.save(user)).thenReturn(user);
        when(initialAdminConfig.getEmail()).thenReturn("");

        UserGetDto updatedUser = userService.update(newUser);

        assertThat(user.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(userMapper.userToUserGetDto(user)).usingRecursiveComparison().isEqualTo(updatedUser);
    }

    @Test
    void updateByNameShouldUpdateSomeFieldsOfUserExcludingPassword(){
        UserPutDto newUser = new UserPutDto("Kevin", null, EMAIL, null, null);

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(initialAdminConfig.getEmail()).thenReturn("");

        UserGetDto updatedUser = userService.update(newUser);

        assertThat(userMapper.userToUserGetDto(user)).usingRecursiveComparison().isEqualTo(updatedUser);
    }


    @Test
    void willThrowWhenTryingToUpdateInitialAdmin(){
        UserPutDto newUser = new UserPutDto("Kevin", "Durant", EMAIL, PASSWORD, null);

        when(initialAdminConfig.getEmail()).thenReturn(EMAIL);

        Assertions.assertThrows(AdminCannotBeModifiedException.class, () -> {
            userService.update(newUser);
        });
    }

    @Test
    void deleteByEmailShouldDeleteUser(){
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        userService.delete(EMAIL);

        verify(userRepository, times(1)).deleteByEmail(EMAIL);
    }

    @Test
    void willThrowWhenTryingToDeleteInitialAdmin(){
        when(initialAdminConfig.getEmail()).thenReturn(EMAIL);

        Assertions.assertThrows(AdminCannotBeModifiedException.class, () -> {
            userService.delete(EMAIL);
        });
    }
}
