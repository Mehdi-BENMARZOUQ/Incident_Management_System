package projectbp.bp_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbp.bp_backend.bean.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);


}
