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
package org.apache.felix.ipojo.test.scenarios.temporal;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.apache.felix.ipojo.test.scenarios.temporal.service.CheckService;
import org.apache.felix.ipojo.test.scenarios.temporal.service.FooService;
import org.apache.felix.ipojo.test.scenarios.util.Utils;
import org.osgi.framework.ServiceReference;

public class TemporalTest extends OSGiTestCase {

   public void testDelay() {
       String prov = "provider";
       ComponentInstance provider = Utils.getComponentInstanceByName(context, "TEMPORAL-FooProvider", prov);
       String un = "under-1";
       ComponentInstance under = Utils.getComponentInstanceByName(context, "TEMPORAL-CheckServiceProviderUsingTemporal", un);

       ServiceReference ref_fs = Utils.getServiceReferenceByName(context, FooService.class.getName(), prov);
       assertNotNull("Check foo availability", ref_fs);

       ServiceReference ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability", ref_cs);

       CheckService cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation", cs.check());

       // Stop the provider.
       provider.stop();
       assertNull("No FooService", Utils.getServiceReference(context, FooService.class.getName(), null));
       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 2", ref_cs);
       long begin = System.currentTimeMillis();
       DelayedProvider dp = new DelayedProvider(provider, 200);
       dp.start();
       cs = (CheckService) context.getService(ref_cs);

       assertTrue("Check invocation - 2", cs.check());
       long end = System.currentTimeMillis();

       assertTrue("Assert delay (" + (end - begin) + ")", (end - begin) >= 200);

       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 3", ref_cs);
       cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation - 3", cs.check());

       provider.stop();
       provider.dispose();
       under.stop();
       under.dispose();
   }

   public void testDelayWithProxy() {
       String prov = "provider";
       ComponentInstance provider = Utils.getComponentInstanceByName(context, "TEMPORAL-FooProvider", prov);
       String un = "under-1";
       ComponentInstance under = Utils.getComponentInstanceByName(context, "TEMPORAL-ProxiedCheckServiceProviderUsingTemporal", un);

       ServiceReference ref_fs = Utils.getServiceReferenceByName(context, FooService.class.getName(), prov);
       assertNotNull("Check foo availability", ref_fs);

       ServiceReference ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability", ref_cs);

       CheckService cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation", cs.check());

       // Stop the provider.
       provider.stop();
       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 2", ref_cs);
       long begin = System.currentTimeMillis();
       DelayedProvider dp = new DelayedProvider(provider, 200);
       dp.start();
       cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation - 2", cs.check());
       long end = System.currentTimeMillis();

       assertTrue("Assert delay", (end - begin) >= 200);

       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 3", ref_cs);
       cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation - 3", cs.check());

       provider.stop();
       provider.dispose();
       under.stop();
       under.dispose();
   }


   public void testTimeout() {
       String prov = "provider";
       ComponentInstance provider = Utils.getComponentInstanceByName(context, "TEMPORAL-FooProvider", prov);
       String un = "under-1";
       ComponentInstance under = Utils.getComponentInstanceByName(context, "TEMPORAL-CheckServiceProviderUsingTemporal", un);

       ServiceReference ref_fs = Utils.getServiceReferenceByName(context, FooService.class.getName(), prov);
       assertNotNull("Check foo availability", ref_fs);

       ServiceReference ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability", ref_cs);

       CheckService cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation", cs.check());

       // Stop the provider.
       provider.stop();
       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 2", ref_cs);
       DelayedProvider dp = new DelayedProvider(provider, 4000);
       dp.start();
       cs = (CheckService) context.getService(ref_cs);
       try {
           cs.check();
       } catch(RuntimeException e) {
           // OK
           dp.stop();
           provider.stop();
           provider.dispose();
           under.stop();
           under.dispose();
           return;
       }

       fail("Timeout expected");
   }


   public void testTimeoutWithProxy() {
       String prov = "provider";
       ComponentInstance provider = Utils.getComponentInstanceByName(context, "TEMPORAL-FooProvider", prov);
       String un = "under-1";
       ComponentInstance under = Utils.getComponentInstanceByName(context, "TEMPORAL-ProxiedCheckServiceProviderUsingTemporal", un);

       ServiceReference ref_fs = Utils.getServiceReferenceByName(context, FooService.class.getName(), prov);
       assertNotNull("Check foo availability", ref_fs);

       ServiceReference ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability", ref_cs);

       CheckService cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation", cs.check());

       // Stop the provider.
       provider.stop();
       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 2", ref_cs);
       DelayedProvider dp = new DelayedProvider(provider, 4000);
       dp.start();
       cs = (CheckService) context.getService(ref_cs);
       try {
           cs.check();
       } catch(RuntimeException e) {
           // OK
           dp.stop();
           provider.stop();
           provider.dispose();
           under.stop();
           under.dispose();
           return;
       }

       fail("Timeout expected");
   }


