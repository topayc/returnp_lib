<?php

/*
@version 1.0
@author 안영철

 1.객체 생성 방법
 ReturnpService returnpService = new ReturnpService("온라인 가맹점 고유 번호", "API KEY", ReturnpService.SERVICE_MODE_DEVELOPE); </br> * </h1></b>

 2.생성자 인자 설명

 쇼핑몰 고유 번호 : 리턴포인트에서 생성 후 제공</br>
 API KEY : 리턴포인트에서 생성, 제공하며, 응답 데이타의 암, 복호화 및 클라이언트 식별에 사용</br>
 서비스 실행 모드 </br>
   - ReturnpService.SERVICE_MODE_DEVELOPE  :  <b>HTTP 로 개발 서버 접속</b></br>
   - ReturnpService.SERVICE_MODE_PRODUCT   :  <b>HTTPS 로 실제 운영 서버 접속</b> </br>
    (초기 개발시 개발 모드로 테스트후 운영 모드로 변경) </br>

 3.사용 방법 - 나의 회원 리스트 가져오기
    $returnpService = new ReturnpService("온라인 가맹점 고유 번호", "API KEY", ReturnpService.SERVICE_MODE_DEVELOPE);
    $returnpService-> get_my_point_infos("topayc@naver.com", "01088227467"); // 회원 포인트 정보 가져오기

    $resultCode = null;
    $message = null;
    $total = null;
    $data = null;

    if ($returnpService.get_response_code(). == ReturnpService::$RESPONSE_OK) {  // 서비스 연결과 처리상에서 에러가 없는 경우
        $resultCode = $$returnpService.get_result_code();   // 일반적인 처리 성공일 경우 "100"을 반환
        $message = $returnpService.get_message();         // 처리 결과 메시지</br>
        $total = $returnpService.get_total();                    // 배열 반환일 경우 해당 배열의 크기, 배열이 아닌 오브젝트 혹은 데이타가 없는 요청인 경우  -1 를 반환
        $data = $returnpService.get_data();                     // 실제 데이타 , 배열 혹은 오브젝트 JSON 스트링으로 반화
    }
 *
 4.서비스 요청에 대한 ResultCode(응답 코드)

  100 : 일반적인 요청에 대한 처리 성공
  300 : 요청에 대해서 수행할수 없음, 비즈니스 로직 처리 오류
  301 : 잘못된 API key
  302 : 해당 이메일에 대한 회원이 존재하지 않음
  303 : 해당 요청값은 중복됨
  304 : 해당 요청값은 중복되지 않음
  305 : 잘못된 가맹 고유 번호, 해당 고유번호의 가맹점이 존재 하지 않음
  306 : 이메일이 중복됨  (회원 가입시 반환될 수 있음)
  307 : 전화번호가 중복됨  (회원 가입시 반환될 수 있음)
  602 : 파라미터로 전달된 전화번호와 실제 디비의 전화번호가 일치하지 않음.
  611 : 존재하지 않는 결제 내역에 대한 취소 요청
  606 : 이미 적립 처리가 완료된 결제 내역
  619 : 등록되어 있지만 QR 적립이 가능한 가맹점이 아님
  500 : 서버 오류
 */

class ReturnpService {
    private $af_id;
    private $api_key;
    private $service_mode;
    private $result;

    private $ENDPOINT_SAVE_CACHE_DATA = "/pointback/v1/api/save_cache_data";
    private $ENDPOINT_GET_CACHE_DATA = "/pointback/v1/api/get_cache_data";
    private $ENDPOINT_GET_MEMBER_INFO = "/pointback/v1/api/get_member_info";
    private $ENDPOINT_IS_REGISTERED = "/pointback/v1/api/is_registered";
    private $ENDPOINT_JOIN_UP = "/pointback/v1/api/join_up";
    private $ENDPOINT_DELETE_MEMBER = "/pointback/v1/api/delete_member";
    private $ENDPOINT_MODIFY_MEMBER = "/pointback/v1/api/modify_member";
    private $ENDPOINT_HANDLE_ACCUMULATE = "/pointback/v1/api/handle_accumulate";
    private $ENDPOINT_LANGS = "/pointback/v1/api/langs";
    private $ENDPOINT_GET_BANK_ACCOUNTS = "/pointback/v1/api/get_bank_accounts";
    private $ENDPOINT_REGISTER_BANK_ACCOUNT = "/pointback/v1/api/register_bank_account";
    private $ENDPOINT_UPDATE_BANK_ACCOUNT = "/pointback/v1/api/update_bank_account";
    private $ENDPOINT_DELETE_BANK_ACCOUNT = "/pointback/v1/api/delete_bank_account";
    private $ENDPOINT_GET_POLICY = "/pointback/v1/api/get_policy";
    private $ENDPOINT_RPOINT_CONVERSION_HISTORY = "/pointback/v1/api/rpoint_conversion_history";
    private $ENDPOINT_REGISTER_WITHDRAWAL = "/pointback/v1/api/register_withdrawal";
    private $ENDPOINT_GET_WITHDRAWAL_HISTORY = "/pointback/v1/api/get_withdrawal_history";
    private $ENDPOINT_DELETE_WITHDRAWAL = "/pointback/v1/api/delete_withdrawal";
    private $ENDPOINT_CANCEL_WITHDRAWAL = "/pointback/v1/api/cancel_withdrawal";
    private $ENDPOINT_UPDATE_WITHDRAWAL = "/pointback/v1/api/update_withdrawal";
    private $ENDPOINT_GET_MY_MEMBERS = "/pointback/v1/api/get_my_members";
    private $ENDPOINT_GET_MY_POINT_INFOS= "/pointback/v1/api/get_my_point_infos";
    private $ENDPOINT_GET_GPOINT_ACCUMULATE_HISTORY= "/pointback/v1/api/get_gpoint_accumulate_history";

