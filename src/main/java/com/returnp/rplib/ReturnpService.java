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
 * <b><h1>
 *  ReturnpService returnpService = </br>
 *  	new ReturnpService("OTID_1551081693681", "9c10c4a7a3ea445e8777b1973752643a", ReturnpService.SERVICE_MODE_DEVELOPE);
 * </h1></b>
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
 * 
 * ReturnpService returnpService = </br>
 * 		new ReturnpService("OTID_1551081693681", "9c10c4a7a3ea445e8777b1973752643a", ReturnpService.SERVICE_MODE_DEVELOPE);
 * returnpService.getMyMembers("topayc@naver.com");</br>
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
 * 
 * 
 * 500 : 서버 오류 </br>
 */

public class ReturnpService {
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
		if (serviceMode.equals(ReturnpService.SERVICE_MODE_DEVLOPEMENT) && 
				serviceMode.equals(ReturnpService.SERVICE_MODE_PRODUCT) && 
				serviceMode.equals(ReturnpService.SERVICE_MODE_LOCAL)) {
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
	public ReturnpService(String afId, String apiKey, String mode){
		this.afId = afId;
		this.apiKey = apiKey;
		if (mode.trim().equals("") || mode == null) {
			this.serviceMode = ReturnpService.SERVICE_MODE_DEVLOPEMENT;
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
		if (this.serviceMode.equals(ReturnpService.SERVICE_MODE_DEVLOPEMENT)) {
			rootUrl = ReturnpService.SERVICE_URL_DEVLOPEMENT;
		} else if (this.serviceMode.equals(ReturnpService.SERVICE_MODE_PRODUCT)) {
			rootUrl = ReturnpService.SERVICE_URL_PRODUCT;
		
		} else if (this.serviceMode.equals(ReturnpService.SERVICE_MODE_LOCAL)) { 
			rootUrl = ReturnpService.SERVICE_URL_LOCAL;
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
	//API  호출 메서드 
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 
	 * 요청된 데이터에 대한 임시 저장 
	 * 분산 서버에서 사용하는 일종의 캐시 컨트롤러
	 
	 * cacheKey  서버에 저장할 키 
	 * cacheData  저장할 테이타 
	 * */
	private String save_cache_data(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(ReturnpService.ENDPOINT_SAVE_CACHE_DATA, params);
		return result;
	}
	

	/**
	 * 캐시된 데이타 가져오기
	 * cacheKey 가져올 키 
	 */
	private String getDataCache(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_CACHE_DATA, params);
		return result;
	}
	
	/**
	 * 회원 정보 가져오기
	 * memberEmail : 회원 이메일
	 */
	private String getMemberInfo(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_MEMBER_INFO, params);
		return result;
	}

	/**
	 * 중복 검사 처리, 등록되어 있는지 여부 
	 * checkExistType ( 1 : 이메일 중복 여부 , 2 : 전화번호 중복 여부 ) 
	 * memberEmail, memberPhone
	 */
	private String isRegistered(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_IS_REGISTERED, params);
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
		String result = this.httpPost(ReturnpService.ENDPOINT_JOIN_UP,params);
		return result;
	}
	
	/**
	 * 회원 삭제
	 * memberEmail
	 * @return
	 * @throws Exception 
	 */
	private String deleteMember(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_DELETE_MEMBER,params);
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
		String result = this.httpPost(ReturnpService.ENDPOINT_MODIFY_MEMBER,params);
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
		String result = this.httpPost(ReturnpService.ENDPOINT_HANDLE_ACCUMULATE , params);
		return result;
	}
	
