package ukma.project.fifam.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ukma.project.fifam.models.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
