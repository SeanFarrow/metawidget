// Metawidget (licensed under LGPL)
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.integrationtest.spring.quirks.model;

import org.metawidget.inspector.spring.UiSpringLookup;

/**
 * Models an entity that tests some Spring-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SpringQuirks {

	//
	// Private members
	//

	private String	mLookup;

	//
	// Public methods
	//

	@UiSpringLookup( value = "${lookup.items}", itemValue = "${'va'}lue", itemLabel = "label" )
	public String getLookup() {

		return mLookup;
	}

	public void setLookup( String lookup ) {

		mLookup = lookup;
	}
}
