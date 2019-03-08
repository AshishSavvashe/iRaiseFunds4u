package com.appbell.iraisefund4u.common.service;

import android.content.Context;
import android.os.AsyncTask;

import com.appbell.common.codevalues.service.CodeValueConstants;
import com.appbell.common.web.util.WebConstants;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.exception.ApplicationException;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.appbell.iraisefund4u.resto.util.RestoAppCache;
import com.appbell.iraisefund4u.resto.db.entity.AppConfigData;
import com.appbell.iraisefund4u.resto.vo.ResponseVO;
import com.appbell.iraisefund4u.resto.vo.RowVO;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@SuppressWarnings("deprecation")
public class ServerCommunicationService implements AndroidAppConstants {
    private static final String CLASS_ID = "ServerCommunicationService:";

    protected Context context;
    private static HttpClient httpclt = null;

    public ServerCommunicationService(Context ctx) {
        this.context = ctx;
    }

    /**
     * server communication in async mode
     * @param postData
     * @param action
     * @param subaction
     * @return
     * @throws ApplicationException
     */
    public ResponseVO processServerRequestInAsyncMode(Map<String, String> postData, String action, String subaction, String jsonResponse)throws ApplicationException {
        return processServerRequest(postData, action, subaction, true, null, jsonResponse);
    }

    /**
     * server communication in callers's sync/async mode
     * @param postData
     * @param action
     * @param subaction
     * @return
     * @throws ApplicationException
     */
    public ResponseVO processServerRequestInSyncMode(Map<String, String> postData, String action, String subaction, String jsonResponse)throws ApplicationException{
        return processServerRequest(postData, action, subaction, false, null, jsonResponse);
    }

    /**
     * Method for sending request to Web Server getting response
     *
     * @param postData
     * @param action
     * @param subaction
     * @return response
     * @throws Exception
     */
    private ResponseVO processServerRequest(Map<String, String> postData, String action, String subaction, boolean asyncMode, String requestProcessorURL, String jsonResponse)throws ApplicationException{
        ResponseVO responseVo = new ResponseVO();

        String urlPart = "_action=" + action + "&_subAction=" + subaction;

        if(asyncMode){
            AndroidRequest req = new AndroidRequest();
            req.postData = postData;
            req.urlPart = urlPart;
            req.jsonResponse = jsonResponse;

            try{
                responseVo = new WebServerCommAsyncTask().execute(req).get();
            }catch(ExecutionException ee){
                AppLoggingUtility.logError(context,CLASS_ID + ee.getMessage());
                responseVo.addError("Error");
            }catch(InterruptedException ie){
                AppLoggingUtility.logError(context,CLASS_ID + ie.getMessage());
                responseVo.addError("Error");
            }catch(Exception e){
                AppLoggingUtility.logError(context,CLASS_ID + e.getMessage());
                responseVo.addError("Error");
            }

        }else{
            responseVo = postData2WebServer(postData, urlPart, requestProcessorURL, jsonResponse);
        }

        if(responseVo.hasErrors()){
            throw new ApplicationException(responseVo.getErrorMessage());
        }

        return responseVo;
    }


    private static HttpClient getHttpClient(){
        if(httpclt == null){

            BasicHttpParams params = new BasicHttpParams();
            ConnManagerParams.setMaxTotalConnections(params, 100);

            //Start - Changes for connection timeout
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Integer.valueOf(60000));
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, Integer.valueOf(65000));
            //End - Changes for connection timeout

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

