#!/usr/bin/perl
# $Id$
#
# Authors:
#      Jeff Buchbinder <jeff@freemedsoftware.org>
#
# FreeMED Electronic Medical Record and Practice Management System
# Copyright (C) 1999-2010 FreeMED Software Foundation
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

# Autodetect path for FreeMED
use FindBin;
use lib "$FindBin::Bin/../lib/perl";
my $rootpath = "$FindBin::Bin/..";

# Add proper libraries for XML-RPC access and configuration data
use Frontier::Client;
use Config::IniFiles;

# Get interval parameter
my $interval = shift || '';
my $date     = shift || '';

# Get link information from the configuration file. In future, this should
# probably be generated by the FreeMED install wizard as a special XML-RPC
# account, so internal processes can use the same configuration file.
my $config = new Config::IniFiles ( -file => $rootpath.'/data/config/xmlrpc.ini' );

# Connect to FreeMED installation, and run all ticklers
my $client = Frontier::Client->new (
	url => $config->val('freemed', 'url'),
	username => $config->val('freemed', 'username'),
	password => $config->val('freemed', 'password'),
	debug => $config->val('freemed', 'debug')
);
print "Calling FreeMED.Tickler.call ( $interval )\n";
my $result = $client->call('FreeMED.Tickler.call', {
	date => $date,
	interval => $interval
});
print($result); print "\n";

