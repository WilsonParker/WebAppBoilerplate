package com.example.webappboilerplate.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.webkit.WebView
import org.apache.http.util.EncodingUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

object NicePayUtility {
    const val REQUEST_CODE = 0x0020
    internal val ISP_LINK = "market://details?id=kvp.jjy.MispAndroid320"      //ISP 설치 링크
    internal val KFTC_LINK = "market://details?id=com.kftc.bankpay.android"    //금융결제원 설치 링크

    internal val MERCHANT_URL = "https://web.nicepay.co.kr/smart/mainPay.jsp"    //가맹점의 결제 요청 페이지 URL

    private var NICE_BANK_URL: String? = ""    // 계좌이체  인증후 거래 요청 URL

    // ISP앱에서 결제후 리턴받을 스키마 이름을 설정합니다.
    // AndroidManaifest.xml에 명시된 값과 동일한 값을 설정하십시요.
    // 스키마 뒤에 ://를 붙여주십시요.
    private val WAP_URL = "nicepaysample" + "://"

    private var BANK_TID: String? = ""        // 계좌이체 거래시 인증ID


    fun getyyyyMMddHHmmss(): String {
        /** yyyyMMddHHmmss Date Format  */
        val yyyyMMddHHmmss = SimpleDateFormat("yyyyMMddHHmmss")
        return yyyyMMddHHmmss.format(Date())
    }


    fun encrypt(strData: String): String { // ��ȣȭ ��ų ������
        var strOUTData = ""
        try {
            val md = MessageDigest.getInstance("MD5") // "MD5 �������� ��ȣȭ"

            md.reset()
            //byte[] bytData = strData.getBytes();
            //md.update(bytData);
            md.update(strData.toByteArray())
            val digest = md.digest()

            val hashedpasswd = StringBuffer()
            var hx: String

            for (i in digest.indices) {
                hx = Integer.toHexString(0xFF and digest[i].toInt())
                //0x03 is equal to 0x3, but we need 0x03 for our md5sum
                if (hx.length == 1) {
                    hx = "0$hx"
                }
                hashedpasswd.append(hx)

            }
            strOUTData = hashedpasswd.toString()
            val raw = strOUTData.toByteArray()
            val encodedBytes = Base64.encode(raw, 0)
            strOUTData = String(encodedBytes)
            //strOUTData = new String(raw);
        } catch (e: NoSuchAlgorithmException) {

        }

        return strOUTData  // ��ȣȭ�� �����͸� ����...
    }

    fun AlertDialog(title: String, message: String, context: Context) {
        var ab: AlertDialog.Builder? = null
        ab = AlertDialog.Builder(context)
        ab.setMessage(message)
        ab.setPositiveButton(android.R.string.ok, null)
        ab.setTitle(title)
        ab.show()
    }


    fun isPackageInstalled(ctx: Context, pkgName: String): Boolean {


        try {
            ctx.packageManager.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return false
        }

        return true
    }

    /**
     *
     * 계좌이체 데이터를 파싱한다.
     *
     * @param str
     * @return
     */
    fun makeBankPayData(str: String): String {
        val arr = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var parse_temp: Array<String>
        val tempMap = HashMap<String, String>()

        for (i in arr.indices) {
            try {
                parse_temp = arr[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                tempMap[parse_temp[0]] = parse_temp[1]
            } catch (e: Exception) {

            }

        }

        BANK_TID = tempMap["user_key"]
        NICE_BANK_URL = tempMap["callbackparam1"]
        return str
    }

    /**
     * ISP가 설치되지 않았을때 처리를 진행한다.
     *
     *
     */
    fun installISP(activity: Activity, webView: WebView) {
        val d = AlertDialog.Builder(activity)
        d.setMessage("ISP결제를 하기 위해서는 ISP앱이 필요합니다.\n설치 페이지로  진행하시겠습니까?")
        d.setTitle("ISP 설치")
        d.setPositiveButton("확인") { dialog, which ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ISP_LINK))
            activity.startActivity(intent)
        }
        d.setNegativeButton("아니요") { dialog, which ->
            dialog.cancel()
            //결제 초기 화면을 요청합니다.
            webView.postUrl(MERCHANT_URL, null)
        }
        d.show()
    }

    /**
     * 계좌이체 BANKPAY 설치 진행 안내
     *
     *
     */
    fun installKFTC(activity: Activity, webView: WebView) {
        val d = AlertDialog.Builder(activity)
        d.setMessage("계좌이체 결제를 하기 위해서는 BANKPAY 앱이 필요합니다.\n설치 페이지로  진행하시겠습니까?")
        d.setTitle("계좌이체 BANKPAY 설치")
        d.setPositiveButton("확인") { dialog, which ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(KFTC_LINK))
            activity.startActivity(intent)
        }
        d.setNegativeButton("아니요") { dialog, which ->
            dialog.cancel()
            webView.postUrl(MERCHANT_URL, null)
        }
        d.show()
    }

    fun onCreateOptionsMenu(menu: Menu): Boolean {
        //getMenuInflater().inflate(R.menu.activity_web_view, menu);
        return true
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent, webView: WebView, activity: Activity) {
        val resVal = data.extras!!.getString("bankpay_value")
        val resCode = data.extras!!.getString("bankpay_code")
        Log.i("NICE", "resCode : $resCode")
        Log.i("NICE", "resVal : $resVal")

        if ("091" == resCode) {//계좌이체 결제를 취소한 경우
            AlertDialog("인증 오류", "계좌이체 결제를 취소하였습니다.", activity)
            webView.postUrl(MERCHANT_URL, null)
        } else if ("060" == resCode) {
            AlertDialog("인증 오류", "타임아웃", activity)
            webView.postUrl(MERCHANT_URL, null)
        } else if ("050" == resCode) {
            AlertDialog("인증 오류", "전자서명 실패", activity)
            webView.postUrl(MERCHANT_URL, null)
        } else if ("040" == resCode) {
            AlertDialog("인증 오류", "OTP/보안카드 처리 실패", activity)
            webView.postUrl(MERCHANT_URL, null)
        } else if ("030" == resCode) {
            AlertDialog("인증 오류", "인증모듈 초기화 오류", activity)
            webView.postUrl(MERCHANT_URL, null)
        } else if ("000" == resCode) {    // 성공일 경우
            val postData = "callbackparam2=$BANK_TID&bankpay_code=$resCode&bankpay_value=$resVal"
            webView.postUrl(NICE_BANK_URL, EncodingUtils.getBytes(postData, "euc-kr"))
        }
    }
}