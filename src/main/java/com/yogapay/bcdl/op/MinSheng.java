/**
 * 项目: bcdl
 * 包名：com.yogapay.bcdl.op
 * 文件名: MinSheng
 * 创建时间: 2014/7/29 15:22
 * 支付界科技有限公司版权所有，保留所有权利
 */
package com.yogapay.bcdl.op;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yogapay.bcdl.domain.MerchantAccount;
import com.yogapay.bcdl.domain.MerchantAccountInfo;
import com.yogapay.bcdl.domain.TransferRecord;
import com.yogapay.bcdl.domain.TransferRecordInfo;
import com.yogapay.bcdl.service.AccountService;
import com.yogapay.bcdl.service.TransferRecordInfoService;
import com.yogapay.bcdl.service.TransferRecordService;
import com.yogapay.bcdl.utils.Commons;
import com.yogapay.bcdl.utils.ConstantsLoader;
import com.yogapay.bcdl.utils.StringUtil;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Todo: 民生银行
 * @Author: Zhanggc
 */
@Service
public class MinSheng {
    private final Logger log = LoggerFactory.getLogger(MinSheng.class);

    @Resource
    private TransferRecordService transferRecordService;
    @Resource
    private TransferRecordInfoService transferRecordInfoService;
    @Resource
    private AccountService accountService;

    /**
     * @param params
     * @return
     * @throws SQLException
     * @Todo 单笔转账: 行内转账
     */
    public String transferOneInnerBank(Map<String, Object> params) throws SQLException {
        TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher((String) params.get("trnId"));
        List<TransferRecordInfo> transferRecordInfoList = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
        for(TransferRecordInfo recordInfo:transferRecordInfoList){
            recordInfo.setInacctStatus("1");//防止重复打款
            transferRecordInfoService.update(recordInfo);
        }

        String content = MinShengCombination.getTransferOneInnerBankXml(params);
        log.info("Method transferOneInnerBank -> request content:{}",content);
        String response = Commons.httpclientSend(ConstantsLoader.getProperty("minsheng_url"), content, "application/x-NS-BDES; charset=GBK");
        return response;
    }

    /**
     * @param result
     * @return
     * @Todo 单笔转账Handle
     */
    public String handleTransferOne(String result) throws SQLException {
        String response = "";
        if (StringUtils.isNotBlank(result)) {
            Map<String, String> map = new HashMap<String, String>();
            String code = getResponseHeaderCode(result);
            log.info("Method handleTransferOne 报文头 响应码：{}", code);
            if ("0".equals(code) || "WEC02".equals(code) || "Error".equals(code) || "".equals(code)) {
                String trnId;   //唯一标志
                String insId;
                String[] stringArray;
                {
                    stringArray = result.split("<trnId>");
                    stringArray = stringArray[1].split("</trnId>");
                    trnId = stringArray[0].trim();
                }
                {
                    stringArray = result.split("<insId>");
                    stringArray = stringArray[1].split("</insId>");
                    insId = stringArray[0].trim();
                }
                TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher(trnId);
                List<TransferRecordInfo> list = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
                for (TransferRecordInfo transferRecordInfo : list) {
                    transferRecordInfo.setInacctStatus("1");
                    transferRecordInfoService.update(transferRecordInfo);
                }
                map.put("status", "success");
                response = JSON.toJSONString(map);
            } else if (StringUtils.isNotBlank(code)) {
                log.info("重复转账危险出现，请复查！ 响应码：{}", code);
                String trnId;   //唯一标志
                String insId;
                String[] stringArray;
                {
                    stringArray = result.split("<trnId>");
                    stringArray = stringArray[1].split("</trnId>");
                    trnId = stringArray[0].trim();
                }
                {
                    stringArray = result.split("<insId>");
                    stringArray = stringArray[1].split("</insId>");
                    insId = stringArray[0].trim();
                }
                TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher(trnId);
                List<TransferRecordInfo> list = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
                for (TransferRecordInfo transferRecordInfo : list) {
                    transferRecordInfo.setInacctStatus("1");//default 3 but for security alter 1
                    transferRecordInfoService.update(transferRecordInfo);
                }
                map.put("status", "fail");
                response = JSON.toJSONString(map);
            } else {
                map.put("status", "fail");
                response = JSON.toJSONString(map);
            }
        }
        return response;
    }

