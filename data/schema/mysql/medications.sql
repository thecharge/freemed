# $Id$
#
# Authors:
#      Jeff Buchbinder <jeff@freemedsoftware.org>
#
# FreeMED Electronic Medical Record and Practice Management System
# Copyright (C) 1999-2006 FreeMED Software Foundation
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

SOURCE data/schema/mysql/patient.sql

CREATE TABLE IF NOT EXISTS `medications` (
	mdrug			VARCHAR (150),
	mdosage			VARCHAR (150),
	mroute			VARCHAR (150),
	mpatient		INT UNSIGNED NOT NULL DEFAULT 0,
	mdate			DATE,
	id			SERIAL,

	#	Define keys

	KEY			( mpatient, mdate ),
	FOREIGN KEY		( mpatient ) REFERENCES patient ( id ) ON DELETE CASCADE
) ENGINE=InnoDB;
