package com.yogapay.bcdl.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @Title ToString.java
 * @Description 重写toString方法，打印方便
 * @date 2012-5-17 下午10:08:55
 * @version V1.0
 */
public class ToString{
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
