import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
 
public class RGB extends JFrame implements ActionListener{
    JTextField t1,t2,t3;
    JLabel b1,b2,b3;
    JButton jb;
    JPanel jp;
     
    public RGB(){
          super("RGB");
          jp=new JPanel();
          b1=new JLabel("R");
          b2=new JLabel("G");
          b3=new JLabel("B");
          t1=new JTextField(3);
          t2=new JTextField(3);
          t3=new JTextField(3);
          jb=new JButton("确定");
          jb.addActionListener(this);
          jp.add(b1);
          jp.add(t1);
          jp.add(b2);
          jp.add(t2);
          jp.add(b3);
          jp.add(t3);
          jp.add(jb);
          jp.setLayout(new FlowLayout());
           
          add(jp,BorderLayout.CENTER);
          setSize(200,200);
           
          setResizable(false);
          setDefaultCloseOperation(this.EXIT_ON_CLOSE);
          setVisible(true);
          setLocationRelativeTo(null);
    }
          public void actionPerformed(ActionEvent e){
              if(e.getSource().getClass().getSimpleName().equals("JButton")){
                  int r=Integer.parseInt(t1.getText());
                  int g=Integer.parseInt(t2.getText());
                  int b=Integer.parseInt(t3.getText());
                  if(r>=0 && r<=255 && g>=0 && g<=255 && b>=0 && b<=255){
                      Color c=new Color(r,g,b);
                      jp.setBackground(c);
                  }else{
                      System.out.println("请输入(0-255)的整数!");
                  }
              }
        }
  
    public static void main(String[] args) {
        new RGB();
    }
 
}