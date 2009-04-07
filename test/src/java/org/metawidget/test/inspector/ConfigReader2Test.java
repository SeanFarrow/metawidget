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

package org.metawidget.test.inspector;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.metawidget.inspector.ConfigReader2;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;

/**
 * @author Richard Kennard
 */

public class ConfigReader2Test
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public ConfigReader2Test( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testReader()
		throws Exception
	{
		// Configure

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<point xmlns=\"urn:java:java.awt\">";
		xml += "		<location>";
		xml += "			<int>10</int>";
		xml += "			<int>20</int>";
		xml += "		</location>";
		xml += "	</point>";
		xml += "	<swingMetawidget xmlns=\"urn:java:org.metawidget.swing\">";
		xml += "		<widgetBuilder>";
		xml += "			<compositeWidgetBuilder xmlns=\"urn:java:org.metawidget.widgetbuilder.composite\" config=\"CompositeWidgetBuilderConfig\">";
		xml += "				<widgetBuilders>";
		xml += "					<list>";
		xml += "						<swingWidgetBuilder xmlns=\"urn:java:org.metawidget.swing.widgetbuilder\"/>";
		xml += "					</list>";
		xml += "				</widgetBuilders>";
		xml += "			</compositeWidgetBuilder>";
		xml += "		</widgetBuilder>";
		xml += "		<inspector>";
		xml += "			<compositeInspector xmlns=\"urn:java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
		xml += "				<inspectors>";
		xml += "					<list>";
		xml += "						<metawidgetAnnotationInspector xmlns=\"urn:java:org.metawidget.inspector.annotation\"/>";
		xml += "						<facesInspector xmlns=\"java:org.metawidget.inspector.faces\"/>";
		xml += "						<hibernateValidatorInspector xmlns=\"java:org.metawidget.inspector.hibernate.validator\"/>";
		xml += "						<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		xml += "							<propertyStyle>";
		xml += "								<class>org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle</class>";
		xml += "							</propertyStyle>";
		xml += "							<actionStyle>";
		xml += "								<class>org.metawidget.inspector.impl.actionstyle.metawidget.MetawidgetActionStyle</class>";
		xml += "							</actionStyle>";
		xml += "						</propertyTypeInspector>";
		xml += "						<jpaInspector xmlns=\"java:org.metawidget.inspector.jpa\"/>";
		xml += "						<jspAnnotationInspector xmlns=\"java:org.metawidget.inspector.jsp\"/>";
		xml += "						<springAnnotationInspector xmlns=\"java:org.metawidget.inspector.spring\"/>";
		xml += "						<strutsInspector xmlns=\"java:org.metawidget.inspector.struts\" config=\"StrutsInspectorConfig\">";
		xml += "							<inputStreams>";
		xml += "								<list>";
		xml += "									<resource>org/metawidget/test/inspector/struts/test-struts-config1.xml</resource>";
		xml += "									<resource>org/metawidget/test/inspector/struts/test-struts-config2.xml</resource>";
		xml += "								</list>";
		xml += "							</inputStreams>";
		xml += "						</strutsInspector>";
		xml += "						<strutsAnnotationInspector xmlns=\"java:org.metawidget.inspector.struts\"/>";
		xml += "						<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "							<inputStream>";
		xml += "								<resource>org/metawidget/example/swing/addressbook/metawidget-metadata.xml</resource>";
		xml += "							</inputStream>";
		xml += "						</xmlInspector>";
		xml += "						<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "							<inputStream>";
		xml += "								<resource>org/metawidget/example/swing/addressbook/metawidget-metadata.xml</resource>";
		xml += "							</inputStream>";
		xml += "						</xmlInspector>";
		xml += "					</list>";
		xml += "				</inspectors>";
		xml += "			</compositeInspector>";
		xml += "		</inspector>";
		xml += "		<name>";
		xml += "			<string>foo</string>";
		xml += "		</name>";
		xml += "		<opaque>";
		xml += "			<boolean>true</boolean>";
		xml += "		</opaque>";
		xml += "		<parameter>";
		xml += "			<string>a parameter</string>";
		xml += "			<int>1</int>";
		xml += "		</parameter>";
		xml += "		<parameter>";
		xml += "			<string>another parameter</string>";
		xml += "			<int>5</int>";
		xml += "		</parameter>";
		xml += "	</swingMetawidget>";
		xml += "	<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
		xml += "		<inspectors>";
		xml += "			<list>";
		xml += "				<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
		xml += "				<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>";
		xml += "				<java5Inspector xmlns=\"java:org.metawidget.inspector.java5\"/>";
		xml += "			</list>";
		xml += "		</inspectors>";
		xml += "	</compositeInspector>";
		xml += "</metawidget>";

		// New Point

		ConfigReader2 configReader = new ConfigReader2();
		/*Point point = (Point) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Point.class );
		assertTrue( 10 == point.x );
		assertTrue( 20 == point.y );

		// Existing Point

		point = new Point();
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), point );
		assertTrue( 10 == point.x );
		assertTrue( 20 == point.y );

		// SwingMetawidget

		SwingMetawidget metawidget1 = new SwingMetawidget();
		assertTrue( null == metawidget1.getName() );
		assertTrue( !metawidget1.isOpaque() );
		assertTrue( null == metawidget1.getParameter( "a parameter" ) );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1 );

		// Test

		assertTrue( "foo".equals( metawidget1.getName() ) );
		assertTrue( metawidget1.isOpaque() );
		assertTrue( 1 == (Integer) metawidget1.getParameter( "a parameter" ) );
		assertTrue( 5 == (Integer) metawidget1.getParameter( "another parameter" ) );

		// SwingMetawidget2

		SwingMetawidget metawidget2 = new SwingMetawidget();
		assertTrue( null == metawidget2.getName() );
		assertTrue( !metawidget2.isOpaque() );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget2 );

		// Test WidgetBuilder

		Field mixinField = SwingMetawidget.class.getDeclaredField( "mMetawidgetMixin" );
		mixinField.setAccessible( true );
		@SuppressWarnings( "unchecked" )
		MetawidgetMixin<JComponent, SwingMetawidget> mixin1 = (MetawidgetMixin<JComponent, SwingMetawidget>) mixinField.get( metawidget1 );
		@SuppressWarnings( "unchecked" )
		MetawidgetMixin<JComponent, SwingMetawidget> mixin2 = (MetawidgetMixin<JComponent, SwingMetawidget>) mixinField.get( metawidget2 );

		Field widgetBuilderField = BaseMetawidgetMixin.class.getDeclaredField( "mWidgetBuilder" );
		widgetBuilderField.setAccessible( true );
		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent, SwingMetawidget> compositeWidgetBuilder1 = (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilderField.get( mixin1 );
		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent, SwingMetawidget> compositeWidgetBuilder2 = (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilderField.get( mixin2 );

		assertTrue( compositeWidgetBuilder1 == compositeWidgetBuilder2 );

		Field widgetBuildersField = CompositeWidgetBuilder.class.getDeclaredField( "mWidgetBuilders" );
		widgetBuildersField.setAccessible( true );
		@SuppressWarnings( "unchecked" )
		WidgetBuilder<JComponent, SwingMetawidget>[] widgetBuilders = (WidgetBuilder<JComponent, SwingMetawidget>[]) widgetBuildersField.get( compositeWidgetBuilder1 );

		assertTrue( widgetBuilders.length == 1 );
		assertTrue( widgetBuilders[0] instanceof SwingWidgetBuilder );

		// Test Inspector

		Field inspectorField = BaseMetawidgetMixin.class.getDeclaredField( "mInspector" );
		inspectorField.setAccessible( true );
		CompositeInspector compositeInspector1 = (CompositeInspector) inspectorField.get( mixin1 );
		CompositeInspector compositeInspector2 = (CompositeInspector) inspectorField.get( mixin1 );

		assertTrue( compositeInspector1 == compositeInspector2 );

		Field inspectorsField = CompositeInspector.class.getDeclaredField( "mInspectors" );
		inspectorsField.setAccessible( true );
		Inspector[] inspectors = (Inspector[]) inspectorsField.get( compositeInspector1 );

		assertTrue( inspectors.length == 11 );
		assertTrue( inspectors[0] instanceof MetawidgetAnnotationInspector );
		assertTrue( inspectors[1] instanceof FacesInspector );
		assertTrue( inspectors[2] instanceof HibernateValidatorInspector );
		assertTrue( inspectors[3] instanceof PropertyTypeInspector );
		assertTrue( inspectors[4] instanceof JpaInspector );
		assertTrue( inspectors[5] instanceof JspAnnotationInspector );
		assertTrue( inspectors[6] instanceof SpringAnnotationInspector );
		assertTrue( inspectors[7] instanceof StrutsInspector );
		assertTrue( inspectors[8] instanceof StrutsAnnotationInspector );
		assertTrue( inspectors[9] instanceof XmlInspector );
		assertTrue( inspectors[10] instanceof XmlInspector );*/

		// Inspector

		Inspector inspector1 = (Inspector) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
		assertTrue( inspector1 instanceof CompositeInspector );

		Inspector inspector2 = (Inspector) configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
		assertTrue( inspector2 instanceof CompositeInspector );
		assertTrue( inspector1 == inspector2 );
	}

	public void testNoDefaultConstructor()
		throws Exception
	{
		// With config hint

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\"/>";
		xml += "</metawidget>";

		ConfigReader2 configReader = new ConfigReader2();

		try
		{
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "class org.metawidget.inspector.xml.XmlInspector does not have a default constructor. Did you mean config=\"XmlInspectorConfig\"?".equals( e.getMessage() ) );
		}

		// Without config hint

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<Class xmlns=\"java:java.lang\"/>";
		xml += "</metawidget>";

		try
		{
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Class.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "class java.lang.Class does not have a default constructor".equals( e.getMessage() ) );
		}
	}

	public void testUrl()
		throws Exception
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "		<inputStream>";
		xml += "			<url>http://foo.nowhere</url>";
		xml += "		</inputStream>";
		xml += "	</xmlInspector>";
		xml += "</metawidget>";

		ConfigReader2 configReader = new ConfigReader2();

		try
		{
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertTrue( "java.net.UnknownHostException: foo.nowhere".equals( e.getMessage() ) );
		}
	}
}
