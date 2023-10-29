package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Tooltip;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.time.LocalDate;
import java.util.LinkedList;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import gui.supportclass.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import services.*;
import services.impl.*;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Method;

import entities.*;

public class PrototypeController implements Initializable {


	DateTimeFormatter dateformatter;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		bookborrowsystem_service = ServiceManager.createBookBorrowSystem();
		thirdpartyservices_service = ServiceManager.createThirdPartyServices();
		registerservice_service = ServiceManager.createRegisterService();
		loginservice_service = ServiceManager.createLoginService();
		searchbookservice_service = ServiceManager.createSearchBookService();
		borrowbookservice_service = ServiceManager.createBorrowBookService();
		returnbookservice_service = ServiceManager.createReturnBookService();
		manageusercrudservice_service = ServiceManager.createManageUserCRUDService();
		managebookcrudservice_service = ServiceManager.createManageBookCRUDService();
		managebookborrowcrudservice_service = ServiceManager.createManageBookBorrowCRUDService();
				
		this.dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
	   	 //prepare data for contract
	   	 prepareData();
	   	 
	   	 //generate invariant panel
	   	 genereateInvairantPanel();
	   	 
		 //Actor Threeview Binding
		 actorTreeViewBinding();
		 
		 //Generate
		 generatOperationPane();
		 genereateOpInvariantPanel();
		 
		 //prilimariry data
		 try {
			DataFitService.fit();
		 } catch (PreconditionException e) {
			// TODO Auto-generated catch block
		 	e.printStackTrace();
		 }
		 
		 //generate class statistic
		 classStatisicBingding();
		 
		 //generate object statistic
		 generateObjectTable();
		 
		 //genereate association statistic
		 associationStatisicBingding();

