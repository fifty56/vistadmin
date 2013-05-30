package org.vist.vistadmin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public aspect LoggerAspect {

	pointcut gtaBusinessLogging()
    //: execution(* init(..));
	: execution(public * org.vist.vistadmin..*.*(..) ) &&
    !execution(* set*(..)) &&
    !execution(* get*(..)) &&
    !execution(* compareTo(..)) &&        
    !execution(* is*(..)) &&  // getter for boolean properties
    !execution(public String toString()) &&
    !execution(* equals(..)) &&
    !execution(* hashCode(..)) &&
    !execution(* init(..));

	before() : gtaBusinessLogging() {
		Object[] args = thisJoinPoint.getArgs();
		if(thisJoinPoint.getTarget() != null) {
			Logger LOGGER = LoggerFactory.getLogger("trace." + thisJoinPoint.getTarget().getClass().getName());
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug(thisEnclosingJoinPointStaticPart.getSignature().getName() + " has been reached.");
				if(LOGGER.isTraceEnabled()) {
				    LOGGER.trace("Arguments:");
				    for(Object arg : args) {
                        if (arg != null) {
                            LOGGER.trace(arg.toString());
                        }
				    }
				}
			}
		}
    }

	after() : gtaBusinessLogging() {
		if(thisJoinPoint.getTarget() != null) {
			Logger LOGGER = LoggerFactory.getLogger("trace." + thisJoinPoint.getTarget().getClass().getName());
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug(thisEnclosingJoinPointStaticPart.getSignature().getName() + " has been left.");
			}
		}
	}
	
}
