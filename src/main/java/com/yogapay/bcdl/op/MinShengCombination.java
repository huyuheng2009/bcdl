/**
 * 项目: bcdl
 * 包名：com.yogapay.bcdl.op
 * 文件名: MinShengCombination
 * 创建时间: 2014/8/11 10:57
 * 支付界科技有限公司版权所有，保留所有权利
 */
package com.yogapay.bcdl.op;

import com.yogapay.bcdl.utils.Commons;
import com.yogapay.bcdl.utils.ConstantsLoader;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Todo:民生银行拼装
 * @Author: Zhanggc
 */
public class MinShengCombination {
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //装配发文
    public static String getXml(String type,String header,String body){
        String sb = null;
        if(StringUtils.isNotBlank(type)&&StringUtils.isNotBlank(header)&&StringUtils.isNotBlank(body)){
            sb = new String("<?xml version=\"1.0\" encoding=\"GB2312\"?><CMBC header=\"100\" version=\"100\" security=\"none\" lang=\"chs\" trnCode=\""+type+"\">${requestHeader}${xDataBody}</CMBC>");
            sb = sb.replace("${requestHeader}",header).replace("${xDataBody}",body);
        }else{
            throw new IllegalArgumentException("报文头header 或 报文体body 不能为空！");
        }
        return sb;
    }


    //请求头信息
    public  static String getHeader(Map<String,Object> params){
        if (null == params) {
            params = new HashMap<String, Object>();
            params.put("clientId", ConstantsLoader.getProperty("minsheng_clientId"));
            params.put("userId", ConstantsLoader.getProperty("minsheng_userId"));
            params.put("userPswd", ConstantsLoader.getProperty("minsheng_userPswd"));
            params.put("language", "chs");
            params.put("appId", "nsbdes");
            params.put("appVer", "201");
        }
        StringBuffer sb = sb = new StringBuffer("<requestHeader>")
                .append("<dtClient>").append(sf.format(new Date())).append("</dtClient>")
                .append("<clientId>").append(params.get("clientId")).append("</clientId>")
                .append("<userId>").append(params.get("userId")).append("</userId>")
                .append("<userPswd>").append(params.get("userPswd")).append("</userPswd>")
                .append("<language>").append(params.get("language")).append("</language>")
                .append("<appId>").append(params.get("appId")).append("</appId>")
                .append("<appVer>").append(params.get("appVer")).append("</appVer>")
                .append("</requestHeader>");
        return sb.toString();
    }


    /**
     * @Todo 单笔打款: 行内公转私
     * @param params
     * @return
     */
    public static String getTransferOneInnerBankXml(Map<String, Object> params){
        if(null!=params) {
            Commons.clearNull(params);
            StringBuffer body = new StringBuffer("<xDataBody>")
                    .append("<trnId>").append(params.get("trnId")==null?"":params.get("trnId")).append("</trnId>")
                    .append("<insId>").append(params.get("insId")==null?"":params.get("insId")).append("</insId>")
                    .append("<acntNo>").append(params.get("acntNo")==null?"":params.get("acntNo")).append("</acntNo>")
                    .append("<acntToNo>").append(params.get("acntToNo")==null?"":params.get("acntToNo")).append("</acntToNo>")
                    .append("<acntToName>").append(params.get("acntToName")==null?"":params.get("acntToName")).append("</acntToName>")
                    .append("<payeeAcctType>").append(params.get("payeeAcctType")==null?"":params.get("payeeAcctType")).append("</payeeAcctType>")
                    .append("<amount>").append(params.get("amount")==null?"":params.get("amount")).append("</amount>")
                    .append("<explain>").append(params.get("explain")==null?"":params.get("explain")).append("</explain>")
                    .append("</xDataBody>");
            return getXml("CostReimb", getHeader(null), body.toString());
        }
        return "";
    }

