package be.technifutur.introjakartaee.servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/hello/{\\d}")
public class HelloServlet extends HttpServlet {
   private String message;
    @Override
    public void init() throws ServletException {
        System.out.println("Servelt is being initialized");
         message = "Hello Wolrd!";
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        System.out.println(req.getPathInfo().substring(1));
        req.setAttribute("name", name);
        req.getRequestDispatcher("/pages/hello.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}