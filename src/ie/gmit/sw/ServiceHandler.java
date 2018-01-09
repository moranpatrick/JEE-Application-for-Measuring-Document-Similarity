package ie.gmit.sw;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

/**
 * Service Handler processes Post Requests from user and handles file upload.
 * 
 * @author Patrick Moran
 */

@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, 
                 maxFileSize=1024*1024*10,     
                 maxRequestSize=1024*1024*50)   
public class ServiceHandler extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static long jobNumber = 0;	
	private BlockingQueue<Task> inQ = new ArrayBlockingQueue<Task>(100);
	private BlockingQueue<List<Results>> outQ = new ArrayBlockingQueue<List<Results>>(100);	
	private Worker worker;
	private Task task = null;
	private ShingleWorker shingleWorker;
	private List<Shingle> listShingles = new ArrayList<Shingle>();
	private Document document;
	
	/**
     * Service Handler Constructor initialises The GlobalQueue Class and Starts a new Worker Thread.
     */
	public ServiceHandler(){
		GlobalQueue.init();
		worker = new Worker(inQ, outQ);
		new Thread(worker).start();	
	}
	
	 /**
	  * When the Servlet is first loaded this method is called. Application Wide variables are
	  * initialised here.
	  * 
	  * @exception ServletException 
	  */
	public void init() throws ServletException {
		ServletContext ctx = getServletContext(); 
		Global.setShingleSize(Integer.parseInt(ctx.getInitParameter("SHINGLE_SIZE")));
		Global.setMaxHashes(Integer.parseInt(ctx.getInitParameter("MAX_HASHES")));
		Global.setFilePath(ctx.getInitParameter("FILES_PATH"));
	}

	/** 
	 * Handles HTTP GET requests. Receives a list of Shingles back from the 
	 * ShingleWorker Class, Gets added to a new Document object and then added to the in Queue. 
	 * 
	 * @throws ServeletException 
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Step 1) Write out the MIME type
		resp.setContentType("text/html"); 
		
		//Step 2) Get a handle on the PrintWriter to write out HTML
		PrintWriter out = resp.getWriter(); 
		
		//Step 3) Get any submitted form data. These variables are local to this method and thread safe...
		String title = req.getParameter("txtTitle");
		String taskNumber = req.getParameter("frmTaskNumber");
		Part part = req.getPart("txtDocument");
	
		//Step 4) Process the input and write out the response. 
		out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
		out.print("</head>");		
		out.print("<body>");
		
		if (taskNumber == null){
			taskNumber = new String("T" + jobNumber);
			jobNumber++;
			
		}else{
			RequestDispatcher dispatcher = req.getRequestDispatcher("/poll");
			dispatcher.forward(req,resp);		
		}
		
		//Headings
		out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
		out.print("<H2>Document Title: " + title + "</H2>");
		out.print("<h3>Processing...</h3>");
		
		out.print("<form name=\"frmRequestDetails\" action=\"poll\">");
		out.print("<input name=\"txtTitle\" type=\"hidden\" value=\"" + title + "\">");
		out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
		out.print("</form>");								
		out.print("</body>");	
		out.print("</html>");	
		
		//JavaScript to periodically poll the server for updates (this is ideal for an asynchronous operation)
		out.print("<script>");
		out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 10000);"); //Refresh every 10 seconds
		out.print("</script>");
		
		out.print("<h3>Uploaded Document</h3>");	
		out.print("<font color=\"0000ff\">");	
		
		// Divide the document into shingles
		// 1. Create a new Shingle Worker
		shingleWorker = new ShingleWorker(part, taskNumber);
		// 2. Get a list of shingles from the process shingle method in the shingleWorker Class
		listShingles = shingleWorker.processShingle();
		
		// 3. Create a new document to be handled by the ShingleWorker Thread
		document = new Document(listShingles, taskNumber, title);
		
		// 4. Create A New Task
		task = new Task(document);
		
		// 5. Add That Task to the inq
		GlobalQueue.addToInQueue(task);
		
		out.print("</font>");	
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
}