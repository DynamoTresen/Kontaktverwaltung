package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable, test {

    @FXML
    private TextField nameTextField;

    @FXML
    private ComboBox<AsdEmployees> asdEmplComb;

    @FXML
    private ComboBox<Area> areaComb;

    @FXML
    private TableView<Families> tableView;

    @FXML
    private TableColumn<Families, String> col1;

    @FXML
    private TableColumn<Families, String> col2;

    @FXML
    private TableColumn<Families, String> col3;

    ObservableList<Families> list = FXCollections.observableArrayList(

            loadFamilyFromDatabase()

    );
    @FXML
    private TextField mjFirstNameTextField;

    @FXML
    private TextField mjLastNameTextField;

    @FXML
    private TextField mjPhoneTextField;


    public Main main;

    public void setMain(Main main) {
        this.main = main;
        initComboBox();
        loadDataFromDatabase(asdEmplComb);
        initComboBoxArea();
        loadAreasFromDatabase();
    }

    @FXML
    public void handleAddMjEmpl(){
        DBController dbc = new DBController();
        Connection conn = dbc.getConnection();

        String sql = "INSERT INTO mj_employees(first_name,last_name,phone_number) " +
                "VALUES(?,?,?)";
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            String firstName = mjFirstNameTextField.getText();
            String lastName = mjLastNameTextField.getText();
            String phoneNumber = mjPhoneTextField.getText();

            stmt.setString(1,firstName);
            stmt.setString(2,lastName);
            stmt.setString(3,phoneNumber);

            stmt.addBatch();
            stmt.executeBatch();

            mjFirstNameTextField.clear();
            mjLastNameTextField.clear();
            mjPhoneTextField.clear();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteFamily() {
        DBController dbc = new DBController();
        Families selectedFamily = tableView.getSelectionModel().getSelectedItem();

        if(selectedFamily != null) {
            list.remove(selectedFamily);

            try {
                Connection conn = dbc.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM families WHERE ID = ?");

                stmt.setInt(1, selectedFamily.getId());

                stmt.executeUpdate();

                refreshTableView();
            }catch(SQLException e) {
                e.getStackTrace();
            }
        }

    }

    @FXML
    public void handleAddFamily() {
        DBController dbc = new DBController();
        Connection conn = dbc.getConnection();

        String sql = "INSERT INTO families(name,area_id,asd_emp)"
                + "VALUES(?,?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            AsdEmployees asd = asdEmplComb.getValue();
            Area area = areaComb.getValue();
            stmt.setString(1,nameTextField.getText());
            stmt.setInt(2,area.getId());
            stmt.setInt(3,asd.getId());

            stmt.addBatch();
            stmt.executeBatch();

            refreshTableView();

            asdEmplComb.setValue(null);
            areaComb.setValue(null);
            nameTextField.clear();

        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void initComboBox() {

        asdEmplComb.setCellFactory(new Callback<ListView<AsdEmployees>,ListCell<AsdEmployees>>(){
            @Override
            public ListCell<AsdEmployees> call(ListView<AsdEmployees> param){
                return new ListCell<AsdEmployees>() {
                    @Override
                    protected void updateItem(AsdEmployees asdEmployees, boolean empty) {
                        super.updateItem(asdEmployees, empty);
                        if(asdEmployees != null) {
                            setText(asdEmployees.getName());
                        }else {
                            setText("");
                        }
                    }
                };
            }
        });
    }



    public void initComboBoxArea() {

        areaComb.setCellFactory(new Callback<ListView<Area>,ListCell<Area>>(){
            public ListCell<Area> call(ListView<Area> param){
                return new ListCell<Area>() {
                    protected void updateItem(Area area, boolean empty) {
                        super.updateItem(area, empty);
                        if(area != null) {
                            setText(area.getName());
                        }else {
                            setText("");
                        }
                    }
                };
            }
        });

    }

    public void loadAreasFromDatabase() {
        DBController dbc = new DBController();
        Connection con = dbc.getConnection();

        try {
            String query = "SELECT id, name FROM areas";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                Area area = new Area(id,name);

                areaComb.getItems().add(area);

            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromDatabase(ComboBox<AsdEmployees> comboBox) {

        DBController dbc = new DBController();
        Connection con = dbc.getConnection();

        try{
            String query = "SELECT name,id FROM asd_employees";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("id");

                AsdEmployees asdEmployees = new AsdEmployees(name,id);
                comboBox.getItems().add(asdEmployees);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Families> loadFamilyFromDatabase() {
        DBController dbc = new DBController();
        Connection con = dbc.getConnection();
        List<Families> familiesList = new ArrayList<>();
        try {
            String query = "SELECT f.id,f.name,ae.name as asd_name ,area_id,a.name as area_name, asd_emp "
                    + "FROM families f"
                    + " INNER JOIN asd_employees ae ON  f.asd_emp = ae.id"
                    + " INNER JOIN areas a ON f.area_id = a.id";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String asdName = rs.getString("asd_name");
                String areaName = rs.getString("area_name");
                int areaId	= rs.getInt("area_id");
                int asdEmp  = rs.getInt("asd_emp");
                Families fam = new Families(id, name, areaId, asdEmp,asdName,areaName);
                familiesList.add(fam);
            }

            return familiesList;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return familiesList;

    }

    public void refreshTableView() {
        ObservableList<Families> list = FXCollections.observableArrayList(
                loadFamilyFromDatabase()
        );

        tableView.setItems(list);
        tableView.refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(new PropertyValueFactory<Families,String>("name"));
        col2.setCellValueFactory(new PropertyValueFactory<Families,String>("areaName"));
        col3.setCellValueFactory(new PropertyValueFactory<Families,String>("asdName"));

        tableView.setItems(list);
    }
}