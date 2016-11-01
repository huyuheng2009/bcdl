package com.yogapay.bcdl.utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtil {
	public static Map<String, Object> parser1Xml(String xml) {
        System.out.println("xml:"+xml);
		SAXReader saxReader = new SAXReader();   
		Map<String, Object> map = new HashMap<String, Object>();
		try {   
			Document document = saxReader.read(new ByteArrayInputStream(xml.getBytes("GBK")));   
			Element result=document.getRootElement();  
			map.put("stauts", "success");
			for(Iterator i = result.elementIterator(); i.hasNext();){   
				Element r = (Element) i.next();  
				map.put(r.getName(), r.getText());
			}   
		} catch (DocumentException e) {   
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}   
		return map;
		}   
	
	public static Map<String, Object> parser2Xml(String xml) {   
		SAXReader saxReader = new SAXReader();   
		Map<String, Object> map = new HashMap<String, Object>();
		try {   
			Document document = saxReader.read(new ByteArrayInputStream(xml.getBytes("GBK")));   
			Element result=document.getRootElement();  
			map.put("stauts", "success");
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for(Iterator i = result.elementIterator(); i.hasNext();){   
				Element r = (Element) i.next();  
				map.put(r.getName(), r.getText());
				for(Iterator j = r.elementIterator(); j.hasNext();){
					Map<String, Object> m = new HashMap<String, Object>();
					Element g = (Element) j.next();  
					m.put(g.getName(), g.getText());
					list.add(m);
				}
				map.put("list", list);
			}   
		} catch (DocumentException e) {   
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}   
		return map;
		}

    public static void main(String[] args){
        String response = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<Result>" +
                "<ThirdVoucher></ThirdVoucher>" +
                "<FrontLogNo></FrontLogNo>" +
                "</Result>";
        parser1Xml(response);
    }
}
