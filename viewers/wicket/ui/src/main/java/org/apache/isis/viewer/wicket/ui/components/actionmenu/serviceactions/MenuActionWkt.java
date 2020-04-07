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
package org.apache.isis.viewer.wicket.ui.components.actionmenu.serviceactions;

import org.apache.wicket.markup.html.link.AbstractLink;

import org.apache.isis.core.metamodel.spec.feature.ObjectAction;
import org.apache.isis.viewer.common.model.action.MenuActionUiModel;
import org.apache.isis.viewer.common.model.link.ActionLinkFactory;
import org.apache.isis.viewer.common.model.object.ObjectUiModel;

class MenuActionWkt extends MenuActionUiModel<AbstractLink> {

    MenuActionWkt(
            final ActionLinkFactory<AbstractLink> actionLinkFactory,
            final String actionName,
            final ObjectUiModel serviceModel,
            final ObjectAction objectAction,
            final boolean isFirstInSection) {

        super(actionLinkFactory, actionName, serviceModel, objectAction, isFirstInSection); 
    }

}