    /**
     * @param result
     * @return
     * @throws SQLException
     * @Todo 单笔转账查询Handle
     */
    public String handleTransferOneQuery(String result) throws SQLException {
        String response = "";
        if (StringUtils.isNotBlank(result)) {
            Map<String, String> map = new HashMap<String, String>();
            String code = getResponseHeaderCode(result);
            log.info("Method handleTransferOne 报文头 响应码：{}", code);
            if ("0".equals(code)) {
                /**
                 * @Todo statusCode
                 * @Tips
                 * 0：原交易成功
                 * 2:原交易失败
                 * 3:对账因为网络原因失败
                 * 4:原交易处理中
                 */
                String statusCode;    //响应码
                String trnId;   //唯一标志
                String insId;
                String[] stringArray;
                {
                    stringArray = result.split("<statusCode>");
                    stringArray = stringArray[1].split("</statusCode>");
                    statusCode = stringArray[0].trim();
                }
                {
                    stringArray = result.split("<trnId>");
                    stringArray = stringArray[1].split("</trnId>");
                    trnId = stringArray[0].trim();
                }
                {
                    stringArray = result.split("<insId>");
                    stringArray = stringArray[1].split("</insId>");
                    insId = stringArray[0].trim();
                }
                TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher(trnId);
                List<TransferRecordInfo> list = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
                for (TransferRecordInfo transferRecordInfo : list) {
                    transferRecordInfo.setStatus(2);
                    if ("0".equals(statusCode)) {
                        /**
                         * @Todo 账户及积分管理
                         */
                        {
                            //此前状态不能为2 即成功
                            if(!("2".equals(transferRecordInfo.getInacctStatus()))){
                                Map<String,String> params = new HashMap<String, String>();
                                MerchantAccount account = null;
                                MerchantAccountInfo accountInfo = null;
                                params.put("merchantNo",transferRecordInfo.getMerchantNo());
                                account = accountService.findAccountFirst(params);
                                params.put("sthird_voucher",transferRecordInfo.getSthirdVoucher());
                                accountInfo = accountService.findAccountInfoFirst(params);
                                Assert.assertNull("accountInfo 一定要为空！",accountInfo);
                                if(null==accountInfo){
                                    accountInfo = new MerchantAccountInfo();
                                    accountInfo.setAccountId(account.getId());
                                    accountInfo.setOperateType("out");
                                    accountInfo.setAmount(new BigDecimal(transferRecordInfo.getTranAmount().toString()));
                                    boolean isSuccess = accountService.createAccountInfo(accountInfo);
                                    Assert.assertTrue("isSuccess 一定要为True！",isSuccess);
                                    //成功创建明细才更新账户
                                    if(isSuccess) {
                                        Map<String, String> ifParams = new HashMap<String, String>();
                                        Map<String, String> paramList = new HashMap<String, String>();
                                        ifParams.put("merchantNo", transferRecordInfo.getMerchantNo());
                                        paramList.put("outAmount", transferRecordInfo.getTranAmount().toString());
                                        isSuccess = accountService.updateAccount(ifParams, paramList);
                                        Assert.assertTrue("出款，账户更新失败",isSuccess);
                                    }
                                }
                            }
                        }
                        transferRecordInfo.setInacctStatus("2");
                    } else if ("2".equals(statusCode)) {
                        transferRecordInfo.setInacctStatus("3");
                    } else {
                        transferRecordInfo.setInacctStatus("1");
                    }
                    transferRecordInfoService.update(transferRecordInfo);
                }
                map.put("status", "success");
                map.put("trnId", insId);
                map.put("insId", insId);
                map.put("statusCode", statusCode);
                response = JSON.toJSONString(map);
            } else {
                map.put("status", "fail");
                response = JSON.toJSONString(map);
            }
        }
        return response;
    }

