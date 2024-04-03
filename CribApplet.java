import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CribApplet extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Cribbage");
		CribApplet c = new CribApplet();
		f.setContentPane(c);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.pack();
		f.setVisible(true);
	}
	CribPanel cribPanel;
	
	public CribApplet() {
		makeGUI();
		setPreferredSize(new Dimension(750,500));
	}

	public class ActionButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) {
			cribPanel.actionButtonPressed();
		}
	}
	
	public JPanel makeGUI() {
		JPanel newPanel = this;
		newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
		cribPanel = new CribPanel();
		cribPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		cribPanel.setPreferredSize(new Dimension(750,509));
		cribPanel.setMinimumSize(new Dimension(750,509));
		cribPanel.setMaximumSize(new Dimension(750,509));
		newPanel.add(cribPanel);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		JButton actionButton = new JButton("OK!");
		actionButton.addActionListener(new ActionButtonListener());
		buttonPanel.add(actionButton);
		buttonPanel.add(Box.createHorizontalGlue());
		//newPanel.add(buttonPanel);
		newPanel.add(Box.createVerticalGlue());
		return newPanel;
	}

}