	/**
	 * 지원 언어 조회
	 * 파라메터 인자 없음
	 * @return
	 * @throws Exception 
	 */
	private String getLangs(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_LANGS,params);
		return result;
	}

	/**
	 * 은행 계좌 리스트 
 	 * memberEmail
	 * @return
	 * @throws Exception 
	 */
	private String getMemberBankAccounts(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_BANK_ACCOUNTS,params);
		return result;
	}

	/**
	 * 출금 신청
 	 * memberEmail
	 * @return
	 * @throws Exception 
	 */
	private String registerBankAccount(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(ReturnpService.ENDPOINT_REGISTER_BANK_ACCOUNT,params);
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
		String result = this.httpPost(ReturnpService.ENDPOINT_UPDATE_BANK_ACCOUNT,params);
		return result;
	}

	private String deleteBankAccount(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_DELETE_BANK_ACCOUNT,params);
		return result;
	}

	
	/**
	 * 정책 제공 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getPolicy(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_POLICY,params);
		return result;
	}


	/**
	 * R POINT  전환 내역
	 * @return
	 */
	private String getRpointConversionHistory(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_RPOINT_CONVERSION_HISTORY,params);
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
		String result = this.httpPost(ReturnpService.ENDPOINT_REGISTER_WITHDRAWAL,params);
		return result;
	}

	/**
	 * 출금 신청 목록 가져오기
	 * memberEmail
	 */
	private String getWithdrawalHistory(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_WITHDRAWAL_HISTORY,params);
		return result;
	}
	
	
	/**
	 * 출금 취소
	 * memberEmail
	 * pointWithdrawalNo
	 */
	private String cancelWithdrawal(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_CANCEL_WITHDRAWAL,params);
		return result;
	}

	/**
	 * 출금 정보 삭제 (사용자에게 제공하지 않음) 
	 * memberEmail
	 * pointWithdrawalNo
	 */
	private String deletelWithdrawal(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_DELETE_WITHDRAWAL,params);
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
		String result = this.httpPost(ReturnpService.ENDPOINT_UPDATE_WITHDRAWAL,params);
		return result;
	}

	/**
	 * 나의 회원 목록 가져오기
	 * 
	 *  memberEmail
	 * @return
	 */
	private String getMyMembers(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_MY_MEMBERS,params);
		return result;
	}

	/**
	 * 나의 포인트 정보 가져오기
	 * 
	 *  memberEmail
	 * @return
	 */
	private String getMyPointInfos(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_MY_POINT_INFOS,params);
		return result;
	}

	/**
	 * G 포인트 적립 현황 내역 가져오기
	 * 
	 *  memberEmail
	 * @return
	 */
	private String getMyGPointAccumuateHistory(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_GPOINT_ACCUMULATE_HISTORY,params);
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
	 * 정책 제공 
	 * @return
	 * @throws Exception
	 */
	public String getPolicy() throws Exception {
		HashMap<String, Object> param = new HashMap<String, Object>();
		return this.getPolicy(param);
	}
	
        /**
     * 회원 가입
     * @param memberEmail 회원 이메일
     * @param memberName 회원 이름
     * @param memberPhone 회원 핸드폰
     * @param memberPassword  비밀번호
     * @param memberPassword2 두번째 비밀번호(앞으로 사용예약)
     * @param recommenderEmail 추천인 이메일 
     * @return
     * @throws Exception
     */
    public String   join(String memberEmail, String memberName, String memberPhone, String memberPassword,String memberPassword2, String recommenderEmail) throws Exception {
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
        return  this.join(param);
    }

	/**
	 * 회원 정보 수정 및 업데이트 
	 * 아래의 필드중 선택적으로 존재 할 수 있으며, 존재하는 값만 업데이트 함 
	 * @param memberEmail 회원 이메일
	 * @param memberName 회원 이름
	 * @param memberPassword 회원 비밀 번호
	 * @param memberPassword2 두번째 비밀번호(앞으로 사용예약)
	 * @param memberPhone  회원 전화 번호
	 * @param country 국가 코드 2자리 KR등 
	 * @param recommenderEmail 추천인 이메일
	 * @return
	 * @throws Exception 
	 */
	public String modifyMember(String memberEmail, String memberPassword, String memberPassword2, String memberPhone, String country, String recommenderEmail) throws Exception {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("memberEmail", memberEmail);
        param.put("memberPhone", memberPhone);
        param.put("memberPassword", memberPassword);
        param.put("memberPassword2", memberPassword2);
        param.put("country", country);
        if (recommenderEmail != null && !recommenderEmail.trim().equals("")) {
            param.put("recommenderEmail", recommenderEmail);
        }
        return  this.modifyMember(param);
	}
    
     /**
     * 회원 정보 삭제
     * @param memberEmail 삭제할 회원 이메일
     * @throws Exception
     */
    public String  deleteMember(String memberEmail) throws Exception {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("memberEmail", memberEmail);
        return this.deleteMember(param);
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
     * 지원 언어 목록 가져오기 
     * @return
     * @throws Exception
     */
    public String getLangs() throws Exception {
    	HashMap<String, Object> param = new HashMap<String, Object>();
		return this.getLangs(param);
	}

	/**
	 * 회원 은행 계좌 가져오기 
	 * @param memberEmail 회원 이메일
	 * @return
	 * @throws Exception
	 */
	public String getMemberBankAccounts(String memberEmail) throws Exception {
		 HashMap<String, Object> param = new HashMap<String, Object>();
		 param.put("memberEmail", memberEmail);
		String result = this.getMemberBankAccounts(param);
		return result;
	}

	
    /**
     * 계좌 등록하기 
     * @param memberEmail 회원 이메일
     * @param bankName 회원 이름
     * @param bankAccount 은행명
     * @param accountOwner 소유주 명
     * @return
     * @throws Exception
     */
    public String registerBankAccount(String memberEmail, String bankName, String bankAccount, String accountOwner) throws Exception  {
        try {
            String response = null;
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("memberEmail",  memberEmail);
            param.put("bankName" , bankName);
            param.put("bankAccount" , bankAccount);
            param.put("accountOwner" , accountOwner);
            //param.put("accountStatus" , "Y");
            response = this.registerBankAccount(param);
            return response;
        } catch (IOException e) {
            //System.out.println("*** IO 익셉션 발생");
            e.printStackTrace();
        } catch (ParseException e) {
           // System.out.println("*** ParseException 익셉션 발생");
            e.printStackTrace();
        }
        return null;
    }

        /**
     * 계좌 정보 수정
     * @param memberEmail 회원 이메일
     * @param memberBankAccountNo  은행 계좌 등록 번호
     * @param bankName 은행 이름
     * @param bankAccount 은행 계좌
     * @param accountOwner 소유주 이름
     * @param accountStaus 상태 (Y : 사용, N : 사용중지)
     * @return
     * @throws Exception
     */
    public String updateBankAccount(
    		String memberEmail, String memberBankAccountNo, String bankName, String bankAccount, String accountOwner, String accountStaus) throws Exception  {
    	 String response = null;
         HashMap<String, Object> param = new HashMap<String, Object>();
         param.put("memberEmail",  memberEmail);
         param.put("memberBankAccountNo", memberBankAccountNo);
         param.put("bankName" , bankName);
         param.put("bankAccount" , bankAccount);
         param.put("accountOwner" , accountOwner);
         param.put("accountStatus" , accountStaus);
         //param.put("accountStatus" , "Y");
         response = this.updateBankAccount(param);
         return response;
    }

    /**
     * 은행 계좌 삭제
     * @param memberEmail 회원 이메일
     * @param memberBankAccountNo 은행 계좌 등록 번호 
     * @return
     * @throws Exception
     */
    public String deleteBankAccount(String memberEmail, String memberBankAccountNo) throws Exception  {
        try {
            String response = null;
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("memberEmail",  memberEmail);
            param.put("memberBankAccountNo", memberBankAccountNo);
            response = this.deleteBankAccount(param);
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

    /**
     * 출금 신청 목록 가져오기
     * @param memberEmail 회원 이메일
     * @return
     * @throws Exception
     */
    public String getWithdrawalHistory(String memberEmail) throws Exception  {
        try {
            String response = null;
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("memberEmail",  memberEmail);
            response = this.getWithdrawalHistory(param);
            return response;
        } catch (IOException e) {
           // System.out.println("*** IO 익셉션 발생");
            e.printStackTrace();
        } catch (ParseException e) {
            //System.out.println("*** ParseException 익셉션 발생");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 출금 신청하기 
     * @param memberEmail 회원 이메일
     * @param memberBankAccountNo 계좌 등록 번호
     * @param withdrawalAmount 출금 금액 
     * @return
     * @throws Exception
     */
    public String registerWithdrawal(String memberEmail, String memberBankAccountNo, int withdrawalAmount ) throws Exception  {
        try {
            String response = null;
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("memberEmail",  memberEmail);
            param.put("memberBankAccountNo",  memberBankAccountNo);
            param.put("withdrawalAmount",  withdrawalAmount);
            response = this.registerWithdrawal(param);
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

    /**
     * 출금 요청 취소 하기 
     * @param memberEmail 회원 이메일
     * @param memberBankAccountNo 회원 등록번호
     * @return
     * @throws Exception
     */
    public String cancelWithdrawal(String memberEmail, String memberBankAccountNo) throws Exception  {
        try {
            String response = null;
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("memberEmail",  memberEmail);
            param.put("pointWithdrawalNo",  memberBankAccountNo);
            response = this.cancelWithdrawal(param);
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

      /**
     * 출금 삭제하기 
     * @param memberEmail 회원 이메일
     * @param memberBankAccountNo 은행 계좌 등록번호
     * @return
     * @throws Exception
     */
    public String deletelWithdrawal(String memberEmail, String memberBankAccountNo) throws Exception  {
        try {
            String response = null;
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("memberEmail",  memberEmail);
            param.put("pointWithdrawalNo",  memberBankAccountNo);
            response = this.deletelWithdrawal(param);
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

    /**
     * 출금 요청 수정
     * @param memeberEmail 회원 이메일
     * @param pointWithdrawalNo 출금 요청 번호
     * @param memberBankAccountNo 은행 계좌 등록 번호 
     * @param withdrawalAmount 출금 금액
     * @return
     * @throws Exception
     */
    public String updatePointWithdrawal(String memeberEmail,String pointWithdrawalNo , String memberBankAccountNo, String withdrawalAmount  ) throws Exception  {
        try {
            String response = null;
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("memberEmail",  memeberEmail);
            param.put("pointWithdrawalNo",  pointWithdrawalNo);
            param.put("memberBankAccountNo",  memberBankAccountNo);
            param.put("withdrawalAmount",  withdrawalAmount);
            response = this.updatePointWithdrawal(param);
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

    /**
     * 나의 회원 리스트 가져오기 
     * @param memberEmail 회원 이메일
     * @return
     * @throws Exception
     */
    public String getMyMembers(String memberEmail) throws Exception  {
        //System.out.println("#### memberJoin");
        try {
            String response = null;
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("memberEmail",  memberEmail);
            response = this.getMyMembers(param);
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

        /**
     * G-POint 적립 내역 가져오기
     * @param memberEmail 회원 이메일
     * @param searchDateMonth 검색 월
     * @param pageSize 검색 갯수
     * @param offset offset
     * @return
     * @throws Exception
     */
    public String getMyGPointAccumuateHistory(String memberEmail, String searchDateMonth, String pageSize, String offset) throws Exception  {
        //System.out.println("#### memberJoin");
        try {
            String response = null;
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("memberEmail",  memberEmail);
            param.put("searchDateMonth", searchDateMonth);
            param.put("pageSize", pageSize);
            param.put("offset", offset);
            response = this.getMyGPointAccumuateHistory(param);
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
    
    
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// API 테스트 메서드                                                                                                             
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
/*	public static void   callGetMemberInfo(ReturnpService returnpService) throws Exception {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail", "topayc@naver.com");
		response = returnpService.getMemberInfo(param);
	}
	
	public static String   callGetMemberInfo2(ReturnpService returnpService) throws Exception {
		return returnpService.getMemberInfo("topayc1@naver.com");
	}
	
	public static void   callIsRegistered(ReturnpService returnpService) throws Exception {
		System.out.println("#### getMemberBankAccounts");
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("checkValueType", "1");
		param.put("checkValue", "topayc@naver.com");
		response = returnpService.isRegistered(param);
	}
	
	public static String   callIsRegistered2(ReturnpService returnpService) throws Exception {
		return returnpService.isRegistered("phone", "0108822747");
	}
	
	public static void   callMemberJoin(ReturnpService returnpService) throws Exception {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail", "topayc90010@naver.com");
		param.put("memberName", "안영철1000");
		param.put("memberPhone", "01098227407");
		param.put("memberPassword", "a9831000");
		param.put("memberPassword2", "a9831000");
		param.put("country", "KR");
		param.put("recommenderEmail", "topayc1@naver.com");
		response = returnpService.join(param);
	}

	public static String   callMemberJoin2(ReturnpService returnpService) throws Exception {
		return returnpService.join("topayctopayc90901@naver.com", "안영철", "0103822747", "a9831000", "a9831000","topayc1@naver.com");
	}
	public static void   callDeleteMember(ReturnpService returnpService) throws Exception {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail", "topayc9000@naver.com");
		response = returnpService.deleteMember(param);
	}

	public static String callDeleteMember2(ReturnpService returnpService) throws Exception {
		return returnpService.deleteMember("topayctopayc90901@naver.com");
	}

	public static void   callModifyMember(ReturnpService returnpService) throws Exception {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail", "topayc10000@naver.com");
		param.put("memberPhone", "01098227467");
		param.put("country", "US");
		response = returnpService.modifyMember(param);
	}
	public static String callModifyMember2(ReturnpService returnpService) throws Exception {
		return returnpService.modifyMember("topayctopayc9090@naver.com", "a9831000","a9831000", "01011111111", "KR", "topayc1@naver.com");
	}
	

	public static void   callExecuteAccumlate(ReturnpService returnpService) throws Exception  {
		try {
			String response = null;
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("paymentApprovalAmount", "1000000");
			param.put("paymentApprovalStatus", "1");
			param.put("afId",returnpService.getAfId());
			param.put("paymentApprovalDateTime", "2019-03-01 10:10:10");
			param.put("paymentApprovalNumber", "90189121");
			param.put("memberEmail", "topayc10000@naver.com");
			param.put("memberPhone", "01098227467");
			response = returnpService.executeAccumualte(param);
		} catch (IOException e) {
			System.out.println("*** IO 익셉션 발생");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("*** ParseException 익셉션 발생");
			e.printStackTrace();
		}
	}
	
	public static String   callExecuteAccumlate2(ReturnpService returnpService) throws Exception  {
		return  returnpService.executeAccumualte(
			"topayctopayc9090@naver.com",
			"01011111111",
			"44444444",
			"0",
			"2019-03-01 10:10:10",
			"44444444");
	}
	
	public static void   callGetLangs(ReturnpService returnpService) throws Exception  {
		//System.out.println("#### memberJoin");
		try {
			String response = null;
			HashMap<String, Object> param = new HashMap<String, Object>();
			response = returnpService.getLangs(param);;
		} catch (IOException e) {
			System.out.println("*** IO 익셉션 발생");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("*** ParseException 익셉션 발생");
			e.printStackTrace();
		}
	}
	
	public static String callGetLangs2(ReturnpService returnpService) throws Exception  {
		return returnpService.getLangs();
	}
	
	public static void callGetMemberBankAccounts(ReturnpService returnpService) throws Exception  {
		try {
			String response = null;
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("memberEmail",  "topayc@naver.com");
			response = returnpService.getMemberBankAccounts(param);
		} catch (IOException e) {
			System.out.println("*** IO 익셉션 발생");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("*** ParseException 익셉션 발생");
			e.printStackTrace();
		}
	}

	public static String callGetMemberBankAccounts2(ReturnpService returnpService) throws Exception  {
		return returnpService.getMemberBankAccounts("topayc1@naver.com");
	}
	
	public static void   callRegisterBankAccount(ReturnpService returnpService) throws Exception  {
		try {
			String response = null;
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("memberEmail",  "topayc1@naver.com");
			param.put("bankName" , "신한은행");
			param.put("bankAccount" , "111111111");
			param.put("accountOwner" , "안영철");
			//param.put("accountStatus" , "Y");
			response = returnpService.registerBankAccount(param);
		} catch (IOException e) {
			System.out.println("*** IO 익셉션 발생");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("*** ParseException 익셉션 발생");
			e.printStackTrace();
		}
	}
	public static String callRegisterBankAccount2(ReturnpService returnpService) throws Exception  {
		return returnpService.registerBankAccount(
				"topayc1@naver.com",
				"신한은행222",
				"111111111",
				"안영철"
		);
	}
	
	public static void  callUpdateBankAccount(ReturnpService returnpService) throws Exception  {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail",  "topayc1@naver.com");
		param.put("memberBankAccountNo", 708);
		param.put("bankName" , "신한은행변경");
		param.put("bankAccount" , "111111111변경");
		param.put("accountOwner" , "홍길동");
		param.put("accountStatus" , "N");
		response = returnpService.updateBankAccount(param);
	}

	public static String callUpdateBankAccount2(ReturnpService returnpService) throws Exception  {
		return returnpService.updateBankAccount(
			"topayc1@naver.com",
			708,
			"신한은행변경2",
			"111111111변경2",
			"홍길동2",
			"Y"
		);
		
	}
	public static void   callDeleteBankAccount(ReturnpService returnpService) throws Exception  {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail",  "topayc1@naver.com");
		param.put("memberBankAccountNo", 519);
		response = returnpService.deleteBankAccount(param);
	}
	
	public static String callDeleteBankAccount2(ReturnpService returnpService) throws Exception  {
		return returnpService.deleteBankAccount("topayc1@naver.com","708");
	}
	
	
	public static void   callgetWithdrawalHistory(ReturnpService returnpService) throws Exception  {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail",  "topayc1@naver.com");
		response = returnpService.getWithdrawalHistory(param);
	}
	
	public static String callgetWithdrawalHistory2(ReturnpService returnpService) throws Exception  {
		return returnpService.getWithdrawalHistory("topayc1@naver.com");
	}
	
	
	public static void   callRegisterWithdrawal(ReturnpService returnpService) throws Exception  {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail",  "topayc1@naver.com");
		param.put("memberBankAccountNo",  518);
		param.put("withdrawalAmount",  200000);
		response = returnpService.registerWithdrawal(param);
	}
	
	public static String callRegisterWithdrawal2(ReturnpService returnpService) throws Exception  {
		return returnpService.registerWithdrawal("topayc1@naver.com", 707, 20000);
	}
	

	public static void   callCancelWithdrawal(ReturnpService returnpService) throws Exception  {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail",  "topayc1@naver.com");
		param.put("pointWithdrawalNo",  1);
		response = returnpService.cancelWithdrawal(param);
	}
	
	public static String callCancelWithdrawal2(ReturnpService returnpService) throws Exception  {
		return returnpService.cancelWithdrawal("topayc1@naver.com", 149);
	}
	
	
	public static void   callDeleteWithdrawal(ReturnpService returnpService) throws Exception  {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail",  "topayc1@naver.com");
		param.put("pointWithdrawalNo",  1);
		response = returnpService.deletelWithdrawal(param);
	}
	
	public static String callDeleteWithdrawal2(ReturnpService returnpService) throws Exception  {
		return returnpService.deletelWithdrawal("topayc1@naver.com", 149);
	}
	
	
	public static void   callModifyWithdrawal(ReturnpService returnpService) throws Exception  {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail",  "topayc1@naver.com");
		param.put("pointWithdrawalNo",  1);
		param.put("memberBankAccountNo",  518);
		param.put("withdrawalAmount",  200000);
		response = returnpService.updatePointWithdrawal(param);
	}
	
	public static String   callModifyWithdrawal2(ReturnpService returnpService) throws Exception  {
		return  returnpService.updatePointWithdrawal("topayc1@naver.com", 1, 518, 200000);
	}

	
	public static void   callGetMyMembers(ReturnpService returnpService) throws Exception  {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail",  "topayc1@naver.com");
		response = returnpService.getMyMembers(param);
	}
	
	public static String callGetMyMembers2(ReturnpService returnpService) throws Exception  {
		return returnpService.getMyMembers("topayc1@naver.com");
	}
	
	public static void   callGetMyPointInfos(ReturnpService returnpService) throws Exception  {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail",  "lih5026@naver.com");
		response = returnpService.getMyPointInfos(param);
	}
	
	public static String callGetMyPointInfos2(ReturnpService returnpService) throws Exception  {
		return returnpService.getMyPointInfos("topayc1@naver.com");
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------------
	
	public static void   callGetMyGPointAccumuateHistory(ReturnpService returnpService) throws Exception  {
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail",  "lih5026@naver.com");
		param.put("searchDateMonth", "2019-02-01 00:00:00");
		param.put("pageSize", 10);
		param.put("offset", 0);
		response = returnpService.getMyGPointAccumuateHistory(param);
	}
	
	public static String callGetMyGPointAccumuateHistory2(ReturnpService returnpService) throws Exception  {
		return returnpService.getMyGPointAccumuateHistory("lih5026@naver.com", "2019-02-01 00:00:00", 10, 0);
	}
	*/
	
	/*public static void main(String[] args) {
		try {
			ReturnpService returnpService = new ReturnpService(
					"OTID_1551081693681", "9c10c4a7a3ea445e8777b1973752643a", ReturnpService.SERVICE_MODE_DEVLOPEMENT);
			//ReturnpService.callGetMemberInfo(returnpService);
			//ReturnpService.callGetMemberInfo2(returnpService);
			
			//ReturnpService.callIsRegistered(returnpService);
			//ReturnpService.callIsRegistered2(returnpService);
			
			//ReturnpService.callMemberJoin(returnpService);
			//ReturnpService.callMemberJoin2(returnpService);
			
			//ReturnpService.callDeleteMember(returnpService);
			//ReturnpService.callDeleteMember2(returnpService);
			
			//ReturnpService.callModifyMember(returnpService);
			//ReturnpService.callModifyMember2(returnpService);

			//ReturnpService.callExecuteAccumlate(returnpService);
			//ReturnpService.callExecuteAccumlate2(returnpService);

			//ReturnpService.callGetLangs(returnpService);
			//ReturnpService.callGetLangs2(returnpService);
			//System.out.println("responseCode : " + returnpService.getResponseCode());
			

			//ReturnpService.callGetMemberBankAccounts(returnpService);
			//ReturnpService.callGetMemberBankAccounts2(returnpService);
			
			//ReturnpService.callRegisterBankAccount(returnpService);
			//ReturnpService.callRegisterBankAccount2(returnpService);

			//ReturnpService.callUpdateBankAccount(returnpService);
			//ReturnpService.callUpdateBankAccount2(returnpService);
			
			//ReturnpService.callDeleteBankAccount(returnpService);
			//ReturnpService.callDeleteBankAccount2(returnpService);
			
			//ReturnpService.callgetWithdrawalHistory(returnpService);
			//ReturnpService.callgetWithdrawalHistory2(returnpService);
			
			//ReturnpService.callRegisterWithdrawal(returnpService);
			//ReturnpService.callRegisterWithdrawal2(returnpService);
			
			//ReturnpService.callCancelWithdrawal(returnpService);
			//ReturnpService.callCancelWithdrawal2(returnpService);

			//ReturnpService.callDeleteWithdrawal(returnpService);
			//ReturnpService.callDeleteWithdrawal2(returnpService);

			//ReturnpService.callModifyWithdrawal(returnpService);
			//ReturnpService.callModifyWithdrawal2(returnpService);

			//ReturnpService.callGetMyMembers(returnpService);
			//ReturnpService.callGetMyMembers2(returnpService);
			
			//ReturnpService.callGetMyPointInfos(returnpService);
			//ReturnpService.callGetMyPointInfos2(returnpService);

			//ReturnpService.callGetMyGPointAccumuateHistory(returnpService);
			//ReturnpService.callGetMyGPointAccumuateHistory2(returnpService);
			
			System.out.println("===========================================================");
			System.out.println("content :" + returnpService.getContent());
			System.out.println("responseCode: " + returnpService.getResponseCode());
			System.out.println("resultCode: " + returnpService.getResultCode());
			System.out.println("message : " + returnpService.getMessage());
			System.out.println("total : " + returnpService.getTotal());
			System.out.println("data : " + returnpService.getData());
			System.out.println("===========================================================");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