    /**
     * @Todo 单笔打款: 跨行公转私
     * @param params
     * @return
     */
    public static String  getTransferOneInterBankXml(Map<String, Object> params){
        if(null!=params) {
            Commons.clearNull(params);
            StringBuffer body = new StringBuffer("<xDataBody>")
                    .append("<trnId>").append(params.get("trnId")==null?"":params.get("trnId")).append("</trnId>")
                    .append("<cltcookie></cltcookie>")
                    .append("<insId>").append(params.get("insId")==null?"":params.get("insId")).append("</insId>")
                    .append("<acntNo>").append(params.get("acntNo")==null?"":params.get("acntNo")).append("</acntNo>")
                    .append("<acntName>").append(params.get("acntName")==null?"":params.get("acntName")).append("</acntName>")
                    .append("<acntToNo>").append(params.get("acntToNo")==null?"":params.get("acntToNo")).append("</acntToNo>")
                    .append("<acntToName>").append(params.get("acntToName")==null?"":params.get("acntToName")).append("</acntToName>")
                    .append("<externBank>").append(params.get("externBank")==null?"":params.get("externBank")).append("</externBank>")
                    .append("<localFlag>").append(params.get("localFlag")==null?"":params.get("localFlag")).append("</localFlag>")
                    .append("<rcvCustType>2</rcvCustType>")
                    .append("<bankCode>").append(params.get("bankCode")==null?"":params.get("bankCode")).append("</bankCode>")
                    .append("<bankName>").append(params.get("bankName")==null?"":params.get("bankName")).append("</bankName>")
                    .append("<bankAddr>").append(params.get("bankAddr")==null?"":params.get("bankAddr")).append("</bankAddr>")
                    .append("<areaCode>").append(params.get("areaCode")==null?"":params.get("areaCode")).append("</areaCode>")
                    .append("<amount>").append(params.get("amount")==null?"":params.get("amount")).append("</amount>")
                    .append("<explain>平台付款</explain>")
                    .append("<actDate>").append(params.get("actDate")==null?"":params.get("actDate")).append("</actDate>")
                    .append("</xDataBody>");
            return getXml("Xfer", getHeader(null), body.toString());
        }
        return "";
    }

    //单笔查询
    public static String getTransferOneQueryXml(Map<String, Object> params){
        if(null!=params) {
            Commons.clearNull(params);
            StringBuffer body = new StringBuffer("<xDataBody>")
                    .append("<trnId>").append(params.get("trnId")).append("</trnId>")
                    .append("<insId>").append(params.get("insId")).append("</insId>")
                    .append("<svrId>").append(params.get("svrId")).append("</svrId>")
                    .append("</xDataBody>");
            return getXml("qryXfer", getHeader(null), body.toString());
        }
        return "";
    }

    /**
     * @Todo 批量打款：跨行公转私
     * @param params
     * @return
     */
    public static String getTransferBatchInterBankXml(Map<String, Object> params){
        if(null!=params) {
            //Commons.clearNull(params);
            StringBuffer body = new StringBuffer("<xDataBody>")
                    .append("<trnId>").append(params.get("trnId")==null?"":params.get("trnId")).append("</trnId>")
                    .append("<cltcookie></cltcookie>")
                    .append("<insId>").append(params.get("insId")==null?"":params.get("insId")).append("</insId>")
                    .append("<payerAcct>").append(params.get("payerAcct")==null?"":params.get("payerAcct")).append("</payerAcct>")
                    .append("<payType>").append(params.get("payType")==null?"":params.get("payType")).append("</payType>")
                    .append("<totalRow>").append(params.get("totalRow")==null?"":params.get("totalRow")).append("</totalRow>")
                    .append("<totalAmt>").append(params.get("totalAmt")==null?"":params.get("totalAmt")).append("</totalAmt>")
                    .append("<fileContent>").append(params.get("fileContent")==null?"":params.get("fileContent")).append("</fileContent>")
                    .append("</xDataBody>");
            return getXml("batchXfer", getHeader(null), body.toString());
        }
        return "";
    }

    /**
     * @Todo 批量查询：跨行公转私
     * @param params
     * @return
     */
    public static String getTransferBatchQueryInterBankXml(Map<String, Object> params){
        if(null!=params) {
            //Commons.clearNull(params);
            StringBuffer body = new StringBuffer("<xDataBody>")
                    .append("<trnId>").append(params.get("trnId")==null?"":params.get("trnId")).append("</trnId>")
                    .append("<insId>").append(params.get("insId")==null?"":params.get("insId")).append("</insId>")
                    .append("<payType>").append(params.get("payType")==null?"":params.get("payType")).append("</payType>")
                    .append("</xDataBody>");
            return getXml("qryBatchXfer", getHeader(null), body.toString());
        }
        return "";
    }

