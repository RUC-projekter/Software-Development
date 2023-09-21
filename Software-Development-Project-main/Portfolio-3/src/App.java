import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Group;

public class App extends Application {
    final ObservableList<String> transports = FXCollections.observableArrayList();
    final ObservableList<String> fromPortList = FXCollections.observableArrayList();
    final ObservableList<String> toPortList = FXCollections.observableArrayList();
    final ObservableList<ShipData> shipmentData = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        fillComboBox();
        fillFromComboBox();
        getAllShipData();

        Label fromLabel = new Label("Select from port");
        ComboBox<String> fromPort = new ComboBox<>(fromPortList);
        fromPort.setMinWidth(150);
        fromPort.setMaxWidth(150);

        Label toLabel = new Label("Select to port");
        ComboBox<String> toPort = new ComboBox<>(toPortList);
        toPort.setMinWidth(150);
        toPort.setMaxWidth(150);
        toPort.setDisable(true);

        TableView<ShipData> table = new TableView<>();

        TableColumn<ShipData, Integer> column1 = new TableColumn<>("ID");
        column1.setMinWidth(100);
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<ShipData, String> column2 = new TableColumn<>("Vessel name");
        column2.setMinWidth(100);
        column2.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ShipData, String> column3 = new TableColumn<>("From");
        column3.setMinWidth(100);
        column3.setCellValueFactory(new PropertyValueFactory<>("from"));

        TableColumn<ShipData, String> column4 = new TableColumn<>("To");
        column4.setMinWidth(100);
        column4.setCellValueFactory(new PropertyValueFactory<>("to"));

        TableColumn<ShipData, Integer> column5 = new TableColumn<>("Total Containers");
        column5.setMinWidth(100);
        column5.setCellValueFactory(new PropertyValueFactory<>("containers"));

        TableColumn<ShipData, Integer> column6 = new TableColumn<>("Max Capacity");
        column6.setMinWidth(100);
        column6.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        table.getColumns().addAll(column1, column2, column3, column4, column5, column6);
        table.setItems(shipmentData);
        table.setEditable(true);

        Alert alert = new Alert(AlertType.WARNING);

        fromPort.setOnAction(ActionEvent -> {
            toPortList.clear();
            System.out.println("Updating the list: " + fromPort.getValue());
            if (!fromPort.getSelectionModel().isEmpty()) { // Spytter en error ud, hvis dette statement ikke eksistere.
                                                           // Fordi vores getValue() er null.
                toPort.setDisable(false);
                fillToComboBox(fromPort.getValue().toString());
            }

        });

        Button searchBtn = new Button("🔍");

        searchBtn.setOnAction(ActionEvent -> {
            if (fromPort.getSelectionModel().isEmpty() && toPort.getSelectionModel().isEmpty()) {
                alert.setTitle("Error");
                alert.setHeaderText("Please select a from port and a to port.");
                alert.showAndWait();
            } else if (fromPort.getSelectionModel().isEmpty()) {
                alert.setTitle("Error");
                alert.setHeaderText("Please select a from port.");
                alert.showAndWait();
            } else if (toPort.getSelectionModel().isEmpty()) {
                alert.setTitle("Error");
                alert.setHeaderText("Please select a to port.");
                alert.showAndWait();
            }
            if (!fromPort.getSelectionModel().isEmpty() && !toPort.getSelectionModel().isEmpty()) {
                // This refreshes the list after adding the cargo [
                shipmentData.removeAll(shipmentData);
                searchConditions(fromPort.getValue().toString(), toPort.getValue().toString());
            }
        });

        Label addAmountCargoLabel = new Label("Amount of Cargo");
        TextField amountCargo = new TextField();
        // This snippet have been taken from this source:
        // https://www.codegrepper.com/search.php?answer_removed=1&q=javafx%20make%20field%20only%20numeric
        amountCargo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    amountCargo.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Button clearSearch = new Button("Clear Search");
        clearSearch.setOnAction(ActionEvent -> {
            toPort.valueProperty().set(null);
            fromPort.valueProperty().set(null);
            amountCargo.setText("");
            shipmentData.clear();
            getAllShipData();
        });

        Button addCargo = new Button("Add Cargo");
        addCargo.setOnAction(ActionEvent -> {
            if (!fromPort.getSelectionModel().isEmpty() && !toPort.getSelectionModel().isEmpty()) { // Tjek om combobox
                if (shipmentData.get(0).getCapacity() >= shipmentData.get(0).getContainers()
                        + Integer.parseInt(amountCargo.getText())) {
                    // // er selected
                    System.out.println("Vessel ID: " + shipmentData.get(0).getId());
                    addCargo(shipmentData.get(0).getId(), Integer.parseInt(amountCargo.getText()));
                    System.out.println("Successfully loaded " + amountCargo.getText() + " amount of containers.");
                    // This refreshes the list after adding the cargo [
                    shipmentData.removeAll(shipmentData);
                    searchConditions(fromPort.getValue().toString(), toPort.getValue().toString());
                } else {
                    alert.setTitle("Error");
                    alert.setHeaderText(
                            "Too much cargo");
                    alert.setContentText(
                            "Please Select another route or load less cargo.");
                    alert.showAndWait();
                }
            }
            if (fromPort.getSelectionModel().isEmpty() && toPort.getSelectionModel().isEmpty()) {
                alert.setTitle("Error");
                alert.setHeaderText("You need to select two ports before we can add your cargo.");
                alert.showAndWait();
            }
        });

