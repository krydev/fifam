package ukma.project.fifam.dtos.categories;

import com.sun.istack.Nullable;

import javax.validation.constraints.NotNull;

public class ExpenseDto {

    @NotNull
    public String sum;

    @NotNull
    public long recordDate;

    @Nullable
    public String desc;

}