		 //set listener 
		 setListeners();
	}
	
	/**
	 * deepCopyforTreeItem (Actor Generation)
	 */
	TreeItem<String> deepCopyTree(TreeItem<String> item) {
		    TreeItem<String> copy = new TreeItem<String>(item.getValue());
		    for (TreeItem<String> child : item.getChildren()) {
		        copy.getChildren().add(deepCopyTree(child));
		    }
		    return copy;
	}
	
	/**
	 * check all invariant and update invariant panel
	 */
	public void invairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}				
			}
			
			for (Entry<String, Label> inv : service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * check op invariant and update op invariant panel
	 */		
	public void opInvairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : op_entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
			for (Entry<String, Label> inv : op_service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 
	*	generate op invariant panel 
	*/
	public void genereateOpInvariantPanel() {
		
		opInvariantPanel = new HashMap<String, VBox>();
		op_entity_invariants_label_map = new LinkedHashMap<String, Label>();
		op_service_invariants_label_map = new LinkedHashMap<String, Label>();
		
		VBox v;
		List<String> entities;
		v = new VBox();
		
		//entities invariants
		entities = RegisterServiceImpl.opINVRelatedEntity.get("inputUser");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("inputUser" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("RegisterService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("inputUser", v);
		
		v = new VBox();
		
		//entities invariants
		entities = RegisterServiceImpl.opINVRelatedEntity.get("verification");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("verification" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("RegisterService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("verification", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LoginServiceImpl.opINVRelatedEntity.get("inputUsername");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("inputUsername" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LoginService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("inputUsername", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LoginServiceImpl.opINVRelatedEntity.get("inputPassword");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("inputPassword" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LoginService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("inputPassword", v);
		
		v = new VBox();
		
		//entities invariants
		entities = SearchBookServiceImpl.opINVRelatedEntity.get("login");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("login" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("SearchBookService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("login", v);
		
		v = new VBox();
		
		//entities invariants
		entities = SearchBookServiceImpl.opINVRelatedEntity.get("inputtitle");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("inputtitle" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("SearchBookService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("inputtitle", v);
		
		v = new VBox();
		
		//entities invariants
		entities = SearchBookServiceImpl.opINVRelatedEntity.get("brows");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("brows" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("SearchBookService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("brows", v);
		
		v = new VBox();
		
		//entities invariants
		entities = SearchBookServiceImpl.opINVRelatedEntity.get("selectbook");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("selectbook" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("SearchBookService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("selectbook", v);
		
		v = new VBox();
		
		//entities invariants
		entities = BorrowBookServiceImpl.opINVRelatedEntity.get("choosebook");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("choosebook" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("BorrowBookService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("choosebook", v);
		
		v = new VBox();
		
		//entities invariants
		entities = BorrowBookServiceImpl.opINVRelatedEntity.get("selecttime");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("selecttime" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("BorrowBookService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("selecttime", v);
		
		v = new VBox();
		
		//entities invariants
		entities = BorrowBookServiceImpl.opINVRelatedEntity.get("confirm");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("confirm" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("BorrowBookService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("confirm", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ReturnBookServiceImpl.opINVRelatedEntity.get("inputuser");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("inputuser" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ReturnBookService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("inputuser", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ReturnBookServiceImpl.opINVRelatedEntity.get("inputbook");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("inputbook" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ReturnBookService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("inputbook", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ReturnBookServiceImpl.opINVRelatedEntity.get("confirminformation");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("confirminformation" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ReturnBookService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("confirminformation", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageUserCRUDServiceImpl.opINVRelatedEntity.get("createUser");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createUser" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageUserCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createUser", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageUserCRUDServiceImpl.opINVRelatedEntity.get("queryUser");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryUser" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageUserCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryUser", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageUserCRUDServiceImpl.opINVRelatedEntity.get("modifyUser");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyUser" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageUserCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyUser", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageUserCRUDServiceImpl.opINVRelatedEntity.get("deleteUser");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteUser" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageUserCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteUser", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBookCRUDServiceImpl.opINVRelatedEntity.get("createBook");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createBook" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBookCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createBook", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBookCRUDServiceImpl.opINVRelatedEntity.get("queryBook");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryBook" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBookCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryBook", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBookCRUDServiceImpl.opINVRelatedEntity.get("deleteBook");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteBook" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBookCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteBook", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBookCRUDServiceImpl.opINVRelatedEntity.get("modifyBook");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyBook" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBookCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyBook", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBookBorrowCRUDServiceImpl.opINVRelatedEntity.get("createBookBorrow");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createBookBorrow" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBookBorrowCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createBookBorrow", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBookBorrowCRUDServiceImpl.opINVRelatedEntity.get("queryBookBorrow");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryBookBorrow" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBookBorrowCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryBookBorrow", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBookBorrowCRUDServiceImpl.opINVRelatedEntity.get("deleteBookBorrow");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteBookBorrow" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBookBorrowCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteBookBorrow", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageBookBorrowCRUDServiceImpl.opINVRelatedEntity.get("modifyBookBorrow");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyBookBorrow" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageBookBorrowCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyBookBorrow", v);
		
		
	}
	
	
	/*
	*  generate invariant panel
	*/
	public void genereateInvairantPanel() {
		
		service_invariants_label_map = new LinkedHashMap<String, Label>();
		entity_invariants_label_map = new LinkedHashMap<String, Label>();
		
		//entity_invariants_map
		VBox v = new VBox();
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			Label l = new Label(inv.getKey());
			l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			service_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		//entity invariants
		for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
			
			String INVname = inv.getKey();
			Label l = new Label(INVname);
			if (INVname.contains("AssociationInvariants")) {
				l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #099b17 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			} else {
				l.setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
			}	
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			entity_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		ScrollPane scrollPane = new ScrollPane(v);
		scrollPane.setFitToWidth(true);
		all_invariant_pane.setMaxHeight(850);
		
		all_invariant_pane.setContent(scrollPane);
	}	
	
	
	
	/* 
	*	mainPane add listener
	*/
	public void setListeners() {
		 mainPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			 
			 	if (newTab.getText().equals("System State")) {
			 		System.out.println("refresh all");
			 		refreshAll();
			 	}
		    
		    });
	}
	
	
	//checking all invariants
	public void checkAllInvariants() {
		
		invairantPanelUpdate();
	
	}	
	
	//refresh all
	public void refreshAll() {
		
		invairantPanelUpdate();
		classStatisticUpdate();
		generateObjectTable();
	}
	
	
	//update association
	public void updateAssociation(String className) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber();
		}
		
	}
	
	public void updateAssociation(String className, int index) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber(index);
		}
		
	}	
	
	public void generateObjectTable() {
		
		allObjectTables = new LinkedHashMap<String, TableView>();
		
		TableView<Map<String, String>> tableUser = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableUser_UserId = new TableColumn<Map<String, String>, String>("UserId");
		tableUser_UserId.setMinWidth("UserId".length()*10);
		tableUser_UserId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserId"));
		    }
		});	
		tableUser.getColumns().add(tableUser_UserId);
		TableColumn<Map<String, String>, String> tableUser_Username = new TableColumn<Map<String, String>, String>("Username");
		tableUser_Username.setMinWidth("Username".length()*10);
		tableUser_Username.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Username"));
		    }
		});	
		tableUser.getColumns().add(tableUser_Username);
		TableColumn<Map<String, String>, String> tableUser_Password = new TableColumn<Map<String, String>, String>("Password");
		tableUser_Password.setMinWidth("Password".length()*10);
		tableUser_Password.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Password"));
		    }
		});	
		tableUser.getColumns().add(tableUser_Password);
		TableColumn<Map<String, String>, String> tableUser_Mailbox = new TableColumn<Map<String, String>, String>("Mailbox");
		tableUser_Mailbox.setMinWidth("Mailbox".length()*10);
		tableUser_Mailbox.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Mailbox"));
		    }
		});	
		tableUser.getColumns().add(tableUser_Mailbox);
		TableColumn<Map<String, String>, String> tableUser_UserStatus = new TableColumn<Map<String, String>, String>("UserStatus");
		tableUser_UserStatus.setMinWidth("UserStatus".length()*10);
		tableUser_UserStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserStatus"));
		    }
		});	
		tableUser.getColumns().add(tableUser_UserStatus);
		
		//table data
		ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
		List<User> rsUser = EntityManager.getAllInstancesOf("User");
		for (User r : rsUser) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("UserId", String.valueOf(r.getUserId()));
			if (r.getUsername() != null)
				unit.put("Username", String.valueOf(r.getUsername()));
			else
				unit.put("Username", "");
			if (r.getPassword() != null)
				unit.put("Password", String.valueOf(r.getPassword()));
			else
				unit.put("Password", "");
			if (r.getMailbox() != null)
				unit.put("Mailbox", String.valueOf(r.getMailbox()));
			else
				unit.put("Mailbox", "");
			unit.put("UserStatus", String.valueOf(r.getUserStatus()));

			dataUser.add(unit);
		}
		
		tableUser.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableUser.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("User", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableUser.setItems(dataUser);
		allObjectTables.put("User", tableUser);
		
		TableView<Map<String, String>> tableBook = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableBook_BookId = new TableColumn<Map<String, String>, String>("BookId");
		tableBook_BookId.setMinWidth("BookId".length()*10);
		tableBook_BookId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("BookId"));
		    }
		});	
		tableBook.getColumns().add(tableBook_BookId);
		TableColumn<Map<String, String>, String> tableBook_Booktitle = new TableColumn<Map<String, String>, String>("Booktitle");
		tableBook_Booktitle.setMinWidth("Booktitle".length()*10);
		tableBook_Booktitle.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Booktitle"));
		    }
		});	
		tableBook.getColumns().add(tableBook_Booktitle);
		TableColumn<Map<String, String>, String> tableBook_Authors = new TableColumn<Map<String, String>, String>("Authors");
		tableBook_Authors.setMinWidth("Authors".length()*10);
		tableBook_Authors.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Authors"));
		    }
		});	
		tableBook.getColumns().add(tableBook_Authors);
		TableColumn<Map<String, String>, String> tableBook_BookStatus = new TableColumn<Map<String, String>, String>("BookStatus");
		tableBook_BookStatus.setMinWidth("BookStatus".length()*10);
		tableBook_BookStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("BookStatus"));
		    }
		});	
		tableBook.getColumns().add(tableBook_BookStatus);
		
		//table data
		ObservableList<Map<String, String>> dataBook = FXCollections.observableArrayList();
		List<Book> rsBook = EntityManager.getAllInstancesOf("Book");
		for (Book r : rsBook) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("BookId", String.valueOf(r.getBookId()));
			if (r.getBooktitle() != null)
				unit.put("Booktitle", String.valueOf(r.getBooktitle()));
			else
				unit.put("Booktitle", "");
			if (r.getAuthors() != null)
				unit.put("Authors", String.valueOf(r.getAuthors()));
			else
				unit.put("Authors", "");
			unit.put("BookStatus", String.valueOf(r.getBookStatus()));

			dataBook.add(unit);
		}
		
		tableBook.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableBook.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Book", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableBook.setItems(dataBook);
		allObjectTables.put("Book", tableBook);
		
		TableView<Map<String, String>> tableBookBorrow = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableBookBorrow_Id = new TableColumn<Map<String, String>, String>("Id");
		tableBookBorrow_Id.setMinWidth("Id".length()*10);
		tableBookBorrow_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableBookBorrow.getColumns().add(tableBookBorrow_Id);
		TableColumn<Map<String, String>, String> tableBookBorrow_Userid = new TableColumn<Map<String, String>, String>("Userid");
		tableBookBorrow_Userid.setMinWidth("Userid".length()*10);
		tableBookBorrow_Userid.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Userid"));
		    }
		});	
		tableBookBorrow.getColumns().add(tableBookBorrow_Userid);
		TableColumn<Map<String, String>, String> tableBookBorrow_Bookid = new TableColumn<Map<String, String>, String>("Bookid");
		tableBookBorrow_Bookid.setMinWidth("Bookid".length()*10);
		tableBookBorrow_Bookid.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Bookid"));
		    }
		});	
		tableBookBorrow.getColumns().add(tableBookBorrow_Bookid);
		TableColumn<Map<String, String>, String> tableBookBorrow_BorrowTime = new TableColumn<Map<String, String>, String>("BorrowTime");
		tableBookBorrow_BorrowTime.setMinWidth("BorrowTime".length()*10);
		tableBookBorrow_BorrowTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("BorrowTime"));
		    }
		});	
		tableBookBorrow.getColumns().add(tableBookBorrow_BorrowTime);
		TableColumn<Map<String, String>, String> tableBookBorrow_ReturnTime = new TableColumn<Map<String, String>, String>("ReturnTime");
		tableBookBorrow_ReturnTime.setMinWidth("ReturnTime".length()*10);
		tableBookBorrow_ReturnTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ReturnTime"));
		    }
		});	
		tableBookBorrow.getColumns().add(tableBookBorrow_ReturnTime);
		TableColumn<Map<String, String>, String> tableBookBorrow_BorrowStatus = new TableColumn<Map<String, String>, String>("BorrowStatus");
		tableBookBorrow_BorrowStatus.setMinWidth("BorrowStatus".length()*10);
		tableBookBorrow_BorrowStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("BorrowStatus"));
		    }
		});	
		tableBookBorrow.getColumns().add(tableBookBorrow_BorrowStatus);
		
		//table data
		ObservableList<Map<String, String>> dataBookBorrow = FXCollections.observableArrayList();
		List<BookBorrow> rsBookBorrow = EntityManager.getAllInstancesOf("BookBorrow");
		for (BookBorrow r : rsBookBorrow) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Id", String.valueOf(r.getId()));
			unit.put("Userid", String.valueOf(r.getUserid()));
			unit.put("Bookid", String.valueOf(r.getBookid()));
			if (r.getBorrowTime() != null)
				unit.put("BorrowTime", r.getBorrowTime().format(dateformatter));
			else
				unit.put("BorrowTime", "");
			if (r.getReturnTime() != null)
				unit.put("ReturnTime", r.getReturnTime().format(dateformatter));
			else
				unit.put("ReturnTime", "");
			unit.put("BorrowStatus", String.valueOf(r.getBorrowStatus()));

			dataBookBorrow.add(unit);
		}
		
		tableBookBorrow.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableBookBorrow.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("BookBorrow", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableBookBorrow.setItems(dataBookBorrow);
		allObjectTables.put("BookBorrow", tableBookBorrow);
		
		TableView<Map<String, String>> tableLibrarian = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableLibrarian_Id = new TableColumn<Map<String, String>, String>("Id");
		tableLibrarian_Id.setMinWidth("Id".length()*10);
		tableLibrarian_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableLibrarian.getColumns().add(tableLibrarian_Id);
		TableColumn<Map<String, String>, String> tableLibrarian_Username = new TableColumn<Map<String, String>, String>("Username");
		tableLibrarian_Username.setMinWidth("Username".length()*10);
		tableLibrarian_Username.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Username"));
		    }
		});	
		tableLibrarian.getColumns().add(tableLibrarian_Username);
		TableColumn<Map<String, String>, String> tableLibrarian_Password = new TableColumn<Map<String, String>, String>("Password");
		tableLibrarian_Password.setMinWidth("Password".length()*10);
		tableLibrarian_Password.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Password"));
		    }
		});	
		tableLibrarian.getColumns().add(tableLibrarian_Password);
		
		//table data
		ObservableList<Map<String, String>> dataLibrarian = FXCollections.observableArrayList();
		List<Librarian> rsLibrarian = EntityManager.getAllInstancesOf("Librarian");
		for (Librarian r : rsLibrarian) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Id", String.valueOf(r.getId()));
			if (r.getUsername() != null)
				unit.put("Username", String.valueOf(r.getUsername()));
			else
				unit.put("Username", "");
			if (r.getPassword() != null)
				unit.put("Password", String.valueOf(r.getPassword()));
			else
				unit.put("Password", "");

			dataLibrarian.add(unit);
		}
		
		tableLibrarian.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableLibrarian.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Librarian", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableLibrarian.setItems(dataLibrarian);
		allObjectTables.put("Librarian", tableLibrarian);
		
		TableView<Map<String, String>> tableVerificationCode = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableVerificationCode_Mailbox = new TableColumn<Map<String, String>, String>("Mailbox");
		tableVerificationCode_Mailbox.setMinWidth("Mailbox".length()*10);
		tableVerificationCode_Mailbox.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Mailbox"));
		    }
		});	
		tableVerificationCode.getColumns().add(tableVerificationCode_Mailbox);
		TableColumn<Map<String, String>, String> tableVerificationCode_Code = new TableColumn<Map<String, String>, String>("Code");
		tableVerificationCode_Code.setMinWidth("Code".length()*10);
		tableVerificationCode_Code.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Code"));
		    }
		});	
		tableVerificationCode.getColumns().add(tableVerificationCode_Code);
		
		//table data
		ObservableList<Map<String, String>> dataVerificationCode = FXCollections.observableArrayList();
		List<VerificationCode> rsVerificationCode = EntityManager.getAllInstancesOf("VerificationCode");
		for (VerificationCode r : rsVerificationCode) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getMailbox() != null)
				unit.put("Mailbox", String.valueOf(r.getMailbox()));
			else
				unit.put("Mailbox", "");
			if (r.getCode() != null)
				unit.put("Code", String.valueOf(r.getCode()));
			else
				unit.put("Code", "");

			dataVerificationCode.add(unit);
		}
		
		tableVerificationCode.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableVerificationCode.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("VerificationCode", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableVerificationCode.setItems(dataVerificationCode);
		allObjectTables.put("VerificationCode", tableVerificationCode);
		

		
	}
	
	/* 
	* update all object tables with sub dataset
	*/ 
	public void updateUserTable(List<User> rsUser) {
			ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
			for (User r : rsUser) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("UserId", String.valueOf(r.getUserId()));
				if (r.getUsername() != null)
					unit.put("Username", String.valueOf(r.getUsername()));
				else
					unit.put("Username", "");
				if (r.getPassword() != null)
					unit.put("Password", String.valueOf(r.getPassword()));
				else
					unit.put("Password", "");
				if (r.getMailbox() != null)
					unit.put("Mailbox", String.valueOf(r.getMailbox()));
				else
					unit.put("Mailbox", "");
				unit.put("UserStatus", String.valueOf(r.getUserStatus()));
				dataUser.add(unit);
			}
			
			allObjectTables.get("User").setItems(dataUser);
	}
	public void updateBookTable(List<Book> rsBook) {
			ObservableList<Map<String, String>> dataBook = FXCollections.observableArrayList();
			for (Book r : rsBook) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("BookId", String.valueOf(r.getBookId()));
				if (r.getBooktitle() != null)
					unit.put("Booktitle", String.valueOf(r.getBooktitle()));
				else
					unit.put("Booktitle", "");
				if (r.getAuthors() != null)
					unit.put("Authors", String.valueOf(r.getAuthors()));
				else
					unit.put("Authors", "");
				unit.put("BookStatus", String.valueOf(r.getBookStatus()));
				dataBook.add(unit);
			}
			
			allObjectTables.get("Book").setItems(dataBook);
	}
	public void updateBookBorrowTable(List<BookBorrow> rsBookBorrow) {
			ObservableList<Map<String, String>> dataBookBorrow = FXCollections.observableArrayList();
			for (BookBorrow r : rsBookBorrow) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Id", String.valueOf(r.getId()));
				unit.put("Userid", String.valueOf(r.getUserid()));
				unit.put("Bookid", String.valueOf(r.getBookid()));
				if (r.getBorrowTime() != null)
					unit.put("BorrowTime", r.getBorrowTime().format(dateformatter));
				else
					unit.put("BorrowTime", "");
				if (r.getReturnTime() != null)
					unit.put("ReturnTime", r.getReturnTime().format(dateformatter));
				else
					unit.put("ReturnTime", "");
				unit.put("BorrowStatus", String.valueOf(r.getBorrowStatus()));
				dataBookBorrow.add(unit);
			}
			
			allObjectTables.get("BookBorrow").setItems(dataBookBorrow);
	}
	public void updateLibrarianTable(List<Librarian> rsLibrarian) {
			ObservableList<Map<String, String>> dataLibrarian = FXCollections.observableArrayList();
			for (Librarian r : rsLibrarian) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Id", String.valueOf(r.getId()));
				if (r.getUsername() != null)
					unit.put("Username", String.valueOf(r.getUsername()));
				else
					unit.put("Username", "");
				if (r.getPassword() != null)
					unit.put("Password", String.valueOf(r.getPassword()));
				else
					unit.put("Password", "");
				dataLibrarian.add(unit);
			}
			
			allObjectTables.get("Librarian").setItems(dataLibrarian);
	}
	public void updateVerificationCodeTable(List<VerificationCode> rsVerificationCode) {
			ObservableList<Map<String, String>> dataVerificationCode = FXCollections.observableArrayList();
			for (VerificationCode r : rsVerificationCode) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getMailbox() != null)
					unit.put("Mailbox", String.valueOf(r.getMailbox()));
				else
					unit.put("Mailbox", "");
				if (r.getCode() != null)
					unit.put("Code", String.valueOf(r.getCode()));
				else
					unit.put("Code", "");
				dataVerificationCode.add(unit);
			}
			
			allObjectTables.get("VerificationCode").setItems(dataVerificationCode);
	}
	
	/* 
	* update all object tables
	*/ 
	public void updateUserTable() {
			ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
			List<User> rsUser = EntityManager.getAllInstancesOf("User");
			for (User r : rsUser) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("UserId", String.valueOf(r.getUserId()));
				if (r.getUsername() != null)
					unit.put("Username", String.valueOf(r.getUsername()));
				else
					unit.put("Username", "");
				if (r.getPassword() != null)
					unit.put("Password", String.valueOf(r.getPassword()));
				else
					unit.put("Password", "");
				if (r.getMailbox() != null)
					unit.put("Mailbox", String.valueOf(r.getMailbox()));
				else
					unit.put("Mailbox", "");
				unit.put("UserStatus", String.valueOf(r.getUserStatus()));
				dataUser.add(unit);
			}
			
			allObjectTables.get("User").setItems(dataUser);
	}
	public void updateBookTable() {
			ObservableList<Map<String, String>> dataBook = FXCollections.observableArrayList();
			List<Book> rsBook = EntityManager.getAllInstancesOf("Book");
			for (Book r : rsBook) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("BookId", String.valueOf(r.getBookId()));
				if (r.getBooktitle() != null)
					unit.put("Booktitle", String.valueOf(r.getBooktitle()));
				else
					unit.put("Booktitle", "");
				if (r.getAuthors() != null)
					unit.put("Authors", String.valueOf(r.getAuthors()));
				else
					unit.put("Authors", "");
				unit.put("BookStatus", String.valueOf(r.getBookStatus()));
				dataBook.add(unit);
			}
			
			allObjectTables.get("Book").setItems(dataBook);
	}
	public void updateBookBorrowTable() {
			ObservableList<Map<String, String>> dataBookBorrow = FXCollections.observableArrayList();
			List<BookBorrow> rsBookBorrow = EntityManager.getAllInstancesOf("BookBorrow");
			for (BookBorrow r : rsBookBorrow) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Id", String.valueOf(r.getId()));
				unit.put("Userid", String.valueOf(r.getUserid()));
				unit.put("Bookid", String.valueOf(r.getBookid()));
				if (r.getBorrowTime() != null)
					unit.put("BorrowTime", r.getBorrowTime().format(dateformatter));
				else
					unit.put("BorrowTime", "");
				if (r.getReturnTime() != null)
					unit.put("ReturnTime", r.getReturnTime().format(dateformatter));
				else
					unit.put("ReturnTime", "");
				unit.put("BorrowStatus", String.valueOf(r.getBorrowStatus()));
				dataBookBorrow.add(unit);
			}
			
			allObjectTables.get("BookBorrow").setItems(dataBookBorrow);
	}
	public void updateLibrarianTable() {
			ObservableList<Map<String, String>> dataLibrarian = FXCollections.observableArrayList();
			List<Librarian> rsLibrarian = EntityManager.getAllInstancesOf("Librarian");
			for (Librarian r : rsLibrarian) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Id", String.valueOf(r.getId()));
				if (r.getUsername() != null)
					unit.put("Username", String.valueOf(r.getUsername()));
				else
					unit.put("Username", "");
				if (r.getPassword() != null)
					unit.put("Password", String.valueOf(r.getPassword()));
				else
					unit.put("Password", "");
				dataLibrarian.add(unit);
			}
			
			allObjectTables.get("Librarian").setItems(dataLibrarian);
	}
	public void updateVerificationCodeTable() {
			ObservableList<Map<String, String>> dataVerificationCode = FXCollections.observableArrayList();
			List<VerificationCode> rsVerificationCode = EntityManager.getAllInstancesOf("VerificationCode");
			for (VerificationCode r : rsVerificationCode) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getMailbox() != null)
					unit.put("Mailbox", String.valueOf(r.getMailbox()));
				else
					unit.put("Mailbox", "");
				if (r.getCode() != null)
					unit.put("Code", String.valueOf(r.getCode()));
				else
					unit.put("Code", "");
				dataVerificationCode.add(unit);
			}
			
			allObjectTables.get("VerificationCode").setItems(dataVerificationCode);
	}
	
	public void classStatisicBingding() {
	
		 classInfodata = FXCollections.observableArrayList();
	 	 user = new ClassInfo("User", EntityManager.getAllInstancesOf("User").size());
	 	 classInfodata.add(user);
	 	 book = new ClassInfo("Book", EntityManager.getAllInstancesOf("Book").size());
	 	 classInfodata.add(book);
	 	 bookborrow = new ClassInfo("BookBorrow", EntityManager.getAllInstancesOf("BookBorrow").size());
	 	 classInfodata.add(bookborrow);
	 	 librarian = new ClassInfo("Librarian", EntityManager.getAllInstancesOf("Librarian").size());
	 	 classInfodata.add(librarian);
	 	 verificationcode = new ClassInfo("VerificationCode", EntityManager.getAllInstancesOf("VerificationCode").size());
	 	 classInfodata.add(verificationcode);
	 	 
		 class_statisic.setItems(classInfodata);
		 
		 //Class Statisic Binding
		 class_statisic.getSelectionModel().selectedItemProperty().addListener(
				 (observable, oldValue, newValue) ->  { 
				 										 //no selected object in table
				 										 objectindex = -1;
				 										 
				 										 //get lastest data, reflect updateTableData method
				 										 try {
												 			 Method updateob = this.getClass().getMethod("update" + newValue.getName() + "Table", null);
												 			 updateob.invoke(this);			 
												 		 } catch (Exception e) {
												 		 	 e.printStackTrace();
												 		 }		 										 
				 	
				 										 //show object table
				 			 				 			 TableView obs = allObjectTables.get(newValue.getName());
				 			 				 			 if (obs != null) {
				 			 				 				object_statics.setContent(obs);
				 			 				 				object_statics.setText("All Objects " + newValue.getName() + ":");
				 			 				 			 }
				 			 				 			 
				 			 				 			 //update association information
							 			 				 updateAssociation(newValue.getName());
				 			 				 			 
				 			 				 			 //show association information
				 			 				 			 ObservableList<AssociationInfo> asso = allassociationData.get(newValue.getName());
				 			 				 			 if (asso != null) {
				 			 				 			 	association_statisic.setItems(asso);
				 			 				 			 }
				 			 				 		  });
	}
	
	public void classStatisticUpdate() {
	 	 user.setNumber(EntityManager.getAllInstancesOf("User").size());
	 	 book.setNumber(EntityManager.getAllInstancesOf("Book").size());
	 	 bookborrow.setNumber(EntityManager.getAllInstancesOf("BookBorrow").size());
	 	 librarian.setNumber(EntityManager.getAllInstancesOf("Librarian").size());
	 	 verificationcode.setNumber(EntityManager.getAllInstancesOf("VerificationCode").size());
		
	}
	
	/**
	 * association binding
	 */
	public void associationStatisicBingding() {
		
		allassociationData = new HashMap<String, ObservableList<AssociationInfo>>();
		
		ObservableList<AssociationInfo> User_association_data = FXCollections.observableArrayList();
		AssociationInfo User_associatition_UsertoBookBorrow = new AssociationInfo("User", "BookBorrow", "UsertoBookBorrow", true);
		User_association_data.add(User_associatition_UsertoBookBorrow);
		AssociationInfo User_associatition_UsertoVerificationCode = new AssociationInfo("User", "VerificationCode", "UsertoVerificationCode", false);
		User_association_data.add(User_associatition_UsertoVerificationCode);
		
		allassociationData.put("User", User_association_data);
		
		ObservableList<AssociationInfo> Book_association_data = FXCollections.observableArrayList();
		AssociationInfo Book_associatition_BooktoBookBorrow = new AssociationInfo("Book", "BookBorrow", "BooktoBookBorrow", false);
		Book_association_data.add(Book_associatition_BooktoBookBorrow);
		
		allassociationData.put("Book", Book_association_data);
		
		ObservableList<AssociationInfo> BookBorrow_association_data = FXCollections.observableArrayList();
		AssociationInfo BookBorrow_associatition_BookBorrowtoUser = new AssociationInfo("BookBorrow", "User", "BookBorrowtoUser", false);
		BookBorrow_association_data.add(BookBorrow_associatition_BookBorrowtoUser);
		AssociationInfo BookBorrow_associatition_BookBorrowtoBook = new AssociationInfo("BookBorrow", "Book", "BookBorrowtoBook", false);
		BookBorrow_association_data.add(BookBorrow_associatition_BookBorrowtoBook);
		AssociationInfo BookBorrow_associatition_BookBorrowtoLibrarian = new AssociationInfo("BookBorrow", "Librarian", "BookBorrowtoLibrarian", false);
		BookBorrow_association_data.add(BookBorrow_associatition_BookBorrowtoLibrarian);
		
		allassociationData.put("BookBorrow", BookBorrow_association_data);
		
		ObservableList<AssociationInfo> Librarian_association_data = FXCollections.observableArrayList();
		AssociationInfo Librarian_associatition_LibrariantoBookBorrow = new AssociationInfo("Librarian", "BookBorrow", "LibrariantoBookBorrow", true);
		Librarian_association_data.add(Librarian_associatition_LibrariantoBookBorrow);
		
		allassociationData.put("Librarian", Librarian_association_data);
		
		ObservableList<AssociationInfo> VerificationCode_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("VerificationCode", VerificationCode_association_data);
		
		
		association_statisic.getSelectionModel().selectedItemProperty().addListener(
			    (observable, oldValue, newValue) ->  { 
	
							 		if (newValue != null) {
							 			 try {
							 			 	 if (newValue.getNumber() != 0) {
								 				 //choose object or not
								 				 if (objectindex != -1) {
									 				 Class[] cArg = new Class[1];
									 				 cArg[0] = List.class;
									 				 //reflect updateTableData method
										 			 Method updateob = this.getClass().getMethod("update" + newValue.getTargetClass() + "Table", cArg);
										 			 //find choosen object
										 			 Object selectedob = EntityManager.getAllInstancesOf(newValue.getSourceClass()).get(objectindex);
										 			 //reflect find association method
										 			 Method getAssociatedObject = selectedob.getClass().getMethod("get" + newValue.getAssociationName());
										 			 List r = new LinkedList();
										 			 //one or mulity?
										 			 if (newValue.getIsMultiple() == true) {
											 			 
											 			r = (List) getAssociatedObject.invoke(selectedob);
										 			 }
										 			 else {
										 				r.add(getAssociatedObject.invoke(selectedob));
										 			 }
										 			 //invoke update method
										 			 updateob.invoke(this, r);
										 			  
										 			 
								 				 }
												 //bind updated data to GUI
					 				 			 TableView obs = allObjectTables.get(newValue.getTargetClass());
					 				 			 if (obs != null) {
					 				 				object_statics.setContent(obs);
					 				 				object_statics.setText("Targets Objects " + newValue.getTargetClass() + ":");
					 				 			 }
					 				 		 }
							 			 }
							 			 catch (Exception e) {
							 				 e.printStackTrace();
							 			 }
							 		}
					 		  });
		
	}	
	
	

    //prepare data for contract
	public void prepareData() {
		
		//definition map
		definitions_map = new HashMap<String, String>();
		definitions_map.put("inputUser", "user:User = User.allInstance()->any(ban:User | ban.Username = username)\r\r\n");
		definitions_map.put("verification", "user:User = self.User\r\r\n");
		definitions_map.put("inputUsername", "user:User = User.allInstance()->any(ban:User | ban.Username = name)\r\r\n");
		definitions_map.put("inputPassword", "user:User = User.allInstance()->any(ban:User | ban.Username = self.Username)\r\r\n");
		definitions_map.put("login", "user:User = User.allInstance()->any(ban:User | ban.Username = username)\r\r\n");
		definitions_map.put("inputtitle", "book:Book = Book.allInstance()->any(ban:Book | ban.Booktitle = title)\r\r\n");
		definitions_map.put("selectbook", "book:Book = Book.allInstance()->any(ban:Book | ban.BookId = bookid)\r\r\n");
		definitions_map.put("choosebook", "book:Book = Book.allInstance()->any(ban:Book | ban.BookId = bookid)\r\r\n");
		definitions_map.put("inputuser", "user:User = User.allInstance()->any(ban:User | ban.Username = username)\r\r\n");
		definitions_map.put("inputbook", "book:Book = Book.allInstance()->any(ban:Book | ban.Booktitle = bookname)\r\r\n");
		definitions_map.put("createUser", "user:User = User.allInstance()->any(ban:User | ban.UserId = userid)\r\r\n");
		definitions_map.put("queryUser", "user:User = User.allInstance()->any(ban:User | ban.UserId = userid)\r\r\n");
		definitions_map.put("modifyUser", "user:User = User.allInstance()->any(ban:User | ban.UserId = userid)\r\r\n");
		definitions_map.put("deleteUser", "user:User = User.allInstance()->any(ban:User | ban.UserId = userid)\r\r\n");
		definitions_map.put("createBook", "book:Book = Book.allInstance()->any(ban:Book | ban.BookId = bookid)\r\r\n");
		definitions_map.put("queryBook", "book:Book = Book.allInstance()->any(ban:Book | ban.BookId = bookid)\r\r\n");
		definitions_map.put("deleteBook", "book:Book = Book.allInstance()->any(ban:Book | ban.BookId = bookid)\r\r\n");
		definitions_map.put("modifyBook", "book:Book = Book.allInstance()->any(ban:Book | ban.BookId = bookid)\r\r\n");
		definitions_map.put("createBookBorrow", "bookborrow:BookBorrow = BookBorrow.allInstance()->any(ban:BookBorrow | ban.Id = id)\r\r\n");
		definitions_map.put("queryBookBorrow", "bookborrow:BookBorrow = BookBorrow.allInstance()->any(ban:BookBorrow | ban.Id = id)\r\r\n");
		definitions_map.put("deleteBookBorrow", "bookborrow:BookBorrow = BookBorrow.allInstance()->any(ban:BookBorrow | ban.Id = id)\r\r\n");
		definitions_map.put("modifyBookBorrow", "bookborrow:BookBorrow = BookBorrow.allInstance()->any(ban:BookBorrow | ban.Id = id)\r\r\n");
		
		//precondition map
		preconditions_map = new HashMap<String, String>();
		preconditions_map.put("inputUser", "user.oclIsUndefined() = true");
		preconditions_map.put("verification", "true");
		preconditions_map.put("inputUsername", "user.oclIsUndefined() = false");
		preconditions_map.put("inputPassword", "user.oclIsUndefined() = false");
		preconditions_map.put("login", "user.oclIsUndefined() = false");
		preconditions_map.put("inputtitle", "book.oclIsUndefined() = false");
		preconditions_map.put("brows", "true");
		preconditions_map.put("selectbook", "book.oclIsUndefined() = false");
		preconditions_map.put("choosebook", "book.oclIsUndefined() = false");
		preconditions_map.put("selecttime", "true");
		preconditions_map.put("confirm", "true");
		preconditions_map.put("inputuser", "user.oclIsUndefined() = false");
		preconditions_map.put("inputbook", "book.oclIsUndefined() = false");
		preconditions_map.put("confirminformation", "true");
		preconditions_map.put("createUser", "user.oclIsUndefined() = true");
		preconditions_map.put("queryUser", "user.oclIsUndefined() = false");
		preconditions_map.put("modifyUser", "user.oclIsUndefined() = false");
		preconditions_map.put("deleteUser", "user.oclIsUndefined() = false and\nUser.allInstance()->includes(user)\n");
		preconditions_map.put("createBook", "book.oclIsUndefined() = true");
		preconditions_map.put("queryBook", "book.oclIsUndefined() = false");
		preconditions_map.put("deleteBook", "book.oclIsUndefined() = false and\nBook.allInstance()->includes(book)\n");
		preconditions_map.put("modifyBook", "book.oclIsUndefined() = false");
		preconditions_map.put("createBookBorrow", "bookborrow.oclIsUndefined() = true");
		preconditions_map.put("queryBookBorrow", "bookborrow.oclIsUndefined() = false");
		preconditions_map.put("deleteBookBorrow", "bookborrow.oclIsUndefined() = false and\nBookBorrow.allInstance()->includes(bookborrow)\n");
		preconditions_map.put("modifyBookBorrow", "bookborrow.oclIsUndefined() = false");
		
		//postcondition map
		postconditions_map = new HashMap<String, String>();
		postconditions_map.put("inputUser", "let ban:User inban.oclIsNew() and\nban.UserId = 1214 and\nban.Username = username and\nban.Password = password and\nban.Mailbox = email and\nself.User = ban and\nself.Vcode = \"111\" and\nresult = true\n");
		postconditions_map.put("verification", "if(self.Vcode = code)\nthen\nUser.allInstance()->includes(user) and\nresult = true\nelse\nresult = false\nendif");
		postconditions_map.put("inputUsername", "self.Username = name and\nresult = true\n");
		postconditions_map.put("inputPassword", "ifuser.Password = password\nthen\nself.User = user and\nresult = true\nelse\nresult = false\nendif");
		postconditions_map.put("login", "ifuser.Password = password\nthen\nresult = true\nelse\nresult = false\nendif");
		postconditions_map.put("inputtitle", "self.Book = book and\nresult = true\n");
		postconditions_map.put("brows", "result = true");
		postconditions_map.put("selectbook", "ifbookid = self.Book.BookId\nthen\nresult = self.Book\nelse\nresult = null\nendif");
		postconditions_map.put("choosebook", "result = true");
		postconditions_map.put("selecttime", "result = true");
		postconditions_map.put("confirm", "result = true");
		postconditions_map.put("inputuser", "result = true");
		postconditions_map.put("inputbook", "result = true");
		postconditions_map.put("confirminformation", "result = true");
		postconditions_map.put("createUser", "let ban:User inban.oclIsNew() and\nban.UserId = userid and\nban.Username = username and\nban.Password = password and\nban.Mailbox = mailbox and\nban.UserStatus = userstatus and\nUser.allInstance()->includes(ban) and\nresult = true\n");
		postconditions_map.put("queryUser", "result = user");
		postconditions_map.put("modifyUser", "user.UserId = userid and\nuser.Username = username and\nuser.Password = password and\nuser.Mailbox = mailbox and\nuser.UserStatus = userstatus and\nresult = true\n");
		postconditions_map.put("deleteUser", "User.allInstance()->excludes(user) and\nresult = true\n");
		postconditions_map.put("createBook", "let ban:Book inban.oclIsNew() and\nban.BookId = bookid and\nban.Booktitle = booktitle and\nban.Authors = authors and\nban.BookStatus = bookstatus and\nBook.allInstance()->includes(ban) and\nresult = true\n");
		postconditions_map.put("queryBook", "result = book");
		postconditions_map.put("deleteBook", "Book.allInstance()->excludes(book) and\nresult = true\n");
		postconditions_map.put("modifyBook", "book.BookId = bookid and\nbook.Booktitle = booktitle and\nbook.Authors = authors and\nbook.BookStatus = bookstatus and\nresult = true\n");
		postconditions_map.put("createBookBorrow", "let ban:BookBorrow inban.oclIsNew() and\nban.Id = id and\nban.Userid = userid and\nban.Bookid = bookid and\nban.BorrowTime = borrowtime and\nban.ReturnTime = returntime and\nban.BorrowStatus = borrowstatus and\nBookBorrow.allInstance()->includes(ban) and\nresult = true\n");
		postconditions_map.put("queryBookBorrow", "result = bookborrow");
		postconditions_map.put("deleteBookBorrow", "BookBorrow.allInstance()->excludes(bookborrow) and\nresult = true\n");
		postconditions_map.put("modifyBookBorrow", "bookborrow.Id = id and\nbookborrow.Userid = userid and\nbookborrow.Bookid = bookid and\nbookborrow.BorrowTime = borrowtime and\nbookborrow.ReturnTime = returntime and\nbookborrow.BorrowStatus = borrowstatus and\nresult = true\n");
		
		//service invariants map
		service_invariants_map = new LinkedHashMap<String, String>();
		
		//entity invariants map
		entity_invariants_map = new LinkedHashMap<String, String>();
		
	}
	
	public void generatOperationPane() {
		
		 operationPanels = new LinkedHashMap<String, GridPane>();
		
		 // ==================== GridPane_inputUser ====================
		 GridPane inputUser = new GridPane();
		 inputUser.setHgap(4);
		 inputUser.setVgap(6);
		 inputUser.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> inputUser_content = inputUser.getChildren();
		 Label inputUser_username_label = new Label("username:");
		 inputUser_username_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputUser_content.add(inputUser_username_label);
		 GridPane.setConstraints(inputUser_username_label, 0, 0);
		 
		 inputUser_username_t = new TextField();
		 inputUser_content.add(inputUser_username_t);
		 inputUser_username_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputUser_username_t, 1, 0);
		 Label inputUser_password_label = new Label("password:");
		 inputUser_password_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputUser_content.add(inputUser_password_label);
		 GridPane.setConstraints(inputUser_password_label, 0, 1);
		 
		 inputUser_password_t = new TextField();
		 inputUser_content.add(inputUser_password_t);
		 inputUser_password_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputUser_password_t, 1, 1);
		 Label inputUser_email_label = new Label("email:");
		 inputUser_email_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputUser_content.add(inputUser_email_label);
		 GridPane.setConstraints(inputUser_email_label, 0, 2);
		 
		 inputUser_email_t = new TextField();
		 inputUser_content.add(inputUser_email_t);
		 inputUser_email_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputUser_email_t, 1, 2);
		 operationPanels.put("inputUser", inputUser);
		 
		 // ==================== GridPane_verification ====================
		 GridPane verification = new GridPane();
		 verification.setHgap(4);
		 verification.setVgap(6);
		 verification.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> verification_content = verification.getChildren();
		 Label verification_code_label = new Label("code:");
		 verification_code_label.setMinWidth(Region.USE_PREF_SIZE);
		 verification_content.add(verification_code_label);
		 GridPane.setConstraints(verification_code_label, 0, 0);
		 
		 verification_code_t = new TextField();
		 verification_content.add(verification_code_t);
		 verification_code_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(verification_code_t, 1, 0);
		 operationPanels.put("verification", verification);
		 
		 // ==================== GridPane_inputUsername ====================
		 GridPane inputUsername = new GridPane();
		 inputUsername.setHgap(4);
		 inputUsername.setVgap(6);
		 inputUsername.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> inputUsername_content = inputUsername.getChildren();
		 Label inputUsername_name_label = new Label("name:");
		 inputUsername_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputUsername_content.add(inputUsername_name_label);
		 GridPane.setConstraints(inputUsername_name_label, 0, 0);
		 
		 inputUsername_name_t = new TextField();
		 inputUsername_content.add(inputUsername_name_t);
		 inputUsername_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputUsername_name_t, 1, 0);
		 operationPanels.put("inputUsername", inputUsername);
		 
		 // ==================== GridPane_inputPassword ====================
		 GridPane inputPassword = new GridPane();
		 inputPassword.setHgap(4);
		 inputPassword.setVgap(6);
		 inputPassword.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> inputPassword_content = inputPassword.getChildren();
		 Label inputPassword_password_label = new Label("password:");
		 inputPassword_password_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputPassword_content.add(inputPassword_password_label);
		 GridPane.setConstraints(inputPassword_password_label, 0, 0);
		 
		 inputPassword_password_t = new TextField();
		 inputPassword_content.add(inputPassword_password_t);
		 inputPassword_password_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputPassword_password_t, 1, 0);
		 operationPanels.put("inputPassword", inputPassword);
		 
		 // ==================== GridPane_login ====================
		 GridPane login = new GridPane();
		 login.setHgap(4);
		 login.setVgap(6);
		 login.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> login_content = login.getChildren();
		 Label login_username_label = new Label("username:");
		 login_username_label.setMinWidth(Region.USE_PREF_SIZE);
		 login_content.add(login_username_label);
		 GridPane.setConstraints(login_username_label, 0, 0);
		 
		 login_username_t = new TextField();
		 login_content.add(login_username_t);
		 login_username_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(login_username_t, 1, 0);
		 Label login_password_label = new Label("password:");
		 login_password_label.setMinWidth(Region.USE_PREF_SIZE);
		 login_content.add(login_password_label);
		 GridPane.setConstraints(login_password_label, 0, 1);
		 
		 login_password_t = new TextField();
		 login_content.add(login_password_t);
		 login_password_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(login_password_t, 1, 1);
		 operationPanels.put("login", login);
		 
		 // ==================== GridPane_inputtitle ====================
		 GridPane inputtitle = new GridPane();
		 inputtitle.setHgap(4);
		 inputtitle.setVgap(6);
		 inputtitle.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> inputtitle_content = inputtitle.getChildren();
		 Label inputtitle_title_label = new Label("title:");
		 inputtitle_title_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputtitle_content.add(inputtitle_title_label);
		 GridPane.setConstraints(inputtitle_title_label, 0, 0);
		 
		 inputtitle_title_t = new TextField();
		 inputtitle_content.add(inputtitle_title_t);
		 inputtitle_title_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputtitle_title_t, 1, 0);
		 operationPanels.put("inputtitle", inputtitle);
		 
		 // ==================== GridPane_brows ====================
		 GridPane brows = new GridPane();
		 brows.setHgap(4);
		 brows.setVgap(6);
		 brows.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> brows_content = brows.getChildren();
		 Label brows_label = new Label("This operation is no intput parameters..");
		 brows_label.setMinWidth(Region.USE_PREF_SIZE);
		 brows_content.add(brows_label);
		 GridPane.setConstraints(brows_label, 0, 0);
		 operationPanels.put("brows", brows);
		 
		 // ==================== GridPane_selectbook ====================
		 GridPane selectbook = new GridPane();
		 selectbook.setHgap(4);
		 selectbook.setVgap(6);
		 selectbook.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> selectbook_content = selectbook.getChildren();
		 Label selectbook_bookid_label = new Label("bookid:");
		 selectbook_bookid_label.setMinWidth(Region.USE_PREF_SIZE);
		 selectbook_content.add(selectbook_bookid_label);
		 GridPane.setConstraints(selectbook_bookid_label, 0, 0);
		 
		 selectbook_bookid_t = new TextField();
		 selectbook_content.add(selectbook_bookid_t);
		 selectbook_bookid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(selectbook_bookid_t, 1, 0);
		 operationPanels.put("selectbook", selectbook);
		 
		 // ==================== GridPane_choosebook ====================
		 GridPane choosebook = new GridPane();
		 choosebook.setHgap(4);
		 choosebook.setVgap(6);
		 choosebook.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> choosebook_content = choosebook.getChildren();
		 Label choosebook_bookid_label = new Label("bookid:");
		 choosebook_bookid_label.setMinWidth(Region.USE_PREF_SIZE);
		 choosebook_content.add(choosebook_bookid_label);
		 GridPane.setConstraints(choosebook_bookid_label, 0, 0);
		 
		 choosebook_bookid_t = new TextField();
		 choosebook_content.add(choosebook_bookid_t);
		 choosebook_bookid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(choosebook_bookid_t, 1, 0);
		 operationPanels.put("choosebook", choosebook);
		 
		 // ==================== GridPane_selecttime ====================
		 GridPane selecttime = new GridPane();
		 selecttime.setHgap(4);
		 selecttime.setVgap(6);
		 selecttime.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> selecttime_content = selecttime.getChildren();
		 Label selecttime_time_label = new Label("time (yyyy-MM-dd):");
		 selecttime_time_label.setMinWidth(Region.USE_PREF_SIZE);
		 selecttime_content.add(selecttime_time_label);
		 GridPane.setConstraints(selecttime_time_label, 0, 0);
		 
		 selecttime_time_t = new TextField();
		 selecttime_content.add(selecttime_time_t);
		 selecttime_time_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(selecttime_time_t, 1, 0);
		 operationPanels.put("selecttime", selecttime);
		 
		 // ==================== GridPane_confirm ====================
		 GridPane confirm = new GridPane();
		 confirm.setHgap(4);
		 confirm.setVgap(6);
		 confirm.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> confirm_content = confirm.getChildren();
		 Label confirm_label = new Label("This operation is no intput parameters..");
		 confirm_label.setMinWidth(Region.USE_PREF_SIZE);
		 confirm_content.add(confirm_label);
		 GridPane.setConstraints(confirm_label, 0, 0);
		 operationPanels.put("confirm", confirm);
		 
		 // ==================== GridPane_inputuser ====================
		 GridPane inputuser = new GridPane();
		 inputuser.setHgap(4);
		 inputuser.setVgap(6);
		 inputuser.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> inputuser_content = inputuser.getChildren();
		 Label inputuser_username_label = new Label("username:");
		 inputuser_username_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputuser_content.add(inputuser_username_label);
		 GridPane.setConstraints(inputuser_username_label, 0, 0);
		 
		 inputuser_username_t = new TextField();
		 inputuser_content.add(inputuser_username_t);
		 inputuser_username_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputuser_username_t, 1, 0);
		 operationPanels.put("inputuser", inputuser);
		 
		 // ==================== GridPane_inputbook ====================
		 GridPane inputbook = new GridPane();
		 inputbook.setHgap(4);
		 inputbook.setVgap(6);
		 inputbook.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> inputbook_content = inputbook.getChildren();
		 Label inputbook_bookname_label = new Label("bookname:");
		 inputbook_bookname_label.setMinWidth(Region.USE_PREF_SIZE);
		 inputbook_content.add(inputbook_bookname_label);
		 GridPane.setConstraints(inputbook_bookname_label, 0, 0);
		 
		 inputbook_bookname_t = new TextField();
		 inputbook_content.add(inputbook_bookname_t);
		 inputbook_bookname_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(inputbook_bookname_t, 1, 0);
		 operationPanels.put("inputbook", inputbook);
		 
		 // ==================== GridPane_confirminformation ====================
		 GridPane confirminformation = new GridPane();
		 confirminformation.setHgap(4);
		 confirminformation.setVgap(6);
		 confirminformation.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> confirminformation_content = confirminformation.getChildren();
		 Label confirminformation_label = new Label("This operation is no intput parameters..");
		 confirminformation_label.setMinWidth(Region.USE_PREF_SIZE);
		 confirminformation_content.add(confirminformation_label);
		 GridPane.setConstraints(confirminformation_label, 0, 0);
		 operationPanels.put("confirminformation", confirminformation);
		 
		 // ==================== GridPane_createUser ====================
		 GridPane createUser = new GridPane();
		 createUser.setHgap(4);
		 createUser.setVgap(6);
		 createUser.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createUser_content = createUser.getChildren();
		 Label createUser_userid_label = new Label("userid:");
		 createUser_userid_label.setMinWidth(Region.USE_PREF_SIZE);
		 createUser_content.add(createUser_userid_label);
		 GridPane.setConstraints(createUser_userid_label, 0, 0);
		 
		 createUser_userid_t = new TextField();
		 createUser_content.add(createUser_userid_t);
		 createUser_userid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createUser_userid_t, 1, 0);
		 Label createUser_username_label = new Label("username:");
		 createUser_username_label.setMinWidth(Region.USE_PREF_SIZE);
		 createUser_content.add(createUser_username_label);
		 GridPane.setConstraints(createUser_username_label, 0, 1);
		 
		 createUser_username_t = new TextField();
		 createUser_content.add(createUser_username_t);
		 createUser_username_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createUser_username_t, 1, 1);
		 Label createUser_password_label = new Label("password:");
		 createUser_password_label.setMinWidth(Region.USE_PREF_SIZE);
		 createUser_content.add(createUser_password_label);
		 GridPane.setConstraints(createUser_password_label, 0, 2);
		 
		 createUser_password_t = new TextField();
		 createUser_content.add(createUser_password_t);
		 createUser_password_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createUser_password_t, 1, 2);
		 Label createUser_mailbox_label = new Label("mailbox:");
		 createUser_mailbox_label.setMinWidth(Region.USE_PREF_SIZE);
		 createUser_content.add(createUser_mailbox_label);
		 GridPane.setConstraints(createUser_mailbox_label, 0, 3);
		 
		 createUser_mailbox_t = new TextField();
		 createUser_content.add(createUser_mailbox_t);
		 createUser_mailbox_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createUser_mailbox_t, 1, 3);
		 Label createUser_userstatus_label = new Label("userstatus:");
		 createUser_userstatus_label.setMinWidth(Region.USE_PREF_SIZE);
		 createUser_content.add(createUser_userstatus_label);
		 GridPane.setConstraints(createUser_userstatus_label, 0, 4);
		 
		 createUser_userstatus_cb = new ChoiceBox();
