import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import javax.swing.table.AbstractTableModel;
class view extends JFrame implements ActionListener,SqlListener,ListSelectionListener
{
DefaultListModel<Table> model;
private JList tableList;
private JTextField sql;
private JTextArea error,output;
//private JButton connect,quit,disconnect;
private JButton run;
private JButton generateDTO;
private JPanel p8,homePanel,p9;
private JPanel pTable,listPanel; 
private JTable table; 
private JScrollPane jsp; 
private MyModel m;
private Object[][] data ;
private String[] title;
private MyClass myClass;
private int flag;
JScrollPane scrollPane ;
private JMenuBar mb;
private JMenu menu;
public static JMenuItem connect,disconnect,quit,radSettings;
Table DTOTable;
private JLabel moduleTitleLabel,errorLabel,outputLabel,sqlLabel,dbTables,homeLabel1,homeLabel2,homeLabel3;
view()
{

super("GUI client tool for java derby");
DTOTable=null;
data = new Object[0][0];
title = new String[0];
m = new MyModel();
myClass=MyClass.getInstance();
myClass.setSqlListener(this);
moduleTitleLabel=new JLabel("GUI client tool for java derby");
flag=0;
errorLabel =new JLabel("Error");
outputLabel= new JLabel("Output");
sqlLabel= new JLabel("SQL  Statement");
dbTables = new JLabel("Database Tables");
dbTables.setBounds(120,25,150,30);
Font fnn=new Font("Calibary",Font.BOLD,16); 
dbTables.setFont(fnn);

homeLabel1=new JLabel("WELCOME TO");
homeLabel2=new JLabel("'SQL through UI'");
homeLabel3=new JLabel("Create your database here");

Font font1=new Font("Book Antiqua",Font.BOLD,40);
Font font2=new Font("Calligrapher ",Font.PLAIN,60);
Font font3=new Font("Book Antiqua",Font.PLAIN,30);
homeLabel1.setFont(font1);
homeLabel2.setFont(font2);
homeLabel3.setFont(font3);
homeLabel1.setBounds(100,100,600,100);
homeLabel2.setBounds(100,200,600,150);
homeLabel3.setBounds(100,350,600,100);
homePanel=new JPanel();
homePanel.setLayout(null);
homePanel.setBackground(Color.white);
homePanel.add(homeLabel1);
homePanel.add(homeLabel2);
homePanel.add(homeLabel3);
homePanel.setVisible(true);


model = new DefaultListModel<>();
tableList= new JList<>( model );
tableList.setBounds(15,5,300,560);//610
tableList.setBorder(BorderFactory.createLineBorder(Color.black));
scrollPane = new JScrollPane(tableList,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
scrollPane.setBounds(7,80,305,550);
sql=new JTextField();
error=new JTextArea();
output=new JTextArea();
error.setBackground(Color.black);
error.setForeground(Color.red);
output.setForeground(new Color(0,100,0));
Font fn=new Font("Verdana",Font.PLAIN,16);  
error.setFont(fn); 
output.setFont(fnn);
sql.setFont(fn);
error.setLineWrap(true);
output.setLineWrap(true);



connect=new JMenuItem("Connect");
disconnect=new JMenuItem("Disconnect");
disconnect.setEnabled(false);
quit=new JMenuItem("QUIT");
radSettings=new JMenuItem("RAD Settings");

menu=new JMenu("MENU");
menu.add(connect);
menu.add(disconnect);
menu.add(radSettings);
menu.add(quit);


mb=new JMenuBar();
mb.add(menu);
setJMenuBar(mb);



run=new JButton("Run");

connect.setEnabled(true);
disconnect.setEnabled(false);
quit.setEnabled(true);


radSettings.addActionListener(this);
connect.addActionListener(this);
disconnect.addActionListener(this);
quit.addActionListener(this);
run.addActionListener(this);
menu.addActionListener(this);
tableList.addListSelectionListener(this);

JPanel p3=new JPanel();
p3.setLayout(null);
p3.setBounds(15,2,975,58);
sqlLabel.setBounds(9,0,500,29);
sql.setBounds(9,25,700,30);
run.setBounds(750,25,80,30);
p3.add(sqlLabel);
p3.add(sql);
p3.add(run);


JPanel p4=new JPanel();
p4.setLayout(null);
p4.setBounds(15,450,1000,230);
errorLabel.setBounds(15,0,150,30);
error.setBounds(20,30,970,190);//220
p4.add(errorLabel);
p4.add(error);
p4.setBorder(BorderFactory.createLineBorder(Color.black));

JPanel p5=new JPanel();
p5.setLayout(null);
p5.setBounds(15,5,1000,445);
p5.add(p3);
p5.setBorder(BorderFactory.createLineBorder(Color.black));




pTable=new JPanel();
table=new JTable(m); 
Font f=new Font("Verdana",Font.PLAIN,16); 
table.setFont(f); 
table.setRowHeight(30);
table.getTableHeader().setReorderingAllowed(false); 
jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS); 
pTable.setLayout(new BorderLayout()); 
pTable.add(jsp,BorderLayout.CENTER);   
pTable.setVisible(false); 
pTable.setBounds(15,30,984,355);

p8=new JPanel();
p8.setLayout(null);
p8.setBounds(0,61,1000,390);
output.setBorder(BorderFactory.createLineBorder(Color.black));
output.setEditable(false);
outputLabel.setBounds(15,0,150,30);
output.setBounds(20,30,970,350);
output.setVisible(true);
p8.add(outputLabel);
p8.add(pTable);
p8.add(output);
p8.setBorder(BorderFactory.createLineBorder(Color.black));

p5.add(p8);

generateDTO=new JButton("Generate DTO");
generateDTO.setBounds(60,635,200,25);
generateDTO.setEnabled(false);
listPanel = new JPanel();
listPanel.setLayout(null);
listPanel.setBounds(1020,4,338,675);
listPanel.setBorder(BorderFactory.createLineBorder(Color.black));
listPanel.add(dbTables);
listPanel.add(scrollPane);
listPanel.add(generateDTO);



generateDTO.addActionListener(this);

p9=new JPanel();
p9.setLayout(null);
p9.add(p5);
p9.add(p4);
p9.add(listPanel); 
p9.setVisible(false);
add(homePanel);
p9.setBorder(BorderFactory.createLineBorder(Color.black));
setLocation(10,10);
setSize(1480,780);
setVisible(true);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

}


public static void setEnableAfterConnection()
{
connect.setEnabled(false);
disconnect.setEnabled(true);
}
public void actionPerformed(ActionEvent e)
{
if(e.getSource()==connect)
{
MyConnection c = new MyConnection();
add(p9);
homePanel.setVisible(false);
p9.setVisible(true);
}

if(e.getSource()==disconnect)
{
generateDTO.setEnabled(true);
model.removeAllElements();
myClass.disConnect();
connect.setEnabled(true);
disconnect.setEnabled(false);
add(homePanel);
homePanel.setVisible(true);
p9.setVisible(false);
}

if(e.getSource()==quit)
{
myClass.disConnect();
connect.setEnabled(true);
disconnect.setEnabled(false);
quit.setEnabled(false);
System.exit(0);
}


if(e.getSource()==radSettings)
{
RadSettings r=new RadSettings();

}
if(e.getSource()==generateDTO)
{
error.setText("");
File file=new File("RADSetting");
if(!file.exists())
{
error.setText("First set RAD Settings ");
}
else
{
GenerateDL gdl=new GenerateDL(DTOTable);
}
}

/*
if(e.getSource()==menu)
{

if(flag==0)
{
flag=1;
connect.setVisible(true);
disconnect.setVisible(true);
quit.setVisible(true);
}
else
{
if(flag==1)
{
flag=0;
connect.setVisible(false);
disconnect.setVisible(false);
quit.setVisible(false);
}
}
}
*/
if(e.getSource()==run)
{
String s=sql.getText();
if(s.trim().length()==0)
{
error.setText("Please! enter sql statement");
}
else
{
if(s.trim().indexOf("select") != -1 || s.trim().indexOf("describe") != -1 )myClass.myExecuteQuery(s.trim());
else myClass.myExecuteUpdate(s.trim());
}
}
}
public void valueChanged(ListSelectionEvent ev)
{
 if (!ev .getValueIsAdjusting()) {
DTOTable=(Table)tableList.getSelectedValue();
generateDTO.setEnabled(true);
 MouseListener mouseListener = new MouseAdapter() {
      public void mouseClicked(MouseEvent mouseEvent) {
        JList theList = (JList) mouseEvent.getSource();

 if (mouseEvent.getClickCount() == 2) {
         int index = theList.locationToIndex(mouseEvent.getPoint());
          if (index >= 0) {
           Object  o = theList.getModel().getElementAt(index);
DTOTable=(Table)o;
myClass.myExecuteQuery(" select * from "+o.toString().trim()+"");
sql.setText((" select * from "+o.toString().trim()+""));

          }
        }


      }
    };

   tableList.addMouseListener(mouseListener);
}
}








public void setErrorText(String s)
{
error.setText(s);
output.setText("");
}

public void setOutputText(String s)
{
pTable.setVisible(false);
output.setVisible(true);
output.setText(s);
error.setText("");
}


public void setTable(Object[][] data, String [] title)
{
this.data=data;
this.title=title;
AbstractTableModel atm = (AbstractTableModel) table.getModel();
atm.fireTableStructureChanged();
output.setVisible(false);
pTable.setVisible(true);
error.setText("");
}
public void drawDatabaseTable(List<Table> tables)
{
model.removeAllElements();
for(Table s:tables)
{
model.addElement(s);                                                    
}
}

//inner class



public class MyModel extends AbstractTableModel 
{ 
public MyModel() 
{ 

} 
public int getColumnCount() 
{ 
return title.length; 
} 
public String getColumnName(int columnIndex) 
{ 
return title[columnIndex]; 
} 
public int getRowCount() 
{ 
return data.length; 
} 
public boolean isCellEditable(int rowIndex,int columnIndex) 
{  
return false; 
} 
public Object getValueAt(int rowIndex,int columnIndex) 
{ 
return data[rowIndex][columnIndex]; 
} 
/*
public Class getColumnClass(int columnIndex) 
{ 
Class c=null; 
try 
{ 
if(columnIndex==0 || columnIndex==1) 
{
 c=Class.forName("java.lang.Integer"); 
} 
if(columnIndex==2) 
{ 
c=Class.forName("java.lang.String"); 
} 
if(columnIndex==3) 
{
 c=Class.forName("java.lang.Boolean"); 
} 
}catch(ClassNotFoundException cnfe) { } 
return c;
}
public void setValueAt(Object d,int rowIndex,int columnIndex)
 { 
data[rowIndex][columnIndex]=d; 
} 
*/
} 


public static void main(String gg[])
{
try
{
view a=new view();
}catch(Exception e)
{
System.out.println(e);
}
}

}
