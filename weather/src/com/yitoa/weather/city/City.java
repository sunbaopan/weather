package com.yitoa.weather.city;

public class City {
	private String province;
	private String pinyin;
	private String hanzi;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getHanzi() {
		return hanzi;
	}

	public void setHanzi(String hanzi) {
		this.hanzi = hanzi;
	}

	public String toString() {
		if (null == this.getProvince())
			return this.getHanzi() + " | " + this.getPinyin();
		return this.getHanzi() + " | " + this.getPinyin()+" | "+this.getProvince();
	}
}
