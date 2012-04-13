package org.eclipse.swt.mob.examples;

import org.eclipse.swt.mob.MOB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TestMOBExample0 {
	public static void main(String[] args) {
		Display d = new Display();

		MOB mob = new MOB();
		mob.window("My Window").gridLayout(2). //
				label("User name:").text(). //
				label("Password:").text().//
				vbox().span(2). //
				button("OK").button("Cancel");
		mob.show(d);
		while (!d.isDisposed()) {
			if (!d.readAndDispatch()) {
				d.sleep();
			}
			Shell[] shells = d.getShells();
			if (shells.length == 0) {
				break;
			}
		}
	}
}
