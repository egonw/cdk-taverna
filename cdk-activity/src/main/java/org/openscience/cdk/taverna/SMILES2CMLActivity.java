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
package org.openscience.cdk.taverna;

import java.util.HashMap;
import java.util.Map;

import net.sf.taverna.t2.annotation.annotationbeans.MimeType;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.ReferenceServiceException;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.EditException;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;
import net.sf.taverna.t2.workflowmodel.OutputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import org.apache.log4j.Logger;

/**
 * <p>
 * An Activity that holds a constant string value. It is automatically configured to have no input ports
 * and only one output port named <em>value</em>.<br>
 *
 * @author Stuart Owen
 *
 */
public class SMILES2CMLActivity extends AbstractAsynchronousActivity<NoParametersConfigurationBean>{

	private static final Logger logger = Logger.getLogger(SMILES2CMLActivity.class);
			
	private NoParametersConfigurationBean config=null;
	
	@Override
	public void configure(NoParametersConfigurationBean conf)
			throws ActivityConfigurationException {
		this.config=conf;
		if (inputPorts.size() == 0) {
			addInput("iodata-in", 0, true, null, String.class);
		}
		if (outputPorts.size() == 0) {
			addOutput("iodata-out", 0, "text/xml");
		}
	}

	public String getStringValue() {
		return "";
	}
	
	@Override
	public NoParametersConfigurationBean getConfiguration() {
		return config;
	}

	@Override
	public void executeAsynch(final Map<String, T2Reference> data,
			final AsynchronousActivityCallback callback) {
		callback.requestRun(new Runnable() {

			public void run() {
				ReferenceService referenceService = callback.getContext().getReferenceService();
				try {
					ActivityInputPort inputPort = getInputPort("iodata-in");
					Object input = referenceService.renderIdentifier(data
							.get("iodata-in"), inputPort
							.getTranslatedElementClass(), callback
							.getContext()); // should be a String

					// do the interesting CDK stuff here
					
					Map<String,T2Reference> outputData = new HashMap<String, T2Reference>();
					T2Reference id = referenceService.register(
					    null, // FIXME: replace null with actual results
					    0, true, callback.getContext()
					);
					outputData.put("iodata-out", id);
					callback.receiveResult(outputData, new int[0]);
				} catch (ReferenceServiceException e) {
					callback.fail(e.getMessage(),e);
				}
			}
			
		});
		
	}

	public ActivityInputPort getInputPort(String name) {
		for (ActivityInputPort port : getInputPorts()) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	protected void addOutput(String portName, int portDepth, String type) {
		OutputPort port = EditsRegistry.getEdits().createActivityOutputPort(
				portName, portDepth, portDepth);
		MimeType mimeType = new MimeType();
		mimeType.setText(type);
		try {
			EditsRegistry.getEdits().getAddAnnotationChainEdit(port, mimeType).doEdit();
		} catch (EditException e) {
			logger.debug("Error adding MimeType annotation to port", e);
		}
		outputPorts.add(port);
	}

}
