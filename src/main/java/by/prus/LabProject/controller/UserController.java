package by.prus.LabProject.controller;

import by.prus.LabProject.exception.UserServiceException;
import by.prus.LabProject.model.Role;
import by.prus.LabProject.model.dto.UserDto;
import by.prus.LabProject.model.request.UserRequest;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.model.response.UserResponse;
import by.prus.LabProject.service.GiftCertificateService;
import by.prus.LabProject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;

@RestController
@RequestMapping("user")
//http://localhost:8080/labproject/user/
public class UserController {

    @Autowired
    UserService userService; // чтобы поля не подчеркивало, надо добавить @Service над классом
    @Autowired
    GiftCertificateService certificateService;

    @PostMapping(
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public UserResponse createUser(@RequestBody UserRequest userRequest) throws UserServiceException { // конвертирует Java объект в JSON файл

        UserResponse returnValue = new UserResponse();

        if (userRequest.getEmail().isEmpty()) {throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());}

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userRequest, UserDto.class); // копируются поля из одного класса в одноименные поля другого.

        userDto.setRoles(new HashSet<>(Arrays.asList(Role.ROLE_USER.name())));

        UserDto createdUser = userService.createUser(userDto); // валидируем юзера и создаем юзера без пассворда
        returnValue = modelMapper.map(createdUser, UserResponse.class);

        return  returnValue;

    }

}
