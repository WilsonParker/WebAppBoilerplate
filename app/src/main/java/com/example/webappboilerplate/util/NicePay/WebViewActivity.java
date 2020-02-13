package com.example.webappboilerplate.util.NicePay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.*;
import com.example.webappboilerplate.R;
import com.example.webappboilerplate.web.AndroidBridge;
import com.example.webappboilerplate.web.CustomWebView;
import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;

public class WebViewActivity extends Activity {
    final String ISP_LINK =  "market://details?id=kvp.jjy.MispAndroid320";      //ISP 설치 링크
    final String KFTC_LINK = "market://details?id=com.kftc.bankpay.android";    //금융결제원 설치 링크

    final String MERCHANT_URL = "https://web.nicepay.co.kr/smart/mainPay.jsp";	//가맹점의 결제 요청 페이지 URL

    private String NICE_BANK_URL = "";	// 계좌이체  인증후 거래 요청 URL

     /**
       * Custom update
       *
       * @author      Hare
       * @added       2019-06-23
       * @updated     2019-06-23
       * */
    private CustomWebView mWebView;			// 웹뷰 인스턴스
    AndroidBridge bridge;


    // ISP앱에서 결제후 리턴받을 스키마 이름을 설정합니다.
    // AndroidManaifest.xml에 명시된 값과 동일한 값을 설정하십시요.
    // 스키마 뒤에 ://를 붙여주십시요.
    private String WAP_URL = "nicepaysample"+"://";

    private String BANK_TID = "";		// 계좌이체 거래시 인증ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        String urlString = null;

