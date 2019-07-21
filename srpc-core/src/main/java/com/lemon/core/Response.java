package com.lemon.core;

import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public interface Response {

    Long getRequestId();

    Exception getException();

    Object getResult();

}
