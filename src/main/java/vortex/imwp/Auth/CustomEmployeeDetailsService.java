package vortex.imwp.Auth;
import org.springframework.security.core.userdetails.User;
import vortex.imwp.Models.Employee;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vortex.imwp.Services.EmployeeService;

@Service
public class CustomEmployeeDetailsService implements UserDetailsService {
    private final EmployeeService employeeService;
    public CustomEmployeeDetailsService(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return employeeService.getEmployeeByUsername(username)
                .map(dto -> User.builder()
                        .username(dto.getUsername())
                        .password(dto.getPassword())
                        .roles(dto.getJobs().toArray(String[]::new))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User by the username " + username + " not found"));
    }
}
