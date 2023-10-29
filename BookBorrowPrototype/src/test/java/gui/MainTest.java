package gui;

import static org.junit.Assert.assertEquals;

import java.util.Map; 

import org.junit.Ignore;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import gui.Main;
import gui.supportclass.AssociationInfo;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class MainTest extends ApplicationTest {
	
	@Override
	public void start(Stage stage) throws Exception {
		new Main().start(stage);
	}

	@Test
	@Ignore
	public void test() {
		//Write Test Script here
	}
}
