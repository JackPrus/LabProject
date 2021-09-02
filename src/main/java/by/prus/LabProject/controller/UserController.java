package by.prus.LabProject.controller;

import by.prus.LabProject.exception.UserServiceException;
import by.prus.LabProject.model.Role;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.UserDto;
import by.prus.LabProject.model.request.PasswordResetModel;
import by.prus.LabProject.model.request.PasswordResetRequestModel;
import by.prus.LabProject.model.request.RequestOperationName;
import by.prus.LabProject.model.request.UserRequest;
import by.prus.LabProject.model.response.*;
import by.prus.LabProject.service.GiftCertificateService;
import by.prus.LabProject.service.UserService;
import by.prus.LabProject.service.relgenerator.LinkCreator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Dzmitry Prus.
 * @version 1.0
 * The methods of class implement CRUD and others operations with tag.
 * Returns list of users, list of certificates of some exact user, email verification and password reset
 */

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
    @Autowired
    ModelMapper modelMapper;

    @PostAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @GetMapping(
            path = "/{userId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public UserResponse getUser(@PathVariable String userId){
        UserDto userDto = userService.getUserByUserId(userId);
        UserResponse returnValue = modelMapper.map(userDto, UserResponse.class);
        linkCreator.addLinkToUserResponse(returnValue);
        return returnValue;
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public UserResponse createUser(@RequestBody UserRequest userRequest) throws UserServiceException { // конвертирует Java объект в JSON файл

        if (userRequest.getEmail().isEmpty()) {throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());}
        UserDto userDto = modelMapper.map(userRequest, UserDto.class); // копируются поля из одного класса в одноименные поля другого.
        userDto.setRoles(new HashSet<>(Arrays.asList(Role.ROLE_USER.name())));
        UserDto createdUser = userService.createUser(userDto); // валидируем юзера и создаем юзера без пассворда
        UserResponse returnValue = modelMapper.map(createdUser, UserResponse.class);

        return  returnValue;
    }

    // #id == principal.id говорит о том, что юзер может удалить сам себя но не другого юзера.
    // principal означает текущего пользователя, обращающегося к URL
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @DeleteMapping (
            path = "/{userId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public OperationStatusModel deleteUser(@PathVariable String userId){
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        returnValue.setOperationResult(ResponseOperationStatus.ERROR.name());

        userService.deleteUser(userId);

        returnValue.setOperationResult(ResponseOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @GetMapping(
            path = "/{userId}/certificates",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public List<GiftCertificateResponse> getCertificatesOfUser(@PathVariable String userId){
        UserDto userDto = userService.getUserByUserId(userId);
        List <GiftCertificateResponse> returnValue = new ArrayList<>();
        List<GiftCertificateDTO> certificatesDTO = userDto.getCertificates();

        for (GiftCertificateDTO certificateDTO : certificatesDTO){
            GiftCertificateResponse certResponse = modelMapper.map(certificateDTO, GiftCertificateResponse.class);
            linkCreator.addLinkToCertificateResponse(certResponse);
            returnValue.add(certResponse);
        }

        returnValue.sort(Comparator.comparing(GiftCertificateResponse::getName).thenComparing(GiftCertificateResponse::getCreateDate));
        return returnValue;
    }

    // http://localhost:8080/labproject/user/{userId}/{certificateId}
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
    @PutMapping(
            path = "/{userId}/{certificateId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public UserResponse addCertificateToUser(@PathVariable String userId, @PathVariable Long certificateId){
        UserDto userDto = userService.getUserByUserId(userId);
        GiftCertificateDTO certificateDTO = certificateService.getCertificate(certificateId);
        userDto.getCertificates().add(certificateDTO);
        UserDto updatedUser = userService.updateUser(userId, userDto);
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

    /**
     * @param token - token will be created and sent to user's email when user created
     * @return - result of operation of sending email ERROR or SUCCESS
     * when userController creates user cerate method ask this method to send email including verification token
     * when email-verificateion will be done in database column will be 1 that means user verified.
     */
    @GetMapping(
            path = "/email-verification",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE}
            )
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token){
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified){
            returnValue.setOperationResult(ResponseOperationStatus.SUCCESS.name());
        }else {
            returnValue.setOperationResult(ResponseOperationStatus.ERROR.name());
        }

        return returnValue;
    }


    /**
     *
     * @param page - number of pagge mapped in URL
     * @param limit - limit of Users objects represented in JSON or XML view per page.
     * @return - Collection of users in page quantity
     * Hateoas return links for every user. Ass well implemented presentation of links to next and previous page.
     */

    //http://localhost:8080/labproject/user?page=3&limit=2 (выведет количество значений в соответствии с defaultValue of limit)
    // ссылка на метод гет по каждому юзеру
    // ссылки на следующую и предыдущую страницу этого же метода.
    @GetMapping (produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}) // с пагинацией
    public CollectionModel<UserResponse> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page, //возвратит пустой лист , если поставить "1" defaltvalue
            @RequestParam(value = "limit", defaultValue = "25") int limit
    ){
        List<UserResponse> responseValue = new ArrayList<>();
        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users){
            UserResponse userResponse = modelMapper.map(userDto, UserResponse.class);
            linkCreator.addLinkToUserResponse(userResponse);
            responseValue.add(userResponse);
        }
        CollectionModel<UserResponse> returnValue = linkCreator.getNavigationUserLinks(responseValue, page,limit);
        return returnValue;
    }

    //http://localhost:8080/labproject/user/password-reset-request/


    @PostMapping(
            path = "/password-reset-request",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel requestReset(
            @RequestBody PasswordResetRequestModel passwordResetRequestModel){

        OperationStatusModel returnValue = new OperationStatusModel();
        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(ResponseOperationStatus.ERROR.name());

        if (operationResult){
            returnValue.setOperationResult(ResponseOperationStatus.SUCCESS.name());
        }
        return returnValue;
    }

    @PostMapping(
            path = "/password-reset",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.resetPassword(
                passwordResetModel.getToken(),
                passwordResetModel.getPassword());

        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(ResponseOperationStatus.ERROR.name());

        if(operationResult) {
            returnValue.setOperationResult(ResponseOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }

}
