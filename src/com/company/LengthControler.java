package com.company;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class LengthControler extends KeyAdapter {
	JTextField text;
	public LengthControler(JTextField text){
		this.text = text;
	}
	public void keyTyped(KeyEvent e) {
		if (this.text.getText().length() >= Main.MAX_STRING_SIZE) {
			e.consume();
		}
	}

}
