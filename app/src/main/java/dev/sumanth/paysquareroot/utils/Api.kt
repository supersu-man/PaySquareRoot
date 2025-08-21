package dev.sumanth.paysquareroot.utils

import android.util.Base64
import androidx.core.net.toUri
import okhttp3.FormBody
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.CookieHandler
import java.net.CookieManager
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private var httpClient: OkHttpClient? = null

fun getHttpClient(): OkHttpClient {
    if(httpClient == null) {
        val cookieManager: CookieHandler = CookieManager()
        httpClient = OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(cookieManager))
            .followRedirects(true)         // Enables HTTP redirects (3xx)
            .followSslRedirects(true)
            .build()
    }
    return httpClient as OkHttpClient
}

fun landingPage(): Document {
    val request = Request.Builder().url("https://mypayroll.paysquare.com/").build()
    val response = getHttpClient().newCall(request).execute()
    val responseBody = response.body.string()

    val soup = Jsoup.parse(responseBody)

    return soup
}

fun authenticate(soup: Document, username: String, password: String): Boolean {
    val sm = soup.select("script[src^=/default.aspx?_TSM_HiddenField_=ctl03_ToolkitScriptManager1_HiddenField]").attr("src")
    val sm2 = sm.replace("/default.aspx?_TSM_HiddenField_=ctl03_ToolkitScriptManager1_HiddenField&_TSM_CombinedScripts_=", "")
    val decodedString = URLDecoder.decode(sm2, StandardCharsets.UTF_8.toString())

    val viewstate = soup.select("input[name=__VIEWSTATE]").attr("value")
    val viewstategenerator = soup.select("input[name=__VIEWSTATEGENERATOR]").attr("value")
    val eventvalidation = soup.select("input[name=__EVENTVALIDATION]").attr("value")

    val key = "8080808080808080"
    val iv = "8080808080808080"



    val encryptedUsername = encryptAES_CBC_PKCS7(username, key, iv)
    val encryptedPassword = encryptAES_CBC_PKCS7(password, key, iv)

    val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("ctl03\$ToolkitScriptManager1", "ctl03\$UpdatePanel1|ctl03\$btnLogin")
        .addFormDataPart("ctl03_ToolkitScriptManager1_HiddenField", decodedString)
        .addFormDataPart("__EVENTTARGET", "")
        .addFormDataPart("__EVENTARGUMENT", "")
        .addFormDataPart("__VIEWSTATE", viewstate)
        .addFormDataPart("__VIEWSTATEGENERATOR", viewstategenerator)
        .addFormDataPart("__EVENTVALIDATION", eventvalidation)
        .addFormDataPart("fakeusernameremembered", "")
        .addFormDataPart("fakepasswordremembered", "")
        .addFormDataPart("ctl03\$txtuser", "")
        .addFormDataPart("ctl03\$RequiredFieldValidator1_ValidatorCalloutExtender_ClientState", "")
        .addFormDataPart("ctl03\$txtpassword", encryptedPassword)
        .addFormDataPart("ctl03\$ValidatorCalloutExtender1_ClientState", "")
        .addFormDataPart("ctl03\$HdnDIP", encryptedUsername)
        .addFormDataPart("ctl04\$txtUserName", "")
        .addFormDataPart("ctl04\$ValidatorCalloutExtender11_ClientState", "")
        .addFormDataPart("ctl04\$ValidatorCalloutExtender21_ClientState", "")
        .addFormDataPart("__ASYNCPOST", "true")
        .addFormDataPart("ctl03\$btnLogin.x", "9")
        .addFormDataPart("ctl03\$btnLogin.y", "12")
        .build()

    val request = Request.Builder().url("https://mypayroll.paysquare.com/default.aspx").post(body)
        .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
        .addHeader("host", "mypayroll.paysquare.com")
        .addHeader("origin", "https://mypayroll.paysquare.com")
        .addHeader("pragma", "no-cache")
        .addHeader("referer", "https://mypayroll.paysquare.com/")
        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
        .addHeader("x-microsoftajax", "Delta=true")
        .addHeader("x-requested-with", "XMLHttpRequest")
        .build()
    val response = getHttpClient().newCall(request).execute()
    val responsebody = response.body.string()
    if(!responsebody.contains("defaultuserredirect")) {
        println("Failed to authenticate")
        return false
    } else {
        println("Authenticated successfully")
        return true
    }
}

fun getRedirectUrl(): String {
    val request2 = Request.Builder().url("https://mypayroll.paysquare.com/defaultuserredirect.aspx").build()
    val response2 = getHttpClient().newCall(request2).execute()
    val body2 = response2.body.string()
    val redirectUrl = body2.split("location.href='")[1].split("';")[0]
    return redirectUrl
}

