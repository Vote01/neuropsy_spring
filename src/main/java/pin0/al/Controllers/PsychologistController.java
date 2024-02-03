package pin0.al.Controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pin0.al.Models.*;
import pin0.al.Repositories.*;
import pin0.al.Services.EmailService;
import pin0.al.UserService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Controller
public class PsychologistController {
    private PsychologistRep psychologistRep;
    @Autowired
    public void setPsychologistRep(PsychologistRep psychologistRep){
        this.psychologistRep = psychologistRep;
    }
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

    private SessionRep sessionRep;
    @Autowired
    public void setSessionRep(SessionRep sessionRep){
        this.sessionRep = sessionRep;
    }

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

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    @Autowired
    EmailService emailService;




    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/psychologist")
    public String main(@RequestParam(name = "methodIds", required = false) List<Long> methodIds,
                       @RequestParam(name = "specializationIds", required = false) List<Long> specializationIds,
                       @RequestParam(name = "priceRange", required = false) Integer priceRange,
                       Model model) {
        List<Psychologist> psychologists;

        if (methodIds != null && specializationIds != null) {
            List<Method> selectedMethods = mRep.findByIdIn(methodIds);
            List<Specialization> selectedSpecializations = sRep.findByIdIn(specializationIds);

            psychologists = psychologistRep.findByMethodListInAndSpecializationListIn(selectedMethods, selectedSpecializations);
        } else if (methodIds != null) {
            List<Method> selectedMethods = mRep.findByIdIn(methodIds);

            psychologists = psychologistRep.findByMethodListIn(selectedMethods);
        } else if (specializationIds != null) {
            List<Specialization> selectedSpecializations = sRep.findByIdIn(specializationIds);

            psychologists = psychologistRep.findBySpecializationListIn(selectedSpecializations);
        } else {
            // Без фильтрации
            psychologists = (List<Psychologist>) psychologistRep.findAll();
        }
        if (priceRange != null) {
            psychologists = psychologists.stream()
                    .filter(psychologist -> psychologist.getPrice() <= priceRange)
                    .collect(Collectors.toList());
        }
        List<Method> allMethods = (List<Method>) mRep.findAll();
        List<Specialization> allSpecializations = (List<Specialization>) sRep.findAll();




        model.addAttribute("psychologists", psychologists);
        model.addAttribute("allMethods", allMethods);
        model.addAttribute("allSpecializations", allSpecializations);

        return "psychologist/index";
    }

