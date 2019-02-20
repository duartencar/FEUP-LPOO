package engine.Swimmer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

/**
 * Class that holds all the info of a swimmer, including its contests too.
 * @see DataBaseController
 * @see SwimmerContestsController
 */
public class Swimmer extends SwimmerContestsController {
	private int id;
	private String name;
	private LocalDate dateOfBirth;
	private int coach_id;
	private int pool_id;
	private SwimmerContestsController swimmerContests;
	private BufferedImage profilePicture = null;
	private boolean selected = false;

	/**
	 * constructor of Swimmer Class.
	 * @param id - swimmer id in database
	 * @param name - swimmer name
	 * @param dob - swimmer birthday
	 */
	public Swimmer (int id, String name, String dob) {

		if(name == null || name.length() == 0 || dob == null) {
			throw new IllegalArgumentException("Invalid arguments for swimmer creation!");
		}

		this.name = name;

		this.dateOfBirth = parseDate(dob);

		this.swimmerContests = new SwimmerContestsController();
	}

	/**
	 * Extracts date from string
	 * @param date - date to extract from
	 * @return a LocalDate object
	 * @see LocalDate
	 */
	public LocalDate parseDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		return LocalDate.parse(date, formatter);
	}

	/**
	 * @return swimmer database ID.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets swimmer ID to given id
	 * @param id - the new id
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * @return swimmer name.
	 */
	public String getSwimmerName() {
		return name;
	}

	/**
	 * @return swimmer birthday date.
	 */
	public String getSwimmerDateOfBirth() {
		return dateOfBirth.toString();
	}

	/**
	 * @return swimmer pool ID in database.
	 */
	public int getPool_id() {
		return pool_id;
	}

	/**
	 * Sets swimmer pool ID to given value
	 * @param pool_id - the new pool ID
	 */
	public void setPool_id(int pool_id) {
		this.pool_id = pool_id;
	}

	/**
	 * @return swimmer coach ID in database.
	 */
	public int getCoach_id() {
		return coach_id;
	}

	/**
	 * Sets swimmer coach ID to given value
	 * @param coach_id - the new coach ID
	 */
	public void setCoach_id(int coach_id) {
		this.coach_id = coach_id;
	}

	/**
	 * @return swimmer profile picture.
	 */
	public BufferedImage getProfilePicture() {
		return profilePicture;
	}

	/**
	 * Sets swimmer profile picture to given picture
	 * @param profilePicture - the new profile picture
	 */
	public void setProfilePicture(BufferedImage profilePicture) {
		this.profilePicture = profilePicture;
	}
	
	/**
	 * Loads a picture from resources folder
	 * @param imageName - name of the image to be loaded
	 */
	public void loadProfilePicture(String imageName) {
		try {
			profilePicture = ImageIO.read(new File("./media/" + imageName));
		} catch (IOException e) {
			try {
				profilePicture = ImageIO.read(new File("./media/" + "noPicture.png"));
			} catch (IOException e1) {
				System.out.println(e.getMessage());
			}
		}	
	}
	
	/**
	 * @return true if swimmer is selected.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Changes state of swimmer to selected or unselected
	 * @param selected - state (selected/unselected)
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Paints swimmer info to a component
	 * @param g - graphic context
	 * @param y - x coordinate where it starts to paint
	 * @param x - y coordinate where it starts to paint
	 */
	public void paint(Graphics g, int y, int x) {
		g.drawImage(profilePicture, 0, y, x, 100, null);
		if(selected) {
			g.setColor(Color.gray);
		}
		else {
			g.setColor(Color.white);
		}
		g.fillRect(x, y, 332, 100);
		g.setColor(new Color(39, 174, 96));
		g.drawRect(x, y, 332, 100);
		g.setFont(new Font("Tahoma", Font.PLAIN, 16));
		g.setColor(Color.black);
		g.drawString("Nome: " + name, x + 10, y + 30);
		g.drawString("DOB: " + dateOfBirth, x + 10, y + 60);
		g.drawString("ID: " + id, x + 10, y + 90);
	}
}