createUser_userstatus_cb.getItems().add("LEGAL");
createUser_userstatus_cb.getItems().add("ILLEGAL");
createUser_userstatus_cb.getItems().add("UNRELIABLE");
		 createUser_userstatus_cb.getSelectionModel().selectFirst();
		 createUser_content.add(createUser_userstatus_cb);
		 GridPane.setConstraints(createUser_userstatus_cb, 1, 4);
		 operationPanels.put("createUser", createUser);
		 
		 // ==================== GridPane_queryUser ====================
		 GridPane queryUser = new GridPane();
		 queryUser.setHgap(4);
		 queryUser.setVgap(6);
		 queryUser.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryUser_content = queryUser.getChildren();
		 Label queryUser_userid_label = new Label("userid:");
		 queryUser_userid_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryUser_content.add(queryUser_userid_label);
		 GridPane.setConstraints(queryUser_userid_label, 0, 0);
		 
		 queryUser_userid_t = new TextField();
		 queryUser_content.add(queryUser_userid_t);
		 queryUser_userid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryUser_userid_t, 1, 0);
		 operationPanels.put("queryUser", queryUser);
		 
		 // ==================== GridPane_modifyUser ====================
		 GridPane modifyUser = new GridPane();
		 modifyUser.setHgap(4);
		 modifyUser.setVgap(6);
		 modifyUser.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyUser_content = modifyUser.getChildren();
		 Label modifyUser_userid_label = new Label("userid:");
		 modifyUser_userid_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyUser_content.add(modifyUser_userid_label);
		 GridPane.setConstraints(modifyUser_userid_label, 0, 0);
		 
		 modifyUser_userid_t = new TextField();
		 modifyUser_content.add(modifyUser_userid_t);
		 modifyUser_userid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyUser_userid_t, 1, 0);
		 Label modifyUser_username_label = new Label("username:");
		 modifyUser_username_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyUser_content.add(modifyUser_username_label);
		 GridPane.setConstraints(modifyUser_username_label, 0, 1);
		 
		 modifyUser_username_t = new TextField();
		 modifyUser_content.add(modifyUser_username_t);
		 modifyUser_username_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyUser_username_t, 1, 1);
		 Label modifyUser_password_label = new Label("password:");
		 modifyUser_password_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyUser_content.add(modifyUser_password_label);
		 GridPane.setConstraints(modifyUser_password_label, 0, 2);
		 
		 modifyUser_password_t = new TextField();
		 modifyUser_content.add(modifyUser_password_t);
		 modifyUser_password_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyUser_password_t, 1, 2);
		 Label modifyUser_mailbox_label = new Label("mailbox:");
		 modifyUser_mailbox_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyUser_content.add(modifyUser_mailbox_label);
		 GridPane.setConstraints(modifyUser_mailbox_label, 0, 3);
		 
		 modifyUser_mailbox_t = new TextField();
		 modifyUser_content.add(modifyUser_mailbox_t);
		 modifyUser_mailbox_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyUser_mailbox_t, 1, 3);
		 Label modifyUser_userstatus_label = new Label("userstatus:");
		 modifyUser_userstatus_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyUser_content.add(modifyUser_userstatus_label);
		 GridPane.setConstraints(modifyUser_userstatus_label, 0, 4);
		 
		 modifyUser_userstatus_cb = new ChoiceBox();
