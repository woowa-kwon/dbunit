package woowa.dbunit.example.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowa.dbunit.example.company.Company;
import woowa.dbunit.example.company.CompanyRepository;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public UserService(CompanyRepository companyRepository,
                       UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public User register(UserDto userDto) {

        Company company = companyRepository.findById(userDto.getCompanyId())
                .orElseThrow(EntityNotFoundException::new);

        User user = User.builder()
                .name(userDto.getName())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .build();

        user.setCompany(company);
        return userRepository.save(user);
    }

}
