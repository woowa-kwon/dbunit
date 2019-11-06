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

        User savedUser1 = userService.register(userDto1);
        User savedUser2 = userService.register(userDto2);

        User actual1 = userRepository.findById(savedUser1.getId()).get();
        User actual2 = userRepository.findById(savedUser2.getId()).get();

        assertThat(actual1.getEmail()).isEqualTo(userDto1.getEmail());
        assertThat(actual1.getPassword()).isEqualTo(userDto1.getPassword());
        assertThat(actual1.getName()).isEqualTo(userDto1.getName());
        assertThat(actual1.getCompany()).isEqualTo(company1);

        assertThat(actual2.getEmail()).isEqualTo(userDto2.getEmail());
        assertThat(actual2.getPassword()).isEqualTo(userDto2.getPassword());
        assertThat(actual2.getName()).isEqualTo(userDto2.getName());
        assertThat(actual2.getCompany()).isEqualTo(company2);
    }

}