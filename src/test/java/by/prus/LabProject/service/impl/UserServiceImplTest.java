package by.prus.LabProject.service.impl;

import by.prus.LabProject.exception.UserServiceException;
import by.prus.LabProject.model.Role;
import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.UserDto;
import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.RoleEntity;
import by.prus.LabProject.model.entity.UserEntity;
import by.prus.LabProject.repository.RoleRepository;
import by.prus.LabProject.repository.UserRepository;
import by.prus.LabProject.service.Utils;
import by.prus.LabProject.service.mail.YandexSES;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks  // эта аннотация позволяет мокнуть объект содержащий поля @Autowired
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    Utils utils;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    YandexSES yandexSES;

    UserEntity userEntity;
    final String USER_ID = "12344321";
    final String ENCRYPTED_PASSWORD = "12344321";
    final String EMAIL = "test@test.com";
    final String EMAIL_VERIFICATION_TOKEN = "qwerrewq";
    final String USERDTO_PASSWORD = "1234";


    @BeforeEach
    void setUp() {
        // без этого не выйдет инициализация
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        //userEntity.setId(1L);
        userEntity.setUserId(USER_ID);
        userEntity.setEncryptedPassword(ENCRYPTED_PASSWORD);
        userEntity.setEmail(EMAIL); // обязательно должен быть верифицирован в амазоне
        userEntity.setEmailVerificationToken(EMAIL_VERIFICATION_TOKEN);
        userEntity.setEmailVerificationStatus(false);
        userEntity.setCertificatesOfUser(getCertificates());

    }

    @AfterEach
    void tearDown() {
    }



    @Test
    void getUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDto userDto = userService.getUser("test@test.com");
        assertNotNull(userDto);
        assertEquals(EMAIL,userDto.getEmail());
        assertEquals(USER_ID,userDto.getUserId());
        assertEquals(ENCRYPTED_PASSWORD,userDto.getEncryptedPassword());
        assertEquals(EMAIL_VERIFICATION_TOKEN,userDto.getEmailVerificationToken());
        assertFalse(userDto.getEmailVerificationStatus());
        assertEquals(2, userDto.getCertificates().size());
    }

    private Set<GiftCertificateEntity> getCertificates(){
        Set<GiftCertificateEntity> returnCollection = new HashSet<>();

        for (int i=0; i<2; i++){
            GiftCertificateEntity cert = new GiftCertificateEntity();
            cert.setName("testCert"+i);
            cert.setDescription("description of cert1");
            cert.setDuration(4);
            cert.setPrice(new BigDecimal("12.50"));
            cert.setCreateDate(LocalDate.now());
            cert.setLastUpdateDate(LocalDate.now());
            returnCollection.add(cert);
        }
        return returnCollection;
    }

    @Test()
    void userFoundException(){
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(
                UsernameNotFoundException.class,
                ()->{ userService.getUser("jeakkeyj@gmail.com"); }
                );
    }

    @Test()
    void createUserException(){
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setUserId(USER_ID);
        userDto.setEncryptedPassword(ENCRYPTED_PASSWORD);
        userDto.setEmail(EMAIL);
        userDto.setEmailVerificationToken(EMAIL_VERIFICATION_TOKEN);
        userDto.setEmailVerificationStatus(false);

        assertThrows(
                UserServiceException.class,
                ()->{ userService.createUser(userDto); }
        );
    }

    @Test
    final void testCreateUserMethod(){

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateUserId(anyInt())).thenReturn(USER_ID);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(ENCRYPTED_PASSWORD);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(roleRepository.findByName(anyString())).thenReturn(new RoleEntity());
        // ускорит время теста благодаря аutowired yandexSES
        Mockito.doNothing().when(yandexSES).verifyEmail(any(UserDto.class));

        ModelMapper modelMapper = new ModelMapper();

        Set<GiftCertificateEntity> certificates = getCertificates();
        List<GiftCertificateDTO> certificatesDto = new ArrayList<>();
        for (GiftCertificateEntity certEnt : certificates){
            certificatesDto.add(modelMapper.map(certEnt,GiftCertificateDTO.class));
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(USER_ID);
        userDto.setPassword(USERDTO_PASSWORD);
        userDto.setEmail(EMAIL);
        userDto.setEmailVerificationToken(EMAIL_VERIFICATION_TOKEN);
        userDto.setEmailVerificationStatus(false);
        userDto.setCertificates(certificatesDto);
        userDto.setRoles(new ArrayList<String>(Collections.singleton(Role.ROLE_USER.name())));

        UserDto storedUserDetails = userService.createUser(userDto);

        assertNotNull(storedUserDetails);

        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getEmail(), userEntity.getEmail());
        assertEquals(storedUserDetails.getCertificates().size(), userEntity.getCertificatesOfUser().size());
        assertEquals(storedUserDetails.getEncryptedPassword(), userEntity.getEncryptedPassword());
      /*  обратить внимание на 5. Это число знаков, которое мы задаем для кодирования в основном методе.
         этот же метод проверяет действительно ли на такое количество знаков закодировали и сколько раз вызывался
         метод generateUserId
       */
        verify(utils, times(1)).generateUserId(5);
        //пароль это мы уже устанавливали. обратить внимание.
        verify(bCryptPasswordEncoder, times(1)).encode(USERDTO_PASSWORD);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }



}