package ukma.project.fifam.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ukma.project.fifam.models.*;

public interface PeriodicPaysRepo extends JpaRepository<PeriodicPays, UserCategoryIdentity> {
}
