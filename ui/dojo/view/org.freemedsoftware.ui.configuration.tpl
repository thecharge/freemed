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

<script language="javascript">

	var config = {
		corpus: [],
		vars: [],
		tabs: { 'mainTab': document.getElementById( 'mainTab_table' ) },
		appendItem: function( table, item ) {
			var tR = document.createElement( 'tr' );
			var tD1 = document.createElement( 'td' );
			var tD2 = document.createElement( 'td' );

			tD1.style.align = 'right';
			tD1.style.align = 'left';

			tD1.innerHTML = item.c_title;

			// Add to list of variables we have to deal with
			config.vars.push( item.c_option );

			switch ( item.c_type ) {
				case 'Number':
				var widget = document.createElement( 'input' );
				widget.id = item.c_option;
				widget.type = 'text';
				tD2.appendChild( widget );
				break;

				case 'YesNo': 
				var widget = document.createElement( 'select' );
				widget.id = item.c_option;
				widget.options.length = 0;
				widget.options[widget.options.length] = new Option( 'Yes', 1 );
				widget.options[widget.options.length] = new Option( 'No', 0 );
				tD2.appendChild( widget );
				break;

				default:
				alert('Unimplemented widget type ' + item.ctype + '!');
				return false;
				break;
			}

			tR.appendChild( tD1 );
			tR.appendChild( tD2 );
			table.appendChild( tR );
		},
		populateTabContainer: function( data ) {
			for (var i=0; i<data.length; i++) {
				var item = data[i];

				// Figure out tab, do we have to make it?
				var tabId = item.c_section.replace(' ', '');
				var tableId = tabId + '_table';

				// FIXME: this hack is here until building dynamic tags works properly...
				tabId = 'mainTab';
				tableId = 'mainTab_table';

				if ( typeof config.tabs[tabId] == 'undefined' ) {
					var tC = dojo.widget.byId( 'configTabContainer' );
					var contentPane = dojo.widget.createWidget( 'dojo.widget.ContentPane', { id: tabId, widgetId: tabId } );
					tC.appendChild( contentPane );
					var t = document.createElement( 'table' );
					contentPane.setContent( t );
					
					config.tabs[tabId] = 1;
				}

				var p = document.getElementById( tableId );
				config.appendItem( p, item );
			}
		},
		init: function () {
			dojo.io.bind({
				method: 'POST',
				content: { },
				url: '<!--{$relay}-->/org.freemedsoftware.api.SystemConfig.GetAll',
				load: function( type, data, evt ) {
					config.corpus = data;
					config.populateTabContainer( data );
				},
				mimetype: 'text/json'
			});
		},
		onCommit: function ( ) {
			alert('stub: onCommit');
		}
	};

	_container_.addOnLoad(function(){
		config.init();
		dojo.event.connect( dojo.widget.byId( 'configCommitButton' ), 'onClick', config, 'onCommit' );
	});
	_container_.addOnUnload(function(){
		dojo.event.disconnect( dojo.widget.byId( 'configCommitButton' ), 'onClick', config, 'onCommit' );
	});

</script>

<h3><!--{t}-->System Configuration<!--{/t}--></h3>

<div dojoType="TabContainer" widgetId="configTabContainer" style="height: 85%; width: 100%;">

	<div dojoType="ContentPane" label="System" widgetId="mainTab">
		<table id="mainTab_table"></table>
	</div>

</div>

<div align="center">
	<table border="0" style="width: auto;">
		<tr>
			<td align="right">
				<button dojoType="Button" type="button" id="configCommitButton" widgetId="configCommitButton">
					<div><!--{t}-->Commit Changes<!--{/t}--></div>
				</button>
			</td>
			<td align="left">
				<button dojoType="Button" type="button" id="configCancelButton" widgetId="configCancelButton">
					<div><!--{t}-->Cancel<!--{/t}--></div>
				</button>
			</td>
	
		</tr>
	</table>
</div>
