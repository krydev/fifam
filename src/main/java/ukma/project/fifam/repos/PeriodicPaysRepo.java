package ukma.project.fifam.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ukma.project.fifam.models.*;

import java.util.List;
import java.util.Optional;

public interface PeriodicPaysRepo extends JpaRepository<PeriodicPays, Long> {
    @Query("SELECT pp FROM PeriodicPays pp WHERE pp.user=?1")
    Optional<List<PeriodicPays>> findPeriodicPaysByUserId(User id);
}
