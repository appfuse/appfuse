package org.appfuse.ui;

import org.appfuse.command.NewCommand;
import org.appfuse.control.ThreadManager;
import org.appfuse.engine.ApplicationData;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */
public class StartDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;

    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private Container container;
    private GridBagLayout gbLayout;
    private GridBagConstraints gbConstraints;

    private static ApplicationData data;

    {
        createUIComponents();
    }

    /**
     * Default Constructor
     */
    public StartDialog() {}

    public StartDialog(ApplicationData data) {
        this.data = data;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        InitializeDropDowns();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent
e)          {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent
e)          {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void InitializeDropDowns() {
        // database choices
        comboBox1.addItem("MySQL Version 5.x");

        // persistence choices
        comboBox2.addItem("Hibernate");
        comboBox2.addItem("iBatis");

        // web model-view-controller choices
        comboBox3.addItem("Struts2");
        comboBox3.addItem("Java Server Faces");
        comboBox3.addItem("Spring MVC");
        comboBox3.addItem("Tapestry");

        // TODO - add modular vs. basic

    }

    private void onOK() {

        if (data == null) {
            data = new ApplicationData();
        }

        data.setApplicationName(textField1.getText());
        data.setPackageName(textField2.getText());
        data.setDatabaseChoice((String) comboBox1.getSelectedItem());
        data.setDatabaseName(textField3.getText());
        data.setPersistenceChoice((String) comboBox2.getSelectedItem());
        data.setWebChoice((String) comboBox3.getSelectedItem());



        /**
         * Create command and add to ThreadManager for processing
         */
        NewCommand newCommand = new NewCommand(data);
        ThreadManager.getInstance().add(newCommand);


    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        StartDialog dialog = new StartDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }


    private void createUIComponents() {
        contentPane = new JPanel();
        gbLayout = new GridBagLayout();
        gbConstraints = new GridBagConstraints();
        contentPane.setLayout(gbLayout);

        final JLabel label1 = new JLabel();
        label1.setText("Application Name");

        final JLabel label2 = new JLabel();
        label2.setText("Package Name");

        final JLabel label3 = new JLabel();
        label3.setText("Database Name");

        final JLabel label4 = new JLabel();
        label4.setText("Database");

        final JLabel label5 = new JLabel();
        label5.setText("Persistence Module");

        final JLabel label6 = new JLabel();
        label6.setText("Web Modules");

        final JLabel label7 = new JLabel();
        label7.setIcon(new ImageIcon(getClass().getResource("/META-INF/appfuse-title.gif")));

        comboBox1 = new JComboBox();
        comboBox2 = new JComboBox();
        comboBox3 = new JComboBox();

        textField1 = new JTextField();
        textField1.setText("myapp");
        textField2 = new JTextField();
        textField2.setText("com.mycompany");
        textField3 = new JTextField();
        textField3.setText("mydb");
        textField1.setSize(200,10);
        textField2.setSize(200,10);
        textField3.setSize(200,10);

        buttonOK = new JButton("Ok");
        buttonCancel = new JButton("Cancel");

        gbConstraints.anchor = GridBagConstraints.WEST;

        addComponent(label1, 0,0,1,1);
        addComponent(label2, 1,0,1,1);
        addComponent(label3, 2,0,1,1);
        addComponent(label4, 3,0,1,1);
        addComponent(label5, 4,0,1,1);
        addComponent(label6, 5,0,1,1);

        gbConstraints.fill = GridBagConstraints.BOTH;

        addComponent(textField1,0,1,2,1);
        addComponent(textField2,1,1,2,1);
        addComponent(textField3,2,1,2,1);
        addComponent(comboBox1,3,1,2,1);
        addComponent(comboBox2,4,1,2,1);
        addComponent(comboBox3,5,1,2,1);

        gbConstraints.anchor = GridBagConstraints.WEST;
        gbConstraints.fill = GridBagConstraints.CENTER;

        addComponent(label7, 6,0,1,1);
        addComponent(buttonOK,5,1,1,0);
        addComponent(buttonCancel,5,2,1,0);
    }

    private void addComponent(Component c, int row, int column, int width, int height) {
        gbConstraints.gridx = column;
        gbConstraints.gridy = row;

        gbConstraints.gridwidth = width;
        gbConstraints.gridheight = height;

        gbLayout.setConstraints(c, gbConstraints);
        contentPane.add(c);

    }

    /**
     * Getter for dialog data
     * @return
     */
    public ApplicationData getData() {
        return data;
    }

    /**
     * Setter for dialog data
     * @param data
     */
    public void setData(ApplicationData data) {
        this.data = data;
    }
}
