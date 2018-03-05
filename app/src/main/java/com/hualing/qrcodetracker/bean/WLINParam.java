package com.hualing.qrcodetracker.bean;


public class WLINParam {
	//二维码编号
	private String qRCodeID;
	//物料编码
	private String wLCode;
	//货物名称
	private String productName;
	//类别
	private int lb;
	//原料批次
	private String yLPC;
	//数量
    private float shl ;
    //单位重量
    private float dWZL;
	//批次重量
	private float pCZL;
	//单位
    private String dW ;
	//规格
	private String gG;
	//产地
	private String cHD;
	//入库单号
	private String inDh;
	//来料时间（入库时间）
	private String lLTime;
	//操作员（入库人）
	private String cZY;
	//标志位
	private int bz;
	//备注
	private String remark;
	public String getqRCodeID() {
		return qRCodeID;
	}
	public void setqRCodeID(String qRCodeID) {
		this.qRCodeID = qRCodeID;
	}
	public String getwLCode() {
		return wLCode;
	}
	public void setwLCode(String wLCode) {
		this.wLCode = wLCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getLb() {
		return lb;
	}

	public void setLb(int lb) {
		this.lb = lb;
	}

	public float getShl() {
		return shl;
	}

	public void setShl(float shl) {
		this.shl = shl;
	}

	public float getdWZL() {
		return dWZL;
	}

	public void setdWZL(float dWZL) {
		this.dWZL = dWZL;
	}

	public float getpCZL() {
		return pCZL;
	}

	public void setpCZL(float pCZL) {
		this.pCZL = pCZL;
	}

	public String getyLPC() {
		return yLPC;
	}
	public void setyLPC(String yLPC) {
		this.yLPC = yLPC;
	}
	public String getdW() {
		return dW;
	}
	public void setdW(String dW) {
		this.dW = dW;
	}
	public String getgG() {
		return gG;
	}
	public void setgG(String gG) {
		this.gG = gG;
	}
	public String getcHD() {
		return cHD;
	}
	public void setcHD(String cHD) {
		this.cHD = cHD;
	}
	public String getInDh() {
		return inDh;
	}
	public void setInDh(String inDh) {
		this.inDh = inDh;
	}
	public String getlLTime() {
		return lLTime;
	}
	public void setlLTime(String lLTime) {
		this.lLTime = lLTime;
	}
	public String getcZY() {
		return cZY;
	}
	public void setcZY(String cZY) {
		this.cZY = cZY;
	}
	public int getBz() {
		return bz;
	}
	public void setBz(int bz) {
		this.bz = bz;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
