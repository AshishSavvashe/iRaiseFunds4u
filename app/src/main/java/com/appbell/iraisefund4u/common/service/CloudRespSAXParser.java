package com.appbell.iraisefund4u.common.service;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.resto.vo.ResponseVO;
import com.appbell.iraisefund4u.resto.vo.RowVO;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class CloudRespSAXParser extends DefaultHandler {
    ResponseVO res = null;
    boolean e = false;
    boolean m = false;
    InputStream is;

    public CloudRespSAXParser(String str, ResponseVO response) {
        res = response;
        is = new ByteArrayInputStream(str.getBytes());
    }

    public CloudRespSAXParser(InputStream istrm, ResponseVO response) {
        res = response;
        is = istrm;
    }

    public void parseDocument() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        InputSource source = new InputSource(is);
        source.setEncoding("ISO-8859-1");

        parser.parse(source, this);
    }

    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {
        msg = new StringBuilder();
        if(elementName.equalsIgnoreCase("e")) {
            e = true;
        }

        if(elementName.equalsIgnoreCase("m")) {
            m = true;
        }

        if(elementName.equalsIgnoreCase("s")) {
            RowVO row = null;
            if(res.getTable().getRowCount() <= 0){
                row = new RowVO();
                res.getTable().addRow(row);
            }else{
                row = res.getTable().getRow(0);
            }
            row.addColVal(AppUtil.trim(attributes.getValue("name")), AppUtil.trim(attributes.getValue("value")));
        }
    }

    StringBuilder msg = null;
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(e){
            res.addError(AppUtil.trim(msg.toString()));
            e = false;
        }

        if(m){
            res.addMessage(AppUtil.trim(msg.toString()));
            m = false;
        }

    }

    public void characters(char ch[], int start, int length) throws SAXException {
        msg.append(new String(ch, start, length));
    }
}
