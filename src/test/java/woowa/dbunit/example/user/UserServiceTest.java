package woowa.dbunit.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import woowa.dbunit.example.company.Company;
import woowa.dbunit.example.company.CompanyRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void register() throws Exception {
        Company company1 = companyRepository.save(new Company("company1"));
        Company company2 = companyRepository.save(new Company("company2"));

        UserDto userDto1 = UserDto.builder()
                .name("name!!")
                .password("pass!!")
                .email("email1@email.com")
                .companyId(company1.getId())
                .build();

        UserDto userDto2 = UserDto.builder()
                .name("name!!")
                .password("pass!!")
                .email("email2@email.com")
                .companyId(company2.getId())
                .build();

        User result1 = userService.register(userDto1);
        User result2 = userService.register(userDto2);

        User actual1 = userRepository.findById(result1.getId()).get();
        User actual2 = userRepository.findById(result2.getId()).get();

        assertThat(result1.getEmail()).isEqualTo(actual1.getEmail());
        assertThat(result1.getPassword()).isEqualTo(actual1.getPassword());
        assertThat(result1.getName()).isEqualTo(actual1.getName());
        assertThat(result1.getCompany()).isEqualTo(company1);

        assertThat(result2.getEmail()).isEqualTo(actual2.getEmail());
        assertThat(result2.getPassword()).isEqualTo(actual2.getPassword());
        assertThat(result2.getName()).isEqualTo(actual2.getName());
        assertThat(result2.getCompany()).isEqualTo(company2);
    }

}