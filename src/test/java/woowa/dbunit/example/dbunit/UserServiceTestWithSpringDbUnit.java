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

        User savedUser1 = userService.register(userDto1);
        User savedUser2 = userService.register(userDto2);

        User actual1 = userRepository.findById(savedUser1.getId()).get();
        User actual2 = userRepository.findById(savedUser2.getId()).get();

        assertThat(actual1.getEmail()).isEqualTo(userDto1.getEmail());
        assertThat(actual1.getPassword()).isEqualTo(userDto1.getPassword());
        assertThat(actual1.getName()).isEqualTo(userDto1.getName());
        assertThat(actual1.getCompany().getId()).isEqualTo(12345L);

        assertThat(actual2.getEmail()).isEqualTo(userDto2.getEmail());
        assertThat(actual2.getPassword()).isEqualTo(userDto2.getPassword());
        assertThat(actual2.getName()).isEqualTo(userDto2.getName());
        assertThat(actual2.getCompany().getId()).isEqualTo(23456L);
    }

}
