package tdt4140.gr1816.app.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.ListViewMatchers;

public class UserGUITest extends ApplicationTest {

  @BeforeClass
  public static void headless() {
    if (Boolean.valueOf(System.getProperty("gitlab-ci", "false"))) {
      System.setProperty("prism.verbose", "true"); // optional
      System.setProperty("java.awt.headless", "true");
      System.setProperty("testfx.robot", "glass");
      System.setProperty("testfx.headless", "true");
      System.setProperty("glass.platform", "Monocle");
      System.setProperty("monocle.platform", "Headless");
      System.setProperty("prism.order", "sw");
      System.setProperty("prism.text", "t2k");
      System.setProperty("testfx.setup.timeout", "10000");
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("UserGUI.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
  
  private ListView doctorList;
  private ObservableList<String> doctorItems;
  
  @Test
  public void testDataButton() {
	clickOn("#profileTab");
    // Check if databutton is on by defult
    Button dataButton = lookup("#dataButton").query();
    assertEquals("Turn off", dataButton.getText());
    // Click button and check of text changes
    clickOn(dataButton);
    assertEquals("Turn on", dataButton.getText());
    clickOn(dataButton);
    assertEquals("Turn off", dataButton.getText());
  }

  @Test
  public void testDoctorRemoval() {
	clickOn("#profileTab");
    doctorList = lookup("#doctorsListView").query();
    doctorItems = doctorList.getItems();
    Button removeDoctorButton = lookup("#removeDoctorButton").query();

    String doctor = doctorItems.get(0);
    // doctorItems.add(doctor);
    // Check if list has doctor
    assertTrue(doctorItems.contains(doctor));
    // Remove doctor
    doctorList.getSelectionModel().select(doctor);
    clickOn(removeDoctorButton);
    // Check if doctor is removed
    assertFalse(doctorItems.contains(doctor));
  }
  
  @Test
  public void testDeleteDataButton() {
	  clickOn("#sleepTab");
	  Button deleteButton = lookup("#deleteDataButton").query();
	  ListView dataList = lookup("#dataListView").query();
	  ObservableList<String> dataItems = dataList.getItems();
	  
	  String data = dataItems.get(0);
	  
	  assertTrue(dataItems.contains(data));
	  dataList.getSelectionModel().select(data);
	  clickOn(deleteButton);
	  assertFalse(dataItems.contains(data));
	  
  }
  
  @Test
  public void testAcceptDoctor() {
	  clickOn("#doctorTab");
	  Button acceptButton = lookup("#acceptDoctorButton").query();
	  ListView doctorRequestList = lookup("#doctorRequestListView").query();
	  ObservableList<String> doctorRequestItems = doctorRequestList.getItems();
	  
	  String doctor = doctorRequestItems.get(0);
	  
	  assertTrue(doctorRequestItems.contains(doctor));
	  doctorRequestList.getSelectionModel().select(doctor);
	  clickOn(acceptButton);
	  assertFalse(doctorRequestItems.contains(doctor));
	  
  }
}
