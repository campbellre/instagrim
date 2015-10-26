package uk.ac.dundee.computing.aec.instagrim.servlets;

import org.jboss.netty.handler.codec.http.HttpRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Ryan on 26/10/2015.
 *
 * From : http://stackoverflow.com/a/5188772
 */
@WebServlet(name = "logout", urlPatterns = "/logout")
public class logout extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
