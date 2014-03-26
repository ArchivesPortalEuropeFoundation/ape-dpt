package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

/*
 * #%L
 * Data Preparation Tool Standalone mapping tool
 * %%
 * Copyright (C) 2009 - 2014 Archives Portal Europe
 * %%
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */

import javax.swing.*;

/**
 * User: Yoann Moranville
 * Date: 25/04/2013
 *
 * @author Yoann Moranville
 */
public class EagScrollPaneHolder {
    private EagTextAreaScrollable eagTextAreaScrollable;
    private EagScrollPane eagScrollPane;

    public EagScrollPaneHolder(String title) {
        eagTextAreaScrollable = new EagTextAreaScrollable(title);
        eagScrollPane = new EagScrollPane(eagTextAreaScrollable);
    }
    public EagScrollPaneHolder() {
        new EagScrollPaneHolder("");
    }

    public EagScrollPane getScrollPane() {
        return eagScrollPane;
    }

    public String getText() {
        return eagTextAreaScrollable.getText();
    }
}
