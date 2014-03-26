package eu.apenet.dpt.standalone.gui.commons;

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
import java.awt.*;

/**
 * User: Yoann Moranville
 * Date: 05/12/2012
 *
 * @author Yoann Moranville
 */
public class ButtonTab extends JButton {
    public ButtonTab(String title, boolean translationBtn) {
        super(title);
        if(!translationBtn) {
            setBackground(new Color(97, 201, 237));
            setBorder(BorderFactory.createLineBorder(new Color(0, 162, 222), 3));
        } else {
            setBackground(new Color(120, 230, 250));
            setBorder(BorderFactory.createLineBorder(new Color(0, 190, 240), 3));
        }
        setPreferredSize(new Dimension(200, 25));
        setMinimumSize(new Dimension(200, 25));
        setToolTipText(title);
    }

    public ButtonTab(String title) {
        this(title, false);
    }
}
