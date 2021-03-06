package se.ltu.ssr.dataviewer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/EnvironmentalDataViewerS3")
public class EnviormentalDataViewerS3 extends HttpServlet{
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		MongoDbClient mongoDbClient = new MongoDbClient("130.240.134.129",27017,"SSRtestDB","Fiware");
		mongoDbClient.createConnection();
		String json="";
		/*=mongoDbClient.getSensorList();
		JSONObject json2 = null;
		try {
			json2 = new JSONObject(json);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		System.out.println(json2.toString());*/
		//response.setContentType("text/plain");
	    PrintWriter out = response.getWriter();
	    //out.write(json2.toString());
	    
	    long totalcount=0; 
	    JSONObject json2 = null;
	    if((request.getParameter("skip")==null)||(request.getParameter("skip")==null))
	    {
	    	json=mongoDbClient.getSensorData("EnvironmentalSensor_S3");
	    	
	    	try {
				json2 = new JSONObject(json);
				totalcount=json2.getLong("TotalCount");
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
	    }else{
	    	 int skip=Integer.parseInt(request.getParameter("skip"));
	 	    int limit=Integer.parseInt(request.getParameter("limit"));
	 	    json=mongoDbClient.getSensorData("EnvironmentalSensor_S3",skip,limit);
	 	   
	 	  try {
				json2 = new JSONObject(json);
				totalcount=json2.getLong("TotalCount");
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
	 	 
	    }	
		
		
		
		//System.out.println("!"+json2.toString()+"!"+ "");
		String htmlString="<!DOCTYPE html><html>"
						+ "<head>"
							+ "<style>"
								+ "table {"	
								+ "   border-collapse: collapse;"
								+ "   width: 100%;"
								+ "}"

								+ "th, td {"
								+ "    text-align: left;"
								+ "    padding: 8px;"
								+ "}"
								+ "tr:nth-child(even){background-color: #f2f2f2}"
							+ "</style>"
						+ "</head>"
				+ "<body>";
		try {
				htmlString+="<table border=\"1\">";
				htmlString+="<tr><td colspan=\"11\"> Total Data Count==>";
				htmlString+=totalcount;
				htmlString+="</td></tr>";
				htmlString+="<tr>";
				htmlString+="<th>Date </th>";
				htmlString+="<th>N02</th>";		
				htmlString+="<th>PM10</th>";
				htmlString+="<th>PM2.5</th>";
				htmlString+="<th>Location</th>";
				
				htmlString+="</tr>";
				JSONArray data= json2.getJSONArray("Data");
				for(int i =0;i<data.length();i++){
					htmlString+="<tr>";
					JSONObject jo=data.getJSONObject(i);
					htmlString+="<td>";
					htmlString+=jo.getString("updateDateTime");
					htmlString+="</td>";
					JSONArray recordArray=jo.getJSONArray("records");
						for(int j =0;j<recordArray.length();j++){
							htmlString+="<td>";
							htmlString+=((JSONObject) recordArray.get(j)).getString("value");
							htmlString+="</td>";
						}
					htmlString+="</tr>";
			    }
			htmlString+="</table></body></html>";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//response.setContentType("text/plain");
	   // PrintWriter out = response.getWriter();
		// htmlString="<!DOCTYPE html><html><body><table>";
		/*	htmlString+="<table>";
			htmlString+="<tr>";
					htmlString+="<td>Jill</td>";
							htmlString+="<td>Smith</td>";		
									htmlString+="<td>50</td>";
											htmlString+="</tr></table></body></html>";
*/	    out.write(htmlString);
	}


}
