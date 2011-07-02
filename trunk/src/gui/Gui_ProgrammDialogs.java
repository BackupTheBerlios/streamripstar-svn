package gui;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JList;

/**
 * This Class shows a dialog with a table in it. In the table you can 
 * select one row. The selectet text in the row will be set a jtextField,
 * which must be defined, when the construktor is called 
 * @author Johannes Putzke
 *
 */
public class Gui_ProgrammDialogs extends JDialog{
	private static final long serialVersionUID = -3539544765340329305L;

	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");
	private JList list = new JList();
	private JScrollPane listScrollPane = new JScrollPane(list);
	
	/**
	 * 
	 * @param parentFrame
	 * @param title
	 * @param saveField
	 */
	public Gui_ProgrammDialogs(Gui_Settings2 parentFrame, String title, JTextField saveField) {
		super(parentFrame, title);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets( 2, 5, 2, 5);
		c.anchor = GridBagConstraints.NORTHWEST;

		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(listScrollPane);
		c.weighty = 0.0;
		c.weightx = 0.0;
		c.gridy = 1;
		add(okButton);
		c.weightx = 1.0;
		c.gridy = 1;
		add(new JLabel(" ")); //for better look
		c.weightx = 0.0;
		c.gridx = 3;
		add(cancelButton);
	}
}
