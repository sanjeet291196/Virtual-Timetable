
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.*;
import java.sql.SQLException;
import javax.swing.*;

//defines the class of faculty
class Faculty {

    JTextField EmpId;
    JLabel FacultyName, FacultyDept;
    JButton SetScheduleButton;

    //constructor to intialize data members
    Faculty() {
        this.SetScheduleButton = new JButton("Set Schedule");
        this.FacultyDept = new JLabel();
        this.FacultyName = new JLabel();
        this.EmpId = new JTextField();
    }
}//Faculty

//class for designing subject and faculty info page
class SubjectPage {

    static Faculty faculty[];
    static JLabel[] Subs;
    static JButton BackButton, NextButton;
    static JPanel SubInfo = new JPanel();

    //initializes variables
    static void init(int SubjectCount, String[] Subjects) {
        faculty = new Faculty[SubjectCount];
        Subs = new JLabel[SubjectCount];
        BackButton = new JButton("Back");
        NextButton = new JButton("Next");
        for (int i = 0; i < SubjectCount; i++) {
            faculty[i] = new Faculty();
            Subs[i] = new JLabel(Subjects[i]);
            faculty[i].SetScheduleButton.addActionListener((ActionEvent e) -> {
                //if SetScheduleButton is clicked
                //read the schedule for selected faculty and
                //display for editing
                for (int i1 = 0; i1 < SubjectCount; i1++) {
                    if (e.getSource() == faculty[i1].SetScheduleButton) {
                        Main.fn.SetIndex = i1;
                        Main.fn.readFacultySchedule();
                        Main.Card.show(Main.Pan, "SchedulePage");
                        SchedulePage.ConfirmButton.requestFocus();
                        return;
                    }
                }
            });

            faculty[i].EmpId.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    /*
                     show related faculty name and department depending on entered EmpId
                     */
                    try {
                        for (int i = 0; i < SubjectCount; i++) {
                            if (e.getSource() == faculty[i].EmpId) {
                                String s = faculty[i].EmpId.getText().toUpperCase();
                                Variables.rs = Variables.stmt.executeQuery("select * from faculty where empid='" + s + "'");
                                if (Variables.rs.next()) {
                                    faculty[i].FacultyName.setText(Variables.rs.getString(2));
                                    faculty[i].FacultyDept.setText(Variables.rs.getString(3));
                                } else {
                                    faculty[i].FacultyName.setText(null);
                                    faculty[i].FacultyDept.setText(null);
                                }

                                break;
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error: Unable to read database");
                    }
                }
            });
        }

        BackButton.addActionListener((ActionEvent e) -> {
            //If BackButton is clicked on SubjectPage
            //cancel all schedules,
            //Destroy all Pages and return to HomePage
            for (int i = 0; i < SubjectCount; i++) {
                for (int j = 0; j < 6; j++) {
                    for (int k = 0; k < 7; k++) {
                        Main.fn.BusyScheduleInfo[i][j][k] = false;
                    }
                }
            }
            Main.Card.show(Main.Pan, "HomePage");
            SubjectPage.destroy();
            LabSchedulePage.destroy(Main.Pan);
            HomePage.YearSelector.requestFocus();

        });

