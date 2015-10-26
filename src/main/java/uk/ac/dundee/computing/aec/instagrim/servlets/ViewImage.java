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
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by Ryan on 24/10/2015.
 *
 *  Servlet for displaying an image and the details about it. e.g. comments.
 */
@WebServlet(name = "ViewImage", urlPatterns = "/ViewImage/*")
public class ViewImage extends HttpServlet {

    private Cluster cluster = null;

    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String args[] = Convertors.SplitRequestPath(request);
        HttpSession session = request.getSession();
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        String redirect = Default.URL_ROOT;
        if(lg == null)
        {
            redirect += "/401";
        }
        else {
                String user = lg.getUsername();
                String commentBody = request.getParameter("CommentBody");

                Comment c = new Comment();
                c.setCluster(cluster);
                c.EnterComment(user,commentBody,args[2]);

            redirect += "/ViewImage/"+args[2];


        }
        response.sendRedirect(redirect);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String args[] = Convertors.SplitRequestPath(request);

        request.setAttribute("image_url", args[2]);

        Comment c = new Comment();
        c.setCluster(cluster);
        TreeSet<CommentWrapper> comments = null;

        comments = c.GetComments(args[2]);


        Iterator<CommentWrapper> itr = comments.iterator();
        request.setAttribute("comments", itr);


        RequestDispatcher rd = request.getRequestDispatcher("/ViewImage.jsp");
        rd.forward(request,response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
