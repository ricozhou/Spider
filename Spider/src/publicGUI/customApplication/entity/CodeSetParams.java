package publicGUI.customApplication.entity;

public class CodeSetParams {
	// 语言
	public int codeLanguage;
	// 类名
	public String className;

	public int getCodeLanguage() {
		return codeLanguage;
	}

	public void setCodeLanguage(int codeLanguage) {
		this.codeLanguage = codeLanguage;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "CodeSetParams [codeLanguage=" + codeLanguage + ", className=" + className + "]";
	}

}
