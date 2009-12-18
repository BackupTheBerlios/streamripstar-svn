package misc;
/* This program is licensed under the terms of the GPL V3 or newer*/
/* Written by Johannes Putzke*/
/* eMail: die_eule@gmx.net*/ 

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

class SelectAllListener implements MouseListener{
		
		private JTextField x = null;
		
		public SelectAllListener(JTextField x) {
			this.x = x;
		}

		public void mouseClicked(MouseEvent e) {
			//Right-click button for menu
			if(e.getButton() == 3) {
				x.setSelectionStart(0);
				x.setSelectionEnd(x.getText().length());
			}
		}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			
		}

	}