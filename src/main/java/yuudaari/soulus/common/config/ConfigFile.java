package yuudaari.soulus.common.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigFile {

	/**
	 * The filename that this class will serialize to/deserialize from.
	 * If the string provided doesn't end with an extension, the default extension (json) is appended.
	 */
	String file ();

	/**
	 * The properties in the json where this class will serialize to/deserialize from, separated by `.`
	 */
	String path () default "";

	/**
	 * The folder that the file will be placed in
	 */
	String folder () default "";

	/**
	 * The ID of the Config instance this file should be serialized with
	 */
	String id ();
}
