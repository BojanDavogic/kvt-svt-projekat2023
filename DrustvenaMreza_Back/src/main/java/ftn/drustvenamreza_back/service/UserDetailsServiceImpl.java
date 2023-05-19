package ftn.drustvenamreza_back.service;

import ftn.drustvenamreza_back.model.entity.User;
import ftn.drustvenamreza_back.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserServiceImpl userService;

    /*
    @Autowired
    public UserDetailsServiceImpl(UserService userService){
        this.userService = userService;
    }
*/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("There is no user with username " + username);
        } else {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            String dtype = user.getDtype();

            if ("Administrator".equals(dtype)) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else if ("User".equals(dtype)) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            } else {
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername().trim(),
                    user.getPassword().trim(),
                    grantedAuthorities);
        }
    }
}
