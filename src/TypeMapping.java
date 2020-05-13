import java.sql.*;
import java.util.*;
public class TypeMapping
{
private static HashMap<Integer,String> typeMap;
static
{
typeMap = new HashMap<>();
typeMap.put(Types.INTEGER,"int");
typeMap.put(Types.CHAR,"String");
typeMap.put(Types.VARCHAR,"LONGVARCHAR");
typeMap.put(Types.LONGVARCHAR,"String");
typeMap.put(Types.BIT,"Boolean");
typeMap.put(Types.TINYINT ,"int");
typeMap.put(Types.SMALLINT,"int");
typeMap.put(Types.BIGINT,"Long");
typeMap.put(Types.REAL,"Float");
typeMap.put(Types.FLOAT,"Double");
typeMap.put(Types.DOUBLE,"Double");
}
public static String getJavaType(int aa)
{
return typeMap.get(aa);
}
}