/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Stefan Papp
 */
public class WebDavLoginFrame extends JFrame {

    private JButton login;
    private JButton cancel;
    private JTextField uname;
    private JPasswordField pass;
    private JLabel u;
    private JLabel p;

    public WebDavLoginFrame() {
        setTitle("Login");
        setLayout(new GridLayout(3, 2));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        u = new JLabel("Username");
        p = new JLabel("Password");

        uname = new JTextField(20);
        pass = new JPasswordField(20);

        login = new JButton("Login");
        cancel = new JButton("Cancel");

        add(u);
        add(uname);

        add(p);
        add(pass);

        add(login);
        add(cancel);

        uname.requestFocus();

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String un = uname.getText();
                String pa = new String(pass.getPassword());

                if ((un.equals("myuser")) && (pa.equals("mypass"))) {
                    dispose();
                    //add code for connection here
                }
            }
        });

        KeyAdapter k = new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    login.doClick();
                }
            }
        };

        pass.addKeyListener(k);
        uname.addKeyListener(k);

        pack();
        setLocationRelativeTo(null);

    }
}