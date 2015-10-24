package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Ryan on 24/10/2015.
 *
 *  Servlet for displaying an image and the details about it. e.g. comments.
 */
@WebServlet(name = "ViewImage", urlPatterns = "/ViewImage/*")
public class ViewImage extends HttpServlet {

    Cluster cluster = null;

    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // nothing here yer
//        throw new NotImplementedException();
//    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String args[] = Convertors.SplitRequestPath(request);

        request.setAttribute("image_url", args[2]);

        RequestDispatcher rd = request.getRequestDispatcher("/ViewImage.jsp");
        rd.forward(request,response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
