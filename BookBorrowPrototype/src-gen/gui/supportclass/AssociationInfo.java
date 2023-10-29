package gui.supportclass;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.lang.reflect.Method;
import java.util.List;
import entities.EntityManager;
		
public class AssociationInfo {
	
	public AssociationInfo(String sourceClass, String targetClass, String associationName, boolean isMultiple) {
		
		this.sourceClass = new SimpleStringProperty(sourceClass);
		this.targetClass = new SimpleStringProperty(targetClass);
		this.associationName = new SimpleStringProperty(associationName);
		this.isMultiple = new SimpleBooleanProperty(isMultiple);
		this.number = new SimpleIntegerProperty(0);
	}
	
	public void computeAssociationNumber() {
		
		int num = 0;
		List allObject = EntityManager.getAllInstancesOf(sourceClass.getValue());
		//System.out.println(sourceClass.getValue() + " has " + String.valueOf(allObject.size()) + " objects");
		for (Object c : allObject) {
			
			try {
				Method getAssociationMethod = c.getClass().getMethod("get" + associationName.getValue());
				if (isMultiple.getValue() == true) {
					num = num + ((List) getAssociationMethod.invoke(c)).size();
				}	
				else {
					if (getAssociationMethod.invoke(c) != null) {
						num++;
					}
				}
			}
			catch (Exception e) {
				
			}
		}
		
		number.set(num);
		
	}
	
	public void computeAssociationNumber(int i) {
		
		int num = 0;
		
		List allObject = EntityManager.getAllInstancesOf(sourceClass.getValue());
		Object c = allObject.get(i);
			
		try {
			Method getAssociationMethod = c.getClass().getMethod("get" + associationName.getValue());
			if (isMultiple.getValue() == true) {
				num = num + ((List) getAssociationMethod.invoke(c)).size();
			}	
			else {
				if (getAssociationMethod.invoke(c) != null) {
					num++;
				}
			}
		}
		catch (Exception e) {
			
		}
		
		number.set(num);
		
	}
	
	public StringProperty sourceClassProperty() {
		return sourceClass;
	}
	
	public void setSourceClass(String source) {
		this.sourceClass.set(source);
	}
	
	public String getSourceClass() {
		return sourceClass.get();
	}
	
	public StringProperty targetClassProperty() {
		return targetClass;
	}
	
	public void setTargetClass(String target) {
		this.targetClass.set(target);
	}
	
	public String getTargetClass() {
		return targetClass.get();
	}
	
	public StringProperty associationNameProperty() {
		return associationName;
	}
	
	public void setAssociationName(String assoc) {
		associationName.set(assoc);
	}
	
	public String getAssociationName() {
		return associationName.get();
	}
	
	public BooleanProperty isMultipleProperty() {
		return isMultiple;
	}
	
	public void setIsMultiple(boolean ismultiple) {
		isMultiple.set(ismultiple);
	}
	
	public boolean getIsMultiple() {
		return isMultiple.get();
	}

	public IntegerProperty numberProperty() {
		return number;
	}

	public int getNumber() {
		return number.get();
	}

	public void setNumber(int number) {
		this.number.set(number);
	}

	private StringProperty sourceClass;
	private StringProperty targetClass;
	private StringProperty associationName;
	private BooleanProperty isMultiple;
	private IntegerProperty number;

}

