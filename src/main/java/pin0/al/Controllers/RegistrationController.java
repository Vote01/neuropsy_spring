package pin0.al.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pin0.al.Models.*;
import pin0.al.Repositories.ClientRep;
import pin0.al.Repositories.PsychologistRep;
import pin0.al.Repositories.SessionRep;
import pin0.al.Repositories.UserRep;
import pin0.al.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
public class RegistrationController {

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    private UserRep userRep;
    @Autowired
    public void setUserRep(UserRep userRep){
        this.userRep = userRep;
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

    private SessionRep sessionRep;
    @Autowired
    public void setSessionRep(SessionRep sessionRep){
        this.sessionRep = sessionRep;
    }


    @GetMapping("/register")
    public String registerClient(Model model){
        model.addAttribute("userr", new User());

        return "registration/regist";
    }

    @PostMapping("/register")
    public String registerClient(@RequestParam String role, @RequestParam String email,@RequestParam String password, @RequestParam String name,  Model model) {
        User user = new User();
        user.setRole(role);
        user.setEmail(email);
        user.setPassword(password);
        userService.registerUser(user.getEmail(), user.getPassword(), String.valueOf(user.getRoleEnum()));
         Client client = new Client();
         client.setName(name);
         client.setEmail(user.getEmail());
        cRep.save(client);
         user.setIduser(client.getId());
        userRep.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String getProfileClient(Model model, Principal principal){
        User user = userService.findByEmail(principal.getName());
        if(user.getRoleEnum()== Role.CLIENT) {
            Optional<Client> client = cRep.findById(user.getIduser());
            client.ifPresent(value -> model.addAttribute("client", value));
            List<Session> sessions = sessionRep.findByClient(client.get());
            model.addAttribute("sessions", sessions);
            return "registration/profile";
        }
        return "/";
    }
    @GetMapping("/profilepsy")
    public String getProfilePsy(Model model, Principal principal){
        User user = userService.findByEmail(principal.getName());
        if(user.getRoleEnum()== Role.PSYCHOLOGIST) {
            Optional<Psychologist> psychologist = pRep.findById(user.getIduser());
            psychologist.ifPresent(value -> model.addAttribute("psychologist", value));
            List<Session> sessions = sessionRep.findByPsychologist(psychologist.get());
            model.addAttribute("sessions", sessions);
            return "registration/profilepsy";
        }
        return "/";
    }


    @PostMapping("/register-psy")
    public String registerPsychologist(@Valid Psychologist psychologist, BindingResult result, Model model) {
//СОЗДАТЬ ПОЛЕ ДЛЯ ПОДТВЕРЖДЕНИЯ ПСИХОЛОГА
        return "redirect:/login";
    }

}
