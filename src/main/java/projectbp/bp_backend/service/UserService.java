package projectbp.bp_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import projectbp.bp_backend.dao.UserRepo;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo user_repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return user_repo.findByEmail(username).orElseThrow();
    }


}
