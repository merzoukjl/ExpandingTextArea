package org.vaadin.hene.expandingtextarea.widgetset.client.ui;

import org.vaadin.hene.expandingtextarea.ExpandingTextArea;
import org.vaadin.hene.expandingtextarea.widgetset.client.ui.VExpandingTextArea.HeightChangedListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.communication.RpcProxy;
import com.vaadin.terminal.gwt.client.ui.Connect;
import com.vaadin.terminal.gwt.client.ui.textarea.TextAreaConnector;
import com.vaadin.terminal.gwt.client.ui.textarea.VTextArea;

@Connect(ExpandingTextArea.class)
public class ExpandingTextAreaConnector extends TextAreaConnector implements HeightChangedListener {
	
	private ExpandingTextAreaServerRpc rpc = RpcProxy.create(ExpandingTextAreaServerRpc.class, this);
	
    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (uidl.hasAttribute("maxRows")) {
            getWidget().setMaxRows(uidl.getIntAttribute("maxRows"));
        } else {
        	getWidget().setMaxRows(null);
        }

        getWidget().addStyleName(VTextArea.CLASSNAME);

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        	public void execute() {
        		getWidget().checkHeight();
        	}
        });
    }

    @Override
    protected VExpandingTextArea createWidget() {
    	VExpandingTextArea widget = GWT.create(VExpandingTextArea.class);
    	widget.addHeightChangedListener(this);
        return widget;
    }

    @Override
    public VExpandingTextArea getWidget() {
        return (VExpandingTextArea) super.getWidget();
    }

	@Override
	public void heightChanged(int newHeight) {
		rpc.setRows(newHeight);
		getLayoutManager().setNeedsMeasure(this);
	}

}
