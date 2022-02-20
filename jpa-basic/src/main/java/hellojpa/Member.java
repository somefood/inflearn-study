package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {

    @Id // pk
    private Long id;

    @Column(name = "name")
    private String username;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

    @Transient // 메모리에서만
    private int temp;

    // JPA는 기본 생성자 있어야함
    public Member() {
    }
}
