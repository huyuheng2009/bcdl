/**
 * 项目: bcdl
 * 包名：com.yogapay.bcdl.op
 * 文件名: PingAnTest
 * 创建时间: 2014/7/30 10:17
 * 支付界科技有限公司版权所有，保留所有权利
 */
package com.yogapay.bcdl.op;

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
public class PingAnTest extends AbstractTest{
    @Resource
    private PingAn pingAn;


    public void transferOne(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("bankCode","pingAn");
        params.put("sthirdVoucher","00000000000000000055");
        params.put("ccyCode","RMB");
        params.put("outAcctNo","11007187041901");
        params.put("outAcctName","SHENFA007187041");
        params.put("inAcctBankNode","103584001124");
        params.put("inAcctNo","6228480128130991275");
        params.put("inAcctName","张国才");
        params.put("inAcctBankName","中国农业银行股份有限公司深圳东门支行");
        params.put("tranAmount","1");
        params.put("unionFlag","0");
        params.put("addrFlag","1");
        try{
            pingAn.transferOne(params,true);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void transferOneQuery(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("bankCode","pingAn");
        params.put("origThirdVoucher","00000000000000000635");
        params.put("origFrontLogNo","");
        try{
            pingAn.transferOneQuery(params,true);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
