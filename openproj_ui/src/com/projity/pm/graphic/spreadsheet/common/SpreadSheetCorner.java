/*
The contents of this file are subject to the Common Public Attribution License
Version 1.0 (the "License"); you may not use this file except in compliance with
the License. You may obtain a copy of the License at
http://www.projity.com/license . The License is based on the Mozilla Public
License Version 1.1 but Sections 14 and 15 have been added to cover use of
software over a computer network and provide for limited attribution for the
Original Developer. In addition, Exhibit A has been modified to be consistent
with Exhibit B.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the
specific language governing rights and limitations under the License. The
Original Code is OpenProj. The Original Developer is the Initial Developer and
is Projity, Inc. All portions of the code written by Projity are Copyright (c)
2006, 2007. All Rights Reserved. Contributors Projity, Inc.

Alternatively, the contents of this file may be used under the terms of the
Projity End-User License Agreeement (the Projity License), in which case the
provisions of the Projity License are applicable instead of those above. If you
wish to allow use of your version of this file only under the terms of the
Projity License and not to allow others to use your version of this file under
the CPAL, indicate your decision by deleting the provisions above and replace
them with the notice and other provisions required by the Projity  License. If
you do not delete the provisions above, a recipient may use your version of this
file under either the CPAL or the Projity License.

[NOTE: The text of this license may differ slightly from the text of the notices
in Exhibits A and B of the license at http://www.projity.com/license. You should
use the latest text at http://www.projity.com/license for your modifications.
You may not remove this license text from the source files.]

Attribution Information: Attribution Copyright Notice: Copyright � 2006, 2007
Projity, Inc. Attribution Phrase (not exceeding 10 words): Powered by OpenProj,
an open source solution from Projity. Attribution URL: http://www.projity.com
Graphic Image as provided in the Covered Code as file:  openproj_logo.png with
alternatives listed on http://www.projity.com/logo

Display of Attribution Information is required in Larger Works which are defined
in the CPAL as a work which combines Covered Code or portions thereof with code
not governed by the terms of the CPAL. However, in addition to the other notice
obligations, all copies of the Covered Code in Executable and Source Code form
distributed must, as a form of attribution of the original author, include on
each user interface screen the "OpenProj" logo visible to all users.  The
OpenProj logo should be located horizontally aligned with the menu bar and left
justified on the top left of the screen adjacent to the File menu.  The logo
must be at least 100 x 25 pixels.  When users click on the "OpenProj" logo it
must direct them back to http://www.projity.com.
*/
package com.projity.pm.graphic.spreadsheet.common;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.projity.configuration.Dictionary;
import com.projity.pm.graphic.spreadsheet.SpreadSheet;
import com.projity.pm.graphic.spreadsheet.selection.SpreadSheetColumnsPopupMenu;
import com.projity.pm.graphic.spreadsheet.selection.SpreadSheetSelectionModel;
import com.projity.pm.graphic.spreadsheet.selection.TimeSpreadSheetColumnsPopupMenu;
import com.projity.pm.graphic.spreadsheet.time.TimeSpreadSheet;
import com.projity.strings.Messages;
import com.projity.util.Environment;

/**
 *
 */
public class SpreadSheetCorner extends GradientCorner implements ListSelectionListener {
	protected CommonSpreadSheet spreadSheet;
	/**
	 *
	 */
	public SpreadSheetCorner(CommonSpreadSheet spreadSheet) {
		super();
//		this.setOpaque(true);
		if (spreadSheet.isCanSelectFieldArray())
			setToolTipText("dummy"); // needed so getToolTipText will be called
		this.spreadSheet=spreadSheet;
		if (spreadSheet instanceof SpreadSheet) spreadSheet.getRowHeader().getSelectionModel().addListSelectionListener(this);

//
//		setBackground(LafUtils.getUnselectedBackgroundColor());
		if (!Environment.isNewLaf())
			setBorder(new LineBorder(Color.LIGHT_GRAY));
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				CommonSpreadSheet spreadSheet=SpreadSheetCorner.this.spreadSheet;

				if  (SwingUtilities.isRightMouseButton(e) || (SwingUtilities.isLeftMouseButton(e) && e.isControlDown())) {
					if (spreadSheet instanceof CommonSpreadSheet && spreadSheet.getSpreadSheetCategory() != null){
						CommonSpreadSheet sp=(CommonSpreadSheet)spreadSheet;
						if (sp.isCanSelectFieldArray()) {
							//need to build menu each time because it can change
							if (spreadSheet instanceof TimeSpreadSheet){
								TimeSpreadSheetColumnsPopupMenu columnsPopup = new TimeSpreadSheetColumnsPopupMenu((TimeSpreadSheet)sp,sp.getSpreadSheetCategory());
								columnsPopup.show(sp,e.getX(),e.getY());
							}else{
								SpreadSheetColumnsPopupMenu columnsPopup = new SpreadSheetColumnsPopupMenu(sp,sp.getSpreadSheetCategory());
								columnsPopup.show(sp,e.getX(),e.getY());
							}
						}
					}
				}else{
					SpreadSheetSelectionModel selection=spreadSheet.getSelection();
					selection.getColumnSelection().setSelectionInterval(0,spreadSheet.getColumnCount()-1);
					selection.getRowSelection().setSelectionInterval(0,spreadSheet.getRowCount()-1);
					spreadSheet.getRowHeader().getSelectionModel().setSelectionInterval(0,spreadSheet.getRowCount()-1);
					if (Environment.isMac()) setSelected(true);
				}
			}
		});
	}


	public String getToolTipText(MouseEvent e) {
		return "<html>" + Dictionary.getCategoryText(spreadSheet.getSpreadSheetCategory()) + ": " + spreadSheet.getFieldArray() +
		"<br>" + Messages.getString("Text.rightClickSelectToSpreadsheet") + "<html>";
	}


	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel model=(ListSelectionModel)e.getSource();
		//if (!e.getValueIsAdjusting())
		//setSelected(model.getMinSelectionIndex()>=0);
		if (Environment.isMac()){
			if (selected) setSelected(false);
		}else setSelected(model.getMinSelectionIndex()>=0);
	}


}
