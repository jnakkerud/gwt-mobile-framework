/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.linker;

import com.google.gwt.core.ext.LinkerContext;
//import com.google.gwt.core.ext.SelectionProperty;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.AbstractLinker;
import com.google.gwt.core.ext.linker.Artifact;
import com.google.gwt.core.ext.linker.ArtifactSet;
import com.google.gwt.core.ext.linker.CompilationResult;
import com.google.gwt.core.ext.linker.EmittedArtifact;
import com.google.gwt.core.ext.linker.LinkerOrder;
import com.google.gwt.core.ext.linker.SelectionProperty;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author jonnakkerud
 */
@LinkerOrder(LinkerOrder.Order.POST)
public class ManifestLinker extends AbstractLinker {

    List<String> exclusions = new ArrayList<String>();

    public static String MODULE = "example";
    public static String MANIFEST_TEMPLATE = "manifest.tm";


    @Override
    public String getDescription() {
        return "Manifest Linker";
    }

    @Override
    public ArtifactSet link(TreeLogger logger, LinkerContext context, ArtifactSet artifacts) throws UnableToCompleteException {
        ArtifactSet artifactSet = new ArtifactSet(artifacts);

        // load Template
        String user_dir = System.getProperty("user.dir");
        String template = null;
        try {
            template = FileUtils.readFileToString(new File(user_dir + "/" + MANIFEST_TEMPLATE));
        } catch (IOException ex) {
            logger.log(TreeLogger.Type.ERROR, "unable to read template " + user_dir + "/" + MANIFEST_TEMPLATE);
        }

        StringBuilder buf = new StringBuilder();
        SortedSet<CompilationResult> comp_set = artifacts.find(CompilationResult.class);
        for (CompilationResult artifact : comp_set) {
            if (contains(artifact, "mobilesafari") == false) {
                exclusions.add(artifact.getStrongName());
            }
        }

        boolean has_artifacts = (comp_set != null && comp_set.size() > 0);
        for (Artifact artifact : artifacts) {
            //System.out.println(artifact.getClass().getName());
            if (artifact instanceof EmittedArtifact) {
                String partialPath = ((EmittedArtifact) artifact).getPartialPath();
                if (!((EmittedArtifact) artifact).isPrivate() && !exclude(partialPath)) {
                    buf.append(MODULE + "/" + partialPath).append("\n");
                }
            }
        }

        // write buf to template
        HashMap m = new HashMap();
        m.put("ARTIFACTS", buf.toString());
        String manifest_str = replaceTokens(template, "%", m);
        //System.out.println(manifest_str);
        try {
            if (has_artifacts) {
                FileUtils.writeStringToFile(new File(user_dir + "/war/app.manifest"), manifest_str);
            }
        } catch (IOException ex) {
            logger.log(TreeLogger.Type.ERROR, "unable to write template " + user_dir + "/war/app.manifest");
        }

        return artifactSet;
    }

    boolean contains(CompilationResult artifact, String value) {
        for (SortedMap<SelectionProperty, String> map : artifact.getPropertyMap()) {
            for (Map.Entry<SelectionProperty, String> entry : map.entrySet()) {
                if (entry.getValue().equals(value)) {
                    return true;

                }
            }
        }
        return false;
    }

    private boolean exclude(String partialPath) {
        boolean result = false;
        for (String s : exclusions) {
            if (partialPath.startsWith(s)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     *
     * replaceTokens("My name is %name%. What is yours?", "%", [name=Fred]);
     * will return
     * My name is Fred. What is yours?
     *
     * @param str The string to search through
     * @param delim The token delimiters
     * @param hmKeys The keys to replace tokens with
     */
    public static String replaceTokens(String str, String delim, HashMap hmKeys) {
        EnhancedStringTokenizer st = new EnhancedStringTokenizer(str, delim, false);
        StringBuilder output = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            //category.info("token:" + token);
            output.append(token);
            if (st.hasMoreTokens()) {
                String key = st.nextToken();

                if (hmKeys.containsKey(key)) {
                    output.append(hmKeys.get(key));
                } else {
                    output.append(delim);
                    output.append(key);
                    output.append(delim);
                }
            }
        }

        return output.toString();
    }

}
