
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//class for lab page
class LabSchedulePage {

    /**
     * For Selecting Lab Day
     */
    static JComboBox<String> LabDaySelector[];
    /**
     * For Selecting Lab Day
     */
    static JComboBox<String> LabHourSelector[];
    /**
     * To go back to SubjectPage
     */
    static JButton PrevButton;
    /**
     * To generate timetable and display output
     */
    static JButton GenerateButton;
    /**
     * To Store Names of Labs
     */
    static JLabel Labs[];
    /**
     * To place components of LabSchedulePage
     */
    static JPanel LabSchedule = new JPanel();
    static JPanel LabSchedule_Panel = new JPanel();

    //initializes variables
    static void init(int LabCount, String[] LabNames, Main frame) {
        Labs = new JLabel[LabCount];
        LabDaySelector = new JComboBox[LabCount + 1];
        LabHourSelector = new JComboBox[LabCount + 1];
        for (int i = 0; i < LabCount + 1; i++) {
            LabHourSelector[i] = new JComboBox<>();
            LabDaySelector[i] = new JComboBox<>();
            if (i != LabCount) {
                Labs[i] = new JLabel(LabNames[i]);
                LabHourSelector[i].addItem("2nd to 4th");
                LabHourSelector[i].addItem("5nd to 7th");
            }
            LabDaySelector[i].addItem("Mon");
            LabDaySelector[i].addItem("Tue");
            LabDaySelector[i].addItem("Wed");
            LabDaySelector[i].addItem("Thu");
            LabDaySelector[i].addItem("Fri");
            LabDaySelector[i].addItem("Sat");
            LabDaySelector[i].addItemListener((ItemEvent e) -> {
                if (e.getSource() == LabDaySelector[Main.fn.LabCount]) {
                    if (Variables.Affinity) {
                        if (LabSchedulePage.LabDaySelector[Main.fn.LabCount].getSelectedIndex() == 2 || LabSchedulePage.LabDaySelector[Main.fn.LabCount].getSelectedIndex() == 5) {
                            LabSchedulePage.LabHourSelector[Main.fn.LabCount].removeAllItems();
                            LabSchedulePage.LabHourSelector[Main.fn.LabCount].addItem("1 & 2");
                            LabSchedulePage.LabHourSelector[Main.fn.LabCount].addItem("2 & 3");
                            LabSchedulePage.LabHourSelector[Main.fn.LabCount].addItem("3 & 4");
                        } else {
                            LabSchedulePage.LabHourSelector[Main.fn.LabCount].removeAllItems();
                            LabSchedulePage.LabHourSelector[Main.fn.LabCount].addItem("1 & 2");
                            LabSchedulePage.LabHourSelector[Main.fn.LabCount].addItem("2 & 3");
                            LabSchedulePage.LabHourSelector[Main.fn.LabCount].addItem("3 & 4");
                            LabSchedulePage.LabHourSelector[Main.fn.LabCount].addItem("5 & 6");
                            LabSchedulePage.LabHourSelector[Main.fn.LabCount].addItem("6 & 7");
                        }
                    }
                } else {
                    for (int i1 = 0; i1 < Main.fn.LabCount + 1; i1++) {
                        if (e.getSource() == LabSchedulePage.LabDaySelector[i1]) {
                            if (Variables.Affinity) {
                                if (LabSchedulePage.LabDaySelector[i1].getSelectedIndex() == 2 || LabSchedulePage.LabDaySelector[i1].getSelectedIndex() == 5) {
                                    LabSchedulePage.LabHourSelector[i1].removeAllItems();
                                    LabSchedulePage.LabHourSelector[i1].addItem("2nd to 4th");
                                } else {
                                    LabSchedulePage.LabHourSelector[i1].removeAllItems();
                                    LabSchedulePage.LabHourSelector[i1].addItem("2nd to 4th");
                                    LabSchedulePage.LabHourSelector[i1].addItem("5nd to 7th");
                                }
                            }
                        }
                    }
                }
            });
        }

        LabHourSelector[LabCount].addItem("1 & 2");
        LabHourSelector[LabCount].addItem("2 & 3");
        LabHourSelector[LabCount].addItem("3 & 4");
        LabHourSelector[LabCount].addItem("5 & 6");
        LabHourSelector[LabCount].addItem("6 & 7");

        GenerateButton = new JButton("Generate");
        PrevButton = new JButton("Subjects");

        PrevButton.addActionListener((ActionEvent e) -> {
            //goto SubjectPage
            Main.Card.show(Main.Pan, "SubInfo");
            SubjectPage.NextButton.requestFocus();
        });

        GenerateButton.addActionListener((ActionEvent e) -> {
            //Generate and display output timetable
            Main.fn.generateButtonClick(frame);
        });
    }

    //designs lab page
    static void initLabSchedulePage(int LabCount, Main frame) {
        LabSchedule.setLayout(new GridLayout(9, 3,30,10));
        LabSchedule.add(new JLabel(""));
        LabSchedule.add(new JLabel("Day"));
        LabSchedule.add(new JLabel("Hour"));
        for (int i = 0; i < LabCount; i++) {
            LabSchedule.add(Labs[i]);
            LabSchedule.add(LabDaySelector[i]);
            LabSchedule.add(LabHourSelector[i]);
        }
        LabSchedule.add(new JLabel("CDC"));
        LabSchedule.add(LabDaySelector[LabCount]);
        LabSchedule.add(LabHourSelector[LabCount]);

        LabSchedule.add(PrevButton);
        LabSchedule.add(GenerateButton);

        for (int i = LabCount + 1; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                LabSchedule.add(new JLabel());
            }
        }
        LabSchedule_Panel.add(LabSchedule);

        Main.Pan.add(LabSchedule_Panel, "LabSchedule");
        
        frame.pack();
    }

    //destroys the page
    static void destroy(JPanel Pan) {
        LabSchedule.removeAll();
        LabSchedule_Panel.removeAll();
        Pan.remove(LabSchedule_Panel);
    }
}//LabSchedulePage
