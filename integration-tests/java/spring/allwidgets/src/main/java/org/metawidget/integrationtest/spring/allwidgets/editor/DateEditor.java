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

package org.metawidget.integrationtest.spring.allwidgets.editor;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DateEditor
	extends PropertyEditorSupport {

	//
	// Private statics
	//

	private static final DateFormat	FORMAT	= new SimpleDateFormat( "E MMM dd HH:mm:ss z yyyy" );

	static {
		FORMAT.setLenient( false );
	}

	//
	// Public methods
	//

	@Override
	public String getAsText() {

		Object value = getValue();

		if ( value == null ) {
			return "";
		}

		synchronized ( FORMAT ) {
			return FORMAT.format( value );
		}
	}

	@Override
	public void setAsText( String text ) {

		if ( text == null || "".equals( text ) ) {
			setValue( null );
			return;
		}

		try {
			synchronized ( FORMAT ) {
				setValue( FORMAT.parse( text ) );
			}
		} catch ( ParseException e ) {
			throw new IllegalArgumentException( e );
		}
	}
}