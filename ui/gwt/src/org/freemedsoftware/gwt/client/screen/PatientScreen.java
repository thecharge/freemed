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

package org.freemedsoftware.gwt.client.screen;

import java.util.HashMap;

import org.freemedsoftware.gwt.client.ScreenInterface;
import org.freemedsoftware.gwt.client.Util;
import org.freemedsoftware.gwt.client.Api.PatientInterfaceAsync;
import org.freemedsoftware.gwt.client.screen.patient.LetterEntry;
import org.freemedsoftware.gwt.client.screen.patient.ProgressNoteEntry;
import org.freemedsoftware.gwt.client.widget.PatientInfoBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PatientScreen extends ScreenInterface {

	protected TabPanel tabPanel;

	protected PatientInfoBar patientInfoBar = null;

	protected Integer patientId = new Integer(0);

	/**
	 * @gwt.typeArgs <java.lang.String,java.lang.String>
	 */
	protected HashMap patientInfo;

	public PatientScreen() {

		final VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		patientInfoBar = new PatientInfoBar();
		verticalPanel.add(patientInfoBar);

		{
			final MenuBar menuBar = new MenuBar();
			verticalPanel.add(menuBar);

			final MenuBar menuBar_1 = new MenuBar(true);

			menuBar_1.addItem("Letter", new Command() {
				public void execute() {
					Util.spawnTabPatient("Letter", new LetterEntry(), state,
							getObject());
				}
			});

			menuBar_1.addItem("Progress Note", new Command() {
				public void execute() {
					Util.spawnTabPatient("Progress Note",
							new ProgressNoteEntry(), state, getObject());
				}
			});

			menuBar_1.addItem("Prescription", (Command) null);

			menuBar.addItem("New", menuBar_1);

			final MenuBar menuBar_2 = new MenuBar(true);

			menuBar_2.addItem("Billing", (Command) null);

			menuBar_2.addItem("Trending", (Command) null);

			menuBar.addItem("Reporting", menuBar_2);
		}

		final VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel.add(verticalPanel_1);
		verticalPanel_1.setSize("100%", "100%");

		tabPanel = new TabPanel();
		verticalPanel_1.add(tabPanel);
		SimplePanel summary = new SimplePanel();
		tabPanel.add(summary, "Summary");
		tabPanel.selectTab(0);
	}

	/**
	 * Get patient tab panel.
	 * 
	 * @return
	 */
	public TabPanel getTabPanel() {
		return tabPanel;
	}

	/**
	 * Get reference to internal object.
	 * 
	 * @return
	 */
	protected PatientScreen getObject() {
		return this;
	}

	/**
	 * Set patient id.
	 */
	public void setPatient(Integer id) {
		patientId = id;

		if (Util.isStubbedMode()) {
			/**
			 * @gwt.typeArgs <java.lang.String,java.lang.String>
			 */
			HashMap dummy = new HashMap();
			dummy.put("patient_name", "Hackenbush, Hugo Z");
			dummy.put("id", id.toString());
			dummy.put("patient_id", "HUGO01");
			dummy.put("ptdob", "1979-08-10");
			dummy.put("address_line_1", "101 Evergreen Terrace");
			dummy.put("address_line_2", "");
			dummy.put("csz", "N Kilt Town, IL 00000");
			dummy.put("pthphone", "8005551212");
			dummy.put("ptwphone", "860KL51212");
			populatePatientInformation(dummy);
		} else {
			// Set off async method to get information
			PatientInterfaceAsync service = null;
			try {
				service = (PatientInterfaceAsync) Util
						.getProxy("org.freemedsoftware.gwt.client.Api.PatientInterface");
			} catch (Exception e) {
				GWT.log("Exception caught: ", e);
			}
			service.PatientInformation(patientId, new AsyncCallback() {
				public void onSuccess(Object result) {
					/**
					 * @gwt.typeArgs <java.lang.String, java.lang.String>
					 */
					HashMap pInfo = (HashMap) result;
					populatePatientInformation(pInfo);
				}

				public void onFailure(Throwable t) {
					GWT.log("FAILURE: ", t);
				}
			});
		}
	}

	/**
	 * 
	 * @param info
	 * @gwt.typeArgs <java.lang.String, java.lang.String>
	 */
	protected void populatePatientInformation(HashMap info) {
		// Store this in the object
		patientInfo = info;

		// Push out to child widgets
		patientInfoBar.setPatientFromMap(patientInfo);
	}
}
