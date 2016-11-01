import com.yogapay.bcdl.utils.XmlUtil;


public class StringTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "1234567890";
		System.out.println(a.substring(2, a.length()));
		String xml = "<?xml version=\"1.0\" encoding=\"GBK\" ?><Result><successCts>1</successCts><successAmt>0.01</successAmt><faildCts>1</faildCts><faildAmt>0.01</faildAmt><list><FrontLogNo>10101011100428</FrontLogNo><CcyCode>RMB</CcyCode><OutAcctBankName></OutAcctBankName><OutAcctNo>11000097408701</OutAcctNo><InAcctBankName>anything</InAcctBankName><InAcctNo>11000098571501</InAcctNo><InAcctName>EBANK</InAcctName><TranAmount>0.01</TranAmount><UnionFlag>0</UnionFlag><Yhcljg>000000:内部转账交易成功/外部转账交易成功</Yhcljg><Fee>0.00</Fee></list><list><FrontLogNo>10101011100429</FrontLogNo><CcyCode>RMB</CcyCode><OutAcctBankName></OutAcctBankName><OutAcctNo>11000097408701</OutAcctNo><InAcctBankName>anything</InAcctBankName><InAcctNo>11000098571501</InAcctNo><InAcctName>EBANK</InAcctName><TranAmount>0.01</TranAmount><UnionFlag>1</UnionFlag><Yhcljg>MA9112:转账交易失败,收款方户名输入错误</Yhcljg><Fee>0.00</Fee></list></Result>";
		XmlUtil.parser2Xml(xml);
	}

}
