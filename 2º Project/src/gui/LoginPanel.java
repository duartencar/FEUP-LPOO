package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.google.common.hash.Hashing;

import engine.Coach;

public class LoginPanel extends JPanel {

	private static AppInterface app;
	private String name;
	private JTextField usernameField;
	private JLabel usernameLabel;
	private JPasswordField passwordField;
	private JLabel passwordLabel;
	private JButton signInButton;
	private static JLabel msgLabel;

	public LoginPanel(String panelName, AppInterface app) {

		if(panelName == null || panelName.isEmpty()) {
			throw new IllegalArgumentException("Invalid name!");
		}

		this.app = app;

		setName(panelName);

		initialize();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private void initialize() {

		setBounds(100, 100, 432, 768);

		usernameLabel = new JLabel("Username");
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		usernameLabel.setBounds(75, 200, 79, 20);
		add(usernameLabel);

		usernameField = new JTextField();
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		usernameField.setBounds(239-75, 152+50, 164, 20);
		add(usernameField);
		usernameField.setColumns(10);

		passwordLabel = new JLabel("Password");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordLabel.setBounds(75, 181+50, 79, 20);
		add(passwordLabel);

		msgLabel = new JLabel("Insert username and password");
		msgLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		msgLabel.setBounds(75, 106+50, 257, 28);
		add(msgLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(239-75, 183+50, 164, 20);
		add(passwordField);

		signInButton = new JButton("Sign In");
		signInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logIn();
			}
		});
		signInButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		signInButton.setBounds(75, 228+50, 83, 23);
		add(signInButton);

		JButton btnNewButton = new JButton("Create account");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.setBounds(253-75, 228+50, 155, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createAccount();
			}
		});
		add(btnNewButton);
	}

	private void createAccount() {

		String userName = null;
		String password = null;
		String coachName = null;
		ArrayList<String> poolNames = app.getDataBaseConnection().getPoolNames();

		while((userName = JOptionPane.showInputDialog("Insert username:")) != null) {
			if(!app.getDataBaseConnection().checkIfUserExists(userName)) {
				break;
			}

			JOptionPane.showMessageDialog(this, "That username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
		}

		if(userName == null) {
			return;
		}

		if((password = getHash(JOptionPane.showInputDialog("Insert password:"))) == null) {
			return;
		}

		if((coachName = JOptionPane.showInputDialog("Insert your name:")) == null) {
			return;
		}

		Object[] pools = new Object[poolNames.size()];

		int i = 0;

		for(String name : poolNames) {
			pools[i] = poolNames.get(i);
			i++;
		}

		Object selectedPool = JOptionPane.showInputDialog(this,
				"Choose one of the following pools",
				"Choose your pool",
				JOptionPane.INFORMATION_MESSAGE,
				null,
				pools,
				pools[0]);

		if(selectedPool == null) {
			return;
		}

		boolean poolMaster = JOptionPane.showConfirmDialog(this, "Are you the pool master", "Are you in charge?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION;

		if(!app.getDataBaseConnection().insertNewUser(userName, password, coachName, (String)selectedPool, poolMaster)) {
			JOptionPane.showMessageDialog(this, "Failed to add coach", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void logIn() {

		String username = usernameField.getText();

		char[] password = passwordField.getPassword();

		String pw = getHash(new String(password));
		
		msgLabel.setText("Authenticating");

		app.setLogged(authenticate(username, pw));
	}

	private static String getHash(String password) { 	
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}

	private static boolean authenticate(String username, String hashedPassword) {

		if(!checkIfPasswordsMatch(username, hashedPassword)) {
			msgLabel.setText("Authentication failed!");
			return false;
		}

		return loadUser(username);
	}

	private static boolean checkIfPasswordsMatch(String username, String hashedPassword) {

		String userPassword = app.getDataBaseConnection().getStoredPassword(username);

		if(userPassword == null ) {
			msgLabel.setText("Failed to connect");
			return false;
		}

		return userPassword.equals(hashedPassword);
	}

	private static boolean loadUser(String username) {    	
		Coach user = app.getDataBaseConnection().getUser(username);

		if(user == null) {
			msgLabel.setText("Authentication failed!");
			return false;
		}

		msgLabel.setText("Bem vindo " + user.getName());

		app.setUser(user);

		return true;
	}

	public void setLogInOption(boolean state) {
		usernameField.setEnabled(state);
		passwordField.setEnabled(state);
		signInButton.setEnabled(state);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); 
		g.setColor(new Color(223, 249, 251));

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("C:\\Programação\\LPOO1718_T3G16_SwimmingCoach\\media\\loginImage.png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		g.drawImage(image, 0, 0, 432, 225, null);
	}
}