    /**
     * @Security 此接口存在缺陷：当同一账户转账多次相同金额时，
     * 只要有一次转账成功，其他转账都会因此变为转账成功(即使转账失敗)；
     * @param result
     * @return
     * @throws SQLException
     * @Todo 批量转账查询Handle :行内
     */
    public String handleTransferBatchQueryInnerBank(String result) throws SQLException {
        String response = "";
        if (StringUtils.isNotBlank(result)) {
            Map<String, String> map = new HashMap<String, String>();
            String code = getResponseHeaderCode(result);
            log.info("Method handleTransferOne 报文头 响应码：{}", code);
            if ("0".equals(code)) {
                /**
                 * @Todo statusCode
                 * @Tips
                 * 0：原交易成功
                 * 2:原交易失败
                 * 3:对账因为网络原因失败
                 * 4:原交易处理中
                 */
                String statusCode;    //响应码
                String trnId;   //唯一标志
                String insId;
                String[] stringArray;
                List<Map<String, String>> recordInfoList = new ArrayList<Map<String, String>>();
                {
                    stringArray = result.split("<statusCode>");
                    stringArray = stringArray[1].split("</statusCode>");
                    statusCode = stringArray[0].trim();
                }
                {
                    stringArray = result.split("<trnId>");
                    stringArray = stringArray[1].split("</trnId>");
                    trnId = stringArray[0].trim();
                }
                {
                    stringArray = result.split("<insId>");
                    stringArray = stringArray[1].split("</insId>");
                    insId = stringArray[0].trim();
                }
                Map<String, String> attrs;
                {
                    stringArray = result.split("<fileContent>");
                    stringArray = stringArray[1].split("</fileContent>");
                    String fileContent = stringArray[0].trim();
                    String[] infoContentList = fileContent.split("\\^");
                    for (String info : infoContentList) {
                        attrs = new HashMap<String, String>();
                        String[] items = info.split("\\|");
                        attrs.put("inacctNo", items[0]);
                        attrs.put("inacctName", items[1]);
                        attrs.put("tranAmount", items[3]);
                        /**
                         * @Todo code 状态码
                         * 10：转账成功
                         * 20：转账失败
                         */
                        attrs.put("code", items[4]);
                        recordInfoList.add(attrs);

                        log.info("Method handleTransferBigBatchQueryInnerBank 批量转账 卡号：{},户名：{},状态码：{}",
                                new Object[]{attrs.get("inacctNo"), attrs.get("inacctName"), attrs.get("code")});
                    }
                }
                TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher(trnId);
                List<TransferRecordInfo> transferRecordInfoList = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
                for (TransferRecordInfo recordInfo : transferRecordInfoList) {
                    for(Map<String,String> item:recordInfoList){
                        if (null != item && recordInfo.getInacctNo().equals(item.get("inacctNo"))&& recordInfo.getInacctName().equals(item.get("inacctName"))
                                && new BigDecimal(recordInfo.getTranAmount().toString()).setScale(2, RoundingMode.HALF_UP).toString().equals(item.get("tranAmount"))) {
                            recordInfo.setStatus(2);
                            if ("10".equals(item.get("code"))) {
                                /**
                                 * @Todo 账户及积分管理
                                 */
                                {
                                    //此前状态不能为2 即成功
                                    if(!("2".equals(recordInfo.getInacctStatus()))){
                                        Map<String,String> params = new HashMap<String, String>();
                                        MerchantAccount account = null;
                                        MerchantAccountInfo accountInfo = null;
                                        params.put("merchantNo",recordInfo.getMerchantNo());
                                        account = accountService.findAccountFirst(params);
                                        params.put("sthird_voucher",recordInfo.getSthirdVoucher());
                                        accountInfo = accountService.findAccountInfoFirst(params);
                                        Assert.assertNull("accountInfo 一定要为空！",accountInfo);
                                        if(null==accountInfo){
                                            accountInfo = new MerchantAccountInfo();
                                            accountInfo.setAccountId(account.getId());
                                            accountInfo.setOperateType("out");
                                            accountInfo.setAmount(new BigDecimal(recordInfo.getTranAmount().toString()));
                                            boolean isSuccess = accountService.createAccountInfo(accountInfo);
                                            Assert.assertTrue("isSuccess 一定要为True！",isSuccess);
                                            //成功创建明细才更新账户
                                            if(isSuccess) {
                                                Map<String, String> ifParams = new HashMap<String, String>();
                                                Map<String, String> paramList = new HashMap<String, String>();
                                                ifParams.put("merchantNo", recordInfo.getMerchantNo());
                                                paramList.put("outAmount", recordInfo.getTranAmount().toString());
                                                isSuccess = accountService.updateAccount(ifParams, paramList);
                                                Assert.assertTrue("出款，账户更新失败",isSuccess);
                                            }
                                        }
                                    }
                                }
                                recordInfo.setInacctStatus(2 + "");
                            } else if ("20".equals(item.get("code"))) {
                                recordInfo.setInacctStatus(3 + "");
                            } else {
                                recordInfo.setInacctStatus(1 + "");
                            }
                            transferRecordInfoService.update(recordInfo);
                        }
                    }
                }

                map.put("status", "success");
                map.put("info", JSONArray.toJSONString(recordInfoList));
                response = JSONArray.toJSONString(map);
            } else {
                map = new HashMap<String, String>();
                map.put("status", "fail");
                response = JSON.toJSONString(map);
            }
        }
        return response;
    }

