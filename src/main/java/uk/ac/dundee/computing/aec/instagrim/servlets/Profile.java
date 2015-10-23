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
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.UserDetails;

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

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // The request contains the URL
        String[] requestPath = Convertors.SplitRequestPath(request);

        displayInfo(requestPath[2], request, response);



        HttpSession session = request.getSession();
        System.out.println("Session in servlet " + session);


    }

    private void displayInfo(String sUser, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        User u = new User();
        u.setCluster(cluster);
        sUser = sUser.replaceAll("\\s+", "");
        UserDetails ud = u.getUserInfo(sUser);
        RequestDispatcher rd = request.getRequestDispatcher("/Profile.jsp");
        // There is also a getAttributeNames function that may be useful


        request.setAttribute("Userpath", sUser);
        request.setAttribute("Firstname", ud.getFirstname());
        request.setAttribute("Lastname", ud.getLastname());
        request.setAttribute("Email", ud.getEmail());

        rd.forward(request,response);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
