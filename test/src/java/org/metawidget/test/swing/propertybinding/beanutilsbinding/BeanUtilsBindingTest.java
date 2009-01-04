// Metawidget
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

package org.metawidget.test.swing.propertybinding.beanutilsbinding;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.apache.commons.beanutils.ConvertUtils;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.scala.ScalaPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.propertybinding.beanutils.BeanUtilsBinding;

/**
 * @author Richard Kennard
 */

public class BeanUtilsBindingTest
	extends TestCase
{
	//
	// Protected statics
	//

	protected final static String	DATE_FORMAT	= "dd/MM/yyyy";

	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public BeanUtilsBindingTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testScalaBinding()
		throws Exception
	{
		// Model

		ScalaFoo scalaFoo = new ScalaFoo();
		scalaFoo.bar_$eq( new GregorianCalendar( 1975, Calendar.APRIL, 9 ).getTime() );
		ScalaFoo scalaFoo2 = new ScalaFoo();
		scalaFoo.nestedFoo = scalaFoo2;
		ScalaFoo scalaFoo3 = new ScalaFoo();
		scalaFoo2.nestedFoo = scalaFoo3;
		scalaFoo3.bar_$eq( new GregorianCalendar( 1976, Calendar.MAY, 16 ).getTime() );

		// BeanUtilsBinding

		ConvertUtils.register( new org.metawidget.test.swing.allwidgets.converter.beanutils.DateConverter( DATE_FORMAT ), Date.class );

		// Inspect

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setPropertyBindingClass( BeanUtilsBinding.class );
		metawidget.setParameter( "propertyStyle", BeanUtilsBinding.PROPERTYSTYLE_SCALA );
		BaseObjectInspectorConfig config = new BaseObjectInspectorConfig();
		config.setPropertyStyle( ScalaPropertyStyle.class );
		metawidget.setInspector( new PropertyTypeInspector( config ) );
		metawidget.setToInspect( scalaFoo );

		// Loading

		JTextField textField = (JTextField) metawidget.getComponent( 1 );
		assertTrue( "09/04/1975".equals( textField.getText() ));
		JLabel label = (JLabel) metawidget.getComponent( 5 );
		assertTrue( "Not settable".equals( label.getText() ) );

		JTextField nestedTextField = (JTextField) ( (SwingMetawidget) metawidget.getComponent( 3 ) ).getComponent( 1 );
		assertTrue( "".equals( nestedTextField.getText() ));

		JTextField nestedNestedTextField = (JTextField) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 3 ) ).getComponent( 3 ) ).getComponent( 1 );
		assertTrue( "16/05/1976".equals( nestedNestedTextField.getText() ));

		// Saving

		textField.setText( "10/05/1976" );
		nestedNestedTextField.setText( "17/06/1977" );
		metawidget.save();

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime( scalaFoo.bar() );
		assertTrue( 1976 == calendar.get( Calendar.YEAR ));
		assertTrue( null == scalaFoo2.bar() );
		calendar.setTime( scalaFoo3.bar() );
		assertTrue( 1977 == calendar.get( Calendar.YEAR ));
	}

	//
	// Inner class
	//

	protected static class ScalaFoo
	{
		//
		// Private members
		//

		private Date		bar;

		private String		notSettable	= "Not settable";

		protected ScalaFoo	nestedFoo;

		//
		// Public methods
		//

		public Date bar()
		{
			return bar;
		}

		public void bar_$eq( Date theBar )
		{
			bar = theBar;
		}

		public String notSettable()
		{
			return notSettable;
		}

		public ScalaFoo nestedFoo()
		{
			return nestedFoo;
		}
	}
}
