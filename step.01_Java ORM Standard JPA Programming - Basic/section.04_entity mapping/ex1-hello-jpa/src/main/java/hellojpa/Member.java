package hellojpa;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Member {

    @Id
    private Long id;

    @Column(name = "name")              // 해당 필드는 DB 테이블에서 속성(attribute) 명이 `name`임을 지정
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)        // DB 에는 `EnumType`이 없어 해당 annotation 을 사용한다.
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)   // 자바의 경우 (날짜, 시간), DB 의 경우 (날짜, 시간, 날짜+시간) -> 매핑 정보를 줘야함
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob                                // varchar 를 넘어서는 굉장히 큰 컨텐츠를 넣고 싶다면 사용함
    private String description;

}
