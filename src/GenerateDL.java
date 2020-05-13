import java.util.*;
import java.io.*;
import java.sql.*;
class GenerateDL
{
String classname;
private String key,fullPath;
private String pkg;
private Table table;
Connection c;
DatabaseMetaData md;
ResultSet rs;
String primaryKey;
Column d;
GenerateDL(Table table)
{
this.table=table;
try
{
classname=table.getTableName();
classname=classname.substring(0,1).toUpperCase()+classname.substring(1).toLowerCase();
int index;
while(classname.indexOf('_')>=0)
{
index=classname.indexOf('_');
classname=classname.substring(0,index)+classname.substring(index+1,index+2).toUpperCase()+classname.substring(index+2);
}

File file=new File("RADSetting");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
this.fullPath=randomAccessFile.readLine();
this.pkg=randomAccessFile.readLine();
String pkg1=pkg.replaceAll("\\.","\\\\");
this.fullPath=this.fullPath+"\\"+classname+"\\src\\"+pkg1;

randomAccessFile.close();
}catch(IOException io)
{
}
populateDS();
generateDAOException();
generateDTOInterface();
generateDTO();
generateDAOInterface();
generateDAO();
buildConnection();
}
public void populateDS ()
{
try
{
c=MyClass.c;
md=c.getMetaData();
rs=md.getPrimaryKeys(null,null,table.getTableName());
int i=1;
while(rs.next())
{
primaryKey=rs.getString(4);
}
primaryKey=primaryKey.toLowerCase();
while(primaryKey.indexOf('_')>=0)
{
int index=primaryKey.indexOf('_');
primaryKey=primaryKey.substring(0,index)+primaryKey.substring(index+1,index+2).toUpperCase()+primaryKey.substring(index+2);
}
d=null;
List<Column> cc=table.getColumns();
for(Column c:cc)
{
if(c.getColumnName().toLowerCase().equals(primaryKey.toLowerCase()))
{
d=c;
break;
}
}
}catch(Exception e)
{
System.out.println(e.getMessage());
}
}


public void generateDAOException()
{
try
{
String pkg=this.pkg+".dl."+classname+".exceptions";
String fullPath=this.fullPath+"\\dl\\"+classname+"\\exceptions";
File file=new File(fullPath);
if(file.exists()==false)
{
file.mkdirs();
}
String className="DAOException";
String fileName=fullPath+"\\"+className+".java";
File javaFile=new File(fileName);
if(javaFile.exists()) javaFile.delete();
RandomAccessFile randomAccessFile=new RandomAccessFile(javaFile,"rw");
randomAccessFile.writeBytes("package "+pkg+";\r\n");
randomAccessFile.writeBytes("public class  "+className+"  extends Exception implements java.io.Serializable\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("public "+className+" (String message)\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("super(message);\r\n");
randomAccessFile.writeBytes("}\r\n");
randomAccessFile.writeBytes("}\r\n");
}
catch(Exception e)
{
System.out.println(e);
}
}






public void generateDTOInterface()
{
String pkg=this.pkg+".dl."+classname+".interfaces";
String fullPath=this.fullPath+"\\dl\\"+classname+"\\interfaces";
try
{
File file=new File(fullPath);
if(file.exists()==false)
{
file.mkdirs();
}
String className=this.classname+"DTOInterface";
String fileName=fullPath+"\\"+className+".java";
File javaFile=new File(fileName);
if(javaFile.exists()) javaFile.delete();
RandomAccessFile randomAccessFile=new RandomAccessFile(javaFile,"rw");
randomAccessFile.writeBytes("package "+pkg+";\r\n");
randomAccessFile.writeBytes("public interface  "+className+"  extends java.io.Serializable,Comparable<"+className+">\r\n");
randomAccessFile.writeBytes("{\r\n");
List<Column> cc=table.getColumns();
for(Column c:cc)
{
String propertyName;
int propertyType,index;
String javaType="";
propertyName=c.getColumnName();
propertyName=propertyName.toLowerCase();
while(propertyName.indexOf('_')>=0)
{
index=propertyName.indexOf('_');
propertyName=propertyName.substring(0,index)+propertyName.substring(index+1,index+2).toUpperCase()+propertyName.substring(index+2);
}
propertyType=c.getColumnType();
javaType=TypeMapping.getJavaType(propertyType);
randomAccessFile.writeBytes("public void set"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1)+"("+javaType+" "+propertyName+");\r\n");
randomAccessFile.writeBytes("public "+javaType+" get"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1)+"();\r\n");
}
randomAccessFile.writeBytes("}\r\n");
randomAccessFile.close();
}catch(Exception e)
{
System.out.println(e);
}
}



