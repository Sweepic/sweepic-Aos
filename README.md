# SweepicFE

## 기술 스택
- **언어**: Kotlin
- **아키텍처**: MVVM (Model-View-ViewModel)
- **DI 라이브러리**: Hilt
- **네트워크 통신**: Retrofit, OkHttp
- **이미지 처리**: Glide
- **비동기 처리**: Kotlin Coroutines
- **UI 컴포넌트**: Material Components, Navigation Component

- ### 주요 라이브러리
| 라이브러리      | 목적                                        | 버전          |
|----------------|--------------------------------------------|--------------|
| **Retrofit**   | REST API 통신                             | 2.9.0        |
| **Glide**      | 이미지 로딩 및 캐싱                        | 4.14.2       |
| **Hilt**       | 의존성 주입 (Dependency Injection)         | 2.49         |
| **OkHttp**     | HTTP 클라이언트 및 로깅                   | 5.0.0-alpha.2 |
| **Coroutines** | 비동기 작업 처리                          | 1.7.3        |
| **Navigation Component** | 화면 전환 및 인자 전달            | 2.7.7        |

---
- **[FEAT]** : 새로운 기능 구현
- **[MOD]** : 코드 수정 및 내부 파일 수정
- **[ADD]** : 부수적인 코드 추가 및 라이브러리 추가, 새로운 파일 생성
- **[CHORE]** : 버전 코드 수정, 패키지 구조 변경, 타입 및 변수명 변경 등의 작은 작업
- **[DEL]** : 쓸모없는 코드나 파일 삭제
- **[UI]** : UI 작업
- **[FIX]** : 버그 및 오류 해결
- **[HOTFIX]** : issue나 QA에서 문의된 급한 버그 및 오류 해결
- **[MERGE]** : 다른 브랜치와의 MERGE
- **[MOVE]** : 프로젝트 내 파일이나 코드의 이동
- **[RENAME]** : 파일 이름 변경
- **[REFACTOR]** : 전면 수정
- **[DOCS]** : README나 WIKI 등의 문서 개정

  
## Branch Naming Convention
- **기능별 브랜치**:
  - `feature/[SWEP-*]`
  - 예: `feature/[SWEP-00]`, `add/[SWEP-00]`, `ui/[SWEP-00]`
 
- **Main Branch**:
  - `main`: 배포 가능한 안정된 코드
  - `develop`: 개발 코드 통합

---

## Issue Convention
### 이슈 생성
![image](https://github.com/user-attachments/assets/0af8bfab-5b04-4abd-981a-c680ef4d40b0)

### 템플릿 작성
- Assignees 를 자신으로 설정
- Labels 를 선택해 What / name 설정
#### Issue 제목
`[SWEP-*] where / what`

![image](https://github.com/user-attachments/assets/580fb832-51a5-4da9-b865-e718d26b36a3)

### 브랜치 생성
- 브랜치 conevention 에 맞게 브랜치 설정

---
## Commit Convention
#### Summery 작성
`[SWEP-*] where / what`

#### Description
추가 설명이 필요할 경우 작성

#### Commit 시 주의사항
**절대** AppKey 등 민감한 정보가 포함된 파일 올리지 않기

---
## PR(Pull Request) Convention
### PR 생성
![image](https://github.com/user-attachments/assets/f2603e86-4506-4d0f-99c7-a30cfa6ede24)

### 템플릿 작성
- Reviewer(리뷰어) 지정
  - 자신이 아닌 팀원들로 설정
- Assignees(담당자) 지정
  - 자신으로 설정
- Label 지정
  - What / name 설정
#### PR 제목
`[SWEP-*] where / what`
![image](https://github.com/user-attachments/assets/f1abed25-7f21-4bb8-8307-1d05e53cf8b8)

### 코드 리뷰 진행
- 팀원 중 최소 1명의 코드 리뷰가 필요
- 팀원 모두가 코드 리뷰를 하도록 권장

---
## Code Convention

### XML 네이밍

### xml file

- **`Snake Case`**
    - activity_main
    - item_main
    - menu_main

### drawable

- **`Snake Case`**
    - Button 아이콘 ⇒ ic_[where]_[what]
        - 공용으로 사용될 아이콘(툴바, 앱바 등)은 [where] 생략
            - ex) ic_back
    - 이미지 ⇒ img_[where]_[what]
    - 배경 ⇒ bg_[where]_[what]

### shape

- shape_[shape]_[radiusNum]_[color]_[fill/line].xml
- shape ⇒ rect, tri, circle
- 만약 위에만 radius 10dp 줘야하는 경우 [radiusNum]에 top10을 넣어주자 (아래는 bot)
    - ex) shape_rect_10_white000_fill
    - ex) shape_rect_top10_white000_fill_gray100_line

### selector

- sel_[where]_[type]_[what].xml
    - ex) sel_main_color_bnv_menu
    - ex) sel_main_icon_bnv_profile

### Component

[view]_[where]_[description] (뷰이름_화면_무엇을 나타내는지)

ex) tv_main_title

- TextView ⇒ tv
- EditText ⇒ et
- RecyclerView ⇒ rv
- ImageView ⇒ iv
- Button, ImageButton ⇒ btn
- xxLayout ⇒ layout
- ViewPager ⇒ vp
- TabLayout ⇒ tab
- Chip ⇒ chip
- Toolbar ⇒ toolbar
- ScrollView ⇒ sv
- BottomNavigation ⇒ bnv
- FragmentContainerView ⇒ fcv
- View(밑줄, 경계선, 라인) ⇒ view
- FloatingActionButton ⇒ fab
- CardView ⇒ cv

## Kotlin 네이밍

### Class & Interface

- **`Upper Camel Case`**
    - ex) LoginActivity, TodoFragment
    

### 함수와 변수

- **`Lower Camel Case`**
    - **initXXX()** : 초기화 함수 이름
        - **init[View]ClickListener** : 클릭 리스너 설정
        - **init[NameView]Adapter** : 리사이클러뷰 어댑터 설정
            
            ```kotlin
            fun **initPresentAdapter**(){
            		binding.nameRv.adapter = PresentAdapter()
            }
            ```
            
    - **updateXXX()** : 갱신 함수 이름
    - **removeXXX()** : 삭제 함수 이름
    - **setupXXX()** : ViewModel을 observe()할 때 모아놓는 함수 이름
        - setup[ValueName]()
    - **getXXX()** : Return이 있는 데이터를 불러올때 함수 이름
    - **findXXX()** : 특정 객체를 찾는 함수 이름
    - **복수형을 가져올때는 뒤에 s를 붙인다:** getBrands() 꼴
    - **Raw 값으로부터 enum을 찾을 때 함수 이름은 `find()`로 한다.**
- 서버 통신 함수
    - **getXXX()** → getUserList()
    - **deleteXXX()** → deleteUser()
    - **putXXX()** → putProfile()
    - **postXXX()** → postMusic()

### string, color, style

- **`Snake Case`**
- [where]_[what]

---

## 버전 설정

### Android Studio 버전
- **Koala (최신 버전)**

### SDK 버전
- **Compile SDK**: 34
- **Target SDK**: 34
- **Min SDK**: 24

### 테스트 환경
- IDE 내 **Emulator** 사용
- 다양한 **Emulator** 환경을 이용해 여러 기기에서도 적용되는 지 확인
- 실제 디바이스 테스트 권장
