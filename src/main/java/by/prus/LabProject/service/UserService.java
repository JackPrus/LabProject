package by.prus.LabProject.service;

import by.prus.LabProject.model.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);
    UserDto getUser (String email);
    UserDto getUserByUserId (String userId);
    UserDto updateUser (String userId, UserDto user);
    void deleteUser(String userId);
    List<UserDto> getUsers(int page, int limit);
    boolean verifyEmailToken(String token);
    boolean requestPasswordReset(String email);
    boolean resetPassword(String token, String password);

}
