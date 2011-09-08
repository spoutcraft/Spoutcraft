package org.spoutcraft.spoutcraftapi;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author alta189
 *
 */
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
public @interface MPOnly {
	public String author() default "alta189";
	public String version() default "1.0";
	public String shortDescription() default "Indicates that the function or event only is used or runs when the client is in Multiplayer";

}
