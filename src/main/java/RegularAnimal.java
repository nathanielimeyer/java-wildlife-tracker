import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class RegularAnimal extends Animal implements DatabaseManagement{

  public RegularAnimal(String name) {
    this.name = name;
    this.id = id;
  }

  @Override
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO animals (name, endangered) VALUES (:name, false);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<RegularAnimal> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id, name FROM animals WHERE endangered = false;";
      return con.createQuery(sql)
        .executeAndFetch(RegularAnimal.class);
    }
  }

  public static RegularAnimal find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT id, name FROM animals WHERE id=:id;";
      RegularAnimal animal = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(RegularAnimal.class);
      return animal;
    }
  }
}
