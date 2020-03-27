import java.io.*;
import org.json.simple.*;
import java.util.*;
import java.sql.*;
public class MemoryTextToJson 
{
	static String temp;
	static int iteration = 0;
	static float  avg=0;
	static float sum=0;
	static float kbvalue=0;
	static float reqval=0;
	static float max=0;
	static JSONObject object1=new JSONObject();
	static JSONObject object2=new JSONObject();
	static float[] arr = new float[1000];
	public static void main(String[] args) throws Exception
	{
		
		
		 BufferedReader br=new BufferedReader(new FileReader("C:\\Users\\vimal\\Desktop\\CTSMainHackathon\\Memory.txt"));	
		 String Eachline;
		 String val="";
		 int lineNum = 0;
		 while ( (Eachline = br.readLine() ) != null )
		 {
			Eachline=Eachline.trim();
		    lineNum++;
		    if ( lineNum % 2 == 0 )
			  continue;
		    String[] array = Eachline.split("   ");
		   val=array[1].trim();
		   kbvalue=Float.parseFloat(val);
		   reqval=(kbvalue/1000);
		   if(max<reqval)
		   {
			   max=reqval;
		   }
		  temp = iteration+"s";
		  object1.put(temp,reqval);
		  arr[iteration]=reqval;
		  iteration++;
		 }
		 
		 //calculating the total
		 for(int i = 0; i < arr.length; i++) 
		 {
			sum = sum + arr[i];
		 }
		 
		 //calculating the average
		 avg = sum/arr.length;
		 max = (float) (Math.round(max * 100.0) / 100.0);
	     avg = (float) (Math.round(avg * 100.0) / 100.0);
		 updatedb(max,avg);
		    
		 String usecasename = "sample";
	
		//Push to json
		 object2.put("Values",object1);
		 object2.put("usecaseName", usecasename);
		 object2.put("AverageMemory(MB)", avg);
		 object2.put("MaxMemory(MB)", max);
		 FileWriter fp=new FileWriter("C:\\Users\\vimal\\Desktop\\CTSMainHackathon\\sampleoutput.json");
	     fp.write(object2.toJSONString());
		 System.out.println(object2);
		 System.out.println("Maximum Value:"+max);
		 System.out.println("Average Value:"+avg);
		 System.out.println("Open response.html to see the HTML output");
		 
	//html ouput function call
		   htmloutput(avg,max);
	}
	
	//JDBC INSERT QUERY FUNCTION
	public static void updatedb(float max,float avg) throws SQLException
	{
		Connection con=getJDBCConnection();
		Statement s= con.createStatement();
		s.executeUpdate("insert into Memory (Maximum_Memory,Average_Memory) values ("+max+","+avg+")");
		System.out.println("Values Updated In Database");
	}
  
	//HTML Output function
	public static void htmloutput(float avg,float max) throws IOException
	{
		String average=String. valueOf(avg);
		String maximum=String. valueOf(max);
		PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\vimal\\Desktop\\CTSMainHackathon\\response.html"));
		out.println("<table border=1>");
        out.println("<caption>MEMORY VALUES</caption>");
        out.println("<tr><th>MAXIMUM MEMORY</th><th>AVERAGE MEMORY</th></tr>");
        out.println("<tr><td>"+ maximum+"</td><td>"+average+"</td></tr>");     
        out.println("</table>");
        out.close();
	}
	
	//JDBC Connection Function
	public static Connection getJDBCConnection() throws SQLException 
	{
		
		Connection con=null;
		try 
		{
		Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vimaldb","root","root");
		System.out.println("JDBC Connected to Database Successfully");
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
}
