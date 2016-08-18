
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.*;

//class for design of faculty schedule page
class SchedulePage {

    /**
     * To Save the current Schedule Changes and return to SubjectPage
     */
    static JButton ConfirmButton;
    /**
     * To Clear all the schedules
     */
    static JButton ClearButton;
    /**
     * To discard Schedule Changes and return to SubjectPage
     */
    static JButton CancelButton;
    /**
     * To Store Names of Days Of Weak
     */
    static JCheckBox[] Days;
    /**
     * To Store Hour Number
     */
    static JCheckBox[] Hours;
    /**
     * Schedule Selection check boxes
     */
    static JCheckBox[][] ScheduleCheckBox;

    //intializes variables
    static void init() {
        ScheduleCheckBox = new JCheckBox[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                ScheduleCheckBox[i][j] = new JCheckBox();
                ScheduleCheckBox[i][j].addItemListener((ItemEvent e) -> {
                    SetScheduleChange(e);
                });
            }
        }
        Hours = new JCheckBox[7];
        for (int i = 0; i < 7; i++) {
            Hours[i] = new JCheckBox("" + i);
        }

        Days = new JCheckBox[]{
            new JCheckBox("Mon"),
            new JCheckBox("Tue"),
            new JCheckBox("Wed"),
            new JCheckBox("Thu"),
            new JCheckBox("Fri"),
            new JCheckBox("Sat")
        };

        for (int i = 0; i < 6; i++) {
            Days[i].addItemListener((ItemEvent e) -> {
                SetScheduleDayChange(e);
            });
        }

        ConfirmButton = new JButton("Confirm");
        CancelButton = new JButton("Cancel");
        ClearButton = new JButton("Clear All");

        ConfirmButton.addActionListener((ActionEvent e) -> {
            //save the schedule and return to SubjectPage
            Main.fn.confirmSchedule();
            SubjectPage.faculty[0].EmpId.requestFocus();
        });

        ClearButton.addActionListener((ActionEvent e) -> {
            //clear the schedule
            Main.fn.clearSchedule();
        });

        CancelButton.addActionListener((ActionEvent e) -> {
            //Discard any changes and return to SubjectPage
            Main.Card.show(Main.Pan, "SubInfo");
            SubjectPage.faculty[0].EmpId.requestFocus();
        });

    }

    //designs schedule page
    static void initSchedulePage() {
        char[] lunch = {'L', 'U', 'N', 'C', 'H', ' '};
        JPanel ScheduleCheckList = new JPanel();
        ScheduleCheckList.setLayout(new GridLayout(8, 10));

        ScheduleCheckList.add(new JLabel("Days"));
        for (int i = 0; i < 4; i++) {
            ScheduleCheckList.add(Hours[i]);
        }
        ScheduleCheckList.add(new JLabel(""));
        for (int i = 4; i < 7; i++) {
            ScheduleCheckList.add(Hours[i]);
        }

        for (int i = 0; i < 6; i++) {
            ScheduleCheckList.add(SchedulePage.Days[i]);
            for (int j = 0; j < 4; j++) {
                ScheduleCheckList.add(SchedulePage.ScheduleCheckBox[i][j]);
            }
            ScheduleCheckList.add(new Label("" + lunch[i]));
            for (int j = 4; j < 7; j++) {
                ScheduleCheckList.add(SchedulePage.ScheduleCheckBox[i][j]);
            }
        }

        ScheduleCheckList.add(ClearButton);
        ScheduleCheckList.add(ConfirmButton);
        ScheduleCheckList.add(CancelButton);

        Main.Pan.add(ScheduleCheckList, "SchedulePage");
    }

    //sets affinity hours
    static void SetAffinityHours(Functions fn) {
        if (Variables.Affinity) {
            SchedulePage.ScheduleCheckBox[2][5].setText("Afinity");
            SchedulePage.ScheduleCheckBox[2][6].setText("Afinity");
            SchedulePage.ScheduleCheckBox[5][5].setText("Afinity");
            SchedulePage.ScheduleCheckBox[5][6].setText("Afinity");
            SchedulePage.ScheduleCheckBox[2][5].setEnabled(false);
            SchedulePage.ScheduleCheckBox[2][6].setEnabled(false);
            SchedulePage.ScheduleCheckBox[5][5].setEnabled(false);
            SchedulePage.ScheduleCheckBox[5][6].setEnabled(false);
            for (int i = 0; i < fn.SubjectCount; i++) {
                for (int j = 5; j < 7; j++) {
                    fn.BusyScheduleInfo[i][2][j] = true;
                    fn.BusyScheduleInfo[i][5][j] = true;
                }
            }
        } else {
            SchedulePage.ScheduleCheckBox[2][5].setText("");
            SchedulePage.ScheduleCheckBox[2][6].setText("");
            SchedulePage.ScheduleCheckBox[5][5].setText("");
            SchedulePage.ScheduleCheckBox[5][6].setText("");
            SchedulePage.ScheduleCheckBox[2][5].setEnabled(true);
            SchedulePage.ScheduleCheckBox[2][6].setEnabled(true);
            SchedulePage.ScheduleCheckBox[5][5].setEnabled(true);
            SchedulePage.ScheduleCheckBox[5][6].setEnabled(true);
        }
    }

    private static void SetScheduleDayChange(ItemEvent e) {
        for (int i = 0; i < 6; i++) {
            if (Days[i] == e.getSource()) {
                for (int j = 0; j < 7; j++) {
                    ScheduleCheckBox[i][j].setSelected(Days[i].isSelected());
                    SetScheduleChange(new ItemEvent(ScheduleCheckBox[i][j], i, ScheduleCheckBox[i][j], j));
                }
                break;
            }
        }
    }

    private static void SetScheduleChange(ItemEvent e) {
        boolean flag;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (e.getSource() == ScheduleCheckBox[i][j]) {
                    flag = true;
                    for (int k = 0; k < 7; k++) {
                        if (ScheduleCheckBox[i][k].isSelected() == false) {
                            flag = false;
                            break;
                        }
                    }
                    Days[i].setSelected(flag);
                    flag = true;
                    for (int k = 0; k < 6; k++) {
                        if (ScheduleCheckBox[k][j].isSelected() == false) {
                            flag = false;
                            break;
                        }
                    }
                    Hours[j].setSelected(flag);
                    break;
                }
            }
        }
    }
}//SchedulePage