modifyUser_userstatus_cb.getItems().add("LEGAL");
modifyUser_userstatus_cb.getItems().add("ILLEGAL");
modifyUser_userstatus_cb.getItems().add("UNRELIABLE");
		 modifyUser_userstatus_cb.getSelectionModel().selectFirst();
		 modifyUser_content.add(modifyUser_userstatus_cb);
		 GridPane.setConstraints(modifyUser_userstatus_cb, 1, 4);
		 operationPanels.put("modifyUser", modifyUser);
		 
		 // ==================== GridPane_deleteUser ====================
		 GridPane deleteUser = new GridPane();
		 deleteUser.setHgap(4);
		 deleteUser.setVgap(6);
		 deleteUser.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteUser_content = deleteUser.getChildren();
		 Label deleteUser_userid_label = new Label("userid:");
		 deleteUser_userid_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteUser_content.add(deleteUser_userid_label);
		 GridPane.setConstraints(deleteUser_userid_label, 0, 0);
		 
		 deleteUser_userid_t = new TextField();
		 deleteUser_content.add(deleteUser_userid_t);
		 deleteUser_userid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteUser_userid_t, 1, 0);
		 operationPanels.put("deleteUser", deleteUser);
		 
		 // ==================== GridPane_createBook ====================
		 GridPane createBook = new GridPane();
		 createBook.setHgap(4);
		 createBook.setVgap(6);
		 createBook.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createBook_content = createBook.getChildren();
		 Label createBook_bookid_label = new Label("bookid:");
		 createBook_bookid_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBook_content.add(createBook_bookid_label);
		 GridPane.setConstraints(createBook_bookid_label, 0, 0);
		 
		 createBook_bookid_t = new TextField();
		 createBook_content.add(createBook_bookid_t);
		 createBook_bookid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBook_bookid_t, 1, 0);
		 Label createBook_booktitle_label = new Label("booktitle:");
		 createBook_booktitle_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBook_content.add(createBook_booktitle_label);
		 GridPane.setConstraints(createBook_booktitle_label, 0, 1);
		 
		 createBook_booktitle_t = new TextField();
		 createBook_content.add(createBook_booktitle_t);
		 createBook_booktitle_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBook_booktitle_t, 1, 1);
		 Label createBook_authors_label = new Label("authors:");
		 createBook_authors_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBook_content.add(createBook_authors_label);
		 GridPane.setConstraints(createBook_authors_label, 0, 2);
		 
		 createBook_authors_t = new TextField();
		 createBook_content.add(createBook_authors_t);
		 createBook_authors_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBook_authors_t, 1, 2);
		 Label createBook_bookstatus_label = new Label("bookstatus:");
		 createBook_bookstatus_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBook_content.add(createBook_bookstatus_label);
		 GridPane.setConstraints(createBook_bookstatus_label, 0, 3);
		 
		 createBook_bookstatus_cb = new ChoiceBox();
