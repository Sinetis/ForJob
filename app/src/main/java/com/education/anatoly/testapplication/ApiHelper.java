package com.education.anatoly.testapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Anatoly on 13.04.2019.
 */

/**
 *
 */
public class ApiHelper {

    /**
     *
     */
    static private String API_SITE = "http://api.pixlpark.com";

    /**
     *
     */
    static private String GET_REQUEST_TOKEN = "/oauth/requesttoken";
    static private String GET_ACCESS_TOKEN = "/oauth/accesstoken";
    static private String GET_ORDER_LIST = "/orders";

    static private String GET = "GET";
    static private String POST = "POST";

    /**
     *
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static String GetRequestToken()
            throws JSONException, IOException{

        String str;
        str = Request(API_SITE + GET_REQUEST_TOKEN, GET, null);

        JSONObject json = new JSONObject(str);
        return json.getString("RequestToken");
    }

    /**
     *
     * @param requestToken
     * @param publicKey
     * @param privateKey
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static String GetAccessToken(String requestToken, String publicKey,  String privateKey)
            throws JSONException, IOException{

        String str;
        str = Request(
                API_SITE + GET_ACCESS_TOKEN,
                GET,
                new String[][]{
                        {"oauth_token", requestToken},
                        {"grant_type", "api"},
                        {"username", publicKey},
                        {"password", Hash.SHA1(requestToken+privateKey)}}
        );

        JSONObject json = new JSONObject(str);
        return json.getString("AccessToken");
    }

    /**
     *
     * @param accessToken
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static String GetOrderList(String accessToken)
            throws JSONException, IOException{

        String str;
        str = Request(
                API_SITE + GET_ORDER_LIST,
                GET,
                new String[][]{
                        {"oauth_token", accessToken}}
        );

        return str;
    }

    /**
     *
     * @param address
     * @param type
     * @param params
     * @return
     * @throws IOException
     */
    static private String Request(String address, String type, String[][] params)
            throws IOException{

        StringBuilder result = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            URL site = new URL(BuildEndAddress(address, params));

            connection = (HttpURLConnection) site.openConnection();

            connection.setRequestMethod(type);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String str;
            while ((str = reader.readLine()) != null)
                result.append(str);
        }
        finally {
            if (connection != null)
                connection.disconnect();
        }
        return result.toString();
    }

    /**
     *
     * @param address
     * @param params
     * @return
     */
    public static String BuildEndAddress(String address, String[][] params){
        StringBuilder endAddress = new StringBuilder(address);
        if (params != null) {
            endAddress
                    .append("?")
                    .append(params[0][0])
                    .append("=")
                    .append(params[0][1]);
            for (int i = 1; i < params.length; i++)
                endAddress
                        .append("&")
                        .append(params[i][0])
                        .append("=")
                        .append(params[i][1]);
        }
        return endAddress.toString();
    }
}
