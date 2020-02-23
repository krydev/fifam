package ukma.project.fifam.dtos.journals;

import com.sun.istack.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

public class JournalCreateDto {

    @Nullable
    public Long categoryId;

    @DateTimeFormat
    public LocalDateTime recordDate;

    @NotNull
    public String sum;

    @Nullable
    public String desc;

    @NotNull
    public String currBalance;

    public JournalCreateDto(Long categoryId, LocalDateTime recordDate,
                            @NotNull String sum, String desc,
                            @NotNull String currBalance) {
        this.categoryId = categoryId;
        this.recordDate = recordDate;
        this.sum = sum;
        this.desc = desc;
        this.currBalance = currBalance;
    }
}
