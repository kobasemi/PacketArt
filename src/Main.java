import java.awt.*;
import javax.swing.*;

public class Main {
	public static void main(String [] args) {
		JFrame frame = new Form("Entry", EntryForm.class);
		frame.setVisible(true);
		System.out.println("Closed.");
	}
}