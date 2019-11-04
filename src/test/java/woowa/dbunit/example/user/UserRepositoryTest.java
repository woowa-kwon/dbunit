package woowa.dbunit.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import woowa.dbunit.example.company.Company;
import woowa.dbunit.example.company.CompanyRepository;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void save() throws Exception {
        String name = "name!!";
        String email = "email@email.com";
        String password = "password!!";

        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        Company company = new Company("회사!");
        user.setCompany(company);

        companyRepository.save(company);
        User save = userRepository.save(user);

        User result = userRepository.findById(save.getId()).get();

        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(password);

        assertThat(company.getName()).isEqualTo(company.getName());
    }

}