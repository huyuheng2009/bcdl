package com.yogapay.bcdl.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

public class Commons {
	
	private static final Logger log = LoggerFactory
			.getLogger(Commons.class);

    public static String  httpclientSend(String url, String content){
       return  httpclientSend(url,content,"text/xml; charset=GBK");
    }

	public static String httpclientSend(String url, String content,String contentType){
		PostMethod postMethod = new PostMethod(
				url);
//		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//                 new DefaultHttpMethodRetryHandler());
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,
                 new Integer(90000));

		try {
			postMethod.setRequestEntity(new StringRequestEntity(
					content, "text/xml", "GBK"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		postMethod.setRequestHeader("Content-type",
                StringUtils.isNotBlank(contentType)?contentType:"text/xml; charset=GBK");

		HttpClient httpClient = new HttpClient();
		String res = null;
		try {
			int status = httpClient.executeMethod(postMethod);
			log.info("status==="+status);

			res = new String(postMethod.getResponseBody(), "GBK");
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.info("http response:" + res);
		postMethod.releaseConnection();
		return res;
	}

    //空指针改为空字符串
    public static void clearNull(Map<String,Object> params){
        if(null!=params){
            Set<String> keySet = params.keySet();
            for(String key:keySet)
                params.put(key,params.get(key)==null?"":params.get(key));
        }
    }
}
