/**
 * 项目: bcdl
 * 包名：com.yogapay.bcdl.service
 * 文件名: AccountService
 * 创建时间: 2014/10/9 14:44
 * 支付界科技有限公司版权所有，保留所有权利
 */
package com.yogapay.bcdl.service;

import com.yogapay.bcdl.domain.MerchantAccount;
import com.yogapay.bcdl.domain.MerchantAccountInfo;
import com.yogapay.bcdl.utils.Dao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Todo: 账户管理
 * @Author: Zhanggc
 */
@Service
public class AccountService {
    @Resource
    Dao dao;

    public MerchantAccountInfo findAccountInfoFirst(Map<String,String> params) throws SQLException {
        StringBuffer sqlWhere = new StringBuffer(" where 1=1 and a.account_id = b.id ");
        ArrayList<Object> paramList = new ArrayList<Object>();
        if(null!=params){
            if(StringUtils.isNotBlank(params.get("merchantNo"))){
                paramList.add(params.get("merchantNo"));
                sqlWhere.append(" and b.merchant_no=? ");
            }
            if(StringUtils.isNotBlank(params.get("sthirdVoucher"))){
                paramList.add(params.get("sthirdVoucher"));
                sqlWhere.append(" and sthird_voucher=? ");
            }
        }
        String sql = "select a.* from merchant_account_info a,merchant_account b "+sqlWhere;
        return dao.findFirst(MerchantAccountInfo.class,sql,paramList.toArray());
    }

    public boolean updateAccount(Map<String,String> ifParams,Map<String,String> params) throws SQLException {
        if(null==params&&params.size()==0) return false;
        StringBuffer sql = new StringBuffer("update merchant_account set ");
        StringBuffer sqlWhere = new StringBuffer(" where 1=1 ");
        ArrayList<Object> paramList = new ArrayList<Object>();
        if(StringUtils.isNotBlank(params.get("inAmount"))){
            sql.append(" amount=amount+?,avaliable_amount=avaliable_amount+?,");
            paramList.add(params.get("inAmount"));
            paramList.add(params.get("inAmount"));
        }
        if(StringUtils.isNotBlank(params.get("outAmount"))){
            sql.append(" amount=amount-?,avaliable_amount=avaliable_amount-?,");
            paramList.add(params.get("outAmount"));
            paramList.add(params.get("outAmount"));
        }
        if(StringUtils.isNotBlank(ifParams.get("merchantNo"))){
            sqlWhere.append(" and merchant_no=? ");
            paramList.add(ifParams.get("merchantNo"));
        }
        int row = dao.update(sql.substring(0,sql.length()-1)+sqlWhere,paramList);
        if(0==row) return false;
        return true;
    }

    public boolean createAccountInfo(MerchantAccountInfo accountInfo) throws SQLException {
        if(null==accountInfo){return false;}
        String sql = "insert into merchant_account_info(account_id,create_time,amount,operate_type) values(?,now(),?,?)";
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(accountInfo.getAccountId());
        params.add(accountInfo.getAmount());
        params.add(accountInfo.getOperateType());
        int row = dao.update(sql,params.toArray());
        if(row==0) return false;
        return true;
    }

    public MerchantAccount findAccountFirst(Map<String,String> params) throws SQLException {
        StringBuffer sqlWhere = new StringBuffer(" where 1=1 ");
        ArrayList<Object> paramList = new ArrayList<Object>();
        if(null!=params){
            if(StringUtils.isNotBlank(params.get("merchantNo"))){
                paramList.add(params.get("merchantNo"));
                sqlWhere.append(" and merchant_no=? ");
            }
        }
        String sql = "select * from merchant_account "+sqlWhere;
        return dao.findFirst(MerchantAccount.class,sql,paramList.toArray());
    }
}
