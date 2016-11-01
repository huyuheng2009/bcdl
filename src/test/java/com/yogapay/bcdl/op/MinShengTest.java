/**
 * 项目: bcdl
 * 包名：com.yogapay.bcdl.op
 * 文件名: MinshengTest
 * 创建时间: 2014/7/31 10:46
 * 支付界科技有限公司版权所有，保留所有权利
 */
package com.yogapay.bcdl.op;

import com.yogapay.bcdl.utils.Commons;
import com.yogapay.bcdl.utils.junit.AbstractTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Todo:
 * @Author: Zhanggc
 */
public class MinShengTest extends AbstractTest {
    @Resource
    private MinSheng minSheng;


    @Test
    //单笔行内转账
    public void transferOneInnerBank() throws SQLException{
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("trnId","20140710105100000011");
        params.put("insId","20140710105100000011");
        params.put("acntNo","691579798");
        params.put("acntToNo","6226180600003359");//罗湖支行
        params.put("acntToName","库汉桥");
        params.put("payeeAcctType","1");
        params.put("amount","1");
        params.put("explain","311");
        String response =  minSheng.transferOneInnerBank(params);
    }

    @Test
    //单笔行外
    public void transferOneInterBank() throws SQLException{
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("trnId","20140710105100000010");
        params.put("insId","20140710105100000010");
        params.put("acntNo","691579798");
        params.put("acntName","深圳支付界科技有限公司");
        params.put("acntToNo","6228480128130991275");
        params.put("acntToName","张国才");
        params.put("externBank","1");
        params.put("localFlag","2");
        params.put("bankCode","103584001124");
        params.put("bankName","中国农业银行");
        params.put("payeeAcctType","1");
        params.put("amount","0.5");
        params.put("explain","311");
        String response =  minSheng.transferOneInterBank(params);
    }

    @Test
    //单笔转账查询
    public void transferOneQuery(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("trnId","20140710105100000011");
        params.put("insId","20140710105100000011");
        params.put("svrId","");
        minSheng.transferOneQuery(params);
    }

    @Test
    //批量跨行转账
    public void transferBatchInterBank() throws SQLException{
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("trnId","20140710105100000016");//记录结尾12、14、15、16
        params.put("insId","20140710105100000016");
        params.put("payerAcct","691579798");
        params.put("payType","0");
        params.put("totalRow","2");
        params.put("totalAmt","2.00");
        params.put("fileContent","6|00000005|||1|6228480128130991275|张国才|103584001124|中国农业银行股份有限公司深圳东门支行|测试|0||||1.00" +
                "^6|00000006|||1|6227003172490227292|张国才|105595010258|中国建设银行股份有限公司惠州河南岸支行|测试|0||||1.00");

        String response =  minSheng.transferBatchInterBank(params);
    }

    @Test
    //批量跨行转账查询
    public void transferBatchQueryInterBank(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("trnId","20140710105100000016");
        params.put("insId","20140710105100000016");
        params.put("payType","0");

        String response =  minSheng.transferBatchQueryInterBank(params);
    }

    @Test
    //批量行内转账
    public void transferBatchInnerBank()throws SQLException{
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("trnId","20140710105100000013");
        params.put("insId","20140710105100000013");
        params.put("PayerAcNo","691579798");
        params.put("payType","2");
        params.put("totalRow","1");
        params.put("totalAmt","1.00");
        params.put("Usage","312");
        params.put("fileContent","6226180600003359|库汉桥||1.00");

        String response =  minSheng.transferBatchInnerBank(params);
    }

    @Test
    //批量行内转账查询
    public void transferBatchQueryInnerBank(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("trnId","20140710105100000013");
        params.put("insId","20140710105100000013");
        params.put("payType","2");

        String response =  minSheng.transferBatchQueryInnerBank(params);
    }

    @Test
    //账户余额
    public void queryAccount(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("trnId","20140710105100000090");
        params.put("acntNo","691579798");
        String response =  minSheng.queryAccount(params);
    }

    @Test
    //账户明细
    public void queryDetail(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("trnId","20140710105100000090");
        params.put("acntNo","691579798");
        params.put("dateFrom","2014-08-26");
        params.put("dateTo","2014-08-26");
        params.put("startNo","1");
        params.put("endNo","22");
        params.put("typeCode","0");
        String response =  minSheng.queryDetail(params);
    }
}
