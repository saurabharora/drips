/*
 * Copyright 2008 Drips project Owners http://code.google.com/p/drips/
 * Saurabh Arora <saurabh.arora@gmail.com> And Prasen Mukherjee <prasen.bea@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drips.framework.aspects.broadcastingaspect;

import org.drips.framework.aspects.AbstractBroadcastingAspect;
import org.drips.framework.aspects.AbstractBroadcastingAspectMBean;

/**
 * @author Prasen Mukherjee
 */
public interface MyBroadcastingAspectMBean extends AbstractBroadcastingAspectMBean{

    String enableMyAspect();
}
