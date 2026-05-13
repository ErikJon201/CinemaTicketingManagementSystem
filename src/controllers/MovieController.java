package controllers;

import models.CinemaManager;
import models.Movie;
import javafx.scene.control.Label;

public class MovieController {

    public void handleAddMovie(String title, String genre, String durationStr,
                                Label statusLabel, Runnable onSuccess) {
        if (title.isEmpty() || genre.isEmpty() || durationStr.isEmpty()) {
            showError(statusLabel, "All fields are required.");
            return;
        }
        try {
            int duration = Integer.parseInt(durationStr);
            CinemaManager.getInstance().addMovie(new Movie(title, genre, duration));
            showSuccess(statusLabel, "Movie added successfully.");
            onSuccess.run();
        } catch (NumberFormatException e) {
            showError(statusLabel, "Duration must be a number.");
        }
    }

    public void handleUpdateMovie(Movie selected, String title, String genre,
                                   String durationStr, Label statusLabel,
                                   Runnable onSuccess) {
        if (selected == null) {
            showError(statusLabel, "Select a movie to update.");
            return;
        }
        if (title.isEmpty() || genre.isEmpty() || durationStr.isEmpty()) {
            showError(statusLabel, "All fields are required.");
            return;
        }
        try {
            int duration = Integer.parseInt(durationStr);
            selected.setTitle(title);
            selected.setGenre(genre);
            selected.setDuration(duration);
            showSuccess(statusLabel, "Movie updated successfully.");
            onSuccess.run();
        } catch (NumberFormatException e) {
            showError(statusLabel, "Duration must be a number.");
        }
    }

    public void handleDeleteMovie(Movie selected, Label statusLabel, Runnable onSuccess) {
        if (selected == null) {
            showError(statusLabel, "Select a movie to delete.");
            return;
        }
        CinemaManager.getInstance().deleteMovie(selected);
        showSuccess(statusLabel, "Movie deleted successfully.");
        onSuccess.run();
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