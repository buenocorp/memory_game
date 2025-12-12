<%@ page import="java.util.*, Model.Card" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Card> cards = (List<Card>) session.getAttribute("cards");
    List<Integer> flipped = (List<Integer>) session.getAttribute("flipped");
    Set<Integer> matched = (Set<Integer>) session.getAttribute("matched");

    int score = (session.getAttribute("score") != null) ? (int) session.getAttribute("score") : 0;
    int combo = (session.getAttribute("combo") != null) ? (int) session.getAttribute("combo") : 0;
    String lastAction = (String) session.getAttribute("lastAction");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Jogo da Memória SQL</title>

    <style>
        body {
            font-family: "Inter", Arial, sans-serif;
            background: linear-gradient(135deg, #f0f4ff, #dfe9ff);
            color: #333;
            text-align: center;
            padding-bottom: 40px;
        }

        h1 {
            color: #2c3e50;
            font-weight: 700;
            margin-top: 20px;
        }

        .stats {
            margin-top: 15px;
            font-size: 22px;
            color: #2c3e50;
        }

        p {
            font-size: 18px;
            font-weight: 600;
        }

        .board {
            width: 90%;
            margin: auto;
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
            gap: 20px;
            margin-top: 35px;
        }

        /* 🔥 FIX PRINCIPAL */
        .card-container {
            perspective: 1000px;
            width: 140px;
            height: 140px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .card {
            width: 140px;
            height: 140px;
            position: relative;
            transform-style: preserve-3d;
            transition: transform 0.6s;
            cursor: pointer;
            transform-origin: center center;
        }

        .card.flipped { transform: rotateY(180deg); }

        .card-face {
            position: absolute;
            width: 100%;
            height: 100%;
            border-radius: 14px;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 12px;
            text-align: center;
            backface-visibility: hidden;
            font-size: 15px;
            font-weight: 600;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }

        .front {
            background: #ffffff;
            border: 2px solid #d0d7ff;
            color: #1b2a4a;
        }

        .back {
            background: #4fa3ff;
            border: 2px solid #8bc3ff;
            color: #fff;
            transform: rotateY(180deg);
        }

        .matched .back {
            background: #42c97a !important;
            border-color: #9ff7c8;
            color: #fff;
        }

        .reset-btn {
            padding: 12px 22px;
            background: #ff7675;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            margin-top: 25px;
            font-size: 17px;
            font-weight: 600;
            transition: 0.2s;
        }

        .reset-btn:hover {
            background: #ff5252;
            transform: scale(1.05);
        }

        /* 🔥 FIX DO BUTTON */
        .card-button {
            all: unset;
            cursor: pointer;
            width: 140px;
            height: 140px;
            display: block;
        }
    </style>
</head>

<body>

<h1>🧠 Jogo da Memória – SQL Edition</h1>

<div class="stats">
    Pontos: <b><%= score %></b> &nbsp; | &nbsp;
    Combo: <b><%= combo %></b>
</div>

<% if ("correct".equals(lastAction)) { %>
    <p style="color:#00cc88;">✔ Acertou! Combo aumentado!</p>
<% } else if ("wrong".equals(lastAction)) { %>
    <p style="color:#ff4444;">✖ Errou! -20 pontos</p>
<% } %>

<form method="post">
    <input type="hidden" name="action" value="reset"/>
    <button class="reset-btn">🔄 Resetar & Embaralhar</button>
</form>

<div class="board">

<%
    for (int i = 0; i < cards.size(); i++) {
        Card c = cards.get(i);
        boolean isFlipped = flipped.contains(i);
        boolean isMatched = matched.contains(c.getPairId());

        String cardClasses = "card" + (isFlipped || isMatched ? " flipped" : "") + (isMatched ? " matched" : "");
%>

    <div class="card-container">

        <% if (isFlipped || isMatched) { %>

            <div class="<%= cardClasses %>">
                <div class="card-face front"><%= i + 1 %></div>
                <div class="card-face back"><%= c.getText() %></div>
            </div>

        <% } else { %>

            <form method="post">
                <input type="hidden" name="cardIndex" value="<%= i %>"/>
                <button class="card-button">
                    <div class="card">
                        <div class="card-face front"><%= i + 1 %></div>
                        <div class="card-face back"><%= c.getText() %></div>
                    </div>
                </button>
            </form>

        <% } %>

    </div>

<% } %>

</div>

</body>
</html>
