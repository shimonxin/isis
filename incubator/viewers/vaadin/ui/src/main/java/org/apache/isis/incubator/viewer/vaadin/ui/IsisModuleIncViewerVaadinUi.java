/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.incubator.viewer.vaadin.ui;

import com.vaadin.flow.spring.annotation.EnableVaadin;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.apache.isis.incubator.viewer.vaadin.model.IsisModuleIncViewerVaadinModel;
import org.apache.isis.incubator.viewer.vaadin.ui.auth.LogoutHandlerVaa;
import org.apache.isis.incubator.viewer.vaadin.ui.auth.VaadinAuthenticationHandler;
import org.apache.isis.incubator.viewer.vaadin.ui.components.UiComponentFactoryVaa;
import org.apache.isis.incubator.viewer.vaadin.ui.components.blob.BlobFieldFactory;
import org.apache.isis.incubator.viewer.vaadin.ui.components.clob.ClobFieldFactory;
import org.apache.isis.incubator.viewer.vaadin.ui.components.markup.MarkupFieldFactory;
import org.apache.isis.incubator.viewer.vaadin.ui.components.other.FallbackFieldFactory;
import org.apache.isis.incubator.viewer.vaadin.ui.components.text.TextFieldFactory;
import org.apache.isis.viewer.common.model.IsisModuleViewerCommon;


@Configuration
@Import({
        // modules
        IsisModuleViewerCommon.class,
        IsisModuleIncViewerVaadinModel.class,

        // @Service's
        VaadinAuthenticationHandler.class,
        LogoutHandlerVaa.class,
        UiComponentFactoryVaa.class,
        
        // Component Factories
        BlobFieldFactory.class,
        ClobFieldFactory.class,
        MarkupFieldFactory.class,
        TextFieldFactory.class,
        FallbackFieldFactory.class,
        
})
@EnableVaadin("org.apache.isis.incubator.viewer.vaadin.ui") // scan for vaadin annotations
public class IsisModuleIncViewerVaadinUi {
}
