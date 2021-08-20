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
        UserPostDto userWithoutRole = new UserPostDto(1, "Nikola", "Bulat", "bulatn@gmail.com", password);
        List<ApplicationUserRole> roleTypes = List.of(ApplicationUserRole.USER);

        when(roleRepository.findByName(ApplicationUserRole.USER)).thenReturn(Optional.of(new Role(ApplicationUserRole.USER)));

        //TODO: Napravi da bude opstije
        when(passwordEncoder.encode(password)).thenReturn(password);
        userService.create(userWithoutRole, roleTypes);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        User userWithRole = userMapper.userPostDtoToUser(userWithoutRole);
        userWithRole.addRole(new Role(ApplicationUserRole.USER));

        assertThat(userWithRole).usingRecursiveComparison().isEqualTo(capturedUser);
    }


    @Test
    void willThrowWhenUserAlreadyExists(){
        UserPostDto user = new UserPostDto(1, "Nikola", "Bulat", "bulatn@gmail.com", "123");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(user, null)).isInstanceOf(EmailExistsException.class);
    }

    @Test
    void canUpdateAllTheFieldsOfUserWithoutRoles(){
        String password = "123";
        User existingUser = new User(1, "Nikola", "Bulat", "bulatn@gmail.com","random");
        UserPutDto newUser = new UserPutDto("Kevin", "Durant", "bulatn@gmail.com", password, null);

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(initialAdminConfig.getEmail()).thenReturn("");

        UserGetDto updatedUser = userService.update(newUser);

        assertThat(existingUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(userMapper.userToUserGetDto(existingUser)).usingRecursiveComparison().isEqualTo(updatedUser);
    }

    @Test
    void canUpdateSomeFieldsOfUserIncludingPasswordWithoutRoles(){
        String password = "123";
        User existingUser = new User(1, "Nikola", "Bulat", "bulatn@gmail.com","random");
        UserPutDto newUser = new UserPutDto(null, null, "bulatn@gmail.com", password, null);

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(initialAdminConfig.getEmail()).thenReturn("");

        UserGetDto updatedUser = userService.update(newUser);

        assertThat(existingUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(userMapper.userToUserGetDto(existingUser)).usingRecursiveComparison().isEqualTo(updatedUser);
    }

    @Test
    void canUpdateSomeFieldsOfUserExcludingPasswordWithoutRoles(){
        User existingUser = new User(1, "Nikola", "Bulat", "bulatn@gmail.com","random");
        UserPutDto newUser = new UserPutDto("Kevin", null, "bulatn@gmail.com", null, null);

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(initialAdminConfig.getEmail()).thenReturn("");

        UserGetDto updatedUser = userService.update(newUser);

        assertThat(userMapper.userToUserGetDto(existingUser)).usingRecursiveComparison().isEqualTo(updatedUser);
    }

    @Test
    void canUpdateUserWithRoles(){
        String password = "123";
        User existingUser = new User(1, "Nikola", "Bulat", "bulatn@gmail.com","random");
        existingUser.addRole(new Role(ApplicationUserRole.USER));
        UserPutDto newUser = new UserPutDto("Kevin", "Durant", "bulatn@gmail.com", password, List.of(ApplicationUserRole.ADMIN));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(initialAdminConfig.getEmail()).thenReturn("");
        when(roleRepository.findByName(ApplicationUserRole.ADMIN)).thenReturn(Optional.of(new Role(ApplicationUserRole.ADMIN)));

        UserGetDto updatedUser = userService.update(newUser);

        assertThat(existingUser.getRoles().stream().anyMatch(r -> r.getName().equals(ApplicationUserRole.ADMIN))).isTrue();
        assertThat(existingUser.getRoles().stream().anyMatch(r -> r.getName().equals(ApplicationUserRole.USER))).isFalse();
        assertThat(existingUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(userMapper.userToUserGetDto(existingUser)).usingRecursiveComparison().isEqualTo(updatedUser);
    }

    @Test
    void willThrowWhenTryingToUpdateInitialAdmin(){
        String email = "bulatn@gmail.com";
        UserPutDto newUser = new UserPutDto("Kevin", "Durant", email, "123", null);

        when(initialAdminConfig.getEmail()).thenReturn(email);

        assertThatThrownBy(() -> userService.update(newUser)).isInstanceOf(AdminCannotBeModifiedException.class);
    }

    @Test
    void canDeleteByEmail(){
        String email = "bulatn@gmail.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        userService.delete(email);

        verify(userRepository, times(1)).deleteByEmail(email);
    }

    @Test
    void willThrowWhenTryingToDeleteInitialAdmin(){
        String email = "bulatn@gmail.com";

        when(initialAdminConfig.getEmail()).thenReturn(email);

        assertThatThrownBy(() -> userService.delete(email)).isInstanceOf(AdminCannotBeModifiedException.class);
    }

//    @Test
//    void canAddRoles(){
//        User userWithoutRoles = new User(1, "Nikola", "Bulat", "bulatn@gmail.com", "123");
//        User userWithRoles = new User(1, "Nikola", "Bulat", "bulatn@gmail.com", "123");
//        userWithRoles.addRole(new Role(ApplicationUserRole.USER));
//        userWithRoles.addRole(new Role(ApplicationUserRole.ADMIN));
//
//        userService.ad
//    }
}
