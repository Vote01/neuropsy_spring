package pin0.al.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pin0.al.Models.Method;
import pin0.al.Models.Specialization;
import pin0.al.Repositories.MethodRep;
import pin0.al.Repositories.SpecializationRep;

import java.util.List;
import java.util.Optional;


@Controller
public class SpecializationController {
    private SpecializationRep specializationRep;
    @Autowired
    public void setSpecializationRep(SpecializationRep specializationRep){
        this.specializationRep = specializationRep;
    }
    @GetMapping("/specialization")
    public String main(Model model){
        List<Specialization> specialization = (List<Specialization>) specializationRep.findAll();
        model.addAttribute("specializations", specialization);
        return "specialization/index";
    }

    /**
     * Добавление метода
     * */
    @GetMapping("/specialization/adding")
    public String add(Model model){
        model.addAttribute("specialization", new Method());
        return "specialization/adding";
    }
    @PostMapping("/specialization/adding")
    public String add(@Valid Specialization specialization, BindingResult result, Model model){
        if(result.hasErrors()){
            return "specialization/adding";
        }
        try {
            specializationRep.save(specialization);
            return "redirect:/specialization";
        } catch (Exception e) {

            return "specialization/adding";
        }
    }
    /**
     * Изменение метода
     * */
    @GetMapping("/specialization/update/{id}")
    public String update(@PathVariable Long id, Model model){
       Optional<Specialization> specialization = specializationRep.findById(id);
        model.addAttribute("specialization", specialization.get());
        return "specialization/editing";
    }
    @PostMapping("/specialization/update")
    public String update(@Valid Specialization specialization, BindingResult result, Model model){
        if(result.hasErrors()){
            if(result.getFieldError("name") != null) {
                result.rejectValue("name", "error.specialization", "Это имя уже используется");
            }
            return "specialization/editing";
        }
        try {
            specializationRep.save(specialization);
            return "redirect:/specialization";
        } catch (Exception e) {

            return "specialization/editing";
        }
    }
    /**
     * Удаление метода
     * */
    @GetMapping("/specialization/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        specializationRep.deleteById(id);
        return "redirect:/specialization";
    }

}
