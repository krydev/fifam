package ukma.project.fifam.dtos.categories;

import javax.validation.constraints.NotNull;

public class CategoryDto {

    @NotNull
    public String name;

    @NotNull
    public String budget;

    @NotNull
    public String frequency;
}
