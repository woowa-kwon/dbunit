package woowa.dbunit.example.dbunit;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import woowa.dbunit.example.company.Company;
import woowa.dbunit.example.company.CompanyRepository;
import woowa.dbunit.example.user.User;
import woowa.dbunit.example.user.UserDto;
import woowa.dbunit.example.user.UserRepository;
import woowa.dbunit.example.user.UserService;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class UserServiceTestOnlyDbUnit {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DataSource dataSource;

    private Connection connection;
    private IDatabaseConnection iDatabaseConnection;
    private IDataSet flatXmlDataSet;

    @BeforeEach
    void setup() throws Exception {
        connection = dataSource.getConnection();
        iDatabaseConnection = new MySqlConnection(connection, "dbunit");

        InputStream is = this.getClass().getResourceAsStream("dataset.xml");
        flatXmlDataSet = new FlatXmlDataSetBuilder().build(is);
        DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, flatXmlDataSet);
    }

    @AfterEach
    void tearDown() throws Exception {
//        DatabaseOperation.DELETE_ALL.execute(iDatabaseConnection, flatXmlDataSet);
        if (connection != null) {
            connection.close();
        }
        if (iDatabaseConnection != null) {
            iDatabaseConnection.close();
        }
    }

    @Test
    void existDataSet() throws Exception {
        Company company = companyRepository.findById(12345L).get();
        assertThat(company.getName()).isEqualTo("company12345");
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