    /**
     * @param result
     * @return
     * @throws SQLException
     * @Todo 批量转账查询Handle: 跨行
     */
    public String handleTransferBatchQueryInterBank(String result) throws SQLException {
        String response = "";
        if (StringUtils.isNotBlank(result)) {
            Map<String, String> map = new HashMap<String, String>();
            String code = getResponseHeaderCode(result);
            log.info("Method handleTransferOne 报文头 响应码：{}", code);
            if ("0".equals(code)) {
                /**
                 * @Todo statusCode
                 * @Tips
                 * 0：原交易成功
                 * 2:原交易失败
                 * 3:对账因为网络原因失败
                 * 4:原交易处理中
                 */
                String statusCode;    //响应码
                String trnId;   //唯一标志
                String insId;
                String[] stringArray;
                Map<String, Map<String, String>> recordInfoMap = new HashMap<String, Map<String, String>>();
                {
                    stringArray = result.split("<statusCode>");
                    stringArray = stringArray[1].split("</statusCode>");
                    statusCode = stringArray[0].trim();
                }
                {
                    stringArray = result.split("<trnId>");
                    stringArray = stringArray[1].split("</trnId>");
                    trnId = stringArray[0].trim();
                }
                {
                    stringArray = result.split("<insId>");
                    stringArray = stringArray[1].split("</insId>");
                    insId = stringArray[0].trim();
                }
                Map<String, String> attrs;
                {
                    stringArray = result.split("<fileContent>");
                    stringArray = stringArray[1].split("</fileContent>");
                    String fileContent = stringArray[0].trim();
                    String[] infoContentList = fileContent.split("\\^");
                    for (String info : infoContentList) {
                        attrs = new HashMap<String, String>();
                        String[] items = info.split("\\|");
                        attrs.put("suffixSthirdVoucher", items[0]);
                        attrs.put("inacctNo", items[4]);
                        attrs.put("inacctName", items[5]);
                        attrs.put("tranAmount", items[7]);
                        /**
                         * @Todo code 状态码
                         * 0: 转账处理中
                         * 1：转账成功
                         * 2：转账失败
                         */
                        attrs.put("code", items[8]);
                        log.info("Method handleTransferBigBatchQueryInterBank 批量转账 卡号：{},户名：{},状态码：{}",
                                new Object[]{attrs.get("inacctNo"), attrs.get("inacctName"), attrs.get("code")});
                        recordInfoMap.put(attrs.get("suffixSthirdVoucher"), attrs);
                    }
                }
                TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher(trnId);
                List<TransferRecordInfo> transferRecordInfoList = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
                String sthirdVoucher;
                for (TransferRecordInfo recordInfo : transferRecordInfoList) {
                    sthirdVoucher = recordInfo.getSthirdVoucher();
                    attrs = recordInfoMap.get(sthirdVoucher.substring(sthirdVoucher.length() - 8, sthirdVoucher.length()));
                    if (null != attrs && recordInfo.getInacctNo().equals(attrs.get("inacctNo")) && recordInfo.getInacctName().equals(attrs.get("inacctName"))
                            && new BigDecimal(recordInfo.getTranAmount().toString()).setScale(2, RoundingMode.HALF_UP).toString().equals(attrs.get("tranAmount"))) {
                        recordInfo.setStatus(2);
                        if ("1".equals(attrs.get("code"))) {
                            /**
                             * @Todo 账户及积分管理
                             */
                            {
                                //此前状态不能为2 即成功
                                if(!("2".equals(recordInfo.getInacctStatus()))){
                                    Map<String,String> params = new HashMap<String, String>();
                                    MerchantAccount account = null;
                                    MerchantAccountInfo accountInfo = null;
                                    params.put("merchantNo",recordInfo.getMerchantNo());
                                    account = accountService.findAccountFirst(params);
                                    params.put("sthird_voucher",recordInfo.getSthirdVoucher());
                                    accountInfo = accountService.findAccountInfoFirst(params);
                                    Assert.assertNull("accountInfo 一定要为空！",accountInfo);
                                    if(null==accountInfo){
                                        accountInfo = new MerchantAccountInfo();
                                        accountInfo.setAccountId(account.getId());
                                        accountInfo.setOperateType("out");
                                        accountInfo.setAmount(new BigDecimal(recordInfo.getTranAmount().toString()));
                                        boolean isSuccess = accountService.createAccountInfo(accountInfo);
                                        Assert.assertTrue("isSuccess 一定要为True！",isSuccess);
                                        //成功创建明细才更新账户
                                        if(isSuccess) {
                                            Map<String, String> ifParams = new HashMap<String, String>();
                                            Map<String, String> paramList = new HashMap<String, String>();
                                            ifParams.put("merchantNo", recordInfo.getMerchantNo());
                                            paramList.put("outAmount", recordInfo.getTranAmount().toString());
                                            isSuccess = accountService.updateAccount(ifParams, paramList);
                                            Assert.assertTrue("出款，账户更新失败",isSuccess);
                                        }
                                    }
                                }
                            }
                            recordInfo.setInacctStatus(2 + "");
                        } else if ("2".equals(attrs.get("code"))) {
                            recordInfo.setInacctStatus(3 + "");
                        } else {
                            recordInfo.setInacctStatus(1 + "");
                        }
                        transferRecordInfoService.update(recordInfo);
                    }
                }
                map.put("status", "success");
                map.put("info", JSONArray.toJSONString(recordInfoMap));
                response = JSONArray.toJSONString(map);
            } else {
                map = new HashMap<String, String>();
                map.put("status", "fail");
                response = JSON.toJSONString(map);
            }
        }
        return response;
    }

