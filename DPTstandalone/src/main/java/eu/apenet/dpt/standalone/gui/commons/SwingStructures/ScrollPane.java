package eu.apenet.dpt.standalone.gui.commons.SwingStructures;

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
public class ScrollPane extends JScrollPane {
    private TextAreaScrollable textAreaScrollable;

    public ScrollPane(TextAreaScrollable textAreaScrollable) {
        super(textAreaScrollable);
        this.textAreaScrollable = textAreaScrollable;
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public String getText() {
        return textAreaScrollable.getText();
    }
}