   public void testDelayTimeout() {
       String prov = "provider";
       ComponentInstance provider = Utils.getComponentInstanceByName(context, "TEMPORAL-FooProvider", prov);
       String un = "under-1";
       ComponentInstance under = Utils.getComponentInstanceByName(context, "TEMPORAL-CheckServiceProviderTimeoutUsingTemporal", un);

       ServiceReference ref_fs = Utils.getServiceReferenceByName(context, FooService.class.getName(), prov);
       assertNotNull("Check foo availability", ref_fs);

       ServiceReference ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability", ref_cs);

       CheckService cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation", cs.check());

       // Stop the provider.
       provider.stop();
       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 2", ref_cs);
       long begin = System.currentTimeMillis();
       DelayedProvider dp = new DelayedProvider(provider, 200);
       dp.start();
       cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation - 2", cs.check());
       long end = System.currentTimeMillis();

       assertTrue("Assert delay", (end - begin) >= 200);

       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 3", ref_cs);
       cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation - 3", cs.check());

       provider.stop();
       provider.dispose();
       under.stop();
       under.dispose();
   }

   public void testDelayTimeoutWithProxy() {
       String prov = "provider";
       ComponentInstance provider = Utils.getComponentInstanceByName(context, "TEMPORAL-FooProvider", prov);
       String un = "under-1";
       ComponentInstance under = Utils.getComponentInstanceByName(context, "TEMPORAL-ProxiedCheckServiceProviderTimeoutUsingTemporal", un);

       ServiceReference ref_fs = Utils.getServiceReferenceByName(context, FooService.class.getName(), prov);
       assertNotNull("Check foo availability", ref_fs);

       ServiceReference ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability", ref_cs);

       CheckService cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation", cs.check());

       // Stop the provider.
       provider.stop();
       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 2", ref_cs);
       long begin = System.currentTimeMillis();
       DelayedProvider dp = new DelayedProvider(provider, 200);
       dp.start();
       cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation - 2", cs.check());
       long end = System.currentTimeMillis();

       assertTrue("Assert delay", (end - begin) >= 200);

       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 3", ref_cs);
       cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation - 3", cs.check());

       provider.stop();
       provider.dispose();
       under.stop();
       under.dispose();
   }

   public void testSetTimeout() {
       String prov = "provider";
       ComponentInstance provider = Utils.getComponentInstanceByName(context, "TEMPORAL-FooProvider", prov);
       String un = "under-1";
       ComponentInstance under = Utils.getComponentInstanceByName(context, "TEMPORAL-CheckServiceProviderTimeoutUsingTemporal", un);

       ServiceReference ref_fs = Utils.getServiceReferenceByName(context, FooService.class.getName(), prov);
       assertNotNull("Check foo availability", ref_fs);

       ServiceReference ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability", ref_cs);

       CheckService cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation", cs.check());

       // Stop the provider.
       provider.stop();
       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 2", ref_cs);
       DelayedProvider dp = new DelayedProvider(provider, 400);
       dp.start();
       cs = (CheckService) context.getService(ref_cs);
       try {
           cs.check();
       } catch(RuntimeException e) {
           // OK
           dp.stop();
           provider.stop();
           provider.dispose();
           under.stop();
           under.dispose();
           return;
       }

       fail("Timeout expected");
   }


   public void testSetTimeoutWithProxy() {
       String prov = "provider";
       ComponentInstance provider = Utils.getComponentInstanceByName(context, "TEMPORAL-FooProvider", prov);
       String un = "under-1";
       ComponentInstance under = Utils.getComponentInstanceByName(context, "TEMPORAL-ProxiedCheckServiceProviderTimeoutUsingTemporal", un);

       ServiceReference ref_fs = Utils.getServiceReferenceByName(context, FooService.class.getName(), prov);
       assertNotNull("Check foo availability", ref_fs);

       ServiceReference ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability", ref_cs);

       CheckService cs = (CheckService) context.getService(ref_cs);
       assertTrue("Check invocation", cs.check());

       // Stop the provider.
       provider.stop();
       ref_cs = Utils.getServiceReferenceByName(context, CheckService.class.getName(), un);
       assertNotNull("Check cs availability - 2", ref_cs);
       DelayedProvider dp = new DelayedProvider(provider, 400);
       dp.start();
       cs = (CheckService) context.getService(ref_cs);
       try {
           cs.check();
       } catch(RuntimeException e) {
           // OK
           dp.stop();
           provider.stop();
           provider.dispose();
           under.stop();
           under.dispose();
           return;
       }

       fail("Timeout expected");
   }
}
