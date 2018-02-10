package publicGUI.customApplication;

import java.io.File;
import java.io.IOException;

import org.omg.SendingContext.RunTime;

public class b {
	public static void main(String[] args) {
		String cmd= "jre\\bin\\java -cp E:\\Java\\Spider3.0\\customFunction\\bin test2";
		Runtime run=Runtime.getRuntime();  
		try { 
//			run.exec("cmd set classpath=%classpath%;E:\\Java\\Spider3.0\\jre\\lib\\rt.jar;E:\\Java\\Spider3.0\\jre\\lib\\tools.jar;");
			Process process = run.exec(cmd);
			System.out.println(process);
		} catch (IOException e) {
			e.printStackTrace();
		}  	}
}