public void generateDTO()
{
String pkg=this.pkg+".dl."+classname+"";
String fullPath=this.fullPath+"\\dl\\"+classname;
try
{
File file=new File(fullPath);
if(file.exists()==false)
{
file.mkdirs();
}
String propertyName;
int propertyType;
String javaType="";
int index;
String className=this.classname+"DTO";
String fileName=fullPath+"\\"+className+".java";
File javaFile=new File(fileName);
if(javaFile.exists()) javaFile.delete();
RandomAccessFile randomAccessFile=new RandomAccessFile(javaFile,"rw");
randomAccessFile.writeBytes("package "+pkg+";\r\n");
randomAccessFile.writeBytes("import "+pkg+".interfaces.*;\r\n");
randomAccessFile.writeBytes("public class  "+className+" implements "+className+"Interface \r\n");
randomAccessFile.writeBytes("{\r\n");

List<Column> cc=table.getColumns();
for(Column c:cc)
{
propertyName=c.getColumnName();
propertyName=propertyName.toLowerCase();

while(propertyName.indexOf('_')>=0)
{
index=propertyName.indexOf('_');
propertyName=propertyName.substring(0,index)+propertyName.substring(index+1,index+2).toUpperCase()+propertyName.substring(index+2);
}
propertyType=c.getColumnType();
javaType=TypeMapping.getJavaType(propertyType);
randomAccessFile.writeBytes("private "+javaType+" "+propertyName+";\r\n");
}
randomAccessFile.writeBytes("public "+className+"()\r\n");

randomAccessFile.writeBytes("{\r\n");

for(Column c:cc)

{

propertyName=c.getColumnName();
propertyName=propertyName.toLowerCase();
while(propertyName.indexOf('_')>=0)
{
index=propertyName.indexOf('_');
propertyName=propertyName.substring(0,index)+propertyName.substring(index+1,index+2).toUpperCase()+propertyName.substring(index+2);
}
propertyType=c.getColumnType();


javaType=TypeMapping.getJavaType(propertyType);
if(javaType=="int")
randomAccessFile.writeBytes("this."+propertyName+" = 0;\r\n");
else
{
randomAccessFile.writeBytes("this."+propertyName+" = null;\r\n");
}
}

randomAccessFile.writeBytes("}\r\n");
for(Column c:cc)
{
propertyName=c.getColumnName();
propertyName=propertyName.toLowerCase();
while(propertyName.indexOf('_')>=0)
{
index=propertyName.indexOf('_');
propertyName=propertyName.substring(0,index)+propertyName.substring(index+1,index+2).toUpperCase()+propertyName.substring(index+2);
}
propertyType=c.getColumnType();
javaType=TypeMapping.getJavaType(propertyType);
randomAccessFile.writeBytes("public void set"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1)+"("+javaType+" "+propertyName+")\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("this."+propertyName+"="+propertyName+";\r\n");
randomAccessFile.writeBytes("}\r\n");
randomAccessFile.writeBytes("public "+javaType+" get"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1)+"()\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("return  "+propertyName+";\r\n");
randomAccessFile.writeBytes("}\r\n");
}
randomAccessFile.writeBytes("public boolean equals (Object object)\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("if(object==null)return false;\r\n");
randomAccessFile.writeBytes("if(!(object instanceof "+className+"Interface))return false;\r\n");
randomAccessFile.writeBytes(""+className+"Interface other=("+className+"Interface)object;\r\n");
randomAccessFile.writeBytes("return this."+primaryKey+"==other.get"+primaryKey.substring(0,1).toUpperCase()+primaryKey.substring(1)+"();\r\n");
randomAccessFile.writeBytes("}\r\n");

randomAccessFile.writeBytes("public int compareTo("+className+"Interface other)\r\n");
randomAccessFile.writeBytes("{\r\n");
Column d=null;
for(Column c:cc)
{
if(c.getColumnName().toLowerCase().equals(primaryKey.toLowerCase()))
{
d=c;
break;
}
}
if(d.getColumnType()==4)
{
randomAccessFile.writeBytes("return this."+primaryKey+"-other.get"+primaryKey.substring(0,1).toUpperCase()+primaryKey.substring(1)+"();\r\n");
}
else
{
randomAccessFile.writeBytes("return this."+primaryKey+".compareTo(other.get"+primaryKey.substring(0,1).toUpperCase()+primaryKey.substring(1)+"());\r\n");
}
randomAccessFile.writeBytes("}\r\n");

randomAccessFile.writeBytes("public int hashCode()\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("return this."+primaryKey+";\r\n");

randomAccessFile.writeBytes("}\r\n");
randomAccessFile.writeBytes("}\r\n");
randomAccessFile.close();
}catch(Exception e)
{
System.out.println(e);
}
}


public void generateDAOInterface()
{
String exceptionName;
String pkg=this.pkg+".dl."+classname+".interfaces";
String fullPath=this.fullPath+"\\dl\\"+classname+"\\interfaces";
try
{
File file=new File(fullPath);
if(file.exists()==false)
{
file.mkdirs();
}
exceptionName="DAOException";
String className=this.classname+"DAOInterface";
String fileName=fullPath+"\\"+className+".java";
File javaFile=new File(fileName);
if(javaFile.exists()) javaFile.delete();
RandomAccessFile randomAccessFile=new RandomAccessFile(javaFile,"rw");
randomAccessFile.writeBytes("package "+pkg+";\r\n");
randomAccessFile.writeBytes("import java.util.*;\r\n");
randomAccessFile.writeBytes("import "+this.pkg+".dl."+classname+".exceptions.*;\r\n");
randomAccessFile.writeBytes("public interface  "+className+" \r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("public void add("+classname+"DTOInterface "+classname.substring(0,1).toLowerCase()+classname.substring(1)+"DTOInterface)throws "+exceptionName+";\r\n");
randomAccessFile.writeBytes("public void update("+classname+"DTOInterface "+classname.substring(0,1).toLowerCase()+classname.substring(1)+"DTOInterface)throws "+exceptionName+";\r\n");
randomAccessFile.writeBytes("public void delete(int "+primaryKey+")throws "+exceptionName+";\r\n");
String javaType=TypeMapping.getJavaType(d.getColumnType());
randomAccessFile.writeBytes("public  "+classname+"DTOInterface getBy"+primaryKey.substring(0,1).toUpperCase()+primaryKey.substring(1)+"("+javaType+" "+primaryKey+")throws "+exceptionName+";\r\n");

randomAccessFile.writeBytes("public List<"+classname+"DTOInterface>getAll()throws "+exceptionName+";\r\n");

randomAccessFile.writeBytes("public int getCount()throws "+exceptionName+";\r\n");
randomAccessFile.writeBytes("}\r\n");
randomAccessFile.close();
}catch(Exception e)
{
System.out.println(e);
}


}
public void generateDAO()
{
String exceptionName,propertyName,javaType;
int propertyType,i=0,j;
String pkg=this.pkg+".dl";
String fullPath=this.fullPath+"\\dl\\"+classname+"";
try
{
File file=new File(fullPath);
if(file.exists()==false)
{
file.mkdirs();
}
exceptionName="DAOException";
String className=this.classname+"DAO";
String fileName=fullPath+"\\"+className+".java";
File javaFile=new File(fileName);
if(javaFile.exists()) javaFile.delete();
RandomAccessFile randomAccessFile=new RandomAccessFile(javaFile,"rw");
randomAccessFile.writeBytes("package "+pkg+";\r\n");
randomAccessFile.writeBytes("import java.util.*;\r\n");
randomAccessFile.writeBytes("import java.sql.*;\r\n");
randomAccessFile.writeBytes("import java.io.*;\r\n");
randomAccessFile.writeBytes("import "+this.pkg+".dl."+classname+".*;\r\n");
randomAccessFile.writeBytes("import "+this.pkg+".dl."+classname+".interfaces.*;\r\n");
randomAccessFile.writeBytes("import "+this.pkg+".dl."+classname+".exceptions.*;\r\n");


randomAccessFile.writeBytes("public class  "+className+" \r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("Connection c=null;\r\n");
randomAccessFile.writeBytes("public "+className+"() throws DAOException\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("c=BuildConnection.getConnection();\r\n");
randomAccessFile.writeBytes("}\r\n");
randomAccessFile.writeBytes("public void add("+classname+"DTOInterface "+classname.substring(0,1).toLowerCase()+classname.substring(1)+"DTOInterface)throws "+exceptionName+"\r\n");
randomAccessFile.writeBytes("{\r\n");
List<Column> cc=table.getColumns();
for(Column c:cc)
{

propertyName=c.getColumnName();
propertyName=propertyName.toLowerCase();
while(propertyName.indexOf('_')>=0)
{
int index;
index=propertyName.indexOf('_');
propertyName=propertyName.substring(0,index)+propertyName.substring(index+1,index+2).toUpperCase()+propertyName.substring(index+2);
}
propertyName=propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
propertyType=c.getColumnType();
javaType=TypeMapping.getJavaType(propertyType);
if(!( primaryKey.toLowerCase().equals(c.getColumnName().toLowerCase()) )   )
{
randomAccessFile.writeBytes(""+javaType+" v"+propertyName+"="+classname.substring(0,1).toLowerCase()+classname.substring(1)+"DTOInterface.get"+propertyName+"();\r\n");
}
else
{
randomAccessFile.writeBytes(""+javaType+" v"+propertyName+";\r\n");
}
}
randomAccessFile.writeBytes("try\r\n{\r\n");
randomAccessFile.writeBytes("PreparedStatement ps=c.prepareStatement(\"insert into "+classname+" (");
i=0;
for(  Column c:cc )
{
if(!( primaryKey.toLowerCase().equals(c.getColumnName().toLowerCase()) )   )i++;
}
j=0;
for(Column c:cc)
{
if(!( primaryKey.toLowerCase().equals(c.getColumnName().toLowerCase()) )   )
{
randomAccessFile.writeBytes(""+c.getColumnName()+"");
if(j==i-1)
{
randomAccessFile.writeBytes(")values(");
}
else
{
randomAccessFile.writeBytes(",");
}
j++;
}
}
j=0;
while(j<i)
{
randomAccessFile.writeBytes("?");
if(j==i-1)
{
randomAccessFile.writeBytes(")\",Statement.RETURN_GENERATED_KEYS);\r\n");
}
else
{
randomAccessFile.writeBytes(",");
}
j++;
}
j=1;
for(Column c:cc)
{
if(!( primaryKey.toLowerCase().equals(c.getColumnName().toLowerCase()) )   )
{
propertyName=c.getColumnName();
propertyName=propertyName.toLowerCase();
while(propertyName.indexOf('_')>=0)
{
int index;
index=propertyName.indexOf('_');
propertyName=propertyName.substring(0,index)+propertyName.substring(index+1,index+2).toUpperCase()+propertyName.substring(index+2);
}
propertyName=propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
if(c.getColumnType()==4)
{
randomAccessFile.writeBytes("ps.setInt("+j+",v"+propertyName+");\r\n");
}
else
{
randomAccessFile.writeBytes("ps.setString("+j+",v"+propertyName+");\r\n");
}
j++;
}
}
randomAccessFile.writeBytes("ps.executeUpdate();\r\n");
randomAccessFile.writeBytes("ResultSet r=ps.getGeneratedKeys();\r\n");
randomAccessFile.writeBytes("if(r.next())"+classname.substring(0,1).toLowerCase()+classname.substring(1)+"DTOInterface.set"+primaryKey.substring(0,1).toUpperCase()+primaryKey.substring(1)+"");
if(d.getColumnType()==4)
{
randomAccessFile.writeBytes("(r.getInt(1));\r\n");
}
else
{
randomAccessFile.writeBytes("(r.getString(1));\r\n");
}
randomAccessFile.writeBytes("r.close();\r\nps.close();\r\nc.close();\r\n }catch(Exception e)\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("throw new DAOException(e.getMessage());\r\n");
randomAccessFile.writeBytes("}\r\n");
randomAccessFile.writeBytes("}\r\n");



randomAccessFile.writeBytes("public void update("+classname+"DTOInterface "+classname.substring(0,1).toLowerCase()+classname.substring(1)+"DTOInterface)throws "+exceptionName+"\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("try\r\n{\r\n");
for(Column c:cc)
{
propertyName=c.getColumnName();
propertyName=propertyName.toLowerCase();
while(propertyName.indexOf('_')>=0)
{
int index;
index=propertyName.indexOf('_');
propertyName=propertyName.substring(0,index)+propertyName.substring(index+1,index+2).toUpperCase()+propertyName.substring(index+2);
}
propertyName=propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
propertyType=c.getColumnType();
javaType=TypeMapping.getJavaType(propertyType);
randomAccessFile.writeBytes(""+javaType+" v"+propertyName+"="+classname.substring(0,1).toLowerCase()+classname.substring(1)+"DTOInterface.get"+propertyName+"();\r\n");
}
randomAccessFile.writeBytes("PreparedStatement ps=c.prepareStatement(\"select * from "+classname+" where "+d.getColumnName()+"=?\");\r\n" );

if(d.getColumnType()==4)
{
randomAccessFile.writeBytes("ps.setInt(1,v"+primaryKey.substring(0,1).toUpperCase()+primaryKey.substring(1)+");\r\n");
}
else
{
randomAccessFile.writeBytes("ps.setString(1,"+primaryKey+");\r\n");
}

randomAccessFile.writeBytes("ResultSet r=ps.executeQuery();\r\nif(!(r.next()))\r\n{\r\n");
randomAccessFile.writeBytes("r.close();\r\nps.close();\r\nc.close();\r\nthrow new DAOException(\""+primaryKey+" is not exists in "+classname+" \");\r\n}\r\n");



randomAccessFile.writeBytes("ps=c.prepareStatement(\"update "+classname+"  set ");
i=0;
for(  Column c:cc )
{
if(!( primaryKey.toLowerCase().equals(c.getColumnName().toLowerCase()) )   )i++;
}
j=0;
for(Column c:cc)
{
if(!( primaryKey.toLowerCase().equals(c.getColumnName().toLowerCase()) )   )
{
randomAccessFile.writeBytes(""+c.getColumnName()+"=? ");
if(j==i-1)
{
randomAccessFile.writeBytes("where "+d.getColumnName()+"=?\");");
}
else
{
randomAccessFile.writeBytes(",");
}
j++;
}
}
j=1;
randomAccessFile.writeBytes("\r\n");
for(Column c:cc)
{
propertyName=c.getColumnName();
propertyName=propertyName.toLowerCase();
while(propertyName.indexOf('_')>=0)
{
int index;
index=propertyName.indexOf('_');
propertyName=propertyName.substring(0,index)+propertyName.substring(index+1,index+2).toUpperCase()+propertyName.substring(index+2);
}
propertyName=propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
if(c.getColumnName().equals(d.getColumnName()))
{
if(c.getColumnType()==4)
{
randomAccessFile.writeBytes("ps.setInt("+(i+1)+",v"+propertyName+");\r\n");
}
else
{
randomAccessFile.writeBytes("ps.setString("+(i+1)+",v"+propertyName+");\r\n");
}
}
else
{
if(c.getColumnType()==4)
{
randomAccessFile.writeBytes("ps.setInt("+j+",v"+propertyName+");\r\n");
}
else
{
randomAccessFile.writeBytes("ps.setString("+j+",v"+propertyName+");\r\n");
}
j++;
}
}
randomAccessFile.writeBytes("ps.executeUpdate();\r\n");
randomAccessFile.writeBytes("ps.close();\r\nc.close();\r\n }catch(Exception e)\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("throw new DAOException(e.getMessage());\r\n");
randomAccessFile.writeBytes("}\r\n");
randomAccessFile.writeBytes("}\r\n");




randomAccessFile.writeBytes("public void delete(int "+primaryKey+")throws "+exceptionName+"\r\n");
randomAccessFile.writeBytes("{\r\n");






randomAccessFile.writeBytes("try\r\n{\r\n");
randomAccessFile.writeBytes("PreparedStatement ps=c.prepareStatement(\"select * from "+classname+" where "+d.getColumnName()+"=?\");\r\n" );

if(d.getColumnType()==4)
{
randomAccessFile.writeBytes("ps.setInt(1,"+primaryKey+");\r\n");
}
else
{
randomAccessFile.writeBytes("ps.setString(1,"+primaryKey+");\r\n");
}

randomAccessFile.writeBytes("ResultSet r=ps.executeQuery();\r\nif(!(r.next()))\r\n{\r\n");
randomAccessFile.writeBytes("r.close();\r\nps.close();\r\nc.close();\r\nthrow new DAOException(\""+primaryKey+" is not exists in "+classname+" \");\r\n}\r\n");
randomAccessFile.writeBytes("ps=c.prepareStatement(\"delete from "+classname+" where "+d.getColumnName()+"=?\");\r\n");
if(d.getColumnType()==4)
{
randomAccessFile.writeBytes("ps.setInt(1,"+primaryKey+");\r\n");
}
else
{
randomAccessFile.writeBytes("ps.setString(1,"+primaryKey+");\r\n");
}
randomAccessFile.writeBytes("ps.executeUpdate();\r\n");
randomAccessFile.writeBytes("ps.close();\r\nc.close();\r\n }catch(Exception e)\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("throw new DAOException(e.getMessage());\r\n");
randomAccessFile.writeBytes("}\r\n");







randomAccessFile.writeBytes("}\r\n");














javaType=TypeMapping.getJavaType(d.getColumnType());
randomAccessFile.writeBytes("public  "+classname+"DTOInterface getBy"+primaryKey.substring(0,1).toUpperCase()+primaryKey.substring(1)+"("+javaType+" "+primaryKey+")throws "+exceptionName+"\r\n");
randomAccessFile.writeBytes("{\r\n");



randomAccessFile.writeBytes("try\r\n{\r\n");
randomAccessFile.writeBytes("PreparedStatement ps=c.prepareStatement(\"select * from "+classname+" where "+d.getColumnName()+"=?\");\r\n" );

if(d.getColumnType()==4)
{
randomAccessFile.writeBytes("ps.setInt(1,"+primaryKey+");\r\n");
}
else
{
randomAccessFile.writeBytes("ps.setString(1,"+primaryKey+");\r\n");
}
randomAccessFile.writeBytes(""+classname+"DTOInterface "+ classname.substring(0,1).toLowerCase()+classname.substring(1)+" = new "+classname+"DTO();\r\n");
randomAccessFile.writeBytes("ResultSet r=ps.executeQuery();\r\nif((r.next()))\r\n{\r\n");

for(Column c:cc)
{

propertyName=c.getColumnName();
propertyName=propertyName.toLowerCase();
while(propertyName.indexOf('_')>=0)
{
int index;
index=propertyName.indexOf('_');
propertyName=propertyName.substring(0,index)+propertyName.substring(index+1,index+2).toUpperCase()+propertyName.substring(index+2);
}
propertyName=propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
if(c.getColumnType()==4)
{
randomAccessFile.writeBytes(""+ classname.substring(0,1).toLowerCase()+classname.substring(1)+".set"+propertyName+"(r.getInt(\""+c.getColumnName()+"\"));\r\n");
}
else
{

randomAccessFile.writeBytes(""+ classname.substring(0,1).toLowerCase()+classname.substring(1)+".set"+propertyName+"(r.getString(\""+c.getColumnName()+"\"));\r\n");
}
}




randomAccessFile.writeBytes("r.close();\r\nps.close();\r\nc.close();\r\n}\r\n");

randomAccessFile.writeBytes("return "+ className.substring(0,1).toLowerCase()+classname.substring(1)+";\r\n");

randomAccessFile.writeBytes("}catch(Exception e)\r\n\r\n");





randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("throw new DAOException(e.getMessage());\r\n");
randomAccessFile.writeBytes("}\r\n");

randomAccessFile.writeBytes("}\r\n");










randomAccessFile.writeBytes("public List<"+classname+"DTOInterface>getAll()throws "+exceptionName+"\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("try\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("List<"+classname+"DTOInterface>  "+ className.substring(0,1).toLowerCase()+classname.substring(1)+"s= new LinkedList<>();\r\n");

randomAccessFile.writeBytes("PreparedStatement ps=c.prepareStatement(\"select * from "+classname+"\");\r\n" );

randomAccessFile.writeBytes("ResultSet r=ps.executeQuery();\r\nwhile((r.next()))\r\n{\r\n");
randomAccessFile.writeBytes(""+classname+"DTOInterface "+className.substring(0,1).toLowerCase()+classname.substring(1)+" = new "+classname+"DTO();\r\n");
for(Column c:cc)
{

propertyName=c.getColumnName();
propertyName=propertyName.toLowerCase();
while(propertyName.indexOf('_')>=0)
{
int index;
index=propertyName.indexOf('_');
propertyName=propertyName.substring(0,index)+propertyName.substring(index+1,index+2).toUpperCase()+propertyName.substring(index+2);
}
propertyName=propertyName.substring(0,1).toUpperCase()+propertyName.substring(1);
if(c.getColumnType()==4)
{
randomAccessFile.writeBytes(""+ classname.substring(0,1).toLowerCase()+classname.substring(1)+".set"+propertyName+"(r.getInt(\""+c.getColumnName()+"\"));\r\n");
}
else
{

randomAccessFile.writeBytes(""+ classname.substring(0,1).toLowerCase()+classname.substring(1)+".set"+propertyName+"(r.getString(\""+c.getColumnName()+"\"));\r\n");
}
}
randomAccessFile.writeBytes(""+ classname.substring(0,1).toLowerCase()+classname.substring(1)+"s.add("+ classname.substring(0,1).toLowerCase()+classname.substring(1)+");\r\n");
randomAccessFile.writeBytes("}\r\n");

randomAccessFile.writeBytes("r.close();\r\nps.close();\r\nc.close();\r\n");

randomAccessFile.writeBytes("return "+ className.substring(0,1).toLowerCase()+classname.substring(1)+"s;\r\n");
randomAccessFile.writeBytes("}catch(Exception e)\r\n\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("throw new DAOException(e.getMessage());\r\n");
randomAccessFile.writeBytes("}\r\n");

randomAccessFile.writeBytes("}\r\n");










randomAccessFile.writeBytes("public int getCount()throws "+exceptionName+"\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("try\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("PreparedStatement ps=c.prepareStatement(\"select * from "+classname+"\");\r\n" );
randomAccessFile.writeBytes("int count=0;\r\n");
randomAccessFile.writeBytes("ResultSet r=ps.executeQuery();\r\nif(r.next())\r\n{\r\ncount = r.getInt(\"1\");\r\nr.close();\r\nps.close();\r\nc.close();\r\n}\r\nreturn count;\r\n");

randomAccessFile.writeBytes("}catch(Exception e)\r\n\r\n");
randomAccessFile.writeBytes("{\r\n");
randomAccessFile.writeBytes("throw new DAOException(e.getMessage());\r\n");
randomAccessFile.writeBytes("}\r\n");

randomAccessFile.writeBytes("}\r\n");
randomAccessFile.writeBytes("}\r\n");







randomAccessFile.close();
}catch(Exception e)
{
System.out.println(e);
}


}








public void buildConnection()
{
try
{
String string;
MyClass m=MyClass.getInstance();
string=m.getString();
String pkg=this.pkg+".dl."+classname;
String fullPath=this.fullPath+"\\dl\\"+classname;
File file=new File(fullPath);
String fileName=""+fullPath+"\\BuildConnection.java";
String exceptionName="DAOException";
if(file.exists()==false)
{
file.mkdirs();
}
File javaFile=new File(fileName);
if(javaFile.exists()) javaFile.delete();
RandomAccessFile randomAccessFile=new RandomAccessFile(javaFile,"rw");
randomAccessFile.writeBytes("package " +pkg+";\r\n");
randomAccessFile.writeBytes("import java.sql.*;\r\n");
randomAccessFile.writeBytes("import " +pkg+".exceptions.*;\r\n");
randomAccessFile.writeBytes("public class BuildConnection \r\n");
randomAccessFile.writeBytes("{ \r\n");
randomAccessFile.writeBytes("private static Connection c; \r\n");
randomAccessFile.writeBytes("private BuildConnection() \r\n");
randomAccessFile.writeBytes("{ \r\n");
randomAccessFile.writeBytes("} \r\n");
randomAccessFile.writeBytes("public static Connection getConnection() throws "+exceptionName+" \r\n");
randomAccessFile.writeBytes("{ \r\n");
randomAccessFile.writeBytes("try \r\n");
randomAccessFile.writeBytes("{ \r\n");
randomAccessFile.writeBytes(" Class.forName(\"org.apache.derby.jdbc.ClientDriver\");\r\n");
randomAccessFile.writeBytes("c=DriverManager.getConnection(\""+string+"\");\r\n");
randomAccessFile.writeBytes("}catch(SQLException sqlException) \r\n");
randomAccessFile.writeBytes("{ \r\n");
randomAccessFile.writeBytes("throw new "+exceptionName+ "(sqlException.getMessage()); \r\n");
randomAccessFile.writeBytes("} \r\n");
randomAccessFile.writeBytes("catch(ClassNotFoundException cnfe) \r\n");
randomAccessFile.writeBytes("{ \r\n");
randomAccessFile.writeBytes("} \r\n");
randomAccessFile.writeBytes("return c; \r\n");
randomAccessFile.writeBytes("} \r\n");
randomAccessFile.writeBytes("} \r\n");

randomAccessFile.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}








