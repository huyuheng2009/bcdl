//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import com.yogapay.bcdl.domain.TransferRecord;
//import com.yogapay.bcdl.domain.TransferRecordInfo;
//import com.yogapay.bcdl.domain.TransferRecordQuery;
//import com.yogapay.bcdl.utils.Commons;
//
//
//public class PinganTest {
//
//	public static void main(String[] args) {
//		getPinganTransferOne();
//	}
//	
//	private static void getPinganTransferOne(){
//		TransferRecord transferRecord = new TransferRecord();
//		transferRecord.setBankCode("pingan");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHss");
//		transferRecord.setThirdVoucher(sdf.format(new Date())+"zc2s5236");
//		transferRecord.setCstInnerFlowNo("09876543210987654321");
//		transferRecord.setCcyCode("RMB");
//		transferRecord.setOutAcctNo("11007187041901");
//		transferRecord.setOutAcctName("SHENFA007187041");
//		TransferRecordInfo detail = new TransferRecordInfo();
////		detail.setInAcctBankNode("102100000353");
//		detail.setInAcctNo("6216260000000000548");
//		detail.setInAcctName("PA测试67336");
//		detail.setInAcctBankName("平安银行");
//		detail.setTranAmount(2.12);
//		detail.setAddrFlag("2");
//		detail.setUnionFlag("1");
//		List<TransferRecordInfo> list = new ArrayList<TransferRecordInfo>();
//		list.add(detail);
//		transferRecord.setList(list);
//		String content = Commons.getPinganTransferOneXml(transferRecord);
//		System.out.println("content===");
//		System.out.println(content);
////		content = "A0010101010010107990000999900000000005114004  12345012010081115421620100811153400      999999000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000<?xml version=\"1.0\" encoding=\"GB2312\"?><Result><ThirdVoucher>20100811153405</ThirdVoucher><CcyCode>RMB</CcyCode><OutAcctNo>11002873403401</OutAcctNo><OutAcctName>ebt</OutAcctName><OutAcctAddr/><InAcctBankNode/><InAcctRecCode/><InAcctNo>11002873390701</InAcctNo><InAcctName>EBANK</InAcctName><InAcctBankName>anything</InAcctBankName><TranAmount>000.01</TranAmount><AmountCode/><UseEx>testreturn</UseEx><UnionFlag>1</UnionFlag><SysFlag>2</SysFlag><AddrFlag>1</AddrFlag><RealFlag>2</RealFlag><MainAcctNo/></Result>";
//		String result = Commons.httpclientSend("http://127.0.0.1:7070", content);
//		System.out.println("result="+result);
//	}
//	
//	private static void getPinganTransferOneQuery(){
//		TransferRecordQuery query = new TransferRecordQuery();
//		query.setOrigThirdVoucher("12345678901234567890");
//		query.setOrigFrontLogNo("12345678901234");
//		String content = Commons.getPinganTransferOneQueryXml(query);
//		String result = Commons.httpclientSend("http://127.0.0.1:7070", content);
//		System.out.println("result="+result);
//	}
//	
//	public static void getPinganTransferBatch(){
//		TransferRecord transferRecord = new TransferRecord();
//		transferRecord.setBankCode("pingan");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHSS");
//		transferRecord.setThirdVoucher(sdf.format(new Date())+"12345678");
//		transferRecord.setTotalCts(2);
//		transferRecord.setTotalAmt(5.00);
//		transferRecord.setCstInnerFlowNo("12345678901234567890");
//		transferRecord.setCcyCode("RMB");
//		transferRecord.setOutAcctNo("12345678901234");
//		transferRecord.setOutAcctName("支付界");
//		
//		TransferRecordInfo detail1 = new TransferRecordInfo();
//		detail1.setsThirdVoucher("12345678901234567890");
//		detail1.setInAcctNo("6226660602188753");
//		detail1.setInAcctName("韩磊");
//		detail1.setInAcctBankName("光大银行");
//		detail1.setTranAmount(2.00);
//		detail1.setUseEx("结算");
//		detail1.setUnionFlag("0");
//		detail1.setAddrFlag("1");
//		
//		TransferRecordInfo detail2 = new TransferRecordInfo();
//		detail2.setsThirdVoucher("12345678901234567891");
//		detail2.setInAcctNo("6226660602188753");
//		detail2.setInAcctName("韩磊");
//		detail2.setInAcctBankName("光大银行");
//		detail2.setTranAmount(3.00);
//		detail2.setUseEx("结算");
//		detail2.setUnionFlag("0");
//		detail2.setAddrFlag("1");
//		
//		List<TransferRecordInfo> list = new ArrayList<TransferRecordInfo>();
//		list.add(detail1);
//		list.add(detail2);
//		String content = Commons.getPinganTransferBatchXml(transferRecord);
//		String result = Commons.httpclientSend("http://127.0.0.1:7070", content);
//		System.out.println("result="+result);
//	}
//
//	public static void getPinganTransferBigBatch(){
//		TransferRecord transferRecord = new TransferRecord();
//		transferRecord.setBankCode("pingan");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHSS");
//		transferRecord.setThirdVoucher(sdf.format(new Date())+"12345678");
//		transferRecord.setTotalCts(2);
//		transferRecord.setTotalAmt(5.00);
//		transferRecord.setBSysFlag("Y");
//		transferRecord.setCcyCode("RMB");
//		transferRecord.setOutAcctNo("12345678901234");
//		transferRecord.setOutAcctName("支付界");
//		
//		TransferRecordInfo detail1 = new TransferRecordInfo();
//		detail1.setsThirdVoucher("12345678901234567890");
//		detail1.setInAcctNo("6226660602188753");
//		detail1.setInAcctName("韩磊");
//		detail1.setInAcctBankName("光大银行");
//		detail1.setTranAmount(2.00);
//		detail1.setUseEx("结算");
//		detail1.setUnionFlag("0");
//		detail1.setAddrFlag("1");
//		
//		TransferRecordInfo detail2 = new TransferRecordInfo();
//		detail2.setsThirdVoucher("12345678901234567891");
//		detail2.setInAcctNo("6226660602188753");
//		detail2.setInAcctName("韩磊");
//		detail2.setInAcctBankName("光大银行");
//		detail2.setTranAmount(3.00);
//		detail2.setUseEx("结算");
//		detail2.setUnionFlag("0");
//		detail2.setAddrFlag("1");
//		
//		List<TransferRecordInfo> list = new ArrayList<TransferRecordInfo>();
//		list.add(detail1);
//		list.add(detail2);
//		
//		transferRecord.setList(list);
//		
//		String content = Commons.getPinganTransferBigBatchXml(transferRecord);
//		String result = Commons.httpclientSend("http://127.0.0.1:7070", content);
//		System.out.println("result="+result);
//	}
//	
//	public static void getPinganTransferBatchQuery(){
//		TransferRecordQuery query = new TransferRecordQuery();
//		query.setOrigThirdLogNo("12345678901234567890");
//		String SThirdVoucher1 = "12345678901234567890";
//		String SThirdVoucher2 = "123456789012345678981";
//		List<String> SThirdVoucherList = new ArrayList<String>();
//		SThirdVoucherList.add(SThirdVoucher1);
//		SThirdVoucherList.add(SThirdVoucher2);
//		query.setSThirdVoucherList(SThirdVoucherList);
//		String content = Commons.getPinganTransferBatchQueryXml(query);
//		String result = Commons.httpclientSend("http://127.0.0.1:7070", content);
//		System.out.println("result="+result);
//	}
//	
//}
