## 🌱 Team Convention

| Tag Name | Description |
| --- | --- |
| feat | 새로운 기능을 추가할 때 사용 |
| fix | 버그를 수정할 때 사용 |
| style | 코드 포맷 변경, 세미콜론 누락 등 로직 변경이 없는 경우 사용 |
| refactor | 프로덕션 코드를 리팩토링할 때 사용 |
| comment | 주석을 추가하거나 변경할 때 사용 |
| docs | 문서를 수정할 때 사용 |
| test | 테스트 코드 작성, 리팩토링, 실제 코드 변경 없이 테스트만 추가할 때 사용 |
| chore | 빌드 업무 수정, 패키지 매니저 수정 등, 실제 코드 변경이 없는 경우 사용 |
| rename | 파일 또는 폴더명을 수정하거나 이동할 때 사용 |
| remove | 파일 삭제 작업만 수행할 때 사용 |

<br>

- Commit 시 `Tag Name` 은 소문자로 통일합니다. (Feat -> feat, Docs -> docs)
  ```text
  feat: 회원 가입 기능 구현
  
  SMS, 이메일 중복 확인 API 개발
  ```
- branch 이름은 목적에 따라 `tag name + 기능명` 으로 작성합니다.
  ```text
  feat/get-notification, fix/sms-notification-bug
  ```
- Pull Request 이름은 `{이슈번호}:[FEAT] {기능}` 으로 작성합니다.
  ```
  1:[FEAT] 알림 정보 조회 API 구현
  ```
- Issue 이름은 `[FEAT] {기능}` 으로 작성합니다.
  ```
  [FEAT] 알림 정보 조회 API 구현
  ```