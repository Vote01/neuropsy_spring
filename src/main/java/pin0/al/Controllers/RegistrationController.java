package pin0.al.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pin0.al.Models.*;
import pin0.al.Repositories.*;
import pin0.al.Services.EmailService;
import pin0.al.UserService;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    private MethodRep mRep;
    @Autowired
    public void setMethodRep(MethodRep mRep){
        this.mRep = mRep;
    }
    private SpecializationRep sRep;
    @Autowired
    public void setSpecializationRep(SpecializationRep sRep){
        this.sRep = sRep;
    }

    @Autowired
    EmailService emailService;


    @GetMapping("/register")
    public String registerClient(Model model){
        model.addAttribute("userr", new User());

        return "registration/regist";
    }

    @PostMapping("/register")
    public String registerClient(@RequestParam String role, @RequestParam String email,@RequestParam String password, @RequestParam String name,  Model model) {
        try {
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
            emailService.sendSimpleEmail(
                    email,
                    "Регистрация на NeuroPsy",
                    "Вы успешно зарегестрированы на сайте NeuroPsy!");
            return "redirect:/login";
        } catch (Exception e) {
            return "registration/regist";
        }

    }

    @GetMapping("/registerpsy")
    public String registerpsy(Model model){
        List<Method> allMethods = (List<Method>) mRep.findAll();
        List<Specialization> allSpecializations = (List<Specialization>) sRep.findAll();

        model.addAttribute("allSpecializations", allSpecializations);
        model.addAttribute("allMethods", allMethods);
        model.addAttribute("psychologist", new Psychologist());
        return "registration/registpsy";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @PostMapping("/registerpsy")
    public String registerpsy(@Valid Psychologist psychologist, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "redirect:/registerpsy";
        }
        try {

            pRep.save(psychologist);

           return "redirect:/registpsythank";
        } catch (Exception e) {
            return "redirect:/registerpsy";
        }
    }

    @GetMapping("/registpsythank")
    public String registpsythank(Model model){
        return "registration/registpsythank";
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




}
