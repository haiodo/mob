package org.eclipse.swt.mob;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.mob.internal.Factories;
import org.eclipse.swt.mob.internal.MOBProperties;
import org.eclipse.swt.mob.internal.MOBWidget;
import org.eclipse.swt.mob.internal.WidgetKind;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

public class MOB {
	private MOBWidget currentWindow = null;
	private MOBWidget display = new MOBWidget(null, null);
	private List<MOBWidget> currentGroup = new ArrayList<MOBWidget>();
	private DataBindingContext dbc;
	private MOBWidget lastChild;

	public interface WidgetFactory<T> {
		Widget create(T parent, int style);

		int getDefaultStyle();

		WidgetKind getKind();
	}

	public void show(Display d) {
		display.setDisplay(d);
		display.construct(null);
		d.syncExec(new Runnable() {
			@Override
			public void run() {
				dbc = new DataBindingContext(SWTObservables.getRealm(display
						.getDisplay()));
			}
		});
		display.show();
	}

	public MOB window(String text) {
		MOBWidget widget = null;
		if (currentWindow != null) {
			widget = newChild(currentWindow, Factories.shell);
		} else {
			widget = newChild(display, Factories.displayShell);
			currentWindow = widget;
			currentGroup.add(0, currentWindow);
		}
		widget.setAttr("text", text);
		return this;
	}

	private MOBWidget newChild(MOBWidget root, WidgetFactory factory) {
		MOBWidget ch = new MOBWidget(root, factory);
		if (root != null) {
			root.append(ch);
		}
		lastChild = ch;
		return ch;
	}

	public MOB label(String value) {
		newChild(currentGroup.get(0), Factories.text).setAttr(
				MOBProperties.TEXT, value);
		return this;
	}

	public MOB text() {
		newChild(currentGroup.get(0), Factories.editbox);
		return this;
	}

	public MOB button(String value) {
		newChild(currentGroup.get(0), Factories.push_button).setAttr(
				MOBProperties.TEXT, value);
		return this;
	}

	public MOB vbox() {
		MOBWidget widget = newChild(currentGroup.get(0), Factories.composite)
				.setAttr(MOBProperties.LAYOUT,
						new Object[] { MOBProperties.Layouts.vrow });
		currentGroup.add(0, widget);
		return this;
	}

	public MOB box() {
		MOBWidget widget = newChild(currentGroup.get(0), Factories.composite);
		currentGroup.add(0, widget);
		return this;
	}

	public MOBStyles styles() {
		return new MOBStyles(display);
	}
}
