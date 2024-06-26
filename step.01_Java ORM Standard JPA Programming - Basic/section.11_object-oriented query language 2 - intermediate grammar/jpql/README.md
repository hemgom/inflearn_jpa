# 섹션 11. 객체지향 쿼리 언어 2 - 중급 문법
## 01. 경로 표현식
`.(점)`을 통해 객체 그래프를 탐색하는 것
- `상태 필드(status field)`: 단순히 값을 저장하기 위한 필드
  - ex) `m.username`
  - `경로 탐색의 끝`이기 때문에 이후 `탐색이 불가능` 하다.  
<br/>

- `연관 필드(association field)`: 연관관계를 위한 필드
  - 단일 값 연관 필드
    - ex) `m.order`, `m.order.orderAmount`
    - `묵시적 내부 조인(inner join)`이 발생하며, `탐색이 가능`  
<br/>

  - 컬렉션 값 연관 필드
    - ex)`m.orders`
    - `묵시적 내부 조인(inner join)`이 발생하며, `탐색이 불가능`
    - 만약, 탐색을 하고 싶다면 `FROM 절`에 `명시적 조인`을 작성하고 `별칭`을 통해 탐색한다.  
> _`명시적 조인`과 `묵시적 조인`_  
>> 명시적 조인은 `join` 키워드를 통해 직접 사용하는 걸 말 한다.  
>
>> 묵시적 조인은 `경로 표현식`을 통해 묵시적으로 `SQL 조인`이 발생하는 걸 말 한다.  
>>  - 묵시적 조인인 경우에는 `내부 조인(inner join)`만 가능하다.  
>>  - 항시 내부 조인이 발생하기에 `FROM 절`에 영향을 주게 된다.  

<br/>

### 결론
기본적으로 `명시적 조인을 사용`하도록 하자.
- `조인`은 `SQL 튜닝`에 중요한 포인트이다.
- 묵시적 조인의 경우 `JOIN`이 일어나는 상황을 한 눈에 파악하기가 어렵다.  
<br/><br/><br/>

## 02. 페치 조인 1- 기본
`페치 조인`은 실무에서 `엄청 중요`하기에 반드시 짚고 가도록 하자.
- `SQL join`의 종류가 아니며, `JPQL`에서 `성능 최적화`를 위해 제공하는 기능이다.
- 연관 엔티티나 컬렉션을 `SQL query` 한 번에 `함께 조회하는 기능`으로 보면 된다.
### 엔티티 페치 조인
`select {별칭} from {엔티티} {별칭} [left [outer]|inner] join fetch {조인 경로}`
```
// JPQL
select m from Member m join fetch m.team

// SQL
SELECT M.*, T.*
FROM MEMBER M
INNER JOIN TEAM T ON M.TEAM_ID=T.ID
```
- ex) 회원 정보와 회원이 속한 팀 정보도 같이 조회 할 경우
- 페치 조인으로 회원과 팀을 함께 조회하기 때문에 지연로딩 적용이 안 됨
<br/><br/>

### 컬렉션 페치 조인
```
// JPQL
select t from Team t join fetch t.members where t.nam='teamA'

// SQL
SELECT T.*, M.*
FROM TEAM T
INNER JOIN MEMBER M ON T.ID=M.TEAM_ID
WHERE T.NAME='teamA'
```
- 일대다 관계
- ex) 팀A 에 속한 회원 정보를 같이 조회 할 경우
- 페치 조인으로 회원과 팀을 함께 조회하기 때문에 지연로딩 적용이 안 됨
- 기본적으로 중복제거가 되지 않음, `DB`에서 찾은 개수 만큼 컬렉션에 넣어서 반환해 줌
  - `일대다`의 관계에서는 결과 데이터가 `뻥 튀기` 될 수 있음
  - `다대일`의 관계에서는 결과 테이거가 `뻥 튀기` 될 일이 없음
> `하이버네이트 6`부터는 `DISTINCT` 옵션을 사용하지 않아도 애플리케이션에서 자동으로 적용 됨  

<br/><br/>

### DISTINCT
`SQL`에서 `DISTINCT`는 `중복 결과 제거` 명령어다.
- 하지만 `페치 조인`을 함께 사용 할 경우, 결과 데이터들 중 하나라도 다른 부분이 있다면 `중복 정보`라 판단하지 않음 
  - 즉, 원하는 `중복 제거 결과`를 얻기 어려울 수 있다.
- `JPQL`에서는 아래의 2가지 기능을 제공
  - `SQL`에 `DISTINCT`를 추가
  - `애플리케이션에서` 엔티티의 `중복을 제거`
    - 같은 식별자를 가진 엔티티 제거하기 때문에 최대한 데이터 중복을 제거 할 수가 있음  
<br/><br/>

### 일반 조인과 페치 조인 차이점
- `join`: 연관 엔티티를 함께 조회하지 않음
  - `JPQL`은 결과 반환 시 연관관계를 고려하지 않음
  - `select 절`에 지정한 엔티티에 대해서만 조회 함  
<br/>

- `join fetch`: 연관 엔티티를 함께 조회 함 (즉시 로딩)
  - 객체 그래프를 `SQL 한 번`으로 조회하는 개념  
<br/><br/><br/>

## 03. 페치 조인 2 - 한계 
`페치 조인`의 특징과 한계
- 페치 조인 대상에는 `별칭을 주지 말 것`
  - 하이버네이트에서는 별칭을 줄 수 있게 기능을 제공하지만 가급적 사용하지 말 것
  - `데이터 정합성` , `객체 그래프 사상 불일치` 등의 문제가 있기 때문이다.  
