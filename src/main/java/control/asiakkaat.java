package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import model.Myynti;
import model.dao.Dao;


@WebServlet("/asiakkaat/*")
public class asiakkaat extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public asiakkaat() {
        super();
        System.out.println("asiakkaat.asiakkaat()");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doGet()");
		String pathInfo = request.getPathInfo();		
		System.out.println("polku: "+pathInfo);	
		String hakusana="";
		if(pathInfo!=null) {		
			hakusana = pathInfo.replace("/", "");
		}		
		Dao dao = new Dao();
		ArrayList<Myynti> asiakkaat = dao.listaaKaikki(hakusana);
		System.out.println(asiakkaat);
		String strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();	
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(strJSON);		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("asiakkaat.doPost()");
		JSONObject jsonObj = new JsonStrToObj().convert(request);	
		Myynti myynti = new Myynti();
		myynti.setEtunimi(jsonObj.getString("etunimi"));
		myynti.setSukunimi(jsonObj.getString("sukunimi"));
		myynti.setPuhelin(jsonObj.getString("puhelin"));
		myynti.setSposti(jsonObj.getString("sposti"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.lisaaAsiakas(myynti)){ 
			out.println("{\"response\":1}");  
		}else{
			out.println("{\"response\":0}");  
		}		
	}

	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("asiakkaat.doPut()");
	}

	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("asiakkaat.doDelete()");
		String pathInfo = request.getPathInfo();	
		System.out.println("polku: "+pathInfo);
		String poistettavaAsiakasID = pathInfo.replace("/", "");		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.poistaAsiakas(poistettavaAsiakasID)){ 
			out.println("{\"response\":1}");  
		}else{
			out.println("{\"response\":0}");  
		}	
	}

}