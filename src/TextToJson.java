import java.io.*;
import java.sql.*;
import java.util.*;
import org.json.simple.JSONObject;
public class TextToJson {
	
	static BufferedReader br = null;
	static int iteration = 0;
	static String jsonstr;
	static double[] valuesarr = new double[1000];
	static int transactionnumber=0;
	public static void main(String[] args) {
		JSONObject obj = new JSONObject();		
		double totalvalue=0;
		double avg=0;
		double max=0;
		try {
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/vimaldb","root","root");  
			PreparedStatement stmt=con.prepareStatement("insert into cpuvalues (transactioname, averagevalue, maximumvalue) values(?,?,?)");   
    		System.out.println("Database Connected Successfully\n");
			String line;
			br = new BufferedReader(new FileReader("C:\\Users\\vimal\\Desktop\\Ctshackathonproject\\sampleinput.txt"));
			while ((line = br.readLine()) != null) {
				StringTokenizer stringTokenizer = new StringTokenizer(line, " ");
				transactionnumber++;
				stmt.setString(1,"Transaction"+transactionnumber);
				while (stringTokenizer.hasMoreElements()) {
					int i=0;
					while(i<8) {
						stringTokenizer.nextElement().toString();
						i++;
					}
					Double reqCPU = Double.parseDouble(stringTokenizer.nextElement().toString());
					if(max<reqCPU)
						max=reqCPU;
					totalvalue+=reqCPU;
					while(i<11) 
					{
						stringTokenizer.nextElement().toString();
						i++;
					}
					iteration++;
					jsonstr = iteration + "s";
					obj.put(jsonstr,reqCPU);
					valuesarr[iteration]=reqCPU;
				}
				for(int index=0;index<valuesarr.length;index++)
				{
					totalvalue=totalvalue+valuesarr[index];
				}
				avg = totalvalue/valuesarr.length;
				obj.put("total", totalvalue);
				obj.put("average", avg);
				stmt.setDouble(2, avg);
				stmt.setDouble(3, totalvalue);
                
				//Execute the query
				stmt.execute();
				
                //printing the Values in the json format
				System.out.println(obj);
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