createBook_bookstatus_cb.getItems().add("BORROWED");
createBook_bookstatus_cb.getItems().add("STOCKED");
		 createBook_bookstatus_cb.getSelectionModel().selectFirst();
		 createBook_content.add(createBook_bookstatus_cb);
		 GridPane.setConstraints(createBook_bookstatus_cb, 1, 3);
		 operationPanels.put("createBook", createBook);
		 
		 // ==================== GridPane_queryBook ====================
		 GridPane queryBook = new GridPane();
		 queryBook.setHgap(4);
		 queryBook.setVgap(6);
		 queryBook.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryBook_content = queryBook.getChildren();
		 Label queryBook_bookid_label = new Label("bookid:");
		 queryBook_bookid_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryBook_content.add(queryBook_bookid_label);
		 GridPane.setConstraints(queryBook_bookid_label, 0, 0);
		 
		 queryBook_bookid_t = new TextField();
		 queryBook_content.add(queryBook_bookid_t);
		 queryBook_bookid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryBook_bookid_t, 1, 0);
		 operationPanels.put("queryBook", queryBook);
		 
		 // ==================== GridPane_deleteBook ====================
		 GridPane deleteBook = new GridPane();
		 deleteBook.setHgap(4);
		 deleteBook.setVgap(6);
		 deleteBook.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteBook_content = deleteBook.getChildren();
		 Label deleteBook_bookid_label = new Label("bookid:");
		 deleteBook_bookid_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteBook_content.add(deleteBook_bookid_label);
		 GridPane.setConstraints(deleteBook_bookid_label, 0, 0);
		 
		 deleteBook_bookid_t = new TextField();
		 deleteBook_content.add(deleteBook_bookid_t);
		 deleteBook_bookid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteBook_bookid_t, 1, 0);
		 operationPanels.put("deleteBook", deleteBook);
		 
		 // ==================== GridPane_modifyBook ====================
		 GridPane modifyBook = new GridPane();
		 modifyBook.setHgap(4);
		 modifyBook.setVgap(6);
		 modifyBook.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyBook_content = modifyBook.getChildren();
		 Label modifyBook_bookid_label = new Label("bookid:");
		 modifyBook_bookid_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBook_content.add(modifyBook_bookid_label);
		 GridPane.setConstraints(modifyBook_bookid_label, 0, 0);
		 
		 modifyBook_bookid_t = new TextField();
		 modifyBook_content.add(modifyBook_bookid_t);
		 modifyBook_bookid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBook_bookid_t, 1, 0);
		 Label modifyBook_booktitle_label = new Label("booktitle:");
		 modifyBook_booktitle_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBook_content.add(modifyBook_booktitle_label);
		 GridPane.setConstraints(modifyBook_booktitle_label, 0, 1);
		 
		 modifyBook_booktitle_t = new TextField();
		 modifyBook_content.add(modifyBook_booktitle_t);
		 modifyBook_booktitle_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBook_booktitle_t, 1, 1);
		 Label modifyBook_authors_label = new Label("authors:");
		 modifyBook_authors_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBook_content.add(modifyBook_authors_label);
		 GridPane.setConstraints(modifyBook_authors_label, 0, 2);
		 
		 modifyBook_authors_t = new TextField();
		 modifyBook_content.add(modifyBook_authors_t);
		 modifyBook_authors_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBook_authors_t, 1, 2);
		 Label modifyBook_bookstatus_label = new Label("bookstatus:");
		 modifyBook_bookstatus_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBook_content.add(modifyBook_bookstatus_label);
		 GridPane.setConstraints(modifyBook_bookstatus_label, 0, 3);
		 
		 modifyBook_bookstatus_cb = new ChoiceBox();
