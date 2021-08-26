package by.prus.LabProject.service.mail;

import by.prus.LabProject.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YandexSES {

    @Autowired
    MailSender mailSender;

    //Address will be verified with AmazonSES
    final String FROM = "jeakkey@yandex.ru";

    //the subject line for the email
    final String EMAIL_VERIFICATION_SUBJECT = "One last step to complete your registration with LabProject";
    final String PASSWORD_RESET_SUBJECT = "Password reset request";

    //the HTML body for the email.
    final String HTMLBODY = "<h1>Please verify your email address<h1>"
            + "<p>Thank you for registertin with our mobile app. To complete registration process abd be able to log in"
            + " click on the following link:"
            + "<a href='http://localhost:8080/LabProjectEmailVerification/email-verification.html?token=$tokenValue'>"
            + " Final step to complete your registration"
            + "</a><br/><br/>"
            + "Thank you! And we are waiting for you inside!";

    // The email body for recipients with non-HTML email clients.

    final String TEXTBODY = "Please verify your email address."
            +"Thank you for registering with our app. To complete registration process and be able to log in, "
            + "open then the following URL in your browser window: "
            + "'http://localhost:8080/LabProjectEmailVerification/email-verification.html?token=$tokenValue'"
            + " Thank you! And we are waiting for you inside!";

    //the HTML body for the email.
    final String PASSWORD_RESET_HTMLBODY = "<h1>A request to reset your password<h1>"
            + "<p>Hi, $firstName!</p>"
            + "<p>Click on the link below to set a new password:</p>"
            + "<a href='http://localhost:8080/LabProjectEmailVerification/password-reset.html?token=$tokenValue'>"
            + " Click this link to reset Password"
            + "</a><br/><br/>"
            + "Thank you!";

    // The email body for recipients with non-HTML email clients.
    // Notice LabProjectEmailVerification - is other project

    final String PASSWORD_RESET_TEXTBODY = "Hi, $login!"
            + " Click on the link below to set a new password:"
            + "'http://localhost:8080/LabProjectEmailVerification/password-reset.html?token=$tokenValue'"
            + " Thank you!";


    public void verifyEmail(UserDto userDto){

        String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
        String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());

        mailSender.send(userDto.getEmail(), EMAIL_VERIFICATION_SUBJECT, textBodyWithToken);

    }

    public boolean sendPasswordResetRequest (String email, String token){

        boolean returnValue = false;

        String textBodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
        textBodyWithToken = textBodyWithToken.replace("$login",email);

        mailSender.send(email, PASSWORD_RESET_SUBJECT, textBodyWithToken);

        returnValue = true;

        return  returnValue;
    }
}
