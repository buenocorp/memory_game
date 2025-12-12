package Model;

public class Card {
    private int pairId;
    private String text;
    private boolean answer;

    public Card(int pairId, String text, boolean answer) {
        this.pairId = pairId;
        this.text = text;
        this.answer = answer;
    }

    public int getPairId() { return pairId; }
    public String getText() { return text; }
    public boolean isAnswer() { return answer; }
}
