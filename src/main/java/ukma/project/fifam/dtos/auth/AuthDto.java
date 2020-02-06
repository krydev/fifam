package ukma.project.fifam.dtos.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class AuthDto {
    @NotNull
    @Email
    public String email;

    @NotNull
    public String password;
}
