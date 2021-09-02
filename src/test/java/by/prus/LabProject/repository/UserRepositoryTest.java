package by.prus.LabProject.repository;

import by.prus.LabProject.model.entity.GiftCertificateEntity;
import by.prus.LabProject.model.entity.UserEntity;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

//    Тест закомментирован поскольку затрагивает работу базы данных. Тест рабочий, для проверки необходимо раскомментировать

//    @Autowired
//    UserRepository userRepository;
//
//    final String USER_ID1 = "testUser1";
//    final String ENCRYPTED_PASSWORD1 = "qwer";
//    final String EMAIL1 = "test1@test.com";
//    final String EMAIL_VERIFICATION_TOKEN1 = "rewq";
//    final String USERDTO_PASSWORD1 = "1234";
//
//    final String USER_ID2 = "testUser2";
//    final String ENCRYPTED_PASSWORD2 = "qwer";
//    final String EMAIL2 = "test2@test.com";
//    final String EMAIL_VERIFICATION_TOKEN2 = "rewq";
//    final String USERDTO_PASSWORD2 = "1234";
//
//
//
//
//    @BeforeEach
//    void setUp() {
//        if (userRepository.findByUserId(USER_ID1)==null&&
//        userRepository.findByUserId(USER_ID2)==null){
//            createRecrods();
//        }
//    }
//
//
//    @AfterEach
//    void tearDown() {
//        deleteTestRecords();
//    }
//
//    @Test
//    final void testGetVerifiedUsers() {
//        Pageable pageableRequest = PageRequest.of(0, 2); //1 страница по 2 объекту на странице
//        Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
//        assertNotNull(pages);
//
//        List<UserEntity> userEntities = pages.getContent();
//        assertNotNull(userEntities);
//        assertEquals(2, userEntities.size());
//    }
//
//    @Test
//    final void findUserByEmail(){
//        String email=EMAIL1;
//        UserEntity userEntity = userRepository.findByEmail(email);
//        assertNotNull(userEntity);
//        assertEquals(userEntity.getEmail(), EMAIL1);
//    }
//
//    @Test
//    final void findByEmailPartAndReturnPage(){
//        // must return 2 parts becase of test1@test.com and test2@test.com
//        String emailPart = "test.com";
//        Pageable pageableRequest = PageRequest.of(0, 5); //1 страница по 5 объектов на странице
//        Page<UserEntity> pages = userRepository.findTagsByEmailPartAndReturnPage(emailPart, pageableRequest);
//
//        assertNotNull(pages);
//        List<UserEntity> userEntities = pages.getContent();
//        assertNotNull(userEntities);
//        assertEquals(2, userEntities.size());
//    }
//
//
//
//
//    @Test
//    final void testFindUserEntityByUserId() {
//        String userId = USER_ID1;
//        UserEntity userEntity = userRepository.findByUserId(userId);
//        assertNotNull(userEntity);
//        assertTrue(userEntity.getUserId().equals(userId));
//    }
//
//
//
//    private void createRecrods() {
//        // Prepare User Entity
//        UserEntity userEntity = new UserEntity();
//        //userEntity.setId(1L);
//        userEntity.setUserId(USER_ID1);
//        userEntity.setEncryptedPassword(ENCRYPTED_PASSWORD1);
//        userEntity.setEmail(EMAIL1); // обязательно должен быть верифицирован в амазоне
//        userEntity.setEmailVerificationToken(EMAIL_VERIFICATION_TOKEN1);
//        userEntity.setEmailVerificationStatus(false);
//        userEntity.setCertificatesOfUser(getCertificates());
//
//        userRepository.save(userEntity);
//
//
//        UserEntity userEntity2 = new UserEntity();
//        //userEntity.setId(1L);
//        userEntity2.setUserId(USER_ID2);
//        userEntity2.setEncryptedPassword(ENCRYPTED_PASSWORD2);
//        userEntity2.setEmail(EMAIL2); // обязательно должен быть верифицирован в амазоне
//        userEntity2.setEmailVerificationToken(EMAIL_VERIFICATION_TOKEN2);
//        userEntity2.setEmailVerificationStatus(false);
//        userEntity2.setCertificatesOfUser(getCertificates());
//
//        userRepository.save(userEntity2);
//        System.out.println("Records created");
//    }
//
//    private void deleteTestRecords(){
//        UserEntity userEntity1 = userRepository.findByEmail(EMAIL1);
//        UserEntity userEntity2 = userRepository.findByEmail(EMAIL2);
//        if (userEntity1!=null){userRepository.delete(userEntity1);}
//        if (userEntity2!=null){userRepository.delete(userEntity2);}
//        System.out.println("Users deleted");
//    }
//
//    private List<GiftCertificateEntity> getCertificates(){
//        List<GiftCertificateEntity> returnCollection = new ArrayList<>();
//
//        for (int i=0; i<2; i++){
//            GiftCertificateEntity cert = new GiftCertificateEntity();
//            cert.setName("testCert"+i);
//            cert.setDescription("description of cert1");
//            cert.setDuration(4);
//            cert.setPrice(new BigDecimal("12.50"));
//            cert.setCreateDate(LocalDate.now());
//            cert.setLastUpdateDate(LocalDate.now());
//            returnCollection.add(cert);
//        }
//        return returnCollection;
//    }

}