package org.eclipse.swt.mob.internal;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.mob.MOB.WidgetFactory;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class Factories {
	public static WidgetFactory<Shell> shell = new WidgetFactory<Shell>() {
		@Override
		public Widget create(Shell parent, int style) {
			Shell widget = new Shell(parent, style);
			GridLayoutFactory.swtDefaults().applyTo(widget);
			return widget;
		}

		@Override
		public int getDefaultStyle() {
			return SWT.SHELL_TRIM | SWT.RESIZE;
		}
	};
	public static WidgetFactory<Display> displayShell = new WidgetFactory<Display>() {
		@Override
		public Widget create(Display parent, int style) {
			Shell widget = new Shell(parent, style);
			GridLayoutFactory.swtDefaults().applyTo(widget);
			return widget;
		}

		@Override
		public int getDefaultStyle() {
			return SWT.SHELL_TRIM | SWT.RESIZE;
		}
	};
	public static WidgetFactory<Composite> text = new WidgetFactory<Composite>() {
		@Override
		public Widget create(Composite parent, int style) {
			Label widget = new Label(parent, style);
			applyDefaultWidgetLayout(widget);
			return widget;
		}

		public int getDefaultStyle() {
			return SWT.WRAP;
		};
	};
	public static WidgetFactory<Composite> editbox = new WidgetFactory<Composite>() {

		@Override
		public Widget create(Composite parent, int style) {
			Text widget = new Text(parent, style);
			applyDefaultWidgetLayout(widget);
			return widget;
		}

		@Override
		public int getDefaultStyle() {
			return SWT.BORDER;
		}
	};
	public static WidgetFactory<Composite> push_button = new WidgetFactory<Composite>() {

		@Override
		public Widget create(Composite parent, int style) {
			Button widget = new Button(parent, style | SWT.PUSH);
			applyDefaultWidgetLayout(widget);
			return widget;
		}

		@Override
		public int getDefaultStyle() {
			return 0;
		}
	};
	public static WidgetFactory<Composite> composite = new WidgetFactory<Composite>() {
		@Override
		public int getDefaultStyle() {
			return 0;
		}

		@Override
		public Widget create(Composite parent, int style) {
			Composite widget = new Composite(parent, style);
			GridLayoutFactory.swtDefaults().applyTo(widget);
			return widget;
		}
	};

	protected static void applyDefaultWidgetLayout(Control widget) {
		if (widget instanceof Label) {
			return;
		}
		if (widget instanceof Text | widget instanceof StyledText) {
			if (widget.getParent().getLayout() instanceof GridLayout) {
				GridDataFactory.fillDefaults().grab(true, false)
						.applyTo(widget);
			}
		}
	}

}
