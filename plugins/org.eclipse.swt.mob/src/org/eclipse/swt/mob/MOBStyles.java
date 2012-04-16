package org.eclipse.swt.mob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.mob.internal.MOBProperties;
import org.eclipse.swt.mob.internal.MOBWidget;
import org.eclipse.swt.mob.internal.WidgetKind;

public class MOBStyles {

	public static enum Align {
		left, right, top, bottom, center, vcenter, fill, vfill
	};

	private MOBWidget root;
	private MOBWidget current;

	public MOBStyles(MOBWidget display) {
		this.root = display;
	}

	public MOBStyles style() {
		current = root;
		return this;
	}

	public MOBStyles window(String... title) {
		Map<String, String> propertyMatcher = new HashMap<String, String>();
		if (title != null && title.length > 0) {
			propertyMatcher.put(MOBProperties.TEXT, title[0]);
		}
		findMove(WidgetKind.window, propertyMatcher);
		return this;
	}

	public MOBStyles box() {
		findMove(WidgetKind.composite, null);
		return this;
	}

	public MOBStyles label(String... title) {
		Map<String, String> propertyMatcher = new HashMap<String, String>();
		if (title != null && title.length > 0) {
			propertyMatcher.put(MOBProperties.TEXT, title[0]);
		}
		findMove(WidgetKind.label, propertyMatcher);
		return this;
	}

	public MOBStyles text() {
		findMove(WidgetKind.text, null);
		return this;
	}

	public MOBStyles button(String... title) {
		Map<String, String> propertyMatcher = new HashMap<String, String>();
		if (title != null && title.length > 0) {
			propertyMatcher.put(MOBProperties.TEXT, title[0]);
		}
		findMove(WidgetKind.button, propertyMatcher);
		return this;
	}

	private void findMove(WidgetKind kind, Map<String, String> propertyMatcher) {
		if (current == null) {
			current = root;
		}
		while (current != null) {
			nextWidget();
			if (current != null && current.getKind().equals(kind)) {
				if (propertyMatcher == null || propertyMatcher.size() == 0) {
					break; // We found required field
				}
				int matches = 0;
				for (String key : propertyMatcher.keySet()) {
					String attrStr = current.getAttrStr(key);
					if (attrStr != null) {
						if (attrStr.equals(propertyMatcher.get(key))) {
							matches++;
						}
					} else {
						// Failed to match
						break;
					}
				}
				if (matches == propertyMatcher.size()) {
					break; // We found matched item
				}
			}
		}
		if (current == null) {
			throw new RuntimeException("Failed to locate widget with kind: "
					+ kind.name());
		}
	}

	private void nextWidget() {
		if (current != null) {
			List<MOBWidget> children = current.getChildren();
			if (children != null && children.size() > 0) {
				current = children.get(0);
				return;
			}
			// No children, go next on parent.
			MOBWidget parent = current.getParent();
			if (parent != null) {
				List<MOBWidget> list = parent.getChildren();
				int index = list.indexOf(current);
				if (index + 1 < list.size()) {
					current = list.get(index + 1);
				} else {
					current = parent;
					nextWidget();
				}
			} else {
				current = null;
			}
		}
	}

	// Styles
	public MOBStyles gridLayout(int columns) {
		if (current != null) {
			current.setAttr(MOBProperties.LAYOUT, new Object[] {
					MOBProperties.Layouts.grid, columns });
		}
		return this;
	}

	public MOBStyles span(int columns) {
		if (current != null) {
			current.setAttr(MOBProperties.LAYOUT_SPAN, new Integer[] { columns,
					1 });
		}
		return this;
	}

	public MOBStyles align(Align... align) {
		if (current != null) {
			current.setAttr(MOBProperties.LAYOUT_ALIGN, align);
		}
		return this;
	}

	public MOBStyles grab(boolean width, boolean height) {
		if (current != null) {
			current.setAttr(MOBProperties.LAYOUT_GRAB, new Boolean[] { width,
					height });
		}
		return this;
	}
}
