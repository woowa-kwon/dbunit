package woowa.dbunit.example.user;

import lombok.*;
import woowa.dbunit.example.company.Company;

import javax.persistence.*;

@Entity
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "password", nullable = false, length = 10)
    private String password;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Builder
    public User(String name,
                String password,
                String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

}
