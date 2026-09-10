# Crimson Citadel 게임 저장 서버

Spring 입문 과제로 구현한 게임 체크포인트 저장 서버. 전투 계산, 적 배치, 보상 등은 클라이언트가 담당하고, 서버는 게임과 덱을 저장·조회하는 CRUD와 시즌 랭킹 조회를 제공한다.

## 요구 사항

- JDK 21
- Docker (MySQL 실행용)

## 실행 방법

### 1. MySQL 컨테이너 실행

```bash
docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=12345678 mysql:8.4
```

### 2. 데이터베이스 생성

```bash
docker exec -it mysql mysql -uroot -p12345678 -e "CREATE DATABASE crimsoncitadel;"
```

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

Windows에서는 `gradlew.bat bootRun`을 사용한다.

`src/main/resources/application.properties`에 위 컨테이너 기준(계정 `root`, 비밀번호 `12345678`, 데이터베이스 `crimsoncitadel`)의 접속 정보가 이미 설정되어 있어 별도 설정 없이 바로 실행된다.

### 4. 접속

브라우저에서 `http://localhost:8080`으로 접속하면 게임 화면이 표시된다.

## 레벨별 진행 내용

과제 진행 중 겪은 문제와 해결 과정은 아래 글에 정리했다. 여기서는 레벨별 요구사항과 수정 내용만 간단히 정리한다.

- [Spring 입문 프로젝트: 붉은 달의 성채 서버 구현(필수 항목)](https://velog.io/@gwangmin-kim/Spring-%EC%9E%85%EB%AC%B8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%B6%89%EC%9D%80-%EB%8B%AC%EC%9D%98-%EC%84%B1%EC%B1%84)
- [Spring 입문 프로젝트: 붉은 달의 성채 서버 구현(도전 항목)](https://velog.io/@gwangmin-kim/Spring-%EC%9E%85%EB%AC%B8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%B6%89%EC%9D%80-%EB%8B%AC%EC%9D%98-%EC%84%B1%EC%B1%84%EB%8F%84%EC%A0%84-%ED%95%AD%EB%AA%A9)

### 필수 항목

| 레벨 | 요구사항 | 수정 내용 |
| --- | --- | --- |
| 1 | Docker MySQL 연결 | `application.properties` 신규 작성. 데이터소스 접속 정보와 `ddl-auto=update`(재시작해도 데이터 유지) 설정 |
| 2 | 빈 등록 고치기 | `GameService`에 누락된 `@Service` 추가 |
| 3 | RESTful 경로 맞추기 | 게임 목록 조회 매핑 오타 수정 (`/game` → `/games`) |
| 4 | `@Transactional` 버그 고치기 | `createGame()`의 `@Transactional(readOnly = true)`를 `@Transactional`로 수정 |
| 5 | 요청 검증과 응답 DTO | `CardResponse` 필드·생성자 완성, `RunCardRequest`에 Bean Validation 어노테이션(`@NotBlank`, `@Min`/`@Max`) 추가 |
| 6 | 보상 카드 선택과 진행 저장 | `PUT /games/{gameId}/progress` 컨트롤러 구현 |
| 7 | 목록·상세 조회 | `GameSummaryResponse` 작성, 목록·상세 조회 서비스 로직 구현. `@EnableJpaAuditing`/`BaseTimeEntity`로 `createdAt`/`updatedAt` 응답 포함. 목록 조회 정렬(id 내림차순) 및 조회 진입점 통일 |
| 8 | 변경 감지로 이름 수정, 자식부터 삭제 | 이름 변경 API(더티 체킹 기반) 구현, 삭제 API 구현(자식 RunCard를 먼저 삭제한 뒤 게임 삭제) |

### 도전 항목

| 레벨 | 요구사항 | 수정 내용 |
| --- | --- | --- |
| 9 | 끝난 게임 덮어쓰기 막기 | `updateProgress()`에 `isFinished()` 체크 추가, 끝난 게임에 대한 저장 요청을 409로 차단 |
| 10 | 전역 예외 처리 | `GlobalExceptionHandler`에 `GameNotFoundException`(404)·`GameFinishedException`(409) 핸들러 추가, 서비스의 `ResponseStatusException` 사용을 두 예외로 교체 |
| 11 | N+1 없는 카드 수 집계와 저장 시간 | JPQL `GROUP BY` + DTO 프로젝션(`DeckCount`)으로 게임별 카드 수를 한 번의 쿼리로 집계. 목록 조회 쿼리 수를 게임 개수와 무관하게 2회로 고정 |
| 12 | 랭킹 | 외부 랭킹 API 응답 DTO 설계, `RestClient` 기반 클라이언트 구현. 레코드별 유효성 검증(`RankingRecordValidator`), 정렬 및 플레이어당 최고 기록 선별 로직 구현. 외부 API 실패나 응답 이상 시 404를 반환해 게임 클라이언트가 랭킹 패널만 숨기고 정상 진행되도록 처리 |
