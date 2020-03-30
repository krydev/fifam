package ukma.project.fifam.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ukma.project.fifam.models.Category;
import ukma.project.fifam.models.User;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.user=?1")
    Optional<List<Category>> findCategoriesByUserId(User id);
}