        Text emptyTxt1 = new Text("");
        Text emptyTxt2 = new Text("");
        Text emptyTxt3 = new Text("");

        VBox fromPortItem = new VBox(5);
        fromPortItem.getChildren().addAll(fromLabel, fromPort);

        VBox toPortItem = new VBox(5);
        toPortItem.getChildren().addAll(toLabel, toPort);

        VBox searchItem = new VBox(5);
        searchItem.getChildren().addAll(emptyTxt1, searchBtn);

        VBox addCargoItem = new VBox(5);
        addCargoItem.getChildren().addAll(emptyTxt2, addCargo);

        VBox clearItem = new VBox(5);
        clearItem.getChildren().addAll(emptyTxt3, clearSearch);

        VBox addAmountCargoItem = new VBox(5);
        addAmountCargoItem.getChildren().addAll(addAmountCargoLabel, amountCargo);

        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(fromPortItem, toPortItem, searchItem, addAmountCargoItem, addCargoItem, clearItem);

        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(hbox, table);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(new Group(vbox)); // Dimmensionerne på programmet.
        stage.setResizable(false); // Gør så den ikke kan resizes.
        stage.setTitle("Portfolio 3"); // Navnet på programmet.
        stage.setScene(scene);
        stage.show();
    }

    public void searchConditions(String from, String to) {
        String queryString = "select t.id as id, h1.name as fromport, h2.name as toport, v.name as vessel, Sum(f.containers) as containers, v.capacity from transport t inner join vessel v on t.vessel = v.id inner join habour h1 on t.fromhabour = h1.id inner join habour h2 on t.tohabour = h2.id left outer join flow f on t.id = f.transport WHERE fromport = \""
                + from + "\" AND toport = \""
                + to + "\";";
        try (Connection connection = Connect.connect()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("vessel");
                int containers = rs.getInt("containers");
                int capacity = rs.getInt("capacity");
                shipmentData.add(new ShipData(id, name, from, to, containers, capacity));
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }

    public void fillComboBox() {
        String queryString = "SELECT name FROM vessel AS v INNER JOIN flow AS f ON v.id = f.id;";
        try (Connection connection = Connect.connect()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                transports.add(name);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }

    public void fillFromComboBox() {
        String queryString = "select t.id as id, h1.name as fromport, h2.name as toport,v.name as vessel, Sum(f.containers) as containers, v.capacity from transport t inner join vessel v on t.vessel = v.id inner join habour h1 on t.fromhabour = h1.id inner join habour h2 on t.tohabour = h2.id left outer join flow f on t.id = f.transport group by fromport";
        try (Connection connection = Connect.connect()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String fromPort = rs.getString("fromport");
                fromPortList.add(fromPort);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }

    public void fillToComboBox(String from) {
        String queryString = "select t.id as id, h1.name as fromport, h2.name as toport,v.name as vessel, Sum(f.containers) as containers, v.capacity from transport t inner join vessel v on t.vessel = v.id inner join habour h1 on t.fromhabour = h1.id inner join habour h2 on t.tohabour = h2.id left outer join flow f on t.id = f.transport WHERE fromport = \""
                + from + "\" GROUP BY t.id;";
        try (Connection connection = Connect.connect()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String toPort = rs.getString("toport");
                toPortList.add(toPort);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }

    public void addCargo(int id, int containers) {
        String queryString = "insert into flow(transport, containers) values (?, ?);";
        try (Connection connection = Connect.connect()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setInt(1, id);
            statement.setInt(2, containers);

            statement.execute();
        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }

    public void getAllShipData() {
        String queryString = "select t.id as id, h1.name as fromport, h2.name as toport,v.name as vessel, Sum(f.containers) as containers, v.capacity from transport t inner join vessel v on t.vessel = v.id inner join habour h1 on t.fromhabour = h1.id inner join habour h2 on t.tohabour = h2.id left outer join flow f on t.id = f.transport group by t.id";
        try (Connection connection = Connect.connect()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("vessel");
                String from = rs.getString("fromport");
                String to = rs.getString("toport");
                int containers = rs.getInt("containers");
                int capacity = rs.getInt("capacity");
                shipmentData.add(new ShipData(id, name, from, to, containers, capacity));
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
