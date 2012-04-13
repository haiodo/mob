package org.eclipse.swt.mob.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.mob.MOB.WidgetFactory;
import org.eclipse.swt.mob.internal.MOBProperties.Layouts;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

public class MOBWidget {
	private MOBWidget parent;
	private List<MOBWidget> children = null;
	private WidgetFactory factory;
	private Map<String, Object> properties = null;
	private Widget widget;
	private Display display;

	public MOBWidget(WidgetFactory<?> clazz) {
		this.factory = clazz;
	}

	public void append(MOBWidget child) {
		if (children == null) {
			children = new ArrayList<MOBWidget>();
		}
		children.add(child);
	}

	public MOBWidget setAttr(String key, Object value) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		properties.put(key, value);
		return this;
	}

	public void construct(MOBWidget parent) {
		this.parent = parent;

		if (factory != null) {
			widget = factory.create(parent != null ? parent.getWidget() : null,
					collectStyle());
		}
		if (children != null && children.size() > 0) {
			// construct and add all childrens
			for (MOBWidget widget : children) {
				widget.construct(this);
			}
		}
		// Update properties
		if (properties != null) {
			Object textValue = properties.get(MOBProperties.TEXT);
			if (textValue != null && textValue instanceof String) {
				setText((String) textValue);
			}
			if (widget instanceof Composite) {
				Object[] layout = (Object[]) properties
						.get(MOBProperties.LAYOUT);
				applyLayout(layout);
			}
			// Apply layout style
			applyStyle((Integer[]) properties.get(MOBProperties.LAYOUT_SPAN));
		}

	}

	private void applyStyle(Integer[] span) {
		if (span != null && widget instanceof Control) {
			Control ctrl = (Control) widget;
			if (ctrl.getParent().getLayout() instanceof GridLayout) {
				if (ctrl.getLayoutData() == null) {
					GridDataFactory.fillDefaults().span(span[0], span[1]);
				} else if (ctrl.getLayoutData() instanceof GridData) {
					GridData gdata = (GridData) ctrl.getLayoutData();
					gdata.horizontalSpan = span[0];
					gdata.verticalSpan = span[1];
				}
			}
		}
	}

	private void applyLayout(Object[] layout) {
		if (layout != null) {
			MOBProperties.Layouts l = (Layouts) layout[0];
			switch (l) {
			case grid:
				GridLayoutFactory.swtDefaults().equalWidth(false)
						.numColumns((Integer) layout[1])
						.applyTo((Composite) widget);
				break;
			case row:
				RowLayoutFactory.swtDefaults().type(SWT.HORIZONTAL)
						.applyTo((Composite) widget);
				break;
			case vrow:
				RowLayoutFactory.swtDefaults().type(SWT.HORIZONTAL)
						.applyTo((Composite) widget);
				break;
			}
		}
	}

	private Widget getWidget() {
		return widget;
	}

	public Display getDisplay() {
		if (display != null) {
			return display;
		}
		if (parent != null) {
			return parent.getDisplay();
		}
		return null;
	}

	private void setText(String textValue) {
		if (widget instanceof Shell) {
			((Shell) widget).setText(textValue);
		} else if (widget instanceof Label) {
			((Label) widget).setText(textValue);
		} else if (widget instanceof Button) {
			((Button) widget).setText(textValue);
		}
	}

	private int collectStyle() {
		if (factory == null) {
			return 0;
		}
		return factory.getDefaultStyle();
	}

	public void show() {
		if (widget instanceof Shell) {
			Shell shell = (Shell) widget;
			shell.open();
		}
		if (widget == null && children != null) { // This is root, show
													// windows
			for (MOBWidget child : children) {
				child.show();
			}
		}
	}

	public void setDisplay(Display d) {
		this.display = d;
	}
}
