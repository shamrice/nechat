package io.github.shamrice.nechat.server.webservice.security.services;

import io.github.shamrice.nechat.server.core.CoreContext;
import io.github.shamrice.nechat.server.core.db.dto.UserDto;
import io.github.shamrice.nechat.server.core.db.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * Created by Erik on 10/20/2017.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) {

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ADMIN");
        HashSet<GrantedAuthority> grantedAuthorityHashSet = new HashSet<>();
        grantedAuthorityHashSet.add(grantedAuthority);

        UserService userService = new UserService(CoreContext.getInstance());
        UserDto user = userService.getUser(userName);

        if (user != null && user.getLogin() != null && user.getUserId() >= 0) {
            return new User(user.getLogin(), user.getPassword(), grantedAuthorityHashSet);
        } else {
            System.out.println("User returned null and potentially does not exist.");
            return null;
        }
    }
}
