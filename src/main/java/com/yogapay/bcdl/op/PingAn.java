package com.yogapay.bcdl.op;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yogapay.bcdl.domain.MerchantAccount;
import com.yogapay.bcdl.domain.MerchantAccountInfo;
import com.yogapay.bcdl.domain.TransferRecord;
import com.yogapay.bcdl.domain.TransferRecordInfo;
import com.yogapay.bcdl.service.AccountService;
import com.yogapay.bcdl.service.SeqService;
import com.yogapay.bcdl.service.TransferRecordInfoService;
import com.yogapay.bcdl.service.TransferRecordService;
import com.yogapay.bcdl.utils.*;
import junit.framework.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PingAn {
	private static final Logger log = LoggerFactory
			.getLogger(PingAn.class);
	@Resource
	private Dao dao;
	@Resource
	private TransferRecordService transferRecordService;
	@Resource
	private TransferRecordInfoService transferRecordInfoService;
	@Resource
	private SeqService seqService;
    @Resource
    private AccountService accountService;

	//单笔转账4004
	public String transferOne(Map<String, Object> map,boolean test) throws SQLException{
		
		TransferRecordInfo transferRecordInfo = transferRecordInfoService.findTransferRecordInfoBySThirdVoucher((String)map.get("sthirdVoucher"));
		TransferRecord transferRecord =  transferRecordService.findTransferRecordById(transferRecordInfo.getTransferRecordId());
//		transferRecordService.update(transferRecord);
		transferRecordInfo.setInacctStatus("1");
		transferRecordInfoService.update(transferRecordInfo);
		map.put("thirdVoucher", transferRecord.getThirdVoucher());
		
		String content = PingAnCombination.getTransferOneXml(map);
        String response;
        if(test){
            response = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                    "<Result>" +
                    "<ThirdVoucher>"+transferRecord.getThirdVoucher()+"</ThirdVoucher>" +
                    "<FrontLogNo>"+transferRecord.getThirdVoucher()+"</FrontLogNo>" +
                    "</Result>";
        }else{
            response = Commons.httpclientSend(ConstantsLoader.getProperty("pingan_url"), content,"text/xml; charset=GBK");
        }
		return response;
	}
	
	public String handleTransfer(String result) throws SQLException{
		String response = null;
		Map<String, String> map = new HashMap<String, String>();
		if(result.indexOf("<") == -1){
			String thirdVoucher = result.substring(67, 87);
			TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher(thirdVoucher);
			List<TransferRecordInfo> list = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
			for(TransferRecordInfo transferRecordInfo : list){
				transferRecordInfo.setInacctStatus("0");
				transferRecordInfoService.update(transferRecordInfo);
			}
			
			 map.put("status", "fail");
			response = JSON.toJSONString(map);
		}else{
			String xml = result.substring(result.indexOf("<"), result.length());
			Map<String, Object> obj = XmlUtil.parser1Xml(xml);
			String thirdVoucher = (String) obj.get("ThirdVoucher");
			log.info("thirdVoucher===="+thirdVoucher);
			TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher(thirdVoucher);
			List<TransferRecordInfo> list = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
			for(TransferRecordInfo transferRecordInfo : list){
				System.out.println("obj"+obj.get("FrontLogNo"));
				transferRecordInfo.setInacctStatus("1");
				transferRecordInfo.setFrontLogNo((String)obj.get("FrontLogNo"));
				
				transferRecordInfoService.update(transferRecordInfo);
			}
			
			map.put("status", "success");
			response = JSON.toJSONString(map);
		}
		return response;
	}
	
	
	//单笔转账结果查询4005
	public String transferOneQuery(Map<String, Object> map,boolean test) throws SQLException{
		String thirdVoucher = StringUtil.stringFillLeftZero(seqService.getSeq("third_voucher"), 8);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHss");
		map.put("thirdVoucher", sdf.format(now)+thirdVoucher);
		String content = PingAnCombination.getTransferOneQueryXml(map);
        String response;
        if(test){
            response = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                    "<Result>" +
                    "<OrigThirdVoucher>"+map.get("origThirdVoucher")+"</OrigThirdVoucher>" +
                    "<FrontLogNo>"+map.get("origThirdVoucher")+"</FrontLogNo>" +
                    "<Stt>20</Stt>" +
                    "</Result>";
        }else{
            response = Commons.httpclientSend(ConstantsLoader.getProperty("pingan_url"), content);
        }

		return response;
	}
	
	public String handleTransferOneQuery(String result) throws SQLException{
		String response = null;
		if(result.indexOf("<") > -1){
			String xml = result.substring(result.indexOf("<"), result.length());
			Map<String, Object> obj = XmlUtil.parser1Xml(xml);
			String thirdVoucher = (String) obj.get("OrigThirdVoucher");
			TransferRecord transferRecord = transferRecordService.findTransferRecordByThirdVoucher(thirdVoucher);
			List<TransferRecordInfo> list = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(transferRecord.getId());
			for(TransferRecordInfo transferRecordInfo : list){
				transferRecordInfo.setStatus(2);
				if("20".equals((String)obj.get("Stt"))){
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
                            Assert.assertNull("accountInfo 一定要为空！", accountInfo);
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
				}else if("30".equals((String)obj.get("Stt"))){
					transferRecordInfo.setInacctStatus("3");
				}else{
					transferRecordInfo.setInacctStatus("1");
				}
//				if("000000:转账交易成功".equals(obj.get("Yhcljg"))){
//					transferRecordInfo.setInacctStatus("2");
//				}else if("MA9111:交易处理中".equals(obj.get("Yhcljg")) || "000000:交易受理成功待处理".equals(obj.get("Yhcljg")) ||
//						"000000:交易处理中".equals(obj.get("Yhcljg")) || "000000:交易受理成功处理中".equals(obj.get("Yhcljg"))){
//					transferRecordInfo.setInacctStatus("1");
//				}else{
//					transferRecordInfo.setInacctStatus("3");
//				}
				
				transferRecordInfoService.update(transferRecordInfo);
			}
			response = JSON.toJSONString(obj);
		}else{
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", "fail");
			response = JSON.toJSONString(map);
		}
		return response;
	}
	
	//批量转账：4018
	public String transferBigBatch(Map<String, Object> map,boolean test) throws SQLException{
		String list = (String) map.get("list");
		JSONArray listArry = JSON.parseArray(list);
		String sthirdVoucher = null;
		for(int i =0; i<listArry.size(); i++){
			sthirdVoucher = (String) listArry.getJSONObject(i).get("sthirdVoucher");
			TransferRecordInfo transferRecordInfo = transferRecordInfoService.findTransferRecordInfoBySThirdVoucher(sthirdVoucher);
			transferRecordInfo.setInacctStatus("1");
			transferRecordInfoService.update(transferRecordInfo);
		}
		sthirdVoucher = (String) listArry.getJSONObject(0).get("sthirdVoucher");
		
		TransferRecordInfo transferRecordInfo = transferRecordInfoService.findTransferRecordInfoBySThirdVoucher(sthirdVoucher);
		TransferRecord transferRecord =  transferRecordService.findTransferRecordById(transferRecordInfo.getTransferRecordId());
//		transferRecordService.update(transferRecord);
		map.put("thirdVoucher", transferRecord.getThirdVoucher());
		String content = PingAnCombination.getTransferBigBatchXml(map);
        String response;

        if(test){
            response ="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                    "<Result>" +
                    "<ThirdVoucher>"+transferRecord.getThirdVoucher()+"</ThirdVoucher>" +
                    "<FrontLogNo>"+transferRecord.getThirdVoucher()+"</FrontLogNo>" +
                    "</Result>";
        }else{
            response = Commons.httpclientSend(ConstantsLoader.getProperty("pingan_url"), content);
        }
		return response;
	}
	
	public String handleTransferBigBatchQuery(String result) throws SQLException{
		String response = null;
		if(result.indexOf("<") > -1){
			String xml = result.substring(result.indexOf("<"), result.length());
			Map<String, Object> obj = XmlUtil.parser2Xml(xml);
			log.info("obj====="+obj);
			List<Map<String, Object>> list = (List<Map<String, Object>>) obj.get("list");
			System.out.println("list.size="+list.size());
			System.out.println("list="+list);
			TransferRecordInfo transferRecordInfo = null;
			for(Map<String, Object> map : list){
				if(map.containsKey("SThirdVoucher")){
					log.info("======="+map.get("SThirdVoucher"));
					transferRecordInfo = transferRecordInfoService.findTransferRecordInfoBySThirdVoucher((String)map.get("SThirdVoucher"));
				}
				if(map.containsKey("Stt")){
					log.info("======="+map.get("Stt"));
                    transferRecordInfo.setStatus(2);
					if("20".equals((String)map.get("Stt"))){
						transferRecordInfo.setInacctStatus("2");
					}else if("30".equals((String)map.get("Stt"))){
						transferRecordInfo.setInacctStatus("3");
					}else{
						transferRecordInfo.setInacctStatus("1");
					}
					transferRecordInfoService.update(transferRecordInfo);
				}
			}
			
			response = JSON.toJSONString(obj);
		}else{
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", "fail");
			response = JSON.toJSONString(map);
		}
		return response;
	}
	
	
	//批量或者单笔转账结果查询：4015
	public String transferBathcQuery(Map<String ,Object> map,boolean test) throws SQLException{
		String thirdVoucher = StringUtil.stringFillLeftZero(seqService.getSeq("third_voucher"), 8);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHss");
		map.put("thirdVoucher", sdf.format(now)+thirdVoucher);
		String content = PingAnCombination.getTransferBatchQueryXml(map);
        String response;
        if(test){
            TransferRecord record = transferRecordService.findTransferRecordByThirdVoucher(map.get("origThirdVoucher").toString());
            List<TransferRecordInfo> recordInfoList = transferRecordInfoService.findTransferRecordInfoByTransferRecordId(record.getId());
            response = "<?xml version=\"1.0\" encoding=\"GBK\" ?><Result>${body}</Result>";
            StringBuffer body = new StringBuffer();
            for(TransferRecordInfo recordInfo:recordInfoList){
                body.append("<list><SThirdVoucher>"+recordInfo.getSthirdVoucher()+"</SThirdVoucher><Stt>20</Stt></list>");
            }
            response = response.replace("${body}",body);
        }else{
            response = Commons.httpclientSend(ConstantsLoader.getProperty("pingan_url"), content);
        }
		return response;
	}
	
	//企业当日交易详情查询[4008]
	public String transDetailCurrent(Map<String, Object> map) throws SQLException{
		String thirdVoucher = StringUtil.stringFillLeftZero(seqService.getSeq("third_voucher"), 8);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHss");
		String headerThirdVoucher = sdf.format(now)+thirdVoucher;
		map.put("headerThirdVoucher", headerThirdVoucher);
		String content = PingAnCombination.getTransDetailCurrentXml(map);
		String response = Commons.httpclientSend(ConstantsLoader.getProperty("pingan_url"), content);
		return response;
	}
	
	public String handleTransDetailCurrent(String result){
		String response = null;
		if(result.indexOf("<") > -1){
			String xml = result.substring(result.indexOf("<"), result.length());
			response = JSON.toJSONString(XmlUtil.parser2Xml(xml));
		}else{
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", "fail");
			response = JSON.toJSONString(map);
		}
		return response;
	}
	
	//查询账户历史交易明细[4013]
	public String transDetailHistory(Map<String, Object> map) throws SQLException{
		String thirdVoucher = StringUtil.stringFillLeftZero(seqService.getSeq("third_voucher"), 8);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHss");
		String headerThirdVoucher = sdf.format(now)+thirdVoucher;
		map.put("headerThirdVoucher", headerThirdVoucher);
		String content = PingAnCombination.getTransDetailHistoryXml(map);
		String response = Commons.httpclientSend(ConstantsLoader.getProperty("pingan_url"), content,"text/xml; charset=GBK");
		return response;
	}
	
	public String handleTransDetailHistory(String result){
		String response = null;
		if(result.indexOf("<") > -1){
			String xml = result.substring(result.indexOf("<"), result.length());
			response = JSON.toJSONString(XmlUtil.parser2Xml(xml));
		}else{
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", "fail");
			response = JSON.toJSONString(map);
		}
		return response;
	}
}
