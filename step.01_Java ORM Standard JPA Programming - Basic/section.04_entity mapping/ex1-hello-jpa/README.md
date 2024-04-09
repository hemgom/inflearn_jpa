# 섹션 04. 엔티티 매핑
엔티티 매핑 소개
- `객체와 테이블 매핑`: `@Entity`, `@Table`
- `필드와 컬럼 매핑`: `@Column`
- `기본 키 매핑`: `@Id`
- `연관관계 매핑`: `@ManyToOne`, `@JoinColumn`
## 01. 객체와 테이블 매핑
### @Entity
해당 `annotation`이 붙은 클래스를 `엔티티`라고 하며, `JPA가 관리`한다.
- `JPA`를 통해 테이블과 매핑 할 클래스라면 `@Entity`가 필수!
- 사용 시 주의사항
  - `기본 생성자 필수(파라미터가 없는 public, protected 생성자)`
    - `JPA 스펙 상 규정`
  - `final class`, `enum`, `interface`, `inner class` 사용 X
  - 저장 필드에 `final` 사용 X
#### @Entity 속성 - name
`JPA`에서 사용할 엔티티 이름을 지정하는 속성
- `기본 값`: `annotation`이 적용된 클래스 명
  - 사실 크게 쓸일이 없다. 오히려 사용하게 되면 혼란스럽다.
  - 대부분 기본 값을 사용하게 될 것이다.  
<br/><br/>

### @Table
`엔티티`와 매핑 할 `테이블`을 지정한다.
- 회사 방침 혹은 클라이언트의 요구로 `특정 테이블 명`을 사용해야 할 경우 사용하게 됨
- `엔티티 명`과 `테이블 명`이 달라도 서로 매핑이 가능해짐
#### @Table 속성
- `name`: 매핑 할 테이블 명을 지정, `기본 값은 엔티티 명`을 사용 함
- `catalog`: `DB catalog` 매핑
- `schema`: `DB schema` 매핑
- `uniqueConstraints(DDL)`: `DDL` 생성 시 유니크 제약 조건 생성  
<br/><br/><br/>

## 02. 데이터베이스 스키마 자동 생성
객체 매핑을 해두면 `JPA`가 자동으로 애플리케이션 실행 시점에 `DDL`을 자동으로 생성해줌
- 객체 중심의 개발이 가능함
- `DB 방언`을 활용해 사용되는 `DB`의 적절한 `DDL 생성`
- 이렇게 생성된 `DDL`은 `개발 시점`에서만 사용한다.
  - 운영 시 사용하면 클일남!!
  - 운영 서버에서는 사용되지 않거나, 사용하더라도 반드시 다듬어서 사용해야 한다.
### DB Schema 자동 생성 속성
현재 프로젝트의 `persistence.xml`에 `hibernate.hbm2ddl.auto` 옵션을 통해 속성을 적용 할 수 있다.
- `create`: 실행 시점에 엔티티와 매핑된 테이블을 `삭제(DROP)`하고 `새로 생성(CREATE)`한다.
- `create-drop`: `create`와 같으나 테이블 `삭제(DROP)`가 종료 시점에도 수행된다.
  - 해당 옵션은 보통 `TEST CASE`를 통한 테스트 시 사용된다.
- `update`: 변경 사항만 반영되며 `추가`에 대한 수정만 적용된다.
  - `삭제`에 대한 부분은 적용되지 않음
  - `운영 DB`에는 사용하면 안되는 옵션
- `validate`: `엔티티`와 `테이블`이 정상 매핑 되었는지를 체크해 줌
- `none`: 해당 옵션을 사용하지 않는다는 명시적 표기  
<br/><br/>

### DB Schema 자동 생성 주의
- `운영장비`에는 `절대!` `create, create-drop, update`를 사용하면 안 됨
- `개발장비`에만 `create, update`를 사용하며 보통 아래처럼 사용함
  - `개발 초기`: `create` 또는 `update`
  - `테스트 서버(여러 개발자가 사용하는 중간 서버)`: `update` 또는 `validate`
    - 하지만 해당 경우의 조차도 `update`는 피하도록 하자
  - `스테이징과 운영서버`: `validate` 또는 `none`  
