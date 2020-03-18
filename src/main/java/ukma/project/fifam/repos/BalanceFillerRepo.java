package ukma.project.fifam.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ukma.project.fifam.models.BalanceFiller;
import ukma.project.fifam.models.User;

import java.util.List;
import java.util.Optional;

public interface BalanceFillerRepo extends JpaRepository<BalanceFiller, Long> {
    @Query("SELECT bf FROM BalanceFiller bf WHERE bf.user=?1")
    Optional<List<BalanceFiller>> findBalanceFillersByUserId(User id);
}