fun encryptAES_CBC_PKCS7(input: String, keyStr: String, ivStr: String): String {
    val keyBytes = keyStr.toByteArray(Charsets.UTF_8)
    val ivBytes = ivStr.toByteArray(Charsets.UTF_8)

    val keySpec = SecretKeySpec(keyBytes, "AES")
    val ivSpec = IvParameterSpec(ivBytes)

    val cipher =
        Cipher.getInstance("AES/CBC/PKCS5Padding") // PKCS5Padding works the same as PKCS7 in Java
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

    val encrypted = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
    return Base64.encodeToString(encrypted, Base64.DEFAULT)

}

fun getAccessTokenObject(username: String?, isLms: Boolean): JSONObject {
    val endpoint = if(isLms) "https://mylmsapi.paysquare.com" else "https://mypayrollapi.payrollengine.net"
    val formBody = "grant_type=password&username=$username&password=$username"
    val requestBody = formBody.toRequestBody("application/x-www-form-urlencoded".toMediaType())
    val request = Request.Builder().post(requestBody).url("$endpoint/token")
        .addHeader("accept", "application/json, text/plain, */*")
        .addHeader("accept-encoding", "gzip, deflate, br, zstd")
        .addHeader("accept-language", "en-US,en;q=0.9")
        .addHeader("connection", "keep-alive")
        .addHeader("content-type", "application/x-www-form-urlencoded;charset=UTF-8")
        .addHeader("host", endpoint.removePrefix("https://"))
        .addHeader("origin", endpoint)
        .addHeader("referer", "$endpoint/")
        .addHeader("sec-fetch-dest", "empty")
        .addHeader("sec-fetch-mode", "cors")
        .addHeader("sec-fetch-site", "same-site")
        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
        .build()

    val response = getHttpClient().newCall(request).execute()

    val body = response.body.string()
    val jsonObj = JSONObject(body)

    return jsonObj
}

fun getParams(redirectUrl: String): Map<String, String> {
    val fragment = redirectUrl.toUri().fragment ?: ""
    val queryPart = fragment.substringAfter("?", "")

    val params = queryPart.split("&").associate {
        it.substringBefore("=") to it.substringAfter("=")
    }

    return params
}

fun dummyRequests(redirectUrl: String) {
    val request5 = Request.Builder().url(redirectUrl).build()
    getHttpClient().newCall(request5).execute()

    val request3 = Request.Builder().url("https://mypayrollui.payrollengine.net/app/auth/views/ssoin.html").build()
    getHttpClient().newCall(request3).execute()
}

fun getData(accessTokenObject: JSONObject, params: Map<String,String>, loginUrl: String, isLms: Boolean): JSONObject {
    val endpoint = if(isLms) "https://mylmsapi.paysquare.com" else "https://mypayrollapi.payrollengine.net"
    val accessToken = accessTokenObject["access_token"] as String
    val onresuerauqsyap = params["onresuerauqsyap"] as String
    val ynapmocerauqsyap = params["ynapmocerauqsyap"] as String
    val etaderauqsyap = params["etaderauqsyap"] as String
    val formBody = FormBody.Builder()
        .add("CompanyId", ynapmocerauqsyap)
        .add("CustomParam", etaderauqsyap)
        .add("EmpCode", onresuerauqsyap)
        .add("LoginUrl", loginUrl)
        .build()
    val request = Request.Builder().url("$endpoint/api/Login/SSOIN")
        .post(formBody)
        .addHeader("accept", "application/json, text/plain, */*")
        .addHeader("host", endpoint.removePrefix("https://"))
        .addHeader("origin", endpoint)
        .addHeader("referer", "$endpoint/")
        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0")
        .addHeader("authorization", "Bearer $accessToken")
        .build()
    val response = getHttpClient().newCall(request).execute()
    val responseBody = response.body.string()
    val jsonObject = JSONObject(responseBody)
    val data = jsonObject.getJSONObject("data")
    return data
}

fun getDashboardData(accessTokenObject: JSONObject): JSONObject {
    val accessToken = accessTokenObject["access_token"] as String
    val formBody = FormBody.Builder().add("SelMonthYear", true.toString()).build()
    val request = Request.Builder().url("https://mypayrollapi.payrollengine.net/api/Dashboard/GetDashboardData").post(formBody)
        .addHeader("accept", "application/json, text/plain, */*")
        .addHeader("host", "mypayrollapi.payrollengine.net")
        .addHeader("origin", "https://mypayrollui.payrollengine.net")
        .addHeader("referer", "https://mypayrollui.payrollengine.net/")
        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0")
        .addHeader("authorization", "Bearer $accessToken")
        .build()
    val response = getHttpClient().newCall(request).execute()
    val responseBody = response.body.string()
    val jsonObject = JSONObject(responseBody)
    return jsonObject
}

