package ukma.project.fifam.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ukma.project.fifam.models.User;

public interface UserRepo extends JpaRepository<User, Long> {
}
