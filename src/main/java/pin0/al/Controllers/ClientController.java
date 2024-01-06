package pin0.al.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pin0.al.Models.Client;
import pin0.al.Models.Method;
import pin0.al.Models.Specialization;
import pin0.al.Repositories.ClientRep;
import pin0.al.Repositories.SpecializationRep;

import java.util.List;
import java.util.Optional;


@Controller
public class ClientController {
    private ClientRep clientRep;
    @Autowired
    public void setClientRep(ClientRep clientRep){
        this.clientRep = clientRep;
    }
    @GetMapping("/client")
    public String main(Model model){
        List<Client> clients = (List<Client>) clientRep.findAll();
        model.addAttribute("clients", clients);
        return "client/index";
    }

    /**
     * Добавление метода
     * */
    @GetMapping("/client/adding")
    public String add(Model model){
        model.addAttribute("client", new Client());
        return "client/adding";
    }
    @PostMapping("/client/adding")
    public String add(@Valid Client client, BindingResult result, Model model){
        if(result.hasErrors()){
            return "client/adding";
        }
        try {
            clientRep.save(client);
            return "redirect:/client";
        } catch (Exception e) {

            return "client/adding";
        }
    }
    /**
     * Изменение метода
     * */
    @GetMapping("/client/update/{id}")
    public String update(@PathVariable Long id, Model model){
       Optional<Client> client = clientRep.findById(id);
        model.addAttribute("client", client.get());
        return "client/editing";
    }
    @PostMapping("/client/update")
    public String update(@Valid Client client, BindingResult result, Model model){
        if(result.hasErrors()){
            if(result.getFieldError("email") != null) {
                result.rejectValue("email", "error.client", "Эта почта уже используется");
            }
            return "client/editing";
        }
        try {
            clientRep.save(client);
            return "redirect:/client";
        } catch (Exception e) {

            return "client/editing";
        }
    }
    /**
     * Удаление метода
     * */
    @GetMapping("/client/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        clientRep.deleteById(id);
        return "redirect:/client";
    }

}