            final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
            schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));

            ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
            httpclt = new DefaultHttpClient(cm, params);
        }
        return httpclt;
    }

    /**
     *
     * @param requestData
     * @param urlPart
     * @return
     * @throws Exception
     */
    protected ResponseVO postData2WebServer(Map<String, String> requestData, String urlPart, String requestProcessorURL, String jsonResponse) {
        ResponseVO responseVo = new ResponseVO();
        InputStream is = null;


        try {
            AppConfigData config = RestoAppCache.getAppConfig(context);

            requestProcessorURL = !AndroidAppUtil.isBlank(requestProcessorURL) ? requestProcessorURL : AndroidAppUtil.getAppRequestProcessorURL();

            String urlStr = urlPart != null ? (requestProcessorURL + urlPart) : requestProcessorURL;
            urlStr = urlStr + "&" + WebConstants.REQUEST_SOURCE + "=" + CodeValueConstants.REQUEST_SOURCE_AndroidApp;
            String[] appVersion = AndroidAppUtil.getAppVersionInfo(context);
            urlStr = urlStr + "&" + WebConstants.APP_VERSION + "=" + appVersion[0]+"/"+appVersion[1];
            urlStr = urlStr + "&" + WebConstants.APP_BUILD_CODE + "=" + WebConstants.APP_BUILD_CODE_1038;
            urlStr = urlStr + "&" + WebConstants.JSON_REQUEST + "=" + jsonResponse;

            HttpPost httppost = new HttpPost(urlStr);

            if (requestData != null) {
                Iterator<String> it = requestData.keySet().iterator();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                String key 	 = null;
                String value = null;
                while (it.hasNext()) {
                    key = AndroidAppUtil.trim(it.next());
                    value = AndroidAppUtil.trim(requestData.get(key));
                    nameValuePairs.add(new BasicNameValuePair(key, value));
                }
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }

            HttpResponse response = getHttpClient().execute(httppost);
            is = response.getEntity().getContent();

            if(CodeValueConstants.YesNo_Yes.equalsIgnoreCase(jsonResponse)){
                responseVo = parseJSONResponse(is);
            }else{
                new CloudRespSAXParser(is, responseVo).parseDocument();
            }

        } catch (UnsupportedEncodingException uee) {
            responseVo.addError("Error While Processing Request With Backend Service.");
            AppLoggingUtility.logErrorAndDndPost(context, uee, CLASS_ID + "UnsupportedEncodingException");
        } catch (ClientProtocolException cpe) {
            responseVo.addError("Could Not Connect With Backend Service.");
            AppLoggingUtility.logErrorAndDndPost(context, cpe, CLASS_ID + "ClientProtocolException");
        } catch (ConnectTimeoutException cte) {
            responseVo.addError("Server Is Too Busy.");
            AppLoggingUtility.logErrorAndDndPost(context, cte, CLASS_ID + "ConnectTimeoutException");
        } catch (HttpHostConnectException hhce) {
            responseVo.addError("Internet is not available, request can not be processed!!!");
            AppLoggingUtility.logErrorAndDndPost(context, hhce, CLASS_ID + "HttpHostConnectException");
        } catch (IOException ioe) {
            responseVo.addError("Error while connecting with server. Try again!!");
            AppLoggingUtility.logErrorAndDndPost(context, ioe, CLASS_ID + "IOException");
        } catch (ParserConfigurationException pce) {
            responseVo.addError("Error while parsing response from server.");
            AppLoggingUtility.logErrorAndDndPost(context, pce, CLASS_ID + "ParserConfigurationException");
        }catch (SAXException se) {
            responseVo.addError("Error while parsing response from server.");
            AppLoggingUtility.logErrorAndDndPost(context, se, CLASS_ID + "SAXException");
        } catch (JSONException e) {
            responseVo.addError("Error while parsing response from server.");
            AppLoggingUtility.logErrorAndDndPost(context, e, CLASS_ID + "JSONException ");
        } finally {
            try{
                if (is != null){
                    is.close();
				/*	if(android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.HONEYCOMB){
						is.reset();
					}else{
						is.close();
					}*/
                }
            }catch(IOException io){
                AppLoggingUtility.logError(context, CLASS_ID + io.getMessage());
            }
        }
        return responseVo;
    }



    /**
     * Asynchronous task for actual interaction with Web Server It will be done
     * in a background
     */
    private class WebServerCommAsyncTask extends AsyncTask<AndroidRequest, Void, ResponseVO> {
        @Override
        protected ResponseVO doInBackground(AndroidRequest... requestData) {
            ResponseVO response = new ServerCommunicationService(context).postData2WebServer(requestData[0].postData, requestData[0].urlPart, null, requestData[0].jsonResponse);
            return response;
        }

        @Override
        protected void onPostExecute(ResponseVO result) {
        }

        @Override
        protected void onPreExecute() {
        }
    }

    private class AndroidRequest {
        Map<String, String> postData = null;
        String urlPart = null;
        String jsonResponse;
    }



    /**
     *
     * @return
     * @throws ApplicationException
     */
    protected Map<String, String> createRequestObject(AppConfigData appConfig)throws ApplicationException{
        if(appConfig == null){
            throw new ApplicationException("Can not have null technical properties");
        }

        Map<String, String> requestData = new HashMap<>();

        return requestData;
    }

    private static CookieManager cookieManager = null;
    public static void initializeCookies(){
        if(cookieManager == null){
            cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
        }
    }


    protected String getCurrnencyType(String currency){
        String currencyType;
        if(CodeValueConstants.CURRENCY_TYPE_EUROS.equalsIgnoreCase(currency)){
            currencyType = context.getResources().getString(R.string.Currency_Euro);
        }else if(CodeValueConstants.CURRENCY_TYPE_RUPEES.equalsIgnoreCase(currency)){
            currencyType = context.getResources().getString(R.string.Currency_Rs);
        }else{
            currencyType = context.getResources().getString(R.string.Currency_Dollar);
        }
        return currencyType;
    }


    private ResponseVO parseJSONResponse(InputStream is) throws JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ResponseVO responseVO = new ResponseVO();
        JSONObject jsonObject = new JSONObject(sb.toString());

        if(jsonObject.has("error")){
            responseVO.addError(jsonObject.optString("error"));
        }else {
            responseVO.setJsonResponse(jsonObject);
        }
        return responseVO;
    }

    public void RestaurantDetails(int voucherBookID){

    }
}