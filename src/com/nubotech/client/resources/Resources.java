/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.nubotech.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ExternalTextResource;

/**
 * A ResourceBundle that holds the references to the resources used by
 * GwtFeedReader.
 */
public interface Resources extends ClientBundle {

    public static final Resources INSTANCE = (Resources) GWT.create(Resources.class);

    /**
     * The non-structural CSS rules.
     */
    @Source("appearance.css")
    public AppearanceCss appearanceCss();

    @Source("sectionBg.png")
    public DataResource group();

    @Source("on.png")
    public DataResource on();

    @Source("off.png")
    public DataResource off();

    @Source("slider_left.png")
    public DataResource sliderLeft();

    @Source("slider_right.png")
    public DataResource sliderRight();

    @Source("slider_center.png")
    public DataResource sliderCenter();

    @Source("example1.htm")
    ExternalTextResource example1Text();

    @Source("example2.htm")
    ExternalTextResource example2Text();

}
