package login.entity;

public class UserInfo {
	//姓名
	public String userName;
	//密码
	public String password;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "UserInfo [userName=" + userName + ", password=" + password + "]";
	}
	
}
