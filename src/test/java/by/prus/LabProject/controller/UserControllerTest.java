package by.prus.LabProject.controller;

import by.prus.LabProject.model.dto.GiftCertificateDTO;
import by.prus.LabProject.model.dto.UserDto;
import by.prus.LabProject.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {



    @InjectMocks // эта аннотация позволяет мокнуть объект содержащий поля @Autowired
    UserController userController;

    @Mock
    UserServiceImpl userService; //? почему не injectMock

    UserDto userDto;
    final String USER_ID = "qwer";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userDto = new UserDto();
        userDto.setEmail("jeakkeyj@gmail.com");
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        userDto.setAddresses(getAddressesDto());
        userDto.setEncryptedPassword("xcf58tugh47");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUser() {
        // когда мы вызываем метод getUserById тогда мы возвращаем userDto
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        UserRest userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
    }

    private List<GiftCertificateDTO> getAddressesDto() {
        GiftCertificateDTO certificate = new GiftCertificateDTO();
        certificate.setName("test sport certificate");
        certificate.setDescription("description of sport Certificate");
        certificate.setDuration(4);
        certificate.setCertificateTags();

        AddressDTO billingAddressDto = new AddressDTO();
        billingAddressDto.setType("billling");
        billingAddressDto.setCity("Vancouver");
        billingAddressDto.setCountry("Canada");
        billingAddressDto.setPostalCode("ABC123");
        billingAddressDto.setStreetName("123 Street name");

        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDto);
        addresses.add(billingAddressDto);

        return addresses;

    }

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