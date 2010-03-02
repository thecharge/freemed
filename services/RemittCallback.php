<?php
 // $Id$
 //
 // Authors:
 //      Jeff Buchbinder <jeff@freemedsoftware.org>
 //
 // FreeMED Electronic Medical Record and Practice Management System
 // Copyright (C) 1999-2010 FreeMED Software Foundation
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

include_once( dirname( __FILE__ ) . '/../lib/freemed.php' );

$uri = parse_url( $_SERVER['REQUEST_URI'], PHP_URL_QUERY );

if ($uri == "wsdl" ) {
	Header( "Content-type: text/xml" );
	readfile( PHYSICAL_LOCATION . '/data/wsdl/RemittCallback.wsdl' );
	die();
}

$soap = new SoapServer(
	  PHYSICAL_LOCATION . '/data/wsdl/RemittCallback.wsdl'
	, array( 'uri' => $_SERVER['REQUEST_URI'] )
);
$soap->setClass( 'RemittCallback' );
$soap->handle( );

class RemittCallback {

	public function getProtocolVersion() {
		return 0.5;
	} // end method getProtocolVersion

	public function sendRemittancePayload( $payloadType, $originalReference, $payload ) {
		// TODO: handle payloadType properly. For now, assume 835 XML
		//$billkey = $originalReference;
		$parser = CreateObject( 'org.freemedsoftware.core.Parser_835XML', $payload );
		return $parser->Handle();
	} // end method sendRemittancePayload

} // end class RemittCallback

?>
