/**
 * 项目: bcdl
 * 包名：com.yogapay.bcdl.op
 * 文件名: PingAnCombination
 * 创建时间: 2014/8/11 10:57
 * 支付界科技有限公司版权所有，保留所有权利
 */
package com.yogapay.bcdl.op;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yogapay.bcdl.utils.ConstantsLoader;
import com.yogapay.bcdl.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Todo:平安银行拼装
 * @Author: Zhanggc
 */
public class PingAnCombination {
    private static StringBuffer getHeader(String content, String type , String thirdVoucher){
        StringBuffer header = new StringBuffer();
        try {
			header.append(ConstantsLoader.getProperty("pingan_request_header")+StringUtil.stringFillLeftZero(content.getBytes("GBK").length, 10));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date now = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat time = new SimpleDateFormat("HHmmss");
        header.append(type + "  1234501"+date.format(now)+time.format(now));
        header.append(thirdVoucher);
        header.append("999999000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        return header;
    }

    //单笔转账4004
    public static String getTransferOneXml(Map<String, Object> map){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        sb.append("<Result><ThirdVoucher>").append(map.get("thirdVoucher")).append("</ThirdVoucher>");
        sb.append("<CstInnerFlowNo>").append(map.get("thirdVoucher")).append("</CstInnerFlowNo>");
        sb.append("<CcyCode>RMB</CcyCode>");
        sb.append("<OutAcctNo>").append(map.get("outAcctNo")).append("</OutAcctNo>");
        sb.append("<OutAcctName>").append(map.get("outAcctName")).append("</OutAcctName>");
        sb.append("<InAcctBankNode>").append(map.get("inAcctBankNode")).append("</InAcctBankNode>");
        sb.append("<InAcctNo>").append(map.get("inAcctNo")).append("</InAcctNo>");
        sb.append("<InAcctName>").append(map.get("inAcctName")).append("</InAcctName>");
        sb.append("<InAcctBankName>").append(map.get("inAcctBankName")).append("</InAcctBankName>");
        sb.append("<TranAmount>").append(map.get("tranAmount")).append("</TranAmount>");
        sb.append("<UnionFlag>").append(map.get("unionFlag")).append("</UnionFlag>");
        sb.append("<AddrFlag>").append(map.get("addrFlag")).append("</AddrFlag>");
        sb.append("</Result>");
        StringBuffer header = getHeader(sb.toString(), "4004", (String)map.get("thirdVoucher"));
        header.append(sb.toString());
        System.out.println(header.toString());
        return header.toString();

    }

    //单笔转账查询[4005]
    public static String getTransferOneQueryXml(Map<String, Object> map){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        sb.append("<Result>");
        sb.append("<OrigThirdVoucher>").append(map.get("origThirdVoucher")).append("</OrigThirdVoucher>");
        sb.append("<OrigFrontLogNo>").append(map.get("origFrontLogNo")).append("</OrigFrontLogNo>");
        sb.append("</Result>");
        StringBuffer header = getHeader(sb.toString(), "4005", (String)map.get("thirdVoucher"));
        header.append(sb.toString());
        System.out.println(header.toString());
        return header.toString();
    }

    //企业批量实时资金划转[4014]
//	public static String getTransferBatchXml(TransferRecord transferRecord){
//		StringBuffer sb = new StringBuffer();
//		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
//		sb.append("<Result><ThirdVoucher>").append(transferRecord.getThirdVoucher()).append("</ThirdVoucher>");
//		sb.append("<totalCts>").append(transferRecord.getTotalCts()).append("</totalCts>");
//		sb.append("<totalAmt>").append(transferRecord.getTotalAmt()).append("</totalAmt>");
//		List<TransferRecordInfo> list = transferRecord.getList();
//		for(TransferRecordInfo detail : list){
//			sb.append("<HOResultSet4014R>");
//			sb.append("<SThirdVoucher>").append(detail.getsThirdVoucher()).append("</transferRecord>");
//			sb.append("<CcyCode>RMB</CcyCode>");
//			sb.append("<OutAcctNo>").append(transferRecord.getOutAcctNo()).append("</OutAcctNo>");
//			sb.append("<OutAcctName>").append(transferRecord.getOutAcctName()).append("</OutAcctName>");
//			sb.append("<InAcctNo>").append(detail.getInAcctNo()).append("</InAcctNo>");
//			sb.append("<InAcctName>").append(detail.getInAcctName()).append("</InAcctName>");
//			sb.append("<InAcctBankName>").append(detail.getInAcctBankName()).append("</InAcctBankName>");
//			sb.append("<TranAmount>").append(detail.getTranAmount()).append("</TranAmount>");
//			sb.append("<UseEx>").append(detail.getUseEx()).append("</UseEx>");
//			sb.append("<UnionFlag>").append(detail.getUnionFlag()).append("</UnionFlag>");
//			sb.append("<AddrFlag>").append(detail.getAddrFlag()).append("</AddrFlag>");
//			sb.append("</HOResultSet4014R>");
//		}
//		sb.append("</Result>");
//
//		StringBuffer header = getHeader(sb.toString(), "4014", "");
//		header.append(sb.toString());
//		System.out.println(header.toString());
//		return header.toString();
//	}

    //企业大批量资金划转[4018]
    public static String getTransferBigBatchXml(Map<String, Object> map){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        sb.append("<Result><ThirdVoucher>").append(map.get("thirdVoucher")).append("</ThirdVoucher>");
        sb.append("<totalCts>").append(map.get("totalCts")).append("</totalCts>");
        sb.append("<totalAmt>").append(map.get("totalAmt")).append("</totalAmt>");
        sb.append("<BSysFlag>Y</BSysFlag><CcyCode>RMB</CcyCode>");
        sb.append("<OutAcctNo>").append(map.get("outAcctNo")).append("</OutAcctNo>");
        sb.append("<OutAcctName>").append(map.get("outAcctName")).append("</OutAcctName>");

        String list = (String) map.get("list");
        JSONArray listArry = JSON.parseArray(list);
        for(int i=0; i<listArry.size(); i++){
            sb.append("<HOResultSet4018R>");
            JSONObject o = listArry.getJSONObject(i);
            sb.append("<SThirdVoucher>").append(o.get("sthirdVoucher")).append("</SThirdVoucher>");
            sb.append("<CstInnerFlowNo>").append(o.get("sthirdVoucher")).append("</CstInnerFlowNo>");
            sb.append("<CcyCode>RMB</CcyCode>");
            sb.append("<InAcctNo>").append(o.get("inAcctNo")).append("</InAcctNo>");
            sb.append("<InAcctName>").append(o.get("inAcctName")).append("</InAcctName>");
            sb.append("<InAcctBankName>").append(o.get("inAcctBankName")).append("</InAcctBankName>");
            sb.append("<TranAmount>").append(o.get("tranAmount")).append("</TranAmount>");
            sb.append("<UseEx>").append(o.get("useEx")).append("</UseEx>");
            sb.append("<UnionFlag>").append(o.get("unionFlag")).append("</UnionFlag>");
            sb.append("<AddrFlag>").append(o.get("addrFlag")).append("</AddrFlag>");
            sb.append("</HOResultSet4018R>");
        }
        sb.append("</Result>");

        StringBuffer header = getHeader(sb.toString(), "4018", (String)map.get("thirdVoucher"));
        header.append(sb.toString());
        System.out.println(header.toString());
        return header.toString();
    }

    //批量转账指令查询[4015]
    public static String getTransferBatchQueryXml(Map<String, Object> map){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        sb.append("<Result><OrigThirdVoucher>").append(map.get("origThirdVoucher")).append("</OrigThirdVoucher>");
//		String list = (String) map.get("List");
//		JSONArray listArry = JSON.parseArray(list);
//		for(int i=0; i<listArry.size(); i++){
//			JSONObject o = listArry.getJSONObject(i);
//			sb.append("<HOResultSet4015R>");
//			sb.append("<SThirdVoucher>").append(o.get("sthirdVoucher")).append("</SThirdVoucher>");
//			sb.append("</HOResultSet4015R>");
//		}
        sb.append("</Result>");

        StringBuffer header = getHeader(sb.toString(), "4015", (String)map.get("thirdVoucher"));
        header.append(sb.toString());
        System.out.println(header.toString());
        return header.toString();
    }

    //企业账户余额查询 [4001]
    public static String getAccountQueryXml(String accountNo){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        sb.append("<Result><Account>").append(accountNo).append("</Result></Account>");

        StringBuffer header = getHeader(sb.toString(), "4001", "");
        header.append(sb.toString());
        System.out.println(header.toString());
        return header.toString();
    }

    //企业当日交易详情查询[4008]
    public static String getTransDetailCurrentXml(Map<String, Object> map){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?><Result>");
        sb.append("<AcctNo>").append(map.get("acctNo")).append("</AcctNo>");
        sb.append("<CcyCode>RMB</CcyCode>");
        sb.append("<JournalNo>").append(map.get("joumalNo")).append("</JournalNo>");
        sb.append("<LogCount>").append(map.get("logCount")).append("</LogCount></Result>");

        StringBuffer header = getHeader(sb.toString(), "4008", (String)map.get("headerThirdVoucher"));
        header.append(sb.toString());
        System.out.println(header.toString());
        return header.toString();
    }

    //查询账户历史交易明细[4013]
    public static String getTransDetailHistoryXml(Map<String, Object> map){
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?><Result>");
        sb.append("<AcctNo>").append(map.get("acctNo")).append("</AcctNo>");
        sb.append("<CcyCode>RMB</CcyCode>");
        sb.append("<BeginDate>").append(map.get("beginDate")).append("</BeginDate>");
        sb.append("<EndDate>").append(map.get("endDate")).append("</EndDate>");
        sb.append("<PageNo>").append(map.get("pageNo")).append("</PageNo></Result>");

        StringBuffer header = getHeader(sb.toString(), "4013", (String) map.get("headerThirdVoucher"));
        header.append(sb.toString());
        System.out.println(header.toString());
        return header.toString();
    }
}
