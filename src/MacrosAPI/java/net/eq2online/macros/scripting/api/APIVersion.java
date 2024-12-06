/**
 * 
 */
package net.eq2online.macros.scripting.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for versioning API module classes
 * 
 * @author Adam Mummery-Smith
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface APIVersion
{
	int value();
}
