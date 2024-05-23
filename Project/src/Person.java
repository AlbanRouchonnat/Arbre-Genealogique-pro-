import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Person {
    private String name;
    private int age;
    private List<Person> children = new ArrayList<>();
    private Person spouse;
    private List<Person> parents = new ArrayList<>();

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

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

    public void addChild(Person child) {
        this.children.add(child);
        child.addParent(this);
    }

    public List<Person> getParents() {
        return parents;
    }

    public void addParent(Person parent) {
        if (!this.parents.contains(parent)) {
            this.parents.add(parent);
        }
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public void marry(Person person) {
        if (this.spouse == null && person.spouse == null) {
            this.spouse = person;
            person.spouse = this;
        }
    }


    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", spouse=" + (spouse != null ? spouse.getName() : "none") +
                ", children=" + children.stream().map(Person::getName).collect(Collectors.toList()) +
                ", parents=" + parents.stream().map(Person::getName).collect(Collectors.toList()) +
                '}';
    }
}