    public  static $SERVICE_MODE_LOCAL = "LOCAL";
    public  static $SERVICE_MODE_DEVLOPEMENT = "DEVELOPEMENT";
    public  static $SERVICE_MODE_PRODUCT = "PRODUCT";

    public  static $RESPONSE_OK = "1000";
    public  static $RESPONSE_ERROR = "2000";

    private $SERVICE_URL_LOCAL = "http://127.0.0.1:8080";
    private $SERVICE_URL_DEVLOPEMENT = "http://211.254.212.90:8083";
    private $SERVICE_URL_PRODUCT = "https://www.returnp.com:9094";


     public function __construct($af_id, $api_key, $service_mode) {
        if (empty($af_id) ||  empty($api_key)  || empty($service_mode)){
            throw new Exception("Parameter afi_id, api_key, service_mode required");
        }
        $this -> af_id = $af_id;
        $this -> service_mode = $service_mode;
        $this -> api_key = $api_key;
    }

    private function get_service_root_url(){
        $rootUrl = null;
        if ($this-> service_mode == self::$SERVICE_MODE_LOCAL) {
            $rootUrl =  $this->SERVICE_URL_LOCAL;
        } else if ($this-> service_mode == self::$SERVICE_MODE_PRODUCT) {
            $rootUrl = $this->SERVICE_URL_PRODUCT;

        } else if ($this-> service_mode == self::$SERVICE_MODE_DEVLOPEMENT) {
            $rootUrl = $this->SERVICE_URL_DEVLOPEMENT;
        } else {
            throw new Exception("unsupported ServiceMode");
        }
        return $rootUrl;
    }

