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
import org.eclipse.swt.mob.MOBStyles.Align;
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
	private WidgetKind kind;

	public MOBWidget(MOBWidget parent, WidgetFactory<?> clazz) {
		this.parent = parent;
		this.factory = clazz;
		if (clazz != null) {
			this.kind = clazz.getKind();
		} else {
			this.kind = WidgetKind.display;
		}

	}

	public WidgetKind getKind() {
		return kind;
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

	public String getAttrStr(String key) {
		if (properties != null) {
			Object value = properties.get(key);
			if (value != null) {
				return value.toString();
			}
		}
		return null;
	}

	public void construct(MOBWidget parent) {
		this.parent = parent;

		if (factory != null) {
			widget = factory.create(parent != null ? parent.getWidget() : null,
					collectStyle());
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
			applyStyle((Integer[]) properties.get(MOBProperties.LAYOUT_SPAN),
					(Align[]) properties.get(MOBProperties.LAYOUT_ALIGN),
					(Boolean[]) properties.get(MOBProperties.LAYOUT_GRAB));
		}
		if (children != null && children.size() > 0) {
			// construct and add all childrens
			for (MOBWidget widget : children) {
				widget.construct(this);
			}
		}

	}

	private void applyStyle(Integer[] span, Align[] aligns, Boolean[] grab) {
		if (widget instanceof Control) {
			Control ctrl = (Control) widget;
			if (ctrl.getParent() != null
					&& ctrl.getParent().getLayout() instanceof GridLayout) {
				if (ctrl.getLayoutData() == null) {
					GridDataFactory.fillDefaults().applyTo(ctrl);
				}
				GridData data = (GridData) ctrl.getLayoutData();
				if (span != null) {
					data.horizontalSpan = span[0];
					data.verticalSpan = span[1];
				}
				if (aligns != null && aligns.length > 0) {
					for (Align align : aligns) {
						switch (align) {
						case bottom:
							data.verticalAlignment |= SWT.END;
							break;
						case left:
							data.horizontalAlignment |= SWT.BEGINNING;
							break;
						case right:
							data.horizontalAlignment |= SWT.END;
							break;
						case top:
							data.verticalAlignment |= SWT.BEGINNING;
							break;
						case center:
							data.horizontalAlignment |= SWT.CENTER;
							break;
						case vcenter:
							data.verticalAlignment |= SWT.CENTER;
							break;
						case fill:
							data.horizontalAlignment |= SWT.FILL;
							break;
						case vfill:
							data.verticalAlignment |= SWT.FILL;
							break;
						}
					}
				}
				if (grab != null) {
					if (grab[0]) {
						//data.horizontalAlignment = SWT.FILL;
						data.grabExcessHorizontalSpace = true;
					} else {
						data.grabExcessHorizontalSpace = false;
					}
					if (grab[1]) {
						//data.verticalAlignment = SWT.FILL;
						data.grabExcessVerticalSpace = true;
					} else {
						data.grabExcessVerticalSpace = false;
					}
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

	public List<MOBWidget> getChildren() {
		return children;
	}

	public MOBWidget getParent() {
		return parent;
	}
}
