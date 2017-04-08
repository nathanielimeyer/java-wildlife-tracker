import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class EndangeredAnimalTest {
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void endangeredAnimal_instantiatesCorrectly_true() {
    EndangeredAnimal testEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    assertEquals(true, testEndangeredAnimal instanceof EndangeredAnimal);
  }

  @Test
  public void getHealth_returnsHealthAttribute_true() {
    EndangeredAnimal testEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    assertEquals("Healthy", testEndangeredAnimal.getHealth());
  }

  @Test
  public void getAge_returnsAgeAttribute_true() {
    EndangeredAnimal testEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    assertEquals("Young", testEndangeredAnimal.getAge());
  }

  @Test
  public void alreadyInDB_doesntSaveExistingAnimals_true() {
    EndangeredAnimal testEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    try { testEndangeredAnimal.save(); }
    catch (IllegalArgumentException exception) {}
    EndangeredAnimal secondEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    try { secondEndangeredAnimal.save(); }
    catch (IllegalArgumentException exception) {}
    assertEquals(1, EndangeredAnimal.all().size());
    EndangeredAnimal thirdEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Old");
    try { thirdEndangeredAnimal.save(); }
    catch (IllegalArgumentException exception) {}
    assertEquals(2, EndangeredAnimal.all().size());
  }

  @Test
  public void getName_returnsNameAttribute_true() {
    EndangeredAnimal testEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    assertEquals("Fox", testEndangeredAnimal.getName());
  }

  @Test
  public void save_assignsIdAndSavesObjectToDatabase() {
    EndangeredAnimal testEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    testEndangeredAnimal.save();
    EndangeredAnimal savedEndangeredAnimal = EndangeredAnimal.all().get(0);
    assertEquals(testEndangeredAnimal.getId(), savedEndangeredAnimal.getId());
  }

  @Test
  public void all_returnsAllInstancesOfEndangeredAnimal_true() {
    EndangeredAnimal firstEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    firstEndangeredAnimal.save();
    EndangeredAnimal secondEndangeredAnimal = new EndangeredAnimal("Badger", "Okay", "Adult");
    secondEndangeredAnimal.save();
    assertEquals(true, EndangeredAnimal.all().get(0).equals(firstEndangeredAnimal));
    assertEquals(true, EndangeredAnimal.all().get(1).equals(secondEndangeredAnimal));
  }

  @Test
  public void find_returnsAnimalWithSameId_secondAnimal() {
    EndangeredAnimal firstEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    firstEndangeredAnimal.save();
    EndangeredAnimal secondEndangeredAnimal = new EndangeredAnimal("Badger", "Okay", "Adult");
    secondEndangeredAnimal.save();
    assertEquals(EndangeredAnimal.find(secondEndangeredAnimal.getId()), secondEndangeredAnimal);
  }

  @Test
  public void update_updatesHealthAttribute_true() {
    EndangeredAnimal testEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    testEndangeredAnimal.save();
    testEndangeredAnimal.updateHealth("ill");
    assertEquals("ill", EndangeredAnimal.find(testEndangeredAnimal.getId()).getHealth());
  }

  @Test
  public void update_updatesAgeAttribute_true() {
    EndangeredAnimal testEndangeredAnimal = new EndangeredAnimal("Fox", "Healthy", "Young");
    testEndangeredAnimal.save();
    testEndangeredAnimal.updateAge("Adult");
    assertEquals("Adult", EndangeredAnimal.find(testEndangeredAnimal.getId()).getAge());
  }

  @Test
  public void getSightings_returnsAllSightingsOfThisAnimal_true() {
    EndangeredAnimal firstEndangeredAnimal = new EndangeredAnimal("Gray wolf", "Healthy", "Young");
    firstEndangeredAnimal.save();
    Sighting firstSighting = new Sighting (firstEndangeredAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    firstSighting.save();
    EndangeredAnimal secondTestEndangeredAnimal = new EndangeredAnimal("Loggerhead sea turtle", "Ill", "Old");
    secondTestEndangeredAnimal.save();
    Sighting secondTestSighting = new Sighting (secondTestEndangeredAnimal.getId(), "45.472428, -121.946466", "Ranger Reese");
    secondTestSighting.save();
    Sighting thirdTestSighting = new Sighting (firstEndangeredAnimal.getId(), "44.472428, -122.946466", "Ranger Reese");
    thirdTestSighting.save();
    List<Sighting> wolfSightings = firstEndangeredAnimal.getSightings();
    List<Sighting> turtleSightings = secondTestEndangeredAnimal.getSightings();
    assertTrue(wolfSightings.get(0).equals(firstSighting));
    assertTrue(turtleSightings.get(0).equals(secondTestSighting));
    assertTrue(wolfSightings.get(1).equals(thirdTestSighting));
  }

  @Test
  public void minimumSightingsConstant() {
    EndangeredAnimal firstEndangeredAnimal = new EndangeredAnimal("Gray wolf", "Healthy", "Young");
    firstEndangeredAnimal.save();
    assertEquals(10, firstEndangeredAnimal.getMinimumSightings());
  }

}
