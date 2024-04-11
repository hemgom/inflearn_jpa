# 섹션 10. 객체지향 쿼리 언어 1 - 기본 문법
## 01. 소개
`JPA`는 다양한 쿼리 방법을 지원한다.
- `JPQL`
- `JPA Criteria`
- `QueryDSL`
- `네이티브 SQL`
- `JDBC API 직접 사용, MyBatis, SprginJdbcTemplate 함께 사용`
### JPQL 소개
`JPA`가 제공하는 `SQL`을 추상화한 `객체 지향 쿼리 언어`
- 가장 단순한 조회 방법
  - `EntityManager.find()`
  - `객체 그래프 탐색`: `a.getB().getC()`  
<br/>

- `JPA`를 사용하게 되면 `엔티티 객체`를 중심으로 개발을 하게 된다.
  - `JQPL` 또한 `엔티티 객체를 대상`으로 쿼리
    - `SQL`의 경우 `DB 테이블을 대상`으로 쿼리
  - 그렇기에 `검색`을 할 때도 테이블이 아니라 `엔티티 객체`를 대상으로 검색하게 된다.
    - 이렇게 되면 고민되는 것이 `조건이 있는 검색`이다.
      - 조건에 포함되는 테이블을 `모두 객체로 변환해 검색`하는 것은 `불가능, 비효율`이다.
      - 그래서 필요 데이터만 `DB`에서 불러오기 위해 `검색 조건이 포함된 SQL`이 필요하다.  
<br/>

- `SQL 문법`과 유사하며 `ANSI 표준 SQL` 지원 문법은 모두 지원
  - like `SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN`
#### 사용
```java
String jpql = "select m From Member m where m.name like '%hello%'";

List<Member> result = em.createQuery(jpql, Member.class).getResultList();
```
- `JQPL`쿼리를 작성하고 `em.createQuery()`를 통해 쿼리를 생성한다.
  - `getResultList()`를 사용해 조회 결과를 컬렉션으로 반환 받는다.
- 작성한 쿼리에 `Member m`은 엔티티 객체를 말한다.
  - `JPQL`이 객체 지향 쿼리 언어기 때문이다.  
<br/>

#### 정리
`JPQL`은 테이블이 아니라 `객체`를 대상으로 검색하는 `객체 지향 쿼리(=객체 지향 SQL)`
- `SQL`을 추상화하기 때문에 `특정 DB SQL`에 의존하지 않음  
<br/><br/>

### Criteria
`JPA` 공식 스펙에 포함된 기능이며 문자가 아닌 `자바코드`로 `JPQL`을 작성 할 수 있다.
```java
//Criteria 사용 준비
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Member> query = cb.createQuery(Member.class);

// 루트 클래스 (조회 시작 클래스)
Root<Member> m = query.from(Member.class);

// 쿼리 생성
CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
List<Member> result = em.createQuery(cq).getResultList();
```
- `JPQL` 빌더 역할을 한다.
- `자바코드`로 작성하기에 컴파일 단계에서 오류를 잡을 수 있다.
- 하지만, `너무 복잡`하고 `실용성이 없다`.
  - 그래서 실무에서는 `Criteria`보다 `QueryDSL`을 사용하길 권장한다.  
<br/><br/>

### QueryDSL
문자가 아닌 `자바코드`로 `JPQL`을 작성 할 수 있다.
```java
// JPQL : select m from Member m where m.age > 18
JPAQueryFactory query = new JPAQueryFactory(EntityManager);
QMember m = QMember.member;

// 쿼리 생성
List<Member> result = query
        .selectFrom(m)
        .where(m.age.gt(18))
        .orderBy(m.name.desc())
        .fetch();
```
- `JPQL` 빌더 역할을 한다.
- `자바코드`로 작성하기에 컴파일 단게에서 오류를 잡을 수 있다.
- `동적 쿼리 작성이 편리`하며 `단순하고 쉽다는` 강점이 있음
  - 실무 사용을 굉장히 권장함  
<br/><br/>

### 네이티브 SQL
`JPA`가 제공하는 `SQL`을 직접 사용하는 기능
```java
String sql = "SELECT ID, AGE, TEAM_ID, NAME FROM MEMBER WHERE NAME = `kim'";

List<Member> result = em.createNativeQuery(sql, Member.class).getResultList();
```
- `JPQL`로 해결 할 수 없는 `특정 DB에 의존적인 기능`을 사용 할 때 쓰는 기능
  - ex) 오라클의 `CONNECT BY`, 특정 `DB`만 사용하는 `SQL 힌트`  
<br/><br/>

### JDBC 직접 사용, SpringJdbcTemplate 등
`JPA`를 사용하면서 `JDBC 커넥션`을 직접 사용하거나 `SpringJdbcTemplate`, `Mybatis` 등을 함께 사용 가능하다.
- 단, 영속성 컨텍스트를 `적절한 시점에 강제로 플러시` 할 필요가 있다.
  - `JPA`를 통해 `쿼리`를 날리는 경우 자동적으로 `flush()`가 수행된다.
  - 만약 `JPA`를 통하지 않고 `쿼리`를 날린다면, 아직 `DB`에 반영 되지 않은 데이터이기에 아무것도 조회 할 수 없다.
    - ex) `JPA`를 우회해 `SQL 실행 전` 영속성 컨텍스트 `수동 플러시`