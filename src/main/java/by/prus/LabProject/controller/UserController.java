package by.prus.LabProject.controller;

import by.prus.LabProject.exception.UserServiceException;
import by.prus.LabProject.model.Role;
import by.prus.LabProject.model.dto.UserDto;
import by.prus.LabProject.model.request.UserRequest;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.model.response.OperationStatusModel;
import by.prus.LabProject.model.response.UserResponse;
import by.prus.LabProject.service.GiftCertificateService;
import by.prus.LabProject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE}
    )
    //produces возвращает XML и JSON представление. По дефолту XML т.к. стоит первым тут
    public UserResponse getUser(@PathVariable String id){

        UserResponse returnValue = new UserResponse();

        UserDto userDto = userService.getUserByUserId(id);
        // если будет копипропертис вместо маппера - то не сработает лист и тест не сработает
        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(userDto, UserResponse.class);

        return returnValue;

    }


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


    // http://localhost:8080/labproject/user/email-verification?token=sdfsdf
    /*
    При выполеннии пост запроса на создание юзера в базе данных появляется
    верификейшн токен.
    При выполнении гет запроса выше, где в качестве токена будет указан этот токен
    Выполнится верификация. И в базе данных верификейшнтокен исчезнет, а на месте
    email-verification-status будет 1, что соответствует верифицированному пользователю.
     */
    @GetMapping(path = "/email-verification", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token){
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName("verify email");
        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified){
            returnValue.setOperationResult("verifyed successfully");
        }else {
            returnValue.setOperationResult("email verification error");
        }

        return returnValue;
    }

}
