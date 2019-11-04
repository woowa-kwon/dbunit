package woowa.dbunit.example.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
    private String name;
    private String password;
    private String email;
    private Long companyId;

    @Builder
    public UserDto(String name,
                   String password,
                   String email,
                   Long companyId) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.companyId = companyId;
    }

}
