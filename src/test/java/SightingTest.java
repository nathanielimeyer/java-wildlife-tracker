import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DateFormat;
import java.util.Date;
import java.sql.Timestamp;

public class SightingTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void sighting_instantiatesCorrectly_true() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    Sighting testSighting = new Sighting(testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertEquals(true, testSighting instanceof Sighting);
  }

  @Test
  public void getRegularAnimalId_returnsRegularAnimalId_int() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    Sighting testSighting = new Sighting(testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertEquals(testRegularAnimal.getId(), testSighting.getAnimalId());
  }

  @Test
  public void getLocation_returnsLocation_String() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    Sighting testSighting = new Sighting(testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertTrue(testSighting.getLocation().equals("45.472428, -121.946466"));
  }

  @Test
  public void getRangerName_returnsRangerName_String() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    Sighting testSighting = new Sighting(testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertTrue(testSighting.getRangerName().equals("Ranger Avery"));
  }

  @Test
  public void save_recordsTimestampInDatabase() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    Sighting testSighting = new Sighting(testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
    Timestamp testSightingTimestamp = Sighting.find(testSighting.getId()).getTimestamp();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(rightNow.getDay(), testSightingTimestamp.getDay());
  }

  @Test
  public void equals_returnsTrueIfLocationAndDescriptionAreSame_true() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    Sighting testSighting = new Sighting(testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    Sighting anotherSighting = new Sighting(testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertTrue(testSighting.equals(anotherSighting));
  }

  @Test
  public void save_insertsObjectIntoDatabase_Sighting() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    Sighting testSighting = new Sighting (testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
    assertEquals(true, Sighting.all().get(0).equals(testSighting));
  }

  @Test
  public void all_returnsAllInstancesOfSighting_true() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    Sighting testSighting = new Sighting (testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
    RegularAnimal secondTestRegularAnimal = new RegularAnimal("Badger");
    secondTestRegularAnimal.save();
    Sighting secondTestSighting = new Sighting (testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Reese");
    secondTestSighting.save();
    assertEquals(true, Sighting.all().get(0).equals(testSighting));
    assertEquals(true, Sighting.all().get(1).equals(secondTestSighting));
  }

  @Test
  public void find_returnsSightingWithSameId_secondSighting() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    Sighting testSighting = new Sighting (testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
    RegularAnimal secondTestRegularAnimal = new RegularAnimal("Badger");
    secondTestRegularAnimal.save();
    Sighting secondTestSighting = new Sighting (testRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Reese");
    secondTestSighting.save();
    assertEquals(Sighting.find(secondTestSighting.getId()), secondTestSighting);
  }

  @Test
  public void find_returnsNullWhenNoRegularAnimalFound_null() {
    assertTrue(RegularAnimal.find(999) == null);
  }

}
