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

package org.metawidget.example.gwt.addressbook.client.ui;

import org.metawidget.example.gwt.addressbook.client.rpc.ContactsServiceAsync;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.gwt.client.binding.BindingAdapter;
import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;

/**
 * @author Richard Kennard
 */

public class ContactDialog
	extends DialogBox
{
	//
	//
	// Constructor
	//
	//

	public ContactDialog( final ContactsServiceAsync contactsService, final Contact contact )
	{
		setStyleName( "contact-dialog" );
		setPopupPosition( 100, 50 );
		Grid grid = new Grid( 1, 2 );
		setWidget( grid );

		// Left-hand image

		ColumnFormatter columnFormatter = grid.getColumnFormatter();
		Image image = new Image();
		columnFormatter.setStyleName( 0, "page-image" );
		grid.setWidget( 0, 0, image );

		// Metawidget

		final GwtMetawidget metawidget = new GwtMetawidget();
		metawidget.setReadOnly( contact.getId() != 0 );
		metawidget.setToInspect( contact );
		grid.setWidget( 0, 1, metawidget );
		columnFormatter.setStyleName( 1, "content" );

		// Binding

		@SuppressWarnings( "unchecked" )
		BindingAdapter<Contact> bindingAdapter = (BindingAdapter<Contact>) GWT.create( Contact.class );
		bindingAdapter.setAdaptee( contact );
		metawidget.setBinding( bindingAdapter );

		// Title

		StringBuilder builder = new StringBuilder( contact.getFullname() );

		if ( builder.length() > 0 )
			builder.append( " - " );

		// Personal/business icon

		if ( contact instanceof PersonalContact )
		{
			builder.append( "personalContact" );
			image.setUrl( "media/personal.gif" );
		}
		else
		{
			builder.append( "businessContact" );
			image.setUrl( "media/business.gif" );
		}

		setText( builder.toString() );

		// Embedded buttons

		Facet buttonsFacet = new Facet();
		buttonsFacet.setName( "buttons" );
		metawidget.add( buttonsFacet );

		HorizontalPanel panel = new HorizontalPanel();
		buttonsFacet.add( panel );

		final Button saveButton = new Button( "Save" );
		saveButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				contactsService.save( contact, new AsyncCallback<Object>()
				{
					public void onFailure( Throwable caught )
					{
						Window.alert( caught.getMessage() );
					}

					public void onSuccess( Object result )
					{
						ContactDialog.this.hide();
					}
				} );
			}
		} );
		panel.add( saveButton );

		final Button deleteButton = new Button( "Delete" );
		deleteButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				if ( Window.confirm( "Sure you want to delete this contact?" ) )
				{
					contactsService.delete( contact, new AsyncCallback<Boolean>()
					{
						public void onFailure( Throwable caught )
						{
							Window.alert( caught.getMessage() );
						}

						public void onSuccess( Boolean result )
						{
							ContactDialog.this.hide();
						}
					} );
				}
			}
		} );
		panel.add( deleteButton );

		saveButton.setVisible( !metawidget.isReadOnly() );
		deleteButton.setVisible( !metawidget.isReadOnly() && contact.getId() != 0 );

		final Button editButton = new Button( "Edit" );
		editButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				metawidget.setReadOnly( false );
				editButton.setVisible( false );
				saveButton.setVisible( true );
				deleteButton.setVisible( true );
				metawidget.buildWidgets();
			}
		} );
		editButton.setVisible( metawidget.isReadOnly() );
		panel.add( editButton );

		Button cancelButton = new Button( "Cancel" );
		cancelButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				ContactDialog.this.hide();
			}
		} );
		panel.add( cancelButton );

		metawidget.buildWidgets();
	}
}
