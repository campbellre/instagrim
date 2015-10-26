package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.CommentWrapper;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.DataException;
import uk.ac.dundee.computing.aec.instagrim.models.Comment;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Created by Ryan on 26/10/2015.
 */
public class Comments extends HttpServlet {

    Cluster cluster = null;

    public void init(ServletConfig config) throws ServletException {
        cluster = CassandraHosts.getCluster();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String args[] = Convertors.SplitRequestPath(request);
        HttpSession session = request.getSession();
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        if (lg == null) {
            response.sendRedirect("401");
        } else {

            String user = lg.getUsername();
            String commentBody = request.getParameter("CommentBody");

            Comment c = new Comment();
            c.setCluster(cluster);
            c.EnterComment(user, commentBody, args[2]);

        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String args[] = Convertors.SplitRequestPath(request);

        Comment c = new Comment();
        c.setCluster(cluster);
        TreeSet<CommentWrapper> comments = null;
        try {
            comments = c.GetComments(args[2]);
        } catch (DataException e) {
            response.sendRedirect("404");
        }

        request.setAttribute("comments", comments);

    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
