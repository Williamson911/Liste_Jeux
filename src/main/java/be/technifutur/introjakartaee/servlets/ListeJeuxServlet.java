package be.technifutur.introjakartaee.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/listeJeux")
public class ListeJeuxServlet extends HttpServlet {

    private List<Jeux> listeJeux; // ✅ List<Jeux> et non List<String>

    @Override
    public void init() throws ServletException {
        listeJeux = new ArrayList<>();
        listeJeux.add(new Jeux(1, "Demon's Souls",   2009, "Le premier souls, sombre et impitoyable.", "/images/DemonsSouls.jpg"));

        listeJeux.add(new Jeux(2, "Dark Souls",      2011, "Un classique culte, interconnecté et mystérieux.", "/images/DarkSouls.jpg"));
        listeJeux.add(new Jeux(3, "Dark Souls II",   2014, "Plus grand, plus difficile, plus controversé.", "/images/DarkSouls2.jpg"));
        listeJeux.add(new Jeux(4, "Bloodborne",      2015, "Ambiance gothique lovecraftienne, combat agressif.", "/images/bloodborne.jpg"));
        listeJeux.add(new Jeux(5, "Dark Souls III",  2016, "La conclusion épique de la trilogie.", "/images/DarkSouls3.jpg"));
        listeJeux.add(new Jeux(6, "Sekiro",          2019, "Samouraï, parry et boss brutaux.", "/images/Sekiro.jpg"));
        listeJeux.add(new Jeux(7, "Elden Ring",      2022, "Open world épique co-écrit avec G.R.R. Martin.", "/images/eldenring.jpg"));
        // Stocke la liste dans le contexte pour la partager avec detailJeuServelt
        getServletContext().setAttribute("listeJeux", listeJeux);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("listeJeux", listeJeux);
        req.getRequestDispatcher("/pages/listeJeux.jsp").forward(req, resp);
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
//    }

    @Override
    public void destroy() {
        super.destroy();
    }
}