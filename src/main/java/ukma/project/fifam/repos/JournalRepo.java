package ukma.project.fifam.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ukma.project.fifam.models.*;

import java.util.List;
import java.util.Optional;

public interface JournalRepo extends JpaRepository<Journal, Long> {
    @Query("SELECT j FROM Journal j WHERE j.user=?1")
    Optional<List<Journal>> findJournalsByUserId(User id);
}