        NextButton.addActionListener((ActionEvent e) -> {
            //if NextButton on SubjectPage is clicked
            //goto LabSchedulePage
            Main.Card.show(Main.Pan, "LabSchedule");
            LabSchedulePage.LabDaySelector[0].requestFocus();
        });
    }

    //designs subject page
    static void initSubInfoPage(int SubjectCount) {

        SubInfo.setLayout(new GridLayout(SubjectCount + 2, 5,20,3));

        SubInfo.add(new JLabel("Subject"));
        SubInfo.add(new JLabel("Employee ID"));
        SubInfo.add(new JLabel("Name"));
        SubInfo.add(new JLabel("Dept"));
        SubInfo.add(new JLabel("Busy Schedule"));
        for (int i = 0; i < SubjectCount; i++) {
            SubInfo.add(SubjectPage.Subs[i]);
            SubInfo.add(SubjectPage.faculty[i].EmpId);
            SubInfo.add(SubjectPage.faculty[i].FacultyName);
            SubInfo.add(SubjectPage.faculty[i].FacultyDept);
            SubInfo.add(SubjectPage.faculty[i].SetScheduleButton);
        }

        SubInfo.add(BackButton);
        if (HomePage.YearSelector.getSelectedIndex() == 3 && HomePage.SemSelector.getSelectedIndex() == 1) {
            SubInfo.add(LabSchedulePage.GenerateButton);

        } else {
            SubInfo.add(NextButton);
        }

        Main.Pan.add(SubInfo, "SubInfo");
    }

    //destroys the page
    static void destroy() {
        SubInfo.removeAll();
        Main.Pan.remove(SubInfo);
    }

    //shows the faculty or subject page
    static void showFacultyPage(Main frame) throws IOException {
        String FileName = "Subject Info/";
        switch (HomePage.YearSelector.getSelectedIndex()) {
            case 0:
                FileName = FileName + "First";
                Variables.Affinity = false;
                break;
            case 3:
                FileName = FileName + HomePage.BranchSelector.getSelectedItem() + (HomePage.YearSelector.getSelectedIndex() + 1) + (HomePage.SemSelector.getSelectedIndex() + 1);
                Variables.Affinity = false;
                break;
            default:
                FileName = FileName + HomePage.BranchSelector.getSelectedItem() + (HomePage.YearSelector.getSelectedIndex() + 1) + (HomePage.SemSelector.getSelectedIndex() + 1);
                Variables.Affinity = true;
        }
        FileName = FileName + ".txt";
        Variables.file = new FileReader(FileName);
        Variables.Reader = new BufferedReader(Variables.file);
        Main.fn.SubjectCount = Integer.parseInt(Variables.Reader.readLine());
        Main.fn.LabCount = Integer.parseInt(Variables.Reader.readLine());
        Main.fn.Subjects = new String[Main.fn.SubjectCount];
        Main.fn.Classes = new int[Main.fn.SubjectCount];
        Main.fn.ClassesAlloted = new int[Main.fn.SubjectCount];
        Main.fn.BusyScheduleInfo = new boolean[Main.fn.SubjectCount][6][7];
        Main.fn.Labs = new String[Main.fn.LabCount];
        for (int i = 0; i < Main.fn.SubjectCount; i++) {
            Main.fn.Subjects[i] = Variables.Reader.readLine();
            Main.fn.Classes[i] = Integer.parseInt(Variables.Reader.readLine());
            Main.fn.ClassesAlloted[i] = 0;
        }
        if (HomePage.YearSelector.getSelectedIndex() == 0) {
            if (HomePage.BranchSelector.getSelectedIndex() == 3) {
                Main.fn.Subjects[4] = "EM";
            } else {
                Main.fn.Subjects[4] = "MM";
            }
        }
        for (int i = 0; i < Main.fn.LabCount; i++) {
            Main.fn.Labs[i] = Variables.Reader.readLine();
        }
        for (int i = 0; i < Main.fn.SubjectCount; i++) {
            for (int j = 0; j < 7; j++) {
                for (int k = 0; k < 6; k++) {
                    Main.fn.BusyScheduleInfo[i][k][j] = false;
                }
            }
        }
        SchedulePage.SetAffinityHours(Main.fn);
        LabSchedulePage.init(Main.fn.LabCount, Main.fn.Labs,frame);
        LabSchedulePage.initLabSchedulePage(Main.fn.LabCount, frame);
        SubjectPage.init(Main.fn.SubjectCount, Main.fn.Subjects);
        SubjectPage.initSubInfoPage(Main.fn.SubjectCount);
        Main.Card.show(Main.Pan, "SubInfo");
    }
}//SubjectPage
