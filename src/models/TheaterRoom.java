package models;

public class TheaterRoom {
    private String name;
    private int rows, cols;

    public TheaterRoom(String name, int rows, int cols) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getCapacity() {
        return rows * cols;
    } 

    @Override
    public String toString() {
        return name;
    }
}