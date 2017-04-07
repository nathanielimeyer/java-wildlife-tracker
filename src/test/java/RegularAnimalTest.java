import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class RegularAnimalTest {
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void animal_instantiatesCorrectly_false() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    assertEquals(true, testRegularAnimal instanceof RegularAnimal);
  }

  @Test
  public void getName_animalInstantiatesWithName_Deer() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    assertEquals("Deer", testRegularAnimal.getName());
  }

  @Test
  public void equals_returnsTrueIfNameIsTheSame_false() {
    RegularAnimal firstRegularAnimal = new RegularAnimal("Deer");
    RegularAnimal anotherRegularAnimal = new RegularAnimal("Deer");
    assertTrue(firstRegularAnimal.equals(anotherRegularAnimal));
  }

  @Test
  public void alreadyInDB_doesntSaveExistingAnimals_true() {
    RegularAnimal firstRegularAnimal = new RegularAnimal("Deer");
    firstRegularAnimal.save();
    RegularAnimal secondRegularAnimal = new RegularAnimal("Deer");
    try { secondRegularAnimal.save(); }
    catch (IllegalArgumentException exception) {}
    assertEquals(1, RegularAnimal.all().size());
  }

  @Test
  public void save_assignsIdToObjectAndSavesObjectToDatabase() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    RegularAnimal savedRegularAnimal = RegularAnimal.all().get(0);
    System.out.println(savedRegularAnimal.getId());
    System.out.println(testRegularAnimal.getId());
    assertEquals(testRegularAnimal.getId(), savedRegularAnimal.getId());
  }

  @Test
  public void all_returnsAllInstancesOfRegularAnimal_false() {
    RegularAnimal firstRegularAnimal = new RegularAnimal("Deer");
    firstRegularAnimal.save();
    RegularAnimal secondRegularAnimal = new RegularAnimal("Black Bear");
    secondRegularAnimal.save();
    assertEquals(true, RegularAnimal.all().get(0).equals(firstRegularAnimal));
    assertEquals(true, RegularAnimal.all().get(1).equals(secondRegularAnimal));
  }

  @Test
  public void find_returnsRegularAnimalWithSameId_secondRegularAnimal() {
    RegularAnimal firstRegularAnimal = new RegularAnimal("Deer");
    firstRegularAnimal.save();
    RegularAnimal secondRegularAnimal = new RegularAnimal("Black Bear");
    secondRegularAnimal.save();
    assertEquals(RegularAnimal.find(secondRegularAnimal.getId()), secondRegularAnimal);
  }

  @Test
  public void delete_deletesRegularAnimalFromDatabase_0() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    testRegularAnimal.delete();
    assertEquals(0, RegularAnimal.all().size());
  }

  public void updateName_updatesRegularAnimalNameInDatabase_String() {
    RegularAnimal testRegularAnimal = new RegularAnimal("Deer");
    testRegularAnimal.save();
    testRegularAnimal.updateName("Buck");
    assertEquals("Buck", testRegularAnimal.getName());
  }

  @Test
  public void find_returnsNullWhenNoRegularAnimalFound_null() {
    assertTrue(RegularAnimal.find(999) == null);
  }

  @Test
  public void getSightings_returnsAllSightingsOfThisRegularAnimal_true() {
    RegularAnimal firstRegularAnimal = new RegularAnimal("Deer");
    firstRegularAnimal.save();
    Sighting firstSighting = new Sighting (firstRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    firstSighting.save();
    RegularAnimal secondTestRegularAnimal = new RegularAnimal("Badger");
    secondTestRegularAnimal.save();
    Sighting secondTestSighting = new Sighting (secondTestRegularAnimal.getId(), "45.472428, -121.946466", "Ranger Reese");
    secondTestSighting.save();
    Sighting thirdTestSighting = new Sighting (firstRegularAnimal.getId(), "44.472428, -122.946466", "Ranger Reese");
    thirdTestSighting.save();
    List<Sighting> deerSightings = firstRegularAnimal.getSightings();
    List<Sighting> badgerSightings = secondTestRegularAnimal.getSightings();
    assertTrue(deerSightings.get(0).equals(firstSighting));
    assertTrue(badgerSightings.get(0).equals(secondTestSighting));
    assertTrue(deerSightings.get(1).equals(thirdTestSighting));
  }
}
