<!--{* Smarty *}-->
<!--{*
 // $Id$
 //
 // Authors:
 //      Jeff Buchbinder <jeff@freemedsoftware.org>
 //
 // FreeMED Electronic Medical Record and Practice Management System
 // Copyright (C) 1999-2007 FreeMED Software Foundation
 //
 // This program is free software; you can redistribute it and/or modify
 // it under the terms of the GNU General Public License as published by
 // the Free Software Foundation; either version 2 of the License, or
 // (at your option) any later version.
 //
 // This program is distributed in the hope that it will be useful,
 // but WITHOUT ANY WARRANTY; without even the implied warranty of
 // MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 // GNU General Public License for more details.
 //
 // You should have received a copy of the GNU General Public License
 // along with this program; if not, write to the Free Software
 // Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*}-->

<!--{if $MODE ne 'widget'}-->
<!--{include file="org.freemedsoftware.ui.framework.tpl"}-->
<!--{/if}-->

<script type="text/javascript">
	dojo.require("dojo.event.*");
	dojo.require("dojo.widget.FilteringTable");
</script>

<script language="javascript">
	dojo.addOnLoad(function() {
		// Define selection event for table
		dojo.event.connect(dojo.widget.byId('tagSimpleTable'), "onSelect", function () {
			var val;
			if (dojo.widget.byId('tagSimpleTable').getSelectedData().length > 0) {
				dojo.debug("found getSelectedData()");
				val = dojo.widget.byId('tagSimpleTable').getSelectedData()[0].patient_record;
			}
			dojo.debug(dojo.json.serialize(dojo.widget.byId('tagSimpleTable').getSelectedData()[0]));
			if (val) {
				// Move to the patient EMR record in question
				dojo.widget.byId('tagSimpleTable').disable();
				freemedLoadPage('<!--{$base_uri}-->/controller.php/<!--{$ui}-->/org.freemedsoftware.controller.patient.overview?patient=' + val);
				return true;
			}
		});
		// Initial load of data for search.
		dojo.io.bind({
			method : 'POST',
			url: '<!--{$base_uri}-->/relay.php/json/org.freemedsoftware.module.PatientTag.SimpleTagSearch?param0=<!--{$tag}-->',
			error: function() { },
			load: function(type, data, evt) {
				if (data) {
					dojo.widget.byId('tagSimpleTable').store.setData(data);
				}
			},
			mimetype: "text/json"
		});
	});
</script>

<div class="searchHeader">
	<table border="0" cellpadding="0" cellspacing="0" width="98%"><tr>
	<td align="left"><b><!--{t}-->Tags found for<!--{/t}--></b>: "<!--{$tag|escape}-->"</td>
	<td align="right" style="padding-right: 10px;"><img src="<!--{$htdocs}-->/images/magnifying_glass.png" border="0" onClick="window.location='<!--{$base_uri}-->/controller.php/<!--{$ui}-->/org.freemedsoftware.ui.tag.searchform'; return true;" /></td>
	</tr></table>
</div>

<div class="tableContainer">
	<table dojoType="FilteringTable" id="tagSimpleTable" widgetId="tagSimpleTable" headClass="fixedHeader"
	 tbodyClass="scrollContent" enableAlternateRows="true" rowAlternateClass="alternateRow"
	 valueField="patient_record"
	 border="0" multiple="no">
	<thead>
		<tr>
			<th field="patient_id" dataType="String">Record</th>
			<th field="last_name" dataType="String">Last</th>
			<th field="first_name" dataType="String">First</th>
			<th field="middle_name" dataType="String">Middle</th>
			<th field="date_of_birth" dataType="String">DOB</th>
			<th field="last_seen" dataType="String">Last Seen</th>
		</tr>
	</thead>
	<tbody>
<!--{*
        * patient_record - Patient record id
        * patient_id - Practice ID for patient
        * last_seen - Date last seen/next appointment
        * first_name - First name of patient
        * last_name - Last name of patient
        * middle_name - Middle name of patient
        * date_of_birth - Patient's date of birth
*}-->
	</tbody>
	</table>
</div>

<!--{if $MODE ne 'widget'}-->
<!--{include file="org.freemedsoftware.ui.footer.tpl"}-->
<!--{/if}-->
