package gui.supportclass;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;;

public class ClassInfo {

	public ClassInfo(String name, int num) {
		
		this.name = new SimpleStringProperty(name);
		this.number = new SimpleIntegerProperty(num);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public IntegerProperty numberProperty() {
		return number;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public int getNumber() {
		return number.get();
	}

	public void setNumber(int number) {
		this.number.set(number);
	}

	private StringProperty name;
	private IntegerProperty number;
}

