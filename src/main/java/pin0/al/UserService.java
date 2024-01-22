package pin0.al;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import pin0.al.Repositories.UserRep;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    private UserRep userRepository;

    public void registerUser(String login, String password, String role) {

        String encodedPassword = passwordEncoder.encode(password);
        UserDetails anshulUser = User.withUsername(login).password(encodedPassword).roles(role).build();
        userDetailsManager.createUser(anshulUser);
}

    public pin0.al.Models.User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