<br/>

- `둘 이상`의 컬렉션은 `페치 조인` 할 수 없음
- 컬렉션 페치 조인 시 `페이징 API(setFirstResult, setMaxResults)`를 사용 할 수 없음
  - 페이징에 따라 조회한 데이터의 무결성이 깨질 수가 있음.
    - 예시로 `팀 A`에는 회원이 둘 인데, 페이징을 `1`로 지정하면 결국 `팀 A`에는 회원 하나만 있다는 결과가 됨
  - `일대일`, `다대일` 같은 `단일 값 연관 필드`들은 `페치 조인`을 하더라도 `페이징 가능`
  - 하버네이트의 경우 경고 로그를 띄워주고 메모리에서 페이징 한다.
    - 굉장히 위험하다, `하버네이트 자체에서도 경고` 하지만 쿼리를 보면 `팀에 대한 데이터를 전부` 끌어 옴
    - 예로 10만 건의 팀 데이터가 있다면 해당 정보를 다 가져와 메모리에서 페이징하기에 `장애 나기 딱 좋은 환경`이 됨  
<br/>

- 연관 엔티티들을 `SQL 한 번`으로 조회 가능 `(=성능 최적화)`
- 엔티티에 직접 적용하는 `글로벌 로딩 전략`보다 우선순위가 높음
  - ex) `@OneToMany(fetch = FetchType.LAZY)`보다 우선순위가 높다.
  - 그렇기에 실무에선 보통 기본적으로 `글로벌 로딩 전략`을 `지연 로딩(LAZY)`로 가져 감
  - 그리고 최적화가 필요한 곳에 `join fetch`를 적용한다.  
### 정리
`페치 조인`은 `만능`이 아님, 모든 걸 해결 할 순 없음
- `객체 그래프 유지` 시, 사용하면 효과적이다.
- 여러 테이블을 조인해 엔티티의 모양이 아닌 `전혀 다른 결과가 필요`하다면 `일반 조인`을 사용하자
  - 이후 필요한 데이터들만 조회해서 `DTO`로 반환하는 것이 효과적이다.
- 연관 엔티티가 `쿼리 검색 조건에 필요`하나 `실제 데이터를 필요로 하지 않는다`면 `일반 조인` 사용
- 연관 엔티티의 `실제 데이터들이 필요`하다면 `페치 조인` 사용  
<br/><br/><br/>

## 04. 다형성 쿼리
### TYPE
조회 대상을 특정 자식으로 한정
```
// JPQL
select i from Item i where type(i) IN (Book, Movie)

// SQL
select i from i where i.DTYPE in ('B', 'M')
```  
<br/><br/>

### TREAT
`JAVA`의 타입 캐스팅과 유사함
```
// JPQL
select i from Item i where treat(i as Book).author = 'kim'

// SQL
select i.* from Item i where i.DTYPE = 'B' and i.author = 'kim'
```
- 상속 구조에서 부모 타입을 특정 자식 타입으로 다룰 때 사용
- `FROM, WHERE, SELECT(하이버네이트 지원)`절에서 사용 가능  
<br/><br/><br/>

## 05. 엔티티 직접 사용
`JPQL`에서 엔티티를 직접 사용하면 `SQL`에서는 해당 엔티티의 `기본 키 값` 또는 `외래 키 값`을 사용한다.
```
// JPQL
select count(m.id) from Member m  // 엔티티의 아이디를 사용
select count(m) from Member m     // 엔티티를 직접 사용

// SQL(JPQL 둘다 같은 다음 SQL 실행)
select count(m.id) as cnt from Member m
```
- 당연해 보이면서도, 모르면 당황스러울 유용한 기능이다.  
<br/><br/><br/>

## 06. Named 쿼리
미리 정의하여 `이름을 부여`해두고 사용하는 `JPQL`
- `정적 쿼리`에만 사용 가능
- `annotation`이나 `xml`에 정의 가능
  - `xml`이 항상 `우선순위를 높게` 가짐
  - 애플리케이션 `운영 환경에 따라` 다른 XML 을 배포 가능
- 애플리케이션 로딩 시점에 `초기화 후 재사용 함`
- 애플리케이션 로딩 시점에 `쿼리 검증`
  - 가장 강력한 장점!
- 관례적으로 네이밍은 `{entity name}.추가 네임`으로 많이 사용 함
- 하지만 실무에서는 `Spring Data JPA`를 섞어서 많이 사용하므로 그렇게 사용 할 일은 없어 보임  
<br/><br/><br/>

## 07. 벌크 연산
`쿼리 한 번`으로 다수의 `SQL` `delete, update 문`을 처리
- `executeUpdate()`를 사용한다.
  - 반환 결과는 `영향을 받는 엔티티의 개수`이다.
  - `UPDATE, DELETE` 지원
  - `INESRT(insert into .. select, 하이버네이트 지원)`
> _주의점!_  <br/>
> `벌크 연산`의 경우 `영속성 컨텍스트`를 무시하고 바로 `DB`에 직접 쿼리를 날린다.  <br/>
> 그래서 벌크 연산만 사용한다면 상관이 없겠지만, 연산 후 `한 트랜잭션 내`에서 최신화된 데이터를 조회하기 위해서는 `영속성 컨텍스트 초기화`를 항상 염두해 두어야 한다.