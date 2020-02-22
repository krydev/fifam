package ukma.project.fifam.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ukma.project.fifam.models.*;

import java.util.List;
import java.util.Optional;

public interface JournalRepo extends JpaRepository<Journal, UserCategoryIdentity> {
    @Query("SELECT j FROM Journal j WHERE j.userId=?1")
    public Optional<List<Journal>> findJournalsByUserId(Long id);
}
