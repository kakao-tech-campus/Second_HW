# 📚 API 명세서

## Login

| Method | Endpoint | Description   | Parameters | Request Body | Response | Status Code |
|--------|----------|---------------|------------|--------------|----------|-------------|
| POST   | /login   | 로그인 처리    | 없음       | `{ "email": string, "password": string }` | "로그인 성공" | 200 OK |
| POST   | /logout  | 로그아웃 처리 | 없음       | 없음         | "로그아웃 성공" | 200 OK |

---

## User

| Method | Endpoint         | Description      | Parameters | Request Body | Response | Status Code |
|--------|------------------|------------------|------------|--------------|----------|-------------|
| POST   | /users/signup    | 회원가입         | 없음       | `{ "userName": string, "email": string, "password": string }` | `{ "id": long, "userName": string, "email": string, "createdAt": string, "updatedAt": string }` | 200 OK |
| GET    | /users           | 유저 목록 조회   | 없음       | 없음         | `[ { "id": long, "userName": string, "email": string, "createdAt": string, "updatedAt": string }, ... ]` | 200 OK |
| GET    | /users/{id}      | 단일 유저 조회   | Path: id (Long) | 없음    | `{ "id": long, "userName": string, "email": string, "createdAt": string, "updatedAt": string }` | 200 OK |
| PUT    | /users/me        | 내 정보 수정     | Session: LOGIN_USER (Long) | `{ "userName": string, "email": string, "password": string }` | `{ "id": long, "userName": string, "email": string, "createdAt": string, "updatedAt": string }` | 200 OK |
| DELETE | /users/me        | 회원 탈퇴        | Session: LOGIN_USER (Long) | 없음 | 없음 | 200 OK |

---

## Schedule

| Method | Endpoint            | Description           | Parameters | Request Body | Response | Status Code |
|--------|---------------------|-----------------------|------------|--------------|----------|-------------|
| POST   | /schedules          | 일정 생성             | Session: LOGIN_USER (Long) | `{ "title": string, "content": string }` | `{ "id": long, "userId": long, "title": string, "content": string, "createdAt": string, "updatedAt": string }` | 200 OK |
| GET    | /schedules          | 일정 목록 조회        | 없음       | 없음         | `[ { "id": long, "userId": long, "title": string, "content": string, "createdAt": string, "updatedAt": string }, ... ]` | 200 OK |
| GET    | /schedules/{id}     | 단일 일정 조회        | Path: id (Long) | 없음     | `{ "id": long, "userId": long, "title": string, "content": string, "createdAt": string, "updatedAt": string }` | 200 OK |
| PUT    | /schedules/{id}     | 일정 수정             | Path: id (Long)<br>Session: LOGIN_USER (Long) | `{ "title": string, "content": string }` | `{ "id": long, "userId": long, "title": string, "content": string, "createdAt": string, "updatedAt": string }` | 200 OK |
| DELETE | /schedules/{id}     | 일정 삭제             | Path: id (Long)<br>Session: LOGIN_USER (Long) | 없음 | 없음 | 200 OK |
| GET    | /schedules/page     | 일정 페이지 조회 (페이징) | Query: page (int, default: 1), size (int, default: 10) | 없음 | `Page<[ { "id": long, "title": string, "content": string, "commentCount": int, "createdAt": string, "updatedAt": string, "userName": string } ]>` | 200 OK |

---

> **참고**
> - `Session: LOGIN_USER (Long)`: 로그인된 사용자의 ID가 세션에 저장되어 있다는 의미입니다.
> - `Page<...>`: 페이징 처리된 결과를 의미합니다.
> - 각 엔드포인트의 Request/Response는 JSON 포맷입니다.


# ERD 설계
![스크린샷 2025-06-01 204830](https://github.com/user-attachments/assets/cb361abc-1911-44ed-bcf9-c8906f948ed6)



# DB
## 📊 데이터베이스 테이블 정의
```
users 테이블
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

schedules 테이블
CREATE TABLE schedules (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  content VARCHAR(255) NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

