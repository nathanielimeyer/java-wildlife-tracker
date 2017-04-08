import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class EndangeredAnimal extends Animal implements DatabaseManagement{
  private String health;
  private String age;
  private static final int minimum_sightings = 10;

  public EndangeredAnimal(String name, String health, String age) {
    this.name = name;
    this.id = id;
    this.health = health;
    this.age = age;
  }

  public String getHealth() {
    return health;
  }

  public String getAge() {
    return age;
  }

  public int getMinimumSightings(){
    return minimum_sightings;
  }

  @Override
  public boolean equals(Object otherEndangeredAnimal) {
    if(!(otherEndangeredAnimal instanceof EndangeredAnimal)) {
      return false;
    } else {
      EndangeredAnimal newEndangeredAnimal = (EndangeredAnimal) otherEndangeredAnimal;
      return this.getName().equals(newEndangeredAnimal.getName()) && this.getHealth().equals(newEndangeredAnimal.getHealth()) && this.getAge().equals(newEndangeredAnimal.getAge());
    }
  }

  @Override
  public void save() {
    if (alreadyInDB()) {
      throw new IllegalArgumentException("That animal is already in the DB.");
    } else {
      try(Connection con = DB.sql2o.open()) {
        String sql = "INSERT INTO animals (name, endangered, health, age) VALUES (:name, true, :health, :age);";
        this.id = (int) con.createQuery(sql, true)
          .addParameter("name", this.name)
          .addParameter("health", this.health)
          .addParameter("age", this.age)
          .executeUpdate()
          .getKey();
      }
    }
  }

  @Override
  public Boolean alreadyInDB() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT CASE WHEN EXISTS (SELECT 1 FROM animals WHERE name LIKE :name AND health LIKE :health AND age LIKE :age) THEN 'true' ELSE 'false' END;";
      return con.createQuery(sql)
        .addParameter("name", this.name)
        .addParameter("health", this.health)
        .addParameter("age", this.age)
        .executeAndFetchFirst(Boolean.class);
    }
  }

  public static List<EndangeredAnimal> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id, name, endangered, health, age FROM animals WHERE endangered = true;";
      return con.createQuery(sql)
        .executeAndFetch(EndangeredAnimal.class);
    }
  }

  public static EndangeredAnimal find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id, name, endangered, health, age FROM animals WHERE id=:id;";
      EndangeredAnimal endangeredanimal = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(EndangeredAnimal.class);
      return endangeredanimal;
    }
  }

  public void updateHealth(String health) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE animals SET health=:health WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .addParameter("health", health)
        .executeUpdate();
    }
  }

  public void updateAge(String age) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE animals SET age=:age WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("age", age)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
}
