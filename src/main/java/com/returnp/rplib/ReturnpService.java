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


public class ReturnpService {
	public static String ENDPOINT_SAVE_CACHE_DATA = "/pointback/v1/api/save_cache_data";
	public static String ENDPOINT_GET_CACHE_DATA = "/pointback/v1/api/get_cache_data";
	public static String ENDPOINT_GET_MEMBER_INFO = "/pointback/v1/api/get_member_info";
	public static String ENDPOINT_IS_REGISTERED = "/pointback/v1/api/is_registered";
	public static String ENDPOINT_JOIN_UP = "/pointback/v1/api/join_up";
	public static String ENDPOINT_DELETE_MEMBER = "/pointback/v1/api/delete_member";
	public static String ENDPOINT_MODIFY_MEMBER = "/pointback/v1/api/modify_member";
	public static String ENDPOINT_HANDLE_ACCUMULATE = "/pointback/v1/api/handle_accumulate";
	public static String ENDPOINT_ACCUMULAGE_BY_PAN = "/pointback/v1/api/accumulage_by_pan";
	public static String ENDPOINT_CANCEL_ACCUMULATE_BY_PAN = "/pointback/v1/api/cancel_accumulate_by_pan";
	public static String ENDPOINT_LANGS = "/pointback/v1/api/langs";
	public static String ENDPOINT_GET_BANK_ACCOUNTS = "/pointback/v1/api/get_bank_accounts";
	public static String ENDPOINT_REGISTER_BANK_ACCOUNT = "/pointback/v1/api/register_bank_account";
	public static String ENDPOINT_UPDATE_BANK_ACCOUNT = "/pointback/v1/api/update_bank_account";
	public static String ENDPOINT_DELETE_BANK_ACCOUNT = "/pointback/v1/api/delete_bank_account";
	public static String ENDPOINT_GET_POLICY = "/pointback/v1/api/get_policy";
	public static String ENDPOINT_GPOINT_ACCUMULATE_HISTORY = "/pointback/v1/api/gpoint_accumulate_history";
	public static String ENDPOINT_RPOINT_CONVERSION_HISTORY = "/pointback/v1/api/rpoint_conversion_history";
	public static String ENDPOINT_REGISTER_WITHDRAWAL = "/pointback/v1/api/register_withdrawal";
	public static String ENDPOINT_GET_WITHDRAWAL_HISTORY = "/pointback/v1/api/get_withdrawal_history";
	public static String ENDPOINT_DELETE_WITHDRAWAL = "/pointback/v1/api/delete_withdrawal";
	public static String ENDPOINT_UPDATE_WITHDRAWAL = "/pointback/v1/api/update_withdrawal";
	public static String ENDPOINT_GET_MY_MEMBERS = "/pointback/v1/api/get_my_members";
	
	private String afId;
	private String apiKey;
	
	ReturnpService(String afId, String apiKey){
		this.afId = afId;
		this.apiKey = apiKey;
	}
	
	public String getAfId() { return afId; }
	public void setAfId(String afId) { this.afId = afId; }
	public String getApiKey() { return apiKey; }
	public void setApiKey(String apiKey) { this.apiKey = apiKey; }

	private String httpPost(String endPoint, HashMap<String, Object> params) throws IOException, ParseException {
		StringBuilder postData = new StringBuilder();
		params.put("afId", this.afId);
		for(Map.Entry<String,Object> param : params.entrySet()) {
			if(postData.length() != 0) postData.append('&');
	        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	        postData.append('=');
	        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		
		endPoint = "http://127.0.0.1:8080" + endPoint;
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
		StringBuilder getData = new StringBuilder();
		params.put("afId", this.afId);
		for(Map.Entry<String,Object> param : params.entrySet()) {
			if(getData.length() != 0) getData.append('&');
	        getData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	        getData.append('=');
	        getData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		
		endPoint = "http://127.0.0.1:8080" + endPoint;
		URL url = new URL(endPoint+ "?" + getData.toString());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        String response = this.convertStreamToString(conn.getInputStream());
		return this.decodeResponse(response);
	}
	
	private String decodeResponse(String response) throws UnsupportedEncodingException, ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONObject json = (JSONObject) jsonParser.parse(response);
		System.out.println("원문 Response");
		System.out.println(json.toString());
		
		if (json.get("data") != null ) {
			json.put("data", Aes256Crypto.decode(BASE64Util.decodeString(json.get("data").toString()), this.apiKey));
		}
		
		long total = (long)json.get("total");
		System.out.println("total : "  + total);
		System.out.println("복호화 Response");
		System.out.println(json.toString());
		
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
	 
	 * cacheKey
	 * cacheData
	 * */
	public String save_cache_data(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(ReturnpService.ENDPOINT_SAVE_CACHE_DATA, params);
		return result;
	}
	

	/**
	 * 캐시된 데이타 가져오기
	 * cacheKey
	 */
	public String getDataCache(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_CACHE_DATA, params);
		return result;
	}
	
	/**
	 * 회원 정보 가져오기
	 * memberEmail
	 */
	public String getMemberInfo(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_MEMBER_INFO, params);
		return result;
	}

