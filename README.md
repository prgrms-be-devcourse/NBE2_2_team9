# 💊 Pill-Buddy

Pill-Buddy는 사용자가 매일 복용해야 할 약이나 영양제를 관리하고, 복용 시간에 맞춰 알림을 받을 수 있도록 돕는 서비스입니다.<br> 사용자는 보호자를 추가하여 약 복용 여부를 확인하고 관리할 수 있으며, [식품의약품안전처](https://www.mfds.go.kr/index.do)의 공공 API 를 통해 약의 주요 정보들을 조회할 수 있습니다.

<br>

## 👬 팀 소개
|                                        Backend                                         |                                        Backend                                         |                           Backend                            |                           Backend                            |                           Backend                            |
|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:-------------------------------------------------------------:|:-------------------------------------------------------------:|:-------------------------------------------------------------:|
| ![진우](https://github.com/user-attachments/assets/43b44089-e9a1-4e6a-89a1-b1bc9a8e8a4a) | ![홍제](https://github.com/user-attachments/assets/6ab12390-7dd5-46c7-88dd-b808a86de5dd) | ![성겸](https://github.com/user-attachments/assets/d12b5ad8-95b9-4e1c-a207-e99a7a123e38) | ![현우](https://github.com/user-attachments/assets/7e75a7a3-d77b-44bd-8dcb-080378caf6e9) | ![소희](https://github.com/user-attachments/assets/b729e0e6-2724-471f-b544-a31e46d0a0d6) |
|                        [이진우 (팀장)](https://github.com/jinw0olee)                        |                           [안홍제](https://github.com/hongjeZZ)                           |      [김성겸](https://github.com/xxxkyeom)      |      [신현우](https://github.com/Dia2Fan)      |      [양소희](https://github.com/MisaSohee)      |
|   프로젝트 총괄 관리<br>팀원 간 소통 조율<br>보호자 기능 구현   |                       Github 관리<br>회원 관리 기능 구현<br>JWT 인증 로직 구현                       | 프로젝트 기획<br>사용자 기능 구현 | 프로젝트 기획<br>e약은요 API를 통해 약 정보 검색 기능 구현 | 프로젝트 기획<br>coolsms API를 통해 알림 기능 구현 | 


<br>

## 🛠️ 개발 환경

<img width="1000" alt="image" src="https://github.com/user-attachments/assets/a0c99fad-cd33-4f11-bf06-46573784e48f">

<br>

## 💻 프로젝트 구조

<details>
  <summary>프로젝트 구조 보기(눌러서 보기)</summary>

```yml
├── java
│   └── com
│       └── medinine
│           └── pillbuddy
│               ├── PillBuddyApplication.java
│               ├── domain
│               │   |
│               │   ├── medicationApi # 약 정보 검색
│               │   │   ├── controller
│               │   │   │   └── MedicationApiController.java
│               │   │   ├── dto
│               │   │   │   ├── JsonForm.java
│               │   │   │   ├── MedicationDTO.java
│               │   │   │   └── MedicationForm.java
│               │   │   ├── entity
│               │   │   │   └── Medication.java
│               │   │   ├── repository
│               │   │   │   └── MedicationApiRepository.java
│               │   │   └── service
│               │   │       └── MedicationApiService.java
│               │   │
│               │   │
│               │   ├── notification # 알림
│               │   │   ├── controller
│               │   │   │   └── NotificationController.java
│               │   │   ├── dto
│               │   │   │   ├── NotificationDTO.java
│               │   │   │   └── UserNotificationDTO.java
│               │   │   ├── entity
│               │   │   │   └── Notification.java
│               │   │   ├── provider
│               │   │   │   └── SmsProvider.java
│               │   │   ├── repository
│               │   │   │   └── NotificationRepository.java
│               │   │   └── service
│               │   │       └── NotificationService.java
│               │   │
│               │   │
│               │   ├── record # 복용 기록
│               │   │   ├── dto
│               │   │   │   └── RecordDTO.java
│               │   │   ├── entity
│               │   │   │   ├── Record.java
│               │   │   │   └── Taken.java
│               │   │   ├── repository
│               │   │   │   └── RecordRepository.java
│               │   │   └── service
│               │   │       ├── RecordService.java
│               │   │       └── RecordServiceImpl.java
│               │   │
│               │   │
│               │   ├── user # 회원
│               │   │   ├── caregiver # 보호자 
│               │   │   │   ├── controller
│               │   │   │   │   └── CaregiverController.java
│               │   │   │   ├── entity
│               │   │   │   │   └── Caregiver.java
│               │   │   │   ├── repository
│               │   │   │   │   └── CaregiverRepository.java
│               │   │   │   └── service
│               │   │   │       └── CaregiverService.java
│               │   │   |
│               │   │   |
│               │   │   ├── caretaker # 사용자
│               │   │   │   ├── controller
│               │   │   │   │   └── CaretakerController.java
│               │   │   │   ├── dto
│               │   │   │   │   └── CaretakerCaregiverDTO.java
│               │   │   │   ├── entity
│               │   │   │   │   ├── Caretaker.java
│               │   │   │   │   └── CaretakerCaregiver.java
│               │   │   │   ├── repository
│               │   │   │   │   ├── CaretakerCaregiverRepository.java
│               │   │   │   │   └── CaretakerRepository.java
│               │   │   │   └── service
│               │   │   │       ├── CaretakerService.java
│               │   │   │       └── CaretakerServiceImpl.java
│               │   │   |
│               │   │   |
│               │   │   ├── controller
│               │   │   │   ├── AuthController.java
│               │   │   │   └── UserController.java
│               │   │   ├── dto
│               │   │   │   ├── JoinDto.java
│               │   │   │   ├── LoginDto.java
│               │   │   │   ├── UserDto.java
│               │   │   │   ├── UserPasswordUpdateDto.java
│               │   │   │   ├── UserType.java
│               │   │   │   └── UserUpdateDto.java
│               │   │   ├── entity # 회원 공통
│               │   │   │   ├── Role.java
│               │   │   │   └── User.java
│               │   │   |
│               │   │   |
│               │   │   ├── profile # 프로필 이미지 관련
│               │   │   │   ├── controller
│               │   │   │   │   └── ProfileController.java
│               │   │   │   ├── dto
│               │   │   │   │   └── ProfileUploadDto.java
│               │   │   │   ├── entity
│               │   │   │   │   └── Image.java
│               │   │   │   ├── repository
│               │   │   │   │   └── ImageRepository.java
│               │   │   │   └── service
│               │   │   │       ├── ProfileService.java
│               │   │   │       └── uploader
│               │   │   │           ├── CaregiverProfileUploader.java
│               │   │   │           ├── CaretakerProfileUploader.java
│               │   │   │           └── ProfileUploader.java
│               │   │   └── service
│               │   │       ├── AuthService.java
│               │   │       ├── CustomUserDetails.java
│               │   │       ├── MyUserDetailsService.java
│               │   │       └── UserService.java
│               │   │
│               │   │
│               │   └── userMedication # 사용자 약 정보
│               │       ├── controller
│               │       │   └── UserMedicationController.java
│               │       ├── dto
│               │       │   └── UserMedicationDTO.java
│               │       ├── entity
│               │       │   ├── Frequency.java
│               │       │   ├── MedicationType.java
│               │       │   └── UserMedication.java
│               │       ├── repository
│               │       │   └── UserMedicationRepository.java
│               │       └── service
│               │           ├── UserMedicationService.java
│               │           └── UserMedicationServiceImpl.java
│               │
│               │
│               └── global
│                   ├── advice # 예외 처리
│                   │   └── GlobalExceptionHandler.java
│                   │
│                   ├── config # 설정 관련
│                   │   ├── RedisConfig.java
│                   │   ├── SecurityConfig.java
│                   │   └── SwaggerConfig.java
│                   │
│                   ├── entity # 공통 엔티티
│                   │   └── BaseTimeEntity.java
│                   │
│                   ├── exception # 예외 관련
│                   │   ├── ErrorCode.java
│                   │   ├── ErrorResponse.java
│                   │   └── PillBuddyCustomException.java
│                   │
│                   ├── jwt # JWT 관련
│                   │   ├── JwtAccessDeniedHandler.java
│                   │   ├── JwtAuthenticationEntryPoint.java
│                   │   ├── JwtAuthenticationFilter.java
│                   │   ├── JwtToken.java
│                   │   └── JwtTokenProvider.java
│                   │
│                   ├── redis
│                   │   └── RedisUtils.java
│                   │
│                   └── util
│                       └── UploadUtils.java
│                   
└── resources
    ├── application-db.yml
    └── application.yml

```

</details>

<br>

## 🔗 ER Diagram
<img width="1000" alt="ERD" src="https://github.com/user-attachments/assets/a643e7fe-787c-48e1-ac56-33ba4c9d1914">

<br>
<br>

## ⭐️ Class Diagram
<img width="1000" alt="image" src="https://github.com/user-attachments/assets/c2af143a-bb44-40f8-b5fe-beec11be6533">

<br>
<br>

## 👨🏻‍💻 Sequence Diagram

#### 🔐 로그인
<img width="1000" alt="로그인시퀀스" src="https://github.com/user-attachments/assets/08db32d9-0fe0-4f63-87a3-dcfdc72271fc">

<br>

---

#### ♻️ 토큰 재발급
<img width="1000" alt="재발급시퀀스" src="https://github.com/user-attachments/assets/9afa4ff9-86b8-482b-9c17-bcad349e289b">

<br>

---

#### 💌 알림 전송
<img width="1200" alt="알림시퀀스" src="https://github.com/user-attachments/assets/240e999c-7156-4d40-9485-ee35c06c20c6">

<br>

---

#### 💊 약 정보 검색
<img width="1000" alt="약검색시퀀스" src="https://github.com/user-attachments/assets/8d620c59-19e3-420b-aba6-04f9398cd45c">

