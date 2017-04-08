import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Animal {
  public String name;
  public int id;
  public boolean endangered;

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public boolean getEndangered(){
    return endangered;
  }

  @Override
  public boolean equals(Object otherAnimal) {
    if(!(otherAnimal instanceof Animal)) {
      return false;
    } else {
      Animal newAnimal = (Animal) otherAnimal;
      return this.getName().equals(newAnimal.getName());
    }
  }

  public void updateName(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE animals SET name=:name WHERE id=:id;";
      con.createQuery(sql)
        .addParameter("id", id)
        .addParameter("name", name)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sightingsSql = "DELETE FROM sightings WHERE animal_id=:animal_id;";
      con.createQuery(sightingsSql)
        .addParameter("animal_id", id)
        .executeUpdate();
      String animalsSql = "DELETE FROM animals WHERE id=:id;";
      con.createQuery(animalsSql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public List<Sighting> getSightings() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings WHERE animal_id=:id;";
        List<Sighting> sightings = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetch(Sighting.class);
      return sightings;
    }
  }

  public Boolean alreadyInDB() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT CASE WHEN EXISTS (SELECT 1 FROM animals WHERE name LIKE :name) THEN 'true' ELSE 'false' END;";
      return con.createQuery(sql)
        .addParameter("name", this.name)
        .executeAndFetchFirst(Boolean.class);
    }
  }
}
