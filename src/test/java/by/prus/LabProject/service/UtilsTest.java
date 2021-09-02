package by.prus.LabProject.service;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest()
class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateUserId() {
        String userId = utils.generateUserId(5);
        String userId2 = utils.generateUserId(5);

        assertNotNull(userId);
        assertNotNull(userId2);

        assertTrue(userId.length()==5);
        assertFalse(userId.equalsIgnoreCase(userId2));
    }

    @Test
    //@Disabled // не запустится
    void hasTokenNotExpired() {
        String token = utils.generateEmailVerificationToken("qwerrewq");
        assertNotNull(token);
        boolean hasTokenExpired = Utils.hasTokenExpired(token);
        assertFalse(hasTokenExpired);
    }

    @Test
    void hasTokenExpired(){

        boolean returnValue = true;

        String epiredToken = utils.generateEmailVerificationToken("qwerrewq");
        boolean hasTokenExpired = Utils.hasTokenExpired(epiredToken);
        assertFalse(hasTokenExpired);

    }
}