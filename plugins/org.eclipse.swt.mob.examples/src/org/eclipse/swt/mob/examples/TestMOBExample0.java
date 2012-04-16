package org.eclipse.swt.mob.examples;

import org.eclipse.swt.mob.MOB;
import org.eclipse.swt.mob.MOBStyles.Align;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TestMOBExample0 {
	public static void main(String[] args) {
		Display d = new Display();

		MOB mob = new MOB();

		mob.window("My Window"). //
				label("User name:").text(). //
				label("Password:").text().//
				box(). //
				button("OK").button("Cancel");

		// Need to have fully css like syntax here
		mob.styles().
		/**/
		style().window().gridLayout(2).
		/**/
		style().box().gridLayout(5).span(2).grab(true, true).
		/**/
		style().button().align(Align.right).
		/**/
		style().label("Password:").text().grab(true, false).
		/**/
		style().label().text().grab(true, false);

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
