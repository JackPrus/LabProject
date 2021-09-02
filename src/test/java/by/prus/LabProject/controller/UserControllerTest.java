package by.prus.LabProject.controller;

import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.UserDto;
import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.TagEntity;
import by.prus.LabProject.model.entity.supporting.CertificateTag;
import by.prus.LabProject.model.response.GiftCertificateResponse;
import by.prus.LabProject.model.response.UserResponse;
import by.prus.LabProject.service.impl.UserServiceImpl;
import by.prus.LabProject.service.relgenerator.LinkCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {



    @InjectMocks // эта аннотация позволяет мокнуть объект содержащий поля @Autowired
    UserController userController;

    @Mock
    UserServiceImpl userService; //? почему не injectMock
    @Mock
    LinkCreator linkCreator;

    UserDto userDto;
    final String USER_ID = "reqwrewqrewq";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userDto = new UserDto();
        userDto.setEmail("jeakkeyj@gmail.com");
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        userDto.setCertificates(getCertificateResponseList()); //2 штуки
        userDto.setEncryptedPassword("qwerqwerrewqreqw");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUser() {
        // когда мы вызываем метод getUserById тогда мы возвращаем userDto
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        UserResponse userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertTrue(userDto.getCertificates().size() == userRest.getCertificates().size());
        assertEquals(userDto.getEmail(), userRest.getEmail());
    }

    private List<GiftCertificateDTO> getCertificateResponseList() {//2 certResponse
        List<GiftCertificateDTO> returnCollection = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for (int i=0; i<2; i++){
            GiftCertificateEntity certEntity = getCertificate();
            GiftCertificateDTO certResponse = modelMapper.map(certEntity, GiftCertificateDTO.class);
            certResponse.setCertificateTags(getCertificateTag());
            returnCollection.add(certResponse);
        }
        return returnCollection;
    }

    private List<CertificateTag> getCertificateTag() { // возвращает 3 элемента.

        List<CertificateTag> listOfCertTags = new ArrayList<>();

        for (int i=0 ; i<2; i++){
            CertificateTag certificateTag = new CertificateTag();
            listOfCertTags.add(certificateTag);
        }

        return listOfCertTags;
    }

    private GiftCertificateEntity getCertificate (){
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("testCert1");
        cert.setDescription("description of cert1");
        cert.setDuration(4);
        cert.setPrice(new BigDecimal("12.50"));
        cert.setCreateDate(LocalDate.now());
        cert.setLastUpdateDate(LocalDate.now());
        return cert;
    }

    private TagEntity getTag (){
        TagEntity tag = new TagEntity();
        tag.setName("testag1");
        return tag;
    }

    //example how 'thenReturn' works like
    @Test
    public void iterator_will_return_hello_world() {
        //подготавливаем
        Iterator i = mock(Iterator.class);
        when(i.next()).thenReturn("Hello").thenReturn("World");
        //выполняем
        String result = i.next()+" "+i.next();
        //сравниваем
        assertEquals("Hello World", result);
    }

}