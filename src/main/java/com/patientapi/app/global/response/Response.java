package com.patientapi.app.global.response;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 요청 결과에 따른 공통 처리 값을 정의한 클래스입니다.
 * 모든 요청은 해당 클래스를 상속하여 사용해야합니다.
 * 각 처리 결과는 생성자에 쓰인 code 문자열을 통해 Info Enum 클래스의 사유와 매핑되어 결과 값에 포함되며,
 * 일치하는 code가 없을시 결과 값에 표현되지 않습니다.   
 */
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
	private String code;
	private String reason;
	
	public Response(String code) {
		Info info = Info.findBy(code);
		if (info != null) {
			this.code = info.name().substring(1);
			this.reason = info.getReason();
		}
	}
	
	enum Info {
		_1000("정상 처리 되었습니다."),
		_1001("표시할 결과가 없습니다."),
		_1003("올바르지 않은 파일 형식입니다."),
		_1004("이미지 처리 중 오류가 발생했습니다."),
		_9995("요청 데이터 범위가 유효하지 않습니다."),
		_9996("요청 타입이 올바르지 않습니다."),
		_9997("지원하지 않는 http method입니다."),
		_9998("올바르지 않은 json 데이터 입니다."),
		_9999("처리 중 오류가 발생했습니다.");
		
		private String reason;
		
		private Info(String reason) {
			this.reason = reason;
		}

		private static final Map<String, Info> stringToInfo = new HashMap<String, Info>();
		static {
			for (Info status : values()) {
				stringToInfo.put(status.name().substring(1), status);
			}
		}
		
		public static Info findBy(String code) {
			return stringToInfo.get(code);
		}
		
		public String getReason() {
			return this.reason;
		}
	}
}
