/*******************************************************************************
 * Copyright (C) 2007 The University of Manchester   
 *               2009 Egon Willighagen <egon.willighagen@gmail.com>
 * 
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *    
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *    
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package org.openscience.cdk.taverna.views;

import java.awt.Frame;

import javax.swing.Action;

import org.openscience.cdk.taverna.SMILES2CMLActivity;
import org.openscience.cdk.taverna.NoParametersConfigurationBean;
import org.openscience.cdk.taverna.actions.SMILES2CMLActivityConfigurationAction;

import net.sf.taverna.t2.workbench.ui.actions.activity.HTMLBasedActivityContextualView;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

public class SMILES2CMLActivityContextualView extends HTMLBasedActivityContextualView<NoParametersConfigurationBean> {

	private static final long serialVersionUID = -553974533001808511L;

	public SMILES2CMLActivityContextualView(Activity<?> activity) {
		super(activity);
	}

	@Override
	protected String getViewTitle() {
		return "SMILES 2 CML Activity";
	}

	@Override
	protected String getRawTableRowsHtml() {
		String html = "";
		return html;
	}

	@Override
	public Action getConfigureAction(Frame owner) {
		return new SMILES2CMLActivityConfigurationAction((SMILES2CMLActivity)getActivity(),owner);
	}

}


