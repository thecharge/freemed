/*
 * $Id$
 *
 * Authors:
 *      Jeff Buchbinder <jeff@freemedsoftware.org>
 *
 * FreeMED Electronic Medical Record and Practice Management System
 * Copyright (C) 1999-2008 FreeMED Software Foundation
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.freemedsoftware.gwt.client.widget;

import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SimpleUIBuilder extends Composite {

	/**
	 * Interface for any <SimpleUIBuilder> subclasses to receive infomation back
	 * from this piece.
	 * 
	 * @author jeff@freemedsoftware.org
	 * 
	 */
	public interface Receiver {

		/**
		 * Check to make sure data is valid.
		 * 
		 * @param data
		 * @return null if there are no errors, or else a list of errors.
		 */
		public String validateData(HashMap<String, String> data);

		/**
		 * Handle data.
		 * 
		 * @param data
		 */
		public void processData(HashMap<String, String> data);
	};

	protected FlexTable table;

	protected Receiver receiver = null;

	protected HashMap<String, Widget> widgets;

	public SimpleUIBuilder() {
		final VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		verticalPanel.setWidth("100%");

		table = new FlexTable();
		verticalPanel.add(table);

		Button commitChangesButton = new Button("Commit Changes");
		commitChangesButton.addClickListener(new ClickListener() {

			@Override
			public void onClick(Widget sender) {
				// Collect data
				HashMap<String, String> data = new HashMap<String, String>();

				// If a receiver has been set, push there
				if (receiver != null) {
					String v = receiver.validateData(data);
					if (v == null) {
						receiver.processData(data);
					}
				}
			}

		});

		// Initialize widget container
		widgets = new HashMap<String, Widget>();
	}

	/**
	 * Allow assigning of event <Receiver> to this widget.
	 * 
	 * @param r
	 */
	public void setReceiver(Receiver r) {
		receiver = r;
	}

	/**
	 * Add widget to display
	 * 
	 * @param name
	 *            Variable name to be associated with this widget.
	 * @param title
	 *            Caption for display to the user.
	 * @param type
	 *            Widget type, textual.
	 * @param options
	 *            Optional, to describe additional options.
	 * @param value
	 *            Default value.
	 */
	public void addWidget(String name, String title, String type,
			String options, String value) {
		Widget w;

		if (type.compareToIgnoreCase("text") == 0) {
			w = new TextBox();
		} else if (type.compareToIgnoreCase("modulemultiple") == 0) {
			w = new SupportModuleMultipleChoiceWidget(options);
		} else if (type.compareToIgnoreCase("module") == 0) {
			w = new SupportModuleWidget(options);
		} else if (type.compareToIgnoreCase("select") == 0) {
			w = new CustomListBox();

			// Push in all "options" values
			String[] o = options.split(",");
			for (int iter = 0; iter < o.length; iter++) {
				// Check for "description" pairs
				if (o[iter].contains("|")) {
					String[] i = o[iter].split("|");
					((CustomListBox) w).addItem(i[0], i[1]);
				}
				((CustomListBox) w).addItem(o[iter]);
			}
		} else if (type.compareToIgnoreCase("patient") == 0) {
			w = new PatientWidget();
		} else {
			// Unimplemented, use text box as fallback
			w = new TextBox();
		}

		// Add to indices and display
		widgets.put(name, w);
		table.setText(widgets.size() - 1, 0, title);
		table.setWidget(widgets.size() - 1, 1, w);

		// Set widget value after it is added.
		this.setWidgetValue(name, value);
	}

	/**
	 * Form HashMap containing all values contained in this widget.
	 * 
	 * @return
	 */
	public HashMap<String, String> getValues() {
		HashMap<String, String> c = new HashMap<String, String>();
		Iterator<String> iter = widgets.keySet().iterator();
		while (iter.hasNext()) {
			String k = iter.next();
			c.put(k, getWidgetValue(k));
		}
		return c;
	}

	/**
	 * Set all widget values.
	 * 
	 * @param c
	 */
	public void setValues(HashMap<String, String> c) {
		Iterator<String> iter = widgets.keySet().iterator();
		while (iter.hasNext()) {
			String k = iter.next();
			String v = c.get(k);
			if (v != null) {
				setWidgetValue(k, v);
			}
		}
	}

	/**
	 * Convenience method for extracting value from a contained widget given the
	 * widget's name in the widgets hashmap.
	 * 
	 * @param name
	 *            "name" key in the widgets hashmap
	 * @return Value of the specified widget, or null if none is found.
	 */
	public String getWidgetValue(String name) {
		Widget w = widgets.get(name);
		if (w instanceof TextBox) {
			return ((TextBox) w).getText();
		}
		if (w instanceof CustomListBox) {
			return ((CustomListBox) w).getWidgetValue();
		}
		if (w instanceof SupportModuleWidget) {
			return ((SupportModuleWidget) w).getValue().toString();
		}
		if (w instanceof SupportModuleMultipleChoiceWidget) {
			return ((SupportModuleMultipleChoiceWidget) w)
					.getCommaSeparatedValues();
		}
		if (w instanceof PatientWidget) {
			return ((PatientWidget) w).getValue().toString();
		}
		return null;
	}

	/**
	 * Convenience method for setting value of a contained widget given the
	 * widget's name in the widgets hashmap.
	 * 
	 * @param name
	 *            "name" key in the widgets hashmap
	 * @param value
	 *            Value to assign
	 */
	public void setWidgetValue(String name, String value) {
		Widget w = widgets.get(name);
		if (value != null) {
			if (w instanceof TextBox) {
				((TextBox) w).setText(value);
			}
			if (w instanceof CustomListBox) {
				((CustomListBox) w).setWidgetValue(value);
			}
			if (w instanceof SupportModuleWidget) {
				((SupportModuleWidget) w).setValue(new Integer(value));
			}
			if (w instanceof SupportModuleMultipleChoiceWidget) {
				((SupportModuleMultipleChoiceWidget) w)
						.setCommaSeparatedValues(value);
			}
			if (w instanceof PatientWidget) {
				((PatientWidget) w).setValue(new Integer(value));
			}
		}
	}

}