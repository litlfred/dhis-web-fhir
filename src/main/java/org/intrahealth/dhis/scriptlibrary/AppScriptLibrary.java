package org.intrahealth.dhis.scriptlibrary;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.appmanager.App;
import org.hisp.dhis.appmanager.AppManager;
import org.hisp.dhis.appmanager.AppStatus;
import org.hisp.dhis.datavalue.DefaultDataValueService;
import org.hisp.dhis.render.DefaultRenderService;
import org.intrahealth.dhis.scriptlibrary.ScriptLibrary;
import org.intrahealth.dhis.scriptlibrary.ScriptLibraryJSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**                                                                                                                                                                                 
 * @author Carl Leitner <litlfred@gmail.com>
 */
public class AppScriptLibraryJSON implements ScriptLibrary {

    protected static final Log log = LogFactory.getLog( DefaultDataValueService.class );    
    private final ResourceLoader resourceLoader = new DefaultResourceLoader();    
    @Autowired
    private  AppManager appManager; //not sure how this gets set!
    protected Iterable<Resource> scriptLocations;


    //stolen from dhis-web/dhis-web-api/src/main/java/org/hisp/dhis/webapi/controller/AppController.java
    private Resource findResource( Iterable<Resource> locations, String resourceName )
        throws IOException
    {
        for ( Resource location : locations ) {
            Resource resource = location.createRelative( resourceName );
            if ( resource.exists() && resource.isReadable() ) {
                return resource;
            }
        }
        return null;
    }
    protected String app;
    protected String[] resources;
    protected String[] operations;
    protected String pathPrefix;
    public AppScriptLibrary(String app, String pathPrefix) {
	log.info("Init library for " + app + "  under " + pathPrefix);
	this.app = app;
	this.pathPrefix = pathPrefix;
	this.resources = resources;
	this.operations = operations;	
	this.scriptLocations =  = Lists.newArrayList(
            resourceLoader.getResource( "file:" + appManager.getAppFolderPath() + "/" + app + "/script-library/"  + pathPrefix + "/"),
            resourceLoader.getResource( "classpath*:/apps/" + app + "/script-library/"  + pathPrefix + "/" ),
            resourceLoader.getResource( "/apps/" + app + "/script-library/"  + pathPrefix + "/" )
	    );
    }

    protected JsonObject loadDependencies() {
        Iterable<Resource> locations = Lists.newArrayList(
            resourceLoader.getResource( "file:" + appManager.getAppFolderPath() + "/" + app + "/" ),
            resourceLoader.getResource( "classpath*:/apps/" + app + "/" )
            resourceLoader.getResource( "/apps/" + app + "/" )
        );
	try {
	    Resource manifest = findResource( locations, "manifest.webapp" );
	    JsonReader jsonReader = Json.createReader(new new InputStreamReader(manifest.getInputStream()));
	    JsonObject json = jsonReader.readObject();
	    jsonReader.close();
	    JsonObjects sl = json.get("script-library");
	    JsonObjects deps = sl.get("dependencies");
	    return deps;
	} catch (Exception e) {
	    Json.createObjectBuilder().build(); //return empty 
	}
	
    }

    public boolean containsScript(String name) {
	try {
	    Resource r = findResource(scriptLocations,name);
	} catch (IOException e) {
	    return false;
	}
	return (r != null);
    }

    public String retrieveSource(String name) 
	throws IOException 
    {
	Resource r = findResource(scriptLocations,name );
	if (r == null) {
	    throw IOException("Could not get " + name);
	}
	return  IOUtils.toString(r.getInputStream(), "UTF-8");
    }

    public String[] retrieveDependencies(String name) {	
	try {
	    Object[] rdeps = this.loadDependencies().getJsonArray(name);
	    ArrayList<String> deps = new ArrayList<String>();
	    for (Object o: rdeps) {
		if (o instanceof String) {
		    deps.add((String) o);
		}
	    }
	    return deps.toArray();
	} catch (Exception e) {
	    return new String[0];
	}

	
    }
    



}
