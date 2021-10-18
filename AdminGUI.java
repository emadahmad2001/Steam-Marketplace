import Backend.Transaction;

import javax.swing.*;

public class AdminGUI {
    JFrame frame;
    JTextField t, t1, t2;
    JLabel label, label1, label2, label3, label4;
    JButton b, b1, b2, b3, b4, b5, b6, b7, b8;
    Transaction transaction;
    Login login;


    public void createFrame() {
        frame = new JFrame();//creating instance of JFrame

        t = new JTextField();//creating instance of JTextField
        t.setBounds(180, 100, 100, 40);//x axis, y axis, width, height
        t.setVisible(false);

        label = new JLabel("Enter Game:");
        label.setBounds(70, 100, 100, 40);
        label.setVisible(false);


        t1 = new JTextField();
        t1.setBounds(180, 160, 100, 40);
        t1.setVisible(false);

        label1 = new JLabel("Enter Amount");
        label1.setBounds(70, 160, 100, 40);
        label1.setVisible(false);

        t2 = new JTextField();
        t2.setBounds(180, 160, 100, 40);
        t2.setVisible(false);

        label2 = new JLabel("");
        label2.setBounds(70, 160, 100, 40);
        label2.setVisible(false);


        b = new JButton("Buy");
        b.setBounds(175, 20, 150, 40);
        b.addActionListener(e -> {
            setButtons(b);
            setInputsLabels();
            setBuy();
        });

        b1 = new JButton("Sell");
        b1.setBounds(175, 60, 150, 40);
        b1.addActionListener(e -> {
            setButtons(b1);
            setInputsLabels();
            setSell();
        });

        b2 = new JButton("View Marketplace");
        b2.setBounds(175, 100, 150, 40);
        b2.addActionListener(e -> {
            setButtons(b2);
        });

        b3 = new JButton("Refund");
        b3.setBounds(175, 140, 150, 40);
        b3.addActionListener(e -> {
            setButtons(b3);
            setInputsLabels();
            setRefund();
        });

        b4 = new JButton("Add Credit");
        b4.setBounds(175, 180, 150, 40);
        b4.addActionListener(e -> {
            setButtons(b4);
            setInputsLabels();
            setAddCredit();
        });

        b5 = new JButton("Auction Sale");
        b5.setBounds(175, 220, 150, 40);
        b5.addActionListener(e -> {
            setButtons(b5);
        });

        b6 = new JButton("Create New User");
        b6.setBounds(175, 260, 150, 40);
        b6.addActionListener(e -> {
            setButtons(b6);
        });

        b7 = new JButton("Enter");
        b7.setBounds(175, 500, 150, 40);
        b7.setVisible(false);
        b7.addActionListener(e -> {
        });

        b8 = new JButton("Logout");
        b8.setBounds(175, 300, 150, 40);
        b8.setVisible(true);
        b8.addActionListener(e -> {
            frame.setVisible(false);
            login.reset();
            login.frame.setVisible(true);
        });


        frame.add(b);
        frame.add(b1);
        frame.add(b2);
        frame.add(b3);
        frame.add(b4);
        frame.add(b5);
        frame.add(b6);
        frame.add(b7);
        frame.add(b8);
        frame.add(t);
        frame.add(t1);
        frame.add(t2);
        frame.add(label);
        frame.add(label1);
        frame.add(label2);


        frame.setSize(500, 600);//400 width and 500 height
        frame.setLayout(null);//using no layout managers
        frame.setVisible(true);//making the frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//When you press the program stops
        frame.setLocationRelativeTo(null);//Move frame to middle

    }

    public void setButtons(JButton button) {

        b.setVisible(true);
        b1.setVisible(true);
        b2.setVisible(true);
        b3.setVisible(true);
        b4.setVisible(true);
        b5.setVisible(true);
        b6.setVisible(true);
        b7.setVisible(true);

        button.setVisible(false);
    }

    public void setInputsLabels() {
        label.setVisible(false);
        label1.setVisible(false);
        label2.setVisible(false);

        t.setVisible(false);
        t1.setVisible(false);
        t2.setVisible(false);

    }

    public void setBuy() {

        label.setBounds(50, 350, 100, 40);
        label.setVisible(true);
        label.setText("Name of Game");

        t.setBounds(50, 375, 100, 40);
        t.setVisible(true);


        label2.setBounds(350, 350, 100, 40);
        label2.setVisible(true);
        label2.setText("Name of Seller:");

        t2.setBounds(350, 375, 100, 40);
        t2.setVisible(true);

        b7.addActionListener(e -> {
            //transaction.writeBuyToDaily(t2.getText(), t.getText(), transaction.currentUser.getName(), true);
        });
    }

    public void setSell() {

        label.setBounds(50, 350, 100, 40);
        label.setVisible(true);
        label.setText("Name of Game:");

        t.setBounds(50, 375, 100, 40);
        t.setVisible(true);

        label1.setBounds(350, 350, 100, 40);
        label1.setVisible(true);
        label1.setText("Sell For:");

        t1.setBounds(350, 375, 100, 40);
        t1.setVisible(true);

        b7.setText("Sell Game");
    }

    public void setRefund() {
        label.setBounds(50, 350, 100, 40);
        label.setVisible(true);
        label.setText("Name of Buyer:");

        t.setBounds(50, 375, 100, 40);
        t.setVisible(true);

        label1.setBounds(210, 350, 100, 40);
        label1.setVisible(true);
        label1.setText("Amount:");

        t1.setBounds(200, 375, 100, 40);
        t1.setVisible(true);

        label2.setBounds(350, 350, 100, 40);
        label2.setVisible(true);
        label2.setText("Name of Seller:");

        t2.setBounds(350, 375, 100, 40);
        t2.setVisible(true);


        b7.setText("Refund User");


    }

    public void setAddCredit() {
        label.setBounds(50, 350, 100, 40);
        label.setVisible(true);
        label.setText("Name of User:");

        t.setBounds(50, 375, 100, 40);
        t.setVisible(true);

        label1.setBounds(350, 350, 100, 40);
        label1.setVisible(true);
        label1.setText("Credit Amount:");

        t1.setBounds(350, 375, 100, 40);
        t1.setVisible(true);

        b7.setText("Add Credit To User");
    }

}
