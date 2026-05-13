package controllers;

import models.CinemaManager;
import models.Movie;
import models.Showtime;
import javafx.scene.control.Label;

public class ShowtimeController {

    public void handleAddShowtime(Movie movie, String room, String date, String time,
                                   String priceStr, Label statusLabel, Runnable onSuccess) {
        if (movie == null || room.isEmpty() || date.isEmpty()
                || time.isEmpty() || priceStr.isEmpty()) {
            showError(statusLabel, "All fields are required.");
            return;
        }
        if (hasConflict(null, room, date, time)) {
            showError(statusLabel, room + " is already booked on " + date + " at " + time + ".");
            return;
        }
        try {
            double price = Double.parseDouble(priceStr);
            CinemaManager.getInstance().addShowtime(
                new Showtime(movie, room, date, time, price, 5, 8));
            showSuccess(statusLabel, "Showtime added successfully.");
            onSuccess.run();
        } catch (NumberFormatException e) {
            showError(statusLabel, "Price must be a number.");
        }
    }

    public void handleUpdateShowtime(Showtime selected, String room, String date,
                                      String time, String priceStr,
                                      Label statusLabel, Runnable onSuccess) {
        if (selected == null) {
            showError(statusLabel, "Select a showtime to update.");
            return;
        }
        if (room.isEmpty() || date.isEmpty() || time.isEmpty() || priceStr.isEmpty()) {
            showError(statusLabel, "All fields are required.");
            return;
        }
        if (hasConflict(selected, room, date, time)) {
            showError(statusLabel, room + " is already booked on " + date + " at " + time + ".");
            return;
        }
        try {
            double price = Double.parseDouble(priceStr);
            selected.setRoomName(room);
            selected.setDate(date);
            selected.setTime(time);
            selected.setPrice(price);
            showSuccess(statusLabel, "Showtime updated successfully.");
            onSuccess.run();
        } catch (NumberFormatException e) {
            showError(statusLabel, "Price must be a number.");
        }
    }

    public void handleDeleteShowtime(Showtime selected, Label statusLabel, Runnable onSuccess) {
        if (selected == null) {
            showError(statusLabel, "Select a showtime to delete.");
            return;
        }
        CinemaManager.getInstance().deleteShowtime(selected);
        showSuccess(statusLabel, "Showtime deleted successfully.");
        onSuccess.run();
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private boolean hasConflict(Showtime exclude, String room, String date, String time) {
        for (Showtime st : CinemaManager.getInstance().getShowtimes()) {
            if (st == exclude) continue;
            if (st.getRoomName().equalsIgnoreCase(room) &&
                st.getDate().equalsIgnoreCase(date) &&
                st.getTime().equalsIgnoreCase(time)) {
                return true;
            }
        }
        return false;
    }

    private void showError(Label label, String message) {
        label.setStyle("-fx-text-fill: #e05555; -fx-font-size: 12px;");
        label.setText("⚠  " + message);
    }

    private void showSuccess(Label label, String message) {
        label.setStyle("-fx-text-fill: #4caf82; -fx-font-size: 12px;");
        label.setText("✓  " + message);
    }
}