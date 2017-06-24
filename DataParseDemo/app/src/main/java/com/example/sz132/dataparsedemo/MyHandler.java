package com.example.sz132.dataparsedemo;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by sz132 on 2017/6/24.
 */

public class MyHandler extends DefaultHandler {

    private static final String TAG = "MyHandler";

    private String nodeName;   // 节点名

    private StringBuilder id;
    private StringBuilder name;
    private StringBuilder version;

    //开始解析XML的时候调用
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        //初始化
        id = new StringBuilder();
        name = new StringBuilder();
        version = new StringBuilder();
    }

    //开始解析某个节点的时候调用
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        //记录当前节点
        nodeName = localName;
    }

    //获取节点中内容的时候调用
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        //根据当前的节点名判断内容添加到哪一个StringBuilder对象中
        if ("id".equals(nodeName)){
            id.append(ch, start, length);
        }else if ("name".equals(nodeName)){
            name.append(ch, start, length);
        }else if ("version".equals(nodeName)){
            version.append(ch, start, length);
        }
    }

    //完成某个节点的解析的时候调用
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ("app".equals(localName)){
            Log.i(TAG, "endElement: "+id.toString().trim());
            Log.i(TAG, "endElement: "+name.toString().trim());
            Log.i(TAG, "endElement: "+version.toString().trim());
            //清空StringBuilder
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }

    //完成XML解析的时候调用
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
