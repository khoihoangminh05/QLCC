package app.controllers.admin;

import app.controllers.DashboardControllable;
import app.models.FeeName;
import app.models.FeeRecord;
import app.models.User;
import app.services.ApiService;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FeeManagementController implements DashboardControllable {

    @FXML
    private TableView<FeeRecord> tableView;
    @FXML private TableColumn<FeeRecord, Number> colHouseholdId;
    @FXML private TableColumn<FeeRecord, String> colOwner;
    @FXML private TableColumn<FeeRecord, String> colStatus;
    @FXML private TableColumn<FeeRecord, Number> colTotalFee;
    @FXML private TableColumn<FeeRecord, String> colPaid;
    @FXML private TableColumn<FeeRecord, String> colAction;

    // Filter controls
    
    @FXML private TextField ownerField;
    @FXML private Button btnApplyFilter;

    // Fee type list
    @FXML private ListView<FeeName> feeTypeList;

    // Add fee controls
    @FXML private TextField newFeeTypeField;
    @FXML private TextField newAmountField;
    @FXML private TextField newUnitField;
    @FXML private Button btnAddFeeType;
    
    @FXML
    private TextField addOwnerField;

    @FXML
    private ComboBox<String> addStatusCombo;
    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private Button btnAddHouseholdFee;
    
    private ObservableList<FeeName> feeTypes;


    @Override
    public void setDashboardController(DashBoardController controller) {
        // Future extension point
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        
        loadData();
        feeTypes = FXCollections.observableArrayList(ApiService.getAllFeename());
        
        feeTypeList.setItems(feeTypes);
        
        feeTypeList.setCellFactory(lv -> new ListCell<FeeName>() {
            private final HBox hbox = new HBox(10);
            private final Label label = new Label();
            private final Region spacer = new Region();
            private final Button deleteButton = new Button();

            {
            	HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.setSpacing(10);
                ImageView deleteIcon = new ImageView(new Image(getClass().getResource("/app/assets/img/delete.png").toString()));
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.getStyleClass().add("icon-button-delete");
                deleteButton.setOnAction(event -> {
                    FeeName item = getItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận xóa");
                    alert.setHeaderText("Bạn có chắc chắn muốn xóa?");
                    alert.setContentText("Khoản thu: " + item.getName() );

                    Optional<ButtonType> result = alert.showAndWait();
                    if (!result.isPresent() || result.get() != ButtonType.OK) {
                    	
                       return;
                    }
                    if (item != null) {
                        boolean isDelete = ApiService.delFeeName(item.getName());
                        System.out.print(item.getName());
                        if(isDelete) {
                        	 getListView().getItems().remove(item);
                        } else {
                        	showAlert(Alert.AlertType.ERROR, "Lỗi!", "Xóa khoản thu thất bại!");
                        }
                       
                    }
                });

                hbox.getChildren().addAll(label, spacer, deleteButton);
            }

            @Override
            protected void updateItem(FeeName item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item.toString());
                    setGraphic(hbox);
                }
            }
        });

        // Add button action
        btnAddFeeType.setOnAction(e -> handleAddFeeType());
        btnApplyFilter.setOnAction(e -> handleFilter());
        
        
        addStatusCombo.setItems(FXCollections.observableArrayList("RESIDENT", "MOVED_OUT"));
        statusCombo.setItems(FXCollections.observableArrayList("ALL","RESIDENT", "MOVED_OUT"));

        btnAddHouseholdFee.setOnAction(e -> {
            String owner = addOwnerField.getText();
            String status = addStatusCombo.getValue();
            
            if (owner.isEmpty() || status == null ) {
            	showAlert(Alert.AlertType.WARNING, "Cảnh báo!", "Chưa nhập đủ các trường!");
                return;
            }

            try {
                
               boolean isCreate = ApiService.createFeeManage(owner, status);
               if(isCreate) {
            	   loadData();
               } else {
            	   showAlert(Alert.AlertType.ERROR, "Lỗi!", "Thêm hộ khẩu thất bại!");
               }
     
               
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Lỗi!", "Có lỗi trong quá trình thêm hộ khẩu!");
            }
        });
    }
    
    private void handleAddFeeType() {
        String type = newFeeTypeField.getText();
        String amountStr = newAmountField.getText();
        String unitStr = newUnitField.getText();

        if (type.isEmpty() || amountStr.isEmpty() || unitStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing input", "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        try {
        
            Double amount = Double.parseDouble(amountStr);
            boolean isAdd = ApiService.addFeeName(type, unitStr, amount);
            if(isAdd) {
            FeeName newFeeName = new FeeName(0, type, amount, unitStr);
            feeTypes.add(newFeeName);
           
            newFeeTypeField.clear();
            newAmountField.clear();
            newUnitField.clear();
            } else {
            	showAlert(Alert.AlertType.ERROR, "Invalid amount", "Thêm khoản thu mới thất bại!");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid amount", "Số tiền không hợp lệ.");
        }
    }
    
    private void handleFilter() {
        
        String owner = ownerField.getText();
        String status = statusCombo.getValue();

        List<FeeRecord> records = ApiService.getAllPayments();

        List<FeeRecord> filteredRecords = records.stream()
            .filter(user -> (owner.isEmpty() || user.getOwnerUserName().toLowerCase().contains(owner)))
            .filter(user -> (status.isEmpty() || status.equals("ALL") || user.getAccom_status().equals(status)))
            .collect(Collectors.toList());

        Platform.runLater(() -> {
            ObservableList<FeeRecord> data = FXCollections.observableArrayList(filteredRecords);
            tableView.setItems(data);
           
            
        });
    }

    private void setupTableColumns() {
    	colHouseholdId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        colOwner.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOwnerUserName()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAccom_status()));
        colTotalFee.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalFee()));
        colPaid.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isPaid() ? "COMPLETED" : "INCOMPLETE"));

        colPaid.setCellFactory(column -> new TableCell<FeeRecord, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label dot = new Label("●");
                    dot.getStyleClass().add(item.equalsIgnoreCase("Completed") ? "status-dot-green" : "status-dot-red");

                    Label label = new Label(item);
                    label.setGraphic(dot);
                    label.setContentDisplay(ContentDisplay.LEFT);
                    label.getStyleClass().add(item.equalsIgnoreCase("Completed") ? "status-pill-green" : "status-pill-red");

                    setGraphic(label);
                    setAlignment(Pos.CENTER);
                }
            }
        });
        

        

        colAction.setCellFactory(col -> new TableCell<>() {
            private final HBox actionBox = new HBox(10);

            {
                actionBox.setAlignment(Pos.CENTER);

                // Tạo icon và HBox bao
                ImageView iconGhiNhan = createIcon("/app/assets/img/order.png");
                ImageView iconChiTiet = createIcon("/app/assets/img/eye.png");
                ImageView iconXoa = createIcon("/app/assets/img/delete.png");

                HBox boxGhiNhan = wrapIcon(iconGhiNhan, "Ghi nhận nộp phí");
                HBox boxChiTiet = wrapIcon(iconChiTiet, "Xem chi tiết");
                HBox boxXoa = wrapIcon(iconXoa, "Xóa hộ");

                // Sự kiện
                boxGhiNhan.setOnMouseClicked(e -> openReceiptDialog(getTableView().getItems().get(getIndex())));
                boxChiTiet.setOnMouseClicked(e -> openDetailDialog(getTableView().getItems().get(getIndex())));
                boxXoa.setOnMouseClicked(e -> deleteHousehold(getTableView().getItems().get(getIndex())));

                actionBox.getChildren().addAll(boxGhiNhan, boxChiTiet, boxXoa);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }

            private ImageView createIcon(String path) {
                ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(path)));
                icon.setFitWidth(18);
                icon.setFitHeight(18);
                return icon;
            }

            private HBox wrapIcon(ImageView icon, String tooltipText) {
                HBox box = new HBox(icon);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(5));
                box.setCursor(Cursor.HAND);
                Tooltip.install(box, new Tooltip(tooltipText));
                box.getStyleClass().add("action-icon"); // Thêm class CSS nếu cần
                return box;
            }
        });


        colHouseholdId.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        colOwner.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        colStatus.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        colTotalFee.prefWidthProperty().bind(tableView.widthProperty().multiply(0.13));
        colPaid.prefWidthProperty().bind(tableView.widthProperty().multiply(0.17));
        colAction.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
    }

    

    public void loadData() {
        new Thread(() -> {
            try {
            	List<FeeRecord> records = ApiService.getAllPayments();
                Platform.runLater(() -> {
                    ObservableList<FeeRecord> data = FXCollections.observableArrayList(records);
                    tableView.setItems(data);
                   
                    
                });
            } catch (Exception e) {
                Platform.runLater(() -> showError("Lỗi khi lấy dữ liệu: " + e.getMessage()));
            }
        }).start();
    }

   

    private void openReceiptDialog(FeeRecord record) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/admin/receiptDialog.fxml"));
            Parent root = loader.load();
            ReceiptDialogController controller = loader.getController();
            controller.setPaymentRecord(record);

            Stage stage = new Stage();
            stage.setTitle("Biên Lai Thanh Toán");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/app/assets/css/admin/receiptDialog.css").toExternalForm());
            stage.setScene(scene);
            stage.showAndWait();

            if (controller.isConfirmed()) {
                loadData();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Không thể mở biên lai: " + e.getMessage());
        }
    }
    
    private void deleteHousehold(FeeRecord household) {
        try {
        	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa?");
            alert.setContentText("Người dùng: " + household.getOwnerUserName() );

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
            	boolean isDelete = ApiService.delFeeRecord(household.getOwnerUserName());
                if(isDelete) {
                	loadData();
                	
                } else {
                	showError("Xóa hộ khẩu thất bại: " );
                }
               
            }
            
           } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    private void openDetailDialog(FeeRecord household) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/admin/paymentDetail.fxml"));
            Parent root = loader.load();

            PaymentDetailController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Household Detail");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/app/assets/css/admin/payment_detail.css").toExternalForm());
            stage.setScene(scene);
            controller.setData(household, stage, feeTypes);
            
           

            stage.showAndWait();
            if (controller.isConfirmed()) {
                loadData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void handleAddPaymentButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/views/admin/add_payment.fxml"));
            Parent root = loader.load();
            AddPaymentController controller = loader.getController();
            controller.setFeeManagementController(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/app/assets/css/admin/add_payment.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Thêm Khoản Phí");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Không thể mở form thêm khoản phí: " + e.getMessage());
        }
    }
    
   

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}