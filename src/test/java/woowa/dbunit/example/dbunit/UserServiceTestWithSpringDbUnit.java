package woowa.dbunit.example.dbunit;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import woowa.dbunit.example.TestConfig;
import woowa.dbunit.example.user.User;
import woowa.dbunit.example.user.UserDto;
import woowa.dbunit.example.user.UserRepository;
import woowa.dbunit.example.user.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestExecutionListeners({
        DbUnitTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class, // Bean 을 DI 받기 위해 선언해줘야 한다.
//        TransactionDbUnitTestExecutionListener.class // @DatabaseSetup 부터 @DatabaseTearDown 까지 트랜잭션이 적용된다.
                                                    // @DatabaseTearDown 선언시 DbUnitTestExecutionListener 와 함께 사용할 수 없다.
})
@DbUnitConfiguration(databaseConnection = "dbUnitDatabaseConnection")
@DatabaseSetup(value = {"company.xml", "user.xml"}, type = DatabaseOperation.CLEAN_INSERT)
@DatabaseTearDown(value = {"company.xml", "user.xml"}, type = DatabaseOperation.DELETE_ALL)
public class UserServiceTestWithSpringDbUnit {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void notNull() throws Exception {
        assertThat(userService).isNotNull();
    }

    @Test
    void register() throws Exception {
        UserDto userDto1 = UserDto.builder()
                .name("name!!")
                .password("pass!!")
                .email("email1@email.com")
                .companyId(12345L)
                .build();

        UserDto userDto2 = UserDto.builder()
                .name("name!!")
                .password("pass!!")
                .email("email2@email.com")
                .companyId(23456L)
                .build();

        User result1 = userService.register(userDto1);
        User result2 = userService.register(userDto2);

        User actual1 = userRepository.findById(result1.getId()).get();
        User actual2 = userRepository.findById(result2.getId()).get();

        assertThat(result1.getEmail()).isEqualTo(actual1.getEmail());
        assertThat(result1.getPassword()).isEqualTo(actual1.getPassword());
        assertThat(result1.getName()).isEqualTo(actual1.getName());
        assertThat(result1.getCompany().getId()).isEqualTo(12345L);

        assertThat(result2.getEmail()).isEqualTo(actual2.getEmail());
        assertThat(result2.getPassword()).isEqualTo(actual2.getPassword());
        assertThat(result2.getName()).isEqualTo(actual2.getName());
        assertThat(result2.getCompany().getId()).isEqualTo(23456L);
    }

}
