

import Backend.Data;
import Backend.Transaction;

import javax.swing.*;

public class Login {
    JFrame frame;
    JTextField t, t1;
    JLabel label, label1, label2, label3, label4, label5;
    JButton b;
    Transaction transaction;

    public Login() {
        this.transaction = new Transaction();
    }

    public void createFrame() {
        frame = new JFrame();//creating instance of JFrame

        t = new JTextField();//creating instance of JTextField
        t.setBounds(180, 100, 100, 40);//x axis, y axis, width, height

        label = new JLabel("Enter Username");
        label.setBounds(70, 100, 100, 40);

        t1 = new JTextField();
        t1.setBounds(180, 160, 100, 40);

        label1 = new JLabel("Enter Password");
        label1.setBounds(70, 160, 100, 40);

        label2 = new JLabel("Wrong Password");
        label2.setBounds(130, 260, 150, 40);

        label3 = new JLabel("Login successful");
        label3.setBounds(130, 260, 150, 40);

        label4 = new JLabel("User does not exist");
        label4.setBounds(130, 260, 150, 40);

        label5 = new JLabel("Invalid input in UserLogins.txt");
        label5.setBounds(80, 260, 220, 40);

        b = new JButton("Enter");
        b.setBounds(130, 220, 100, 40);


        frame.add(t);//adding items to frame
        frame.add(t1);
        frame.add(b);
        frame.add(label);
        frame.add(label1);
        frame.add(label2);
        frame.add(label3);
        frame.add(label4);
        frame.add(label5);

        label2.setVisible(false);
        label3.setVisible(false);
        label4.setVisible(false);
        label5.setVisible(false);

        frame.setSize(400, 500);//400 width and 500 height
        frame.setLayout(null);//using no layout managers
        frame.setVisible(true);//making the frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//When you press the program stops
        frame.setLocationRelativeTo(null);//Move frame to middle

    }

    public void badPass() {
        label3.setVisible(false);
        label4.setVisible(false);
        label5.setVisible(false);
        label2.setVisible(true);
        frame.setVisible(true);
    }

    public void invalidTxt() {
        label3.setVisible(false);
        label4.setVisible(false);
        label2.setVisible(false);
        label5.setVisible(true);
        frame.setVisible(true);
    }

    public void badLogin() {
        label2.setVisible(false);
        label3.setVisible(false);
        label5.setVisible(false);
        label4.setVisible(true);
        frame.setVisible(true);
    }

    public void loginSuccess() {
        label2.setVisible(false);
        label4.setVisible(false);
        label5.setVisible(false);
        label3.setVisible(true);
        frame.setVisible(false);
    }

    public void reset() {
        label2.setVisible(false);
        label4.setVisible(false);
        label3.setVisible(false);
        label5.setVisible(false);
        t.setText("");
        t1.setText("");
    }

    public boolean guiPicker(String type) {
        if (type == null) {
            return false;
        }
        if (type.equals(Data.ADMIN)) {
            AdminGUI adminGUI = new AdminGUI();
            adminGUI.transaction = this.transaction;
            adminGUI.login = this;
            adminGUI.createFrame();
            return true;
        }
        return false;

    }

    
}