fun SSOUT(accessTokenObject: JSONObject) {
    val accessToken = accessTokenObject["access_token"] as String
    val formBody = FormBody.Builder().add("ActionState", "LMS").build()
    val request = Request.Builder().url("https://mypayrollapi.payrollengine.net/api/Login/SSOOUT").post(formBody)
        .addHeader("accept", "application/json, text/plain, */*")
        .addHeader("host", "mypayrollapi.payrollengine.net")
        .addHeader("origin", "https://mypayrollui.payrollengine.net")
        .addHeader("referer", "https://mypayrollui.payrollengine.net/")
        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0")
        .addHeader("authorization", "Bearer $accessToken")
        .build()
    val response = getHttpClient().newCall(request).execute()

    val json = JSONObject(response.body.string())
    val url = json.getString("description")

    val request5 = Request.Builder().url(url).build()
    getHttpClient().newCall(request5).execute()

    val request6 = Request.Builder().url("https://mylmsui.paysquare.com/").build()
    getHttpClient().newCall(request6).execute()

    val request7 = Request.Builder().url("https://mylmsui.paysquare.com/app/auth/views/ssoin.html").build()
    getHttpClient().newCall(request7).execute()
}

fun getLmsDashboardData(accessTokenObject: JSONObject): JSONObject {
    val accessToken = accessTokenObject["access_token"] as String
    val body = "{}".toRequestBody("application/json; charset=utf-8".toMediaType());
    val request = Request.Builder().url("https://mylmsapi.paysquare.com/api/Dashboard/GetDashboardData").post(body)
        .addHeader("accept", "application/json, text/plain, */*")
        .addHeader("host", "mylmsapi.paysquare.com")
        .addHeader("origin", "https://mylmsui.paysquare.com")
        .addHeader("referer", "https://mylmsui.paysquare.com/")
        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0")
        .addHeader("authorization", "Bearer $accessToken")
        .build()

    val response = getHttpClient().newCall(request).execute()
    val responseBody = response.body.string()
    val jsonObject = JSONObject(responseBody)
    return jsonObject
}

fun getPunchTimes(accessTokenObject: JSONObject): JSONObject {
    val accessToken = accessTokenObject["access_token"] as String

    val formBody = FormBody.Builder()
        .add("param1", "Employee")
        .add("FromDate", getUtcWindowFromDate(LocalDate.now()).first)
        .add("ToDate", getUtcWindowFromDate(LocalDate.now()).second)
        .build()
    val request = Request.Builder().url("https://mylmsapi.paysquare.com/api/showEmpPunches/searchEmpPunchList").post(formBody)
        .addHeader("accept", "application/json, text/plain, */*")
        .addHeader("host", "mylmsapi.paysquare.com")
        .addHeader("origin", "https://mylmsui.paysquare.com")
        .addHeader("referer", "https://mylmsui.paysquare.com/")
        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0")
        .addHeader("authorization", "Bearer $accessToken")
        .build()

    val response = getHttpClient().newCall(request).execute()
    val responseBody = response.body.string()
    val jsonObject = JSONObject(responseBody)
    return jsonObject
}

fun getHolidayList(accessTokenObject: JSONObject): JSONObject {
    val accessToken = accessTokenObject["access_token"] as String
    val formBody = FormBody.Builder().add("Year", "2025").build()
    val request = Request.Builder().url("https://mylmsapi.paysquare.com/api/Reports/holidaylist").post(formBody)
        .addHeader("accept", "application/json, text/plain, */*")
        .addHeader("host", "mylmsapi.paysquare.com")
        .addHeader("origin", "https://mylmsui.paysquare.com")
        .addHeader("referer", "https://mylmsui.paysquare.com/")
        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0")
        .addHeader("authorization", "Bearer $accessToken")
        .build()

    val response = getHttpClient().newCall(request).execute()
    val responseBody = response.body.string()
    val jsonObject = JSONObject(responseBody)
    return jsonObject
}

fun getChangelogJson(): JSONArray {
    val request = Request.Builder().url("https://raw.githubusercontent.com/supersu-man/supersu-man.github.io/refs/heads/main/src/assets/paysquareroot/changelog.json").build()
    val response = getHttpClient().newCall(request).execute()
    val responseBody = response.body.string()
    val jsonArray = JSONArray(responseBody)
    return jsonArray
}
fun getUtcWindowFromDate(date: LocalDate): Pair<String, String> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    val start = date
        .minusDays(1)
        .atTime(18, 30)
        .atZone(ZoneOffset.UTC)
    val end = start
        .plusDays(1)
        .withHour(18)
        .withMinute(29)
        .withSecond(59)
        .withNano(999_000_000)

    return Pair(start.format(formatter), end.format(formatter))
}


