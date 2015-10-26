package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.CommentWrapper;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.Default;
import uk.ac.dundee.computing.aec.instagrim.models.Comment;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String args[] = Convertors.SplitRequestPath(request);
        HttpSession session = request.getSession();
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        if(lg == null)
        {
            // Error message
        }
        else {
            if(lg.getlogedin()) {
                String user = lg.getUsername();
                String commentBody = request.getParameter("CommentBody");

                Comment c = new Comment();
                c.setCluster(cluster);
                c.EnterComment(user,commentBody,args[2]);

            }
            else{
                // Error message
            }


        }
        response.sendRedirect(Default.URL_ROOT+"/ViewImage/"+args[2]);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String args[] = Convertors.SplitRequestPath(request);

        request.setAttribute("image_url", args[2]);

        Comment c = new Comment();
        c.setCluster(cluster);
        TreeSet<CommentWrapper> comments = c.GetComments(args[2]);
        Iterator<CommentWrapper> it = comments.iterator();

        request.setAttribute("commentsi", it);


        RequestDispatcher rd = request.getRequestDispatcher("/ViewImage.jsp");
        rd.forward(request,response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