        Uri uri = intent.getData();
        mWebView = findViewById(R.id.webview);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClientClass());

         /**
           * Android Bridge 추가
           *
           * @author      Hare
           * @added       2019-06-22
           * @updated     2019-06-22
           * */
        bridge = new AndroidBridge(this, mWebView);
        mWebView.addJavascriptInterface(bridge, "app_android");

        // Webview 설정 - javascript 허용
        mWebView.getSettings().setJavaScriptEnabled(true);

        //Webview 설정 - 캐시사용
        /**
         * WebView에서 캐시사용 관련 Default 설정은 WebSettings.LOAD_DEFAULT 입니다.
         * ex) mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
         * 가급적 캐시 사용 설정을 변경하지 않을것을 권고 드립니다.
         * @중요 : 'WebSettings.LOAD_CACHE_ELSE_NETWORK' 로 변경금지.
         */

        // Webview 설정 - 쿠키 등 결제를 위한 설정
        /**************************************************************
         * 안드로이드 5.0 이상으로 tagetSDK를 설정하여 빌드한경우 아래 구문을 추가하여 주십시요
         **************************************************************/
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }

        /**************************************************************
         * 안드로이드 5.0 이전 버전으로 tagetSDK를 설정하여 빌드한경우 아래 구문을 추가하여 주십시요
         **************************************************************/
        /*
        CookieManager cookieManager = CookieManager.Z();
        cookieManager.setAcceptCookie(true);
        */

        if( uri != null ) {
            String url = uri.toString();
            Log.i("NICE","NicePay onCreate url :" + url);
            if (url.startsWith(WAP_URL)) {
                // 결제결과 url을 설정한다.
                urlString = url;
                urlString = url.substring(WAP_URL.length());
                mWebView.loadUrl(urlString);

            } else
                mWebView.loadUrl(url);
        } else {
            // 결제 요청할 가맹점의 웹페이지를 호출합니다.
            // 가맹점에 맞는 URL로 변경하십시요. 해당 웹페이지는 NICEPAY_SMART_WEB을 참조합니다.
            mWebView.postUrl(MERCHANT_URL, null);
        }
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("NICE","NicePay OverrideUrlLoading : "+url);
            Intent intent = null;
            // 웹뷰에서 ispmobile  실행한 경우...
            if( url.startsWith("ispmobile") ) {
                if( Utility.isPackageInstalled(getApplicationContext(), "kvp.jjy.MispAndroid320") ) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else {
                    installISP();
                    return true;
                }

                // 웹뷰에서 계좌이체를 실행한 경우...
            } else  if(url.startsWith("kftc-bankpay") ) {
                if( Utility.isPackageInstalled(getApplicationContext(), "com.kftc.bankpay.android") ) {
                    String sub_str_param = "kftc-bankpay://eftpay?";
                    String reqParam = url.substring(sub_str_param.length());
                    try {
                        reqParam = URLDecoder.decode(reqParam,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    reqParam = makeBankPayData(reqParam);

                    intent = new Intent(Intent.ACTION_MAIN);
                    intent.setComponent(new ComponentName("com.kftc.bankpay.android","com.kftc.bankpay.android.activity.MainActivity"));
                    intent.putExtra("requestInfo",reqParam);
                    startActivityForResult(intent,1);

                    return true;
                } else{
                    installKFTC();
                    return true;
                }

                // 웹뷰에서 안심클릭을 실행한 경우...
            } else if (url != null	&& (url.contains("vguard")
                    || url.contains("droidxantivirus")
                    || url.contains("lottesmartpay")
                    || url.contains("smshinhancardusim://")
                    || url.contains("shinhan-sr-ansimclick")
                    || url.contains("v3mobile")
                    || url.endsWith(".apk")
                    || url.contains("smartwall://")
                    || url.contains("appfree://")
                    || url.contains("market://")
                    || url.contains("ansimclick://")
                    || url.contains("ansimclickscard")
                    || url.contains("ansim://")
                    || url.contains("mpocket")
                    || url.contains("mvaccine")
                    || url.contains("market.android.com")
                    || url.startsWith("intent://")
                    || url.contains("samsungpay")
                    || url.contains("droidx3web://")
                    || url.contains("kakaopay")
                    || url.contains("callonlinepay")	//2018-01-15 LG페이추가
                    || url.contains("http://m.ahnlab.com/kr/site/download"))) {

                try{
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Log.i("NICE","intent getDataString +++===>"+intent.getDataString());

                    } catch (URISyntaxException ex) {
                        Log.e("Browser","Bad URI " + url + ":" + ex.getMessage());
                        return false;
                    }

                    if( url.startsWith("intent") ) { //chrome 버젼 방식
                        if( getPackageManager().resolveActivity(intent,0)==null ) {
                            String packagename=intent.getPackage();
                            if( packagename !=null ) {
                                Uri uri = Uri.parse("market://search?q=pname:"+packagename);
                                intent = new Intent(Intent.ACTION_VIEW,uri);
                                startActivity(intent);
                                return true;
                            }
                        }

                        Uri uri = Uri.parse(intent.getDataString());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);

                        return true;
                    } else { //구 방식
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        //return true;
                    }
                } catch( Exception e ) {
                    Log.i("NICE", e.getMessage());
                    return false;
                }
            }
            // ispmobile에서 결제 완료후 스마트주문 앱을 호출하여 결제결과를 전달하는 경우
            else if (url.startsWith(WAP_URL)) {
                String thisurl = url.substring(WAP_URL.length());
                view.loadUrl(thisurl);
                return  true;
            } else {
                view.loadUrl(url);
                return  false;
            }

            return true;
        }
    }

    /**
     *
     * 	계좌이체 결과값을 받아와 오류시 해당 메세지를, 성공시에는 결과 페이지를 호출한다.
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String resVal = data.getExtras().getString("bankpay_value");
        String resCode = data.getExtras().getString("bankpay_code");
        Log.i("NICE","resCode : "+ resCode);
        Log.i("NICE","resVal : "+ resVal);

        if("091".equals(resCode)){//계좌이체 결제를 취소한 경우
            Utility.AlertDialog("인증 오류", "계좌이체 결제를 취소하였습니다.",  WebViewActivity.this);
            mWebView.postUrl(MERCHANT_URL, null);
        } else if("060".equals(resCode)){
            Utility.AlertDialog("인증 오류", "타임아웃",  WebViewActivity.this);
            mWebView.postUrl(MERCHANT_URL, null);
        } else if("050".equals(resCode)){
            Utility.AlertDialog("인증 오류", "전자서명 실패",  WebViewActivity.this);
            mWebView.postUrl(MERCHANT_URL, null);
        } else if("040".equals(resCode)){
            Utility.AlertDialog("인증 오류", "OTP/보안카드 처리 실패",  WebViewActivity.this);
            mWebView.postUrl(MERCHANT_URL, null);
        } else if("030".equals(resCode)){
            Utility.AlertDialog("인증 오류", "인증모듈 초기화 오류",  WebViewActivity.this);
            mWebView.postUrl(MERCHANT_URL, null);
        } else if("000".equals(resCode)){	// 성공일 경우
            String postData = "callbackparam2="+BANK_TID+"&bankpay_code="+resCode+"&bankpay_value="+resVal;
            mWebView.postUrl(NICE_BANK_URL, EncodingUtils.getBytes(postData,"euc-kr"));
        }
    }

    /**
     *
     * 계좌이체 데이터를 파싱한다.
     *
     * @param str
     * @return
     */
    private String makeBankPayData(String str) {
        String[] arr = str.split("&");
        String[] parse_temp;
        HashMap<String, String> tempMap = new HashMap<String,String> ();

        for( int i=0;i<arr.length;i++ ) {
            try {
                parse_temp = arr[i].split("=");
                tempMap.put(parse_temp[0], parse_temp[1]);
            } catch(Exception e){

            }
        }

        BANK_TID = tempMap.get("user_key");
        NICE_BANK_URL = tempMap.get("callbackparam1");
        return str;
    }

    /**
     * 	ISP가 설치되지 않았을때 처리를 진행한다.
     *
     *
     */
    private void installISP() {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setMessage("ISP결제를 하기 위해서는 ISP앱이 필요합니다.\n설치 페이지로  진행하시겠습니까?");
        d.setTitle( "ISP 설치" );
        d.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ISP_LINK));
                startActivity(intent);
            }
        });
        d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //결제 초기 화면을 요청합니다.
                mWebView.postUrl(MERCHANT_URL, null);

            }
        });
        d.show();
    }
    /**
     * 	계좌이체 BANKPAY 설치 진행 안내
     *
     *
     */
    private void installKFTC() {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setMessage("계좌이체 결제를 하기 위해서는 BANKPAY 앱이 필요합니다.\n설치 페이지로  진행하시겠습니까?");
        d.setTitle( "계좌이체 BANKPAY 설치" );
        d.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(KFTC_LINK));
                startActivity(intent);
            }
        });
        d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mWebView.postUrl(MERCHANT_URL, null);
            }
        });
        d.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_web_view, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        bridge.finishAndHome();
    }
}
