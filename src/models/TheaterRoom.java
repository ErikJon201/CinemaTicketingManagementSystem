package models;

public class TheaterRoom {
    public static final String STANDARD = "Standard";
    public static final String PREMIUM  = "Premium";
    public static final String IMAX     = "IMAX";
    public static final String FOUR_DX  = "4DX";

    private String name;
    private String type;
    private int rows;
    private int cols;

    public TheaterRoom(String name, String type, int rows, int cols) {
        this.name = name;
        this.type = type;
        this.rows = rows;
        this.cols = cols;
    }

    // Backward-compatible constructor
    public TheaterRoom(String name, int rows, int cols) {
        this(name, STANDARD, rows, cols);
    }

    public String getName()       { return name; }
    public String getType()       { return type; }
    public int getRows()          { return rows; }
    public int getCols()          { return cols; }
    public int getCapacity()      { return rows * cols; }

    public void setName(String n) { this.name = n; }
    public void setType(String t) { this.type = t; }
    public void setRows(int r)    { this.rows = r; }
    public void setCols(int c)    { this.cols = c; }

    @Override
    public String toString()      { return name + " (" + type + ")"; }
}