package com.yogapay.bcdl.domain;

public class TransferRecordInfo extends ToString{
	private Long id;
	private Long transferRecordId; //TransferRecord的id
	private String inacctNo;//收款人账户
	private String inacctName;//收款人账户户名
	private String inacctBankName;//收款人开户行名称
	private Double tranAmount;//转出金额
	private String unionFlag;//行内跨行标志
	private String addrFlag;//同城/异地标志
	private String sthirdVoucher;//单笔转账凭证号(批次中的流水号)
	private String useEx;//资金用途
	private Integer status; //转账状态
	private String frontLogNo;//银行流水号
	private String inacctStatus;//入账状态
    private String merchantNo;//商户编号

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInacctNo() {
		return inacctNo;
	}
	public void setInacctNo(String inacctNo) {
		this.inacctNo = inacctNo;
	}
	public String getInacctName() {
		return inacctName;
	}
	public void setInacctName(String inacctName) {
		this.inacctName = inacctName;
	}
	public String getInacctBankName() {
		return inacctBankName;
	}
	public void setInacctBankName(String inacctBankName) {
		this.inacctBankName = inacctBankName;
	}
	public Double getTranAmount() {
		return tranAmount;
	}
	public void setTranAmount(Double tranAmount) {
		this.tranAmount = tranAmount;
	}
	public String getUnionFlag() {
		return unionFlag;
	}
	public void setUnionFlag(String unionFlag) {
		this.unionFlag = unionFlag;
	}
	public String getAddrFlag() {
		return addrFlag;
	}
	public void setAddrFlag(String addrFlag) {
		this.addrFlag = addrFlag;
	}
	public String getUseEx() {
		return useEx;
	}
	public void setUseEx(String useEx) {
		this.useEx = useEx;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getTransferRecordId() {
		return transferRecordId;
	}
	public void setTransferRecordId(Long transferRecordId) {
		this.transferRecordId = transferRecordId;
	}
	public String getFrontLogNo() {
		return frontLogNo;
	}
	public void setFrontLogNo(String frontLogNo) {
		this.frontLogNo = frontLogNo;
	}
	public String getInacctStatus() {
		return inacctStatus;
	}
	public void setInacctStatus(String inacctStatus) {
		this.inacctStatus = inacctStatus;
	}

    public String getSthirdVoucher() {
        return sthirdVoucher;
    }

    public void setSthirdVoucher(String sthirdVoucher) {
        this.sthirdVoucher = sthirdVoucher;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
}
