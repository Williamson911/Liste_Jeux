package be.technifutur.introjakartaee.servlets.auh;

import be.technifutur.introjakartaee.dao.UserPostgresDAO;
import be.technifutur.introjakartaee.enums.UserRole;
import be.technifutur.introjakartaee.models.User;
import jakarta.servlet.ServletException;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/auth/register")
public class RegisterServlet extends HttpServlet {

    private final UserPostgresDAO userDao = new UserPostgresDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email    = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm  = req.getParameter("confirm");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Email et mot de passe obligatoires.");
            req.getRequestDispatcher("/pages/auth/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirm)) {
            req.setAttribute("error", "Les mots de passe ne correspondent pas.");
            req.getRequestDispatcher("/pages/auth/register.jsp").forward(req, resp);
            return;
        }

        if (userDao.finByEmail(email) != null) {
            req.setAttribute("error", "Un compte existe déjà avec cet email.");
            req.getRequestDispatcher("/pages/auth/register.jsp").forward(req, resp);
            return;
        }

        User user = new User(email, BCrypt.hashpw(password, BCrypt.gensalt()), UserRole.USER);
        userDao.save(user);
        req.getSession(true).setAttribute("user", userDao.finByEmail(email));

        resp.sendRedirect(req.getContextPath() + "/listeJeux");
    }
}
