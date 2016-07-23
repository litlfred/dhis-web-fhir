package org.intrahealth.dhis.fhir;
/*
 * Copyright (c) 2016, IntraHealth International
 * All rights reserved.
 * GPL v3
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.intrahealth.dhis.Processor;

/**                                                                                                                                                                                 
 * @author Carl Leitner <litlfred@gmail.com>
 */
public class BaseProcessor extends Processor { 
    FhirContext fctx;
    IParser xmlParser;
    IParser jsonParser;
    
    public BaseProcessor(ScriptLibrary sl) {
	super(sl,jslibs);
	setFhirContext();
        fctx.setParserErrorHandler(new StrictErrorHandler());
	xmlParser = fctx.newXmlParser();
	jsonParser = fctx.newJsonParser();
    }
    
    abstract protected void setFhirContext();
    
    abstract String public getResourceName();
    
    public String getOperationKey(String operation) {
	return getResourceName() + "_" + operation;
    }

    public Boolean hasOperation(String operation) {
	return hasScript(getOperationKey(operation));
    }

    public Boolean processOperation(HttpServletRequest http_request, Object dhis_request, String operation) {
	processScript(dhis_request,dhis_request,getOperationKey(operation));
    }
    
    public String toJsonString(BaseResource r) {
	return jsonParser.encodeResourceToString(r);
    }
    public String toJSONString(Bundle b) {
	return jsonParser.encodeBundleToString(b);
    }

    public String toXMLString(BaseResource r) {
	return xmlParser.encodeResourceToString(r);
    }
    public String toXMLString(Bundle b) {
	return xmlParser.encodeBundleToString(b);
    }
    


    public Bundle bundleFromXML(String r) {
	Object o = xmlParser.parse(r);
	if (! o instanceof Bundle) {
	    return null;
	}
	return o;
    }
    public Bundle bundleFromXML(Reader r) {
	Object o = xmlParser.parse(r);
	if (! o instanceof Bundle) {
	    return null;
	}
	return o;
    }
    public Bundle bundleFromJSON(JsonObject o) {
	return bundleFromJSON(o.toString());
    }
    public Bundle bundleFromJSON(String r) {
	Object o = jsonParser.parse(r);
	if (! o instanceof Bundle) {
	    return null;
	}
	return o;
    }
    public Bundle bundleFromJSON(Reader r) {
	Object o = jsonParser.parse(r);
	if (! o instanceof Bundle) {
	    return null;
	}
	return o;
    }

    public BaseResource resourceFromXML(String r) {
	Object o = xmlParser.parse(r);
	if (! o instanceof BaseResource) {
	    return null;
	}
	return o;
    }
    public BaseResource resourceFromXML(Reader r) {
	Object o = xmlParser.parse(r);
	if (! o instanceof BaseResource) {
	    return null;
	}
	return o;
    }
    public BaseResource resourceFromJSON(String r) {
	Object o = jsonParser.parse(r);
	if (! o instanceof BaseResource) {
	    return null;
	}
	return o;
    }
    public BaseResource resourceFromJSON(Reader r) {
	Object o = jsonParser.parse(r);
	if (! o instanceof BaseResource) {
	    return null;
	}
	return o;
    }
    public BaseResource resourceFromJSON(JsonObject o) {
	return resourceFromJSON(o.toString());
    }
    

}