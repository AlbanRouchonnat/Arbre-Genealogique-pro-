public class Relationship {
    private String _key;
    private String _id;
    private String _rev;
    private String relation;
    private String from;
    private String to;

    // Getters et setters
    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}