    public function get($url, $param){
        $param['afId'] = $this -> af_id;
        $curl_session = curl_init();
        curl_setopt($curl_session, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($curl_session, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curl_session, CURLOPT_URL, $this->get_service_root_url().$url.'?'.http_build_query($param, '', '&'));
        $response = curl_exec($curl_session);
        curl_close ($curl_session);
        $this -> decode($response);


    }

    public  function post($url, $param){
        $param['afId'] = $this -> af_id;
        $curl_session = curl_init();
        curl_setopt($curl_session, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curl_session, CURLOPT_URL, $this->get_service_root_url().$url);
        curl_setopt($curl_session, CURLOPT_POST, 1);
        curl_setopt($curl_session, CURLOPT_POSTFIELDSIZE, 0);
        curl_setopt($curl_session, CURLOPT_POSTFIELDS, $param);
        $response = curl_exec($curl_session);
        curl_close ($curl_session);
        $this -> decode($response);
    }

    private function decode($response){
        $json = json_decode($response, true);
        if ($json['data'] != null) {
            //$json['data'] = $this -> aes_256_decode($json['data'] );
            $json['data'] = base64_decode($json['data'] );
        }
        $this -> result = $json;
    }

    private function aes_256_decode($data){
       $iv  = "2H4+HRD0Z6g1qmRw";
       $re = openssl_decrypt(base64_decode($data), "aes-256-cbc", $this ->api_key, true ,$iv);
       echo "복호화 데이타 : ". $re;
       return $re;
    }


    public function get_content_str(){return json_decode($this -> result);}
    public function get_content_json(){return $this -> result;}
    public function get_result_code(){return $this -> result['resultCode'];}
    public function get_data(){return $this -> result['data'];}
    public function get_message(){return $this -> result['message'];}
    public function get_total(){return $this -> result['total'];}
    public function get_response_code(){return $this -> result['responseCode'];}


    /**
     * 회원 정보 가져오기
     * @param $memberEmail 회원 이메일
     * @param $memberEmail 회원 전화번호 01098989812 형태
     */
    public function getMemberInfo($memberEmail, $memberPhone){
        $param['memberEmail'] = $memberEmail;
        $param['memberPhone'] = $memberPhone;
        $result = $this -> get($this ->ENDPOINT_GET_MEMBER_INFO, $param);
        return $result;
    }


    /**
     * 중복 여부 검사
     * @param $checkValueType 체크할 값 타입 , email : 이메일 중복 검사, phone : 핸드폰 중복 검사
     * @param $checkValue checkValueType 에따른 중복 검사할 값
     */
    public function is_registered($checkValueType, $checkValue)  {
        $param['checkValueType'] = $checkValueType;
        $param['checkValue'] = $checkValue;
        $result = $this -> get($this ->ENDPOINT_IS_REGISTERED, $param);
        return $result;
    }


    /**
     * 회원 가입
     * @param $memberEmail 회원 이메일
     * @param $memberName 회원 이름
     * @param $memberPhone 회원 핸드폰
     * @param $memberPassword  비밀번호
     * @param $memberPassword2 두번째 비밀번호(앞으로 사용예약)
     * @param $recommenderEmail 추천인 이메일 , 없으면 널 값
     */
    public function join($memberEmail, $memberName, $memberPhone, $memberPassword,$memberPassword2, $recommenderEmail, $joinRoute)  {
        $param['memberEmail'] = $memberEmail;
        $param['memberName'] = $memberName;
        $param['memberPhone'] = $memberPhone;
        $param['memberPassword'] = $memberPassword;
        $param['memberPassword2'] = $memberPassword2;
        $param['country'] = "KR";
        $param['joinRoute'] = $joinRoute;
        if ($recommenderEmail) {
            $param['recommenderEmail'] = "recommenderEmail";
        }
        $result = $this -> get($this ->ENDPOINT_JOIN_UP, $param);
        return $result;
    }

       /**
     * 포인트 적립 및 적립 취소 요청
     * @param $memberEmail 회원 이메일
     * @param $memberPhone  회원 전화번호
     * @param $paymentApprovalAmount  결제 금액
     * @param $paymentApprovalStatus   결제 상태 (0 : 결제 승인, 1 : 결제 취소)
     * @param $paymentApprovalDateTime  결제 시간(2019-03-01 10:10:10  형식) "yyyy-MM-dd hh:mm:ss" 형식
     * @param $paymentApprovalNumber 결제 번호
     */
    public function execute_accumualte($memberEmail, $memberPhone, $paymentApprovalAmount, $paymentApprovalStatus,  $paymentApprovalDateTime,$paymentApprovalNumber)  {
        $param['paymentApprovalAmount'] = $paymentApprovalAmount;
        $param['paymentApprovalStatus'] = $paymentApprovalStatus;
        $param['paymentApprovalDateTime'] = $paymentApprovalDateTime;
        $param['paymentApprovalNumber'] = $paymentApprovalNumber;
        $param['memberEmail'] = $memberEmail;
        $param['memberPhone'] = $memberPhone;
        $result = $this -> get($this ->ENDPOINT_HANDLE_ACCUMULATE, $param);
        return $result;
    }


    /**
     * 나의 포인트 정보 가져오기
     * @param $memberEmail 회원 이메일
     * @param $memberPhone 회원 전화번호 01098928781 형태
     */

    public function get_my_point_infos($memberEmail, $memberPhone)  {
        $param['memberEmail'] = $memberEmail;
         $param['memberPhone'] = $memberPhone;
        $result = $this -> get($this ->ENDPOINT_GET_MY_POINT_INFOS, $param);
        return $result;
    }
}

$returnpService = new ReturnpService("22222222", "2eff27c0760540ca98a5463dcb07cb3b",ReturnpService::$SERVICE_MODE_LOCAL);
//$returnpService-> getMemberInfo("topayc1@naver.com");
//$returnpService-> is_registered("phone", "0108822747");
$returnpService-> get_my_point_infos("topayc1@naver.com", "01088227467");

if ($returnpService->get_response_code() == ReturnpService::$RESPONSE_OK) {
    echo '-----------------------------------------------------------';
    printf("\n");
    echo $returnpService-> get_response_code();
    printf("\n");
    echo '-----------------------------------------------------------';
    printf("\n");
    echo $returnpService-> get_result_code();
    printf("\n");
    echo '-----------------------------------------------------------';
    printf("\n");
    echo $returnpService-> get_message();
    printf("\n");
    echo '-----------------------------------------------------------';
    printf("\n");
    echo $returnpService-> get_total();
    printf("\n");
    echo '-----------------------------------------------------------';
    printf("\n");
    echo $returnpService-> get_data();
}

 ?>
