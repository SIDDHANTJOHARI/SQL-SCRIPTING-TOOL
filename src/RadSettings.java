 import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JFileChooser;
class RadSettings extends JFrame implements ActionListener
{
private JLabel basePackageName,location;
private JTextField vBasePackageName,vLocation;
private JButton save,browse;
RadSettings ()
{
super("RAD Settings");


basePackageName = new JLabel("BasePackageName");
location= new JLabel("Location");
vBasePackageName = new JTextField ();
vLocation = new JTextField ();

save = new JButton("SAVE");
browse= new JButton("...");

JPanel p1 = new JPanel();
p1.setLayout(null);

basePackageName.setBounds(20,35,150,30);
p1.add(basePackageName);
vBasePackageName.setBounds(170,30,730,30);
p1.add(vBasePackageName);
location.setBounds(20,75,150,30);
p1.add(location);
vLocation.setBounds(170,70,730,30);
p1.add(vLocation);
save.setBounds(220,110,120,30);
p1.add(save);
browse.setBounds(930,75,20,20);
p1.add(browse);


add(p1);                                      
setSize(1000,200);
setVisible(true);
setLocationRelativeTo(null);
save.addActionListener(this);
browse.addActionListener(this);
}

public void actionPerformed(ActionEvent e) 
{
if(e.getSource()==save)
{
String pkg;
String fullPath;
String lcn=vLocation.getText();
pkg=vBasePackageName.getText();
fullPath=lcn;
File file=new File("RADSetting");
if(file.exists()==true)
{
file.delete();
}
try{
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
randomAccessFile.writeBytes(fullPath + "\n");
randomAccessFile.writeBytes(pkg + "\n");
randomAccessFile.close();
}catch(IOException io)
{
}
dispose();
}
if(e.getSource()==browse)
{
JFileChooser fc=new JFileChooser();
fc.setCurrentDirectory(new java.io.File("c:"));
fc.setDialogTitle("Chose Folder");
fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
if(fc.showOpenDialog(browse)==JFileChooser.APPROVE_OPTION){

vLocation.setText(fc.getSelectedFile().getAbsolutePath());
}
}
}

}