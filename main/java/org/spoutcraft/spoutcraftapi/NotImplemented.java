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
public @interface NotImplemented {
	public String author() default "alta189";
	public String version() default "1.0";
	public String shortDescription() default "Indicates that the function or event is implemented into Spoutcraft currently";
}
