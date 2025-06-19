# 🍽️ COOKiT
## 요리 초보를 위한 보유 식재료 기반 맞춤 레시피 큐레이터 서비스

COOKiT은 요리 초보자를 위한 AI 기반의 요리 지원 서비스로, 사용자가 보유한 식재료를 기반으로 맞춤형 레시피를 추천하고 식재료를 쉽게 관리할 수 있도록 지원합니다. OCR을 통한 식재료 자동 등록, GPT-4o Vision 기반 신선도 분석, 개인화된 레시피 추천 기능을 통해 요리의 진입장벽을 낮추고 식재료 낭비를 줄이는 것을 목표로 합니다.
## 📁 Repository 구조

- [WEFRESH-FRONT](https://github.com/2024EwhaCapstone/WEFRESH-FRONT) (React Native-ios)
- [WEFRESH-SERVER](https://github.com/2024EwhaCapstone/WEFRESH-SERVER) (SpringBoot)

---

## 📦 Source Code 설명

### Backend (🌐 Server)
- Spring Boot 기반으로 REST API를 개발하며, Docker로 컨테이너화하여 EC2 인스턴스 내에서 구동.
- Blue-Green Deployment 방식으로 두 개의 컨테이너를 운영하고, 무중단 배포를 위해 Nginx로 트래픽을 전환.
- 데이터베이스는 Amazon RDS(MySQL)를 사용하고, 이미지 및 파일 저장은 Amazon S3로 처리.


### AI 🤖
- GPT-4o mini, GPT-4o Vision, AWS Textract (OCR) , DaLLE 사용.
- GPT-4o mini를 프롬프팅하여 원하는 재료 기반 레시피를 추천.
- 사진을 넣으면 OCR이 이미지에서 텍스트를 추출하여 식재료 상세정보를 출력.
- 입력한 이미지 기반으로 식재료의 상한 부분을 탐지하고, GPT-4 Vision이 해당 이미지 분석을 통해 신선도 상태를 텍스트로 출력.
---
## 📱 How to build
① 아래 명령어로 프로젝트를 로컬 환경에 클론합니다.

`git clone https://github.com/2024EwhaCapstone/WEFRESH-SERVER.git`

② 클론한 폴더에서 build.gradle 파일을 코드 편집기(VS Code, IntelliJ 등)로 연 후, Build 버튼을 클릭하거나 ./gradlew build 명령어를 실행하여 필요한 라이브러리를 다운로드합니다.

③ src/main/resources/application.yml 파일에 데이터베이스 접속 정보, API 키 등 필요한 설정 값을 입력합니다.

④ Run 버튼을 클릭하거나 ./gradlew bootRun 명령어를 통해 프로젝트를 로컬 환경에서 실행합니다.


## 🛠️ How to test
####  iOS 시뮬레이터에서 `.app` 바이너리 실행 (빌드된 앱 테스트)

1. xcode 실행
2. `.app` 바이너리 파일 준비 (예: `000.app`)
   (cookit.app binary file 설치)
  [📦 Download cookit.zip](https://github.com/2024EwhaCapstone/Growth/releases/download/v1.0.0/cookit.zip)
3. 다음의 명령어 실행
```bash
open -a Simulator
xcrun simctl install booted ~/app파일 주소
xcrun simctl launch booted org.reactjs.native.yoonjinchoi.wefresh
```
## 📁 Folder 구조
<pre>

<code>
src/main/java/org/wefresh/wefresh_server
├── auth                      # 인증/인가 관련 로직 (로그인, JWT 등)
│   ├── controller
│   ├── dto
│   └── service
│
├── bookmark                  # 북마크 기능 (즐겨찾기, 저장 등)
│   ├── controller
│   ├── domain
│   ├── dto.response
│   ├── manager
│   ├── repository
│   └── service
│
├── common                    # 공통 모듈 (예외처리, 설정, 공통 응답 등)
│   ├── advice
│   ├── auth
│   ├── base
│   ├── config
│   ├── controller
│   ├── dto
│   └── exception
│
├── external                  # 외부 API 연동 관련 설정
│   ├── config
│   └── service
│
├── food                      # 음식 관련 도메인 (식품 정보, 이미지 등)
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── manager
│   ├── repository
│   └── service
│
├── openAi                    # OpenAI API 연동 (ChatGPT, Vision 등)
│   ├── config
│   ├── controller
│   ├── dto
│   ├── service
│   └── util
│
├── recipe                    # 레시피 정보 제공 관련 로직
│   ├── controller
│   ├── domain
│   ├── dto.response
│   ├── manager
│   ├── repository
│   └── service
│
├── todayRecipe               # 오늘의 레시피 관련 로직
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── manager
│   ├── repository
│   └── service
│
├── user                      # 사용자 정보 및 계정 관리
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── manager
│   ├── repository
│   └── service
│
└── WefreshServerApplication # Spring Boot 메인 클래스 (실행 진입점)

</code>

</pre>

