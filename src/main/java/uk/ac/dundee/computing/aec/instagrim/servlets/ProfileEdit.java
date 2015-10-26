package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Default;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.UserDetails;

import javax.jws.soap.SOAPBinding;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeSet;

/**
 * Created by Ryan on 22/10/2015.
 *
 *  Servlet class for editing the users profile details.
 */
@WebServlet(name = "ProfileEdit", urlPatterns = {
        "/ProfileEdit",
        "/ProfileEdit/*"
})
public class ProfileEdit extends HttpServlet{

    private Cluster cluster = null;

    public void init(ServletConfig config) throws ServletException
    {
        cluster = CassandraHosts.getCluster();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // get the infomation to populate the populate the page with
        displayInfo(request, response);

    }

    private void displayInfo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        User u = new User();
        u.setCluster(cluster);

        HttpSession session = request.getSession();
        UserDetails ud;

        ud = u.getUserInfo(((LoggedIn) session.getAttribute("LoggedIn")).getUsername());

        session.setAttribute("UserDetails",ud);

        RequestDispatcher rd = request.getRequestDispatcher("/ProfileEdit.jsp");
        rd.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        UserDetails ud = new UserDetails();

        HttpSession session = request.getSession();

        String username = ((LoggedIn) session.getAttribute("LoggedIn")).getUsername();

        ud.setLogin(username);
        ud.setFirstname(request.getParameter("firstname"));
        ud.setLastname(request.getParameter("lastname"));
        ud.addEmail(request.getParameter("email"));

        User u = new User();
        u.setCluster(cluster);

        u.setUserDetails(ud);

        response.sendRedirect(Default.URL_ROOT+"/Profile/"+ username);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
