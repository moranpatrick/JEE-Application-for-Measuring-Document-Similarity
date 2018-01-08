package ie.gmit.sw;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * ServicePollHandler Class is responsible for displaying the jaccard results back
 * to the user. 
 * 
 * @author Patrick Moran
 */

public class ServicePollHandler extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private List<Results> listResults = new ArrayList<Results>();
	private BlockingQueue<List<Results>> outQ = new ArrayBlockingQueue<List<Results>>(100);
	
	public void init() throws ServletException {
		
	}

	/** 
	 * Handles HTTP GET requests. Receives a list of results from the 
	 * out queue and displays them to the user.
	 * 
	 * @throws ServeletException 
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html"); 
		PrintWriter out = resp.getWriter(); 
		
		String title = req.getParameter("txtTitle");
		String taskNumber = req.getParameter("frmTaskNumber");
		int counter = 1;
		if (req.getParameter("counter") != null){
			counter = Integer.parseInt(req.getParameter("counter"));
			counter++;
		}
		
		listResults = checkOutQueue(title);
		
		if(listResults != null)
		{
			/*out.printf("<html><head><title>A JEE Application for Measuring Document Similarity</title>");*/
			out.println("<link rel='stylesheet' type='text/css' href='includes/basic.css' />");
			out.print("</p>&nbsp;</p>");
			out.print("<div class=\"animated bounceInDown\" style=\"font-size:32pt; font-family:arial; color:#990000; font-weight:bold\">Results Are In</div>");
		    out.print("</p>&nbsp;</p>");
			out.print("<div class='centered'><table style=\"border: 3px solid black\">");
		    out.print("<tr><th>Uploaded File |</th><th> Files In Database |</th><th> Jaccard Similarity</th></tr>");
		    
		    for (int i = 0; i < listResults.size(); i++) {
				out.print("<tr><td>");
				out.print(listResults.get(i).getNewDoc());
				out.print("</td><td>");
				out.print(listResults.get(i).getOldDoc());
				out.print("</td><td>");
				out.printf("%.0f %%", Double.valueOf(listResults.get(i).getJaccardIndex()) * 100);
				out.print("</td></tr>");
		    }
		    out.println();
		    out.print("</table></div>");

		    out.printf("<p align=\"left\">"
			    + "<button onclick=\"window.location.href='index.jsp'\">Home</button>"
			    + "</p>");
		    out.print("</body></html>");
		}
		else{
			// No Results - Refresh page in 5 seconds
			out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
			out.print("</head>");		
			out.print("<body>");
			out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
			out.print("<H2>Document Title: " + title + "</H2>");
			out.print("<h3>Processing...</h3>");
		
			out.print("<form name=\"frmRequestDetails\">");
			out.print("<input name=\"txtTitle\" type=\"hidden\" value=\"" + title + "\">");
			out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
			out.print("<input name=\"counter\" type=\"hidden\" value=\"" + counter + "\">");
			out.print("</form>");								
			out.print("</body>");	
			out.print("</html>");	
			
			out.print("<script>");
			out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 5000);"); //Refresh every 5 seconds
			out.print("</script>");
		}
	}
	
	/**
	 * Handles HTTP Post Requests.
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
 	}
	
	/**
	 * Polls the out queue for results.
	 * 
	 * @param title
	 * @return List<Results>
	 */
	private List<Results> checkOutQueue(String title){
		// List of Results to be sent back to the doGet()
		List<Results> temp = new ArrayList<Results>();
		
		// Retrieve The OutQ
		outQ = GlobalQueue.getOutQ();
		
		// Loop Over The OutQ
		for(int i = 0;i < outQ.size();i++){
			temp = outQ.peek();
			
			if(temp != null && temp.get(i).getNewDoc().equals(title)){				
				//We have a match add the result to the queue
				temp = outQ.poll();		
			}
			else{
				temp = null;	
			}
		}
		return temp;
	}	
}