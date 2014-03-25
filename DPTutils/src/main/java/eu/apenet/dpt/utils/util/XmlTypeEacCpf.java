package eu.apenet.dpt.utils.util;

/**
 * The described entity in the file EAC-CPF
 *
 */
public enum XmlTypeEacCpf {
	EAC_CPF_PERSON(0, "person"),
	EAC_CPF_FAMILY(1, "family"),
	EAC_CPF_CORPORATEBODY(2, "corporateBody");

	private String name;
	private int identifier;
	
	XmlTypeEacCpf(int identifier, String name) {
        this.setIdentifier(identifier);
		this.setName(name);   
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	
}
