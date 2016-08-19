
import java.awt.*;
import java.io.*;
import java.sql.*;
import javax.swing.JOptionPane;

public class Functions {

    String[] Subjects, Labs;
    int[] Classes, ClassesAlloted;
    int SetIndex, SubjectCount, LabCount;
    boolean[][][] BusyScheduleInfo, temp;
    int[] freeClasses;
    int Days = 6, Hours = 7;
    boolean[] Completed;
    Output OP;

    /**
     * Opens the database for reading the faculty information
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    static void openDatabase() throws FileNotFoundException, ClassNotFoundException, SQLException, IOException {
        /*
             * Read file "Database.txt" for database driver and path
         */
        Variables.file = new FileReader("Database.txt");
        Variables.Reader = new BufferedReader(Variables.file);
        Variables.ClassName = Variables.Reader.readLine();
        Variables.DBurl = Variables.Reader.readLine();
        /*
             * Username and password prompt
         */
        Variables.user = JOptionPane.showInputDialog("Enter Database User Name:");
        Variables.psw = JOptionPane.showInputDialog("Enter Database Password:");
        /*
             * Connect To Database
         */
        Class.forName(Variables.ClassName);
        Variables.c = DriverManager.getConnection(Variables.DBurl, Variables.user, Variables.psw);
        Variables.stmt = Variables.c.createStatement();
    }

    void generateButtonClick(Main frame) {
        if (isValidateSchedule()) {
            OP = new Output();
            if (generateTimetable()) {
                OP.display(SubjectCount, LabCount, frame);
                Main.Card.show(Main.Pan, "Output");
            }
        }
    }

    boolean generateTimetable() {
        temp = BusyScheduleInfo.clone();
        if (countFreeClasses()) {
            return false;
        }
        if (!isValidateSchedule()) {
            return false;
        }
        assignLabs();
        assignCDC();
        assignAffinity();
        int leastFreeFaculty;
        while (true) {
            leastFreeFaculty = findNextLeastFreeFaculty();
            if (leastFreeFaculty < 0) {
                break;
            }
            assignSchedule(leastFreeFaculty);
        }
        return true;
    }

    boolean countFreeClasses() {
        freeClasses = new int[SubjectCount];
        Completed = new boolean[SubjectCount];
        ClassesAlloted = new int[SubjectCount];
        for (int i = 0; i < SubjectCount; i++) {
            freeClasses[i] = 0;
            Completed[i] = false;
            ClassesAlloted[i] = 0;
            for (int j = 0; j < Days; j++) {
                for (int k = 0; k < Hours; k++) {
                    if (!BusyScheduleInfo[i][j][k]) {
                        freeClasses[i]++;
                    }
                }
            }
            if (freeClasses[i] < Classes[i]) {
                JOptionPane.showMessageDialog(null, "Not Enough Free Classes for \"" + Subjects[i] + "\" Faculty");
                return true;
            }
        }
        return false;
    }

    boolean isValidateSchedule() {
        //checks lab schedules
        if (HomePage.YearSelector.getSelectedIndex() == 3 && HomePage.SemSelector.getSelectedIndex() == 1) {
        } else {
            for (int i = 0; i < LabCount; i++) {
                for (int j = i + 1; j < LabCount; j++) {
                    if (LabSchedulePage.LabDaySelector[i].getSelectedIndex() == LabSchedulePage.LabDaySelector[j].getSelectedIndex()) {
                        if (LabSchedulePage.LabHourSelector[i].getSelectedIndex() == LabSchedulePage.LabHourSelector[j].getSelectedIndex()) {
                            LabSchedulePage.LabDaySelector[i].setForeground(Color.red);
                            LabSchedulePage.LabHourSelector[i].setForeground(Color.red);
                            LabSchedulePage.LabDaySelector[j].setForeground(Color.red);
                            LabSchedulePage.LabHourSelector[j].setForeground(Color.red);
                            JOptionPane.showMessageDialog(null, "Error: Lab Schedules collides.\nPlease select Different Schedule for all labs");
                            return false;
                        }
                    }
                }
                LabSchedulePage.LabDaySelector[i].setForeground(Color.green);
                LabSchedulePage.LabHourSelector[i].setForeground(Color.green);
            }

            for (int i = 0; i < LabCount; i++) {
                if (LabSchedulePage.LabDaySelector[i].getSelectedIndex() == LabSchedulePage.LabDaySelector[LabCount].getSelectedIndex()) {
                    if (LabSchedulePage.LabHourSelector[i].getSelectedIndex() == 0) {
                        if (LabSchedulePage.LabHourSelector[LabCount].getSelectedIndex() < 3) {
                            LabSchedulePage.LabDaySelector[i].setForeground(Color.red);
                            LabSchedulePage.LabHourSelector[i].setForeground(Color.red);
                            LabSchedulePage.LabDaySelector[LabCount].setForeground(Color.red);
                            LabSchedulePage.LabHourSelector[LabCount].setForeground(Color.red);
                            JOptionPane.showMessageDialog(null, "Error: Lab Schedules collides.\nPlease select Different Schedule for all labs");
                            return false;
                        }
                    }
                    if (LabSchedulePage.LabHourSelector[i].getSelectedIndex() == 1) {
                        if (LabSchedulePage.LabHourSelector[LabCount].getSelectedIndex() >= 3) {
                            LabSchedulePage.LabDaySelector[i].setForeground(Color.red);
                            LabSchedulePage.LabHourSelector[i].setForeground(Color.red);
                            LabSchedulePage.LabDaySelector[LabCount].setForeground(Color.red);
                            LabSchedulePage.LabHourSelector[LabCount].setForeground(Color.red);
                            javax.swing.JOptionPane.showMessageDialog(null, "Error: Lab Schedules collides.\nPlease select Different Schedule for all labs");
                            return false;
                        }
                    }
                }

                LabSchedulePage.LabDaySelector[i].setForeground(Color.green);
                LabSchedulePage.LabHourSelector[i].setForeground(Color.green);
            }
        }
        return true;
    }

    void assignLabs() {
        if (HomePage.YearSelector.getSelectedIndex() == 3 && HomePage.SemSelector.getSelectedIndex() == 1) {
        } else {
            for (int i = 0; i < LabCount; i++) {
                int labDay = LabSchedulePage.LabDaySelector[i].getSelectedIndex();
                int labHour = LabSchedulePage.LabHourSelector[i].getSelectedIndex() * 3 + 1;
                OP.timetable[labDay][labHour++] = "<--";
                OP.timetable[labDay][labHour++] = Labs[i];
                OP.timetable[labDay][labHour] = "-->";
            }
        }
    }

    void assignCDC() {
        if (HomePage.YearSelector.getSelectedIndex() == 3 && HomePage.SemSelector.getSelectedIndex() == 1) {
        } else {
            int CDCDay = LabSchedulePage.LabDaySelector[LabCount].getSelectedIndex();
            int CDCHour = LabSchedulePage.LabHourSelector[LabCount].getSelectedIndex();
            if (CDCHour >= 3) {
                CDCHour++;
            }
            OP.timetable[CDCDay][CDCHour++] = "<- CDC";
            OP.timetable[CDCDay][CDCHour] = "CDC ->";
        }
    }

    private void assignAffinity() {
        if (Variables.Affinity) {
            OP.timetable[5][4] = "<--";
            OP.timetable[5][5] = "AF";
            OP.timetable[5][6] = "-->";
        }
    }

    int findNextLeastFreeFaculty() {
        int least = 43;
        for (int i = 0; i < SubjectCount; i++) {
            if (freeClasses[i] < least && Completed[i] == false) {
                least = freeClasses[i];
            }
        }
        for (int i = 0; i < SubjectCount; i++) {
            if (freeClasses[i] == least && Completed[i] == false) {
                Completed[i] = true;
                return i;
            }
        }
        return -1;
    }

    //checks whether current class is lab or not
    int isLab(int i, int j) {
        if ((HomePage.YearSelector.getSelectedIndex() == 3 && HomePage.SemSelector.getSelectedIndex() == 1)) {
        } else {
            for (int k = 0; k < LabCount; k++) {
                if (LabSchedulePage.LabDaySelector[k].getSelectedIndex() == i) {
                    if ((LabSchedulePage.LabHourSelector[k].getSelectedIndex() * 3 + 1) == j) {
                        return k;
                    }
                }
            }
            if (LabSchedulePage.LabDaySelector[LabCount].getSelectedIndex() == i) {
                if (LabSchedulePage.LabHourSelector[LabCount].getSelectedIndex() == j) {
                    if (j < 3) {
                        return LabCount;
                    }
                }
                if (LabSchedulePage.LabHourSelector[LabCount].getSelectedIndex() == j - 1) {
                    if (j > 3) {
                        return LabCount;
                    }
                }
            }

        }
        return -1;
    }

    //checks whether faculty is free or not
    boolean isFree(int x, int i, int j) {
        if (ClassesAlloted[x] != Classes[x]) {
            if (temp[x][i][j] == false) {
                if (j == 0) {
                    if (temp[x][i][1] == false) {
                        return true;
                    }
                } else if (Variables.Affinity && (j == 4 && (i == 2 || i == 5)) || j == 6) {
                    if (temp[x][i][j - 1] == false) {
                        return true;
                    }
                } else if (temp[x][i][j - 1] == false && temp[x][i][j + 1] == false) {
                    return true;
                }
            }
        }
        return false;
    }

    //reads and display already defined schedule for a faculty
    void readFacultySchedule() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                SchedulePage.ScheduleCheckBox[i][j].setSelected(BusyScheduleInfo[SetIndex][i][j]);
            }
        }
    }

    //saves and exit the schedule page
    void confirmSchedule() {
        Main.Card.show(Main.Pan, "SubInfo");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                BusyScheduleInfo[SetIndex][i][j]
                        = SchedulePage.ScheduleCheckBox[i][j].isSelected();
            }
        }
    }

    //clear all the schedule for a faculty
    void clearSchedule() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                SchedulePage.ScheduleCheckBox[i][j].setSelected(false);
            }
        }
        if (Variables.Affinity) {
            for (int j = 4; j < 7; j++) {
                SchedulePage.ScheduleCheckBox[5][j].setSelected(true);
            }
        }
    }

    private void assignSchedule(int leastFreeFaculty) {
        if (freeClasses[leastFreeFaculty] < Classes[leastFreeFaculty] + 5 || isRemainingFaculty()) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    if (isFree(leastFreeFaculty, i, j)) {
                        if (isFreeClass(i, j)) {
                            if (ClassesAlloted[leastFreeFaculty] != Classes[leastFreeFaculty]) {
                                OP.timetable[i][j] = Subjects[leastFreeFaculty];
                                temp[leastFreeFaculty][i][j] = true;
                                ClassesAlloted[leastFreeFaculty]++;
                            } else {
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            while (ClassesAlloted[leastFreeFaculty] != Classes[leastFreeFaculty]) {
                int i = (int) (Math.random() * 6);
                int j = (int) (Math.random() * 7);
                if (isFree(leastFreeFaculty, i, j)) {
                    if (isFreeClass(i, j)) {
                        OP.timetable[i][j] = Subjects[leastFreeFaculty];
                        temp[leastFreeFaculty][i][j] = true;
                        ClassesAlloted[leastFreeFaculty]++;
                    }
                }
            }
        }
    }

    private boolean isFreeClass(int i, int j) {
        return OP.timetable[i][j] == null;
    }

    private boolean isRemainingFaculty() {
        int count = 0;
        for (int i = 0; i < SubjectCount; i++) {
            if (Completed[i]) {
                count++;
            }
        }
        return count >= SubjectCount - 1;
    }

}