modifyBook_bookstatus_cb.getItems().add("BORROWED");
modifyBook_bookstatus_cb.getItems().add("STOCKED");
		 modifyBook_bookstatus_cb.getSelectionModel().selectFirst();
		 modifyBook_content.add(modifyBook_bookstatus_cb);
		 GridPane.setConstraints(modifyBook_bookstatus_cb, 1, 3);
		 operationPanels.put("modifyBook", modifyBook);
		 
		 // ==================== GridPane_createBookBorrow ====================
		 GridPane createBookBorrow = new GridPane();
		 createBookBorrow.setHgap(4);
		 createBookBorrow.setVgap(6);
		 createBookBorrow.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createBookBorrow_content = createBookBorrow.getChildren();
		 Label createBookBorrow_id_label = new Label("id:");
		 createBookBorrow_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBookBorrow_content.add(createBookBorrow_id_label);
		 GridPane.setConstraints(createBookBorrow_id_label, 0, 0);
		 
		 createBookBorrow_id_t = new TextField();
		 createBookBorrow_content.add(createBookBorrow_id_t);
		 createBookBorrow_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBookBorrow_id_t, 1, 0);
		 Label createBookBorrow_userid_label = new Label("userid:");
		 createBookBorrow_userid_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBookBorrow_content.add(createBookBorrow_userid_label);
		 GridPane.setConstraints(createBookBorrow_userid_label, 0, 1);
		 
		 createBookBorrow_userid_t = new TextField();
		 createBookBorrow_content.add(createBookBorrow_userid_t);
		 createBookBorrow_userid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBookBorrow_userid_t, 1, 1);
		 Label createBookBorrow_bookid_label = new Label("bookid:");
		 createBookBorrow_bookid_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBookBorrow_content.add(createBookBorrow_bookid_label);
		 GridPane.setConstraints(createBookBorrow_bookid_label, 0, 2);
		 
		 createBookBorrow_bookid_t = new TextField();
		 createBookBorrow_content.add(createBookBorrow_bookid_t);
		 createBookBorrow_bookid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBookBorrow_bookid_t, 1, 2);
		 Label createBookBorrow_borrowtime_label = new Label("borrowtime (yyyy-MM-dd):");
		 createBookBorrow_borrowtime_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBookBorrow_content.add(createBookBorrow_borrowtime_label);
		 GridPane.setConstraints(createBookBorrow_borrowtime_label, 0, 3);
		 
		 createBookBorrow_borrowtime_t = new TextField();
		 createBookBorrow_content.add(createBookBorrow_borrowtime_t);
		 createBookBorrow_borrowtime_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBookBorrow_borrowtime_t, 1, 3);
		 Label createBookBorrow_returntime_label = new Label("returntime (yyyy-MM-dd):");
		 createBookBorrow_returntime_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBookBorrow_content.add(createBookBorrow_returntime_label);
		 GridPane.setConstraints(createBookBorrow_returntime_label, 0, 4);
		 
		 createBookBorrow_returntime_t = new TextField();
		 createBookBorrow_content.add(createBookBorrow_returntime_t);
		 createBookBorrow_returntime_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createBookBorrow_returntime_t, 1, 4);
		 Label createBookBorrow_borrowstatus_label = new Label("borrowstatus:");
		 createBookBorrow_borrowstatus_label.setMinWidth(Region.USE_PREF_SIZE);
		 createBookBorrow_content.add(createBookBorrow_borrowstatus_label);
		 GridPane.setConstraints(createBookBorrow_borrowstatus_label, 0, 5);
		 
		 createBookBorrow_borrowstatus_cb = new ChoiceBox();
createBookBorrow_borrowstatus_cb.getItems().add("BORROWED");
createBookBorrow_borrowstatus_cb.getItems().add("RETURNED");
createBookBorrow_borrowstatus_cb.getItems().add("TIMEOUT");
		 createBookBorrow_borrowstatus_cb.getSelectionModel().selectFirst();
		 createBookBorrow_content.add(createBookBorrow_borrowstatus_cb);
		 GridPane.setConstraints(createBookBorrow_borrowstatus_cb, 1, 5);
		 operationPanels.put("createBookBorrow", createBookBorrow);
		 
		 // ==================== GridPane_queryBookBorrow ====================
		 GridPane queryBookBorrow = new GridPane();
		 queryBookBorrow.setHgap(4);
		 queryBookBorrow.setVgap(6);
		 queryBookBorrow.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryBookBorrow_content = queryBookBorrow.getChildren();
		 Label queryBookBorrow_id_label = new Label("id:");
		 queryBookBorrow_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryBookBorrow_content.add(queryBookBorrow_id_label);
		 GridPane.setConstraints(queryBookBorrow_id_label, 0, 0);
		 
		 queryBookBorrow_id_t = new TextField();
		 queryBookBorrow_content.add(queryBookBorrow_id_t);
		 queryBookBorrow_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryBookBorrow_id_t, 1, 0);
		 operationPanels.put("queryBookBorrow", queryBookBorrow);
		 
		 // ==================== GridPane_deleteBookBorrow ====================
		 GridPane deleteBookBorrow = new GridPane();
		 deleteBookBorrow.setHgap(4);
		 deleteBookBorrow.setVgap(6);
		 deleteBookBorrow.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteBookBorrow_content = deleteBookBorrow.getChildren();
		 Label deleteBookBorrow_id_label = new Label("id:");
		 deleteBookBorrow_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteBookBorrow_content.add(deleteBookBorrow_id_label);
		 GridPane.setConstraints(deleteBookBorrow_id_label, 0, 0);
		 
		 deleteBookBorrow_id_t = new TextField();
		 deleteBookBorrow_content.add(deleteBookBorrow_id_t);
		 deleteBookBorrow_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteBookBorrow_id_t, 1, 0);
		 operationPanels.put("deleteBookBorrow", deleteBookBorrow);
		 
		 // ==================== GridPane_modifyBookBorrow ====================
		 GridPane modifyBookBorrow = new GridPane();
		 modifyBookBorrow.setHgap(4);
		 modifyBookBorrow.setVgap(6);
		 modifyBookBorrow.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyBookBorrow_content = modifyBookBorrow.getChildren();
		 Label modifyBookBorrow_id_label = new Label("id:");
		 modifyBookBorrow_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBookBorrow_content.add(modifyBookBorrow_id_label);
		 GridPane.setConstraints(modifyBookBorrow_id_label, 0, 0);
		 
		 modifyBookBorrow_id_t = new TextField();
		 modifyBookBorrow_content.add(modifyBookBorrow_id_t);
		 modifyBookBorrow_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBookBorrow_id_t, 1, 0);
		 Label modifyBookBorrow_userid_label = new Label("userid:");
		 modifyBookBorrow_userid_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBookBorrow_content.add(modifyBookBorrow_userid_label);
		 GridPane.setConstraints(modifyBookBorrow_userid_label, 0, 1);
		 
		 modifyBookBorrow_userid_t = new TextField();
		 modifyBookBorrow_content.add(modifyBookBorrow_userid_t);
		 modifyBookBorrow_userid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBookBorrow_userid_t, 1, 1);
		 Label modifyBookBorrow_bookid_label = new Label("bookid:");
		 modifyBookBorrow_bookid_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBookBorrow_content.add(modifyBookBorrow_bookid_label);
		 GridPane.setConstraints(modifyBookBorrow_bookid_label, 0, 2);
		 
		 modifyBookBorrow_bookid_t = new TextField();
		 modifyBookBorrow_content.add(modifyBookBorrow_bookid_t);
		 modifyBookBorrow_bookid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBookBorrow_bookid_t, 1, 2);
		 Label modifyBookBorrow_borrowtime_label = new Label("borrowtime (yyyy-MM-dd):");
		 modifyBookBorrow_borrowtime_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBookBorrow_content.add(modifyBookBorrow_borrowtime_label);
		 GridPane.setConstraints(modifyBookBorrow_borrowtime_label, 0, 3);
		 
		 modifyBookBorrow_borrowtime_t = new TextField();
		 modifyBookBorrow_content.add(modifyBookBorrow_borrowtime_t);
		 modifyBookBorrow_borrowtime_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBookBorrow_borrowtime_t, 1, 3);
		 Label modifyBookBorrow_returntime_label = new Label("returntime (yyyy-MM-dd):");
		 modifyBookBorrow_returntime_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBookBorrow_content.add(modifyBookBorrow_returntime_label);
		 GridPane.setConstraints(modifyBookBorrow_returntime_label, 0, 4);
		 
		 modifyBookBorrow_returntime_t = new TextField();
		 modifyBookBorrow_content.add(modifyBookBorrow_returntime_t);
		 modifyBookBorrow_returntime_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyBookBorrow_returntime_t, 1, 4);
		 Label modifyBookBorrow_borrowstatus_label = new Label("borrowstatus:");
		 modifyBookBorrow_borrowstatus_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyBookBorrow_content.add(modifyBookBorrow_borrowstatus_label);
		 GridPane.setConstraints(modifyBookBorrow_borrowstatus_label, 0, 5);
		 
		 modifyBookBorrow_borrowstatus_cb = new ChoiceBox();
