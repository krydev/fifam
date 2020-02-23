package ukma.project.fifam.dtos.categories;

import com.sun.istack.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ExpenseDto {

    @NotNull
    public String sum;

    @Nullable
    public String desc;

}
