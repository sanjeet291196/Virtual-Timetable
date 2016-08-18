
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * output class
 */
class Output {

    /**
     * <code>timetable</code> : 2D array to store output timetable during
     * generation
     */
    String timetable[][];

    /**
     * <code>OPGrid</code> : to place generated <code>timetable</code><p>
     * <code>OPIndex</code> : to place information of faculties for respective
     * subject<p>
     * <code>OP</code> : to place OPGrid and OPIndex<p>
     */
    JPanel OP, OPGrid, OPIndex;

    /**
     * <code>OutputTimeTable</code> : to display the generated timetable in
     * table<p>
     * <code>dayTable</code> : To display days of weak<p>
     * <code>hourTable</code> : To Display Hour Numbers
     */
    JTable OutputTimeTable, dayTable, hourTable;

    public Output() {
        timetable = new String[6][7];
    }

    /**
     * displays the generated timetable
     */
    void display(int SubjectCount, int LabCount, Main frame) {
        String days[][] = {{"Mon"}, {"Tue"}, {"Wed"}, {"Thu"}, {"Fri"}, {"Sat"}};
        String Columns[][] = {{"Days", "1", "2", "3", "4", "5", "6", "7"}};
        String Columns1[] = {"1", "2", "3", "4", "5", "6", "7"};
        OP = new JPanel(new BorderLayout());
        dayTable = new JTable(days, days[0]);
        hourTable = new JTable(Columns, Columns[0]);
        OutputTimeTable = new JTable(timetable, Columns1);

        OPGrid = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 8;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 7);
        OPGrid.add(hourTable, gbc);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 6;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(1, 0, 6, 0);
        OPGrid.add(dayTable, gbc);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 7;
        gbc.gridheight = 6;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(1, 1, 6, 7);
        OPGrid.add(OutputTimeTable, gbc);

        OutputTimeTable.setEnabled(false);
        dayTable.setEnabled(false);
        hourTable.setEnabled(false);

        OPIndex = new JPanel(new GridLayout(SubjectCount + 1, 2,50,10));
        OPIndex.add(new JLabel("Subject"));
        OPIndex.add(new JLabel("Faculty"));
        for (int i = 0; i < SubjectCount; i++) {
            OPIndex.add(new JLabel(SubjectPage.Subs[i].getText()));
            OPIndex.add(new JLabel(SubjectPage.faculty[i].FacultyName.getText()));
        }

        OP.add(OPGrid, BorderLayout.NORTH);
        OP.add(new JLabel());
        JPanel opIndexContainer = new JPanel();
        opIndexContainer.add(OPIndex);
        OP.add(opIndexContainer, BorderLayout.SOUTH);
        Main.Pan.add(OP, "Output");

        frame.pack();

    }

}//Output