modifyBookBorrow_borrowstatus_cb.getItems().add("BORROWED");
modifyBookBorrow_borrowstatus_cb.getItems().add("RETURNED");
modifyBookBorrow_borrowstatus_cb.getItems().add("TIMEOUT");
		 modifyBookBorrow_borrowstatus_cb.getSelectionModel().selectFirst();
		 modifyBookBorrow_content.add(modifyBookBorrow_borrowstatus_cb);
		 GridPane.setConstraints(modifyBookBorrow_borrowstatus_cb, 1, 5);
		 operationPanels.put("modifyBookBorrow", modifyBookBorrow);
		 
	}	

	public void actorTreeViewBinding() {
		
		 

		TreeItem<String> treeRootadministrator = new TreeItem<String>("Root node");
		
		TreeItem<String> subTreeRoot_User = new TreeItem<String>("manageUser");
					 		subTreeRoot_User.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createUser"),
					 			 		new TreeItem<String>("queryUser"),
					 			 		new TreeItem<String>("modifyUser"),
					 			 		new TreeItem<String>("deleteUser")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_Book = new TreeItem<String>("manageBook");
					 		subTreeRoot_Book.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createBook"),
					 			 		new TreeItem<String>("queryBook"),
					 			 		new TreeItem<String>("modifyBook"),
					 			 		new TreeItem<String>("deleteBook")					 			 	
					 			 	));							 		
		TreeItem<String> subTreeRoot_BookBorrow = new TreeItem<String>("manageBookBorrow");
					 		subTreeRoot_BookBorrow.getChildren().addAll(Arrays.asList(					 		
					 			 		new TreeItem<String>("createBookBorrow"),
					 			 		new TreeItem<String>("queryBookBorrow"),
					 			 		new TreeItem<String>("modifyBookBorrow"),
					 			 		new TreeItem<String>("deleteBookBorrow")					 			 	
					 			 	));							 		
		
					 			
						 		
		treeRootadministrator.getChildren().addAll(Arrays.asList(
		 	subTreeRoot_User,
		 	subTreeRoot_Book,
		 	subTreeRoot_BookBorrow
				));	
				
	 			treeRootadministrator.setExpanded(true);

		actor_treeview_administrator.setShowRoot(false);
		actor_treeview_administrator.setRoot(treeRootadministrator);
	 		
		actor_treeview_administrator.getSelectionModel().selectedItemProperty().addListener(
		 				 (observable, oldValue, newValue) -> { 
		 				 								
		 				 							 //clear the previous return
		 											 operation_return_pane.setContent(new Label());
		 											 
		 				 							 clickedOp = newValue.getValue();
		 				 							 GridPane op = operationPanels.get(clickedOp);
		 				 							 VBox vb = opInvariantPanel.get(clickedOp);
		 				 							 
		 				 							 //op pannel
		 				 							 if (op != null) {
		 				 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
		 				 								 
		 				 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
		 				 								 choosenOperation = new LinkedList<TextField>();
		 				 								 for (Node n : l) {
		 				 								 	 if (n instanceof TextField) {
		 				 								 	 	choosenOperation.add((TextField)n);
		 				 								 	  }
		 				 								 }
		 				 								 
		 				 								 definition.setText(definitions_map.get(newValue.getValue()));
		 				 								 precondition.setText(preconditions_map.get(newValue.getValue()));
		 				 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
		 				 								 
		 				 						     }
		 				 							 else {
		 				 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
		 				 								 l.setPadding(new Insets(8, 8, 8, 8));
		 				 								 operation_paras.setContent(l);
		 				 							 }	
		 				 							 
		 				 							 //op invariants
		 				 							 if (vb != null) {
		 				 							 	ScrollPane scrollPane = new ScrollPane(vb);
		 				 							 	scrollPane.setFitToWidth(true);
		 				 							 	invariants_panes.setMaxHeight(200); 
		 				 							 	//all_invariant_pane.setContent(scrollPane);	
		 				 							 	
		 				 							 	invariants_panes.setContent(scrollPane);
		 				 							 } else {
		 				 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
		 				 							     l.setPadding(new Insets(8, 8, 8, 8));
		 				 							     invariants_panes.setContent(l);
		 				 							 }
		 				 							 
		 				 							 //reset pre- and post-conditions area color
		 				 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
		 				 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
		 				 							 //reset condition panel title
		 				 							 precondition_pane.setText("Precondition");
		 				 							 postcondition_pane.setText("Postcondition");
		 				 						} 
		 				 				);

		
		
		 
		TreeItem<String> treeRootcustomer = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_register = new TreeItem<String>("register");
			subTreeRoot_register.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("inputUser"),
					 	new TreeItem<String>("verification")
				 		));	
			TreeItem<String> subTreeRoot_login = new TreeItem<String>("login");
			subTreeRoot_login.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("inputUsername"),
					 	new TreeItem<String>("inputPassword")
				 		));	
			TreeItem<String> subTreeRoot_searchBook = new TreeItem<String>("searchBook");
			subTreeRoot_searchBook.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("login"),
					 	new TreeItem<String>("inputtitle"),
					 	new TreeItem<String>("brows"),
					 	new TreeItem<String>("selectbook")
				 		));	
			TreeItem<String> subTreeRoot_borrowBook = new TreeItem<String>("borrowBook");
			subTreeRoot_borrowBook.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("choosebook"),
					 	new TreeItem<String>("selecttime"),
					 	new TreeItem<String>("confirm")
				 		));	
		
		treeRootcustomer.getChildren().addAll(Arrays.asList(
			subTreeRoot_register,
			subTreeRoot_login,
			subTreeRoot_searchBook,
			subTreeRoot_borrowBook
					));
		
		treeRootcustomer.setExpanded(true);

		actor_treeview_customer.setShowRoot(false);
		actor_treeview_customer.setRoot(treeRootcustomer);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_customer.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootlibrarian = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_returnBook = new TreeItem<String>("returnBook");
			subTreeRoot_returnBook.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("inputuser"),
					 	new TreeItem<String>("inputbook"),
					 	new TreeItem<String>("confirminformation")
				 		));	
		
		treeRootlibrarian.getChildren().addAll(Arrays.asList(
			subTreeRoot_returnBook
					));
		
		treeRootlibrarian.setExpanded(true);

		actor_treeview_librarian.setShowRoot(false);
		actor_treeview_librarian.setRoot(treeRootlibrarian);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_librarian.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
	}

	/**
	*    Execute Operation
	*/
	@FXML
	public void execute(ActionEvent event) {
		
		switch (clickedOp) {
		case "inputUser" : inputUser(); break;
		case "verification" : verification(); break;
		case "inputUsername" : inputUsername(); break;
		case "inputPassword" : inputPassword(); break;
		case "login" : login(); break;
		case "inputtitle" : inputtitle(); break;
		case "brows" : brows(); break;
		case "selectbook" : selectbook(); break;
		case "choosebook" : choosebook(); break;
		case "selecttime" : selecttime(); break;
		case "confirm" : confirm(); break;
		case "inputuser" : inputuser(); break;
		case "inputbook" : inputbook(); break;
		case "confirminformation" : confirminformation(); break;
		case "createUser" : createUser(); break;
		case "queryUser" : queryUser(); break;
		case "modifyUser" : modifyUser(); break;
		case "deleteUser" : deleteUser(); break;
		case "createBook" : createBook(); break;
		case "queryBook" : queryBook(); break;
		case "deleteBook" : deleteBook(); break;
		case "modifyBook" : modifyBook(); break;
		case "createBookBorrow" : createBookBorrow(); break;
		case "queryBookBorrow" : queryBookBorrow(); break;
		case "deleteBookBorrow" : deleteBookBorrow(); break;
		case "modifyBookBorrow" : modifyBookBorrow(); break;
		
		}
		
		System.out.println("execute buttion clicked");
		
		//checking relevant invariants
		opInvairantPanelUpdate();
	}

	/**
	*    Refresh All
	*/		
	@FXML
	public void refresh(ActionEvent event) {
		
		refreshAll();
		System.out.println("refresh all");
	}		
	
	/**
	*    Save All
	*/			
	@FXML
	public void save(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save State to File");
		fileChooser.setInitialFileName("*.state");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showSaveDialog(stage);
		
		if (file != null) {
			System.out.println("save state to file " + file.getAbsolutePath());				
			EntityManager.save(file);
		}
	}
	
	/**
	*    Load All
	*/			
	@FXML
	public void load(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open State File");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showOpenDialog(stage);
		
		if (file != null) {
			System.out.println("choose file" + file.getAbsolutePath());
			EntityManager.load(file); 
		}
		
		//refresh GUI after load data
		refreshAll();
	}
	
	
	//precondition unsat dialog
	public void preconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Precondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}
	
	//postcondition unsat dialog
	public void postconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Postcondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}

	public void thirdpartyServiceUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("third party service is exception");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}		
	
	
	public void inputUser() {
		
		System.out.println("execute inputUser");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: inputUser in service: RegisterService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(registerservice_service.inputUser(
			inputUser_username_t.getText(),
			inputUser_password_t.getText(),
			inputUser_email_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void verification() {
		
		System.out.println("execute verification");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: verification in service: RegisterService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(registerservice_service.verification(
			verification_code_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void inputUsername() {
		
		System.out.println("execute inputUsername");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: inputUsername in service: LoginService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(loginservice_service.inputUsername(
			inputUsername_name_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void inputPassword() {
		
		System.out.println("execute inputPassword");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: inputPassword in service: LoginService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(loginservice_service.inputPassword(
			inputPassword_password_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void login() {
		
		System.out.println("execute login");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: login in service: SearchBookService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(searchbookservice_service.login(
			login_username_t.getText(),
			login_password_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void inputtitle() {
		
		System.out.println("execute inputtitle");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: inputtitle in service: SearchBookService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(searchbookservice_service.inputtitle(
			inputtitle_title_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void brows() {
		
		System.out.println("execute brows");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: brows in service: SearchBookService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(searchbookservice_service.brows(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void selectbook() {
		
		System.out.println("execute selectbook");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: selectbook in service: SearchBookService ");
		
		try {
			//invoke op with parameters
				Book r = searchbookservice_service.selectbook(
				Integer.valueOf(selectbook_bookid_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableBook = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableBook_BookId = new TableColumn<Map<String, String>, String>("BookId");
				tableBook_BookId.setMinWidth("BookId".length()*10);
				tableBook_BookId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("BookId"));
				    }
				});	
				tableBook.getColumns().add(tableBook_BookId);
				TableColumn<Map<String, String>, String> tableBook_Booktitle = new TableColumn<Map<String, String>, String>("Booktitle");
				tableBook_Booktitle.setMinWidth("Booktitle".length()*10);
				tableBook_Booktitle.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Booktitle"));
				    }
				});	
				tableBook.getColumns().add(tableBook_Booktitle);
				TableColumn<Map<String, String>, String> tableBook_Authors = new TableColumn<Map<String, String>, String>("Authors");
				tableBook_Authors.setMinWidth("Authors".length()*10);
				tableBook_Authors.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Authors"));
				    }
				});	
				tableBook.getColumns().add(tableBook_Authors);
				TableColumn<Map<String, String>, String> tableBook_BookStatus = new TableColumn<Map<String, String>, String>("BookStatus");
				tableBook_BookStatus.setMinWidth("BookStatus".length()*10);
				tableBook_BookStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("BookStatus"));
				    }
				});	
				tableBook.getColumns().add(tableBook_BookStatus);
				
				ObservableList<Map<String, String>> dataBook = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("BookId", String.valueOf(r.getBookId()));
					if (r.getBooktitle() != null)
						unit.put("Booktitle", String.valueOf(r.getBooktitle()));
					else
						unit.put("Booktitle", "");
					if (r.getAuthors() != null)
						unit.put("Authors", String.valueOf(r.getAuthors()));
					else
						unit.put("Authors", "");
					unit.put("BookStatus", String.valueOf(r.getBookStatus()));
					dataBook.add(unit);
				
				
				tableBook.setItems(dataBook);
				operation_return_pane.setContent(tableBook);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void choosebook() {
		
		System.out.println("execute choosebook");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: choosebook in service: BorrowBookService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(borrowbookservice_service.choosebook(
			Integer.valueOf(choosebook_bookid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void selecttime() {
		
		System.out.println("execute selecttime");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: selecttime in service: BorrowBookService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(borrowbookservice_service.selecttime(
			LocalDate.parse(selecttime_time_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void confirm() {
		
		System.out.println("execute confirm");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: confirm in service: BorrowBookService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(borrowbookservice_service.confirm(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void inputuser() {
		
		System.out.println("execute inputuser");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: inputuser in service: ReturnBookService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(returnbookservice_service.inputuser(
			inputuser_username_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void inputbook() {
		
		System.out.println("execute inputbook");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: inputbook in service: ReturnBookService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(returnbookservice_service.inputbook(
			inputbook_bookname_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void confirminformation() {
		
		System.out.println("execute confirminformation");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: confirminformation in service: ReturnBookService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(returnbookservice_service.confirminformation(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createUser() {
		
		System.out.println("execute createUser");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createUser in service: ManageUserCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageusercrudservice_service.createUser(
			Integer.valueOf(createUser_userid_t.getText()),
			createUser_username_t.getText(),
			createUser_password_t.getText(),
			createUser_mailbox_t.getText(),
			UserStatus.valueOf(createUser_userstatus_cb.getSelectionModel().getSelectedItem().toString())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryUser() {
		
		System.out.println("execute queryUser");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryUser in service: ManageUserCRUDService ");
		
		try {
			//invoke op with parameters
				User r = manageusercrudservice_service.queryUser(
				Integer.valueOf(queryUser_userid_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableUser = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableUser_UserId = new TableColumn<Map<String, String>, String>("UserId");
				tableUser_UserId.setMinWidth("UserId".length()*10);
				tableUser_UserId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("UserId"));
				    }
				});	
				tableUser.getColumns().add(tableUser_UserId);
				TableColumn<Map<String, String>, String> tableUser_Username = new TableColumn<Map<String, String>, String>("Username");
				tableUser_Username.setMinWidth("Username".length()*10);
				tableUser_Username.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Username"));
				    }
				});	
				tableUser.getColumns().add(tableUser_Username);
				TableColumn<Map<String, String>, String> tableUser_Password = new TableColumn<Map<String, String>, String>("Password");
				tableUser_Password.setMinWidth("Password".length()*10);
				tableUser_Password.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Password"));
				    }
				});	
				tableUser.getColumns().add(tableUser_Password);
				TableColumn<Map<String, String>, String> tableUser_Mailbox = new TableColumn<Map<String, String>, String>("Mailbox");
				tableUser_Mailbox.setMinWidth("Mailbox".length()*10);
				tableUser_Mailbox.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Mailbox"));
				    }
				});	
				tableUser.getColumns().add(tableUser_Mailbox);
				TableColumn<Map<String, String>, String> tableUser_UserStatus = new TableColumn<Map<String, String>, String>("UserStatus");
				tableUser_UserStatus.setMinWidth("UserStatus".length()*10);
				tableUser_UserStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("UserStatus"));
				    }
				});	
				tableUser.getColumns().add(tableUser_UserStatus);
				
				ObservableList<Map<String, String>> dataUser = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("UserId", String.valueOf(r.getUserId()));
					if (r.getUsername() != null)
						unit.put("Username", String.valueOf(r.getUsername()));
					else
						unit.put("Username", "");
					if (r.getPassword() != null)
						unit.put("Password", String.valueOf(r.getPassword()));
					else
						unit.put("Password", "");
					if (r.getMailbox() != null)
						unit.put("Mailbox", String.valueOf(r.getMailbox()));
					else
						unit.put("Mailbox", "");
					unit.put("UserStatus", String.valueOf(r.getUserStatus()));
					dataUser.add(unit);
				
				
				tableUser.setItems(dataUser);
				operation_return_pane.setContent(tableUser);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyUser() {
		
		System.out.println("execute modifyUser");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyUser in service: ManageUserCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageusercrudservice_service.modifyUser(
			Integer.valueOf(modifyUser_userid_t.getText()),
			modifyUser_username_t.getText(),
			modifyUser_password_t.getText(),
			modifyUser_mailbox_t.getText(),
			UserStatus.valueOf(modifyUser_userstatus_cb.getSelectionModel().getSelectedItem().toString())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteUser() {
		
		System.out.println("execute deleteUser");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteUser in service: ManageUserCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageusercrudservice_service.deleteUser(
			Integer.valueOf(deleteUser_userid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createBook() {
		
		System.out.println("execute createBook");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createBook in service: ManageBookCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managebookcrudservice_service.createBook(
			Integer.valueOf(createBook_bookid_t.getText()),
			createBook_booktitle_t.getText(),
			createBook_authors_t.getText(),
			BookStatus.valueOf(createBook_bookstatus_cb.getSelectionModel().getSelectedItem().toString())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryBook() {
		
		System.out.println("execute queryBook");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryBook in service: ManageBookCRUDService ");
		
		try {
			//invoke op with parameters
				Book r = managebookcrudservice_service.queryBook(
				Integer.valueOf(queryBook_bookid_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableBook = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableBook_BookId = new TableColumn<Map<String, String>, String>("BookId");
				tableBook_BookId.setMinWidth("BookId".length()*10);
				tableBook_BookId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("BookId"));
				    }
				});	
				tableBook.getColumns().add(tableBook_BookId);
				TableColumn<Map<String, String>, String> tableBook_Booktitle = new TableColumn<Map<String, String>, String>("Booktitle");
				tableBook_Booktitle.setMinWidth("Booktitle".length()*10);
				tableBook_Booktitle.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Booktitle"));
				    }
				});	
				tableBook.getColumns().add(tableBook_Booktitle);
				TableColumn<Map<String, String>, String> tableBook_Authors = new TableColumn<Map<String, String>, String>("Authors");
				tableBook_Authors.setMinWidth("Authors".length()*10);
				tableBook_Authors.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Authors"));
				    }
				});	
				tableBook.getColumns().add(tableBook_Authors);
				TableColumn<Map<String, String>, String> tableBook_BookStatus = new TableColumn<Map<String, String>, String>("BookStatus");
				tableBook_BookStatus.setMinWidth("BookStatus".length()*10);
				tableBook_BookStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("BookStatus"));
				    }
				});	
				tableBook.getColumns().add(tableBook_BookStatus);
				
				ObservableList<Map<String, String>> dataBook = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("BookId", String.valueOf(r.getBookId()));
					if (r.getBooktitle() != null)
						unit.put("Booktitle", String.valueOf(r.getBooktitle()));
					else
						unit.put("Booktitle", "");
					if (r.getAuthors() != null)
						unit.put("Authors", String.valueOf(r.getAuthors()));
					else
						unit.put("Authors", "");
					unit.put("BookStatus", String.valueOf(r.getBookStatus()));
					dataBook.add(unit);
				
				
				tableBook.setItems(dataBook);
				operation_return_pane.setContent(tableBook);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteBook() {
		
		System.out.println("execute deleteBook");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteBook in service: ManageBookCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managebookcrudservice_service.deleteBook(
			Integer.valueOf(deleteBook_bookid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyBook() {
		
		System.out.println("execute modifyBook");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyBook in service: ManageBookCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managebookcrudservice_service.modifyBook(
			Integer.valueOf(modifyBook_bookid_t.getText()),
			modifyBook_booktitle_t.getText(),
			modifyBook_authors_t.getText(),
			BookStatus.valueOf(modifyBook_bookstatus_cb.getSelectionModel().getSelectedItem().toString())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createBookBorrow() {
		
		System.out.println("execute createBookBorrow");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createBookBorrow in service: ManageBookBorrowCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managebookborrowcrudservice_service.createBookBorrow(
			Integer.valueOf(createBookBorrow_id_t.getText()),
			Integer.valueOf(createBookBorrow_userid_t.getText()),
			Integer.valueOf(createBookBorrow_bookid_t.getText()),
			LocalDate.parse(createBookBorrow_borrowtime_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						,
			LocalDate.parse(createBookBorrow_returntime_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						,
			BorrowStatus.valueOf(createBookBorrow_borrowstatus_cb.getSelectionModel().getSelectedItem().toString())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryBookBorrow() {
		
		System.out.println("execute queryBookBorrow");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryBookBorrow in service: ManageBookBorrowCRUDService ");
		
		try {
			//invoke op with parameters
				BookBorrow r = managebookborrowcrudservice_service.queryBookBorrow(
				Integer.valueOf(queryBookBorrow_id_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableBookBorrow = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableBookBorrow_Id = new TableColumn<Map<String, String>, String>("Id");
				tableBookBorrow_Id.setMinWidth("Id".length()*10);
				tableBookBorrow_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
				    }
				});	
				tableBookBorrow.getColumns().add(tableBookBorrow_Id);
				TableColumn<Map<String, String>, String> tableBookBorrow_Userid = new TableColumn<Map<String, String>, String>("Userid");
				tableBookBorrow_Userid.setMinWidth("Userid".length()*10);
				tableBookBorrow_Userid.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Userid"));
				    }
				});	
				tableBookBorrow.getColumns().add(tableBookBorrow_Userid);
				TableColumn<Map<String, String>, String> tableBookBorrow_Bookid = new TableColumn<Map<String, String>, String>("Bookid");
				tableBookBorrow_Bookid.setMinWidth("Bookid".length()*10);
				tableBookBorrow_Bookid.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Bookid"));
				    }
				});	
				tableBookBorrow.getColumns().add(tableBookBorrow_Bookid);
				TableColumn<Map<String, String>, String> tableBookBorrow_BorrowTime = new TableColumn<Map<String, String>, String>("BorrowTime");
				tableBookBorrow_BorrowTime.setMinWidth("BorrowTime".length()*10);
				tableBookBorrow_BorrowTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("BorrowTime"));
				    }
				});	
				tableBookBorrow.getColumns().add(tableBookBorrow_BorrowTime);
				TableColumn<Map<String, String>, String> tableBookBorrow_ReturnTime = new TableColumn<Map<String, String>, String>("ReturnTime");
				tableBookBorrow_ReturnTime.setMinWidth("ReturnTime".length()*10);
				tableBookBorrow_ReturnTime.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("ReturnTime"));
				    }
				});	
				tableBookBorrow.getColumns().add(tableBookBorrow_ReturnTime);
				TableColumn<Map<String, String>, String> tableBookBorrow_BorrowStatus = new TableColumn<Map<String, String>, String>("BorrowStatus");
				tableBookBorrow_BorrowStatus.setMinWidth("BorrowStatus".length()*10);
				tableBookBorrow_BorrowStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("BorrowStatus"));
				    }
				});	
				tableBookBorrow.getColumns().add(tableBookBorrow_BorrowStatus);
				
				ObservableList<Map<String, String>> dataBookBorrow = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Id", String.valueOf(r.getId()));
					unit.put("Userid", String.valueOf(r.getUserid()));
					unit.put("Bookid", String.valueOf(r.getBookid()));
					if (r.getBorrowTime() != null)
						unit.put("BorrowTime", r.getBorrowTime().format(dateformatter));
					else
						unit.put("BorrowTime", "");
					if (r.getReturnTime() != null)
						unit.put("ReturnTime", r.getReturnTime().format(dateformatter));
					else
						unit.put("ReturnTime", "");
					unit.put("BorrowStatus", String.valueOf(r.getBorrowStatus()));
					dataBookBorrow.add(unit);
				
				
				tableBookBorrow.setItems(dataBookBorrow);
				operation_return_pane.setContent(tableBookBorrow);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteBookBorrow() {
		
		System.out.println("execute deleteBookBorrow");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteBookBorrow in service: ManageBookBorrowCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managebookborrowcrudservice_service.deleteBookBorrow(
			Integer.valueOf(deleteBookBorrow_id_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyBookBorrow() {
		
		System.out.println("execute modifyBookBorrow");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyBookBorrow in service: ManageBookBorrowCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(managebookborrowcrudservice_service.modifyBookBorrow(
			Integer.valueOf(modifyBookBorrow_id_t.getText()),
			Integer.valueOf(modifyBookBorrow_userid_t.getText()),
			Integer.valueOf(modifyBookBorrow_bookid_t.getText()),
			LocalDate.parse(modifyBookBorrow_borrowtime_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						,
			LocalDate.parse(modifyBookBorrow_returntime_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						,
			BorrowStatus.valueOf(modifyBookBorrow_borrowstatus_cb.getSelectionModel().getSelectedItem().toString())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}




	//select object index
	int objectindex;
	
	@FXML
	TabPane mainPane;

	@FXML
	TextArea log;
	
	@FXML
	TreeView<String> actor_treeview_customer;
	@FXML
	TreeView<String> actor_treeview_librarian;
	
	@FXML
	TreeView<String> actor_treeview_administrator;


	@FXML
	TextArea definition;
	@FXML
	TextArea precondition;
	@FXML
	TextArea postcondition;
	@FXML
	TextArea invariants;
	
	@FXML
	TitledPane precondition_pane;
	@FXML
	TitledPane postcondition_pane;
	
	//chosen operation textfields
	List<TextField> choosenOperation;
	String clickedOp;
		
	@FXML
	TableView<ClassInfo> class_statisic;
	@FXML
	TableView<AssociationInfo> association_statisic;
	
	Map<String, ObservableList<AssociationInfo>> allassociationData;
	ObservableList<ClassInfo> classInfodata;
	
	BookBorrowSystem bookborrowsystem_service;
	ThirdPartyServices thirdpartyservices_service;
	RegisterService registerservice_service;
	LoginService loginservice_service;
	SearchBookService searchbookservice_service;
	BorrowBookService borrowbookservice_service;
	ReturnBookService returnbookservice_service;
	ManageUserCRUDService manageusercrudservice_service;
	ManageBookCRUDService managebookcrudservice_service;
	ManageBookBorrowCRUDService managebookborrowcrudservice_service;
	
	ClassInfo user;
	ClassInfo book;
	ClassInfo bookborrow;
	ClassInfo librarian;
	ClassInfo verificationcode;
		
	@FXML
	TitledPane object_statics;
	Map<String, TableView> allObjectTables;
	
	@FXML
	TitledPane operation_paras;
	
	@FXML
	TitledPane operation_return_pane;
	
	@FXML 
	TitledPane all_invariant_pane;
	
	@FXML
	TitledPane invariants_panes;
	
	Map<String, GridPane> operationPanels;
	Map<String, VBox> opInvariantPanel;
	
	//all textfiled or eumntity
	TextField inputUser_username_t;
	TextField inputUser_password_t;
	TextField inputUser_email_t;
	TextField verification_code_t;
	TextField inputUsername_name_t;
	TextField inputPassword_password_t;
	TextField login_username_t;
	TextField login_password_t;
	TextField inputtitle_title_t;
	TextField selectbook_bookid_t;
	TextField choosebook_bookid_t;
	TextField selecttime_time_t;
	TextField inputuser_username_t;
	TextField inputbook_bookname_t;
	TextField createUser_userid_t;
	TextField createUser_username_t;
	TextField createUser_password_t;
	TextField createUser_mailbox_t;
	ChoiceBox createUser_userstatus_cb;
	TextField queryUser_userid_t;
	TextField modifyUser_userid_t;
	TextField modifyUser_username_t;
	TextField modifyUser_password_t;
	TextField modifyUser_mailbox_t;
	ChoiceBox modifyUser_userstatus_cb;
	TextField deleteUser_userid_t;
	TextField createBook_bookid_t;
	TextField createBook_booktitle_t;
	TextField createBook_authors_t;
	ChoiceBox createBook_bookstatus_cb;
	TextField queryBook_bookid_t;
	TextField deleteBook_bookid_t;
	TextField modifyBook_bookid_t;
	TextField modifyBook_booktitle_t;
	TextField modifyBook_authors_t;
	ChoiceBox modifyBook_bookstatus_cb;
	TextField createBookBorrow_id_t;
	TextField createBookBorrow_userid_t;
	TextField createBookBorrow_bookid_t;
	TextField createBookBorrow_borrowtime_t;
	TextField createBookBorrow_returntime_t;
	ChoiceBox createBookBorrow_borrowstatus_cb;
	TextField queryBookBorrow_id_t;
	TextField deleteBookBorrow_id_t;
	TextField modifyBookBorrow_id_t;
	TextField modifyBookBorrow_userid_t;
	TextField modifyBookBorrow_bookid_t;
	TextField modifyBookBorrow_borrowtime_t;
	TextField modifyBookBorrow_returntime_t;
	ChoiceBox modifyBookBorrow_borrowstatus_cb;
	
	HashMap<String, String> definitions_map;
	HashMap<String, String> preconditions_map;
	HashMap<String, String> postconditions_map;
	HashMap<String, String> invariants_map;
	LinkedHashMap<String, String> service_invariants_map;
	LinkedHashMap<String, String> entity_invariants_map;
	LinkedHashMap<String, Label> service_invariants_label_map;
	LinkedHashMap<String, Label> entity_invariants_label_map;
	LinkedHashMap<String, Label> op_entity_invariants_label_map;
	LinkedHashMap<String, Label> op_service_invariants_label_map;
	

	
}
