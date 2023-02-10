# patient-api
간단한 정보와 이미지를 입력, 조회, 삭제 할 수 있는 API 입니다.

# 개발 도구 및 버전
- Java 8
- Spring Boot 2.7.8
- Mysql 5.5.27
- [etc](https://github.com/zodaland/patient-api/blob/zodaland/build.gradle)

# API 명세
GET /{id} - 정보 상세 조회

GET /image/{image} - 이미지 조회

POST / - 정보 생성

DELETE / - 제거

# 구현 내용
- 데이터 베이스는 Mysql을 사용했으며 JPA를 통해 데이터를 조작합니다.
- 모든 요청은 Rest로 구현되어 URL path, parameter 혹은 json body를 통해 이루어집니다. (image의 경우 예외)
  - 저장을 제외한 모든 요청의 Content-Type은 application/json입니다.
  - 저장의 Content-Type은 multipart/form-data이며 이미지 외 데이터의 경우 Content-Type은 application/json입니다.
- 모든 응답은 code와 reason을 가질 수 있습니다. (image 조회 성공의 경우 예외)
  - code, reason은 Global 패키지의 Response 클래스로 구현 되며 각 도메인 별 응답 클래스에 확장하여 사용됩니다.
  - Response 클래스 내에 enum으로 code와 reason이 저장 되며 Response 객체 생성시 code를 통해 reason이 자동으로 입력됩니다.
  - Response 객체 생성시 일치하는 code가 없을 경우 code와 reason은 응답되지 않습니다.
- 각 요청에 대한 에러 처리는 RestControllerAdvice를 통해 핸들링 되어 응답합니다.
  - 잘못된 요청 data, method 등 구현되어있는 Exception과 내부 처리 중 발생할 수 있는 custom Exception으로 구성되어 있습니다.
- 외부 환경에 의해 변경 될 수 있는 데이터는 application.properties로 관리하며 도메인 별로 immutable하게 처리됩니다.
- 각 기능에 대한 해결 방법은 다음과 같습니다.
  - 정보 상세 조회의 경우 정보 고유 키를 통해 입력 받아 정보를 반환하며 정보가 없을 시 1001 코드를 반환합니다.
  - 이미지 조회의 경우 이미지 jpg, png파일명 만을 입력 받으며 정상 반환시 이미지의 byte 배열을, 실패 시 1003 코드를 반환합니다.
  - 정보 생성의 경우 이미지와 json 데이터를 입력받으며 이미지를 먼저 저장 후 데이터를 입력합니다. 실패시 transaction에 의해 정보는 저장되지 않습니다.
  - 정보 제거의 경우 유효한 키의 정보를 제거하며 유효하지 않은 키일 경우 정보를 제거하지 않습니다. 제거 혹은 미제거일 시 반환 코드는 같습니다.
  - 테스트 코드는 api의 controller, service의 유닛 테스트와 controller, service 유효 여부 통합 테스트만 작성되었습니다.