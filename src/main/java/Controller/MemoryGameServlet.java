package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

import Model.Card;
import Model.QuestionPair;

@WebServlet("/game")
public class MemoryGameServlet extends HttpServlet {

    private static final int TOTAL_CARDS = 50;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        // Se ainda não tem as cartas, inicializa
        if (session.getAttribute("cards") == null) {

            List<Card> cards = generateCards();
            Collections.shuffle(cards);

            session.setAttribute("cards", cards);
            session.setAttribute("flipped", new ArrayList<Integer>());
            session.setAttribute("matched", new HashSet<Integer>());
            session.setAttribute("score", 0);
            session.setAttribute("combo", 0);
            session.setAttribute("lastAction", "");
        }

        req.getRequestDispatcher("game.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession();

        String action = req.getParameter("action");

        // RESET
        if ("reset".equals(action)) {
            session.invalidate();
            resp.sendRedirect("game");
            return;
        }

        @SuppressWarnings("unchecked")
        List<Card> cards = (List<Card>) session.getAttribute("cards");

        List<Integer> flipped = (List<Integer>) session.getAttribute("flipped");
        Set<Integer> matched = (Set<Integer>) session.getAttribute("matched");

        int score = (int) session.getAttribute("score");
        int combo = (int) session.getAttribute("combo");

        Boolean lock = (Boolean) session.getAttribute("lock");
        if (lock == null) lock = false;

        // Se está no turno de mostrar as duas cartas -> agora fecha
        if (lock) {
            flipped.clear();
            session.setAttribute("flipped", flipped);
            session.setAttribute("lock", false);
            resp.sendRedirect("game");
            return;
        }

        // Clique no card
        int index = Integer.parseInt(req.getParameter("cardIndex"));
        if (!flipped.contains(index)) {
            flipped.add(index);
        }

        // Primeiro clique → só salvar e voltar
        if (flipped.size() == 1) {
            session.setAttribute("flipped", flipped);
            resp.sendRedirect("game");
            return;
        }

        // Se chegou aqui, são 2 cartas → comparar
        Card c1 = cards.get(flipped.get(0));
        Card c2 = cards.get(flipped.get(1));

        if (c1.getPairId() == c2.getPairId() && c1.isAnswer() != c2.isAnswer()) {
            matched.add(c1.getPairId());
            combo++;
            score += 100 * combo;
            session.setAttribute("lastAction", "correct");
            flipped.clear(); // remove imediatamente porque já sumiram
        } else {
            combo = 0;
            score -= 20;
            session.setAttribute("lastAction", "wrong");
            // manter flipped até o próximo request!
            session.setAttribute("lock", true);
        }

        session.setAttribute("score", score);
        session.setAttribute("combo", combo);
        session.setAttribute("matched", matched);
        session.setAttribute("flipped", flipped);

        resp.sendRedirect("game");
    }


    private List<Card> generateCards() {

        List<Card> cards = new ArrayList<>();

        // Exemplo de perguntas SQL (25 pares => 50 cartas)
        List<QuestionPair> pairs = List.of(
                new QuestionPair("SELECT * FROM tabela", "Retorna todos os registros da tabela"),
                new QuestionPair("INNER JOIN a ON a.id = b.id", "Combina registros com correspondências nas duas tabelas"),
                new QuestionPair("WHERE idade > 18", "Filtra registros"),
                new QuestionPair("ORDER BY nome ASC", "Ordena em ordem crescente"),
                new QuestionPair("GROUP BY categoria", "Agrupa resultados"),
                new QuestionPair("COUNT(*)", "Conta o total de registros"),
                new QuestionPair("LEFT JOIN clientes c", "Traz tudo da esquerda e correspondentes da direita"),
                new QuestionPair("PRIMARY KEY", "Chave única identificadora"),
                new QuestionPair("FOREIGN KEY", "Relaciona duas tabelas"),
                new QuestionPair("ALTER TABLE", "Modifica estrutura de uma tabela"),
                new QuestionPair("INSERT INTO tabela ...", "Insere dados"),
                new QuestionPair("UPDATE tabela SET ...", "Atualiza dados"),
                new QuestionPair("DELETE FROM tabela", "Remove registros"),
                new QuestionPair("HAVING COUNT(*) > 1", "Filtra resultados agrupados"),
                new QuestionPair("CREATE TABLE tabela (...)", "Cria tabela"),
                new QuestionPair("DISTINCT", "Remove duplicados"),
                new QuestionPair("LIMIT 10", "Limita quantidade retornada"),
                new QuestionPair("UNION", "Une resultados de SELECTs"),
                new QuestionPair("BETWEEN 10 AND 50", "Filtra por intervalo"),
                new QuestionPair("LIKE '%abc%'", "Busca por padrão"),
                new QuestionPair("IS NULL", "Testa se é nulo"),
                new QuestionPair("RIGHT JOIN", "Traz tudo da direita"),
                new QuestionPair("FULL JOIN", "Traz tudo de ambos mesmo sem match"),
                new QuestionPair("TRUNCATE TABLE", "Remove tudo e zera IDs"),
                new QuestionPair("DROP TABLE", "Apaga tabela do banco")
        );

        int id = 1;
        for (QuestionPair p : pairs) {
            cards.add(new Card(id, p.question, false));
            cards.add(new Card(id, p.answer, true));
            id++;
        }

        return cards;
    }
}
