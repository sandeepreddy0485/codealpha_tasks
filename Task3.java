import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class HotelReservationSystem {
    private Connection conn;
    private JFrame frame;
    private JComboBox<String> roomTypeBox;
    private JTextField nameField, checkInField, checkOutField;
    private JTextArea resultArea;
    private JButton cancelButton;

    public HotelReservationSystem() {
        connectToDatabase();
        createGUI();
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelDB", "root", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        frame = new JFrame("Hotel Reservation System");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

        panel.add(new JLabel("Guest Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Room Type:"));
        roomTypeBox = new JComboBox<>(new String[]{"Single", "Deluxe", "Suite"});
        panel.add(roomTypeBox);

        panel.add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        checkInField = new JTextField();
        panel.add(checkInField);

        panel.add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        checkOutField = new JTextField();
        panel.add(checkOutField);

        JButton bookButton = new JButton("Book Room");
        bookButton.addActionListener(e -> makeReservation());
        panel.add(bookButton);
        
        cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(e -> cancelReservation());
        panel.add(cancelButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        panel.add(new JScrollPane(resultArea));

        frame.add(panel);
        frame.setVisible(true);
    }

    private void makeReservation() {
        String guestName = nameField.getText();
        String roomType = (String) roomTypeBox.getSelectedItem();
        String checkIn = checkInField.getText();
        String checkOut = checkOutField.getText();

        try {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT room_id, price FROM rooms WHERE room_type = ? AND is_available = TRUE LIMIT 1");
            stmt.setString(1, roomType);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int roomId = rs.getInt("room_id");
                double price = rs.getDouble("price");
                double totalCost = price * 1.1; // Including tax

                PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO reservations (guest_name, room_id, check_in, check_out, amount_paid) VALUES (?, ?, ?, ?, ?)");
                insertStmt.setString(1, guestName);
                insertStmt.setInt(2, roomId);
                insertStmt.setString(3, checkIn);
                insertStmt.setString(4, checkOut);
                insertStmt.setDouble(5, totalCost);
                insertStmt.executeUpdate();

                PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE rooms SET is_available = FALSE WHERE room_id = ?");
                updateStmt.setInt(1, roomId);
                updateStmt.executeUpdate();

                resultArea.setText("Reservation Successful!\nRoom ID: " + roomId + "\nTotal Cost: $" + totalCost);
            } else {
                resultArea.setText("No available rooms of this type.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cancelReservation() {
        String guestName = nameField.getText();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT room_id FROM reservations WHERE guest_name = ? ORDER BY reservation_id DESC LIMIT 1");
            stmt.setString(1, guestName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int roomId = rs.getInt("room_id");
                
                PreparedStatement deleteStmt = conn.prepareStatement(
                    "DELETE FROM reservations WHERE guest_name = ? ORDER BY reservation_id DESC LIMIT 1");
                deleteStmt.setString(1, guestName);
                deleteStmt.executeUpdate();

                PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE rooms SET is_available = TRUE WHERE room_id = ?");
                updateStmt.setInt(1, roomId);
                updateStmt.executeUpdate();

                resultArea.setText("Reservation Cancelled Successfully.");
            } else {
                resultArea.setText("No reservation found for this name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HotelReservationSystem();
    }
}