    /**
     * @Todo 批量打款：行内公转私
     * @param params
     * @return
     */
    public static String getTransferBatchInnerBankXml(Map<String, Object> params){
        if(null!=params) {
            //Commons.clearNull(params);
            StringBuffer body = new StringBuffer("<xDataBody>")
                    .append("<trnId>").append(params.get("trnId")==null?"":params.get("trnId")).append("</trnId>")
                    .append("<cltcookie></cltcookie>")
                    .append("<insId>").append(params.get("insId")==null?"":params.get("insId")).append("</insId>")
                    .append("<PayerAcNo>").append(params.get("payerAcNo")==null?"":params.get("payerAcNo")).append("</PayerAcNo>")
                    .append("<payType>").append(params.get("payType")==null?"":params.get("payType")).append("</payType>")
                    .append("<totalRow>").append(params.get("totalRow")==null?"":params.get("totalRow")).append("</totalRow>")
                    .append("<totalAmt>").append(params.get("totalAmt")==null?"":params.get("totalAmt")).append("</totalAmt>")
                    .append("<Usage>").append(params.get("usage")==null?"":params.get("usage")).append("</Usage>")
                    .append("<fileContent>").append(params.get("fileContent")==null?"":params.get("fileContent")).append("</fileContent>")
                    .append("</xDataBody>");
            return getXml("batchCostReimb", getHeader(null), body.toString());
        }
        return "";
    }

    /**
     * @Todo 批量查询：行内公转私
     * @param params
     * @return
     */
    public static String getTransferBatchQueryInnerBankXml(Map<String, Object> params){
        if(null!=params) {
            Commons.clearNull(params);
            StringBuffer body = new StringBuffer("<xDataBody>")
                    .append("<trnId>").append(params.get("trnId")==null?"":params.get("trnId")).append("</trnId>")
                    .append("<insId>").append(params.get("insId")==null?"":params.get("insId")).append("</insId>")
                    .append("<payType>").append(params.get("payType")==null?"":params.get("payType")).append("</payType>")
                    .append("</xDataBody>");
            return getXml("qryBatchCostReimb", getHeader(null), body.toString());
        }
        return "";
    }

    //查询余额
    public static String getAccountQueryXml(Map<String, Object> params){
        if(null!=params){
            Commons.clearNull(params);
            StringBuffer body = new StringBuffer("<xDataBody>")
                    .append("<trnId>").append(params.get("trnId")==null?"":params.get("trnId")).append("</trnId>")
                    .append("<acntList><acntNo>").append(params.get("acntNo")==null?"":params.get("acntNo")).append("</acntNo></acntList>")
                    .append("</xDataBody>");
            return getXml("qryBal",getHeader(null),body.toString());
        }
       return "";
    }

    //明细查询交易
    public static String getQueryDetailXml(Map<String, Object> params){
        if(null!=params){
            //Commons.clearNull(params);
            StringBuffer body = new StringBuffer("<xDataBody>")
                    .append("<trnId>").append(params.get("trnId")==null?"":params.get("trnId")).append("</trnId>")
                    .append("<cltcookie>").append(params.get("cltcookie")==null?"":params.get("cltcookie")).append("</cltcookie>")
                    .append("<acntNo>").append(params.get("acntNo")==null?"":params.get("acntNo")).append("</acntNo>")
                    .append("<dateFrom>").append(params.get("dateFrom")==null?"":params.get("dateFrom")).append("</dateFrom>")
                    .append("<dateTo>").append(params.get("dateTo")==null?"":params.get("dateTo")).append("</dateTo>")
                    .append("<startNo>").append(params.get("startNo")==null?"":params.get("startNo")).append("</startNo>")
                    .append("<endNo>").append(params.get("endNo")==null?"":params.get("endNo")).append("</endNo>")
                    .append("<typeCode>").append(params.get("typeCode")==null?"":params.get("typeCode")).append("</typeCode>")
                    .append("</xDataBody>");
            return getXml("qryDtl",getHeader(null),body.toString());
        }
        return "";
    }
}
