import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class a {
public static void main(String[] args) throws DocumentException {
	String a = "A0010101010090107980000001800000000005764004  12345022014070222443020140702223000000020000000:交易受理成功                                                                                       00000000000000000000000000000<?xml version=\"1.0\" encoding=\"GBK\" ?><Result><ThirdVoucher>20140702223000000020</ThirdVoucher><FrontLogNo>18140702925921</FrontLogNo><CcyCode>RMB</CcyCode><OutAcctName>SHENFA007187041</OutAcctName><OutAcctNo>11007187041901</OutAcctNo><InAcctBankName>平安银行</InAcctBankName><InAcctNo>6216260000000000548</InAcctNo><InAcctName>PA测试67336</InAcctName><TranAmount>0.01</TranAmount><UnionFlag>1</UnionFlag><Fee1>0.00</Fee1><Fee2>0.00</Fee2><SOA_VOUCHER></SOA_VOUCHER><hostFlowNo>0048467</hostFlowNo><Mobile></Mobile><CstInnerFlowNo>20140702223000000020</CstInnerFlowNo></Result>";
	System.out.println(a.substring(67, 87));
}
}
