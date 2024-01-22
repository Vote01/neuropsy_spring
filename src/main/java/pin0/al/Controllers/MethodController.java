package pin0.al.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pin0.al.Models.Method;
import pin0.al.Repositories.MethodRep;

import java.util.List;
import java.util.Optional;


@Controller
public class MethodController {
    private MethodRep methodRep;
    @Autowired
    public void setMethodRep(MethodRep methodRep){
        this.methodRep = methodRep;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/method")
    public String main(Model model){
        List<Method> methods = (List<Method>) methodRep.findAll();
        model.addAttribute("methods", methods);
        return "method/index";
    }

    /**
     * Добавление метода
     * */
    @GetMapping("/method/adding")
    public String add(Model model){
        model.addAttribute("method", new Method());
        return "method/adding";
    }
    @PostMapping("/method/adding")
    public String add(@Valid Method method, BindingResult result, Model model){
        if(result.hasErrors()){
            return "method/adding";
        }
        try {
            methodRep.save(method);
            return "redirect:/method";
        } catch (Exception e) {

            return "method/adding";
        }
    }
    /**
     * Изменение метода
     * */
    @GetMapping("/method/update/{id}")
    public String update(@PathVariable Long id, Model model){
       Optional<Method> method = methodRep.findById(id);
        model.addAttribute("method", method.get());
        return "method/editing";
    }
    @PostMapping("/method/update")
    public String update(@Valid Method method, BindingResult result, Model model){
        if(result.hasErrors()){
            if(result.getFieldError("name") != null) {
                result.rejectValue("name", "error.method", "Это имя уже используется");
            }
            return "method/editing";
        }
        try {
            methodRep.save(method);
            return "redirect:/method";
        } catch (Exception e) {

            return "method/editing";
        }
    }
    /**
     * Удаление метода
     * */
    @GetMapping("/method/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        methodRep.deleteById(id);
        return "redirect:/method";
    }

}