    /**
     * Добавление психолога
     * */
    @GetMapping("/psychologist/adding")
    public String add(Model model){
        List<Method> allMethods = (List<Method>) mRep.findAll();
        List<Specialization> allSpecializations = (List<Specialization>) sRep.findAll();

        model.addAttribute("allSpecializations", allSpecializations);
        model.addAttribute("allMethods", allMethods);
        model.addAttribute("psychologist", new Psychologist());
        return "psychologist/adding";
    }
    @PostMapping("/psychologist/adding")
    public String add(@Valid Psychologist psychologist, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "redirect:/psychologist/adding";
        }
        try {
            // Сохраняем файл
         //   String folder = "/uploads/";
        //    byte[] bytes = image.getBytes();
        //    Path path = Paths.get(folder + image.getOriginalFilename());
        //    Files.write(path, bytes);

            // Сохраняем URL файла в базе данных
      //      psychologist.setImage(folder + image.getOriginalFilename());

            psychologistRep.save(psychologist);
            return "redirect:/psychologist";
        } catch (Exception e) {
            return "redirect:/psychologist/adding";
        }
    }

  //
  @GetMapping("/psychologist/confirmed/{id}")
  public String main(@PathVariable Long id, Model model){
      Optional<Psychologist> psychologist = psychologistRep.findById(id);
      psychologist.get().setConfirmed(true);
      Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
      psychologist.get().setRegistration(currentDate);
      psychologistRep.save(psychologist.get());

      User user = new User();
      user.setRole(String.valueOf(Role.PSYCHOLOGIST));
      user.setEmail(psychologist.get().getEmail());
      String password = new Random().ints(10, 33, 122).collect(StringBuilder::new,
                      StringBuilder::appendCodePoint, StringBuilder::append)
              .toString();
       emailService.sendSimpleEmail(
              psychologist.get().getEmail(),
              "Регистрация на NeuroPsy",
              "Вы успешно зарегестрированы на сайте NeuroPsy! Ваш логин: "+psychologist.get().getEmail()+ " Ваш пароль: "+password);

      user.setPassword(password);
      userService.registerUser(user.getEmail(), user.getPassword(), String.valueOf(user.getRoleEnum()));
      user.setIduser(psychologist.get().getId());
      userRep.save(user);
      return "redirect:/psychologist";
  }





    /**
     * Изменение психолога
     * */
    @GetMapping("/psychologist/update/{id}")
    public String update(@PathVariable Long id, Model model){
       Optional<Psychologist> psychologist = psychologistRep.findById(id);
        List<Method> allMethods = (List<Method>) mRep.findAll();
        List<Specialization> allSpecializations = (List<Specialization>) sRep.findAll();
        model.addAttribute("allMethods", allMethods);
        model.addAttribute("allSpecializations", allSpecializations);
        model.addAttribute("psychologist", psychologist.get());
        return "psychologist/editing";
    }
    @PostMapping("/psychologist/update")
    public String update(@Valid Psychologist psychologist, BindingResult result, Model model){

        try {
            psychologistRep.save(psychologist);
            return "redirect:/psychologist";
        } catch (Exception e) {

           return "psychologist/editing";
          //  return "redirect:/psychologist/update/{}"; сделать контаквапвапвпапвтанацию
        }
    }
    /**
     * Удаление метода
     * */
    @GetMapping("/psychologist/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        psychologistRep.deleteById(id);
        return "redirect:/psychologist";
    }
    /**
     * Админский просмотр деталей
     * */
    @GetMapping("/psychologist/details/{id}")
    public String details(@PathVariable Long id, Model model){
        Optional<Psychologist> psychologist = psychologistRep.findById(id);
        model.addAttribute("psychologist", psychologist.get());
        List<Session> sessions = sessionRep.findByPsychologist(psychologist.get());
        model.addAttribute("sessions", sessions);
        return "psychologist/details";
    }


    /**
     * Клиентский просмотр деталей
     * */
    @GetMapping("/psychologist/detailsforclient/{id}")
    public String detailsforclient(@PathVariable Long id, Model model){
        Optional<Psychologist> psychologist = psychologistRep.findById(id);
        model.addAttribute("psychologist", psychologist.get());
        return "psychologist/detailsforclient";
    }


    @GetMapping("/psychologist/detailsforclient/date/{id}")
    public String detailsdate(@PathVariable Long id, Model model, Principal principal){

        User user = userService.findByEmail(principal.getName());
        if(user.getRoleEnum()== Role.CLIENT) {
            Optional<Client> client = cRep.findById(user.getIduser());
            client.ifPresent(value -> model.addAttribute("client", value));
        }

        Optional<Psychologist> psychologist = psychologistRep.findById(id);
        model.addAttribute("psychologist", psychologist.get());
        model.addAttribute("session", new Session());
        return "session/addingforclient";
    }

    @PostMapping("/psychologist/detailsforclient/date")
    public String detailsdate(@Valid Session session, BindingResult result, Model model){
        try {
            sessionRep.save(session);
            return "redirect:/profile";
        } catch (Exception e) {

            return "redirect:/psychologist";
        }
    }


    @GetMapping("/psychologist/report")
    public void generateReport(HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"report.pdf\"");
            Font font = FontFactory.getFont("DejaVuSans.ttf","cp1251", BaseFont.EMBEDDED, 10);
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
             document.open();

            long psychologistCount = psychologistRep.count();
            List<Object[]> popularMethods = psychologistRep.findPopularMethods();
            long psychologistsWithVideocon = psychologistRep.countByVideoconTrue();
            long completedSessions = sessionRep.countByStatus("F");

            document.add(new Paragraph("Количество психологов: " + psychologistCount, font));
            document.add(new Paragraph("Количество психологов с видеосвязью: " + psychologistsWithVideocon, font));
            StringBuilder methodsInfo = new StringBuilder("Популярные методы: ");
            for (Object[] result : popularMethods) {
                String method = (String) result[0];
                Long count = (Long) result[1];
                methodsInfo.append("\n").append(method).append(", Количество психологов: ").append(count);
            }
            document.add(new Paragraph(methodsInfo.toString(), font));
            document.add(new Paragraph("Количество завершенных сессий: " + completedSessions, font));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
