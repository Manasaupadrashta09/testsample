package com.optum.datagrid.helloworld;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCacheManager;

/**
 * Provides various resources including a cache manager.
 * 
 * @author mupadras
 * 
 */
public class Resources {

    @Inject
    DistributedCache distributedCache;
    @Inject
    ReplicatedCache replicatedCache;
    

    @Produces
    Logger getLogger(InjectionPoint ip) {
        String category = ip.getMember().getDeclaringClass().getName();
        return Logger.getLogger(category);
    }

    @Produces
    RemoteCacheManager getCacheManager() throws Exception {
        return distributedCache.getCacheManager();
    }
    
    @Produces
    RemoteCacheManager getCacheManager1() throws Exception {
        return replicatedCache.getCacheManager();
    }

}
