import java.io.*;
import java.sql.*;
import java.util.*;

//Importing jar file for json
import org.json.simple.*;

	
public class TextToJson 
{
	static String temp;
	static BufferedReader br = null;
	static int iteration = 0;
	static double max=0;
    static double avg=0;
    static double sum=0;
    
    //Declaring JSON object and JSON array
	static JSONObject obj = new JSONObject();
	static JSONArray array = new JSONArray();
	static double[] arr = new double[1000];
	
	public static void main(String[] args) throws SQLException 
	{
		//JDBC CONNECTION
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vimaldb","root","root");
		Statement s= con.createStatement();
		System.out.println("JDBC Connected to Database Successfully");
		try {
			String Eachline;
			
			//Reading the input file sampleinput.txt
			
			br = new BufferedReader(new FileReader("C:\\Users\\vimal\\Desktop\\Ctshackathonproject\\sampleinput.txt"));
			while ((Eachline = br.readLine()) != null) 
			{
				StringTokenizer stringTokenizer = new StringTokenizer(Eachline, " ");
				while (stringTokenizer.hasMoreElements()) 
				{
					int x=0;
					while(x<8) 
					{
						stringTokenizer.nextElement().toString();
						x++;
					}
					
                   //Required CPU Value
					
					Double reqCPU = Double.parseDouble(stringTokenizer.nextElement().toString());
					if(max<reqCPU)
						max=reqCPU;
					while(x<11) 
					{
						stringTokenizer.nextElement().toString();
						x++;
					}
					 iteration++;
					 //Inserting the values to the db
					 s.executeUpdate("insert into cputable (count,cputime) values ("+iteration+","+reqCPU+")");
					temp = iteration+"s";
					obj.put(temp,reqCPU);
					arr[iteration]=reqCPU;
				}
			}
			System.out.println("CPU Values inserted into the db");
			for (int i = 0; i < arr.length; i++) 
			{
				sum = sum + arr[i];
			}
					avg = sum/arr.length;
					obj.put("Total",sum);
					obj.put("Max",max);
					obj.put("Avg",avg);
					array.add(obj);
					System.out.println("JSON FORMAT");
					System.out.println(array);
					System.out.println("Average and maximum value also displayed in the html format open response.html");
					PreparedStatement pt2=con.prepareStatement("SELECT max(cputime),avg(cputime) FROM cputable");
					ResultSet result1=pt2.executeQuery();
					while(result1.next()) {
						String maximum=result1.getString(1);
						String average=result1.getString(2);
						 PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\vimal\\Desktop\\Ctshackathonproject\\responsetime.html"));
						 out.println("<table border=1>");
				         out.println("<caption>CPU VALUES</caption>");
				         out.println("<tr><th>MAXIMUM CPU TIME</th><th>AVERAGE CPU TIME</th></tr>");
				         out.println("<tr><td>"+ maximum+"</td><td>"+average+"</td></tr>");     
				         out.println("</table>");
				         out.close();
					}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				if (br != null)
					br.close();

			} 
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
	}

}
