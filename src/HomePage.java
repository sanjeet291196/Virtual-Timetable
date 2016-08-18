
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// class for defining homepage
class HomePage {

    /**
     * Year Selection Component
     */
    static JComboBox<String> YearSelector;
    /**
     * Semester Selection Component
     */
    static JComboBox<String> SemSelector;
    /**
     * Branch Selection Component
     */
    static JComboBox<String> BranchSelector;
    /**
     * NextButton to goto SubjectPage
     */
    static JButton NextButton;

    //intializing homepage elements
    static void init(Main frame) {
        /*
         Create and initialise YearSelector
         */
        YearSelector = new JComboBox<>();
        YearSelector.addItem("I Year");
        YearSelector.addItem("II Year");
        YearSelector.addItem("III Year");
        YearSelector.addItem("IV Year");
        YearSelector.addItemListener((ItemEvent e) -> {
            //if YearSelector Choice is changed
            //set corresponding semester
            SemSelector.removeAllItems();
            SemSelector.addItem("I Sem");
            if (YearSelector.getSelectedIndex() != 0) {
                SemSelector.addItem("II Sem");
            }
        });

        /*
         Create and initialise SemSelector
         */
        SemSelector = new JComboBox<>();
        SemSelector.addItem("I Sem");

        /*
         Create and initialise BranchSelector
         */
        BranchSelector = new JComboBox<>();
        BranchSelector.addItem("CSE");
        BranchSelector.addItem("ECE");
        BranchSelector.addItem("EEE");
        BranchSelector.addItem("MECH");

        /*
         Initialize NextButton
         */
        NextButton = new JButton("Next");
        NextButton.addActionListener((ActionEvent e) -> {
            //if NextButton on HomePage is clicked
            //goto SubjectPage
            try {
                SubjectPage.showFacultyPage(frame);
                SubjectPage.faculty[0].EmpId.requestFocus();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ERROR:Cannot Read File");
            }
        });
    }

    //placing homepage elements to panel and displaying the pan
    static void initHomePage(Main frame) {
        JPanel Home = new JPanel();
        Home.setLayout(new GridLayout(0, 2,20,5));
        Home.add(new JLabel("Year:"));
        Home.add(HomePage.YearSelector);
        Home.add(new JLabel("Semister:"));
        Home.add(HomePage.SemSelector);
        Home.add(new JLabel("Branch:"));
        Home.add(HomePage.BranchSelector);
        Home.add(new JLabel(""));
        Home.add(HomePage.NextButton);

        
        JPanel Home_pan = new JPanel();
        
        
        Home_pan.add(Home);
        

        Main.Pan.add(Home_pan, "HomePage");
        
        frame.pack();
    }
}//homepage
