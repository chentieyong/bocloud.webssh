package com.bocloud.webssh.service.utils;

/**
 * 
 * @author tangcq
 * @since 2018-02-01
 */
public enum Operation {

	/**
	 * 删除操作
	 */
	DELETE("delete"),
	/**
	 * 更新操作
	 */
	MODIFY("modify");
	private String operate;

	private Operation(String operate) {
		this.operate = operate;
	}

	/**
	 * @return the operate
	 */
	public String getOperate() {
		return operate;
	}

	// public boolean isEqualTo(String operate) {
	// if (StringUtils.isEmpty(operate)) {
	// return false;
	// }
	// Operation[] operations= Operation.values();
	// for(Operation operation:operations) {
	// if (operation.getOperate().equalsIgnoreCase(operate)) {
	// return true;
	// }
	// }
	// return false;
	// }
}
