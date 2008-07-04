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

import org.freemedsoftware.gwt.client.Util;
import org.freemedsoftware.gwt.client.Module.MedicationsAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

public class RecentMedicationsList extends Composite {

	protected Integer patientId = new Integer(0);

	protected CustomSortableTable medicationsTable;
	
	public RecentMedicationsList() {
		medicationsTable = new CustomSortableTable();
		initWidget(medicationsTable);
	}

	public void setPatientId(Integer id) {
		patientId = id;
		populate();
	}

	protected void populate() {
		if (Util.isStubbedMode()) {

		} else {
			getProxy().GetMostRecent(patientId, new AsyncCallback() {
				public void onSuccess(Object o) {
					/**
					 * @gwt.typeArgs <java.lang.String,java.lang.String>
					 */
					HashMap[] m = (HashMap[]) o;
					medicationsTable.clear();
					for (int iter=0; iter< m.length;iter++) {
						
					}
				}

				public void onFailure(Throwable t) {

				}
			});
		}
	}

	/**
	 * Internal method to retrieve proxy object from Util.getProxy()
	 * 
	 * @return
	 */
	protected MedicationsAsync getProxy() {
		MedicationsAsync p = null;
		try {
			p = (MedicationsAsync) Util
					.getProxy("org.freemedsoftware.gwt.client.Module.Medications");
		} catch (Exception ex) {
			GWT.log("Exception", ex);
		}
		return p;
	}

}
