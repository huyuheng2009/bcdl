package com.yogapay.bcdl.domain;

import java.util.Date;
import java.util.List;

public class TransferRecord extends ToString{
	private Long id;
	private String thirdVoucher;//转账凭证
	private Date createTime; //创建时间
	private String transferNo; //转账编号
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getThirdVoucher() {
		return thirdVoucher;
	}
	public void setThirdVoucher(String thirdVoucher) {
		this.thirdVoucher = thirdVoucher;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getTransferNo() {
		return transferNo;
	}
	public void setTransferNo(String transferNo) {
		this.transferNo = transferNo;
	}
	
}
