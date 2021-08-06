package com.prime.rush_hour.services;

import com.prime.rush_hour.dtos.RolePutDto;
import com.prime.rush_hour.dtos.UserGetDto;
import com.prime.rush_hour.dtos.UserPostDto;
import com.prime.rush_hour.dtos.UserPutDto;
import com.prime.rush_hour.entities.Role;
import com.prime.rush_hour.exception_handling.AdminCannotBeDeletedException;
import com.prime.rush_hour.exception_handling.EmailExistsException;
import com.prime.rush_hour.exception_handling.UserNotFoundException;
import com.prime.rush_hour.entities.User;
import com.prime.rush_hour.mapstruct.mappers.UserMapper;
import com.prime.rush_hour.repositories.RoleRepository;
import com.prime.rush_hour.repositories.UserRepository;
import com.prime.rush_hour.security.authorization.ApplicationUserRole;
import com.prime.rush_hour.security.configuration.InitialAdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private InitialAdminConfig initialAdminConfig;

    @Override
    public List<UserGetDto> get() {
        return userMapper.usersToUserGetDtos(userRepository.findAll());
    }

    @Override
    public UserGetDto get(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return userMapper.userToUserGetDto(user);
    }

    @Override
    public UserGetDto create(UserPostDto userPostDto, List<ApplicationUserRole> roleTypes) {
        if(userRepository.existsByEmail(userPostDto.getEmail())) throw new EmailExistsException(userPostDto.getEmail());

        User user = userMapper.userPostDtoToUser(userPostDto);
        addRoles(user, roleTypes);
        encodePassword(user);
        userRepository.save(user);
        return userMapper.userToUserGetDto(user);
    }

    @Override
    public UserGetDto update(String email, UserPutDto userPutDto){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        if(userPutDto.getEmail() != null && userRepository.existsByEmail(userPutDto.getEmail())) throw new EmailExistsException(userPutDto.getEmail());

        userMapper.update(userPutDto, user);
        if(userPutDto.getPassword() != null) encodePassword(user);
        userRepository.save(user);
        return userMapper.userToUserGetDto(user);
    }

    @Override
    public void updateRoles(String email, List<RolePutDto> rolePutDtos) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        user.getRoles().clear();
        List<ApplicationUserRole> roleTypes = rolePutDtos.stream().map(rolePutDto -> rolePutDto.getName()).collect(Collectors.toList());
        addRoles(user, roleTypes);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(String email) {
        if(email.equals(initialAdminConfig.getEmail())) throw new AdminCannotBeDeletedException();

        if(!userRepository.existsByEmail(email)) throw new UserNotFoundException(email);
        userRepository.deleteByEmail(email);
    }

    private void encodePassword(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private void addRoles(User user, List<ApplicationUserRole> roleTypes){
        for (ApplicationUserRole roleType : roleTypes) {
            Role role = roleRepository.findByName(roleType).orElseThrow(() -> new IllegalArgumentException());
            user.addRole(role);
        }
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setInitialAdminConfig(InitialAdminConfig initialAdminConfig) {
        this.initialAdminConfig = initialAdminConfig;
    }
}
