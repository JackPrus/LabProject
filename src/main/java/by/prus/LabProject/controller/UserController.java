package by.prus.LabProject.controller;

import by.prus.LabProject.exception.UserServiceException;
import by.prus.LabProject.model.Role;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.UserDto;
import by.prus.LabProject.model.request.UserRequest;
import by.prus.LabProject.model.response.ErrorMessages;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.model.response.OperationStatusModel;
import by.prus.LabProject.model.response.UserResponse;
import by.prus.LabProject.service.GiftCertificateService;
import by.prus.LabProject.service.UserService;
import by.prus.LabProject.service.relgenerator.LinkCreator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("user")
//http://localhost:8080/labproject/user/
public class UserController {

    @Autowired
    UserService userService; // чтобы поля не подчеркивало, надо добавить @Service над классом
    @Autowired
    GiftCertificateService certificateService;
    @Autowired
    LinkCreator linkCreator;

    @GetMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    //produces возвращает XML и JSON представление. По дефолту XML т.к. стоит первым тут
    public UserResponse getUser(@PathVariable String id){

        UserResponse returnValue = new UserResponse();

        UserDto userDto = userService.getUserByUserId(id);
        // если будет копипропертис вместо маппера - то не сработает лист и тест не сработает
        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(userDto, UserResponse.class);
        linkCreator.addLinkToUserResponse(returnValue);

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

    @GetMapping(
            path = "/{userId}/certificates",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )public List<GiftCertificateResponse> getCertificatesOfUser(@PathVariable String userId){
        UserDto userDto = userService.getUserByUserId(userId);
        List <GiftCertificateResponse> returnValue = new ArrayList<>();
        Set<GiftCertificateDTO> certificatesDTO = userDto.getCertificates();
        ModelMapper modelMapper = new ModelMapper();

        for (GiftCertificateDTO certificateDTO : certificatesDTO){
            GiftCertificateResponse certResponse = modelMapper.map(certificateDTO, GiftCertificateResponse.class);
            linkCreator.addLinkToCertificateResponse(certResponse);
            returnValue.add(certResponse);
        }

        returnValue.sort(Comparator.comparing(GiftCertificateResponse::getName).thenComparing(GiftCertificateResponse::getCreateDate));
        return returnValue;
    }

    // http://localhost:8080/labproject/user/{userId}/{certificateId}
    @PutMapping(
            path = "/{userId}/{certificateId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )public UserResponse addCertificateToUser(@PathVariable String userId, @PathVariable Long certificateId){
        UserDto userDto = userService.getUserByUserId(userId);
        GiftCertificateDTO certificateDTO = certificateService.getCertificate(certificateId);
        userDto.getCertificates().add(certificateDTO);
        UserDto updatedUser = userService.updateUser(userId, userDto);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(updatedUser, UserResponse.class);
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

    @DeleteMapping (
            path = "/{userId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public OperationStatusModel deleteUser(@PathVariable String userId){
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName("Delete of user");

        userService.deleteUser(userId);

        returnValue.setOperationResult("User deleted successfully");
        return returnValue;
    }

    //http://localhost:8080/labproject/user?page=3&limit=2 (выведет количество значений в соответствии с defaultValue of limit)
    // ссылка на метод гет по каждому юзеру
    // ссылки на следующую и предыдущую страницу этого же метода.
    @GetMapping (produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}) // с пагинацией
    public CollectionModel<UserResponse> getUsers(@RequestParam(value = "page", defaultValue = "0") int page, //возвратит пустой лист , если поставить "1" defaltvalue
                                                  @RequestParam(value = "limit", defaultValue = "25") int limit){

        List<UserResponse> responseValue = new ArrayList<>();
        List<UserDto> users = userService.getUsers(page, limit);
        ModelMapper modelMapper = new ModelMapper();

        for (UserDto userDto : users){
            UserResponse userResponse = modelMapper.map(userDto, UserResponse.class);
            linkCreator.addLinkToUserResponse(userResponse);
            responseValue.add(userResponse);
        }

        CollectionModel<UserResponse> returnValue = linkCreator.getNavigationUserLinks(responseValue, page,limit);

        return returnValue;
    }

}
