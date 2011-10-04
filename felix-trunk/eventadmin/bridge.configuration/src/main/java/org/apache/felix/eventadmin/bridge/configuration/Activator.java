/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.eventadmin.bridge.configuration;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The BundleActivator that will register an ConfigurationEventListener service
 * with the framework on start. Subsequently, ConfigurationEvents will be
 * bridged to available EventAdmin services (as per spec).
 * 
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
 */
public class Activator implements BundleActivator
{
    /**
     * This registers an ConfigurationEventListener service with the framework
     * that bridges ConfigurationEvents to the EventAdmin.
     * 
     * @param context
     *            The context to use
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(final BundleContext context) throws Exception
    {
        new ConfigurationEventToEventAdminBridge(context);
    }

    /**
     * Stop the bridging of ConfigurationEvents to the EventAdmin.
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(final BundleContext context) throws Exception
    {
        // Services are unregistered by the framework
    }
}
