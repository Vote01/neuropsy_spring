package pin0.al.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pin0.al.Services.EmailService;

@RestController
public class EmailController {

    @Autowired
    EmailService emailService;


    @GetMapping("/send-email/{user-email}")
    public @ResponseBody ResponseEntity sendSimpleEmail(
            @PathVariable("user-email") String email) {
        try {
            emailService.sendSimpleEmail(
                    email,
                    "Уведомление",
                    "Данное письмо сформировано автоматически от сервиса Spring Boot");
        } catch (MailException mailException) {
            return new ResponseEntity<>(
                    "Невозможно отправить почту"+HttpStatus.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return new ResponseEntity<>(
                "Письмо успешно отправлено.",
                HttpStatus.OK
        );
    }
}