	/**
	 * 중복 검사 처리, 등록되어 있는지 여부 
	 * checkExistType ( 1 : 이메일 중복 여부 , 2 : 전화번호 중복 여부 ) 
	 * memberEmail, memberPhone
	 */
	public String isRegistered(HashMap<String, Object> params) throws Exception {
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
	 */
	public String join(HashMap<String, Object> params) throws IOException, ParseException {
		String result = this.httpPost(ReturnpService.ENDPOINT_JOIN_UP,params);
		return result;
	}
	
	/**
	 * 회원 삭제
	 * memberEmail
	 * @return
	 * @throws Exception 
	 */
	public String deleteMember(HashMap<String, Object> params) throws Exception {
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
	 */
	public String modifyMember(HashMap<String, Object> params) throws IOException, ParseException {
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
	 */
	public String executeAccumualte(HashMap<String, Object> params) throws IOException, ParseException {
		String result = this.httpPost(ReturnpService.ENDPOINT_HANDLE_ACCUMULATE , params);
		return result;
	}
	
	/**
	 * 지원 언어 조회
	 * 파라메터 인자 없음
	 * @return
	 * @throws Exception 
	 */
	public String getLangs(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_LANGS,params);
		return result;
	}

	/**
	 * 은행 계좌 리스트 
 	 * memberEmail
	 * @return
	 * @throws Exception 
	 */
	public String getMemberBankAccounts(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_BANK_ACCOUNTS,params);
		return result;
	}

	public String registerBankAccount(HashMap<String, Object> params) throws IOException, ParseException {
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
	 */
	public String updateBankAccount(HashMap<String, Object> params) throws IOException, ParseException {
		String result = this.httpPost(ReturnpService.ENDPOINT_UPDATE_BANK_ACCOUNT,params);
		return result;
	}

	public String deleteBankAccount(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_DELETE_BANK_ACCOUNT,params);
		return result;
	}

	/**
	 * 정책 제공
	 */
	public String getPolicy(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_POLICY,params);
		return result;
	}

	/**
	 * G POINT 	 적립 내역
	 */
	public String getGpointAccumuateHistory(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GPOINT_ACCUMULATE_HISTORY,params);
		return result;
	}

	/**
	 * R POINT  전환 내역
	 * @return
	 */
	public String getRpointConversionHistory(HashMap<String, Object> params) throws Exception {
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
	public String registerWithdrawal(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(ReturnpService.ENDPOINT_REGISTER_WITHDRAWAL,params);
		return result;
	}

	/**
	 * 출금 신청 목록 가져오기
	 * memberEmail
	 */
	public String getWithdrawalHistory(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_WITHDRAWAL_HISTORY,params);
		return result;
	}
	
	/**
	 * 출금 정보 삭제
	 * memberEmail
	 * pointWithdrawalNo
	 */
	public String deleteWithdrawal(HashMap<String, Object> params) throws Exception {
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
	public String updatePointWithdrawal(HashMap<String, Object> params) throws Exception {
		String result = this.httpPost(ReturnpService.ENDPOINT_UPDATE_WITHDRAWAL,params);
		return result;
	}

	/**
	 * 나의 회원 목록 가져오기
	 * 
	 *  memberEmail
	 * @return
	 */
	public String getMyMembers(HashMap<String, Object> params) throws Exception {
		String result = this.httpGet(ReturnpService.ENDPOINT_GET_MY_MEMBERS,params);
		return result;
	}
	
	/*API 테스트 메서드 */
	public static void   callGetMemberInfo(ReturnpService returnpService) throws Exception {
		System.out.println("#### getMemberInfo");
		
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail", "topayc@naver.com");
		response = returnpService.getMemberInfo(param);
	}
	
	public static void   callGetMemberBankAccounts(ReturnpService returnpService) throws Exception {
		System.out.println("#### getMemberBankAccounts");
		
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail", "topayc@naver.com");
		response = returnpService.getMemberBankAccounts(param);
	}

	public static void   callIsRegistered(ReturnpService returnpService) throws Exception {
		System.out.println("#### getMemberBankAccounts");
		
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("checkValueType", "phone");
		param.put("memberPhone", "1111");
		//param.put("checkValueType", "email");
		//param.put("memberEmail", "topayc@naver.com");
		response = returnpService.isRegistered(param);
	}
	
	public static void   callMemberJoin(ReturnpService returnpService) throws Exception {
		//System.out.println("#### memberJoin");
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail", "topayc90010@naver.com");
		param.put("memberName", "안영철1000");
		param.put("memberPhone", "01098227407");
		param.put("memberPassword", "a9831000");
		param.put("country", "KR");
		param.put("recommenderEmail", "topayc1@naver.com");
		response = returnpService.join(param);
	}
	
	public static void   callDeleteMember(ReturnpService returnpService) throws Exception {
		//System.out.println("#### memberJoin");
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail", "topayc9000@naver.com");
		response = returnpService.deleteMember(param);
	}

	public static void   callModifyMember(ReturnpService returnpService) throws Exception {
		//System.out.println("#### memberJoin");
		String response = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("memberEmail", "topayc10000@naver.com");
		param.put("memberPhone", "01098227467");
		param.put("country", "US");
		response = returnpService.modifyMember(param);
	}

	public static void   callExecuteAccumlate(ReturnpService returnpService)  {
		//System.out.println("#### memberJoin");
		try {
			String response = null;
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("paymentApprovalAmount", "999999");
			param.put("paymentApprovalStatus", "0");
			param.put("paymentApprovalDateTime", "2019-03-01 10:10:10");
			param.put("paymentApprovalNumber", "90189121");
			param.put("afId", returnpService.getAfId());
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
	
	public static void main(String[] args) {
		try {
			ReturnpService returnpService = new ReturnpService("OTID_1549626897248", "62bcaa83ab2f465abd0c9fbdfa32bfc3");
			//ReturnpService.callGetMemberInfo(returnpService);
			//ReturnpService.callGetMemberBankAccounts(returnpService);
			//ReturnpService.callIsRegistered(returnpService);
			//ReturnpService.callMemberJoin(returnpService);
			//ReturnpService.callDeleteMember(returnpService);
			//ReturnpService.callModifyMember(returnpService);
			ReturnpService.callExecuteAccumlate(returnpService);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