    /**
     * @param params
     * @return
     * @throws SQLException
     * @Todo 单笔转账: 跨行转账
     */
    public String transferOneInterBank(Map<String, Object> params) throws SQLException{
        TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher((String) params.get("trnId"));
        List<TransferRecordInfo> transferRecordInfoList = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
        for(TransferRecordInfo recordInfo:transferRecordInfoList){
            recordInfo.setInacctStatus("1");//防止重复打款
            transferRecordInfoService.update(recordInfo);
        }

        String content = MinShengCombination.getTransferOneInterBankXml(params);
        log.info("Method transferOneInterBank -> request content:{}",content);
        String response = Commons.httpclientSend(ConstantsLoader.getProperty("minsheng_url"), content, "application/x-NS-BDES; charset=GBK");
        return response;
    }

    /**
     * @param params
     * @return
     * @Todo 批量转账：跨行转账
     */
    public String transferBatchInterBank(Map<String, Object> params) throws SQLException{
        TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher((String)params.get("trnId"));
        List<TransferRecordInfo> transferRecordInfoList = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
        for(TransferRecordInfo recordInfo:transferRecordInfoList){
            recordInfo.setInacctStatus("1");//防止重复打款
            transferRecordInfoService.update(recordInfo);
        }

        String content = MinShengCombination.getTransferBatchInterBankXml(params);
        log.info("Method transferBatchInterBank -> request content:{}",content);
        String response = Commons.httpclientSend(ConstantsLoader.getProperty("minsheng_url"), content, "application/x-NS-BDES; charset=GBK");
        return response;
    }


    /**
     * @param params
     * @return
     * @Todo 批量转账：行内转账
     */
    public String transferBatchInnerBank(Map<String, Object> params) throws SQLException{
        TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher((String)params.get("trnId"));
        List<TransferRecordInfo> transferRecordInfoList = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
        for(TransferRecordInfo recordInfo:transferRecordInfoList){
            recordInfo.setInacctStatus("1");//防止重复打款
            transferRecordInfoService.update(recordInfo);
        }

        String content = MinShengCombination.getTransferBatchInnerBankXml(params);
        log.info("Method transferBatchInnerBank -> request content:{}",content);
        String response = Commons.httpclientSend(ConstantsLoader.getProperty("minsheng_url"), content, "application/x-NS-BDES; charset=GBK");
        return response;
    }

