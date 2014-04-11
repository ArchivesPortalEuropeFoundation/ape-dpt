package eu.apenet.dpt.standalone.gui.eacCpf;

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
 * Class for exceptions of EAC-CPF form.
 */
public class EacCpfFormException extends Exception {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -8724467760643976873L;

	/**
	 * Empty constructor.
	 */
	public EacCpfFormException() {
		super();
	}

	/**
	 * Constructor with message and cause.
	 *
	 * @param message
	 * @param cause
	 */
	public EacCpfFormException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor with message.
	 *
	 * @param message
	 */
	public EacCpfFormException(String message) {
		super(message);
	}

	/**
	 * Constructor with cause.
	 *
	 * @param cause
	 */
	public EacCpfFormException(Throwable cause) {
		super(cause);
	}
}