<br/><br/>

### DDL 생성 기능
`DDL 자동 생성`할 때만 영향을 주며, `JPA`의 실행 로직에는 영향을 주지 않음
- `제약 조건 추가`
  - ex) 회원 이름 필수, 10자를 초과하면 안 됨 → `@Column(nullable = false, length = 10)`
- `유니크 제약 조건 추가`
  - `@Table(uniqueConstraints = {@UniqueConstraints(name = "NAME_AGE_UNIQUE", columnNames = {"NAME", "AGE"})})`  
<br/><br/><br/>

## 03. 필드와 컬럼 매핑
예제에 적용 할 요구사항은 아래와 같다.
1. 회원은 `일반 회원`과 `관리자`로 구분되어야 한다.
2. 회원 `가입일`과 `수정일`이 있어야 한다.
3. 회원 `설명을 위한 필드`가 있어야 한다.
   - 해당 필드는 길이 제한이 없다.  
### 매핑 어노테이션 정리
`hibernate.hbm2ddl.auto` 기준
- `@Column`: 컬럼 매핑
- `@Temporal`: `날짜 타입(Date, Calendar)` 매핑
- `@Enumerated`: `enum` 타입 매핑
- `@Lob`: `BLOB`, `CLOB` 매핑
- `@Transient`: 적용 필드를 컬럼에 매핑하지 않음 `(DB 와 연관 없는 필드)`
#### @Column
`@Column`의 속성
- `name`: 필드와 매핑 할 `테이블의 컬럼 이름` 지정, 기본 값은 `객체 필드 명`
- `nullable(DDL)`: `null` 값의 `허용 여부`를 설정
  - `false` 설정 시 `DDL 생성 시`, `not null` 제약 조건이 붙음
- `insertable, updatable`: 해당 컬럼 등록/변경 여부 설정, 기본 값 `true`
  - `true`라면 허용, `false`라면 허용하지 않음
- `length(DDL)`: 문자 길이 제약 조건, `String` 타입에만 사용, 기본 값 `255`
- `columnDefinition(DDL)`: `DB` 컬럼 정보를 직접 줄 수 있음
  - ex) varchar(100) default 'EMPTY'
  - 적용한 문구가 그대로 `DDL 문`에 들어가게 됨
- `unique(DDL)`: 한 컬럼에 간단하게 유니크 제약조건을 걸 때 사용, 하지만 잘 사용하진 않음  
<br/>

#### @Enumerated
`@Enumerated`의 속성
- `value`
  - `EnumType.ORDINAL`: `enum 순서(Integer)`를 DB 에 저장 `(기본 값)`
    - 잘못하면 해결 불가능한 버그를 생성하는 옵션으로 사용하면 안 됨
  - `EnumType.STRING`: `enum 이름(String)`을 DB 에 저장
    - 무조건 해당 옵션을 사용하게 됨  
<br/>

#### @Temporal
`@Temporal`의 속성
- `value`
  - `TemporalType.DATE`: `날짜`, `DB date 타입`과 매핑
  - `TemporalType.TIME`: `시간`, `DB time 타입`과 매핑
  - `TemporalType.TIMESTAMP`: `날짜와 시간`, `DB timestamp 타입`과 매핑
> 참고!  <br/>
> 최신 하이버네이트에서는 `LocalDate`와 `LocalDateTime`을 지원하며, 해당 타입을 사용할 때는 해당 `annotation 생략`이 가능하다.  

<br/>

#### @Lob
지정 할 수 있는 속성이 따로 없다.
- 매핑하는 필드의 타입에 따라 매핑 방식이 바뀐다.
  - `문자`라면 `CLOB 매핑`, 나머지의 경우 `BLOB 매핑`  
<br/>

#### @Transient
필드 매핑을 사용하지 않을 필드에 사용된다.
- 당연하지만 적용된 필드는 `DB`에 `저장되지 않고 조회도 되지 않는`다.
- 보통의 경우 `메모리상`에서만 임시로 어떤 값을 보관하는 필드에 사용된다.  
<br/><br/><br/>

