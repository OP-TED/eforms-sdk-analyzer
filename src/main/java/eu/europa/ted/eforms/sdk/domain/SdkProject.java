package eu.europa.ted.eforms.sdk.domain;

import org.apache.maven.model.Model;

/**
 * Information on the SDK from its pom.xml.
 * Just a read-only wrapper of the Maven Model, with getters for the info we need.
 */
public class SdkProject {
	private final Model mavenModel;

	public SdkProject(Model mavenModel) {
		this.mavenModel = mavenModel;
	}

	public String getVersion() {
		return mavenModel.getVersion();
	}
}
