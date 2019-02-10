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
	public static String ENDPOINT_GET_MEMBER_INFO = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_SAVE_CACHE_DATA = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_GET_CACHE_DATA = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_CHECK_DUPLICATED = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_JOIN_UP = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_DELETE_MEMBER = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_MODIFY_MEMBER = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_HANDLE_ACCUMULATE = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_ACCUMULAGE_BY_PAN = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_CANCEL_ACCUMULATE_BY_PAN = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_LANGS = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_GET_BANK_ACCOUNTS = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_REGISTER_BANK_ACCOUNT = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_UPDATE_BANK_ACCOUNT = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_DELETE_BANK_ACCOUNT = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_GET_POLICY = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_GPOINT_ACCUMULATE_HISTORY = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_RPOINT_CONVERSION_HISTORY = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_REGISTER_WITHDRAWAL = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_GET_WITHDRAWAL_HISTORY = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_DELETE_WITHDRAWAL = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_UPDATE_WITHDRAWAL = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	public static String ENDPOINT_GET_MY_MEMBERS = "http://127.0.0.1:8080/pointback/v1/api/get_member_info";
	private String sfId;
	private String apiKey;
	
	ReturnpService(String sfId, String apiKey){
		this.sfId = sfId;
		this.apiKey = apiKey;
	}
	
	
	private String post(String endPoint, HashMap<String, Object> params) throws IOException, ParseException {
		StringBuilder postData = new StringBuilder();
		params.put("sfId", this.sfId);
		for(Map.Entry<String,Object> param : params.entrySet()) {
			if(postData.length() != 0) postData.append('&');
	        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	        postData.append('=');
	        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}

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
	
	private String get(String endPoint, HashMap<String, Object> params) throws Exception {
		StringBuilder getData = new StringBuilder();
		params.put("sfId", this.sfId);
		for(Map.Entry<String,Object> param : params.entrySet()) {
			if(getData.length() != 0) getData.append('&');
	        getData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	        getData.append('=');
	        getData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		
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
		json.put("data", Aes256Crypto.decode(BASE64Util.decodeString(json.get("data").toString()), this.apiKey));
		System.out.println("decodeResponse");
		
		JSONParser parser= new JSONParser();
		JSONObject dataJson = (JSONObject) parser.parse(json.get("data").toString());
		System.out.println(dataJson.get("memberName").toString());
		return  json.toString();
	}

	public String getMemberInfo(HashMap<String, Object> params) throws Exception {
		String result = this.get(ReturnpService.ENDPOINT_GET_MEMBER_INFO, params);
		return result;
	}

	public String join(HashMap<String, Object> params) throws IOException, ParseException {
		String result = this.post(ReturnpService.ENDPOINT_GET_MEMBER_INFO,params);
		return result;
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
	
	public static void main(String[] args) {
		try {
			ReturnpService conn = 
				new ReturnpService("OTID_1549615398051", "f130c6befd7e4ddf9f9425d34e0838b9");
			
			/*getMemberInfo 테스트*/
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("memberEmail", "topayc@naver.com");
			System.out.println(conn.getMemberInfo(param));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
