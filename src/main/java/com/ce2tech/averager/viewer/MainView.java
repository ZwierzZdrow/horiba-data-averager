package com.ce2tech.averager.viewer;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class MainView extends JFrame {

    @Getter private DataPanel dataPanel;

    public MainView() {
        initFrame();
        initDataPanel();
        initMainMenuBar();

        setVisible(true);
    }

    //METHODS
    private void initFrame() {
        setTitle("Horiba data averager");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLayout(new BorderLayout(10,10));

//        //Close listener copy-pasted from stackoverflow
//        addWindowListener( new WindowAdapter()
//        {
//            public void windowClosing(WindowEvent e)
//            {
//                JFrame frame = (JFrame)e.getSource();
//
//                int result = JOptionPane.showConfirmDialog(
//                        frame,
//                        "Are you sure you want to exit the application?",
//                        "Exit Application",
//                        JOptionPane.YES_NO_OPTION);
//
//                if (result == JOptionPane.YES_OPTION)
//                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            }
//        });
    }

    private void initDataPanel() {
        dataPanel = new DataPanel();
        add(dataPanel, BorderLayout.CENTER);
    }

    private void initMainMenuBar() {
        JMenuBar mainMenuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem closeApp = new JMenuItem("Close");
        JMenu calculations = new JMenu("Calculations");
        JMenuItem averageData60 = new JMenuItem("Convert to 1-min average");
        JMenuItem averageData3600 = new JMenuItem("Convert to 1-hour average");
        JMenuItem averageCustom = new JMenuItem("Convert custom average");
        JMenu help = new JMenu("Help");
        JMenuItem aboutApp = new JMenuItem("About Application");

        openFile.addActionListener( (e) -> new ChooseFile(dataPanel, e.getActionCommand()) );
        saveFile.addActionListener( (e) -> new ChooseFile(dataPanel, e.getActionCommand()) );
        averageData60.addActionListener( (e) -> dataPanel.averageData(60, LocalTime.MIN, LocalTime.MAX) );
        averageData3600.addActionListener( (e) -> dataPanel.averageData(3600, LocalTime.MIN, LocalTime.MAX) );
        averageCustom.addActionListener( (e) -> dataPanel.averageData(60, LocalTime.of(14,20), LocalTime.of(15,10)));
        aboutApp.addActionListener( (e) ->
                JOptionPane.showMessageDialog(dataPanel, "Program was created by Michal \"Lasuch\" Garus.\n" +
                        "If you need any help you can buy a box of cakes and come to me directly.\n" +
                        "You can also try to email me, not guarantee I'll response without cakes.\n" +
                        "m.garus@ce2tech.pl", "About Application", JOptionPane.INFORMATION_MESSAGE) );

        mainMenuBar.add(menuFile);
        menuFile.add(openFile);
        menuFile.add(saveFile);
        menuFile.addSeparator();
        menuFile.add(closeApp);
        mainMenuBar.add(calculations);
        calculations.add(averageData60);
        calculations.add(averageData3600);
        calculations.add(averageCustom);
        mainMenuBar.add(help);
        help.add(aboutApp);
        setJMenuBar(mainMenuBar);
    }

}