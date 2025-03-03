package com.example.helloworldjfxtemplate.DAO;

import com.example.helloworldjfxtemplate.helper.JDBC;
import com.example.helloworldjfxtemplate.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * AppointmentDAO class has sql queries to get appointment date from database
 */
public class AppointmentDAO {

    /**
     * getAppointmentList() has SQL query to get obervable list of all appointments in database.
     * @return appointmentList
     */
    public static ObservableList<Appointment> getAppointmentList() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                    "ORDER BY appointments.Appointment_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                int appointmentContactID = rs.getInt("Contact_ID");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerID = rs.getInt("Customer_ID");
                int appointmentUserID = rs.getInt("User_ID");
                String appointmentContactName = rs.getString("Contact_Name");


                Appointment c = new Appointment(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation,
                        appointmentType, appointmentStart, appointmentEnd, appointmentCustomerID, appointmentUserID,
                        appointmentContactID, appointmentContactName);
                appointmentList.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointmentList;
    }

    /**
     * addAppoint() method has SQL query to add appointment to the database
     * @param appointmentTitle
     * @param appointmentDescription
     * @param appointmentLocation
     * @param appointmentType
     * @param appointmentStart
     * @param appointmentEnd
     * @param appointmentCreateDate
     * @param appointmentCreatedBy
     * @param appointmentLastUpdate
     * @param appointmentUpdatedBy
     * @param appointmentCustomerID
     * @param appointmentUserID
     * @param appointmentContact
     * @throws SQLException
     */
    public static void addAppoint(String appointmentTitle, String appointmentDescription, String appointmentLocation,
                                      String appointmentType, LocalDateTime appointmentStart, LocalDateTime appointmentEnd,
                                      LocalDateTime appointmentCreateDate, String appointmentCreatedBy, LocalDateTime appointmentLastUpdate,
                                      String appointmentUpdatedBy, int appointmentCustomerID, int appointmentUserID, int appointmentContact)
            throws SQLException {
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, " +
                "Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertAppoint = JDBC.connection.prepareStatement(sql);

        insertAppoint.setString(1, appointmentTitle);
        insertAppoint.setString(2, appointmentDescription);
        insertAppoint.setString(3, appointmentLocation);
        insertAppoint.setString(4, appointmentType);
        insertAppoint.setTimestamp(5, Timestamp.valueOf(appointmentStart));
        insertAppoint.setTimestamp(6, Timestamp.valueOf(appointmentEnd));
        insertAppoint.setTimestamp(7, Timestamp.valueOf(appointmentCreateDate));
        insertAppoint.setString(8, appointmentCreatedBy);
        insertAppoint.setTimestamp(9, Timestamp.valueOf(appointmentLastUpdate));
        insertAppoint.setString(10, appointmentUpdatedBy);
        insertAppoint.setInt(11, appointmentCustomerID);
        insertAppoint.setInt(12, appointmentUserID);
        insertAppoint.setInt(13, appointmentContact);
        insertAppoint.executeUpdate();
    }

    /**
     * updtAppoint() method has SQL query to update existing appointment in the database
     * @param appointmentTitle
     * @param appointmentDescription
     * @param appointmentLocation
     * @param appointmentType
     * @param appointmentStart
     * @param appointmentEnd
     * @param appointmentLastUpdate
     * @param appointmentUpdatedBy
     * @param appointmentCustomerID
     * @param appointmentUserID
     * @param appointmentContact
     * @param appointmentID
     */
    public static void updtAppoint(String appointmentTitle, String appointmentDescription,
                                         String appointmentLocation, String appointmentType, LocalDateTime appointmentStart,
                                         LocalDateTime appointmentEnd, LocalDateTime appointmentLastUpdate,
                                       String appointmentUpdatedBy, int appointmentCustomerID, int appointmentUserID,
                                         int appointmentContact, int appointmentID) {
        try {
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                    "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement updateAppointment = JDBC.connection.prepareStatement(sql);

            updateAppointment.setString(1, appointmentTitle);
            updateAppointment.setString(2, appointmentDescription);
            updateAppointment.setString(3, appointmentLocation);
            updateAppointment.setString(4, appointmentType);
            updateAppointment.setTimestamp(5, Timestamp.valueOf(appointmentStart));
            updateAppointment.setTimestamp(6, Timestamp.valueOf(appointmentEnd));
            updateAppointment.setTimestamp(7, Timestamp.valueOf(appointmentLastUpdate));
            updateAppointment.setString(8, appointmentUpdatedBy);
            updateAppointment.setInt(9, appointmentCustomerID);
            updateAppointment.setInt(10, appointmentUserID);
            updateAppointment.setInt(11, appointmentContact);
            updateAppointment.setInt(12, appointmentID);
            updateAppointment.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * delAppoint() method has SQL query that removes appointment from database
     * @param appointmentID
     */
    public static void delAppoint(int appointmentID) {
        try {
            String sqldelete = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement deleteAppoint = JDBC.connection.prepareStatement(sqldelete);
            deleteAppoint.setInt(1, appointmentID);
            deleteAppoint.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * getWeeklyAppointments() method has SQL query that returns ObservableList with all appointments sorted weekly
     * @return weeklyList
     */
    public static ObservableList<Appointment> getWeeklyAppointments() {
        ObservableList<Appointment> weeklyList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE YEARWEEK(START) = YEARWEEK(NOW()) ORDER BY appointments.Appointment_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                int appointmentContactID = rs.getInt("Contact_ID");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerID = rs.getInt("Customer_ID");
                int appointmentUserID = rs.getInt("User_ID");
                String appointmentContactName = rs.getString("Contact_Name");
                Appointment weekly = new Appointment(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation,
                        appointmentType, appointmentStart, appointmentEnd, appointmentCustomerID, appointmentUserID,
                        appointmentContactID, appointmentContactName);
                weeklyList.add(weekly);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return weeklyList;
    }

    /**
     * getMonthlyAppointments() method has SQL query that returns ObservableList with all appointments sorted monthly
     * @return monthlyList
     */
    public static ObservableList<Appointment> getMonthlyAppointments() {
        ObservableList<Appointment> monthlyList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                    "WHERE MONTH(Start) = MONTH(NOW()) ORDER BY appointments.Appointment_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                int appointmentContactID = rs.getInt("Contact_ID");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerID = rs.getInt("Customer_ID");
                int appointmentUserID = rs.getInt("User_ID");
                String appointmentContactName = rs.getString("Contact_Name");
                Appointment monthly = new Appointment(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation,
                        appointmentType, appointmentStart, appointmentEnd, appointmentCustomerID, appointmentUserID,
                        appointmentContactID, appointmentContactName);
                monthlyList.add(monthly);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return monthlyList;
    }

    /**
     * getUserAppointments method has SQL query that returns ObservableList with all appointments for specific user
     * @param userID
     * @return userAppointments
     */
    public static ObservableList<Appointment> getUserAppointments(int userID) {
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        try {
            String sqlStatement = "SELECT * FROM Appointments WHERE User_ID  = '" + userID + "'";
            PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                int appointmentContactID = rs.getInt("Contact_ID");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerID = rs.getInt("Customer_ID");
                int appointmentUserID = rs.getInt("User_ID");
                Appointment results = new Appointment(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation,
                        appointmentType, appointmentStart, appointmentEnd, appointmentCustomerID, appointmentUserID, appointmentContactID);
                userAppointments.add(results);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userAppointments;
    }

    /**
     * getAppointTypeMonth() method has SQL query that returns ObservableList with total number of appointments
     * for current month
     * @return appTypeMonthTotal
     */
    public static ObservableList<Appointment> getAppointTypeMonth() {
        ObservableList<Appointment> appTypeMonthTotal = FXCollections.observableArrayList();
        try {
            String sql = "SELECT DISTINCT(MONTHNAME(Start)) AS Month, Count(*) AS NUM FROM appointments " +
                    "GROUP BY Month";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String appointType = rs.getString("Month");
                int appointTypeTotal = rs.getInt("NUM");
                Appointment result = new Appointment(appointType, appointTypeTotal);
                appTypeMonthTotal.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appTypeMonthTotal;
    }

    /**
     * getAppointmentType() method has SQL query that returns ObservableList with total number of appointments
     * for specific type
     * @return appointmentListType
     */
    public static ObservableList<Appointment> getAppointmentType() {
        ObservableList<Appointment> appointmentListType = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Type, Count(*) AS NUM FROM appointments GROUP BY Type";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String appointType = rs.getString("Type");
                int appointTypeTotal = rs.getInt("NUM");
                Appointment result = new Appointment(appointType, appointTypeTotal);
                appointmentListType.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentListType;
    }

    /**
     * getContactAppoint() method has SQL query that returns ObservableList with all appointments for specific contact
     * from database
     * @param contactID
     * @return contactAppointment
     */
    public static ObservableList<Appointment> getContactAppoint(int contactID) {
        ObservableList<Appointment> contactAppointment = FXCollections.observableArrayList();
        try {
            String sqlStatement = "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                    "WHERE appointments.Contact_ID  = '" + contactID + "'";
            PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                int appointmentContact = rs.getInt("Contact_ID");
                String appointmentContactName = rs.getString("Contact_Name");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerID = rs.getInt("Customer_ID");
                int appointmentUserID = rs.getInt("User_ID");
                String appointmentLocation = rs.getString("Location");
                Appointment result = new Appointment(appointmentID, appointmentTitle, appointmentDescription,
                        appointmentLocation, appointmentType, appointmentStart, appointmentEnd, appointmentCustomerID,
                        appointmentUserID, appointmentContact, appointmentContactName);
                contactAppointment.add(result);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contactAppointment;
    }
}