## 04. 기본 키 매핑
사용되는 `annotation`에는 2가지가 있다.
- `@Id`, `@GeneratedValue`
### 기본 키 매핑 방법
- `직접 할당`: `@Id`만 단일 사용
- `자동 생성`: `@Id + @GeneratedValue`
  - `@GeneratedValue`의 경우 3가지 전략 방식이 존재하며 `기본 키 생성 전략에 맞춰` 옵션을 적용해 사용한다.  
<br/><br/>

### @GeneratedValue - 전략
`strategy` 속성을 통해 전략을 지정 할 수 있다.
#### GenerationType.IDENTITY
기본 키 생성을 `DB`에 위임하는 전략
- 주로 `MySQL`, `PostgreSQL`, `SQL Server`, `DB2`에서 사용 됨
- `JPA`는 보통 트랜잭션 커밋 시점에 `INSERT SQL`을 실행
- `AUTO_INCREMENT`는 `DB`에 `INSERT SQL` 실행 후 `ID 값`을 알 수 있음
- `em.persist()` 시점에 즉시 `INSERT SQL` 실행하고 `DB`에서 식별자 조회
```java
import jakarta.persistence.*;

// IDENTITY 전략 매핑 예시
@Entity
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
}
```  
<br/>

#### GenerationType.SEQUENCE
`DB Sequence`는 유일한 값을 순선대로 생성하는 특별한 `DB Object`이다.
- 주로 `Oracle`, `PostgreSQL`, `DB2`, `H2 DB`에서 사용 됨
```java
import jakarta.persistence.*;

// SEQUENCE 전략 매핑 예시
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",          // generator(식별자 생성기) 이름
        sequenceName = "MEMBER_SEQ",            // DB 에 등록되어 있는 시퀀스 이름
        initialValue = 1, allocationSize = 1)
public class Member {
  @Id
  // 전략 방식과 사용 할 generator 를 이름을 통해 지정
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
  private Long id;
}
```
- `@SequenceGenerator`가 필요하며 해당 `annotation`의 속성은 아래와 같다.
  - `name`: `generator 이름` 지정, 필수 값이다.
  - `sequenceName`: `DB`에 등록되어 있는 `시퀀스 이름`
  - `initialValue`: `DDL 생성 시`에만 사용 되며, 생성 시 시작하는 값을 지정
    - 기본 값은 `1`이다.
  - `allocationSize`: 한 번의 시퀀스 호출에 `증가하는 수를 지정(성능 최적화에 사용 됨)`
    - `DB 시퀀스` 값이 하나씩 증가하도록 설정되어 있다면 반드시 `1` 설정해야 한다.
    - 왜냐하면 해당 속성의 기본 값이 `50`이기 때문이다.
    - 해당 속성은 `동시성 이슈 없이 다양한 문제들도 해결 가능`하게 한다.
      1. `next call` 한 번에 지정된 값 만큼의 사이즈를 `DB`에 올려둔다.
      2. 그런 후, 사용 할 때는 메모리에서 `1`씩 사용하고 `DB`에 올려둔 만큼 메모리를 사용했다면 이 때 `next call` 한 번 더 호출 한다.
    - 적정 지정 값은 `50 ~ 100`라고 한다.
<br/>

#### GenerationType.TABLE
`키 생성 전용 테이블`을 하나 만들어 `DB Sequence`를 흉내낸 전략
- 모든 `DB`에 적용 할 수 있다는 장점이 있음에도, `성능 이슈 문제`가 따라와 잘 사용 되지 않음
- 나중에 사용 할 필요가 있다면 좀 더 알아보도록 하자.  
<br/><br/>

### 권장하는 식별 전략
`기본 키`의 경우 `null`이 아니어야 하며, 유일하고 `절대 변하면` 안 된다.
- 이러한 특성 때문에 `자연키`에서는 해당 조건을 만족하는 대상을 찾기 어렵다.
- 그러므로 우리는 `대리(대체)키`를 사용 할 필요가 있다.
- `권장 방식`: `Long 형` + `대체키` + `키 생성전략 사용`