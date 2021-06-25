import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class MyCalculator extends JFrame implements ActionListener
{
	Panel panTop,panMemory,panDown;
	JLabel lblStd,lblHistory,lblCurrent;
	JButton btnMemory[]=new JButton[5];
	JButton btnDown[]=new JButton[24];
	String strMemory[]= {"MC","MR","M+","M-","MS"};
	String strDown[]= {"%","CE","C","\u232b"," 1/x","x\u00B2","\u221Ax","\u00F7","7","8","9","\u00D7","4","5","6","-","1","2","3","+","\u00B1","0",".","="};
	Boolean overlapFlag=false;
	double history,memory,ans;
	char op;
	
	MyCalculator()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(470,700);
		setLocationRelativeTo(null);
		setTitle("MyCalculator");
		setLayout(new BorderLayout());
		
		Font f1=new Font(Font.SANS_SERIF,Font.PLAIN,25);
		Font f2=new Font(Font.SANS_SERIF,Font.BOLD,60);
		Font f3=new Font(Font.SANS_SERIF,Font.BOLD,20);
		Font f4=new Font(Font.SANS_SERIF,Font.PLAIN,25);
		Font f5=new Font(Font.SANS_SERIF,Font.BOLD,25);
		
		lblStd=new JLabel("= 	Standard @");
		lblStd.setFont(f1);
		
		lblHistory=new JLabel("",JLabel.RIGHT);
		lblHistory.setFont(f1);
		
		lblCurrent=new JLabel("0",JLabel.RIGHT);
		lblCurrent.setFont(f2);
		
		panTop=new Panel();
		panTop.setLayout(new GridLayout(4,1));
		panTop.setBackground(new Color(240,240,240));
		
		panMemory=new Panel();
		panMemory.setLayout(new GridLayout(1,5));
		
		panDown=new Panel();
		panDown.setLayout(new GridLayout(6,4));
		
		//adding all components
		panTop.add(lblStd);
		panTop.add(lblHistory);
		panTop.add(lblCurrent);
		
		for(int i=0;i<btnMemory.length;i++)
		{
			btnMemory[i]=new JButton(strMemory[i]);
			btnMemory[i].setFont(f3);
			btnMemory[i].setBackground(new Color(240,240,240));
			btnMemory[i].setBorder(BorderFactory.createEmptyBorder());
			btnMemory[i].addActionListener(this);
			btnMemory[i].addMouseListener(new MouseAdapter()
			{
				Color oldClr;
				public void mouseEntered(MouseEvent me)
				{
					JButton b1=(JButton)me.getSource();
					oldClr=b1.getBackground();
					b1.setBackground(new Color(180,180,180));
				}
				public void mouseExited(MouseEvent me)
				{
					JButton b1=(JButton)me.getSource();
					b1.setBackground(oldClr);
				}
			});
			panMemory.add(btnMemory[i]);
		}
		
		panTop.add(panMemory);
		
		for(int i=0;i<btnDown.length;i++)
		{
			btnDown[i]=new JButton(strDown[i]);
			
			if(i<8 || i==11 || i==15 || i==19) 
			{
				btnDown[i].setBackground(new Color(244,244,244));
				btnDown[i].setFont(f4);
			}
			else if(i==23)
			{
				btnDown[i].setBackground(new Color(145,193,231));
				btnDown[i].setFont(f5);
			}
			else
			{
				btnDown[i].setBackground(new Color(253,253,253));
				btnDown[i].setFont(f5);
			}
			btnDown[i].setBorder(BorderFactory.createLineBorder(Color.WHITE));
			btnDown[i].addActionListener(this);
			btnDown[i].addMouseListener(new MouseAdapter()
			{
				Color oldClr1;
				public void mouseEntered(MouseEvent me)
				{
					JButton b2=(JButton)me.getSource();
					if(b2.getText()=="=")
					{
						oldClr1=b2.getBackground();
						b2.setBackground(new Color(61,151,203));
					}
					else
					{
						oldClr1=b2.getBackground();
						b2.setBackground(new Color(200,180,180));
					}
				}
				public void mouseExited(MouseEvent me)
				{
					JButton b2=(JButton)me.getSource();
					b2.setBackground(oldClr1);
				}
			});
			panDown.add(btnDown[i]);
		}
		add(panTop,BorderLayout.NORTH);
		add(panDown);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent ae)
	{
		String s1=ae.getActionCommand();
		String curr=lblCurrent.getText();
		double currNo=Double.parseDouble(curr);
		
		if(Character.isDigit(s1.charAt(0)))
		{
			if(overlapFlag==true)
			{
				//overlap
				lblCurrent.setText(filter(s1));
				overlapFlag=false;
			}
			else	//append
				lblCurrent.setText(filter(curr+s1));
		}
		
		if(isOperator(s1.charAt(0)))
		{
			overlapFlag=true;
			if(op=='\u0000')
			{
				history=currNo;
				op=s1.charAt(0);
				lblHistory.setText(lblHistory.getText()+filter(history+"")+op);
			}
			else
			{
				ans=eval(op,history,currNo);
				lblCurrent.setText(filter(ans+""));
				op=s1.charAt(0);
				history=ans;
				lblHistory.setText(lblHistory.getText()+filter(currNo+"")+op);
			}
		}
		else if(s1.equals("="))
		{
			ans=eval(op,history,currNo);
			lblCurrent.setText(filter(ans+""));
			lblHistory.setText("");
			history=0;
			op='\u0000';
			overlapFlag=true;
		}
		else if(s1.equals("%"))
		{
			if(op=='+' || op=='-')
			{
				lblCurrent.setText(filter(history*currNo/100+""));
				lblHistory.setText(lblHistory.getText()+filter(history*currNo/100+""));
			}
			else if(op=='\u00D7' || op=='\u00F7')
			{
				lblCurrent.setText(currNo/100+"");
				lblHistory.setText(lblHistory.getText()+filter(currNo/100+""));
			}
			overlapFlag=true;
			
		}
		else if(s1.equals("CE"))
		{
			lblCurrent.setText("0");
		}
		else if(s1.equals("C"))
		{
			lblHistory.setText("");
			lblCurrent.setText("0");
			history=0;
			op='\u0000';
		}
		else if(s1.equals("\u232b"))
		{
			//backspace
			lblCurrent.setText(filter(curr.substring(0,curr.length()-1)));
		}
		else if(s1.equals(" 1/x"))
		{
			lblCurrent.setText(filter(1/currNo+""));
		}
		else if(s1.equals("x\u00B2"))
		{
			//square
			lblCurrent.setText(filter(currNo*currNo+""));
		}
		else if(s1.equals("\u221Ax"))
		{
			//square root
			lblCurrent.setText(filter(Math.sqrt(currNo)+""));
		}
		else if(s1.equals("\u00B1"))
		{
			// +/-
			lblCurrent.setText(filter(-1*currNo+""));
		}
		else if(s1.equals("."))
		{
			lblCurrent.setText(curr+s1);
		}
		else if(s1.equals("MC"))
		{
			memory=0;
		}
		else if(s1.equals("MR"))
		{
			lblCurrent.setText(filter(memory+""));
			overlapFlag=true;
		}
		else if(s1.equals("M+"))
		{
			memory+=Double.parseDouble(lblCurrent.getText());
			overlapFlag=true;
		}
		else if(s1.equals("M-"))
		{
			memory-=Double.parseDouble(lblCurrent.getText());
			overlapFlag=true;
		}
		else if(s1.equals("MS"))
		{
			memory=Double.parseDouble(lblCurrent.getText());
			overlapFlag=true;
		}
	}
	
	String filter(String s) 
	{
		double d=Double.parseDouble(s);
		if(d==(int)d)
			return (int)d+"";
		else
			return d+"";
	}
	boolean isOperator(char ch)
	{
		if(ch=='+' || ch=='-' || ch=='\u00D7' || ch=='\u00F7')
			return true;
		else
			return false;
	}
	double eval(char op,double op1,double op2)
	{
		switch(op)
		{
			case '+':return op1+op2;
			case '-':return op1-op2;
			case '\u00D7':return op1*op2;
			case '\u00F7':return op1/op2;
			default:return 0;
		}
	}
	public static void main(String[] args) 
	{
		new MyCalculator();
	}
}
