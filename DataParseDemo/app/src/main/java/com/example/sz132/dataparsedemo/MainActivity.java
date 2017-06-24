package com.example.sz132.dataparsedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private OkHttpClient client;
    private Request request;
    private Response response;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.tv);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    client = new OkHttpClient();
//                    request = new Request.Builder()
////                            .url("http://192.168.1.124:8080/get_data.xml")
//                            .url("http://192.168.1.124:8080/get_data.json")
//                            .build();
//                    response = client.newCall(request).execute();
////                    paresXMLWithPull(response.body().string());
////                    parseXMLWithSAX(response.body().string());
////                    parseJSONWithJSONObject(response.body().string());
//                    parseJSONWithGson(response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        Log.i(TAG, "onResponse: "+Thread.currentThread().getId());
        OkHttpUtil.sendRequest("http://192.168.1.124:8080/get_data.json", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: "+Thread.currentThread().getId());
                parseJSONWithGson(response.body().string());
            }
        });
    }

    /**
     * 使用官方gson解析JSON
     * @param string
     */
    private void parseJSONWithGson(final String string) {
        Log.i(TAG, "onResponse: "+Thread.currentThread().getId());
        Gson gson = new Gson();

        //解析成对象
//        Bean bean = gson.fromJson(string, Bean.class);

        //解析成数组
        List<Bean> list = gson.fromJson(string, new TypeToken<List<Bean>>(){}.getType());
        for (Bean bean:list) {
            Log.i(TAG, "parseJSONWithGson: "+bean.getId()+"--"+bean.getName()+"--"+bean.getVersion());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(string);
            }
        });
    }


    /**
     * 使用JSONObject解析JSON
     * @param string
     */
    private void parseJSONWithJSONObject(String string) {
        try {
            JSONArray array = new JSONArray(string);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                String id = object.getString("id");
                String name = object.getString("name");
                String version = object.getString("version");
                Log.i(TAG, "parseJSONWithJSONObject: "+id);
                Log.i(TAG, "parseJSONWithJSONObject: "+name);
                Log.i(TAG, "parseJSONWithJSONObject: "+version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * SAX解析XML
     * @param string
     */
    private void parseXMLWithSAX(String string) {
        try {
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            MyHandler handler = new MyHandler();
            //将MyHandler实例放入reader
            reader.setContentHandler(handler);
            //开始执行解析
            reader.parse(new InputSource(new StringReader(string)));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pull解析XML
     * @param string
     */
    private void paresXMLWithPull(String string) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(string));
            int eventType = parser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = parser.getName();
                switch (eventType){
                    //开始解析节点
                    case XmlPullParser.START_TAG:
                        if ("id".equals(nodeName)){
                            id = parser.nextText();
                        }else if ("name".equals(nodeName)){
                            name = parser.nextText();
                        }else if ("version".equals(nodeName)){
                            version = parser.nextText();
                        }
                        break;
                    //完成解析某个节点
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodeName)){
                            Log.i(TAG, "paresXMLWithPull: "+id);
                            Log.i(TAG, "paresXMLWithPull: "+name);
                            Log.i(TAG, "paresXMLWithPull: "+version);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析的XML数据
     * <apps>
        <app>
            <id>1</id>
            <name>Xiao</name>
            <version>1.0</version>
        </app>
        <app>
            <id>2</id>
            <name>Meng</name>
            <version>2.0</version>
        </app>
        <app>
            <id>3</id>
            <name>Jie</name>
            <version>3.0</version>
        </app>
     </apps>
     */
}
