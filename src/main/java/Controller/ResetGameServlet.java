package Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/reset")
public class ResetGameServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        // recria tudo
        session.setAttribute("flipped", new ArrayList<Integer>());
        session.setAttribute("matched", new HashSet<Integer>());
        session.setAttribute("score", 0);
        session.setAttribute("combo", 0);
        session.setAttribute("lastAction", "");

        // redireciona de volta para o jogo
        response.sendRedirect("game");
    }
}
