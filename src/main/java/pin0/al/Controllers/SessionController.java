package pin0.al.Controllers;

import jakarta.validation.Valid;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pin0.al.Models.Client;
import pin0.al.Models.Psychologist;
import pin0.al.Models.Session;
import pin0.al.Repositories.ClientRep;
import pin0.al.Repositories.PsychologistRep;
import pin0.al.Repositories.SessionRep;
import pin0.al.Services.EmailService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
public class SessionController {


    @Autowired
    EmailService emailService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Environment environment;
    private SessionRep sessionRep;
    @Autowired
    public void setSessionRep(SessionRep sessionRep){
        this.sessionRep = sessionRep;
    }
    private ClientRep cRep;
    @Autowired
    public void setClientRep(ClientRep clientRep){
        this.cRep = clientRep;
    }
    private PsychologistRep pRep;
    @Autowired
    public void setPsychologistRep(PsychologistRep pRep){
        this.pRep = pRep;
    }
    @GetMapping("/session")
    public String main(Model model){

        List<Session> sessions = (List<Session>) sessionRep.findAll();
        model.addAttribute("sessions", sessions);
        return "session/index";
    }

    /**
     * Добавление метода
     * */
    @GetMapping("/session/adding")
    public String add(Model model){
        List<Psychologist> psychologist = (List<Psychologist>)pRep.findAll();
        List<Client> client = (List<Client>) cRep.findAll();
        model.addAttribute("client", client);
        model.addAttribute("psychologist",psychologist);
        model.addAttribute("session", new Session());
        return "session/adding";
    }
    @PostMapping("/session/adding")
    public String add(@Valid Session session, BindingResult result){
        try {
            sessionRep.save(session);
            emailService.sendSimpleEmail(
                    session.getClient().getEmail(),
                    "Назначена сессия",
                    "Вы назначили сессию с "+session.getPsychologist().getName()+ " на "+session.getDateDay());

            emailService.sendSimpleEmail(
                    session.getPsychologist().getEmail(),
                    "Вам назначена сессия",
                    "Вам назначили сессию на "+session.getDateDay());
            return "redirect:/profile";
        } catch (Exception e) {
            return "redirect:/psychologist";
        }
    }


    /**
     * Изменение метода
     * */
    @GetMapping("/session/update/{id}")
    public String update(@PathVariable Long id, Model model){
       Optional<Session> session = sessionRep.findById(id);
        List<Psychologist> psychologist = (List<Psychologist>)pRep.findAll();
        List<Client> client = (List<Client>) cRep.findAll();
        List<String> sessionTimes = Arrays.asList("03:00", "06:00", "09:00", "12:00", "15:00", "18:00", "21:00", "00:00");
        model.addAttribute("sessionTimes", sessionTimes);
        model.addAttribute("clients", client);
        model.addAttribute("psychologists",psychologist);
        model.addAttribute("session", session.get());
        return "session/editing";
    }
    @PostMapping("/session/update")
    public String update(@Valid Session session, BindingResult result, Model model){
        if(result.hasErrors()){
            if(result.getFieldError("name") != null) {
                result.rejectValue("name", "error.session", "Это имя уже используется");
            }
            return "session/editing";
        }
        try {
            sessionRep.save(session);
            return "redirect:/session";
        } catch (Exception e) {

            return "session/editing";
        }
    }
    /**
     * Удаление метода
     * */
    @GetMapping("/session/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        sessionRep.deleteById(id);
        return "redirect:/session";
    }

    @GetMapping("/session/cancel/{id}")
    public String cancelsession(@PathVariable("id") Long id){
        Optional<Session> session =  sessionRep.findById(id);
        session.get().setStatus("M");
        sessionRep.save(session.get());
        return "redirect:/home";
    }

    @GetMapping("/session/finish/{id}")
    public String finishsession(@PathVariable("id") Long id){
        Optional<Session> session =  sessionRep.findById(id);
        session.get().setStatus("F");
        sessionRep.save(session.get());
        return "redirect:/home";
    }



}
