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
		Dao dao = new Dao();
		ArrayList<Myynti> asiakkaat;
		String strJSON = "";
		if(pathInfo==null) {	
			asiakkaat = dao.listaaKaikki();
			strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();
		} else if (pathInfo.indexOf("haeyksi") != -1) {
			int asiakas_id = Integer.parseInt(pathInfo.replace("/haeyksi/", ""));
			Myynti myynti = dao.etsiAsiakas(asiakas_id);
			if(myynti==null) {
				strJSON = "{}";
			} else {
			JSONObject JSON = new JSONObject();
			JSON.put("asiakas_id", myynti.getAsiakas_id());
			JSON.put("etunimi", myynti.getEtunimi());
			JSON.put("sukunimi", myynti.getSukunimi());
			JSON.put("puhelin", myynti.getPuhelin());
			JSON.put("sposti", myynti.getSposti());
			strJSON = JSON.toString(); }
		} else {
			String hakusana = pathInfo.replace("/", "");
			asiakkaat = dao.listaaKaikki(hakusana);
			strJSON = new JSONObject().put("asiakkaat", asiakkaat).toString();
		}		
		
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
		JSONObject jsonObj = new JsonStrToObj().convert(request);	
		Myynti myynti = new Myynti();
		myynti.setAsiakas_id(Integer.parseInt(jsonObj.getString("asiakas_id")));
		myynti.setEtunimi(jsonObj.getString("etunimi"));
		myynti.setSukunimi(jsonObj.getString("sukunimi"));
		myynti.setPuhelin(jsonObj.getString("puhelin"));
		myynti.setSposti(jsonObj.getString("sposti"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.muutaAsiakas(myynti)){ 
			out.println("{\"response\":1}");  
		}else{
			out.println("{\"response\":0}");  
		}	
		
	}

	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("asiakkaat.doDelete()");
		String pathInfo = request.getPathInfo();	
		System.out.println("polku: "+pathInfo);
		int asiakas_id = Integer.parseInt(pathInfo.replace("/", ""));		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.poistaAsiakas(asiakas_id)){ 
			out.println("{\"response\":1}");  
		}else{
			out.println("{\"response\":0}");  
		}	
	}

}