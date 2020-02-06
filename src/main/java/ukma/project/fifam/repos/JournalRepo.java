package ukma.project.fifam.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ukma.project.fifam.models.*;

public interface JournalRepo extends JpaRepository<Journal, UserCategoryIdentity> {
}
