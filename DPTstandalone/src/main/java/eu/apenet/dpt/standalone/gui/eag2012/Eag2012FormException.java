package eu.apenet.dpt.standalone.gui.eag2012;

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

/**
 * User: Yoann Moranville
 * Date: 20/11/2012
 *
 * @author Yoann Moranville
 */
public class Eag2012FormException extends Exception {
    public Eag2012FormException() {
        super();
    }

    public Eag2012FormException(String message, Throwable cause) {
        super(message, cause);
    }

    public Eag2012FormException(String message) {
        super(message);
    }

    public Eag2012FormException(Throwable cause) {
        super(cause);
    }
}
