package publicGUI.entity;

import login.entity.CommSetParams;
import login.entity.UserInfo;

public class PassParameter {
	// 用户信息
	public UserInfo userInfo;
	// 参数信息
	public SetParams setParams;
	// 通讯信息
	public CommSetParams commSetParams;

	public CommSetParams getCommSetParams() {
		return commSetParams;
	}

	public void setCommSetParams(CommSetParams commSetParams) {
		this.commSetParams = commSetParams;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public SetParams getSetParams() {
		return setParams;
	}

	public void setSetParams(SetParams setParams) {
		this.setParams = setParams;
	}

	@Override
	public String toString() {
		return "PassParameter [userInfo=" + userInfo + ", setParams=" + setParams + ", commSetParams=" + commSetParams
				+ "]";
	}

}