    /**
     * @param result
     * @return
     * @throws SQLException
     * @Todo 批量转账Handle
     */
    public String handleTranferBatch(String result) throws SQLException {
        String response = "";
        if (StringUtils.isNotBlank(result)) {
            Map<String, String> map = new HashMap<String, String>();
            String code = getResponseHeaderCode(result);
            log.info("Method handleTransferOne 报文头 响应码：{}", code);
            if ("0".equals(code) || "WEC02".equals(code) || "Error".equals(code) || "".equals(code)) {
                String trnId;   //唯一标志
                String insId;
                String[] stringArray;
                {
                    stringArray = result.split("<trnId>");
                    stringArray = stringArray[1].split("</trnId>");
                    trnId = stringArray[0].trim();
                }
                {
                    stringArray = result.split("<insId>");
                    stringArray = stringArray[1].split("</insId>");
                    insId = stringArray[0].trim();
                }
                TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher(trnId);
                List<TransferRecordInfo> list = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
                for (TransferRecordInfo transferRecordInfo : list) {
                    transferRecordInfo.setInacctStatus("1");
                    transferRecordInfo.setFrontLogNo(insId);
                    transferRecordInfoService.update(transferRecordInfo);
                }

                map.put("status", "success");
                response = JSON.toJSONString(map);
            } else if (StringUtils.isNotBlank(code)) {
                log.info("重复转账危险出现，请复查！ 响应码：{}", code);
                String trnId;   //唯一标志
                String insId;
                String[] stringArray;
                {
                    stringArray = result.split("<trnId>");
                    stringArray = stringArray[1].split("</trnId>");
                    trnId = stringArray[0].trim();
                }
                {
                    stringArray = result.split("<insId>");
                    stringArray = stringArray[1].split("</insId>");
                    insId = stringArray[0].trim();
                }
                TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher(trnId);
                List<TransferRecordInfo> list = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
                for (TransferRecordInfo transferRecordInfo : list) {
                    transferRecordInfo.setInacctStatus("1");//default 3 but for security alter 1
                    transferRecordInfo.setFrontLogNo(insId);
                    transferRecordInfoService.update(transferRecordInfo);
                }
                map.put("status", "fail");
                response = JSON.toJSONString(map);
            } else {
                map.put("status", "fail");
                response = JSON.toJSONString(map);
            }
        }
        return response;
    }

    //单笔查询
    public String transferOneQuery(Map<String, Object> params) {
        String content = MinShengCombination.getTransferOneQueryXml(params);
        String response = Commons.httpclientSend(ConstantsLoader.getProperty("minsheng_url"), content, "application/x-NS-BDES; charset=GBK");
        return response;
    }

    /**
     * @param params
     * @return
     * @Todo 批量查询交易：行内查询
     */
    public String transferBatchQueryInnerBank(Map<String, Object> params) {
        String content = MinShengCombination.getTransferBatchQueryInnerBankXml(params);
        String response = Commons.httpclientSend(ConstantsLoader.getProperty("minsheng_url"), content, "application/x-NS-BDES; charset=GBK");
        return response;
    }

    /**
     * @param params
     * @return
     * @Todo 批量查询交易：跨行查询
     */
    public String transferBatchQueryInterBank(Map<String, Object> params) {
        String content = MinShengCombination.getTransferBatchQueryInterBankXml(params);
        String response = Commons.httpclientSend(ConstantsLoader.getProperty("minsheng_url"), content, "application/x-NS-BDES; charset=GBK");
        return response;
    }

    /**
     * @param params
     * @return
     * @Todo 账户余额查询
     */
    public String queryAccount(Map<String, Object> params) {
        String content = MinShengCombination.getAccountQueryXml(params);
        String response = Commons.httpclientSend(ConstantsLoader.getProperty("minsheng_url"), content, "application/x-NS-BDES; charset=GBK");
        return response;
    }

    /**
     * @param params
     * @return
     * @Todo 账户明细
     */
    public String queryDetail(Map<String, Object> params) {
        String content = MinShengCombination.getQueryDetailXml(params);
        String response = Commons.httpclientSend(ConstantsLoader.getProperty("minsheng_url"), content, "application/x-NS-BDES; charset=GBK");
        return response;
    }

    /**
     * @param result
     * @return 0:成功
     * WEC32：当是查询交易表示查询条件错误
     * WEC02：转账的时候出现网络异常，具体转账成败未知；
     * 其他：失败代码，表示交易失败
     * 空字符串：响应异常
     * Error：自定义异常
     * @Todo 获取响应报文头<responseHeader></responseHeader> 响应码 code
     */
    public String getResponseHeaderCode(String result) {
        String code = "";
        String[] stringArray;
        if (result.contains("<code>")) {
            stringArray = result.split("<code>");
            stringArray = stringArray[1].split("</code>");
            code = "".equals(stringArray[0].trim()) ? "Error" : stringArray[0].trim();
        }
        return code;
    }
}
