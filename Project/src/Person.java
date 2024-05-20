import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private int age;
    private List<Person> children = new ArrayList<>();
    private Person spouse;

    // Constructeur
    public Person() {
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Person> getChildren() {
        return children;
    }

    // Méthode pour ajouter un enfant
    public void addChild(Person child) {
        this.children.add(child);
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    // Surcharge de toString pour afficher les informations de la personne
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", spouse=" + (spouse != null ? spouse.getName() : "none") +
                ", children=" + children.size() +
                '}';
    }
}
