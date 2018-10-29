package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pinda.HierarchicalCluster;
import pinda.Pinda;

import com.google.gson.Gson;

/**
 * Servlet implementation class Servlet
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Servlet" })
public class Servlet extends HttpServlet {
	
	private static final long serialVersionUID = -2099634213603977903L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		PrintWriter out = response.getWriter();
		Pinda p = new Pinda();
		Gson gson = new Gson();
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		
		if (request == null || request.getParameter("matrix") == null || request.getParameter("matrix").isEmpty()) out.print(gson.toJson("null"));
		else out.print(gson.toJson(p.group(request.getParameter("matrix"))));
		
		out.close();
	}

}
