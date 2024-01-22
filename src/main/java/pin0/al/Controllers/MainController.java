package pin0.al.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(Model model){
        return "redirect:/licences";
    }
    @GetMapping("/about")
    public String about(@RequestParam(name="name", required = false, defaultValue = "По умолчанию") String name, Model model){
        model.addAttribute("author", name);
        return "main/about";
    }










































    //LB3
    /*private StudentService studentService;
    @Autowired
    public void setStudentService(StudentService studentService){
        this.studentService = studentService;
    }
    @GetMapping("/institut")
    public String mainPage(Model model) {
        List<Student> allStudents = studentService.getAllstudents();
        model.addAttribute("students", allStudents);
        return "main/institut";
    }
    @GetMapping("/details/{id}")
    public String detailsPage(Model model, @PathVariable("id") Long id) {
        Student selectedStudent = studentService.getStudentById(id);
        model.addAttribute("selectedStudent", selectedStudent);
        return "main/details";
    }
    @GetMapping("/students/delete/{id}")
    public String deleteStudentById(@PathVariable("id") Long id){
        studentService.deleteStudentById(id);
        return "redirect:/institut";
    }
    @GetMapping("/form")
    public String mainForm(Model model){
        model.addAttribute("student", new Student());
        return "main/main-form";
    }
    @PostMapping("/form")
    public String mainForm(@ModelAttribute Student student, Model model){
        model.addAttribute("student", student);
        return "main/result";
    }*/
}
