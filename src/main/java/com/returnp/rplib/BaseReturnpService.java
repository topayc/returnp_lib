package com.returnp.rplib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * @version 1.0
 * @author 안영철
 * </br>
 * </br>
 * 객체 생성 방법 
 * <b><h1> *  ReturnpService returnpService = new ReturnpService("온라인 가맹점 고유 번호", "API KEY", ReturnpService.SERVICE_MODE_DEVELOPE); </br> * </h1></b>
 * 
 * <b><h1>생성자 인자 설명</h1></b>
 * 
 * 1.쇼핑몰 고유 번호 : 리턴포인트에서 생성 후 제공</br>
 * 2.API KEY : 리턴포인트에서 생성, 제공하며, 응답 데이타의 암, 복호화 및 클라이언트 식별에 사용</br>
 * 3.서비스 실행 모드 </br>
 *  - ReturnpService.SERVICE_MODE_DEVELOPE  :  <b>HTTP 로 개발 서버 접속</b></br>
 *  - ReturnpService.SERVICE_MODE_PRODUCT   :  <b>HTTPS 로 실제 운영 서버 접속</b> </br>
 *   (초기 개발시 개발 모드로 테스트후 운영 모드로 변경) </br>
 *   
 * <b><h1> 사용 방법 - 나의 회원 리스트 가져오기  </h1></b></br>
 * ReturnpService returnpService = new ReturnpService("온라인 가맹점 고유 번호", "API KEY", ReturnpService.SERVICE_MODE_DEVELOPE);</br>
 * returnpService.getMyMembers("topayc@naver.com"); // 회원 정보 가져오기 </br>
 * 
 * String resultCode = null;</br>
 * String message = null;</br>
 * String total = null;</br>
 * String data = null;</br>
 * 
 * if (returnpService.getResponseCode().equals(ReturnpService.RESPONSE_OK) {  // 서비스 연결과 처리상에서 에러가 없는 경우 </br>
 * 	 resultCode = returnpService.getResultCode();   // 일반적인 처리 성공일 경우 "100"을 반환</br>
 * 	 message = returnpService.getMessage();         // 처리 결과 메시지</br>
 * 	 total = returnpService.getTotal();                    // 배열 반환일 경우 해당 배열의 크기, 배열이 아닌 오브젝트 혹은 데이타가 없는 요청인 경우  -1 를 반환</br>
 * 	 data = returnpService.getData();                     // 실제 데이타 , 배열 혹은 오브젝트 JSON 스트링으로 반화 </br>
 * }
 * 
 * <b><h1> 서비스 요청에 대한 ResultCode(응답 코드)   </h1></b></br>
 * 100 : 일반적인 요청에 대한 처리 성공 </br>
 * 
 * 300 : 요청에 대해서 수행할수 없음, 비즈니스 로직 처리 오류  </br>
 * 301 : 잘못된 API key</br>
 * 302 : 해당 이메일에 대한 회원이 존재하지 않음</br>
 * 303 : 해당 요청값은 중복됨</br>
 * 304 : 해당 요청값은 중복되지 않음</br>
 * 305 : 잘못된 가맹 고유 번호, 해당 고유번호의 가맹점이 존재 하지 않음 </br>
 * 306 : 이메일이 중복됨  (회원 가입시 반환될 수 있음)
 * 307 : 전화번호가 중복됨  (회원 가입시 반환될 수 있음)
 * 602 : 파라미터로 전달된 전화번호와 실제 디비의 전화번호가 일치하지 않음. 
 * 611 : 존재하지 않는 결제 내역에 대한 취소 요청 
 * 606 : 이미 적립 처리가 완료된 결제 내역
 * 619 : 등록되어 있지만 QR 적립이 가능한 가맹점이 아님
 * 
 * 
 * 500 : 서버 오류 </br>
 */

public class BaseReturnpService {
	private static String ENDPOINT_SAVE_CACHE_DATA = "/pointback/v1/api/save_cache_data";
	private static String ENDPOINT_GET_CACHE_DATA = "/pointback/v1/api/get_cache_data";
	private static String ENDPOINT_GET_MEMBER_INFO = "/pointback/v1/api/get_member_info";
	private static String ENDPOINT_IS_REGISTERED = "/pointback/v1/api/is_registered";
	private static String ENDPOINT_JOIN_UP = "/pointback/v1/api/join_up";
	private static String ENDPOINT_DELETE_MEMBER = "/pointback/v1/api/delete_member";
	private static String ENDPOINT_MODIFY_MEMBER = "/pointback/v1/api/modify_member";
	private static String ENDPOINT_HANDLE_ACCUMULATE = "/pointback/v1/api/handle_accumulate";
	//private static String ENDPOINT_ACCUMULAGE_BY_PAN = "/pointback/v1/api/accumulage_by_pan";
	//private static String ENDPOINT_CANCEL_ACCUMULATE_BY_PAN = "/pointback/v1/api/cancel_accumulate_by_pan";
	private static String ENDPOINT_LANGS = "/pointback/v1/api/langs";
	private static String ENDPOINT_GET_BANK_ACCOUNTS = "/pointback/v1/api/get_bank_accounts";
	private static String ENDPOINT_REGISTER_BANK_ACCOUNT = "/pointback/v1/api/register_bank_account";
	private static String ENDPOINT_UPDATE_BANK_ACCOUNT = "/pointback/v1/api/update_bank_account";
	private static String ENDPOINT_DELETE_BANK_ACCOUNT = "/pointback/v1/api/delete_bank_account";
	private static String ENDPOINT_GET_POLICY = "/pointback/v1/api/get_policy";
	private static String ENDPOINT_RPOINT_CONVERSION_HISTORY = "/pointback/v1/api/rpoint_conversion_history";
	private static String ENDPOINT_REGISTER_WITHDRAWAL = "/pointback/v1/api/register_withdrawal";
	private static String ENDPOINT_GET_WITHDRAWAL_HISTORY = "/pointback/v1/api/get_withdrawal_history";
	private static String ENDPOINT_DELETE_WITHDRAWAL = "/pointback/v1/api/delete_withdrawal";
	private static String ENDPOINT_CANCEL_WITHDRAWAL = "/pointback/v1/api/cancel_withdrawal";
	private static String ENDPOINT_UPDATE_WITHDRAWAL = "/pointback/v1/api/update_withdrawal";
	private static String ENDPOINT_GET_MY_MEMBERS = "/pointback/v1/api/get_my_members";
	private static String ENDPOINT_GET_MY_POINT_INFOS= "/pointback/v1/api/get_my_point_infos";
	private static String ENDPOINT_GET_GPOINT_ACCUMULATE_HISTORY= "/pointback/v1/api/get_gpoint_accumulate_history";
	
	public static String SERVICE_MODE_LOCAL = "LOCAL";
	public static String SERVICE_MODE_DEVLOPEMENT = "DEVELOPEMENT";
	public static String SERVICE_MODE_PRODUCT = "PRODUCT";
	
	public static String RESPONSE_OK = "1000";
	public static String RESPONSE_ERROR = "2000";

	private static String SERVICE_URL_LOCAL = "http://127.0.0.1:8080";
	private static String SERVICE_URL_DEVLOPEMENT = "http://211.254.212.90:8083";
	private static String SERVICE_URL_PRODUCT = "https://www.returnp.com:9094";
	
	private String afId;
	private String apiKey;
	private String serviceMode ; 
	private JSONObject result;
	
	public String getAfId() { return afId; }
	public void setAfId(String afId) { this.afId = afId; }
	public String getApiKey() { return apiKey; }
	public void setApiKey(String apiKey) { this.apiKey = apiKey; }
	
	public String getServiceMode() { return serviceMode; }
	public void setServiceMode(String serviceMode) throws Exception {
		if (serviceMode.equals(BaseReturnpService.SERVICE_MODE_DEVLOPEMENT) && 
				serviceMode.equals(BaseReturnpService.SERVICE_MODE_PRODUCT) && 
				serviceMode.equals(BaseReturnpService.SERVICE_MODE_LOCAL)) {
			throw new Exception("unsupported ServiceMode");
		}
		this.serviceMode = serviceMode;
	}
	
	/**
	 * 생성자
	 * @param afId  리턴포인트에서 생성해서 제공하는 고유 번호
	 * @param apiKey 리턴포인트에서 생성해서 제공하는 Api key (암복호화와 및 클라이언트 식별에 사용)
	 * @param mode  실행 모드 DEVELOPEMENT : 개발 서버 연동, PRODUCT: 실제 운영 서버 연동 
	 * 개발시에는 DEVELOPEMENT 로 테스트를 하고, 실제 배포시에는 PRODUCT로 하여 객체 생성
	 */
	public BaseReturnpService(String afId, String apiKey, String mode){
		this.afId = afId;
		this.apiKey = apiKey;
		if (mode.trim().equals("") || mode == null) {
			this.serviceMode = BaseReturnpService.SERVICE_MODE_DEVLOPEMENT;
		}else {
			this.serviceMode = mode;
		}
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	private String getServiceUrl () throws Exception {
		String rootUrl = null;
		if (this.serviceMode.equals(BaseReturnpService.SERVICE_MODE_DEVLOPEMENT)) {
			rootUrl = BaseReturnpService.SERVICE_URL_DEVLOPEMENT;
		} else if (this.serviceMode.equals(BaseReturnpService.SERVICE_MODE_PRODUCT)) {
			rootUrl = BaseReturnpService.SERVICE_URL_PRODUCT;
		
		} else if (this.serviceMode.equals(BaseReturnpService.SERVICE_MODE_LOCAL)) { 
			rootUrl = BaseReturnpService.SERVICE_URL_LOCAL;
		} else {
			throw new Exception("unsupported ServiceMode");
		}
		return rootUrl;
	}
	private String httpPost(String endPoint, HashMap<String, Object> params) throws Exception {
		this.result = null;
		StringBuilder postData = new StringBuilder();
		params.put("afId", this.afId);
		for(Map.Entry<String,Object> param : params.entrySet()) {
			if(postData.length() != 0) postData.append('&');
	        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	        postData.append('=');
	        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		
		endPoint = this.getServiceUrl() + endPoint;
		//System.out.println("Api Call URL : " + endPoint);
		
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		URL url = new URL(endPoint); // 호출할 url
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes); // POST 호출
        String response = this.convertStreamToString(conn.getInputStream());
		return this.decodeResponse(response);
	}
	
	private String httpGet(String endPoint, HashMap<String, Object> params) throws Exception {
		this.result = null;
		StringBuilder getData = new StringBuilder();
		params.put("afId", this.afId);
		for(Map.Entry<String,Object> param : params.entrySet()) {
			if(getData.length() != 0) getData.append('&');
	        getData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	        getData.append('=');
	        getData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		
		endPoint = this.getServiceUrl() + endPoint;
		//System.out.println("Api Call URL : " + endPoint);
		
		URL url = new URL(endPoint+ "?" + getData.toString());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        String response = this.convertStreamToString(conn.getInputStream());
		return this.decodeResponse(response);
	}
	
	@SuppressWarnings("unchecked")
	private String decodeResponse(String response) throws UnsupportedEncodingException, ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONObject json = (JSONObject) jsonParser.parse(response);
		//System.out.println("원문 Response");
		//System.out.println(json.toString());
		
		if (json.get("data") != null ) {
			json.put("data", Aes256Crypto.decode(BASE64Util.decodeString(json.get("data").toString()), this.apiKey));
		}
		
	/*	long total = -1;
		if (json.get("total") != null ) {
			total = (long)json.get("total");
		}*/
		//System.out.println("total : "  + total);
		//System.out.println("복호화 Response");
		//System.out.println(json.toString());
		//System.out.println("DATA");
		//System.out.println(json.get("data"));
		this.result = json;
		/*JSONParser parser= new JSONParser();
		JSONObject dataJson = (JSONObject) parser.parse(json.get("data").toString());
		System.out.println(dataJson.get("memberName").toString());*/
		return  json.toString();
	}
	
	private String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception  e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// API  호출 메서드
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 회원 정보 가져오기
	 * memberEmail : 회원 이메일
	 */
	private String getMemberInfo(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_GET_MEMBER_INFO, params);
		return result;
	}
	
	/** 
	 * 요청된 데이터에 대한 임시 저장 
	 * 분산 서버에서 사용하는 일종의 캐시 컨트롤러
	 
	 * cacheKey  서버에 저장할 키 
	 * cacheData  저장할 테이타 
	 * */
	private String save_cache_data(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(BaseReturnpService.ENDPOINT_SAVE_CACHE_DATA, params);
		return result;
	}
	

	/**
	 * 캐시된 데이타 가져오기
	 * cacheKey 가져올 키 
	 */
	private String getDataCache(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_GET_CACHE_DATA, params);
		return result;
	}
	

	/**
	 * 중복 검사 처리, 등록되어 있는지 여부 
	 * checkExistType ( 1 : 이메일 중복 여부 , 2 : 전화번호 중복 여부 ) 
	 * memberEmail, memberPhone
	 */
	private String isRegistered(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_IS_REGISTERED, params);
		return result;
	}

	/**
	 * 회원 가입
	 * memberEmail
	 * memberName
	 * memberPassword
	 * memberPassword2
	 * memberPhone
	 * country
	 * recommenderEmail
	 * @return
	 * @throws Exception 
	 */
	private String join(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(BaseReturnpService.ENDPOINT_JOIN_UP,params);
		return result;
	}
	
	/**
	 * 회원 삭제
	 * memberEmail
	 * @return
	 * @throws Exception 
	 */
	private String deleteMember(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_DELETE_MEMBER,params);
		return result;
	}
	
	/**
	 * 회원 정보 수정 및 업데이트 
	 * 아래의 필드중 선택적으로 존재 할 수 있으며, 존재하는 값만 업데이트 함 
	 * memberEmail
	 * memberName
	 * memberPassword
	 * memberPassword2
	 * memberPhone
	 * country
	 * recommenderEmail
	 * @return
	 * @throws Exception 
	 */
	private String modifyMember(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(BaseReturnpService.ENDPOINT_MODIFY_MEMBER,params);
		return result;
	}
	
	/**
	 * 포인트 적립 및 취소 처리에 대한 핸들러  
	 * qr_org : qr 적립 , 취소 요청일 경우 qr 데이타
	 * pam : 결제 금액
	 * pas  : 결제 상태. 0 : 결제 승인(적립 요청) , 1 : 결체 취소 (적립 취소 요청)
	 * pat : 결제 시간 yyyy-MM-dd hh:mm:ss
	 * pan : 결제 번호
	 * af_id : 가맹점 고유 번호
	 * phoneNumber : 회원의 핸드폰 번호
	 * phoneNUmberCountry : 국가 코드가 있는 회원의 핸드폰 번호
	 * 
	 * @return
	 * @throws Exception 
	 */
	private String executeAccumualte(HashMap<String, Object> params) throws Exception {
		/*afId  세팅*/
		params.put("afId", this.getAfId());
		String result = this.httpPost(BaseReturnpService.ENDPOINT_HANDLE_ACCUMULATE , params);
		return result;
	}
	
	/**
	 * 지원 언어 조회
	 * 파라메터 인자 없음
	 * @return
	 * @throws Exception 
	 */
	private String getLangs(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_LANGS,params);
		return result;
	}

	/**
	 * 은행 계좌 리스트 
 	 * memberEmail
	 * @return
	 * @throws Exception 
	 */
	private String getMemberBankAccounts(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_GET_BANK_ACCOUNTS,params);
		return result;
	}

	/**
	 * 출금 신청
 	 * memberEmail
	 * @return
	 * @throws Exception 
	 */
	private String registerBankAccount(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(BaseReturnpService.ENDPOINT_REGISTER_BANK_ACCOUNT,params);
		return result;
	}
	
	/**
	 * 은행 계좌 수정
	 * memberBankAccountNo
	 * memberEmail
	 * 
	 * 아래의 필드는 선택적
	 * bankName
	 * bankAccount
	 * accountOwner

	 * @return
	 * @throws Exception 
	 */
	private String updateBankAccount(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(BaseReturnpService.ENDPOINT_UPDATE_BANK_ACCOUNT,params);
		return result;
	}

	private String deleteBankAccount(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_DELETE_BANK_ACCOUNT,params);
		return result;
	}

	
	/**
	 * 정책 제공 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getPolicy(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_GET_POLICY,params);
		return result;
	}


	/**
	 * R POINT  전환 내역
	 * @return
	 */
	private String getRpointConversionHistory(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_RPOINT_CONVERSION_HISTORY,params);
		return result;
	}

	/**
	 * R PAY 출금 신청 하기 
	 * memberEmail
	 * memberBankAccountNo
	 * withdrawalAmount
	 * @return
	 */
	private String registerWithdrawal(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(BaseReturnpService.ENDPOINT_REGISTER_WITHDRAWAL,params);
		return result;
	}

	/**
	 * 출금 신청 목록 가져오기
	 * memberEmail
	 */
	private String getWithdrawalHistory(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_GET_WITHDRAWAL_HISTORY,params);
		return result;
	}
	
	
	/**
	 * 출금 취소
	 * memberEmail
	 * pointWithdrawalNo
	 */
	private String cancelWithdrawal(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_CANCEL_WITHDRAWAL,params);
		return result;
	}

	/**
	 * 출금 정보 삭제 (사용자에게 제공하지 않음) 
	 * memberEmail
	 * pointWithdrawalNo
	 */
	private String deletelWithdrawal(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_DELETE_WITHDRAWAL,params);
		return result;
	}

	/**
	 * 출금 신청 수정하기
	 * memberEmail
	 * pointWithdrawalNo
	 * memberBankAccountNo
	 * withdrawalAmount
	 */
	private String updatePointWithdrawal(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(BaseReturnpService.ENDPOINT_UPDATE_WITHDRAWAL,params);
		return result;
	}

	/**
	 * 나의 회원 목록 가져오기
	 * 
	 *  memberEmail
	 * @return
	 */
	private String getMyMembers(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_GET_MY_MEMBERS,params);
		return result;
	}

	/**
	 * 나의 포인트 정보 가져오기
	 * 
	 *  memberEmail
	 * @return
	 */
	private String getMyPointInfos(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_GET_MY_POINT_INFOS,params);
		return result;
	}

	/**
	 * G 포인트 적립 현황 내역 가져오기
	 * 
	 *  memberEmail
	 * @return
	 */
	private String getMyGPointAccumuateHistory(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(BaseReturnpService.ENDPOINT_GET_GPOINT_ACCUMULATE_HISTORY,params);
		return result;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// 이후 메서드는 위와 동일한 기능을 하되, 매개 변수 인자를 맵 형태가 아닌, 일반 인자의 형식으로 호출함 
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	/**
     * 회원 정보 가져오기 
     * @param memberEmail 회원 이메일
     * @return 
     * @throws Exception
     */
    public String getMemberInfo(String memberEmail) throws Exception {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("memberEmail", memberEmail);
        String response = this.getMemberInfo(param);
        return response;
    }

      /**
     * 중복 여부 검사 
     * @param checkValueType 체크할 값 타입 , email : 이메일 중복 검사, phone : 핸드폰 중복 검사 
     * @param checkValue checkValueType 에따른 중복 검사할 값  
     * @return
     * @throws Exception
     */
    public String isRegistered(String checkValueType, String checkValue) throws Exception {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("checkValueType", checkValueType);
        param.put("checkValue", checkValue);
        String response = this.isRegistered(param);
        return response;
    }
	
        /**
     * 회원 가입
     * @param memberEmail 회원 이메일
     * @param memberName 회원 이름
     * @param memberPhone 회원 핸드폰
     * @param memberPassword  비밀번호
     * @param memberPassword2 두번째 비밀번호(앞으로 사용예약)
     * @param recommenderEmail 추천인 이메일 , 없으면 널 값
     * @return
     * @throws Exception
     */
    public String   join(String memberEmail, String memberName, String memberPhone, String memberPassword,String memberPassword2, String recommenderEmail, String joinRoute) throws Exception {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("memberEmail", memberEmail);
        param.put("memberName", memberName);
        param.put("memberPhone", memberPhone);
        param.put("memberPassword", memberPassword);
        param.put("memberPassword2", memberPassword2);
        param.put("country", "KR");
        if (recommenderEmail != null && !recommenderEmail.trim().equals("")) {
            param.put("recommenderEmail", recommenderEmail);
        }
        param.put("joinrout", joinRoute);
        return  this.join(param);
    }

     /**
     * 포인트 적립 
     * @param memberEmail 회원 이메일
     * @param memberPhone  회원 전화번호
     * @param paymentApprovalAmount  결제 금액
     * @param paymentApprovalStatus   결제 상태 (0 : 결제 승인, 1 : 결제 취소)
     * @param paymentApprovalDateTime  결제 시간(2019-03-01 10:10:10  형식) "yyyy-MM-dd hh:mm:ss" 형식
     * @param paymentApprovalNumber 결제 번호
     * @return
     * @throws Exception
     */
    public String executeAccumualte(
            String memberEmail, 
            String memberPhone, 
            String paymentApprovalAmount, 
            String paymentApprovalStatus,  
            String paymentApprovalDateTime,
            String paymentApprovalNumber) throws Exception  {
        try {
            HashMap<String, Object> param = new HashMap<String, Object>();
            /*afId 는 executeAccumualte 메소드 안에서 자동 설정됨*/
            param.put("paymentApprovalAmount", paymentApprovalAmount);
            param.put("paymentApprovalStatus", paymentApprovalStatus);
            param.put("afId",this.getAfId());
            param.put("paymentApprovalDateTime", paymentApprovalDateTime);
            param.put("paymentApprovalNumber", paymentApprovalNumber);
            param.put("memberEmail", memberEmail);
            param.put("memberPhone", memberPhone);
            return  this.executeAccumualte(param);
        } catch (IOException e) {
            //System.out.println("*** IO 익셉션 발생");
            e.printStackTrace();
        } catch (ParseException e) {
            //System.out.println("*** ParseException 익셉션 발생");
            e.printStackTrace();
        }
        return null;
    }
    

        /**
     * 나의 포인트 정보 가져오기
     * @param memberEmail 회원 이메일
     * @return
     * @throws Exception
     */
    public String getMyPointInfos(String memberEmail) throws Exception  {
        //System.out.println("#### memberJoin");
        try {
            String response = null;
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("memberEmail",  memberEmail);
            response = this.getMyPointInfos(param);
            return response;
        } catch (IOException e) {
            //System.out.println("*** IO 익셉션 발생");
            e.printStackTrace();
        } catch (ParseException e) {
            //System.out.println("*** ParseException 익셉션 발생");
            e.printStackTrace();
        }
        return null;
    }


    
    public String getContent() {
    	return this.result.toString();
    }
    
    public String getResponseCode() {
    	if (this.result.get("responseCode") != null) {
    		return this.result.get("responseCode").toString();
    	}else {
    		return null;
    	}
    }
    
    public String getResultCode() {
    	if (this.result.get("resultCode") != null) {
    		return this.result.get("resultCode").toString();
    	}else {
    		return null;
    	}
    }
    
    public String  getData() {
    	if (this.result.get("data") != null) {
    		return this.result.get("data").toString();
    	}else {
    		return null;
    	}
    }
    
    public String getMessage() {
    	if (this.result.get("message") != null) {
    		return this.result.get("message").toString();
    	}else {
    		return null;
    	}
    }
    
    public int getTotal() {
    	if (this.result.get("total") != null) {
    		return Integer.valueOf(this.result.get("total").toString());
    	}else {
    		return -1;
    	}
    }
	
	/*public static void main(String[] args) {
		try {
			BaseReturnpService returnpService = new BaseReturnpService(
					"A909010", "ea7bc4e2bffc42169adb944d5f39bb22", BaseReturnpService.SERVICE_MODE_DEVLOPEMENT);
			
			returnpService.getMemberInfo("topayc1@naver.com");
			//returnpService.getMyPointInfos("topayc1@naver.com");
			//ReturnpService.callGetMemberInfo2(returnpService);
			//returnpService.isRegistered("phone", "0108822747");
			//returnpService.join("topayc112121@naver.com", "안영철", "01088229967", "a9831000", "a9831000","topayc1@naver.com");
			returnpService.executeAccumualte(
				"topayc1@naver.com",
				"01088227467",
				"44444444",
				"1",
				"2019-03-01 10:10:10",
				"44444444");

			
			System.out.println("===========================================================");
			//System.out.println("orginal String" + returnpService.getContent());
			System.out.println("===========================================================");
			System.out.println(">> content");
			System.out.println(returnpService.getContent());
			System.out.println();
			System.out.println(">> responseCode");
			System.out.println(returnpService.getResponseCode());
			System.out.println();
			System.out.println(">> resultCode");
			System.out.println(returnpService.getResultCode());
			System.out.println();
			System.out.println(">> message ");
			System.out.println(returnpService.getMessage());
			System.out.println();
			System.out.println(">> total" );
			System.out.println(returnpService.getTotal());
			System.out.println();
			System.out.println(">> data ");
			System.out.println(returnpService.getData());
			System.out.println();
			System.out.println("===========================================================");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
