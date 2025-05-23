package app.controllers.admin;

import app.models.FeeRecord;
import app.models.PaymentRecord;
import app.services.ApiService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ReceiptDialogController {

    @FXML
    private Label lblOwner;
    @FXML
    private Label lblTotalFee;
    @FXML
    private TextField txtPayment;

    private FeeRecord feeRecord;
    private boolean confirmed = false;

    public void setPaymentRecord(FeeRecord feeRecord) {
        this.feeRecord = feeRecord;
        lblOwner.setText(feeRecord.getOwnerUserName());
        lblTotalFee.setText(String.valueOf(feeRecord.getTotalFee()));
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @FXML
    private void handleConfirm() {
    	try {
            double totalFee = Double.parseDouble(lblTotalFee.getText());
            double payment = Double.parseDouble(txtPayment.getText());

            if (payment <= 0 || payment > totalFee) {
                showAlert("Số tiền nộp không hợp lệ!");
                return;
            }

            
            
            ApiService.paidFee(lblOwner.getText(), payment);

            showAlert("Thanh toán thành công!");
            confirmed = true;
            closeWindow();
        } catch (NumberFormatException e) {
            showAlert("Vui lòng nhập số tiền hợp lệ!");
        } catch (Exception e) {
            showAlert("Đã xảy ra lỗi: " + e.getMessage());
        }
    }
    

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông Báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) lblOwner.getScene().getWindow();
        stage.close();
    }
}
