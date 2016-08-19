
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    private static boolean day_btn_event = false;
    private static boolean hr_btn_event = false;

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
            Hours[i].addItemListener((ItemEvent e) -> {
                if (!day_btn_event) {
                    SetScheduleHourChange(e);
                }
            });
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
                if (!hr_btn_event) {
                    SetScheduleDayChange(e);
                }
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
        ScheduleCheckList.setLayout(new GridLayout(8, 10, 1, 5));

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

        JPanel schedule_panel = new JPanel();
        schedule_panel.add(ScheduleCheckList);

        Main.Pan.add(schedule_panel, "SchedulePage");
    }

    //sets affinity hours
    static void SetAffinityHours(Functions fn) {
        if (Variables.Affinity) {
            SchedulePage.ScheduleCheckBox[5][4].setText("Afinity");
            SchedulePage.ScheduleCheckBox[5][5].setText("Afinity");
            SchedulePage.ScheduleCheckBox[5][6].setText("Afinity");
            SchedulePage.ScheduleCheckBox[5][4].setEnabled(false);
            SchedulePage.ScheduleCheckBox[5][5].setEnabled(false);
            SchedulePage.ScheduleCheckBox[5][6].setEnabled(false);
            for (int i = 0; i < fn.SubjectCount; i++) {
                for (int j = 4; j < 7; j++) {
                    fn.BusyScheduleInfo[i][5][j] = true;
                }
            }
        } else {
            SchedulePage.ScheduleCheckBox[5][4].setText("");
            SchedulePage.ScheduleCheckBox[5][5].setText("");
            SchedulePage.ScheduleCheckBox[5][6].setText("");
            SchedulePage.ScheduleCheckBox[5][4].setEnabled(true);
            SchedulePage.ScheduleCheckBox[5][5].setEnabled(true);
            SchedulePage.ScheduleCheckBox[5][6].setEnabled(true);
        }
    }

    private static void SetScheduleDayChange(ItemEvent e) {
        for (int i = 0; i < 6; i++) {
            if (Days[i] == e.getSource()) {
                for (int j = 0; j < 7; j++) {
                    day_btn_event = true;
                    ScheduleCheckBox[i][j].setSelected(Days[i].isSelected());
                    SetScheduleChange(new ItemEvent(ScheduleCheckBox[i][j], i, ScheduleCheckBox[i][j], j));
                }
                break;
            }
        }
        day_btn_event = false;
    }

    private static void SetScheduleChange(ItemEvent e) {
        boolean flag;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (e.getSource() == ScheduleCheckBox[i][j]) {
                    if (!day_btn_event) {
                        flag = true;
                        for (int k = 0; k < 7; k++) {
                            if (ScheduleCheckBox[i][k].isSelected() == false) {
                                flag = false;
                                break;
                            }
                        }
                        Days[i].setSelected(flag);
                    }
                    if (!hr_btn_event) {
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
        if (Variables.Affinity) {            
            SchedulePage.ScheduleCheckBox[5][4].setSelected(true);
            SchedulePage.ScheduleCheckBox[5][5].setSelected(true);
            SchedulePage.ScheduleCheckBox[5][6].setSelected(true);
        } else {
            SchedulePage.ScheduleCheckBox[5][4].setSelected(false);
            SchedulePage.ScheduleCheckBox[5][5].setSelected(false);
            SchedulePage.ScheduleCheckBox[5][6].setSelected(false);
        }
    }

    private static void SetScheduleHourChange(ItemEvent e) {
        for (int i = 0; i < 7; i++) {
            if (Hours[i] == e.getSource()) {
                for (int j = 0; j < 6; j++) {
                    hr_btn_event = true;
                    ScheduleCheckBox[j][i].setSelected(Hours[i].isSelected());
                    SetScheduleChange(new ItemEvent(ScheduleCheckBox[j][i], j, ScheduleCheckBox[j][i], i));
                }
                break;
            }
        }
        hr_btn_event = false;
    }
}//SchedulePage
