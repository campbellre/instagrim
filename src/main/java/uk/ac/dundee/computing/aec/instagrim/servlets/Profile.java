/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.User;

import javax.management.modelmbean.RequiredModelMBean;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;


/**
 * @author Ryan
 */
@WebServlet(name = "Profile", urlPatterns = {
        "/Profile",
        "/Profile/*"
})
public class Profile extends HttpServlet {

    private Cluster cluster = null;
    private HashMap CommandsMap = new HashMap();

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // The request contains the URL
        String[] requestPath = Convertors.SplitRequestPath(request);

        displayInfo(requestPath[2],request,response);

        HttpSession session = request.getSession();
        System.out.println("Session in servlet " + session);


    }

    private void displayInfo(String User, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        User u = new User();
        u.setCluster(cluster);
        HashMap<String, String> hm = u.getUserInfo(User);
        RequestDispatcher rd = request.getRequestDispatcher("/Profile.jsp");
        // There is also a getAttributeNames function that may be useful
        request.setAttribute("User", hm.get("Username"));
        request.setAttribute("Firstname", hm.get("FirstName"));
        request.setAttribute("Lastname", hm.get("LastName"));
        if(hm.containsKey("Email0") == true) {
            request.setAttribute("Email", hm.get("Email0"));
        }
        else{
            request.setAttribute("Email","No Value");
        }
        rd.forward(request,response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
