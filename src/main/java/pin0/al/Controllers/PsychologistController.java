package pin0.al.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pin0.al.Models.Method;
import pin0.al.Models.Psychologist;
import pin0.al.Models.Session;
import pin0.al.Models.Specialization;
import pin0.al.Repositories.MethodRep;
import pin0.al.Repositories.PsychologistRep;
import pin0.al.Repositories.SessionRep;
import pin0.al.Repositories.SpecializationRep;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
            // Фильтрация по методам и специализациям
            List<Method> selectedMethods = mRep.findByIdIn(methodIds);
            List<Specialization> selectedSpecializations = sRep.findByIdIn(specializationIds);

            psychologists = psychologistRep.findByMethodListInAndSpecializationListIn(selectedMethods, selectedSpecializations);
        } else if (methodIds != null) {
            // Фильтрация по методам
            List<Method> selectedMethods = mRep.findByIdIn(methodIds);

            psychologists = psychologistRep.findByMethodListIn(selectedMethods);
        } else if (specializationIds != null) {
            // Фильтрация по специализациям
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
     * Добавление метода
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
    public String add(@Valid Psychologist psychologist, BindingResult result, Model model, @RequestParam("image") MultipartFile image) {
        if(result.hasErrors()){
            return "psychologist/adding";
        }
        try {
            // Сохраняем файл
            String folder = "/uploads/";
            byte[] bytes = image.getBytes();
            Path path = Paths.get(folder + image.getOriginalFilename());
            Files.write(path, bytes);

            // Сохраняем URL файла в базе данных
            psychologist.setImage(folder + image.getOriginalFilename());

            psychologistRep.save(psychologist);
            return "redirect:/psychologist";
        } catch (Exception e) {
            return "psychologist/adding";
        }
    }

    /**
     * Изменение метода
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

    @GetMapping("/psychologist/details/{id}")
    public String details(@PathVariable Long id, Model model){
        Optional<Psychologist> psychologist = psychologistRep.findById(id);
        model.addAttribute("psychologist", psychologist.get());
        List<Session> sessions = sessionRep.findByPsychologist(psychologist.get());
        model.addAttribute("sessions", sessions);
        return "psychologist/details";
    }
    @GetMapping("/psychologist/detailsforclient/{id}")
    public String detailsforclient(@PathVariable Long id, Model model){
        Optional<Psychologist> psychologist = psychologistRep.findById(id);
        model.addAttribute("psychologist", psychologist.get());
        return "psychologist/detailsforclient";
    }


}
