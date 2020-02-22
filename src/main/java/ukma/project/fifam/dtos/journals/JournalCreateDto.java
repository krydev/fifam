package ukma.project.fifam.dtos.journals;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

public class JournalCreateDto {

    @NotNull
    public Long categoryId;

    @DateTimeFormat
    public Date recordDate;

    @NotNull
    public String sum;

    @Null
    public String desc;

    @NotNull
    public String currBalance;
}
